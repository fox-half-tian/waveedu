package com.zhulang.waveedu.judge.judge;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhulang.waveedu.judge.entity.problem.ProblemCase;
import com.zhulang.waveedu.judge.exception.SystemError;
import com.zhulang.waveedu.judge.service.ProgramHomeworkProblemCaseService;
import com.zhulang.waveedu.judge.util.Constants;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: Himit_ZH
 * @Date: 2021/4/16 13:21
 * @Description: 判题流程解耦重构2.0，该类只负责题目测试数据的检查与初始化
 */
@Component
public class ProblemTestCaseUtils {

    @Resource
    private ProgramHomeworkProblemCaseService programHomeworkProblemCaseService;

    private final static Pattern EOL_PATTERN = Pattern.compile("[^\\S\\n]+(?=\\n)");


    /**
     * 获取指定题目的info数据
     *
     * @param problemId 问题id
     * @param testCasesDir 测试案例文件目录
     * @return info数据
     * @throws SystemError 系统错误
     */
    public JSONObject loadTestCaseInfo(Integer problemId, String testCasesDir) throws SystemError {
        // 如果本地文件存在该信息文件，就获取信息。
        if (FileUtil.exist(testCasesDir + "/info")) {
            // 获取问题案例信息文件输入流
            FileReader fileReader = new FileReader(testCasesDir + "/info", CharsetUtil.UTF_8);
            // 获取到问题案例信息文件的内容
            String infoStr = fileReader.readString();
            // 转为 json
            /*
                0. testCasesSize：testCasesSize -> 2
                1. testCases：testCases ->
                        4.1 caseId -> 1
                            inputName -> 1.in
                            outputName -> 1.out
                            outputMd5 -> e4da3b7fbbce2345d7772b0674a318d5
                            outputSize -> 1
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
            return JSONUtil.parseObj(infoStr);
        } else {
            // 如果本地文件不存在该信息文件，就需要初始化案例输入输出文件与问题案例信息
            return tryInitTestCaseInfo(testCasesDir, problemId);
        }
    }

    /**
     * 若没有测试数据，则尝试从数据库获取并且初始化到本地，如果数据库中该题目测试数据为空，rsync同步也出了问题，则直接判系统错误
     *
     * @param testCasesDir 测试案例存放目录
     * @param problemId    问题id
     * @return 测试数据信息
     * @throws SystemError 系统错误
     */
    public JSONObject tryInitTestCaseInfo(String testCasesDir,
                                          Integer problemId) throws SystemError {
        // 获取问题的所有案例信息
        List<HashMap<String, Object>> testCases = programHomeworkProblemCaseService.getProblemCases(problemId);
        if (testCases == null || testCases.size() == 0) {
            throw new SystemError("题号为：" + problemId + "的评测数据为空！", null, "The test cases does not exist.");
        }

        JSONObject result = new JSONObject();
        result.set("testCasesSize", testCases.size());

        JSONArray testCaseList = new JSONArray(testCases.size());

        // 将案例保存到 testCasesDir 文件夹下
        for (int index = 0; index < testCases.size(); index++) {
            JSONObject jsonObject = new JSONObject();
            String inputName = (index + 1) + ".in";
            jsonObject.set("caseId", testCases.get(index).get("id"));
            jsonObject.set("inputName", inputName);
            // 生成对应文件
            FileWriter inFileWriter = new FileWriter(testCasesDir + "/" + inputName, CharsetUtil.UTF_8);
            // 将该测试数据的输入写入到文件
            inFileWriter.write((String) testCases.get(index).get("input"));

            String outputName = (index + 1) + ".out";
            jsonObject.set("outputName", outputName);
            // 生成对应文件
            String outputData = (String) testCases.get(index).get("output");
            FileWriter outFileWriter = new FileWriter(testCasesDir + "/" + outputName, CharsetUtil.UTF_8);
            outFileWriter.write(outputData);

            // 原数据MD5
            jsonObject.set("outputMd5", DigestUtils.md5DigestAsHex(outputData.getBytes(StandardCharsets.UTF_8)));
            // 原数据大小
            jsonObject.set("outputSize", outputData.getBytes(StandardCharsets.UTF_8).length);
            // 去掉全部空格的MD5，用来判断pe
            jsonObject.set("allStrippedOutputMd5", DigestUtils.md5DigestAsHex(outputData.replaceAll("\\s+", "").getBytes(StandardCharsets.UTF_8)));
            // 默认去掉文末空格的MD5
            jsonObject.set("EOFStrippedOutputMd5", DigestUtils.md5DigestAsHex(rtrim(outputData).getBytes(StandardCharsets.UTF_8)));

            testCaseList.add(jsonObject);
        }

        result.set("testCases", testCaseList);

        FileWriter infoFileWriter = new FileWriter(testCasesDir + "/info", CharsetUtil.UTF_8);
        // 写入记录文件
        infoFileWriter.write(JSONUtil.toJsonStr(result));

        return result;
    }

    // 去除每行末尾的空白符
    public static String rtrim(String value) {
        if (value == null) {
            return null;
        }
        return EOL_PATTERN.matcher(StrUtil.trimEnd(value)).replaceAll("");
    }
}