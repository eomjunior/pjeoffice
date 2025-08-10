/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ExpandableBuffer
/*     */ {
/*     */   private Mode mode;
/*     */   private ByteBuffer buffer;
/*     */   
/*     */   public enum Mode
/*     */   {
/*  47 */     INPUT, OUTPUT;
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
/*     */   protected ExpandableBuffer(int bufferSize) {
/*  63 */     this.buffer = ByteBuffer.allocate(bufferSize);
/*  64 */     this.mode = Mode.INPUT;
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
/*     */   protected Mode mode() {
/*  77 */     return this.mode;
/*     */   }
/*     */   
/*     */   protected ByteBuffer buffer() {
/*  81 */     return this.buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOutputMode() {
/*  88 */     if (this.mode != Mode.OUTPUT) {
/*  89 */       this.buffer.flip();
/*  90 */       this.mode = Mode.OUTPUT;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInputMode() {
/*  98 */     if (this.mode != Mode.INPUT) {
/*  99 */       if (this.buffer.hasRemaining()) {
/* 100 */         this.buffer.compact();
/*     */       } else {
/* 102 */         this.buffer.clear();
/*     */       } 
/* 104 */       this.mode = Mode.INPUT;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void expandCapacity(int capacity) {
/* 109 */     ByteBuffer oldBuffer = this.buffer;
/* 110 */     this.buffer = ByteBuffer.allocate(capacity);
/* 111 */     oldBuffer.flip();
/* 112 */     this.buffer.put(oldBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void expand() throws BufferOverflowException {
/* 121 */     int newcapacity = this.buffer.capacity() + 1 << 1;
/* 122 */     if (newcapacity < 0) {
/* 123 */       int vmBytes = 8;
/* 124 */       int javaBytes = 8;
/*     */       
/* 126 */       int headRoom = Math.max(8, 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       newcapacity = Integer.MAX_VALUE - headRoom;
/*     */       
/* 137 */       if (newcapacity <= this.buffer.capacity()) {
/* 138 */         throw new BufferOverflowException();
/*     */       }
/*     */     } 
/* 141 */     expandCapacity(newcapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void ensureCapacity(int requiredCapacity) {
/* 150 */     if (requiredCapacity > this.buffer.capacity()) {
/* 151 */       expandCapacity(requiredCapacity);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void ensureAdjustedCapacity(int requiredCapacity) {
/* 161 */     if (requiredCapacity > this.buffer.capacity()) {
/* 162 */       int adjustedCapacity = (requiredCapacity >> 10) + 1 << 10;
/* 163 */       expandCapacity(adjustedCapacity);
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
/*     */   protected boolean hasData() {
/* 177 */     setOutputMode();
/* 178 */     return this.buffer.hasRemaining();
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
/*     */   protected int length() {
/* 190 */     setOutputMode();
/* 191 */     return this.buffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int capacity() {
/* 200 */     setInputMode();
/* 201 */     return this.buffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void clear() {
/* 211 */     this.buffer.clear();
/* 212 */     this.mode = Mode.INPUT;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 217 */     StringBuilder sb = new StringBuilder();
/* 218 */     sb.append("[mode=");
/* 219 */     sb.append(this.mode);
/* 220 */     sb.append(" pos=");
/* 221 */     sb.append(this.buffer.position());
/* 222 */     sb.append(" lim=");
/* 223 */     sb.append(this.buffer.limit());
/* 224 */     sb.append(" cap=");
/* 225 */     sb.append(this.buffer.capacity());
/* 226 */     sb.append("]");
/* 227 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ExpandableBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */