/*     */ package org.apache.hc.core5.http.ssl;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import org.apache.hc.core5.http.ParseException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*     */ final class TlsVersionParser
/*     */ {
/*  38 */   public static final TlsVersionParser INSTANCE = new TlsVersionParser();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private final Tokenizer tokenizer = Tokenizer.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ProtocolVersion parse(CharSequence buffer, Tokenizer.Cursor cursor, BitSet delimiters) throws ParseException {
/*  50 */     int major, minor, lowerBound = cursor.getLowerBound();
/*  51 */     int upperBound = cursor.getUpperBound();
/*     */     
/*  53 */     int pos = cursor.getPos();
/*  54 */     if (pos + 4 > cursor.getUpperBound()) {
/*  55 */       throw new ParseException("Invalid TLS protocol version", buffer, lowerBound, upperBound, pos);
/*     */     }
/*  57 */     if (buffer.charAt(pos) != 'T' || buffer.charAt(pos + 1) != 'L' || buffer.charAt(pos + 2) != 'S' || buffer
/*  58 */       .charAt(pos + 3) != 'v') {
/*  59 */       throw new ParseException("Invalid TLS protocol version", buffer, lowerBound, upperBound, pos);
/*     */     }
/*  61 */     pos += 4;
/*  62 */     cursor.updatePos(pos);
/*  63 */     if (cursor.atEnd()) {
/*  64 */       throw new ParseException("Invalid TLS version", buffer, lowerBound, upperBound, pos);
/*     */     }
/*  66 */     String s = this.tokenizer.parseToken(buffer, cursor, delimiters);
/*  67 */     int idx = s.indexOf('.');
/*  68 */     if (idx == -1) {
/*     */       int i;
/*     */       try {
/*  71 */         i = Integer.parseInt(s);
/*  72 */       } catch (NumberFormatException e) {
/*  73 */         throw new ParseException("Invalid TLS major version", buffer, lowerBound, upperBound, pos);
/*     */       } 
/*  75 */       return new ProtocolVersion("TLS", i, 0);
/*     */     } 
/*  77 */     String s1 = s.substring(0, idx);
/*     */     
/*     */     try {
/*  80 */       major = Integer.parseInt(s1);
/*  81 */     } catch (NumberFormatException e) {
/*  82 */       throw new ParseException("Invalid TLS major version", buffer, lowerBound, upperBound, pos);
/*     */     } 
/*  84 */     String s2 = s.substring(idx + 1);
/*     */     
/*     */     try {
/*  87 */       minor = Integer.parseInt(s2);
/*  88 */     } catch (NumberFormatException e) {
/*  89 */       throw new ParseException("Invalid TLS minor version", buffer, lowerBound, upperBound, pos);
/*     */     } 
/*  91 */     return new ProtocolVersion("TLS", major, minor);
/*     */   }
/*     */ 
/*     */   
/*     */   ProtocolVersion parse(String s) throws ParseException {
/*  96 */     if (s == null) {
/*  97 */       return null;
/*     */     }
/*  99 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/* 100 */     return parse(s, cursor, null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ssl/TlsVersionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */