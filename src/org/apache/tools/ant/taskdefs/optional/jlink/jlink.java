/*     */ package org.apache.tools.ant.taskdefs.optional.jlink;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipOutputStream;
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
/*     */ public class jlink
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int VECTOR_INIT_SIZE = 10;
/*  49 */   private String outfile = null;
/*     */   
/*  51 */   private List<String> mergefiles = new Vector<>(10);
/*     */   
/*  53 */   private List<String> addfiles = new Vector<>(10);
/*     */ 
/*     */   
/*     */   private boolean compression = false;
/*     */ 
/*     */   
/*  59 */   byte[] buffer = new byte[8192];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutfile(String outfile) {
/*  67 */     if (outfile == null) {
/*     */       return;
/*     */     }
/*  70 */     this.outfile = outfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMergeFile(String fileToMerge) {
/*  78 */     if (fileToMerge == null) {
/*     */       return;
/*     */     }
/*  81 */     this.mergefiles.add(fileToMerge);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAddFile(String fileToAdd) {
/*  88 */     if (fileToAdd == null) {
/*     */       return;
/*     */     }
/*  91 */     this.addfiles.add(fileToAdd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMergeFiles(String... filesToMerge) {
/*  99 */     if (filesToMerge == null) {
/*     */       return;
/*     */     }
/* 102 */     for (String element : filesToMerge) {
/* 103 */       addMergeFile(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAddFiles(String... filesToAdd) {
/* 112 */     if (filesToAdd == null) {
/*     */       return;
/*     */     }
/* 115 */     for (String element : filesToAdd) {
/* 116 */       addAddFile(element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompression(boolean compress) {
/* 125 */     this.compression = compress;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void link() throws Exception {
/* 142 */     ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(Paths.get(this.outfile, new String[0]), new java.nio.file.OpenOption[0]));
/*     */     try {
/* 144 */       if (this.compression) {
/* 145 */         output.setMethod(8);
/* 146 */         output.setLevel(-1);
/*     */       } else {
/* 148 */         output.setMethod(0);
/*     */       } 
/* 150 */       for (String path : this.mergefiles) {
/* 151 */         File f = new File(path);
/*     */         
/* 153 */         if (f.getName().endsWith(".jar") || f
/* 154 */           .getName().endsWith(".zip")) {
/*     */           
/* 156 */           mergeZipJarContents(output, f);
/*     */           
/*     */           continue;
/*     */         } 
/* 160 */         addAddFile(path);
/*     */       } 
/*     */       
/* 163 */       for (String name : this.addfiles) {
/* 164 */         File f = new File(name);
/*     */         
/* 166 */         if (f.isDirectory()) {
/* 167 */           addDirContents(output, f, f.getName() + '/', this.compression); continue;
/*     */         } 
/* 169 */         addFile(output, f, "", this.compression);
/*     */       } 
/*     */       
/* 172 */       output.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         output.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     }  } public static void main(String[] args) {
/* 181 */     if (args.length < 2) {
/* 182 */       System.out.println("usage: jlink output input1 ... inputN");
/* 183 */       System.exit(1);
/*     */     } 
/* 185 */     jlink linker = new jlink();
/*     */     
/* 187 */     linker.setOutfile(args[0]);
/*     */ 
/*     */     
/* 190 */     for (int i = 1; i < args.length; i++) {
/* 191 */       linker.addMergeFile(args[i]);
/*     */     }
/*     */     try {
/* 194 */       linker.link();
/* 195 */     } catch (Exception ex) {
/* 196 */       System.err.print(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeZipJarContents(ZipOutputStream output, File f) throws IOException {
/* 206 */     if (!f.exists()) {
/*     */       return;
/*     */     }
/* 209 */     ZipFile zipf = new ZipFile(f); try {
/* 210 */       Enumeration<? extends ZipEntry> entries = zipf.entries();
/* 211 */       while (entries.hasMoreElements()) {
/* 212 */         ZipEntry inputEntry = entries.nextElement();
/*     */ 
/*     */ 
/*     */         
/* 216 */         String inputEntryName = inputEntry.getName();
/* 217 */         int index = inputEntryName.indexOf("META-INF");
/*     */         
/* 219 */         if (index < 0) {
/*     */           
/*     */           try {
/* 222 */             output.putNextEntry(processEntry(zipf, inputEntry));
/* 223 */           } catch (ZipException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 230 */             if (ex.getMessage().contains("duplicate")) {
/*     */               continue;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 236 */             throw ex;
/*     */           } 
/*     */           
/* 239 */           InputStream in = zipf.getInputStream(inputEntry); 
/* 240 */           try { int len = this.buffer.length;
/* 241 */             int count = -1;
/*     */             
/* 243 */             while ((count = in.read(this.buffer, 0, len)) > 0) {
/* 244 */               output.write(this.buffer, 0, count);
/*     */             }
/* 246 */             output.closeEntry();
/* 247 */             if (in != null) in.close();  } catch (Throwable throwable) { if (in != null)
/*     */               try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; } 
/*     */         } 
/* 250 */       }  zipf.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         zipf.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/* 258 */     }  } private void addDirContents(ZipOutputStream output, File dir, String prefix, boolean compress) throws IOException { for (String name : dir.list()) {
/* 259 */       File file = new File(dir, name);
/*     */       
/* 261 */       if (file.isDirectory()) {
/* 262 */         addDirContents(output, file, prefix + name + '/', compress);
/*     */       } else {
/* 264 */         addFile(output, file, prefix, compress);
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getEntryName(File file, String prefix) {
/* 275 */     String name = file.getName();
/*     */     
/* 277 */     if (!name.endsWith(".class")) {
/*     */       
/* 279 */       try { InputStream input = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]); 
/* 280 */         try { String className = ClassNameReader.getClassName(input);
/*     */           
/* 282 */           if (className != null)
/* 283 */           { String str = className.replace('.', '/') + ".class";
/*     */             
/* 285 */             if (input != null) input.close();  return str; }  if (input != null) input.close();  } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 289 */     System.out.printf("From %1$s and prefix %2$s, creating entry %2$s%3$s%n", new Object[] { file
/*     */           
/* 291 */           .getPath(), prefix, name });
/* 292 */     return prefix + name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFile(ZipOutputStream output, File file, String prefix, boolean compress) throws IOException {
/* 302 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/* 305 */     ZipEntry entry = new ZipEntry(getEntryName(file, prefix));
/*     */     
/* 307 */     entry.setTime(file.lastModified());
/* 308 */     entry.setSize(file.length());
/* 309 */     if (!compress) {
/* 310 */       entry.setCrc(calcChecksum(file));
/*     */     }
/* 312 */     addToOutputStream(output, Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToOutputStream(ZipOutputStream output, InputStream input, ZipEntry ze) throws IOException {
/*     */     try {
/* 321 */       output.putNextEntry(ze);
/* 322 */     } catch (ZipException zipEx) {
/*     */       
/* 324 */       input.close();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     int numBytes;
/* 330 */     while ((numBytes = input.read(this.buffer)) > 0) {
/* 331 */       output.write(this.buffer, 0, numBytes);
/*     */     }
/* 333 */     output.closeEntry();
/* 334 */     input.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipEntry processEntry(ZipFile zip, ZipEntry inputEntry) {
/* 355 */     String name = inputEntry.getName();
/*     */     
/* 357 */     if (!inputEntry.isDirectory() && !name.endsWith(".class")) {
/* 358 */       try { InputStream input = zip.getInputStream(zip.getEntry(name)); 
/* 359 */         try { String className = ClassNameReader.getClassName(input);
/*     */           
/* 361 */           if (className != null) {
/* 362 */             name = className.replace('.', '/') + ".class";
/*     */           }
/* 364 */           if (input != null) input.close();  } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 368 */     ZipEntry outputEntry = new ZipEntry(name);
/*     */     
/* 370 */     outputEntry.setTime(inputEntry.getTime());
/* 371 */     outputEntry.setExtra(inputEntry.getExtra());
/* 372 */     outputEntry.setComment(inputEntry.getComment());
/* 373 */     outputEntry.setTime(inputEntry.getTime());
/* 374 */     if (this.compression) {
/* 375 */       outputEntry.setMethod(8);
/*     */     } else {
/*     */       
/* 378 */       outputEntry.setMethod(0);
/* 379 */       outputEntry.setCrc(inputEntry.getCrc());
/* 380 */       outputEntry.setSize(inputEntry.getSize());
/*     */     } 
/* 382 */     return outputEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long calcChecksum(File f) throws IOException {
/* 390 */     return calcChecksum(new BufferedInputStream(
/* 391 */           Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long calcChecksum(InputStream in) throws IOException {
/* 399 */     CRC32 crc = new CRC32();
/* 400 */     int len = this.buffer.length;
/*     */     
/*     */     int count;
/* 403 */     while ((count = in.read(this.buffer, 0, len)) > 0) {
/* 404 */       crc.update(this.buffer, 0, count);
/*     */     }
/* 406 */     in.close();
/* 407 */     return crc.getValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jlink/jlink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */