/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.io.HttpMessageWriter;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.http.message.BasicLineFormatter;
/*     */ import org.apache.hc.core5.http.message.LineFormatter;
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
/*     */   implements HttpMessageWriter<T>
/*     */ {
/*     */   private final CharArrayBuffer lineBuf;
/*     */   private final LineFormatter lineFormatter;
/*     */   
/*     */   public AbstractMessageWriter(LineFormatter formatter) {
/*  66 */     this.lineFormatter = (formatter != null) ? formatter : (LineFormatter)BasicLineFormatter.INSTANCE;
/*  67 */     this.lineBuf = new CharArrayBuffer(128);
/*     */   }
/*     */   
/*     */   LineFormatter getLineFormatter() {
/*  71 */     return this.lineFormatter;
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
/*     */   public void write(T message, SessionOutputBuffer buffer, OutputStream outputStream) throws IOException, HttpException {
/*  86 */     Args.notNull(message, "HTTP message");
/*  87 */     Args.notNull(buffer, "Session output buffer");
/*  88 */     Args.notNull(outputStream, "Output stream");
/*  89 */     writeHeadLine(message, this.lineBuf);
/*  90 */     buffer.writeLine(this.lineBuf, outputStream);
/*  91 */     for (Iterator<Header> it = message.headerIterator(); it.hasNext(); ) {
/*  92 */       Header header = it.next();
/*  93 */       if (header instanceof FormattedHeader) {
/*  94 */         CharArrayBuffer chbuffer = ((FormattedHeader)header).getBuffer();
/*  95 */         buffer.writeLine(chbuffer, outputStream); continue;
/*     */       } 
/*  97 */       this.lineBuf.clear();
/*  98 */       this.lineFormatter.formatHeader(this.lineBuf, header);
/*  99 */       buffer.writeLine(this.lineBuf, outputStream);
/*     */     } 
/*     */     
/* 102 */     this.lineBuf.clear();
/* 103 */     buffer.writeLine(this.lineBuf, outputStream);
/*     */   }
/*     */   
/*     */   protected abstract void writeHeadLine(T paramT, CharArrayBuffer paramCharArrayBuffer) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/AbstractMessageWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */