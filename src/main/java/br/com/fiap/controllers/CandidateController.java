package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.CandidateDTO;
import br.com.fiap.models.dto.Request.CandidateSurveyDTO;
import br.com.fiap.models.entities.*;
import br.com.fiap.models.prompts.CreateBehaviorProfile;
import br.com.fiap.models.repositories.CandidateBehaviorProfileRepository;
import br.com.fiap.models.repositories.CandidateRepository;
import br.com.fiap.services.OpenAiService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

@Path("candidate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CandidateController {

    public CandidateController() throws SQLException {
    }

    CandidateRepository candidateRepository = new CandidateRepository();
    CandidateBehaviorProfileRepository candidateBehaviorProfileRepository = new CandidateBehaviorProfileRepository();

    /// ------------------ CANDIDATE METHODS ------------------ ///

    /// Create a new candidate
    @POST
    @Path("/create")
    public Response createCandidate(@HeaderParam("Authorization") String authHeader, CandidateDTO candidateDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (candidateDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados do candidato não informados"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateDTO.getDisplayName() == null || candidateDTO.getDisplayName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Nome de exibição é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateDTO.getPurpose() == null || candidateDTO.getPurpose().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Objetivo é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateDTO.getWorkStyle() == null || candidateDTO.getWorkStyle().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Estilo de trabalho é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateDTO.getInterests() == null || candidateDTO.getInterests().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Interesses são obrigatórios"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Boolean candidateExists = candidateRepository.checkIfCandidateExists(authData.getUserId());
            if (candidateExists) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Candidato já cadastrado"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Candidate candidate = new Candidate();
            candidate.setUserId(authData.getUserId());
            candidate.setDisplayName(candidateDTO.getDisplayName());
            candidate.setPurpose(candidateDTO.getPurpose());
            candidate.setWorkStyle(candidateDTO.getWorkStyle());
            candidate.setInterests(candidateDTO.getInterests());
            candidateRepository.registerCandidate(candidate);


            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Currículo cadastrado com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao cadastrar candidato: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Update candidate profile.
    @PUT
    @Path("/update-profile")
    public Response updateCandidateProfile(@HeaderParam("Authorization") String authHeader, CandidateDTO candidateDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            Candidate candidate = candidateRepository.getCandidateByUserId(authData.getUserId());

            if (candidate == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Candidato não encontrado"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateDTO.getDisplayName() != null && !candidateDTO.getDisplayName().trim().isEmpty()) {
                candidate.setDisplayName(candidateDTO.getDisplayName());
            }

            if (candidateDTO.getPurpose() != null && !candidateDTO.getPurpose().trim().isEmpty()) {
                candidate.setPurpose(candidateDTO.getPurpose());
            }

            if (candidateDTO.getWorkStyle() != null && !candidateDTO.getWorkStyle().trim().isEmpty()) {
                candidate.setWorkStyle(candidateDTO.getWorkStyle());
            }

            if (candidateDTO.getInterests() != null && !candidateDTO.getInterests().trim().isEmpty()) {
                candidate.setInterests(candidateDTO.getInterests());
            }

            candidateRepository.updateCandidate(candidate);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Perfil atualizado com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao cadastrar candidato: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    /// Behavior
    @POST
    @Path("/create-behavior-profile")
    public Response createBehaviorProfile(@HeaderParam("Authorization") String authHeader, CandidateSurveyDTO candidateSurveyDTO) {
        {
            try {
                AuthDTO authData = AuthUtil.extractUser(authHeader);

                if (candidateSurveyDTO == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(Map.of("status", "error", "message", "Questionário não informado"))
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                }

                OpenAiService openai = new OpenAiService();

                String behaviorProfile = openai.Chat(CreateBehaviorProfile.systemPrompt, candidateSurveyDTO.toStringProfile());

                System.out.println("OpenAI retornou: " + behaviorProfile);

                CandidateBehaviorProfile candidateBehaviorProfile = new CandidateBehaviorProfile();
                candidateBehaviorProfile.setCandidateId(authData.getUserId());
                candidateBehaviorProfile.setAiProfile(behaviorProfile);

                candidateBehaviorProfileRepository.createCandidateBehaviorProfile(candidateBehaviorProfile);

                return Response.status(Response.Status.OK)
                        .entity(Map.of("status", "success", "message", "Perfil comportamental cadastrado com sucesso"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Map.of("status", "error", "message", "Erro ao cadastrar perfil comportamental " + e.getMessage()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }
    }
}
