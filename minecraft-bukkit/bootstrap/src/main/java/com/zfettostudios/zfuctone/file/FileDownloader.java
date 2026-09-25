package com.zfettostudios.zfuctone.file;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloader {
    @Setter
    @Getter
    private static HttpClient client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();

    public static void download(String url, String target) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build();

        Path destination = Paths.get(target);
        client.send(request, HttpResponse.BodyHandlers.ofFile(destination));
    }
}
