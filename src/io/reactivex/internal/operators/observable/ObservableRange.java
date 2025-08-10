/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
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
/*     */ public final class ObservableRange
/*     */   extends Observable<Integer>
/*     */ {
/*     */   private final int start;
/*     */   private final long end;
/*     */   
/*     */   public ObservableRange(int start, int count) {
/*  27 */     this.start = start;
/*  28 */     this.end = start + count;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super Integer> o) {
/*  33 */     RangeDisposable parent = new RangeDisposable(o, this.start, this.end);
/*  34 */     o.onSubscribe((Disposable)parent);
/*  35 */     parent.run();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RangeDisposable
/*     */     extends BasicIntQueueDisposable<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = 396518478098735504L;
/*     */     
/*     */     final Observer<? super Integer> downstream;
/*     */     
/*     */     final long end;
/*     */     
/*     */     long index;
/*     */     boolean fused;
/*     */     
/*     */     RangeDisposable(Observer<? super Integer> actual, long start, long end) {
/*  52 */       this.downstream = actual;
/*  53 */       this.index = start;
/*  54 */       this.end = end;
/*     */     }
/*     */     
/*     */     void run() {
/*  58 */       if (this.fused) {
/*     */         return;
/*     */       }
/*  61 */       Observer<? super Integer> actual = this.downstream;
/*  62 */       long e = this.end; long i;
/*  63 */       for (i = this.index; i != e && get() == 0; i++) {
/*  64 */         actual.onNext(Integer.valueOf((int)i));
/*     */       }
/*  66 */       if (get() == 0) {
/*  67 */         lazySet(1);
/*  68 */         actual.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Integer poll() throws Exception {
/*  75 */       long i = this.index;
/*  76 */       if (i != this.end) {
/*  77 */         this.index = i + 1L;
/*  78 */         return Integer.valueOf((int)i);
/*     */       } 
/*  80 */       lazySet(1);
/*  81 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  86 */       return (this.index == this.end);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/*  91 */       this.index = this.end;
/*  92 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  97 */       set(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 102 */       return (get() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 107 */       if ((mode & 0x1) != 0) {
/* 108 */         this.fused = true;
/* 109 */         return 1;
/*     */       } 
/* 111 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */