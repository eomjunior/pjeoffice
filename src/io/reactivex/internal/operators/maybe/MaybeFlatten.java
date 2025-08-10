/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
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
/*     */ public final class MaybeFlatten<T, R>
/*     */   extends AbstractMaybeWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatten(MaybeSource<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  36 */     super(source);
/*  37 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*  42 */     this.source.subscribe(new FlatMapMaybeObserver<T, R>(observer, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapMaybeObserver<T, R>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 4375739915521278546L;
/*     */     
/*     */     final MaybeObserver<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     FlatMapMaybeObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
/*  59 */       this.downstream = actual;
/*  60 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       DisposableHelper.dispose(this);
/*  66 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  71 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  76 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  77 */         this.upstream = d;
/*     */         
/*  79 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       MaybeSource<? extends R> source;
/*     */       try {
/*  88 */         source = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null MaybeSource");
/*  89 */       } catch (Exception ex) {
/*  90 */         Exceptions.throwIfFatal(ex);
/*  91 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  95 */       if (!isDisposed()) {
/*  96 */         source.subscribe(new InnerObserver());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 102 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 107 */       this.downstream.onComplete();
/*     */     }
/*     */     
/*     */     final class InnerObserver
/*     */       implements MaybeObserver<R>
/*     */     {
/*     */       public void onSubscribe(Disposable d) {
/* 114 */         DisposableHelper.setOnce(MaybeFlatten.FlatMapMaybeObserver.this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 119 */         MaybeFlatten.FlatMapMaybeObserver.this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 124 */         MaybeFlatten.FlatMapMaybeObserver.this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 129 */         MaybeFlatten.FlatMapMaybeObserver.this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatten.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */