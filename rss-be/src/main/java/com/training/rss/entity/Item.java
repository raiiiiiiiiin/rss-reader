package com.training.rss.entity;


import com.training.rss.util.StringUtil;
import com.training.rss.entity.common.CommonFieldsEntity;

import javax.xml.bind.annotation.XmlElement;

public class Item extends CommonFieldsEntity {

    private String author;
    private String thumbnail;
    private String category;
    private Enclosure enclosure;
    private String pubDate;

    public Item() {
    }

    @XmlElement
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlElement
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @XmlElement
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @XmlElement
    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    @XmlElement
    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "Item:\n" +
                "title:" + StringUtil.getString(getTitle()) + "\n" +
                " link:" + StringUtil.getString(getLink()) + "\n" +
                " description:" + StringUtil.getString(getDescription()) + "\n" +
                " category:" + StringUtil.getString(category) + "\n" +
                " enclosure:\n" + (enclosure==null?"":enclosure + "\n") +
                " pubDate:" + StringUtil.getString(pubDate) + "\n";
    }
}
