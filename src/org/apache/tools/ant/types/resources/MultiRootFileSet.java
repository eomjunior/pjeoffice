/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.types.AbstractFileSet;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiRootFileSet
/*     */   extends AbstractFileSet
/*     */   implements ResourceCollection
/*     */ {
/*  41 */   private SetType type = SetType.file;
/*     */   private boolean cache = true;
/*  43 */   private List<File> baseDirs = new ArrayList<>();
/*     */   
/*     */   private Union union;
/*     */   
/*     */   public void setDir(File dir) {
/*  48 */     throw new BuildException(getDataTypeName() + " doesn't support the dir attribute");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(SetType type) {
/*  57 */     if (isReference()) {
/*  58 */       throw tooManyAttributes();
/*     */     }
/*  60 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setCache(boolean b) {
/*  68 */     if (isReference()) {
/*  69 */       throw tooManyAttributes();
/*     */     }
/*  71 */     this.cache = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaseDirs(String dirs) {
/*  79 */     if (isReference()) {
/*  80 */       throw tooManyAttributes();
/*     */     }
/*  82 */     if (dirs != null && !dirs.isEmpty()) {
/*  83 */       for (String d : dirs.split(",")) {
/*  84 */         this.baseDirs.add(getProject().resolveFile(d));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredBaseDir(FileResource r) {
/*  94 */     if (isReference()) {
/*  95 */       throw noChildrenAllowed();
/*     */     }
/*  97 */     this.baseDirs.add(r.getFile());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 102 */     if (!this.baseDirs.isEmpty()) {
/* 103 */       throw tooManyAttributes();
/*     */     }
/* 105 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 115 */     if (isReference()) {
/* 116 */       return getRef().clone();
/*     */     }
/* 118 */     MultiRootFileSet fs = (MultiRootFileSet)super.clone();
/* 119 */     fs.baseDirs = new ArrayList<>(this.baseDirs);
/* 120 */     fs.union = null;
/* 121 */     return fs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 130 */     if (isReference()) {
/* 131 */       return getRef().iterator();
/*     */     }
/* 133 */     return merge().iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 142 */     if (isReference()) {
/* 143 */       return getRef().size();
/*     */     }
/* 145 */     return merge().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 154 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     if (isReference()) {
/* 165 */       return getRef().toString();
/*     */     }
/* 167 */     return merge().toString();
/*     */   }
/*     */   
/*     */   private MultiRootFileSet getRef() {
/* 171 */     return (MultiRootFileSet)getCheckedRef(MultiRootFileSet.class);
/*     */   }
/*     */   
/*     */   private synchronized Union merge() {
/* 175 */     if (this.cache && this.union != null) {
/* 176 */       return this.union;
/*     */     }
/* 178 */     Union u = new Union();
/* 179 */     setup(u);
/* 180 */     if (this.cache) {
/* 181 */       this.union = u;
/*     */     }
/* 183 */     return u;
/*     */   }
/*     */   
/*     */   private void setup(Union u) {
/* 187 */     for (File d : this.baseDirs) {
/* 188 */       u.add(new Worker(this, this.type, d));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum SetType
/*     */   {
/* 196 */     file, dir, both;
/*     */   }
/*     */   
/*     */   private static class Worker
/*     */     extends AbstractFileSet
/*     */     implements ResourceCollection {
/*     */     private final MultiRootFileSet.SetType type;
/*     */     
/*     */     private Worker(MultiRootFileSet fs, MultiRootFileSet.SetType type, File dir) {
/* 205 */       super(fs);
/* 206 */       this.type = type;
/* 207 */       setDir(dir);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFilesystemOnly() {
/* 212 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Resource> iterator() {
/* 217 */       DirectoryScanner ds = getDirectoryScanner();
/*     */ 
/*     */       
/* 220 */       String[] names = (this.type == MultiRootFileSet.SetType.file) ? ds.getIncludedFiles() : ds.getIncludedDirectories();
/* 221 */       if (this.type == MultiRootFileSet.SetType.both) {
/* 222 */         String[] files = ds.getIncludedFiles();
/* 223 */         String[] merged = new String[names.length + files.length];
/* 224 */         System.arraycopy(names, 0, merged, 0, names.length);
/* 225 */         System.arraycopy(files, 0, merged, names.length, files.length);
/* 226 */         names = merged;
/*     */       } 
/* 228 */       return new FileResourceIterator(getProject(), getDir(getProject()), names);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 234 */       DirectoryScanner ds = getDirectoryScanner();
/*     */ 
/*     */       
/* 237 */       int count = (this.type == MultiRootFileSet.SetType.file) ? ds.getIncludedFilesCount() : ds.getIncludedDirsCount();
/* 238 */       if (this.type == MultiRootFileSet.SetType.both) {
/* 239 */         count += ds.getIncludedFilesCount();
/*     */       }
/* 241 */       return count;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/MultiRootFileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */