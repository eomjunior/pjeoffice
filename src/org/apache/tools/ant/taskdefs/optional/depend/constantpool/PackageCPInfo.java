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
/*    */ public class PackageCPInfo
/*    */   extends ConstantCPInfo
/*    */ {
/*    */   private int packageNameIndex;
/*    */   private String packageName;
/*    */   
/*    */   public PackageCPInfo() {
/* 32 */     super(20, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(DataInputStream cpStream) throws IOException {
/* 37 */     this.packageNameIndex = cpStream.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resolve(ConstantPool constantPool) {
/* 42 */     this.packageName = ((Utf8CPInfo)constantPool.getEntry(this.packageNameIndex)).getValue();
/*    */     
/* 44 */     super.resolve(constantPool);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 49 */     return "Package info Constant Pool Entry for " + this.packageName + "[" + this.packageNameIndex + "]";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/PackageCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */