/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UUEncoder
/*     */ {
/*     */   protected static final int DEFAULT_MODE = 644;
/*     */   private static final int MAX_CHARS_PER_LINE = 45;
/*     */   private static final int INPUT_BUFFER_SIZE = 4500;
/*     */   private OutputStream out;
/*     */   private String name;
/*     */   
/*     */   public UUEncoder(String name) {
/*  49 */     this.name = name;
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
/*     */   public void encode(InputStream is, OutputStream out) throws IOException {
/*  62 */     this.out = out;
/*  63 */     encodeBegin();
/*  64 */     byte[] buffer = new byte[4500];
/*     */     int count;
/*  66 */     while ((count = is.read(buffer, 0, buffer.length)) != -1) {
/*  67 */       int pos = 0;
/*  68 */       while (count > 0) {
/*     */ 
/*     */         
/*  71 */         int num = (count > 45) ? 45 : count;
/*  72 */         encodeLine(buffer, pos, num, out);
/*  73 */         pos += num;
/*  74 */         count -= num;
/*     */       } 
/*     */     } 
/*  77 */     out.flush();
/*  78 */     encodeEnd();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void encodeString(String n) {
/*  85 */     PrintStream writer = new PrintStream(this.out);
/*  86 */     writer.print(n);
/*  87 */     writer.flush();
/*     */   }
/*     */   
/*     */   private void encodeBegin() {
/*  91 */     encodeString("begin 644 " + this.name + "\n");
/*     */   }
/*     */   
/*     */   private void encodeEnd() {
/*  95 */     encodeString(" \nend\n");
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
/*     */   private void encodeLine(byte[] data, int offset, int length, OutputStream out) throws IOException {
/* 112 */     out.write((byte)((length & 0x3F) + 32));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     for (int i = 0; i < length; ) {
/*     */       
/* 120 */       byte b = 1;
/* 121 */       byte c = 1;
/*     */       
/* 123 */       byte a = data[offset + i++];
/* 124 */       if (i < length) {
/* 125 */         b = data[offset + i++];
/* 126 */         if (i < length) {
/* 127 */           c = data[offset + i++];
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 132 */       byte d1 = (byte)((a >>> 2 & 0x3F) + 32);
/* 133 */       byte d2 = (byte)((a << 4 & 0x30 | b >>> 4 & 0xF) + 32);
/* 134 */       byte d3 = (byte)((b << 2 & 0x3C | c >>> 6 & 0x3) + 32);
/* 135 */       byte d4 = (byte)((c & 0x3F) + 32);
/*     */ 
/*     */       
/* 138 */       out.write(d1);
/* 139 */       out.write(d2);
/* 140 */       out.write(d3);
/* 141 */       out.write(d4);
/*     */     } 
/*     */ 
/*     */     
/* 145 */     out.write(10);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/UUEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */