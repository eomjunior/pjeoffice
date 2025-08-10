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
/*     */ 
/*     */ public class DirSet
/*     */   extends AbstractFileSet
/*     */   implements ResourceCollection
/*     */ {
/*     */   public DirSet() {}
/*     */   
/*     */   protected DirSet(DirSet dirset) {
/*  46 */     super(dirset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  56 */     if (isReference()) {
/*  57 */       return getRef().clone();
/*     */     }
/*  59 */     return super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/*  69 */     if (isReference()) {
/*  70 */       return getRef().iterator();
/*     */     }
/*  72 */     return (Iterator<Resource>)new FileResourceIterator(getProject(), getDir(getProject()), 
/*  73 */         getDirectoryScanner().getIncludedDirectories());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  83 */     if (isReference()) {
/*  84 */       return getRef().size();
/*     */     }
/*  86 */     return getDirectoryScanner().getIncludedDirsCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/*  96 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return String.join(";", (CharSequence[])getDirectoryScanner().getIncludedDirectories());
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractFileSet getRef(Project p) {
/* 111 */     return getCheckedRef((Class)DirSet.class, getDataTypeName(), p);
/*     */   }
/*     */   
/*     */   private DirSet getRef() {
/* 115 */     return getCheckedRef(DirSet.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/DirSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */