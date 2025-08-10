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
/*     */ public class TypeAnnotationInfo
/*     */   extends AnnotationInfo
/*     */ {
/*     */   private int u1TargetType;
/*     */   private int u1TypeParameterIndex;
/*     */   private int u2SupertypeIndex;
/*     */   private int u1TypeBoundIndex;
/*     */   private int u1FormalParameterIndex;
/*     */   private int u2ThrowsTypeIndex;
/*     */   private int u2ExceptionTableIndex;
/*     */   private int u2Offset;
/*     */   private int u1TypeArgumentIndex;
/*     */   private LocalvarTarget localvarTarget;
/*     */   private TypePath targetPath;
/*     */   
/*     */   public static TypeAnnotationInfo create(DataInput din) throws IOException {
/*  39 */     TypeAnnotationInfo an = new TypeAnnotationInfo();
/*  40 */     an.read(din);
/*  41 */     return an;
/*     */   }
/*     */   
/*     */   void read(DataInput din) throws IOException {
/*  45 */     this.u1TargetType = din.readUnsignedByte();
/*     */     
/*  47 */     switch (this.u1TargetType) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 1:
/*  55 */         this.u1TypeParameterIndex = din.readUnsignedByte();
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/*  61 */         this.u2SupertypeIndex = din.readUnsignedShort();
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 17:
/*     */       case 18:
/*  69 */         this.u1TypeParameterIndex = din.readUnsignedByte();
/*  70 */         this.u1TypeBoundIndex = din.readUnsignedByte();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */         break;
/*     */ 
/*     */       
/*     */       case 22:
/*  81 */         this.u1FormalParameterIndex = din.readUnsignedByte();
/*     */         break;
/*     */       
/*     */       case 23:
/*  85 */         this.u2ThrowsTypeIndex = din.readUnsignedShort();
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 64:
/*     */       case 65:
/*  94 */         this.localvarTarget = new LocalvarTarget();
/*  95 */         this.localvarTarget.readInfo(din);
/*     */         break;
/*     */       
/*     */       case 66:
/*  99 */         this.u2ExceptionTableIndex = din.readUnsignedShort();
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/* 109 */         this.u2Offset = din.readUnsignedShort();
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/* 121 */         this.u2Offset = din.readUnsignedShort();
/* 122 */         this.u1TypeArgumentIndex = din.readUnsignedByte();
/*     */         break;
/*     */       default:
/* 125 */         throw new IllegalArgumentException("Unkown annotation target type: 0x" + Integer.toHexString(this.u1TargetType) + "");
/*     */     } 
/*     */     
/* 128 */     this.targetPath = new TypePath();
/* 129 */     this.targetPath.readInfo(din);
/*     */     
/* 131 */     super.read(din);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput dout) throws IOException {
/* 141 */     dout.writeByte(this.u1TargetType);
/*     */     
/* 143 */     switch (this.u1TargetType) {
/*     */       case 0:
/*     */       case 1:
/* 146 */         dout.writeByte(this.u1TypeParameterIndex);
/*     */         break;
/*     */       case 16:
/* 149 */         dout.writeShort(this.u2SupertypeIndex);
/*     */         break;
/*     */       case 17:
/*     */       case 18:
/* 153 */         dout.writeByte(this.u1TypeParameterIndex);
/* 154 */         dout.writeByte(this.u1TypeBoundIndex);
/*     */         break;
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */         break;
/*     */       case 22:
/* 161 */         dout.writeByte(this.u1FormalParameterIndex);
/*     */         break;
/*     */       case 23:
/* 164 */         dout.writeShort(this.u2ThrowsTypeIndex);
/*     */         break;
/*     */       case 64:
/*     */       case 65:
/* 168 */         this.localvarTarget.writeInfo(dout);
/*     */         break;
/*     */       case 66:
/* 171 */         dout.writeShort(this.u2ExceptionTableIndex);
/*     */         break;
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/* 177 */         dout.writeShort(this.u2Offset);
/*     */         break;
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/* 184 */         dout.writeShort(this.u2Offset);
/* 185 */         dout.writeByte(this.u1TypeArgumentIndex);
/*     */         break;
/*     */       default:
/* 188 */         throw new IllegalArgumentException("Unkown annotation target type: 0x" + Integer.toHexString(this.u1TargetType) + "");
/*     */     } 
/*     */     
/* 191 */     this.targetPath.writeInfo(dout);
/*     */     
/* 193 */     super.write(dout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class LocalvarTarget
/*     */   {
/*     */     private int u2TableLength;
/*     */ 
/*     */ 
/*     */     
/*     */     private LocalVarTargetVariableInfo[] table;
/*     */ 
/*     */ 
/*     */     
/*     */     protected void readInfo(DataInput din) throws IOException {
/* 210 */       this.u2TableLength = din.readUnsignedShort();
/* 211 */       this.table = new LocalVarTargetVariableInfo[this.u2TableLength];
/* 212 */       for (int i = 0; i < this.u2TableLength; i++) {
/* 213 */         this.table[i] = LocalVarTargetVariableInfo.create(din);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeInfo(DataOutput dout) throws IOException {
/* 224 */       dout.writeShort(this.u2TableLength);
/* 225 */       for (int i = 0; i < this.u2TableLength; i++) {
/* 226 */         this.table[i].write(dout);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static class LocalVarTargetVariableInfo
/*     */     {
/*     */       private int u2startPc;
/*     */ 
/*     */       
/*     */       private int u2length;
/*     */ 
/*     */       
/*     */       private int u2index;
/*     */ 
/*     */ 
/*     */       
/*     */       public static LocalVarTargetVariableInfo create(DataInput din) throws IOException {
/* 246 */         LocalVarTargetVariableInfo lvi = new LocalVarTargetVariableInfo();
/* 247 */         lvi.read(din);
/* 248 */         return lvi;
/*     */       }
/*     */       
/*     */       private void read(DataInput din) throws IOException {
/* 252 */         this.u2startPc = din.readUnsignedShort();
/* 253 */         this.u2length = din.readUnsignedShort();
/* 254 */         this.u2index = din.readUnsignedShort();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(DataOutput dout) throws IOException {
/* 264 */         dout.writeShort(this.u2startPc);
/* 265 */         dout.writeShort(this.u2length);
/* 266 */         dout.writeShort(this.u2index);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class TypePath
/*     */   {
/*     */     private int u1PathLength;
/*     */ 
/*     */ 
/*     */     
/*     */     private PathEntry[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void readInfo(DataInput din) throws IOException {
/* 287 */       this.u1PathLength = din.readUnsignedByte();
/* 288 */       this.entries = new PathEntry[this.u1PathLength];
/* 289 */       for (int i = 0; i < this.u1PathLength; i++) {
/* 290 */         this.entries[i] = PathEntry.create(din);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeInfo(DataOutput dout) throws IOException {
/* 301 */       dout.writeByte(this.u1PathLength);
/* 302 */       for (int i = 0; i < this.entries.length; i++) {
/* 303 */         this.entries[i].write(dout);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static class PathEntry
/*     */     {
/*     */       private int u1PathKind;
/*     */ 
/*     */ 
/*     */       
/*     */       private int u1TypeArgumentIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public static PathEntry create(DataInput din) throws IOException {
/* 322 */         PathEntry lvi = new PathEntry();
/* 323 */         lvi.read(din);
/* 324 */         return lvi;
/*     */       }
/*     */       
/*     */       private void read(DataInput din) throws IOException {
/* 328 */         this.u1PathKind = din.readUnsignedByte();
/* 329 */         this.u1TypeArgumentIndex = din.readUnsignedByte();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(DataOutput dout) throws IOException {
/* 339 */         dout.writeByte(this.u1PathKind);
/* 340 */         dout.writeByte(this.u1TypeArgumentIndex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/TypeAnnotationInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */