package br.com.fiap.models.dto.Response;

import br.com.fiap.models.entities.JobOpening;

public class JobOpeningWithCompanyNameDTO {
    private String companyName;
    private JobOpening job;

    public JobOpeningWithCompanyNameDTO(String companyName, JobOpening job) {
        this.companyName = companyName;
        this.job = job;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public JobOpening getJob() {
        return job;
    }

    public void setJob(JobOpening job) {
        this.job = job;
    }
}
