/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.yshrink.ant.MethodSection;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.MethodDescriptor;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ import java.util.Collection;
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
/*    */ public class SerializationFilter
/*    */   extends MethodFilter
/*    */ {
/*    */   public SerializationFilter(Project project) {
/* 24 */     super(project);
/*    */     
/* 26 */     MethodSection msWrite = new MethodSection();
/* 27 */     msWrite.setSignature("void writeObject(java.io.ObjectOutputStream)");
/* 28 */     msWrite.setAccess("private");
/* 29 */     addMethodSection(msWrite);
/*    */     
/* 31 */     MethodSection msRead = new MethodSection();
/* 32 */     msRead.setSignature("void readObject(java.io.ObjectInputStream)");
/* 33 */     msRead.setAccess("private");
/* 34 */     addMethodSection(msRead);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 41 */     boolean r = false;
/*    */     
/* 43 */     Collection<String> interfaces = cd.getAllImplementedInterfaces(model);
/* 44 */     if (interfaces.contains("java/io/Serializable")) {
/* 45 */       r = true;
/*    */     }
/*    */     
/* 48 */     return (r && super.isEntryPointMethod(model, cd, md));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/SerializationFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */