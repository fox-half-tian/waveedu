package com.zhulang.waveedu.judge.exception;

import lombok.Data;

/**
 * @author 狐狸半面添
 * @create 2023-03-13 23:58
 */
@Data
public class LanguageNoSupportError extends Exception{
    public LanguageNoSupportError(String message){
        super(message);
    }
}
