/*    */ package com.yworks.yshrink.model;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvocationFactory
/*    */ {
/* 13 */   private static final InvocationFactory instance = new InvocationFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected static InvocationFactory getInstance() {
/* 21 */     return instance;
/*    */   }
/*    */   
/* 24 */   private Map<String, Invocation> invocations = new HashMap<>();
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
/*    */   protected Invocation getInvocation(int opcode, String type, String name, String desc) {
/* 36 */     String key = type + name + desc + opcode;
/* 37 */     Invocation val = this.invocations.get(key);
/* 38 */     if (null == val) {
/* 39 */       val = new Invocation(opcode, type, name, desc);
/* 40 */       this.invocations.put(key, val);
/*    */     } 
/* 42 */     return val;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/InvocationFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */