package com.training.rss.controller.model;

import com.training.rss.validator.constraint.UrlConstraint;

import javax.validation.constraints.NotEmpty;

public class Rss {

    @NotEmpty(message = "{validation.url.notEmpty}")
    @UrlConstraint
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
