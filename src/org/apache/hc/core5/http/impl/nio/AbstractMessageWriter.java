/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.message.BasicLineFormatter;
/*     */ import org.apache.hc.core5.http.message.LineFormatter;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriter;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageWriter<T extends HttpMessage>
/*     */   implements NHttpMessageWriter<T>
/*     */ {
/*     */   private final CharArrayBuffer lineBuf;
/*     */   private final LineFormatter lineFormatter;
/*     */   
/*     */   public AbstractMessageWriter(LineFormatter formatter) {
/*  65 */     this.lineFormatter = (formatter != null) ? formatter : (LineFormatter)BasicLineFormatter.INSTANCE;
/*  66 */     this.lineBuf = new CharArrayBuffer(64);
/*     */   }
/*     */   
/*     */   LineFormatter getLineFormatter() {
/*  70 */     return this.lineFormatter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(T message, SessionOutputBuffer sessionBuffer) throws IOException, HttpException {
/*  86 */     Args.notNull(message, "HTTP message");
/*  87 */     Args.notNull(sessionBuffer, "Session output buffer");
/*     */     
/*  89 */     writeHeadLine(message, this.lineBuf);
/*  90 */     sessionBuffer.writeLine(this.lineBuf);
/*  91 */     for (Iterator<Header> it = message.headerIterator(); it.hasNext(); ) {
/*  92 */       Header header = it.next();
/*  93 */       if (header instanceof FormattedHeader) {
/*  94 */         CharArrayBuffer buffer = ((FormattedHeader)header).getBuffer();
/*  95 */         sessionBuffer.writeLine(buffer); continue;
/*     */       } 
/*  97 */       this.lineBuf.clear();
/*  98 */       this.lineFormatter.formatHeader(this.lineBuf, header);
/*  99 */       sessionBuffer.writeLine(this.lineBuf);
/*     */     } 
/*     */     
/* 102 */     this.lineBuf.clear();
/* 103 */     sessionBuffer.writeLine(this.lineBuf);
/*     */   }
/*     */   
/*     */   protected abstract void writeHeadLine(T paramT, CharArrayBuffer paramCharArrayBuffer) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */