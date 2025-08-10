/*    */ package org.apache.tools.ant.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.apache.tools.ant.Project;
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
/*    */ public class PropertyOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   private Project project;
/*    */   private String property;
/*    */   private boolean trim;
/*    */   
/*    */   public PropertyOutputStream(Project p, String s) {
/* 44 */     this(p, s, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PropertyOutputStream(Project p, String s, boolean b) {
/* 55 */     this.project = p;
/* 56 */     this.property = s;
/* 57 */     this.trim = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 65 */     if (this.project != null && this.property != null) {
/* 66 */       String s = new String(toByteArray());
/* 67 */       this.project.setNewProperty(this.property, this.trim ? s.trim() : s);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/PropertyOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */