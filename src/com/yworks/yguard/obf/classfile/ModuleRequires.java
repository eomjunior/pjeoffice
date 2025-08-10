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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModuleRequires
/*    */ {
/*    */   final int u2requiresIndex;
/*    */   final int u2requiresFlags;
/*    */   final int u2requiresVersionIndex;
/*    */   
/*    */   private ModuleRequires(int index, int flags, int versionIndex) {
/* 27 */     this.u2requiresIndex = index;
/* 28 */     this.u2requiresFlags = flags;
/* 29 */     this.u2requiresVersionIndex = versionIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static ModuleRequires read(DataInput din) throws IOException {
/* 40 */     int index = din.readUnsignedShort();
/* 41 */     int flags = din.readUnsignedShort();
/* 42 */     int versionIndex = din.readUnsignedShort();
/* 43 */     return new ModuleRequires(index, flags, versionIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 53 */     dout.writeShort(this.u2requiresIndex);
/* 54 */     dout.writeShort(this.u2requiresFlags);
/* 55 */     dout.writeShort(this.u2requiresVersionIndex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModuleRequires.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */