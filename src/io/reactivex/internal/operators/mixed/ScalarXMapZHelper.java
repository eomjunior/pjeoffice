/*     */ package io.reactivex.internal.operators.mixed;
/*     */ 
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.operators.maybe.MaybeToObservable;
/*     */ import io.reactivex.internal.operators.single.SingleToObservable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ScalarXMapZHelper
/*     */ {
/*     */   private ScalarXMapZHelper() {
/*  36 */     throw new IllegalStateException("No instances!");
/*     */   }
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
/*     */   static <T> boolean tryAsCompletable(Object source, Function<? super T, ? extends CompletableSource> mapper, CompletableObserver observer) {
/*  53 */     if (source instanceof Callable) {
/*     */       
/*  55 */       Callable<T> call = (Callable<T>)source;
/*  56 */       CompletableSource cs = null;
/*     */       try {
/*  58 */         T item = call.call();
/*  59 */         if (item != null) {
/*  60 */           cs = (CompletableSource)ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null CompletableSource");
/*     */         }
/*  62 */       } catch (Throwable ex) {
/*  63 */         Exceptions.throwIfFatal(ex);
/*  64 */         EmptyDisposable.error(ex, observer);
/*  65 */         return true;
/*     */       } 
/*     */       
/*  68 */       if (cs == null) {
/*  69 */         EmptyDisposable.complete(observer);
/*     */       } else {
/*  71 */         cs.subscribe(observer);
/*     */       } 
/*  73 */       return true;
/*     */     } 
/*  75 */     return false;
/*     */   }
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
/*     */   static <T, R> boolean tryAsMaybe(Object source, Function<? super T, ? extends MaybeSource<? extends R>> mapper, Observer<? super R> observer) {
/*  92 */     if (source instanceof Callable) {
/*     */       
/*  94 */       Callable<T> call = (Callable<T>)source;
/*  95 */       MaybeSource<? extends R> cs = null;
/*     */       try {
/*  97 */         T item = call.call();
/*  98 */         if (item != null) {
/*  99 */           cs = (MaybeSource<? extends R>)ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null MaybeSource");
/*     */         }
/* 101 */       } catch (Throwable ex) {
/* 102 */         Exceptions.throwIfFatal(ex);
/* 103 */         EmptyDisposable.error(ex, observer);
/* 104 */         return true;
/*     */       } 
/*     */       
/* 107 */       if (cs == null) {
/* 108 */         EmptyDisposable.complete(observer);
/*     */       } else {
/* 110 */         cs.subscribe(MaybeToObservable.create(observer));
/*     */       } 
/* 112 */       return true;
/*     */     } 
/* 114 */     return false;
/*     */   }
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
/*     */   static <T, R> boolean tryAsSingle(Object source, Function<? super T, ? extends SingleSource<? extends R>> mapper, Observer<? super R> observer) {
/* 131 */     if (source instanceof Callable) {
/*     */       
/* 133 */       Callable<T> call = (Callable<T>)source;
/* 134 */       SingleSource<? extends R> cs = null;
/*     */       try {
/* 136 */         T item = call.call();
/* 137 */         if (item != null) {
/* 138 */           cs = (SingleSource<? extends R>)ObjectHelper.requireNonNull(mapper.apply(item), "The mapper returned a null SingleSource");
/*     */         }
/* 140 */       } catch (Throwable ex) {
/* 141 */         Exceptions.throwIfFatal(ex);
/* 142 */         EmptyDisposable.error(ex, observer);
/* 143 */         return true;
/*     */       } 
/*     */       
/* 146 */       if (cs == null) {
/* 147 */         EmptyDisposable.complete(observer);
/*     */       } else {
/* 149 */         cs.subscribe(SingleToObservable.create(observer));
/*     */       } 
/* 151 */       return true;
/*     */     } 
/* 153 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/mixed/ScalarXMapZHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */