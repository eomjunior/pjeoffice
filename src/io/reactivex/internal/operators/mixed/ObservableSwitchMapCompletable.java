/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class ObservableSwitchMapCompletable<T>
/*     */   extends Completable
/*     */ {
/*     */   final Observable<T> source;
/*     */   final Function<? super T, ? extends CompletableSource> mapper;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ObservableSwitchMapCompletable(Observable<T> source, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  45 */     this.source = source;
/*  46 */     this.mapper = mapper;
/*  47 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  52 */     if (!ScalarXMapZHelper.tryAsCompletable(this.source, this.mapper, observer)) {
/*  53 */       this.source.subscribe(new SwitchMapCompletableObserver<T>(observer, this.mapper, this.delayErrors));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchMapCompletableObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     final Function<? super T, ? extends CompletableSource> mapper;
/*     */     
/*     */     final boolean delayErrors;
/*     */     
/*     */     final AtomicThrowable errors;
/*     */     final AtomicReference<SwitchMapInnerObserver> inner;
/*  69 */     static final SwitchMapInnerObserver INNER_DISPOSED = new SwitchMapInnerObserver(null);
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Disposable upstream;
/*     */ 
/*     */     
/*     */     SwitchMapCompletableObserver(CompletableObserver downstream, Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors) {
/*  77 */       this.downstream = downstream;
/*  78 */       this.mapper = mapper;
/*  79 */       this.delayErrors = delayErrors;
/*  80 */       this.errors = new AtomicThrowable();
/*  81 */       this.inner = new AtomicReference<SwitchMapInnerObserver>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  86 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  87 */         this.upstream = d;
/*  88 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       CompletableSource c;
/*     */       try {
/*  97 */         c = (CompletableSource)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null CompletableSource");
/*  98 */       } catch (Throwable ex) {
/*  99 */         Exceptions.throwIfFatal(ex);
/* 100 */         this.upstream.dispose();
/* 101 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 105 */       SwitchMapInnerObserver o = new SwitchMapInnerObserver(this);
/*     */       
/*     */       while (true) {
/* 108 */         SwitchMapInnerObserver current = this.inner.get();
/* 109 */         if (current == INNER_DISPOSED) {
/*     */           break;
/*     */         }
/* 112 */         if (this.inner.compareAndSet(current, o)) {
/* 113 */           if (current != null) {
/* 114 */             current.dispose();
/*     */           }
/* 116 */           c.subscribe(o);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 124 */       if (this.errors.addThrowable(t)) {
/* 125 */         if (this.delayErrors) {
/* 126 */           onComplete();
/*     */         } else {
/* 128 */           disposeInner();
/* 129 */           Throwable ex = this.errors.terminate();
/* 130 */           if (ex != ExceptionHelper.TERMINATED) {
/* 131 */             this.downstream.onError(ex);
/*     */           }
/*     */         } 
/*     */       } else {
/* 135 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 141 */       this.done = true;
/* 142 */       if (this.inner.get() == null) {
/* 143 */         Throwable ex = this.errors.terminate();
/* 144 */         if (ex == null) {
/* 145 */           this.downstream.onComplete();
/*     */         } else {
/* 147 */           this.downstream.onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void disposeInner() {
/* 153 */       SwitchMapInnerObserver o = this.inner.getAndSet(INNER_DISPOSED);
/* 154 */       if (o != null && o != INNER_DISPOSED) {
/* 155 */         o.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 161 */       this.upstream.dispose();
/* 162 */       disposeInner();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 167 */       return (this.inner.get() == INNER_DISPOSED);
/*     */     }
/*     */     
/*     */     void innerError(SwitchMapInnerObserver sender, Throwable error) {
/* 171 */       if (this.inner.compareAndSet(sender, null) && 
/* 172 */         this.errors.addThrowable(error)) {
/* 173 */         if (this.delayErrors) {
/* 174 */           if (this.done) {
/* 175 */             Throwable ex = this.errors.terminate();
/* 176 */             this.downstream.onError(ex);
/*     */           } 
/*     */         } else {
/* 179 */           dispose();
/* 180 */           Throwable ex = this.errors.terminate();
/* 181 */           if (ex != ExceptionHelper.TERMINATED) {
/* 182 */             this.downstream.onError(ex);
/*     */           }
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 188 */       RxJavaPlugins.onError(error);
/*     */     }
/*     */     
/*     */     void innerComplete(SwitchMapInnerObserver sender) {
/* 192 */       if (this.inner.compareAndSet(sender, null) && 
/* 193 */         this.done) {
/* 194 */         Throwable ex = this.errors.terminate();
/* 195 */         if (ex == null) {
/* 196 */           this.downstream.onComplete();
/*     */         } else {
/* 198 */           this.downstream.onError(ex);
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
/*     */       final ObservableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent;
/*     */       
/*     */       SwitchMapInnerObserver(ObservableSwitchMapCompletable.SwitchMapCompletableObserver<?> parent) {
/* 212 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 217 */         DisposableHelper.setOnce(this, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 222 */         this.parent.innerError(this, e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 227 */         this.parent.innerComplete(this);
/*     */       }
/*     */       
/*     */       void dispose() {
/* 231 */         DisposableHelper.dispose(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ObservableSwitchMapCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */