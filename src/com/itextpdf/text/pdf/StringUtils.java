/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtils
/*     */ {
/*  50 */   private static final byte[] r = DocWriter.getISOBytes("\\r");
/*  51 */   private static final byte[] n = DocWriter.getISOBytes("\\n");
/*  52 */   private static final byte[] t = DocWriter.getISOBytes("\\t");
/*  53 */   private static final byte[] b = DocWriter.getISOBytes("\\b");
/*  54 */   private static final byte[] f = DocWriter.getISOBytes("\\f");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] escapeString(byte[] bytes) {
/*  67 */     ByteBuffer content = new ByteBuffer();
/*  68 */     escapeString(bytes, content);
/*  69 */     return content.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void escapeString(byte[] bytes, ByteBuffer content) {
/*  79 */     content.append_i(40);
/*  80 */     for (int k = 0; k < bytes.length; k++) {
/*  81 */       byte c = bytes[k];
/*  82 */       switch (c) {
/*     */         case 13:
/*  84 */           content.append(r);
/*     */           break;
/*     */         case 10:
/*  87 */           content.append(n);
/*     */           break;
/*     */         case 9:
/*  90 */           content.append(t);
/*     */           break;
/*     */         case 8:
/*  93 */           content.append(b);
/*     */           break;
/*     */         case 12:
/*  96 */           content.append(f);
/*     */           break;
/*     */         case 40:
/*     */         case 41:
/*     */         case 92:
/* 101 */           content.append_i(92).append_i(c);
/*     */           break;
/*     */         default:
/* 104 */           content.append_i(c); break;
/*     */       } 
/*     */     } 
/* 107 */     content.append_i(41);
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
/*     */   public static byte[] convertCharsToBytes(char[] chars) {
/* 119 */     byte[] result = new byte[chars.length * 2];
/* 120 */     for (int i = 0; i < chars.length; i++) {
/* 121 */       result[2 * i] = (byte)(chars[i] / 256);
/* 122 */       result[2 * i + 1] = (byte)(chars[i] % 256);
/*     */     } 
/* 124 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/StringUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */