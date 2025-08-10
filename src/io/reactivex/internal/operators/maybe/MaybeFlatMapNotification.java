/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class MaybeFlatMapNotification<T, R>
/*     */   extends AbstractMaybeWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;
/*     */   final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
/*     */   final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
/*     */   
/*     */   public MaybeFlatMapNotification(MaybeSource<T> source, Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier) {
/*  44 */     super(source);
/*  45 */     this.onSuccessMapper = onSuccessMapper;
/*  46 */     this.onErrorMapper = onErrorMapper;
/*  47 */     this.onCompleteSupplier = onCompleteSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/*  52 */     this.source.subscribe(new FlatMapMaybeObserver<T, R>(observer, this.onSuccessMapper, this.onErrorMapper, this.onCompleteSupplier));
/*     */   }
/*     */ 
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
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper;
/*     */     
/*     */     final Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper;
/*     */     
/*     */     final Callable<? extends MaybeSource<? extends R>> onCompleteSupplier;
/*     */     
/*     */     Disposable upstream;
/*     */ 
/*     */     
/*     */     FlatMapMaybeObserver(MaybeObserver<? super R> actual, Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier) {
/*  75 */       this.downstream = actual;
/*  76 */       this.onSuccessMapper = onSuccessMapper;
/*  77 */       this.onErrorMapper = onErrorMapper;
/*  78 */       this.onCompleteSupplier = onCompleteSupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  83 */       DisposableHelper.dispose(this);
/*  84 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  89 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  95 */         this.upstream = d;
/*     */         
/*  97 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       MaybeSource<? extends R> source;
/*     */       try {
/* 106 */         source = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.onSuccessMapper.apply(value), "The onSuccessMapper returned a null MaybeSource");
/* 107 */       } catch (Exception ex) {
/* 108 */         Exceptions.throwIfFatal(ex);
/* 109 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 113 */       source.subscribe(new InnerObserver());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       MaybeSource<? extends R> source;
/*     */       try {
/* 121 */         source = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.onErrorMapper.apply(e), "The onErrorMapper returned a null MaybeSource");
/* 122 */       } catch (Exception ex) {
/* 123 */         Exceptions.throwIfFatal(ex);
/* 124 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       source.subscribe(new InnerObserver());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       MaybeSource<? extends R> source;
/*     */       try {
/* 136 */         source = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onCompleteSupplier returned a null MaybeSource");
/* 137 */       } catch (Exception ex) {
/* 138 */         Exceptions.throwIfFatal(ex);
/* 139 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 143 */       source.subscribe(new InnerObserver());
/*     */     }
/*     */     
/*     */     final class InnerObserver
/*     */       implements MaybeObserver<R>
/*     */     {
/*     */       public void onSubscribe(Disposable d) {
/* 150 */         DisposableHelper.setOnce(MaybeFlatMapNotification.FlatMapMaybeObserver.this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R value) {
/* 155 */         MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 160 */         MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 165 */         MaybeFlatMapNotification.FlatMapMaybeObserver.this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */