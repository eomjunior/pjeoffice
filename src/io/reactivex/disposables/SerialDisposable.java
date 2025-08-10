/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import io.reactivex.annotations.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SerialDisposable
/*    */   implements Disposable
/*    */ {
/*    */   final AtomicReference<Disposable> resource;
/*    */   
/*    */   public SerialDisposable() {
/* 33 */     this.resource = new AtomicReference<Disposable>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SerialDisposable(@Nullable Disposable initialDisposable) {
/* 41 */     this.resource = new AtomicReference<Disposable>(initialDisposable);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean set(@Nullable Disposable next) {
/* 52 */     return DisposableHelper.set(this.resource, next);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean replace(@Nullable Disposable next) {
/* 63 */     return DisposableHelper.replace(this.resource, next);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Disposable get() {
/* 72 */     Disposable d = this.resource.get();
/* 73 */     if (d == DisposableHelper.DISPOSED) {
/* 74 */       return Disposables.disposed();
/*    */     }
/* 76 */     return d;
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 81 */     DisposableHelper.dispose(this.resource);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 86 */     return DisposableHelper.isDisposed(this.resource.get());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/SerialDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */