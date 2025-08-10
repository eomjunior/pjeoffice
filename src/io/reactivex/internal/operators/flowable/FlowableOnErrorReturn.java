/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.internal.subscribers.SinglePostCompleteSubscriber;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ public final class FlowableOnErrorReturn<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   final Function<? super Throwable, ? extends T> valueSupplier;
/*    */   
/*    */   public FlowableOnErrorReturn(Flowable<T> source, Function<? super Throwable, ? extends T> valueSupplier) {
/* 27 */     super(source);
/* 28 */     this.valueSupplier = valueSupplier;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 33 */     this.source.subscribe((FlowableSubscriber)new OnErrorReturnSubscriber<T>(s, this.valueSupplier));
/*    */   }
/*    */   
/*    */   static final class OnErrorReturnSubscriber<T>
/*    */     extends SinglePostCompleteSubscriber<T, T>
/*    */   {
/*    */     private static final long serialVersionUID = -3740826063558713822L;
/*    */     final Function<? super Throwable, ? extends T> valueSupplier;
/*    */     
/*    */     OnErrorReturnSubscriber(Subscriber<? super T> actual, Function<? super Throwable, ? extends T> valueSupplier) {
/* 43 */       super(actual);
/* 44 */       this.valueSupplier = valueSupplier;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 49 */       this.produced++;
/* 50 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/*    */       T v;
/*    */       try {
/* 57 */         v = (T)ObjectHelper.requireNonNull(this.valueSupplier.apply(t), "The valueSupplier returned a null value");
/* 58 */       } catch (Throwable ex) {
/* 59 */         Exceptions.throwIfFatal(ex);
/* 60 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { t, ex }));
/*    */         return;
/*    */       } 
/* 63 */       complete(v);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 68 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableOnErrorReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */