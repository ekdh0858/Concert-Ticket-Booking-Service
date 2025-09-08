package kr.hhplus.be.server.domain;

import java.util.UUID;

public record User(
    UUID id,
    long money
) {
}
