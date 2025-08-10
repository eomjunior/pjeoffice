/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
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
/*     */ public final class ObservableTakeUntil<T, U>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final ObservableSource<? extends U> other;
/*     */   
/*     */   public ObservableTakeUntil(ObservableSource<T> source, ObservableSource<? extends U> other) {
/*  28 */     super(source);
/*  29 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> child) {
/*  34 */     TakeUntilMainObserver<T, U> parent = new TakeUntilMainObserver<T, U>(child);
/*  35 */     child.onSubscribe(parent);
/*     */     
/*  37 */     this.other.subscribe(parent.otherObserver);
/*  38 */     this.source.subscribe(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeUntilMainObserver<T, U>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 1418547743690811973L;
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     final OtherObserver otherObserver;
/*     */     final AtomicThrowable error;
/*     */     
/*     */     TakeUntilMainObserver(Observer<? super T> downstream) {
/*  55 */       this.downstream = downstream;
/*  56 */       this.upstream = new AtomicReference<Disposable>();
/*  57 */       this.otherObserver = new OtherObserver();
/*  58 */       this.error = new AtomicThrowable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  63 */       DisposableHelper.dispose(this.upstream);
/*  64 */       DisposableHelper.dispose(this.otherObserver);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  69 */       return DisposableHelper.isDisposed(this.upstream.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       HalfSerializer.onNext(this.downstream, t, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  84 */       DisposableHelper.dispose(this.otherObserver);
/*  85 */       HalfSerializer.onError(this.downstream, e, this, this.error);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  90 */       DisposableHelper.dispose(this.otherObserver);
/*  91 */       HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */     }
/*     */     
/*     */     void otherError(Throwable e) {
/*  95 */       DisposableHelper.dispose(this.upstream);
/*  96 */       HalfSerializer.onError(this.downstream, e, this, this.error);
/*     */     }
/*     */     
/*     */     void otherComplete() {
/* 100 */       DisposableHelper.dispose(this.upstream);
/* 101 */       HalfSerializer.onComplete(this.downstream, this, this.error);
/*     */     }
/*     */     
/*     */     final class OtherObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements Observer<U>
/*     */     {
/*     */       private static final long serialVersionUID = -8693423678067375039L;
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 111 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onNext(U t) {
/* 116 */         DisposableHelper.dispose(this);
/* 117 */         ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 122 */         ObservableTakeUntil.TakeUntilMainObserver.this.otherError(e);
/*     */       }
/*     */       
/*     */       public void onComplete()
/*     */       {
/* 127 */         ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete(); } } } final class OtherObserver extends AtomicReference<Disposable> implements Observer<U> { public void onComplete() { ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete(); }
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = -8693423678067375039L;
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*     */       DisposableHelper.setOnce(this, d);
/*     */     }
/*     */     
/*     */     public void onNext(U t) {
/*     */       DisposableHelper.dispose(this);
/*     */       ObservableTakeUntil.TakeUntilMainObserver.this.otherComplete();
/*     */     }
/*     */     
/*     */     public void onError(Throwable e) {
/*     */       ObservableTakeUntil.TakeUntilMainObserver.this.otherError(e);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableTakeUntil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */