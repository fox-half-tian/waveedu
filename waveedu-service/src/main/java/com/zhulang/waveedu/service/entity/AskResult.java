package com.zhulang.waveedu.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 狐狸半面添
 * @create 2023-03-25 13:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AskResult implements Serializable {
    private Integer code;
    private String answer;
}
