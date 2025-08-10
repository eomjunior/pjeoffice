/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
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
/*    */ public class ImgCCITT
/*    */   extends Image
/*    */ {
/*    */   ImgCCITT(Image image) {
/* 62 */     super(image);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImgCCITT(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException {
/* 81 */     super((URL)null);
/* 82 */     if (typeCCITT != 256 && typeCCITT != 257 && typeCCITT != 258)
/* 83 */       throw new BadElementException(MessageLocalization.getComposedMessage("the.ccitt.compression.type.must.be.ccittg4.ccittg3.1d.or.ccittg3.2d", new Object[0])); 
/* 84 */     if (reverseBits)
/* 85 */       TIFFFaxDecoder.reverseBits(data); 
/* 86 */     this.type = 34;
/* 87 */     this.scaledHeight = height;
/* 88 */     setTop(this.scaledHeight);
/* 89 */     this.scaledWidth = width;
/* 90 */     setRight(this.scaledWidth);
/* 91 */     this.colorspace = parameters;
/* 92 */     this.bpc = typeCCITT;
/* 93 */     this.rawData = data;
/* 94 */     this.plainWidth = getWidth();
/* 95 */     this.plainHeight = getHeight();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ImgCCITT.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */