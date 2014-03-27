package eu.wraychus.game2048.gamemodel;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

/**
 * Holds one particular state of a game.
 * The state consists of:
 * - the current numbers in the field
 * - current score
 * - number of moves already performed in the game before the game hit this state
 * The state is mutable by its private methods
 * Public methods don't mutate the state (the 'apply' method [used to perform a move]
 *   clones the state and mutates the new one)
 * @author Jan Martiska
 */
public class GameState {

    private static Logger logger = Logger.getLogger(GameState.class.getName());

    /* We usually play on a 4x4 field. But if you wish, you may experiment with bigger ones,
     just change this number ;) (but it always is a square) */
    public static final int FIELD_SIZE = 4;

    // this holds the actual numbers contained in this state's game field
    // [row][column]
    private int[][] state;

    // how many moves have passed since the game began
    private int numberOfMoves;

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    // the current score (points achieved before the game hit this state)
    private int score;

    /**
     * Creates an initial state for a new game.
     * Fills two random fields with the number 2.
     */
    public GameState() {
        state = new int[FIELD_SIZE][FIELD_SIZE];
        // initialize the state by filling two random fields with number 2
        int x1 = random.nextInt(FIELD_SIZE);
        int y1 = random.nextInt(FIELD_SIZE);
        int x2 = random.nextInt(FIELD_SIZE);
        int y2 = random.nextInt(FIELD_SIZE);
        // really want two different fields..
        while ((x1 == x2) && (y1 == y2)) {
            x2 = random.nextInt(FIELD_SIZE);
            y2 = random.nextInt(FIELD_SIZE);
        }
        state[x1][y1] = 2;
        state[x2][y2] = 2;
    }

    public int getNumberAt(int row, int col) {
        return state[row][col];
    }

    private void put(int row, int col, int value) {
        state[row][col] = value;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n");
        for (int[] row : state) {
            for (int col : row) {
                s.append(String.format("%6d,", col));
            }
            s.append("\n");
        }
        s.append("score=" + score);
        s.append("\n\n");
        return s.toString();
    }

    public int getScore() {
        return score;
    }

    public int getMaximumNumber() {
        int max = Integer.MIN_VALUE;
        for (int[] row : state) {
            for (int col : row) {
                if (col > max) {
                    max = col;
                }
            }
        }
        return max;
    }

    /**
     * Applies a move upon this game state.
     * Does not mutate the game state, creates a new one
     * What it does:
     * - apply the move (squash all fields on the desired direction),
     * - if nothing has changed by applying the move, this move is illegal
     * - if there is no field with zero after this, this move is illegal
     * - pick a random field with a zero and fill it with number 2 or 4
     * @throws InvalidMoveException if this move cannot be applied
     * @throws GameEndException if no move can be applied
    */
    public GameState apply(Direction direction, boolean performAddition) throws InvalidMoveException {
        if (!isPlayable()) {
            throw new GameEndException();
        }
        boolean atLeastOneChange = false;
        GameState finalState = this.copy();
        switch (direction) {
        case UP:
            for (int col = 0; col < FIELD_SIZE; col++) {
                for (int row = 0; row < FIELD_SIZE; row++) {
                    if(finalState.moveOneField(row, col, Direction.UP))
                        atLeastOneChange = true;
                }
            }
            break;
        case DOWN:
            for (int col = 0; col < FIELD_SIZE; col++) {
                for (int row = FIELD_SIZE - 1; row > -1; row--) {
                    if(finalState.moveOneField(row, col, Direction.DOWN))
                        atLeastOneChange=true;
                }
            }
            break;
        case LEFT:
            for (int row = 0; row < FIELD_SIZE; row++) {
                for (int col = 0; col < FIELD_SIZE; col++) {
                    if(finalState.moveOneField(row, col, Direction.LEFT))
                        atLeastOneChange = true;
                }
            }
            break;
        case RIGHT:
            for (int row = 0; row < FIELD_SIZE; row++) {
                for (int col = FIELD_SIZE - 1; col > -1; col--) {
                    if(finalState.moveOneField(row, col, Direction.RIGHT))
                        atLeastOneChange = true;
                }
            }
            break;
        }

        if(!atLeastOneChange)
            throw new InvalidMoveException();

        if (finalState.getNumberOfFieldsWithZero() == 0) {
            throw new InvalidMoveException();
        }

        if (performAddition) {
            // add a new number
            int targetRow;
            int targetCol;
            do {
                targetRow = random.nextInt(FIELD_SIZE);
                targetCol = random.nextInt(FIELD_SIZE);
            } while (finalState.getNumberAt(targetRow, targetCol) > 0);
            int newNumber = random.nextInt(100) < 100 ? 2 : 4;  // :) currently, 100% chance to get a 2
            finalState.put(targetRow, targetCol, newNumber);
        }

        finalState.numberOfMoves++;

        return finalState;
    }

    /**
     * The state is playable (the game can continue) if at least one of these conditions holds true:
     * - there is at least one field with a zero number
     * - there is at least one pair of adjacent fields (lying next to each other in a row or in a column)
     *   with the same numbers in them
     */
    public boolean isPlayable() {
        if (getNumberOfFieldsWithZero() > 0) {
            return true;
        }
        Integer previous = null;
        // find two adjacent same numbers
        // in rows:
        for (int row = 0; row < FIELD_SIZE; row++) {
            previous = null;
            for (int col = 0; col < FIELD_SIZE; col++) {
                int value = state[row][col];
                if (previous == null) {
                    previous = value;
                } else {
                    if (value == previous) {
                        return true;
                    }
                    previous = value;
                }
            }
        }
        // in columns:
        for (int col = 0; col < FIELD_SIZE; col++) {
            previous = null;
            for (int row = 0; row < FIELD_SIZE; row++) {
                int value = state[row][col];
                if (previous == null) {
                    previous = value;
                } else {
                    if (value == previous) {
                        return true;
                    }
                    previous = value;
                }
            }
        }
        return false;
    }

    public int getNumberOfFieldsWithZero() {
        int ret = 0;
        for (int[] row : state) {
            for (int col : row) {
                if (col == 0) {
                    ret++;
                }
            }
        }
        return ret;
    }

    /*
       - pick a field with a non-zero number and 'send' it in the chosen direction, including
         'squashing' everything in the way to the side of the field, possibly merging some
         numbers together
       - mutates the game state!
    */
    private boolean moveOneField(int row, int col, Direction direction) {
        boolean change = false;
        if (state[row][col] == 0) {
            return false;
        }
        while (true) {
            Integer neighbor = nextTo(row, col, direction);
            if (neighbor == null) {
                // end of the game field -> we are done
                break;
            } else if (neighbor == state[row][col]) {
                // same number -> join the neighbor and add the score and we are done
                state[movePositionY(row, direction)][movePositionX(col, direction)] += state[row][col];
                score += 2 * state[row][col];
                state[row][col] = 0;
                change = true;
                break;
            } else if (neighbor == 0) {
                // zero -> move there and continue
                state[movePositionY(row, direction)][movePositionX(col, direction)] = state[row][col];
                state[row][col] = 0;
                row = movePositionY(row, direction);
                col = movePositionX(col, direction);
                change = true;
            } else {
                // different number -> we are done
                break;
            }
        }
        return change;
    }

    /**
     * utility method for translating coordinates [x,y] to [x, y +- 1] by applying a move in a particular direction
     * returns null if it is impossible to translate (would end up outside of the game field)
     */
    private static Integer movePositionX(int col, Direction direction) {
        if (direction == Direction.LEFT) {
            return col > 0 ? col - 1 : null;
        } else if (direction == Direction.RIGHT) {
            return col < FIELD_SIZE - 1 ? col + 1 : null;
        } else {
            return col;
        }
    }

    /**
     * utility method for translating coordinates [x,y] to [x +- 1, y] by applying a move in a particular direction
     * returns null if it is impossible to translate (would end up outside of the game field)
     */
    private static Integer movePositionY(int row, Direction direction) {
        if (direction == Direction.UP) {
            return row > 0 ? row - 1 : null;
        } else if (direction == Direction.DOWN) {
            return row < FIELD_SIZE - 1 ? row + 1 : null;
        } else {
            return row;
        }
    }

    /**
     * Retrieve the number in an adjacent field to [row, column]
     * 'adjacent' means by moving one field in a particular direction
     * returns null if moving in that direction means ending up outside the game field
     */
    private Integer nextTo(int row, int col, Direction direction) {
        Integer targetRow = movePositionY(row, direction);
        Integer targetCol = movePositionX(col, direction);
        if (targetCol == null || targetRow == null) {
            return null;
        } else {
            return state[targetRow][targetCol];
        }
    }


    /**
     * The cloning method.
     * Create a deep copy of this state.
     */
    public GameState copy() {
        int[][] newState = new int[FIELD_SIZE][FIELD_SIZE];
        for (int row = 0; row < FIELD_SIZE; row++) {
            System.arraycopy(state[row], 0, newState[row], 0, FIELD_SIZE);
        }
        GameState state1 = new GameState();
        state1.state = newState;
        state1.numberOfMoves = numberOfMoves;
        state1.score = score;
        return state1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameState)) {
            return false;
        }

        GameState otherState = (GameState)o;

        if (score != otherState.score) {
            return false;
        }

        if(numberOfMoves != otherState.numberOfMoves)
            return false;

        for (int row = 0; row < FIELD_SIZE; row++) {
            for (int col = 0; col < FIELD_SIZE; col++) {
                if (state[row][col] != otherState.state[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return score + Arrays.deepHashCode(state);
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }
}
