package com.i2gether.lic.models;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.Nullable;


public record UserRequest(@Nullable UUID userId, @Nullable UUID sessionId, String question) {

}
