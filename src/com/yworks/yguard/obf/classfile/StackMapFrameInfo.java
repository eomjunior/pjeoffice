/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class StackMapFrameInfo
/*     */ {
/*     */   private VerificationTypeInfo[] verificationTypeInfoStack;
/*     */   private int u2_offset_delta;
/*     */   private VerificationTypeInfo[] verificationTypeInfoLocals;
/*     */   private int u1_frameType;
/*     */   
/*     */   public static StackMapFrameInfo create(DataInput din) throws IOException {
/*  34 */     if (din == null) throw new NullPointerException("DataInput cannot be null!"); 
/*  35 */     StackMapFrameInfo smfi = new StackMapFrameInfo();
/*  36 */     smfi.read(din);
/*  37 */     return smfi;
/*     */   }
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
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/*  51 */     if (this.u1_frameType >= 64)
/*     */     {
/*  53 */       if (this.u1_frameType >= 64 && this.u1_frameType <= 127) {
/*     */         
/*  55 */         this.verificationTypeInfoStack[0].markUtf8Refs(pool);
/*  56 */       } else if (this.u1_frameType == 247) {
/*     */         
/*  58 */         this.verificationTypeInfoStack[0].markUtf8Refs(pool);
/*  59 */       } else if (this.u1_frameType < 248 || this.u1_frameType > 250) {
/*     */         
/*  61 */         if (this.u1_frameType != 251)
/*     */         {
/*  63 */           if (this.u1_frameType >= 252 && this.u1_frameType <= 254) {
/*     */             
/*  65 */             for (int i = 0; i < this.verificationTypeInfoStack.length; i++) {
/*  66 */               this.verificationTypeInfoStack[i].markUtf8Refs(pool);
/*     */             }
/*  68 */           } else if (this.u1_frameType == 255) {
/*     */             int i;
/*  70 */             for (i = 0; i < this.verificationTypeInfoLocals.length; i++) {
/*  71 */               this.verificationTypeInfoLocals[i].markUtf8Refs(pool);
/*     */             }
/*  73 */             for (i = 0; i < this.verificationTypeInfoStack.length; i++) {
/*  74 */               this.verificationTypeInfoStack[i].markUtf8Refs(pool);
/*     */             }
/*     */           } else {
/*  77 */             throw new IllegalArgumentException("Unknown frame type " + this.u1_frameType);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getVerificationTypeInfos() {
/*  88 */     ArrayList<VerificationTypeInfo> result = new ArrayList();
/*  89 */     if (this.verificationTypeInfoLocals != null) {
/*  90 */       for (int i = 0; i < this.verificationTypeInfoLocals.length; i++) {
/*  91 */         VerificationTypeInfo verificationTypeInfo = this.verificationTypeInfoLocals[i];
/*  92 */         result.add(verificationTypeInfo);
/*     */       } 
/*     */     }
/*  95 */     if (this.verificationTypeInfoStack != null) {
/*  96 */       for (int i = 0; i < this.verificationTypeInfoStack.length; i++) {
/*  97 */         VerificationTypeInfo verificationTypeInfo = this.verificationTypeInfoStack[i];
/*  98 */         result.add(verificationTypeInfo);
/*     */       } 
/*     */     }
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInput din) throws IOException {
/* 106 */     this.verificationTypeInfoLocals = null;
/* 107 */     this.verificationTypeInfoStack = null;
/* 108 */     this.u1_frameType = din.readUnsignedByte();
/* 109 */     if (this.u1_frameType >= 64)
/*     */     {
/* 111 */       if (this.u1_frameType >= 64 && this.u1_frameType <= 127) {
/*     */         
/* 113 */         this.verificationTypeInfoStack = new VerificationTypeInfo[1];
/* 114 */         this.verificationTypeInfoStack[0] = VerificationTypeInfo.create(din);
/* 115 */       } else if (this.u1_frameType == 247) {
/*     */         
/* 117 */         this.u2_offset_delta = din.readUnsignedShort();
/* 118 */         this.verificationTypeInfoStack = new VerificationTypeInfo[1];
/* 119 */         this.verificationTypeInfoStack[0] = VerificationTypeInfo.create(din);
/* 120 */       } else if (this.u1_frameType >= 248 && this.u1_frameType <= 250) {
/*     */         
/* 122 */         this.u2_offset_delta = din.readUnsignedShort();
/* 123 */       } else if (this.u1_frameType == 251) {
/*     */         
/* 125 */         this.u2_offset_delta = din.readUnsignedShort();
/* 126 */       } else if (this.u1_frameType >= 252 && this.u1_frameType <= 254) {
/*     */         
/* 128 */         this.u2_offset_delta = din.readUnsignedShort();
/* 129 */         int count = this.u1_frameType - 251;
/* 130 */         this.verificationTypeInfoStack = new VerificationTypeInfo[count];
/* 131 */         for (int i = 0; i < this.verificationTypeInfoStack.length; i++) {
/* 132 */           this.verificationTypeInfoStack[i] = VerificationTypeInfo.create(din);
/*     */         }
/* 134 */       } else if (this.u1_frameType == 255) {
/*     */         
/* 136 */         this.u2_offset_delta = din.readUnsignedShort();
/* 137 */         this.verificationTypeInfoLocals = new VerificationTypeInfo[din.readUnsignedShort()]; int i;
/* 138 */         for (i = 0; i < this.verificationTypeInfoLocals.length; i++) {
/* 139 */           this.verificationTypeInfoLocals[i] = VerificationTypeInfo.create(din);
/*     */         }
/* 141 */         this.verificationTypeInfoStack = new VerificationTypeInfo[din.readUnsignedShort()];
/* 142 */         for (i = 0; i < this.verificationTypeInfoStack.length; i++) {
/* 143 */           this.verificationTypeInfoStack[i] = VerificationTypeInfo.create(din);
/*     */         }
/*     */       } else {
/* 146 */         throw new IllegalArgumentException("Unknown frame type " + this.u1_frameType);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput dout) throws IOException {
/* 158 */     dout.writeByte(this.u1_frameType);
/* 159 */     if (this.u1_frameType >= 64)
/*     */     {
/* 161 */       if (this.u1_frameType >= 64 && this.u1_frameType <= 127) {
/*     */         
/* 163 */         this.verificationTypeInfoStack[0].write(dout);
/* 164 */       } else if (this.u1_frameType == 247) {
/*     */         
/* 166 */         dout.writeShort(this.u2_offset_delta);
/* 167 */         this.verificationTypeInfoStack[0].write(dout);
/* 168 */       } else if (this.u1_frameType >= 248 && this.u1_frameType <= 250) {
/*     */         
/* 170 */         dout.writeShort(this.u2_offset_delta);
/* 171 */       } else if (this.u1_frameType == 251) {
/*     */         
/* 173 */         dout.writeShort(this.u2_offset_delta);
/* 174 */       } else if (this.u1_frameType >= 252 && this.u1_frameType <= 254) {
/*     */         
/* 176 */         dout.writeShort(this.u2_offset_delta);
/* 177 */         for (int i = 0; i < this.verificationTypeInfoStack.length; i++) {
/* 178 */           this.verificationTypeInfoStack[i].write(dout);
/*     */         }
/* 180 */       } else if (this.u1_frameType == 255) {
/*     */         
/* 182 */         dout.writeShort(this.u2_offset_delta);
/* 183 */         dout.writeShort(this.verificationTypeInfoLocals.length); int i;
/* 184 */         for (i = 0; i < this.verificationTypeInfoLocals.length; i++) {
/* 185 */           this.verificationTypeInfoLocals[i].write(dout);
/*     */         }
/* 187 */         dout.writeShort(this.verificationTypeInfoStack.length);
/* 188 */         for (i = 0; i < this.verificationTypeInfoStack.length; i++) {
/* 189 */           this.verificationTypeInfoStack[i].write(dout);
/*     */         }
/*     */       } else {
/* 192 */         throw new IllegalArgumentException("Unknown frame type " + this.u1_frameType);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/StackMapFrameInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */