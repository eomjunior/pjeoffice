/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ForEachWhileObserver<T>
/*     */   extends AtomicReference<Disposable>
/*     */   implements Observer<T>, Disposable
/*     */ {
/*     */   private static final long serialVersionUID = -4403180040475402120L;
/*     */   final Predicate<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   boolean done;
/*     */   
/*     */   public ForEachWhileObserver(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
/*  41 */     this.onNext = onNext;
/*  42 */     this.onError = onError;
/*  43 */     this.onComplete = onComplete;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  48 */     DisposableHelper.setOnce(this, d);
/*     */   }
/*     */   
/*     */   public void onNext(T t) {
/*     */     boolean b;
/*  53 */     if (this.done) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  59 */       b = this.onNext.test(t);
/*  60 */     } catch (Throwable ex) {
/*  61 */       Exceptions.throwIfFatal(ex);
/*  62 */       dispose();
/*  63 */       onError(ex);
/*     */       
/*     */       return;
/*     */     } 
/*  67 */     if (!b) {
/*  68 */       dispose();
/*  69 */       onComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/*  75 */     if (this.done) {
/*  76 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/*  79 */     this.done = true;
/*     */     try {
/*  81 */       this.onError.accept(t);
/*  82 */     } catch (Throwable ex) {
/*  83 */       Exceptions.throwIfFatal(ex);
/*  84 */       RxJavaPlugins.onError((Throwable)new CompositeException(new Throwable[] { t, ex }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  90 */     if (this.done) {
/*     */       return;
/*     */     }
/*  93 */     this.done = true;
/*     */     try {
/*  95 */       this.onComplete.run();
/*  96 */     } catch (Throwable ex) {
/*  97 */       Exceptions.throwIfFatal(ex);
/*  98 */       RxJavaPlugins.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 104 */     DisposableHelper.dispose(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 109 */     return DisposableHelper.isDisposed(get());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/ForEachWhileObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */