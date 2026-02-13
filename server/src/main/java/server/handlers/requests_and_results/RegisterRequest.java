package server.handlers.requests_and_results;

public record RegisterRequest(
        String username,
        String password,
        String email
){}
