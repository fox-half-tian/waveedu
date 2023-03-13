package com.zhulang.waveedu.judge.judge;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.dto.TestJudgeReq;
import com.zhulang.waveedu.judge.dto.TestJudgeRes;
import com.zhulang.waveedu.judge.entity.problem.Problem;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.JudgeDTO;
import com.zhulang.waveedu.judge.judge.entity.JudgeGlobalDTO;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.judge.task.DefaultJudge;
import com.zhulang.waveedu.judge.judge.task.InteractiveJudge;
import com.zhulang.waveedu.judge.judge.task.SpecialJudge;
import com.zhulang.waveedu.judge.judge.task.TestJudge;
import com.zhulang.waveedu.judge.util.Constants;
import com.zhulang.waveedu.judge.util.JudgeUtils;
import com.zhulang.waveedu.judge.util.ThreadPoolUtils;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: Himit_ZH
 * @Date: 2021/4/16 12:15
 * @Description: 判题流程解耦重构3.0，该类负责输入数据进入程序进行测评
 */
@Component
public class JudgeRun {

    @Resource
    private DefaultJudge defaultJudge;

    @Resource
    private SpecialJudge specialJudge;

    @Resource
    private InteractiveJudge interactiveJudge;

    @Resource
    private TestJudge testJudge;

    @Resource
    private LanguageConfigLoader languageConfigLoader;

    /**
     * 将问题的所有示例输入放到用户代码的编译后文件中运行
     *
     * @param submitId 提交id
     * @param problem 问题信息
     * @param judgeLanguage 使用的语言
     * @param testCasesDir 案例目录
     * @param testCasesInfo 案例信息
     * @param userFileId 编译文件id
     * @param userFileContent 用户代码
     * @param getUserOutput false
     * @param judgeCaseMode 模式 default
     * @return
     * @throws SystemError
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<JSONObject> judgeAllCase(Long submitId,
                                         Problem problem,
                                         String judgeLanguage,
                                         String testCasesDir,
                                         JSONObject testCasesInfo,
                                         String userFileId,
                                         String userFileContent,
                                         Boolean getUserOutput,
                                         String judgeCaseMode)
            throws SystemError, ExecutionException, InterruptedException {

        if (testCasesInfo == null) {
            throw new SystemError("The evaluation data of the problem does not exist", null, null);
        }

        /*
        testcaseList
           0.   caseId -> {Integer@11686} 1
                inputName -> 1.in
                outputName -> 1.out
                outputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                outputSize -> {Integer@11686} 1
                allStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                EOFStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5

           1.   caseId -> 2
                inputName -> 2.in
                outputName -> 2.out
                outputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                outputSize -> 1
                allStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                EOFStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
         */
        JSONArray testcaseList = (JSONArray) testCasesInfo.get("testCases");

        // 默认给题目限制时间+200ms用来测评
        Long testTime = (long) problem.getTimeLimit() + 200;

        // default
        Constants.JudgeMode judgeMode = Constants.JudgeMode.getJudgeMode(problem.getJudgeMode());

        if (judgeMode == null) {
            throw new RuntimeException("The judge mode of problem " + problem.getProblemId() + " error:" + problem.getJudgeMode());
        }

        // 用户输出的文件夹：/judge/run/14
//        String runDir = Constants.JudgeDir.RUN_WORKPLACE_DIR.getContent() + File.separator + submitId;
        String runDir = Constants.JudgeDir.RUN_WORKPLACE_DIR.getContent() + "/" + submitId;

        LanguageConfig runConfig = languageConfigLoader.getLanguageConfigByName(judgeLanguage);
        LanguageConfig spjConfig = languageConfigLoader.getLanguageConfigByName("SPJ-" + problem.getSpjLanguage());
        LanguageConfig interactiveConfig = languageConfigLoader.getLanguageConfigByName("INTERACTIVE-" + problem.getSpjLanguage());

        final AbstractJudge abstractJudge = getAbstractJudge(judgeMode);

        JudgeGlobalDTO judgeGlobalDTO = JudgeGlobalDTO.builder()
                .problemId(problem.getId()) // 问题id
                .judgeMode(judgeMode) //default
                .userFileId(userFileId) //编译文件id
                .userFileContent(userFileContent) // 用户代码
                .runDir(runDir) // 用户输出文件夹：/judge/run/14
                .testTime(testTime) // 问题的最大测试限制时间：2200ms
                .maxMemory((long) problem.getMemoryLimit()) // 问题的最大内存限制：18
                .maxTime((long) problem.getTimeLimit()) // 问题最大耗时实际时间：2000ms
                .maxStack(problem.getStackLimit()) // 问题最大栈限制：128
                .testCaseInfo(testCasesInfo) // 问题的案例信息
                .judgeExtraFiles(JudgeUtils.getProblemExtraFileMap(problem, "judge")) // 为空
                .runConfig(runConfig)  // 语言配置信息
                .spjRunConfig(spjConfig) // 为空
                .interactiveRunConfig(interactiveConfig) // 为空
                .needUserOutputFile(getUserOutput) // false
                .removeEOLBlank(problem.getIsRemoveEndBlank()) //空白
                .build();


        // OI题的subtask最低分模式，则每个subtask组只要有一个case非AC 或者 percentage为 0.0则该组剩余评测点跳过，不再评测
        if (Constants.Contest.TYPE_OI.getCode().equals(problem.getType())
                && Constants.JudgeCaseMode.SUBTASK_LOWEST.getMode().equals(judgeCaseMode)) {
            return subtaskJudgeAllCase(testcaseList, testCasesDir, judgeGlobalDTO, abstractJudge);
        } else if (Constants.JudgeCaseMode.ERGODIC_WITHOUT_ERROR.getMode().equals(judgeCaseMode)){
            // 顺序评测测试点，遇到非AC就停止！
            return ergodicJudgeAllCase(testcaseList, testCasesDir, judgeGlobalDTO, abstractJudge);
        } else {
            return defaultJudgeAllCase(testcaseList, testCasesDir, judgeGlobalDTO, abstractJudge);
        }
    }

    /**
     * 默认会评测全部的测试点数据
     * @param testcaseList
     * @param testCasesDir 测试示例目录，比如 /judge/test_case/problem_1000
     * @param judgeGlobalDTO
     * @param abstractJudge
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private List<JSONObject> defaultJudgeAllCase(JSONArray testcaseList,
                                                 String testCasesDir,
                                                 JudgeGlobalDTO judgeGlobalDTO,
                                                 AbstractJudge abstractJudge) throws ExecutionException, InterruptedException {
        List<FutureTask<JSONObject>> futureTasks = new ArrayList<>();
        /*
           0.   caseId -> 1
                inputName -> 1.in
                outputName -> 1.out
                outputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                outputSize -> {Integer@11686} 1
                allStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                EOFStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5

           1.   caseId -> 2
                inputName -> 2.in
                outputName -> 2.out
                outputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                outputSize -> 1
                allStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                EOFStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
         */
        for (int index = 0; index < testcaseList.size(); index++) {
            JSONObject testcase = (JSONObject) testcaseList.get(index);
            // 将每个需要测试的线程任务加入任务列表中
            final int testCaseId = index + 1;
            // 输入文件名
            final String inputFileName = testcase.getStr("inputName");
            // 输出文件名
            final String outputFileName = testcase.getStr("outputName");
            // 题目数据的输入文件的路径
            final String testCaseInputPath = testCasesDir +"/" + inputFileName;
            // 题目数据的输出文件的路径
            final String testCaseOutputPath = testCasesDir + "/"+ outputFileName;
            // 数据库表的测试样例id
            final Long caseId = testcase.getLong("caseId", null);
            // 该测试点的满分
//            final Integer score = testcase.getInt("score", 0);
            // 该测试点的分组（用于subtask）
//            final Integer groupNum = testcase.getInt("groupNum", 1);

            final Long maxOutputSize = Math.max(testcase.getLong("outputSize", 0L) * 2, 32 * 1024 * 1024L);

            JudgeDTO judgeDTO = JudgeDTO.builder()
                    .testCaseNum(testCaseId)
                    .testCaseInputFileName(inputFileName)
                    .testCaseInputPath(testCaseInputPath)
                    .testCaseOutputFileName(outputFileName)
                    .testCaseOutputPath(testCaseOutputPath)
                    .maxOutputSize(maxOutputSize)
                    .build();

            futureTasks.add(new FutureTask<>(() -> {
                JSONObject result = abstractJudge.judge(judgeDTO, judgeGlobalDTO);
                result.set("caseId", caseId);
                result.set("score", judgeDTO.getScore());
                // 1.in
                result.set("inputFileName", judgeDTO.getTestCaseInputFileName());
                // 1.out
                result.set("outputFileName", judgeDTO.getTestCaseOutputFileName());
                // 当前是测试第几个案例
                result.set("seq", testCaseId);
                return result;
            }));

        }
        return SubmitBatchTask2ThreadPool(futureTasks);
    }

    /**
     * 顺序评测，遇到非AC就停止评测
     * @param testcaseList
     * @param testCasesDir
     * @param judgeGlobalDTO
     * @param abstractJudge
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private List<JSONObject> ergodicJudgeAllCase(JSONArray testcaseList,
                                                 String testCasesDir,
                                                 JudgeGlobalDTO judgeGlobalDTO,
                                                 AbstractJudge abstractJudge) throws ExecutionException, InterruptedException {
        List<JSONObject> judgeResList = new ArrayList<>();
        for (int index = 0; index < testcaseList.size(); index++) {
            JSONObject testcase = (JSONObject) testcaseList.get(index);
            // 将每个需要测试的线程任务加入任务列表中
            final int testCaseId = index + 1;
            // 输入文件名
            final String inputFileName = testcase.getStr("inputName");
            // 输出文件名
            final String outputFileName = testcase.getStr("outputName");
            // 题目数据的输入文件的路径
            final String testCaseInputPath = testCasesDir + File.separator + inputFileName;
            // 题目数据的输出文件的路径
            final String testCaseOutputPath = testCasesDir + File.separator + outputFileName;
            // 数据库表的测试样例id
            final Long caseId = testcase.getLong("caseId", null);
            // 该测试点的满分
            final Integer score = testcase.getInt("score", 0);
            // 该测试点的分组（用于subtask）
            final Integer groupNum = testcase.getInt("groupNum", 1);

            final Long maxOutputSize = Math.max(testcase.getLong("outputSize", 0L) * 2, 32 * 1024 * 1024L);

            JudgeDTO judgeDTO = JudgeDTO.builder()
                    .testCaseNum(testCaseId)
                    .testCaseInputFileName(inputFileName)
                    .testCaseInputPath(testCaseInputPath)
                    .testCaseOutputFileName(outputFileName)
                    .testCaseOutputPath(testCaseOutputPath)
                    .maxOutputSize(maxOutputSize)
                    .score(score)
                    .build();

            JSONObject judgeRes = SubmitTask2ThreadPool(new FutureTask<>(() -> {
                JSONObject result = abstractJudge.judge(judgeDTO, judgeGlobalDTO);
                result.set("caseId", caseId);
                result.set("score", judgeDTO.getScore());
                result.set("inputFileName", judgeDTO.getTestCaseInputFileName());
                result.set("outputFileName", judgeDTO.getTestCaseOutputFileName());
                result.set("groupNum", groupNum);
                result.set("seq", judgeDTO.getTestCaseNum());
                return result;
            }));
            judgeResList.add(judgeRes);
            Integer status = judgeRes.getInt("status");
            if (!Constants.Judge.STATUS_ACCEPTED.getStatus().equals(status)){
                break;
            }
        }
        return judgeResList;
    }

    /**
     * 根据测试点的groupNum进行分组，每组按顺序评测，遇到非AC有评测点得分为0分，不再评测该组剩余的测试点
     * @param testcaseList
     * @param testCasesDir
     * @param judgeGlobalDTO
     * @param abstractJudge
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private List<JSONObject> subtaskJudgeAllCase(JSONArray testcaseList,
                                                 String testCasesDir,
                                                 JudgeGlobalDTO judgeGlobalDTO,
                                                 AbstractJudge abstractJudge) throws ExecutionException, InterruptedException {
        Map<Integer, List<JudgeDTO>> judgeDTOMap = new LinkedHashMap<>();
        for (int index = 0; index < testcaseList.size(); index++) {
            JSONObject testcase = (JSONObject) testcaseList.get(index);
            // 将每个需要测试的线程任务加入任务列表中
            final int testCaseId = index + 1;
            // 输入文件名
            final String inputFileName = testcase.getStr("inputName");
            // 输出文件名
            final String outputFileName = testcase.getStr("outputName");
            // 题目数据的输入文件的路径
            final String testCaseInputPath = testCasesDir + File.separator + inputFileName;
            // 题目数据的输出文件的路径
            final String testCaseOutputPath = testCasesDir + File.separator + outputFileName;
            // 数据库表的测试样例id
            final Long caseId = testcase.getLong("caseId", null);
            // 该测试点的满分
            final Integer score = testcase.getInt("score", 0);
            // 该测试点的分组（用于subtask）
            final Integer groupNum = testcase.getInt("groupNum", 1);

            final Long maxOutputSize = Math.max(testcase.getLong("outputSize", 0L) * 2, 32 * 1024 * 1024L);

            JudgeDTO judgeDTO = JudgeDTO.builder()
                    .testCaseNum(testCaseId)
                    .testCaseInputFileName(inputFileName)
                    .testCaseInputPath(testCaseInputPath)
                    .testCaseOutputFileName(outputFileName)
                    .testCaseOutputPath(testCaseOutputPath)
                    .maxOutputSize(maxOutputSize)
                    .score(score)
                    .problemCaseId(caseId)
                    .build();
            List<JudgeDTO> judgeDTOList = judgeDTOMap.get(groupNum);
            if (judgeDTOList == null) {
                judgeDTOList = new ArrayList<>();
                judgeDTOList.add(judgeDTO);
                judgeDTOMap.put(groupNum, judgeDTOList);
            } else {
                judgeDTOList.add(judgeDTO);
            }
        }

        List<JSONObject> judgeResList = new ArrayList<>();
        for (Map.Entry<Integer, List<JudgeDTO>> entry : judgeDTOMap.entrySet()) {
            Integer groupNum = entry.getKey();
            Iterator<JudgeDTO> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                JudgeDTO judgeDTO = iterator.next();
                JSONObject judgeRes = SubmitTask2ThreadPool(new FutureTask<>(() -> {
                    JSONObject result = abstractJudge.judge(judgeDTO, judgeGlobalDTO);
                    result.set("caseId", judgeDTO.getProblemCaseId());
                    result.set("score", judgeDTO.getScore());
                    result.set("inputFileName", judgeDTO.getTestCaseInputFileName());
                    result.set("outputFileName", judgeDTO.getTestCaseOutputFileName());
                    result.set("groupNum", groupNum);
                    result.set("seq", judgeDTO.getTestCaseNum());
                    return result;
                }));
                judgeResList.add(judgeRes);
                Integer status = judgeRes.getInt("status");
                Double percentage = judgeRes.getDouble("percentage");
                if (!Constants.Judge.STATUS_ACCEPTED.getStatus().equals(status)
                        && !(Constants.Judge.STATUS_PARTIAL_ACCEPTED.getStatus().equals(status)
                        && percentage != null && percentage > 0.0)) {
                    // 有评测点得分为0分，不再评测该组其他测试点
                    while (iterator.hasNext()) {
                        JudgeDTO elseJudgeDTO = iterator.next();
                        JSONObject elseJudgeRes = new JSONObject();
                        elseJudgeRes.set("status", Constants.Judge.STATUS_CANCELLED.getStatus());
                        elseJudgeRes.set("memory", 0);
                        elseJudgeRes.set("time", 0);
                        elseJudgeRes.set("errMsg", "Cancelled: Skipped Judging");
                        elseJudgeRes.set("caseId", elseJudgeDTO.getProblemCaseId());
                        elseJudgeRes.set("score", elseJudgeDTO.getScore());
                        elseJudgeRes.set("inputFileName", elseJudgeDTO.getTestCaseInputFileName());
                        elseJudgeRes.set("outputFileName", elseJudgeDTO.getTestCaseOutputFileName());
                        elseJudgeRes.set("groupNum", groupNum);
                        elseJudgeRes.set("seq", judgeDTO.getTestCaseNum());
                        judgeResList.add(elseJudgeRes);
                    }
                    break;
                }
            }
        }
        return judgeResList;
    }

    /**
     * 运行自测评测单个测试点（由接口传入 输入与输出的数据）
     * @param userFileId
     * @param testJudgeReq
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public TestJudgeRes testJudgeCase(String userFileId, TestJudgeReq testJudgeReq) throws ExecutionException, InterruptedException {

        // 默认给限制时间+200ms用来测评
        Long testTime = testJudgeReq.getTimeLimit() + 200L;

        LanguageConfig runConfig = languageConfigLoader.getLanguageConfigByName(testJudgeReq.getLanguage());

        JudgeGlobalDTO judgeGlobalDTO = JudgeGlobalDTO.builder()
                .judgeMode(Constants.JudgeMode.TEST)
                .userFileId(userFileId)
                .userFileContent(testJudgeReq.getCode())
                .testTime(testTime)
                .maxMemory((long) testJudgeReq.getMemoryLimit())
                .maxTime((long) testJudgeReq.getTimeLimit())
                .maxStack(testJudgeReq.getStackLimit())
                .removeEOLBlank(testJudgeReq.getIsRemoveEndBlank())
                .runConfig(runConfig)
                .build();

        Long maxOutputSize = Math.max(testJudgeReq.getTestCaseInput().length() * 2L, 32 * 1024 * 1024L);

        JudgeDTO judgeDTO = JudgeDTO.builder()
                .testCaseInputContent(testJudgeReq.getTestCaseInput() + "\n")
                .maxOutputSize(maxOutputSize)
                .testCaseOutputContent(testJudgeReq.getExpectedOutput())
                .build();

        FutureTask<JSONObject> testJudgeFutureTask = new FutureTask<>(() -> testJudge.judge(judgeDTO, judgeGlobalDTO));

        JSONObject judgeRes = SubmitTask2ThreadPool(testJudgeFutureTask);
        return TestJudgeRes.builder()
                .status(judgeRes.getInt("status"))
                .memory(judgeRes.getLong("memory"))
                .time(judgeRes.getLong("time"))
                .stdout(judgeRes.getStr("output"))
                .stderr(judgeRes.getStr("errMsg"))
                .build();
    }

    private AbstractJudge getAbstractJudge(Constants.JudgeMode judgeMode) {
        switch (judgeMode) {
            case DEFAULT:
                return defaultJudge;
            case SPJ:
                return specialJudge;
            case INTERACTIVE:
                return interactiveJudge;
            default:
                throw new RuntimeException("The problem judge mode is error:" + judgeMode);
        }
    }

    private JSONObject SubmitTask2ThreadPool(FutureTask<JSONObject> futureTask)
            throws InterruptedException, ExecutionException {
        // 提交到线程池进行执行
        ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        while (true) {
            if (futureTask.isDone() && !futureTask.isCancelled()) {
                // 获取线程返回结果
                return futureTask.get();
            } else {
                Thread.sleep(10); // 避免CPU高速运转，这里休息10毫秒
            }
        }
    }

    private List<JSONObject> SubmitBatchTask2ThreadPool(List<FutureTask<JSONObject>> futureTasks)
            throws InterruptedException, ExecutionException {
        // 提交到线程池进行执行
        for (FutureTask<JSONObject> futureTask : futureTasks) {
            ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        }
        List<JSONObject> result = new LinkedList<>();
        while (futureTasks.size() > 0) {
            Iterator<FutureTask<JSONObject>> iterable = futureTasks.iterator();
            //遍历一遍
            while (iterable.hasNext()) {
                FutureTask<JSONObject> future = iterable.next();
                if (future.isDone() && !future.isCancelled()) {
                    // 获取线程返回结果
                    JSONObject tmp = future.get();
                    result.add(tmp);
                    // 任务完成移除任务
                    iterable.remove();
                } else {
                    Thread.sleep(10); // 避免CPU高速运转，这里休息10毫秒
                }
            }
        }
        return result;
    }

}