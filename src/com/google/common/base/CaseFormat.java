/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public enum CaseFormat
/*     */ {
/*  35 */   LOWER_HYPHEN(CharMatcher.is('-'), "-")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  38 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  43 */       if (format == LOWER_UNDERSCORE) {
/*  44 */         return s.replace('-', '_');
/*     */       }
/*  46 */       if (format == UPPER_UNDERSCORE) {
/*  47 */         return Ascii.toUpperCase(s.replace('-', '_'));
/*     */       }
/*  49 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  54 */   LOWER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  57 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/*  62 */       if (format == LOWER_HYPHEN) {
/*  63 */         return s.replace('_', '-');
/*     */       }
/*  65 */       if (format == UPPER_UNDERSCORE) {
/*  66 */         return Ascii.toUpperCase(s);
/*     */       }
/*  68 */       return super.convert(format, s);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  73 */   LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  76 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String normalizeFirstWord(String word) {
/*  81 */       return Ascii.toLowerCase(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  86 */   UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  89 */       return firstCharOnlyToUpper(word);
/*     */     }
/*     */   },
/*     */ 
/*     */   
/*  94 */   UPPER_UNDERSCORE(CharMatcher.is('_'), "_")
/*     */   {
/*     */     String normalizeWord(String word) {
/*  97 */       return Ascii.toUpperCase(word);
/*     */     }
/*     */ 
/*     */     
/*     */     String convert(CaseFormat format, String s) {
/* 102 */       if (format == LOWER_HYPHEN) {
/* 103 */         return Ascii.toLowerCase(s.replace('_', '-'));
/*     */       }
/* 105 */       if (format == LOWER_UNDERSCORE) {
/* 106 */         return Ascii.toLowerCase(s);
/*     */       }
/* 108 */       return super.convert(format, s);
/*     */     }
/*     */   };
/*     */   
/*     */   private final CharMatcher wordBoundary;
/*     */   private final String wordSeparator;
/*     */   
/*     */   CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
/* 116 */     this.wordBoundary = wordBoundary;
/* 117 */     this.wordSeparator = wordSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String to(CaseFormat format, String str) {
/* 126 */     Preconditions.checkNotNull(format);
/* 127 */     Preconditions.checkNotNull(str);
/* 128 */     return (format == this) ? str : convert(format, str);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String convert(CaseFormat format, String s) {
/* 134 */     StringBuilder out = null;
/* 135 */     int i = 0;
/* 136 */     int j = -1;
/* 137 */     while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
/* 138 */       if (i == 0) {
/*     */         
/* 140 */         out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
/* 141 */         out.append(format.normalizeFirstWord(s.substring(i, j)));
/*     */       } else {
/* 143 */         ((StringBuilder)Objects.<StringBuilder>requireNonNull(out)).append(format.normalizeWord(s.substring(i, j)));
/*     */       } 
/* 145 */       out.append(format.wordSeparator);
/* 146 */       i = j + this.wordSeparator.length();
/*     */     } 
/* 148 */     return (i == 0) ? 
/* 149 */       format.normalizeFirstWord(s) : (
/* 150 */       (StringBuilder)Objects.<StringBuilder>requireNonNull(out)).append(format.normalizeWord(s.substring(i))).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Converter<String, String> converterTo(CaseFormat targetFormat) {
/* 160 */     return new StringConverter(this, targetFormat);
/*     */   }
/*     */   
/*     */   private static final class StringConverter
/*     */     extends Converter<String, String> implements Serializable {
/*     */     private final CaseFormat sourceFormat;
/*     */     private final CaseFormat targetFormat;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
/* 170 */       this.sourceFormat = Preconditions.<CaseFormat>checkNotNull(sourceFormat);
/* 171 */       this.targetFormat = Preconditions.<CaseFormat>checkNotNull(targetFormat);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doForward(String s) {
/* 176 */       return this.sourceFormat.to(this.targetFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(String s) {
/* 181 */       return this.targetFormat.to(this.sourceFormat, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 186 */       if (object instanceof StringConverter) {
/* 187 */         StringConverter that = (StringConverter)object;
/* 188 */         return (this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat));
/*     */       } 
/* 190 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 195 */       return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 200 */       return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String normalizeFirstWord(String word) {
/* 209 */     return normalizeWord(word);
/*     */   }
/*     */   
/*     */   private static String firstCharOnlyToUpper(String word) {
/* 213 */     return word.isEmpty() ? 
/* 214 */       word : (
/* 215 */       Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1)));
/*     */   }
/*     */   
/*     */   abstract String normalizeWord(String paramString);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/CaseFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */