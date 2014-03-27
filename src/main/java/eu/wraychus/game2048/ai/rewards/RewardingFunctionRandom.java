package eu.wraychus.game2048.ai.rewards;

import java.util.concurrent.ThreadLocalRandom;

import eu.wraychus.game2048.gamemodel.GameState;

/**
 * @author Jan Martiska
 */
public class RewardingFunctionRandom implements RewardingFunction {

    @Override
    public Integer getReward(GameState gameState) {
        return ThreadLocalRandom.current().nextInt(100);
    }

}
