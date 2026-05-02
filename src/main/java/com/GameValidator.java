package com;

import java.util.List;

public class GameValidator {

    public void validateStartGame(String homeTeam, String awayTeam, List<Game> activeGames) {
        validateTeamName(homeTeam);
        validateTeamName(awayTeam);
        if (homeTeam.equalsIgnoreCase(awayTeam))
            throw new IllegalArgumentException("Home and away team cannot be the same");
        if (isTeamBusy(homeTeam, activeGames))
            throw new IllegalStateException("Team already playing: " + homeTeam);
        if (isTeamBusy(awayTeam, activeGames))
            throw new IllegalStateException("Team already playing: " + awayTeam);
    }

    public void validateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0)
            throw new IllegalArgumentException("Score cannot be negative");
    }

    private void validateTeamName(String team) {
        if (team == null || team.isBlank())
            throw new IllegalArgumentException("Team name cannot be null or blank");
    }

    private boolean isTeamBusy(String team, List<Game> activeGames) {
        return activeGames.stream()
                .anyMatch(g -> g.getHomeTeam().equalsIgnoreCase(team)
                        || g.getAwayTeam().equalsIgnoreCase(team));
    }
}