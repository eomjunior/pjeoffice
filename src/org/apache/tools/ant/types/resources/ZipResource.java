/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.zip.ZipEntry;
/*     */ import org.apache.tools.zip.ZipExtraField;
/*     */ import org.apache.tools.zip.ZipFile;
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
/*     */ public class ZipResource
/*     */   extends ArchiveResource
/*     */ {
/*     */   private String encoding;
/*     */   private ZipExtraField[] extras;
/*     */   private int method;
/*     */   
/*     */   public ZipResource() {}
/*     */   
/*     */   public ZipResource(File z, String enc, ZipEntry e) {
/*  59 */     super(z, true);
/*  60 */     setEncoding(enc);
/*  61 */     setEntry(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setZipfile(File z) {
/*  69 */     setArchive(z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getZipfile() {
/*  77 */     FileProvider fp = (FileProvider)getArchive().as(FileProvider.class);
/*  78 */     return fp.getFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection a) {
/*  87 */     super.addConfigured(a);
/*  88 */     if (!a.isFilesystemOnly()) {
/*  89 */       throw new BuildException("only filesystem resources are supported");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String enc) {
/*  98 */     checkAttributesAllowed();
/*  99 */     this.encoding = enc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 107 */     return isReference() ? 
/* 108 */       getRef().getEncoding() : this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRefid(Reference r) {
/* 116 */     if (this.encoding != null) {
/* 117 */       throw tooManyAttributes();
/*     */     }
/* 119 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/* 129 */     if (isReference()) {
/* 130 */       return getRef().getInputStream();
/*     */     }
/* 132 */     return getZipEntryStream(new ZipFile(getZipfile(), getEncoding()), getName());
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
/*     */   public OutputStream getOutputStream() throws IOException {
/* 144 */     if (isReference()) {
/* 145 */       return getRef().getOutputStream();
/*     */     }
/* 147 */     throw new UnsupportedOperationException("Use the zip task for zip output.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipExtraField[] getExtraFields() {
/* 157 */     if (isReference()) {
/* 158 */       return getRef().getExtraFields();
/*     */     }
/* 160 */     checkEntry();
/* 161 */     if (this.extras == null) {
/* 162 */       return new ZipExtraField[0];
/*     */     }
/* 164 */     return this.extras;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMethod() {
/* 173 */     return this.method;
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
/*     */   public static InputStream getZipEntryStream(final ZipFile zipFile, String zipEntry) throws IOException {
/* 186 */     ZipEntry ze = zipFile.getEntry(zipEntry);
/* 187 */     if (ze == null) {
/* 188 */       zipFile.close();
/* 189 */       throw new BuildException("no entry " + zipEntry + " in " + zipFile.getName());
/*     */     } 
/* 191 */     return new FilterInputStream(zipFile.getInputStream(ze)) {
/*     */         public void close() throws IOException {
/* 193 */           FileUtils.close(this.in);
/* 194 */           zipFile.close();
/*     */         }
/*     */         protected void finalize() throws Throwable {
/*     */           try {
/* 198 */             close();
/*     */           } finally {
/* 200 */             super.finalize();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fetchEntry() {
/* 210 */     ZipFile z = null;
/*     */     try {
/* 212 */       z = new ZipFile(getZipfile(), getEncoding());
/* 213 */       setEntry(z.getEntry(getName()));
/* 214 */     } catch (IOException e) {
/* 215 */       log(e.getMessage(), 4);
/* 216 */       throw new BuildException(e);
/*     */     } finally {
/* 218 */       ZipFile.closeQuietly(z);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ZipResource getRef() {
/* 224 */     return (ZipResource)getCheckedRef(ZipResource.class);
/*     */   }
/*     */   
/*     */   private void setEntry(ZipEntry e) {
/* 228 */     if (e == null) {
/* 229 */       setExists(false);
/*     */       return;
/*     */     } 
/* 232 */     setName(e.getName());
/* 233 */     setExists(true);
/* 234 */     setLastModified(e.getTime());
/* 235 */     setDirectory(e.isDirectory());
/* 236 */     setSize(e.getSize());
/* 237 */     setMode(e.getUnixMode());
/* 238 */     this.extras = e.getExtraFields(true);
/* 239 */     this.method = e.getMethod();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/ZipResource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */