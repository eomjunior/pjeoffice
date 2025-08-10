/*    */ package org.apache.tools.ant.types.spi;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
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
/*    */ public class Provider
/*    */   extends ProjectComponent
/*    */ {
/*    */   private String type;
/*    */   
/*    */   public String getClassName() {
/* 38 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassName(String type) {
/* 46 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void check() {
/* 53 */     if (this.type == null) {
/* 54 */       throw new BuildException("classname attribute must be set for provider element", 
/*    */           
/* 56 */           getLocation());
/*    */     }
/* 58 */     if (this.type.isEmpty())
/* 59 */       throw new BuildException("Invalid empty classname", 
/* 60 */           getLocation()); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/spi/Provider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */