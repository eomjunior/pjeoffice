/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SelectorUtils
/*     */ {
/*     */   public static final String DEEP_TREE_MATCH = "**";
/*  46 */   private static final SelectorUtils instance = new SelectorUtils();
/*  47 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SelectorUtils getInstance() {
/*  60 */     return instance;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchPatternStart(String pattern, String str) {
/*  80 */     return matchPatternStart(pattern, str, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
/* 107 */     if (str.startsWith(File.separator) != pattern
/* 108 */       .startsWith(File.separator)) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     String[] patDirs = tokenizePathAsArray(pattern);
/* 113 */     String[] strDirs = tokenizePathAsArray(str);
/* 114 */     return matchPatternStart(patDirs, strDirs, isCaseSensitive);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matchPatternStart(String[] patDirs, String[] strDirs, boolean isCaseSensitive) {
/* 138 */     int patIdxStart = 0;
/* 139 */     int patIdxEnd = patDirs.length - 1;
/* 140 */     int strIdxStart = 0;
/* 141 */     int strIdxEnd = strDirs.length - 1;
/*     */ 
/*     */     
/* 144 */     while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
/* 145 */       String patDir = patDirs[patIdxStart];
/* 146 */       if (patDir.equals("**")) {
/*     */         break;
/*     */       }
/* 149 */       if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
/* 150 */         return false;
/*     */       }
/* 152 */       patIdxStart++;
/* 153 */       strIdxStart++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     return (strIdxStart > strIdxEnd || patIdxStart <= patIdxEnd);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchPath(String pattern, String str) {
/* 179 */     String[] patDirs = tokenizePathAsArray(pattern);
/* 180 */     return matchPath(patDirs, tokenizePathAsArray(str), true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
/* 203 */     String[] patDirs = tokenizePathAsArray(pattern);
/* 204 */     return matchPath(patDirs, tokenizePathAsArray(str), isCaseSensitive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matchPath(String[] tokenizedPattern, String[] strDirs, boolean isCaseSensitive) {
/* 213 */     int patIdxStart = 0;
/* 214 */     int patIdxEnd = tokenizedPattern.length - 1;
/* 215 */     int strIdxStart = 0;
/* 216 */     int strIdxEnd = strDirs.length - 1;
/*     */ 
/*     */     
/* 219 */     while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
/* 220 */       String patDir = tokenizedPattern[patIdxStart];
/* 221 */       if (patDir.equals("**")) {
/*     */         break;
/*     */       }
/* 224 */       if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
/* 225 */         return false;
/*     */       }
/* 227 */       patIdxStart++;
/* 228 */       strIdxStart++;
/*     */     } 
/* 230 */     if (strIdxStart > strIdxEnd) {
/*     */       
/* 232 */       for (int j = patIdxStart; j <= patIdxEnd; j++) {
/* 233 */         if (!tokenizedPattern[j].equals("**")) {
/* 234 */           return false;
/*     */         }
/*     */       } 
/* 237 */       return true;
/*     */     } 
/* 239 */     if (patIdxStart > patIdxEnd)
/*     */     {
/* 241 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 245 */     while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
/* 246 */       String patDir = tokenizedPattern[patIdxEnd];
/* 247 */       if (patDir.equals("**")) {
/*     */         break;
/*     */       }
/* 250 */       if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
/* 251 */         return false;
/*     */       }
/* 253 */       patIdxEnd--;
/* 254 */       strIdxEnd--;
/*     */     } 
/* 256 */     if (strIdxStart > strIdxEnd) {
/*     */       
/* 258 */       for (int j = patIdxStart; j <= patIdxEnd; j++) {
/* 259 */         if (!tokenizedPattern[j].equals("**")) {
/* 260 */           return false;
/*     */         }
/*     */       } 
/* 263 */       return true;
/*     */     } 
/*     */     
/* 266 */     while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
/* 267 */       int patIdxTmp = -1;
/* 268 */       for (int j = patIdxStart + 1; j <= patIdxEnd; j++) {
/* 269 */         if (tokenizedPattern[j].equals("**")) {
/* 270 */           patIdxTmp = j;
/*     */           break;
/*     */         } 
/*     */       } 
/* 274 */       if (patIdxTmp == patIdxStart + 1) {
/*     */         
/* 276 */         patIdxStart++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 281 */       int patLength = patIdxTmp - patIdxStart - 1;
/* 282 */       int strLength = strIdxEnd - strIdxStart + 1;
/* 283 */       int foundIdx = -1;
/*     */       
/* 285 */       for (int k = 0; k <= strLength - patLength; ) {
/* 286 */         for (int m = 0; m < patLength; m++) {
/* 287 */           String subPat = tokenizedPattern[patIdxStart + m + 1];
/* 288 */           String subStr = strDirs[strIdxStart + k + m];
/* 289 */           if (!match(subPat, subStr, isCaseSensitive)) {
/*     */             k++; continue;
/*     */           } 
/*     */         } 
/* 293 */         foundIdx = strIdxStart + k;
/*     */       } 
/*     */       
/* 296 */       if (foundIdx == -1) {
/* 297 */         return false;
/*     */       }
/*     */       
/* 300 */       patIdxStart = patIdxTmp;
/* 301 */       strIdxStart = foundIdx + patLength;
/*     */     } 
/*     */     
/* 304 */     for (int i = patIdxStart; i <= patIdxEnd; i++) {
/* 305 */       if (!"**".equals(tokenizedPattern[i])) {
/* 306 */         return false;
/*     */       }
/*     */     } 
/* 309 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean match(String pattern, String str) {
/* 327 */     return match(pattern, str, true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean match(String pattern, String str, boolean caseSensitive) {
/* 349 */     char[] patArr = pattern.toCharArray();
/* 350 */     char[] strArr = str.toCharArray();
/* 351 */     int patIdxStart = 0;
/* 352 */     int patIdxEnd = patArr.length - 1;
/* 353 */     int strIdxStart = 0;
/* 354 */     int strIdxEnd = strArr.length - 1;
/*     */     
/* 356 */     boolean containsStar = false;
/* 357 */     for (char ch : patArr) {
/* 358 */       if (ch == '*') {
/* 359 */         containsStar = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 364 */     if (!containsStar) {
/*     */       
/* 366 */       if (patIdxEnd != strIdxEnd) {
/* 367 */         return false;
/*     */       }
/* 369 */       for (int i = 0; i <= patIdxEnd; i++) {
/* 370 */         char ch = patArr[i];
/* 371 */         if (ch != '?' && different(caseSensitive, ch, strArr[i])) {
/* 372 */           return false;
/*     */         }
/*     */       } 
/* 375 */       return true;
/*     */     } 
/*     */     
/* 378 */     if (patIdxEnd == 0) {
/* 379 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     while (true) {
/* 384 */       char ch = patArr[patIdxStart];
/* 385 */       if (ch == '*' || strIdxStart > strIdxEnd) {
/*     */         break;
/*     */       }
/* 388 */       if (ch != '?' && 
/* 389 */         different(caseSensitive, ch, strArr[strIdxStart])) {
/* 390 */         return false;
/*     */       }
/* 392 */       patIdxStart++;
/* 393 */       strIdxStart++;
/*     */     } 
/* 395 */     if (strIdxStart > strIdxEnd)
/*     */     {
/*     */       
/* 398 */       return allStars(patArr, patIdxStart, patIdxEnd);
/*     */     }
/*     */ 
/*     */     
/*     */     while (true) {
/* 403 */       char ch = patArr[patIdxEnd];
/* 404 */       if (ch == '*' || strIdxStart > strIdxEnd) {
/*     */         break;
/*     */       }
/* 407 */       if (ch != '?' && different(caseSensitive, ch, strArr[strIdxEnd])) {
/* 408 */         return false;
/*     */       }
/* 410 */       patIdxEnd--;
/* 411 */       strIdxEnd--;
/*     */     } 
/* 413 */     if (strIdxStart > strIdxEnd)
/*     */     {
/*     */       
/* 416 */       return allStars(patArr, patIdxStart, patIdxEnd);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 421 */     while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
/* 422 */       int patIdxTmp = -1;
/* 423 */       for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
/* 424 */         if (patArr[i] == '*') {
/* 425 */           patIdxTmp = i;
/*     */           break;
/*     */         } 
/*     */       } 
/* 429 */       if (patIdxTmp == patIdxStart + 1) {
/*     */         
/* 431 */         patIdxStart++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 436 */       int patLength = patIdxTmp - patIdxStart - 1;
/* 437 */       int strLength = strIdxEnd - strIdxStart + 1;
/* 438 */       int foundIdx = -1;
/*     */       
/* 440 */       for (int j = 0; j <= strLength - patLength; ) {
/* 441 */         for (int k = 0; k < patLength; k++) {
/* 442 */           char ch = patArr[patIdxStart + k + 1];
/* 443 */           if (ch != '?' && different(caseSensitive, ch, strArr[strIdxStart + j + k])) {
/*     */             j++;
/*     */             continue;
/*     */           } 
/*     */         } 
/* 448 */         foundIdx = strIdxStart + j;
/*     */       } 
/*     */ 
/*     */       
/* 452 */       if (foundIdx == -1) {
/* 453 */         return false;
/*     */       }
/* 455 */       patIdxStart = patIdxTmp;
/* 456 */       strIdxStart = foundIdx + patLength;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 461 */     return allStars(patArr, patIdxStart, patIdxEnd);
/*     */   }
/*     */   
/*     */   private static boolean allStars(char[] chars, int start, int end) {
/* 465 */     for (int i = start; i <= end; i++) {
/* 466 */       if (chars[i] != '*') {
/* 467 */         return false;
/*     */       }
/*     */     } 
/* 470 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean different(boolean caseSensitive, char ch, char other) {
/* 475 */     return caseSensitive ? (
/* 476 */       (ch != other)) : (
/* 477 */       (Character.toUpperCase(ch) != Character.toUpperCase(other)));
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
/*     */   public static Vector<String> tokenizePath(String path) {
/* 489 */     return tokenizePath(path, File.separator);
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
/*     */   public static Vector<String> tokenizePath(String path, String separator) {
/* 502 */     Vector<String> ret = new Vector<>();
/* 503 */     if (FileUtils.isAbsolutePath(path)) {
/* 504 */       String[] s = FILE_UTILS.dissect(path);
/* 505 */       ret.add(s[0]);
/* 506 */       path = s[1];
/*     */     } 
/* 508 */     StringTokenizer st = new StringTokenizer(path, separator);
/* 509 */     while (st.hasMoreTokens()) {
/* 510 */       ret.addElement(st.nextToken());
/*     */     }
/* 512 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] tokenizePathAsArray(String path) {
/* 520 */     String root = null;
/* 521 */     if (FileUtils.isAbsolutePath(path)) {
/* 522 */       String[] s = FILE_UTILS.dissect(path);
/* 523 */       root = s[0];
/* 524 */       path = s[1];
/*     */     } 
/* 526 */     char sep = File.separatorChar;
/* 527 */     int start = 0;
/* 528 */     int len = path.length();
/* 529 */     int count = 0;
/* 530 */     for (int pos = 0; pos < len; pos++) {
/* 531 */       if (path.charAt(pos) == sep) {
/* 532 */         if (pos != start) {
/* 533 */           count++;
/*     */         }
/* 535 */         start = pos + 1;
/*     */       } 
/*     */     } 
/* 538 */     if (len != start) {
/* 539 */       count++;
/*     */     }
/* 541 */     String[] l = new String[count + ((root == null) ? 0 : 1)];
/*     */     
/* 543 */     if (root != null) {
/* 544 */       l[0] = root;
/* 545 */       count = 1;
/*     */     } else {
/* 547 */       count = 0;
/*     */     } 
/* 549 */     start = 0;
/* 550 */     for (int i = 0; i < len; i++) {
/* 551 */       if (path.charAt(i) == sep) {
/* 552 */         if (i != start) {
/* 553 */           String tok = path.substring(start, i);
/* 554 */           l[count++] = tok;
/*     */         } 
/* 556 */         start = i + 1;
/*     */       } 
/*     */     } 
/* 559 */     if (len != start) {
/* 560 */       String tok = path.substring(start);
/* 561 */       l[count] = tok;
/*     */     } 
/* 563 */     return l;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOutOfDate(File src, File target, int granularity) {
/* 581 */     return (src.exists() && (!target.exists() || src
/* 582 */       .lastModified() - granularity > target.lastModified()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOutOfDate(Resource src, Resource target, int granularity) {
/* 601 */     return isOutOfDate(src, target, granularity);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOutOfDate(Resource src, Resource target, long granularity) {
/* 619 */     long sourceLastModified = src.getLastModified();
/* 620 */     long targetLastModified = target.getLastModified();
/* 621 */     return (src.isExists() && (sourceLastModified == 0L || targetLastModified == 0L || sourceLastModified - granularity > targetLastModified));
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
/*     */   public static String removeWhitespace(String input) {
/* 636 */     StringBuilder result = new StringBuilder();
/* 637 */     if (input != null) {
/* 638 */       StringTokenizer st = new StringTokenizer(input);
/* 639 */       while (st.hasMoreTokens()) {
/* 640 */         result.append(st.nextToken());
/*     */       }
/*     */     } 
/* 643 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasWildcards(String input) {
/* 652 */     return (input.contains("*") || input.contains("?"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String rtrimWildcardTokens(String input) {
/* 661 */     return (new TokenizedPattern(input)).rtrimWildcardTokens().toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SelectorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */