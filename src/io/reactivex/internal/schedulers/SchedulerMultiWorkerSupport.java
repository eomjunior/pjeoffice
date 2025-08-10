package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;

public interface SchedulerMultiWorkerSupport {
  void createWorkers(int paramInt, @NonNull WorkerCallback paramWorkerCallback);
  
  public static interface WorkerCallback {
    void onWorker(int param1Int, @NonNull Scheduler.Worker param1Worker);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/SchedulerMultiWorkerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */