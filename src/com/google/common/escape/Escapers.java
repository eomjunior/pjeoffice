/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Escapers
/*     */ {
/*     */   public static Escaper nullEscaper() {
/*  42 */     return NULL_ESCAPER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static final Escaper NULL_ESCAPER = new CharEscaper()
/*     */     {
/*     */       public String escape(String string)
/*     */       {
/*  51 */         return (String)Preconditions.checkNotNull(string);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       @CheckForNull
/*     */       protected char[] escape(char c) {
/*  58 */         return null;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder builder() {
/*  80 */     return new Builder();
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
/*     */   public static final class Builder
/*     */   {
/*  95 */     private final Map<Character, String> replacementMap = new HashMap<>();
/*  96 */     private char safeMin = Character.MIN_VALUE;
/*  97 */     private char safeMax = Character.MAX_VALUE; @CheckForNull
/*  98 */     private String unsafeReplacement = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder setSafeRange(char safeMin, char safeMax) {
/* 114 */       this.safeMin = safeMin;
/* 115 */       this.safeMax = safeMax;
/* 116 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder setUnsafeReplacement(String unsafeReplacement) {
/* 129 */       this.unsafeReplacement = unsafeReplacement;
/* 130 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder addEscape(char c, String replacement) {
/* 145 */       Preconditions.checkNotNull(replacement);
/*     */       
/* 147 */       this.replacementMap.put(Character.valueOf(c), replacement);
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Escaper build() {
/* 153 */       return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax)
/*     */         {
/*     */           @CheckForNull
/* 156 */           private final char[] replacementChars = (Escapers.Builder.this.unsafeReplacement != null) ? Escapers.Builder.this.unsafeReplacement.toCharArray() : null;
/*     */ 
/*     */           
/*     */           @CheckForNull
/*     */           protected char[] escapeUnsafe(char c) {
/* 161 */             return this.replacementChars;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UnicodeEscaper asUnicodeEscaper(Escaper escaper) {
/* 182 */     Preconditions.checkNotNull(escaper);
/* 183 */     if (escaper instanceof UnicodeEscaper)
/* 184 */       return (UnicodeEscaper)escaper; 
/* 185 */     if (escaper instanceof CharEscaper) {
/* 186 */       return wrap((CharEscaper)escaper);
/*     */     }
/*     */ 
/*     */     
/* 190 */     throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper
/* 191 */         .getClass().getName());
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
/*     */   @CheckForNull
/*     */   public static String computeReplacement(CharEscaper escaper, char c) {
/* 205 */     return stringOrNull(escaper.escape(c));
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
/*     */   @CheckForNull
/*     */   public static String computeReplacement(UnicodeEscaper escaper, int cp) {
/* 219 */     return stringOrNull(escaper.escape(cp));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static String stringOrNull(@CheckForNull char[] in) {
/* 224 */     return (in == null) ? null : new String(in);
/*     */   }
/*     */ 
/*     */   
/*     */   private static UnicodeEscaper wrap(final CharEscaper escaper) {
/* 229 */     return new UnicodeEscaper()
/*     */       {
/*     */         @CheckForNull
/*     */         protected char[] escape(int cp)
/*     */         {
/* 234 */           if (cp < 65536) {
/* 235 */             return escaper.escape((char)cp);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 241 */           char[] surrogateChars = new char[2];
/* 242 */           Character.toChars(cp, surrogateChars, 0);
/* 243 */           char[] hiChars = escaper.escape(surrogateChars[0]);
/* 244 */           char[] loChars = escaper.escape(surrogateChars[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 250 */           if (hiChars == null && loChars == null)
/*     */           {
/* 252 */             return null;
/*     */           }
/*     */           
/* 255 */           int hiCount = (hiChars != null) ? hiChars.length : 1;
/* 256 */           int loCount = (loChars != null) ? loChars.length : 1;
/* 257 */           char[] output = new char[hiCount + loCount];
/* 258 */           if (hiChars != null) {
/*     */             
/* 260 */             for (int n = 0; n < hiChars.length; n++) {
/* 261 */               output[n] = hiChars[n];
/*     */             }
/*     */           } else {
/* 264 */             output[0] = surrogateChars[0];
/*     */           } 
/* 266 */           if (loChars != null) {
/* 267 */             for (int n = 0; n < loChars.length; n++) {
/* 268 */               output[hiCount + n] = loChars[n];
/*     */             }
/*     */           } else {
/* 271 */             output[hiCount] = surrogateChars[1];
/*     */           } 
/* 273 */           return output;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/escape/Escapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */