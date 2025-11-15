package br.com.fiap.models.repositories;

import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.models.entities.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyRepository {
    private Connection connection;

    public CompanyRepository() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void registerCompany(Company company) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO companies (user_id, name, description, industry, culture) VALUES (?, ?, ?, ?, ?)");

        stm.setInt(1, company.getUserId());
        stm.setString(2, company.getName());
        stm.setString(3, company.getDescription());
        stm.setString(4, company.getIndustry());
        stm.setString(5, company.getCulture());

        stm.executeUpdate();
        stm.close();
    }

    public Company getCompanyById(int user_id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM companies WHERE user_id = ?");
        stm.setLong(1, user_id);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            Company company = new Company();
            company.setUserId(rs.getInt("user_id"));
            company.setName(rs.getString("name"));
            company.setDescription(rs.getString("description"));
            company.setIndustry(rs.getString("industry"));
            company.setCulture(rs.getString("culture"));
            return company;
        }
        throw new SQLException("Empresa não encontrada");
    }

    public Company getCompanyByName(String name) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM companies WHERE name = ?");
        stm.setString(1, name);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            Company company = new Company();
            company.setUserId(rs.getInt("user_id"));
            company.setName(rs.getString("name"));
            company.setDescription(rs.getString("description"));
            company.setIndustry(rs.getString("industry"));
            company.setCulture(rs.getString("culture"));
            return company;
        }

        return null;
    }

    public void updateCompany(Company company) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("UPDATE companies SET name = ?, description = ?, industry = ?, culture = ? WHERE user_id = ?");

        stm.setString(1, company.getName());
        stm.setString(2, company.getDescription());
        stm.setString(3, company.getIndustry());
        stm.setString(4, company.getCulture());
        stm.setInt(5, company.getUserId());

        stm.executeUpdate();
        stm.close();
    }

    public void deleteCompany(int user_id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("DELETE FROM companies WHERE user_id = ?");
        stm.setInt(1, user_id);
        int rowsAffected = stm.executeUpdate();
        if(rowsAffected == 0) {
            throw new SQLException("Empresa não encontrada");
        }
        stm.close();
    }
}
