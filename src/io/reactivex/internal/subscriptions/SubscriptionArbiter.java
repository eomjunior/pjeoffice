/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public class SubscriptionArbiter
/*     */   extends AtomicInteger
/*     */   implements Subscription
/*     */ {
/*     */   private static final long serialVersionUID = -2189523197179400958L;
/*     */   Subscription actual;
/*     */   long requested;
/*     */   final AtomicReference<Subscription> missedSubscription;
/*     */   final AtomicLong missedRequested;
/*     */   final AtomicLong missedProduced;
/*     */   final boolean cancelOnReplace;
/*     */   volatile boolean cancelled;
/*     */   protected boolean unbounded;
/*     */   
/*     */   public SubscriptionArbiter(boolean cancelOnReplace) {
/*  65 */     this.cancelOnReplace = cancelOnReplace;
/*  66 */     this.missedSubscription = new AtomicReference<Subscription>();
/*  67 */     this.missedRequested = new AtomicLong();
/*  68 */     this.missedProduced = new AtomicLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSubscription(Subscription s) {
/*  76 */     if (this.cancelled) {
/*  77 */       s.cancel();
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     ObjectHelper.requireNonNull(s, "s is null");
/*     */     
/*  83 */     if (get() == 0 && compareAndSet(0, 1)) {
/*  84 */       Subscription subscription = this.actual;
/*     */       
/*  86 */       if (subscription != null && this.cancelOnReplace) {
/*  87 */         subscription.cancel();
/*     */       }
/*     */       
/*  90 */       this.actual = s;
/*     */       
/*  92 */       long r = this.requested;
/*     */       
/*  94 */       if (decrementAndGet() != 0) {
/*  95 */         drainLoop();
/*     */       }
/*     */       
/*  98 */       if (r != 0L) {
/*  99 */         s.request(r);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 105 */     Subscription a = this.missedSubscription.getAndSet(s);
/* 106 */     if (a != null && this.cancelOnReplace) {
/* 107 */       a.cancel();
/*     */     }
/* 109 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void request(long n) {
/* 114 */     if (SubscriptionHelper.validate(n)) {
/* 115 */       if (this.unbounded) {
/*     */         return;
/*     */       }
/* 118 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 119 */         long r = this.requested;
/*     */         
/* 121 */         if (r != Long.MAX_VALUE) {
/* 122 */           r = BackpressureHelper.addCap(r, n);
/* 123 */           this.requested = r;
/* 124 */           if (r == Long.MAX_VALUE) {
/* 125 */             this.unbounded = true;
/*     */           }
/*     */         } 
/* 128 */         Subscription a = this.actual;
/*     */         
/* 130 */         if (decrementAndGet() != 0) {
/* 131 */           drainLoop();
/*     */         }
/*     */         
/* 134 */         if (a != null) {
/* 135 */           a.request(n);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 141 */       BackpressureHelper.add(this.missedRequested, n);
/*     */       
/* 143 */       drain();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void produced(long n) {
/* 148 */     if (this.unbounded) {
/*     */       return;
/*     */     }
/* 151 */     if (get() == 0 && compareAndSet(0, 1)) {
/* 152 */       long r = this.requested;
/*     */       
/* 154 */       if (r != Long.MAX_VALUE) {
/* 155 */         long u = r - n;
/* 156 */         if (u < 0L) {
/* 157 */           SubscriptionHelper.reportMoreProduced(u);
/* 158 */           u = 0L;
/*     */         } 
/* 160 */         this.requested = u;
/*     */       } 
/*     */       
/* 163 */       if (decrementAndGet() == 0) {
/*     */         return;
/*     */       }
/*     */       
/* 167 */       drainLoop();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 172 */     BackpressureHelper.add(this.missedProduced, n);
/*     */     
/* 174 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 179 */     if (!this.cancelled) {
/* 180 */       this.cancelled = true;
/*     */       
/* 182 */       drain();
/*     */     } 
/*     */   }
/*     */   
/*     */   final void drain() {
/* 187 */     if (getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/* 190 */     drainLoop();
/*     */   }
/*     */   
/*     */   final void drainLoop() {
/* 194 */     int missed = 1;
/*     */     
/* 196 */     long requestAmount = 0L;
/* 197 */     Subscription requestTarget = null;
/*     */ 
/*     */     
/*     */     while (true) {
/* 201 */       Subscription ms = this.missedSubscription.get();
/*     */       
/* 203 */       if (ms != null) {
/* 204 */         ms = this.missedSubscription.getAndSet(null);
/*     */       }
/*     */       
/* 207 */       long mr = this.missedRequested.get();
/* 208 */       if (mr != 0L) {
/* 209 */         mr = this.missedRequested.getAndSet(0L);
/*     */       }
/*     */       
/* 212 */       long mp = this.missedProduced.get();
/* 213 */       if (mp != 0L) {
/* 214 */         mp = this.missedProduced.getAndSet(0L);
/*     */       }
/*     */       
/* 217 */       Subscription a = this.actual;
/*     */       
/* 219 */       if (this.cancelled) {
/* 220 */         if (a != null) {
/* 221 */           a.cancel();
/* 222 */           this.actual = null;
/*     */         } 
/* 224 */         if (ms != null) {
/* 225 */           ms.cancel();
/*     */         }
/*     */       } else {
/* 228 */         long r = this.requested;
/* 229 */         if (r != Long.MAX_VALUE) {
/* 230 */           long u = BackpressureHelper.addCap(r, mr);
/*     */           
/* 232 */           if (u != Long.MAX_VALUE) {
/* 233 */             long v = u - mp;
/* 234 */             if (v < 0L) {
/* 235 */               SubscriptionHelper.reportMoreProduced(v);
/* 236 */               v = 0L;
/*     */             } 
/* 238 */             r = v;
/*     */           } else {
/* 240 */             r = u;
/*     */           } 
/* 242 */           this.requested = r;
/*     */         } 
/*     */         
/* 245 */         if (ms != null) {
/* 246 */           if (a != null && this.cancelOnReplace) {
/* 247 */             a.cancel();
/*     */           }
/* 249 */           this.actual = ms;
/* 250 */           if (r != 0L) {
/* 251 */             requestAmount = BackpressureHelper.addCap(requestAmount, r);
/* 252 */             requestTarget = ms;
/*     */           } 
/* 254 */         } else if (a != null && mr != 0L) {
/* 255 */           requestAmount = BackpressureHelper.addCap(requestAmount, mr);
/* 256 */           requestTarget = a;
/*     */         } 
/*     */       } 
/*     */       
/* 260 */       missed = addAndGet(-missed);
/* 261 */       if (missed == 0) {
/* 262 */         if (requestAmount != 0L) {
/* 263 */           requestTarget.request(requestAmount);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isUnbounded() {
/* 275 */     return this.unbounded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCancelled() {
/* 283 */     return this.cancelled;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/SubscriptionArbiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */