/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharSet
/*     */   extends EnumeratedAttribute
/*     */ {
/*  33 */   private static final List<String> VALUES = new ArrayList<>();
/*     */   
/*     */   static {
/*  36 */     for (Map.Entry<String, Charset> entry : Charset.availableCharsets().entrySet()) {
/*  37 */       VALUES.add(entry.getKey());
/*  38 */       VALUES.addAll(((Charset)entry.getValue()).aliases());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSet(String value) {
/*  53 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet getDefault() {
/*  61 */     return new CharSet(Charset.defaultCharset().name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet getAscii() {
/*  69 */     return new CharSet(StandardCharsets.US_ASCII.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSet getUtf8() {
/*  77 */     return new CharSet(StandardCharsets.UTF_8.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equivalent(CharSet cs) {
/*  86 */     return getCharset().name().equals(cs.getCharset().name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/*  94 */     return Charset.forName(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getValues() {
/* 103 */     return VALUES.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setValue(String value) {
/* 113 */     String realValue = value;
/* 114 */     if (value == null || value.isEmpty()) {
/* 115 */       realValue = Charset.defaultCharset().name();
/*     */     } else {
/* 117 */       for (String v : Arrays.<String>asList(new String[] { value, value.toLowerCase(), value.toUpperCase() })) {
/* 118 */         if (VALUES.contains(v)) {
/* 119 */           realValue = v;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 124 */     super.setValue(realValue);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/CharSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */