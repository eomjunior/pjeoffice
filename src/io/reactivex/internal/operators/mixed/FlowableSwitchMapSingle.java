/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ 
/*     */ 
/*     */ public final class FlowableSwitchMapSingle<T, R>
/*     */   extends Flowable<R>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableSwitchMapSingle(Flowable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
/*  50 */     this.source = source;
/*  51 */     this.mapper = mapper;
/*  52 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super R> s) {
/*  57 */     this.source.subscribe(new SwitchMapSingleSubscriber<T, R>(s, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapSingleSubscriber<T, R>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -5402190102429853762L;
/*     */     
/*     */     final Subscriber<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     
/*     */     final AtomicLong requested;
/*     */     final AtomicReference<SwitchMapSingleObserver<R>> inner;
/*  77 */     static final SwitchMapSingleObserver<Object> INNER_DISPOSED = new SwitchMapSingleObserver(null);
/*     */ 
/*     */     
/*     */     Subscription upstream;
/*     */ 
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     long emitted;
/*     */ 
/*     */     
/*     */     SwitchMapSingleSubscriber(Subscriber<? super R> downstream, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
/*  91 */       this.downstream = downstream;
/*  92 */       this.mapper = mapper;
/*  93 */       this.delayErrors = delayErrors;
/*  94 */       this.errors = new AtomicThrowable();
/*  95 */       this.requested = new AtomicLong();
/*  96 */       this.inner = new AtomicReference<SwitchMapSingleObserver<R>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 101 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 102 */         this.upstream = s;
/* 103 */         this.downstream.onSubscribe(this);
/* 104 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       SingleSource<? extends R> ss;
/* 111 */       SwitchMapSingleObserver<R> current = this.inner.get();
/* 112 */       if (current != null) {
/* 113 */         current.dispose();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 119 */         ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
/* 120 */       } catch (Throwable ex) {
/* 121 */         Exceptions.throwIfFatal(ex);
/* 122 */         this.upstream.cancel();
/* 123 */         this.inner.getAndSet(INNER_DISPOSED);
/* 124 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       SwitchMapSingleObserver<R> observer = new SwitchMapSingleObserver<R>(this);
/*     */       
/*     */       while (true) {
/* 131 */         current = this.inner.get();
/* 132 */         if (current == INNER_DISPOSED) {
/*     */           break;
/*     */         }
/* 135 */         if (this.inner.compareAndSet(current, observer)) {
/* 136 */           ss.subscribe(observer);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 144 */       if (this.errors.addThrowable(t)) {
/* 145 */         if (!this.delayErrors) {
/* 146 */           disposeInner();
/*     */         }
/* 148 */         this.done = true;
/* 149 */         drain();
/*     */       } else {
/* 151 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 157 */       this.done = true;
/* 158 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeInner() {
/* 163 */       SwitchMapSingleObserver<R> current = (SwitchMapSingleObserver<R>)this.inner.getAndSet(INNER_DISPOSED);
/* 164 */       if (current != null && current != INNER_DISPOSED) {
/* 165 */         current.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 171 */       BackpressureHelper.add(this.requested, n);
/* 172 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 177 */       this.cancelled = true;
/* 178 */       this.upstream.cancel();
/* 179 */       disposeInner();
/*     */     }
/*     */     
/*     */     void innerError(SwitchMapSingleObserver<R> sender, Throwable ex) {
/* 183 */       if (this.inner.compareAndSet(sender, null) && 
/* 184 */         this.errors.addThrowable(ex)) {
/* 185 */         if (!this.delayErrors) {
/* 186 */           this.upstream.cancel();
/* 187 */           disposeInner();
/*     */         } 
/* 189 */         drain();
/*     */         
/*     */         return;
/*     */       } 
/* 193 */       RxJavaPlugins.onError(ex);
/*     */     }
/*     */     
/*     */     void drain() {
/* 197 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 201 */       int missed = 1;
/* 202 */       Subscriber<? super R> downstream = this.downstream;
/* 203 */       AtomicThrowable errors = this.errors;
/* 204 */       AtomicReference<SwitchMapSingleObserver<R>> inner = this.inner;
/* 205 */       AtomicLong requested = this.requested;
/* 206 */       long emitted = this.emitted;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 211 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 215 */         if (errors.get() != null && 
/* 216 */           !this.delayErrors) {
/* 217 */           Throwable ex = errors.terminate();
/* 218 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 223 */         boolean d = this.done;
/* 224 */         SwitchMapSingleObserver<R> current = inner.get();
/* 225 */         boolean empty = (current == null);
/*     */         
/* 227 */         if (d && empty) {
/* 228 */           Throwable ex = errors.terminate();
/* 229 */           if (ex != null) {
/* 230 */             downstream.onError(ex);
/*     */           } else {
/* 232 */             downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 237 */         if (empty || current.item == null || emitted == requested.get()) {
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
/* 248 */           this.emitted = emitted;
/* 249 */           missed = addAndGet(-missed);
/* 250 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         inner.compareAndSet(current, null);
/*     */         downstream.onNext(current.item);
/*     */         emitted++;
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class SwitchMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
/*     */       private static final long serialVersionUID = 8042919737683345351L;
/*     */       final FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> parent;
/*     */       volatile R item;
/*     */       
/*     */       SwitchMapSingleObserver(FlowableSwitchMapSingle.SwitchMapSingleSubscriber<?, R> parent) {
/* 266 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 271 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 276 */         this.item = t;
/* 277 */         this.parent.drain();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 282 */         this.parent.innerError(this, e);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 286 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/FlowableSwitchMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */