/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscriptions.BasicQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class FlowableRangeLong
/*     */   extends Flowable<Long>
/*     */ {
/*     */   final long start;
/*     */   final long end;
/*     */   
/*     */   public FlowableRangeLong(long start, long count) {
/*  32 */     this.start = start;
/*  33 */     this.end = start + count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Long> s) {
/*  38 */     if (s instanceof ConditionalSubscriber) {
/*  39 */       s.onSubscribe((Subscription)new RangeConditionalSubscription((ConditionalSubscriber<? super Long>)s, this.start, this.end));
/*     */     } else {
/*     */       
/*  42 */       s.onSubscribe((Subscription)new RangeSubscription(s, this.start, this.end));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseRangeSubscription
/*     */     extends BasicQueueSubscription<Long>
/*     */   {
/*     */     private static final long serialVersionUID = -2252972430506210021L;
/*     */     
/*     */     final long end;
/*     */     long index;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     BaseRangeSubscription(long index, long end) {
/*  57 */       this.index = index;
/*  58 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int requestFusion(int mode) {
/*  63 */       return mode & 0x1;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public final Long poll() {
/*  69 */       long i = this.index;
/*  70 */       if (i == this.end) {
/*  71 */         return null;
/*     */       }
/*  73 */       this.index = i + 1L;
/*  74 */       return Long.valueOf(i);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isEmpty() {
/*  79 */       return (this.index == this.end);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void clear() {
/*  84 */       this.index = this.end;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/*  89 */       if (SubscriptionHelper.validate(n) && 
/*  90 */         BackpressureHelper.add((AtomicLong)this, n) == 0L) {
/*  91 */         if (n == Long.MAX_VALUE) {
/*  92 */           fastPath();
/*     */         } else {
/*  94 */           slowPath(n);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 102 */       this.cancelled = true;
/*     */     }
/*     */     
/*     */     abstract void fastPath();
/*     */     
/*     */     abstract void slowPath(long param1Long);
/*     */   }
/*     */   
/*     */   static final class RangeSubscription
/*     */     extends BaseRangeSubscription
/*     */   {
/*     */     private static final long serialVersionUID = 2587302975077663557L;
/*     */     final Subscriber<? super Long> downstream;
/*     */     
/*     */     RangeSubscription(Subscriber<? super Long> actual, long index, long end) {
/* 117 */       super(index, end);
/* 118 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 123 */       long f = this.end;
/* 124 */       Subscriber<? super Long> a = this.downstream;
/*     */       long i;
/* 126 */       for (i = this.index; i != f; i++) {
/* 127 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 130 */         a.onNext(Long.valueOf(i));
/*     */       } 
/* 132 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 135 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 140 */       long e = 0L;
/* 141 */       long f = this.end;
/* 142 */       long i = this.index;
/* 143 */       Subscriber<? super Long> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 147 */         if (e != r && i != f) {
/* 148 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 152 */           a.onNext(Long.valueOf(i));
/*     */           
/* 154 */           e++;
/* 155 */           i++;
/*     */           continue;
/*     */         } 
/* 158 */         if (i == f) {
/* 159 */           if (!this.cancelled) {
/* 160 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 165 */         r = get();
/* 166 */         if (e == r) {
/* 167 */           this.index = i;
/* 168 */           r = addAndGet(-e);
/* 169 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 172 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class RangeConditionalSubscription
/*     */     extends BaseRangeSubscription
/*     */   {
/*     */     private static final long serialVersionUID = 2587302975077663557L;
/*     */     final ConditionalSubscriber<? super Long> downstream;
/*     */     
/*     */     RangeConditionalSubscription(ConditionalSubscriber<? super Long> actual, long index, long end) {
/* 185 */       super(index, end);
/* 186 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 191 */       long f = this.end;
/* 192 */       ConditionalSubscriber<? super Long> a = this.downstream;
/*     */       long i;
/* 194 */       for (i = this.index; i != f; i++) {
/* 195 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 198 */         a.tryOnNext(Long.valueOf(i));
/*     */       } 
/* 200 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 203 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 208 */       long e = 0L;
/* 209 */       long f = this.end;
/* 210 */       long i = this.index;
/* 211 */       ConditionalSubscriber<? super Long> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 215 */         if (e != r && i != f) {
/* 216 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 220 */           if (a.tryOnNext(Long.valueOf(i))) {
/* 221 */             e++;
/*     */           }
/*     */           
/* 224 */           i++;
/*     */           continue;
/*     */         } 
/* 227 */         if (i == f) {
/* 228 */           if (!this.cancelled) {
/* 229 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 234 */         r = get();
/* 235 */         if (e == r) {
/* 236 */           this.index = i;
/* 237 */           r = addAndGet(-e);
/* 238 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 241 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRangeLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */