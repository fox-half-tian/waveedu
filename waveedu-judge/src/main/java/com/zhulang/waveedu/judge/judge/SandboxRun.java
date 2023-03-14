package com.zhulang.waveedu.judge.judge;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 12:09
 */

/**
 * args: string[]; // command line argument
 * env?: string[]; // environment
 * <p>
 * // specifies file input / pipe collector for program file descriptors
 * files?: (LocalFile | MemoryFile | PreparedFile | Pipe | null)[];
 * tty?: boolean; // enables tty on the input and output pipes (should have just one input & one output)
 * // Notice: must have TERM environment variables (e.g. TERM=xterm)
 * <p>
 * // limitations
 * cpuLimit?: number;     // ns
 * realCpuLimit?: number; // deprecated: use clock limit instead (still working)
 * clockLimit?: number;   // ns
 * memoryLimit?: number;  // byte
 * stackLimit?: number;   // byte (N/A on windows, macOS cannot set over 32M)
 * procLimit?: number;
 * <p>
 * // copy the correspond file to the container dst path
 * copyIn?: {[dst:string]:LocalFile | MemoryFile | PreparedFile};
 * <p>
 * // copy out specifies files need to be copied out from the container after execution
 * copyOut?: string[];
 * // similar to copyOut but stores file in executor service and returns fileId, later download through /file/:fileId
 * copyOutCached?: string[];
 * // specifies the directory to dump container /w content
 * copyOutDir: string
 * // specifies the max file size to copy out
 * copyOutMax: number; // byte
 */

@Slf4j(topic = "hoj")
public class SandboxRun {

    private static final RestTemplate restTemplate;

    // 单例模式
    private static final SandboxRun instance = new SandboxRun();

    private static final String SANDBOX_BASE_URL = "http://8.130.97.145:5050";

    public static final HashMap<String, Integer> RESULT_MAP_STATUS = new HashMap<>();

    private static final int maxProcessNumber = 128;

    private static final int TIME_LIMIT_MS = 16000;

    private static final int MEMORY_LIMIT_MB = 512;

    private static final int STACK_LIMIT_MB = 128;

    private static final int STDIO_SIZE_MB = 32;

    private SandboxRun() {

    }

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(20000);
        requestFactory.setReadTimeout(180000);
        restTemplate = new RestTemplate(requestFactory);
    }

    static {
        RESULT_MAP_STATUS.put("Time Limit Exceeded", Constants.Judge.STATUS_TIME_LIMIT_EXCEEDED.getStatus());
        RESULT_MAP_STATUS.put("Memory Limit Exceeded", Constants.Judge.STATUS_MEMORY_LIMIT_EXCEEDED.getStatus());
        RESULT_MAP_STATUS.put("Output Limit Exceeded", Constants.Judge.STATUS_RUNTIME_ERROR.getStatus());
        RESULT_MAP_STATUS.put("Accepted", Constants.Judge.STATUS_ACCEPTED.getStatus());
        RESULT_MAP_STATUS.put("Nonzero Exit Status", Constants.Judge.STATUS_RUNTIME_ERROR.getStatus());
        RESULT_MAP_STATUS.put("Internal Error", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
        RESULT_MAP_STATUS.put("File Error", Constants.Judge.STATUS_SYSTEM_ERROR.getStatus());
        RESULT_MAP_STATUS.put("Signalled", Constants.Judge.STATUS_RUNTIME_ERROR.getStatus());
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static String getSandboxBaseUrl() {
        return SANDBOX_BASE_URL;
    }

    public static final List<String> signals = Arrays.asList(
            "", // 0
            "Hangup", // 1
            "Interrupt", // 2
            "Quit", // 3
            "Illegal instruction", // 4
            "Trace/breakpoint trap", // 5
            "Aborted", // 6
            "Bus error", // 7
            "Floating point exception", // 8
            "Killed", // 9
            "User defined signal 1", // 10
            "Segmentation fault", // 11
            "User defined signal 2", // 12
            "Broken pipe", // 13
            "Alarm clock", // 14
            "Terminated", // 15
            "Stack fault", // 16
            "Child exited", // 17
            "Continued", // 18
            "Stopped (signal)", // 19
            "Stopped", // 20
            "Stopped (tty input)", // 21
            "Stopped (tty output)", // 22
            "Urgent I/O condition", // 23
            "CPU time limit exceeded", // 24
            "File size limit exceeded", // 25
            "Virtual timer expired", // 26
            "Profiling timer expired", // 27
            "Window changed", // 28
            "I/O possible", // 29
            "Power failure", // 30
            "Bad system call" // 31
    );


    public JSONArray run(String uri, JSONObject param) throws SystemError {
        HttpHeaders headers = new HttpHeaders();
        // 请求格式为 application/json
        headers.setContentType(MediaType.APPLICATION_JSON);
        /*
            测试案例的时候的参数：
                - args -> /usr/bin/java
                          -Dfile.encoding=UTF-8
                          -cp
                          /w/Main.jar
                          Main
                - env -> PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
                         LANG=en_US.UTF-8
                         LC_ALL=en_US.UTF-8
                         LANGUAGE=en_US:en
                         HOME=/w
                - files -> 0.src -> /judge/test_case/problem_1000/1.in
                           1.name -> stdout
                             max -> 33554432
                           2.name -> stderr
                             max -> 16777216
                - cpuLimit -> 2200000000
                - clockLimit -> 6600000000
                - memoryLimit -> 123731968
                - procLimit -> 128
                - stackLimit -> 134217728
                - copyIn -> Main.jar
                - copyOut -> stdout
                             stderr
         */
        HttpEntity<String> request = new HttpEntity<>(JSONUtil.toJsonStr(param), headers);
        ResponseEntity<String> postForEntity;
        try {
            // 编译时与测试案例时 -》 SANDBOX_BASE_URL + uri：http://1.14.94.100:5050/run
            postForEntity = restTemplate.postForEntity(SANDBOX_BASE_URL + uri, request, String.class);
            /*
            响应数据 postForEntity
                编译时：
                    <200,
                        [
                            {
                                "status":"Accepted",
                                "exitStatus":0,
                                "time":765628562,
                                "memory":42446848,
                                "runTime":489545524,
                                "files":{
                                    "stderr":"",
                                    "stdout":"added manifest\n adding: Main.class(in = 520) (out= 344)(deflated 33%)\n"
                                },
                                "fileIds":{
                                    "Main.jar":"6CYD6KZI"
                                }
                             }
                        ],
                        [
                            Content-Type:"application/json; charset=utf-8",
                            Date:"Fri, 10 Mar 2023 17:18:19 GMT",
                            Content-Length:"233"
                        ]
                    >

               测试案例成功时：
                    [
                        {
                            "status":"Accepted",
                            "exitStatus":0,
                            "time":88361171,"memory":11141120,
                            "runTime":102205702,
                            "files":
                                {
                                    "stderr":"",
                                    "stdout":"5"
                                }
                        }
                    ]
             */

            /*
                JSONUtil.parseArray(postForEntity.getBody())：
                    编译时：
                        0. status -> Accepted
                        1. exitStatus -> 0
                        2. time -> 765628562
                        3. memory -> 42446848
                        4. runTime -> 489545524
                        5. files：files ->
                                    stderr ->
                                    stdout -> added manifest adding: Main.class(in = 520) (out= 344)(deflated 33%)
                        6. fileIds：Main.jar -> 6CYD6KZI

                    案例测试成功时：
                        0. status -> Accepted
                        1. exitStatus -> 0
                        2. time -> 88361171
                        3. memory -> 11141120
                        4. runTime -> 102205702
                        5. files ->   stderr->""
                                      stdout->5
             */

            return JSONUtil.parseArray(postForEntity.getBody());
        } catch (RestClientResponseException ex) {
            if (ex.getRawStatusCode() != 200) {
                throw new SystemError("Cannot connect to sandbox service.", null, ex.getResponseBodyAsString());
            }
        } catch (Exception e) {
            throw new SystemError("Call SandBox Error.", null, e.getMessage());
        }
        return null;
    }

    public static void delFile(String fileId) {

        try {
            restTemplate.delete(SANDBOX_BASE_URL + "/file/{0}", fileId);
        } catch (RestClientResponseException ex) {
            if (ex.getRawStatusCode() != 200) {
                log.error("安全沙箱判题的删除内存中的文件缓存操作异常----------------->{}", ex.getResponseBodyAsString());
            }
        }

    }

    /**
     * "files": [{
     * "content": ""
     * }, {
     * "name": "stdout",
     * "max": 1024 * 1024 * 32
     * }, {
     * "name": "stderr",
     * "max": 1024 * 1024 * 32
     * }]
     */
    private static final JSONArray COMPILE_FILES = new JSONArray();

    static {
        JSONObject content = new JSONObject();
        content.set("content", "");

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * STDIO_SIZE_MB);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * STDIO_SIZE_MB);
        COMPILE_FILES.put(content);
        COMPILE_FILES.put(stdout);
        COMPILE_FILES.put(stderr);
    }

    /**
     * @param maxCpuTime        最大编译的cpu时间 ms
     * @param maxRealTime       最大编译的真实时间 ms
     * @param maxMemory         最大编译的空间 b
     * @param maxStack          最大编译的栈空间 b
     * @param srcName           编译的源文件名字
     * @param exeName           编译生成的exe文件名字
     * @param args              编译的cmd参数
     * @param envs              编译的环境变量
     * @param code              编译的源代码
     * @param needCopyOutCached 是否需要生成用户程序的缓存文件，即生成用户程序id
     *
     * @Description 编译运行
     * @Return
     */
    public static JSONArray compile(Long maxCpuTime,
                                    Long maxRealTime,
                                    Long maxMemory,
                                    Long maxStack,
                                    String srcName,
                                    String exeName,
                                    List<String> args,
                                    List<String> envs,
                                    String code,
                                    Boolean needCopyOutCached
                                    ) throws SystemError {
        JSONObject cmd = new JSONObject();
        /*
            0. /bin/bash
            1. -c
            2. javac -encoding utf-8 Main.java && jar -cvf Main.jar *.class
         */
        cmd.set("args", args);
        /*
            0. PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
            1. LANG=en_US.UTF-8
            2. LC_ALL=en_US.UTF-8
            3. LANGUAGE=en_US:en
            4. HOME=/w
         */
        cmd.set("env", envs);
        /*
            0. content -> ""
            1. name -> "stdout"
               max -> 33554432
            2. name -> "stderr"
               max -> 33554432
         */
        cmd.set("files", COMPILE_FILES);
        // ms-->ns：10000000000
        cmd.set("cpuLimit", maxCpuTime * 1000 * 1000L);
        // 20000000000
        cmd.set("clockLimit", maxRealTime * 1000 * 1000L);
        // byte 536870912
        cmd.set("memoryLimit", maxMemory);
        // 128
        cmd.set("procLimit", maxProcessNumber);
        // 268435456
        cmd.set("stackLimit", maxStack);

        JSONObject fileContent = new JSONObject();
        // 用户代码
        fileContent.set("content", code);

        JSONObject copyIn = new JSONObject();
        // 用户代码放到 Main.java 文件中
        copyIn.set(srcName, fileContent);

        cmd.set("copyIn", copyIn);
        cmd.set("copyOut", new JSONArray().put("stdout").put("stderr"));

        // 需要生成用户程序的缓存文件，即生成用户程序id
        if (needCopyOutCached) {
            cmd.set("copyOutCached", new JSONArray().put(exeName));
        }


        JSONObject param = new JSONObject();
        // 命令准备好
        param.set("cmd", new JSONArray().put(cmd));

        // 编译用户的代码
        JSONArray result = instance.run("/run", param);
        /*
            compileRes：
                0. status -> Accepted
                1. exitStatus -> 0
                2. time -> 765628562
                3. memory -> 42446848
                4. runTime -> 489545524
                5. files：files ->
                            stderr ->
                            stdout -> added manifest adding: Main.class(in = 520) (out= 344)(deflated 33%)
                6. fileIds：Main.jar -> 6CYD6KZI
         */
        JSONObject compileRes = (JSONObject) result.get(0);
        compileRes.set("originalStatus", compileRes.getStr("status"));
        compileRes.set("status", RESULT_MAP_STATUS.get(compileRes.getStr("status")));
        /*
            result：
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
        // 返回编译结果
        return result;
    }


    /**
     * @param args            普通评测运行cmd的命令参数
     * @param envs            普通评测运行的环境变量
     * @param testCasePath    题目数据的输入文件路径
     * @param testCaseContent 题目数据的输入数据（与testCasePath二者选一）
     * @param maxTime         评测的最大限制时间 ms
     * @param maxOutputSize   评测的最大输出大小 kb
     * @param maxStack        评测的最大限制栈空间 mb
     * @param exeName         评测的用户程序名称
     * @param fileId          评测的用户程序文件id
     * @param fileContent     评测的用户程序文件内容，如果userFileId存在则为null
     * @MethodName testCase
     * @Description 普通评测
     * @Return JSONArray
     * @Since 2022/1/3
     */
    public static JSONArray testCase(List<String> args,
                                     List<String> envs,
                                     String testCasePath,
                                     String testCaseContent,
                                     Long maxTime,
                                     Long maxMemory,
                                     Long maxOutputSize,
                                     Integer maxStack,
                                     String exeName,
                                     String fileId,
                                     String fileContent) throws SystemError {

        JSONObject cmd = new JSONObject();
        cmd.set("args", args);
        cmd.set("env", envs);

        JSONArray files = new JSONArray();
        JSONObject content = new JSONObject();
        if (StringUtils.isEmpty(testCasePath)) {
            content.set("content", testCaseContent);
        } else {
            content.set("src", testCasePath);
        }

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", maxOutputSize);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 16);
        files.put(content);
        files.put(stdout);
        files.put(stderr);

        cmd.set("files", files);

        // ms-->ns
        cmd.set("cpuLimit", maxTime * 1000 * 1000L);
        cmd.set("clockLimit", maxTime * 1000 * 1000L * 3);
        // byte
        cmd.set("memoryLimit", (maxMemory + 100) * 1024 * 1024L);
        cmd.set("procLimit", maxProcessNumber);
        cmd.set("stackLimit", maxStack * 1024 * 1024L);

        JSONObject exeFile = new JSONObject();
        if (!StringUtils.isEmpty(fileId)) {
            exeFile.set("fileId", fileId);
        } else {
            exeFile.set("content", fileContent);
        }
        JSONObject copyIn = new JSONObject();
        copyIn.set(exeName, exeFile);

        cmd.set("copyIn", copyIn);
        cmd.set("copyOut", new JSONArray().put("stdout").put("stderr"));


        /*
            cmd：
                - args -> /usr/bin/java
                          -Dfile.encoding=UTF-8
                          -cp
                          /w/Main.jar
                          Main
                - env -> PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
                         LANG=en_US.UTF-8
                         LC_ALL=en_US.UTF-8
                         LANGUAGE=en_US:en
                         HOME=/w
                - files -> 0.src -> /judge/test_case/problem_1000/1.in
                           1.name -> stdout
                             max -> 33554432
                           2.name -> stderr
                             max -> 16777216
                - cpuLimit -> 2200000000
                - clockLimit -> 6600000000
                - memoryLimit -> 123731968
                - procLimit -> 128
                - stackLimit -> 134217728
                - copyIn -> Main.jar
                - copyOut -> stdout
                             stderr
         */

        JSONObject param = new JSONObject();
        param.set("cmd", new JSONArray().put(cmd));

        // 调用判题安全沙箱
        JSONArray result = instance.run("/run", param);

        JSONObject testcaseRes = (JSONObject) result.get(0);
        testcaseRes.set("originalStatus", testcaseRes.getStr("status"));
        testcaseRes.set("status", RESULT_MAP_STATUS.get(testcaseRes.getStr("status")));
        /*
           result：
                0. status -> 0
                1. exitStatus -> 0
                2. time -> 88361171
                3. memory -> 11141120
                4. runTime -> 102205702
                5. files ->   stderr->""
                              stdout->5
                6. originalStatus -> Accepted
         */
        return result;
    }

}