package eu.wraychus.game2048.gamemodel;

/**
 * @author Jan Martiska
 */
public class GameResult {

    private int score;
    private int movesCount;
    private long miliseconds;

    public GameResult(int score, int movesCount, long miliseconds) {
        this.score = score;
        this.movesCount = movesCount;
        this.miliseconds = miliseconds;
    }


    public int getScore() {
        return score;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public long getMiliseconds() {
        return miliseconds;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "score=" + score +
                ", movesCount=" + movesCount +
                ", miliseconds=" + miliseconds +
                '}';
    }
}
