/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class CMapToUnicode
/*     */   extends AbstractCMap
/*     */ {
/*  51 */   private Map<Integer, String> singleByteMappings = new HashMap<Integer, String>();
/*  52 */   private Map<Integer, String> doubleByteMappings = new HashMap<Integer, String>();
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
/*     */   public boolean hasOneByteMappings() {
/*  67 */     return !this.singleByteMappings.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasTwoByteMappings() {
/*  76 */     return !this.doubleByteMappings.isEmpty();
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
/*     */ 
/*     */   
/*     */   public String lookup(byte[] code, int offset, int length) {
/*  90 */     String result = null;
/*  91 */     Integer key = null;
/*  92 */     if (length == 1) {
/*     */       
/*  94 */       key = Integer.valueOf(code[offset] & 0xFF);
/*  95 */       result = this.singleByteMappings.get(key);
/*  96 */     } else if (length == 2) {
/*  97 */       int intKey = code[offset] & 0xFF;
/*  98 */       intKey <<= 8;
/*  99 */       intKey += code[offset + 1] & 0xFF;
/* 100 */       key = Integer.valueOf(intKey);
/*     */       
/* 102 */       result = this.doubleByteMappings.get(key);
/*     */     } 
/*     */     
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   public Map<Integer, Integer> createReverseMapping() throws IOException {
/* 109 */     Map<Integer, Integer> result = new HashMap<Integer, Integer>();
/* 110 */     for (Map.Entry<Integer, String> entry : this.singleByteMappings.entrySet()) {
/* 111 */       result.put(Integer.valueOf(convertToInt(entry.getValue())), entry.getKey());
/*     */     }
/* 113 */     for (Map.Entry<Integer, String> entry : this.doubleByteMappings.entrySet()) {
/* 114 */       result.put(Integer.valueOf(convertToInt(entry.getValue())), entry.getKey());
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   public Map<Integer, Integer> createDirectMapping() throws IOException {
/* 120 */     Map<Integer, Integer> result = new HashMap<Integer, Integer>();
/* 121 */     for (Map.Entry<Integer, String> entry : this.singleByteMappings.entrySet()) {
/* 122 */       result.put(entry.getKey(), Integer.valueOf(convertToInt(entry.getValue())));
/*     */     }
/* 124 */     for (Map.Entry<Integer, String> entry : this.doubleByteMappings.entrySet()) {
/* 125 */       result.put(entry.getKey(), Integer.valueOf(convertToInt(entry.getValue())));
/*     */     }
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   private int convertToInt(String s) throws IOException {
/* 131 */     byte[] b = s.getBytes("UTF-16BE");
/* 132 */     int value = 0;
/* 133 */     for (int i = 0; i < b.length - 1; i++) {
/* 134 */       value += b[i] & 0xFF;
/* 135 */       value <<= 8;
/*     */     } 
/* 137 */     value += b[b.length - 1] & 0xFF;
/* 138 */     return value;
/*     */   }
/*     */   
/*     */   void addChar(int cid, String uni) {
/* 142 */     this.doubleByteMappings.put(Integer.valueOf(cid), uni);
/*     */   }
/*     */ 
/*     */   
/*     */   void addChar(PdfString mark, PdfObject code) {
/*     */     try {
/* 148 */       byte[] src = mark.getBytes();
/* 149 */       String dest = createStringFromBytes(code.getBytes());
/* 150 */       if (src.length == 1) {
/* 151 */         this.singleByteMappings.put(Integer.valueOf(src[0] & 0xFF), dest);
/* 152 */       } else if (src.length == 2) {
/* 153 */         int intSrc = src[0] & 0xFF;
/* 154 */         intSrc <<= 8;
/* 155 */         intSrc |= src[1] & 0xFF;
/* 156 */         this.doubleByteMappings.put(Integer.valueOf(intSrc), dest);
/*     */       } else {
/* 158 */         throw new IOException(MessageLocalization.getComposedMessage("mapping.code.should.be.1.or.two.bytes.and.not.1", src.length));
/*     */       }
/*     */     
/* 161 */     } catch (Exception ex) {
/* 162 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String createStringFromBytes(byte[] bytes) throws IOException {
/* 167 */     String retval = null;
/* 168 */     if (bytes.length == 1) {
/* 169 */       retval = new String(bytes);
/*     */     } else {
/* 171 */       retval = new String(bytes, "UTF-16BE");
/*     */     } 
/* 173 */     return retval;
/*     */   }
/*     */   
/*     */   public static CMapToUnicode getIdentity() {
/* 177 */     CMapToUnicode uni = new CMapToUnicode();
/* 178 */     for (int i = 0; i < 65537; i++) {
/* 179 */       uni.addChar(i, Utilities.convertFromUtf32(i));
/*     */     }
/* 181 */     return uni;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapToUnicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */