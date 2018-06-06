package com.training.rss.service;

import com.training.rss.entity.Rss;
import com.training.rss.exception.RssException;

import java.io.File;

public interface RssService {

    Rss getRssFeed(String url) throws RssException;

    File getRssFeedAsFile(String url) throws RssException;

    File getObjectAsFile(Object object) throws RssException;
}
