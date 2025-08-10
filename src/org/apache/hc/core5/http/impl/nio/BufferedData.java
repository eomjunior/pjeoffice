/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
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
/*     */ public class BufferedData
/*     */   extends ExpandableBuffer
/*     */ {
/*     */   public static BufferedData allocate(int bufferSize) {
/*  48 */     return new BufferedData(bufferSize);
/*     */   }
/*     */   
/*     */   protected BufferedData(int bufferSize) {
/*  52 */     super(bufferSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasData() {
/*  57 */     return super.hasData();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int length() {
/*  62 */     return super.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int capacity() {
/*  67 */     return super.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/*  72 */     super.clear();
/*     */   }
/*     */   
/*     */   public final void put(ByteBuffer src) {
/*  76 */     Args.notNull(src, "Data source");
/*  77 */     setInputMode();
/*  78 */     int requiredCapacity = buffer().position() + src.remaining();
/*  79 */     ensureAdjustedCapacity(requiredCapacity);
/*  80 */     buffer().put(src);
/*     */   }
/*     */   
/*     */   public final int readFrom(ReadableByteChannel channel) throws IOException {
/*  84 */     Args.notNull(channel, "Channel");
/*  85 */     setInputMode();
/*  86 */     if (!buffer().hasRemaining()) {
/*  87 */       expand();
/*     */     }
/*  89 */     return channel.read(buffer());
/*     */   }
/*     */   
/*     */   public final int writeTo(WritableByteChannel dst) throws IOException {
/*  93 */     if (dst == null) {
/*  94 */       return 0;
/*     */     }
/*  96 */     setOutputMode();
/*  97 */     return dst.write(buffer());
/*     */   }
/*     */   
/*     */   public final ByteBuffer data() {
/* 101 */     setOutputMode();
/* 102 */     return buffer();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/BufferedData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */