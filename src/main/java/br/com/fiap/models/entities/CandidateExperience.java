package br.com.fiap.models.entities;

public class CandidateExperience {
    private Integer id;
    private Long candidateId;
    private String companyName;
    private String role;
    private String description;
    private String startDate;
    private String endDate;

    public CandidateExperience(Long candidateId, String companyName, String role, String description, String startDate, String endDate) {
        this.candidateId = candidateId;
        this.companyName = companyName;
        this.role = role;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CandidateExperience(String companyName, String role, String description, String startDate, String endDate) {
        this.companyName = companyName;
        this.role = role;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CandidateExperience() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate;}

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
