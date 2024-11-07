package com.bensiebert.iflk;

import java.util.HashMap;

public class HashMapUtil {

    public static String toHeaderString(HashMap<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        for(String key : headers.keySet()) {
            sb.append(key).append(": ").append(headers.get(key)).append("\n");
        }
        return sb.toString();
    }
}
