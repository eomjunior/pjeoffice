/*     */ package com.itextpdf.text.html;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public final class HtmlEncoder
/*     */ {
/*  93 */   private static final String[] HTML_CODE = new String[256];
/*     */   static {
/*     */     int i;
/*  96 */     for (i = 0; i < 10; i++) {
/*  97 */       HTML_CODE[i] = "&#00" + i + ";";
/*     */     }
/*     */     
/* 100 */     for (i = 10; i < 32; i++) {
/* 101 */       HTML_CODE[i] = "&#0" + i + ";";
/*     */     }
/*     */     
/* 104 */     for (i = 32; i < 128; i++) {
/* 105 */       HTML_CODE[i] = String.valueOf((char)i);
/*     */     }
/*     */ 
/*     */     
/* 109 */     HTML_CODE[9] = "\t";
/* 110 */     HTML_CODE[10] = "<br />\n";
/* 111 */     HTML_CODE[34] = "&quot;";
/* 112 */     HTML_CODE[38] = "&amp;";
/* 113 */     HTML_CODE[60] = "&lt;";
/* 114 */     HTML_CODE[62] = "&gt;";
/*     */     
/* 116 */     for (i = 128; i < 256; i++) {
/* 117 */       HTML_CODE[i] = "&#" + i + ";";
/*     */     }
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
/*     */   public static String encode(String string) {
/* 130 */     int n = string.length();
/*     */     
/* 132 */     StringBuffer buffer = new StringBuffer();
/*     */     
/* 134 */     for (int i = 0; i < n; i++) {
/* 135 */       char character = string.charAt(i);
/*     */       
/* 137 */       if (character < 'Ā') {
/* 138 */         buffer.append(HTML_CODE[character]);
/*     */       }
/*     */       else {
/*     */         
/* 142 */         buffer.append("&#").append(character).append(';');
/*     */       } 
/*     */     } 
/* 145 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encode(BaseColor color) {
/* 155 */     StringBuffer buffer = new StringBuffer("#");
/* 156 */     if (color.getRed() < 16) {
/* 157 */       buffer.append('0');
/*     */     }
/* 159 */     buffer.append(Integer.toString(color.getRed(), 16));
/* 160 */     if (color.getGreen() < 16) {
/* 161 */       buffer.append('0');
/*     */     }
/* 163 */     buffer.append(Integer.toString(color.getGreen(), 16));
/* 164 */     if (color.getBlue() < 16) {
/* 165 */       buffer.append('0');
/*     */     }
/* 167 */     buffer.append(Integer.toString(color.getBlue(), 16));
/* 168 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getAlignment(int alignment) {
/* 178 */     switch (alignment) {
/*     */       case 0:
/* 180 */         return "left";
/*     */       case 1:
/* 182 */         return "center";
/*     */       case 2:
/* 184 */         return "right";
/*     */       case 3:
/*     */       case 8:
/* 187 */         return "justify";
/*     */       case 4:
/* 189 */         return "top";
/*     */       case 5:
/* 191 */         return "middle";
/*     */       case 6:
/* 193 */         return "bottom";
/*     */       case 7:
/* 195 */         return "baseline";
/*     */     } 
/* 197 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   private static final Set<String> NEWLINETAGS = new HashSet<String>();
/*     */ 
/*     */   
/*     */   static {
/* 209 */     NEWLINETAGS.add("p");
/* 210 */     NEWLINETAGS.add("blockquote");
/* 211 */     NEWLINETAGS.add("br");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNewLineTag(String tag) {
/* 219 */     return NEWLINETAGS.contains(tag);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/HtmlEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */