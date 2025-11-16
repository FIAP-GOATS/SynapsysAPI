package br.com.fiap.models.dto.Request;

public class CandidateSkillDTO {
    private String skillName;
    private int level;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
