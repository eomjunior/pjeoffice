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
/*    */ public class ModuleProvides
/*    */ {
/*    */   final int u2providesIndex;
/*    */   final int[] u2providesWithIndex;
/*    */   
/*    */   private ModuleProvides(int index, int[] withIndex) {
/* 23 */     this.u2providesIndex = index;
/* 24 */     this.u2providesWithIndex = withIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static ModuleProvides read(DataInput din) throws IOException {
/* 35 */     int index = din.readUnsignedShort();
/* 36 */     int withCount = din.readUnsignedShort();
/* 37 */     int[] withIndex = new int[withCount];
/* 38 */     for (int j = 0; j < withCount; j++) {
/* 39 */       withIndex[j] = din.readUnsignedShort();
/*    */     }
/*    */     
/* 42 */     return new ModuleProvides(index, withIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 52 */     dout.writeShort(this.u2providesIndex);
/* 53 */     int u2providesWithCount = this.u2providesWithIndex.length;
/* 54 */     dout.writeShort(u2providesWithCount);
/* 55 */     for (int i = 0; i < u2providesWithCount; i++)
/* 56 */       dout.writeShort(this.u2providesWithIndex[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModuleProvides.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */