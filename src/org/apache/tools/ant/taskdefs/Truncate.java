/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Truncate
/*     */   extends Task
/*     */ {
/*     */   private static final int BUFFER_SIZE = 1024;
/*  43 */   private static final Long ZERO = Long.valueOf(0L);
/*     */   
/*     */   private static final String NO_CHILD = "No files specified.";
/*     */   
/*     */   private static final String INVALID_LENGTH = "Cannot truncate to length ";
/*     */   
/*     */   private static final String READ_WRITE = "rw";
/*     */   
/*  51 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  53 */   private static final byte[] FILL_BUFFER = new byte[1024];
/*     */   
/*     */   private Path path;
/*     */   
/*     */   private boolean create = true;
/*     */   
/*     */   private boolean mkdirs = false;
/*     */   
/*     */   private Long length;
/*     */   
/*     */   private Long adjust;
/*     */ 
/*     */   
/*     */   public void setFile(File f) {
/*  67 */     add((ResourceCollection)new FileResource(f));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ResourceCollection rc) {
/*  75 */     getPath().add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdjust(Long adjust) {
/*  84 */     this.adjust = adjust;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLength(Long length) {
/*  93 */     this.length = length;
/*  94 */     if (length != null && length.longValue() < 0L) {
/*  95 */       throw new BuildException("Cannot truncate to length " + length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreate(boolean create) {
/* 104 */     this.create = create;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMkdirs(boolean mkdirs) {
/* 113 */     this.mkdirs = mkdirs;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/* 118 */     if (this.length != null && this.adjust != null) {
/* 119 */       throw new BuildException("length and adjust are mutually exclusive options");
/*     */     }
/*     */     
/* 122 */     if (this.length == null && this.adjust == null) {
/* 123 */       this.length = ZERO;
/*     */     }
/* 125 */     if (this.path == null) {
/* 126 */       throw new BuildException("No files specified.");
/*     */     }
/* 128 */     for (Resource r : this.path) {
/* 129 */       File f = ((FileProvider)r.as(FileProvider.class)).getFile();
/* 130 */       if (shouldProcess(f)) {
/* 131 */         process(f);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean shouldProcess(File f) {
/* 137 */     if (f.isFile()) {
/* 138 */       return true;
/*     */     }
/* 140 */     if (!this.create) {
/* 141 */       return false;
/*     */     }
/* 143 */     Exception exception = null;
/*     */     try {
/* 145 */       if (FILE_UTILS.createNewFile(f, this.mkdirs)) {
/* 146 */         return true;
/*     */       }
/* 148 */     } catch (IOException e) {
/* 149 */       exception = e;
/*     */     } 
/* 151 */     String msg = "Unable to create " + f;
/* 152 */     if (exception == null) {
/* 153 */       log(msg, 1);
/* 154 */       return false;
/*     */     } 
/* 156 */     throw new BuildException(msg, exception);
/*     */   }
/*     */   
/*     */   private void process(File f) {
/* 160 */     long len = f.length();
/*     */     
/* 162 */     long newLength = (this.length == null) ? (len + this.adjust.longValue()) : this.length.longValue();
/*     */     
/* 164 */     if (len == newLength) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     RandomAccessFile raf = null;
/*     */     try {
/* 170 */       raf = new RandomAccessFile(f, "rw");
/* 171 */     } catch (Exception e) {
/* 172 */       throw new BuildException("Could not open " + f + " for writing", e);
/*     */     } 
/*     */     try {
/* 175 */       if (newLength > len) {
/* 176 */         long pos = len;
/* 177 */         raf.seek(pos);
/* 178 */         while (pos < newLength) {
/* 179 */           long writeCount = Math.min(FILL_BUFFER.length, newLength - pos);
/*     */           
/* 181 */           raf.write(FILL_BUFFER, 0, (int)writeCount);
/* 182 */           pos += writeCount;
/*     */         } 
/*     */       } else {
/* 185 */         raf.setLength(newLength);
/*     */       } 
/* 187 */     } catch (IOException e) {
/* 188 */       throw new BuildException("Exception working with " + raf, e);
/*     */     } finally {
/*     */       try {
/* 191 */         raf.close();
/* 192 */       } catch (IOException e) {
/* 193 */         log("Caught " + e + " closing " + raf, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized Path getPath() {
/* 199 */     if (this.path == null) {
/* 200 */       this.path = new Path(getProject());
/*     */     }
/* 202 */     return this.path;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Truncate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */