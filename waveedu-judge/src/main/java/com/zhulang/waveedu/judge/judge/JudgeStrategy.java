package com.zhulang.waveedu.judge.judge;

import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.dto.ProblemLimitInfoDTO;
import com.zhulang.waveedu.judge.dto.ToJudgeDTO;
import com.zhulang.waveedu.judge.exception.CompileError;
import com.zhulang.waveedu.judge.exception.LanguageNoSupportError;
import com.zhulang.waveedu.judge.exception.SubmitError;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.*;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 09:30
 */
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
            if (languageConfig == null) {
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
            return getJudgeInfo(allCaseResultList);
        } catch (LanguageNoSupportError languageNoSupportError) {
            result.put("code", Constants.Judge.STATUS_LANGUAGE_NO_SUPPORT.getStatus());
            result.put("errMsg", languageNoSupportError.getMessage());
            log.error("[Judge] [LanguageNoSupport Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    languageNoSupportError);
        } catch (SystemError systemError) {
            result.put("code", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
            result.put("errMsg", "Oops, something has gone wrong with the judgeServer. Please report this to administrator.");
            log.error("[Judge] [System Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    systemError);
        } catch (SubmitError submitError) {
            result.put("code", Constants.Judge.STATUS_SUBMITTED_FAILED.getStatus());
            result.put("errMsg", mergeNonEmptyStrings(submitError.getMessage(), submitError.getStdout(), submitError.getStderr()));
            log.error("[Judge] [Submit Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    submitError);
        } catch (CompileError compileError) {
            result.put("code", Constants.Judge.STATUS_COMPILE_ERROR.getStatus());
            result.put("errMsg", mergeNonEmptyStrings(compileError.getStdout(), compileError.getStderr()));
        } catch (Exception e) {
            result.put("code", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
            result.put("errMsg", "Oops, something has gone wrong with the judgeServer. Please report this to administrator.");
            log.error("[Judge] [System Runtime Error] Problem Id:[{}], Error:[{}]",
                    problemLimitInfo.getId(),
                    e);
        } finally {
            // 删除tmpfs内存中的用户代码可执行文件
            if (StringUtils.hasText(userFileId)) {
                SandboxRun.delFile(userFileId);
            }
        }
        return result;
    }

    /**
     * 进行最终测试结果的判断（除编译失败外的评测状态码和时间，空间,OI题目的得分）
     *
     * @param testCaseResultList 测试结果信息
     * @return 最终结果
     */
    public HashMap<String, Object> getJudgeInfo(List<JSONObject> testCaseResultList) {

        int maxTime = -1;
        int maxMemory = -1;

        // 记录所有测试点的结果
        for (JSONObject jsonObject : testCaseResultList) {

            // 状态
            int status = jsonObject.getInt("status");


            if (!Objects.equals(status, Constants.Judge.STATUS_ACCEPTED.getStatus())) {
                HashMap<String, Object> result = new HashMap<>(2);
                result.put("code", status);
                result.put("errMsg", jsonObject.getStr("errMsg"));
                return result;
            }

            // 实际运行时间
            int caseTime = jsonObject.getLong("time").intValue();
            // 实际消耗内存
            int caseMemory = jsonObject.getLong("memory").intValue();
            // 用时和内存占用保存为多个测试点中最长的
            if (caseTime > maxTime) {
                maxTime = caseTime;
            }
            if (caseMemory > maxMemory) {
                maxMemory = caseMemory;
            }
        }
        HashMap<String, Object> result = new HashMap<>(3);
        result.put("code", Constants.Judge.STATUS_ACCEPTED.getStatus());
        result.put("time",maxTime);
        result.put("memory",maxMemory);
        return result;
    }

    public String mergeNonEmptyStrings(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (StringUtils.hasText(str)) {
                sb.append(str.substring(0, Math.min(1024 * 1024, str.length()))).append("\n");
            }
        }
        return sb.toString();
    }
}