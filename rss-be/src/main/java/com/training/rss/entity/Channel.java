package com.training.rss.entity;

import com.training.rss.entity.common.CommonFieldsEntity;
import com.training.rss.util.StringUtil;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class Channel extends CommonFieldsEntity {

    private String lastBuildDate;
    private List<Item> items;

    public Channel() {
    }

    @XmlElement
    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    @XmlElement(name = "item")
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Channel:\n" +
                "title:" + StringUtil.getString(getTitle()) + "\n" +
                " link:" + StringUtil.getString(getLink()) + "\n" +
                " description:" + StringUtil.getString(getDescription()) + "\n" +
                " lastBuildDate:" + StringUtil.getString(lastBuildDate) + "\n" +
                " items:\n" + items;
    }
}
