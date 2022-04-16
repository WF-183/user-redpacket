package com.jindidata.user.redpacket.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedPacketDto implements Serializable {

    private static final long serialVersionUID = -7649620505621648887L;

    private Long redId;


}
