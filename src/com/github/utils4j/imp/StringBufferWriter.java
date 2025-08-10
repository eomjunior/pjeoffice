/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.io.Writer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringBufferWriter
/*    */   extends Writer
/*    */   implements Serializable
/*    */ {
/*    */   private final StringBuffer buffer;
/*    */   
/*    */   public StringBufferWriter() {
/* 37 */     this.buffer = new StringBuffer();
/*    */   }
/*    */   
/*    */   public StringBufferWriter(int capacity) {
/* 41 */     this.buffer = new StringBuffer(capacity);
/*    */   }
/*    */   
/*    */   public StringBufferWriter(StringBuffer buffer) {
/* 45 */     this.buffer = (buffer != null) ? buffer : new StringBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer append(char value) {
/* 50 */     this.buffer.append(value);
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer append(CharSequence value) {
/* 56 */     this.buffer.append(value);
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Writer append(CharSequence value, int start, int end) {
/* 62 */     this.buffer.append(value, start, end);
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() {}
/*    */ 
/*    */   
/*    */   public void write(String value) {
/* 76 */     if (value != null) {
/* 77 */       this.buffer.append(value);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(char[] value, int offset, int length) {
/* 83 */     if (value != null) {
/* 84 */       this.buffer.append(value, offset, length);
/*    */     }
/*    */   }
/*    */   
/*    */   public StringBuffer getBuilder() {
/* 89 */     return this.buffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return this.buffer.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/StringBufferWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */