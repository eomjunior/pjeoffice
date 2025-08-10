/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Globs
/*     */ {
/*     */   private static final String regexMetaChars = ".^$+{[]|()";
/*     */   private static final String globMetaChars = "\\*?[{";
/*     */   
/*     */   private static boolean isRegexMeta(char c) {
/*  37 */     return (".^$+{[]|()".indexOf(c) != -1);
/*     */   }
/*     */   
/*     */   private static boolean isGlobMeta(char c) {
/*  41 */     return ("\\*?[{".indexOf(c) != -1);
/*     */   }
/*  43 */   private static char EOL = Character.MIN_VALUE;
/*     */   
/*     */   private static char next(String glob, int i) {
/*  46 */     if (i < glob.length()) {
/*  47 */       return glob.charAt(i);
/*     */     }
/*  49 */     return EOL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toRegexPattern(String globPattern, boolean isDos) {
/*  58 */     boolean inGroup = false;
/*  59 */     StringBuilder regex = new StringBuilder("^");
/*     */     
/*  61 */     int i = 0;
/*  62 */     while (i < globPattern.length()) {
/*  63 */       char next; boolean hasRangeStart; char last, c = globPattern.charAt(i++);
/*  64 */       switch (c) {
/*     */         
/*     */         case '\\':
/*  67 */           if (i == globPattern.length()) {
/*  68 */             throw new PatternSyntaxException("No character to escape", globPattern, i - 1);
/*     */           }
/*     */           
/*  71 */           next = globPattern.charAt(i++);
/*  72 */           if (isGlobMeta(next) || isRegexMeta(next)) {
/*  73 */             regex.append('\\');
/*     */           }
/*  75 */           regex.append(next);
/*     */           continue;
/*     */         case '/':
/*  78 */           if (isDos) {
/*  79 */             regex.append("\\\\"); continue;
/*     */           } 
/*  81 */           regex.append(c);
/*     */           continue;
/*     */ 
/*     */         
/*     */         case '[':
/*  86 */           if (isDos) {
/*  87 */             regex.append("[[^\\\\]&&[");
/*     */           } else {
/*  89 */             regex.append("[[^/]&&[");
/*     */           } 
/*  91 */           if (next(globPattern, i) == '^') {
/*     */             
/*  93 */             regex.append("\\^");
/*  94 */             i++;
/*     */           } else {
/*     */             
/*  97 */             if (next(globPattern, i) == '!') {
/*  98 */               regex.append('^');
/*  99 */               i++;
/*     */             } 
/*     */             
/* 102 */             if (next(globPattern, i) == '-') {
/* 103 */               regex.append('-');
/* 104 */               i++;
/*     */             } 
/*     */           } 
/* 107 */           hasRangeStart = false;
/* 108 */           last = Character.MIN_VALUE;
/* 109 */           while (i < globPattern.length()) {
/* 110 */             c = globPattern.charAt(i++);
/* 111 */             if (c == ']') {
/*     */               break;
/*     */             }
/* 114 */             if (c == '/' || (isDos && c == '\\')) {
/* 115 */               throw new PatternSyntaxException("Explicit 'name separator' in class", globPattern, i - 1);
/*     */             }
/*     */ 
/*     */             
/* 119 */             if (c == '\\' || c == '[' || (c == '&' && 
/* 120 */               next(globPattern, i) == '&'))
/*     */             {
/* 122 */               regex.append('\\');
/*     */             }
/* 124 */             regex.append(c);
/*     */             
/* 126 */             if (c == '-') {
/* 127 */               if (!hasRangeStart) {
/* 128 */                 throw new PatternSyntaxException("Invalid range", globPattern, i - 1);
/*     */               }
/*     */               
/* 131 */               if ((c = next(globPattern, i++)) == EOL || c == ']') {
/*     */                 break;
/*     */               }
/* 134 */               if (c < last) {
/* 135 */                 throw new PatternSyntaxException("Invalid range", globPattern, i - 3);
/*     */               }
/*     */               
/* 138 */               regex.append(c);
/* 139 */               hasRangeStart = false; continue;
/*     */             } 
/* 141 */             hasRangeStart = true;
/* 142 */             last = c;
/*     */           } 
/*     */           
/* 145 */           if (c != ']') {
/* 146 */             throw new PatternSyntaxException("Missing ']", globPattern, i - 1);
/*     */           }
/* 148 */           regex.append("]]");
/*     */           continue;
/*     */         case '{':
/* 151 */           if (inGroup) {
/* 152 */             throw new PatternSyntaxException("Cannot nest groups", globPattern, i - 1);
/*     */           }
/*     */           
/* 155 */           regex.append("(?:(?:");
/* 156 */           inGroup = true;
/*     */           continue;
/*     */         case '}':
/* 159 */           if (inGroup) {
/* 160 */             regex.append("))");
/* 161 */             inGroup = false; continue;
/*     */           } 
/* 163 */           regex.append('}');
/*     */           continue;
/*     */         
/*     */         case ',':
/* 167 */           if (inGroup) {
/* 168 */             regex.append(")|(?:"); continue;
/*     */           } 
/* 170 */           regex.append(',');
/*     */           continue;
/*     */         
/*     */         case '*':
/* 174 */           if (next(globPattern, i) == '*') {
/*     */             
/* 176 */             regex.append(".*");
/* 177 */             i++;
/*     */             continue;
/*     */           } 
/* 180 */           if (isDos) {
/* 181 */             regex.append("[^\\\\]*"); continue;
/*     */           } 
/* 183 */           regex.append("[^/]*");
/*     */           continue;
/*     */ 
/*     */         
/*     */         case '?':
/* 188 */           if (isDos) {
/* 189 */             regex.append("[^\\\\]"); continue;
/*     */           } 
/* 191 */           regex.append("[^/]");
/*     */           continue;
/*     */       } 
/*     */ 
/*     */       
/* 196 */       if (isRegexMeta(c)) {
/* 197 */         regex.append('\\');
/*     */       }
/* 199 */       regex.append(c);
/*     */     } 
/*     */ 
/*     */     
/* 203 */     if (inGroup) {
/* 204 */       throw new PatternSyntaxException("Missing '}", globPattern, i - 1);
/*     */     }
/*     */     
/* 207 */     return regex.append('$').toString();
/*     */   }
/*     */   
/*     */   public static String toUnixRegexPattern(String globPattern) {
/* 211 */     return toRegexPattern(globPattern, false);
/*     */   }
/*     */   
/*     */   public static String toWindowsRegexPattern(String globPattern) {
/* 215 */     return toRegexPattern(globPattern, true);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Globs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */