/*    */ package com.itextpdf.text.pdf.codec.wmf;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MetaObject
/*    */ {
/*    */   public static final int META_NOT_SUPPORTED = 0;
/*    */   public static final int META_PEN = 1;
/*    */   public static final int META_BRUSH = 2;
/*    */   public static final int META_FONT = 3;
/* 51 */   public int type = 0;
/*    */ 
/*    */   
/*    */   public MetaObject() {}
/*    */   
/*    */   public MetaObject(int type) {
/* 57 */     this.type = type;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 61 */     return this.type;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */