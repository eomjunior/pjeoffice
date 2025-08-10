/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.annotations.Nullable;
/*    */ import io.reactivex.functions.Consumer;
/*    */ import io.reactivex.internal.observers.BasicFuseableObserver;
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
/*    */ public final class ObservableDoAfterNext<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Consumer<? super T> onAfterNext;
/*    */   
/*    */   public ObservableDoAfterNext(ObservableSource<T> source, Consumer<? super T> onAfterNext) {
/* 32 */     super(source);
/* 33 */     this.onAfterNext = onAfterNext;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 38 */     this.source.subscribe((Observer)new DoAfterObserver<T>(observer, this.onAfterNext));
/*    */   }
/*    */   
/*    */   static final class DoAfterObserver<T>
/*    */     extends BasicFuseableObserver<T, T> {
/*    */     final Consumer<? super T> onAfterNext;
/*    */     
/*    */     DoAfterObserver(Observer<? super T> actual, Consumer<? super T> onAfterNext) {
/* 46 */       super(actual);
/* 47 */       this.onAfterNext = onAfterNext;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 52 */       this.downstream.onNext(t);
/*    */       
/* 54 */       if (this.sourceMode == 0) {
/*    */         try {
/* 56 */           this.onAfterNext.accept(t);
/* 57 */         } catch (Throwable ex) {
/* 58 */           fail(ex);
/*    */         } 
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public int requestFusion(int mode) {
/* 65 */       return transitiveBoundaryFusion(mode);
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public T poll() throws Exception {
/* 71 */       T v = (T)this.qd.poll();
/* 72 */       if (v != null) {
/* 73 */         this.onAfterNext.accept(v);
/*    */       }
/* 75 */       return v;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDoAfterNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */