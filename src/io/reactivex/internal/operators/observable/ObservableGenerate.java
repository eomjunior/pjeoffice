/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Emitter;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableGenerate<T, S>
/*     */   extends Observable<T>
/*     */ {
/*     */   final Callable<S> stateSupplier;
/*     */   final BiFunction<S, Emitter<T>, S> generator;
/*     */   final Consumer<? super S> disposeState;
/*     */   
/*     */   public ObservableGenerate(Callable<S> stateSupplier, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
/*  32 */     this.stateSupplier = stateSupplier;
/*  33 */     this.generator = generator;
/*  34 */     this.disposeState = disposeState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super T> observer) {
/*     */     S state;
/*     */     try {
/*  42 */       state = this.stateSupplier.call();
/*  43 */     } catch (Throwable e) {
/*  44 */       Exceptions.throwIfFatal(e);
/*  45 */       EmptyDisposable.error(e, observer);
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     GeneratorDisposable<T, S> gd = new GeneratorDisposable<T, S>(observer, this.generator, this.disposeState, state);
/*  50 */     observer.onSubscribe(gd);
/*  51 */     gd.run();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class GeneratorDisposable<T, S>
/*     */     implements Emitter<T>, Disposable
/*     */   {
/*     */     final Observer<? super T> downstream;
/*     */     
/*     */     final BiFunction<S, ? super Emitter<T>, S> generator;
/*     */     
/*     */     final Consumer<? super S> disposeState;
/*     */     
/*     */     S state;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     boolean terminate;
/*     */     boolean hasNext;
/*     */     
/*     */     GeneratorDisposable(Observer<? super T> actual, BiFunction<S, ? super Emitter<T>, S> generator, Consumer<? super S> disposeState, S initialState) {
/*  72 */       this.downstream = actual;
/*  73 */       this.generator = generator;
/*  74 */       this.disposeState = disposeState;
/*  75 */       this.state = initialState;
/*     */     }
/*     */     
/*     */     public void run() {
/*  79 */       S s = this.state;
/*     */       
/*  81 */       if (this.cancelled) {
/*  82 */         this.state = null;
/*  83 */         dispose(s);
/*     */         
/*     */         return;
/*     */       } 
/*  87 */       BiFunction<S, ? super Emitter<T>, S> f = this.generator;
/*     */ 
/*     */       
/*     */       while (true) {
/*  91 */         if (this.cancelled) {
/*  92 */           this.state = null;
/*  93 */           dispose(s);
/*     */           
/*     */           return;
/*     */         } 
/*  97 */         this.hasNext = false;
/*     */         
/*     */         try {
/* 100 */           s = (S)f.apply(s, this);
/* 101 */         } catch (Throwable ex) {
/* 102 */           Exceptions.throwIfFatal(ex);
/* 103 */           this.state = null;
/* 104 */           this.cancelled = true;
/* 105 */           onError(ex);
/* 106 */           dispose(s);
/*     */           
/*     */           return;
/*     */         } 
/* 110 */         if (this.terminate) {
/* 111 */           this.cancelled = true;
/* 112 */           this.state = null;
/* 113 */           dispose(s);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void dispose(S s) {
/*     */       try {
/* 122 */         this.disposeState.accept(s);
/* 123 */       } catch (Throwable ex) {
/* 124 */         Exceptions.throwIfFatal(ex);
/* 125 */         RxJavaPlugins.onError(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 131 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 136 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 141 */       if (!this.terminate) {
/* 142 */         if (this.hasNext) {
/* 143 */           onError(new IllegalStateException("onNext already called in this generate turn"));
/*     */         }
/* 145 */         else if (t == null) {
/* 146 */           onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         } else {
/* 148 */           this.hasNext = true;
/* 149 */           this.downstream.onNext(t);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 157 */       if (this.terminate) {
/* 158 */         RxJavaPlugins.onError(t);
/*     */       } else {
/* 160 */         if (t == null) {
/* 161 */           t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */         }
/* 163 */         this.terminate = true;
/* 164 */         this.downstream.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 170 */       if (!this.terminate) {
/* 171 */         this.terminate = true;
/* 172 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableGenerate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */