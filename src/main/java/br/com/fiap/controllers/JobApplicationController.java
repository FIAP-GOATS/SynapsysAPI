package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.JobApplicationDTO;
import br.com.fiap.models.entities.JobApplication;
import br.com.fiap.models.entities.JobOpening;
import br.com.fiap.models.repositories.*;
import br.com.fiap.services.JobApplicationWorker;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("job/application")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JobApplicationController {

    public JobApplicationController() throws SQLException {
    }

    JobApplicationRepository jobApplicationRepository = new JobApplicationRepository();
    JobOpeningRepository jobOpeningRepository = new JobOpeningRepository();
    CandidateRepository candidateRepository = new CandidateRepository();
    CandidateBehaviorProfileRepository candidateBehaviorProfileRepository = new CandidateBehaviorProfileRepository();
    CandidateEducationRepository candidateEducationRepository = new CandidateEducationRepository();
    CandidateSkillsRepository candidateSkillsRepository = new CandidateSkillsRepository();
    CandidateExperienceRepository candidateExperienceRepository = new CandidateExperienceRepository();
    JobFitScoreRepository jobFitScoreRepository = new JobFitScoreRepository();


    JobApplicationWorker jobApplicationWorker = new JobApplicationWorker(
            jobApplicationRepository,
            jobOpeningRepository,
            candidateRepository,
            candidateBehaviorProfileRepository,
            candidateEducationRepository,
            candidateSkillsRepository,
            candidateExperienceRepository,
            jobFitScoreRepository
    );


    /// ------------------ JOB APPLICATION METHODS ------------------ ///

    /// Create a new job application
    @POST
    @Path("/create")
    public Response createJobApplication(@HeaderParam("Authorization") String authHeader, JobApplicationDTO jobApplicationDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (jobApplicationDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados da candidatura não informados"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobApplicationDTO.getJobId() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "ID da vaga é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            JobApplication jobApplication = new JobApplication();
            jobApplication.setJobId(jobApplicationDTO.getJobId());
            jobApplication.setCandidateId(authData.getUserId());
            jobApplication.setStatus("pending");
            jobApplicationRepository.createJobApplication(jobApplication);

            jobApplicationWorker.run(jobApplication);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Candidatura criada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao criar candidatura: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get job applications by candidate (authenticated user)
    @GET
    @Path("/list")
    public Response getJobApplicationsByCandidate(@HeaderParam("Authorization") String authHeader) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            List<JobApplication> jobApplications = jobApplicationRepository.getJobApplicationsByCandidateId(authData.getUserId().intValue());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", jobApplications))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao listar candidaturas: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Get job application by ID
    @GET
    @Path("/get")
    public Response getJobApplicationById(@HeaderParam("Authorization") String authHeader, @QueryParam("id") int id) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobApplication jobApplication = jobApplicationRepository.getJobApplicationById(id);

            if (jobApplication == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Candidatura não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (!jobApplication.getCandidateId().equals(authData.getUserId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para visualizar esta candidatura"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", jobApplication))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("status", "error", "message", e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Update job application
    @PUT
    @Path("/update")
    public Response updateJobApplication(@HeaderParam("Authorization") String authHeader, @QueryParam("id") int id, JobApplicationDTO jobApplicationDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobApplication jobApplication = jobApplicationRepository.getJobApplicationById(id);

            if (jobApplication == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Candidatura não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            JobOpening jobOpening = jobOpeningRepository.getJobOpeningById(jobApplication.getJobId());
            if (jobOpening == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Oferta não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (! (jobOpening.getCompanyId() == authData.getUserId().intValue()) ) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para editar esta candidatura"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (jobApplicationDTO.getJobId() > 0) {
                jobApplication.setJobId(jobApplicationDTO.getJobId());
            }

            jobApplicationRepository.updateJobApplication(jobApplication);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Candidatura atualizada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao atualizar candidatura: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Delete job application - INACTIVE
    @DELETE
    @Path("")
    public Response deleteJobApplication(@HeaderParam("Authorization") String authHeader, @QueryParam("id") int id) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            JobApplication jobApplication = jobApplicationRepository.getJobApplicationById(id);

            if (jobApplication == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Candidatura não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (!jobApplication.getCandidateId().equals(authData.getUserId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para deletar esta candidatura"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            jobApplicationRepository.deleteJobApplication(id);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Candidatura deletada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao deletar candidatura: " + e.getMessage()))
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
