package com.training.rss.entity;

import com.training.rss.util.StringUtil;

import javax.xml.bind.annotation.XmlAttribute;

public class Enclosure {

    private String url;

    private String type;

    private long length;

    public Enclosure() {
    }

    @XmlAttribute
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return  "  url:" + StringUtil.getString(url) + "\n" +
                "  type:" + StringUtil.getString(type);
    }
}
