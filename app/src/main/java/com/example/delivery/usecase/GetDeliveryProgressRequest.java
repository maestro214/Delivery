package com.example.delivery.usecase;

public class GetDeliveryProgressRequest {
    private final String carrierId;
    private final String trackId;

    public GetDeliveryProgressRequest(String carrierId, String trackId) {
        this.carrierId = carrierId;
        this.trackId = trackId;
    }

    public String getCarrierId() {
        return carrierId;
    }

    public String getTrackId() {
        return trackId;
    }
}
