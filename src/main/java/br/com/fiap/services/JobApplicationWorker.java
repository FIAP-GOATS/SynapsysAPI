package br.com.fiap.services;

import br.com.fiap.models.entities.*;
import static br.com.fiap.models.prompts.JobFitScore.SystemPrompt;
import br.com.fiap.models.repositories.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class JobApplicationWorker {

    private final JobOpeningRepository jobOpeningRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateBehaviorProfileRepository candidateBehaviorProfileRepository;
    private final CandidateEducationRepository candidateEducationRepository;
    private final CandidateSkillsRepository candidateSkillsRepository;
    private final CandidateExperienceRepository candidateExperienceRepository;
    private final JobFitScoreRepository jobFitScoreRepository;

    public JobApplicationWorker(JobApplicationRepository jobApplicationRepository,
                                JobOpeningRepository jobOpeningRepository,
                                CandidateRepository candidateRepository,
                                CandidateBehaviorProfileRepository candidateBehaviorProfileRepository,
                                CandidateEducationRepository candidateEducationRepository,
                                CandidateSkillsRepository candidateSkillsRepository,
                                CandidateExperienceRepository candidateExperienceRepository,
                                JobFitScoreRepository jobFitScoreRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
        this.candidateRepository = candidateRepository;
        this.candidateBehaviorProfileRepository = candidateBehaviorProfileRepository;
        this.candidateEducationRepository = candidateEducationRepository;
        this.candidateSkillsRepository = candidateSkillsRepository;
        this.candidateExperienceRepository = candidateExperienceRepository;
        this.jobFitScoreRepository = jobFitScoreRepository;
    }

    public void run(JobApplication jobApplication)
            throws SQLException, IOException, InterruptedException, NullPointerException, Exception {

        try {
            execute(jobApplication);

        } catch (SQLException e) {
            System.err.println("[JobApplicationWorker] SQL ERROR: " + e.getMessage());
            throw e;

        } catch (IOException e) {
            System.err.println("[JobApplicationWorker] IO ERROR: " + e.getMessage());
            throw e;

        } catch (InterruptedException e) {
            System.err.println("[JobApplicationWorker] INTERRUPTED: " + e.getMessage());
            Thread.currentThread().interrupt();
            throw e;

        } catch (NullPointerException e) {
            System.err.println("[JobApplicationWorker] NULL DATA: " + e.getMessage());
            throw e;

        } catch (Exception e) {
            System.err.println("[JobApplicationWorker] UNEXPECTED ERROR: " + e.getMessage());
            throw e;
        }
    }

    // -------------------------------------------------------
    // LÓGICA PRINCIPAL
    // -------------------------------------------------------
    private void execute(JobApplication jobApplication)
            throws SQLException, IOException, InterruptedException {

        Long candidateId = jobApplication.getCandidateId();

        JobOpening job = jobOpeningRepository.getJobOpeningById(jobApplication.getJobId());
        Candidate candidate = candidateRepository.getCandidateByUserId(candidateId);
        CandidateBehaviorProfile candidateBehaviorProfile =
                candidateBehaviorProfileRepository.getCandidateBehaviorProfileByCandidateId(candidateId.intValue());
        List<CandidateEducation> candidateEducations =
                candidateEducationRepository.getCandidateEducationsByCandidateId(candidateId.intValue());
        List<CandidateSkills> candidateSkills =
                candidateSkillsRepository.getCandidateSkillsById(candidateId.intValue());
        List<CandidateExperience> candidateExperiences =
                candidateExperienceRepository.getCandidateExperiencesByCandidateId(candidateId.intValue());

        String educationInfo = candidateEducations == null || candidateEducations.isEmpty()
                ? "- No education information registered"
                : candidateEducations.stream()
                .map(edu -> String.format(
                        "- Institution: %s%n" +
                                "  Course: %s%n" +
                                "  Level: %s",
                        edu.getInstitution(),
                        edu.getCourse(),
                        edu.getLevel()
                ))
                .collect(Collectors.joining(System.lineSeparator()));

        String experienceInfo = candidateExperiences == null || candidateExperiences.isEmpty()
                ? "- No experience information registered"
                : candidateExperiences.stream()
                .map(exp -> String.format(
                        "- Company Name: %s%n" +
                                "  Role: %s%n" +
                                "  Description: %s%n" +
                                "  Start Date: %s%n" +
                                "  End Date: %s",
                        exp.getCompanyName(),
                        exp.getRole(),
                        exp.getDescription(),
                        exp.getStartDate(),
                        exp.getEndDate()
                ))
                .collect(Collectors.joining(System.lineSeparator()));

        String skillsInfo = candidateSkills == null || candidateSkills.isEmpty()
                ? "- No skills registered"
                : candidateSkills.stream()
                .map(skill -> String.format(
                        "- Name: %s%n" +
                                "  Level: %d",
                        skill.getSkillName(),
                        skill.getLevel()
                ))
                .collect(Collectors.joining(System.lineSeparator()));

        String compiledData = String.format(
                """
                --Candidate Information--
                General:
                - Purpose: %s
                - Work Style: %s
                - Interests: %s
                - Behavior Profile: %s

                Education:
                %s

                Experience:
                %s

                Skills:
                %s

                --Job Information--
                - Title: %s
                - Description: %s
                - Work Model: %s
                - Required Skills: %s
                """,
                candidate.getPurpose(),
                candidate.getWorkStyle(),
                candidate.getInterests(),
                candidateBehaviorProfile.getAiProfile(),
                educationInfo,
                experienceInfo,
                skillsInfo,
                job.getTitle(),
                job.getDescription(),
                job.getWorkModel(),
                job.getRequiredSkills()
        );

        OpenAiService openAiService = new OpenAiService();
        String score = openAiService.Chat(SystemPrompt, compiledData);

        JobFitScore jobFitScore = new JobFitScore(
                job.getId(),
                candidate.getUserId().intValue(),
                0,
                0,
                Double.parseDouble(score)
        );

        JobFitScore newJobFitScore = jobFitScoreRepository.create(jobFitScore);
        return;
    }
}
