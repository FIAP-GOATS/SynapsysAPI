package br.com.fiap.models.dto.Request;

public class CandidateDTO {
    private String displayName;
    private String purpose;
    private String workStyle;
    private String interests;


    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getWorkStyle() { return workStyle; }
    public void setWorkStyle(String workStyle) { this.workStyle = workStyle; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
}