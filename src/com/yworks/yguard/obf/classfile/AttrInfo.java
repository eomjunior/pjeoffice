/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import com.yworks.yguard.Conversion;
/*     */ import com.yworks.yguard.ParseException;
/*     */ import com.yworks.yguard.obf.Tools;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttrInfo
/*     */   implements ClassConstants
/*     */ {
/*     */   public static final int CONSTANT_FIELD_SIZE = 6;
/*     */   private int u2attrNameIndex;
/*     */   protected int u4attrLength;
/*     */   private byte[] info;
/*     */   protected ClassFile owner;
/*     */   
/*     */   public static AttrInfo create(DataInput din, ClassFile cf) throws IOException {
/*  59 */     if (din == null) throw new NullPointerException("No input stream was provided.");
/*     */ 
/*     */     
/*  62 */     AttrInfo ai = null;
/*  63 */     int attrNameIndex = din.readUnsignedShort();
/*  64 */     int attrLength = din.readInt();
/*     */     
/*  66 */     CpInfo cpInfo = cf.getCpEntry(attrNameIndex);
/*  67 */     if (cpInfo instanceof Utf8CpInfo) {
/*     */       
/*  69 */       String attrName = ((Utf8CpInfo)cpInfo).getString();
/*  70 */       if (attrName.equals("Code")) {
/*     */         
/*  72 */         ai = new CodeAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  74 */       else if (attrName.equals("ConstantValue")) {
/*     */         
/*  76 */         ai = new ConstantValueAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  78 */       else if (attrName.equals("Exceptions")) {
/*     */         
/*  80 */         ai = new ExceptionsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  82 */       else if (attrName.equals("StackMapTable")) {
/*     */         
/*  84 */         ai = new StackMapTableAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  86 */       else if (attrName.equals("LineNumberTable")) {
/*     */         
/*  88 */         ai = new LineNumberTableAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  90 */       else if (attrName.equals("SourceFile")) {
/*     */         
/*  92 */         ai = new SourceFileAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  94 */       else if (attrName.equals("LocalVariableTable")) {
/*     */         
/*  96 */         ai = new LocalVariableTableAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*  98 */       else if (attrName.equals("InnerClasses")) {
/*     */         
/* 100 */         ai = new InnerClassesAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 102 */       else if (attrName.equals("Synthetic")) {
/*     */         
/* 104 */         ai = new SyntheticAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 106 */       else if (attrName.equals("Deprecated")) {
/*     */         
/* 108 */         ai = new DeprecatedAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 110 */       else if (attrName.equals("Signature")) {
/* 111 */         ai = new SignatureAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 113 */       else if (attrName.equals("LocalVariableTypeTable")) {
/* 114 */         ai = new LocalVariableTypeTableAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 116 */       else if (attrName.equals("EnclosingMethod")) {
/* 117 */         ai = new EnclosingMethodAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 119 */       else if (attrName.equals("RuntimeVisibleAnnotations")) {
/* 120 */         ai = new RuntimeVisibleAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 122 */       else if (attrName.equals("RuntimeVisibleTypeAnnotations")) {
/* 123 */         ai = new RuntimeVisibleTypeAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 125 */       else if (attrName.equals("RuntimeInvisibleAnnotations")) {
/* 126 */         ai = new RuntimeInvisibleAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 128 */       else if (attrName.equals("RuntimeInvisibleTypeAnnotations")) {
/* 129 */         ai = new RuntimeInvisibleTypeAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 131 */       else if (attrName.equals("RuntimeVisibleParameterAnnotations")) {
/* 132 */         ai = new RuntimeVisibleParameterAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 134 */       else if (attrName.equals("RuntimeInvisibleParameterAnnotations")) {
/* 135 */         ai = new RuntimeInvisibleParameterAnnotationsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 137 */       else if (attrName.equals("AnnotationDefault")) {
/* 138 */         ai = new AnnotationDefaultAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 140 */       else if (attrName.equals("BootstrapMethods")) {
/* 141 */         ai = new BootstrapMethodsAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 143 */       else if (attrName.equals("Bridge") && attrLength == 0) {
/* 144 */         ai = new AttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 146 */       else if (attrName.equals("Enum") && attrLength == 0) {
/* 147 */         ai = new AttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 149 */       else if (attrName.equals("Varargs") && attrLength == 0) {
/* 150 */         ai = new AttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 152 */       else if ("MethodParameters".equals(attrName)) {
/* 153 */         ai = new MethodParametersAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 155 */       else if ("Module".equals(attrName)) {
/* 156 */         ai = new ModuleAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 158 */       else if ("ModulePackages".equals(attrName)) {
/* 159 */         ai = new ModulePackagesAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 161 */       else if ("ModuleMainClass".equals(attrName)) {
/* 162 */         ai = new ModuleMainClassAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 164 */       else if ("NestHost".equals(attrName)) {
/* 165 */         ai = new NestHostAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 167 */       else if ("NestMembers".equals(attrName)) {
/* 168 */         ai = new NestMembersAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 170 */       else if ("SourceDebugExtension".equals(attrName)) {
/* 171 */         ai = new AttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 173 */       else if ("Record".equals(attrName)) {
/* 174 */         ai = new RecordAttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/* 176 */       else if ("PermittedSubclasses".equals(attrName)) {
/* 177 */         ai = new PermittedSubclassesAttrInfo(cf, attrNameIndex, attrLength);
/*     */       } else {
/*     */         
/* 180 */         if (attrLength > 0) {
/* 181 */           Logger.getInstance().warning("Unrecognized attribute '" + attrName + "' in " + Conversion.toJavaClass(cf.getName()));
/*     */         }
/* 183 */         ai = new AttrInfo(cf, attrNameIndex, attrLength);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 188 */       throw new ParseException("Inconsistent reference to Constant Pool.");
/*     */     } 
/*     */     
/* 191 */     ai.readInfo(din);
/* 192 */     return ai;
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
/*     */   protected AttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 205 */     this.owner = cf;
/* 206 */     this.u2attrNameIndex = attrNameIndex;
/* 207 */     this.u4attrLength = attrLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAttrNameIndex() {
/* 217 */     return this.u2attrNameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAttrInfoLength() {
/* 227 */     return this.u4attrLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttrName() {
/* 237 */     return "Unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trimAttrsExcept(String[] keepAttrs) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/* 254 */     pool.incRefCount(this.u2attrNameIndex);
/* 255 */     markUtf8RefsInInfo(pool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/* 274 */     this.info = new byte[this.u4attrLength];
/* 275 */     din.readFully(this.info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(DataOutput dout) throws IOException {
/* 286 */     if (dout == null) throw new IOException("No output stream was provided."); 
/* 287 */     dout.writeShort(this.u2attrNameIndex);
/* 288 */     dout.writeInt(getAttrInfoLength());
/* 289 */     writeInfo(dout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInfo(DataOutput dout) throws IOException {
/* 300 */     dout.write(this.info);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 304 */     return getAttrName() + "[" + getAttrInfoLength() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static AttrInfo[] filter(AttrInfo[] attributes, String[] acceptedAttrs) {
/* 315 */     if (attributes == null) {
/* 316 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 320 */     int attrsToKeepCount = 0;
/* 321 */     for (int i = 0, n = attributes.length; i < n; i++) {
/* 322 */       if (Tools.isInArray(attributes[i].getAttrName(), acceptedAttrs)) {
/* 323 */         attrsToKeepCount++;
/* 324 */         attributes[i].trimAttrsExcept(acceptedAttrs);
/*     */       } else {
/* 326 */         attributes[i] = null;
/*     */       } 
/*     */     } 
/*     */     
/* 330 */     AttrInfo[] attrsToKeep = new AttrInfo[attrsToKeepCount];
/* 331 */     for (int k = 0, j = 0, m = attributes.length; k < m; k++) {
/* 332 */       if (attributes[k] != null) {
/* 333 */         attrsToKeep[j++] = attributes[k];
/*     */       }
/*     */     } 
/* 336 */     return attrsToKeep;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/AttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */