package client;

import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;

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

    public RegisterResult registerCommunicator(RegisterRequest request) throws URISyntaxException, IOException, InterruptedException {
       HttpResponse<String> httpResponse = doGet("localhost", 8080, "/user" );
       String response = mapper.toJsonSerializer(httpResponse.body());
       return new RegisterResult(null, null);

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
            Optional<String> lengthHeader = headers.firstValue("Content-Length");

            System.out.printf("Received %s bytes%n", lengthHeader.orElse("unknown"));
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
            System.out.println(httpResponse.body());
        }

        return httpResponse;
    }
}
