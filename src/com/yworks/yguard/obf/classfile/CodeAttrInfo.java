/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeAttrInfo
/*     */   extends AttrInfo
/*     */ {
/*     */   public static final int CONSTANT_FIELD_SIZE = 12;
/*     */   private int u2maxStack;
/*     */   private int u2maxLocals;
/*     */   private int u4codeLength;
/*     */   private byte[] code;
/*     */   private int u2exceptionTableLength;
/*     */   private ExceptionInfo[] exceptionTable;
/*     */   protected int u2attributesCount;
/*     */   protected AttrInfo[] attributes;
/*     */   
/*     */   protected CodeAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/*  56 */     super(cf, attrNameIndex, attrLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAttrInfoLength() {
/*  63 */     int length = 12 + this.u4codeLength + this.u2exceptionTableLength * 8;
/*     */     
/*  65 */     for (int i = 0; i < this.u2attributesCount; i++)
/*     */     {
/*  67 */       length += 6 + this.attributes[i].getAttrInfoLength();
/*     */     }
/*  69 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttrName() {
/*  75 */     return "Code";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trimAttrsExcept(String[] keepAttrs) {
/*  84 */     this.attributes = AttrInfo.filter(this.attributes, keepAttrs);
/*  85 */     this.u2attributesCount = this.attributes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/*  91 */     for (int i = 0; i < this.attributes.length; i++)
/*     */     {
/*  93 */       this.attributes[i].markUtf8Refs(pool);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/* 100 */     this.u2maxStack = din.readUnsignedShort();
/* 101 */     this.u2maxLocals = din.readUnsignedShort();
/* 102 */     this.u4codeLength = din.readInt();
/* 103 */     this.code = new byte[this.u4codeLength];
/* 104 */     din.readFully(this.code);
/* 105 */     this.u2exceptionTableLength = din.readUnsignedShort();
/* 106 */     this.exceptionTable = new ExceptionInfo[this.u2exceptionTableLength]; int i;
/* 107 */     for (i = 0; i < this.u2exceptionTableLength; i++)
/*     */     {
/* 109 */       this.exceptionTable[i] = ExceptionInfo.create(din);
/*     */     }
/* 111 */     this.u2attributesCount = din.readUnsignedShort();
/* 112 */     this.attributes = new AttrInfo[this.u2attributesCount];
/* 113 */     for (i = 0; i < this.u2attributesCount; i++)
/*     */     {
/* 115 */       this.attributes[i] = AttrInfo.create(din, this.owner);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInfo(DataOutput dout) throws IOException {
/* 122 */     dout.writeShort(this.u2maxStack);
/* 123 */     dout.writeShort(this.u2maxLocals);
/* 124 */     dout.writeInt(this.u4codeLength);
/* 125 */     dout.write(this.code);
/* 126 */     dout.writeShort(this.u2exceptionTableLength); int i;
/* 127 */     for (i = 0; i < this.u2exceptionTableLength; i++)
/*     */     {
/* 129 */       this.exceptionTable[i].write(dout);
/*     */     }
/* 131 */     dout.writeShort(this.u2attributesCount);
/* 132 */     for (i = 0; i < this.u2attributesCount; i++)
/*     */     {
/* 134 */       this.attributes[i].write(dout);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/CodeAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */