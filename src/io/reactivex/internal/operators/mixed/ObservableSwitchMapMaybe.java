/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
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
/*     */ public final class ObservableSwitchMapMaybe<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableSwitchMapMaybe(Observable<T> source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors) {
/*  47 */     this.source = source;
/*  48 */     this.mapper = mapper;
/*  49 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  54 */     if (!ScalarXMapZHelper.tryAsMaybe(this.source, this.mapper, observer)) {
/*  55 */       this.source.subscribe(new SwitchMapMaybeMainObserver<T, R>(observer, this.mapper, this.delayErrors));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapMaybeMainObserver<T, R>
/*     */     extends AtomicInteger
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -5402190102429853762L;
/*     */     
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends MaybeSource<? extends R>> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     final AtomicReference<SwitchMapMaybeObserver<R>> inner;
/*  74 */     static final SwitchMapMaybeObserver<Object> INNER_DISPOSED = new SwitchMapMaybeObserver(null);
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
/*     */     SwitchMapMaybeMainObserver(Observer<? super R> downstream, Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors) {
/*  86 */       this.downstream = downstream;
/*  87 */       this.mapper = mapper;
/*  88 */       this.delayErrors = delayErrors;
/*  89 */       this.errors = new AtomicThrowable();
/*  90 */       this.inner = new AtomicReference<SwitchMapMaybeObserver<R>>();
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
/*     */       MaybeSource<? extends R> ms;
/* 104 */       SwitchMapMaybeObserver<R> current = this.inner.get();
/* 105 */       if (current != null) {
/* 106 */         current.dispose();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 112 */         ms = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null MaybeSource");
/* 113 */       } catch (Throwable ex) {
/* 114 */         Exceptions.throwIfFatal(ex);
/* 115 */         this.upstream.dispose();
/* 116 */         this.inner.getAndSet(INNER_DISPOSED);
/* 117 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 121 */       SwitchMapMaybeObserver<R> observer = new SwitchMapMaybeObserver<R>(this);
/*     */       
/*     */       while (true) {
/* 124 */         current = this.inner.get();
/* 125 */         if (current == INNER_DISPOSED) {
/*     */           break;
/*     */         }
/* 128 */         if (this.inner.compareAndSet(current, observer)) {
/* 129 */           ms.subscribe(observer);
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
/* 156 */       SwitchMapMaybeObserver<R> current = (SwitchMapMaybeObserver<R>)this.inner.getAndSet(INNER_DISPOSED);
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
/*     */     void innerError(SwitchMapMaybeObserver<R> sender, Throwable ex) {
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
/*     */     void innerComplete(SwitchMapMaybeObserver<R> sender) {
/* 189 */       if (this.inner.compareAndSet(sender, null)) {
/* 190 */         drain();
/*     */       }
/*     */     }
/*     */     
/*     */     void drain() {
/* 195 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 199 */       int missed = 1;
/* 200 */       Observer<? super R> downstream = this.downstream;
/* 201 */       AtomicThrowable errors = this.errors;
/* 202 */       AtomicReference<SwitchMapMaybeObserver<R>> inner = this.inner;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 207 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 211 */         if (errors.get() != null && 
/* 212 */           !this.delayErrors) {
/* 213 */           Throwable ex = errors.terminate();
/* 214 */           downstream.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 219 */         boolean d = this.done;
/* 220 */         SwitchMapMaybeObserver<R> current = inner.get();
/* 221 */         boolean empty = (current == null);
/*     */         
/* 223 */         if (d && empty) {
/* 224 */           Throwable ex = errors.terminate();
/* 225 */           if (ex != null) {
/* 226 */             downstream.onError(ex);
/*     */           } else {
/* 228 */             downstream.onComplete();
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 233 */         if (empty || current.item == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 242 */           missed = addAndGet(-missed);
/* 243 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         inner.compareAndSet(current, null);
/*     */         downstream.onNext(current.item);
/*     */       } 
/*     */     }
/*     */     
/*     */     static final class SwitchMapMaybeObserver<R>
/*     */       extends AtomicReference<Disposable> implements MaybeObserver<R> {
/*     */       private static final long serialVersionUID = 8042919737683345351L;
/*     */       final ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> parent;
/*     */       volatile R item;
/*     */       
/*     */       SwitchMapMaybeObserver(ObservableSwitchMapMaybe.SwitchMapMaybeMainObserver<?, R> parent) {
/* 259 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 264 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(R t) {
/* 269 */         this.item = t;
/* 270 */         this.parent.drain();
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 275 */         this.parent.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 280 */         this.parent.innerComplete(this);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 284 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableSwitchMapMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */