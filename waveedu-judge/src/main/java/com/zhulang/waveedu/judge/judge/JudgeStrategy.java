package com.zhulang.waveedu.judge.judge;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhulang.waveedu.judge.dto.TestJudgeReq;
import com.zhulang.waveedu.judge.dto.TestJudgeRes;
import com.zhulang.waveedu.judge.entity.judge.Judge;
import com.zhulang.waveedu.judge.entity.judge.JudgeCase;
import com.zhulang.waveedu.judge.entity.problem.Problem;
import com.zhulang.waveedu.judge.exception.CompileError;
import com.zhulang.waveedu.judge.exception.SubmitError;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.judge.entity.Pair_;
import com.zhulang.waveedu.judge.util.Constants;
import com.zhulang.waveedu.judge.util.JudgeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Slf4j(topic = "hoj")
@Component
public class JudgeStrategy {

    @Resource
    private JudgeEntityService judgeEntityService;

    @Resource
    private ProblemTestCaseUtils problemTestCaseUtils;

    @Resource
    private JudgeCaseEntityService JudgeCaseEntityService;

    @Resource
    private LanguageConfigLoader languageConfigLoader;

    @Resource
    private JudgeRun judgeRun;

    public HashMap<String, Object> judge(Problem problem, Judge judge) {

        HashMap<String, Object> result = new HashMap<>();
        // 编译好的临时代码文件id
        String userFileId = null;
        try {
            // 对用户源代码进行编译 获取tmpfs中的fileId
            // 有的语言可能不支持编译, 目前有js、php不支持编译，则 languageConfig 的值为
            LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName(judge.getLanguage());

            if (languageConfig.getCompileCommand() != null) {
                // 对用户的代码进行编译，获取到编译后的文件的id
                userFileId = Compiler.compile(languageConfig,
                        judge.getCode(),
                        judge.getLanguage()
                );
            }
            // 指定测试数据文件所在文件夹：/judge/test_case/problem_1000   1000是问题的id
            String testCasesDir = Constants.JudgeDir.TEST_CASE_DIR.getContent() + "/problem_" + problem.getId();
            // 从文件中加载测试数据json
            /*
            testCasesInfo：
                0. mode -> default
                1. version -> 0
                2. judgeCaseMode -> default
                3. testCasesSize：testCasesSize -> 2
                4. testCases：testCases ->
                        4.1 caseId -> {Integer@11686} 1
                            inputName -> 1.in
                            outputName -> 1.out
                            outputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                            outputSize -> {Integer@11686} 1
                            allStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                            EOFStrippedOutputMd5 -> e4da3b7fbbce2345d7772b0674a318d5

                        4.2 caseId -> 2
                            inputName -> 2.in
                            outputName -> 2.out
                            outputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                            outputSize -> 1
                            allStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
                            EOFStrippedOutputMd5 -> a87ff679a2f3e71d9181a67b7542122c
             */
            JSONObject testCasesInfo = problemTestCaseUtils.loadTestCaseInfo(problem.getId(),
                    testCasesDir,
                    problem.getCaseVersion(),
                    problem.getJudgeMode(),
                    problem.getJudgeCaseMode());


            // 获取题目数据的评测模式
            // default
            String infoJudgeCaseMode = testCasesInfo.getStr("judgeCaseMode", Constants.JudgeCaseMode.DEFAULT.getMode());
            // default
            String judgeCaseMode = getFinalJudgeCaseMode(problem.getType(), problem.getJudgeCaseMode(), infoJudgeCaseMode);

            // 开始测试每个测试点
            List<JSONObject> allCaseResultList = judgeRun.judgeAllCase(judge.getSubmitId(),
                    problem,
                    judge.getLanguage(),
                    testCasesDir,
                    testCasesInfo,
                    userFileId,
                    judge.getCode(),
                    false,
                    judgeCaseMode);

            // 对全部测试点结果进行评判,获取最终评判结果
            return getJudgeInfo(allCaseResultList, problem, judge, judgeCaseMode);
        } catch (SystemError systemError) {
            result.put("code", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
            result.put("errMsg", "Oops, something has gone wrong with the judgeServer. Please report this to administrator.");
            result.put("time", 0);
            result.put("memory", 0);
            log.error("[Judge] [System Error] Submit Id:[{}] Problem Id:[{}], Error:[{}]",
                    judge.getSubmitId(),
                    problem.getId(),
                    systemError);
        } catch (SubmitError submitError) {
            result.put("code", Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus());
            result.put("errMsg", mergeNonEmptyStrings(submitError.getMessage(), submitError.getStdout(), submitError.getStderr()));
            result.put("time", 0);
            result.put("memory", 0);
            log.error("[Judge] [Submit Error] Submit Id:[{}] Problem Id:[{}], Error:[{}]",
                    judge.getSubmitId(),
                    problem.getId(),
                    submitError);
        } catch (CompileError compileError) {
            result.put("code", Constants.Judge.STATUS_COMPILE_ERROR.getStatus());
            result.put("errMsg", mergeNonEmptyStrings(compileError.getStdout(), compileError.getStderr()));
            result.put("time", 0);
            result.put("memory", 0);
        } catch (Exception e) {
            result.put("code", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
            result.put("errMsg", "Oops, something has gone wrong with the judgeServer. Please report this to administrator.");
            result.put("time", 0);
            result.put("memory", 0);
            log.error("[Judge] [System Runtime Error] Submit Id:[{}] Problem Id:[{}], Error:[{}]",
                    judge.getSubmitId(),
                    problem.getId(),
                    e);
        } finally {

            // 删除tmpfs内存中的用户代码可执行文件
            if (!StringUtils.isEmpty(userFileId)) {
                SandboxRun.delFile(userFileId);
            }
        }
        return result;
    }


    private Boolean checkOrCompileExtraProgram(Problem problem) throws CompileError, SystemError {

        Constants.JudgeMode judgeMode = Constants.JudgeMode.getJudgeMode(problem.getJudgeMode());

        String currentVersion = problem.getCaseVersion();

        LanguageConfig languageConfig;

        String programFilePath;

        String programVersionPath;

        switch (judgeMode) {
            case DEFAULT:
                return true;
            case SPJ:
                languageConfig = languageConfigLoader.getLanguageConfigByName("SPJ-" + problem.getSpjLanguage());

                programFilePath = Constants.JudgeDir.SPJ_WORKPLACE_DIR.getContent() + File.separator +
                        problem.getId() + File.separator + languageConfig.getExeName();

                programVersionPath = Constants.JudgeDir.SPJ_WORKPLACE_DIR.getContent() + File.separator +
                        problem.getId() + File.separator + "version";

                // 如果不存在该已经编译好的程序，则需要再次进行编译
                if (!FileUtil.exist(programFilePath) || !FileUtil.exist(programVersionPath)) {
                    boolean isCompileSpjOk = Compiler.compileSpj(problem.getSpjCode(), problem.getId(), problem.getSpjLanguage(),
                            JudgeUtils.getProblemExtraFileMap(problem, "judge"));

                    FileWriter fileWriter = new FileWriter(programVersionPath);
                    fileWriter.write(currentVersion);
                    return isCompileSpjOk;
                }

                FileReader spjVersionReader = new FileReader(programVersionPath);
                String recordSpjVersion = spjVersionReader.readString();

                // 版本变动也需要重新编译
                if (!currentVersion.equals(recordSpjVersion)) {
                    boolean isCompileSpjOk = Compiler.compileSpj(problem.getSpjCode(), problem.getId(), problem.getSpjLanguage(),
                            JudgeUtils.getProblemExtraFileMap(problem, "judge"));
                    FileWriter fileWriter = new FileWriter(programVersionPath);
                    fileWriter.write(currentVersion);
                    return isCompileSpjOk;
                }

                break;
            case INTERACTIVE:
                languageConfig = languageConfigLoader.getLanguageConfigByName("INTERACTIVE-" + problem.getSpjLanguage());
                programFilePath = Constants.JudgeDir.INTERACTIVE_WORKPLACE_DIR.getContent() + File.separator +
                        problem.getId() + File.separator + languageConfig.getExeName();

                programVersionPath = Constants.JudgeDir.INTERACTIVE_WORKPLACE_DIR.getContent() + File.separator +
                        problem.getId() + File.separator + "version";

                // 如果不存在该已经编译好的程序，则需要再次进行编译 版本变动也需要重新编译
                if (!FileUtil.exist(programFilePath) || !FileUtil.exist(programVersionPath)) {
                    boolean isCompileInteractive = Compiler.compileInteractive(problem.getSpjCode(), problem.getId(), problem.getSpjLanguage(),
                            JudgeUtils.getProblemExtraFileMap(problem, "judge"));
                    FileWriter fileWriter = new FileWriter(programVersionPath);
                    fileWriter.write(currentVersion);
                    return isCompileInteractive;
                }

                FileReader interactiveVersionFileReader = new FileReader(programVersionPath);
                String recordInteractiveVersion = interactiveVersionFileReader.readString();

                // 版本变动也需要重新编译
                if (!currentVersion.equals(recordInteractiveVersion)) {
                    boolean isCompileInteractive = Compiler.compileSpj(problem.getSpjCode(), problem.getId(), problem.getSpjLanguage(),
                            JudgeUtils.getProblemExtraFileMap(problem, "judge"));

                    FileWriter fileWriter = new FileWriter(programVersionPath);
                    fileWriter.write(currentVersion);

                    return isCompileInteractive;
                }

                break;
            default:
                throw new RuntimeException("The problem mode is error:" + judgeMode);
        }

        return true;
    }

    // 获取判题的运行时间，运行空间，OI得分
    public HashMap<String, Object> computeResultInfo(List<JudgeCase> allTestCaseResultList,
                                                     Boolean isACM,
                                                     Integer errorCaseNum,
                                                     Integer totalScore,
                                                     Integer problemDifficulty,
                                                     String judgeCaseMode) {
        HashMap<String, Object> result = new HashMap<>();
        // 用时和内存占用保存为多个测试点中最长的
        allTestCaseResultList.stream().max(Comparator.comparing(JudgeCase::getTime))
                .ifPresent(t -> result.put("time", t.getTime()));

        allTestCaseResultList.stream().max(Comparator.comparing(JudgeCase::getMemory))
                .ifPresent(t -> result.put("memory", t.getMemory()));

        // OI题目计算得分
        if (!isACM) {
            // 全对的直接用总分*0.1+2*题目难度
            if (errorCaseNum == 0 && Constants.JudgeCaseMode.DEFAULT.getMode().equals(judgeCaseMode)) {
                int oiRankScore = (int) Math.round(totalScore * 0.1 + 2 * problemDifficulty);
                result.put("score", totalScore);
                result.put("oiRankScore", oiRankScore);
            } else {
                int sumScore = 0;
                if (Constants.JudgeCaseMode.SUBTASK_LOWEST.getMode().equals(judgeCaseMode)) {
                    HashMap<Integer, Integer> groupNumMapScore = new HashMap<>();
                    for (JudgeCase testcaseResult : allTestCaseResultList) {
                        groupNumMapScore.merge(testcaseResult.getGroupNum(), testcaseResult.getScore(), Math::min);
                    }
                    for (Integer minScore : groupNumMapScore.values()) {
                        sumScore += minScore;
                    }
                } else if (Constants.JudgeCaseMode.SUBTASK_AVERAGE.getMode().equals(judgeCaseMode)) {
                    // 预处理 切换成Map Key: groupNum Value: <count,sum_score>
                    HashMap<Integer, Pair_<Integer, Integer>> groupNumMapScore = new HashMap<>();
                    for (JudgeCase testcaseResult : allTestCaseResultList) {
                        Pair_<Integer, Integer> pair = groupNumMapScore.get(testcaseResult.getGroupNum());
                        if (pair == null) {
                            groupNumMapScore.put(testcaseResult.getGroupNum(), new Pair_<>(1, testcaseResult.getScore()));
                        } else {
                            int count = pair.getKey() + 1;
                            int score = pair.getValue() + testcaseResult.getScore();
                            groupNumMapScore.put(testcaseResult.getGroupNum(), new Pair_<>(count, score));
                        }
                    }
                    for (Pair_<Integer, Integer> pair : groupNumMapScore.values()) {
                        sumScore += (int) Math.round(pair.getValue() * 1.0 / pair.getKey());
                    }
                } else {
                    for (JudgeCase testcaseResult : allTestCaseResultList) {
                        sumScore += testcaseResult.getScore();
                    }
                }
                if (totalScore != 0 && sumScore > totalScore) {
                    sumScore = totalScore;
                }
                //测试点总得分*0.1+2*题目难度*（测试点总得分/题目总分）
                int oiRankScore = (int) Math.round(sumScore * 0.1 + 2 * problemDifficulty * (sumScore * 1.0 / totalScore));
                result.put("score", sumScore);
                result.put("oiRankScore", oiRankScore);
            }
        }
        return result;
    }

    // 进行最终测试结果的判断（除编译失败外的评测状态码和时间，空间,OI题目的得分）
    public HashMap<String, Object> getJudgeInfo(List<JSONObject> testCaseResultList,
                                                Problem problem,
                                                Judge judge,
                                                String judgeCaseMode) {

        boolean isACM = Objects.equals(problem.getType(), Constants.Contest.TYPE_ACM.getCode());

        List<JSONObject> errorTestCaseList = new LinkedList<>();

        List<JudgeCase> allCaseResList = new LinkedList<>();

        // 记录所有测试点的结果
        testCaseResultList.forEach(jsonObject -> {
            Integer time = jsonObject.getLong("time").intValue();
            Integer memory = jsonObject.getLong("memory").intValue();
            Integer status = jsonObject.getInt("status");

            Long caseId = jsonObject.getLong("caseId", null);
            Integer groupNum = jsonObject.getInt("groupNum", null);
            Integer seq = jsonObject.getInt("seq", 0);
            String inputFileName = jsonObject.getStr("inputFileName");
            String outputFileName = jsonObject.getStr("outputFileName");
            String msg = jsonObject.getStr("errMsg");
            JudgeCase judgeCase = new JudgeCase();
            judgeCase.setTime(time)
                    .setMemory(memory)
                    .setStatus(status)
                    .setInputData(inputFileName)
                    .setOutputData(outputFileName)
                    .setPid(problem.getId())
                    .setUid(judge.getUid())
                    .setCaseId(caseId)
                    .setSeq(seq)
                    .setGroupNum(groupNum)
                    .setMode(judgeCaseMode)
                    .setSubmitId(judge.getSubmitId());

            if (!StringUtils.isEmpty(msg) && !Objects.equals(status, Constants.Judge.STATUS_COMPILE_ERROR.getStatus())) {
                judgeCase.setUserOutput(msg);
            }

            if (isACM) {
                if (!Objects.equals(status, Constants.Judge.STATUS_ACCEPTED.getStatus())) {
                    errorTestCaseList.add(jsonObject);
                }
            } else {
                int oiScore = jsonObject.getInt("score");
                if (Objects.equals(status, Constants.Judge.STATUS_ACCEPTED.getStatus())) {
                    judgeCase.setScore(oiScore);
                } else if (Objects.equals(status, Constants.Judge.STATUS_PARTIAL_ACCEPTED.getStatus())) {
                    errorTestCaseList.add(jsonObject);
                    Double percentage = jsonObject.getDouble("percentage");
                    if (percentage != null) {
                        int score = (int) Math.floor(percentage * oiScore);
                        judgeCase.setScore(score);
                    } else {
                        judgeCase.setScore(0);
                    }
                } else {
                    errorTestCaseList.add(jsonObject);
                    judgeCase.setScore(0);
                }
            }

            allCaseResList.add(judgeCase);
        });

        // 更新到数据库
        boolean addCaseRes = JudgeCaseEntityService.saveBatch(allCaseResList);
        if (!addCaseRes) {
            log.error("题号为：" + problem.getId() + "，提交id为：" + judge.getSubmitId() + "的各个测试数据点的结果更新到数据库操作失败");
        }

        // 获取判题的运行时间，运行空间，OI得分
        HashMap<String, Object> result = computeResultInfo(allCaseResList,
                isACM,
                errorTestCaseList.size(),
                problem.getIoScore(),
                problem.getDifficulty(),
                judgeCaseMode);

        // 如果该题为ACM类型的题目，多个测试点全部正确则AC，否则取第一个错误的测试点的状态
        // 如果该题为OI类型的题目, 若多个测试点全部正确则AC，若全部错误则取第一个错误测试点状态，否则为部分正确
        if (errorTestCaseList.size() == 0) { // 全部测试点正确，则为AC
            result.put("code", Constants.Judge.STATUS_ACCEPTED.getStatus());
        } else if (isACM || errorTestCaseList.size() == testCaseResultList.size()) {
            errorTestCaseList.sort(Comparator.comparingInt(jsonObject -> jsonObject.getInt("seq")));
            result.put("code", errorTestCaseList.get(0).getInt("status"));
            result.put("errMsg", errorTestCaseList.get(0).getStr("errMsg", ""));
        } else {
            result.put("code", Constants.Judge.STATUS_PARTIAL_ACCEPTED.getStatus());
        }
        return result;
    }


    private String getUserFileName(String language) {
        switch (language) {
            case "PHP":
                return "main.php";
            case "JavaScript Node":
            case "JavaScript V8":
                return "main.js";
        }
        return "main";
    }

    public String mergeNonEmptyStrings(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (!StringUtils.isEmpty(str)) {
                sb.append(str.substring(0, Math.min(1024 * 1024, str.length()))).append("\n");
            }
        }
        return sb.toString();
    }

    private String getFinalJudgeCaseMode(Integer type, String problemJudgeCaseMode, String infoJudgeCaseMode) {
        if (problemJudgeCaseMode == null) {
            return infoJudgeCaseMode;
        }
        if (Objects.equals(type, Constants.Contest.TYPE_ACM.getCode())) {
            // ACM题目 以题目现有的judgeCaseMode为准
            return problemJudgeCaseMode;
        } else {
            // OI题目 涉及到可能有子任务分组评测，还是依赖info文件的配置为准
            return infoJudgeCaseMode;
        }
    }
}
