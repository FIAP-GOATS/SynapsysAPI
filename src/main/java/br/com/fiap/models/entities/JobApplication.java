package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class JobApplication {
    private int id;
    private int jobId;
    private Long candidateId;
    private LocalDateTime appliedAt;
    private String status;

    public JobApplication(int id, int jobId, Long candidateId, LocalDateTime appliedAt, String status) {
        this.id = id;
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.appliedAt = appliedAt;
        this.status = status;
    }

    public JobApplication(int jobId, Long candidateId) {
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.appliedAt = LocalDateTime.now();
        this.status = "pending";
    }

    public JobApplication() {
        this.appliedAt = LocalDateTime.now();
        this.status = "pending";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
