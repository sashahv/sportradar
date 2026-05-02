package com;

public class Game {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final int insertionOrder;

    public Game(String homeTeam, String awayTeam, int insertionOrder) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.insertionOrder = insertionOrder;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getInsertionOrder() {
        return insertionOrder;
    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }
}
