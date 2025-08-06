package com.example.notification_svc.exception.response;


public record EmailErrorResponse(int status, String message, String cause) { }
