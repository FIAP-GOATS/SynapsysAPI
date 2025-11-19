package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.JobOpeningDTO;
import br.com.fiap.models.entities.JobFitScore;
import br.com.fiap.models.entities.JobOpening;
import br.com.fiap.models.repositories.JobFitScoreRepository;
import br.com.fiap.models.repositories.JobOpeningRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("job")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobOpeningController {

    public JobOpeningController() throws SQLException {
    }

    JobOpeningRepository jobOpeningRepository = new JobOpeningRepository();
    JobFitScoreRepository jobFitScoreRepository = new JobFitScoreRepository();

    /// ------------------ JOB OPENING METHODS ------------------ ///

    /// Create a new job opening
    @POST
    @Path("/create")
    public Response createJobOpening(@HeaderParam("Authorization") String authHeader, JobOpeningDTO jobOpeningDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (jobOpeningDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados da vaga n�o informados"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getTitle() == null || jobOpeningDTO.getTitle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "T�tulo � obrigat�rio"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getDescription() == null || jobOpeningDTO.getDescription().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Descricaoo � obrigat�ria"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getSalary() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Sal�rio deve ser maior que zero"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getWorkModel() == null || jobOpeningDTO.getWorkModel().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Modelo de trabalho � obrigat�rio"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getRequiredSkills() == null || jobOpeningDTO.getRequiredSkills().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Habilidades requeridas s�o obrigat�rias"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            JobOpening jobOpening = new JobOpening();
            jobOpening.setCompanyId(authData.getUserId().intValue());
            jobOpening.setTitle(jobOpeningDTO.getTitle());
            jobOpening.setDescription(jobOpeningDTO.getDescription());
            jobOpening.setSalary(jobOpeningDTO.getSalary());
            jobOpening.setWorkModel(jobOpeningDTO.getWorkModel());
            jobOpening.setRequiredSkills(jobOpeningDTO.getRequiredSkills());
            jobOpeningRepository.createJobOpening(jobOpening);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Vaga cadastrada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao cadastrar vaga: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get all job openings from the company
    @GET
    @Path("/list")
    public Response getJobOpeningsByCompany(@HeaderParam("Authorization") String authHeader) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            List<JobOpening> jobOpenings =
                    jobOpeningRepository.getJobsOpeningByCompanyId(authData.getUserId().intValue());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", jobOpenings))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message",
                            "Erro ao buscar vagas da empresa: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message",
                            "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get all job openings with no restriction
    @GET
    @Path("/all")
    public Response getAllJobOpenings() throws SQLException {
        try {
            var jobOpenings = jobOpeningRepository.getAllJobOpenings();

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", jobOpenings))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao buscar vagas: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Update job opening
    @PUT
    @Path("/update")
    public Response updateJobOpening(@HeaderParam("Authorization") String authHeader, @QueryParam("id") int id, JobOpeningDTO jobOpeningDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobOpening jobOpening = jobOpeningRepository.getJobOpeningById(id);

            if (jobOpening == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Vaga n�o encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpening.getCompanyId() != authData.getUserId().intValue()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Voc� n�o tem permiss�o para editar esta vaga"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpeningDTO.getTitle() != null && !jobOpeningDTO.getTitle().trim().isEmpty()) {
                jobOpening.setTitle(jobOpeningDTO.getTitle());
            }

            if (jobOpeningDTO.getDescription() != null && !jobOpeningDTO.getDescription().trim().isEmpty()) {
                jobOpening.setDescription(jobOpeningDTO.getDescription());
            }

            if (jobOpeningDTO.getSalary() > 0) {
                jobOpening.setSalary(jobOpeningDTO.getSalary());
            }

            if (jobOpeningDTO.getWorkModel() != null && !jobOpeningDTO.getWorkModel().trim().isEmpty()) {
                jobOpening.setWorkModel(jobOpeningDTO.getWorkModel());
            }

            if (jobOpeningDTO.getRequiredSkills() != null && !jobOpeningDTO.getRequiredSkills().trim().isEmpty()) {
                jobOpening.setRequiredSkills(jobOpeningDTO.getRequiredSkills());
            }

            jobOpeningRepository.updateJobOpening(jobOpening);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Vaga atualizada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao atualizar vaga: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Delete job opening
    @DELETE
    @Path("/delete")
    public Response deleteJobOpening(@HeaderParam("Authorization") String authHeader, @QueryParam("id") int id) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobOpening jobOpening = jobOpeningRepository.getJobOpeningById(id);

            if (jobOpening == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Vaga n�o encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobOpening.getCompanyId() != authData.getUserId().intValue()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Voc� n�o tem permissao para deletar esta vaga"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            jobOpeningRepository.deleteJobOpening(id);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Vaga deletada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao deletar vaga: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get job scoreboard
    @GET
    @Path("/scoreboard")
    public Response getJobScoreboard(@HeaderParam("Authorization") String authHeader, @QueryParam("jobId") int jobId) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobOpening jobOpening = jobOpeningRepository.getJobOpeningById(jobId);

            if (jobOpening == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Vaga não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            /*if (jobOpening.getCompanyId() != authData.getUserId().intValue()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para visualizar o scoreboard desta vaga"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }*/

            List<JobFitScore> scores = jobFitScoreRepository.getByJobId(jobId);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", scores))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao buscar scoreboard: " + e.getMessage()))
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
