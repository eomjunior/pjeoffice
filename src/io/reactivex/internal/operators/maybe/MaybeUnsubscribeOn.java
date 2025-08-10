/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.Scheduler;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeUnsubscribeOn<T>
/*    */   extends AbstractMaybeWithUpstream<T, T>
/*    */ {
/*    */   final Scheduler scheduler;
/*    */   
/*    */   public MaybeUnsubscribeOn(MaybeSource<T> source, Scheduler scheduler) {
/* 32 */     super(source);
/* 33 */     this.scheduler = scheduler;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/* 38 */     this.source.subscribe(new UnsubscribeOnMaybeObserver<T>(observer, this.scheduler));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class UnsubscribeOnMaybeObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements MaybeObserver<T>, Disposable, Runnable
/*    */   {
/*    */     private static final long serialVersionUID = 3256698449646456986L;
/*    */     
/*    */     final MaybeObserver<? super T> downstream;
/*    */     final Scheduler scheduler;
/*    */     Disposable ds;
/*    */     
/*    */     UnsubscribeOnMaybeObserver(MaybeObserver<? super T> actual, Scheduler scheduler) {
/* 53 */       this.downstream = actual;
/* 54 */       this.scheduler = scheduler;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 59 */       Disposable d = getAndSet((Disposable)DisposableHelper.DISPOSED);
/* 60 */       if (d != DisposableHelper.DISPOSED) {
/* 61 */         this.ds = d;
/* 62 */         this.scheduler.scheduleDirect(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void run() {
/* 68 */       this.ds.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 73 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 78 */       if (DisposableHelper.setOnce(this, d)) {
/* 79 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 85 */       this.downstream.onSuccess(value);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 90 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 95 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeUnsubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */