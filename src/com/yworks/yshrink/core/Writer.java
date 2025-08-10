/*     */ package com.yworks.yshrink.core;
/*     */ 
/*     */ import com.yworks.common.ResourcePolicy;
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.util.Version;
/*     */ import com.yworks.util.abstractjar.Archive;
/*     */ import com.yworks.util.abstractjar.ArchiveWriter;
/*     */ import com.yworks.util.abstractjar.Factory;
/*     */ import com.yworks.util.abstractjar.StreamProvider;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import org.objectweb.asm.ClassReader;
/*     */ import org.objectweb.asm.ClassWriter;
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
/*     */ public class Writer
/*     */ {
/*     */   private static final String MANIFEST_FILENAME = "META-INF/MANIFEST.MF";
/*     */   private static final String SIGNATURE_FILE_PREFIX = "META-INF/";
/*     */   private static final String SIGNATURE_FILE_SUFFIX = ".SF";
/*     */   private final boolean createStubs;
/*     */   private final MessageDigest[] digests;
/*     */   
/*     */   public Writer(boolean createStubs, String digestNamesStr) {
/*  54 */     this.createStubs = createStubs;
/*     */ 
/*     */     
/*  57 */     String[] digestNames = digestNamesStr.trim().equalsIgnoreCase("none") ? new String[0] : digestNamesStr.split(",");
/*     */     int i;
/*  59 */     for (i = 0; i < digestNames.length; i++) {
/*  60 */       digestNames[i] = digestNames[i].trim();
/*     */     }
/*     */     
/*  63 */     this.digests = new MessageDigest[digestNames.length];
/*     */     
/*  65 */     for (i = digestNames.length - 1; i >= 0; i--) {
/*     */       try {
/*  67 */         this.digests[i] = MessageDigest.getInstance(digestNames[i]);
/*  68 */       } catch (NoSuchAlgorithmException e) {
/*  69 */         Logger.err("Unknwon digest algorithm: " + digestNames[i]);
/*  70 */         this.digests[i] = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Model model, ShrinkBag bag) throws IOException {
/*  77 */     File in = bag.getIn();
/*  78 */     File out = bag.getOut();
/*     */     
/*  80 */     Logger.log("writing shrinked " + in + " to " + out + ".");
/*     */     
/*  82 */     Logger.shrinkLog("<inOutPair in=\"" + in + "\" out=\"" + out + "\">");
/*     */     
/*  84 */     long inLength = in.length();
/*     */     
/*  86 */     StreamProvider provider = Factory.newStreamProvider(in);
/*     */     
/*  88 */     if (!out.exists()) out.createNewFile();
/*     */     
/*  90 */     Manifest newManifest = getManifest(in);
/*  91 */     ArchiveWriter writer = newArchiveCreator(out, newManifest);
/*     */     
/*  93 */     int numClasses = 0;
/*  94 */     int numObsoleteClasses = 0;
/*  95 */     int numObsoleteMethods = 0;
/*  96 */     int numObsoleteFields = 0;
/*  97 */     int numRemovedResources = 0;
/*     */     
/*  99 */     Set<String> nonEmptyDirs = new HashSet<>(5);
/*     */     
/* 101 */     Logger.shrinkLog("\t<removed-code>");
/*     */     
/* 103 */     DataInputStream stream = provider.getNextClassEntryStream();
/* 104 */     for (; stream != null; 
/* 105 */       stream = provider.getNextClassEntryStream()) {
/*     */       
/* 107 */       String entryName = provider.getCurrentEntryName();
/*     */       
/* 109 */       numClasses++;
/*     */       
/* 111 */       ClassDescriptor cd = model.getClassDescriptor(entryName
/* 112 */           .substring(0, entryName.lastIndexOf(".class")));
/* 113 */       boolean obsolete = model.isObsolete(cd.getNode());
/*     */       
/* 115 */       if (!obsolete) {
/*     */         
/* 117 */         nonEmptyDirs.add(provider.getCurrentDir());
/*     */ 
/*     */         
/* 120 */         ClassWriter cw = new ClassWriter(1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 125 */         OutputVisitor outputVisitor = new OutputVisitor(cw, model, this.createStubs);
/* 126 */         ClassReader cr = new ClassReader(stream);
/*     */ 
/*     */         
/* 129 */         cr.accept(outputVisitor, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 134 */         numObsoleteMethods += outputVisitor.getNumObsoleteMethods();
/* 135 */         numObsoleteFields += outputVisitor.getNumObsoleteFields();
/*     */         
/* 137 */         byte[] modifiedClass = cw.toByteArray();
/* 138 */         writer.addFile(entryName, modifiedClass);
/*     */       } else {
/* 140 */         newManifest.getEntries().remove(entryName);
/* 141 */         numObsoleteClasses++;
/* 142 */         Logger.shrinkLog("\t\t<class name=\"" + Util.toJavaClass(entryName) + "\" />");
/*     */       } 
/*     */       
/* 145 */       close(stream);
/*     */     } 
/*     */     
/* 148 */     Logger.shrinkLog("\t</removed-code>");
/* 149 */     Logger.shrinkLog("\t<removed-resources>");
/*     */     
/* 151 */     ResourcePolicy resourcePolicy = bag.getResources();
/*     */     
/* 153 */     if (!resourcePolicy.equals(ResourcePolicy.NONE)) {
/*     */       
/* 155 */       provider.reset();
/*     */       
/* 157 */       DataInputStream dataInputStream = provider.getNextResourceEntryStream();
/* 158 */       for (; dataInputStream != null; 
/* 159 */         dataInputStream = provider.getNextResourceEntryStream()) {
/* 160 */         String entryName = provider.getCurrentEntryName();
/*     */         
/* 162 */         if (!resourcePolicy.equals(ResourcePolicy.NONE) && (resourcePolicy
/* 163 */           .equals(ResourcePolicy.COPY) || (resourcePolicy
/* 164 */           .equals(ResourcePolicy.AUTO) && nonEmptyDirs
/* 165 */           .contains(provider.getCurrentDir())))) {
/*     */           
/* 167 */           copyResource(entryName, provider, dataInputStream, writer);
/*     */         } else {
/* 169 */           numRemovedResources++;
/* 170 */           Logger.shrinkLog("\t<resource dir=\"" + provider
/* 171 */               .getCurrentDir() + "\" name=\"" + provider.getCurrentFilename() + "\" />");
/*     */         } 
/*     */         
/* 174 */         close(dataInputStream);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 179 */       provider.close();
/* 180 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 184 */     Logger.shrinkLog("\t</removed-resources>");
/*     */     
/* 186 */     writer.close();
/*     */     
/* 188 */     long outLength = out.length();
/*     */     
/* 190 */     NumberFormat nf = NumberFormat.getPercentInstance();
/* 191 */     nf.setMinimumFractionDigits(2);
/* 192 */     String percent = nf.format(1.0D - outLength / inLength);
/*     */     
/* 194 */     Logger.log("\tshrinked " + in + " BY " + percent + ".");
/* 195 */     Logger.log("\tsize before: " + (inLength / 1024L) + " KB, size after: " + (outLength / 1024L) + " KB.");
/* 196 */     Logger.log("\tremoved " + numObsoleteClasses + " classes, " + numObsoleteMethods + " methods, " + numObsoleteFields + " fields, " + numRemovedResources + " resources.");
/*     */     
/* 198 */     Logger.log("\t" + (numClasses - numObsoleteClasses) + " classes remaining of " + numClasses + " total.");
/*     */     
/* 200 */     Logger.shrinkLog("</inOutPair>");
/*     */   }
/*     */   
/*     */   private static void close(InputStream is) {
/*     */     try {
/* 205 */       is.close();
/* 206 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void copyResource(String entryName, StreamProvider jarStreamProvider, DataInputStream stream, ArchiveWriter writer) throws IOException {
/* 215 */     if (!entryName.equals("META-INF/MANIFEST.MF") && (
/* 216 */       !entryName.endsWith(".SF") || 
/* 217 */       !entryName.startsWith("META-INF/"))) {
/*     */       
/* 219 */       int entrySize = (int)jarStreamProvider.getCurrentEntry().getSize();
/* 220 */       if (-1 != entrySize) {
/* 221 */         byte[] data = new byte[entrySize];
/* 222 */         stream.readFully(data);
/* 223 */         writer.addFile(entryName, data);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArchiveWriter newArchiveCreator(File out, Manifest manifest) throws IOException {
/* 232 */     return out.isDirectory() ? 
/* 233 */       (ArchiveWriter)new DirectoryWriter(out, manifest) : 
/* 234 */       new JarWriter(out, manifest);
/*     */   }
/*     */   
/*     */   private static Manifest getManifest(File file) throws IOException {
/* 238 */     Archive inJar = Factory.newArchive(file);
/*     */     
/* 240 */     Manifest oldManifest = inJar.getManifest();
/*     */     
/* 242 */     Manifest newManifest = (oldManifest != null) ? new Manifest(oldManifest) : new Manifest();
/*     */     
/*     */     try {
/* 245 */       inJar.close();
/* 246 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 250 */     return newManifest;
/*     */   }
/*     */   
/*     */   private class JarWriter
/*     */     extends ArchiveWriter
/*     */   {
/* 256 */     private Set<String> directoriesWritten = new HashSet<>();
/*     */     
/*     */     private final FileOutputStream fos;
/*     */     private final JarOutputStream jos;
/*     */     private Manifest manifest;
/*     */     
/*     */     public JarWriter(File outFile, Manifest manifest) throws IOException {
/* 263 */       super(manifest);
/*     */       
/* 265 */       this.manifest = manifest;
/*     */       
/* 267 */       this.fos = new FileOutputStream(outFile);
/*     */       
/* 269 */       this.jos = new JarOutputStream(new BufferedOutputStream(this.fos));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void addDigests(String entryName) {
/* 276 */       Attributes oldEntryAttributes = this.manifest.getAttributes(entryName);
/* 277 */       Attributes newEntryAttributes = new Attributes(Writer.this.digests.length + 1);
/*     */       
/* 279 */       if (null != oldEntryAttributes) {
/* 280 */         Set<Object> keys = oldEntryAttributes.keySet();
/* 281 */         for (Object key : keys) {
/* 282 */           if (((Attributes.Name)key).toString().indexOf("Digest") == -1) {
/* 283 */             newEntryAttributes.put(key, oldEntryAttributes.get(key));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 288 */       StringBuffer digestsList = new StringBuffer();
/* 289 */       for (int i = 0; i < Writer.this.digests.length; i++) {
/* 290 */         MessageDigest digest = Writer.this.digests[i];
/*     */         
/* 292 */         if (null != digest) {
/*     */           
/* 294 */           String digestKey = digest.getAlgorithm() + "-Digest";
/* 295 */           digestsList.append(digest.getAlgorithm());
/* 296 */           if (i < Writer.this.digests.length - 1) {
/* 297 */             digestsList.append(", ");
/*     */           }
/*     */           
/* 300 */           String digestVal = Util.toBase64(digest.digest());
/*     */           
/* 302 */           newEntryAttributes.putValue(digestKey, digestVal);
/*     */         } 
/*     */       } 
/*     */       
/* 306 */       newEntryAttributes.putValue("Digest-Algorithms", digestsList.toString());
/*     */       
/* 308 */       this.manifest.getEntries().put(entryName, newEntryAttributes);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setComment(String comment) {
/* 313 */       this.jos.setComment(comment);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void addFile(String fileName, byte[] data) throws IOException {
/* 319 */       JarEntry outEntry = new JarEntry(fileName);
/* 320 */       addDirectory(fileName);
/* 321 */       this.jos.putNextEntry(outEntry);
/* 322 */       this.jos.write(data);
/* 323 */       this.jos.closeEntry();
/*     */       
/* 325 */       calcDigests(data);
/*     */       
/* 327 */       addDigests(fileName);
/*     */     }
/*     */ 
/*     */     
/*     */     private void calcDigests(byte[] data) {
/* 332 */       for (int i = Writer.this.digests.length - 1; i >= 0; i--) {
/* 333 */         if (null != Writer.this.digests[i]) {
/* 334 */           Writer.this.digests[i].reset();
/* 335 */           Writer.this.digests[i].update(data);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void addDirectory(String fileName) throws IOException {
/* 342 */       int index = 0;
/* 343 */       while ((index = fileName.indexOf("/", index + 1)) >= 0) {
/* 344 */         String directory = fileName.substring(0, index + 1);
/* 345 */         if (!this.directoriesWritten.contains(directory)) {
/* 346 */           this.directoriesWritten.add(directory);
/* 347 */           JarEntry directoryEntry = new JarEntry(directory);
/* 348 */           this.jos.putNextEntry(directoryEntry);
/* 349 */           this.jos.closeEntry();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 357 */       finishManifest();
/*     */       
/* 359 */       if (this.jos != null) {
/* 360 */         this.jos.finish();
/* 361 */         this.jos.close();
/*     */       } 
/* 363 */       if (this.fos != null) {
/* 364 */         this.fos.close();
/*     */       }
/*     */     }
/*     */     
/*     */     private void finishManifest() throws IOException {
/* 369 */       this.manifest.getMainAttributes().putValue("Created-by", "yGuard Bytecode Obfuscator: Shrinker " + 
/* 370 */           Version.getVersion());
/*     */       
/* 372 */       addDirectory("META-INF/MANIFEST.MF");
/* 373 */       this.jos.putNextEntry(new JarEntry("META-INF/MANIFEST.MF"));
/* 374 */       this.manifest.write(this.jos);
/* 375 */       this.jos.closeEntry();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/core/Writer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */