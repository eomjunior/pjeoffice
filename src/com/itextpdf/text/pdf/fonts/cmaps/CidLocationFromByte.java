/*    */ package com.itextpdf.text.pdf.fonts.cmaps;
/*    */ 
/*    */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*    */ import com.itextpdf.text.pdf.PRTokeniser;
/*    */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CidLocationFromByte
/*    */   implements CidLocation
/*    */ {
/*    */   private byte[] data;
/*    */   
/*    */   public CidLocationFromByte(byte[] data) {
/* 60 */     this.data = data;
/*    */   }
/*    */   
/*    */   public PRTokeniser getLocation(String location) throws IOException {
/* 64 */     return new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(this.data)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CidLocationFromByte.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */