package com.zhulang.waveedu.edu.constant;

/**
 * @author 狐狸半面添
 * @create 2023-01-19 0:15
 */
public class EduConstants {
    /**
     * 默认的课程封面路径
     */
    public final static String DEFAULT_LESSON_COVER = "https://waveedu.oss-cn-hangzhou.aliyuncs.com/lesson-cover/waveedudefault.png";

    /**
     * 默认的课程班级封面路径
     */
    public final static String DEFAULT_LESSON_CLASS_COVER = "https://waveedu.oss-cn-hangzhou.aliyuncs.com/lesson-class-cover/waveedudefault.png";


    /**
     * 默认的课程简单文件信息列表最大查询条数
     */
    public static final Integer DEFAULT_LESSON_SIMPLE_FILE_LIST_QUERY_LIMIT = 15;

    /**
     * 默认的课程详细文件信息列表最大查询条数
     */
    public static final Integer DEFAULT_LESSON_DETAIL_FILE_LIST_QUERY_LIMIT = 25;

    /**
     * 默认的课程班级文件列表最大查询条数
     */
    public static final Integer DEFAULT_LESSON_CLASS_FILE_LIST_QUERY_LIMIT = 25;

    /**
     * 默认的班级作业检查任务最大查询数量
     */
    public static final Integer DEFAULT_LESSON_CLASS_HOMEWORK_CHECK_QUERY_LIMIT = 25;

    /**
     * 默认的创建的课程班级信息列表最大查询条数
     */
    public static final Integer DEFAULT_CREATE_LESSON_CLASS_LIST_QUERY_LIMIT = 12;

    /**
     * 课程中用户的身份表示
     * 0：非课程成员，游客
     * 1：班级普通成员
     * 2：教学团队成员
     * 3：创建者
     */
    public static final Integer LESSON_IDENTITY_VISITOR = 0;
    public static final Integer LESSON_IDENTITY_COMMON = 1;
    public static final Integer LESSON_IDENTITY_TCH = 2;
    public static final Integer LESSON_IDENTITY_CREATOR = 3;

    /**
     * 课程小节的文件类型
     * 0：视频
     * 1：其它
     */
    public static final Integer LESSON_SECTION_FILE_TYPE_VIDEO = 0;
    public static final Integer LESSON_SECTION_FILE_TYPE_OTHER = 1;
}
