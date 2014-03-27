package eu.wraychus.game2048.playing.callbacks;

import java.util.concurrent.Callable;

/**
 * @author Jan Martiska
 */
public class CallbackTask<T> implements Runnable {

    private Callable<T> underlyingTask;
    private CallbackHandler<T> callbackHandler;

    public CallbackTask(Callable<T> underlyingTask, CallbackHandler<T> handler) {
        this.underlyingTask = underlyingTask;
        this.callbackHandler = handler;
    }

    @Override
    public void run() {
        try {
            T result = underlyingTask.call();
            callbackHandler.handle(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
