package com.zhulang.waveedu.judge.judge;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.exception.CompileError;
import com.zhulang.waveedu.judge.exception.SubmitError;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.LanguageConfig;
import com.zhulang.waveedu.judge.util.Constants;
import com.zhulang.waveedu.judge.util.JudgeUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 09:30
 * @Description: 判题流程解耦重构2.0，该类只负责编译
 */
public class Compiler {

    public static String compile(LanguageConfig languageConfig,
                                 String code) throws SystemError, CompileError, SubmitError {

        // 调用安全沙箱进行编译
        JSONArray result = SandboxRun.compile(
                // 编译最大cpu运行时间（s），java 默认是 10000s
                languageConfig.getMaxCpuTime(),
                // 编译最大真实运行时间（s）,java 默认是 20000s
                languageConfig.getMaxRealTime(),
                // 编译最大运行空间（b），java 默认是 536870912b
                languageConfig.getMaxMemory(),
                // 编译的最大栈空间 256MB
                256 * 1024 * 1024L,
                // 源代码文件名称，java 默认是 Main.java
                languageConfig.getSrcName(),
                // 源代码的可执行文件名称，java 默认是 Main.jar
                languageConfig.getExeName(),
                /*
                    编译命令，java 默认是：
                                0. /bin/bash
                                1. -c
                                2. javac -encoding utf-8 Main.java && jar -cvf Main.jar *.class
                 */
                parseCompileCommand(languageConfig.getCompileCommand()),
                /*
                    编译运行环境，java 默认是：
                                0. PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
                                1. LANG=en_US.UTF-8
                                2. LC_ALL=en_US.UTF-8
                                3. LANGUAGE=en_US:en
                                4. HOME=/w
                 */
                languageConfig.getCompileEnvs(),
                // 用户的代码
                code,
                // 需要生成用户程序的缓存文件，即生成用户程序id
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
        if (!StringUtils.hasText(fileId)) {
            throw new SubmitError("Executable file not found.", ((JSONObject) compileResult.get("files")).getStr("stdout"),
                    ((JSONObject) compileResult.get("files")).getStr("stderr"));
        }
        // 返回文件的id
        return fileId;
    }

    private static List<String> parseCompileCommand(String command) {
        return JudgeUtils.translateCommandline(command);
    }
}