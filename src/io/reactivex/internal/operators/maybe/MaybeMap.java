/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeMap<T, R>
/*    */   extends AbstractMaybeWithUpstream<T, R>
/*    */ {
/*    */   final Function<? super T, ? extends R> mapper;
/*    */   
/*    */   public MaybeMap(MaybeSource<T> source, Function<? super T, ? extends R> mapper) {
/* 34 */     super(source);
/* 35 */     this.mapper = mapper;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/* 40 */     this.source.subscribe(new MapMaybeObserver<T, R>(observer, this.mapper));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class MapMaybeObserver<T, R>
/*    */     implements MaybeObserver<T>, Disposable
/*    */   {
/*    */     final MaybeObserver<? super R> downstream;
/*    */     final Function<? super T, ? extends R> mapper;
/*    */     Disposable upstream;
/*    */     
/*    */     MapMaybeObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends R> mapper) {
/* 52 */       this.downstream = actual;
/* 53 */       this.mapper = mapper;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 58 */       Disposable d = this.upstream;
/* 59 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 60 */       d.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 65 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 70 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 71 */         this.upstream = d;
/*    */         
/* 73 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/*    */       R v;
/*    */       try {
/* 82 */         v = (R)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null item");
/* 83 */       } catch (Throwable ex) {
/* 84 */         Exceptions.throwIfFatal(ex);
/* 85 */         this.downstream.onError(ex);
/*    */         
/*    */         return;
/*    */       } 
/* 89 */       this.downstream.onSuccess(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 94 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 99 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */