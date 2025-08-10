/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ import java.util.Vector;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.types.ArchiveFileSet;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.Mapper;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.ZipFileSet;
/*      */ import org.apache.tools.ant.types.spi.Service;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.StreamUtils;
/*      */ import org.apache.tools.zip.JarMarker;
/*      */ import org.apache.tools.zip.ZipEntry;
/*      */ import org.apache.tools.zip.ZipExtraField;
/*      */ import org.apache.tools.zip.ZipFile;
/*      */ import org.apache.tools.zip.ZipOutputStream;
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
/*      */ public class Jar
/*      */   extends Zip
/*      */ {
/*      */   private static final String INDEX_NAME = "META-INF/INDEX.LIST";
/*      */   private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
/*   83 */   private List<Service> serviceList = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest configuredManifest;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest savedConfiguredManifest;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest filesetManifest;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest originalManifest;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FilesetManifestConfig filesetManifestConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mergeManifestsMain = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest manifest;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String manifestEncoding;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private File manifestFile;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean index = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean indexMetaInf = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean createEmpty = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<String> rootEntries;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Path indexJars;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  161 */   private FileNameMapper indexJarsMapper = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   private StrictMode strict = new StrictMode("ignore");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean mergeClassPaths = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean flattenClassPaths = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  188 */   private static final ZipExtraField[] JAR_MARKER = new ZipExtraField[] {
/*  189 */       (ZipExtraField)JarMarker.getInstance()
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   public Jar() {
/*  195 */     this.archiveType = "jar";
/*  196 */     this.emptyBehavior = "create";
/*  197 */     setEncoding("UTF8");
/*  198 */     setZip64Mode(Zip.Zip64ModeAttribute.NEVER);
/*  199 */     this.rootEntries = new Vector<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWhenempty(Zip.WhenEmpty we) {
/*  209 */     log("JARs are never empty, they contain at least a manifest file", 1);
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
/*      */   public void setWhenmanifestonly(Zip.WhenEmpty we) {
/*  224 */     this.emptyBehavior = we.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStrict(StrictMode strict) {
/*  234 */     this.strict = strict;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setJarfile(File jarFile) {
/*  245 */     setDestFile(jarFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIndex(boolean flag) {
/*  254 */     this.index = flag;
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
/*      */   public void setIndexMetaInf(boolean flag) {
/*  274 */     this.indexMetaInf = flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setManifestEncoding(String manifestEncoding) {
/*  283 */     this.manifestEncoding = manifestEncoding;
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
/*      */   public void addConfiguredManifest(Manifest newManifest) throws ManifestException {
/*  295 */     if (this.configuredManifest == null) {
/*  296 */       this.configuredManifest = newManifest;
/*      */     } else {
/*  298 */       this.configuredManifest.merge(newManifest, false, this.mergeClassPaths);
/*      */     } 
/*  300 */     this.savedConfiguredManifest = this.configuredManifest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setManifest(File manifestFile) {
/*  311 */     if (!manifestFile.exists()) {
/*  312 */       throw new BuildException("Manifest file: " + manifestFile + " does not exist.", 
/*  313 */           getLocation());
/*      */     }
/*      */     
/*  316 */     this.manifestFile = manifestFile;
/*      */   }
/*      */   
/*      */   private Manifest getManifest(File manifestFile) {
/*      */     
/*  321 */     try { InputStreamReader isr = new InputStreamReader(Files.newInputStream(manifestFile.toPath(), new java.nio.file.OpenOption[0]), getManifestCharset()); 
/*  322 */       try { Manifest manifest = getManifest(isr);
/*  323 */         isr.close(); return manifest; } catch (Throwable throwable) { try { isr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*  324 */     { throw new BuildException("Unable to read manifest file: " + manifestFile + " (" + e
/*  325 */           .getMessage() + ")", e); }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Manifest getManifestFromJar(File jarFile) throws IOException {
/*  335 */     ZipFile zf = new ZipFile(jarFile);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  340 */     try { ZipEntry ze = StreamUtils.enumerationAsStream(zf.entries()).filter(entry -> "META-INF/MANIFEST.MF".equalsIgnoreCase(entry.getName())).findFirst().orElse(null);
/*  341 */       if (ze == null)
/*  342 */       { Manifest manifest = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  348 */         zf.close(); return manifest; }  InputStreamReader isr = new InputStreamReader(zf.getInputStream(ze), StandardCharsets.UTF_8); try { Manifest manifest = getManifest(isr); isr.close(); zf.close(); return manifest; } catch (Throwable throwable) { try { isr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*      */        }
/*      */     catch (Throwable throwable) { try { zf.close(); }
/*      */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/*  353 */      } private Manifest getManifest(Reader r) { try { return new Manifest(r); }
/*  354 */     catch (ManifestException e)
/*  355 */     { log("Manifest is invalid: " + e.getMessage(), 0);
/*  356 */       throw new BuildException("Invalid Manifest: " + this.manifestFile, e, 
/*  357 */           getLocation()); }
/*  358 */     catch (IOException e)
/*  359 */     { throw new BuildException("Unable to read manifest file (" + e
/*  360 */           .getMessage() + ")", e); }
/*      */      }
/*      */ 
/*      */   
/*      */   private boolean jarHasIndex(File jarFile) throws IOException {
/*  365 */     ZipFile zf = new ZipFile(jarFile);
/*      */     try {
/*  367 */       boolean bool = StreamUtils.enumerationAsStream(zf.entries()).anyMatch(ze -> "META-INF/INDEX.LIST".equalsIgnoreCase(ze.getName()));
/*  368 */       zf.close();
/*      */       return bool;
/*      */     } catch (Throwable throwable) {
/*      */       try {
/*      */         zf.close();
/*      */       } catch (Throwable throwable1) {
/*      */         throwable.addSuppressed(throwable1);
/*      */       } 
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilesetmanifest(FilesetManifestConfig config) {
/*  385 */     this.filesetManifestConfig = config;
/*  386 */     this.mergeManifestsMain = (config != null && "merge".equals(config.getValue()));
/*      */     
/*  388 */     if (this.filesetManifestConfig != null && 
/*  389 */       !"skip".equals(this.filesetManifestConfig.getValue()))
/*      */     {
/*  391 */       this.doubleFilePass = true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMetainf(ZipFileSet fs) {
/*  402 */     fs.setPrefix("META-INF/");
/*  403 */     addFileset((FileSet)fs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredIndexJars(Path p) {
/*  412 */     if (this.indexJars == null) {
/*  413 */       this.indexJars = new Path(getProject());
/*      */     }
/*  415 */     this.indexJars.append(p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredIndexJarsMapper(Mapper mapper) {
/*  425 */     if (this.indexJarsMapper != null) {
/*  426 */       throw new BuildException("Cannot define more than one indexjar-mapper", 
/*  427 */           getLocation());
/*      */     }
/*  429 */     this.indexJarsMapper = mapper.getImplementation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileNameMapper getIndexJarsMapper() {
/*  438 */     return this.indexJarsMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredService(Service service) {
/*  449 */     service.check();
/*  450 */     this.serviceList.add(service);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeServices(ZipOutputStream zOut) throws IOException {
/*  457 */     for (Service service : this.serviceList) {
/*  458 */       InputStream is = service.getAsStream();
/*      */       try {
/*  460 */         super.zipFile(is, zOut, "META-INF/services/" + service
/*  461 */             .getType(), 
/*  462 */             System.currentTimeMillis(), (File)null, 33188);
/*      */         
/*  464 */         if (is != null) is.close(); 
/*      */       } catch (Throwable throwable) {
/*      */         if (is != null)
/*      */           try {
/*      */             is.close();
/*      */           } catch (Throwable throwable1) {
/*      */             throwable.addSuppressed(throwable1);
/*      */           }  
/*      */         throw throwable;
/*      */       } 
/*      */     }  } public void setMergeClassPathAttributes(boolean b) {
/*  475 */     this.mergeClassPaths = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFlattenAttributes(boolean b) {
/*  486 */     this.flattenClassPaths = b;
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
/*      */   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
/*  499 */     if (!this.skipWriting) {
/*  500 */       Manifest jarManifest = createManifest();
/*  501 */       writeManifest(zOut, jarManifest);
/*  502 */       writeServices(zOut);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Manifest createManifest() throws BuildException {
/*      */     try {
/*      */       Manifest finalManifest;
/*  509 */       if (this.manifest == null && this.manifestFile != null)
/*      */       {
/*      */         
/*  512 */         this.manifest = getManifest(this.manifestFile);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  520 */       boolean mergeFileSetFirst = (!this.mergeManifestsMain && this.filesetManifest != null && this.configuredManifest == null && this.manifest == null);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  525 */       if (mergeFileSetFirst) {
/*  526 */         finalManifest = new Manifest();
/*  527 */         finalManifest.merge(this.filesetManifest, false, this.mergeClassPaths);
/*  528 */         finalManifest.merge(Manifest.getDefaultManifest(), true, this.mergeClassPaths);
/*      */       } else {
/*      */         
/*  531 */         finalManifest = Manifest.getDefaultManifest();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  542 */       if (isInUpdateMode()) {
/*  543 */         finalManifest.merge(this.originalManifest, false, this.mergeClassPaths);
/*      */       }
/*  545 */       if (!mergeFileSetFirst) {
/*  546 */         finalManifest.merge(this.filesetManifest, false, this.mergeClassPaths);
/*      */       }
/*  548 */       finalManifest.merge(this.configuredManifest, !this.mergeManifestsMain, this.mergeClassPaths);
/*      */       
/*  550 */       finalManifest.merge(this.manifest, !this.mergeManifestsMain, this.mergeClassPaths);
/*      */ 
/*      */       
/*  553 */       return finalManifest;
/*      */     }
/*  555 */     catch (ManifestException e) {
/*  556 */       log("Manifest is invalid: " + e.getMessage(), 0);
/*  557 */       throw new BuildException("Invalid Manifest", e, getLocation());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeManifest(ZipOutputStream zOut, Manifest manifest) throws IOException {
/*  563 */     StreamUtils.enumerationAsStream(manifest.getWarnings())
/*  564 */       .forEach(warning -> log("Manifest warning: " + warning, 1));
/*      */     
/*  566 */     zipDir((Resource)null, zOut, "META-INF/", 16877, JAR_MARKER);
/*      */ 
/*      */     
/*  569 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  570 */     OutputStreamWriter osw = new OutputStreamWriter(baos, Manifest.JAR_CHARSET);
/*  571 */     PrintWriter writer = new PrintWriter(osw);
/*  572 */     manifest.write(writer, this.flattenClassPaths);
/*  573 */     if (writer.checkError()) {
/*  574 */       throw new IOException("Encountered an error writing the manifest");
/*      */     }
/*  576 */     writer.close();
/*      */ 
/*      */     
/*  579 */     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
/*      */     try {
/*  581 */       super.zipFile(bais, zOut, "META-INF/MANIFEST.MF", 
/*  582 */           System.currentTimeMillis(), (File)null, 33188);
/*      */     }
/*      */     finally {
/*      */       
/*  586 */       FileUtils.close(bais);
/*      */     } 
/*  588 */     super.initZipOutputStream(zOut);
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
/*      */   protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
/*  602 */     if (this.index) {
/*  603 */       createIndexList(zOut);
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
/*      */   private void createIndexList(ZipOutputStream zOut) throws IOException {
/*  618 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*      */     
/*  620 */     PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
/*      */ 
/*      */     
/*  623 */     writer.println("JarIndex-Version: 1.0");
/*  624 */     writer.println();
/*      */ 
/*      */     
/*  627 */     writer.println(this.zipFile.getName());
/*      */     
/*  629 */     writeIndexLikeList(new ArrayList<>(this.addedDirs.keySet()), this.rootEntries, writer);
/*      */     
/*  631 */     writer.println();
/*      */     
/*  633 */     if (this.indexJars != null) {
/*  634 */       FileNameMapper mapper = this.indexJarsMapper;
/*  635 */       if (mapper == null) {
/*  636 */         mapper = createDefaultIndexJarsMapper();
/*      */       }
/*  638 */       for (String indexJarEntry : this.indexJars.list()) {
/*  639 */         String[] names = mapper.mapFileName(indexJarEntry);
/*  640 */         if (names != null && names.length > 0) {
/*  641 */           ArrayList<String> dirs = new ArrayList<>();
/*  642 */           ArrayList<String> files = new ArrayList<>();
/*  643 */           grabFilesAndDirs(indexJarEntry, dirs, files);
/*  644 */           if (dirs.size() + files.size() > 0) {
/*  645 */             writer.println(names[0]);
/*  646 */             writeIndexLikeList(dirs, files, writer);
/*  647 */             writer.println();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  653 */     if (writer.checkError()) {
/*  654 */       throw new IOException("Encountered an error writing jar index");
/*      */     }
/*  656 */     writer.close();
/*      */     
/*  658 */     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()); try {
/*  659 */       super.zipFile(bais, zOut, "META-INF/INDEX.LIST", System.currentTimeMillis(), (File)null, 33188);
/*      */       
/*  661 */       bais.close();
/*      */     } catch (Throwable throwable) {
/*      */       try {
/*      */         bais.close();
/*      */       } catch (Throwable throwable1) {
/*      */         throwable.addSuppressed(throwable1);
/*      */       } 
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   private FileNameMapper createDefaultIndexJarsMapper() {
/*  672 */     Manifest mf = createManifest();
/*      */     
/*  674 */     Manifest.Attribute classpath = mf.getMainSection().getAttribute("Class-Path");
/*  675 */     String[] cpEntries = null;
/*  676 */     if (classpath != null && classpath.getValue() != null) {
/*  677 */       StringTokenizer tok = new StringTokenizer(classpath.getValue(), " ");
/*      */       
/*  679 */       cpEntries = new String[tok.countTokens()];
/*  680 */       int c = 0;
/*  681 */       while (tok.hasMoreTokens()) {
/*  682 */         cpEntries[c++] = tok.nextToken();
/*      */       }
/*      */     } 
/*      */     
/*  686 */     return new IndexJarsFilenameMapper(cpEntries);
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
/*      */   protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
/*  705 */     if ("META-INF/MANIFEST.MF".equalsIgnoreCase(vPath)) {
/*  706 */       if (isFirstPass()) {
/*  707 */         filesetManifest(fromArchive, is);
/*      */       }
/*  709 */     } else if ("META-INF/INDEX.LIST".equalsIgnoreCase(vPath) && this.index) {
/*  710 */       logWhenWriting("Warning: selected " + this.archiveType + " files include a " + "META-INF/INDEX.LIST" + " which will be replaced by a newly generated one.", 1);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  715 */       if (this.index && !vPath.contains("/")) {
/*  716 */         this.rootEntries.add(vPath);
/*      */       }
/*  718 */       super.zipFile(is, zOut, vPath, lastModified, fromArchive, mode);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void filesetManifest(File file, InputStream is) throws IOException {
/*  723 */     if (this.manifestFile != null && this.manifestFile.equals(file)) {
/*      */ 
/*      */       
/*  726 */       log("Found manifest " + file, 3);
/*  727 */       if (is == null) {
/*  728 */         this.manifest = getManifest(file);
/*      */       } else {
/*      */         
/*  731 */         InputStreamReader isr = new InputStreamReader(is, getManifestCharset()); 
/*  732 */         try { this.manifest = getManifest(isr);
/*  733 */           isr.close(); } catch (Throwable throwable) { try { isr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/*      */       } 
/*  735 */     } else if (this.filesetManifestConfig != null && 
/*  736 */       !"skip".equals(this.filesetManifestConfig.getValue())) {
/*      */       
/*  738 */       logWhenWriting("Found manifest to merge in file " + file, 3);
/*      */       
/*      */       try {
/*      */         Manifest newManifest;
/*      */         
/*  743 */         if (is == null) {
/*  744 */           newManifest = getManifest(file);
/*      */         } else {
/*      */           
/*  747 */           InputStreamReader isr = new InputStreamReader(is, getManifestCharset()); 
/*  748 */           try { newManifest = getManifest(isr);
/*  749 */             isr.close(); } catch (Throwable throwable) { try { isr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */              throw throwable; }
/*      */         
/*  752 */         }  if (this.filesetManifest == null) {
/*  753 */           this.filesetManifest = newManifest;
/*      */         } else {
/*  755 */           this.filesetManifest.merge(newManifest, false, this.mergeClassPaths);
/*      */         } 
/*  757 */       } catch (UnsupportedEncodingException e) {
/*  758 */         Manifest newManifest; throw new BuildException("Unsupported encoding while reading manifest: " + newManifest
/*  759 */             .getMessage(), newManifest);
/*  760 */       } catch (ManifestException e) {
/*  761 */         log("Manifest in file " + file + " is invalid: " + e
/*  762 */             .getMessage(), 0);
/*  763 */         throw new BuildException("Invalid Manifest", e, getLocation());
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
/*      */   protected Zip.ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
/*  811 */     if (this.skipWriting) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  816 */       Resource[][] manifests = grabManifests(rcs);
/*  817 */       int count = 0;
/*  818 */       for (Resource[] mf : manifests) {
/*  819 */         count += mf.length;
/*      */       }
/*  821 */       log("found a total of " + count + " manifests in " + manifests.length + " resource collections", 3);
/*      */ 
/*      */       
/*  824 */       return new Zip.ArchiveState(true, manifests);
/*      */     } 
/*      */ 
/*      */     
/*  828 */     if (zipFile.exists()) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  833 */         this.originalManifest = getManifestFromJar(zipFile);
/*  834 */         if (this.originalManifest == null) {
/*  835 */           log("Updating jar since the current jar has no manifest", 3);
/*      */           
/*  837 */           needsUpdate = true;
/*      */         } else {
/*  839 */           Manifest mf = createManifest();
/*  840 */           if (!mf.equals(this.originalManifest)) {
/*  841 */             log("Updating jar since jar manifest has changed", 3);
/*      */             
/*  843 */             needsUpdate = true;
/*      */           } 
/*      */         } 
/*  846 */       } catch (Throwable t) {
/*  847 */         log("error while reading original manifest in file: " + zipFile
/*  848 */             .toString() + " due to " + t.getMessage(), 1);
/*      */         
/*  850 */         needsUpdate = true;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  855 */       needsUpdate = true;
/*      */     } 
/*      */     
/*  858 */     this.createEmpty = needsUpdate;
/*  859 */     if (!needsUpdate && this.index) {
/*      */       try {
/*  861 */         needsUpdate = !jarHasIndex(zipFile);
/*  862 */       } catch (IOException e) {
/*      */         
/*  864 */         needsUpdate = true;
/*      */       } 
/*      */     }
/*  867 */     return super.getResourcesToAdd(rcs, zipFile, needsUpdate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createEmptyZip(File zipFile) throws BuildException {
/*  878 */     if (!this.createEmpty) {
/*  879 */       return true;
/*      */     }
/*      */     
/*  882 */     if ("skip".equals(this.emptyBehavior)) {
/*  883 */       if (!this.skipWriting) {
/*  884 */         log("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
/*      */       }
/*      */ 
/*      */       
/*  888 */       return true;
/*      */     } 
/*  890 */     if ("fail".equals(this.emptyBehavior)) {
/*  891 */       throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", 
/*      */ 
/*      */           
/*  894 */           getLocation());
/*      */     }
/*      */     
/*  897 */     if (!this.skipWriting) {
/*  898 */       log("Building MANIFEST-only jar: " + 
/*  899 */           getDestFile().getAbsolutePath());
/*      */       
/*  901 */       try { ZipOutputStream zOut = new ZipOutputStream(getDestFile()); 
/*  902 */         try { zOut.setEncoding(getEncoding());
/*  903 */           zOut.setUseZip64(getZip64Mode().getMode());
/*  904 */           if (isCompress()) {
/*  905 */             zOut.setMethod(8);
/*      */           } else {
/*  907 */             zOut.setMethod(0);
/*      */           } 
/*  909 */           initZipOutputStream(zOut);
/*  910 */           finalizeZipOutputStream(zOut);
/*  911 */           zOut.close(); } catch (Throwable throwable) { try { zOut.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/*  912 */       { throw new BuildException("Could not create almost empty JAR archive (" + ioe
/*  913 */             .getMessage() + ")", ioe, 
/*  914 */             getLocation()); }
/*      */       finally
/*  916 */       { this.createEmpty = false; }
/*      */     
/*      */     } 
/*  919 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cleanUp() {
/*  930 */     super.cleanUp();
/*  931 */     checkJarSpec();
/*      */ 
/*      */     
/*  934 */     if (!this.doubleFilePass || !this.skipWriting) {
/*  935 */       this.manifest = null;
/*  936 */       this.configuredManifest = this.savedConfiguredManifest;
/*  937 */       this.filesetManifest = null;
/*  938 */       this.originalManifest = null;
/*      */     } 
/*  940 */     this.rootEntries.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkJarSpec() {
/*  950 */     StringBuilder message = new StringBuilder();
/*      */ 
/*      */     
/*  953 */     Manifest.Section mainSection = (this.configuredManifest == null) ? null : this.configuredManifest.getMainSection();
/*      */     
/*  955 */     if (mainSection == null) {
/*  956 */       message.append("No Implementation-Title set.");
/*  957 */       message.append("No Implementation-Version set.");
/*  958 */       message.append("No Implementation-Vendor set.");
/*      */     } else {
/*  960 */       if (mainSection.getAttribute("Implementation-Title") == null) {
/*  961 */         message.append("No Implementation-Title set.");
/*      */       }
/*  963 */       if (mainSection.getAttribute("Implementation-Version") == null) {
/*  964 */         message.append("No Implementation-Version set.");
/*      */       }
/*  966 */       if (mainSection.getAttribute("Implementation-Vendor") == null) {
/*  967 */         message.append("No Implementation-Vendor set.");
/*      */       }
/*      */     } 
/*      */     
/*  971 */     if (message.length() > 0) {
/*  972 */       message.append(String.format("%nLocation: %s%n", new Object[] { getLocation() }));
/*  973 */       if ("fail".equalsIgnoreCase(this.strict.getValue())) {
/*  974 */         throw new BuildException(message.toString(), getLocation());
/*      */       }
/*  976 */       logWhenWriting(message.toString(), this.strict.getLogLevel());
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
/*      */   public void reset() {
/*  989 */     super.reset();
/*  990 */     this.emptyBehavior = "create";
/*  991 */     this.configuredManifest = null;
/*  992 */     this.filesetManifestConfig = null;
/*  993 */     this.mergeManifestsMain = false;
/*  994 */     this.manifestFile = null;
/*  995 */     this.index = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class FilesetManifestConfig
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/* 1008 */       return new String[] { "skip", "merge", "mergewithoutmain" };
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
/*      */   protected final void writeIndexLikeList(List<String> dirs, List<String> files, PrintWriter writer) {
/* 1026 */     Collections.sort(dirs);
/* 1027 */     Collections.sort(files);
/* 1028 */     for (String dir : dirs) {
/*      */       
/* 1030 */       dir = dir.replace('\\', '/');
/* 1031 */       if (dir.startsWith("./")) {
/* 1032 */         dir = dir.substring(2);
/*      */       }
/* 1034 */       while (dir.startsWith("/")) {
/* 1035 */         dir = dir.substring(1);
/*      */       }
/* 1037 */       int pos = dir.lastIndexOf('/');
/* 1038 */       if (pos != -1) {
/* 1039 */         dir = dir.substring(0, pos);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1047 */       if (!this.indexMetaInf && dir.startsWith("META-INF")) {
/*      */         continue;
/*      */       }
/*      */       
/* 1051 */       writer.println(dir);
/*      */     } 
/*      */     
/* 1054 */     Objects.requireNonNull(writer); files.forEach(writer::println);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String findJarName(String fileName, String[] classpath) {
/* 1079 */     if (classpath == null) {
/* 1080 */       return (new File(fileName)).getName();
/*      */     }
/* 1082 */     fileName = fileName.replace(File.separatorChar, '/');
/*      */     
/* 1084 */     SortedMap<String, String> matches = new TreeMap<>(Comparator.<String>comparingInt(s -> (s == null) ? 0 : s.length()).reversed());
/*      */     
/* 1086 */     for (String element : classpath) {
/* 1087 */       String candidate = element;
/*      */       while (true) {
/* 1089 */         if (fileName.endsWith(candidate)) {
/* 1090 */           matches.put(candidate, element);
/*      */           break;
/*      */         } 
/* 1093 */         int slash = candidate.indexOf('/');
/* 1094 */         if (slash < 0) {
/*      */           break;
/*      */         }
/* 1097 */         candidate = candidate.substring(slash + 1);
/*      */       } 
/*      */     } 
/* 1100 */     return matches.isEmpty() ? null : matches.get(matches.firstKey());
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
/*      */   protected static void grabFilesAndDirs(String file, List<String> dirs, List<String> files) throws IOException {
/* 1115 */     ZipFile zf = new ZipFile(file, "utf-8"); 
/* 1116 */     try { Set<String> dirSet = new HashSet<>();
/* 1117 */       StreamUtils.enumerationAsStream(zf.getEntries()).forEach(ze -> {
/*      */             String name = ze.getName();
/*      */ 
/*      */             
/*      */             if (ze.isDirectory()) {
/*      */               dirSet.add(name);
/*      */             } else if (!name.contains("/")) {
/*      */               files.add(name);
/*      */             } else {
/*      */               dirSet.add(name.substring(0, name.lastIndexOf('/') + 1));
/*      */             } 
/*      */           });
/*      */ 
/*      */       
/* 1131 */       dirs.addAll(dirSet);
/* 1132 */       zf.close(); }
/*      */     catch (Throwable throwable) { try { zf.close(); }
/*      */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/* 1136 */      } private Resource[][] grabManifests(ResourceCollection[] rcs) { Resource[][] manifests = new Resource[rcs.length][];
/* 1137 */     for (int i = 0; i < rcs.length; i++) {
/*      */       Resource[][] resources;
/* 1139 */       if (rcs[i] instanceof FileSet) {
/* 1140 */         resources = grabResources(new FileSet[] { (FileSet)rcs[i] });
/*      */       } else {
/* 1142 */         resources = grabNonFileSetResources(new ResourceCollection[] { rcs[i] });
/*      */       } 
/*      */       
/* 1145 */       for (int j = 0; j < (resources[0]).length; j++) {
/* 1146 */         String name = resources[0][j].getName().replace('\\', '/');
/* 1147 */         if (rcs[i] instanceof ArchiveFileSet) {
/* 1148 */           ArchiveFileSet afs = (ArchiveFileSet)rcs[i];
/* 1149 */           if (!afs.getFullpath(getProject()).isEmpty()) {
/* 1150 */             name = afs.getFullpath(getProject());
/* 1151 */           } else if (!afs.getPrefix(getProject()).isEmpty()) {
/* 1152 */             String prefix = afs.getPrefix(getProject());
/* 1153 */             if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
/* 1154 */               prefix = prefix + "/";
/*      */             }
/* 1156 */             name = prefix + name;
/*      */           } 
/*      */         } 
/* 1159 */         if ("META-INF/MANIFEST.MF".equalsIgnoreCase(name)) {
/* 1160 */           (new Resource[1])[0] = resources[0][j]; manifests[i] = new Resource[1];
/*      */           break;
/*      */         } 
/*      */       } 
/* 1164 */       if (manifests[i] == null) {
/* 1165 */         manifests[i] = new Resource[0];
/*      */       }
/*      */     } 
/* 1168 */     return manifests; }
/*      */ 
/*      */   
/*      */   private Charset getManifestCharset() {
/* 1172 */     if (this.manifestEncoding == null) {
/* 1173 */       return Charset.defaultCharset();
/*      */     }
/*      */     try {
/* 1176 */       return Charset.forName(this.manifestEncoding);
/* 1177 */     } catch (IllegalArgumentException e) {
/* 1178 */       throw new BuildException("Unsupported encoding while reading manifest: " + e
/*      */           
/* 1180 */           .getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class StrictMode
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public StrictMode() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public StrictMode(String value) {
/* 1196 */       setValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/* 1205 */       return new String[] { "fail", "warn", "ignore" };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getLogLevel() {
/* 1212 */       return "ignore".equals(getValue()) ? 3 : 1;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class IndexJarsFilenameMapper
/*      */     implements FileNameMapper
/*      */   {
/*      */     private String[] classpath;
/*      */ 
/*      */ 
/*      */     
/*      */     IndexJarsFilenameMapper(String[] classpath) {
/* 1227 */       this.classpath = classpath;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFrom(String from) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTo(String to) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] mapFileName(String sourceFileName) {
/* 1246 */       String result = Jar.findJarName(sourceFileName, this.classpath);
/* 1247 */       (new String[1])[0] = result; return (result == null) ? null : new String[1];
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Jar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */