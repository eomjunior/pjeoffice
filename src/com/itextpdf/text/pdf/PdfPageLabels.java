/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.factories.RomanAlphabetFactory;
/*     */ import com.itextpdf.text.factories.RomanNumberFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPageLabels
/*     */ {
/*     */   public static final int DECIMAL_ARABIC_NUMERALS = 0;
/*     */   public static final int UPPERCASE_ROMAN_NUMERALS = 1;
/*     */   public static final int LOWERCASE_ROMAN_NUMERALS = 2;
/*     */   public static final int UPPERCASE_LETTERS = 3;
/*     */   public static final int LOWERCASE_LETTERS = 4;
/*     */   public static final int EMPTY = 5;
/*  84 */   static PdfName[] numberingStyle = new PdfName[] { PdfName.D, PdfName.R, new PdfName("r"), PdfName.A, new PdfName("a") };
/*     */ 
/*     */ 
/*     */   
/*     */   private HashMap<Integer, PdfDictionary> map;
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPageLabels() {
/*  93 */     this.map = new HashMap<Integer, PdfDictionary>();
/*  94 */     addPageLabel(1, 0, null, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageLabel(int page, int numberStyle, String text, int firstPage) {
/* 104 */     if (page < 1 || firstPage < 1)
/* 105 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.a.page.label.the.page.numbers.must.be.greater.or.equal.to.1", new Object[0])); 
/* 106 */     PdfDictionary dic = new PdfDictionary();
/* 107 */     if (numberStyle >= 0 && numberStyle < numberingStyle.length)
/* 108 */       dic.put(PdfName.S, numberingStyle[numberStyle]); 
/* 109 */     if (text != null) {
/* 110 */       dic.put(PdfName.P, new PdfString(text, "UnicodeBig"));
/*     */     }
/* 112 */     if (firstPage != 1)
/* 113 */       dic.put(PdfName.ST, new PdfNumber(firstPage)); 
/* 114 */     this.map.put(Integer.valueOf(page - 1), dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageLabel(int page, int numberStyle, String text, int firstPage, boolean includeFirstPage) {
/* 126 */     if (page < 1 || firstPage < 1)
/* 127 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("in.a.page.label.the.page.numbers.must.be.greater.or.equal.to.1", new Object[0])); 
/* 128 */     PdfDictionary dic = new PdfDictionary();
/* 129 */     if (numberStyle >= 0 && numberStyle < numberingStyle.length)
/* 130 */       dic.put(PdfName.S, numberingStyle[numberStyle]); 
/* 131 */     if (text != null)
/* 132 */       dic.put(PdfName.P, new PdfString(text, "UnicodeBig")); 
/* 133 */     if (firstPage != 1 || includeFirstPage)
/* 134 */       dic.put(PdfName.ST, new PdfNumber(firstPage)); 
/* 135 */     this.map.put(Integer.valueOf(page - 1), dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageLabel(int page, int numberStyle, String text) {
/* 145 */     addPageLabel(page, numberStyle, text, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageLabel(int page, int numberStyle) {
/* 154 */     addPageLabel(page, numberStyle, null, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageLabel(PdfPageLabelFormat format) {
/* 160 */     addPageLabel(format.physicalPage, format.numberStyle, format.prefix, format.logicalPage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePageLabel(int page) {
/* 167 */     if (page <= 1)
/*     */       return; 
/* 169 */     this.map.remove(Integer.valueOf(page - 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getDictionary(PdfWriter writer) {
/*     */     try {
/* 177 */       return PdfNumberTree.writeTree(this.map, writer);
/*     */     }
/* 179 */     catch (IOException e) {
/* 180 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getPageLabels(PdfReader reader) {
/* 190 */     int n = reader.getNumberOfPages();
/*     */     
/* 192 */     PdfDictionary dict = reader.getCatalog();
/* 193 */     PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObjectRelease(dict.get(PdfName.PAGELABELS));
/*     */     
/* 195 */     if (labels == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     String[] labelstrings = new String[n];
/*     */     
/* 200 */     HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);
/*     */     
/* 202 */     int pagecount = 1;
/*     */     
/* 204 */     String prefix = "";
/* 205 */     char type = 'D';
/* 206 */     for (int i = 0; i < n; i++) {
/* 207 */       Integer current = Integer.valueOf(i);
/* 208 */       if (numberTree.containsKey(current)) {
/* 209 */         PdfDictionary d = (PdfDictionary)PdfReader.getPdfObjectRelease(numberTree.get(current));
/* 210 */         if (d.contains(PdfName.ST)) {
/* 211 */           pagecount = ((PdfNumber)d.get(PdfName.ST)).intValue();
/*     */         } else {
/*     */           
/* 214 */           pagecount = 1;
/*     */         } 
/* 216 */         if (d.contains(PdfName.P)) {
/* 217 */           prefix = ((PdfString)d.get(PdfName.P)).toUnicodeString();
/*     */         } else {
/*     */           
/* 220 */           prefix = "";
/*     */         } 
/* 222 */         if (d.contains(PdfName.S)) {
/* 223 */           type = ((PdfName)d.get(PdfName.S)).toString().charAt(1);
/*     */         } else {
/*     */           
/* 226 */           type = 'e';
/*     */         } 
/*     */       } 
/* 229 */       switch (type) {
/*     */         default:
/* 231 */           labelstrings[i] = prefix + pagecount;
/*     */           break;
/*     */         case 'R':
/* 234 */           labelstrings[i] = prefix + RomanNumberFactory.getUpperCaseString(pagecount);
/*     */           break;
/*     */         case 'r':
/* 237 */           labelstrings[i] = prefix + RomanNumberFactory.getLowerCaseString(pagecount);
/*     */           break;
/*     */         case 'A':
/* 240 */           labelstrings[i] = prefix + RomanAlphabetFactory.getUpperCaseString(pagecount);
/*     */           break;
/*     */         case 'a':
/* 243 */           labelstrings[i] = prefix + RomanAlphabetFactory.getLowerCaseString(pagecount);
/*     */           break;
/*     */         case 'e':
/* 246 */           labelstrings[i] = prefix;
/*     */           break;
/*     */       } 
/* 249 */       pagecount++;
/*     */     } 
/* 251 */     return labelstrings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfPageLabelFormat[] getPageLabelFormats(PdfReader reader) {
/* 261 */     PdfDictionary dict = reader.getCatalog();
/* 262 */     PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObjectRelease(dict.get(PdfName.PAGELABELS));
/* 263 */     if (labels == null)
/* 264 */       return null; 
/* 265 */     HashMap<Integer, PdfObject> numberTree = PdfNumberTree.readTree(labels);
/* 266 */     Integer[] numbers = new Integer[numberTree.size()];
/* 267 */     numbers = (Integer[])numberTree.keySet().toArray((Object[])numbers);
/* 268 */     Arrays.sort((Object[])numbers);
/* 269 */     PdfPageLabelFormat[] formats = new PdfPageLabelFormat[numberTree.size()];
/*     */ 
/*     */ 
/*     */     
/* 273 */     for (int k = 0; k < numbers.length; k++) {
/* 274 */       String prefix; int numberStyle, pagecount; Integer key = numbers[k];
/* 275 */       PdfDictionary d = (PdfDictionary)PdfReader.getPdfObjectRelease(numberTree.get(key));
/* 276 */       if (d.contains(PdfName.ST)) {
/* 277 */         pagecount = ((PdfNumber)d.get(PdfName.ST)).intValue();
/*     */       } else {
/* 279 */         pagecount = 1;
/*     */       } 
/* 281 */       if (d.contains(PdfName.P)) {
/* 282 */         prefix = ((PdfString)d.get(PdfName.P)).toUnicodeString();
/*     */       } else {
/* 284 */         prefix = "";
/*     */       } 
/* 286 */       if (d.contains(PdfName.S)) {
/* 287 */         char type = ((PdfName)d.get(PdfName.S)).toString().charAt(1);
/* 288 */         switch (type) { case 'R':
/* 289 */             numberStyle = 1; break;
/* 290 */           case 'r': numberStyle = 2; break;
/* 291 */           case 'A': numberStyle = 3; break;
/* 292 */           case 'a': numberStyle = 4; break;
/* 293 */           default: numberStyle = 0; break; }
/*     */       
/*     */       } else {
/* 296 */         numberStyle = 5;
/*     */       } 
/* 298 */       formats[k] = new PdfPageLabelFormat(key.intValue() + 1, numberStyle, prefix, pagecount);
/*     */     } 
/* 300 */     return formats;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PdfPageLabelFormat
/*     */   {
/*     */     public int physicalPage;
/*     */     
/*     */     public int numberStyle;
/*     */     
/*     */     public String prefix;
/*     */     
/*     */     public int logicalPage;
/*     */ 
/*     */     
/*     */     public PdfPageLabelFormat(int physicalPage, int numberStyle, String prefix, int logicalPage) {
/* 317 */       this.physicalPage = physicalPage;
/* 318 */       this.numberStyle = numberStyle;
/* 319 */       this.prefix = prefix;
/* 320 */       this.logicalPage = logicalPage;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 325 */       return String.format("Physical page %s: style: %s; prefix '%s'; logical page: %s", new Object[] { Integer.valueOf(this.physicalPage), Integer.valueOf(this.numberStyle), this.prefix, Integer.valueOf(this.logicalPage) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPageLabels.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */