package br.com.fiap.models.entities;

public class CandidateEducation {
    private Integer id;
    private Integer candidateId;
    private String institution;
    private String course;
    private String level;
    private String startDate;
    private String endDate;

    public CandidateEducation(Integer candidateId, String institution, String course, String level, String startDate, String endDate) {
        this.candidateId = candidateId;
        this.institution = institution;
        this.course = course;
        this.level = level;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CandidateEducation(String institution, String course, String level, String startDate, String endDate) {
        this.institution = institution;
        this.course = course;
        this.level = level;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CandidateEducation() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCandidateId() { return candidateId; }
    public void setCandidateId(Integer candidateId) { this.candidateId = candidateId; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}
