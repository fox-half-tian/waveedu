package com.zhulang.waveedu.judge.judge;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.JudgeDTO;
import com.zhulang.waveedu.judge.judge.entity.JudgeGlobalDTO;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.judge.task.DefaultJudge;
import com.zhulang.waveedu.judge.util.Constants;
import com.zhulang.waveedu.judge.util.ThreadPoolUtils;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 11:30
 * @Description: 判题流程解耦重构3.0，该类负责输入数据进入程序进行测评
 */
@Component
public class JudgeRun {

    @Resource
    private DefaultJudge defaultJudge;

    @Resource
    private LanguageConfigLoader languageConfigLoader;

    /**
     * 将问题的所有示例输入放到用户代码的编译后文件中运行
     *
     * @param problemLimitInfo         问题信息
     * @param judgeLanguage   使用的语言
     * @param testCasesDir    案例目录
     * @param testCasesInfo   案例信息
     * @param userFileId      编译文件id
     * @param userFileContent 用户代码
     * @return
     * @throws SystemError
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<JSONObject> judgeAllCase(ProblemLimitInfoDTO problemLimitInfo,
                                         String judgeLanguage,
                                         String testCasesDir,
                                         JSONObject testCasesInfo,
                                         String userFileId,
                                         String userFileContent) throws SystemError, ExecutionException, InterruptedException {



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
        Long testTime = (long) (problemLimitInfo.getTimeLimit() + 200);


        final AbstractJudge abstractJudge = defaultJudge;

        LanguageConfig runConfig = languageConfigLoader.getLanguageConfigByName(judgeLanguage);

        JudgeGlobalDTO judgeGlobalDTO = JudgeGlobalDTO.builder()
                // 问题id
                .problemId(problemLimitInfo.getId())
                //编译文件id
                .userFileId(userFileId)
                // 用户的代码
                .userFileContent(userFileContent)
                // 规定该问题的最大测试限制时间：2200ms
                .testTime(testTime)
                // 规定该问题的最大内存限制：18
                .maxMemory((long) problemLimitInfo.getMemoryLimit())
                // 问题最大耗时实际时间：2000ms
                .maxTime((long) problemLimitInfo.getTimeLimit())
                // 问题最大栈限制：128
                .maxStack(problemLimitInfo.getStackLimit())
                // 问题的案例信息
                .testCaseInfo(testCasesInfo)
                // 语言配置信息
                .runConfig(runConfig)
                // 移除末尾空白
                .removeEolBlank(true)
                .build();

            // 顺序评测测试点，遇到非AC就停止！
            return ergodicJudgeAllCase(testcaseList, testCasesDir, judgeGlobalDTO, abstractJudge);
    }


    /**
     * 顺序评测，遇到非AC就停止评测
     *
     * @param testcaseList 测试案例信息列表
     * @param testCasesDir 测试案例文件所在目录
     * @param judgeGlobalDTO 判题信息
     * @param abstractJudge 模式
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
            final String testCaseInputPath = testCasesDir + "/" + inputFileName;
            // 题目数据的输出文件的路径
            final String testCaseOutputPath = testCasesDir + "/" + outputFileName;
            // 数据库表的测试样例id
            final Long caseId = testcase.getLong("caseId", null);

            final Long maxOutputSize = Math.max(testcase.getLong("outputSize", 0L) * 2, 32 * 1024 * 1024L);

            JudgeDTO judgeDTO = JudgeDTO.builder()
                    .testCaseNum(testCaseId)
                    .testCaseInputFileName(inputFileName)
                    .testCaseInputPath(testCaseInputPath)
                    .testCaseOutputFileName(outputFileName)
                    .testCaseOutputPath(testCaseOutputPath)
                    .maxOutputSize(maxOutputSize)
                    .build();

            JSONObject judgeRes = SubmitTask2ThreadPool(new FutureTask<>(() -> {
                JSONObject result = abstractJudge.judge(judgeDTO, judgeGlobalDTO);
                result.set("caseId", caseId);
                result.set("inputFileName", judgeDTO.getTestCaseInputFileName());
                result.set("outputFileName", judgeDTO.getTestCaseOutputFileName());
                result.set("seq", judgeDTO.getTestCaseNum());
                return result;
            }));
            // 把运行与校验的结果放入到集合中
            judgeResList.add(judgeRes);
            Integer status = judgeRes.getInt("status");
            // 如果程序出错，就直接退出校验
            if (!Constants.Judge.STATUS_ACCEPTED.getStatus().equals(status)) {
                break;
            }
        }
        return judgeResList;
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
                // 避免CPU高速运转，这里休息10毫秒
                Thread.sleep(10);
            }
        }
    }

}