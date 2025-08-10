/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.escape.UnicodeEscaper;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*  57 */   private static final char[] PLUS_SIGN = new char[] { '+' };
/*     */ 
/*     */   
/*  60 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean plusForSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] safeOctets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace) {
/*  88 */     Preconditions.checkNotNull(safeChars);
/*     */     
/*  90 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/*  91 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */     
/*  94 */     safeChars = safeChars + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
/*     */ 
/*     */     
/*  97 */     if (plusForSpace && safeChars.contains(" ")) {
/*  98 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 101 */     this.plusForSpace = plusForSpace;
/* 102 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] createSafeOctets(String safeChars) {
/* 111 */     int maxChar = -1;
/* 112 */     char[] safeCharArray = safeChars.toCharArray();
/* 113 */     for (char c : safeCharArray) {
/* 114 */       maxChar = Math.max(c, maxChar);
/*     */     }
/* 116 */     boolean[] octets = new boolean[maxChar + 1];
/* 117 */     for (char c : safeCharArray) {
/* 118 */       octets[c] = true;
/*     */     }
/* 120 */     return octets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 129 */     Preconditions.checkNotNull(csq);
/* 130 */     for (; index < end; index++) {
/* 131 */       char c = csq.charAt(index);
/* 132 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/*     */         break;
/*     */       }
/*     */     } 
/* 136 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escape(String s) {
/* 145 */     Preconditions.checkNotNull(s);
/* 146 */     int slen = s.length();
/* 147 */     for (int index = 0; index < slen; index++) {
/* 148 */       char c = s.charAt(index);
/* 149 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/* 150 */         return escapeSlow(s, index);
/*     */       }
/*     */     } 
/* 153 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected char[] escape(int cp) {
/* 162 */     if (cp < this.safeOctets.length && this.safeOctets[cp])
/* 163 */       return null; 
/* 164 */     if (cp == 32 && this.plusForSpace)
/* 165 */       return PLUS_SIGN; 
/* 166 */     if (cp <= 127) {
/*     */ 
/*     */       
/* 169 */       char[] dest = new char[3];
/* 170 */       dest[0] = '%';
/* 171 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 172 */       dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
/* 173 */       return dest;
/* 174 */     }  if (cp <= 2047) {
/*     */ 
/*     */       
/* 177 */       char[] dest = new char[6];
/* 178 */       dest[0] = '%';
/* 179 */       dest[3] = '%';
/* 180 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 181 */       cp >>>= 4;
/* 182 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 183 */       cp >>>= 2;
/* 184 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 185 */       cp >>>= 4;
/* 186 */       dest[1] = UPPER_HEX_DIGITS[0xC | cp];
/* 187 */       return dest;
/* 188 */     }  if (cp <= 65535) {
/*     */ 
/*     */       
/* 191 */       char[] dest = new char[9];
/* 192 */       dest[0] = '%';
/* 193 */       dest[1] = 'E';
/* 194 */       dest[3] = '%';
/* 195 */       dest[6] = '%';
/* 196 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 197 */       cp >>>= 4;
/* 198 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 199 */       cp >>>= 2;
/* 200 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 201 */       cp >>>= 4;
/* 202 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 203 */       cp >>>= 2;
/* 204 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 205 */       return dest;
/* 206 */     }  if (cp <= 1114111) {
/* 207 */       char[] dest = new char[12];
/*     */ 
/*     */       
/* 210 */       dest[0] = '%';
/* 211 */       dest[1] = 'F';
/* 212 */       dest[3] = '%';
/* 213 */       dest[6] = '%';
/* 214 */       dest[9] = '%';
/* 215 */       dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
/* 216 */       cp >>>= 4;
/* 217 */       dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 218 */       cp >>>= 2;
/* 219 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 220 */       cp >>>= 4;
/* 221 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 222 */       cp >>>= 2;
/* 223 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 224 */       cp >>>= 4;
/* 225 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 226 */       cp >>>= 2;
/* 227 */       dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
/* 228 */       return dest;
/*     */     } 
/*     */     
/* 231 */     throw new IllegalArgumentException("Invalid unicode character value " + cp);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/PercentEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */