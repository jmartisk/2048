package eu.wraychus.game2048.multirunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.log4j.Logger;

import eu.wraychus.game2048.ai.AI;
import eu.wraychus.game2048.playing.GamePlayedByAI;
import eu.wraychus.game2048.playing.callbacks.CallbackTask;

/**
 * @author Jan Martiska
 */
public class MultigameRunner {

    private Supplier<AI> aiSupplier;
    private final MultigameResult result = new MultigameResult();
    private static Logger logger = Logger.getLogger("MULTIGAME");
    private int gamesCount;

    public MultigameRunner(Supplier<AI> ai, int gamesCount) {
        this.aiSupplier = ai;
        this.gamesCount = gamesCount;
    }

    public void doit() {
        ExecutorService executor = Executors.newScheduledThreadPool(4);
        for(int i=0; i<gamesCount; i++) {
            executor.submit(new CallbackTask<>(new GamePlayedByAI(aiSupplier.get()), gameResult -> {
                result.submitScore(gameResult);
                logger.info(result);
            }));
        }
        try {
            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MultigameResult getResult() {
        return result;
    }
}
