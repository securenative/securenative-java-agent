package com.securenative.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskResult {
    private String riskLevel;
    private double score;
    private String action;

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