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
/*    */ public class MetaBrush
/*    */   extends MetaObject
/*    */ {
/*    */   public static final int BS_SOLID = 0;
/*    */   public static final int BS_NULL = 1;
/*    */   public static final int BS_HATCHED = 2;
/*    */   public static final int BS_PATTERN = 3;
/*    */   public static final int BS_DIBPATTERN = 5;
/*    */   public static final int HS_HORIZONTAL = 0;
/*    */   public static final int HS_VERTICAL = 1;
/*    */   public static final int HS_FDIAGONAL = 2;
/*    */   public static final int HS_BDIAGONAL = 3;
/*    */   public static final int HS_CROSS = 4;
/*    */   public static final int HS_DIAGCROSS = 5;
/* 62 */   int style = 0;
/*    */   int hatch;
/* 64 */   BaseColor color = BaseColor.WHITE;
/*    */   
/*    */   public MetaBrush() {
/* 67 */     this.type = 2;
/*    */   }
/*    */   
/*    */   public void init(InputMeta in) throws IOException {
/* 71 */     this.style = in.readWord();
/* 72 */     this.color = in.readColor();
/* 73 */     this.hatch = in.readWord();
/*    */   }
/*    */   
/*    */   public int getStyle() {
/* 77 */     return this.style;
/*    */   }
/*    */   
/*    */   public int getHatch() {
/* 81 */     return this.hatch;
/*    */   }
/*    */   
/*    */   public BaseColor getColor() {
/* 85 */     return this.color;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaBrush.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */