/*     */ package org.apache.tools.ant.taskdefs.optional.depend;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ClassCPInfo;
/*     */ import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPool;
/*     */ import org.apache.tools.ant.taskdefs.optional.depend.constantpool.ConstantPoolEntry;
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
/*     */ public class ClassFile
/*     */ {
/*     */   private static final int CLASS_MAGIC = -889275714;
/*     */   private ConstantPool constantPool;
/*     */   private String className;
/*     */   
/*     */   public void read(InputStream stream) throws IOException, ClassFormatError {
/*  59 */     DataInputStream classStream = new DataInputStream(stream);
/*     */     
/*  61 */     if (classStream.readInt() != -889275714) {
/*  62 */       throw new ClassFormatError("No Magic Code Found - probably not a Java class file.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  67 */     classStream.readUnsignedShort();
/*  68 */     classStream.readUnsignedShort();
/*     */ 
/*     */     
/*  71 */     this.constantPool = new ConstantPool();
/*     */     
/*  73 */     this.constantPool.read(classStream);
/*  74 */     this.constantPool.resolve();
/*     */     
/*  76 */     classStream.readUnsignedShort();
/*  77 */     int thisClassIndex = classStream.readUnsignedShort();
/*  78 */     classStream.readUnsignedShort();
/*     */     
/*  80 */     ClassCPInfo classInfo = (ClassCPInfo)this.constantPool.getEntry(thisClassIndex);
/*  81 */     this.className = classInfo.getClassName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<String> getClassRefs() {
/*  91 */     Vector<String> classRefs = new Vector<>();
/*     */     
/*  93 */     int size = this.constantPool.size();
/*  94 */     for (int i = 0; i < size; i++) {
/*  95 */       ConstantPoolEntry entry = this.constantPool.getEntry(i);
/*     */       
/*  97 */       if (entry != null && entry
/*  98 */         .getTag() == 7) {
/*  99 */         ClassCPInfo classEntry = (ClassCPInfo)entry;
/*     */         
/* 101 */         if (!classEntry.getClassName().equals(this.className)) {
/* 102 */           classRefs.add(
/* 103 */               ClassFileUtils.convertSlashName(classEntry.getClassName()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     return classRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFullClassName() {
/* 117 */     return ClassFileUtils.convertSlashName(this.className);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */