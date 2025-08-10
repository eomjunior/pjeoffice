/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Queue;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class ConsumingQueueIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private final Queue<T> queue;
/*    */   
/*    */   ConsumingQueueIterator(Queue<T> queue) {
/* 34 */     this.queue = (Queue<T>)Preconditions.checkNotNull(queue);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   protected T computeNext() {
/* 41 */     if (this.queue.isEmpty()) {
/* 42 */       return endOfData();
/*    */     }
/* 44 */     return this.queue.remove();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ConsumingQueueIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */