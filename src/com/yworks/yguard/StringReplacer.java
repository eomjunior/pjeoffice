/*     */ package com.yworks.yguard;
/*     */ 
/*     */ import com.yworks.yguard.obf.GuardDB;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class StringReplacer
/*     */ {
/*     */   Pattern pattern;
/*     */   
/*     */   public StringReplacer(String patternString) {
/*  33 */     setPattern(patternString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String patternString) {
/*  43 */     this.pattern = Pattern.compile(patternString);
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
/*     */   public void replace(String in, StringBuffer result, Map map) {
/*  55 */     String line = in;
/*     */     
/*  57 */     result.setLength(0);
/*  58 */     Matcher matcher = this.pattern.matcher(line);
/*  59 */     String match = null;
/*  60 */     String replacement = null;
/*     */     
/*  62 */     boolean found = matcher.find();
/*  63 */     while (found) {
/*     */       
/*  65 */       match = line.substring(matcher.start(), matcher.end());
/*     */       
/*  67 */       replacement = (String)map.get(match);
/*  68 */       if (replacement == null) replacement = match; 
/*  69 */       if (replacement.indexOf('\\') >= 0) {
/*  70 */         replacement = replacement.replaceAll("\\\\", "\\\\\\\\");
/*     */       }
/*  72 */       if (replacement.indexOf('$') >= 0) {
/*  73 */         replacement = replacement.replaceAll("\\$", "\\\\\\$");
/*     */       }
/*  75 */       matcher.appendReplacement(result, replacement);
/*  76 */       found = matcher.find();
/*     */     } 
/*  78 */     matcher.appendTail(result);
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
/*     */   public void replace(Reader in, Writer out, GuardDB db, String separator) throws IOException {
/*  92 */     BufferedReader bin = new BufferedReader(in);
/*     */     
/*  94 */     StringBuffer result = new StringBuffer(80);
/*     */     String line;
/*  96 */     while ((line = bin.readLine()) != null) {
/*     */       
/*  98 */       result.setLength(0);
/*  99 */       Matcher matcher = this.pattern.matcher(line);
/*     */ 
/*     */       
/* 102 */       boolean found = matcher.find();
/* 103 */       while (found) {
/*     */         
/* 105 */         String replacement = "";
/* 106 */         String match = line.substring(matcher.start(), matcher.end());
/* 107 */         String[] parts = match.split(Pattern.quote(separator));
/* 108 */         List<String> mapped = db.translateItem(parts);
/* 109 */         while (mapped.size() < parts.length) {
/* 110 */           mapped.add(parts[mapped.size()]);
/*     */         }
/* 112 */         for (int i = 0; i < mapped.size(); i++) {
/* 113 */           if (i > 0) replacement = replacement + separator; 
/* 114 */           replacement = replacement + (String)mapped.get(i);
/*     */         } 
/* 116 */         if (replacement.indexOf('\\') >= 0) {
/* 117 */           replacement = replacement.replaceAll("\\\\", "\\\\\\\\");
/*     */         }
/* 119 */         if (replacement.indexOf('$') >= 0) {
/* 120 */           replacement = replacement.replaceAll("\\$", "\\\\\\$");
/*     */         }
/*     */         
/* 123 */         matcher.appendReplacement(result, replacement);
/* 124 */         found = matcher.find();
/*     */       } 
/* 126 */       matcher.appendTail(result);
/* 127 */       out.write(result.toString());
/* 128 */       out.write(10);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/StringReplacer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */