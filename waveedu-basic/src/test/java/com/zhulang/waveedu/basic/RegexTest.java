package com.zhulang.waveedu.basic;

import com.zhulang.waveedu.common.util.RegexUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 狐狸半面添
 * @create 2023-01-30 20:17
 */
@SpringBootTest
public class RegexTest {
    @Test
    public void testName(){
        System.out.println(RegexUtils.isNameInvalid("唐雨浪59"));
    }
    @Test
    public void testSignature(){
        System.out.println(RegexUtils.isSignatureInvalid("我"));
    }
    @Test
    public void testSex(){
        System.out.println(RegexUtils.isSexInvalid("男"));
    }
}
