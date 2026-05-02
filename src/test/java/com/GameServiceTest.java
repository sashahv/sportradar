package com;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService service;

    @BeforeEach
    void setUp() {
        service = new GameService();
    }

    @Test
    void shouldStartGameWithInitialScoreZeroZero() {
        service.startGame("Mexico", "Canada");

        List<Game> summary = service.getSummary();
        assertEquals(1, summary.size());
        assertEquals(0, summary.get(0).getHomeScore());
        assertEquals(0, summary.get(0).getAwayScore());
    }

    @Test
    void shouldThrowWhenStartingDuplicateGame() {
        service.startGame("Mexico", "Canada");

        assertThrows(IllegalStateException.class,
                () -> service.startGame("Mexico", "Canada"));
    }

    @Test
    void shouldThrowWhenHomeTeamAlreadyPlaying() {
        service.startGame("Mexico", "Canada");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.startGame("Mexico", "Brazil"));
        assertTrue(ex.getMessage().contains("Mexico"));
    }

    @Test
    void shouldThrowWhenAwayTeamAlreadyPlaying() {
        service.startGame("Mexico", "Canada");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> service.startGame("Brazil", "Canada"));
        assertTrue(ex.getMessage().contains("Canada"));
    }

    @Test
    void shouldThrowWhenSameTeamAsHomeAndAway() {
        assertThrows(IllegalArgumentException.class,
                () -> service.startGame("Mexico", "Mexico"));
    }

    @Test
    void shouldThrowWhenHomeTeamIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.startGame(null, "Canada"));
    }

    @Test
    void shouldThrowWhenAwayTeamIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> service.startGame("Mexico", "  "));
    }

    @Test
    void shouldBeCaseInsensitiveWhenDetectingTeamBusy() {
        service.startGame("Mexico", "Canada");

        assertThrows(IllegalStateException.class,
                () -> service.startGame("mexico", "Brazil"));
    }

    @Test
    void shouldRemoveGameFromBoard() {
        service.startGame("Mexico", "Canada");
        service.finishGame("Mexico", "Canada");

        assertTrue(service.getSummary().isEmpty());
    }

    @Test
    void shouldThrowWhenFinishingNonExistentGame() {
        assertThrows(IllegalArgumentException.class,
                () -> service.finishGame("Mexico", "Canada"));
    }

    @Test
    void shouldThrowWhenFinishingAlreadyFinishedGame() {
        service.startGame("Mexico", "Canada");
        service.finishGame("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class,
                () -> service.finishGame("Mexico", "Canada"));
    }

    @Test
    void shouldThrowWhenFinishingWithReversedTeamOrder() {
        service.startGame("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class,
                () -> service.finishGame("Canada", "Mexico"));
    }

    @Test
    void shouldUpdateScore() {
        service.startGame("Mexico", "Canada");
        service.updateScore("Mexico", "Canada", 0, 5);

        Game game = service.getSummary().get(0);
        assertEquals(0, game.getHomeScore());
        assertEquals(5, game.getAwayScore());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentGame() {
        assertThrows(IllegalArgumentException.class,
                () -> service.updateScore("Mexico", "Canada", 1, 0));
    }

    @Test
    void shouldThrowWhenUpdatingFinishedGame() {
        service.startGame("Mexico", "Canada");
        service.finishGame("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateScore("Mexico", "Canada", 1, 0));
    }

    @Test
    void shouldThrowOnNegativeHomeScore() {
        service.startGame("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateScore("Mexico", "Canada", -1, 0));
    }

    @Test
    void shouldThrowOnNegativeAwayScore() {
        service.startGame("Mexico", "Canada");

        assertThrows(IllegalArgumentException.class,
                () -> service.updateScore("Mexico", "Canada", 0, -1));
    }

    @Test
    void shouldAllowNoOpUpdate() {
        service.startGame("Mexico", "Canada");
        service.updateScore("Mexico", "Canada", 0, 0);

        Game game = service.getSummary().get(0);
        assertEquals(0, game.getHomeScore());
        assertEquals(0, game.getAwayScore());
    }

    @Test
    void shouldReturnEmptySummaryWhenNoActiveGames() {
        assertTrue(service.getSummary().isEmpty());
    }

    @Test
    void shouldReturnSingleGame() {
        service.startGame("Mexico", "Canada");

        assertEquals(1, service.getSummary().size());
    }

    @Test
    void shouldOrderByTotalScoreDescending() {
        service.startGame("Mexico", "Canada");
        service.updateScore("Mexico", "Canada", 0, 5);
        service.startGame("Spain", "Brazil");
        service.updateScore("Spain", "Brazil", 10, 2);

        List<Game> summary = service.getSummary();
        assertEquals("Spain", summary.get(0).getHomeTeam());
        assertEquals("Mexico", summary.get(1).getHomeTeam());
    }

    @Test
    void shouldOrderByInsertionOrderWhenTotalScoreIsEqual() {
        service.startGame("Germany", "France");
        service.updateScore("Germany", "France", 2, 2);
        service.startGame("Argentina", "Australia");
        service.updateScore("Argentina", "Australia", 3, 1);

        List<Game> summary = service.getSummary();
        assertEquals("Argentina", summary.get(0).getHomeTeam());
        assertEquals("Germany", summary.get(1).getHomeTeam());
    }

    @Test
    void shouldMatchSpecExample() {
        service.startGame("Mexico", "Canada");
        service.updateScore("Mexico", "Canada", 0, 5);
        service.startGame("Spain", "Brazil");
        service.updateScore("Spain", "Brazil", 10, 2);
        service.startGame("Germany", "France");
        service.updateScore("Germany", "France", 2, 2);
        service.startGame("Uruguay", "Italy");
        service.updateScore("Uruguay", "Italy", 6, 6);
        service.startGame("Argentina", "Australia");
        service.updateScore("Argentina", "Australia", 3, 1);

        List<Game> summary = service.getSummary();
        assertEquals("Uruguay",   summary.get(0).getHomeTeam());
        assertEquals("Spain",     summary.get(1).getHomeTeam());
        assertEquals("Mexico",    summary.get(2).getHomeTeam());
        assertEquals("Argentina", summary.get(3).getHomeTeam());
        assertEquals("Germany",   summary.get(4).getHomeTeam());
    }

    @Test
    void shouldNotIncludeFinishedGameInSummary() {
        service.startGame("Mexico", "Canada");
        service.startGame("Spain", "Brazil");
        service.finishGame("Mexico", "Canada");

        List<Game> summary = service.getSummary();
        assertEquals(1, summary.size());
        assertEquals("Spain", summary.get(0).getHomeTeam());
    }

    @Test
    void shouldReturnImmutableSummary() {
        service.startGame("Mexico", "Canada");

        assertThrows(UnsupportedOperationException.class,
                () -> service.getSummary().add(new Game("X", "Y", 99)));
    }
}