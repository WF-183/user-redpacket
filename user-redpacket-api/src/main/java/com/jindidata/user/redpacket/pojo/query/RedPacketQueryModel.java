package com.jindidata.user.redpacket.pojo.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RedPacketQueryModel extends QueryModel {

    private static final long serialVersionUID = 5095060263517753201L;

    public RedPacketQueryModel(String id) {
        super(id);
    }

    private Long userId;

    private Long groupId;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date endTime;

    private String keyword;

    private String keywordReg;

    private String orderBy = "id";

}

