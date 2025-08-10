/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.util.NotificationLite;
/*    */ import java.util.Queue;
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
/*    */ public final class BlockingObserver<T>
/*    */   extends AtomicReference<Disposable>
/*    */   implements Observer<T>, Disposable
/*    */ {
/*    */   private static final long serialVersionUID = -4875965440900746268L;
/* 28 */   public static final Object TERMINATED = new Object();
/*    */   
/*    */   final Queue<Object> queue;
/*    */   
/*    */   public BlockingObserver(Queue<Object> queue) {
/* 33 */     this.queue = queue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 38 */     DisposableHelper.setOnce(this, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onNext(T t) {
/* 43 */     this.queue.offer(NotificationLite.next(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 48 */     this.queue.offer(NotificationLite.error(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 53 */     this.queue.offer(NotificationLite.complete());
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 58 */     if (DisposableHelper.dispose(this)) {
/* 59 */       this.queue.offer(TERMINATED);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 65 */     return (get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BlockingObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */