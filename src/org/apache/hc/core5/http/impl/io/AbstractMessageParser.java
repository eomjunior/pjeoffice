/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
/*     */ import org.apache.hc.core5.http.message.LazyLineParser;
/*     */ import org.apache.hc.core5.http.message.LineParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageParser<T extends HttpMessage>
/*     */   implements HttpMessageParser<T>
/*     */ {
/*     */   private static final int HEAD_LINE = 0;
/*     */   private static final int HEADERS = 1;
/*     */   private final Http1Config http1Config;
/*     */   private final List<CharArrayBuffer> headerLines;
/*     */   private final CharArrayBuffer headLine;
/*     */   private final LineParser lineParser;
/*     */   private int state;
/*     */   private T message;
/*     */   
/*     */   public AbstractMessageParser(LineParser lineParser, Http1Config http1Config) {
/*  79 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLineParser.INSTANCE;
/*  80 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/*  81 */     this.headerLines = new ArrayList<>();
/*  82 */     this.headLine = new CharArrayBuffer(128);
/*  83 */     this.state = 0;
/*     */   }
/*     */   
/*     */   LineParser getLineParser() {
/*  87 */     return this.lineParser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Header[] parseHeaders(SessionInputBuffer inBuffer, InputStream inputStream, int maxHeaderCount, int maxLineLen, LineParser lineParser) throws HttpException, IOException {
/* 116 */     List<CharArrayBuffer> headerLines = new ArrayList<>();
/* 117 */     return parseHeaders(inBuffer, inputStream, maxHeaderCount, maxLineLen, (lineParser != null) ? lineParser : (LineParser)LazyLineParser.INSTANCE, headerLines);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Header[] parseHeaders(SessionInputBuffer inBuffer, InputStream inputStream, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines) throws HttpException, IOException {
/* 153 */     Args.notNull(inBuffer, "Session input buffer");
/* 154 */     Args.notNull(inputStream, "Input stream");
/* 155 */     Args.notNull(parser, "Line parser");
/* 156 */     Args.notNull(headerLines, "Header line list");
/*     */     
/* 158 */     CharArrayBuffer current = null;
/* 159 */     CharArrayBuffer previous = null;
/*     */     while (true) {
/* 161 */       if (current == null) {
/* 162 */         current = new CharArrayBuffer(64);
/*     */       } else {
/* 164 */         current.clear();
/*     */       } 
/* 166 */       int readLen = inBuffer.readLine(current, inputStream);
/* 167 */       if (readLen == -1 || current.length() < 1) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 174 */       if ((current.charAt(0) == ' ' || current.charAt(0) == '\t') && previous != null) {
/*     */ 
/*     */         
/* 177 */         int j = 0;
/* 178 */         while (j < current.length()) {
/* 179 */           char ch = current.charAt(j);
/* 180 */           if (ch != ' ' && ch != '\t') {
/*     */             break;
/*     */           }
/* 183 */           j++;
/*     */         } 
/* 185 */         if (maxLineLen > 0 && previous
/* 186 */           .length() + 1 + current.length() - j > maxLineLen) {
/* 187 */           throw new MessageConstraintException("Maximum line length limit exceeded");
/*     */         }
/* 189 */         previous.append(' ');
/* 190 */         previous.append(current, j, current.length() - j);
/*     */       } else {
/* 192 */         headerLines.add(current);
/* 193 */         previous = current;
/* 194 */         current = null;
/*     */       } 
/* 196 */       if (maxHeaderCount > 0 && headerLines.size() >= maxHeaderCount) {
/* 197 */         throw new MessageConstraintException("Maximum header count exceeded");
/*     */       }
/*     */     } 
/* 200 */     Header[] headers = new Header[headerLines.size()];
/* 201 */     for (int i = 0; i < headerLines.size(); i++) {
/* 202 */       CharArrayBuffer buffer = headerLines.get(i);
/* 203 */       headers[i] = parser.parseHeader(buffer);
/*     */     } 
/* 205 */     return headers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected IOException createConnectionClosedException() {
/* 235 */     return (IOException)new ConnectionClosedException();
/*     */   } public T parse(SessionInputBuffer buffer, InputStream inputStream) throws IOException, HttpException {
/*     */     int n;
/*     */     Header[] headers;
/*     */     T result;
/* 240 */     Args.notNull(buffer, "Session input buffer");
/* 241 */     Args.notNull(inputStream, "Input stream");
/* 242 */     int st = this.state;
/* 243 */     switch (st) {
/*     */       case 0:
/* 245 */         for (n = 0; n < this.http1Config.getMaxEmptyLineCount(); n++) {
/* 246 */           this.headLine.clear();
/* 247 */           int i = buffer.readLine(this.headLine, inputStream);
/* 248 */           if (i == -1) {
/* 249 */             return null;
/*     */           }
/* 251 */           if (this.headLine.length() > 0) {
/* 252 */             this.message = createMessage(this.headLine);
/* 253 */             if (this.message != null) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         } 
/* 258 */         if (this.message == null) {
/* 259 */           throw new MessageConstraintException("Maximum empty line limit exceeded");
/*     */         }
/* 261 */         this.state = 1;
/*     */       
/*     */       case 1:
/* 264 */         headers = parseHeaders(buffer, inputStream, this.http1Config
/*     */ 
/*     */             
/* 267 */             .getMaxHeaderCount(), this.http1Config
/* 268 */             .getMaxLineLength(), this.lineParser, this.headerLines);
/*     */ 
/*     */         
/* 271 */         this.message.setHeaders(headers);
/* 272 */         result = this.message;
/* 273 */         this.message = null;
/* 274 */         this.headerLines.clear();
/* 275 */         this.state = 0;
/* 276 */         return result;
/*     */     } 
/* 278 */     throw new IllegalStateException("Inconsistent parser state");
/*     */   }
/*     */   
/*     */   protected abstract T createMessage(CharArrayBuffer paramCharArrayBuffer) throws IOException, HttpException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/AbstractMessageParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */