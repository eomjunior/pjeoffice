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
/*    */ public class MethodHandleCPInfo
/*    */   extends ConstantPoolEntry
/*    */ {
/*    */   private ConstantPoolEntry reference;
/*    */   private ReferenceKind referenceKind;
/*    */   private int referenceIndex;
/*    */   
/*    */   public enum ReferenceKind
/*    */   {
/* 36 */     REF_getField,
/* 37 */     REF_getStatic,
/* 38 */     REF_putField,
/* 39 */     REF_putStatic,
/* 40 */     REF_invokeVirtual,
/* 41 */     REF_invokeStatic,
/* 42 */     REF_invokeSpecial,
/* 43 */     REF_newInvokeSpecial,
/* 44 */     REF_invokeInterface;
/*    */     
/*    */     public int value() {
/* 47 */       return ordinal() + 1;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public MethodHandleCPInfo() {
/* 53 */     super(15, 1);
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
/* 66 */     this.referenceKind = ReferenceKind.values()[cpStream.readUnsignedByte() - 1];
/* 67 */     this.referenceIndex = cpStream.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     if (isResolved()) {
/* 78 */       return "MethodHandle : " + this.reference.toString();
/*    */     }
/* 80 */     return "MethodHandle : Reference kind = " + this.referenceKind + "Reference index = " + this.referenceIndex;
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
/* 93 */     this.reference = constantPool.getEntry(this.referenceIndex);
/* 94 */     this.reference.resolve(constantPool);
/* 95 */     super.resolve(constantPool);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/MethodHandleCPInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */