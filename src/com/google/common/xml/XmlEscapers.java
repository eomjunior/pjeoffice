/*     */ package com.google.common.xml;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.escape.Escaper;
/*     */ import com.google.common.escape.Escapers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public class XmlEscapers
/*     */ {
/*     */   private static final char MIN_ASCII_CONTROL_CHAR = '\000';
/*     */   private static final char MAX_ASCII_CONTROL_CHAR = '\037';
/*     */   private static final Escaper XML_ESCAPER;
/*     */   private static final Escaper XML_CONTENT_ESCAPER;
/*     */   private static final Escaper XML_ATTRIBUTE_ESCAPER;
/*     */   
/*     */   public static Escaper xmlContentEscaper() {
/*  74 */     return XML_CONTENT_ESCAPER;
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
/*     */   public static Escaper xmlAttributeEscaper() {
/*  98 */     return XML_ATTRIBUTE_ESCAPER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 106 */     Escapers.Builder builder = Escapers.builder();
/*     */ 
/*     */ 
/*     */     
/* 110 */     builder.setSafeRange(false, '�');
/*     */     
/* 112 */     builder.setUnsafeReplacement("�");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     for (char c = Character.MIN_VALUE; c <= '\037'; c = (char)(c + 1)) {
/* 124 */       if (c != '\t' && c != '\n' && c != '\r') {
/* 125 */         builder.addEscape(c, "�");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 131 */     builder.addEscape('&', "&amp;");
/* 132 */     builder.addEscape('<', "&lt;");
/* 133 */     builder.addEscape('>', "&gt;");
/* 134 */     XML_CONTENT_ESCAPER = builder.build();
/* 135 */     builder.addEscape('\'', "&apos;");
/* 136 */     builder.addEscape('"', "&quot;");
/* 137 */     XML_ESCAPER = builder.build();
/* 138 */     builder.addEscape('\t', "&#x9;");
/* 139 */     builder.addEscape('\n', "&#xA;");
/* 140 */     builder.addEscape('\r', "&#xD;");
/* 141 */     XML_ATTRIBUTE_ESCAPER = builder.build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/xml/XmlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */