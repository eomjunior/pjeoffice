/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileResourceIterator
/*     */   implements Iterator<Resource>
/*     */ {
/*     */   private Project project;
/*     */   private File basedir;
/*     */   private String[] files;
/*  35 */   private int pos = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResourceIterator(Project project) {
/*  51 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public FileResourceIterator(File basedir) {
/*  62 */     this((Project)null, basedir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResourceIterator(Project project, File basedir) {
/*  73 */     this(project);
/*  74 */     this.basedir = basedir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public FileResourceIterator(File basedir, String[] filenames) {
/*  86 */     this(null, basedir, filenames);
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
/*     */   public FileResourceIterator(Project project, File basedir, String[] filenames) {
/*  98 */     this(project, basedir);
/*  99 */     addFiles(filenames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFiles(String[] s) {
/* 107 */     int start = (this.files == null) ? 0 : this.files.length;
/* 108 */     String[] newfiles = new String[start + s.length];
/* 109 */     if (start > 0) {
/* 110 */       System.arraycopy(this.files, 0, newfiles, 0, start);
/*     */     }
/* 112 */     this.files = newfiles;
/* 113 */     System.arraycopy(s, 0, this.files, start, s.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 122 */     return (this.pos < this.files.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource next() {
/* 131 */     return nextResource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileResource nextResource() {
/* 147 */     if (!hasNext()) {
/* 148 */       throw new NoSuchElementException();
/*     */     }
/* 150 */     FileResource result = new FileResource(this.basedir, this.files[this.pos++]);
/* 151 */     result.setProject(this.project);
/* 152 */     return result;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public FileResourceIterator() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/FileResourceIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */