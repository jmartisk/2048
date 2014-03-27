package eu.wraychus.game2048.ai.comparators;

import java.util.Comparator;

import eu.wraychus.game2048.gamemodel.GameState;

/**
 * First, prefer the number of '0' fields in the game
 * Second, prefer higher score
 */
public class ComparatorByFreeFieldsAndScore implements Comparator<GameState> {

    @Override
    public int compare(GameState o1, GameState o2) {
        if (o2.getNumberOfFieldsWithZero() > o1.getNumberOfFieldsWithZero()) {
            return 1;
        } else if (o2.getNumberOfFieldsWithZero() < o1.getNumberOfFieldsWithZero()) {
            return -1;
        } else if (o2.getScore() > o1.getScore()) {
            return 1;
        } else if (o2.getScore() < o1.getScore()) {
            return 1;
        }
        return 0;
    }
}
