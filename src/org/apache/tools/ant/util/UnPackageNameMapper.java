/*    */ package org.apache.tools.ant.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnPackageNameMapper
/*    */   extends GlobPatternMapper
/*    */ {
/*    */   protected String extractVariablePart(String name) {
/* 44 */     String var = name.substring(this.prefixLength, name
/* 45 */         .length() - this.postfixLength);
/* 46 */     return var.replace('.', File.separatorChar);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/UnPackageNameMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */