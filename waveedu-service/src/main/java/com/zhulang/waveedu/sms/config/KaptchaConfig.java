package com.zhulang.waveedu.sms.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 谷歌提供的图片验证码kaptcha
 *
 * @author 狐狸半面添
 * @create 2023-01-29 11:02
 */
@Configuration
public class KaptchaConfig {
    @Bean
    public Producer kaptcha() {
        Properties properties = new Properties();
        /*
            设置图片有边框并且为蓝色
            properties.setProperty("kaptcha.border", "no");
            properties.setProperty("kaptcha.border.color", "blue");
         */

        // 设置图片无边框
        properties.setProperty("kaptcha.border", "no");

        // 背景颜色渐变开始，这里设置的是rgb值156,156,156
        properties.put("kaptcha.background.clear.from", "156,156,156");
        // 背景颜色渐变结束，这里设置以白色结束
        properties.put("kaptcha.background.clear.to", "white");
        // 字体颜色，这里设置为黑色
        properties.put("kaptcha.textproducer.font.color", "black");
        // 文字间隔，这里设置为10px
        properties.put("kaptcha.textproducer.char.space", "10");

        /*
            如果需要去掉干扰线，则如此配置：
            properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
         */

        // 干扰线颜色配置，这里设置成了idea的Darcula主题的背景色
        properties.put("kaptcha.noise.color", "43,43,43");

        // 字体
        properties.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 图片宽度，默认也是200px
        properties.setProperty("kaptcha.image.width", "200");
        // 图片高度，默认也是50px
        properties.setProperty("kaptcha.image.height", "50");
        // 从哪些字符中产生
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789abcdefghijklmnopqrsduvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        // 字符个数
        properties.setProperty("kaptcha.textproducer.char.length", "5");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
