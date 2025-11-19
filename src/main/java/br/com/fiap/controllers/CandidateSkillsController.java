package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.CandidateSkillDTO;
import br.com.fiap.models.entities.CandidateSkills;
import br.com.fiap.models.repositories.CandidateSkillsRepository;
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
public class CandidateSkillsController {

    CandidateSkillsRepository candidateSkillsRepository = new CandidateSkillsRepository();

    public CandidateSkillsController() throws SQLException {
    }

    @POST
    @Path("new-skill")
    public Response addSkillToCandidate(@HeaderParam("Authorization") String authHeader, CandidateSkillDTO candidateSkillDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (candidateSkillDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados do candidato inválidos"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateSkillDTO.getLevel() < 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Nível da habilidade inválido"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            Boolean candidateAlreadyHasTheSkill = candidateSkillsRepository.checkIfSkillExists(candidateSkillDTO.getSkillName(), authData.getUserId());

            if (candidateAlreadyHasTheSkill) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("status", "error", "message", "Habilidade já cadastrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            CandidateSkills candidateSkills = new CandidateSkills();
            candidateSkills.setCandidateId(authData.getUserId());
            candidateSkills.setSkillName(candidateSkillDTO.getSkillName().toLowerCase());
            candidateSkills.setLevel(candidateSkillDTO.getLevel());
            candidateSkills.setCandidateId(authData.getUserId());

            candidateSkillsRepository.addSkilltoCandidate(candidateSkills);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("status", "success", "message", "Habilidade adicionada com sucesso"))
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
    @Path("update-skill")
    public Response updateCandidateSkill(@HeaderParam("Authorization") String authHeader, CandidateSkillDTO candidateSkillDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            CandidateSkills existingSkill = candidateSkillsRepository.getCandidateSkillByName(candidateSkillDTO.getSkillName().toLowerCase(), authData.getUserId());

            if (existingSkill == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Habilidade do candidato não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateSkillDTO.getSkillName() != null && !candidateSkillDTO.getSkillName().trim().isEmpty()) {
                existingSkill.setSkillName(candidateSkillDTO.getSkillName().toLowerCase());
            }

            if (candidateSkillDTO.getSkillName() != null && candidateSkillDTO.getLevel() > 0) {
                existingSkill.setLevel(candidateSkillDTO.getLevel());
            }

            candidateSkillsRepository.updateCandidateSkill(existingSkill);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Habilidade atualizada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message", "Erro ao atualizar habilidade: " + e.getMessage()))
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
    @Path("skills")
    public Response getCandidateSkills(@HeaderParam("Authorization") String authHeader) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            List<CandidateSkills> candidateSkillsList = candidateSkillsRepository.getCandidateSkillsById(authData.getUserId().intValue());

            if (candidateSkillsList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Nenhuma habilidade encontrada para o usuário: " + authData.getUserId()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            ModelMapper mapper = new ModelMapper();
            List<CandidateSkillDTO> candidateSkillsDTOList = candidateSkillsList.stream()
                    .map(skill -> mapper.map(skill, CandidateSkillDTO.class))
                    .toList();

            if (candidateSkillsDTOList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Nenhuma habilidade encontrada para o usuário: " + authData.getUserId()))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", candidateSkillsDTOList))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message",  e.getMessage()))
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
    @Path("delete-skill")
    public Response deleteCandidateSkill(@HeaderParam("Authorization") String authHeader, @QueryParam("skillName") String skillName) {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            CandidateSkills candidateSkills = candidateSkillsRepository.getCandidateSkillByName(skillName.toLowerCase(), authData.getUserId());

            candidateSkillsRepository.deleteCandidateSkill(candidateSkills.getId());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Habilidade deletada com sucesso"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("status", "error", "message",  e.getMessage()))
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
