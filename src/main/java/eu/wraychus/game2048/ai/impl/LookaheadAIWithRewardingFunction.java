package eu.wraychus.game2048.ai.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.ai.rewards.RewardingFunction;
import eu.wraychus.game2048.gamemodel.Direction;
import eu.wraychus.game2048.gamemodel.GameEndException;
import eu.wraychus.game2048.gamemodel.GameState;
import eu.wraychus.game2048.gamemodel.InvalidMoveException;

/**
 * Looks into the future, but
 * simulates the future by just accepting one random possible outcome per move
 */
public class LookaheadAIWithRewardingFunction implements AI {

    private static Logger logger = Logger.getLogger("GAME");

    private final RewardingFunction function;
    private final int lookaheadLevel;

    public LookaheadAIWithRewardingFunction(RewardingFunction rewardingFunction, int lookaheadLevel) {
        if(lookaheadLevel < 1)
            throw new IllegalArgumentException("Lookahead level must be at least 1");
        this.function = rewardingFunction;
        this.lookaheadLevel = lookaheadLevel;
    }

    @Override
    public Direction performMove(GameState currentState) {
        Direction bestDirection = null;
        int bestReward = Integer.MIN_VALUE;
        for (Direction direction : Direction.values()) {
            Set<GameState> gameStates = new LinkedHashSet<>();
            try {
                gameStates.add(currentState.apply(direction, true));
            } catch (InvalidMoveException e) {
                continue;
            }
            for(int expansion = 1; expansion<lookaheadLevel; expansion++) {
                gameStates = expand(gameStates);
            }
            for(GameState state : gameStates) {
                int reward = function.getReward(state);
                if(reward > bestReward) {
                    bestReward = reward;
                    bestDirection = direction;
                }
            }
        }
        return bestDirection;
    }

    private Set<GameState> expand(Set<GameState> initialSet) {
        Set<GameState> ret = new LinkedHashSet<>();
        for(GameState state : initialSet) {
            if(!state.isPlayable())
                continue;
            for (Direction direction : Direction.values()) {
                try {
                    ret.add(state.apply(direction, true));
                } catch (InvalidMoveException|GameEndException e) {
                    // NOOP
                }
            }
        }
        if(ret.isEmpty())
            return initialSet;
        return ret;
    }



}
