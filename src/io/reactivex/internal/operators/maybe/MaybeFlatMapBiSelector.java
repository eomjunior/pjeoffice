/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeFlatMapBiSelector<T, U, R>
/*     */   extends AbstractMaybeWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends MaybeSource<? extends U>> mapper;
/*     */   final BiFunction<? super T, ? super U, ? extends R> resultSelector;
/*     */   
/*     */   public MaybeFlatMapBiSelector(MaybeSource<T> source, Function<? super T, ? extends MaybeSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
/*  42 */     super(source);
/*  43 */     this.mapper = mapper;
/*  44 */     this.resultSelector = resultSelector;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*  49 */     this.source.subscribe(new FlatMapBiMainObserver<T, U, R>(observer, this.mapper, this.resultSelector));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapBiMainObserver<T, U, R>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     final Function<? super T, ? extends MaybeSource<? extends U>> mapper;
/*     */     
/*     */     final InnerObserver<T, U, R> inner;
/*     */ 
/*     */     
/*     */     FlatMapBiMainObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends MaybeSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
/*  62 */       this.inner = new InnerObserver<T, U, R>(actual, resultSelector);
/*  63 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  68 */       DisposableHelper.dispose(this.inner);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  73 */       return DisposableHelper.isDisposed(this.inner.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  78 */       if (DisposableHelper.setOnce(this.inner, d)) {
/*  79 */         this.inner.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       MaybeSource<? extends U> next;
/*     */       try {
/*  88 */         next = (MaybeSource<? extends U>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null MaybeSource");
/*  89 */       } catch (Throwable ex) {
/*  90 */         Exceptions.throwIfFatal(ex);
/*  91 */         this.inner.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  95 */       if (DisposableHelper.replace(this.inner, null)) {
/*  96 */         this.inner.value = value;
/*  97 */         next.subscribe(this.inner);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 103 */       this.inner.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 108 */       this.inner.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     static final class InnerObserver<T, U, R>
/*     */       extends AtomicReference<Disposable>
/*     */       implements MaybeObserver<U>
/*     */     {
/*     */       private static final long serialVersionUID = -2897979525538174559L;
/*     */       
/*     */       final MaybeObserver<? super R> downstream;
/*     */       
/*     */       final BiFunction<? super T, ? super U, ? extends R> resultSelector;
/*     */       
/*     */       T value;
/*     */       
/*     */       InnerObserver(MaybeObserver<? super R> actual, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
/* 125 */         this.downstream = actual;
/* 126 */         this.resultSelector = resultSelector;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 131 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */       
/*     */       public void onSuccess(U value) {
/*     */         R r;
/* 136 */         T t = this.value;
/* 137 */         this.value = null;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 142 */           r = (R)ObjectHelper.requireNonNull(this.resultSelector.apply(t, value), "The resultSelector returned a null value");
/* 143 */         } catch (Throwable ex) {
/* 144 */           Exceptions.throwIfFatal(ex);
/* 145 */           this.downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 149 */         this.downstream.onSuccess(r);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 154 */         this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 159 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapBiSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */