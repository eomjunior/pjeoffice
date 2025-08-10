/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.annotations.Nullable;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class ObservableMap<T, U>
/*    */   extends AbstractObservableWithUpstream<T, U>
/*    */ {
/*    */   final Function<? super T, ? extends U> function;
/*    */   
/*    */   public ObservableMap(ObservableSource<T> source, Function<? super T, ? extends U> function) {
/* 26 */     super(source);
/* 27 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super U> t) {
/* 32 */     this.source.subscribe((Observer)new MapObserver<T, U>(t, this.function));
/*    */   }
/*    */   
/*    */   static final class MapObserver<T, U> extends BasicFuseableObserver<T, U> {
/*    */     final Function<? super T, ? extends U> mapper;
/*    */     
/*    */     MapObserver(Observer<? super U> actual, Function<? super T, ? extends U> mapper) {
/* 39 */       super(actual);
/* 40 */       this.mapper = mapper;
/*    */     }
/*    */     
/*    */     public void onNext(T t) {
/*    */       U v;
/* 45 */       if (this.done) {
/*    */         return;
/*    */       }
/*    */       
/* 49 */       if (this.sourceMode != 0) {
/* 50 */         this.downstream.onNext(null);
/*    */ 
/*    */         
/*    */         return;
/*    */       } 
/*    */       
/*    */       try {
/* 57 */         v = (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.");
/* 58 */       } catch (Throwable ex) {
/* 59 */         fail(ex);
/*    */         return;
/*    */       } 
/* 62 */       this.downstream.onNext(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public int requestFusion(int mode) {
/* 67 */       return transitiveBoundaryFusion(mode);
/*    */     }
/*    */ 
/*    */     
/*    */     @Nullable
/*    */     public U poll() throws Exception {
/* 73 */       T t = (T)this.qd.poll();
/* 74 */       return (t != null) ? (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.") : null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */