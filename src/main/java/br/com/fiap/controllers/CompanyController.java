package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.entities.Company;
import br.com.fiap.models.repositories.CompanyRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Map;

@Path("company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyController {

    public CompanyController() throws SQLException {
    }

    CompanyRepository companyRepository = new CompanyRepository();

    /// ------------------ COMPANY METHODS ------------------ ///

    /// Create a new company profile
    @POST
    @Path("/create")
    public Response createCompany(@HeaderParam("Authorization") String authHeader, Company companyBody) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (companyBody == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados da empresa não informados"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (companyBody.getName() == null || companyBody.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Nome da empresa é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (companyBody.getDescription() == null || companyBody.getDescription().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Descrição da empresa é obrigatória"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (companyBody.getIndustry() == null || companyBody.getIndustry().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Setor/Indústria é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (companyBody.getCulture() == null || companyBody.getCulture().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Cultura da empresa é obrigatória"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Company company = new Company();
            company.setUserId(authData.getUserId().intValue());
            company.setName(companyBody.getName());
            company.setDescription(companyBody.getDescription());
            company.setIndustry(companyBody.getIndustry());
            company.setCulture(companyBody.getCulture());

            companyRepository.registerCompany(company);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("status", "success", "message", "Empresa cadastrada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao cadastrar empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get an authenticated company profile
    @GET
    @Path("/me")
    public Response getMyCompany(@HeaderParam("Authorization") String authHeader) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            Company company = companyRepository.getCompanyById(authData.getUserId().intValue());

            if (company == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Empresa não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", company))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao buscar empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get a company profile by name
    @GET
    @Path("/{name}")
    public Response getCompanyByName(@PathParam("name") String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Nome da empresa é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Company company = companyRepository.getCompanyByName(name);

            if (company == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Empresa não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", company))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao buscar empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Update an authenticated company profile
    @PUT
    @Path("/update")
    public Response updateCompany(@HeaderParam("Authorization") String authHeader, Company companyBody) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            Company company = companyRepository.getCompanyById(authData.getUserId().intValue());

            if (company == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Empresa não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (companyBody.getName() != null && !companyBody.getName().trim().isEmpty()) {
                company.setName(companyBody.getName());
            }

            if (companyBody.getDescription() != null && !companyBody.getDescription().trim().isEmpty()) {
                company.setDescription(companyBody.getDescription());
            }

            if (companyBody.getIndustry() != null && !companyBody.getIndustry().trim().isEmpty()) {
                company.setIndustry(companyBody.getIndustry());
            }

            if (companyBody.getCulture() != null && !companyBody.getCulture().trim().isEmpty()) {
                company.setCulture(companyBody.getCulture());
            }

            companyRepository.updateCompany(company);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Perfil da empresa atualizado com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao atualizar empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Delete an authenticated company profile
    @DELETE
    @Path("/delete")
    public Response deleteCompany(@HeaderParam("Authorization") String authHeader) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            companyRepository.deleteCompany(authData.getUserId().intValue());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Empresa deletada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao deletar empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
