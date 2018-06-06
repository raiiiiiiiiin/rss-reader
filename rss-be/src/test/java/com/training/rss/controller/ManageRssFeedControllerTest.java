package com.training.rss.controller;

import com.training.rss.entity.Rss;
import com.training.rss.entity.Channel;
import com.training.rss.service.RssService;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ManageRssFeedController.class)
public class ManageRssFeedControllerTest {

    @Autowired
    private MockMvc mvc;

    @Value("${default.rss.feed}")
    private String[] expectedFeed;

    @MockBean
    private RssService rssService;

    @Test
    public void getDefaultRssFeed() throws Exception {
        mvc.perform(get("/manage-rss/get-default-list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(expectedFeed[0])));
    }

    @Test
    public void getFeed() throws Exception {
        String url1 = "http://test.com/rss1";
        String url2 = "http://test.com/rss2";

        Rss expectedRss1 = new Rss();
        Channel channel1 = new Channel();
        channel1.setLink(url1);
        expectedRss1.setChannel(channel1);

        Rss expectedRss2 = new Rss();
        Channel channel2 = new Channel();
        channel2.setLink(url2);
        expectedRss2.setChannel(channel2);

        when(rssService.getRssFeed(url1)).thenReturn(expectedRss1);
        when(rssService.getRssFeed(url2)).thenReturn(expectedRss2);

        mvc.perform(post("/manage-rss/get-feed")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\""+url1+"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel.link", is(url1)));

        mvc.perform(post("/manage-rss/get-feed")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\""+url2+"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.channel.link", is(url2)));
    }

    @Test
    public void downloadRss() throws Exception {
        String url1 = "http://test.com/rss1";

        File file = new File(getClass().getClassLoader().getResource("test.txt").getFile());

        when(rssService.getRssFeedAsFile(url1)).thenReturn(file);

        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String expected = new String(encoded, StandardCharsets.UTF_8);

        mvc.perform(post("/manage-rss/download-feed")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"url\":\""+url1+"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    public void downloadItem() throws Exception {
        File file = new File(getClass().getClassLoader().getResource("test.txt").getFile());

        when(rssService.getObjectAsFile(notNull())).thenReturn(file);

        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String expected = new String(encoded, StandardCharsets.UTF_8);

        mvc.perform(post("/manage-rss/download-item")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"link\":\"http://test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}
