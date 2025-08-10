/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class TokenParser
/*     */   extends Tokenizer
/*     */ {
/*  50 */   public static final TokenParser INSTANCE = new TokenParser();
/*     */ 
/*     */   
/*     */   public static final char DQUOTE = '"';
/*     */   
/*     */   public static final char ESCAPE = '\\';
/*     */ 
/*     */   
/*     */   public String parseToken(CharSequence buf, ParserCursor cursor, BitSet delimiters) {
/*  59 */     return parseToken(buf, cursor, delimiters);
/*     */   }
/*     */   
/*     */   public String parseValue(CharSequence buf, ParserCursor cursor, BitSet delimiters) {
/*  63 */     return parseValue(buf, cursor, delimiters);
/*     */   }
/*     */   
/*     */   public void skipWhiteSpace(CharSequence buf, ParserCursor cursor) {
/*  67 */     skipWhiteSpace(buf, cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyContent(CharSequence buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
/*  72 */     super.copyContent(buf, cursor, delimiters, dst);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyContent(CharSequence buf, Tokenizer.Cursor cursor, BitSet delimiters, StringBuilder dst) {
/*  78 */     ParserCursor parserCursor = new ParserCursor(cursor.getLowerBound(), cursor.getUpperBound());
/*  79 */     parserCursor.updatePos(cursor.getPos());
/*  80 */     copyContent(buf, parserCursor, delimiters, dst);
/*  81 */     cursor.updatePos(parserCursor.getPos());
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyUnquotedContent(CharSequence buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
/*  86 */     super.copyUnquotedContent(buf, cursor, delimiters, dst);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyUnquotedContent(CharSequence buf, Tokenizer.Cursor cursor, BitSet delimiters, StringBuilder dst) {
/*  92 */     ParserCursor parserCursor = new ParserCursor(cursor.getLowerBound(), cursor.getUpperBound());
/*  93 */     parserCursor.updatePos(cursor.getPos());
/*  94 */     copyUnquotedContent(buf, parserCursor, delimiters, dst);
/*  95 */     cursor.updatePos(parserCursor.getPos());
/*     */   }
/*     */   
/*     */   public void copyQuotedContent(CharSequence buf, ParserCursor cursor, StringBuilder dst) {
/*  99 */     super.copyQuotedContent(buf, cursor, dst);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyQuotedContent(CharSequence buf, Tokenizer.Cursor cursor, StringBuilder dst) {
/* 104 */     ParserCursor parserCursor = new ParserCursor(cursor.getLowerBound(), cursor.getUpperBound());
/* 105 */     parserCursor.updatePos(cursor.getPos());
/* 106 */     copyQuotedContent(buf, parserCursor, dst);
/* 107 */     cursor.updatePos(parserCursor.getPos());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/TokenParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */