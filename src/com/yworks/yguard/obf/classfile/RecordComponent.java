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
/*    */ public class RecordComponent
/*    */ {
/*    */   static final int CONSTANT_FIELD_SIZE = 6;
/*    */   int u2nameIndex;
/*    */   int u2descriptorIndex;
/*    */   AttrInfo[] attributes;
/*    */   
/*    */   private RecordComponent(int nameIndex, int descriptorIndex, AttrInfo[] attributes) {
/* 25 */     this.u2nameIndex = nameIndex;
/* 26 */     this.u2descriptorIndex = descriptorIndex;
/* 27 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   int getNameIndex() {
/* 31 */     return this.u2nameIndex;
/*    */   }
/*    */   
/*    */   void setNameIndex(int index) {
/* 35 */     this.u2nameIndex = index;
/*    */   }
/*    */   
/*    */   int getDescriptorIndex() {
/* 39 */     return this.u2descriptorIndex;
/*    */   }
/*    */   
/*    */   void setDescriptorIndex(int index) {
/* 43 */     this.u2descriptorIndex = index;
/*    */   }
/*    */   
/*    */   AttrInfo[] getAttributes() {
/* 47 */     return this.attributes;
/*    */   }
/*    */   
/*    */   boolean trimAttrsExcept(String[] keepAttrs) {
/* 51 */     int oldAttributesCount = this.attributes.length;
/* 52 */     this.attributes = AttrInfo.filter(this.attributes, keepAttrs);
/* 53 */     return (oldAttributesCount != this.attributes.length);
/*    */   }
/*    */   
/*    */   void markUtf8Refs(ConstantPool pool) {
/* 57 */     pool.incRefCount(this.u2nameIndex);
/* 58 */     pool.incRefCount(this.u2descriptorIndex);
/* 59 */     for (int i = 0, n = this.attributes.length; i < n; i++) {
/* 60 */       this.attributes[i].markUtf8Refs(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static RecordComponent read(DataInput din, ClassFile cf) throws IOException {
/* 67 */     int nameIndex = din.readUnsignedShort();
/* 68 */     int descriptorIndex = din.readUnsignedShort();
/*    */     
/* 70 */     int attributesCount = din.readUnsignedShort();
/* 71 */     AttrInfo[] attributes = new AttrInfo[attributesCount];
/* 72 */     for (int i = 0; i < attributesCount; i++) {
/* 73 */       attributes[i] = AttrInfo.create(din, cf);
/*    */     }
/*    */     
/* 76 */     return new RecordComponent(nameIndex, descriptorIndex, attributes);
/*    */   }
/*    */   
/*    */   void write(DataOutput dout) throws IOException {
/* 80 */     dout.writeShort(this.u2nameIndex);
/* 81 */     dout.writeShort(this.u2descriptorIndex);
/*    */     
/* 83 */     int u2attributesCount = this.attributes.length;
/* 84 */     dout.writeShort(u2attributesCount);
/* 85 */     for (int i = 0; i < u2attributesCount; i++)
/* 86 */       this.attributes[i].write(dout); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RecordComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */