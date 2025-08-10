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
/*     */ public final class ObservableRangeLong
/*     */   extends Observable<Long>
/*     */ {
/*     */   private final long start;
/*     */   private final long count;
/*     */   
/*     */   public ObservableRangeLong(long start, long count) {
/*  24 */     this.start = start;
/*  25 */     this.count = count;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super Long> o) {
/*  30 */     RangeDisposable parent = new RangeDisposable(o, this.start, this.start + this.count);
/*  31 */     o.onSubscribe((Disposable)parent);
/*  32 */     parent.run();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class RangeDisposable
/*     */     extends BasicIntQueueDisposable<Long>
/*     */   {
/*     */     private static final long serialVersionUID = 396518478098735504L;
/*     */     
/*     */     final Observer<? super Long> downstream;
/*     */     
/*     */     final long end;
/*     */     
/*     */     long index;
/*     */     boolean fused;
/*     */     
/*     */     RangeDisposable(Observer<? super Long> actual, long start, long end) {
/*  49 */       this.downstream = actual;
/*  50 */       this.index = start;
/*  51 */       this.end = end;
/*     */     }
/*     */     
/*     */     void run() {
/*  55 */       if (this.fused) {
/*     */         return;
/*     */       }
/*  58 */       Observer<? super Long> actual = this.downstream;
/*  59 */       long e = this.end; long i;
/*  60 */       for (i = this.index; i != e && get() == 0; i++) {
/*  61 */         actual.onNext(Long.valueOf(i));
/*     */       }
/*  63 */       if (get() == 0) {
/*  64 */         lazySet(1);
/*  65 */         actual.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Long poll() throws Exception {
/*  72 */       long i = this.index;
/*  73 */       if (i != this.end) {
/*  74 */         this.index = i + 1L;
/*  75 */         return Long.valueOf(i);
/*     */       } 
/*  77 */       lazySet(1);
/*  78 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  83 */       return (this.index == this.end);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/*  88 */       this.index = this.end;
/*  89 */       lazySet(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  94 */       set(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  99 */       return (get() != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 104 */       if ((mode & 0x1) != 0) {
/* 105 */         this.fused = true;
/* 106 */         return 1;
/*     */       } 
/* 108 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableRangeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */