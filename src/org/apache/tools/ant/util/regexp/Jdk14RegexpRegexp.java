/*    */ package org.apache.tools.ant.util.regexp;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jdk14RegexpRegexp
/*    */   extends Jdk14RegexpMatcher
/*    */   implements Regexp
/*    */ {
/*    */   private static final int DECIMAL = 10;
/*    */   
/*    */   protected int getSubsOptions(int options) {
/* 39 */     int subsOptions = 1;
/* 40 */     if (RegexpUtil.hasFlag(options, 16)) {
/* 41 */       subsOptions = 16;
/*    */     }
/* 43 */     return subsOptions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String substitute(String input, String argument, int options) throws BuildException {
/* 58 */     StringBuilder subst = new StringBuilder();
/* 59 */     for (int i = 0; i < argument.length(); i++) {
/* 60 */       char c = argument.charAt(i);
/* 61 */       if (c == '$') {
/* 62 */         subst.append('\\');
/* 63 */         subst.append('$');
/* 64 */       } else if (c == '\\') {
/* 65 */         if (++i < argument.length()) {
/* 66 */           c = argument.charAt(i);
/* 67 */           int value = Character.digit(c, 10);
/* 68 */           if (value > -1) {
/* 69 */             subst.append('$').append(value);
/*    */           } else {
/* 71 */             subst.append(c);
/*    */           } 
/*    */         } else {
/*    */           
/* 75 */           subst.append('\\');
/*    */         } 
/*    */       } else {
/* 78 */         subst.append(c);
/*    */       } 
/*    */     } 
/*    */     
/* 82 */     int sOptions = getSubsOptions(options);
/* 83 */     Pattern p = getCompiledPattern(options);
/* 84 */     StringBuffer sb = new StringBuffer();
/*    */     
/* 86 */     Matcher m = p.matcher(input);
/* 87 */     if (RegexpUtil.hasFlag(sOptions, 16)) {
/* 88 */       sb.append(m.replaceAll(subst.toString()));
/* 89 */     } else if (m.find()) {
/* 90 */       m.appendReplacement(sb, subst.toString());
/* 91 */       m.appendTail(sb);
/*    */     } else {
/* 93 */       sb.append(input);
/*    */     } 
/* 95 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/regexp/Jdk14RegexpRegexp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */