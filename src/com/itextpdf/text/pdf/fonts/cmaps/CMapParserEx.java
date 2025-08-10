/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PRTokeniser;
/*     */ import com.itextpdf.text.pdf.PdfContentParser;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMapParserEx
/*     */ {
/*  62 */   private static final PdfName CMAPNAME = new PdfName("CMapName");
/*     */   private static final String DEF = "def";
/*     */   private static final String ENDCIDRANGE = "endcidrange";
/*     */   private static final String ENDCIDCHAR = "endcidchar";
/*     */   private static final String ENDBFRANGE = "endbfrange";
/*     */   private static final String ENDBFCHAR = "endbfchar";
/*     */   private static final String USECMAP = "usecmap";
/*     */   private static final int MAXLEVEL = 10;
/*     */   
/*     */   public static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location) throws IOException {
/*  72 */     parseCid(cmapName, cmap, location, 0);
/*     */   }
/*     */   
/*     */   private static void parseCid(String cmapName, AbstractCMap cmap, CidLocation location, int level) throws IOException {
/*  76 */     if (level >= 10)
/*     */       return; 
/*  78 */     PRTokeniser inp = location.getLocation(cmapName);
/*     */     try {
/*  80 */       ArrayList<PdfObject> list = new ArrayList<PdfObject>();
/*  81 */       PdfContentParser cp = new PdfContentParser(inp);
/*  82 */       int maxExc = 50;
/*     */       while (true) {
/*     */         try {
/*  85 */           cp.parse(list);
/*     */         }
/*  87 */         catch (Exception ex) {
/*  88 */           if (--maxExc < 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*  92 */         if (list.isEmpty())
/*     */           break; 
/*  94 */         String last = ((PdfObject)list.get(list.size() - 1)).toString();
/*  95 */         if (level == 0 && list.size() == 3 && last.equals("def")) {
/*  96 */           PdfObject key = list.get(0);
/*  97 */           if (PdfName.REGISTRY.equals(key)) {
/*  98 */             cmap.setRegistry(((PdfObject)list.get(1)).toString()); continue;
/*  99 */           }  if (PdfName.ORDERING.equals(key)) {
/* 100 */             cmap.setOrdering(((PdfObject)list.get(1)).toString()); continue;
/* 101 */           }  if (CMAPNAME.equals(key)) {
/* 102 */             cmap.setName(((PdfObject)list.get(1)).toString()); continue;
/* 103 */           }  if (PdfName.SUPPLEMENT.equals(key))
/*     */             try {
/* 105 */               cmap.setSupplement(((PdfNumber)list.get(1)).intValue());
/*     */             }
/* 107 */             catch (Exception exception) {} 
/*     */           continue;
/*     */         } 
/* 110 */         if ((last.equals("endcidchar") || last.equals("endbfchar")) && list.size() >= 3) {
/* 111 */           int lmax = list.size() - 2;
/* 112 */           for (int k = 0; k < lmax; k += 2) {
/* 113 */             if (list.get(k) instanceof PdfString)
/* 114 */               cmap.addChar((PdfString)list.get(k), list.get(k + 1)); 
/*     */           } 
/*     */           continue;
/*     */         } 
/* 118 */         if ((last.equals("endcidrange") || last.equals("endbfrange")) && list.size() >= 4) {
/* 119 */           int lmax = list.size() - 3;
/* 120 */           for (int k = 0; k < lmax; k += 3) {
/* 121 */             if (list.get(k) instanceof PdfString && list.get(k + 1) instanceof PdfString)
/* 122 */               cmap.addRange((PdfString)list.get(k), (PdfString)list.get(k + 1), list.get(k + 2)); 
/*     */           } 
/*     */           continue;
/*     */         } 
/* 126 */         if (last.equals("usecmap") && list.size() == 2 && list.get(0) instanceof PdfName) {
/* 127 */           parseCid(PdfName.decodeName(((PdfObject)list.get(0)).toString()), cmap, location, level + 1);
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 132 */       inp.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void encodeSequence(int size, byte[] seqs, char cid, ArrayList<char[]> planes) {
/* 137 */     size--;
/* 138 */     int nextPlane = 0;
/* 139 */     for (int idx = 0; idx < size; idx++) {
/* 140 */       char[] arrayOfChar = planes.get(nextPlane);
/* 141 */       int i = seqs[idx] & 0xFF;
/* 142 */       char c1 = arrayOfChar[i];
/* 143 */       if (c1 != '\000' && (c1 & 0x8000) == 0)
/* 144 */         throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0])); 
/* 145 */       if (c1 == '\000') {
/* 146 */         planes.add(new char[256]);
/* 147 */         c1 = (char)(planes.size() - 1 | 0x8000);
/* 148 */         arrayOfChar[i] = c1;
/*     */       } 
/* 150 */       nextPlane = c1 & 0x7FFF;
/*     */     } 
/* 152 */     char[] plane = planes.get(nextPlane);
/* 153 */     int one = seqs[size] & 0xFF;
/* 154 */     char c = plane[one];
/* 155 */     if ((c & 0x8000) != 0)
/* 156 */       throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping", new Object[0])); 
/* 157 */     plane[one] = cid;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapParserEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */