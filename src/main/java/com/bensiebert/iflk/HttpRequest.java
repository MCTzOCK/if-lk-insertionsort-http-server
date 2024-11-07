package com.bensiebert.iflk;

import java.util.Arrays;
import java.util.HashMap;

public class HttpRequest {

    public HashMap<String, String> headers;
    public String method;
    public String path;
    public String protocol;

    public HttpRequest(HashMap<String, String> headers, String method, String path, String protocol) {
        this.headers = headers;
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public static HttpRequest fromRaw(String request) {
        HashMap<String, String> h = new HashMap<>();
        String v = request.split(" ")[0];
        String path = request.split(" ")[1];
        String proto = request.split(" ")[2];

        for(String headerLine: request.split("\n")) {
            if(headerLine.contains(":")) {
                h.put(headerLine.split(":")[0], Arrays.stream(headerLine.split(":")).skip(1).reduce((a, b) -> a + ":" + b).get().trim());
            }
        }

        return new HttpRequest(h, v, path, proto);
    }
}
