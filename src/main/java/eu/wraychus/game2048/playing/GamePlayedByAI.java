package eu.wraychus.game2048.playing;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.gamemodel.Direction;
import eu.wraychus.game2048.gamemodel.GameResult;
import eu.wraychus.game2048.gamemodel.GameState;
import eu.wraychus.game2048.gamemodel.InvalidMoveException;

/**
 * @author Jan Martiska
 */
public class GamePlayedByAI implements Callable<GameResult> {

    private Logger logger = Logger.getLogger("GAME");

    private AI ai;

    public GamePlayedByAI(AI ai) {
        this.ai = ai;
    }

    @Override
    public GameResult call() throws Exception {
        long time = System.currentTimeMillis();
        GameState currentState = new GameState();
        while(currentState.isPlayable()) {
            logger.debug(currentState);
            Direction move = ai.performMove(currentState);
            logger.debug("Performing move: " + move);
            try {
                currentState = currentState.apply(move, true);
            } catch (InvalidMoveException e) {
                throw new Error("Should not happen");
            }
        }
        logger.debug(currentState);
        logger.debug("GAME END.");
        return new GameResult(currentState.getScore(), currentState.getNumberOfMoves(), System.currentTimeMillis()-time);
    }
}
