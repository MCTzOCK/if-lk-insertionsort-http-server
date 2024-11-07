package com.bensiebert.iflk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Server {

    public ServerSocket srv;

    public Server(int port) {
        try {
            srv = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println("Failed to create server socket: " + ex.getMessage());
            System.exit(1);
        }

        System.out.println("Server started on port " + port);

        Thread listenThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Socket s = srv.accept();
                        handleClient(s);
                    } catch (IOException ex) {
                        System.err.println("Failed to accept client: " + ex.getMessage());
                    }
                }
            }
        };

        listenThread.start();
    }

    public static void handleClient(Socket s) {
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("Client connected: " + s.getInetAddress().getHostAddress());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.isEmpty()) {
                            break;
                        }
                        content.append(line).append("\n");
                    }

                    String rawRequest = content.toString();

                    HttpRequest req = HttpRequest.fromRaw(rawRequest);
                    HttpResponse res = handleRequest(req);

                    writer.write(res.toString());
                    writer.close();
                    reader.close();
                    s.close();
                    Thread.currentThread().interrupt();

                } catch (IOException ex) {
                    System.err.println("Failed to close client socket: " + ex.getMessage());
                }
            }
        }.start();
    }

    public static HttpResponse handleRequest(HttpRequest req) {
        switch (req.path) {
            case "/":
                return new HttpResponse(HttpResponse.defaultHeaders(), Templater.render(
                        new Templater().getTemplate("index"),
                        new HashMap<String, String>() {{
                            put("TITLE", "Insertion Sort");
                        }}
                ), HttpResponse.HTTP_200);
            case "/favicon.ico":
                return new HttpResponse(HttpResponse.defaultHeaders(), "", HttpResponse.HTTP_404);
            default:
                Integer[] nums = Arrays.stream(req.path.substring(1).split(",")).map(Integer::parseInt).toArray(Integer[]::new);

                List<Integer> numbers = new List<>();
                numbers.toFirst();
                for (int num : nums) {
                    numbers.append(num);
                }
                numbers.toFirst();

                List<Integer> ret = new List<>();

                if (numbers.isEmpty()) {
                    return new HttpResponse(HttpResponse.defaultHeaders(), Templater.render(
                            new Templater().getTemplate("result"),
                            new HashMap<String, String>() {{
                                put("TITLE", "Insertion Sort");
                                put("MESSAGE", "Keine Zahlen zum Sortieren angegeben.");
                            }}
                    ), HttpResponse.HTTP_200);
                }

                ret.toFirst();

                while (numbers.hasAccess()) {
                    int num = numbers.getContent();
                    ret.toFirst();
                    while (ret.hasAccess() && ret.getContent().compareTo(num) < 0) {
                        ret.next();
                    }
                    if (ret.hasAccess()) {
                        ret.insert(num);
                    } else {
                        ret.append(num);
                    }
                    numbers.next();
                }

                return new HttpResponse(HttpResponse.defaultHeaders(), Templater.render(
                        new Templater().getTemplate("result"),
                        new HashMap<String, String>() {{
                            put("TITLE", "Insertion Sort");
                            put("MESSAGE", "Sortierte Reihenfolge: <b>" + listToString(ret) + "</b>");
                        }}
                ), HttpResponse.HTTP_200);
        }
    }

    public static String listToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        list.toFirst();
        while (list.hasAccess()) {
            sb.append(list.getContent()).append(", ");
            list.next();
        }
        String s = sb.toString();
        if (s.endsWith(", ")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }
}
