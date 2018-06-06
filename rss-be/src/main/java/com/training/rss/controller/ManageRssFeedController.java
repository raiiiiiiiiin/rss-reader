package com.training.rss.controller;

import com.training.rss.entity.Item;
import com.training.rss.exception.RssException;
import com.training.rss.service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "/manage-rss")
public class ManageRssFeedController {

    @Value("${default.rss.feed}")
    private String[] defaultRssFeed;

    @Autowired
    private RssService rssService;

    @GetMapping(path = "/get-default-list")
    public String[] getDefaultRssFeed() {
        return defaultRssFeed;
    }


    @PostMapping(path = "/get-feed")
    public com.training.rss.entity.Rss getChannel(@RequestBody @Valid com.training.rss.controller.model.Rss rss) throws RssException {
        return rssService.getRssFeed(rss.getUrl());
    }

    @PostMapping(path = "/download-feed")
    public ResponseEntity<Resource> downloadChannel(@RequestBody @Valid com.training.rss.controller.model.Rss rss) throws RssException {
        return getResourceFromFile(rssService.getRssFeedAsFile(rss.getUrl()));
    }

    @PostMapping(path = "/download-item")
    public ResponseEntity<Resource> downloadItem(@RequestBody Item item) throws RssException {
        return getResourceFromFile(rssService.getObjectAsFile(item));
    }

    private ResponseEntity<Resource> getResourceFromFile(File file) throws RssException {
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RssException("Unable to generate text file");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("text/plain;charset=UTF-8"))
                .body(resource);
    }
}
