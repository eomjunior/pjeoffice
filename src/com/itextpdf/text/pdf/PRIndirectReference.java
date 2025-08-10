/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PRIndirectReference
/*    */   extends PdfIndirectReference
/*    */ {
/*    */   protected PdfReader reader;
/*    */   
/*    */   public PRIndirectReference(PdfReader reader, int number, int generation) {
/* 64 */     this.type = 10;
/* 65 */     this.number = number;
/* 66 */     this.generation = generation;
/* 67 */     this.reader = reader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PRIndirectReference(PdfReader reader, int number) {
/* 78 */     this(reader, number, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 84 */     if (writer != null) {
/* 85 */       int n = writer.getNewObjectNumber(this.reader, this.number, this.generation);
/* 86 */       os.write(PdfEncodings.convertToBytes(n + " " + (this.reader.isAppendable() ? this.generation : 0) + " R", (String)null));
/*    */     } else {
/*    */       
/* 89 */       super.toPdf((PdfWriter)null, os);
/*    */     } 
/*    */   }
/*    */   
/*    */   public PdfReader getReader() {
/* 94 */     return this.reader;
/*    */   }
/*    */   
/*    */   public void setNumber(int number, int generation) {
/* 98 */     this.number = number;
/* 99 */     this.generation = generation;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PRIndirectReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */