/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.resources.FileResourceIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSet
/*     */   extends AbstractFileSet
/*     */   implements ResourceCollection
/*     */ {
/*     */   public FileSet() {}
/*     */   
/*     */   protected FileSet(FileSet fileset) {
/*  45 */     super(fileset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  55 */     if (isReference()) {
/*  56 */       return getRef().clone();
/*     */     }
/*  58 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/*  68 */     if (isReference()) {
/*  69 */       return getRef().iterator();
/*     */     }
/*  71 */     return (Iterator<Resource>)new FileResourceIterator(getProject(), getDir(getProject()), 
/*  72 */         getDirectoryScanner().getIncludedFiles());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  82 */     if (isReference()) {
/*  83 */       return getRef().size();
/*     */     }
/*  85 */     return getDirectoryScanner().getIncludedFilesCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef(Project p) {
/* 100 */     return getCheckedRef((Class)FileSet.class, getDataTypeName(), p);
/*     */   }
/*     */   
/*     */   private FileSet getRef() {
/* 104 */     return getCheckedRef(FileSet.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/FileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */