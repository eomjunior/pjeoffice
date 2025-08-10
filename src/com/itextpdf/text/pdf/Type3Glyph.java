/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.DocumentException;
/*    */ import com.itextpdf.text.Image;
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Type3Glyph
/*    */   extends PdfContentByte
/*    */ {
/*    */   private PageResources pageResources;
/*    */   private boolean colorized;
/*    */   
/*    */   private Type3Glyph() {
/* 57 */     super(null);
/*    */   }
/*    */   
/*    */   Type3Glyph(PdfWriter writer, PageResources pageResources, float wx, float llx, float lly, float urx, float ury, boolean colorized) {
/* 61 */     super(writer);
/* 62 */     this.pageResources = pageResources;
/* 63 */     this.colorized = colorized;
/* 64 */     if (colorized) {
/* 65 */       this.content.append(wx).append(" 0 d0\n");
/*    */     } else {
/*    */       
/* 68 */       this.content.append(wx).append(" 0 ").append(llx).append(' ').append(lly).append(' ').append(urx).append(' ').append(ury).append(" d1\n");
/*    */     } 
/*    */   }
/*    */   
/*    */   PageResources getPageResources() {
/* 73 */     return this.pageResources;
/*    */   }
/*    */   
/*    */   public void addImage(Image image, float a, float b, float c, float d, float e, float f, boolean inlineImage) throws DocumentException {
/* 77 */     if (!this.colorized && (!image.isMask() || (image.getBpc() != 1 && image.getBpc() <= 255)))
/* 78 */       throw new DocumentException(MessageLocalization.getComposedMessage("not.colorized.typed3.fonts.only.accept.mask.images", new Object[0])); 
/* 79 */     super.addImage(image, a, b, c, d, e, f, inlineImage);
/*    */   }
/*    */   
/*    */   public PdfContentByte getDuplicate() {
/* 83 */     Type3Glyph dup = new Type3Glyph();
/* 84 */     dup.writer = this.writer;
/* 85 */     dup.pdf = this.pdf;
/* 86 */     dup.pageResources = this.pageResources;
/* 87 */     dup.colorized = this.colorized;
/* 88 */     return dup;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Type3Glyph.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */