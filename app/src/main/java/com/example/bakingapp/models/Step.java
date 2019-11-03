package com.example.bakingapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Step implements Serializable {
    @SerializedName("id")
    private int stedId;
    private String description;
    private String shortDescription;
    @SerializedName("videoURL")
    private String videoUrl;
    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    public Step(int stedId, String description, String shortDescription, String videoUrl, String thumbnailUrl) {
        this.stedId = stedId;
        this.description = description;
        this.shortDescription = shortDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getStepId() {
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
