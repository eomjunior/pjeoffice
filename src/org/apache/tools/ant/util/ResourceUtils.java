/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*     */ import org.apache.tools.ant.types.FilterChain;
/*     */ import org.apache.tools.ant.types.FilterSetCollection;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.ResourceFactory;
/*     */ import org.apache.tools.ant.types.TimeComparison;
/*     */ import org.apache.tools.ant.types.resources.Appendable;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.types.resources.Restrict;
/*     */ import org.apache.tools.ant.types.resources.StringResource;
/*     */ import org.apache.tools.ant.types.resources.Touchable;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.types.resources.selectors.Date;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*     */ import org.apache.tools.ant.types.selectors.SelectorUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceUtils
/*     */ {
/*  67 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
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
/*     */   public static final String ISO_8859_1 = "ISO-8859-1";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long MAX_IO_CHUNK_SIZE = 16777216L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets) {
/*  98 */     return selectOutOfDateSources(logTo, source, mapper, targets, FILE_UTILS
/*  99 */         .getFileTimestampGranularity());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Resource[] selectOutOfDateSources(ProjectComponent logTo, Resource[] source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
/* 124 */     Union u = new Union();
/* 125 */     u.addAll(Arrays.asList(source));
/*     */     
/* 127 */     ResourceCollection rc = selectOutOfDateSources(logTo, (ResourceCollection)u, mapper, targets, granularity);
/* 128 */     return (rc.size() == 0) ? new Resource[0] : ((Union)rc).listResources();
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
/*     */   public static ResourceCollection selectOutOfDateSources(ProjectComponent logTo, ResourceCollection source, FileNameMapper mapper, ResourceFactory targets, long granularity) {
/* 148 */     logFuture(logTo, source, granularity);
/* 149 */     return selectSources(logTo, source, mapper, targets, sr -> ());
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
/*     */   
/*     */   public static ResourceCollection selectSources(ProjectComponent logTo, ResourceCollection source, FileNameMapper mapper, ResourceFactory targets, ResourceSelectorProvider selector) {
/* 172 */     if (source.isEmpty()) {
/* 173 */       logTo.log("No sources found.", 3);
/* 174 */       return Resources.NONE;
/*     */     } 
/* 176 */     Union union1 = Union.getInstance(source);
/*     */     
/* 178 */     Union result = new Union();
/* 179 */     for (Resource sr : union1) {
/* 180 */       String srName = sr.getName();
/* 181 */       if (srName != null) {
/* 182 */         srName = srName.replace('/', File.separatorChar);
/*     */       }
/*     */ 
/*     */       
/* 186 */       String[] targetnames = null;
/*     */       try {
/* 188 */         targetnames = mapper.mapFileName(srName);
/* 189 */       } catch (Exception e) {
/* 190 */         logTo.log("Caught " + e + " mapping resource " + sr, 3);
/*     */       } 
/*     */       
/* 193 */       if (targetnames == null || targetnames.length == 0) {
/* 194 */         logTo.log(sr + " skipped - don't know how to handle it", 3);
/*     */         
/*     */         continue;
/*     */       } 
/* 198 */       Union targetColl = new Union();
/* 199 */       for (String targetname : targetnames) {
/* 200 */         if (targetname == null) {
/* 201 */           targetname = "(no name)";
/*     */         }
/* 203 */         targetColl.add((ResourceCollection)targets.getResource(targetname
/* 204 */               .replace(File.separatorChar, '/')));
/*     */       } 
/*     */       
/* 207 */       Restrict r = new Restrict();
/* 208 */       r.add(selector.getTargetSelectorForSource(sr));
/* 209 */       r.add((ResourceCollection)targetColl);
/* 210 */       if (r.size() > 0) {
/* 211 */         result.add((ResourceCollection)sr);
/* 212 */         Resource t = r.iterator().next();
/* 213 */         logTo.log(sr.getName() + " added as " + t.getName() + (
/* 214 */             t.isExists() ? " is outdated." : " doesn't exist."), 3);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 219 */       logTo.log(sr.getName() + " omitted as " + targetColl
/* 220 */           .toString() + (
/* 221 */           (targetColl.size() == 1) ? " is" : " are ") + " up to date.", 3);
/*     */     } 
/*     */     
/* 224 */     return (ResourceCollection)result;
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
/*     */   public static void copyResource(Resource source, Resource dest) throws IOException {
/* 241 */     copyResource(source, dest, null);
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
/*     */   public static void copyResource(Resource source, Resource dest, Project project) throws IOException {
/* 260 */     copyResource(source, dest, null, null, false, false, null, null, project);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
/* 297 */     copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
/* 335 */     copyResource(source, dest, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyResource(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project, boolean force) throws IOException {
/*     */     String effectiveInputEncoding;
/* 375 */     if (!overwrite && !SelectorUtils.isOutOfDate(source, dest, 
/* 376 */         FileUtils.getFileUtils().getFileTimestampGranularity())) {
/*     */       return;
/*     */     }
/*     */     
/* 380 */     boolean filterSetsAvailable = (filters != null && filters.hasFilters());
/*     */     
/* 382 */     boolean filterChainsAvailable = (filterChains != null && !filterChains.isEmpty());
/*     */     
/* 384 */     if (source instanceof StringResource) {
/* 385 */       effectiveInputEncoding = ((StringResource)source).getEncoding();
/*     */     } else {
/* 387 */       effectiveInputEncoding = inputEncoding;
/*     */     } 
/* 389 */     File destFile = null;
/* 390 */     if (dest.as(FileProvider.class) != null) {
/* 391 */       destFile = ((FileProvider)dest.as(FileProvider.class)).getFile();
/*     */     }
/* 393 */     if (destFile != null && destFile.isFile() && !destFile.canWrite()) {
/* 394 */       if (!force) {
/* 395 */         throw new ReadOnlyTargetFileException(destFile);
/*     */       }
/* 397 */       if (!FILE_UTILS.tryHardToDelete(destFile)) {
/* 398 */         throw new IOException("failed to delete read-only destination file " + destFile);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 403 */     if (filterSetsAvailable) {
/* 404 */       copyWithFilterSets(source, dest, filters, filterChains, append, effectiveInputEncoding, outputEncoding, project);
/*     */     
/*     */     }
/* 407 */     else if (filterChainsAvailable || (effectiveInputEncoding != null && 
/*     */       
/* 409 */       !effectiveInputEncoding.equals(outputEncoding)) || (effectiveInputEncoding == null && outputEncoding != null)) {
/*     */       
/* 411 */       copyWithFilterChainsOrTranscoding(source, dest, filterChains, append, effectiveInputEncoding, outputEncoding, project);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 416 */       boolean copied = false;
/* 417 */       if (source.as(FileProvider.class) != null && destFile != null && !append) {
/*     */ 
/*     */         
/* 420 */         File sourceFile = ((FileProvider)source.as(FileProvider.class)).getFile();
/*     */         try {
/* 422 */           copyUsingFileChannels(sourceFile, destFile, project);
/* 423 */           copied = true;
/* 424 */         } catch (IOException ex) {
/*     */ 
/*     */           
/* 427 */           String msg = "Attempt to copy " + sourceFile + " to " + destFile + " using NIO Channels failed due to '" + ex.getMessage() + "'.  Falling back to streams.";
/*     */           
/* 429 */           if (project != null) {
/* 430 */             project.log(msg, 1);
/*     */           } else {
/* 432 */             System.err.println(msg);
/*     */           } 
/*     */         } 
/*     */       } 
/* 436 */       if (!copied) {
/* 437 */         copyUsingStreams(source, dest, append, project);
/*     */       }
/*     */     } 
/* 440 */     if (preserveLastModified) {
/* 441 */       Touchable t = (Touchable)dest.as(Touchable.class);
/* 442 */       if (t != null) {
/* 443 */         setLastModified(t, source.getLastModified());
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
/*     */ 
/*     */   
/*     */   public static void setLastModified(Touchable t, long time) {
/* 459 */     t.touch((time < 0L) ? System.currentTimeMillis() : time);
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
/*     */   public static boolean contentEquals(Resource r1, Resource r2, boolean text) throws IOException {
/* 476 */     if (r1.isExists() != r2.isExists()) {
/* 477 */       return false;
/*     */     }
/* 479 */     if (!r1.isExists())
/*     */     {
/* 481 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 485 */     if (r1.isDirectory() || r2.isDirectory())
/*     */     {
/* 487 */       return false;
/*     */     }
/* 489 */     if (r1.equals(r2)) {
/* 490 */       return true;
/*     */     }
/* 492 */     if (!text) {
/* 493 */       long s1 = r1.getSize();
/* 494 */       long s2 = r2.getSize();
/* 495 */       if (s1 != -1L && s2 != -1L && s1 != s2)
/*     */       {
/* 497 */         return false;
/*     */       }
/*     */     } 
/* 500 */     return (compareContent(r1, r2, text) == 0);
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
/*     */   public static int compareContent(Resource r1, Resource r2, boolean text) throws IOException {
/* 517 */     if (r1.equals(r2)) {
/* 518 */       return 0;
/*     */     }
/* 520 */     boolean e1 = r1.isExists();
/* 521 */     boolean e2 = r2.isExists();
/* 522 */     if (!e1 && !e2) {
/* 523 */       return 0;
/*     */     }
/* 525 */     if (e1 != e2) {
/* 526 */       return e1 ? 1 : -1;
/*     */     }
/* 528 */     boolean d1 = r1.isDirectory();
/* 529 */     boolean d2 = r2.isDirectory();
/* 530 */     if (d1 && d2) {
/* 531 */       return 0;
/*     */     }
/* 533 */     if (d1 || d2) {
/* 534 */       return d1 ? -1 : 1;
/*     */     }
/* 536 */     return text ? textCompare(r1, r2) : binaryCompare(r1, r2);
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
/*     */   public static FileResource asFileResource(FileProvider fileProvider) {
/* 549 */     if (fileProvider instanceof FileResource || fileProvider == null) {
/* 550 */       return (FileResource)fileProvider;
/*     */     }
/* 552 */     return new FileResource(Project.getProject(fileProvider), fileProvider
/* 553 */         .getFile());
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
/*     */   private static int binaryCompare(Resource r1, Resource r2) throws IOException {
/* 572 */     InputStream in1 = new BufferedInputStream(r1.getInputStream());
/*     */     try {
/* 574 */       InputStream in2 = new BufferedInputStream(r2.getInputStream()); try {
/*     */         int b1;
/* 576 */         for (b1 = in1.read(); b1 != -1; b1 = in1.read())
/* 577 */         { int b2 = in2.read();
/* 578 */           if (b1 != b2)
/* 579 */           { boolean bool = (b1 > b2) ? true : true;
/*     */ 
/*     */ 
/*     */             
/* 583 */             in2.close(); in1.close(); return bool; }  }  b1 = (in2.read() == -1) ? 0 : -1; in2.close(); in1.close(); return b1;
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           in2.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         in1.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/* 598 */     }  } private static int textCompare(Resource r1, Resource r2) throws IOException { BufferedReader in1 = new BufferedReader(new InputStreamReader(r1.getInputStream()));
/*     */     try {
/* 600 */       BufferedReader in2 = new BufferedReader(new InputStreamReader(r2.getInputStream()));
/*     */       
/* 602 */       try { String expected = in1.readLine();
/* 603 */         while (expected != null)
/* 604 */         { String actual = in2.readLine();
/* 605 */           if (!expected.equals(actual))
/* 606 */           { if (actual == null)
/* 607 */             { boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 614 */               in2.close(); in1.close(); return bool1; }  int i = expected.compareTo(actual); in2.close(); in1.close(); return i; }  expected = in1.readLine(); }  boolean bool = (in2.readLine() == null) ? false : true; in2.close(); in1.close(); return bool; }
/*     */       catch (Throwable throwable) { try { in2.close(); }
/*     */         catch (Throwable throwable1)
/*     */         { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/*     */     
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         in1.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       }  throw throwable;
/* 626 */     }  } private static void logFuture(ProjectComponent logTo, ResourceCollection rc, long granularity) { long now = System.currentTimeMillis() + granularity;
/* 627 */     Date sel = new Date();
/* 628 */     sel.setMillis(now);
/* 629 */     sel.setWhen(TimeComparison.AFTER);
/* 630 */     Restrict future = new Restrict();
/* 631 */     future.add((ResourceSelector)sel);
/* 632 */     future.add(rc);
/* 633 */     for (Resource r : future) {
/* 634 */       logTo.log("Warning: " + r.getName() + " modified in the future.", 1);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void copyWithFilterSets(Resource source, Resource dest, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
/* 646 */     if (areSame(source, dest)) {
/*     */       
/* 648 */       log(project, "Skipping (self) copy of " + source + " to " + dest);
/*     */       
/*     */       return;
/*     */     } 
/* 652 */     Reader in = filterWith(project, inputEncoding, filterChains, source
/* 653 */         .getInputStream());
/*     */ 
/*     */     
/* 656 */     try { BufferedWriter out = new BufferedWriter(new OutputStreamWriter(getOutputStream(dest, append, project), charsetFor(outputEncoding)));
/*     */       
/* 658 */       try { LineTokenizer lineTokenizer = new LineTokenizer();
/* 659 */         lineTokenizer.setIncludeDelims(true);
/* 660 */         String line = lineTokenizer.getToken(in);
/* 661 */         while (line != null) {
/* 662 */           if (line.isEmpty()) {
/*     */ 
/*     */             
/* 665 */             out.newLine();
/*     */           } else {
/* 667 */             out.write(filters.replaceTokens(line));
/*     */           } 
/* 669 */           line = lineTokenizer.getToken(in);
/*     */         } 
/* 671 */         out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (in != null) in.close();  }
/*     */     catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 676 */      } private static Reader filterWith(Project project, String encoding, Vector<FilterChain> filterChains, InputStream input) { ChainReaderHelper.ChainReader chainReader; Reader r = new InputStreamReader(input, charsetFor(encoding));
/* 677 */     if (filterChains != null && !filterChains.isEmpty()) {
/* 678 */       ChainReaderHelper crh = new ChainReaderHelper();
/* 679 */       crh.setBufferSize(8192);
/* 680 */       crh.setPrimaryReader(r);
/* 681 */       crh.setFilterChains(filterChains);
/* 682 */       crh.setProject(project);
/* 683 */       chainReader = crh.getAssembledReader();
/*     */     } 
/* 685 */     return new BufferedReader((Reader)chainReader); }
/*     */ 
/*     */   
/*     */   private static Charset charsetFor(String encoding) {
/* 689 */     return (encoding == null) ? Charset.defaultCharset() : Charset.forName(encoding);
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
/*     */   private static void copyWithFilterChainsOrTranscoding(Resource source, Resource dest, Vector<FilterChain> filterChains, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
/* 701 */     if (areSame(source, dest)) {
/*     */       
/* 703 */       log(project, "Skipping (self) copy of " + source + " to " + dest);
/*     */       
/*     */       return;
/*     */     } 
/* 707 */     Reader in = filterWith(project, inputEncoding, filterChains, source
/* 708 */         .getInputStream());
/*     */     
/*     */     try {
/* 711 */       BufferedWriter out = new BufferedWriter(new OutputStreamWriter(getOutputStream(dest, append, project), charsetFor(outputEncoding))); 
/* 712 */       try { char[] buffer = new char[8192];
/*     */         while (true) {
/* 714 */           int nRead = in.read(buffer, 0, buffer.length);
/* 715 */           if (nRead == -1) {
/*     */             break;
/*     */           }
/* 718 */           out.write(buffer, 0, nRead);
/*     */         } 
/* 720 */         out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (in != null) in.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (in != null)
/*     */         try {
/*     */           in.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable;
/* 728 */     }  } private static void copyUsingFileChannels(File sourceFile, File destFile, Project project) throws IOException { if (FileUtils.getFileUtils().areSame(sourceFile, destFile)) {
/*     */       
/* 730 */       log(project, "Skipping (self) copy of " + sourceFile + " to " + destFile);
/*     */       return;
/*     */     } 
/* 733 */     File parent = destFile.getParentFile();
/* 734 */     if (parent != null && !parent.isDirectory() && 
/* 735 */       !parent.mkdirs() && !parent.isDirectory()) {
/* 736 */       throw new IOException("failed to create the parent directory for " + destFile);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 741 */     FileChannel srcChannel = FileChannel.open(sourceFile.toPath(), new OpenOption[] { StandardOpenOption.READ }); 
/* 742 */     try { FileChannel destChannel = FileChannel.open(destFile.toPath(), new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE });
/*     */ 
/*     */ 
/*     */       
/* 746 */       try { long position = 0L;
/* 747 */         long count = srcChannel.size();
/* 748 */         while (position < count) {
/*     */           
/* 750 */           long chunk = Math.min(16777216L, count - position);
/* 751 */           position += destChannel
/* 752 */             .transferFrom(srcChannel, position, chunk);
/*     */         } 
/* 754 */         if (destChannel != null) destChannel.close();  } catch (Throwable throwable) { if (destChannel != null) try { destChannel.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (srcChannel != null) srcChannel.close();  }
/*     */     catch (Throwable throwable) { if (srcChannel != null)
/*     */         try {
/*     */           srcChannel.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable; }
/* 761 */      } private static void copyUsingStreams(Resource source, Resource dest, boolean append, Project project) throws IOException { if (areSame(source, dest)) {
/*     */       
/* 763 */       log(project, "Skipping (self) copy of " + source + " to " + dest);
/*     */       return;
/*     */     } 
/* 766 */     InputStream in = source.getInputStream(); 
/* 767 */     try { OutputStream out = getOutputStream(dest, append, project);
/*     */       
/* 769 */       try { byte[] buffer = new byte[8192];
/* 770 */         int count = 0;
/*     */         while (true)
/* 772 */         { out.write(buffer, 0, count);
/* 773 */           count = in.read(buffer, 0, buffer.length);
/* 774 */           if (count == -1)
/* 775 */           { if (out != null) out.close();  if (in != null) in.close();  return; }  }  } catch (Throwable throwable) { if (out != null)
/*     */           try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (in != null)
/*     */         try { in.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 780 */      } private static OutputStream getOutputStream(Resource resource, boolean append, Project project) throws IOException { if (append) {
/* 781 */       Appendable a = (Appendable)resource.as(Appendable.class);
/* 782 */       if (a != null) {
/* 783 */         return a.getAppendOutputStream();
/*     */       }
/* 785 */       String msg = "Appendable OutputStream not available for non-appendable resource " + resource + "; using plain OutputStream";
/*     */       
/* 787 */       if (project != null) {
/* 788 */         project.log(msg, 3);
/*     */       } else {
/* 790 */         System.out.println(msg);
/*     */       } 
/*     */     } 
/* 793 */     return resource.getOutputStream(); }
/*     */ 
/*     */   
/*     */   private static boolean areSame(Resource resource1, Resource resource2) throws IOException {
/* 797 */     if (resource1 == null || resource2 == null) {
/* 798 */       return false;
/*     */     }
/* 800 */     FileProvider fileResource1 = (FileProvider)resource1.as(FileProvider.class);
/* 801 */     FileProvider fileResource2 = (FileProvider)resource2.as(FileProvider.class);
/* 802 */     return (fileResource1 != null && fileResource2 != null && 
/* 803 */       FileUtils.getFileUtils().areSame(fileResource1.getFile(), fileResource2.getFile()));
/*     */   }
/*     */   
/*     */   private static void log(Project project, String message) {
/* 807 */     log(project, message, 3);
/*     */   }
/*     */   
/*     */   private static void log(Project project, String message, int level) {
/* 811 */     if (project == null) {
/* 812 */       System.out.println(message);
/*     */     } else {
/* 814 */       project.log(message, level);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ReadOnlyTargetFileException
/*     */     extends IOException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public ReadOnlyTargetFileException(File destFile) {
/* 829 */       super("can't write to read-only destination file " + destFile);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ResourceSelectorProvider {
/*     */     ResourceSelector getTargetSelectorForSource(Resource param1Resource);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ResourceUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */