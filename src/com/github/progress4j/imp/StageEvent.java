/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IStageEvent;
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
/*    */ class StageEvent
/*    */   extends Event
/*    */   implements IStageEvent
/*    */ {
/*    */   private final boolean end;
/*    */   
/*    */   StageEvent(IState state, String message, int stackSize, boolean end) {
/* 38 */     super(state, message, stackSize);
/* 39 */     this.end = end;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isEnd() {
/* 44 */     return this.end;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isStart() {
/* 49 */     return !this.end;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/StageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */