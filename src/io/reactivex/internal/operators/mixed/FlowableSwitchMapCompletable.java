/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableSwitchMapCompletable<T>
/*     */   extends Completable
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public FlowableSwitchMapCompletable(Flowable<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  48 */     this.source = source;
/*  49 */     this.mapper = mapper;
/*  50 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  55 */     this.source.subscribe(new SwitchMapCompletableObserver<T>(observer, this.mapper, this.delayErrors));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapCompletableObserver<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     final AtomicReference<SwitchMapInnerObserver> inner;
/*  70 */     static final SwitchMapInnerObserver INNER_DISPOSED = new SwitchMapInnerObserver(null);
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Subscription upstream;
/*     */ 
/*     */     
/*     */     SwitchMapCompletableObserver(CompletableObserver downstream, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  78 */       this.downstream = downstream;
/*  79 */       this.mapper = mapper;
/*  80 */       this.delayErrors = delayErrors;
/*  81 */       this.errors = new AtomicThrowable();
/*  82 */       this.inner = new AtomicReference<SwitchMapInnerObserver>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  87 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  88 */         this.upstream = s;
/*  89 */         this.downstream.onSubscribe(this);
/*  90 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       CompletableSource c;
/*     */       try {
/*  99 */         c = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null CompletableSource");
/* 100 */       } catch (Throwable ex) {
/* 101 */         Exceptions.throwIfFatal(ex);
/* 102 */         this.upstream.cancel();
/* 103 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 107 */       SwitchMapInnerObserver o = new SwitchMapInnerObserver(this);
/*     */       
/*     */       while (true) {
/* 110 */         SwitchMapInnerObserver current = this.inner.get();
/* 111 */         if (current == INNER_DISPOSED) {
/*     */           break;
/*     */         }
/* 114 */         if (this.inner.compareAndSet(current, o)) {
/* 115 */           if (current != null) {
/* 116 */             current.dispose();
/*     */           }
/* 118 */           c.subscribe(o);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 126 */       if (this.errors.addThrowable(t)) {
/* 127 */         if (this.delayErrors) {
/* 128 */           onComplete();
/*     */         } else {
/* 130 */           disposeInner();
/* 131 */           Throwable ex = this.errors.terminate();
/* 132 */           if (ex != ExceptionHelper.TERMINATED) {
/* 133 */             this.downstream.onError(ex);
/*     */           }
/*     */         } 
/*     */       } else {
/* 137 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 143 */       this.done = true;
/* 144 */       if (this.inner.get() == null) {
/* 145 */         Throwable ex = this.errors.terminate();
/* 146 */         if (ex == null) {
/* 147 */           this.downstream.onComplete();
/*     */         } else {
/* 149 */           this.downstream.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void disposeInner() {
/* 155 */       SwitchMapInnerObserver o = this.inner.getAndSet(INNER_DISPOSED);
/* 156 */       if (o != null && o != INNER_DISPOSED) {
/* 157 */         o.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 163 */       this.upstream.cancel();
/* 164 */       disposeInner();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 169 */       return (this.inner.get() == INNER_DISPOSED);
/*     */     }
/*     */     
/*     */     void innerError(SwitchMapInnerObserver sender, Throwable error) {
/* 173 */       if (this.inner.compareAndSet(sender, null) && 
/* 174 */         this.errors.addThrowable(error)) {
/* 175 */         if (this.delayErrors) {
/* 176 */           if (this.done) {
/* 177 */             Throwable ex = this.errors.terminate();
/* 178 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } else {
/* 181 */           dispose();
/* 182 */           Throwable ex = this.errors.terminate();
/* 183 */           if (ex != ExceptionHelper.TERMINATED) {
/* 184 */             this.downstream.onError(ex);
/*     */           }
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 190 */       RxJavaPlugins.onError(error);
/*     */     }
/*     */     
/*     */     void innerComplete(SwitchMapInnerObserver sender) {
/* 194 */       if (this.inner.compareAndSet(sender, null) && 
/* 195 */         this.done) {
/* 196 */         Throwable ex = this.errors.terminate();
/* 197 */         if (ex == null) {
/* 198 */           this.downstream.onComplete();
/*     */         } else {
/* 200 */           this.downstream.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     static final class SwitchMapInnerObserver
/*     */       extends AtomicReference<Disposable>
/*     */       implements CompletableObserver
/*     */     {
/*     */       private static final long serialVersionUID = -8003404460084760287L;
/*     */       final FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent;
/*     */       
/*     */       SwitchMapInnerObserver(FlowableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent) {
/* 214 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 219 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 224 */         this.parent.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 229 */         this.parent.innerComplete(this);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 233 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/FlowableSwitchMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */