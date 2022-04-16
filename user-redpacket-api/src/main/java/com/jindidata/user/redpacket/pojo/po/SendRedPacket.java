package com.jindidata.user.redpacket.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class SendRedPacket {

    private Long id;
    private Long sendRedPacketId;
    private Long groupId;
    private Long userId;
    private String redType;
    private String copies;
    private Integer money;
    private Integer moneyBack;
    private String status;
    private String userOrMerchant;
    private Date createDate;
    private Integer months;
    private Integer days;
    private Date finishDate;

}
