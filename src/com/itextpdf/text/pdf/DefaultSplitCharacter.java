/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.SplitCharacter;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSplitCharacter
/*     */   implements SplitCharacter
/*     */ {
/*  63 */   private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2,4}-\\d{2}-\\d{2,4})");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] characters;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultSplitCharacter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultSplitCharacter(char character) {
/*  85 */     this(new char[] { character });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultSplitCharacter(char[] characters) {
/*  94 */     this.characters = characters;
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
/*     */   public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
/* 119 */     char[] ccTmp = checkDatePattern(String.valueOf(cc));
/* 120 */     char c = getCurrentCharacter(current, ccTmp, ck);
/*     */     
/* 122 */     if (this.characters != null) {
/* 123 */       for (int i = 0; i < this.characters.length; i++) {
/* 124 */         if (c == this.characters[i]) {
/* 125 */           return true;
/*     */         }
/*     */       } 
/* 128 */       return false;
/*     */     } 
/*     */     
/* 131 */     if (c <= ' ' || c == '-' || c == '‐') {
/* 132 */       return true;
/*     */     }
/* 134 */     if (c < ' ')
/* 135 */       return false; 
/* 136 */     return ((c >= ' ' && c <= '​') || (c >= '⺀' && c < '힠') || (c >= '豈' && c < 'ﬀ') || (c >= '︰' && c < '﹐') || (c >= '｡' && c < 'ﾠ'));
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
/*     */   protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
/* 152 */     if (ck == null) {
/* 153 */       return cc[current];
/*     */     }
/* 155 */     return (char)ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
/*     */   }
/*     */   
/*     */   private char[] checkDatePattern(String data) {
/* 159 */     if (data.contains("-")) {
/* 160 */       Matcher m = DATE_PATTERN.matcher(data);
/* 161 */       if (m.find()) {
/* 162 */         String tmpData = m.group(1).replace('-', '‑');
/* 163 */         data = data.replaceAll(m.group(1), tmpData);
/*     */       } 
/*     */     } 
/* 166 */     return data.toCharArray();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/DefaultSplitCharacter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */