package com.jindidata.user.redpacket.constant;

public class Global {

    // ---------------------------------------实例状态相关开始----------------------------------//
    public static final String DEAL_W = "W";// 待处理

    public static final String DEAL_C = "C"; // 处理完成

    public static final String U = "U";// 用戶

    public static final String M = "M";// 商家

    // ---------------------------------------实例状态相关结束----------------------------------//

    // ---------------------------------------渠道开始----------------------------------//

    public static final String CHANNEL_B = "B"; // 包红包渠道

    public static final String CHANNEL_F = "F"; // 发红包渠道

    public static final String CHANNEL_D = "D"; // 兑红包渠道

    public static final String CHANNEL_Q = "Q"; // 兑红包渠道

    // ---------------------------------------渠道结束----------------------------------//

    // ---------------------------------------红包类型相关变量定义开始----------------------------------//

    public static final String RED_TYPE_01 = "red_type_01";// 红包类型1 线段切割法 不公平，有惊喜

    public static final String RED_TYPE_02 = "red_type_02";// 红包类型2 二倍均值法 公平，无惊喜 每次随机金额范围是一样的

    // ---------------------------------------红包类型相关变量定义结束----------------------------------//

}
