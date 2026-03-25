package client;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.net.http.HttpClient;


class ClientCommunicator {

    private static final int TIMEOUT_MILLIS = 5000;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public HttpResponse<String> doGet(String host, int port, String urlPath, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .GET();

        if (authToken != null) {
            builder.header("authorization", authToken);
        }

        HttpRequest request = builder.build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Throw exception if needed
        checkExceptions(httpResponse);
        return httpResponse;
    }

    public HttpResponse<String> doPost(String host, int port, String urlPath, String message, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8));

        if (authToken != null) {
            builder.header("authorization", authToken);
        }
        HttpRequest request = builder.build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Throw exception if needed
        checkExceptions(httpResponse);

        return httpResponse;
    }

    public HttpResponse<String> doDelete(String host, int port, String urlPath, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .DELETE();

        if (authToken != null) {
            builder.header("authorization", authToken);
        }
        HttpRequest request = builder.build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Throw exception if needed
        checkExceptions(httpResponse);

        return httpResponse;
    }

    public HttpResponse<String> doPut(String host, int port, String urlPath, String message, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .PUT(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8));

        if (authToken != null) {
            builder.header("authorization", authToken);
        }
        HttpRequest request = builder.build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Throw exception if needed
        checkExceptions(httpResponse);

        return httpResponse;

    }

    public void checkExceptions(HttpResponse<String> httpResponse) {
        switch (httpResponse.statusCode()){
            case 403:
                throw new ClientExceptions("Already taken");
            case 400:
                throw new ClientExceptions("Bad Requests");
            case 401:
                throw new ClientExceptions("Unauthorized");
        }
    }
}


