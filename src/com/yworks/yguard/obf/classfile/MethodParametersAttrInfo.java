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
/*    */ public class MethodParametersAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private MethodParameter[] parameters;
/*    */   
/*    */   MethodParametersAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 31 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 39 */     return "MethodParameters";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 46 */     for (int i = 0; i < this.parameters.length; i++) {
/* 47 */       int index = (this.parameters[i]).u2nameIndex;
/* 48 */       if (index > 0) {
/* 49 */         pool.incRefCount(index);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 58 */     int u1parameterCount = din.readUnsignedByte();
/* 59 */     this.parameters = new MethodParameter[u1parameterCount];
/* 60 */     for (int i = 0; i < u1parameterCount; i++) {
/* 61 */       this.parameters[i] = MethodParameter.read(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 69 */     int u1parameterCount = this.parameters.length;
/* 70 */     dout.writeByte(u1parameterCount);
/* 71 */     for (int i = 0; i < u1parameterCount; i++)
/* 72 */       this.parameters[i].write(dout); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/MethodParametersAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */