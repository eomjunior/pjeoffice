/*    */ package io.reactivex.internal.disposables;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*    */ public final class ArrayCompositeDisposable
/*    */   extends AtomicReferenceArray<Disposable>
/*    */   implements Disposable
/*    */ {
/*    */   private static final long serialVersionUID = 2746389416410565408L;
/*    */   
/*    */   public ArrayCompositeDisposable(int capacity) {
/* 32 */     super(capacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean setResource(int index, Disposable resource) {
/*    */     while (true) {
/* 43 */       Disposable o = get(index);
/* 44 */       if (o == DisposableHelper.DISPOSED) {
/* 45 */         resource.dispose();
/* 46 */         return false;
/*    */       } 
/* 48 */       if (compareAndSet(index, o, resource)) {
/* 49 */         if (o != null) {
/* 50 */           o.dispose();
/*    */         }
/* 52 */         return true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Disposable replaceResource(int index, Disposable resource) {
/*    */     while (true) {
/* 65 */       Disposable o = get(index);
/* 66 */       if (o == DisposableHelper.DISPOSED) {
/* 67 */         resource.dispose();
/* 68 */         return null;
/*    */       } 
/* 70 */       if (compareAndSet(index, o, resource)) {
/* 71 */         return o;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 78 */     if (get(0) != DisposableHelper.DISPOSED) {
/* 79 */       int s = length();
/* 80 */       for (int i = 0; i < s; i++) {
/* 81 */         Disposable o = get(i);
/* 82 */         if (o != DisposableHelper.DISPOSED) {
/* 83 */           o = getAndSet(i, DisposableHelper.DISPOSED);
/* 84 */           if (o != DisposableHelper.DISPOSED && o != null) {
/* 85 */             o.dispose();
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 94 */     return (get(0) == DisposableHelper.DISPOSED);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/ArrayCompositeDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */