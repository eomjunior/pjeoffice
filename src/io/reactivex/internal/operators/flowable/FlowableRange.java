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
/*     */ public final class FlowableRange
/*     */   extends Flowable<Integer>
/*     */ {
/*     */   final int start;
/*     */   final int end;
/*     */   
/*     */   public FlowableRange(int start, int count) {
/*  31 */     this.start = start;
/*  32 */     this.end = start + count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Integer> s) {
/*  37 */     if (s instanceof ConditionalSubscriber) {
/*  38 */       s.onSubscribe((Subscription)new RangeConditionalSubscription((ConditionalSubscriber<? super Integer>)s, this.start, this.end));
/*     */     } else {
/*     */       
/*  41 */       s.onSubscribe((Subscription)new RangeSubscription(s, this.start, this.end));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseRangeSubscription
/*     */     extends BasicQueueSubscription<Integer>
/*     */   {
/*     */     private static final long serialVersionUID = -2252972430506210021L;
/*     */     final int end;
/*     */     int index;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     BaseRangeSubscription(int index, int end) {
/*  55 */       this.index = index;
/*  56 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int requestFusion(int mode) {
/*  61 */       return mode & 0x1;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public final Integer poll() {
/*  67 */       int i = this.index;
/*  68 */       if (i == this.end) {
/*  69 */         return null;
/*     */       }
/*  71 */       this.index = i + 1;
/*  72 */       return Integer.valueOf(i);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isEmpty() {
/*  77 */       return (this.index == this.end);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void clear() {
/*  82 */       this.index = this.end;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/*  87 */       if (SubscriptionHelper.validate(n) && 
/*  88 */         BackpressureHelper.add((AtomicLong)this, n) == 0L) {
/*  89 */         if (n == Long.MAX_VALUE) {
/*  90 */           fastPath();
/*     */         } else {
/*  92 */           slowPath(n);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 100 */       this.cancelled = true;
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
/*     */     final Subscriber<? super Integer> downstream;
/*     */     
/*     */     RangeSubscription(Subscriber<? super Integer> actual, int index, int end) {
/* 115 */       super(index, end);
/* 116 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 121 */       int f = this.end;
/* 122 */       Subscriber<? super Integer> a = this.downstream;
/*     */       
/* 124 */       for (int i = this.index; i != f; i++) {
/* 125 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 128 */         a.onNext(Integer.valueOf(i));
/*     */       } 
/* 130 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 133 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 138 */       long e = 0L;
/* 139 */       int f = this.end;
/* 140 */       int i = this.index;
/* 141 */       Subscriber<? super Integer> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 145 */         if (e != r && i != f) {
/* 146 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 150 */           a.onNext(Integer.valueOf(i));
/*     */           
/* 152 */           e++;
/* 153 */           i++;
/*     */           continue;
/*     */         } 
/* 156 */         if (i == f) {
/* 157 */           if (!this.cancelled) {
/* 158 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 163 */         r = get();
/* 164 */         if (e == r) {
/* 165 */           this.index = i;
/* 166 */           r = addAndGet(-e);
/* 167 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 170 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class RangeConditionalSubscription
/*     */     extends BaseRangeSubscription
/*     */   {
/*     */     private static final long serialVersionUID = 2587302975077663557L;
/*     */     final ConditionalSubscriber<? super Integer> downstream;
/*     */     
/*     */     RangeConditionalSubscription(ConditionalSubscriber<? super Integer> actual, int index, int end) {
/* 183 */       super(index, end);
/* 184 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     void fastPath() {
/* 189 */       int f = this.end;
/* 190 */       ConditionalSubscriber<? super Integer> a = this.downstream;
/*     */       
/* 192 */       for (int i = this.index; i != f; i++) {
/* 193 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 196 */         a.tryOnNext(Integer.valueOf(i));
/*     */       } 
/* 198 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/* 201 */       a.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     void slowPath(long r) {
/* 206 */       long e = 0L;
/* 207 */       int f = this.end;
/* 208 */       int i = this.index;
/* 209 */       ConditionalSubscriber<? super Integer> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 213 */         if (e != r && i != f) {
/* 214 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */           
/* 218 */           if (a.tryOnNext(Integer.valueOf(i))) {
/* 219 */             e++;
/*     */           }
/*     */           
/* 222 */           i++;
/*     */           continue;
/*     */         } 
/* 225 */         if (i == f) {
/* 226 */           if (!this.cancelled) {
/* 227 */             a.onComplete();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 232 */         r = get();
/* 233 */         if (e == r) {
/* 234 */           this.index = i;
/* 235 */           r = addAndGet(-e);
/* 236 */           if (r == 0L) {
/*     */             return;
/*     */           }
/* 239 */           e = 0L;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */