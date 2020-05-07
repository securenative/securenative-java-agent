package com.securenative.agent.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskResult {
    @JsonProperty("riskLevel")
    private String riskLevel;
    @JsonProperty("score")
    private double score;
    @JsonProperty("action")
    private String action;

    // Empty constructor for deserialization
    public RiskResult() {
    }

    public RiskResult(String riskLevel, double score, String action) {
        this.riskLevel = riskLevel;
        this.score = score;
        this.action = action;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}