package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class CandidateBehaviorProfile {
    private Long candidateId;
    private String aiProfile;
    private LocalDateTime lastUpdated;

    public CandidateBehaviorProfile(Long candidateId, String aiProfile) {
        this.candidateId = candidateId;
        this.aiProfile = aiProfile;
        this.lastUpdated = LocalDateTime.now();
    }

    public CandidateBehaviorProfile() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public String getAiProfile() { return aiProfile; }
    public void setAiProfile(String aiProfile) { this.aiProfile = aiProfile; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
