package com.api.ecommerce.shared.security.jwt;

public record JwtPrincipal(
        Long userId,
        String username
) {
}
