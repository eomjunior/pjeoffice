/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import com.yworks.yguard.ParseException;
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
/*     */ public class ElementValueInfo
/*     */ {
/*     */   protected int u1Tag;
/*     */   protected int u2cpIndex;
/*     */   protected int u2typeNameIndex;
/*     */   protected int u2constNameIndex;
/*     */   protected AnnotationInfo nestedAnnotation;
/*     */   protected ElementValueInfo[] arrayValues;
/*     */   
/*     */   public static ElementValueInfo create(DataInput din) throws IOException {
/*  51 */     ElementValueInfo evp = new ElementValueInfo();
/*  52 */     evp.read(din);
/*  53 */     return evp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBoolValue(ConstantPool cp) {
/*  63 */     if (this.u1Tag == 90) {
/*  64 */       CpInfo cpEntry = cp.getCpEntry(this.u2cpIndex);
/*  65 */       return (cpEntry instanceof IntegerCpInfo && ((IntegerCpInfo)cpEntry).asBool());
/*     */     } 
/*  67 */     throw new RuntimeException("cannot get bool value of " + this.u1Tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void read(DataInput din) throws IOException {
/*     */     int count, i;
/*  78 */     this.u1Tag = din.readUnsignedByte();
/*  79 */     switch (this.u1Tag) {
/*     */       
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 70:
/*     */       case 73:
/*     */       case 74:
/*     */       case 83:
/*     */       case 90:
/*     */       case 115:
/*  90 */         this.u2cpIndex = din.readUnsignedShort();
/*     */         return;
/*     */       case 101:
/*  93 */         this.u2typeNameIndex = din.readUnsignedShort();
/*  94 */         this.u2constNameIndex = din.readUnsignedShort();
/*     */         return;
/*     */       case 99:
/*  97 */         this.u2cpIndex = din.readUnsignedShort();
/*     */         return;
/*     */       case 64:
/* 100 */         this.nestedAnnotation = AnnotationInfo.create(din);
/*     */         return;
/*     */       case 91:
/* 103 */         count = din.readUnsignedShort();
/* 104 */         this.arrayValues = new ElementValueInfo[count];
/* 105 */         for (i = 0; i < count; i++)
/*     */         {
/* 107 */           this.arrayValues[i] = create(din);
/*     */         }
/*     */         return;
/*     */     } 
/* 111 */     throw new ParseException("Unkown tag type in ElementValuePair: " + this.u1Tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/*     */     int i;
/* 121 */     switch (this.u1Tag) {
/*     */       
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 70:
/*     */       case 73:
/*     */       case 74:
/*     */       case 83:
/*     */       case 90:
/*     */       case 115:
/* 132 */         pool.getCpEntry(this.u2cpIndex).incRefCount();
/*     */         break;
/*     */       case 101:
/* 135 */         pool.getCpEntry(this.u2typeNameIndex).incRefCount();
/* 136 */         pool.getCpEntry(this.u2constNameIndex).incRefCount();
/*     */         break;
/*     */       case 99:
/* 139 */         pool.getCpEntry(this.u2cpIndex).incRefCount();
/*     */         break;
/*     */       case 64:
/* 142 */         this.nestedAnnotation.markUtf8RefsInInfo(pool);
/*     */         break;
/*     */       case 91:
/* 145 */         for (i = 0; i < this.arrayValues.length; i++)
/*     */         {
/* 147 */           this.arrayValues[i].markUtf8RefsInInfo(pool);
/*     */         }
/*     */         break;
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
/*     */     int count, i;
/* 161 */     dout.writeByte(this.u1Tag);
/* 162 */     switch (this.u1Tag) {
/*     */       
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 70:
/*     */       case 73:
/*     */       case 74:
/*     */       case 83:
/*     */       case 90:
/*     */       case 115:
/* 173 */         dout.writeShort(this.u2cpIndex);
/*     */         break;
/*     */       case 101:
/* 176 */         dout.writeShort(this.u2typeNameIndex);
/* 177 */         dout.writeShort(this.u2constNameIndex);
/*     */         break;
/*     */       case 99:
/* 180 */         dout.writeShort(this.u2cpIndex);
/*     */         break;
/*     */       case 64:
/* 183 */         this.nestedAnnotation.write(dout);
/*     */         break;
/*     */       case 91:
/* 186 */         count = this.arrayValues.length;
/* 187 */         dout.writeShort(count);
/* 188 */         for (i = 0; i < count; i++)
/*     */         {
/* 190 */           this.arrayValues[i].write(dout);
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ElementValueInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */