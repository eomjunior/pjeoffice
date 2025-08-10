/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.functions.Function;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.reactivestreams.Publisher;
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
/*     */ public final class SingleInternalHelper
/*     */ {
/*     */   private SingleInternalHelper() {
/*  32 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */   
/*     */   enum NoSuchElementCallable implements Callable<NoSuchElementException> {
/*  36 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public NoSuchElementException call() throws Exception {
/*  40 */       return new NoSuchElementException();
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Callable<NoSuchElementException> emptyThrower() {
/*  45 */     return NoSuchElementCallable.INSTANCE;
/*     */   }
/*     */   
/*     */   enum ToFlowable
/*     */     implements Function<SingleSource, Publisher> {
/*  50 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public Publisher apply(SingleSource<?> v) {
/*  54 */       return (Publisher)new SingleToFlowable(v);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Function<SingleSource<? extends T>, Publisher<? extends T>> toFlowable() {
/*  60 */     return ToFlowable.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class ToFlowableIterator<T> implements Iterator<Flowable<T>> {
/*     */     private final Iterator<? extends SingleSource<? extends T>> sit;
/*     */     
/*     */     ToFlowableIterator(Iterator<? extends SingleSource<? extends T>> sit) {
/*  67 */       this.sit = sit;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  72 */       return this.sit.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public Flowable<T> next() {
/*  77 */       return new SingleToFlowable<T>(this.sit.next());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  82 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ToFlowableIterable<T>
/*     */     implements Iterable<Flowable<T>> {
/*     */     private final Iterable<? extends SingleSource<? extends T>> sources;
/*     */     
/*     */     ToFlowableIterable(Iterable<? extends SingleSource<? extends T>> sources) {
/*  91 */       this.sources = sources;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Flowable<T>> iterator() {
/*  96 */       return new SingleInternalHelper.ToFlowableIterator<T>(this.sources.iterator());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> Iterable<? extends Flowable<T>> iterableToFlowable(Iterable<? extends SingleSource<? extends T>> sources) {
/* 101 */     return new ToFlowableIterable<T>(sources);
/*     */   }
/*     */   
/*     */   enum ToObservable
/*     */     implements Function<SingleSource, Observable> {
/* 106 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public Observable apply(SingleSource<?> v) {
/* 110 */       return new SingleToObservable(v);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Function<SingleSource<? extends T>, Observable<? extends T>> toObservable() {
/* 116 */     return ToObservable.INSTANCE;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleInternalHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */