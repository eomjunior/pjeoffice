/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ public class Wire
/*     */ {
/*     */   private static final int MAX_STRING_BUILDER_SIZE = 2048;
/*  41 */   private static final ThreadLocal<StringBuilder> THREAD_LOCAL = new ThreadLocal<>();
/*     */   
/*     */   private final Logger log;
/*     */   
/*     */   private final String id;
/*     */ 
/*     */   
/*     */   private static StringBuilder getStringBuilder() {
/*  49 */     StringBuilder result = THREAD_LOCAL.get();
/*  50 */     if (result == null) {
/*  51 */       result = new StringBuilder(2048);
/*  52 */       THREAD_LOCAL.set(result);
/*     */     } 
/*  54 */     trimToMaxSize(result, 2048);
/*  55 */     result.setLength(0);
/*  56 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void trimToMaxSize(StringBuilder stringBuilder, int maxSize) {
/*  67 */     if (stringBuilder != null && stringBuilder.capacity() > maxSize) {
/*  68 */       stringBuilder.setLength(maxSize);
/*  69 */       stringBuilder.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Wire(Logger log, String id) {
/*  78 */     this.log = log;
/*  79 */     this.id = id;
/*     */   }
/*     */   
/*     */   private void wire(String header, byte[] b, int pos, int off) {
/*  83 */     StringBuilder buffer = getStringBuilder();
/*  84 */     for (int i = 0; i < off; i++) {
/*  85 */       int ch = b[pos + i];
/*  86 */       if (ch == 13) {
/*  87 */         buffer.append("[\\r]");
/*  88 */       } else if (ch == 10) {
/*  89 */         buffer.append("[\\n]\"");
/*  90 */         buffer.insert(0, "\"");
/*  91 */         buffer.insert(0, header);
/*  92 */         this.log.debug("{} {}", this.id, buffer);
/*  93 */         buffer.setLength(0);
/*  94 */       } else if (ch < 32 || ch >= 127) {
/*  95 */         buffer.append("[0x");
/*  96 */         buffer.append(Integer.toHexString(ch));
/*  97 */         buffer.append("]");
/*     */       } else {
/*  99 */         buffer.append((char)ch);
/*     */       } 
/*     */     } 
/* 102 */     if (buffer.length() > 0) {
/* 103 */       buffer.append('"');
/* 104 */       buffer.insert(0, '"');
/* 105 */       buffer.insert(0, header);
/* 106 */       this.log.debug("{} {}", this.id, buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 112 */     return this.log.isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void output(byte[] b, int pos, int off) {
/* 116 */     Args.notNull(b, "Output");
/* 117 */     wire(">> ", b, pos, off);
/*     */   }
/*     */   
/*     */   public void input(byte[] b, int pos, int off) {
/* 121 */     Args.notNull(b, "Input");
/* 122 */     wire("<< ", b, pos, off);
/*     */   }
/*     */   
/*     */   public void output(byte[] b) {
/* 126 */     Args.notNull(b, "Output");
/* 127 */     output(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void input(byte[] b) {
/* 131 */     Args.notNull(b, "Input");
/* 132 */     input(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void output(int b) {
/* 136 */     output(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void input(int b) {
/* 140 */     input(new byte[] { (byte)b });
/*     */   }
/*     */   
/*     */   public void output(String s) {
/* 144 */     Args.notNull(s, "Output");
/* 145 */     output(s.getBytes());
/*     */   }
/*     */   
/*     */   public void input(String s) {
/* 149 */     Args.notNull(s, "Input");
/* 150 */     input(s.getBytes());
/*     */   }
/*     */   
/*     */   public void output(ByteBuffer b) {
/* 154 */     Args.notNull(b, "Output");
/* 155 */     if (b.hasArray()) {
/* 156 */       output(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*     */     } else {
/* 158 */       byte[] tmp = new byte[b.remaining()];
/* 159 */       b.get(tmp);
/* 160 */       output(tmp);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void input(ByteBuffer b) {
/* 165 */     Args.notNull(b, "Input");
/* 166 */     if (b.hasArray()) {
/* 167 */       input(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*     */     } else {
/* 169 */       byte[] tmp = new byte[b.remaining()];
/* 170 */       b.get(tmp);
/* 171 */       input(tmp);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/Wire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */