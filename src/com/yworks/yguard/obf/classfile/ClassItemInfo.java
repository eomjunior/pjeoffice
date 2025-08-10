/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import com.yworks.yguard.obf.ObfuscationConfig;
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
/*     */ public abstract class ClassItemInfo
/*     */   implements ClassConstants
/*     */ {
/*     */   private int u2accessFlags;
/*     */   private int u2nameIndex;
/*     */   private int u2descriptorIndex;
/*     */   protected int u2attributesCount;
/*     */   protected AttrInfo[] attributes;
/*     */   private ClassFile cf;
/*     */   private boolean isSynthetic = false;
/*  43 */   private static final ObfuscationConfig DUMMY = new ObfuscationConfig(true, true);
/*  44 */   private ObfuscationConfig obfuscationConfig = DUMMY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassItemInfo(ClassFile cf) {
/*  55 */     this.cf = cf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ObfuscationConfig getObfuscationConfig(String name, AttrInfo[] attributes) {
/*  66 */     if (attributes == null) return null; 
/*  67 */     for (int i = 0; i < attributes.length; i++) {
/*  68 */       AttrInfo attribute = attributes[i];
/*     */       
/*  70 */       if (attribute instanceof RuntimeVisibleAnnotationsAttrInfo) {
/*  71 */         RuntimeVisibleAnnotationsAttrInfo annotation = (RuntimeVisibleAnnotationsAttrInfo)attribute;
/*  72 */         ClassFile owner = annotation.getOwner();
/*  73 */         AnnotationInfo[] clAnnotations = annotation.getAnnotations();
/*  74 */         for (int j = 0; j < clAnnotations.length; j++) {
/*  75 */           Utf8CpInfo cpEntry = (Utf8CpInfo)owner.getCpEntry(annotation.getU2TypeIndex(j));
/*  76 */           String currentAnnotationName = cpEntry.getString();
/*     */           
/*  78 */           if (currentAnnotationName != null && currentAnnotationName.contains(ObfuscationConfig.annotationClassName)) {
/*     */             
/*  80 */             AnnotationInfo clAnnotation = clAnnotations[j];
/*  81 */             boolean exclude = getExclude(clAnnotation, owner);
/*  82 */             boolean applyToMembers = getApplyToMembers(clAnnotation, owner);
/*  83 */             Logger.getInstance().log(String.format("Applied annotation %s to %s", new Object[] { ObfuscationConfig.annotationClassName, name }));
/*  84 */             return new ObfuscationConfig(exclude, applyToMembers);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean getExclude(AnnotationInfo clAnnotation, ClassFile owner) {
/*  96 */     ElementValuePairInfo[] elementValuePairs = clAnnotation.getElementValuePairs();
/*     */     
/*  98 */     for (int i = 0; i < elementValuePairs.length; i++) {
/*  99 */       ElementValuePairInfo elementValuePair = elementValuePairs[i];
/* 100 */       Utf8CpInfo cpEntry = (Utf8CpInfo)owner.getCpEntry(elementValuePair.getU2ElementNameIndex());
/* 101 */       if ("exclude".equals(cpEntry.getString())) {
/* 102 */         return elementValuePair.getElementValue().getBoolValue(owner.getConstantPool());
/*     */       }
/*     */     } 
/*     */     
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean getApplyToMembers(AnnotationInfo clAnnotation, ClassFile owner) {
/* 113 */     ElementValuePairInfo[] elementValuePairs = clAnnotation.getElementValuePairs();
/*     */     
/* 115 */     for (int i = 0; i < elementValuePairs.length; i++) {
/* 116 */       ElementValuePairInfo elementValuePair = elementValuePairs[i];
/* 117 */       Utf8CpInfo cpEntry = (Utf8CpInfo)owner.getCpEntry(elementValuePair.getU2ElementNameIndex());
/* 118 */       if ("applyToMembers".equals(cpEntry.getString())) {
/* 119 */         return elementValuePair.getElementValue().getBoolValue(owner.getConstantPool());
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/* 131 */     return this.isSynthetic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getNameIndex() {
/* 138 */     return this.u2nameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setNameIndex(int index) {
/* 145 */     this.u2nameIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDescriptorIndex() {
/* 152 */     return this.u2descriptorIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setDescriptorIndex(int index) {
/* 159 */     this.u2descriptorIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 168 */     return ((Utf8CpInfo)this.cf.getCpEntry(this.u2nameIndex)).getString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 178 */     return ((Utf8CpInfo)this.cf.getCpEntry(this.u2descriptorIndex)).getString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 188 */     return this.u2accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trimAttrsExcept(String[] keepAttrs) {
/* 199 */     this.attributes = AttrInfo.filter(this.attributes, keepAttrs);
/* 200 */     this.u2attributesCount = this.attributes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/* 210 */     pool.incRefCount(this.u2nameIndex);
/* 211 */     pool.incRefCount(this.u2descriptorIndex);
/* 212 */     for (int i = 0; i < this.attributes.length; i++)
/*     */     {
/* 214 */       this.attributes[i].markUtf8Refs(pool);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void read(DataInput din) throws IOException {
/* 226 */     this.u2accessFlags = din.readUnsignedShort();
/* 227 */     this.u2nameIndex = din.readUnsignedShort();
/* 228 */     this.u2descriptorIndex = din.readUnsignedShort();
/* 229 */     this.u2attributesCount = din.readUnsignedShort();
/* 230 */     this.attributes = new AttrInfo[this.u2attributesCount];
/* 231 */     for (int i = 0; i < this.u2attributesCount; i++) {
/*     */       
/* 233 */       this.attributes[i] = AttrInfo.create(din, this.cf);
/* 234 */       if (this.attributes[i].getAttrName().equals("Synthetic"))
/*     */       {
/* 236 */         this.isSynthetic = true;
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
/*     */   
/*     */   public void write(DataOutput dout) throws IOException {
/* 249 */     if (dout == null) throw new NullPointerException("No output stream was provided."); 
/* 250 */     dout.writeShort(this.u2accessFlags);
/* 251 */     dout.writeShort(this.u2nameIndex);
/* 252 */     dout.writeShort(this.u2descriptorIndex);
/* 253 */     dout.writeShort(this.u2attributesCount);
/* 254 */     for (int i = 0; i < this.u2attributesCount; i++)
/*     */     {
/* 256 */       this.attributes[i].write(dout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscationConfig getObfuscationConfig() {
/* 266 */     if (this.obfuscationConfig == DUMMY) {
/* 267 */       this.obfuscationConfig = getObfuscationConfig(String.format("%s#%s", new Object[] { this.cf.getName(), getName() }), this.attributes);
/*     */     }
/* 269 */     return this.obfuscationConfig;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ClassItemInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */