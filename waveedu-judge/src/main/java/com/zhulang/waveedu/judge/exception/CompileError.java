package com.zhulang.waveedu.judge.exception;

import lombok.Data;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 22:36
 */
@Data
public class CompileError extends Exception {
    private String message;
    private String stdout;
    private String stderr;

    public CompileError(String message, String stdout, String stderr) {
        super(message);
        this.message = message;
        this.stdout = stdout;
        this.stderr = stderr;
    }
}