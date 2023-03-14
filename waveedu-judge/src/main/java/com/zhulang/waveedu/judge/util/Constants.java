package com.zhulang.waveedu.judge.util;


/**
 * @author 狐狸半面添
 * @Description: 常量枚举类
 * @since 2023-03-14
 */
public class Constants {
    /**
     * @Description 提交评测结果的状态码
     */
    public enum Judge {
        /**
         * 输出格式错误
         */
        STATUS_PRESENTATION_ERROR(-3, "Presentation Error"),
        /**
         * 编译错误
         */
        STATUS_COMPILE_ERROR(-2, "Compile Error"),
        /**
         * 答案错误
         */
        STATUS_WRONG_ANSWER(-1, "Wrong Answer"),
        /**
         * 评测通过
         */
        STATUS_ACCEPTED(0, "Accepted"),
        /**
         * 时间超限
         */
        STATUS_TIME_LIMIT_EXCEEDED(1, "Time Limit Exceeded"),
        /**
         * 空间超限
         */
        STATUS_MEMORY_LIMIT_EXCEEDED(2, "Memory Limit Exceeded"),
        /**
         * 运行错误
         */
        STATUS_RUNTIME_ERROR(3, "Runtime Error"),
        /**
         * 系统错误
         */
        STATUS_SYSTEM_ERROR(4, "System Error"),
        /**
         * 提交失败
         */
        STATUS_SUBMITTED_FAILED(10, "Submitted Failed"),
        /**
         * 无状态
         */
        STATUS_NULL(15, "No Status"),
        /**
         * 语言不支持
         */
        STATUS_LANGUAGE_NO_SUPPORT(16, "language no support");

        private final Integer status;
        private final String name;

        Judge(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public static Judge getTypeByStatus(int status) {
            for (Judge judge : Judge.values()) {
                if (judge.getStatus() == status) {
                    return judge;
                }
            }
            return STATUS_NULL;
        }
    }


    public static final String TEST_CASE_DIR = "/judge/test_case";


}