/*    */ package com.itextpdf.text.pdf.codec.wmf;
/*    */ 
/*    */ import com.itextpdf.text.BaseColor;
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
/*    */ public class MetaPen
/*    */   extends MetaObject
/*    */ {
/*    */   public static final int PS_SOLID = 0;
/*    */   public static final int PS_DASH = 1;
/*    */   public static final int PS_DOT = 2;
/*    */   public static final int PS_DASHDOT = 3;
/*    */   public static final int PS_DASHDOTDOT = 4;
/*    */   public static final int PS_NULL = 5;
/*    */   public static final int PS_INSIDEFRAME = 6;
/* 58 */   int style = 0;
/* 59 */   int penWidth = 1;
/* 60 */   BaseColor color = BaseColor.BLACK;
/*    */   
/*    */   public MetaPen() {
/* 63 */     this.type = 1;
/*    */   }
/*    */   
/*    */   public void init(InputMeta in) throws IOException {
/* 67 */     this.style = in.readWord();
/* 68 */     this.penWidth = in.readShort();
/* 69 */     in.readWord();
/* 70 */     this.color = in.readColor();
/*    */   }
/*    */   
/*    */   public int getStyle() {
/* 74 */     return this.style;
/*    */   }
/*    */   
/*    */   public int getPenWidth() {
/* 78 */     return this.penWidth;
/*    */   }
/*    */   
/*    */   public BaseColor getColor() {
/* 82 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaPen.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */