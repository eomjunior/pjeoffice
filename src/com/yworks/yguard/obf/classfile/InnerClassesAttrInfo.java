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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InnerClassesAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2numberOfClasses;
/*    */   private InnerClassesInfo[] classes;
/*    */   
/*    */   protected InnerClassesAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 40 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 46 */     return "InnerClasses";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected InnerClassesInfo[] getInfo() {
/* 56 */     return this.classes;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 62 */     for (int i = 0; i < this.classes.length; i++)
/*    */     {
/* 64 */       this.classes[i].markUtf8Refs(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 71 */     this.u2numberOfClasses = din.readUnsignedShort();
/* 72 */     this.classes = new InnerClassesInfo[this.u2numberOfClasses];
/* 73 */     for (int i = 0; i < this.u2numberOfClasses; i++)
/*    */     {
/* 75 */       this.classes[i] = InnerClassesInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 82 */     dout.writeShort(this.u2numberOfClasses);
/* 83 */     for (int i = 0; i < this.u2numberOfClasses; i++)
/*    */     {
/* 85 */       this.classes[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/InnerClassesAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */