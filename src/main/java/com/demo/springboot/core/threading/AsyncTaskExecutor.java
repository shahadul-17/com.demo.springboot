package com.demo.springboot.core.threading;

import com.demo.springboot.core.utilities.ObjectUtilities;
import com.demo.springboot.core.utilities.ThreadUtilities;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

final class AsyncTaskExecutor {

    private static final int EXECUTOR_SERVICE_TERMINATION_WAIT_TIMEOUT_IN_MILLISECONDS = 20;
    private static final Logger logger = LogManager.getLogger(AsyncTaskExecutor.class);
    private static final ThreadFactory virtualThreadFactory = Thread.ofVirtual()
            .name("virtual-", 1L)
            .factory();
    private static final ExecutorService executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);

    /**
     * This method submits a task to the executor service
     * in a thread-safe manner.
     * @param task Task to execute.
     * @return An AsyncTask object.
     * @param <Type> Asynchronous task result type.
     */
    @SuppressWarnings(value = "unchecked")
    private static <Type> AsyncTask<Type> submitTaskToExecutorService(Object task) {
        Future<?> future = null;
        Exception exception = null;

        try {
            // checks the instance type of the task...
            if (task instanceof Runnable runnable) {
                future = executorService.submit(runnable);
            } else if (task instanceof Callable<?> callable) {
                future = executorService.submit(callable);
            } else {
                // if the task doesn't match any of the types,
                // we'll set an exception...
                exception = new Exception("Invalid task provided.");
            }
        } catch (Exception _exception) {
            // assigns the exception to the outer scope variable...
            exception = _exception;
        }

        // if future is not null, returns an async task derived from the future...
        if (future != null) { return AsyncTask.from((Future<Type>) future); }

        logger.log(Level.ERROR, "An exception occurred while running the async task.", exception);

        // otherwise, we shall create an async task from the exception...
        return AsyncTask.from(exception);
    }

    /**
     * Asynchronously executes a task.
     * @implNote This method is thread-safe.
     * @param task Task to execute.
     * @return An AsyncTask object.
     */
    static AsyncTask<?> run(Runnable task) {
        return submitTaskToExecutorService(task);
    }

    /**
     * Asynchronously executes a task.
     * @implNote This method is thread-safe.
     * @param task Task to execute.
     * @return An AsyncTask object.
     * @param <Type> Asynchronous task result type.
     */
    static <Type> AsyncTask<Type> run(Callable<Type> task) {
        return submitTaskToExecutorService(task);
    }

    /**
     * Throws exception if the provided array of objects
     * contains exception.
     * @param objects An array of objects that may or may not
     *                contain exception.
     * @return The provided array without any modification.
     * @throws Exception If any of the objects is an exception.
     */
    private static Object[] throwExceptionIfExists(Object[] objects) throws Exception {
        for (var i = 0; i < objects.length; ++i) {
            var object = objects[i];

            // if object is not an instance of Exception class,
            // we shall skip this iteration...
            if (!(object instanceof Exception exception)) { continue; }

            // if exception is found, we'll throw the exception...
            throw exception;
        }

        // if no exception is found after iteration,
        // we shall return the objects as-is...
        return objects;
    }

    /**
     * Awaits all the async tasks. This method throws
     * the first available exception (if found).
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * @throws Exception If any of the async tasks threw exception.
     */
    static Object[] await(AsyncTask<?>[] asyncTasks) throws Exception {
        // awaits all the async tasks...
        var results = awaitAll(asyncTasks);

        // otherwise, looks for exception object within the results list...
        return throwExceptionIfExists(results);
    }

    /**
     * Awaits all the async tasks. This method throws
     * the first available exception (if found).
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * @throws Exception If any of the async tasks threw exception.
     */
    static Object[] await(Iterable<AsyncTask<?>> asyncTasks) throws Exception {
        // awaits all the async tasks...
        var results = awaitAll(asyncTasks);

        // otherwise, looks for exception object within the results list...
        return throwExceptionIfExists(results);
    }

    /**
     * Awaits all the async tasks. This method throws
     * the first available exception (if found).
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * @throws Exception If any of the async tasks threw exception.
     */
    static Object[] await(List<AsyncTask<?>> asyncTasks) throws Exception {
        // awaits all the async tasks...
        var results = awaitAll(asyncTasks);

        // otherwise, looks for exception object within the results list...
        return throwExceptionIfExists(results);
    }

    /**
     * Awaits all the async tasks.
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * The list may contain actual results or exceptions.
     */
    static Object[] awaitAll(AsyncTask<?>[] asyncTasks) {
        return awaitAll(asyncTasks, asyncTasks.length);
    }

    /**
     * Awaits all the async tasks.
     * @param asyncTasks Async tasks to be awaited.
     * @param length Length of the array till which
     * @return An array containing the results of all the tasks.
     * The list may contain actual results or exceptions.
     */
    static Object[] awaitAll(AsyncTask<?>[] asyncTasks, int length) {
        // instantiates an array to hold all the async task results...
        Object[] results = new Object[length];

        for (var i = 0; i < length; ++i) {
            var asyncTask = asyncTasks[i];
            Object result;

            try {
                // awaiting async task may throw exception...
                result = asyncTask.await();
            } catch (Exception exception) {
                // if exception is thrown, we shall set the
                // exception as the result...
                result = exception;
            }

            // adds the result to the array...
            results[i] = result;
        }

        // finally, we shall return the results...
        return results;
    }

    /**
     * Awaits all the async tasks.
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * The list may contain actual results or exceptions.
     */
    static Object[] awaitAll(Iterable<AsyncTask<?>> asyncTasks) {
        // instantiates a list to hold all the async tasks...
        List<AsyncTask<?>> _asyncTasks = new ArrayList<>();

        // adds all the async tasks to our newly created list...
        for (var asyncTask : asyncTasks) {
            _asyncTasks.add(asyncTask);
        }

        // awaits all the tasks...
        return awaitAll(_asyncTasks);
    }

    /**
     * Awaits all the async tasks.
     * @param asyncTasks Async tasks to be awaited.
     * @return An array containing the results of all the tasks.
     * The list may contain actual results or exceptions.
     */
    static Object[] awaitAll(List<AsyncTask<?>> asyncTasks) {
        // checks if null or empty list is provided...
        if (asyncTasks == null || asyncTasks.isEmpty()) { return ObjectUtilities.getEmptyObjectArray(); }

        return awaitAll(asyncTasks.toArray(new AsyncTask[0]));
    }

    static ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Releases all the resources associated with the
     * asynchronous task execution runtime.
     */
    static void dispose() {
        logger.log(Level.INFO, "Releasing all the resources associated with the asynchronous task executor.");

        try {
            executorService.shutdownNow();

            logger.log(Level.INFO, "Executor service shutdown successful.");
        } catch (Exception exception) {
            logger.log(Level.ERROR, "An exception occurred while shutting down the underlying executor service.", exception);
        }

        // waits for the executor service termination...
        while (!ThreadUtilities.awaitExecutorServiceTermination(
                EXECUTOR_SERVICE_TERMINATION_WAIT_TIMEOUT_IN_MILLISECONDS, executorService)) {
            logger.log(Level.INFO, "Waiting for the executor service termination.");
        }

        logger.log(Level.INFO, "Successfully terminated the executor service.");

        try {
            executorService.close();

            logger.log(Level.INFO, "Successfully closed the executor service.");
        } catch (Exception exception) {
            logger.log(Level.WARN, "An exception occurred while closing the underlying executor service.", exception);
        }
    }
}
