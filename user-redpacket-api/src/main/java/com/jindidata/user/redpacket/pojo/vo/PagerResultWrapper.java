package com.jindidata.user.redpacket.pojo.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */

public class PagerResultWrapper<T> implements Serializable {
    private static final long serialVersionUID = 8640779315392285438L;
    private int pageNum;
    private int total;
    private int realTotal;
    private List<T> items;

    public PagerResultWrapper() {
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String toString() {
        return "PagerResultWrapper [pageNum=" + this.pageNum + ", total=" + this.total + ", realTotal=" + this.realTotal + ", items=" + this.items + "]";
    }

    public int getRealTotal() {
        return this.realTotal;
    }

    public void setRealTotal(int realTotal) {
        this.realTotal = realTotal;
    }
}
