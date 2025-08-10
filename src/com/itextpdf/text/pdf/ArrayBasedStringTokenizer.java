/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ArrayBasedStringTokenizer
/*     */ {
/*     */   private final Pattern regex;
/*     */   
/*     */   public ArrayBasedStringTokenizer(String[] tokens) {
/*  63 */     this.regex = Pattern.compile(getRegexFromTokens(tokens));
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] tokenize(String text) {
/*  68 */     List<String> tokens = new ArrayList<String>();
/*     */     
/*  70 */     Matcher matcher = this.regex.matcher(text);
/*     */     
/*  72 */     int endIndexOfpreviousMatch = 0;
/*     */     
/*  74 */     while (matcher.find()) {
/*     */       
/*  76 */       int startIndexOfMatch = matcher.start();
/*     */       
/*  78 */       String previousToken = text.substring(endIndexOfpreviousMatch, startIndexOfMatch);
/*     */       
/*  80 */       if (previousToken.length() > 0) {
/*  81 */         tokens.add(previousToken);
/*     */       }
/*     */       
/*  84 */       String currentMatch = matcher.group();
/*     */ 
/*     */ 
/*     */       
/*  88 */       tokens.add(currentMatch);
/*     */       
/*  90 */       endIndexOfpreviousMatch = matcher.end();
/*     */     } 
/*     */ 
/*     */     
/*  94 */     String tail = text.substring(endIndexOfpreviousMatch, text.length());
/*     */     
/*  96 */     if (tail.length() > 0) {
/*  97 */       tokens.add(tail);
/*     */     }
/*     */     
/* 100 */     return tokens.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getRegexFromTokens(String[] tokens) {
/* 105 */     StringBuilder regexBuilder = new StringBuilder(100);
/*     */     
/* 107 */     for (String token : tokens) {
/* 108 */       regexBuilder.append("(").append(token).append(")|");
/*     */     }
/*     */     
/* 111 */     regexBuilder.setLength(regexBuilder.length() - 1);
/*     */     
/* 113 */     String regex = regexBuilder.toString();
/*     */     
/* 115 */     return regex;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ArrayBasedStringTokenizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */