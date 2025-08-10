/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IAcumulator;
/*    */ import java.io.IOException;
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
/*    */ public class LineAppender
/*    */   implements IAcumulator<String>
/*    */ {
/* 35 */   private final StringBuilder sb = new StringBuilder();
/*    */ 
/*    */   
/*    */   public String get() {
/* 39 */     return this.sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void accept(String line) {
/* 44 */     this.sb.append(line).append(System.lineSeparator());
/*    */   }
/*    */ 
/*    */   
/*    */   public String handleFail(IOException e) {
/* 49 */     return get();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/LineAppender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */