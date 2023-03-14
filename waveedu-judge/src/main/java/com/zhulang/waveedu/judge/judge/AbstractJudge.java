package com.zhulang.waveedu.judge.judge;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.judge.entity.JudgeDTO;
import com.zhulang.waveedu.judge.judge.entity.JudgeGlobalDTO;
import com.zhulang.waveedu.judge.judge.entity.SandBoxRes;
import java.util.regex.Pattern;

/**
 * @author 狐狸半面添
 * @create 2023-03-14 09:30
 */
public abstract class AbstractJudge {

    private final static Pattern EOL_PATTERN = Pattern.compile("[^\\S\\n]+(?=\\n)");

    public JSONObject judge(JudgeDTO judgeDTO, JudgeGlobalDTO judgeGlobalDTO) throws SystemError {


        // 对该案例进行测试
        JSONArray judgeResultList = judgeCase(judgeDTO, judgeGlobalDTO);
        /*
            judgeResultList：
                0. status -> 0
                1. exitStatus -> 0
                2. time -> 88361171
                3. memory -> 11141120
                4. runTime -> 102205702
                5. files ->   stderr->""
                              stdout->5
                6. originalStatus -> Accepted
         */
        return process(judgeDTO, judgeGlobalDTO, judgeResultList);
    }

    public abstract JSONArray judgeCase(JudgeDTO judgeDTO, JudgeGlobalDTO judgeGlobalDTO) throws SystemError;

    /**
     * 分析运行的结果并对数据进行校验
     *
     * @param judgeDTO
     * @param judgeGlobalDTO
     * @param judgeResultList 所有测试案例的结果信息
     * @return
     * @throws SystemError
     */
    private JSONObject process(JudgeDTO judgeDTO, JudgeGlobalDTO judgeGlobalDTO, JSONArray judgeResultList) throws SystemError {
        /*
            judgeResult：
                0. status -> 0
                1. exitStatus -> 0
                2. time -> 88361171
                3. memory -> 11141120
                4. runTime -> 102205702
                5. files ->   stderr->""
                              stdout->5
                6. originalStatus -> Accepted
         */
        JSONObject judgeResult = (JSONObject) judgeResultList.get(0);
        SandBoxRes sandBoxRes = SandBoxRes.builder()
                .stdout(((JSONObject) judgeResult.get("files")).getStr("stdout"))
                .stderr(((JSONObject) judgeResult.get("files")).getStr("stderr"))
                //  ns->ms
                .time(judgeResult.getLong("time") / 1000000)
                // b-->kb
                .memory(judgeResult.getLong("memory") / 1024)
                .exitCode(judgeResult.getInt("exitStatus"))
                .status(judgeResult.getInt("status"))
                .originalStatus(judgeResult.getStr("originalStatus"))
                .build();
        /*
            sandBoxRes：
                - status -> 0
                - originalStatus -> Accepted
                - exitCode -> 0
                - memory -> 10880
                - time -> 88
                - stdout -> 5
                - stderr -> ""
         */
        // 如果正确，则包含运行时间，消耗内存，状态为0
        return checkResult(sandBoxRes, judgeDTO, judgeGlobalDTO);
    }

    public abstract JSONObject checkResult(SandBoxRes sandBoxRes, JudgeDTO judgeDTO, JudgeGlobalDTO judgeGlobalDTO) throws SystemError;


    /**
     * 去除行末尾空白符
     *
     * @param value 内容
     * @return 取出默认空白后的结果
     */
    protected String rtrim(String value) {
        if (value == null) {
            return null;
        }
        return EOL_PATTERN.matcher(StrUtil.trimEnd(value)).replaceAll("");
    }
}