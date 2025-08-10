/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IStage;
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
/*    */ public class StateWrapper
/*    */   implements IState
/*    */ {
/*    */   private final IState state;
/*    */   
/*    */   public StateWrapper(IState state) {
/* 38 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStep() {
/* 43 */     return this.state.getStep();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTotal() {
/* 48 */     return this.state.getTotal();
/*    */   }
/*    */ 
/*    */   
/*    */   public IStage getStage() {
/* 53 */     return this.state.getStage();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTime() {
/* 58 */     return this.state.getTime();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStepTree() {
/* 63 */     return this.state.getStepTree();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAborted() {
/* 68 */     return this.state.isAborted();
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getAbortCause() {
/* 73 */     return this.state.getAbortCause();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/StateWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */