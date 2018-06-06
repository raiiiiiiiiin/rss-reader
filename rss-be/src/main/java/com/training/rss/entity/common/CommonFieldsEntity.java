package com.training.rss.entity.common;

import com.training.rss.adapter.StringExcludeAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class CommonFieldsEntity {

    private String title;
    private String link;
    private String description;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringExcludeAdapter.class)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement(required = true)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringExcludeAdapter.class)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
