package com.bensiebert.iflk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Templater {

    public static String render(String template, HashMap<String, String> values) {
        String rendered = template;
        for(String key : values.keySet()) {
            rendered = rendered.replace("{{" + key + "}}", values.get(key));
        }
        return rendered;
    }

    public String getTemplate(String name) {
        try(InputStream in = getClass().getResourceAsStream("/templates/" + name + ".html")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            return reader.lines().reduce("", (a, b) -> a + b);
        } catch (IOException e) {
            System.out.println("Failed to load template: " + e.getMessage());
            return "<h1>500 Internal Server Error</h1><h2>The server failed to load the template.</h2>";
        }
    }
}
