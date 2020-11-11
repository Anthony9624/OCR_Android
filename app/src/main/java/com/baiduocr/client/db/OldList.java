package com.baiduocr.client.db;

import org.litepal.crud.LitePalSupport;

/**
 * class desc :
 */
public class OldList extends LitePalSupport {

    private long id;
    private long createTime;
    private String content;

    public long getCreateTime() {
        return createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
