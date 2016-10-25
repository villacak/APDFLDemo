package com.proquest.demo;

import com.proquest.demo.constants.Constants;
import com.proquest.demo.pojos.ReturnObject;

import java.util.concurrent.*;

/**
 * Created by kvillaca on 10/24/16.
 */
public class MyThreads implements Runnable {

    private String[] jsons;
    private Thread t;

    public MyThreads(final String[] jsons) {
        this.jsons = jsons;
    }

    @Override
    public void run() {
        final ExecutorService executorMain = Executors.newFixedThreadPool(Constants.MAX_THREAD_COUNT);

        try {
            // This loop would emulate the CSV file with several pdfs within, in here we have several strings in an array
            for (int arrayItemIndex = 0; arrayItemIndex < jsons.length; arrayItemIndex++) {
                // To trigger multi threads
                final Callable<ReturnObject> callable = new MainCallable(jsons[arrayItemIndex]);
                final Future<ReturnObject> dataReturnList = executorMain.submit(callable);
                final ReturnObject tempReturnObj = dataReturnList.get();

                // To debug, will run sequentially
//                    final MainCallable mc = new MainCallable(jsons[i]);
//                    final ReturnObject tempReturnObj = mc.call();
                System.out.println("-------------------------");
                System.out.println(tempReturnObj);
            }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getMessage());
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (!executorMain.isTerminated()) {
                executorMain.shutdown();
            }
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
}
