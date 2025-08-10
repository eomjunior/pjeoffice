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
/*    */ public class ModuleOpens
/*    */ {
/*    */   final int u2opensIndex;
/*    */   final int u2opensFlags;
/*    */   final int[] u2opensToIndex;
/*    */   
/*    */   private ModuleOpens(int index, int flags, int[] toIndex) {
/* 27 */     this.u2opensIndex = index;
/* 28 */     this.u2opensFlags = flags;
/* 29 */     this.u2opensToIndex = toIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static ModuleOpens read(DataInput din) throws IOException {
/* 40 */     int index = din.readUnsignedShort();
/* 41 */     int flags = din.readUnsignedShort();
/* 42 */     int toCount = din.readUnsignedShort();
/* 43 */     int[] toIndex = new int[toCount];
/* 44 */     for (int j = 0; j < toCount; j++) {
/* 45 */       toIndex[j] = din.readUnsignedShort();
/*    */     }
/*    */     
/* 48 */     return new ModuleOpens(index, flags, toIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 58 */     dout.writeShort(this.u2opensIndex);
/* 59 */     dout.writeShort(this.u2opensFlags);
/* 60 */     int u2opensToCount = this.u2opensToIndex.length;
/* 61 */     dout.writeShort(u2opensToCount);
/* 62 */     for (int i = 0; i < u2opensToCount; i++)
/* 63 */       dout.writeShort(this.u2opensToIndex[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModuleOpens.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */