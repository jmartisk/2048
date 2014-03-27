package eu.wraychus.game2048.ai.impl;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.gamemodel.Direction;
import eu.wraychus.game2048.gamemodel.GameState;
import eu.wraychus.game2048.gamemodel.InvalidMoveException;

/**
 * @author Jan Martiska
 */
public class SimpleAIWithComparator implements AI {

    private static Logger logger = Logger.getLogger("GAME");

    private final Comparator<GameState> comparator;

    public SimpleAIWithComparator(Comparator<GameState> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Direction performMove(GameState currentState) {
        SortedMap<GameState, Direction> possibleStates = new TreeMap<>(comparator);
        for (Direction direction : Direction.values()) {
            try {
                GameState possibleState = currentState.apply(direction, false);
                possibleStates.put(possibleState, direction);
            } catch (InvalidMoveException e) {
                logger.debug("Cannot go " + direction);
            }
        }

        Direction result = null;
        for (Map.Entry<GameState, Direction> possibleMove : possibleStates.entrySet()) {
            logger.debug("Can go " + possibleMove.getValue()
                    + ", score=" + possibleMove.getKey().getScore()
                    + ", free fields=" + possibleMove.getKey().getNumberOfFieldsWithZero());
            // take the best (~first) one
            if (result == null) {
                result = possibleMove.getValue();
            }

        }
        return result;
    }

}
