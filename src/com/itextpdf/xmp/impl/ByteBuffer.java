/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteBuffer
/*     */ {
/*     */   private byte[] buffer;
/*     */   private int length;
/*  50 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer(int initialCapacity) {
/*  58 */     this.buffer = new byte[initialCapacity];
/*  59 */     this.length = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer(byte[] buffer) {
/*  68 */     this.buffer = buffer;
/*  69 */     this.length = buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer(byte[] buffer, int length) {
/*  79 */     if (length > buffer.length)
/*     */     {
/*  81 */       throw new ArrayIndexOutOfBoundsException("Valid length exceeds the buffer length.");
/*     */     }
/*  83 */     this.buffer = buffer;
/*  84 */     this.length = length;
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
/*     */   public ByteBuffer(InputStream in) throws IOException {
/*  97 */     int chunk = 16384;
/*  98 */     this.length = 0;
/*  99 */     this.buffer = new byte[chunk];
/*     */     
/*     */     int read;
/* 102 */     while ((read = in.read(this.buffer, this.length, chunk)) > 0) {
/*     */       
/* 104 */       this.length += read;
/* 105 */       if (read == chunk)
/*     */       {
/* 107 */         ensureCapacity(this.length + chunk);
/*     */       }
/*     */     } 
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
/*     */   public ByteBuffer(byte[] buffer, int offset, int length) {
/* 124 */     if (length > buffer.length - offset)
/*     */     {
/* 126 */       throw new ArrayIndexOutOfBoundsException("Valid length exceeds the buffer length.");
/*     */     }
/* 128 */     this.buffer = new byte[length];
/* 129 */     System.arraycopy(buffer, offset, this.buffer, 0, length);
/* 130 */     this.length = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getByteStream() {
/* 139 */     return new ByteArrayInputStream(this.buffer, 0, this.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 149 */     return this.length;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public byte byteAt(int index) {
/* 169 */     if (index < this.length)
/*     */     {
/* 171 */       return this.buffer[index];
/*     */     }
/*     */ 
/*     */     
/* 175 */     throw new IndexOutOfBoundsException("The index exceeds the valid buffer area");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int charAt(int index) {
/* 186 */     if (index < this.length)
/*     */     {
/* 188 */       return this.buffer[index] & 0xFF;
/*     */     }
/*     */ 
/*     */     
/* 192 */     throw new IndexOutOfBoundsException("The index exceeds the valid buffer area");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(byte b) {
/* 203 */     ensureCapacity(this.length + 1);
/* 204 */     this.buffer[this.length++] = b;
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
/*     */   public void append(byte[] bytes, int offset, int len) {
/* 217 */     ensureCapacity(this.length + len);
/* 218 */     System.arraycopy(bytes, offset, this.buffer, this.length, len);
/* 219 */     this.length += len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(byte[] bytes) {
/* 229 */     append(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(ByteBuffer anotherBuffer) {
/* 239 */     append(anotherBuffer.buffer, 0, anotherBuffer.length);
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
/*     */   public String getEncoding() {
/* 252 */     if (this.encoding == null)
/*     */     {
/*     */       
/* 255 */       if (this.length < 2) {
/*     */ 
/*     */         
/* 258 */         this.encoding = "UTF-8";
/*     */       }
/* 260 */       else if (this.buffer[0] == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 267 */         if (this.length < 4 || this.buffer[1] != 0)
/*     */         {
/* 269 */           this.encoding = "UTF-16BE";
/*     */         }
/* 271 */         else if ((this.buffer[2] & 0xFF) == 254 && (this.buffer[3] & 0xFF) == 255)
/*     */         {
/* 273 */           this.encoding = "UTF-32BE";
/*     */         }
/*     */         else
/*     */         {
/* 277 */           this.encoding = "UTF-32";
/*     */         }
/*     */       
/* 280 */       } else if ((this.buffer[0] & 0xFF) < 128) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         if (this.buffer[1] != 0)
/*     */         {
/* 288 */           this.encoding = "UTF-8";
/*     */         }
/* 290 */         else if (this.length < 4 || this.buffer[2] != 0)
/*     */         {
/* 292 */           this.encoding = "UTF-16LE";
/*     */         }
/*     */         else
/*     */         {
/* 296 */           this.encoding = "UTF-32LE";
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 307 */       else if ((this.buffer[0] & 0xFF) == 239) {
/*     */         
/* 309 */         this.encoding = "UTF-8";
/*     */       }
/* 311 */       else if ((this.buffer[0] & 0xFF) == 254) {
/*     */         
/* 313 */         this.encoding = "UTF-16";
/*     */       }
/* 315 */       else if (this.length < 4 || this.buffer[2] != 0) {
/*     */         
/* 317 */         this.encoding = "UTF-16";
/*     */       }
/*     */       else {
/*     */         
/* 321 */         this.encoding = "UTF-32";
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 326 */     return this.encoding;
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
/*     */   private void ensureCapacity(int requestedLength) {
/* 338 */     if (requestedLength > this.buffer.length) {
/*     */       
/* 340 */       byte[] oldBuf = this.buffer;
/* 341 */       this.buffer = new byte[oldBuf.length * 2];
/* 342 */       System.arraycopy(oldBuf, 0, this.buffer, 0, oldBuf.length);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/ByteBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */