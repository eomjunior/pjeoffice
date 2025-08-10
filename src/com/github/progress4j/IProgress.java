package com.github.progress4j;

import com.github.utils4j.IDisposable;
import com.github.utils4j.IInterruptable;
import io.reactivex.Observable;
import java.util.function.Consumer;

public interface IProgress extends IDisposable, IInterruptable {
  String getName();
  
  void begin(String paramString) throws InterruptedException;
  
  void begin(IStage paramIStage) throws InterruptedException;
  
  void begin(String paramString, int paramInt) throws InterruptedException;
  
  void begin(IStage paramIStage, int paramInt) throws InterruptedException;
  
  void step(String paramString, Object... paramVarArgs) throws InterruptedException;
  
  void info(String paramString, Object... paramVarArgs) throws InterruptedException;
  
  void end() throws InterruptedException;
  
  void skip(long paramLong) throws InterruptedException;
  
  void throwIfInterrupted() throws InterruptedException;
  
  boolean isClosed();
  
  Throwable getAbortCause();
  
  <T extends Throwable> T abort(T paramT);
  
  IProgress reset();
  
  IProgress stackTracer(Consumer<IState> paramConsumer);
  
  Observable<IStepEvent> stepObservable();
  
  Observable<IStageEvent> stageObservable();
  
  Observable<IProgress> disposeObservable();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/IProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */