/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.lang.management.ManagementFactory;
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
/*    */ 
/*    */ 
/*    */ public class ProcessUtil
/*    */ {
/*    */   public static String getProcessId(String fallback) {
/* 41 */     String jvmName = ManagementFactory.getRuntimeMXBean().getName();
/* 42 */     int index = jvmName.indexOf('@');
/*    */     
/* 44 */     if (index < 1)
/*    */     {
/* 46 */       return fallback;
/*    */     }
/*    */     
/*    */     try {
/* 50 */       return Long.toString(Long.parseLong(jvmName.substring(0, index)));
/* 51 */     } catch (NumberFormatException numberFormatException) {
/*    */ 
/*    */       
/* 54 */       return fallback;
/*    */     } 
/*    */   }
/*    */   public static void main(String[] args) {
/* 58 */     System.out.println(getProcessId("<PID>"));
/*    */     try {
/* 60 */       Thread.sleep(120000L);
/* 61 */     } catch (Exception exception) {}
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ProcessUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */