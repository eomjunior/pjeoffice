/*     */ package org.apache.tools.ant.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.util.regexp.RegexpUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LineContainsRegExp
/*     */   extends BaseParamFilterReader
/*     */   implements ChainableReader
/*     */ {
/*     */   private static final String REGEXP_KEY = "regexp";
/*     */   private static final String NEGATE_KEY = "negate";
/*     */   private static final String CS_KEY = "casesensitive";
/*  61 */   private Vector<RegularExpression> regexps = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private String line = null;
/*     */   
/*     */   private boolean negate = false;
/*  71 */   private int regexpOptions = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineContainsRegExp() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineContainsRegExp(Reader in) {
/*  89 */     super(in);
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
/*     */   public int read() throws IOException {
/* 104 */     if (!getInitialized()) {
/* 105 */       initialize();
/* 106 */       setInitialized(true);
/*     */     } 
/*     */     
/* 109 */     int ch = -1;
/*     */     
/* 111 */     if (this.line != null) {
/* 112 */       ch = this.line.charAt(0);
/* 113 */       if (this.line.length() == 1) {
/* 114 */         this.line = null;
/*     */       } else {
/* 116 */         this.line = this.line.substring(1);
/*     */       } 
/*     */     } else {
/* 119 */       for (this.line = readLine(); this.line != null; this.line = readLine()) {
/* 120 */         boolean matches = true;
/* 121 */         for (RegularExpression regexp : this.regexps) {
/* 122 */           if (!regexp.getRegexp(getProject()).matches(this.line, this.regexpOptions)) {
/* 123 */             matches = false;
/*     */             break;
/*     */           } 
/*     */         } 
/* 127 */         if (matches ^ isNegated()) {
/*     */           break;
/*     */         }
/*     */       } 
/* 131 */       if (this.line != null) {
/* 132 */         return read();
/*     */       }
/*     */     } 
/* 135 */     return ch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredRegexp(RegularExpression regExp) {
/* 145 */     this.regexps.addElement(regExp);
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
/*     */   private void setRegexps(Vector<RegularExpression> regexps) {
/* 158 */     this.regexps = regexps;
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
/*     */   private Vector<RegularExpression> getRegexps() {
/* 172 */     return this.regexps;
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
/*     */   public Reader chain(Reader rdr) {
/* 186 */     LineContainsRegExp newFilter = new LineContainsRegExp(rdr);
/* 187 */     newFilter.setRegexps(getRegexps());
/* 188 */     newFilter.setNegate(isNegated());
/* 189 */     newFilter.setCaseSensitive(!RegexpUtil.hasFlag(this.regexpOptions, 256));
/*     */     
/* 191 */     return newFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNegate(boolean b) {
/* 199 */     this.negate = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean b) {
/* 208 */     this.regexpOptions = RegexpUtil.asOptions(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNegated() {
/* 216 */     return this.negate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegexp(String pattern) {
/* 225 */     RegularExpression regexp = new RegularExpression();
/* 226 */     regexp.setPattern(pattern);
/* 227 */     this.regexps.addElement(regexp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize() {
/* 234 */     Parameter[] params = getParameters();
/* 235 */     if (params != null)
/* 236 */       for (Parameter param : params) {
/* 237 */         if ("regexp".equals(param.getType())) {
/* 238 */           setRegexp(param.getValue());
/* 239 */         } else if ("negate".equals(param.getType())) {
/* 240 */           setNegate(Project.toBoolean(param.getValue()));
/* 241 */         } else if ("casesensitive".equals(param.getType())) {
/* 242 */           setCaseSensitive(Project.toBoolean(param.getValue()));
/*     */         } 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/LineContainsRegExp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */