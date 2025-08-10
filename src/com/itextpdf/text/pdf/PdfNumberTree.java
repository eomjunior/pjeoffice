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
/*     */ public class PdfNumberTree
/*     */ {
/*     */   private static final int leafSize = 64;
/*     */   
/*     */   public static <O extends PdfObject> PdfDictionary writeTree(HashMap<Integer, O> items, PdfWriter writer) throws IOException {
/*  67 */     if (items.isEmpty())
/*  68 */       return null; 
/*  69 */     Integer[] numbers = new Integer[items.size()];
/*  70 */     numbers = (Integer[])items.keySet().toArray((Object[])numbers);
/*  71 */     Arrays.sort((Object[])numbers);
/*  72 */     if (numbers.length <= 64) {
/*  73 */       PdfDictionary dic = new PdfDictionary();
/*  74 */       PdfArray ar = new PdfArray();
/*  75 */       for (int i = 0; i < numbers.length; i++) {
/*  76 */         ar.add(new PdfNumber(numbers[i].intValue()));
/*  77 */         ar.add((PdfObject)items.get(numbers[i]));
/*     */       } 
/*  79 */       dic.put(PdfName.NUMS, ar);
/*  80 */       return dic;
/*     */     } 
/*  82 */     int skip = 64;
/*  83 */     PdfIndirectReference[] kids = new PdfIndirectReference[(numbers.length + 64 - 1) / 64];
/*  84 */     for (int k = 0; k < kids.length; k++) {
/*  85 */       int offset = k * 64;
/*  86 */       int end = Math.min(offset + 64, numbers.length);
/*  87 */       PdfDictionary dic = new PdfDictionary();
/*  88 */       PdfArray arr = new PdfArray();
/*  89 */       arr.add(new PdfNumber(numbers[offset].intValue()));
/*  90 */       arr.add(new PdfNumber(numbers[end - 1].intValue()));
/*  91 */       dic.put(PdfName.LIMITS, arr);
/*  92 */       arr = new PdfArray();
/*  93 */       for (; offset < end; offset++) {
/*  94 */         arr.add(new PdfNumber(numbers[offset].intValue()));
/*  95 */         arr.add((PdfObject)items.get(numbers[offset]));
/*     */       } 
/*  97 */       dic.put(PdfName.NUMS, arr);
/*  98 */       kids[k] = writer.addToBody(dic).getIndirectReference();
/*     */     } 
/* 100 */     int top = kids.length;
/*     */     while (true) {
/* 102 */       if (top <= 64) {
/* 103 */         PdfArray arr = new PdfArray();
/* 104 */         for (int j = 0; j < top; j++)
/* 105 */           arr.add(kids[j]); 
/* 106 */         PdfDictionary dic = new PdfDictionary();
/* 107 */         dic.put(PdfName.KIDS, arr);
/* 108 */         return dic;
/*     */       } 
/* 110 */       skip *= 64;
/* 111 */       int tt = (numbers.length + skip - 1) / skip;
/* 112 */       for (int i = 0; i < tt; i++) {
/* 113 */         int offset = i * 64;
/* 114 */         int end = Math.min(offset + 64, top);
/* 115 */         PdfDictionary dic = new PdfDictionary();
/* 116 */         PdfArray arr = new PdfArray();
/* 117 */         arr.add(new PdfNumber(numbers[i * skip].intValue()));
/* 118 */         arr.add(new PdfNumber(numbers[Math.min((i + 1) * skip, numbers.length) - 1].intValue()));
/* 119 */         dic.put(PdfName.LIMITS, arr);
/* 120 */         arr = new PdfArray();
/* 121 */         for (; offset < end; offset++) {
/* 122 */           arr.add(kids[offset]);
/*     */         }
/* 124 */         dic.put(PdfName.KIDS, arr);
/* 125 */         kids[i] = writer.addToBody(dic).getIndirectReference();
/*     */       } 
/* 127 */       top = tt;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void iterateItems(PdfDictionary dic, HashMap<Integer, PdfObject> items) {
/* 132 */     PdfArray nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.NUMS));
/* 133 */     if (nn != null) {
/* 134 */       for (int k = 0; k < nn.size(); k++) {
/* 135 */         PdfNumber s = (PdfNumber)PdfReader.getPdfObjectRelease(nn.getPdfObject(k++));
/* 136 */         items.put(Integer.valueOf(s.intValue()), nn.getPdfObject(k));
/*     */       }
/*     */     
/* 139 */     } else if ((nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS))) != null) {
/* 140 */       for (int k = 0; k < nn.size(); k++) {
/* 141 */         PdfDictionary kid = (PdfDictionary)PdfReader.getPdfObjectRelease(nn.getPdfObject(k));
/* 142 */         iterateItems(kid, items);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static HashMap<Integer, PdfObject> readTree(PdfDictionary dic) {
/* 148 */     HashMap<Integer, PdfObject> items = new HashMap<Integer, PdfObject>();
/* 149 */     if (dic != null)
/* 150 */       iterateItems(dic, items); 
/* 151 */     return items;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfNumberTree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */