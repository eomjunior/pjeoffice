/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DeferredScalarObserver<T, R>
/*    */   extends DeferredScalarDisposable<R>
/*    */   implements Observer<T>
/*    */ {
/*    */   private static final long serialVersionUID = -266195175408988651L;
/*    */   protected Disposable upstream;
/*    */   
/*    */   public DeferredScalarObserver(Observer<? super R> downstream) {
/* 39 */     super(downstream);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 44 */     if (DisposableHelper.validate(this.upstream, d)) {
/* 45 */       this.upstream = d;
/*    */       
/* 47 */       this.downstream.onSubscribe((Disposable)this);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 53 */     this.value = null;
/* 54 */     error(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 59 */     R v = this.value;
/* 60 */     if (v != null) {
/* 61 */       this.value = null;
/* 62 */       complete(v);
/*    */     } else {
/* 64 */       complete();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 70 */     super.dispose();
/* 71 */     this.upstream.dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/DeferredScalarObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */