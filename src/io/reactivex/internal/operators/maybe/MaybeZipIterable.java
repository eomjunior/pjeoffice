/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeObserver;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MaybeZipIterable<T, R>
/*    */   extends Maybe<R>
/*    */ {
/*    */   final Iterable<? extends MaybeSource<? extends T>> sources;
/*    */   final Function<? super Object[], ? extends R> zipper;
/*    */   
/*    */   public MaybeZipIterable(Iterable<? extends MaybeSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/* 32 */     this.sources = sources;
/* 33 */     this.zipper = zipper;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(MaybeObserver<? super R> observer) {
/* 39 */     MaybeSource[] arrayOfMaybeSource = new MaybeSource[8];
/* 40 */     int n = 0;
/*    */     
/*    */     try {
/* 43 */       for (MaybeSource<? extends T> source : this.sources) {
/* 44 */         if (source == null) {
/* 45 */           EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*    */           return;
/*    */         } 
/* 48 */         if (n == arrayOfMaybeSource.length) {
/* 49 */           arrayOfMaybeSource = Arrays.<MaybeSource>copyOf(arrayOfMaybeSource, n + (n >> 2));
/*    */         }
/* 51 */         arrayOfMaybeSource[n++] = source;
/*    */       } 
/* 53 */     } catch (Throwable ex) {
/* 54 */       Exceptions.throwIfFatal(ex);
/* 55 */       EmptyDisposable.error(ex, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     if (n == 0) {
/* 60 */       EmptyDisposable.complete(observer);
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     if (n == 1) {
/* 65 */       arrayOfMaybeSource[0].subscribe(new MaybeMap.MapMaybeObserver<Object, R>(observer, new SingletonArrayFunc()));
/*    */       
/*    */       return;
/*    */     } 
/* 69 */     MaybeZipArray.ZipCoordinator<T, R> parent = new MaybeZipArray.ZipCoordinator<T, R>(observer, n, this.zipper);
/*    */     
/* 71 */     observer.onSubscribe(parent);
/*    */     
/* 73 */     for (int i = 0; i < n; i++) {
/* 74 */       if (parent.isDisposed()) {
/*    */         return;
/*    */       }
/*    */       
/* 78 */       arrayOfMaybeSource[i].subscribe(parent.observers[i]);
/*    */     } 
/*    */   }
/*    */   
/*    */   final class SingletonArrayFunc
/*    */     implements Function<T, R> {
/*    */     public R apply(T t) throws Exception {
/* 85 */       return (R)ObjectHelper.requireNonNull(MaybeZipIterable.this.zipper.apply(new Object[] { t }, ), "The zipper returned a null value");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */