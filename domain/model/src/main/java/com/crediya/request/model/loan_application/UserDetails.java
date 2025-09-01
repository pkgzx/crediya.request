package com.crediya.request.model.loan_application;


public record UserDetails(
    String id,
    String name,
    String lastName,
    String identification,
    String email
) {
}

