/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.HalfSerializer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ObservableMergeWithCompletable<T>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final CompletableSource other;
/*     */   
/*     */   public ObservableMergeWithCompletable(Observable<T> source, CompletableSource other) {
/*  35 */     super((ObservableSource<T>)source);
/*  36 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  41 */     MergeWithObserver<T> parent = new MergeWithObserver<T>(observer);
/*  42 */     observer.onSubscribe(parent);
/*  43 */     this.source.subscribe(parent);
/*  44 */     this.other.subscribe(parent.otherObserver);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeWithObserver<T>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -4592979584110982903L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Disposable> mainDisposable;
/*     */     
/*     */     final OtherObserver otherObserver;
/*     */     
/*     */     final AtomicThrowable error;
/*     */     volatile boolean mainDone;
/*     */     volatile boolean otherDone;
/*     */     
/*     */     MergeWithObserver(Observer<? super T> downstream) {
/*  65 */       this.downstream = downstream;
/*  66 */       this.mainDisposable = new AtomicReference<Disposable>();
/*  67 */       this.otherObserver = new OtherObserver(this);
/*  68 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  73 */       DisposableHelper.setOnce(this.mainDisposable, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  78 */       HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable ex) {
/*  83 */       DisposableHelper.dispose(this.otherObserver);
/*  84 */       HalfSerializer.onError(this.downstream, ex, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  89 */       this.mainDone = true;
/*  90 */       if (this.otherDone) {
/*  91 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  97 */       return DisposableHelper.isDisposed(this.mainDisposable.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 102 */       DisposableHelper.dispose(this.mainDisposable);
/* 103 */       DisposableHelper.dispose(this.otherObserver);
/*     */     }
/*     */     
/*     */     void otherError(Throwable ex) {
/* 107 */       DisposableHelper.dispose(this.mainDisposable);
/* 108 */       HalfSerializer.onError(this.downstream, ex, this, this.error);
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 112 */       this.otherDone = true;
/* 113 */       if (this.mainDone) {
/* 114 */         HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */       }
/*     */     }
/*     */     
/*     */     static final class OtherObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver
/*     */     {
/*     */       private static final long serialVersionUID = -2935427570954647017L;
/*     */       final ObservableMergeWithCompletable.MergeWithObserver<?> parent;
/*     */       
/*     */       OtherObserver(ObservableMergeWithCompletable.MergeWithObserver<?> parent) {
/* 126 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 131 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 136 */         this.parent.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 141 */         this.parent.otherComplete(); } } } static final class OtherObserver extends AtomicReference<Disposable> implements CompletableObserver { public void onComplete() { this.parent.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -2935427570954647017L;
/*     */     final ObservableMergeWithCompletable.MergeWithObserver<?> parent;
/*     */     
/*     */     OtherObserver(ObservableMergeWithCompletable.MergeWithObserver<?> parent) {
/*     */       this.parent = parent;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       this.parent.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableMergeWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */