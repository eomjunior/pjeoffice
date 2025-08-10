/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class ObservableSwitchMapSingle<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableSwitchMapSingle(Observable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
/*  47 */     this.source = source;
/*  48 */     this.mapper = mapper;
/*  49 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  54 */     if (!ScalarXMapZHelper.tryAsSingle(this.source, this.mapper, observer)) {
/*  55 */       this.source.subscribe(new SwitchMapSingleMainObserver<T, R>(observer, this.mapper, this.delayErrors));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapSingleMainObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5402190102429853762L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends SingleSource<? extends R>> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     final AtomicReference<SwitchMapSingleObserver<R>> inner;
/*  74 */     static final SwitchMapSingleObserver<Object> INNER_DISPOSED = new SwitchMapSingleObserver(null);
/*     */ 
/*     */     
/*     */     Disposable upstream;
/*     */ 
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     volatile boolean cancelled;
/*     */ 
/*     */     
/*     */     SwitchMapSingleMainObserver(Observer<? super R> downstream, Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors) {
/*  86 */       this.downstream = downstream;
/*  87 */       this.mapper = mapper;
/*  88 */       this.delayErrors = delayErrors;
/*  89 */       this.errors = new AtomicThrowable();
/*  90 */       this.inner = new AtomicReference<SwitchMapSingleObserver<R>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  95 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  96 */         this.upstream = d;
/*  97 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       SingleSource<? extends R> ss;
/* 104 */       SwitchMapSingleObserver<R> current = this.inner.get();
/* 105 */       if (current != null) {
/* 106 */         current.dispose();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 112 */         ss = (SingleSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
/* 113 */       } catch (Throwable ex) {
/* 114 */         Exceptions.throwIfFatal(ex);
/* 115 */         this.upstream.dispose();
/* 116 */         this.inner.getAndSet(INNER_DISPOSED);
/* 117 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 121 */       SwitchMapSingleObserver<R> observer = new SwitchMapSingleObserver<R>(this);
/*     */       
/*     */       while (true) {
/* 124 */         current = this.inner.get();
/* 125 */         if (current == INNER_DISPOSED) {
/*     */           break;
/*     */         }
/* 128 */         if (this.inner.compareAndSet(current, observer)) {
/* 129 */           ss.subscribe(observer);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 137 */       if (this.errors.addThrowable(t)) {
/* 138 */         if (!this.delayErrors) {
/* 139 */           disposeInner();
/*     */         }
/* 141 */         this.done = true;
/* 142 */         drain();
/*     */       } else {
/* 144 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 150 */       this.done = true;
/* 151 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void disposeInner() {
/* 156 */       SwitchMapSingleObserver<R> current = (SwitchMapSingleObserver<R>)this.inner.getAndSet(INNER_DISPOSED);
/* 157 */       if (current != null && current != INNER_DISPOSED) {
/* 158 */         current.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 164 */       this.cancelled = true;
/* 165 */       this.upstream.dispose();
/* 166 */       disposeInner();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 171 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void innerError(SwitchMapSingleObserver<R> sender, Throwable ex) {
/* 175 */       if (this.inner.compareAndSet(sender, null) && 
/* 176 */         this.errors.addThrowable(ex)) {
/* 177 */         if (!this.delayErrors) {
/* 178 */           this.upstream.dispose();
/* 179 */           disposeInner();
/*     */         } 
/* 181 */         drain();
/*     */         
/*     */         return;
/*     */       } 
/* 185 */       RxJavaPlugins.onError(ex);
/*     */     }
/*     */     
/*     */     void drain() {
/* 189 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 193 */       int missed = 1;
/* 194 */       Observer<? super R> downstream = this.downstream;
/* 195 */       AtomicThrowable errors = this.errors;
/* 196 */       AtomicReference<SwitchMapSingleObserver<R>> inner = this.inner;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 201 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 205 */         if (errors.get() != null && 
/* 206 */           !this.delayErrors) {
/* 207 */           Throwable ex = errors.terminate();
/* 208 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 213 */         boolean d = this.done;
/* 214 */         SwitchMapSingleObserver<R> current = inner.get();
/* 215 */         boolean empty = (current == null);
/*     */         
/* 217 */         if (d && empty) {
/* 218 */           Throwable ex = errors.terminate();
/* 219 */           if (ex != null) {
/* 220 */             downstream.onError(ex);
/*     */           } else {
/* 222 */             downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 227 */         if (empty || current.item == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 236 */           missed = addAndGet(-missed);
/* 237 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         inner.compareAndSet(current, null);
/*     */         downstream.onNext(current.item);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class SwitchMapSingleObserver<R>
/*     */       extends AtomicReference<Disposable> implements SingleObserver<R> {
/*     */       private static final long serialVersionUID = 8042919737683345351L;
/*     */       final ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> parent;
/*     */       volatile R item;
/*     */       
/*     */       SwitchMapSingleObserver(ObservableSwitchMapSingle.SwitchMapSingleMainObserver<?, R> parent) {
/* 253 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 258 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 263 */         this.item = t;
/* 264 */         this.parent.drain();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 269 */         this.parent.innerError(this, e);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 273 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableSwitchMapSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */