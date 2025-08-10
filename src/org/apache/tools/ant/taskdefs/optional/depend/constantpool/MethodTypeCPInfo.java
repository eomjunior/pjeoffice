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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodTypeCPInfo
/*    */   extends ConstantCPInfo
/*    */ {
/*    */   private int methodDescriptorIndex;
/*    */   private String methodDescriptor;
/*    */   
/*    */   public MethodTypeCPInfo() {
/* 36 */     super(16, 1);
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
/*    */   public void read(DataInputStream cpStream) throws IOException {
/* 49 */     this.methodDescriptorIndex = cpStream.readUnsignedShort();
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
/*    */   public void resolve(ConstantPool constantPool) {
/* 62 */     Utf8CPInfo methodClass = (Utf8CPInfo)constantPool.getEntry(this.methodDescriptorIndex);
/* 63 */     methodClass.resolve(constantPool);
/* 64 */     this.methodDescriptor = methodClass.getValue();
/* 65 */     super.resolve(constantPool);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     if (isResolved()) {
/* 76 */       return "MethodDescriptor: " + this.methodDescriptor;
/*    */     }
/* 78 */     return "MethodDescriptorIndex: " + this.methodDescriptorIndex;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodTypeCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */