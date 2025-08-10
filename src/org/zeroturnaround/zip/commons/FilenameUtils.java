/*     */ package org.zeroturnaround.zip.commons;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilenameUtils
/*     */ {
/*     */   public static final char EXTENSION_SEPARATOR = '.';
/* 100 */   public static final String EXTENSION_SEPARATOR_STR = Character.valueOf('.').toString();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char UNIX_SEPARATOR = '/';
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char WINDOWS_SEPARATOR = '\\';
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static final char SYSTEM_SEPARATOR = File.separatorChar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isSystemWindows() {
/* 131 */     return (SYSTEM_SEPARATOR == '\\');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSeparator(char ch) {
/* 142 */     return (ch == '/' || ch == '\\');
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getPrefixLength(String filename) {
/* 176 */     if (filename == null) {
/* 177 */       return -1;
/*     */     }
/* 179 */     int len = filename.length();
/* 180 */     if (len == 0) {
/* 181 */       return 0;
/*     */     }
/* 183 */     char ch0 = filename.charAt(0);
/* 184 */     if (ch0 == ':') {
/* 185 */       return -1;
/*     */     }
/* 187 */     if (len == 1) {
/* 188 */       if (ch0 == '~') {
/* 189 */         return 2;
/*     */       }
/* 191 */       return isSeparator(ch0) ? 1 : 0;
/*     */     } 
/*     */     
/* 194 */     if (ch0 == '~') {
/* 195 */       int posUnix = filename.indexOf('/', 1);
/* 196 */       int posWin = filename.indexOf('\\', 1);
/* 197 */       if (posUnix == -1 && posWin == -1) {
/* 198 */         return len + 1;
/*     */       }
/* 200 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/* 201 */       posWin = (posWin == -1) ? posUnix : posWin;
/* 202 */       return Math.min(posUnix, posWin) + 1;
/*     */     } 
/* 204 */     char ch1 = filename.charAt(1);
/* 205 */     if (ch1 == ':') {
/* 206 */       ch0 = Character.toUpperCase(ch0);
/* 207 */       if (ch0 >= 'A' && ch0 <= 'Z') {
/* 208 */         if (len == 2 || !isSeparator(filename.charAt(2))) {
/* 209 */           return 2;
/*     */         }
/* 211 */         return 3;
/*     */       } 
/* 213 */       return -1;
/*     */     } 
/*     */     
/* 216 */     if (isSeparator(ch0) && isSeparator(ch1)) {
/* 217 */       int posUnix = filename.indexOf('/', 2);
/* 218 */       int posWin = filename.indexOf('\\', 2);
/* 219 */       if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
/* 220 */         return -1;
/*     */       }
/* 222 */       posUnix = (posUnix == -1) ? posWin : posUnix;
/* 223 */       posWin = (posWin == -1) ? posUnix : posWin;
/* 224 */       return Math.min(posUnix, posWin) + 1;
/*     */     } 
/*     */     
/* 227 */     return isSeparator(ch0) ? 1 : 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/commons/FilenameUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */