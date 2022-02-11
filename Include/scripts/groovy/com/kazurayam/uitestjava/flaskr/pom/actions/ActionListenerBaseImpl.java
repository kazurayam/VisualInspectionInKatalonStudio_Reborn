package com.kazurayam.uitestjava.flaskr.pom.actions;

import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionListenerBaseImpl extends ActionListener {

    public ActionListenerBaseImpl() {}

    @SuppressWarnings("rawtypes")
    @Override
    public void on(Class clazz, URL url, Map<String, String> attributes) {
        Logger logger = LoggerFactory.getLogger(clazz);
        Gson gson = new Gson();
        String msg = String.format("%s %s", url.toExternalForm(), gson.toJson(attributes));
        logger.info(msg);
    }
}