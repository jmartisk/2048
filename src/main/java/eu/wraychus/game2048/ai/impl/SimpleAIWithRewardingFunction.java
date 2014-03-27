package eu.wraychus.game2048.ai.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.ai.rewards.RewardingFunction;
import eu.wraychus.game2048.gamemodel.Direction;
import eu.wraychus.game2048.gamemodel.GameState;
import eu.wraychus.game2048.gamemodel.InvalidMoveException;

/**
 * @author Jan Martiska
 */
public class SimpleAIWithRewardingFunction implements AI {
    private static Logger logger = Logger.getLogger("GAME");

    private final RewardingFunction function;

    public SimpleAIWithRewardingFunction(RewardingFunction rewardingFunction) {
        this.function = rewardingFunction;
    }

    @Override
    public Direction performMove(GameState currentState) {
        Map<Direction, Integer> possibleStates = new HashMap<>(4);
        for (Direction direction : Direction.values()) {
            try {
                GameState possibleState = currentState.apply(direction, true);
                possibleStates.put(direction, function.getReward(possibleState));
            } catch (InvalidMoveException e) {
                logger.debug("Cannot go " + direction);
            }
        }

        Direction result = null;
        int bestReward = Integer.MIN_VALUE;
        for (Map.Entry<Direction, Integer> possibleMove : possibleStates.entrySet()) {
            logger.debug("Can go " + possibleMove.getKey()
                    + ", score=" + possibleMove.getValue());
            if (result == null || possibleMove.getValue() > bestReward) {
                bestReward = possibleMove.getValue();
                result = possibleMove.getKey();
            }
        }
        return result;
    }
}
