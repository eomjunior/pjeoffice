/*    */ package com.itextpdf.text.pdf.parser.clipper;
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
/*    */ public class LongRect
/*    */ {
/*    */   public long left;
/*    */   public long top;
/*    */   public long right;
/*    */   public long bottom;
/*    */   
/*    */   public LongRect() {}
/*    */   
/*    */   public LongRect(long l, long t, long r, long b) {
/* 88 */     this.left = l;
/* 89 */     this.top = t;
/* 90 */     this.right = r;
/* 91 */     this.bottom = b;
/*    */   }
/*    */   
/*    */   public LongRect(LongRect ir) {
/* 95 */     this.left = ir.left;
/* 96 */     this.top = ir.top;
/* 97 */     this.right = ir.right;
/* 98 */     this.bottom = ir.bottom;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/LongRect.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */