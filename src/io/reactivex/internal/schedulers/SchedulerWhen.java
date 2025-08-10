/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.disposables.Disposables;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.processors.FlowableProcessor;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchedulerWhen
/*     */   extends Scheduler
/*     */   implements Disposable
/*     */ {
/*     */   private final Scheduler actualScheduler;
/*     */   private final FlowableProcessor<Flowable<Completable>> workerProcessor;
/*     */   private Disposable disposable;
/*     */   
/*     */   public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> combine, Scheduler actualScheduler) {
/* 111 */     this.actualScheduler = actualScheduler;
/*     */     
/* 113 */     this.workerProcessor = UnicastProcessor.create().toSerialized();
/*     */ 
/*     */     
/*     */     try {
/* 117 */       this.disposable = ((Completable)combine.apply(this.workerProcessor)).subscribe();
/* 118 */     } catch (Throwable e) {
/* 119 */       throw ExceptionHelper.wrapOrThrow(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 125 */     this.disposable.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 130 */     return this.disposable.isDisposed();
/*     */   }
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public Scheduler.Worker createWorker() {
/* 136 */     Scheduler.Worker actualWorker = this.actualScheduler.createWorker();
/*     */ 
/*     */     
/* 139 */     FlowableProcessor<ScheduledAction> actionProcessor = UnicastProcessor.create().toSerialized();
/*     */     
/* 141 */     Flowable<Completable> actions = actionProcessor.map(new CreateWorkerFunction(actualWorker));
/*     */ 
/*     */     
/* 144 */     Scheduler.Worker worker = new QueueWorker(actionProcessor, actualWorker);
/*     */ 
/*     */     
/* 147 */     this.workerProcessor.onNext(actions);
/*     */ 
/*     */     
/* 150 */     return worker;
/*     */   }
/*     */   
/* 153 */   static final Disposable SUBSCRIBED = new SubscribedDisposable();
/*     */   
/* 155 */   static final Disposable DISPOSED = Disposables.disposed();
/*     */   
/*     */   static abstract class ScheduledAction
/*     */     extends AtomicReference<Disposable> implements Disposable {
/*     */     ScheduledAction() {
/* 160 */       super(SchedulerWhen.SUBSCRIBED);
/*     */     }
/*     */     
/*     */     void call(Scheduler.Worker actualWorker, CompletableObserver actionCompletable) {
/* 164 */       Disposable oldState = get();
/*     */       
/* 166 */       if (oldState == SchedulerWhen.DISPOSED) {
/*     */         return;
/*     */       }
/*     */       
/* 170 */       if (oldState != SchedulerWhen.SUBSCRIBED) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       Disposable newState = callActual(actualWorker, actionCompletable);
/*     */       
/* 179 */       if (!compareAndSet(SchedulerWhen.SUBSCRIBED, newState))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 184 */         newState.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract Disposable callActual(Scheduler.Worker param1Worker, CompletableObserver param1CompletableObserver);
/*     */     
/*     */     public boolean isDisposed() {
/* 192 */       return get().isDisposed();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 199 */       Disposable oldState, newState = SchedulerWhen.DISPOSED;
/*     */       do {
/* 201 */         oldState = get();
/* 202 */         if (oldState == SchedulerWhen.DISPOSED) {
/*     */           return;
/*     */         }
/*     */       }
/* 206 */       while (!compareAndSet(oldState, newState));
/*     */       
/* 208 */       if (oldState != SchedulerWhen.SUBSCRIBED)
/*     */       {
/* 210 */         oldState.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class ImmediateAction
/*     */     extends ScheduledAction {
/*     */     private final Runnable action;
/*     */     
/*     */     ImmediateAction(Runnable action) {
/* 220 */       this.action = action;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Disposable callActual(Scheduler.Worker actualWorker, CompletableObserver actionCompletable) {
/* 225 */       return actualWorker.schedule(new SchedulerWhen.OnCompletedAction(this.action, actionCompletable));
/*     */     }
/*     */   }
/*     */   
/*     */   static class DelayedAction
/*     */     extends ScheduledAction {
/*     */     private final Runnable action;
/*     */     private final long delayTime;
/*     */     private final TimeUnit unit;
/*     */     
/*     */     DelayedAction(Runnable action, long delayTime, TimeUnit unit) {
/* 236 */       this.action = action;
/* 237 */       this.delayTime = delayTime;
/* 238 */       this.unit = unit;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Disposable callActual(Scheduler.Worker actualWorker, CompletableObserver actionCompletable) {
/* 243 */       return actualWorker.schedule(new SchedulerWhen.OnCompletedAction(this.action, actionCompletable), this.delayTime, this.unit);
/*     */     }
/*     */   }
/*     */   
/*     */   static class OnCompletedAction implements Runnable {
/*     */     final CompletableObserver actionCompletable;
/*     */     final Runnable action;
/*     */     
/*     */     OnCompletedAction(Runnable action, CompletableObserver actionCompletable) {
/* 252 */       this.action = action;
/* 253 */       this.actionCompletable = actionCompletable;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 259 */         this.action.run();
/*     */       } finally {
/* 261 */         this.actionCompletable.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class CreateWorkerFunction implements Function<ScheduledAction, Completable> {
/*     */     final Scheduler.Worker actualWorker;
/*     */     
/*     */     CreateWorkerFunction(Scheduler.Worker actualWorker) {
/* 270 */       this.actualWorker = actualWorker;
/*     */     }
/*     */ 
/*     */     
/*     */     public Completable apply(SchedulerWhen.ScheduledAction action) {
/* 275 */       return new WorkerCompletable(action);
/*     */     }
/*     */     
/*     */     final class WorkerCompletable extends Completable {
/*     */       final SchedulerWhen.ScheduledAction action;
/*     */       
/*     */       WorkerCompletable(SchedulerWhen.ScheduledAction action) {
/* 282 */         this.action = action;
/*     */       }
/*     */ 
/*     */       
/*     */       protected void subscribeActual(CompletableObserver actionCompletable) {
/* 287 */         actionCompletable.onSubscribe(this.action);
/* 288 */         this.action.call(SchedulerWhen.CreateWorkerFunction.this.actualWorker, actionCompletable);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static final class QueueWorker extends Scheduler.Worker {
/*     */     private final AtomicBoolean unsubscribed;
/*     */     private final FlowableProcessor<SchedulerWhen.ScheduledAction> actionProcessor;
/*     */     private final Scheduler.Worker actualWorker;
/*     */     
/*     */     QueueWorker(FlowableProcessor<SchedulerWhen.ScheduledAction> actionProcessor, Scheduler.Worker actualWorker) {
/* 299 */       this.actionProcessor = actionProcessor;
/* 300 */       this.actualWorker = actualWorker;
/* 301 */       this.unsubscribed = new AtomicBoolean();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 308 */       if (this.unsubscribed.compareAndSet(false, true)) {
/* 309 */         this.actionProcessor.onComplete();
/* 310 */         this.actualWorker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 316 */       return this.unsubscribed.get();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
/* 323 */       SchedulerWhen.DelayedAction delayedAction = new SchedulerWhen.DelayedAction(action, delayTime, unit);
/* 324 */       this.actionProcessor.onNext(delayedAction);
/* 325 */       return delayedAction;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public Disposable schedule(@NonNull Runnable action) {
/* 332 */       SchedulerWhen.ImmediateAction immediateAction = new SchedulerWhen.ImmediateAction(action);
/* 333 */       this.actionProcessor.onNext(immediateAction);
/* 334 */       return immediateAction;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SubscribedDisposable
/*     */     implements Disposable
/*     */   {
/*     */     public void dispose() {}
/*     */     
/*     */     public boolean isDisposed() {
/* 345 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/SchedulerWhen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */