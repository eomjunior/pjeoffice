/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.spi.Filter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringMatchFilter
/*     */   extends Filter
/*     */ {
/*     */   public static final String STRING_TO_MATCH_OPTION = "StringToMatch";
/*     */   public static final String ACCEPT_ON_MATCH_OPTION = "AcceptOnMatch";
/*     */   boolean acceptOnMatch = true;
/*     */   String stringToMatch;
/*     */   
/*     */   public String[] getOptionStrings() {
/*  64 */     return new String[] { "StringToMatch", "AcceptOnMatch" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOption(String key, String value) {
/*  73 */     if (key.equalsIgnoreCase("StringToMatch")) {
/*  74 */       this.stringToMatch = value;
/*  75 */     } else if (key.equalsIgnoreCase("AcceptOnMatch")) {
/*  76 */       this.acceptOnMatch = OptionConverter.toBoolean(value, this.acceptOnMatch);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setStringToMatch(String s) {
/*  81 */     this.stringToMatch = s;
/*     */   }
/*     */   
/*     */   public String getStringToMatch() {
/*  85 */     return this.stringToMatch;
/*     */   }
/*     */   
/*     */   public void setAcceptOnMatch(boolean acceptOnMatch) {
/*  89 */     this.acceptOnMatch = acceptOnMatch;
/*     */   }
/*     */   
/*     */   public boolean getAcceptOnMatch() {
/*  93 */     return this.acceptOnMatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int decide(LoggingEvent event) {
/* 100 */     String msg = event.getRenderedMessage();
/*     */     
/* 102 */     if (msg == null || this.stringToMatch == null) {
/* 103 */       return 0;
/*     */     }
/* 105 */     if (msg.indexOf(this.stringToMatch) == -1) {
/* 106 */       return 0;
/*     */     }
/* 108 */     if (this.acceptOnMatch) {
/* 109 */       return 1;
/*     */     }
/* 111 */     return -1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/StringMatchFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */