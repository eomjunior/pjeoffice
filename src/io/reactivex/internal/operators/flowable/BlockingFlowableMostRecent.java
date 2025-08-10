/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.subscribers.DefaultSubscriber;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class BlockingFlowableMostRecent<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final T initialValue;
/*     */   
/*     */   public BlockingFlowableMostRecent(Flowable<T> source, T initialValue) {
/*  37 */     this.source = source;
/*  38 */     this.initialValue = initialValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public java.util.Iterator<T> iterator() {
/*  43 */     MostRecentSubscriber<T> mostRecentSubscriber = new MostRecentSubscriber<T>(this.initialValue);
/*     */     
/*  45 */     this.source.subscribe((FlowableSubscriber)mostRecentSubscriber);
/*     */     
/*  47 */     return mostRecentSubscriber.getIterable();
/*     */   }
/*     */   
/*     */   static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
/*     */     volatile Object value;
/*     */     
/*     */     MostRecentSubscriber(T value) {
/*  54 */       this.value = NotificationLite.next(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  59 */       this.value = NotificationLite.complete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  64 */       this.value = NotificationLite.error(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T args) {
/*  69 */       this.value = NotificationLite.next(args);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator getIterable() {
/*  78 */       return new Iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     final class Iterator
/*     */       implements java.util.Iterator<T>
/*     */     {
/*     */       private Object buf;
/*     */ 
/*     */       
/*     */       public boolean hasNext() {
/*  89 */         this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value;
/*  90 */         return !NotificationLite.isComplete(this.buf);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public T next() {
/*     */         try {
/*  97 */           if (this.buf == null) {
/*  98 */             this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value;
/*     */           }
/* 100 */           if (NotificationLite.isComplete(this.buf)) {
/* 101 */             throw new NoSuchElementException();
/*     */           }
/* 103 */           if (NotificationLite.isError(this.buf)) {
/* 104 */             throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
/*     */           }
/* 106 */           return (T)NotificationLite.getValue(this.buf);
/*     */         } finally {
/*     */           
/* 109 */           this.buf = null;
/*     */         } 
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 115 */         throw new UnsupportedOperationException("Read only iterator"); } } } final class Iterator implements java.util.Iterator<T> { public void remove() { throw new UnsupportedOperationException("Read only iterator"); }
/*     */ 
/*     */     
/*     */     private Object buf;
/*     */     
/*     */     public boolean hasNext() {
/*     */       this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value;
/*     */       return !NotificationLite.isComplete(this.buf);
/*     */     }
/*     */     
/*     */     public T next() {
/*     */       try {
/*     */         if (this.buf == null)
/*     */           this.buf = BlockingFlowableMostRecent.MostRecentSubscriber.this.value; 
/*     */         if (NotificationLite.isComplete(this.buf))
/*     */           throw new NoSuchElementException(); 
/*     */         if (NotificationLite.isError(this.buf))
/*     */           throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf)); 
/*     */         return (T)NotificationLite.getValue(this.buf);
/*     */       } finally {
/*     */         this.buf = null;
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/BlockingFlowableMostRecent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */