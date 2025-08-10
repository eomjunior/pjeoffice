/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Latin1Converter
/*     */ {
/*     */   private static final int STATE_START = 0;
/*     */   private static final int STATE_UTF8CHAR = 11;
/*     */   
/*     */   public static ByteBuffer convert(ByteBuffer buffer) {
/*  87 */     if ("UTF-8".equals(buffer.getEncoding())) {
/*     */ 
/*     */       
/*  90 */       byte[] readAheadBuffer = new byte[8];
/*     */       
/*  92 */       int readAhead = 0;
/*     */       
/*  94 */       int expectedBytes = 0;
/*     */       
/*  96 */       ByteBuffer out = new ByteBuffer(buffer.length() * 4 / 3);
/*     */       
/*  98 */       int state = 0;
/*  99 */       for (int i = 0; i < buffer.length(); i++) {
/*     */         byte[] utf8;
/* 101 */         int b = buffer.charAt(i);
/*     */         
/* 103 */         switch (state) {
/*     */ 
/*     */           
/*     */           default:
/* 107 */             if (b < 127) {
/*     */               
/* 109 */               out.append((byte)b); break;
/*     */             } 
/* 111 */             if (b >= 192) {
/*     */ 
/*     */               
/* 114 */               expectedBytes = -1;
/* 115 */               int test = b;
/* 116 */               for (; expectedBytes < 8 && (test & 0x80) == 128; test <<= 1)
/*     */               {
/* 118 */                 expectedBytes++;
/*     */               }
/* 120 */               readAheadBuffer[readAhead++] = (byte)b;
/* 121 */               state = 11;
/*     */               
/*     */               break;
/*     */             } 
/*     */             
/* 126 */             utf8 = convertToUTF8((byte)b);
/* 127 */             out.append(utf8);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 11:
/* 132 */             if (expectedBytes > 0 && (b & 0xC0) == 128) {
/*     */ 
/*     */               
/* 135 */               readAheadBuffer[readAhead++] = (byte)b;
/* 136 */               expectedBytes--;
/*     */               
/* 138 */               if (expectedBytes == 0) {
/*     */                 
/* 140 */                 out.append(readAheadBuffer, 0, readAhead);
/* 141 */                 readAhead = 0;
/*     */                 
/* 143 */                 state = 0;
/*     */               } 
/*     */ 
/*     */               
/*     */               break;
/*     */             } 
/*     */             
/* 150 */             utf8 = convertToUTF8(readAheadBuffer[0]);
/* 151 */             out.append(utf8);
/*     */ 
/*     */             
/* 154 */             i -= readAhead;
/* 155 */             readAhead = 0;
/*     */             
/* 157 */             state = 0;
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } 
/* 164 */       if (state == 11)
/*     */       {
/* 166 */         for (int j = 0; j < readAhead; j++) {
/*     */           
/* 168 */           byte b = readAheadBuffer[j];
/* 169 */           byte[] utf8 = convertToUTF8(b);
/* 170 */           out.append(utf8);
/*     */         } 
/*     */       }
/*     */       
/* 174 */       return out;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 179 */     return buffer;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] convertToUTF8(byte ch) {
/* 196 */     int c = ch & 0xFF;
/*     */     
/*     */     try {
/* 199 */       if (c >= 128)
/*     */       {
/* 201 */         if (c == 129 || c == 141 || c == 143 || c == 144 || c == 157)
/*     */         {
/* 203 */           return new byte[] { 32 };
/*     */         }
/*     */ 
/*     */         
/* 207 */         return (new String(new byte[] { ch }, "cp1252")).getBytes("UTF-8");
/*     */       }
/*     */     
/* 210 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */ 
/*     */ 
/*     */     
/* 214 */     return new byte[] { ch };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/Latin1Converter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */