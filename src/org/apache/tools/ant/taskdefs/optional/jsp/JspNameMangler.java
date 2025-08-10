/*     */ package org.apache.tools.ant.taskdefs.optional.jsp;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspNameMangler
/*     */   implements JspMangler
/*     */ {
/*  36 */   public static final String[] keywords = new String[] { "assert", "abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String mapJspToJavaName(File jspFile) {
/*  63 */     return mapJspToBaseName(jspFile) + ".java";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String mapJspToBaseName(File jspFile) {
/*  74 */     String className = stripExtension(jspFile);
/*     */ 
/*     */ 
/*     */     
/*  78 */     for (String keyword : keywords) {
/*  79 */       if (className.equals(keyword)) {
/*  80 */         className = className + "%";
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     StringBuilder modifiedClassName = new StringBuilder(className.length());
/*     */     
/*  88 */     char firstChar = className.charAt(0);
/*  89 */     if (Character.isJavaIdentifierStart(firstChar)) {
/*  90 */       modifiedClassName.append(firstChar);
/*     */     } else {
/*  92 */       modifiedClassName.append(mangleChar(firstChar));
/*     */     } 
/*     */     
/*  95 */     for (char subChar : className.substring(1).toCharArray()) {
/*  96 */       if (Character.isJavaIdentifierPart(subChar)) {
/*  97 */         modifiedClassName.append(subChar);
/*     */       } else {
/*  99 */         modifiedClassName.append(mangleChar(subChar));
/*     */       } 
/*     */     } 
/* 102 */     return modifiedClassName.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String stripExtension(File jspFile) {
/* 113 */     return StringUtils.removeSuffix(jspFile.getName(), ".jsp");
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
/*     */   private static String mangleChar(char ch) {
/* 125 */     if (ch == File.separatorChar) {
/* 126 */       ch = '/';
/*     */     }
/* 128 */     String s = Integer.toHexString(ch);
/* 129 */     int nzeros = 5 - s.length();
/* 130 */     char[] result = new char[6];
/* 131 */     result[0] = '_';
/* 132 */     for (int i = 1; i <= nzeros; i++) {
/* 133 */       result[i] = '0';
/*     */     }
/* 135 */     int resultIndex = 0;
/* 136 */     for (int j = nzeros + 1; j < 6; j++) {
/* 137 */       result[j] = s.charAt(resultIndex++);
/*     */     }
/* 139 */     return new String(result);
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
/*     */   public String mapPath(String path) {
/* 152 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/JspNameMangler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */