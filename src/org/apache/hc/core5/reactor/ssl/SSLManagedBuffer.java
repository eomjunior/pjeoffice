/*     */ package org.apache.hc.core5.reactor.ssl;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SSLManagedBuffer
/*     */ {
/*     */   abstract ByteBuffer acquire();
/*     */   
/*     */   abstract void release();
/*     */   
/*     */   abstract boolean isAcquired();
/*     */   
/*     */   abstract boolean hasData();
/*     */   
/*     */   static SSLManagedBuffer create(SSLBufferMode mode, int size) {
/*  61 */     return (mode == SSLBufferMode.DYNAMIC) ? new DynamicBuffer(size) : new StaticBuffer(size);
/*     */   }
/*     */   
/*     */   static final class StaticBuffer
/*     */     extends SSLManagedBuffer {
/*     */     private final ByteBuffer buffer;
/*     */     
/*     */     public StaticBuffer(int size) {
/*  69 */       Args.positive(size, "size");
/*  70 */       this.buffer = ByteBuffer.allocate(size);
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer acquire() {
/*  75 */       return this.buffer;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void release() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isAcquired() {
/*  85 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasData() {
/*  90 */       return (this.buffer.position() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DynamicBuffer
/*     */     extends SSLManagedBuffer
/*     */   {
/*     */     private ByteBuffer wrapped;
/*     */     private final int length;
/*     */     
/*     */     public DynamicBuffer(int size) {
/* 101 */       Args.positive(size, "size");
/* 102 */       this.length = size;
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBuffer acquire() {
/* 107 */       if (this.wrapped != null) {
/* 108 */         return this.wrapped;
/*     */       }
/* 110 */       this.wrapped = ByteBuffer.allocate(this.length);
/* 111 */       return this.wrapped;
/*     */     }
/*     */ 
/*     */     
/*     */     public void release() {
/* 116 */       this.wrapped = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquired() {
/* 121 */       return (this.wrapped != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasData() {
/* 126 */       return (this.wrapped != null && this.wrapped.position() > 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ssl/SSLManagedBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */