/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
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
/*    */ public class BootstrapMethod
/*    */ {
/*    */   int u2bootstrapMethodRef;
/*    */   int[] u2bootstrapArguments;
/*    */   
/*    */   private BootstrapMethod(int methodRef, int[] arguments) {
/* 23 */     this.u2bootstrapMethodRef = methodRef;
/* 24 */     this.u2bootstrapArguments = arguments;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static BootstrapMethod read(DataInput din) throws IOException {
/* 35 */     int methodRef = din.readUnsignedShort();
/* 36 */     int numArguments = din.readUnsignedShort();
/* 37 */     int[] arguments = new int[numArguments];
/* 38 */     for (int i = 0; i < numArguments; i++) {
/* 39 */       arguments[i] = din.readUnsignedShort();
/*    */     }
/* 41 */     return new BootstrapMethod(methodRef, arguments);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 51 */     dout.writeShort(this.u2bootstrapMethodRef);
/* 52 */     int u2numBootstrapArguments = this.u2bootstrapArguments.length;
/* 53 */     dout.writeShort(u2numBootstrapArguments);
/* 54 */     for (int j = 0; j < u2numBootstrapArguments; j++) {
/* 55 */       dout.writeShort(this.u2bootstrapArguments[j]);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBootstrapMethodRef() {
/* 65 */     return this.u2bootstrapMethodRef;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int[] getBootstrapArguments() {
/* 74 */     return this.u2bootstrapArguments;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/BootstrapMethod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */