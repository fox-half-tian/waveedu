package com.zhulang.waveedu.judge.judge;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.exception.CompileError;
import com.zhulang.waveedu.judge.exception.SubmitError;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.util.Constants;
import com.zhulang.waveedu.judge.util.JudgeUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Himit_ZH
 * @Date: 2021/4/16 12:14
 * @Description: 判题流程解耦重构2.0，该类只负责编译
 */
public class Compiler {

    public static String compile(LanguageConfig languageConfig, String code,
                                 String language) throws SystemError, CompileError, SubmitError {
        // 语言的限制
        if (languageConfig == null) {
            throw new RuntimeException("Unsupported language " + language);
        }

        // 调用安全沙箱进行编译
        JSONArray result = SandboxRun.compile(
                languageConfig.getMaxCpuTime(),
                languageConfig.getMaxRealTime(),
                languageConfig.getMaxMemory(),
                256 * 1024 * 1024L,
                languageConfig.getSrcName(),
                languageConfig.getExeName(),
                parseCompileCommand(languageConfig.getCompileCommand()),
                languageConfig.getCompileEnvs(),
                code,
                null,
                true
        );
        /*
            compileResult：
                0. status -> 0
                1. exitStatus -> 0
                2. time -> 765628562
                3. memory -> 42446848
                4. runTime -> 489545524
                5. files：files ->
                            stderr ->
                            stdout -> added manifest adding: Main.class(in = 520) (out= 344)(deflated 33%)
                6. fileIds：Main.jar -> 6CYD6KZI
                7. originalStatus -> Accepted
         */
        JSONObject compileResult = (JSONObject) result.get(0);
        // Constants.Judge.STATUS_ACCEPTED.getStatus() 的值为0，如果编译失败，则抛出CompileError
        if (compileResult.getInt("status").intValue() != Constants.Judge.STATUS_ACCEPTED.getStatus()) {
            throw new CompileError("Compile Error.", ((JSONObject) compileResult.get("files")).getStr("stdout"),
                    ((JSONObject) compileResult.get("files")).getStr("stderr"));
        }
        // languageConfig.getExeName() 的值是 Main.jar，可以获取到 fileId 为 6CYD6KZI
        String fileId = ((JSONObject) compileResult.get("fileIds")).getStr(languageConfig.getExeName());
        // 如果是空说明没有找到文件，就抛出异常
        if (StringUtils.isEmpty(fileId)) {
            throw new SubmitError("Executable file not found.", ((JSONObject) compileResult.get("files")).getStr("stdout"),
                    ((JSONObject) compileResult.get("files")).getStr("stderr"));
        }
        // 返回文件的id
        return fileId;
    }

    public static Boolean compileSpj(String code, Long pid, String language, HashMap<String, String> extraFiles) throws SystemError {

        LanguageConfigLoader languageConfigLoader = SpringUtil.getBean(LanguageConfigLoader.class);
        LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName("SPJ-" + language);

        if (languageConfig == null) {
            throw new RuntimeException("Unsupported SPJ language:" + language);
        }

        boolean copyOutExe = true;
        if (pid == null) { // 题目id为空，则不进行本地存储，可能为新建题目时测试特判程序是否正常的判断而已
            copyOutExe = false;
        }

        // 调用安全沙箱对特别判题程序进行编译
        JSONArray res = SandboxRun.compile(languageConfig.getMaxCpuTime(),
                languageConfig.getMaxRealTime(),
                languageConfig.getMaxMemory(),
                256 * 1024 * 1024L,
                languageConfig.getSrcName(),
                languageConfig.getExeName(),
                parseCompileCommand(languageConfig.getCompileCommand()),
                languageConfig.getCompileEnvs(),
                code,
                extraFiles,
                false,
                copyOutExe,
                Constants.JudgeDir.SPJ_WORKPLACE_DIR.getContent() + File.separator + pid
        );
        JSONObject compileResult = (JSONObject) res.get(0);
        if (compileResult.getInt("status").intValue() != Constants.Judge.STATUS_ACCEPTED.getStatus()) {
            throw new SystemError("Special Judge Code Compile Error.", ((JSONObject) compileResult.get("files")).getStr("stdout"),
                    ((JSONObject) compileResult.get("files")).getStr("stderr"));
        }
        return true;
    }


    public static Boolean compileInteractive(String code, Long pid, String language, HashMap<String, String> extraFiles) throws SystemError {

        LanguageConfigLoader languageConfigLoader = SpringUtil.getBean(LanguageConfigLoader.class);
        LanguageConfig languageConfig = languageConfigLoader.getLanguageConfigByName("INTERACTIVE-" + language);

        if (languageConfig == null) {
            throw new RuntimeException("Unsupported interactive language:" + language);
        }

        boolean copyOutExe = true;
        if (pid == null) { // 题目id为空，则不进行本地存储，可能为新建题目时测试特判程序是否正常的判断而已
            copyOutExe = false;
        }

        // 调用安全沙箱对特别判题程序进行编译
        JSONArray res = SandboxRun.compile(languageConfig.getMaxCpuTime(),
                languageConfig.getMaxRealTime(),
                languageConfig.getMaxMemory(),
                256 * 1024 * 1024L,
                languageConfig.getSrcName(),
                languageConfig.getExeName(),
                parseCompileCommand(languageConfig.getCompileCommand()),
                languageConfig.getCompileEnvs(),
                code,
                extraFiles,
                false,
                copyOutExe,
                Constants.JudgeDir.INTERACTIVE_WORKPLACE_DIR.getContent() + File.separator + pid
        );
        JSONObject compileResult = (JSONObject) res.get(0);
        if (compileResult.getInt("status").intValue() != Constants.Judge.STATUS_ACCEPTED.getStatus()) {
            throw new SystemError("Interactive Judge Code Compile Error.", ((JSONObject) compileResult.get("files")).getStr("stdout"),
                    ((JSONObject) compileResult.get("files")).getStr("stderr"));
        }
        return true;
    }

    private static List<String> parseCompileCommand(String command) {
        return JudgeUtils.translateCommandline(command);
    }
}