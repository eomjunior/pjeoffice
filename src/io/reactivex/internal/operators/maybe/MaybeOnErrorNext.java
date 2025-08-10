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
/*     */ public final class MaybeOnErrorNext<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;
/*     */   final boolean allowFatal;
/*     */   
/*     */   public MaybeOnErrorNext(MaybeSource<T> source, Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction, boolean allowFatal) {
/*  39 */     super(source);
/*  40 */     this.resumeFunction = resumeFunction;
/*  41 */     this.allowFatal = allowFatal;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  46 */     this.source.subscribe(new OnErrorNextMaybeObserver<T>(observer, this.resumeFunction, this.allowFatal));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class OnErrorNextMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 2026620218879969836L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     final Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction;
/*     */     
/*     */     final boolean allowFatal;
/*     */ 
/*     */     
/*     */     OnErrorNextMaybeObserver(MaybeObserver<? super T> actual, Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction, boolean allowFatal) {
/*  64 */       this.downstream = actual;
/*  65 */       this.resumeFunction = resumeFunction;
/*  66 */       this.allowFatal = allowFatal;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  71 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  76 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  81 */       if (DisposableHelper.setOnce(this, d)) {
/*  82 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  88 */       this.downstream.onSuccess(value);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       MaybeSource<? extends T> m;
/*  93 */       if (!this.allowFatal && !(e instanceof Exception)) {
/*  94 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 100 */         m = (MaybeSource<? extends T>)ObjectHelper.requireNonNull(this.resumeFunction.apply(e), "The resumeFunction returned a null MaybeSource");
/* 101 */       } catch (Throwable ex) {
/* 102 */         Exceptions.throwIfFatal(ex);
/* 103 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*     */         
/*     */         return;
/*     */       } 
/* 107 */       DisposableHelper.replace(this, null);
/*     */       
/* 109 */       m.subscribe(new NextMaybeObserver<T>(this.downstream, this));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 114 */       this.downstream.onComplete();
/*     */     }
/*     */     
/*     */     static final class NextMaybeObserver<T>
/*     */       implements MaybeObserver<T> {
/*     */       final MaybeObserver<? super T> downstream;
/*     */       final AtomicReference<Disposable> upstream;
/*     */       
/*     */       NextMaybeObserver(MaybeObserver<? super T> actual, AtomicReference<Disposable> d) {
/* 123 */         this.downstream = actual;
/* 124 */         this.upstream = d;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 129 */         DisposableHelper.setOnce(this.upstream, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T value) {
/* 134 */         this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 139 */         this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 144 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeOnErrorNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */