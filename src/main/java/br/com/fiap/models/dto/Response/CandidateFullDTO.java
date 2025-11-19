package br.com.fiap.models.dto.Response;

import java.time.LocalDateTime;

public class CandidateFullDTO {

    private String displayName;
    private String purpose;
    private String workStyle;
    private String interests;
    private String aiProfile;

    private EducationDTO[] educations;
    private ExperienceDTO[] experiences;
    private SkillDTO[] skills;
    private ApplicationDTO[] applications;

    // ----------- Getters e Setters do Candidate -----------

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getWorkStyle() { return workStyle; }
    public void setWorkStyle(String workStyle) { this.workStyle = workStyle; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public String getAiProfile() { return aiProfile; }
    public void setAiProfile(String aiProfile) { this.aiProfile = aiProfile; }

    public EducationDTO[] getEducations() { return educations; }
    public void setEducations(EducationDTO[] educations) { this.educations = educations; }

    public ExperienceDTO[] getExperiences() { return experiences; }
    public void setExperiences(ExperienceDTO[] experiences) { this.experiences = experiences; }

    public SkillDTO[] getSkills() { return skills; }
    public void setSkills(SkillDTO[] skills) { this.skills = skills; }

    public ApplicationDTO[] getApplications() { return applications; }
    public void setApplications(ApplicationDTO[] applications) { this.applications = applications; }


    // ----------- DTO Internas -----------

    public static class EducationDTO {
        private String institution;
        private String course;
        private String level;
        private String startDate;
        private String endDate;

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


    public static class ExperienceDTO {
        private String companyName;
        private String role;
        private String description;
        private String startDate;
        private String endDate;

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
    }


    public static class SkillDTO {
        private String skillName;
        private int level;

        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
    }


    public static class ApplicationDTO {
        private JobDTO job;
        private LocalDateTime appliedAt;
        private String status;

        public JobDTO getJob() { return job; }
        public void setJob(JobDTO job) { this.job = job; }

        public LocalDateTime getAppliedAt() { return appliedAt; }
        public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public static class JobDTO {
            private String title;
            private String description;
            private double salary;
            private String workModel;
            private String requiredSkills;

            private String companyName;
            private int companyId;

            public String getTitle() { return title; }
            public void setTitle(String title) { this.title = title; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }

            public double getSalary() { return salary; }
            public void setSalary(double salary) { this.salary = salary; }

            public String getWorkModel() { return workModel; }
            public void setWorkModel(String workModel) { this.workModel = workModel; }

            public String getRequiredSkills() { return requiredSkills; }
            public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

            public String getCompanyName() { return companyName; }
            public void setCompanyName(String companyName) { this.companyName = companyName; }

            public int getCompanyId() { return companyId; }
            public void setCompanyId(int companyId) { this.companyId = companyId; }
        }
    }
}
