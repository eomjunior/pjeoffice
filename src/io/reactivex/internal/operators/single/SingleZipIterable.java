/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.EmptyDisposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.Arrays;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public final class SingleZipIterable<T, R>
/*    */   extends Single<R>
/*    */ {
/*    */   final Iterable<? extends SingleSource<? extends T>> sources;
/*    */   final Function<? super Object[], ? extends R> zipper;
/*    */   
/*    */   public SingleZipIterable(Iterable<? extends SingleSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
/* 32 */     this.sources = sources;
/* 33 */     this.zipper = zipper;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super R> observer) {
/* 39 */     SingleSource[] arrayOfSingleSource = new SingleSource[8];
/* 40 */     int n = 0;
/*    */     
/*    */     try {
/* 43 */       for (SingleSource<? extends T> source : this.sources) {
/* 44 */         if (source == null) {
/* 45 */           EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
/*    */           return;
/*    */         } 
/* 48 */         if (n == arrayOfSingleSource.length) {
/* 49 */           arrayOfSingleSource = Arrays.<SingleSource>copyOf(arrayOfSingleSource, n + (n >> 2));
/*    */         }
/* 51 */         arrayOfSingleSource[n++] = source;
/*    */       } 
/* 53 */     } catch (Throwable ex) {
/* 54 */       Exceptions.throwIfFatal(ex);
/* 55 */       EmptyDisposable.error(ex, observer);
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     if (n == 0) {
/* 60 */       EmptyDisposable.error(new NoSuchElementException(), observer);
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     if (n == 1) {
/* 65 */       arrayOfSingleSource[0].subscribe(new SingleMap.MapSingleObserver<Object, R>(observer, new SingletonArrayFunc()));
/*    */       
/*    */       return;
/*    */     } 
/* 69 */     SingleZipArray.ZipCoordinator<T, R> parent = new SingleZipArray.ZipCoordinator<T, R>(observer, n, this.zipper);
/*    */     
/* 71 */     observer.onSubscribe(parent);
/*    */     
/* 73 */     for (int i = 0; i < n; i++) {
/* 74 */       if (parent.isDisposed()) {
/*    */         return;
/*    */       }
/*    */       
/* 78 */       arrayOfSingleSource[i].subscribe(parent.observers[i]);
/*    */     } 
/*    */   }
/*    */   
/*    */   final class SingletonArrayFunc
/*    */     implements Function<T, R> {
/*    */     public R apply(T t) throws Exception {
/* 85 */       return (R)ObjectHelper.requireNonNull(SingleZipIterable.this.zipper.apply(new Object[] { t }, ), "The zipper returned a null value");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleZipIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */