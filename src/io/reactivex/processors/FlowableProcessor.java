/*    */ package io.reactivex.processors;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.annotations.CheckReturnValue;
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.annotations.Nullable;
/*    */ import org.reactivestreams.Processor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FlowableProcessor<T>
/*    */   extends Flowable<T>
/*    */   implements Processor<T, T>, FlowableSubscriber<T>
/*    */ {
/*    */   public abstract boolean hasSubscribers();
/*    */   
/*    */   public abstract boolean hasThrowable();
/*    */   
/*    */   public abstract boolean hasComplete();
/*    */   
/*    */   @Nullable
/*    */   public abstract Throwable getThrowable();
/*    */   
/*    */   @CheckReturnValue
/*    */   @NonNull
/*    */   public final FlowableProcessor<T> toSerialized() {
/* 74 */     if (this instanceof SerializedProcessor) {
/* 75 */       return this;
/*    */     }
/* 77 */     return new SerializedProcessor<T>(this);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/FlowableProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */