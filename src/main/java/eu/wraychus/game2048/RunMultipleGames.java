package eu.wraychus.game2048;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.impl.LookaheadAIWithRewardingFunction;
import eu.wraychus.game2048.ai.rewards.RewardingFunctionByScore;
import eu.wraychus.game2048.multirunner.MultigameRunner;

/**
 * @author Jan Martiska
 */
public class RunMultipleGames {

    private static Logger logger = Logger.getLogger(RunMultipleGames.class.getName());

    public static void main(String[] args) throws InterruptedException {
        Logger.getLogger("GAME").setLevel(Level.OFF);
        Logger.getLogger("MULTIGAME").setLevel(Level.OFF);

        int gamesPerMode = 20;
        MultigameRunner runner;

        runner = new MultigameRunner(
                () -> new LookaheadAIWithRewardingFunction(new RewardingFunctionByScore(), 6),
                gamesPerMode);
        runner.doit();
        logger.info("lookahead=4, rewarding by score");
        logger.info(runner.getResult());
        logger.info("");
    }

}
