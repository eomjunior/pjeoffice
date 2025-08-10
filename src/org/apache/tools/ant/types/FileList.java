/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class FileList
/*     */   extends DataType
/*     */   implements ResourceCollection
/*     */ {
/*  40 */   private List<String> filenames = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File dir;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileList() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileList(FileList filelist) {
/*  57 */     this.dir = filelist.dir;
/*  58 */     this.filenames = filelist.filenames;
/*  59 */     setProject(filelist.getProject());
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
/*     */   public void setRefid(Reference r) throws BuildException {
/*  73 */     if (this.dir != null || !this.filenames.isEmpty()) {
/*  74 */       throw tooManyAttributes();
/*     */     }
/*  76 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File dir) throws BuildException {
/*  86 */     checkAttributesAllowed();
/*  87 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDir(Project p) {
/*  95 */     if (isReference()) {
/*  96 */       return getRef(p).getDir(p);
/*     */     }
/*  98 */     return this.dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiles(String filenames) {
/* 108 */     checkAttributesAllowed();
/* 109 */     if (filenames != null && !filenames.isEmpty()) {
/* 110 */       StringTokenizer tok = new StringTokenizer(filenames, ", \t\n\r\f", false);
/*     */       
/* 112 */       while (tok.hasMoreTokens()) {
/* 113 */         this.filenames.add(tok.nextToken());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getFiles(Project p) {
/* 124 */     if (isReference()) {
/* 125 */       return getRef(p).getFiles(p);
/*     */     }
/*     */     
/* 128 */     if (this.dir == null) {
/* 129 */       throw new BuildException("No directory specified for filelist.");
/*     */     }
/*     */     
/* 132 */     if (this.filenames.isEmpty()) {
/* 133 */       throw new BuildException("No files specified for filelist.");
/*     */     }
/*     */     
/* 136 */     return this.filenames.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FileName
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 151 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 158 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredFile(FileName name) {
/* 169 */     if (name.getName() == null) {
/* 170 */       throw new BuildException("No name specified in nested file element");
/*     */     }
/*     */     
/* 173 */     this.filenames.add(name.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Resource> iterator() {
/* 183 */     if (isReference()) {
/* 184 */       return getRef().iterator();
/*     */     }
/* 186 */     return (Iterator<Resource>)new FileResourceIterator(getProject(), this.dir, this.filenames
/* 187 */         .<String>toArray(new String[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 197 */     if (isReference()) {
/* 198 */       return getRef().size();
/*     */     }
/* 200 */     return this.filenames.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFilesystemOnly() {
/* 210 */     return true;
/*     */   }
/*     */   
/*     */   private FileList getRef() {
/* 214 */     return getCheckedRef(FileList.class);
/*     */   }
/*     */   
/*     */   private FileList getRef(Project p) {
/* 218 */     return getCheckedRef(FileList.class, getDataTypeName(), p);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/FileList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */