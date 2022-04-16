package com.jindidata.user.redpacket.pojo.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QueryModel implements Serializable {

	private static final long serialVersionUID = -2350142975210512347L;
	
	public QueryModel(String id){
		if(id!=null&&StringUtils.isNumeric(id))
			this.id = Long.parseLong(id);
	}
	
	private Long id;
	private Integer ps = DEFAULT_PS;
	private Integer pn = DEFAULT_PN;

	public static final Integer DEFAULT_PN = 1;
	public static final Integer DEFAULT_PS = 15;
	
	public Integer getOffset(){
		if(pn==null||ps==null)
			return null;
		return (pn-1)*ps;
	}
}

