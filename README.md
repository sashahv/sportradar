# Football World Cup Scoreboard

A simple library to track live World Cup matches and their scores. Built as a coding exercise for Sportradar.

```bash
mvn test
```

---

## What it does

- Start a game (initial score 0-0)
- Update score with absolute values (not incremental)
- Finish a game - removes it from the board entirely
- Get a summary sorted by total score descending; ties broken by most recently started

---

## Assumptions

- Home/away order matters - `Mexico vs Canada` and `Canada vs Mexico` are treated as different games
- A team can only play one game at a time
- Team names are case-insensitive
- Negative scores are rejected
- Finished games are gone - the spec says remove, so there's no history

## What I skipped

**Thread safety** - not mentioned in the spec. If needed, compound operations like find+remove would require more than just swapping to `ConcurrentHashMap`.

**Score going backwards** - spec doesn't say to reject it, so I allow it. Worth a conversation.

## Tiebreaker

Games with equal total score are ordered by insertion time - implemented as a counter rather than `System.currentTimeMillis()` to keep tests deterministic.

---

## Tech

- Java 17
- JUnit 5
- AssertJ