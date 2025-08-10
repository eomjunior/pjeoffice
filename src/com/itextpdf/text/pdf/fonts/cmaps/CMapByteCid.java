/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMapByteCid
/*     */   extends AbstractCMap
/*     */ {
/*  53 */   private ArrayList<char[]> planes = (ArrayList)new ArrayList<char>();
/*     */   
/*     */   public CMapByteCid() {
/*  56 */     this.planes.add(new char[256]);
/*     */   }
/*     */ 
/*     */   
/*     */   void addChar(PdfString mark, PdfObject code) {
/*  61 */     if (!(code instanceof PdfNumber))
/*     */       return; 
/*  63 */     encodeSequence(decodeStringToByte(mark), (char)((PdfNumber)code).intValue());
/*     */   }
/*     */   
/*     */   private void encodeSequence(byte[] seqs, char cid) {
/*  67 */     int size = seqs.length - 1;
/*  68 */     int nextPlane = 0;
/*  69 */     for (int idx = 0; idx < size; idx++) {
/*  70 */       char[] arrayOfChar = this.planes.get(nextPlane);
/*  71 */       int i = seqs[idx] & 0xFF;
/*  72 */       char c1 = arrayOfChar[i];
/*  73 */       if (c1 != '\000' && (c1 & 0x8000) == 0)
/*  74 */         throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0])); 
/*  75 */       if (c1 == '\000') {
/*  76 */         this.planes.add(new char[256]);
/*  77 */         c1 = (char)(this.planes.size() - 1 | 0x8000);
/*  78 */         arrayOfChar[i] = c1;
/*     */       } 
/*  80 */       nextPlane = c1 & 0x7FFF;
/*     */     } 
/*  82 */     char[] plane = this.planes.get(nextPlane);
/*  83 */     int one = seqs[size] & 0xFF;
/*  84 */     char c = plane[one];
/*  85 */     if ((c & 0x8000) != 0)
/*  86 */       throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0])); 
/*  87 */     plane[one] = cid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int decodeSingle(CMapSequence seq) {
/*  96 */     int end = seq.off + seq.len;
/*  97 */     int currentPlane = 0;
/*  98 */     while (seq.off < end) {
/*  99 */       int one = seq.seq[seq.off++] & 0xFF;
/* 100 */       seq.len--;
/* 101 */       char[] plane = this.planes.get(currentPlane);
/* 102 */       int cid = plane[one];
/* 103 */       if ((cid & 0x8000) == 0) {
/* 104 */         return cid;
/*     */       }
/*     */       
/* 107 */       currentPlane = cid & 0x7FFF;
/*     */     } 
/* 109 */     return -1;
/*     */   }
/*     */   
/*     */   public String decodeSequence(CMapSequence seq) {
/* 113 */     StringBuilder sb = new StringBuilder();
/* 114 */     int cid = 0;
/* 115 */     while ((cid = decodeSingle(seq)) >= 0) {
/* 116 */       sb.append((char)cid);
/*     */     }
/* 118 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapByteCid.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */