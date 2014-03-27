package eu.wraychus.game2048.ai.rewards;

import eu.wraychus.game2048.gamemodel.GameState;

/**
 * @author Jan Martiska
 */
public class RewardingFunctionByScore implements RewardingFunction {
    @Override
    public Integer getReward(GameState gameState) {
        return gameState.getScore();
    }
}
