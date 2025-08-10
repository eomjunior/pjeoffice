/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IEvent;
/*    */ import com.github.progress4j.IState;
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
/*    */ abstract class Event
/*    */   extends StateWrapper
/*    */   implements IEvent
/*    */ {
/*    */   private final String message;
/*    */   private final int stackSize;
/*    */   
/*    */   protected Event(IState state, String message, int stackSize) {
/* 40 */     super(state);
/* 41 */     this.stackSize = stackSize;
/* 42 */     this.message = message;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final int getStackSize() {
/* 48 */     return this.stackSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getMessage() {
/* 53 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isIndeterminated() {
/* 58 */     return (getTotal() < 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 63 */     return "[" + this.stackSize + "]" + getStage().toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */