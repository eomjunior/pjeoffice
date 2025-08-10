/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.regexp.RegexpMatcher;
/*     */ import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;
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
/*     */ public class RegexpPatternMapper
/*     */   implements FileNameMapper
/*     */ {
/*     */   private static final int DECIMAL = 10;
/*  38 */   protected RegexpMatcher reg = null;
/*  39 */   protected char[] to = null;
/*  40 */   protected StringBuffer result = new StringBuffer();
/*     */ 
/*     */   
/*     */   private boolean handleDirSep;
/*     */ 
/*     */   
/*     */   private int regexpOptions;
/*     */ 
/*     */ 
/*     */   
/*     */   public RegexpPatternMapper() throws BuildException {
/*  51 */     this.handleDirSep = false;
/*  52 */     this.regexpOptions = 0;
/*     */     this.reg = (new RegexpMatcherFactory()).newRegexpMatcher();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandleDirSep(boolean handleDirSep) {
/*  61 */     this.handleDirSep = handleDirSep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/*  72 */     this.regexpOptions = RegexpUtil.asOptions(caseSensitive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String from) throws BuildException {
/*  82 */     if (from == null) {
/*  83 */       throw new BuildException("this mapper requires a 'from' attribute");
/*     */     }
/*     */     try {
/*  86 */       this.reg.setPattern(from);
/*  87 */     } catch (NoClassDefFoundError e) {
/*     */ 
/*     */       
/*  90 */       throw new BuildException("Cannot load regular expression matcher", e);
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
/*     */   public void setTo(String to) {
/* 102 */     if (to == null) {
/* 103 */       throw new BuildException("this mapper requires a 'to' attribute");
/*     */     }
/* 105 */     this.to = to.toCharArray();
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
/*     */   public String[] mapFileName(String sourceFileName) {
/* 118 */     if (sourceFileName == null) {
/* 119 */       return null;
/*     */     }
/* 121 */     if (this.handleDirSep && 
/* 122 */       sourceFileName.contains("\\")) {
/* 123 */       sourceFileName = sourceFileName.replace('\\', '/');
/*     */     }
/*     */     
/* 126 */     if (this.reg == null || this.to == null || 
/* 127 */       !this.reg.matches(sourceFileName, this.regexpOptions)) {
/* 128 */       return null;
/*     */     }
/* 130 */     return new String[] { replaceReferences(sourceFileName) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String replaceReferences(String source) {
/* 140 */     List<String> v = this.reg.getGroups(source, this.regexpOptions);
/*     */     
/* 142 */     this.result.setLength(0);
/* 143 */     for (int i = 0; i < this.to.length; i++) {
/* 144 */       if (this.to[i] == '\\') {
/* 145 */         if (++i < this.to.length) {
/* 146 */           int value = Character.digit(this.to[i], 10);
/* 147 */           if (value > -1) {
/* 148 */             this.result.append(v.get(value));
/*     */           } else {
/* 150 */             this.result.append(this.to[i]);
/*     */           } 
/*     */         } else {
/*     */           
/* 154 */           this.result.append('\\');
/*     */         } 
/*     */       } else {
/* 157 */         this.result.append(this.to[i]);
/*     */       } 
/*     */     } 
/* 160 */     return this.result.substring(0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/RegexpPatternMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */