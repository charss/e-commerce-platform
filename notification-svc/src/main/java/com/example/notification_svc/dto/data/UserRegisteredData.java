package com.example.notification_svc.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisteredData(
        @JsonProperty(required = true) String email,
        @JsonProperty(required = true) String firstName,
        @JsonProperty(required = true) String lastName
) { }
