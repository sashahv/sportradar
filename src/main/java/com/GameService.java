package com;

import java.util.Comparator;
import java.util.List;

public class GameService {

    private final GameRepository repository;
    private final GameValidator validator;

    public GameService() {
        this.repository = new GameRepository();
        this.validator = new GameValidator();
    }

    public void startGame(String homeTeam, String awayTeam) {
        validator.validateStartGame(homeTeam, awayTeam, repository.findAll());
        var sequence = repository.nextSeq();
        var game = new Game(homeTeam, awayTeam, sequence);
        repository.add(game);
    }

    public void finishGame(String homeTeam, String awayTeam) {
        repository.remove(repository.findByTeams(homeTeam, awayTeam));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        validator.validateScore(homeScore, awayScore);
        var game = repository.findByTeams(homeTeam, awayTeam);
        game.setHomeScore(homeScore);
        game.setAwayScore(awayScore);
    }

    public List<Game> getSummary() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(Game::getTotalScore).reversed()
                        .thenComparing(Comparator.comparingInt(Game::getInsertionOrder).reversed()))
                .toList();
    }
}