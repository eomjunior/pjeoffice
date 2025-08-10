/*    */ package org.apache.tools.ant.util.optional;
/*    */ 
/*    */ import java.security.Permission;
/*    */ import org.apache.tools.ant.ExitException;
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
/*    */ public class NoExitSecurityManager
/*    */   extends SecurityManager
/*    */ {
/*    */   public void checkExit(int status) {
/* 41 */     throw new ExitException(status);
/*    */   }
/*    */   
/*    */   public void checkPermission(Permission perm) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/optional/NoExitSecurityManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */