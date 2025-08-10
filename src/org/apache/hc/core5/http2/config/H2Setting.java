/*    */ package org.apache.hc.core5.http2.config;
/*    */ 
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public final class H2Setting
/*    */ {
/*    */   private final H2Param param;
/*    */   private final int value;
/*    */   
/*    */   public H2Setting(H2Param param, int value) {
/* 42 */     Args.notNull(param, "Setting parameter");
/* 43 */     Args.notNegative(value, "Setting value must be a non-negative value");
/* 44 */     this.param = param;
/* 45 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 49 */     return this.param.code;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 53 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     StringBuilder sb = (new StringBuilder()).append(this.param).append(": ").append(this.value);
/* 59 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/config/H2Setting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */