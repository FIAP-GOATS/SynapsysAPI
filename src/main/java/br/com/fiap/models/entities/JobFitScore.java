package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class JobFitScore {
    private int id;
    private int jobId;
    private int candidateId;
    private double technicalScore;
    private double culturalScore;
    private double totalScore;
    private LocalDateTime createdAt;

    public JobFitScore(int id, int jobId, int candidateId, double technicalScore, double culturalScore, double totalScore, LocalDateTime createdAt) {
        this.id = id;
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.technicalScore = technicalScore;
        this.culturalScore = culturalScore;
        this.totalScore = totalScore;
        this.createdAt = createdAt;
    }

    public JobFitScore(int jobId, int candidateId, double technicalScore, double culturalScore, double totalScore) {
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.technicalScore = technicalScore;
        this.culturalScore = culturalScore;
        this.totalScore = totalScore;
        this.createdAt = LocalDateTime.now();
    }

    public JobFitScore() {
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }

    public double getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(double technicalScore) { this.technicalScore = technicalScore; }

    public double getCulturalScore() { return culturalScore; }
    public void setCulturalScore(double culturalScore) { this.culturalScore = culturalScore; }

    public double getTotalScore() { return totalScore; }
    public void setTotalScore(double totalScore) { this.totalScore = totalScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
