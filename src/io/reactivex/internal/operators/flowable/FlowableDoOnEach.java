/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
/*     */ import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class FlowableDoOnEach<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Consumer<? super T> onNext;
/*     */   final Consumer<? super Throwable> onError;
/*     */   final Action onComplete;
/*     */   final Action onAfterTerminate;
/*     */   
/*     */   public FlowableDoOnEach(Flowable<T> source, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  37 */     super(source);
/*  38 */     this.onNext = onNext;
/*  39 */     this.onError = onError;
/*  40 */     this.onComplete = onComplete;
/*  41 */     this.onAfterTerminate = onAfterTerminate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  46 */     if (s instanceof ConditionalSubscriber) {
/*  47 */       this.source.subscribe((FlowableSubscriber)new DoOnEachConditionalSubscriber<T>((ConditionalSubscriber<? super T>)s, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
/*     */     } else {
/*     */       
/*  50 */       this.source.subscribe((FlowableSubscriber)new DoOnEachSubscriber<T>(s, this.onNext, this.onError, this.onComplete, this.onAfterTerminate));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoOnEachSubscriber<T>
/*     */     extends BasicFuseableSubscriber<T, T>
/*     */   {
/*     */     final Consumer<? super T> onNext;
/*     */     
/*     */     final Consumer<? super Throwable> onError;
/*     */     
/*     */     final Action onComplete;
/*     */     
/*     */     final Action onAfterTerminate;
/*     */     
/*     */     DoOnEachSubscriber(Subscriber<? super T> actual, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/*  67 */       super(actual);
/*  68 */       this.onNext = onNext;
/*  69 */       this.onError = onError;
/*  70 */       this.onComplete = onComplete;
/*  71 */       this.onAfterTerminate = onAfterTerminate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  76 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  80 */       if (this.sourceMode != 0) {
/*  81 */         this.downstream.onNext(null);
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/*  86 */         this.onNext.accept(t);
/*  87 */       } catch (Throwable e) {
/*  88 */         fail(e);
/*     */         
/*     */         return;
/*     */       } 
/*  92 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  97 */       if (this.done) {
/*  98 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 101 */       this.done = true;
/* 102 */       boolean relay = true;
/*     */       try {
/* 104 */         this.onError.accept(t);
/* 105 */       } catch (Throwable e) {
/* 106 */         Exceptions.throwIfFatal(e);
/* 107 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/* 108 */         relay = false;
/*     */       } 
/* 110 */       if (relay) {
/* 111 */         this.downstream.onError(t);
/*     */       }
/*     */       
/*     */       try {
/* 115 */         this.onAfterTerminate.run();
/* 116 */       } catch (Throwable e) {
/* 117 */         Exceptions.throwIfFatal(e);
/* 118 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 124 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/* 128 */         this.onComplete.run();
/* 129 */       } catch (Throwable e) {
/* 130 */         fail(e);
/*     */         
/*     */         return;
/*     */       } 
/* 134 */       this.done = true;
/* 135 */       this.downstream.onComplete();
/*     */       
/*     */       try {
/* 138 */         this.onAfterTerminate.run();
/* 139 */       } catch (Throwable e) {
/* 140 */         Exceptions.throwIfFatal(e);
/* 141 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 147 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       T v;
/*     */       try {
/* 156 */         v = (T)this.qs.poll();
/* 157 */       } catch (Throwable ex) {
/* 158 */         Exceptions.throwIfFatal(ex);
/*     */         try {
/* 160 */           this.onError.accept(ex);
/* 161 */         } catch (Throwable exc) {
/* 162 */           throw new CompositeException(new Throwable[] { ex, exc });
/*     */         } 
/* 164 */         throw ExceptionHelper.throwIfThrowable(ex);
/*     */       } 
/*     */       
/* 167 */       if (v != null) {
/*     */         try {
/*     */           try {
/* 170 */             this.onNext.accept(v);
/* 171 */           } catch (Throwable ex) {
/* 172 */             Exceptions.throwIfFatal(ex);
/*     */             try {
/* 174 */               this.onError.accept(ex);
/* 175 */             } catch (Throwable exc) {
/* 176 */               throw new CompositeException(new Throwable[] { ex, exc });
/*     */             } 
/* 178 */             throw ExceptionHelper.throwIfThrowable(ex);
/*     */           } 
/*     */         } finally {
/* 181 */           this.onAfterTerminate.run();
/*     */         }
/*     */       
/* 184 */       } else if (this.sourceMode == 1) {
/* 185 */         this.onComplete.run();
/*     */         
/* 187 */         this.onAfterTerminate.run();
/*     */       } 
/*     */       
/* 190 */       return v;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DoOnEachConditionalSubscriber<T>
/*     */     extends BasicFuseableConditionalSubscriber<T, T>
/*     */   {
/*     */     final Consumer<? super T> onNext;
/*     */     
/*     */     final Consumer<? super Throwable> onError;
/*     */     
/*     */     final Action onComplete;
/*     */     final Action onAfterTerminate;
/*     */     
/*     */     DoOnEachConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
/* 206 */       super(actual);
/* 207 */       this.onNext = onNext;
/* 208 */       this.onError = onError;
/* 209 */       this.onComplete = onComplete;
/* 210 */       this.onAfterTerminate = onAfterTerminate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 215 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 219 */       if (this.sourceMode != 0) {
/* 220 */         this.downstream.onNext(null);
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/* 225 */         this.onNext.accept(t);
/* 226 */       } catch (Throwable e) {
/* 227 */         fail(e);
/*     */         
/*     */         return;
/*     */       } 
/* 231 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 236 */       if (this.done) {
/* 237 */         return false;
/*     */       }
/*     */       
/*     */       try {
/* 241 */         this.onNext.accept(t);
/* 242 */       } catch (Throwable e) {
/* 243 */         fail(e);
/* 244 */         return false;
/*     */       } 
/*     */       
/* 247 */       return this.downstream.tryOnNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 252 */       if (this.done) {
/* 253 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 256 */       this.done = true;
/* 257 */       boolean relay = true;
/*     */       try {
/* 259 */         this.onError.accept(t);
/* 260 */       } catch (Throwable e) {
/* 261 */         Exceptions.throwIfFatal(e);
/* 262 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, e }));
/* 263 */         relay = false;
/*     */       } 
/* 265 */       if (relay) {
/* 266 */         this.downstream.onError(t);
/*     */       }
/*     */       
/*     */       try {
/* 270 */         this.onAfterTerminate.run();
/* 271 */       } catch (Throwable e) {
/* 272 */         Exceptions.throwIfFatal(e);
/* 273 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 279 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       try {
/* 283 */         this.onComplete.run();
/* 284 */       } catch (Throwable e) {
/* 285 */         fail(e);
/*     */         
/*     */         return;
/*     */       } 
/* 289 */       this.done = true;
/* 290 */       this.downstream.onComplete();
/*     */       
/*     */       try {
/* 293 */         this.onAfterTerminate.run();
/* 294 */       } catch (Throwable e) {
/* 295 */         Exceptions.throwIfFatal(e);
/* 296 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 302 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       T v;
/*     */       try {
/* 311 */         v = (T)this.qs.poll();
/* 312 */       } catch (Throwable ex) {
/* 313 */         Exceptions.throwIfFatal(ex);
/*     */         try {
/* 315 */           this.onError.accept(ex);
/* 316 */         } catch (Throwable exc) {
/* 317 */           throw new CompositeException(new Throwable[] { ex, exc });
/*     */         } 
/* 319 */         throw ExceptionHelper.throwIfThrowable(ex);
/*     */       } 
/*     */       
/* 322 */       if (v != null) {
/*     */         try {
/*     */           try {
/* 325 */             this.onNext.accept(v);
/* 326 */           } catch (Throwable ex) {
/* 327 */             Exceptions.throwIfFatal(ex);
/*     */             try {
/* 329 */               this.onError.accept(ex);
/* 330 */             } catch (Throwable exc) {
/* 331 */               throw new CompositeException(new Throwable[] { ex, exc });
/*     */             } 
/* 333 */             throw ExceptionHelper.throwIfThrowable(ex);
/*     */           } 
/*     */         } finally {
/* 336 */           this.onAfterTerminate.run();
/*     */         }
/*     */       
/* 339 */       } else if (this.sourceMode == 1) {
/* 340 */         this.onComplete.run();
/*     */         
/* 342 */         this.onAfterTerminate.run();
/*     */       } 
/*     */       
/* 345 */       return v;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDoOnEach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */