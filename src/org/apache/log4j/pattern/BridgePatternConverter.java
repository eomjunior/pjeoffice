/*     */ package org.apache.log4j.pattern;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.helpers.PatternConverter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BridgePatternConverter
/*     */   extends PatternConverter
/*     */ {
/*     */   private LoggingEventPatternConverter[] patternConverters;
/*     */   private FormattingInfo[] patternFields;
/*     */   private boolean handlesExceptions = false;
/*     */   
/*     */   public BridgePatternConverter(String pattern) {
/*  61 */     List converters = new ArrayList();
/*  62 */     List fields = new ArrayList();
/*  63 */     Map converterRegistry = null;
/*     */     
/*  65 */     PatternParser.parse(pattern, converters, fields, converterRegistry, PatternParser.getPatternLayoutRules());
/*     */     
/*  67 */     this.patternConverters = new LoggingEventPatternConverter[converters.size()];
/*  68 */     this.patternFields = new FormattingInfo[converters.size()];
/*     */     
/*  70 */     int i = 0;
/*  71 */     Iterator converterIter = converters.iterator();
/*  72 */     Iterator<FormattingInfo> fieldIter = fields.iterator();
/*     */     
/*  74 */     while (converterIter.hasNext()) {
/*  75 */       Object converter = converterIter.next();
/*     */       
/*  77 */       if (converter instanceof LoggingEventPatternConverter) {
/*  78 */         this.patternConverters[i] = (LoggingEventPatternConverter)converter;
/*  79 */         this.handlesExceptions |= this.patternConverters[i].handlesThrowable();
/*     */       } else {
/*  81 */         this.patternConverters[i] = new LiteralPatternConverter("");
/*     */       } 
/*     */       
/*  84 */       if (fieldIter.hasNext()) {
/*  85 */         this.patternFields[i] = fieldIter.next();
/*     */       } else {
/*  87 */         this.patternFields[i] = FormattingInfo.getDefault();
/*     */       } 
/*     */       
/*  90 */       i++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String convert(LoggingEvent event) {
/* 101 */     StringBuffer sbuf = new StringBuffer();
/* 102 */     format(sbuf, event);
/*     */     
/* 104 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(StringBuffer sbuf, LoggingEvent e) {
/* 114 */     for (int i = 0; i < this.patternConverters.length; i++) {
/* 115 */       int startField = sbuf.length();
/* 116 */       this.patternConverters[i].format(e, sbuf);
/* 117 */       this.patternFields[i].format(startField, sbuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoresThrowable() {
/* 128 */     return !this.handlesExceptions;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/pattern/BridgePatternConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */