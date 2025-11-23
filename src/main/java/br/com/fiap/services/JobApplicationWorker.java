package br.com.fiap.services;

import br.com.fiap.models.entities.*;

import br.com.fiap.models.prompts.JobFitScorePrompt;
import br.com.fiap.models.repositories.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class JobApplicationWorker {

    private final JobOpeningRepository jobOpeningRepository;
    private final CandidateBehaviorProfileRepository candidateBehaviorProfileRepository;
    private final CandidateEducationRepository candidateEducationRepository;
    private final CandidateSkillsRepository candidateSkillsRepository;
    private final CandidateExperienceRepository candidateExperienceRepository;
    private final JobFitScoreRepository jobFitScoreRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationWorker(JobApplicationRepository jobApplicationRepository,
                                JobOpeningRepository jobOpeningRepository,
                                CandidateBehaviorProfileRepository candidateBehaviorProfileRepository,
                                CandidateEducationRepository candidateEducationRepository,
                                CandidateSkillsRepository candidateSkillsRepository,
                                CandidateExperienceRepository candidateExperienceRepository,
                                JobFitScoreRepository jobFitScoreRepository,
                                UserRepository userRepository,
                                CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
        this.jobOpeningRepository = jobOpeningRepository;
        this.candidateBehaviorProfileRepository = candidateBehaviorProfileRepository;
        this.candidateEducationRepository = candidateEducationRepository;
        this.candidateSkillsRepository = candidateSkillsRepository;
        this.candidateExperienceRepository = candidateExperienceRepository;
        this.jobFitScoreRepository = jobFitScoreRepository;
        this.userRepository = userRepository;
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
        User candidate = userRepository.getUserById(candidateId);
        CandidateBehaviorProfile candidateBehaviorProfile =
                candidateBehaviorProfileRepository.getCandidateBehaviorProfileByCandidateId(candidateId.intValue());
        List<CandidateEducation> candidateEducations =
                candidateEducationRepository.getCandidateEducationsByCandidateId(candidateId.intValue());
        List<CandidateSkills> candidateSkills =
                candidateSkillsRepository.getCandidateSkillsById(candidateId.intValue());
        List<CandidateExperience> candidateExperiences =
                candidateExperienceRepository.getCandidateExperiencesByCandidateId(candidateId.intValue());

        Company company = companyRepository.getCompanyById(job.getCompanyId());

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

        String candidateBehaviorProfileInfo = candidateBehaviorProfile != null
                ? candidateBehaviorProfile.getAiProfile()
                : "- No behavior profile registered";

        String JobsInfo = String.format(
                "Job Title: %s%n" +
                        "Job Description: %s%n" +
                        "Job Requirements: %s%n",
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills()
        );

        String CompanyInfo = String.format(
                "Company Name: %s%n" +
                        "Company Description: %s%n" +
                        "Company Industry: %s%n" +
                        "Company Culture: %s%n",
                company.getName(),
                company.getDescription(),
                company.getIndustry(),
                company.getCulture()
        );

        String compiledString = String.format(
                "Candidate Name: %s%n" +
                        "Candidate Email: %s%n" +
                        "Candidate Behavior Profile:%n%s%n%n" +
                        "Candidate Education:%n%s%n%n" +
                        "Candidate Experience:%n%s%n%n" +
                        "Candidate Skills:%n%s%n%n" +
                        "Job Information:%n%s%n%n" +
                        "Company Information:%n%s%n",
                candidate.getUsername(),
                candidate.getEmail(),
                candidateBehaviorProfileInfo,
                educationInfo,
                experienceInfo,
                skillsInfo,
                JobsInfo,
                CompanyInfo
        );

        OpenAiService openAiService = new OpenAiService();

        String aiResponse = openAiService.Chat(JobFitScorePrompt.SystemPrompt, compiledString);

        int fitScore = Integer.parseInt(aiResponse.trim());

        JobFitScore score = new JobFitScore();
        score.setJobId(job.getId());
        score.setCandidateId(candidateId.intValue());
        score.setTotalScore(fitScore);

        jobFitScoreRepository.create(score);

        return;
    }
}
