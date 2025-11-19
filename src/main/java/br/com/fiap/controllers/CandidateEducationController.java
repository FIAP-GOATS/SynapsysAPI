package br.com.fiap.controllers;

import br.com.fiap.authentication.AuthUtil;
import br.com.fiap.models.dto.Request.AuthDTO;
import br.com.fiap.models.dto.Request.CandidateEducationDTO;
import br.com.fiap.models.entities.CandidateEducation;
import br.com.fiap.models.repositories.CandidateEducationRepository;
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
public class CandidateEducationController {

    CandidateEducationRepository candidateEducationRepository = new CandidateEducationRepository();

    public CandidateEducationController() throws SQLException {
    }

    @POST
    @Path("new-education")
    public Response AddEducationToCandidate(@HeaderParam("Authorization") String authHeader, CandidateEducationDTO candidateEducationDTO) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            if (candidateEducationDTO == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Dados de educação inválidos"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateEducationDTO.getInstitution() == null || candidateEducationDTO.getInstitution().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Nome da instituição é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateEducationDTO.getLevel() == null || candidateEducationDTO.getLevel().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Grau de escolaridade é obrigatório"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateEducationDTO.getStartDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Data de início é obrigatória"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateEducationDTO.getEndDate() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("status", "error", "message", "Data de término é obrigatória"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            CandidateEducation candidateEducation = new CandidateEducation();
            candidateEducation.setCandidateId(authData.getUserId());
            candidateEducation.setInstitution(candidateEducationDTO.getInstitution());
            candidateEducation.setCourse(candidateEducationDTO.getCourse());
            candidateEducation.setLevel(candidateEducationDTO.getLevel());
            candidateEducation.setStartDate(candidateEducationDTO.getStartDate());
            candidateEducation.setEndDate(candidateEducationDTO.getEndDate());

            candidateEducationRepository.registerCandidateEducation(candidateEducation);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("status", "success", "message", "Educação adicionada com sucesso"))
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
    @Path("update-education")
    public Response updateCandidateEducation(@HeaderParam("Authorization") String authHeader,
                                             CandidateEducationDTO candidateEducationDTO,
                                             @QueryParam("institution") String institution,
                                             @QueryParam("course") String course) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            CandidateEducation candidateEducation = candidateEducationRepository.getCandidateEducationByInstitutionAndCourse(institution, course);
            if (candidateEducation == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Educação do candidato não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if(!candidateEducation.getCandidateId().equals(authData.getUserId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para atualizar esta educação"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if (candidateEducationDTO.getInstitution() != null && !candidateEducationDTO.getInstitution().trim().isEmpty()) {
                candidateEducation.setInstitution(candidateEducationDTO.getInstitution());
            }
            if (candidateEducationDTO.getCourse() != null && !candidateEducationDTO.getCourse().trim().isEmpty()) {
                candidateEducation.setCourse(candidateEducationDTO.getCourse());
            }
            if (candidateEducationDTO.getLevel() != null && !candidateEducationDTO.getLevel().trim().isEmpty()) {
                candidateEducation.setLevel(candidateEducationDTO.getLevel());
            }
            if (candidateEducationDTO.getStartDate() != null) {
                candidateEducation.setStartDate(candidateEducationDTO.getStartDate());
            }
            if (candidateEducationDTO.getEndDate() != null) {
                candidateEducation.setEndDate(candidateEducationDTO.getEndDate());
            }

            candidateEducationRepository.updateCandidateEducation(candidateEducation);

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Educação atualizada com sucesso"))
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

    @GET
    @Path("education")
     public Response getCandidateEducation(@HeaderParam("Authorization") String authHeader) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            List<CandidateEducation> educationList = candidateEducationRepository.getCandidateEducationsByCandidateId(authData.getUserId().intValue());

            if(educationList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Nenhum registro de educação encontrado para o candidato"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            ModelMapper mapper = new ModelMapper();
            List<CandidateEducationDTO> educationDTOs = educationList.stream()
                    .map(education -> mapper.map(education, CandidateEducationDTO.class))
                    .toList();

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "data", educationDTOs))
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
    @Path("delete-education")
    public Response deleteCandidateEducation(@HeaderParam("Authorization") String authHeader,
                                             @QueryParam("institution") String institution,
                                             @QueryParam("course") String course) throws SQLException {
        try {
            AuthDTO authData = AuthUtil.extractUser(authHeader);

            CandidateEducation candidateEducation = candidateEducationRepository.getCandidateEducationByInstitutionAndCourse(institution, course);

            if (candidateEducation == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("status", "error", "message", "Educação do candidato não encontrada"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            if(!candidateEducation.getCandidateId().equals(authData.getUserId())) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(Map.of("status", "error", "message", "Você não tem permissão para deletar esta educação"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            candidateEducationRepository.deleteCandidateEducation(candidateEducation.getId());

            return Response.status(Response.Status.OK)
                    .entity(Map.of("status", "success", "message", "Educação deletada com sucesso"))
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

