/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenizedPattern
/*     */ {
/*  39 */   public static final TokenizedPattern EMPTY_PATTERN = new TokenizedPattern("", new String[0]);
/*     */ 
/*     */ 
/*     */   
/*     */   private final String pattern;
/*     */ 
/*     */   
/*     */   private final String[] tokenizedPattern;
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPattern(String pattern) {
/*  51 */     this(pattern, SelectorUtils.tokenizePathAsArray(pattern));
/*     */   }
/*     */   
/*     */   TokenizedPattern(String pattern, String[] tokens) {
/*  55 */     this.pattern = pattern;
/*  56 */     this.tokenizedPattern = tokens;
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
/*     */   public boolean matchPath(TokenizedPath path, boolean isCaseSensitive) {
/*  71 */     return SelectorUtils.matchPath(this.tokenizedPattern, path.getTokens(), isCaseSensitive);
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
/*     */   public boolean matchStartOf(TokenizedPath path, boolean caseSensitive) {
/*  85 */     return SelectorUtils.matchPatternStart(this.tokenizedPattern, path
/*  86 */         .getTokens(), caseSensitive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public String getPattern() {
/*  97 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 106 */     return (o instanceof TokenizedPattern && this.pattern
/* 107 */       .equals(((TokenizedPattern)o).pattern));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 111 */     return this.pattern.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int depth() {
/* 120 */     return this.tokenizedPattern.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsPattern(String pat) {
/* 130 */     return Stream.<String>of(this.tokenizedPattern).anyMatch(Predicate.isEqual(pat));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPath rtrimWildcardTokens() {
/* 140 */     StringBuilder sb = new StringBuilder();
/* 141 */     int newLen = 0;
/* 142 */     for (; newLen < this.tokenizedPattern.length && 
/* 143 */       !SelectorUtils.hasWildcards(this.tokenizedPattern[newLen]); newLen++) {
/*     */ 
/*     */       
/* 146 */       if (newLen > 0 && sb
/* 147 */         .charAt(sb.length() - 1) != File.separatorChar) {
/* 148 */         sb.append(File.separator);
/*     */       }
/* 150 */       sb.append(this.tokenizedPattern[newLen]);
/*     */     } 
/* 152 */     if (newLen == 0) {
/* 153 */       return TokenizedPath.EMPTY_PATH;
/*     */     }
/* 155 */     String[] newPats = new String[newLen];
/* 156 */     System.arraycopy(this.tokenizedPattern, 0, newPats, 0, newLen);
/* 157 */     return new TokenizedPath(sb.toString(), newPats);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean endsWith(String s) {
/* 167 */     return (this.tokenizedPattern.length > 0 && this.tokenizedPattern[this.tokenizedPattern.length - 1]
/* 168 */       .equals(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPattern withoutLastToken() {
/* 177 */     if (this.tokenizedPattern.length == 0) {
/* 178 */       throw new IllegalStateException("can't strip a token from nothing");
/*     */     }
/* 180 */     if (this.tokenizedPattern.length == 1) {
/* 181 */       return EMPTY_PATTERN;
/*     */     }
/* 183 */     String toStrip = this.tokenizedPattern[this.tokenizedPattern.length - 1];
/* 184 */     int index = this.pattern.lastIndexOf(toStrip);
/* 185 */     String[] tokens = new String[this.tokenizedPattern.length - 1];
/* 186 */     System.arraycopy(this.tokenizedPattern, 0, tokens, 0, this.tokenizedPattern.length - 1);
/*     */     
/* 188 */     return new TokenizedPattern(this.pattern.substring(0, index), tokens);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/TokenizedPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */