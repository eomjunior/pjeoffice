/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.apache.hc.core5.util.Tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class BasicLineParser
/*     */   implements LineParser
/*     */ {
/*  51 */   public static final BasicLineParser INSTANCE = new BasicLineParser();
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final BitSet FULL_STOP = Tokenizer.INIT_BITSET(new int[] { 46 });
/*  56 */   private static final BitSet BLANKS = Tokenizer.INIT_BITSET(new int[] { 32, 9 });
/*  57 */   private static final BitSet COLON = Tokenizer.INIT_BITSET(new int[] { 58 });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ProtocolVersion protocol;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Tokenizer tokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser(ProtocolVersion proto) {
/*  74 */     this.protocol = (proto != null) ? proto : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*  75 */     this.tokenizer = Tokenizer.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicLineParser() {
/*  82 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   ProtocolVersion parseProtocolVersion(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/*     */     int major, minor;
/*  88 */     String protoname = this.protocol.getProtocol();
/*  89 */     int protolength = protoname.length();
/*     */     
/*  91 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/*     */     
/*  93 */     int pos = cursor.getPos();
/*     */ 
/*     */     
/*  96 */     if (pos + protolength + 4 > cursor.getUpperBound()) {
/*  97 */       throw new ParseException("Invalid protocol version", buffer, cursor
/*  98 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/*     */ 
/*     */     
/* 102 */     boolean ok = true;
/* 103 */     for (int i = 0; ok && i < protolength; i++) {
/* 104 */       ok = (buffer.charAt(pos + i) == protoname.charAt(i));
/*     */     }
/* 106 */     if (ok) {
/* 107 */       ok = (buffer.charAt(pos + protolength) == '/');
/*     */     }
/* 109 */     if (!ok) {
/* 110 */       throw new ParseException("Invalid protocol version", buffer, cursor
/* 111 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/*     */     
/* 114 */     cursor.updatePos(pos + protolength + 1);
/*     */     
/* 116 */     String token1 = this.tokenizer.parseToken((CharSequence)buffer, cursor, FULL_STOP);
/*     */     
/*     */     try {
/* 119 */       major = Integer.parseInt(token1);
/* 120 */     } catch (NumberFormatException e) {
/* 121 */       throw new ParseException("Invalid protocol major version number", buffer, cursor
/* 122 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     } 
/* 124 */     if (cursor.atEnd()) {
/* 125 */       throw new ParseException("Invalid protocol version", buffer, cursor
/* 126 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/* 128 */     cursor.updatePos(cursor.getPos() + 1);
/* 129 */     String token2 = this.tokenizer.parseToken((CharSequence)buffer, cursor, BLANKS);
/*     */     
/*     */     try {
/* 132 */       minor = Integer.parseInt(token2);
/* 133 */     } catch (NumberFormatException e) {
/* 134 */       throw new ParseException("Invalid protocol minor version number", buffer, cursor
/* 135 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     } 
/* 137 */     return (ProtocolVersion)HttpVersion.get(major, minor);
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
/*     */   public RequestLine parseRequestLine(CharArrayBuffer buffer) throws ParseException {
/* 151 */     Args.notNull(buffer, "Char array buffer");
/*     */     
/* 153 */     ParserCursor cursor = new ParserCursor(0, buffer.length());
/* 154 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 155 */     String method = this.tokenizer.parseToken((CharSequence)buffer, cursor, BLANKS);
/* 156 */     if (TextUtils.isEmpty(method)) {
/* 157 */       throw new ParseException("Invalid request line", buffer, cursor
/* 158 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/* 160 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 161 */     String uri = this.tokenizer.parseToken((CharSequence)buffer, cursor, BLANKS);
/* 162 */     if (TextUtils.isEmpty(uri)) {
/* 163 */       throw new ParseException("Invalid request line", buffer, cursor
/* 164 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/* 166 */     ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/* 167 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 168 */     if (!cursor.atEnd()) {
/* 169 */       throw new ParseException("Invalid request line", buffer, cursor
/* 170 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/* 172 */     return new RequestLine(method, uri, ver);
/*     */   }
/*     */   
/*     */   public StatusLine parseStatusLine(CharArrayBuffer buffer) throws ParseException {
/*     */     int statusCode;
/* 177 */     Args.notNull(buffer, "Char array buffer");
/*     */     
/* 179 */     ParserCursor cursor = new ParserCursor(0, buffer.length());
/* 180 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 181 */     ProtocolVersion ver = parseProtocolVersion(buffer, cursor);
/* 182 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 183 */     String s = this.tokenizer.parseToken((CharSequence)buffer, cursor, BLANKS);
/* 184 */     for (int i = 0; i < s.length(); i++) {
/* 185 */       if (!Character.isDigit(s.charAt(i))) {
/* 186 */         throw new ParseException("Status line contains invalid status code", buffer, cursor
/* 187 */             .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 192 */       statusCode = Integer.parseInt(s);
/* 193 */     } catch (NumberFormatException e) {
/* 194 */       throw new ParseException("Status line contains invalid status code", buffer, cursor
/* 195 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     } 
/* 197 */     String text = buffer.substringTrimmed(cursor.getPos(), cursor.getUpperBound());
/* 198 */     return new StatusLine(ver, statusCode, text);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
/* 203 */     Args.notNull(buffer, "Char array buffer");
/*     */     
/* 205 */     ParserCursor cursor = new ParserCursor(0, buffer.length());
/* 206 */     this.tokenizer.skipWhiteSpace((CharSequence)buffer, cursor);
/* 207 */     String name = this.tokenizer.parseToken((CharSequence)buffer, cursor, COLON);
/* 208 */     if (cursor.getPos() == cursor.getLowerBound() || cursor.getPos() == cursor.getUpperBound() || buffer
/* 209 */       .charAt(cursor.getPos()) != ':' || 
/* 210 */       TextUtils.isEmpty(name) || 
/* 211 */       Tokenizer.isWhitespace(buffer.charAt(cursor.getPos() - 1))) {
/* 212 */       throw new ParseException("Invalid header", buffer, cursor
/* 213 */           .getLowerBound(), cursor.getUpperBound(), cursor.getPos());
/*     */     }
/* 215 */     String value = buffer.substringTrimmed(cursor.getPos() + 1, cursor.getUpperBound());
/* 216 */     return new BasicHeader(name, value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicLineParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */