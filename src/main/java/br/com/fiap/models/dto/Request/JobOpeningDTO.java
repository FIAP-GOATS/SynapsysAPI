package br.com.fiap.models.dto.Request;

public class JobOpeningDTO {

    private String title;
    private String description;
    private double salary;
    private String workModel;
    private String requiredSkills;

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
}