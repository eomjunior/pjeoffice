/*      */ package com.yworks.yguard.obf;
/*      */ 
/*      */ import com.yworks.util.Version;
/*      */ import com.yworks.util.abstractjar.Archive;
/*      */ import com.yworks.util.abstractjar.ArchiveWriter;
/*      */ import com.yworks.util.abstractjar.Entry;
/*      */ import com.yworks.util.abstractjar.Factory;
/*      */ import com.yworks.yguard.Conversion;
/*      */ import com.yworks.yguard.ObfuscationListener;
/*      */ import com.yworks.yguard.ParseException;
/*      */ import com.yworks.yguard.obf.classfile.ClassConstants;
/*      */ import com.yworks.yguard.obf.classfile.ClassFile;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.security.DigestOutputStream;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.Manifest;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GuardDB
/*      */   implements ClassConstants
/*      */ {
/*      */   private static final String STREAM_NAME_MANIFEST = "META-INF/MANIFEST.MF";
/*      */   private static final String MANIFEST_NAME_TAG = "Name";
/*      */   private static final String MANIFEST_DIGESTALG_TAG = "Digest-Algorithms";
/*      */   private static final String CLASS_EXT = ".class";
/*      */   private static final String MULTI_RELEASE_PREFIX = "META-INF/versions/";
/*      */   private static final String SIGNATURE_PREFIX = "META-INF/";
/*      */   private static final String SIGNATURE_EXT = ".SF";
/*      */   private static final String LOG_MEMORY_USED = "  Memory in use after class data structure built: ";
/*      */   private static final String LOG_MEMORY_TOTAL = "  Total memory available                        : ";
/*      */   private static final String LOG_MEMORY_BYTES = " bytes";
/*      */   private static final String WARNING_SCRIPT_ENTRY_ABSENT = "<!-- WARNING - identifier from script file not found in JAR: ";
/*      */   private static final String ERROR_CORRUPT_CLASS = "<!-- ERROR - corrupt class file: ";
/*      */   private Archive[] inJar;
/*      */   private Manifest[] oldManifest;
/*      */   private Manifest[] newManifest;
/*      */   private ClassTree classTree;
/*      */   private boolean hasMap = false;
/*      */   private transient ArrayList listenerList;
/*      */   private boolean replaceClassNameStrings;
/*      */   private boolean pedantic;
/*      */   private ResourceHandler resourceHandler;
/*      */   private String[] digestStrings;
/*      */   
/*      */   public GuardDB(File[] inFile) throws IOException {
/*  103 */     this.inJar = new Archive[inFile.length];
/*  104 */     for (int i = 0; i < inFile.length; i++) {
/*  105 */       this.inJar[i] = Factory.newArchive(inFile[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void finalize() throws IOException {
/*  111 */     close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceHandler(ResourceHandler handler) {
/*  121 */     this.resourceHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOutName(String inName) {
/*  132 */     return this.classTree.getOutName(inName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void retain(Collection rgsEntries, PrintWriter log) throws IOException {
/*  147 */     if (this.classTree == null || this.hasMap) {
/*      */       
/*  149 */       this.hasMap = false;
/*  150 */       buildClassTree(log);
/*      */     } 
/*      */ 
/*      */     
/*  154 */     retainByAnnotation();
/*      */ 
/*      */ 
/*      */     
/*  158 */     retainByRule(rgsEntries, log);
/*      */   }
/*      */   
/*      */   private void retainByAnnotation() {
/*  162 */     this.classTree.walkTree(new TreeAction()
/*      */         {
/*      */           private ObfuscationConfig getApplyingObfuscationConfig(Cl cl) {
/*  165 */             ObfuscationConfig obfuscationConfig = cl.getObfuscationConfig();
/*  166 */             if (cl.getObfuscationConfig() != null && obfuscationConfig.applyToMembers) {
/*  167 */               return obfuscationConfig;
/*      */             }
/*  169 */             Cl currentCl = cl;
/*      */             
/*  171 */             while (currentCl.isInnerClass()) {
/*  172 */               TreeItem parent = currentCl.getParent();
/*  173 */               if (parent instanceof Cl) {
/*  174 */                 currentCl = (Cl)parent;
/*  175 */                 ObfuscationConfig parentConfig = currentCl.getObfuscationConfig();
/*  176 */                 if (parentConfig != null && parentConfig.applyToMembers) {
/*  177 */                   return parentConfig;
/*      */                 }
/*      */                 continue;
/*      */               } 
/*  181 */               return null;
/*      */             } 
/*      */ 
/*      */             
/*  185 */             return null;
/*      */           }
/*      */ 
/*      */           
/*      */           public void classAction(Cl cl) {
/*  190 */             super.classAction(cl);
/*      */ 
/*      */             
/*  193 */             ObfuscationConfig config = cl.getObfuscationConfig();
/*      */             
/*  195 */             if (config != null) {
/*  196 */               if (config.exclude) {
/*  197 */                 GuardDB.this.classTree.retainClass(cl.getFullInName(), 4103, 0, 0, true);
/*      */               }
/*      */             } else {
/*      */               
/*  201 */               ObfuscationConfig parentConfig = getApplyingObfuscationConfig(cl);
/*  202 */               if (parentConfig != null && parentConfig.exclude)
/*      */               {
/*  204 */                 GuardDB.this.classTree.retainClass(cl.getFullInName(), 4103, 0, 0, true);
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void methodAction(Md md) {
/*  211 */             super.methodAction(md);
/*      */             
/*  213 */             ObfuscationConfig config = md.getObfuscationConfig();
/*      */ 
/*      */             
/*  216 */             if (config != null) {
/*  217 */               if (config.exclude) {
/*  218 */                 GuardDB.this.classTree.retainMethod(md.getFullInName(), md.getDescriptor());
/*      */               }
/*      */             } else {
/*      */               
/*  222 */               ObfuscationConfig parentConfig = getApplyingObfuscationConfig((Cl)md.getParent());
/*  223 */               if (parentConfig != null && parentConfig.exclude)
/*      */               {
/*  225 */                 GuardDB.this.classTree.retainMethod(md.getFullInName(), md.getDescriptor());
/*      */               }
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void fieldAction(Fd fd) {
/*  232 */             super.fieldAction(fd);
/*      */             
/*  234 */             ObfuscationConfig config = fd.getObfuscationConfig();
/*      */ 
/*      */             
/*  237 */             if (config != null) {
/*  238 */               if (config.exclude) {
/*  239 */                 GuardDB.this.classTree.retainField(fd.getFullInName());
/*      */               }
/*      */             } else {
/*      */               
/*  243 */               ObfuscationConfig parentConfig = getApplyingObfuscationConfig((Cl)fd.getParent());
/*  244 */               if (parentConfig != null && parentConfig.exclude)
/*      */               {
/*  246 */                 GuardDB.this.classTree.retainField(fd.getFullInName());
/*      */               }
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private void retainByRule(Collection rgsEntries, PrintWriter log) {
/*  254 */     for (Iterator<YGuardRule> it = rgsEntries.iterator(); it.hasNext(); ) {
/*      */       
/*  256 */       YGuardRule entry = it.next();
/*      */       
/*      */       try {
/*  259 */         switch (entry.type) {
/*      */           
/*      */           case 9:
/*  262 */             this.classTree.retainLineNumberTable(entry.name, entry.lineNumberTableMapper);
/*      */             continue;
/*      */           case 8:
/*  265 */             this.classTree.retainSourceFileAttributeMap(entry.name, entry.obfName);
/*      */             continue;
/*      */           case 0:
/*  268 */             this.classTree.retainAttribute(entry.name);
/*      */             continue;
/*      */           case 10:
/*  271 */             this.classTree.retainAttributeForClass(entry.descriptor, entry.name);
/*      */             continue;
/*      */           case 1:
/*  274 */             this.classTree.retainClass(entry.name, entry.retainClasses, entry.retainMethods, entry.retainFields, true);
/*      */             continue;
/*      */           case 3:
/*  277 */             this.classTree.retainMethod(entry.name, entry.descriptor);
/*      */             continue;
/*      */           case 11:
/*  280 */             this.classTree.retainPackage(entry.name);
/*      */             continue;
/*      */           case 2:
/*  283 */             this.classTree.retainField(entry.name);
/*      */             continue;
/*      */           case 4:
/*  286 */             this.classTree.retainPackageMap(entry.name, entry.obfName);
/*      */             continue;
/*      */           case 5:
/*  289 */             this.classTree.retainClassMap(entry.name, entry.obfName);
/*      */             continue;
/*      */           case 7:
/*  292 */             this.classTree.retainMethodMap(entry.name, entry.descriptor, entry.obfName);
/*      */             continue;
/*      */           
/*      */           case 6:
/*  296 */             this.classTree.retainFieldMap(entry.name, entry.obfName);
/*      */             continue;
/*      */         } 
/*  299 */         throw new ParseException("Illegal type: " + entry.type);
/*      */       
/*      */       }
/*  302 */       catch (RuntimeException e) {
/*      */ 
/*      */ 
/*      */         
/*  306 */         log.println("<!-- WARNING - identifier from script file not found in JAR: " + entry.name + " -->");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remapTo(File[] out, Filter fileFilter, PrintWriter log, boolean conserveManifest) throws IOException, ClassNotFoundException {
/*  328 */     if (this.classTree == null)
/*      */     {
/*  330 */       buildClassTree(log);
/*      */     }
/*      */ 
/*      */     
/*  334 */     if (!this.hasMap)
/*      */     {
/*  336 */       createMap(log);
/*      */     }
/*      */     
/*  339 */     this.oldManifest = new Manifest[out.length];
/*  340 */     this.newManifest = new Manifest[out.length];
/*  341 */     parseManifest();
/*      */     
/*  343 */     StringBuffer replaceNameLog = new StringBuffer();
/*  344 */     StringBuffer replaceContentsLog = new StringBuffer();
/*      */     
/*  346 */     ArchiveWriter outJar = null;
/*      */     
/*  348 */     DataInputStream inStream = null;
/*  349 */     for (int i = 0; i < this.inJar.length; i++) {
/*      */       
/*  351 */       outJar = null;
/*      */ 
/*      */       
/*  354 */       List<Object[]> jarEntries = new ArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  360 */       try { Enumeration<Entry> entries = this.inJar[i].getEntries();
/*  361 */         fireObfuscatingJar(this.inJar[i].getName(), out[i].getName());
/*  362 */         ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
/*  363 */         while (entries.hasMoreElements()) {
/*      */ 
/*      */           
/*  366 */           Entry inEntry = entries.nextElement();
/*      */ 
/*      */           
/*  369 */           if (inEntry.isDirectory()) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  376 */           inStream = new DataInputStream(new BufferedInputStream(this.inJar[i].getInputStream(inEntry)));
/*  377 */           String inName = inEntry.getName();
/*  378 */           if (inName.endsWith(".class")) {
/*      */             
/*  380 */             if (fileFilter == null || fileFilter.accepts(inName)) {
/*      */               
/*  382 */               ClassFile cf = ClassFile.create(inStream);
/*  383 */               fireObfuscatingClass(Conversion.toJavaClass(cf.getName()));
/*  384 */               cf.remap(this.classTree, this.replaceClassNameStrings, log);
/*  385 */               String outName = createClassFileName(inName, cf) + ".class";
/*  386 */               JarEntry outEntry = new JarEntry(outName);
/*      */ 
/*      */               
/*  389 */               if (this.digestStrings == null) {
/*  390 */                 this.digestStrings = new String[] { "SHA-1", "MD5" };
/*      */               }
/*  392 */               MessageDigest[] digests = new MessageDigest[this.digestStrings.length];
/*      */               
/*  394 */               DataOutputStream classOutputStream = fillDigests(baos, this.digestStrings, digests);
/*      */ 
/*      */               
/*  397 */               cf.write(classOutputStream);
/*  398 */               classOutputStream.flush();
/*  399 */               jarEntries.add(new Object[] { outEntry, baos.toByteArray() });
/*  400 */               baos.reset();
/*      */               
/*  402 */               updateManifest(i, inName, outName, digests);
/*      */             }  continue;
/*      */           } 
/*  405 */           if ("META-INF/MANIFEST.MF".equals(inName.toUpperCase()) || (inName
/*  406 */             .length() > "META-INF/".length() + 1 + ".SF".length() && inName
/*  407 */             .indexOf("META-INF/") != -1 && inName
/*  408 */             .substring(inName.length() - ".SF".length(), inName.length()).equals(".SF"))) {
/*      */             continue;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  416 */           long size = inEntry.getSize();
/*  417 */           if (size != -1L) {
/*      */             String outName;
/*      */             
/*  420 */             if (this.digestStrings == null) {
/*  421 */               this.digestStrings = new String[] { "SHA-1", "MD5" };
/*      */             }
/*  423 */             MessageDigest[] digests = new MessageDigest[this.digestStrings.length];
/*  424 */             DataOutputStream dataOutputStream = fillDigests(baos, this.digestStrings, digests);
/*      */ 
/*      */ 
/*      */             
/*  428 */             StringBuffer outNameBuffer = new StringBuffer(80);
/*      */             
/*  430 */             if (this.resourceHandler != null && this.resourceHandler.filterName(inName, outNameBuffer)) {
/*      */               
/*  432 */               outName = outNameBuffer.toString();
/*  433 */               if (!outName.equals(inName))
/*      */               {
/*  435 */                 replaceNameLog.append("  <resource name=\"");
/*  436 */                 replaceNameLog.append(ClassTree.toUtf8XmlString(inName));
/*  437 */                 replaceNameLog.append("\" map=\"");
/*  438 */                 replaceNameLog.append(ClassTree.toUtf8XmlString(outName));
/*  439 */                 replaceNameLog.append("\"/>\n");
/*      */               
/*      */               }
/*      */ 
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/*  448 */               String appendName = "";
/*  449 */               if (inName.contains("$")) appendName = inName.substring(inName.lastIndexOf("/")); 
/*  450 */               outName = this.classTree.getOutName(inName);
/*  451 */               if (appendName.length() > 0) {
/*  452 */                 outName = outName.replace(outName.substring(outName.lastIndexOf("/")), appendName);
/*      */               }
/*      */             } 
/*      */             
/*  456 */             if (this.resourceHandler == null || !this.resourceHandler.filterContent(inStream, dataOutputStream, inName)) {
/*      */               
/*  458 */               byte[] bytes = new byte[(int)size];
/*  459 */               inStream.readFully(bytes);
/*      */ 
/*      */ 
/*      */               
/*  463 */               dataOutputStream.write(bytes, 0, bytes.length);
/*      */             }
/*      */             else {
/*      */               
/*  467 */               replaceContentsLog.append("  <resource name=\"");
/*  468 */               replaceContentsLog.append(ClassTree.toUtf8XmlString(inName));
/*  469 */               replaceContentsLog.append("\"/>\n");
/*      */             } 
/*      */             
/*  472 */             dataOutputStream.flush();
/*  473 */             JarEntry outEntry = new JarEntry(outName);
/*      */ 
/*      */             
/*  476 */             jarEntries.add(new Object[] { outEntry, baos.toByteArray() });
/*  477 */             baos.reset();
/*      */             
/*  479 */             updateManifest(i, inName, outName, digests);
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  484 */         if (conserveManifest) {
/*  485 */           outJar = Factory.newArchiveWriter(out[i], this.oldManifest[i]);
/*      */         } else {
/*  487 */           outJar = Factory.newArchiveWriter(out[i], this.newManifest[i]);
/*      */         } 
/*  489 */         if (Version.getJarComment() != null) {
/*  490 */           outJar.setComment(Version.getJarComment());
/*      */         }
/*      */ 
/*      */         
/*  494 */         Collections.sort(jarEntries, new Comparator() {
/*      */               public int compare(Object a, Object b) {
/*  496 */                 Object[] array1 = (Object[])a;
/*  497 */                 JarEntry entry1 = (JarEntry)array1[0];
/*  498 */                 Object[] array2 = (Object[])b;
/*  499 */                 JarEntry entry2 = (JarEntry)array2[0];
/*  500 */                 return entry1.getName().compareTo(entry2.getName());
/*      */               }
/*      */             });
/*      */         
/*  504 */         Set<String> directoriesWritten = new HashSet();
/*  505 */         for (int j = 0; j < jarEntries.size(); j++) {
/*  506 */           Object[] array = jarEntries.get(j);
/*  507 */           JarEntry entry = (JarEntry)array[0];
/*  508 */           String name = entry.getName();
/*      */           
/*  510 */           if (!entry.isDirectory()) {
/*  511 */             int index = 0;
/*  512 */             while ((index = name.indexOf("/", index + 1)) >= 0) {
/*  513 */               String directory = name.substring(0, index + 1);
/*  514 */               if (!directoriesWritten.contains(directory)) {
/*  515 */                 directoriesWritten.add(directory);
/*  516 */                 outJar.addDirectory(directory);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/*  521 */           outJar.addFile(entry.getName(), (byte[])array[1]);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  541 */         this.inJar[i].close();
/*  542 */         if (inStream != null)
/*      */         {
/*  544 */           inStream.close();
/*      */         }
/*  546 */         if (outJar != null)
/*      */         {
/*  548 */           outJar.close(); }  } catch (Exception e) { log.println(); log.println("<!-- An exception has occured."); if (e instanceof java.util.zip.ZipException) { log.println("This is most likely due to a duplicate .class file in your jar!"); log.println("Please check that there are no out-of-date or backup duplicate .class files in your jar!"); }  log.println(e.toString()); e.printStackTrace(log); log.println("-->"); throw new IOException("An error ('" + e.getMessage() + "') occured during the remapping! See the log!)"); } finally { this.inJar[i].close(); if (inStream != null) inStream.close();  if (outJar != null) outJar.close();
/*      */          }
/*      */     
/*      */     } 
/*      */     
/*  553 */     this.classTree.dump(log);
/*  554 */     if (replaceContentsLog.length() > 0 || replaceNameLog.length() > 0) {
/*      */       
/*  556 */       log.println("<!--");
/*  557 */       if (replaceNameLog.length() > 0) {
/*      */         
/*  559 */         log.println("\n<adjust replaceName=\"true\">");
/*  560 */         log.print(replaceNameLog);
/*  561 */         log.println("</adjust>");
/*      */       } 
/*  563 */       if (replaceContentsLog.length() > 0) {
/*      */         
/*  565 */         log.println("\n<adjust replaceContents=\"true\">");
/*  566 */         log.print(replaceContentsLog);
/*  567 */         log.println("</adjust>");
/*      */       } 
/*  569 */       log.println("-->");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private DataOutputStream fillDigests(ByteArrayOutputStream baos, String[] digestStrings, MessageDigest[] digests) throws NoSuchAlgorithmException {
/*  576 */     OutputStream stream = baos;
/*      */     
/*  578 */     for (int i = 0; i < digestStrings.length; i++) {
/*  579 */       String digestString = digestStrings[i];
/*  580 */       MessageDigest digest = MessageDigest.getInstance(digestString);
/*  581 */       digests[i] = digest;
/*  582 */       stream = new DigestOutputStream(stream, digest);
/*      */     } 
/*  584 */     return new DataOutputStream(stream);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  594 */     for (int i = 0; i < this.inJar.length; i++) {
/*      */       
/*  596 */       if (this.inJar[i] != null) {
/*      */         
/*  598 */         this.inJar[i].close();
/*  599 */         this.inJar[i] = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseManifest() throws IOException {
/*  607 */     for (int i = 0; i < this.oldManifest.length; i++) {
/*      */ 
/*      */ 
/*      */       
/*  611 */       this.oldManifest[i] = this.inJar[i].getManifest();
/*      */       
/*  613 */       if (this.oldManifest[i] == null) {
/*  614 */         this.oldManifest[i] = new Manifest();
/*      */       }
/*      */ 
/*      */       
/*  618 */       this.newManifest[i] = new Manifest();
/*      */ 
/*      */       
/*  621 */       for (Iterator<Map.Entry<Object, Object>> iterator = this.oldManifest[i].getMainAttributes().entrySet().iterator(); iterator.hasNext(); ) {
/*  622 */         Map.Entry entry = iterator.next();
/*  623 */         Attributes.Name name = (Attributes.Name)entry.getKey();
/*  624 */         String value = (String)entry.getValue();
/*  625 */         if (this.resourceHandler != null) {
/*  626 */           name = new Attributes.Name(this.resourceHandler.filterString(name.toString(), "META-INF/MANIFEST.MF"));
/*  627 */           value = this.resourceHandler.filterString(value, "META-INF/MANIFEST.MF");
/*      */         } 
/*  629 */         this.newManifest[i].getMainAttributes().putValue(name.toString(), value);
/*      */       } 
/*      */       
/*  632 */       this.newManifest[i].getMainAttributes().putValue("Created-by", "yGuard Bytecode Obfuscator " + Version.getVersion());
/*      */ 
/*      */       
/*  635 */       Iterator<Map.Entry> it = this.oldManifest[i].getEntries().entrySet().iterator();
/*  636 */       while (it.hasNext()) {
/*  637 */         Map.Entry entry = it.next();
/*  638 */         String name = (String)entry.getKey();
/*  639 */         if (name.endsWith("/")) {
/*  640 */           this.newManifest[i].getEntries().put(name, (Attributes)entry.getValue());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateManifest(int manifestIndex, String inName, String outName, MessageDigest[] digests) {
/*  651 */     Manifest nm = this.newManifest[manifestIndex];
/*  652 */     Manifest om = this.oldManifest[manifestIndex];
/*      */     
/*  654 */     Attributes oldAtts = om.getAttributes(inName);
/*  655 */     Attributes newAtts = new Attributes();
/*      */ 
/*      */ 
/*      */     
/*  659 */     if (oldAtts != null) {
/*  660 */       for (Iterator<Map.Entry<Object, Object>> it = oldAtts.entrySet().iterator(); it.hasNext(); ) {
/*  661 */         Map.Entry entry = it.next();
/*  662 */         Object key = entry.getKey();
/*  663 */         String name = key.toString();
/*  664 */         if (!name.equalsIgnoreCase("Name") && name
/*  665 */           .indexOf("Digest") == -1) {
/*  666 */           newAtts.remove(name);
/*  667 */           newAtts.putValue(name, (String)entry.getValue());
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  673 */     if (digests != null && digests.length > 0) {
/*      */ 
/*      */       
/*  676 */       StringBuffer sb = new StringBuffer(); int i;
/*  677 */       for (i = 0; i < digests.length; i++) {
/*      */         
/*  679 */         sb.append(digests[i].getAlgorithm());
/*  680 */         if (i < digests.length - 1) {
/*  681 */           sb.append(", ");
/*      */         }
/*      */       } 
/*  684 */       newAtts.remove("Digest-Algorithms");
/*  685 */       newAtts.putValue("Digest-Algorithms", sb.toString());
/*      */ 
/*      */       
/*  688 */       for (i = 0; i < digests.length; i++) {
/*      */         
/*  690 */         newAtts.remove(digests[i].getAlgorithm() + "-Digest");
/*  691 */         newAtts.putValue(digests[i].getAlgorithm() + "-Digest", Tools.toBase64(digests[i].digest()));
/*      */       } 
/*      */     } 
/*      */     
/*  695 */     if (!newAtts.isEmpty())
/*      */     {
/*  697 */       nm.getEntries().put(outName, newAtts);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildClassTree(PrintWriter log) throws IOException {
/*  705 */     this.classTree = new ClassTree();
/*  706 */     this.classTree.setPedantic(isPedantic());
/*  707 */     this.classTree.setReplaceClassNameStrings(this.replaceClassNameStrings);
/*  708 */     ClassFile.resetDangerHeader();
/*      */     
/*  710 */     Map<Object, Object> parsedClasses = new HashMap<>();
/*  711 */     for (int i = 0; i < this.inJar.length; i++) {
/*      */       
/*  713 */       Enumeration<Entry> entries = this.inJar[i].getEntries();
/*  714 */       fireParsingJar(this.inJar[i].getName());
/*  715 */       while (entries.hasMoreElements()) {
/*      */ 
/*      */         
/*  718 */         Entry inEntry = entries.nextElement();
/*  719 */         String name = inEntry.getName();
/*  720 */         if (name.endsWith(".class")) {
/*      */           
/*  722 */           fireParsingClass(Conversion.toJavaClass(name));
/*      */ 
/*      */ 
/*      */           
/*  726 */           DataInputStream inStream = new DataInputStream(new BufferedInputStream(this.inJar[i].getInputStream(inEntry)));
/*  727 */           ClassFile cf = null;
/*      */           
/*      */           try {
/*  730 */             cf = ClassFile.create(inStream);
/*      */           }
/*  732 */           catch (Exception e) {
/*      */             
/*  734 */             log.println("<!-- ERROR - corrupt class file: " + createJarName(this.inJar[i], name) + " -->");
/*  735 */             e.printStackTrace(log);
/*  736 */             throw new ParseException(e);
/*      */           }
/*      */           finally {
/*      */             
/*  740 */             inStream.close();
/*      */           } 
/*      */           
/*  743 */           if (cf != null) {
/*  744 */             String cfn = cf.getName();
/*      */             
/*  746 */             String key = "module-info".equals(cfn) ? createModuleKey(cf) : createClassFileName(name, cf);
/*      */             
/*  748 */             Object[] old = (Object[])parsedClasses.get(key);
/*  749 */             if (old != null) {
/*  750 */               int jarIndex = ((Integer)old[0]).intValue();
/*      */ 
/*      */ 
/*      */               
/*  754 */               String warning = "yGuard detected a duplicate class definition for \n    " + Conversion.toJavaClass(cfn) + "\n    [" + createJarName(this.inJar[jarIndex], old[1].toString()) + "] in \n    [" + createJarName(this.inJar[i], name) + "]";
/*  755 */               log.write("<!-- \n" + warning + "\n-->\n");
/*  756 */               if (jarIndex == i) {
/*  757 */                 throw new IOException(warning + "\nPlease remove inappropriate duplicates first!");
/*      */               }
/*  759 */               if (this.pedantic) {
/*  760 */                 throw new IOException(warning + "\nMake sure these files are of the same version!");
/*      */               }
/*      */             } else {
/*      */               
/*  764 */               parsedClasses.put(key, new Object[] { new Integer(i), name });
/*      */             } 
/*      */ 
/*      */             
/*  768 */             cf.logDangerousMethods(log, this.replaceClassNameStrings);
/*  769 */             this.classTree.addClassFile(cf);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  777 */     ClassTree ct = this.classTree;
/*  778 */     ct.walkTree(new TreeAction()
/*      */         {
/*      */           public void classAction(Cl cl)
/*      */           {
/*  782 */             if (cl.isInnerClass()) {
/*      */               
/*  784 */               Cl parent = (Cl)cl.getParent();
/*  785 */               cl.access = parent.getInnerClassModifier(cl.getInName());
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   private static String createJarName(Archive jar, String name) {
/*  792 */     return "jar:" + jar.getName() + "|" + name;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String createClassFileName(String jarEntryName, ClassFile cf) {
/*  798 */     String prefix = "META-INF/versions/";
/*  799 */     if (jarEntryName.startsWith("META-INF/versions/")) {
/*  800 */       int idx = jarEntryName.indexOf('/', "META-INF/versions/".length());
/*  801 */       return jarEntryName.substring(0, idx + 1) + cf.getName();
/*      */     } 
/*  803 */     return cf.getName();
/*      */   }
/*      */ 
/*      */   
/*      */   private static String createModuleKey(ClassFile cf) {
/*  808 */     return "module-info:" + cf.findModuleName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createMap(PrintWriter log) throws ClassNotFoundException {
/*  816 */     this.classTree.generateNames();
/*      */ 
/*      */ 
/*      */     
/*  820 */     this.classTree.resolveClasses();
/*      */ 
/*      */     
/*  823 */     this.hasMap = true;
/*      */ 
/*      */     
/*  826 */     Runtime rt = Runtime.getRuntime();
/*  827 */     rt.gc();
/*  828 */     log.println("<!--");
/*  829 */     log.println("  Memory in use after class data structure built: " + Long.toString(rt.totalMemory() - rt.freeMemory()) + " bytes");
/*  830 */     log.println("  Total memory available                        : " + Long.toString(rt.totalMemory()) + " bytes");
/*  831 */     log.println("-->");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fireParsingJar(String jar) {
/*  841 */     if (this.listenerList == null)
/*  842 */       return;  for (int i = 0, j = this.listenerList.size(); i < j; i++) {
/*  843 */       ((ObfuscationListener)this.listenerList.get(i)).parsingJar(jar);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fireParsingClass(String className) {
/*  853 */     if (this.listenerList == null)
/*  854 */       return;  for (int i = 0, j = this.listenerList.size(); i < j; i++) {
/*  855 */       ((ObfuscationListener)this.listenerList.get(i)).parsingClass(className);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fireObfuscatingJar(String inJar, String outJar) {
/*  866 */     if (this.listenerList == null)
/*  867 */       return;  for (int i = 0, j = this.listenerList.size(); i < j; i++) {
/*  868 */       ((ObfuscationListener)this.listenerList.get(i)).obfuscatingJar(inJar, outJar);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fireObfuscatingClass(String className) {
/*  878 */     if (this.listenerList == null)
/*  879 */       return;  for (int i = 0, j = this.listenerList.size(); i < j; i++) {
/*  880 */       ((ObfuscationListener)this.listenerList.get(i)).obfuscatingClass(className);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addListener(ObfuscationListener listener) {
/*  891 */     if (this.listenerList == null)
/*      */     {
/*  893 */       this.listenerList = new ArrayList();
/*      */     }
/*  895 */     this.listenerList.add(listener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeListener(ObfuscationListener listener) {
/*  905 */     if (this.listenerList != null)
/*      */     {
/*  907 */       this.listenerList.remove(listener);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isReplaceClassNameStrings() {
/*  918 */     return this.replaceClassNameStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setReplaceClassNameStrings(boolean replaceClassNameStrings) {
/*  928 */     this.replaceClassNameStrings = replaceClassNameStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPedantic() {
/*  939 */     return this.pedantic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPedantic(boolean pedantic) {
/*  949 */     this.pedantic = pedantic;
/*  950 */     Cl.setPedantic(pedantic);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String translateJavaFile(String javaClass) {
/*  963 */     Cl cl = this.classTree.findClassForName(javaClass.replace('/', '.'));
/*  964 */     if (cl != null)
/*      */     {
/*  966 */       return cl.getFullOutName();
/*      */     }
/*      */ 
/*      */     
/*  970 */     return javaClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String translateJavaClass(String javaClass) {
/*  983 */     Cl cl = this.classTree.findClassForName(javaClass);
/*  984 */     if (cl != null)
/*      */     {
/*  986 */       return cl.getFullOutName().replace('/', '.');
/*      */     }
/*      */ 
/*      */     
/*  990 */     return javaClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> translateItem(String[] items) {
/* 1008 */     List<String> mapped = new ArrayList<>();
/* 1009 */     List<String> partialItems = Arrays.asList(items);
/* 1010 */     TreeItem item = this.classTree.findTreeItem(items);
/* 1011 */     while (partialItems.size() > 0 && item == null) {
/* 1012 */       partialItems = partialItems.subList(0, partialItems.size() - 1);
/* 1013 */       String[] partialItemsArray = new String[partialItems.size()];
/* 1014 */       partialItems.toArray(partialItemsArray);
/* 1015 */       item = this.classTree.findTreeItem(partialItemsArray);
/*      */     } 
/* 1017 */     while (item != null) {
/* 1018 */       mapped.add(item.getOutName());
/* 1019 */       item = item.parent;
/*      */     } 
/* 1021 */     if (mapped.size() > 0) {
/*      */       
/* 1023 */       mapped = mapped.subList(0, mapped.size() - 1);
/*      */       
/* 1025 */       Collections.reverse(mapped);
/*      */     } 
/* 1027 */     return mapped;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDigests(String[] digestStrings) {
/* 1036 */     this.digestStrings = digestStrings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAnnotationClass(String annotationClass) {
/* 1045 */     ObfuscationConfig.annotationClassName = annotationClass;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/GuardDB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */