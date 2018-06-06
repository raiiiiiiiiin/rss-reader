package com.training.rss.service.impl;

import com.training.rss.entity.Rss;
import com.training.rss.exception.RssException;
import com.training.rss.service.RssService;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class RssServiceImpl implements RssService {

    @Override
    public Rss getRssFeed(String urlString) throws RssException {
        Rss result = null;
        try {
            URL url = new URL(urlString);
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            result = (Rss) jaxbContext.createUnmarshaller().unmarshal(url);
        } catch (MalformedURLException e) {
            throw new RssException("Invalid URL");
        } catch (UnmarshalException e) {
            throw new RssException("Url provided is not RSS format");
        } catch (JAXBException e) {
            throw new RssException("Invalid Channel class");
        }

        return result;
    }

    @Override
    public File getRssFeedAsFile(String url) throws RssException {
        Rss rss = getRssFeed(url);

        if (rss == null || rss.getChannel() == null) {
            throw new RssException("Unable to get RSS Feed");
        }

        return getObjectAsFile(rss.getChannel());
    }

    @Override
    public File getObjectAsFile(Object object) throws RssException {
        String fileName = object.getClass().getSimpleName() + ".txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(fileName));
            fileWriter.write(object.toString());
        } catch (IOException e) {
            throw new RssException("Cannot save txt file!");
        }finally {
            if (fileWriter!= null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                }
            }
        }
        return new File(fileName);
    }
}
