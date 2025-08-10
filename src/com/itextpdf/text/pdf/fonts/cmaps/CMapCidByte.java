/*    */ package com.itextpdf.text.pdf.fonts.cmaps;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfNumber;
/*    */ import com.itextpdf.text.pdf.PdfObject;
/*    */ import com.itextpdf.text.pdf.PdfString;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMapCidByte
/*    */   extends AbstractCMap
/*    */ {
/* 52 */   private HashMap<Integer, byte[]> map = (HashMap)new HashMap<Integer, byte>();
/* 53 */   private final byte[] EMPTY = new byte[0];
/*    */ 
/*    */   
/*    */   void addChar(PdfString mark, PdfObject code) {
/* 57 */     if (!(code instanceof PdfNumber))
/*    */       return; 
/* 59 */     byte[] ser = decodeStringToByte(mark);
/* 60 */     this.map.put(Integer.valueOf(((PdfNumber)code).intValue()), ser);
/*    */   }
/*    */   
/*    */   public byte[] lookup(int cid) {
/* 64 */     byte[] ser = this.map.get(Integer.valueOf(cid));
/* 65 */     if (ser == null) {
/* 66 */       return this.EMPTY;
/*    */     }
/* 68 */     return ser;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapCidByte.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */