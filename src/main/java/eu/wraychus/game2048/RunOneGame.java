package eu.wraychus.game2048;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.ai.impl.LookaheadAIWithRewardingFunction;
import eu.wraychus.game2048.ai.rewards.RewardingFunctionByScore;
import eu.wraychus.game2048.gamemodel.GameResult;
import eu.wraychus.game2048.playing.GamePlayedByAI;

/**
 * @author Jan Martiska
 */
public class RunOneGame {

    private static Logger logger = Logger.getLogger(RunOneGame.class.getName());

    public static void main(String[] args) throws Exception {
        Logger.getLogger("GAME").setLevel(Level.DEBUG);
        AI ai = new LookaheadAIWithRewardingFunction(new RewardingFunctionByScore(), 5);
        GamePlayedByAI thread =  new GamePlayedByAI(ai);
        GameResult result = thread.call();
        logger.info(result);
    }
}
