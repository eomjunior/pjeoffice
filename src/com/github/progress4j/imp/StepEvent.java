/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IState;
/*    */ import com.github.progress4j.IStepEvent;
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
/*    */ class StepEvent
/*    */   extends Event
/*    */   implements IStepEvent
/*    */ {
/*    */   private boolean info;
/*    */   
/*    */   StepEvent(IState state, String message, int stackSize, boolean info) {
/* 36 */     super(state, message, stackSize);
/* 37 */     this.info = info;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInfo() {
/* 42 */     return this.info;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/StepEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */