/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*     */ final class DistinguishedNameParser
/*     */ {
/*  41 */   public static final DistinguishedNameParser INSTANCE = new DistinguishedNameParser();
/*     */   
/*  43 */   private static final BitSet EQUAL_OR_COMMA_OR_PLUS = Tokenizer.INIT_BITSET(new int[] { 61, 44, 43 });
/*  44 */   private static final BitSet COMMA_OR_PLUS = Tokenizer.INIT_BITSET(new int[] { 44, 43 });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private final Tokenizer tokenParser = new InternalTokenParser();
/*     */ 
/*     */   
/*     */   private String parseToken(CharArrayBuffer buf, Tokenizer.Cursor cursor, BitSet delimiters) {
/*  53 */     return this.tokenParser.parseToken((CharSequence)buf, cursor, delimiters);
/*     */   }
/*     */   
/*     */   private String parseValue(CharArrayBuffer buf, Tokenizer.Cursor cursor, BitSet delimiters) {
/*  57 */     return this.tokenParser.parseValue((CharSequence)buf, cursor, delimiters);
/*     */   }
/*     */   
/*     */   private NameValuePair parseParameter(CharArrayBuffer buf, Tokenizer.Cursor cursor) {
/*  61 */     String name = parseToken(buf, cursor, EQUAL_OR_COMMA_OR_PLUS);
/*  62 */     if (cursor.atEnd()) {
/*  63 */       return (NameValuePair)new BasicNameValuePair(name, null);
/*     */     }
/*  65 */     int delim = buf.charAt(cursor.getPos());
/*  66 */     cursor.updatePos(cursor.getPos() + 1);
/*  67 */     if (delim == 44) {
/*  68 */       return (NameValuePair)new BasicNameValuePair(name, null);
/*     */     }
/*  70 */     String value = parseValue(buf, cursor, COMMA_OR_PLUS);
/*  71 */     if (!cursor.atEnd()) {
/*  72 */       cursor.updatePos(cursor.getPos() + 1);
/*     */     }
/*  74 */     return (NameValuePair)new BasicNameValuePair(name, value);
/*     */   }
/*     */   
/*     */   List<NameValuePair> parse(CharArrayBuffer buf, Tokenizer.Cursor cursor) {
/*  78 */     List<NameValuePair> params = new ArrayList<>();
/*  79 */     this.tokenParser.skipWhiteSpace((CharSequence)buf, cursor);
/*  80 */     while (!cursor.atEnd()) {
/*  81 */       NameValuePair param = parseParameter(buf, cursor);
/*  82 */       params.add(param);
/*     */     } 
/*  84 */     return params;
/*     */   }
/*     */   
/*     */   List<NameValuePair> parse(String s) {
/*  88 */     if (s == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     CharArrayBuffer buffer = new CharArrayBuffer(s.length());
/*  92 */     buffer.append(s);
/*  93 */     Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
/*  94 */     return parse(buffer, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class InternalTokenParser
/*     */     extends Tokenizer
/*     */   {
/*     */     public void copyUnquotedContent(CharSequence buf, Tokenizer.Cursor cursor, BitSet delimiters, StringBuilder dst) {
/* 105 */       int pos = cursor.getPos();
/* 106 */       int indexFrom = cursor.getPos();
/* 107 */       int indexTo = cursor.getUpperBound();
/* 108 */       boolean escaped = false;
/* 109 */       for (int i = indexFrom; i < indexTo; i++, pos++) {
/* 110 */         char current = buf.charAt(i);
/* 111 */         if (escaped) {
/* 112 */           dst.append(current);
/* 113 */           escaped = false;
/*     */         } else {
/* 115 */           if ((delimiters != null && delimiters.get(current)) || 
/* 116 */             Tokenizer.isWhitespace(current) || current == '"')
/*     */             break; 
/* 118 */           if (current == '\\') {
/* 119 */             escaped = true;
/*     */           } else {
/* 121 */             dst.append(current);
/*     */           } 
/*     */         } 
/*     */       } 
/* 125 */       cursor.updatePos(pos);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/DistinguishedNameParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */