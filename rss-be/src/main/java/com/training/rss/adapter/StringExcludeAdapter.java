package com.training.rss.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@Component
public class StringExcludeAdapter extends XmlAdapter<String, String> {

    private static String[] excludedWords;

    protected StringExcludeAdapter() {
        super();
    }

    @Value("${exclude.words}")
    public void setDatabase(String[] excludedWords) {
        this.excludedWords = excludedWords;
    }

    @Override
    public String unmarshal(String text) throws Exception {
        String result = text;
        if (excludedWords != null && excludedWords.length !=0) {
            for (String word : excludedWords) {
                result = text.replace(word, "");
            }
        }

        return result;
    }

    @Override
    public String marshal(String text) {
        return text;
    }
}
