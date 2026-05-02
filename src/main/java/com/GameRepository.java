package com;

import java.util.ArrayList;
import java.util.List;

public class GameRepository {

    private final List<Game> activeGames = new ArrayList<>();
    private int seq;

    public void add(Game game) {
        activeGames.add(game);
    }

    public void remove(Game game) {
        activeGames.remove(game);
    }

    public Game findByTeams(String homeTeam, String awayTeam) {
        return activeGames.stream()
                .filter(g -> g.getHomeTeam().equalsIgnoreCase(homeTeam))
                .filter(g -> g.getAwayTeam().equalsIgnoreCase(awayTeam))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + homeTeam + " vs " + awayTeam));
    }

    public List<Game> findAll() {
        return List.copyOf(activeGames);
    }

    public int nextSeq() {
        return seq++;
    }
}