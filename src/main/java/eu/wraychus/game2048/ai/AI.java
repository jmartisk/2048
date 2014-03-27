package eu.wraychus.game2048.ai;

import eu.wraychus.game2048.gamemodel.Direction;
import eu.wraychus.game2048.gamemodel.GameState;

/**
 * @author Jan Martiska
 */
public interface AI {

    Direction performMove(GameState currentState);

}
