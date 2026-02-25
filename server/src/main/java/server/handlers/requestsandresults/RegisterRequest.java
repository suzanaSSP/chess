package server.handlers.requestsandresults;

public record RegisterRequest(
        String username,
        String password,
        String email
){}
