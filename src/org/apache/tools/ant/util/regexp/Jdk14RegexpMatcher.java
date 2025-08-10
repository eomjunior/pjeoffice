/*     */ package org.apache.tools.ant.util.regexp;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jdk14RegexpMatcher
/*     */   implements RegexpMatcher
/*     */ {
/*     */   private String pattern;
/*     */   
/*     */   public void setPattern(String pattern) {
/*  43 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/*  53 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Pattern getCompiledPattern(int options) throws BuildException {
/*     */     try {
/*  65 */       return Pattern.compile(this.pattern, getCompilerOptions(options));
/*  66 */     } catch (PatternSyntaxException e) {
/*  67 */       throw new BuildException(e);
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
/*     */   public boolean matches(String argument) throws BuildException {
/*  79 */     return matches(argument, 0);
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
/*     */   public boolean matches(String input, int options) throws BuildException {
/*     */     try {
/*  93 */       return getCompiledPattern(options).matcher(input).find();
/*  94 */     } catch (Exception e) {
/*  95 */       throw new BuildException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<String> getGroups(String argument) throws BuildException {
/* 112 */     return getGroups(argument, 0);
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
/*     */   public Vector<String> getGroups(String input, int options) throws BuildException {
/* 129 */     Pattern p = getCompiledPattern(options);
/* 130 */     Matcher matcher = p.matcher(input);
/* 131 */     if (!matcher.find()) {
/* 132 */       return null;
/*     */     }
/* 134 */     Vector<String> v = new Vector<>();
/* 135 */     int cnt = matcher.groupCount();
/* 136 */     for (int i = 0; i <= cnt; i++) {
/* 137 */       String match = matcher.group(i);
/*     */       
/* 139 */       if (match == null) {
/* 140 */         match = "";
/*     */       }
/* 142 */       v.add(match);
/*     */     } 
/* 144 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getCompilerOptions(int options) {
/* 154 */     int cOptions = 1;
/*     */     
/* 156 */     if (RegexpUtil.hasFlag(options, 256)) {
/* 157 */       cOptions |= 0x2;
/*     */     }
/* 159 */     if (RegexpUtil.hasFlag(options, 4096)) {
/* 160 */       cOptions |= 0x8;
/*     */     }
/* 162 */     if (RegexpUtil.hasFlag(options, 65536)) {
/* 163 */       cOptions |= 0x20;
/*     */     }
/*     */     
/* 166 */     return cOptions;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/Jdk14RegexpMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */