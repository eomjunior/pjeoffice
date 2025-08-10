/*     */ package org.apache.tools.ant.taskdefs.optional.unix;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.FileScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.dispatch.DispatchTask;
/*     */ import org.apache.tools.ant.dispatch.DispatchUtils;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.types.FileSet;
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
/*     */ public class Symlink
/*     */   extends DispatchTask
/*     */ {
/*     */   private String resource;
/*     */   private String link;
/* 110 */   private List<FileSet> fileSets = new ArrayList<>();
/*     */   
/*     */   private String linkFileName;
/*     */   
/*     */   private boolean overwrite;
/*     */   
/*     */   private boolean failonerror;
/*     */   
/*     */   private boolean executing = false;
/*     */ 
/*     */   
/*     */   public void init() throws BuildException {
/* 122 */     super.init();
/* 123 */     setDefaults();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void execute() throws BuildException {
/* 132 */     if (this.executing) {
/* 133 */       throw new BuildException("Infinite recursion detected in Symlink.execute()");
/*     */     }
/*     */     
/*     */     try {
/* 137 */       this.executing = true;
/* 138 */       DispatchUtils.execute(this);
/*     */     } finally {
/* 140 */       this.executing = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void single() throws BuildException {
/*     */     try {
/* 151 */       if (this.resource == null) {
/* 152 */         handleError("Must define the resource to symlink to!");
/*     */         return;
/*     */       } 
/* 155 */       if (this.link == null) {
/* 156 */         handleError("Must define the link name for symlink!");
/*     */         return;
/*     */       } 
/* 159 */       doLink(this.resource, this.link);
/*     */     } finally {
/* 161 */       setDefaults();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete() throws BuildException {
/*     */     try {
/* 172 */       if (this.link == null) {
/* 173 */         handleError("Must define the link name for symlink!");
/*     */         return;
/*     */       } 
/* 176 */       Path linkPath = Paths.get(this.link, new String[0]);
/* 177 */       if (!Files.isSymbolicLink(linkPath)) {
/* 178 */         log("Skipping deletion of " + linkPath + " since it's not a symlink", 3);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 184 */       log("Removing symlink: " + this.link);
/* 185 */       deleteSymLink(linkPath);
/* 186 */     } catch (IOException ioe) {
/* 187 */       handleError(ioe.getMessage());
/*     */     } finally {
/* 189 */       setDefaults();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recreate() throws BuildException {
/*     */     try {
/* 200 */       if (this.fileSets.isEmpty()) {
/* 201 */         handleError("File set identifying link file(s) required for action recreate");
/*     */         
/*     */         return;
/*     */       } 
/* 205 */       Properties links = loadLinks(this.fileSets);
/* 206 */       for (String link : links.stringPropertyNames()) {
/* 207 */         String resource = links.getProperty(link);
/*     */         try {
/* 209 */           if (Files.isSymbolicLink(Paths.get(link, new String[0])) && (new File(link))
/* 210 */             .getCanonicalPath().equals((new File(resource)).getCanonicalPath())) {
/*     */ 
/*     */ 
/*     */             
/* 214 */             log("not recreating " + link + " as it points to the correct target already", 4);
/*     */             
/*     */             continue;
/*     */           } 
/* 218 */         } catch (IOException e) {
/* 219 */           String errMessage = "Failed to check if path " + link + " is a symbolic link, linking to " + resource;
/* 220 */           if (this.failonerror) {
/* 221 */             throw new BuildException(errMessage, e);
/*     */           }
/*     */           
/* 224 */           log(errMessage, 2);
/*     */           
/*     */           continue;
/*     */         } 
/* 228 */         doLink(resource, link);
/*     */       } 
/*     */     } finally {
/* 231 */       setDefaults();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void record() throws BuildException {
/*     */     try {
/* 242 */       if (this.fileSets.isEmpty()) {
/* 243 */         handleError("Fileset identifying links to record required");
/*     */         return;
/*     */       } 
/* 246 */       if (this.linkFileName == null) {
/* 247 */         handleError("Name of file to record links in required");
/*     */         
/*     */         return;
/*     */       } 
/* 251 */       Map<File, List<File>> byDir = new HashMap<>();
/*     */ 
/*     */       
/* 254 */       findLinks(this.fileSets).forEach(link -> ((List<File>)byDir.computeIfAbsent(link.getParentFile(), ())).add(link));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 259 */       byDir.forEach((dir, linksInDir) -> {
/*     */             Properties linksToStore = new Properties();
/*     */ 
/*     */             
/*     */             for (File link : linksInDir) {
/*     */               try {
/*     */                 linksToStore.put(link.getName(), link.getCanonicalPath());
/* 266 */               } catch (IOException ioe) {
/*     */                 handleError("Couldn't get canonical name of parent link");
/*     */               } 
/*     */             } 
/*     */             writePropertyFile(linksToStore, dir);
/*     */           });
/*     */     } finally {
/* 273 */       setDefaults();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDefaults() {
/* 282 */     this.resource = null;
/* 283 */     this.link = null;
/* 284 */     this.linkFileName = null;
/* 285 */     this.failonerror = true;
/* 286 */     this.overwrite = false;
/* 287 */     setAction("single");
/* 288 */     this.fileSets.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverwrite(boolean owrite) {
/* 299 */     this.overwrite = owrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean foe) {
/* 309 */     this.failonerror = foe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAction(String action) {
/* 320 */     super.setAction(action);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLink(String link) {
/* 329 */     this.link = link;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResource(String src) {
/* 339 */     this.resource = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkfilename(String lf) {
/* 349 */     this.linkFileName = lf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 358 */     this.fileSets.add(set);
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
/*     */   @Deprecated
/*     */   public static void deleteSymlink(String path) throws IOException {
/* 374 */     deleteSymlink(Paths.get(path, new String[0]).toFile());
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
/*     */   
/*     */   @Deprecated
/*     */   public static void deleteSymlink(File linkfil) throws IOException {
/* 397 */     if (!Files.isSymbolicLink(linkfil.toPath())) {
/*     */       return;
/*     */     }
/* 400 */     deleteSymLink(linkfil.toPath());
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
/*     */   private void writePropertyFile(Properties properties, File dir) throws BuildException {
/*     */     
/* 414 */     try { BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream((new File(dir, this.linkFileName)).toPath(), new java.nio.file.OpenOption[0])); 
/* 415 */       try { properties.store(bos, "Symlinks from " + dir);
/* 416 */         bos.close(); } catch (Throwable throwable) { try { bos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 417 */     { throw new BuildException(ioe, getLocation()); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleError(String msg) {
/* 429 */     if (this.failonerror) {
/* 430 */       throw new BuildException(msg);
/*     */     }
/* 432 */     log(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doLink(String resource, String link) throws BuildException {
/* 443 */     Path linkPath = Paths.get(link, new String[0]);
/* 444 */     Path target = Paths.get(resource, new String[0]);
/* 445 */     boolean alreadyExists = Files.exists(linkPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 446 */     if (!alreadyExists) {
/*     */ 
/*     */       
/*     */       try {
/* 450 */         log("creating symlink " + linkPath + " -> " + target, 4);
/* 451 */         Files.createSymbolicLink(linkPath, target, (FileAttribute<?>[])new FileAttribute[0]);
/* 452 */       } catch (IOException e) {
/* 453 */         if (this.failonerror) {
/* 454 */           throw new BuildException("Failed to create symlink " + link + " to target " + resource, e);
/*     */         }
/* 456 */         log("Unable to create symlink " + link + " to target " + resource, e, 2);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 461 */     if (!this.overwrite) {
/* 462 */       log("Skipping symlink creation, since file at " + link + " already exists and overwrite is set to false", 2);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 468 */     boolean existingFileDeleted = linkPath.toFile().delete();
/* 469 */     if (!existingFileDeleted) {
/* 470 */       handleError("Deletion of file at " + link + " failed, while trying to overwrite it with a symlink");
/*     */       return;
/*     */     } 
/*     */     try {
/* 474 */       log("creating symlink " + linkPath + " -> " + target + " after removing original", 4);
/*     */       
/* 476 */       Files.createSymbolicLink(linkPath, target, (FileAttribute<?>[])new FileAttribute[0]);
/* 477 */     } catch (IOException e) {
/* 478 */       if (this.failonerror) {
/* 479 */         throw new BuildException("Failed to create symlink " + link + " to target " + resource, e);
/*     */       }
/* 481 */       log("Unable to create symlink " + link + " to target " + resource, e, 2);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<File> findLinks(List<FileSet> fileSets) {
/* 497 */     Set<File> result = new HashSet<>();
/* 498 */     for (Iterator<FileSet> iterator = fileSets.iterator(); iterator.hasNext(); ) { FileSet fs = iterator.next();
/* 499 */       DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/*     */       
/* 501 */       File dir = fs.getDir(getProject());
/*     */       
/* 503 */       Stream.<String[]>of(new String[][] { ds.getIncludedFiles(), ds.getIncludedDirectories()
/* 504 */           }).flatMap(Stream::of).forEach(path -> {
/*     */             try {
/*     */               File f = new File(dir, path);
/*     */               
/*     */               File pf = f.getParentFile();
/*     */               
/*     */               String name = f.getName();
/*     */               File parentDirCanonicalizedFile = new File(pf.getCanonicalPath(), name);
/*     */               if (Files.isSymbolicLink(parentDirCanonicalizedFile.toPath())) {
/*     */                 result.add(parentDirCanonicalizedFile);
/*     */               }
/* 515 */             } catch (IOException e) {
/*     */               handleError("IOException: " + path + " omitted");
/*     */             } 
/*     */           }); }
/*     */     
/* 520 */     return result;
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
/*     */   private Properties loadLinks(List<FileSet> fileSets) {
/* 535 */     Properties finalList = new Properties();
/*     */     
/* 537 */     for (FileSet fs : fileSets) {
/* 538 */       DirectoryScanner ds = new DirectoryScanner();
/* 539 */       fs.setupDirectoryScanner((FileScanner)ds, getProject());
/* 540 */       ds.setFollowSymlinks(false);
/* 541 */       ds.scan();
/* 542 */       File dir = fs.getDir(getProject());
/*     */ 
/*     */       
/* 545 */       for (String name : ds.getIncludedFiles()) {
/* 546 */         File inc = new File(dir, name);
/* 547 */         File pf = inc.getParentFile();
/* 548 */         Properties links = new Properties();
/*     */         
/* 550 */         try { InputStream is = new BufferedInputStream(Files.newInputStream(inc.toPath(), new java.nio.file.OpenOption[0])); 
/* 551 */           try { links.load(is);
/* 552 */             pf = pf.getCanonicalFile();
/* 553 */             is.close(); } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (FileNotFoundException fnfe)
/* 554 */         { handleError("Unable to find " + name + "; skipping it."); }
/*     */         
/* 556 */         catch (IOException ioe)
/* 557 */         { handleError("Unable to open " + name + " or its parent dir; skipping it."); }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 562 */           links.store(new PrintStream((OutputStream)new LogOutputStream((Task)this, 2)), "listing properties");
/*     */         
/*     */         }
/* 565 */         catch (IOException ex) {
/* 566 */           log("failed to log unshortened properties");
/* 567 */           links.list(new PrintStream((OutputStream)new LogOutputStream((Task)this, 2)));
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 574 */         for (String key : links.stringPropertyNames()) {
/* 575 */           finalList.put((new File(pf, key)).getAbsolutePath(), links
/* 576 */               .getProperty(key));
/*     */         }
/*     */       } 
/*     */     } 
/* 580 */     return finalList;
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
/*     */   private static void deleteSymLink(Path path) throws IOException {
/* 594 */     boolean deleted = path.toFile().delete();
/* 595 */     if (!deleted)
/* 596 */       throw new IOException("Could not delete symlink at " + path); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/unix/Symlink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */