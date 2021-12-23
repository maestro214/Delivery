package com.example.delivery.models;

public class ApiPost {
    private static String location;

    public static String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "ApiPost{" +
                "location='" + location + '\'' +
                '}';
    }
}
