/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
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
/*    */ public final class SingleMap<T, R>
/*    */   extends Single<R>
/*    */ {
/*    */   final SingleSource<? extends T> source;
/*    */   final Function<? super T, ? extends R> mapper;
/*    */   
/*    */   public SingleMap(SingleSource<? extends T> source, Function<? super T, ? extends R> mapper) {
/* 28 */     this.source = source;
/* 29 */     this.mapper = mapper;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super R> t) {
/* 34 */     this.source.subscribe(new MapSingleObserver<T, R>(t, this.mapper));
/*    */   }
/*    */   
/*    */   static final class MapSingleObserver<T, R>
/*    */     implements SingleObserver<T>
/*    */   {
/*    */     final SingleObserver<? super R> t;
/*    */     final Function<? super T, ? extends R> mapper;
/*    */     
/*    */     MapSingleObserver(SingleObserver<? super R> t, Function<? super T, ? extends R> mapper) {
/* 44 */       this.t = t;
/* 45 */       this.mapper = mapper;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 50 */       this.t.onSubscribe(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       R v;
/*    */       try {
/* 57 */         v = (R)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper function returned a null value.");
/* 58 */       } catch (Throwable e) {
/* 59 */         Exceptions.throwIfFatal(e);
/* 60 */         onError(e);
/*    */         
/*    */         return;
/*    */       } 
/* 64 */       this.t.onSuccess(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 69 */       this.t.onError(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */