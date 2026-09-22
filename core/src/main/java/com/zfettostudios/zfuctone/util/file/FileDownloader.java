package com.zfettostudios.zfuctone.util.file;

import com.zfettostudios.zjtime.Time;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class FileDownloader {

    public record DownloadResult(
        int statusCode,
        Path path,
        String url,
        long contentLength,
        Map<String, List<String>> headers
    ) {

    }

    public static CompletableFuture<DownloadResult> download(String urlString, Path targetPath, Time timeout) {
        return downloadWithRetry(urlString, targetPath, timeout, 3);
    }

    private static CompletableFuture<DownloadResult> downloadWithRetry(String urlString, Path targetPath, Time timeout, int retries) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (targetPath.getParent() != null) Files.createDirectories(targetPath.getParent());

                URL url = URI.create(urlString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                // Полноценная мимикрия под браузер для обхода защиты Cloudflare / Maven CDN
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                connection.setRequestProperty("Connection", "keep-alive");

                connection.setConnectTimeout((int) timeout.toDuration().toMillis());
                connection.setReadTimeout((int) timeout.toDuration().toMillis());
                connection.setInstanceFollowRedirects(true);

                int responseCode = connection.getResponseCode();

                // Ручная обработка редиректов
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || responseCode == 307 || responseCode == 308) {
                    String newUrl = connection.getHeaderField("Location");
                    return downloadWithRetry(newUrl, targetPath, timeout, retries).join();
                }

                long contentLength = connection.getContentLengthLong();
                Map<String, List<String>> headers = connection.getHeaderFields();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (InputStream in = connection.getInputStream()) {
                        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                return new DownloadResult(responseCode, targetPath, urlString, contentLength, headers);

            } catch (Exception e) {
                // Если произошел сброс соединения (Connection reset) и есть попытки — пробуем снова
                if (retries > 0 && (e instanceof SocketException || (e.getCause() != null && e.getCause() instanceof SocketException))) {
                    try {
                        Thread.sleep(500); // Небольшая пауза перед повтором
                    } catch (InterruptedException ignored) {}
                    return downloadWithRetry(urlString, targetPath, timeout, retries - 1).join();
                }
                throw new RuntimeException("Failed to download file from " + urlString + " Reason: " + e, e);
            }
        });
    }
}