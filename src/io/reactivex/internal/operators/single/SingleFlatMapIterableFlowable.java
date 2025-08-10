/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SingleFlatMapIterableFlowable<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMapIterableFlowable(SingleSource<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  45 */     this.source = source;
/*  46 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  51 */     this.source.subscribe(new FlatMapIterableObserver<T, R>(s, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapIterableObserver<T, R>
/*     */     extends BasicIntQueueSubscription<R>
/*     */     implements SingleObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -8938804753851907758L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile Iterator<? extends R> it;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     boolean outputFused;
/*     */     
/*     */     FlatMapIterableObserver(Subscriber<? super R> actual, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  76 */       this.downstream = actual;
/*  77 */       this.mapper = mapper;
/*  78 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  83 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  84 */         this.upstream = d;
/*     */         
/*  86 */         this.downstream.onSubscribe((Subscription)this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*     */       Iterator<? extends R> iterator;
/*     */       boolean has;
/*     */       try {
/*  95 */         iterator = ((Iterable<? extends R>)this.mapper.apply(value)).iterator();
/*     */         
/*  97 */         has = iterator.hasNext();
/*  98 */       } catch (Throwable ex) {
/*  99 */         Exceptions.throwIfFatal(ex);
/* 100 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 104 */       if (!has) {
/* 105 */         this.downstream.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/* 109 */       this.it = iterator;
/* 110 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 115 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 116 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 121 */       if (SubscriptionHelper.validate(n)) {
/* 122 */         BackpressureHelper.add(this.requested, n);
/* 123 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 129 */       this.cancelled = true;
/* 130 */       this.upstream.dispose();
/* 131 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */     
/*     */     void drain() {
/* 135 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 139 */       Subscriber<? super R> a = this.downstream;
/* 140 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 142 */       if (this.outputFused && iterator != null) {
/* 143 */         a.onNext(null);
/* 144 */         a.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/* 148 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 152 */         if (iterator != null) {
/* 153 */           long r = this.requested.get();
/* 154 */           long e = 0L;
/*     */           
/* 156 */           if (r == Long.MAX_VALUE) {
/* 157 */             slowPath(a, iterator);
/*     */             
/*     */             return;
/*     */           } 
/* 161 */           while (e != r) {
/* 162 */             R v; boolean b; if (this.cancelled) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 169 */               v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 170 */             } catch (Throwable ex) {
/* 171 */               Exceptions.throwIfFatal(ex);
/* 172 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 176 */             a.onNext(v);
/*     */             
/* 178 */             if (this.cancelled) {
/*     */               return;
/*     */             }
/*     */             
/* 182 */             e++;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 187 */               b = iterator.hasNext();
/* 188 */             } catch (Throwable ex) {
/* 189 */               Exceptions.throwIfFatal(ex);
/* 190 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 194 */             if (!b) {
/* 195 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 200 */           if (e != 0L) {
/* 201 */             BackpressureHelper.produced(this.requested, e);
/*     */           }
/*     */         } 
/*     */         
/* 205 */         missed = addAndGet(-missed);
/* 206 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */         
/* 210 */         if (iterator == null)
/* 211 */           iterator = this.it; 
/*     */       } 
/*     */     }
/*     */     void slowPath(Subscriber<? super R> a, Iterator<? extends R> iterator) {
/*     */       while (true) {
/*     */         R v;
/*     */         boolean b;
/* 218 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 225 */           v = iterator.next();
/* 226 */         } catch (Throwable ex) {
/* 227 */           Exceptions.throwIfFatal(ex);
/* 228 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 232 */         a.onNext(v);
/*     */         
/* 234 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 241 */           b = iterator.hasNext();
/* 242 */         } catch (Throwable ex) {
/* 243 */           Exceptions.throwIfFatal(ex);
/* 244 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 248 */         if (!b) {
/* 249 */           a.onComplete();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 257 */       if ((mode & 0x2) != 0) {
/* 258 */         this.outputFused = true;
/* 259 */         return 2;
/*     */       } 
/* 261 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 266 */       this.it = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 271 */       return (this.it == null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 277 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 279 */       if (iterator != null) {
/* 280 */         R v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 281 */         if (!iterator.hasNext()) {
/* 282 */           this.it = null;
/*     */         }
/* 284 */         return v;
/*     */       } 
/* 286 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMapIterableFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */