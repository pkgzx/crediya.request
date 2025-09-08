package com.crediya.request.model.auth;

public record Auth (
  String identification,
  String email,
  String role
) {
}