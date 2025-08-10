/*     */ package org.apache.log4j.jdbc;
/*     */ 
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.log4j.helpers.PatternConverter;
/*     */ import org.apache.log4j.helpers.PatternParser;
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
/*     */ class JdbcPatternParser
/*     */ {
/*     */   private static final String QUESTION_MARK = "?";
/*     */   private static final char PERCENT_CHAR = '%';
/*  36 */   private static final Pattern STRING_LITERAL_PATTERN = Pattern.compile("'((?>[^']|'')+)'");
/*     */ 
/*     */ 
/*     */   
/*     */   private String parameterizedSql;
/*     */ 
/*     */   
/*  43 */   private final List<String> patternStringRepresentationList = new ArrayList<String>();
/*  44 */   private final List<PatternConverter> args = new ArrayList<PatternConverter>();
/*     */   
/*     */   JdbcPatternParser(String insertString) {
/*  47 */     init(insertString);
/*     */   }
/*     */   
/*     */   public String getParameterizedSql() {
/*  51 */     return this.parameterizedSql;
/*     */   }
/*     */   
/*     */   public List<String> getUnmodifiablePatternStringRepresentationList() {
/*  55 */     return Collections.unmodifiableList(this.patternStringRepresentationList);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  60 */     return "JdbcPatternParser{sql=" + this.parameterizedSql + ",args=" + this.patternStringRepresentationList + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(String insertString) {
/*  67 */     if (insertString == null) {
/*  68 */       throw new IllegalArgumentException("Null pattern");
/*     */     }
/*     */     
/*  71 */     Matcher m = STRING_LITERAL_PATTERN.matcher(insertString);
/*  72 */     StringBuffer sb = new StringBuffer();
/*  73 */     while (m.find()) {
/*  74 */       String matchedStr = m.group(1);
/*  75 */       if (matchedStr.indexOf('%') == -1) {
/*  76 */         replaceWithMatchedStr(m, sb);
/*     */         continue;
/*     */       } 
/*  79 */       replaceWithBind(m, sb, matchedStr);
/*     */     } 
/*     */     
/*  82 */     m.appendTail(sb);
/*  83 */     this.parameterizedSql = sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private void replaceWithMatchedStr(Matcher m, StringBuffer sb) {
/*  88 */     m.appendReplacement(sb, "'$1'");
/*     */   }
/*     */   
/*     */   private void replaceWithBind(Matcher m, StringBuffer sb, String matchedStr) {
/*  92 */     m.appendReplacement(sb, "?");
/*     */ 
/*     */     
/*  95 */     matchedStr = matchedStr.replaceAll("''", "'");
/*  96 */     this.patternStringRepresentationList.add(matchedStr);
/*  97 */     this.args.add((new PatternParser(matchedStr)).parse());
/*     */   }
/*     */   
/*     */   public void setParameters(PreparedStatement ps, LoggingEvent logEvent) throws SQLException {
/* 101 */     for (int i = 0; i < this.args.size(); i++) {
/* 102 */       PatternConverter head = this.args.get(i);
/* 103 */       String value = buildValueStr(logEvent, head);
/* 104 */       ps.setString(i + 1, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String buildValueStr(LoggingEvent logEvent, PatternConverter head) {
/* 109 */     StringBuffer buffer = new StringBuffer();
/* 110 */     PatternConverter c = head;
/* 111 */     while (c != null) {
/* 112 */       c.format(buffer, logEvent);
/* 113 */       c = c.next;
/*     */     } 
/* 115 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/jdbc/JdbcPatternParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */