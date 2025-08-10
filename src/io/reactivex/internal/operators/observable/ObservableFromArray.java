/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicQueueDisposable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableFromArray<T>
/*     */   extends Observable<T>
/*     */ {
/*     */   final T[] array;
/*     */   
/*     */   public ObservableFromArray(T[] array) {
/*  24 */     this.array = array;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*  29 */     FromArrayDisposable<T> d = new FromArrayDisposable<T>(observer, this.array);
/*     */     
/*  31 */     observer.onSubscribe((Disposable)d);
/*     */     
/*  33 */     if (d.fusionMode) {
/*     */       return;
/*     */     }
/*     */     
/*  37 */     d.run();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FromArrayDisposable<T>
/*     */     extends BasicQueueDisposable<T>
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final T[] array;
/*     */     
/*     */     int index;
/*     */     boolean fusionMode;
/*     */     volatile boolean disposed;
/*     */     
/*     */     FromArrayDisposable(Observer<? super T> actual, T[] array) {
/*  53 */       this.downstream = actual;
/*  54 */       this.array = array;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/*  59 */       if ((mode & 0x1) != 0) {
/*  60 */         this.fusionMode = true;
/*  61 */         return 1;
/*     */       } 
/*  63 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/*  69 */       int i = this.index;
/*  70 */       T[] a = this.array;
/*  71 */       if (i != a.length) {
/*  72 */         this.index = i + 1;
/*  73 */         return (T)ObjectHelper.requireNonNull(a[i], "The array element is null");
/*     */       } 
/*  75 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  80 */       return (this.index == this.array.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/*  85 */       this.index = this.array.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  90 */       this.disposed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  95 */       return this.disposed;
/*     */     }
/*     */     
/*     */     void run() {
/*  99 */       T[] a = this.array;
/* 100 */       int n = a.length;
/*     */       
/* 102 */       for (int i = 0; i < n && !isDisposed(); i++) {
/* 103 */         T value = a[i];
/* 104 */         if (value == null) {
/* 105 */           this.downstream.onError(new NullPointerException("The element at index " + i + " is null"));
/*     */           return;
/*     */         } 
/* 108 */         this.downstream.onNext(value);
/*     */       } 
/* 110 */       if (!isDisposed())
/* 111 */         this.downstream.onComplete(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFromArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */