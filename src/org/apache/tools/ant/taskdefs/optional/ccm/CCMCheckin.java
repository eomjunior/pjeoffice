/*    */ package org.apache.tools.ant.taskdefs.optional.ccm;
/*    */ 
/*    */ import java.util.Date;
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
/*    */ public class CCMCheckin
/*    */   extends CCMCheck
/*    */ {
/*    */   public CCMCheckin() {
/* 34 */     setCcmAction("ci");
/* 35 */     setComment("Checkin " + new Date());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ccm/CCMCheckin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */