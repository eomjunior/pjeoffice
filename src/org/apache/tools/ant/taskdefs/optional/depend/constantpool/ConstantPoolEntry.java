/*     */ package org.apache.tools.ant.taskdefs.optional.depend.constantpool;
/*     */ 
/*     */ import java.io.DataInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConstantPoolEntry
/*     */ {
/*     */   public static final int CONSTANT_UTF8 = 1;
/*     */   public static final int CONSTANT_INTEGER = 3;
/*     */   public static final int CONSTANT_FLOAT = 4;
/*     */   public static final int CONSTANT_LONG = 5;
/*     */   public static final int CONSTANT_DOUBLE = 6;
/*     */   public static final int CONSTANT_CLASS = 7;
/*     */   public static final int CONSTANT_STRING = 8;
/*     */   public static final int CONSTANT_FIELDREF = 9;
/*     */   public static final int CONSTANT_METHODREF = 10;
/*     */   public static final int CONSTANT_INTERFACEMETHODREF = 11;
/*     */   public static final int CONSTANT_NAMEANDTYPE = 12;
/*     */   public static final int CONSTANT_METHODHANDLE = 15;
/*     */   public static final int CONSTANT_METHODTYPE = 16;
/*     */   public static final int CONSTANT_INVOKEDYNAMIC = 18;
/*     */   public static final int CONSTANT_MODULEINFO = 19;
/*     */   public static final int CONSTANT_PACKAGEINFO = 20;
/*     */   private int tag;
/*     */   private int numEntries;
/*     */   private boolean resolved;
/*     */   
/*     */   public ConstantPoolEntry(int tagValue, int entries) {
/* 106 */     this.tag = tagValue;
/* 107 */     this.numEntries = entries;
/* 108 */     this.resolved = false;
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
/*     */ 
/*     */   
/*     */   public static ConstantPoolEntry readEntry(DataInputStream cpStream) throws IOException {
/*     */     ConstantPoolEntry cpInfo;
/* 125 */     int cpTag = cpStream.readUnsignedByte();
/*     */ 
/*     */     
/* 128 */     switch (cpTag) {
/*     */       
/*     */       case 1:
/* 131 */         cpInfo = new Utf8CPInfo();
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
/* 182 */         cpInfo.read(cpStream);
/*     */         
/* 184 */         return cpInfo;case 3: cpInfo = new IntegerCPInfo(); cpInfo.read(cpStream); return cpInfo;case 4: cpInfo = new FloatCPInfo(); cpInfo.read(cpStream); return cpInfo;case 5: cpInfo = new LongCPInfo(); cpInfo.read(cpStream); return cpInfo;case 6: cpInfo = new DoubleCPInfo(); cpInfo.read(cpStream); return cpInfo;case 7: cpInfo = new ClassCPInfo(); cpInfo.read(cpStream); return cpInfo;case 8: cpInfo = new StringCPInfo(); cpInfo.read(cpStream); return cpInfo;case 9: cpInfo = new FieldRefCPInfo(); cpInfo.read(cpStream); return cpInfo;case 10: cpInfo = new MethodRefCPInfo(); cpInfo.read(cpStream); return cpInfo;case 11: cpInfo = new InterfaceMethodRefCPInfo(); cpInfo.read(cpStream); return cpInfo;case 12: cpInfo = new NameAndTypeCPInfo(); cpInfo.read(cpStream); return cpInfo;case 15: cpInfo = new MethodHandleCPInfo(); cpInfo.read(cpStream); return cpInfo;case 16: cpInfo = new MethodTypeCPInfo(); cpInfo.read(cpStream); return cpInfo;case 18: cpInfo = new InvokeDynamicCPInfo(); cpInfo.read(cpStream); return cpInfo;case 19: cpInfo = new ModuleCPInfo(); cpInfo.read(cpStream); return cpInfo;case 20: cpInfo = new PackageCPInfo(); cpInfo.read(cpStream); return cpInfo;
/*     */     } 
/*     */     throw new ClassFormatError("Invalid Constant Pool entry Type " + cpTag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isResolved() {
/* 196 */     return this.resolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resolve(ConstantPool constantPool) {
/* 207 */     this.resolved = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void read(DataInputStream paramDataInputStream) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTag() {
/* 226 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getNumEntries() {
/* 236 */     return this.numEntries;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/constantpool/ConstantPoolEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */