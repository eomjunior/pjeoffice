/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ public enum PjeAccessTime
/*    */ {
/* 33 */   AT_THIS_TIME("Sim desta vez"),
/* 34 */   AWAYS("Sempre"),
/* 35 */   NOT("Não"),
/* 36 */   NEVER("Nunca"); private static final PjeAccessTime[] VALUES;
/*    */   static {
/* 38 */     VALUES = values();
/*    */   }
/*    */   private String message;
/*    */   
/*    */   PjeAccessTime(String message) {
/* 43 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/* 47 */     return this.message;
/*    */   }
/*    */   
/*    */   public static void printOptions(PrintStream out) {
/* 51 */     for (PjeAccessTime at : values()) {
/* 52 */       out.println("[" + (at.ordinal() + 1) + "] " + at);
/*    */     }
/*    */   }
/*    */   
/*    */   public static PjeAccessTime fromOptions(int option) {
/* 57 */     return (option < 0 || option >= VALUES.length) ? null : VALUES[option];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeAccessTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */