package br.com.fiap.models.entities;

import java.time.LocalDateTime;

public class Company {
    private int userId;
    private String name;
    private String description;
    private String industry;
    private String culture; /// :: Cultura da empresa.
    private LocalDateTime createdAt;

    public Company(int userId, String name, String description, String industry, String culture) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.culture = culture;
        this.createdAt = LocalDateTime.now();
    }

    public Company(String name, String description, String industry, String culture) {
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.culture = culture;
        this.createdAt = LocalDateTime.now();
    }

    public Company() {
        this.createdAt = LocalDateTime.now();
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public String getCulture() { return culture;
    }
    public void setCulture(String culture) { this.culture = culture; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
