/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ import com.itextpdf.text.pdf.PdfTemplate;
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
/*    */ 
/*    */ public class ImgTemplate
/*    */   extends Image
/*    */ {
/*    */   ImgTemplate(Image image) {
/* 63 */     super(image);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImgTemplate(PdfTemplate template) throws BadElementException {
/* 72 */     super((URL)null);
/* 73 */     if (template == null)
/* 74 */       throw new BadElementException(MessageLocalization.getComposedMessage("the.template.can.not.be.null", new Object[0])); 
/* 75 */     if (template.getType() == 3)
/* 76 */       throw new BadElementException(MessageLocalization.getComposedMessage("a.pattern.can.not.be.used.as.a.template.to.create.an.image", new Object[0])); 
/* 77 */     this.type = 35;
/* 78 */     this.scaledHeight = template.getHeight();
/* 79 */     setTop(this.scaledHeight);
/* 80 */     this.scaledWidth = template.getWidth();
/* 81 */     setRight(this.scaledWidth);
/* 82 */     setTemplateData(template);
/* 83 */     this.plainWidth = getWidth();
/* 84 */     this.plainHeight = getHeight();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ImgTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */