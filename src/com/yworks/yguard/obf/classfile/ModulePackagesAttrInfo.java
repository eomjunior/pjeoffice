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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModulePackagesAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int[] u2packageIndex;
/*    */   
/*    */   ModulePackagesAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 33 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 41 */     return "ModulePackages";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 48 */     int u2packageCount = din.readUnsignedShort();
/* 49 */     this.u2packageIndex = new int[u2packageCount];
/* 50 */     for (int i = 0; i < u2packageCount; i++) {
/* 51 */       this.u2packageIndex[i] = din.readUnsignedShort();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 59 */     int u2packageCount = this.u2packageIndex.length;
/* 60 */     dout.writeShort(u2packageCount);
/* 61 */     for (int i = 0; i < u2packageCount; i++)
/* 62 */       dout.writeShort(this.u2packageIndex[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModulePackagesAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */