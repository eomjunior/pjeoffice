/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IInputDescriptor;
/*    */ import com.github.filehandler4j.imp.InputDescriptor;
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
/*    */ public class PdfInputDescriptor
/*    */   extends InputDescriptor
/*    */ {
/*    */   private PdfInputDescriptor() {}
/*    */   
/*    */   public static class Builder
/*    */     extends InputDescriptor.Builder
/*    */   {
/*    */     public Builder() {
/* 41 */       super(".pdf");
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public PdfInputDescriptor build() throws IOException {
/* 47 */       return (PdfInputDescriptor)super.build();
/*    */     }
/*    */ 
/*    */     
/*    */     protected InputDescriptor createDescriptor() {
/* 52 */       return new PdfInputDescriptor();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/PdfInputDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */