package eu.wraychus.game2048.ai.rewards;

import eu.wraychus.game2048.gamemodel.GameState;

/**
 * @author Jan Martiska
 */
@FunctionalInterface
public interface RewardingFunction {

    Integer getReward(GameState gameState);

}
