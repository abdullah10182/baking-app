package com.example.bakingapp.models;

import java.io.Serializable;

public class Step implements Serializable {
    private int stedId;
    private String description;
    private String shortDescription;
    private String videoUrl;
    private String thumbnailUrl;

    public Step(int stedId, String description, String shortDescription, String videoUrl, String thumbnailUrl) {
        this.stedId = stedId;
        this.description = description;
        this.shortDescription = shortDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getStedId() {
        return stedId;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
