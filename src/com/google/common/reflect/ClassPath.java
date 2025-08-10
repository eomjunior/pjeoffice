/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.StandardSystemProperty;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.io.ByteSource;
/*     */ import com.google.common.io.CharSource;
/*     */ import com.google.common.io.Resources;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public final class ClassPath
/*     */ {
/*  95 */   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
/*     */ 
/*     */ 
/*     */   
/*  99 */   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
/*     */   
/*     */   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
/*     */   
/*     */   private final ImmutableSet<ResourceInfo> resources;
/*     */   
/*     */   private ClassPath(ImmutableSet<ResourceInfo> resources) {
/* 106 */     this.resources = resources;
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
/*     */   public static ClassPath from(ClassLoader classloader) throws IOException {
/* 126 */     ImmutableSet<LocationInfo> locations = locationsFrom(classloader);
/*     */ 
/*     */ 
/*     */     
/* 130 */     Set<File> scanned = new HashSet<>();
/* 131 */     for (UnmodifiableIterator<LocationInfo> unmodifiableIterator1 = locations.iterator(); unmodifiableIterator1.hasNext(); ) { LocationInfo location = unmodifiableIterator1.next();
/* 132 */       scanned.add(location.file()); }
/*     */ 
/*     */ 
/*     */     
/* 136 */     ImmutableSet.Builder<ResourceInfo> builder = ImmutableSet.builder();
/* 137 */     for (UnmodifiableIterator<LocationInfo> unmodifiableIterator2 = locations.iterator(); unmodifiableIterator2.hasNext(); ) { LocationInfo location = unmodifiableIterator2.next();
/* 138 */       builder.addAll((Iterable)location.scanResources(scanned)); }
/*     */     
/* 140 */     return new ClassPath(builder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ResourceInfo> getResources() {
/* 148 */     return this.resources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getAllClasses() {
/* 157 */     return FluentIterable.from((Iterable)this.resources).filter(ClassInfo.class).toSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses() {
/* 165 */     return FluentIterable.from((Iterable)this.resources)
/* 166 */       .filter(ClassInfo.class)
/* 167 */       .filter(ClassInfo::isTopLevel)
/* 168 */       .toSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
/* 173 */     Preconditions.checkNotNull(packageName);
/* 174 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 175 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 176 */       if (classInfo.getPackageName().equals(packageName)) {
/* 177 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 180 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
/* 188 */     Preconditions.checkNotNull(packageName);
/* 189 */     String packagePrefix = packageName + '.';
/* 190 */     ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
/* 191 */     for (UnmodifiableIterator<ClassInfo> unmodifiableIterator = getTopLevelClasses().iterator(); unmodifiableIterator.hasNext(); ) { ClassInfo classInfo = unmodifiableIterator.next();
/* 192 */       if (classInfo.getName().startsWith(packagePrefix)) {
/* 193 */         builder.add(classInfo);
/*     */       } }
/*     */     
/* 196 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ResourceInfo
/*     */   {
/*     */     private final File file;
/*     */ 
/*     */     
/*     */     private final String resourceName;
/*     */     
/*     */     final ClassLoader loader;
/*     */ 
/*     */     
/*     */     static ResourceInfo of(File file, String resourceName, ClassLoader loader) {
/* 212 */       if (resourceName.endsWith(".class")) {
/* 213 */         return new ClassPath.ClassInfo(file, resourceName, loader);
/*     */       }
/* 215 */       return new ResourceInfo(file, resourceName, loader);
/*     */     }
/*     */ 
/*     */     
/*     */     ResourceInfo(File file, String resourceName, ClassLoader loader) {
/* 220 */       this.file = (File)Preconditions.checkNotNull(file);
/* 221 */       this.resourceName = (String)Preconditions.checkNotNull(resourceName);
/* 222 */       this.loader = (ClassLoader)Preconditions.checkNotNull(loader);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final URL url() {
/* 234 */       URL url = this.loader.getResource(this.resourceName);
/* 235 */       if (url == null) {
/* 236 */         throw new NoSuchElementException(this.resourceName);
/*     */       }
/* 238 */       return url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ByteSource asByteSource() {
/* 249 */       return Resources.asByteSource(url());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final CharSource asCharSource(Charset charset) {
/* 261 */       return Resources.asCharSource(url(), charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public final String getResourceName() {
/* 266 */       return this.resourceName;
/*     */     }
/*     */ 
/*     */     
/*     */     final File getFile() {
/* 271 */       return this.file;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 276 */       return this.resourceName.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 281 */       if (obj instanceof ResourceInfo) {
/* 282 */         ResourceInfo that = (ResourceInfo)obj;
/* 283 */         return (this.resourceName.equals(that.resourceName) && this.loader == that.loader);
/*     */       } 
/* 285 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 291 */       return this.resourceName;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ClassInfo
/*     */     extends ResourceInfo
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     ClassInfo(File file, String resourceName, ClassLoader loader) {
/* 304 */       super(file, resourceName, loader);
/* 305 */       this.className = ClassPath.getClassName(resourceName);
/*     */     }
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
/*     */     public String getPackageName() {
/* 320 */       return Reflection.getPackageName(this.className);
/*     */     }
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
/*     */     public String getSimpleName() {
/* 333 */       int lastDollarSign = this.className.lastIndexOf('$');
/* 334 */       if (lastDollarSign != -1) {
/* 335 */         String innerClassName = this.className.substring(lastDollarSign + 1);
/*     */ 
/*     */         
/* 338 */         return CharMatcher.inRange('0', '9').trimLeadingFrom(innerClassName);
/*     */       } 
/* 340 */       String packageName = getPackageName();
/* 341 */       if (packageName.isEmpty()) {
/* 342 */         return this.className;
/*     */       }
/*     */ 
/*     */       
/* 346 */       return this.className.substring(packageName.length() + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 356 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isTopLevel() {
/* 368 */       return (this.className.indexOf('$') == -1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> load() {
/*     */       try {
/* 379 */         return this.loader.loadClass(this.className);
/* 380 */       } catch (ClassNotFoundException e) {
/*     */         
/* 382 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 388 */       return this.className;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ImmutableSet<LocationInfo> locationsFrom(ClassLoader classloader) {
/* 398 */     ImmutableSet.Builder<LocationInfo> builder = ImmutableSet.builder();
/* 399 */     for (UnmodifiableIterator<Map.Entry<File, ClassLoader>> unmodifiableIterator = getClassPathEntries(classloader).entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<File, ClassLoader> entry = unmodifiableIterator.next();
/* 400 */       builder.add(new LocationInfo(entry.getKey(), entry.getValue())); }
/*     */     
/* 402 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LocationInfo
/*     */   {
/*     */     final File home;
/*     */     
/*     */     private final ClassLoader classloader;
/*     */ 
/*     */     
/*     */     LocationInfo(File home, ClassLoader classloader) {
/* 414 */       this.home = (File)Preconditions.checkNotNull(home);
/* 415 */       this.classloader = (ClassLoader)Preconditions.checkNotNull(classloader);
/*     */     }
/*     */ 
/*     */     
/*     */     public final File file() {
/* 420 */       return this.home;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<ClassPath.ResourceInfo> scanResources() throws IOException {
/* 425 */       return scanResources(new HashSet<>());
/*     */     }
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
/*     */     public ImmutableSet<ClassPath.ResourceInfo> scanResources(Set<File> scannedFiles) throws IOException {
/* 443 */       ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
/* 444 */       scannedFiles.add(this.home);
/* 445 */       scan(this.home, scannedFiles, builder);
/* 446 */       return builder.build();
/*     */     }
/*     */ 
/*     */     
/*     */     private void scan(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/*     */       try {
/* 452 */         if (!file.exists()) {
/*     */           return;
/*     */         }
/* 455 */       } catch (SecurityException e) {
/* 456 */         ClassPath.logger.warning("Cannot access " + file + ": " + e);
/*     */         
/*     */         return;
/*     */       } 
/* 460 */       if (file.isDirectory()) {
/* 461 */         scanDirectory(file, builder);
/*     */       } else {
/* 463 */         scanJar(file, scannedUris, builder);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void scanJar(File file, Set<File> scannedUris, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/*     */       JarFile jarFile;
/*     */       try {
/* 472 */         jarFile = new JarFile(file);
/* 473 */       } catch (IOException e) {
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 478 */         for (UnmodifiableIterator<File> unmodifiableIterator = ClassPath.getClassPathFromManifest(file, jarFile.getManifest()).iterator(); unmodifiableIterator.hasNext(); ) { File path = unmodifiableIterator.next();
/*     */ 
/*     */           
/* 481 */           if (scannedUris.add(path.getCanonicalFile())) {
/* 482 */             scan(path, scannedUris, builder);
/*     */           } }
/*     */         
/* 485 */         scanJarFile(jarFile, builder);
/*     */       } finally {
/*     */         try {
/* 488 */           jarFile.close();
/* 489 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void scanJarFile(JarFile file, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) {
/* 495 */       Enumeration<JarEntry> entries = file.entries();
/* 496 */       while (entries.hasMoreElements()) {
/* 497 */         JarEntry entry = entries.nextElement();
/* 498 */         if (entry.isDirectory() || entry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */           continue;
/*     */         }
/* 501 */         builder.add(ClassPath.ResourceInfo.of(new File(file.getName()), entry.getName(), this.classloader));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void scanDirectory(File directory, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/* 507 */       Set<File> currentPath = new HashSet<>();
/* 508 */       currentPath.add(directory.getCanonicalFile());
/* 509 */       scanDirectory(directory, "", currentPath, builder);
/*     */     }
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
/*     */     private void scanDirectory(File directory, String packagePrefix, Set<File> currentPath, ImmutableSet.Builder<ClassPath.ResourceInfo> builder) throws IOException {
/* 529 */       File[] files = directory.listFiles();
/* 530 */       if (files == null) {
/* 531 */         ClassPath.logger.warning("Cannot read directory " + directory);
/*     */         
/*     */         return;
/*     */       } 
/* 535 */       for (File f : files) {
/* 536 */         String name = f.getName();
/* 537 */         if (f.isDirectory()) {
/* 538 */           File deref = f.getCanonicalFile();
/* 539 */           if (currentPath.add(deref)) {
/* 540 */             scanDirectory(deref, packagePrefix + name + "/", currentPath, builder);
/* 541 */             currentPath.remove(deref);
/*     */           } 
/*     */         } else {
/* 544 */           String resourceName = packagePrefix + name;
/* 545 */           if (!resourceName.equals("META-INF/MANIFEST.MF")) {
/* 546 */             builder.add(ClassPath.ResourceInfo.of(f, resourceName, this.classloader));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 554 */       if (obj instanceof LocationInfo) {
/* 555 */         LocationInfo that = (LocationInfo)obj;
/* 556 */         return (this.home.equals(that.home) && this.classloader.equals(that.classloader));
/*     */       } 
/* 558 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 563 */       return this.home.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 568 */       return this.home.toString();
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
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<File> getClassPathFromManifest(File jarFile, @CheckForNull Manifest manifest) {
/* 582 */     if (manifest == null) {
/* 583 */       return ImmutableSet.of();
/*     */     }
/* 585 */     ImmutableSet.Builder<File> builder = ImmutableSet.builder();
/*     */     
/* 587 */     String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
/* 588 */     if (classpathAttribute != null) {
/* 589 */       for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
/*     */         URL url;
/*     */         try {
/* 592 */           url = getClassPathEntry(jarFile, path);
/* 593 */         } catch (MalformedURLException e) {
/*     */           
/* 595 */           logger.warning("Invalid Class-Path entry: " + path);
/*     */           continue;
/*     */         } 
/* 598 */         if (url.getProtocol().equals("file")) {
/* 599 */           builder.add(toFile(url));
/*     */         }
/*     */       } 
/*     */     }
/* 603 */     return builder.build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
/* 608 */     LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
/*     */     
/* 610 */     ClassLoader parent = classloader.getParent();
/* 611 */     if (parent != null) {
/* 612 */       entries.putAll((Map<? extends File, ? extends ClassLoader>)getClassPathEntries(parent));
/*     */     }
/* 614 */     for (UnmodifiableIterator<URL> unmodifiableIterator = getClassLoaderUrls(classloader).iterator(); unmodifiableIterator.hasNext(); ) { URL url = unmodifiableIterator.next();
/* 615 */       if (url.getProtocol().equals("file")) {
/* 616 */         File file = toFile(url);
/* 617 */         if (!entries.containsKey(file)) {
/* 618 */           entries.put(file, classloader);
/*     */         }
/*     */       }  }
/*     */     
/* 622 */     return ImmutableMap.copyOf(entries);
/*     */   }
/*     */   
/*     */   private static ImmutableList<URL> getClassLoaderUrls(ClassLoader classloader) {
/* 626 */     if (classloader instanceof URLClassLoader) {
/* 627 */       return ImmutableList.copyOf((Object[])((URLClassLoader)classloader).getURLs());
/*     */     }
/* 629 */     if (classloader.equals(ClassLoader.getSystemClassLoader())) {
/* 630 */       return parseJavaClassPath();
/*     */     }
/* 632 */     return ImmutableList.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableList<URL> parseJavaClassPath() {
/* 641 */     ImmutableList.Builder<URL> urls = ImmutableList.builder();
/* 642 */     for (String entry : Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())) {
/*     */       try {
/*     */         try {
/* 645 */           urls.add((new File(entry)).toURI().toURL());
/* 646 */         } catch (SecurityException e) {
/* 647 */           urls.add(new URL("file", null, (new File(entry)).getAbsolutePath()));
/*     */         } 
/* 649 */       } catch (MalformedURLException e) {
/* 650 */         logger.log(Level.WARNING, "malformed classpath entry: " + entry, e);
/*     */       } 
/*     */     } 
/* 653 */     return urls.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static URL getClassPathEntry(File jarFile, String path) throws MalformedURLException {
/* 664 */     return new URL(jarFile.toURI().toURL(), path);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static String getClassName(String filename) {
/* 669 */     int classNameEnd = filename.length() - ".class".length();
/* 670 */     return filename.substring(0, classNameEnd).replace('/', '.');
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static File toFile(URL url) {
/* 676 */     Preconditions.checkArgument(url.getProtocol().equals("file"));
/*     */     try {
/* 678 */       return new File(url.toURI());
/* 679 */     } catch (URISyntaxException e) {
/* 680 */       return new File(url.getPath());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/ClassPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */