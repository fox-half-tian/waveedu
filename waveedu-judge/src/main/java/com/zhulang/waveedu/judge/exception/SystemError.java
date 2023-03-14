package com.zhulang.waveedu.judge.exception;

import lombok.Data;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 22:36
 */
@Data
public class SystemError extends Exception {
    private String message;
    private String stdout;
    private String stderr;

    public SystemError(String message, String stdout, String stderr) {
        super(message + " " + stderr);
        this.message = message;
        this.stdout = stdout;
        this.stderr = stderr;
    }

}