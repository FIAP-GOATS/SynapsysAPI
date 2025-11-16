package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class Candidate {
    private long userId;
    private String displayName;
    private String purpose;
    private String workStyle;
    private String interests;
    private LocalDateTime createdAt;

    public Candidate(long userId, String displayName, String purpose, String workStyle, String interests) {
        this.userId = userId;
        this.displayName = displayName;
        this.purpose = purpose;
        this.workStyle = workStyle;
        this.interests = interests;
        this.createdAt = LocalDateTime.now();
    }

    public Candidate(String displayName, String purpose, String workStyle, String interests) {
        this.displayName = displayName;
        this.purpose = purpose;
        this.workStyle = workStyle;
        this.interests = interests;
        this.createdAt = LocalDateTime.now();
    }

    public Candidate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters e setters
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getWorkStyle() { return workStyle; }
    public void setWorkStyle(String workStyle) { this.workStyle = workStyle; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

