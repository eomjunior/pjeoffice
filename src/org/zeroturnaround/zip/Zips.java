/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import org.zeroturnaround.zip.commons.FileUtils;
/*     */ import org.zeroturnaround.zip.commons.IOUtils;
/*     */ import org.zeroturnaround.zip.transform.ZipEntryTransformer;
/*     */ import org.zeroturnaround.zip.transform.ZipEntryTransformerEntry;
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
/*     */ public class Zips
/*     */ {
/*     */   private final File src;
/*     */   private File dest;
/*     */   private Charset charset;
/*     */   private boolean preserveTimestamps;
/*  78 */   private List<ZipEntrySource> changedEntries = new ArrayList<ZipEntrySource>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private Set<String> removedEntries = new HashSet<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private List<ZipEntryTransformerEntry> transformers = new ArrayList<ZipEntryTransformerEntry>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameMapper nameMapper;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean unpackedResult;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Zips(File src) {
/* 103 */     this.src = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Zips get(File src) {
/* 113 */     return new Zips(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Zips create() {
/* 123 */     return new Zips(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips addEntry(ZipEntrySource entry) {
/* 134 */     this.changedEntries.add(entry);
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips addEntries(ZipEntrySource[] entries) {
/* 146 */     this.changedEntries.addAll(Arrays.asList(entries));
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips addFile(File file) {
/* 158 */     return addFile(file, false, null);
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
/*     */   public Zips addFile(File file, boolean preserveRoot) {
/* 171 */     return addFile(file, preserveRoot, null);
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
/*     */   public Zips addFile(File file, FileFilter filter) {
/* 183 */     return addFile(file, false, filter);
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
/*     */   public Zips addFile(File file, boolean preserveRoot, FileFilter filter) {
/* 197 */     if (!file.isDirectory()) {
/* 198 */       this.changedEntries.add(new FileSource(file.getName(), file));
/* 199 */       return this;
/*     */     } 
/*     */     
/* 202 */     Collection<File> files = ZTFileUtil.listFiles(file);
/* 203 */     for (File entryFile : files) {
/* 204 */       if (filter != null && !filter.accept(entryFile)) {
/*     */         continue;
/*     */       }
/* 207 */       String entryPath = getRelativePath(file, entryFile);
/* 208 */       if (File.separatorChar == '\\')
/*     */       {
/* 210 */         entryPath = entryPath.replace('\\', '/');
/*     */       }
/* 212 */       if (preserveRoot) {
/* 213 */         entryPath = file.getName() + entryPath;
/*     */       }
/* 215 */       if (entryPath.startsWith("/")) {
/* 216 */         entryPath = entryPath.substring(1);
/*     */       }
/* 218 */       this.changedEntries.add(new FileSource(entryPath, entryFile));
/*     */     } 
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   private String getRelativePath(File parent, File file) {
/* 224 */     String parentPath = parent.getPath();
/* 225 */     String filePath = file.getPath();
/* 226 */     if (!filePath.startsWith(parentPath)) {
/* 227 */       throw new IllegalArgumentException("File " + file + " is not a child of " + parent);
/*     */     }
/* 229 */     return filePath.substring(parentPath.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips removeEntry(String entry) {
/* 239 */     this.removedEntries.add(entry);
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips removeEntries(String[] entries) {
/* 250 */     this.removedEntries.addAll(Arrays.asList(entries));
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips preserveTimestamps() {
/* 260 */     this.preserveTimestamps = true;
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips setPreserveTimestamps(boolean preserve) {
/* 271 */     this.preserveTimestamps = preserve;
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips charset(Charset charset) {
/* 282 */     this.charset = charset;
/* 283 */     return this;
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
/*     */   public Zips destination(File destination) {
/* 296 */     this.dest = destination;
/* 297 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips nameMapper(NameMapper nameMapper) {
/* 306 */     this.nameMapper = nameMapper;
/* 307 */     return this;
/*     */   }
/*     */   
/*     */   public Zips unpack() {
/* 311 */     this.unpackedResult = true;
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInPlace() {
/* 319 */     return (this.dest == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isUnpack() {
/* 326 */     return (this.unpackedResult || (this.dest != null && this.dest.isDirectory()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zips addTransformer(String path, ZipEntryTransformer transformer) {
/* 337 */     this.transformers.add(new ZipEntryTransformerEntry(path, transformer));
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process() {
/* 346 */     if (this.src == null && this.dest == null) {
/* 347 */       throw new IllegalArgumentException("Source and destination shouldn't be null together");
/*     */     }
/*     */     
/* 350 */     File destinationFile = null;
/*     */     try {
/* 352 */       destinationFile = getDestinationFile();
/* 353 */       ZipOutputStream out = null;
/* 354 */       ZipEntryOrInfoAdapter zipEntryAdapter = null;
/*     */       
/* 356 */       if (destinationFile.isFile()) {
/* 357 */         out = ZipFileUtil.createZipOutputStream(new BufferedOutputStream(new FileOutputStream(destinationFile)), this.charset);
/* 358 */         zipEntryAdapter = new ZipEntryOrInfoAdapter(new CopyingCallback(this.transformers, out, this.preserveTimestamps), null);
/*     */       } else {
/*     */         
/* 361 */         zipEntryAdapter = new ZipEntryOrInfoAdapter(new UnpackingCallback(this.transformers, destinationFile), null);
/*     */       } 
/*     */       try {
/* 364 */         processAllEntries(zipEntryAdapter);
/*     */       } finally {
/*     */         
/* 367 */         IOUtils.closeQuietly(out);
/*     */       } 
/* 369 */       handleInPlaceActions(destinationFile);
/*     */     }
/* 371 */     catch (IOException e) {
/* 372 */       ZipExceptionUtil.rethrow(e);
/*     */     } finally {
/*     */       
/* 375 */       if (isInPlace())
/*     */       {
/* 377 */         FileUtils.deleteQuietly(destinationFile);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processAllEntries(ZipEntryOrInfoAdapter zipEntryAdapter) {
/* 383 */     iterateChangedAndAdded(zipEntryAdapter);
/* 384 */     iterateExistingExceptRemoved(zipEntryAdapter);
/*     */   }
/*     */   
/*     */   private File getDestinationFile() throws IOException {
/* 388 */     if (isUnpack()) {
/* 389 */       if (isInPlace()) {
/* 390 */         File tempFile = File.createTempFile("zips", null);
/* 391 */         FileUtils.deleteQuietly(tempFile);
/* 392 */         tempFile.mkdirs();
/* 393 */         return tempFile;
/*     */       } 
/*     */       
/* 396 */       if (!this.dest.isDirectory()) {
/*     */         
/* 398 */         FileUtils.deleteQuietly(this.dest);
/* 399 */         File result = new File(this.dest.getAbsolutePath());
/* 400 */         result.mkdirs();
/* 401 */         return result;
/*     */       } 
/* 403 */       return this.dest;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 408 */     if (isInPlace()) {
/* 409 */       return File.createTempFile("zips", ".zip");
/*     */     }
/*     */     
/* 412 */     if (this.dest.isDirectory()) {
/*     */       
/* 414 */       FileUtils.deleteQuietly(this.dest);
/* 415 */       return new File(this.dest.getAbsolutePath());
/*     */     } 
/* 417 */     return this.dest;
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
/*     */   public void iterate(ZipEntryCallback zipEntryCallback) {
/* 436 */     ZipEntryOrInfoAdapter zipEntryAdapter = new ZipEntryOrInfoAdapter(zipEntryCallback, null);
/* 437 */     processAllEntries(zipEntryAdapter);
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
/*     */   public void iterate(ZipInfoCallback callback) {
/* 454 */     ZipEntryOrInfoAdapter zipEntryAdapter = new ZipEntryOrInfoAdapter(null, callback);
/*     */     
/* 456 */     processAllEntries(zipEntryAdapter);
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
/*     */   public byte[] getEntry(String name) {
/* 468 */     if (this.src == null) {
/* 469 */       throw new IllegalStateException("Source is not given");
/*     */     }
/* 471 */     return ZipUtil.unpackEntry(this.src, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsEntry(String name) {
/* 482 */     if (this.src == null) {
/* 483 */       throw new IllegalStateException("Source is not given");
/*     */     }
/* 485 */     return ZipUtil.containsEntry(this.src, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iterateExistingExceptRemoved(ZipEntryOrInfoAdapter zipEntryCallback) {
/* 496 */     if (this.src == null) {
/*     */       return;
/*     */     }
/*     */     
/* 500 */     Set<String> removedDirs = ZipUtil.filterDirEntries(this.src, this.removedEntries);
/*     */     
/* 502 */     ZipFile zf = null;
/*     */     try {
/* 504 */       zf = getZipFile();
/*     */ 
/*     */       
/* 507 */       Enumeration<? extends ZipEntry> en = zf.entries();
/* 508 */       while (en.hasMoreElements()) {
/* 509 */         ZipEntry entry = en.nextElement();
/* 510 */         String entryName = entry.getName();
/* 511 */         if (this.removedEntries.contains(entryName) || isEntryInDir(removedDirs, entryName)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 516 */         if (this.nameMapper != null) {
/* 517 */           String mappedName = this.nameMapper.map(entry.getName());
/* 518 */           if (mappedName == null) {
/*     */             continue;
/*     */           }
/* 521 */           if (!mappedName.equals(entry.getName()))
/*     */           {
/* 523 */             entry = ZipEntryUtil.copy(entry, mappedName);
/*     */           }
/*     */         } 
/*     */         
/* 527 */         InputStream is = zf.getInputStream(entry);
/*     */         try {
/* 529 */           zipEntryCallback.process(is, entry);
/*     */         }
/* 531 */         catch (ZipBreakException ex) {
/*     */           
/*     */           break;
/*     */         } finally {
/* 535 */           IOUtils.closeQuietly(is);
/*     */         }
/*     */       
/*     */       } 
/* 539 */     } catch (IOException e) {
/* 540 */       ZipExceptionUtil.rethrow(e);
/*     */     } finally {
/*     */       
/* 543 */       ZipUtil.closeQuietly(zf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void iterateChangedAndAdded(ZipEntryOrInfoAdapter zipEntryCallback) {
/* 554 */     for (ZipEntrySource entrySource : this.changedEntries) {
/* 555 */       InputStream entrySourceStream = null;
/*     */       
/* 557 */       try { ZipEntry entry = entrySource.getEntry();
/* 558 */         if (this.nameMapper != null)
/* 559 */         { String mappedName = this.nameMapper.map(entry.getName());
/* 560 */           if (mappedName == null)
/*     */           
/*     */           { 
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
/* 578 */             IOUtils.closeQuietly(entrySourceStream); continue; }  if (!mappedName.equals(entry.getName())) entry = ZipEntryUtil.copy(entry, mappedName);  }  entrySourceStream = entrySource.getInputStream(); zipEntryCallback.process(entrySourceStream, entry); } catch (ZipBreakException ex) { break; } catch (IOException e) { ZipExceptionUtil.rethrow(e); } finally { IOUtils.closeQuietly(entrySourceStream); }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleInPlaceActions(File result) throws IOException {
/* 589 */     if (isInPlace()) {
/*     */       
/* 591 */       FileUtils.forceDelete(this.src);
/* 592 */       if (result.isFile()) {
/* 593 */         FileUtils.moveFile(result, this.src);
/*     */       } else {
/*     */         
/* 596 */         FileUtils.moveDirectory(result, this.src);
/*     */       } 
/*     */     } 
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
/*     */   private boolean isEntryInDir(Set<String> dirNames, String entryName) {
/* 610 */     for (String dirName : dirNames) {
/* 611 */       if (entryName.startsWith(dirName)) {
/* 612 */         return true;
/*     */       }
/*     */     } 
/* 615 */     return false;
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
/*     */   private ZipFile getZipFile() throws IOException {
/* 628 */     return ZipFileUtil.getZipFile(this.src, this.charset);
/*     */   }
/*     */   
/*     */   private static class CopyingCallback
/*     */     implements ZipEntryCallback {
/*     */     private final Map<String, ZipEntryTransformer> entryByPath;
/*     */     private final ZipOutputStream out;
/*     */     private final Set<String> visitedNames;
/*     */     private final boolean preserveTimestapms;
/*     */     
/*     */     private CopyingCallback(List<ZipEntryTransformerEntry> transformerEntries, ZipOutputStream out, boolean preserveTimestapms) {
/* 639 */       this.out = out;
/* 640 */       this.preserveTimestapms = preserveTimestapms;
/* 641 */       this.entryByPath = ZipUtil.transformersByPath(transformerEntries);
/* 642 */       this.visitedNames = new HashSet<String>();
/*     */     }
/*     */     
/*     */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 646 */       String entryName = zipEntry.getName();
/*     */       
/* 648 */       if (this.visitedNames.contains(entryName)) {
/*     */         return;
/*     */       }
/* 651 */       this.visitedNames.add(entryName);
/*     */       
/* 653 */       ZipEntryTransformer transformer = this.entryByPath.remove(entryName);
/* 654 */       if (transformer == null) {
/* 655 */         ZipEntryUtil.copyEntry(zipEntry, in, this.out, this.preserveTimestapms);
/*     */       } else {
/*     */         
/* 658 */         transformer.transform(in, zipEntry, this.out);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UnpackingCallback
/*     */     implements ZipEntryCallback {
/*     */     private final Map<String, ZipEntryTransformer> entryByPath;
/*     */     private final Set<String> visitedNames;
/*     */     private final File destination;
/*     */     
/*     */     private UnpackingCallback(List<ZipEntryTransformerEntry> entries, File destination) {
/* 670 */       this.destination = destination;
/* 671 */       this.entryByPath = ZipUtil.transformersByPath(entries);
/* 672 */       this.visitedNames = new HashSet<String>();
/*     */     }
/*     */     
/*     */     public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 676 */       String entryName = zipEntry.getName();
/*     */       
/* 678 */       if (this.visitedNames.contains(entryName)) {
/*     */         return;
/*     */       }
/* 681 */       this.visitedNames.add(entryName);
/*     */       
/* 683 */       File file = new File(this.destination, entryName);
/* 684 */       if (zipEntry.isDirectory()) {
/* 685 */         FileUtils.forceMkdir(file);
/*     */         
/*     */         return;
/*     */       } 
/* 689 */       FileUtils.forceMkdir(file.getParentFile());
/* 690 */       file.createNewFile();
/*     */ 
/*     */       
/* 693 */       ZipEntryTransformer transformer = this.entryByPath.remove(entryName);
/* 694 */       if (transformer == null) {
/* 695 */         FileUtils.copy(in, file);
/*     */       } else {
/*     */         
/* 698 */         transformIntoFile(transformer, in, zipEntry, file);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void transformIntoFile(final ZipEntryTransformer transformer, final InputStream entryIn, final ZipEntry zipEntry, File destination) throws IOException {
/* 703 */       PipedInputStream pipedIn = new PipedInputStream();
/* 704 */       PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);
/*     */       
/* 706 */       final ZipOutputStream zipOut = new ZipOutputStream(pipedOut);
/* 707 */       ZipInputStream zipIn = new ZipInputStream(pipedIn);
/*     */       
/* 709 */       ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
/*     */       
/*     */       try {
/* 712 */         newFixedThreadPool.execute(new Runnable() {
/*     */               public void run() {
/*     */                 try {
/* 715 */                   transformer.transform(entryIn, zipEntry, zipOut);
/*     */                 }
/* 717 */                 catch (IOException e) {
/* 718 */                   ZipExceptionUtil.rethrow(e);
/*     */                 } 
/*     */               }
/*     */             });
/* 722 */         zipIn.getNextEntry();
/* 723 */         FileUtils.copy(zipIn, destination);
/*     */       } finally {
/*     */         
/*     */         try {
/* 727 */           zipIn.closeEntry();
/*     */         }
/* 729 */         catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */         
/* 733 */         newFixedThreadPool.shutdown();
/* 734 */         IOUtils.closeQuietly(pipedIn);
/* 735 */         IOUtils.closeQuietly(zipIn);
/* 736 */         IOUtils.closeQuietly(pipedOut);
/* 737 */         IOUtils.closeQuietly(zipOut);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/Zips.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */