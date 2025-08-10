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
/*    */ public class NestMembersAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int[] u2classes;
/*    */   
/*    */   NestMembersAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 33 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 41 */     return "NestMembers";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 48 */     int u2numberOfClasses = din.readUnsignedShort();
/* 49 */     this.u2classes = new int[u2numberOfClasses];
/* 50 */     for (int i = 0; i < u2numberOfClasses; i++) {
/* 51 */       this.u2classes[i] = din.readUnsignedShort();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 59 */     int u2numberOfClasses = this.u2classes.length;
/* 60 */     dout.writeShort(u2numberOfClasses);
/* 61 */     for (int i = 0; i < u2numberOfClasses; i++)
/* 62 */       dout.writeShort(this.u2classes[i]); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/NestMembersAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */