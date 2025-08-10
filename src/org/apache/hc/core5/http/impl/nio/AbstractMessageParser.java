/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.message.LazyLineParser;
/*     */ import org.apache.hc.core5.http.message.LineParser;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParser;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
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
/*     */ public abstract class AbstractMessageParser<T extends HttpMessage>
/*     */   implements NHttpMessageParser<T>
/*     */ {
/*     */   private State state;
/*     */   private T message;
/*     */   private CharArrayBuffer lineBuf;
/*     */   private final List<CharArrayBuffer> headerBufs;
/*     */   private int emptyLineCount;
/*     */   private final LineParser lineParser;
/*     */   private final Http1Config messageConstraints;
/*     */   
/*     */   private enum State
/*     */   {
/*  54 */     READ_HEAD_LINE, READ_HEADERS, COMPLETED;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractMessageParser(LineParser lineParser, Http1Config messageConstraints) {
/*  79 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLineParser.INSTANCE;
/*  80 */     this.messageConstraints = (messageConstraints != null) ? messageConstraints : Http1Config.DEFAULT;
/*  81 */     this.headerBufs = new ArrayList<>();
/*  82 */     this.state = State.READ_HEAD_LINE;
/*     */   }
/*     */   
/*     */   LineParser getLineParser() {
/*  86 */     return this.lineParser;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  91 */     this.state = State.READ_HEAD_LINE;
/*  92 */     this.headerBufs.clear();
/*  93 */     this.emptyLineCount = 0;
/*  94 */     this.message = null;
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
/*     */   private T parseHeadLine() throws IOException, HttpException {
/* 108 */     if (this.lineBuf.isEmpty()) {
/* 109 */       this.emptyLineCount++;
/* 110 */       if (this.emptyLineCount >= this.messageConstraints.getMaxEmptyLineCount()) {
/* 111 */         throw new MessageConstraintException("Maximum empty line limit exceeded");
/*     */       }
/* 113 */       return null;
/*     */     } 
/* 115 */     return createMessage(this.lineBuf);
/*     */   }
/*     */   
/*     */   private void parseHeader() throws IOException {
/* 119 */     CharArrayBuffer current = this.lineBuf;
/* 120 */     int count = this.headerBufs.size();
/* 121 */     if ((this.lineBuf.charAt(0) == ' ' || this.lineBuf.charAt(0) == '\t') && count > 0) {
/*     */       
/* 123 */       CharArrayBuffer previous = this.headerBufs.get(count - 1);
/* 124 */       int i = 0;
/* 125 */       while (i < current.length()) {
/* 126 */         char ch = current.charAt(i);
/* 127 */         if (ch != ' ' && ch != '\t') {
/*     */           break;
/*     */         }
/* 130 */         i++;
/*     */       } 
/* 132 */       int maxLineLen = this.messageConstraints.getMaxLineLength();
/* 133 */       if (maxLineLen > 0 && previous.length() + 1 + current.length() - i > maxLineLen) {
/* 134 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 136 */       previous.append(' ');
/* 137 */       previous.append(current, i, current.length() - i);
/*     */     } else {
/* 139 */       this.headerBufs.add(current);
/* 140 */       this.lineBuf = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T parse(SessionInputBuffer sessionBuffer, boolean endOfStream) throws IOException, HttpException {
/* 147 */     Args.notNull(sessionBuffer, "Session input buffer");
/* 148 */     while (this.state != State.COMPLETED) {
/* 149 */       if (this.lineBuf == null) {
/* 150 */         this.lineBuf = new CharArrayBuffer(64);
/*     */       } else {
/* 152 */         this.lineBuf.clear();
/*     */       } 
/* 154 */       boolean lineComplete = sessionBuffer.readLine(this.lineBuf, endOfStream);
/* 155 */       int maxLineLen = this.messageConstraints.getMaxLineLength();
/* 156 */       if (maxLineLen > 0 && (this.lineBuf
/* 157 */         .length() > maxLineLen || (!lineComplete && sessionBuffer
/* 158 */         .length() > maxLineLen))) {
/* 159 */         throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */       }
/* 161 */       if (!lineComplete) {
/*     */         break;
/*     */       }
/*     */       
/* 165 */       switch (this.state) {
/*     */         case READ_HEAD_LINE:
/* 167 */           this.message = parseHeadLine();
/* 168 */           if (this.message != null) {
/* 169 */             this.state = State.READ_HEADERS;
/*     */           }
/*     */           break;
/*     */         case READ_HEADERS:
/* 173 */           if (this.lineBuf.length() > 0) {
/* 174 */             int maxHeaderCount = this.messageConstraints.getMaxHeaderCount();
/* 175 */             if (maxHeaderCount > 0 && this.headerBufs.size() >= maxHeaderCount) {
/* 176 */               throw new MessageConstraintException("Maximum header count exceeded");
/*     */             }
/*     */             
/* 179 */             parseHeader(); break;
/*     */           } 
/* 181 */           this.state = State.COMPLETED;
/*     */           break;
/*     */       } 
/*     */       
/* 185 */       if (endOfStream && !sessionBuffer.hasData()) {
/* 186 */         this.state = State.COMPLETED;
/*     */       }
/*     */     } 
/* 189 */     if (this.state == State.COMPLETED) {
/* 190 */       for (CharArrayBuffer buffer : this.headerBufs) {
/* 191 */         this.message.addHeader(this.lineParser.parseHeader(buffer));
/*     */       }
/* 193 */       return this.message;
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract T createMessage(CharArrayBuffer paramCharArrayBuffer) throws HttpException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractMessageParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */