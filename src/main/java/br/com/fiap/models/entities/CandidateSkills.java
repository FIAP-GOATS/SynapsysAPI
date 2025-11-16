package br.com.fiap.models.entities;

public class CandidateSkills {
    private int id;
    private long candidateId;
    private String skillName;
    private int level;

    public CandidateSkills(long candidateId, String skillName, int level) {
        this.candidateId = candidateId;
        this.skillName = skillName;
        setLevel(level);
    }

    public CandidateSkills(String skillName, int level) {
        this.skillName = skillName;
        setLevel(level);
    }

    public CandidateSkills() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getCandidateId() { return candidateId; }
    public void setCandidateId(long candidateId) { this.candidateId = candidateId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public int getLevel() { return level; }
    public void setLevel(int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("O nível deve estar entre 1 e 10.");
        }
        this.level = level;
    }
}
