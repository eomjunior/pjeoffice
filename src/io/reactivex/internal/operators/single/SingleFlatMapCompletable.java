/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
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
/*     */ public final class SingleFlatMapCompletable<T>
/*     */   extends Completable
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   
/*     */   public SingleFlatMapCompletable(SingleSource<T> source, Function<? super T, ? extends CompletableSource> mapper) {
/*  36 */     this.source = source;
/*  37 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  42 */     FlatMapCompletableObserver<T> parent = new FlatMapCompletableObserver<T>(observer, this.mapper);
/*  43 */     observer.onSubscribe(parent);
/*  44 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapCompletableObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements SingleObserver<T>, CompletableObserver, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2177128922851101253L;
/*     */     
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     FlatMapCompletableObserver(CompletableObserver actual, Function<? super T, ? extends CompletableSource> mapper) {
/*  59 */       this.downstream = actual;
/*  60 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  65 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  70 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  75 */       DisposableHelper.replace(this, d);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       CompletableSource cs;
/*     */       try {
/*  83 */         cs = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null CompletableSource");
/*  84 */       } catch (Throwable ex) {
/*  85 */         Exceptions.throwIfFatal(ex);
/*  86 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  90 */       if (!isDisposed()) {
/*  91 */         cs.subscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  97 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */