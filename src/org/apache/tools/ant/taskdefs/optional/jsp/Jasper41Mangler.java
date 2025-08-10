/*    */ package org.apache.tools.ant.taskdefs.optional.jsp;
/*    */ 
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jasper41Mangler
/*    */   implements JspMangler
/*    */ {
/*    */   public String mapJspToJavaName(File jspFile) {
/* 38 */     String jspUri = jspFile.getAbsolutePath();
/* 39 */     int start = jspUri.lastIndexOf(File.separatorChar) + 1;
/* 40 */     StringBuilder modifiedClassName = new StringBuilder(jspUri.length() - start);
/* 41 */     if (!Character.isJavaIdentifierStart(jspUri.charAt(start)) || jspUri
/* 42 */       .charAt(start) == '_')
/*    */     {
/*    */       
/* 45 */       modifiedClassName.append('_');
/*    */     }
/* 47 */     for (char ch : jspUri.substring(start).toCharArray()) {
/* 48 */       if (Character.isJavaIdentifierPart(ch)) {
/* 49 */         modifiedClassName.append(ch);
/* 50 */       } else if (ch == '.') {
/* 51 */         modifiedClassName.append('_');
/*    */       } else {
/* 53 */         modifiedClassName.append(mangleChar(ch));
/*    */       } 
/*    */     } 
/* 56 */     return modifiedClassName.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String mangleChar(char ch) {
/* 64 */     String s = Integer.toHexString(ch);
/* 65 */     int nzeros = 5 - s.length();
/* 66 */     char[] result = new char[6];
/* 67 */     result[0] = '_'; int i;
/* 68 */     for (i = 1; i <= nzeros; i++)
/* 69 */       result[i] = '0'; 
/*    */     int j;
/* 71 */     for (i = nzeros + 1, j = 0; i < 6; i++, j++) {
/* 72 */       result[i] = s.charAt(j);
/*    */     }
/* 74 */     return new String(result);
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
/*    */   public String mapPath(String path) {
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/Jasper41Mangler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */