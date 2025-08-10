/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
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
/*     */ public final class MaybeSubscribeOn<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public MaybeSubscribeOn(MaybeSource<T> source, Scheduler scheduler) {
/*  31 */     super(source);
/*  32 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  37 */     SubscribeOnMaybeObserver<T> parent = new SubscribeOnMaybeObserver<T>(observer);
/*  38 */     observer.onSubscribe(parent);
/*     */     
/*  40 */     parent.task.replace(this.scheduler.scheduleDirect(new SubscribeTask<T>(parent, this.source)));
/*     */   }
/*     */   
/*     */   static final class SubscribeTask<T> implements Runnable {
/*     */     final MaybeObserver<? super T> observer;
/*     */     final MaybeSource<T> source;
/*     */     
/*     */     SubscribeTask(MaybeObserver<? super T> observer, MaybeSource<T> source) {
/*  48 */       this.observer = observer;
/*  49 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  54 */       this.source.subscribe(this.observer);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SubscribeOnMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final SequentialDisposable task;
/*     */     
/*     */     private static final long serialVersionUID = 8571289934935992137L;
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     SubscribeOnMaybeObserver(MaybeObserver<? super T> downstream) {
/*  69 */       this.downstream = downstream;
/*  70 */       this.task = new SequentialDisposable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  75 */       DisposableHelper.dispose(this);
/*  76 */       this.task.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  81 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  86 */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  91 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  96 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 101 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */