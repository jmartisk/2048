package eu.wraychus.game2048.multirunner;

import eu.wraychus.game2048.gamemodel.GameResult;

/**
 * @author Jan Martiska
 */
public class MultigameResult {

    private int maxScore = Integer.MIN_VALUE;
    private int minScore = Integer.MAX_VALUE;
    private int games = 0;
    private double averageScore = 0;
    private long totalScore = 0;

    private double averageLength = 0;
    private long totalLength = 0;

    private double averageMiliseconds = 0;
    private long totalMiliseconds = 0;

    public synchronized void submitScore(GameResult result) {
        int score = result.getScore();
        int length = result.getMovesCount();
        long miliseconds = result.getMiliseconds();
        games++;
        totalScore += score;
        totalLength += length;
        totalMiliseconds += miliseconds;
        if(score > maxScore)
            maxScore = score;
        if(score < minScore)
            minScore = score;
        averageScore = totalScore/(double)games;
        averageLength = totalLength/(double)games;
        averageMiliseconds = totalMiliseconds/(double)games;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getGames() {
        return games;
    }

    public double getAverageScore() {
        return averageScore;
    }

    @Override
    public String toString() {
        return "{" +
                "maxScore=" + maxScore +
                ", minScore=" + minScore +
                ", games=" + games +
                ", averageScore=" + averageScore +
                ", averageLength=" + averageLength +
                ", averageMiliseconds=" + averageMiliseconds +
                '}';
    }

    public long getTotalLength() {
        return totalLength;
    }

    public double getAverageLength() {
        return averageLength;
    }
}
