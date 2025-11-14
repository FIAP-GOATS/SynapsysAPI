package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class JobOpening {
    private int id;
    private int companyId;
    private String title;
    private String description;
    private double salary;
    private String workModel;
    private String requiredSkills;
    private LocalDateTime createdAt;
    private int active;

    public JobOpening(int id, int companyId, String title, String description, double salary, String workModel, String requiredSkills, LocalDateTime createdAt, int active) {
        this.id = id;
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.workModel = workModel;
        this.requiredSkills = requiredSkills;
        this.createdAt = createdAt;
        this.active = active;
    }

    public JobOpening(int companyId, String title, String description, double salary, String workModel, String requiredSkills) {
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.workModel = workModel;
        this.requiredSkills = requiredSkills;
        this.createdAt = LocalDateTime.now();
        this.active = 1;
    }

    public JobOpening() {
        this.createdAt = LocalDateTime.now();
        this.active = 1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }
}
