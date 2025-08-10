/*    */ package org.apache.hc.core5.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.CharBuffer;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringAsyncEntityConsumer
/*    */   extends AbstractCharAsyncEntityConsumer<String>
/*    */ {
/*    */   private final int capacityIncrement;
/*    */   private final CharArrayBuffer content;
/*    */   
/*    */   public StringAsyncEntityConsumer(int bufSize, int capacityIncrement, CharCodingConfig charCodingConfig) {
/* 50 */     super(bufSize, charCodingConfig);
/* 51 */     this.capacityIncrement = Args.positive(capacityIncrement, "Capacity increment");
/* 52 */     this.content = new CharArrayBuffer(1024);
/*    */   }
/*    */   
/*    */   public StringAsyncEntityConsumer(int capacityIncrement) {
/* 56 */     this(8192, capacityIncrement, CharCodingConfig.DEFAULT);
/*    */   }
/*    */   
/*    */   public StringAsyncEntityConsumer(CharCodingConfig charCodingConfig) {
/* 60 */     this(8192, 2147483647, charCodingConfig);
/*    */   }
/*    */   
/*    */   public StringAsyncEntityConsumer() {
/* 64 */     this(2147483647);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void streamStart(ContentType contentType) throws HttpException, IOException {}
/*    */ 
/*    */   
/*    */   protected int capacityIncrement() {
/* 73 */     int available = this.content.capacity() - this.content.length();
/* 74 */     return Math.max(this.capacityIncrement, available);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void data(CharBuffer src, boolean endOfStream) {
/* 79 */     Args.notNull(src, "CharBuffer");
/* 80 */     int chunk = src.remaining();
/* 81 */     this.content.ensureCapacity(chunk);
/* 82 */     src.get(this.content.array(), this.content.length(), chunk);
/* 83 */     this.content.setLength(this.content.length() + chunk);
/*    */   }
/*    */ 
/*    */   
/*    */   public String generateContent() {
/* 88 */     return this.content.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseResources() {
/* 93 */     this.content.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/StringAsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */