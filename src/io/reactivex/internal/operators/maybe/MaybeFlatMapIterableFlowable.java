/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ public final class MaybeFlatMapIterableFlowable<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatMapIterableFlowable(MaybeSource<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper) {
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
/*     */     implements MaybeObserver<T>
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
/*     */     public void onComplete() {
/* 121 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 126 */       if (SubscriptionHelper.validate(n)) {
/* 127 */         BackpressureHelper.add(this.requested, n);
/* 128 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 134 */       this.cancelled = true;
/* 135 */       this.upstream.dispose();
/* 136 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     } void fastPath(Subscriber<? super R> a, Iterator<? extends R> iterator) {
/*     */       while (true) {
/*     */         R v;
/*     */         boolean b;
/* 141 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 148 */           v = iterator.next();
/* 149 */         } catch (Throwable ex) {
/* 150 */           Exceptions.throwIfFatal(ex);
/* 151 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 155 */         a.onNext(v);
/*     */         
/* 157 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 164 */           b = iterator.hasNext();
/* 165 */         } catch (Throwable ex) {
/* 166 */           Exceptions.throwIfFatal(ex);
/* 167 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 171 */         if (!b) {
/* 172 */           a.onComplete();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 179 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 183 */       Subscriber<? super R> a = this.downstream;
/* 184 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 186 */       if (this.outputFused && iterator != null) {
/* 187 */         a.onNext(null);
/* 188 */         a.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/* 192 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 196 */         if (iterator != null) {
/* 197 */           long r = this.requested.get();
/*     */           
/* 199 */           if (r == Long.MAX_VALUE) {
/* 200 */             fastPath(a, iterator);
/*     */             
/*     */             return;
/*     */           } 
/* 204 */           long e = 0L;
/*     */           
/* 206 */           while (e != r) {
/* 207 */             R v; boolean b; if (this.cancelled) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 214 */               v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 215 */             } catch (Throwable ex) {
/* 216 */               Exceptions.throwIfFatal(ex);
/* 217 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 221 */             a.onNext(v);
/*     */             
/* 223 */             if (this.cancelled) {
/*     */               return;
/*     */             }
/*     */             
/* 227 */             e++;
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 232 */               b = iterator.hasNext();
/* 233 */             } catch (Throwable ex) {
/* 234 */               Exceptions.throwIfFatal(ex);
/* 235 */               a.onError(ex);
/*     */               
/*     */               return;
/*     */             } 
/* 239 */             if (!b) {
/* 240 */               a.onComplete();
/*     */               
/*     */               return;
/*     */             } 
/*     */           } 
/* 245 */           if (e != 0L) {
/* 246 */             BackpressureHelper.produced(this.requested, e);
/*     */           }
/*     */         } 
/*     */         
/* 250 */         missed = addAndGet(-missed);
/* 251 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */         
/* 255 */         if (iterator == null) {
/* 256 */           iterator = this.it;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 263 */       if ((mode & 0x2) != 0) {
/* 264 */         this.outputFused = true;
/* 265 */         return 2;
/*     */       } 
/* 267 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 272 */       this.it = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 277 */       return (this.it == null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 283 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 285 */       if (iterator != null) {
/* 286 */         R v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 287 */         if (!iterator.hasNext()) {
/* 288 */           this.it = null;
/*     */         }
/* 290 */         return v;
/*     */       } 
/* 292 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapIterableFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */