/*     */ package org.apache.hc.core5.net;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PercentCodec
/*     */ {
/*  43 */   static final BitSet GEN_DELIMS = new BitSet(256);
/*  44 */   static final BitSet SUB_DELIMS = new BitSet(256);
/*  45 */   static final BitSet UNRESERVED = new BitSet(256);
/*  46 */   static final BitSet URIC = new BitSet(256); private static final int RADIX = 16;
/*     */   
/*     */   static {
/*  49 */     GEN_DELIMS.set(58);
/*  50 */     GEN_DELIMS.set(47);
/*  51 */     GEN_DELIMS.set(63);
/*  52 */     GEN_DELIMS.set(35);
/*  53 */     GEN_DELIMS.set(91);
/*  54 */     GEN_DELIMS.set(93);
/*  55 */     GEN_DELIMS.set(64);
/*     */     
/*  57 */     SUB_DELIMS.set(33);
/*  58 */     SUB_DELIMS.set(36);
/*  59 */     SUB_DELIMS.set(38);
/*  60 */     SUB_DELIMS.set(39);
/*  61 */     SUB_DELIMS.set(40);
/*  62 */     SUB_DELIMS.set(41);
/*  63 */     SUB_DELIMS.set(42);
/*  64 */     SUB_DELIMS.set(43);
/*  65 */     SUB_DELIMS.set(44);
/*  66 */     SUB_DELIMS.set(59);
/*  67 */     SUB_DELIMS.set(61);
/*     */     int i;
/*  69 */     for (i = 97; i <= 122; i++) {
/*  70 */       UNRESERVED.set(i);
/*     */     }
/*  72 */     for (i = 65; i <= 90; i++) {
/*  73 */       UNRESERVED.set(i);
/*     */     }
/*     */     
/*  76 */     for (i = 48; i <= 57; i++) {
/*  77 */       UNRESERVED.set(i);
/*     */     }
/*  79 */     UNRESERVED.set(45);
/*  80 */     UNRESERVED.set(46);
/*  81 */     UNRESERVED.set(95);
/*  82 */     UNRESERVED.set(126);
/*  83 */     URIC.or(SUB_DELIMS);
/*  84 */     URIC.or(UNRESERVED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void encode(StringBuilder buf, CharSequence content, Charset charset, BitSet safechars, boolean blankAsPlus) {
/*  91 */     if (content == null) {
/*     */       return;
/*     */     }
/*  94 */     CharBuffer cb = CharBuffer.wrap(content);
/*  95 */     ByteBuffer bb = ((charset != null) ? charset : StandardCharsets.UTF_8).encode(cb);
/*  96 */     while (bb.hasRemaining()) {
/*  97 */       int b = bb.get() & 0xFF;
/*  98 */       if (safechars.get(b)) {
/*  99 */         buf.append((char)b); continue;
/* 100 */       }  if (blankAsPlus && b == 32) {
/* 101 */         buf.append("+"); continue;
/*     */       } 
/* 103 */       buf.append("%");
/* 104 */       char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/* 105 */       char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/* 106 */       buf.append(hex1);
/* 107 */       buf.append(hex2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void encode(StringBuilder buf, CharSequence content, Charset charset, boolean blankAsPlus) {
/* 113 */     encode(buf, content, charset, UNRESERVED, blankAsPlus);
/*     */   }
/*     */   
/*     */   public static void encode(StringBuilder buf, CharSequence content, Charset charset) {
/* 117 */     encode(buf, content, charset, UNRESERVED, false);
/*     */   }
/*     */   
/*     */   public static String encode(CharSequence content, Charset charset) {
/* 121 */     if (content == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     StringBuilder buf = new StringBuilder();
/* 125 */     encode(buf, content, charset, UNRESERVED, false);
/* 126 */     return buf.toString();
/*     */   }
/*     */   
/*     */   static String decode(CharSequence content, Charset charset, boolean plusAsBlank) {
/* 130 */     if (content == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     ByteBuffer bb = ByteBuffer.allocate(content.length());
/* 134 */     CharBuffer cb = CharBuffer.wrap(content);
/* 135 */     while (cb.hasRemaining()) {
/* 136 */       char c = cb.get();
/* 137 */       if (c == '%' && cb.remaining() >= 2) {
/* 138 */         char uc = cb.get();
/* 139 */         char lc = cb.get();
/* 140 */         int u = Character.digit(uc, 16);
/* 141 */         int l = Character.digit(lc, 16);
/* 142 */         if (u != -1 && l != -1) {
/* 143 */           bb.put((byte)((u << 4) + l)); continue;
/*     */         } 
/* 145 */         bb.put((byte)37);
/* 146 */         bb.put((byte)uc);
/* 147 */         bb.put((byte)lc); continue;
/*     */       } 
/* 149 */       if (plusAsBlank && c == '+') {
/* 150 */         bb.put((byte)32); continue;
/*     */       } 
/* 152 */       bb.put((byte)c);
/*     */     } 
/*     */     
/* 155 */     bb.flip();
/* 156 */     return ((charset != null) ? charset : StandardCharsets.UTF_8).decode(bb).toString();
/*     */   }
/*     */   
/*     */   public static String decode(CharSequence content, Charset charset) {
/* 160 */     return decode(content, charset, false);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/net/PercentCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */