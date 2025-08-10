/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IStage;
/*    */ 
/*    */ public class Stage implements IStage {
/*    */   private String message;
/*    */   
/*    */   public Stage(String message) {
/*  9 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 14 */     return this.message;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/Stage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */