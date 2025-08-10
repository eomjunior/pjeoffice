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
/*    */ public class PermittedSubclassesAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int[] u2classes;
/*    */   
/*    */   PermittedSubclassesAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 21 */     super(cf, attrNameIndex, attrLength);
/* 22 */     this.u2classes = new int[0];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 30 */     return "PermittedSubclasses";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 37 */     int u2numberOfClasses = din.readUnsignedShort();
/* 38 */     this.u2classes = new int[u2numberOfClasses];
/* 39 */     for (int i = 0; i < u2numberOfClasses; i++) {
/* 40 */       this.u2classes[i] = din.readUnsignedShort();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 48 */     int u2numberOfClasses = this.u2classes.length;
/* 49 */     dout.writeShort(u2numberOfClasses);
/* 50 */     for (int i = 0; i < u2numberOfClasses; i++)
/* 51 */       dout.writeShort(this.u2classes[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/PermittedSubclassesAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */