/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.util.BlockingHelper;
/*    */ import io.reactivex.internal.util.ExceptionHelper;
/*    */ import java.util.concurrent.CountDownLatch;
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
/*    */ public abstract class BlockingBaseObserver<T>
/*    */   extends CountDownLatch
/*    */   implements Observer<T>, Disposable
/*    */ {
/*    */   T value;
/*    */   Throwable error;
/*    */   Disposable upstream;
/*    */   volatile boolean cancelled;
/*    */   
/*    */   public BlockingBaseObserver() {
/* 32 */     super(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onSubscribe(Disposable d) {
/* 37 */     this.upstream = d;
/* 38 */     if (this.cancelled) {
/* 39 */       d.dispose();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void onComplete() {
/* 45 */     countDown();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 50 */     this.cancelled = true;
/* 51 */     Disposable d = this.upstream;
/* 52 */     if (d != null) {
/* 53 */       d.dispose();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isDisposed() {
/* 59 */     return this.cancelled;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final T blockingGet() {
/* 68 */     if (getCount() != 0L) {
/*    */       try {
/* 70 */         BlockingHelper.verifyNonBlocking();
/* 71 */         await();
/* 72 */       } catch (InterruptedException ex) {
/* 73 */         dispose();
/* 74 */         throw ExceptionHelper.wrapOrThrow(ex);
/*    */       } 
/*    */     }
/*    */     
/* 78 */     Throwable e = this.error;
/* 79 */     if (e != null) {
/* 80 */       throw ExceptionHelper.wrapOrThrow(e);
/*    */     }
/* 82 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BlockingBaseObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */