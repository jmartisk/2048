package eu.wraychus.game2048.ai.comparators;

import java.util.Comparator;

import eu.wraychus.game2048.gamemodel.GameState;

/**
 * First, prefer the number of '0' fields in the game
 * Second, prefer higher score
 */
public class ComparatorByScore implements Comparator<GameState> {

    @Override
    public int compare(GameState o1, GameState o2) {
        if (o2.getScore() > o1.getScore()) {
            return 1;
        } else if (o2.getScore() < o1.getScore()) {
            return -1;
        }
        return 1; // must not return 0!!!!!!!
        //  the map is sorted by keys, so two keys must never be equal, we would lose some values
    }
}
