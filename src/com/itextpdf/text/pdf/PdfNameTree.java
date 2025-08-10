/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
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
/*     */ public class PdfNameTree
/*     */ {
/*     */   private static final int leafSize = 64;
/*     */   
/*     */   public static PdfDictionary writeTree(HashMap<String, ? extends PdfObject> items, PdfWriter writer) throws IOException {
/*  71 */     if (items.isEmpty())
/*  72 */       return null; 
/*  73 */     String[] names = new String[items.size()];
/*  74 */     names = (String[])items.keySet().toArray((Object[])names);
/*  75 */     Arrays.sort((Object[])names);
/*  76 */     if (names.length <= 64) {
/*  77 */       PdfDictionary dic = new PdfDictionary();
/*  78 */       PdfArray ar = new PdfArray();
/*  79 */       for (int i = 0; i < names.length; i++) {
/*  80 */         ar.add(new PdfString(names[i], null));
/*  81 */         ar.add(items.get(names[i]));
/*     */       } 
/*  83 */       dic.put(PdfName.NAMES, ar);
/*  84 */       return dic;
/*     */     } 
/*  86 */     int skip = 64;
/*  87 */     PdfIndirectReference[] kids = new PdfIndirectReference[(names.length + 64 - 1) / 64];
/*  88 */     for (int k = 0; k < kids.length; k++) {
/*  89 */       int offset = k * 64;
/*  90 */       int end = Math.min(offset + 64, names.length);
/*  91 */       PdfDictionary dic = new PdfDictionary();
/*  92 */       PdfArray arr = new PdfArray();
/*  93 */       arr.add(new PdfString(names[offset], null));
/*  94 */       arr.add(new PdfString(names[end - 1], null));
/*  95 */       dic.put(PdfName.LIMITS, arr);
/*  96 */       arr = new PdfArray();
/*  97 */       for (; offset < end; offset++) {
/*  98 */         arr.add(new PdfString(names[offset], null));
/*  99 */         arr.add(items.get(names[offset]));
/*     */       } 
/* 101 */       dic.put(PdfName.NAMES, arr);
/* 102 */       kids[k] = writer.addToBody(dic).getIndirectReference();
/*     */     } 
/* 104 */     int top = kids.length;
/*     */     while (true) {
/* 106 */       if (top <= 64) {
/* 107 */         PdfArray arr = new PdfArray();
/* 108 */         for (int j = 0; j < top; j++)
/* 109 */           arr.add(kids[j]); 
/* 110 */         PdfDictionary dic = new PdfDictionary();
/* 111 */         dic.put(PdfName.KIDS, arr);
/* 112 */         return dic;
/*     */       } 
/* 114 */       skip *= 64;
/* 115 */       int tt = (names.length + skip - 1) / skip;
/* 116 */       for (int i = 0; i < tt; i++) {
/* 117 */         int offset = i * 64;
/* 118 */         int end = Math.min(offset + 64, top);
/* 119 */         PdfDictionary dic = new PdfDictionary();
/* 120 */         PdfArray arr = new PdfArray();
/* 121 */         arr.add(new PdfString(names[i * skip], null));
/* 122 */         arr.add(new PdfString(names[Math.min((i + 1) * skip, names.length) - 1], null));
/* 123 */         dic.put(PdfName.LIMITS, arr);
/* 124 */         arr = new PdfArray();
/* 125 */         for (; offset < end; offset++) {
/* 126 */           arr.add(kids[offset]);
/*     */         }
/* 128 */         dic.put(PdfName.KIDS, arr);
/* 129 */         kids[i] = writer.addToBody(dic).getIndirectReference();
/*     */       } 
/* 131 */       top = tt;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static PdfString iterateItems(PdfDictionary dic, HashMap<String, PdfObject> items, PdfString leftOverString) {
/* 136 */     PdfArray nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.NAMES));
/* 137 */     if (nn != null) {
/* 138 */       for (int k = 0; k < nn.size(); k++) {
/*     */         PdfString s;
/* 140 */         if (leftOverString == null) {
/* 141 */           s = (PdfString)PdfReader.getPdfObjectRelease(nn.getPdfObject(k++));
/*     */         } else {
/*     */           
/* 144 */           s = leftOverString;
/* 145 */           leftOverString = null;
/*     */         } 
/* 147 */         if (k < nn.size())
/* 148 */         { items.put(PdfEncodings.convertToString(s.getBytes(), null), nn.getPdfObject(k)); }
/*     */         else
/* 150 */         { return s; } 
/*     */       } 
/* 152 */     } else if ((nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS))) != null) {
/* 153 */       for (int k = 0; k < nn.size(); k++) {
/* 154 */         PdfDictionary kid = (PdfDictionary)PdfReader.getPdfObjectRelease(nn.getPdfObject(k));
/* 155 */         leftOverString = iterateItems(kid, items, leftOverString);
/*     */       } 
/*     */     } 
/* 158 */     return null;
/*     */   }
/*     */   
/*     */   public static HashMap<String, PdfObject> readTree(PdfDictionary dic) {
/* 162 */     HashMap<String, PdfObject> items = new HashMap<String, PdfObject>();
/* 163 */     if (dic != null)
/* 164 */       iterateItems(dic, items, null); 
/* 165 */     return items;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfNameTree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */