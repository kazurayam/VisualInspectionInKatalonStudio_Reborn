package com.kazurayam.uitestjava.flaskr.pom.actions;

import java.net.URL;
import java.util.Map;

public abstract class ActionListener {

    public ActionListener() {}

    @SuppressWarnings("rawtypes")
    public abstract void on(Class clazz, URL url, Map<String, String> attributes);

}
