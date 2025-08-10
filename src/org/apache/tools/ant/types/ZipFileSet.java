/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZipFileSet
/*     */   extends ArchiveFileSet
/*     */ {
/*     */   public ZipFileSet() {}
/*     */   
/*     */   protected ZipFileSet(FileSet fileset) {
/*  46 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ZipFileSet(ZipFileSet fileset) {
/*  54 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ArchiveScanner newArchiveScanner() {
/*  63 */     ZipScanner zs = new ZipScanner();
/*  64 */     zs.setEncoding(getEncoding());
/*  65 */     return zs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef(Project p) {
/*  76 */     dieOnCircularReference(p);
/*  77 */     Object o = getRefid().getReferencedObject(p);
/*  78 */     if (o instanceof ZipFileSet) {
/*  79 */       return (AbstractFileSet)o;
/*     */     }
/*  81 */     if (o instanceof FileSet) {
/*  82 */       ZipFileSet zfs = new ZipFileSet((FileSet)o);
/*  83 */       configureFileSet(zfs);
/*  84 */       return zfs;
/*     */     } 
/*  86 */     String msg = getRefid().getRefId() + " doesn't denote a zipfileset or a fileset";
/*  87 */     throw new BuildException(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef() {
/*  97 */     return getRef(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 107 */     if (isReference()) {
/* 108 */       return getRef().clone();
/*     */     }
/* 110 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ZipFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */