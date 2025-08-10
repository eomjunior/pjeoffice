/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ abstract class ReferenceDisposable<T>
/*    */   extends AtomicReference<T>
/*    */   implements Disposable
/*    */ {
/*    */   private static final long serialVersionUID = 6537757548749041217L;
/*    */   
/*    */   ReferenceDisposable(T value) {
/* 32 */     super((T)ObjectHelper.requireNonNull(value, "value is null"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void onDisposed(@NonNull T paramT);
/*    */   
/*    */   public final void dispose() {
/* 39 */     T value = get();
/* 40 */     if (value != null) {
/* 41 */       value = getAndSet(null);
/* 42 */       if (value != null) {
/* 43 */         onDisposed(value);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isDisposed() {
/* 50 */     return (get() == null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/ReferenceDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */