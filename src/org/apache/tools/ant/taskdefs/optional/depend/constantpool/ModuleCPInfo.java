/*    */ package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
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
/*    */ public class ModuleCPInfo
/*    */   extends ConstantCPInfo
/*    */ {
/*    */   private int moduleNameIndex;
/*    */   private String moduleName;
/*    */   
/*    */   public ModuleCPInfo() {
/* 32 */     super(19, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(DataInputStream cpStream) throws IOException {
/* 37 */     this.moduleNameIndex = cpStream.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resolve(ConstantPool constantPool) {
/* 42 */     this.moduleName = ((Utf8CPInfo)constantPool.getEntry(this.moduleNameIndex)).getValue();
/*    */     
/* 44 */     super.resolve(constantPool);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "Module info Constant Pool Entry for " + this.moduleName + "[" + this.moduleNameIndex + "]";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/ModuleCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */