/*     */ package io.reactivex.internal.observers;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.util.BlockingHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public final class BlockingMultiObserver<T>
/*     */   extends CountDownLatch
/*     */   implements SingleObserver<T>, CompletableObserver, MaybeObserver<T>
/*     */ {
/*     */   T value;
/*     */   Throwable error;
/*     */   Disposable upstream;
/*     */   volatile boolean cancelled;
/*     */   
/*     */   public BlockingMultiObserver() {
/*  40 */     super(1);
/*     */   }
/*     */   
/*     */   void dispose() {
/*  44 */     this.cancelled = true;
/*  45 */     Disposable d = this.upstream;
/*  46 */     if (d != null) {
/*  47 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/*  53 */     this.upstream = d;
/*  54 */     if (this.cancelled) {
/*  55 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSuccess(T value) {
/*  61 */     this.value = value;
/*  62 */     countDown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable e) {
/*  67 */     this.error = e;
/*  68 */     countDown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/*  73 */     countDown();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T blockingGet() {
/*  82 */     if (getCount() != 0L) {
/*     */       try {
/*  84 */         BlockingHelper.verifyNonBlocking();
/*  85 */         await();
/*  86 */       } catch (InterruptedException interruptedException) {
/*  87 */         dispose();
/*  88 */         throw ExceptionHelper.wrapOrThrow(interruptedException);
/*     */       } 
/*     */     }
/*  91 */     Throwable ex = this.error;
/*  92 */     if (ex != null) {
/*  93 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     }
/*  95 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T blockingGet(T defaultValue) {
/* 105 */     if (getCount() != 0L) {
/*     */       try {
/* 107 */         BlockingHelper.verifyNonBlocking();
/* 108 */         await();
/* 109 */       } catch (InterruptedException interruptedException) {
/* 110 */         dispose();
/* 111 */         throw ExceptionHelper.wrapOrThrow(interruptedException);
/*     */       } 
/*     */     }
/* 114 */     Throwable ex = this.error;
/* 115 */     if (ex != null) {
/* 116 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     }
/* 118 */     T v = this.value;
/* 119 */     return (v != null) ? v : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable blockingGetError() {
/* 128 */     if (getCount() != 0L) {
/*     */       try {
/* 130 */         BlockingHelper.verifyNonBlocking();
/* 131 */         await();
/* 132 */       } catch (InterruptedException ex) {
/* 133 */         dispose();
/* 134 */         return ex;
/*     */       } 
/*     */     }
/* 137 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable blockingGetError(long timeout, TimeUnit unit) {
/* 148 */     if (getCount() != 0L) {
/*     */       try {
/* 150 */         BlockingHelper.verifyNonBlocking();
/* 151 */         if (!await(timeout, unit)) {
/* 152 */           dispose();
/* 153 */           throw ExceptionHelper.wrapOrThrow(new TimeoutException(ExceptionHelper.timeoutMessage(timeout, unit)));
/*     */         } 
/* 155 */       } catch (InterruptedException ex) {
/* 156 */         dispose();
/* 157 */         throw ExceptionHelper.wrapOrThrow(ex);
/*     */       } 
/*     */     }
/* 160 */     return this.error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean blockingAwait(long timeout, TimeUnit unit) {
/* 171 */     if (getCount() != 0L) {
/*     */       try {
/* 173 */         BlockingHelper.verifyNonBlocking();
/* 174 */         if (!await(timeout, unit)) {
/* 175 */           dispose();
/* 176 */           return false;
/*     */         } 
/* 178 */       } catch (InterruptedException interruptedException) {
/* 179 */         dispose();
/* 180 */         throw ExceptionHelper.wrapOrThrow(interruptedException);
/*     */       } 
/*     */     }
/* 183 */     Throwable ex = this.error;
/* 184 */     if (ex != null) {
/* 185 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     }
/* 187 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BlockingMultiObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */