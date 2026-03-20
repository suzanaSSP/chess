package client;

import com.google.gson.Gson;
import io.javalin.http.UnauthorizedResponse;
import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;
import io.javalin.http.BadRequestResponse;
import java.nio.channels.AlreadyBoundException;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Optional;
import java.net.http.HttpClient;


class ClientCommunicator {
    private static final int TIMEOUT_MILLIS = 5000;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    Serializer mapper = new Serializer();
    Gson gson = new Gson();

    public RegisterResult registerCommunicator(RegisterRequest request) throws URISyntaxException, IOException, InterruptedException {
        String requestString = gson.toJson(request).toString();
       HttpResponse<String> httpResponse = doPost("localhost", 8080, "/user", requestString);
       RegisterResult result = gson.fromJson(httpResponse.body(), RegisterResult.class);
       return result;

    }

    public HttpResponse<String> doGet(String host, int port, String urlPath) throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", "abc123")
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200) {
            HttpHeaders headers = httpResponse.headers();
            System.out.println(httpResponse.body());
        }

        return httpResponse;
    }

    public HttpResponse<String> doPost(String host, int port, String urlPath, String message) throws URISyntaxException, IOException, InterruptedException {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, urlPath);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", "abc123")
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200) {
            HttpHeaders headers = httpResponse.headers();
            Optional<String> lengthHeader = headers.firstValue("Content-Length");

        } else {
            switch (httpResponse.statusCode()){
                case 403:
                    throw new AlreadyBoundException();
                case 400:
                    throw new BadRequestResponse();
                case 401:
                    throw new UnauthorizedResponse();
            }
        }
        return httpResponse;
    }

}
