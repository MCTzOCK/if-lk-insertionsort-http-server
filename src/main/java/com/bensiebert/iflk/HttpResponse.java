package com.bensiebert.iflk;

import java.util.HashMap;

public class HttpResponse {

    public static final String HTTP_200 = "HTTP/1.1 200 OK";
    public static final String HTTP_404 = "HTTP/1.1 404 Not Found";
    public static final String HTTP_500 = "HTTP/1.1 500 Internal Server Error";

    public static final String SERVER = "InsertionSortHTTPServer";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static HashMap<String, String> defaultHeaders() {
        HashMap<String, String> headers = new HashMap<>();

        headers.put("Content-Type", CONTENT_TYPE_HTML);

        return headers;
    }

    public HashMap<String, String> headers = new HashMap<>();
    public String content = "";
    public String status;

    public HttpResponse(HashMap<String, String> headers, String content, String status) {
        this.headers = headers;
        this.content = content;
        this.status = status;

        this.headers.put("Server", SERVER);
    }

    @Override
    public String toString() {
        return status + "\n" + HashMapUtil.toHeaderString(headers) + "\n\n" + content;
    }
}
