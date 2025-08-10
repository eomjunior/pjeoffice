/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class BasicHeaderValueParser
/*     */   implements HeaderValueParser
/*     */ {
/*  49 */   public static final BasicHeaderValueParser INSTANCE = new BasicHeaderValueParser();
/*     */ 
/*     */   
/*     */   private static final char PARAM_DELIMITER = ';';
/*     */   
/*     */   private static final char ELEM_DELIMITER = ',';
/*     */   
/*  56 */   private static final BitSet TOKEN_DELIMITER = Tokenizer.INIT_BITSET(new int[] { 61, 59, 44 });
/*  57 */   private static final BitSet VALUE_DELIMITER = Tokenizer.INIT_BITSET(new int[] { 59, 44 });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private final Tokenizer tokenizer = Tokenizer.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private static final HeaderElement[] EMPTY_HEADER_ELEMENT_ARRAY = new HeaderElement[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static final NameValuePair[] EMPTY_NAME_VALUE_ARRAY = new NameValuePair[0];
/*     */ 
/*     */   
/*     */   public HeaderElement[] parseElements(CharSequence buffer, ParserCursor cursor) {
/*  77 */     Args.notNull(buffer, "Char sequence");
/*  78 */     Args.notNull(cursor, "Parser cursor");
/*  79 */     List<HeaderElement> elements = new ArrayList<>();
/*  80 */     while (!cursor.atEnd()) {
/*  81 */       HeaderElement element = parseHeaderElement(buffer, cursor);
/*  82 */       if (!element.getName().isEmpty() || element.getValue() != null) {
/*  83 */         elements.add(element);
/*     */       }
/*     */     } 
/*  86 */     return elements.<HeaderElement>toArray(EMPTY_HEADER_ELEMENT_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderElement parseHeaderElement(CharSequence buffer, ParserCursor cursor) {
/*  91 */     Args.notNull(buffer, "Char sequence");
/*  92 */     Args.notNull(cursor, "Parser cursor");
/*  93 */     NameValuePair nvp = parseNameValuePair(buffer, cursor);
/*  94 */     NameValuePair[] params = null;
/*  95 */     if (!cursor.atEnd()) {
/*  96 */       char ch = buffer.charAt(cursor.getPos() - 1);
/*  97 */       if (ch != ',') {
/*  98 */         params = parseParameters(buffer, cursor);
/*     */       }
/*     */     } 
/* 101 */     return new BasicHeaderElement(nvp.getName(), nvp.getValue(), params);
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair[] parseParameters(CharSequence buffer, ParserCursor cursor) {
/* 106 */     Args.notNull(buffer, "Char sequence");
/* 107 */     Args.notNull(cursor, "Parser cursor");
/* 108 */     this.tokenizer.skipWhiteSpace(buffer, cursor);
/* 109 */     List<NameValuePair> params = new ArrayList<>();
/* 110 */     while (!cursor.atEnd()) {
/* 111 */       NameValuePair param = parseNameValuePair(buffer, cursor);
/* 112 */       params.add(param);
/* 113 */       char ch = buffer.charAt(cursor.getPos() - 1);
/* 114 */       if (ch == ',') {
/*     */         break;
/*     */       }
/*     */     } 
/* 118 */     return params.<NameValuePair>toArray(EMPTY_NAME_VALUE_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair parseNameValuePair(CharSequence buffer, ParserCursor cursor) {
/* 123 */     Args.notNull(buffer, "Char sequence");
/* 124 */     Args.notNull(cursor, "Parser cursor");
/*     */     
/* 126 */     String name = this.tokenizer.parseToken(buffer, cursor, TOKEN_DELIMITER);
/* 127 */     if (cursor.atEnd()) {
/* 128 */       return new BasicNameValuePair(name, null);
/*     */     }
/* 130 */     int delim = buffer.charAt(cursor.getPos());
/* 131 */     cursor.updatePos(cursor.getPos() + 1);
/* 132 */     if (delim != 61) {
/* 133 */       return new BasicNameValuePair(name, null);
/*     */     }
/* 135 */     String value = this.tokenizer.parseValue(buffer, cursor, VALUE_DELIMITER);
/* 136 */     if (!cursor.atEnd()) {
/* 137 */       cursor.updatePos(cursor.getPos() + 1);
/*     */     }
/* 139 */     return new BasicNameValuePair(name, value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeaderValueParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */