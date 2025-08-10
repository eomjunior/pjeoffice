/*    */ package io.reactivex.flowables;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.annotations.Nullable;
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
/*    */ public abstract class GroupedFlowable<K, T>
/*    */   extends Flowable<T>
/*    */ {
/*    */   final K key;
/*    */   
/*    */   protected GroupedFlowable(@Nullable K key) {
/* 42 */     this.key = key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public K getKey() {
/* 52 */     return this.key;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/flowables/GroupedFlowable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */