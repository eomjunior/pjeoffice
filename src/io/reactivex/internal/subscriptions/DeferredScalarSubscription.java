/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ 
/*     */ 
/*     */ public class DeferredScalarSubscription<T>
/*     */   extends BasicIntQueueSubscription<T>
/*     */ {
/*     */   private static final long serialVersionUID = -2151279923272604993L;
/*     */   protected final Subscriber<? super T> downstream;
/*     */   protected T value;
/*     */   static final int NO_REQUEST_NO_VALUE = 0;
/*     */   static final int NO_REQUEST_HAS_VALUE = 1;
/*     */   static final int HAS_REQUEST_NO_VALUE = 2;
/*     */   static final int HAS_REQUEST_HAS_VALUE = 3;
/*     */   static final int CANCELLED = 4;
/*     */   static final int FUSED_EMPTY = 8;
/*     */   static final int FUSED_READY = 16;
/*     */   static final int FUSED_CONSUMED = 32;
/*     */   
/*     */   public DeferredScalarSubscription(Subscriber<? super T> downstream) {
/*  69 */     this.downstream = downstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void request(long n) {
/*  74 */     if (SubscriptionHelper.validate(n)) {
/*     */       do {
/*  76 */         int state = get();
/*     */ 
/*     */         
/*  79 */         if ((state & 0xFFFFFFFE) != 0) {
/*     */           return;
/*     */         }
/*  82 */         if (state == 1) {
/*  83 */           if (compareAndSet(1, 3)) {
/*  84 */             T v = this.value;
/*  85 */             if (v != null) {
/*  86 */               this.value = null;
/*  87 */               Subscriber<? super T> a = this.downstream;
/*  88 */               a.onNext(v);
/*  89 */               if (get() != 4) {
/*  90 */                 a.onComplete();
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return;
/*     */         } 
/*  96 */       } while (!compareAndSet(0, 2));
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void complete(T v) {
/* 110 */     int state = get();
/*     */     while (true) {
/* 112 */       if (state == 8) {
/* 113 */         this.value = v;
/* 114 */         lazySet(16);
/*     */         
/* 116 */         Subscriber<? super T> a = this.downstream;
/* 117 */         a.onNext(v);
/* 118 */         if (get() != 4) {
/* 119 */           a.onComplete();
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 125 */       if ((state & 0xFFFFFFFD) != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 129 */       if (state == 2) {
/* 130 */         lazySet(3);
/* 131 */         Subscriber<? super T> a = this.downstream;
/* 132 */         a.onNext(v);
/* 133 */         if (get() != 4) {
/* 134 */           a.onComplete();
/*     */         }
/*     */         return;
/*     */       } 
/* 138 */       this.value = v;
/* 139 */       if (compareAndSet(0, 1)) {
/*     */         return;
/*     */       }
/* 142 */       state = get();
/* 143 */       if (state == 4) {
/* 144 */         this.value = null;
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final int requestFusion(int mode) {
/* 152 */     if ((mode & 0x2) != 0) {
/* 153 */       lazySet(8);
/* 154 */       return 2;
/*     */     } 
/* 156 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final T poll() {
/* 162 */     if (get() == 16) {
/* 163 */       lazySet(32);
/* 164 */       T v = this.value;
/* 165 */       this.value = null;
/* 166 */       return v;
/*     */     } 
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 173 */     return (get() != 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 178 */     lazySet(32);
/* 179 */     this.value = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 184 */     set(4);
/* 185 */     this.value = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCancelled() {
/* 193 */     return (get() == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean tryCancel() {
/* 202 */     return (getAndSet(4) != 4);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/DeferredScalarSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */