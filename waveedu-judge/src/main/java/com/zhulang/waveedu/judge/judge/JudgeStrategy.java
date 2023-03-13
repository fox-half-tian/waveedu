package com.zhulang.waveedu.judge.judge;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.entity.judge.Judge;
import com.zhulang.waveedu.judge.entity.judge.JudgeCase;
import com.zhulang.waveedu.judge.entity.problem.Problem;
import com.zhulang.waveedu.judge.exception.CompileError;
import com.zhulang.waveedu.judge.exception.LanguageNoSupportError;
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
    private ProblemTestCaseUtils problemTestCaseUtils;

    @Resource
    private LanguageConfigLoader languageConfigLoader;

    @Resource
    private JudgeRun judgeRun;

    public HashMap<String, Object> judge(ProblemLimitInfoDTO problemLimitInfo, ToJudgeDTO toJudgeDTO) {

        HashMap<String, Object> result = new HashMap<>();
        // 编译好的临时代码文件id
        String userFileId = null;
        try {
            // 对用户源代码进行编译 获取tmpfs中的fileId
            LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName(toJudgeDTO.getLanguage());
            // 有的语言可能不支持编译, 目前有js、php不支持编译，则 languageConfig 的值为 null
            if (languageConfig==null){
                throw new LanguageNoSupportError("Unsupported language " + toJudgeDTO.getLanguage());
            }
            if (languageConfig.getCompileCommand() != null) {
                // c和c++为一倍时间和空间，其它语言为2倍时间和空间
                // languageConfig为编程语言对应的配置信息
                if (languageConfig.getSrcName() == null
                        || (!languageConfig.getSrcName().endsWith(".c") && !languageConfig.getSrcName().endsWith(".cpp"))
                ) {
                    problemLimitInfo.setTimeLimit(problemLimitInfo.getTimeLimit() * 2);
                    problemLimitInfo.setMemoryLimit(problemLimitInfo.getMemoryLimit() * 2);
                }

                // 对用户的代码进行编译，获取到编译后的文件的id
                userFileId = Compiler.compile(languageConfig, toJudgeDTO.getCode());
            }
            // 指定测试数据文件所在文件夹：/judge/test_case/problem_1000   1000是问题的id
            String testCasesDir = Constants.JudgeDir.TEST_CASE_DIR.getContent() + "/problem_" + problemLimitInfo.getId();
            /*
            从文件中加载测试数据json——testCasesInfo：
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
            JSONObject testCasesInfo = problemTestCaseUtils.loadTestCaseInfo(problemLimitInfo.getId(), testCasesDir);
            if (testCasesInfo == null) {
                throw new SystemError("The evaluation data of the problem does not exist", null, null);
            }
            // 开始测试每个测试点，allCaseResultList为评测出来的结果
            List<JSONObject> allCaseResultList = judgeRun.judgeAllCase(
                    problemLimitInfo,
                    toJudgeDTO.getLanguage(),
                    testCasesDir,
                    testCasesInfo,
                    userFileId,
                    toJudgeDTO.getCode()
            );

            // 对全部测试点结果进行评判,获取最终评判结果
            return getJudgeInfo(allCaseResultList, problem, judge, judgeCaseMode);
        }catch (LanguageNoSupportError languageNoSupportError){
            result.put("code",Constants.Judge.STATUS_LANGUAGE_NO_SUPPORT.getStatus());
            result.put("errMsg",languageNoSupportError.getMessage());
            log.error("[Judge] [LanguageNoSupport Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    languageNoSupportError);
        } catch (SystemError systemError) {
            result.put("code", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
            result.put("errMsg", "Oops, something has gone wrong with the judgeServer. Please report this to administrator.");
            result.put("time", 0);
            result.put("memory", 0);
            log.error("[Judge] [System Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    systemError);
        } catch (SubmitError submitError) {
            result.put("code", Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus());
            result.put("errMsg", mergeNonEmptyStrings(submitError.getMessage(), submitError.getStdout(), submitError.getStderr()));
            result.put("time", 0);
            result.put("memory", 0);
            log.error("[Judge] [Submit Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
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
            log.error("[Judge] [System Runtime Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    e);
        } finally {

            // 删除tmpfs内存中的用户代码可执行文件
            if (!StringUtils.isEmpty(userFileId)) {
                SandboxRun.delFile(userFileId);
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

    public String mergeNonEmptyStrings(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (!StringUtils.isEmpty(str)) {
                sb.append(str.substring(0, Math.min(1024 * 1024, str.length()))).append("\n");
            }
        }
        return sb.toString();
    }
}
