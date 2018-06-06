package com.training.rss.validator;

import com.training.rss.validator.constraint.UrlConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlValidator implements ConstraintValidator<UrlConstraint, String> {
    @Override
    public void initialize(UrlConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String urlField, ConstraintValidatorContext context) {
        HttpURLConnection connection;
        try {
            URL url = new URL(urlField);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("HEAD");
        } catch (IOException e){
            return false;
        }
        return true;
    }
}
