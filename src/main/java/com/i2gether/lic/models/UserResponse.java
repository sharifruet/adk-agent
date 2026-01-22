package com.i2gether.lic.models;

import java.util.UUID;

public record UserResponse(UUID userId, UUID sessionId, String answer) {

}
