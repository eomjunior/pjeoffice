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
/*    */ public class ModuleExports
/*    */ {
/*    */   final int u2exportsIndex;
/*    */   final int u2exportsFlags;
/*    */   final int[] u2exportsToIndex;
/*    */   
/*    */   private ModuleExports(int index, int flags, int[] toIndex) {
/* 27 */     this.u2exportsIndex = index;
/* 28 */     this.u2exportsFlags = flags;
/* 29 */     this.u2exportsToIndex = toIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getExportsIndex() {
/* 38 */     return this.u2exportsIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static ModuleExports read(DataInput din) throws IOException {
/* 49 */     int index = din.readUnsignedShort();
/* 50 */     int flags = din.readUnsignedShort();
/* 51 */     int toCount = din.readUnsignedShort();
/* 52 */     int[] toIndex = new int[toCount];
/* 53 */     for (int j = 0; j < toCount; j++) {
/* 54 */       toIndex[j] = din.readUnsignedShort();
/*    */     }
/*    */     
/* 57 */     return new ModuleExports(index, flags, toIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 67 */     dout.writeShort(this.u2exportsIndex);
/* 68 */     dout.writeShort(this.u2exportsFlags);
/* 69 */     int u2exportsToCount = this.u2exportsToIndex.length;
/* 70 */     dout.writeShort(u2exportsToCount);
/* 71 */     for (int i = 0; i < u2exportsToCount; i++)
/* 72 */       dout.writeShort(this.u2exportsToIndex[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModuleExports.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */