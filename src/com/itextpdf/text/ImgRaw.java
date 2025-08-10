/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ import java.net.URL;
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
/*    */ public class ImgRaw
/*    */   extends Image
/*    */ {
/*    */   ImgRaw(Image image) {
/* 61 */     super(image);
/*    */   }
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
/*    */   public ImgRaw(int width, int height, int components, int bpc, byte[] data) throws BadElementException {
/* 75 */     super((URL)null);
/* 76 */     this.type = 34;
/* 77 */     this.scaledHeight = height;
/* 78 */     setTop(this.scaledHeight);
/* 79 */     this.scaledWidth = width;
/* 80 */     setRight(this.scaledWidth);
/* 81 */     if (components != 1 && components != 3 && components != 4)
/* 82 */       throw new BadElementException(MessageLocalization.getComposedMessage("components.must.be.1.3.or.4", new Object[0])); 
/* 83 */     if (bpc != 1 && bpc != 2 && bpc != 4 && bpc != 8)
/* 84 */       throw new BadElementException(MessageLocalization.getComposedMessage("bits.per.component.must.be.1.2.4.or.8", new Object[0])); 
/* 85 */     this.colorspace = components;
/* 86 */     this.bpc = bpc;
/* 87 */     this.rawData = data;
/* 88 */     this.plainWidth = getWidth();
/* 89 */     this.plainHeight = getHeight();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ImgRaw.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */