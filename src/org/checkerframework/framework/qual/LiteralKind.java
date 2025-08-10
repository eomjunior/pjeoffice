/*    */ package org.checkerframework.framework.qual;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum LiteralKind
/*    */ {
/* 17 */   NULL,
/*    */   
/* 19 */   INT,
/*    */   
/* 21 */   LONG,
/*    */   
/* 23 */   FLOAT,
/*    */   
/* 25 */   DOUBLE,
/*    */   
/* 27 */   BOOLEAN,
/*    */   
/* 29 */   CHAR,
/*    */   
/* 31 */   STRING,
/*    */   
/* 33 */   ALL,
/*    */   
/* 35 */   PRIMITIVE;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<LiteralKind> allLiteralKinds() {
/* 44 */     List<LiteralKind> list = new ArrayList<>(Arrays.asList(values()));
/* 45 */     list.remove(ALL);
/* 46 */     list.remove(PRIMITIVE);
/* 47 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<LiteralKind> primitiveLiteralKinds() {
/* 58 */     return Arrays.asList(new LiteralKind[] { INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/checkerframework/framework/qual/LiteralKind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */