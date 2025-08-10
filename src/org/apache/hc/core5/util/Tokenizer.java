/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class Tokenizer
/*     */ {
/*     */   public static final char DQUOTE = '"';
/*     */   public static final char ESCAPE = '\\';
/*     */   public static final int CR = 13;
/*     */   public static final int LF = 10;
/*     */   public static final int SP = 32;
/*     */   public static final int HT = 9;
/*     */   
/*     */   public static class Cursor
/*     */   {
/*     */     private final int lowerBound;
/*     */     private final int upperBound;
/*     */     private int pos;
/*     */     
/*     */     public Cursor(int lowerBound, int upperBound) {
/*  55 */       Args.notNegative(lowerBound, "lowerBound");
/*  56 */       Args.check((lowerBound <= upperBound), "lowerBound cannot be greater than upperBound");
/*  57 */       this.lowerBound = lowerBound;
/*  58 */       this.upperBound = upperBound;
/*  59 */       this.pos = lowerBound;
/*     */     }
/*     */     
/*     */     public int getLowerBound() {
/*  63 */       return this.lowerBound;
/*     */     }
/*     */     
/*     */     public int getUpperBound() {
/*  67 */       return this.upperBound;
/*     */     }
/*     */     
/*     */     public int getPos() {
/*  71 */       return this.pos;
/*     */     }
/*     */     
/*     */     public void updatePos(int pos) {
/*  75 */       Args.check((pos >= this.lowerBound), "pos: %s < lowerBound: %s", new Object[] { Integer.valueOf(pos), Integer.valueOf(this.lowerBound) });
/*  76 */       Args.check((pos <= this.upperBound), "pos: %s > upperBound: %s", new Object[] { Integer.valueOf(pos), Integer.valueOf(this.upperBound) });
/*  77 */       this.pos = pos;
/*     */     }
/*     */     
/*     */     public boolean atEnd() {
/*  81 */       return (this.pos >= this.upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  86 */       StringBuilder buffer = new StringBuilder();
/*  87 */       buffer.append('[');
/*  88 */       buffer.append(this.lowerBound);
/*  89 */       buffer.append('>');
/*  90 */       buffer.append(this.pos);
/*  91 */       buffer.append('>');
/*  92 */       buffer.append(this.upperBound);
/*  93 */       buffer.append(']');
/*  94 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static BitSet INIT_BITSET(int... b) {
/* 100 */     BitSet bitset = new BitSet();
/* 101 */     for (int aB : b) {
/* 102 */       bitset.set(aB);
/*     */     }
/* 104 */     return bitset;
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
/*     */   public static boolean isWhitespace(char ch) {
/* 119 */     return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
/*     */   }
/*     */   
/* 122 */   public static final Tokenizer INSTANCE = new Tokenizer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String parseContent(CharSequence buf, Cursor cursor, BitSet delimiters) {
/* 134 */     Args.notNull(buf, "Char sequence");
/* 135 */     Args.notNull(cursor, "Parser cursor");
/* 136 */     StringBuilder dst = new StringBuilder();
/* 137 */     copyContent(buf, cursor, delimiters, dst);
/* 138 */     return dst.toString();
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
/*     */   public String parseToken(CharSequence buf, Cursor cursor, BitSet delimiters) {
/* 151 */     Args.notNull(buf, "Char sequence");
/* 152 */     Args.notNull(cursor, "Parser cursor");
/* 153 */     StringBuilder dst = new StringBuilder();
/* 154 */     boolean whitespace = false;
/* 155 */     while (!cursor.atEnd()) {
/* 156 */       char current = buf.charAt(cursor.getPos());
/* 157 */       if (delimiters != null && delimiters.get(current))
/*     */         break; 
/* 159 */       if (isWhitespace(current)) {
/* 160 */         skipWhiteSpace(buf, cursor);
/* 161 */         whitespace = true; continue;
/*     */       } 
/* 163 */       if (whitespace && dst.length() > 0) {
/* 164 */         dst.append(' ');
/*     */       }
/* 166 */       copyContent(buf, cursor, delimiters, dst);
/* 167 */       whitespace = false;
/*     */     } 
/*     */     
/* 170 */     return dst.toString();
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
/*     */   public String parseValue(CharSequence buf, Cursor cursor, BitSet delimiters) {
/* 184 */     Args.notNull(buf, "Char sequence");
/* 185 */     Args.notNull(cursor, "Parser cursor");
/* 186 */     StringBuilder dst = new StringBuilder();
/* 187 */     boolean whitespace = false;
/* 188 */     while (!cursor.atEnd()) {
/* 189 */       char current = buf.charAt(cursor.getPos());
/* 190 */       if (delimiters != null && delimiters.get(current))
/*     */         break; 
/* 192 */       if (isWhitespace(current)) {
/* 193 */         skipWhiteSpace(buf, cursor);
/* 194 */         whitespace = true; continue;
/* 195 */       }  if (current == '"') {
/* 196 */         if (whitespace && dst.length() > 0) {
/* 197 */           dst.append(' ');
/*     */         }
/* 199 */         copyQuotedContent(buf, cursor, dst);
/* 200 */         whitespace = false; continue;
/*     */       } 
/* 202 */       if (whitespace && dst.length() > 0) {
/* 203 */         dst.append(' ');
/*     */       }
/* 205 */       copyUnquotedContent(buf, cursor, delimiters, dst);
/* 206 */       whitespace = false;
/*     */     } 
/*     */     
/* 209 */     return dst.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipWhiteSpace(CharSequence buf, Cursor cursor) {
/* 220 */     Args.notNull(buf, "Char sequence");
/* 221 */     Args.notNull(cursor, "Parser cursor");
/* 222 */     int pos = cursor.getPos();
/* 223 */     int indexFrom = cursor.getPos();
/* 224 */     int indexTo = cursor.getUpperBound();
/* 225 */     for (int i = indexFrom; i < indexTo; i++) {
/* 226 */       char current = buf.charAt(i);
/* 227 */       if (!isWhitespace(current)) {
/*     */         break;
/*     */       }
/* 230 */       pos++;
/*     */     } 
/* 232 */     cursor.updatePos(pos);
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
/*     */   public void copyContent(CharSequence buf, Cursor cursor, BitSet delimiters, StringBuilder dst) {
/* 247 */     Args.notNull(buf, "Char sequence");
/* 248 */     Args.notNull(cursor, "Parser cursor");
/* 249 */     Args.notNull(dst, "String builder");
/* 250 */     int pos = cursor.getPos();
/* 251 */     int indexFrom = cursor.getPos();
/* 252 */     int indexTo = cursor.getUpperBound();
/* 253 */     for (int i = indexFrom; i < indexTo; i++) {
/* 254 */       char current = buf.charAt(i);
/* 255 */       if ((delimiters != null && delimiters.get(current)) || isWhitespace(current)) {
/*     */         break;
/*     */       }
/* 258 */       pos++;
/* 259 */       dst.append(current);
/*     */     } 
/* 261 */     cursor.updatePos(pos);
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
/*     */   public void copyUnquotedContent(CharSequence buf, Cursor cursor, BitSet delimiters, StringBuilder dst) {
/* 276 */     Args.notNull(buf, "Char sequence");
/* 277 */     Args.notNull(cursor, "Parser cursor");
/* 278 */     Args.notNull(dst, "String builder");
/* 279 */     int pos = cursor.getPos();
/* 280 */     int indexFrom = cursor.getPos();
/* 281 */     int indexTo = cursor.getUpperBound();
/* 282 */     for (int i = indexFrom; i < indexTo; i++) {
/* 283 */       char current = buf.charAt(i);
/* 284 */       if ((delimiters != null && delimiters.get(current)) || 
/* 285 */         isWhitespace(current) || current == '"') {
/*     */         break;
/*     */       }
/* 288 */       pos++;
/* 289 */       dst.append(current);
/*     */     } 
/* 291 */     cursor.updatePos(pos);
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
/*     */   public void copyQuotedContent(CharSequence buf, Cursor cursor, StringBuilder dst) {
/* 303 */     Args.notNull(buf, "Char sequence");
/* 304 */     Args.notNull(cursor, "Parser cursor");
/* 305 */     Args.notNull(dst, "String builder");
/* 306 */     if (cursor.atEnd()) {
/*     */       return;
/*     */     }
/* 309 */     int pos = cursor.getPos();
/* 310 */     int indexFrom = cursor.getPos();
/* 311 */     int indexTo = cursor.getUpperBound();
/* 312 */     char current = buf.charAt(pos);
/* 313 */     if (current != '"') {
/*     */       return;
/*     */     }
/* 316 */     pos++;
/* 317 */     indexFrom++;
/* 318 */     boolean escaped = false;
/* 319 */     for (int i = indexFrom; i < indexTo; i++, pos++) {
/* 320 */       current = buf.charAt(i);
/* 321 */       if (escaped) {
/* 322 */         if (current != '"' && current != '\\') {
/* 323 */           dst.append('\\');
/*     */         }
/* 325 */         dst.append(current);
/* 326 */         escaped = false;
/*     */       } else {
/* 328 */         if (current == '"') {
/* 329 */           pos++;
/*     */           break;
/*     */         } 
/* 332 */         if (current == '\\') {
/* 333 */           escaped = true;
/* 334 */         } else if (current != '\r' && current != '\n') {
/* 335 */           dst.append(current);
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     cursor.updatePos(pos);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/Tokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */