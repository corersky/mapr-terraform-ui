package com.mapr.ps.cloud.terraform.maprdeployui.service;

public class InvalidClusterStateException extends Exception {
    public InvalidClusterStateException(String message) {
        super(message);
    }
}
