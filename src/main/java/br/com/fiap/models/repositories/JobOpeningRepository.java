package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.JobOpening;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobOpeningRepository {
    private Connection connection;

    public JobOpeningRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void createJobOpening(JobOpening jobOpening) throws SQLException {
        String sql = "INSERT INTO jobs (company_id, title, description, salary, work_model, required_skills) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, jobOpening.getCompanyId());
        stm.setString(2, jobOpening.getTitle());
        stm.setString(3, jobOpening.getDescription());
        stm.setDouble(4, jobOpening.getSalary());
        stm.setString(5, jobOpening.getWorkModel());
        stm.setString(6, jobOpening.getRequiredSkills());
        stm.executeUpdate();
        stm.close();
    }

    public JobOpening getJobOpeningById(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM jobs WHERE id = ?");;
        stm.setInt(1, id);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            JobOpening jobOpening = new JobOpening();
            jobOpening.setId(result.getInt("id"));
            jobOpening.setCompanyId(result.getInt("company_id"));
            jobOpening.setTitle(result.getString("title"));
            jobOpening.setDescription(result.getString("description"));
            jobOpening.setSalary(result.getDouble("salary"));
            jobOpening.setWorkModel(result.getString("work_model"));
            jobOpening.setRequiredSkills(result.getString("required_skills"));
            return jobOpening;
        } else {
            throw new SQLException("Vaga de emprego não encontrada para o ID: " + id);
        }
    }

    public JobOpening getJobsOpeningByCompanyId(int companyId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM jobs WHERE company_id = ?"
        );
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            JobOpening jobOpening = new JobOpening();
            jobOpening.setId(result.getInt("id"));
            jobOpening.setCompanyId(result.getInt("company_id"));
            jobOpening.setTitle(result.getString("title"));
            jobOpening.setDescription(result.getString("description"));
            jobOpening.setSalary(result.getDouble("salary"));
            jobOpening.setWorkModel(result.getString("work_model"));
            jobOpening.setRequiredSkills(result.getString("required_skills"));
            return jobOpening;
        } else {
            throw new SQLException("Vaga de emprego não encontrada para a empresa ID: " + companyId);
        }
    }

    public List<JobOpening> getJobOpeningByRequiredSkills(String skill) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM jobs WHERE required_skills LIKE ?");
        stm.setString(1, "%" + skill + "%");
        ResultSet result = stm.executeQuery();
        List<JobOpening> jobOpenings = new ArrayList<>();
        while (result.next()) {
            JobOpening jobOpening = new JobOpening();
            jobOpening.setId(result.getInt("id"));
            jobOpening.setCompanyId(result.getInt("company_id"));
            jobOpening.setTitle(result.getString("title"));
            jobOpening.setDescription(result.getString("description"));
            jobOpening.setSalary(result.getDouble("salary"));
            jobOpening.setWorkModel(result.getString("work_model"));
            jobOpening.setRequiredSkills(result.getString("required_skills"));
            jobOpenings.add(jobOpening);
        }
        return jobOpenings;
    }

    public List<JobOpening> getJobOpeningByTitle(String title) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM jobs WHERE title LIKE ?");
        stm.setString(1, "%" + title + "%");
        ResultSet result = stm.executeQuery();
        List<JobOpening> jobOpenings = new ArrayList<>();
        while (result.next()) {
            JobOpening jobOpening = new JobOpening();
            jobOpening.setId(result.getInt("id"));
            jobOpening.setCompanyId(result.getInt("company_id"));
            jobOpening.setTitle(result.getString("title"));
            jobOpening.setDescription(result.getString("description"));
            jobOpening.setSalary(result.getDouble("salary"));
            jobOpening.setWorkModel(result.getString("work_model"));
            jobOpening.setRequiredSkills(result.getString("required_skills"));
            jobOpenings.add(jobOpening);
        }
        return jobOpenings;
    }

    public List<JobOpening> getAllJobOpenings() throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM jobs WHERE active = 1");
        ResultSet result = stm.executeQuery();
        List<JobOpening> jobOpenings = new ArrayList<>();
        while (result.next()) {
            JobOpening jobOpening = new JobOpening();
            jobOpening.setId(result.getInt("id"));
            jobOpening.setCompanyId(result.getInt("company_id"));
            jobOpening.setTitle(result.getString("title"));
            jobOpening.setDescription(result.getString("description"));
            jobOpening.setSalary(result.getDouble("salary"));
            jobOpening.setWorkModel(result.getString("work_model"));
            jobOpening.setRequiredSkills(result.getString("required_skills"));
            jobOpenings.add(jobOpening);
        }
        return jobOpenings;
    }

    public void updateJobOpening(JobOpening jobOpening) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE jobs SET company_id = ?, title = ?, description = ?, salary = ?, work_model = ?, required_skills = ? WHERE id = ?");       stm.setInt(1, jobOpening.getCompanyId());
        stm.setInt(1, jobOpening.getCompanyId());
        stm.setString(2, jobOpening.getTitle());
        stm.setString(3, jobOpening.getDescription());
        stm.setDouble(4, jobOpening.getSalary());
        stm.setString(5, jobOpening.getWorkModel());
        stm.setString(6, jobOpening.getRequiredSkills());
        stm.setInt(7, jobOpening.getId());
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Vaga de emprego não encontrada para atualização, ID: " + jobOpening.getId());
        }
        stm.close();
    }

    public void deleteJobOpening(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM jobs WHERE id = ?");
        stm.setInt(1, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Vaga de emprego não encontrada para exclusão, ID: " + id);
        }
        stm.close();
    }

    public void closeJobOpening(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE jobs SET active = 0 WHERE id = ?");
        stm.setInt(1, id);
        int rowsAffected = stm.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Vaga de emprego não encontrada para fechamento, ID: " + id);
        }
        stm.close();
    }

    public JobOpening getClosedJobOpeningsByCompanyId(int company_id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement( "SELECT * FROM jobs WHERE active = 0 AND company_id = ?" );
        stm.setInt(1, company_id);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                JobOpening jobOpening = new JobOpening();
                jobOpening.setId(result.getInt("id"));
                jobOpening.setCompanyId(result.getInt("company_id"));
                jobOpening.setTitle(result.getString("title"));
                jobOpening.setDescription(result.getString("description"));
                jobOpening.setSalary(result.getDouble("salary"));
                jobOpening.setWorkModel(result.getString("work_model"));
                jobOpening.setRequiredSkills(result.getString("required_skills"));
                return jobOpening;
            } else {
                throw new SQLException("Nenhuma vaga de emprego fechada encontrada.");
            }
        }

    }

