import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author 狐狸半面添
 * @create 2023-02-03 15:36
 */
public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.134.136.211:3306/wave_edu?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("狐狸半面添") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-chat\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhulang.waveedu") // 设置父包名
                            .moduleName("chat") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-chat\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("edu_lesson_class"); // 设置需要生成的表名
                            //.addTablePrefix("edu_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    public static void main09(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.134.136.211:3306/wave_edu?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("狐狸半面添") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-edu\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhulang.waveedu") // 设置父包名
                            .moduleName("edu") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-edu\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("edu_program_homework_stu_condition") // 设置需要生成的表名
                            .addTablePrefix("edu_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    public static void main05(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.134.136.211:3306/wave_edu?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("狐狸半面添") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-basic\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhulang.waveedu") // 设置父包名
                            .moduleName("basic") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-basic\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("basic_admin") // 设置需要生成的表名
                            .addTablePrefix("basic_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    public static void main06(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://8.134.136.211:3306/wave_edu?characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("狐狸半面添") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-program\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zhulang.waveedu") // 设置父包名
                            .moduleName("program") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\SoftwareEngineering\\java\\project\\waveedu\\waveedu-program\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("program_problem_bank_case") // 设置需要生成的表名
                            .addTablePrefix("program_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
