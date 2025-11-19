package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.CandidateExperienceDTO;
import br.com.fiap.models.entities.CandidateExperience;
import br.com.fiap.models.repositories.CandidateExperienceRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("candidate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CandidateExperienceController {

    CandidateExperienceRepository candidateExperienceRepository = new CandidateExperienceRepository();

    public CandidateExperienceController() throws SQLException {
    }

    @POST
    @Path("new-experience")
    public Response addExperienceToCandidate(@HeaderParam("Authorization") String authHeader,
                                             CandidateExperienceDTO candidateExperienceDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (candidateExperienceDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados da experiência inválidos"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateExperienceDTO.getCompanyName() == null ||
                    candidateExperienceDTO.getCompanyName().trim().isEmpty() ||
                    candidateExperienceDTO.getRole() == null ||
                    candidateExperienceDTO.getRole().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Empresa e cargo são obrigatórios"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            CandidateExperience experience = new CandidateExperience();
            experience.setCandidateId(authData.getUserId());
            experience.setCompanyName(candidateExperienceDTO.getCompanyName());
            experience.setRole(candidateExperienceDTO.getRole());
            experience.setDescription(candidateExperienceDTO.getDescription());
            experience.setStartDate(candidateExperienceDTO.getStartDate());
            experience.setEndDate(candidateExperienceDTO.getEndDate());

            candidateExperienceRepository.registerCandidateExperience(experience);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("status", "success", "message", "Experiência adicionada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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

    @PUT
    @Path("update-experience")
    public Response updateCandidateExperience(@HeaderParam("Authorization") String authHeader,
                                              @QueryParam("experienceId") Integer experienceId,
                                              CandidateExperienceDTO candidateExperienceDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (experienceId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "ID da experiência é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            CandidateExperience existingExperience = candidateExperienceRepository.getCandidateExperienceById(experienceId);

            if (existingExperience == null || !authData.getUserId().equals(existingExperience.getCandidateId())) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Experiência do candidato não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateExperienceDTO.getCompanyName() != null &&
                    !candidateExperienceDTO.getCompanyName().trim().isEmpty()) {
                existingExperience.setCompanyName(candidateExperienceDTO.getCompanyName());
            }

            if (candidateExperienceDTO.getRole() != null &&
                    !candidateExperienceDTO.getRole().trim().isEmpty()) {
                existingExperience.setRole(candidateExperienceDTO.getRole());
            }

            if (candidateExperienceDTO.getDescription() != null &&
                    !candidateExperienceDTO.getDescription().trim().isEmpty()) {
                existingExperience.setDescription(candidateExperienceDTO.getDescription());
            }

            if (candidateExperienceDTO.getStartDate() != null &&
                    !candidateExperienceDTO.getStartDate().trim().isEmpty()) {
                existingExperience.setStartDate(candidateExperienceDTO.getStartDate());
            }

            if (candidateExperienceDTO.getEndDate() != null &&
                    !candidateExperienceDTO.getEndDate().trim().isEmpty()) {
                existingExperience.setEndDate(candidateExperienceDTO.getEndDate());
            }

            candidateExperienceRepository.updateCandidateExperience(existingExperience);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Experiência atualizada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao atualizar experiência: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro inesperado: " + e.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("experiences")
    public Response getCandidateExperiences(@HeaderParam("Authorization") String authHeader) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            List<CandidateExperience> experienceList =
                    candidateExperienceRepository.getCandidateExperiencesByCandidateId(authData.getUserId().intValue());

            if (experienceList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error",
                                "message", "Nenhuma experiência encontrada para o usuário: " + authData.getUserId()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            ModelMapper mapper = new ModelMapper();
            List<CandidateExperienceDTO> experienceDTOList = experienceList.stream()
                    .map(exp -> mapper.map(exp, CandidateExperienceDTO.class))
                    .toList();

            if (experienceDTOList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error",
                                "message", "Nenhuma experiência encontrada para o usuário: " + authData.getUserId()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", experienceDTOList))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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

    @DELETE
    @Path("delete-experience")
    public Response deleteCandidateExperience(@HeaderParam("Authorization") String authHeader,
                                              @QueryParam("experienceId") Integer experienceId) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (experienceId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "ID da experiência é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            CandidateExperience experience = candidateExperienceRepository.getCandidateExperienceById(experienceId);

            if (experience == null || !authData.getUserId().equals(experience.getCandidateId())) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Experiência do candidato não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            candidateExperienceRepository.deleteCandidateExperience(experience.getId());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Experiência deletada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
}

