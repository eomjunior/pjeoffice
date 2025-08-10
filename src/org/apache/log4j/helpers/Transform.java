/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  32 */   private static final int CDATA_END_LEN = "]]>".length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeTags(String input) {
/*  46 */     if (input == null || input.length() == 0 || (input.indexOf('"') == -1 && input.indexOf('&') == -1 && input
/*  47 */       .indexOf('<') == -1 && input.indexOf('>') == -1)) {
/*  48 */       return input;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*  55 */     char ch = ' ';
/*     */     
/*  57 */     int len = input.length();
/*  58 */     for (int i = 0; i < len; i++) {
/*  59 */       ch = input.charAt(i);
/*  60 */       if (ch > '>') {
/*  61 */         buf.append(ch);
/*  62 */       } else if (ch == '<') {
/*  63 */         buf.append("&lt;");
/*  64 */       } else if (ch == '>') {
/*  65 */         buf.append("&gt;");
/*  66 */       } else if (ch == '&') {
/*  67 */         buf.append("&amp;");
/*  68 */       } else if (ch == '"') {
/*  69 */         buf.append("&quot;");
/*     */       } else {
/*  71 */         buf.append(ch);
/*     */       } 
/*     */     } 
/*  74 */     return buf.toString();
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
/*     */   public static void appendEscapingCDATA(StringBuffer buf, String str) {
/*  88 */     if (str != null) {
/*  89 */       int end = str.indexOf("]]>");
/*  90 */       if (end < 0) {
/*  91 */         buf.append(str);
/*     */       } else {
/*  93 */         int start = 0;
/*  94 */         while (end > -1) {
/*  95 */           buf.append(str.substring(start, end));
/*  96 */           buf.append("]]>]]&gt;<![CDATA[");
/*  97 */           start = end + CDATA_END_LEN;
/*  98 */           if (start < str.length()) {
/*  99 */             end = str.indexOf("]]>", start);
/*     */             continue;
/*     */           } 
/*     */           return;
/*     */         } 
/* 104 */         buf.append(str.substring(start));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/Transform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */