package br.com.fiap.models.dto.Request;

import java.util.List;

public class CandidateSurveyDTO {

    // Propósito Profissional
    private String dailyMotivation;
    private String meaningfulWork;
    private List<String> purposePhrases;

    // Valores Pessoais
    private Integer ethicsFirst;
    private Integer collaborationOverCompetition;
    private Integer diversityStrength;
    private Integer learningOverBeingRight;
    private Integer transparencyValue;
    private Integer structureOverFlexibility;
    private Integer belongingImportance;
    private Integer workLifeBalance;

    // Estilo de Trabalho
    private String problemSolvingStyle;
    private String workPreference;
    private String deadlineApproach;
    private String selfPerception;
    private String environmentPreference;

    // Interesses e Motivações
    private List<String> engagementFactors;
    private String companyType;

    // Competências Técnicas
    private String technicalSkills;
    private String confidentAreas;
    private String developmentAreas;
    private String professionalChallenge;

    public String getDailyMotivation() {
        return dailyMotivation;
    }

    public void setDailyMotivation(String dailyMotivation) {
        this.dailyMotivation = dailyMotivation;
    }

    public String getMeaningfulWork() {
        return meaningfulWork;
    }

    public void setMeaningfulWork(String meaningfulWork) {
        this.meaningfulWork = meaningfulWork;
    }

    public List<String> getPurposePhrases() {
        return purposePhrases;
    }

    public void setPurposePhrases(List<String> purposePhrases) {
        this.purposePhrases = purposePhrases;
    }

    public Integer getEthicsFirst() {
        return ethicsFirst;
    }

    public void setEthicsFirst(Integer ethicsFirst) {
        this.ethicsFirst = ethicsFirst;
    }

    public Integer getCollaborationOverCompetition() {
        return collaborationOverCompetition;
    }

    public void setCollaborationOverCompetition(Integer collaborationOverCompetition) {
        this.collaborationOverCompetition = collaborationOverCompetition;
    }

    public Integer getDiversityStrength() {
        return diversityStrength;
    }

    public void setDiversityStrength(Integer diversityStrength) {
        this.diversityStrength = diversityStrength;
    }

    public Integer getLearningOverBeingRight() {
        return learningOverBeingRight;
    }

    public void setLearningOverBeingRight(Integer learningOverBeingRight) {
        this.learningOverBeingRight = learningOverBeingRight;
    }

    public Integer getTransparencyValue() {
        return transparencyValue;
    }

    public void setTransparencyValue(Integer transparencyValue) {
        this.transparencyValue = transparencyValue;
    }

    public Integer getStructureOverFlexibility() {
        return structureOverFlexibility;
    }

    public void setStructureOverFlexibility(Integer structureOverFlexibility) {
        this.structureOverFlexibility = structureOverFlexibility;
    }

    public Integer getBelongingImportance() {
        return belongingImportance;
    }

    public void setBelongingImportance(Integer belongingImportance) {
        this.belongingImportance = belongingImportance;
    }

    public Integer getWorkLifeBalance() {
        return workLifeBalance;
    }

    public void setWorkLifeBalance(Integer workLifeBalance) {
        this.workLifeBalance = workLifeBalance;
    }

    public String getProblemSolvingStyle() {
        return problemSolvingStyle;
    }

    public void setProblemSolvingStyle(String problemSolvingStyle) {
        this.problemSolvingStyle = problemSolvingStyle;
    }

    public String getWorkPreference() {
        return workPreference;
    }

    public void setWorkPreference(String workPreference) {
        this.workPreference = workPreference;
    }

    public String getDeadlineApproach() {
        return deadlineApproach;
    }

    public void setDeadlineApproach(String deadlineApproach) {
        this.deadlineApproach = deadlineApproach;
    }

    public String getSelfPerception() {
        return selfPerception;
    }

    public void setSelfPerception(String selfPerception) {
        this.selfPerception = selfPerception;
    }

    public String getEnvironmentPreference() {
        return environmentPreference;
    }

    public void setEnvironmentPreference(String environmentPreference) {
        this.environmentPreference = environmentPreference;
    }

    public List<String> getEngagementFactors() {
        return engagementFactors;
    }

    public void setEngagementFactors(List<String> engagementFactors) {
        this.engagementFactors = engagementFactors;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getTechnicalSkills() {
        return technicalSkills;
    }

    public void setTechnicalSkills(String technicalSkills) {
        this.technicalSkills = technicalSkills;
    }

    public String getConfidentAreas() {
        return confidentAreas;
    }

    public void setConfidentAreas(String confidentAreas) {
        this.confidentAreas = confidentAreas;
    }

    public String getDevelopmentAreas() {
        return developmentAreas;
    }

    public void setDevelopmentAreas(String developmentAreas) {
        this.developmentAreas = developmentAreas;
    }

    public String getProfessionalChallenge() {
        return professionalChallenge;
    }

    public void setProfessionalChallenge(String professionalChallenge) {
        this.professionalChallenge = professionalChallenge;
    }

    @Override
    public String toString() {
        return toStringProfile();
    }

    public String toStringProfile() {

        return "=== Propósito Profissional ===\n" +
                "Motivação diária: " + nullSafe(dailyMotivation) + "\n" +
                "Trabalho significativo: " + nullSafe(meaningfulWork) + "\n" +
                "Frases de propósito: " + listSafe(purposePhrases) + "\n\n" +
                "=== Valores Pessoais ===\n" +
                "Ética em primeiro lugar: " + nullSafe(ethicsFirst) + "\n" +
                "Colaboração sobre competição: " + nullSafe(collaborationOverCompetition) + "\n" +
                "Diversidade como força: " + nullSafe(diversityStrength) + "\n" +
                "Aprender > estar certo: " + nullSafe(learningOverBeingRight) + "\n" +
                "Transparência: " + nullSafe(transparencyValue) + "\n" +
                "Estrutura > flexibilidade: " + nullSafe(structureOverFlexibility) + "\n" +
                "Importância de pertencimento: " + nullSafe(belongingImportance) + "\n" +
                "Work-life balance: " + nullSafe(workLifeBalance) + "\n\n" +
                "=== Estilo de Trabalho ===\n" +
                "Estilo de resolução de problemas: " + nullSafe(problemSolvingStyle) + "\n" +
                "Preferência de trabalho: " + nullSafe(workPreference) + "\n" +
                "Abordagem a prazos: " + nullSafe(deadlineApproach) + "\n" +
                "Autopercepção: " + nullSafe(selfPerception) + "\n" +
                "Ambiente preferido: " + nullSafe(environmentPreference) + "\n\n" +
                "=== Interesses e Motivações ===\n" +
                "Fatores de engajamento: " + listSafe(engagementFactors) + "\n" +
                "Tipo de empresa desejada: " + nullSafe(companyType) + "\n\n" +
                "=== Competências Técnicas ===\n" +
                "Habilidades técnicas: " + nullSafe(technicalSkills) + "\n" +
                "Áreas de confiança: " + nullSafe(confidentAreas) + "\n" +
                "Áreas para desenvolvimento: " + nullSafe(developmentAreas) + "\n" +
                "Desafio profissional desejado: " + nullSafe(professionalChallenge) + "\n";
    }

    private String nullSafe(Object o) {
        return o == null ? "-" : o.toString();
    }

    private String listSafe(List<?> list) {
        return (list == null || list.isEmpty()) ? "-" : String.join(", ", list.stream().map(String::valueOf).toList());
    }

}
