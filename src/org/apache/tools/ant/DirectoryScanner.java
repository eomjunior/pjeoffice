/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.tools.ant.taskdefs.condition.Os;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceFactory;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.selectors.FileSelector;
/*      */ import org.apache.tools.ant.types.selectors.SelectorScanner;
/*      */ import org.apache.tools.ant.types.selectors.SelectorUtils;
/*      */ import org.apache.tools.ant.types.selectors.TokenizedPath;
/*      */ import org.apache.tools.ant.types.selectors.TokenizedPattern;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.VectorSet;
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
/*      */ public class DirectoryScanner
/*      */   implements FileScanner, SelectorScanner, ResourceFactory
/*      */ {
/*  155 */   private static final boolean ON_VMS = Os.isFamily("openvms");
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
/*      */   @Deprecated
/*  170 */   protected static final String[] DEFAULTEXCLUDES = new String[] { "**/*~", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.git", "**/.git/**", "**/.gitattributes", "**/.gitignore", "**/.gitmodules", "**/.hg", "**/.hg/**", "**/.hgignore", "**/.hgsub", "**/.hgsubstate", "**/.hgtags", "**/.bzr", "**/.bzr/**", "**/.bzrignore", "**/.DS_Store" };
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
/*      */   public static final int MAX_LEVELS_OF_SYMLINKS = 5;
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
/*      */   public static final String DOES_NOT_EXIST_POSTFIX = " does not exist.";
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
/*  230 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  237 */   private static final Set<String> defaultExcludes = new HashSet<>();
/*      */   static {
/*  239 */     resetDefaultExcludes();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected File basedir;
/*      */ 
/*      */   
/*      */   protected String[] includes;
/*      */ 
/*      */   
/*      */   protected String[] excludes;
/*      */ 
/*      */   
/*  254 */   protected FileSelector[] selectors = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> filesIncluded;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> filesNotIncluded;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> filesExcluded;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> dirsIncluded;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> dirsNotIncluded;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> dirsExcluded;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> filesDeselected;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vector<String> dirsDeselected;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean haveSlowResults = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isCaseSensitive = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean errorOnMissingDir = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean followSymlinks = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean everythingIncluded = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  330 */   private final Set<String> scannedDirs = new HashSet<>();
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
/*  347 */   private final Map<String, TokenizedPath> includeNonPatterns = new HashMap<>();
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
/*  364 */   private final Map<String, TokenizedPath> excludeNonPatterns = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TokenizedPattern[] includePatterns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TokenizedPattern[] excludePatterns;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean areNonPatternSetsReady = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean scanning = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  404 */   private final Object scanLock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean slowScanning = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  418 */   private final Object slowScanLock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  425 */   private IllegalStateException illegal = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  433 */   private int maxLevelsOfSymlinks = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  443 */   private final Set<String> notFollowedSymlinks = new HashSet<>();
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
/*      */   protected static boolean matchPatternStart(String pattern, String str) {
/*  462 */     return SelectorUtils.matchPatternStart(pattern, str);
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
/*      */   protected static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
/*  485 */     return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive);
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
/*      */   protected static boolean matchPath(String pattern, String str) {
/*  500 */     return SelectorUtils.matchPath(pattern, str);
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
/*      */   protected static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
/*  518 */     return SelectorUtils.matchPath(pattern, str, isCaseSensitive);
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
/*      */   public static boolean match(String pattern, String str) {
/*  536 */     return SelectorUtils.match(pattern, str);
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
/*      */   protected static boolean match(String pattern, String str, boolean isCaseSensitive) {
/*  558 */     return SelectorUtils.match(pattern, str, isCaseSensitive);
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
/*      */   public static String[] getDefaultExcludes() {
/*  572 */     synchronized (defaultExcludes) {
/*  573 */       return defaultExcludes.<String>toArray(new String[0]);
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
/*      */   public static boolean addDefaultExclude(String s) {
/*  588 */     synchronized (defaultExcludes) {
/*  589 */       return defaultExcludes.add(s);
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
/*      */   public static boolean removeDefaultExclude(String s) {
/*  605 */     synchronized (defaultExcludes) {
/*  606 */       return defaultExcludes.remove(s);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void resetDefaultExcludes() {
/*  616 */     synchronized (defaultExcludes) {
/*  617 */       defaultExcludes.clear();
/*  618 */       Collections.addAll(defaultExcludes, DEFAULTEXCLUDES);
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
/*      */   public void setBasedir(String basedir) {
/*  632 */     setBasedir((basedir == null) ? null : 
/*  633 */         new File(basedir.replace('/', File.separatorChar).replace('\\', File.separatorChar)));
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
/*      */   public synchronized void setBasedir(File basedir) {
/*  645 */     this.basedir = basedir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized File getBasedir() {
/*  656 */     return this.basedir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isCaseSensitive() {
/*  666 */     return this.isCaseSensitive;
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
/*      */   public synchronized void setCaseSensitive(boolean isCaseSensitive) {
/*  678 */     this.isCaseSensitive = isCaseSensitive;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorOnMissingDir(boolean errorOnMissingDir) {
/*  689 */     this.errorOnMissingDir = errorOnMissingDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isFollowSymlinks() {
/*  700 */     return this.followSymlinks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setFollowSymlinks(boolean followSymlinks) {
/*  709 */     this.followSymlinks = followSymlinks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLevelsOfSymlinks(int max) {
/*  720 */     this.maxLevelsOfSymlinks = max;
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
/*      */   public synchronized void setIncludes(String[] includes) {
/*  738 */     if (includes == null) {
/*  739 */       this.includes = null;
/*      */     } else {
/*  741 */       this
/*  742 */         .includes = (String[])Stream.<String>of(includes).map(DirectoryScanner::normalizePattern).toArray(x$0 -> new String[x$0]);
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
/*      */   public synchronized void setExcludes(String[] excludes) {
/*  760 */     if (excludes == null) {
/*  761 */       this.excludes = null;
/*      */     } else {
/*  763 */       this
/*  764 */         .excludes = (String[])Stream.<String>of(excludes).map(DirectoryScanner::normalizePattern).toArray(x$0 -> new String[x$0]);
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
/*      */   public synchronized void addExcludes(String[] excludes) {
/*  782 */     if (excludes != null && excludes.length > 0) {
/*  783 */       if (this.excludes == null || this.excludes.length == 0) {
/*  784 */         setExcludes(excludes);
/*      */       } else {
/*  786 */         this
/*      */           
/*  788 */           .excludes = (String[])Stream.concat(Stream.of((Object[])this.excludes), Stream.<String>of(excludes).map(DirectoryScanner::normalizePattern)).toArray(x$0 -> new String[x$0]);
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
/*      */   private static String normalizePattern(String p) {
/*  804 */     String pattern = p.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/*  805 */     if (pattern.endsWith(File.separator)) {
/*  806 */       pattern = pattern + "**";
/*      */     }
/*  808 */     return pattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setSelectors(FileSelector[] selectors) {
/*  818 */     this.selectors = selectors;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isEverythingIncluded() {
/*  829 */     return this.everythingIncluded;
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
/*      */   public void scan() throws IllegalStateException {
/*  844 */     synchronized (this.scanLock) {
/*  845 */       if (this.scanning) {
/*  846 */         while (this.scanning) {
/*      */           try {
/*  848 */             this.scanLock.wait();
/*  849 */           } catch (InterruptedException interruptedException) {}
/*      */         } 
/*      */         
/*  852 */         if (this.illegal != null) {
/*  853 */           throw this.illegal;
/*      */         }
/*      */         return;
/*      */       } 
/*  857 */       this.scanning = true;
/*      */     } 
/*  859 */     File savedBase = this.basedir;
/*      */     try {
/*  861 */       synchronized (this) {
/*  862 */         this.illegal = null;
/*  863 */         clearResults();
/*      */ 
/*      */         
/*  866 */         boolean nullIncludes = (this.includes == null);
/*  867 */         (new String[1])[0] = "**"; this.includes = nullIncludes ? new String[1] : this.includes;
/*  868 */         boolean nullExcludes = (this.excludes == null);
/*  869 */         this.excludes = nullExcludes ? new String[0] : this.excludes;
/*      */         
/*  871 */         if (this.basedir != null && !this.followSymlinks && 
/*  872 */           Files.isSymbolicLink(this.basedir.toPath())) {
/*  873 */           this.notFollowedSymlinks.add(this.basedir.getAbsolutePath());
/*  874 */           this.basedir = null;
/*      */         } 
/*      */         
/*  877 */         if (this.basedir == null) {
/*      */           
/*  879 */           if (nullIncludes) {
/*      */             return;
/*      */           }
/*      */         } else {
/*  883 */           if (!this.basedir.exists()) {
/*  884 */             if (this.errorOnMissingDir) {
/*  885 */               this.illegal = new IllegalStateException("basedir " + this.basedir + " does not exist.");
/*      */             }
/*      */             else {
/*      */               
/*      */               return;
/*      */             }
/*      */           
/*  892 */           } else if (!this.basedir.isDirectory()) {
/*  893 */             this.illegal = new IllegalStateException("basedir " + this.basedir + " is not a directory.");
/*      */           } 
/*      */ 
/*      */           
/*  897 */           if (this.illegal != null) {
/*  898 */             throw this.illegal;
/*      */           }
/*      */         } 
/*  901 */         if (isIncluded(TokenizedPath.EMPTY_PATH)) {
/*  902 */           if (isExcluded(TokenizedPath.EMPTY_PATH)) {
/*  903 */             this.dirsExcluded.addElement("");
/*  904 */           } else if (isSelected("", this.basedir)) {
/*  905 */             this.dirsIncluded.addElement("");
/*      */           } else {
/*  907 */             this.dirsDeselected.addElement("");
/*      */           } 
/*      */         } else {
/*  910 */           this.dirsNotIncluded.addElement("");
/*      */         } 
/*  912 */         checkIncludePatterns();
/*  913 */         clearCaches();
/*  914 */         this.includes = nullIncludes ? null : this.includes;
/*  915 */         this.excludes = nullExcludes ? null : this.excludes;
/*      */       } 
/*      */     } finally {
/*  918 */       this.basedir = savedBase;
/*  919 */       synchronized (this.scanLock) {
/*  920 */         this.scanning = false;
/*  921 */         this.scanLock.notifyAll();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkIncludePatterns() {
/*  932 */     ensureNonPatternSetsReady();
/*  933 */     Map<TokenizedPath, String> newroots = new HashMap<>();
/*      */ 
/*      */ 
/*      */     
/*  937 */     for (TokenizedPattern includePattern : this.includePatterns) {
/*  938 */       String pattern = includePattern.toString();
/*  939 */       if (!shouldSkipPattern(pattern)) {
/*  940 */         newroots.put(includePattern.rtrimWildcardTokens(), pattern);
/*      */       }
/*      */     } 
/*  943 */     for (Map.Entry<String, TokenizedPath> entry : this.includeNonPatterns
/*  944 */       .entrySet()) {
/*  945 */       String pattern = entry.getKey();
/*  946 */       if (!shouldSkipPattern(pattern)) {
/*  947 */         newroots.put(entry.getValue(), pattern);
/*      */       }
/*      */     } 
/*      */     
/*  951 */     if (newroots.containsKey(TokenizedPath.EMPTY_PATH) && this.basedir != null) {
/*      */ 
/*      */       
/*  954 */       scandir(this.basedir, "", true);
/*      */     } else {
/*  956 */       File canonBase = null;
/*  957 */       if (this.basedir != null) {
/*      */         try {
/*  959 */           canonBase = this.basedir.getCanonicalFile();
/*  960 */         } catch (IOException ex) {
/*  961 */           throw new BuildException(ex);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/*  966 */       for (Map.Entry<TokenizedPath, String> entry : newroots.entrySet()) {
/*  967 */         TokenizedPath currentPath = entry.getKey();
/*  968 */         String currentelement = currentPath.toString();
/*  969 */         if (this.basedir == null && !FileUtils.isAbsolutePath(currentelement)) {
/*      */           continue;
/*      */         }
/*  972 */         File myfile = new File(this.basedir, currentelement);
/*      */         
/*  974 */         if (myfile.exists()) {
/*      */           
/*      */           try {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  981 */             String path = (this.basedir == null) ? myfile.getCanonicalPath() : FILE_UTILS.removeLeadingPath(canonBase, myfile
/*  982 */                 .getCanonicalFile());
/*  983 */             if (!path.equals(currentelement) || ON_VMS) {
/*  984 */               myfile = currentPath.findFile(this.basedir, true);
/*  985 */               if (myfile != null && this.basedir != null) {
/*  986 */                 currentelement = FILE_UTILS.removeLeadingPath(this.basedir, myfile);
/*      */                 
/*  988 */                 if (!currentPath.toString().equals(currentelement)) {
/*  989 */                   currentPath = new TokenizedPath(currentelement);
/*      */                 }
/*      */               } 
/*      */             } 
/*  993 */           } catch (IOException ex) {
/*  994 */             throw new BuildException(ex);
/*      */           } 
/*      */         }
/*      */         
/*  998 */         if ((myfile == null || !myfile.exists()) && !isCaseSensitive()) {
/*  999 */           File f = currentPath.findFile(this.basedir, false);
/* 1000 */           if (f != null && f.exists()) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1005 */             currentelement = (this.basedir == null) ? f.getAbsolutePath() : FILE_UTILS.removeLeadingPath(this.basedir, f);
/* 1006 */             myfile = f;
/* 1007 */             currentPath = new TokenizedPath(currentelement);
/*      */           } 
/*      */         } 
/*      */         
/* 1011 */         if (myfile != null && myfile.exists()) {
/* 1012 */           if (!this.followSymlinks && currentPath.isSymlink(this.basedir)) {
/* 1013 */             accountForNotFollowedSymlink(currentPath, myfile);
/*      */             continue;
/*      */           } 
/* 1016 */           if (myfile.isDirectory()) {
/* 1017 */             if (isIncluded(currentPath) && !currentelement.isEmpty()) {
/* 1018 */               accountForIncludedDir(currentPath, myfile, true); continue;
/*      */             } 
/* 1020 */             scandir(myfile, currentPath, true); continue;
/*      */           } 
/* 1022 */           if (myfile.isFile()) {
/* 1023 */             String originalpattern = entry.getValue();
/*      */ 
/*      */             
/* 1026 */             boolean included = isCaseSensitive() ? originalpattern.equals(currentelement) : originalpattern.equalsIgnoreCase(currentelement);
/* 1027 */             if (included) {
/* 1028 */               accountForIncludedFile(currentPath, myfile);
/*      */             }
/*      */           } 
/*      */         } 
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
/*      */   private boolean shouldSkipPattern(String pattern) {
/* 1043 */     if (FileUtils.isAbsolutePath(pattern))
/*      */     {
/* 1045 */       return (this.basedir != null && !SelectorUtils.matchPatternStart(pattern, this.basedir
/* 1046 */           .getAbsolutePath(), isCaseSensitive()));
/*      */     }
/*      */     
/* 1049 */     return (this.basedir == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void clearResults() {
/* 1056 */     this.filesIncluded = (Vector<String>)new VectorSet();
/* 1057 */     this.filesNotIncluded = (Vector<String>)new VectorSet();
/* 1058 */     this.filesExcluded = (Vector<String>)new VectorSet();
/* 1059 */     this.filesDeselected = (Vector<String>)new VectorSet();
/* 1060 */     this.dirsIncluded = (Vector<String>)new VectorSet();
/* 1061 */     this.dirsNotIncluded = (Vector<String>)new VectorSet();
/* 1062 */     this.dirsExcluded = (Vector<String>)new VectorSet();
/* 1063 */     this.dirsDeselected = (Vector<String>)new VectorSet();
/* 1064 */     this.everythingIncluded = (this.basedir != null);
/* 1065 */     this.scannedDirs.clear();
/* 1066 */     this.notFollowedSymlinks.clear();
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
/*      */   protected void slowScan() {
/* 1078 */     synchronized (this.slowScanLock) {
/* 1079 */       if (this.haveSlowResults) {
/*      */         return;
/*      */       }
/* 1082 */       if (this.slowScanning) {
/* 1083 */         while (this.slowScanning) {
/*      */           try {
/* 1085 */             this.slowScanLock.wait();
/* 1086 */           } catch (InterruptedException interruptedException) {}
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1092 */       this.slowScanning = true;
/*      */     } 
/*      */     try {
/* 1095 */       synchronized (this) {
/*      */ 
/*      */         
/* 1098 */         boolean nullIncludes = (this.includes == null);
/* 1099 */         (new String[1])[0] = "**"; this.includes = nullIncludes ? new String[1] : this.includes;
/* 1100 */         boolean nullExcludes = (this.excludes == null);
/* 1101 */         this.excludes = nullExcludes ? new String[0] : this.excludes;
/*      */         
/* 1103 */         String[] excl = new String[this.dirsExcluded.size()];
/* 1104 */         this.dirsExcluded.copyInto((Object[])excl);
/*      */         
/* 1106 */         String[] notIncl = new String[this.dirsNotIncluded.size()];
/* 1107 */         this.dirsNotIncluded.copyInto((Object[])notIncl);
/*      */         
/* 1109 */         ensureNonPatternSetsReady();
/*      */         
/* 1111 */         processSlowScan(excl);
/* 1112 */         processSlowScan(notIncl);
/* 1113 */         clearCaches();
/* 1114 */         this.includes = nullIncludes ? null : this.includes;
/* 1115 */         this.excludes = nullExcludes ? null : this.excludes;
/*      */       } 
/*      */     } finally {
/* 1118 */       synchronized (this.slowScanLock) {
/* 1119 */         this.haveSlowResults = true;
/* 1120 */         this.slowScanning = false;
/* 1121 */         this.slowScanLock.notifyAll();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processSlowScan(String[] arr) {
/* 1127 */     for (String element : arr) {
/* 1128 */       TokenizedPath path = new TokenizedPath(element);
/* 1129 */       if (!couldHoldIncluded(path) || contentsExcluded(path)) {
/* 1130 */         scandir(new File(this.basedir, element), path, false);
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
/*      */   protected void scandir(File dir, String vpath, boolean fast) {
/* 1156 */     scandir(dir, new TokenizedPath(vpath), fast);
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
/*      */   private void scandir(File dir, TokenizedPath path, boolean fast) {
/* 1180 */     if (dir == null) {
/* 1181 */       throw new BuildException("dir must not be null.");
/*      */     }
/* 1183 */     String[] newfiles = dir.list();
/* 1184 */     if (newfiles == null) {
/* 1185 */       if (!dir.exists())
/* 1186 */         throw new BuildException(dir + " does not exist."); 
/* 1187 */       if (!dir.isDirectory()) {
/* 1188 */         throw new BuildException("%s is not a directory.", new Object[] { dir });
/*      */       }
/* 1190 */       throw new BuildException("IO error scanning directory '%s'", new Object[] { dir
/* 1191 */             .getAbsolutePath() });
/*      */     } 
/*      */     
/* 1194 */     scandir(dir, path, fast, newfiles, new LinkedList<>());
/*      */   }
/*      */ 
/*      */   
/*      */   private void scandir(File dir, TokenizedPath path, boolean fast, String[] newFiles, Deque<String> directoryNamesFollowed) {
/* 1199 */     String vpath = path.toString();
/* 1200 */     if (!vpath.isEmpty() && !vpath.endsWith(File.separator)) {
/* 1201 */       vpath = vpath + File.separator;
/*      */     }
/*      */ 
/*      */     
/* 1205 */     if (fast && hasBeenScanned(vpath)) {
/*      */       return;
/*      */     }
/* 1208 */     if (!this.followSymlinks) {
/* 1209 */       ArrayList<String> noLinks = new ArrayList<>();
/* 1210 */       for (String newFile : newFiles) {
/*      */         Path filePath;
/* 1212 */         if (dir == null) {
/* 1213 */           filePath = Paths.get(newFile, new String[0]);
/*      */         } else {
/* 1215 */           filePath = Paths.get(dir.toPath().toString(), new String[] { newFile });
/*      */         } 
/* 1217 */         if (Files.isSymbolicLink(filePath)) {
/* 1218 */           String name = vpath + newFile;
/* 1219 */           File file = new File(dir, newFile);
/* 1220 */           if (file.isDirectory()) {
/* 1221 */             this.dirsExcluded.addElement(name);
/* 1222 */           } else if (file.isFile()) {
/* 1223 */             this.filesExcluded.addElement(name);
/*      */           } 
/* 1225 */           accountForNotFollowedSymlink(name, file);
/*      */         } else {
/* 1227 */           noLinks.add(newFile);
/*      */         } 
/*      */       } 
/* 1230 */       newFiles = noLinks.<String>toArray(new String[0]);
/*      */     } else {
/* 1232 */       directoryNamesFollowed.addFirst(dir.getName());
/*      */     } 
/*      */     
/* 1235 */     for (String newFile : newFiles) {
/* 1236 */       String name = vpath + newFile;
/* 1237 */       TokenizedPath newPath = new TokenizedPath(path, newFile);
/* 1238 */       File file = new File(dir, newFile);
/* 1239 */       String[] children = file.list();
/* 1240 */       if (children == null || (children.length == 0 && file.isFile())) {
/* 1241 */         if (isIncluded(newPath)) {
/* 1242 */           accountForIncludedFile(newPath, file);
/*      */         } else {
/* 1244 */           this.everythingIncluded = false;
/* 1245 */           this.filesNotIncluded.addElement(name);
/*      */         } 
/* 1247 */       } else if (file.isDirectory()) {
/*      */         
/* 1249 */         if (this.followSymlinks && 
/* 1250 */           causesIllegalSymlinkLoop(newFile, dir, directoryNamesFollowed)) {
/*      */           
/* 1252 */           System.err.println("skipping symbolic link " + file
/* 1253 */               .getAbsolutePath() + " -- too many levels of symbolic links.");
/*      */ 
/*      */           
/* 1256 */           this.notFollowedSymlinks.add(file.getAbsolutePath());
/*      */         }
/*      */         else {
/*      */           
/* 1260 */           if (isIncluded(newPath)) {
/* 1261 */             accountForIncludedDir(newPath, file, fast, children, directoryNamesFollowed);
/*      */           } else {
/*      */             
/* 1264 */             this.everythingIncluded = false;
/* 1265 */             this.dirsNotIncluded.addElement(name);
/* 1266 */             if (fast && couldHoldIncluded(newPath) && !contentsExcluded(newPath)) {
/* 1267 */               scandir(file, newPath, fast, children, directoryNamesFollowed);
/*      */             }
/*      */           } 
/* 1270 */           if (!fast) {
/* 1271 */             scandir(file, newPath, fast, children, directoryNamesFollowed);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1276 */     if (this.followSymlinks) {
/* 1277 */       directoryNamesFollowed.removeFirst();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void accountForIncludedFile(TokenizedPath name, File file) {
/* 1287 */     processIncluded(name, file, this.filesIncluded, this.filesExcluded, this.filesDeselected);
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
/*      */   private void accountForIncludedDir(TokenizedPath name, File file, boolean fast) {
/* 1300 */     processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
/* 1301 */     if (fast && couldHoldIncluded(name) && !contentsExcluded(name)) {
/* 1302 */       scandir(file, name, fast);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void accountForIncludedDir(TokenizedPath name, File file, boolean fast, String[] children, Deque<String> directoryNamesFollowed) {
/* 1310 */     processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
/* 1311 */     if (fast && couldHoldIncluded(name) && !contentsExcluded(name)) {
/* 1312 */       scandir(file, name, fast, children, directoryNamesFollowed);
/*      */     }
/*      */   }
/*      */   
/*      */   private void accountForNotFollowedSymlink(String name, File file) {
/* 1317 */     accountForNotFollowedSymlink(new TokenizedPath(name), file);
/*      */   }
/*      */   
/*      */   private void accountForNotFollowedSymlink(TokenizedPath name, File file) {
/* 1321 */     if (!isExcluded(name) && (isIncluded(name) || (file
/* 1322 */       .isDirectory() && couldHoldIncluded(name) && !contentsExcluded(name)))) {
/* 1323 */       this.notFollowedSymlinks.add(file.getAbsolutePath());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processIncluded(TokenizedPath path, File file, List<String> inc, List<String> exc, List<String> des) {
/* 1330 */     String name = path.toString();
/* 1331 */     if (inc.contains(name) || exc.contains(name) || des.contains(name)) {
/*      */       return;
/*      */     }
/* 1334 */     boolean included = false;
/* 1335 */     if (isExcluded(path)) {
/* 1336 */       exc.add(name);
/* 1337 */     } else if (isSelected(name, file)) {
/* 1338 */       included = true;
/* 1339 */       inc.add(name);
/*      */     } else {
/* 1341 */       des.add(name);
/*      */     } 
/* 1343 */     this.everythingIncluded &= included;
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
/*      */   protected boolean isIncluded(String name) {
/* 1355 */     return isIncluded(new TokenizedPath(name));
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
/*      */   private boolean isIncluded(TokenizedPath path) {
/* 1367 */     ensureNonPatternSetsReady();
/*      */     
/* 1369 */     String toMatch = path.toString();
/* 1370 */     if (!isCaseSensitive()) {
/* 1371 */       toMatch = toMatch.toUpperCase();
/*      */     }
/* 1373 */     return (this.includeNonPatterns.containsKey(toMatch) || 
/* 1374 */       Stream.<TokenizedPattern>of(this.includePatterns).anyMatch(p -> p.matchPath(path, isCaseSensitive())));
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
/*      */   protected boolean couldHoldIncluded(String name) {
/* 1386 */     return couldHoldIncluded(new TokenizedPath(name));
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
/*      */   private boolean couldHoldIncluded(TokenizedPath tokenizedName) {
/* 1398 */     return Stream.concat(Stream.of((Object[])this.includePatterns), this.includeNonPatterns
/* 1399 */         .values().stream().map(TokenizedPath::toPattern))
/* 1400 */       .anyMatch(pat -> couldHoldIncluded(tokenizedName, pat));
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
/*      */   private boolean couldHoldIncluded(TokenizedPath tokenizedName, TokenizedPattern tokenizedInclude) {
/* 1413 */     return (tokenizedInclude.matchStartOf(tokenizedName, isCaseSensitive()) && 
/* 1414 */       isMorePowerfulThanExcludes(tokenizedName.toString()) && 
/* 1415 */       isDeeper(tokenizedInclude, tokenizedName));
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
/*      */   private boolean isDeeper(TokenizedPattern pattern, TokenizedPath name) {
/* 1427 */     return (pattern.containsPattern("**") || pattern
/* 1428 */       .depth() > name.depth());
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
/*      */   private boolean isMorePowerfulThanExcludes(String name) {
/* 1448 */     String soughtexclude = name + File.separatorChar + "**";
/* 1449 */     return Stream.<TokenizedPattern>of(this.excludePatterns).map(Object::toString)
/* 1450 */       .noneMatch(Predicate.isEqual(soughtexclude));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean contentsExcluded(TokenizedPath path) {
/* 1459 */     return Stream.<TokenizedPattern>of(this.excludePatterns)
/* 1460 */       .filter(p -> p.endsWith("**"))
/* 1461 */       .map(TokenizedPattern::withoutLastToken)
/* 1462 */       .anyMatch(wlt -> wlt.matchPath(path, isCaseSensitive()));
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
/*      */   protected boolean isExcluded(String name) {
/* 1474 */     return isExcluded(new TokenizedPath(name));
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
/*      */   private boolean isExcluded(TokenizedPath name) {
/* 1486 */     ensureNonPatternSetsReady();
/*      */     
/* 1488 */     String toMatch = name.toString();
/* 1489 */     if (!isCaseSensitive()) {
/* 1490 */       toMatch = toMatch.toUpperCase();
/*      */     }
/* 1492 */     return (this.excludeNonPatterns.containsKey(toMatch) || 
/* 1493 */       Stream.<TokenizedPattern>of(this.excludePatterns).anyMatch(p -> p.matchPath(name, isCaseSensitive())));
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
/*      */   protected boolean isSelected(String name, File file) {
/* 1505 */     return (this.selectors == null || 
/* 1506 */       Stream.<FileSelector>of(this.selectors).allMatch(sel -> sel.isSelected(this.basedir, name, file)));
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
/*      */   public String[] getIncludedFiles() {
/*      */     String[] files;
/* 1520 */     synchronized (this) {
/* 1521 */       if (this.filesIncluded == null) {
/* 1522 */         throw new IllegalStateException("Must call scan() first");
/*      */       }
/* 1524 */       files = this.filesIncluded.<String>toArray(new String[0]);
/*      */     } 
/* 1526 */     Arrays.sort((Object[])files);
/* 1527 */     return files;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getIncludedFilesCount() {
/* 1536 */     if (this.filesIncluded == null) {
/* 1537 */       throw new IllegalStateException("Must call scan() first");
/*      */     }
/* 1539 */     return this.filesIncluded.size();
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
/*      */   public synchronized String[] getNotIncludedFiles() {
/* 1554 */     slowScan();
/* 1555 */     return this.filesNotIncluded.<String>toArray(new String[0]);
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
/*      */   public synchronized String[] getExcludedFiles() {
/* 1571 */     slowScan();
/* 1572 */     return this.filesExcluded.<String>toArray(new String[0]);
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
/*      */   public synchronized String[] getDeselectedFiles() {
/* 1588 */     slowScan();
/* 1589 */     return this.filesDeselected.<String>toArray(new String[0]);
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
/*      */   public String[] getIncludedDirectories() {
/*      */     String[] directories;
/* 1603 */     synchronized (this) {
/* 1604 */       if (this.dirsIncluded == null) {
/* 1605 */         throw new IllegalStateException("Must call scan() first");
/*      */       }
/* 1607 */       directories = this.dirsIncluded.<String>toArray(new String[0]);
/*      */     } 
/* 1609 */     Arrays.sort((Object[])directories);
/* 1610 */     return directories;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getIncludedDirsCount() {
/* 1619 */     if (this.dirsIncluded == null) {
/* 1620 */       throw new IllegalStateException("Must call scan() first");
/*      */     }
/* 1622 */     return this.dirsIncluded.size();
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
/*      */   public synchronized String[] getNotIncludedDirectories() {
/* 1637 */     slowScan();
/* 1638 */     return this.dirsNotIncluded.<String>toArray(new String[0]);
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
/*      */   public synchronized String[] getExcludedDirectories() {
/* 1654 */     slowScan();
/* 1655 */     return this.dirsExcluded.<String>toArray(new String[0]);
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
/*      */   public synchronized String[] getDeselectedDirectories() {
/* 1671 */     slowScan();
/* 1672 */     return this.dirsDeselected.<String>toArray(new String[0]);
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
/*      */   public synchronized String[] getNotFollowedSymlinks() {
/*      */     String[] links;
/* 1686 */     synchronized (this) {
/* 1687 */       links = this.notFollowedSymlinks.<String>toArray(new String[0]);
/*      */     } 
/* 1689 */     Arrays.sort((Object[])links);
/* 1690 */     return links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addDefaultExcludes() {
/* 1698 */     Stream<String> s = Stream.<String>of(getDefaultExcludes()).map(p -> p.replace('/', File.separatorChar).replace('\\', File.separatorChar));
/*      */     
/* 1700 */     if (this.excludes != null) {
/* 1701 */       s = Stream.concat(Stream.of(this.excludes), s);
/*      */     }
/* 1703 */     this.excludes = s.<String>toArray(x$0 -> new String[x$0]);
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
/*      */   public synchronized Resource getResource(String name) {
/* 1715 */     return (Resource)new FileResource(this.basedir, name);
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
/*      */   private boolean hasBeenScanned(String vpath) {
/* 1727 */     return !this.scannedDirs.add(vpath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Set<String> getScannedDirs() {
/* 1736 */     return this.scannedDirs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void clearCaches() {
/* 1745 */     this.includeNonPatterns.clear();
/* 1746 */     this.excludeNonPatterns.clear();
/* 1747 */     this.includePatterns = null;
/* 1748 */     this.excludePatterns = null;
/* 1749 */     this.areNonPatternSetsReady = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void ensureNonPatternSetsReady() {
/* 1759 */     if (!this.areNonPatternSetsReady) {
/* 1760 */       this.includePatterns = fillNonPatternSet(this.includeNonPatterns, this.includes);
/* 1761 */       this.excludePatterns = fillNonPatternSet(this.excludeNonPatterns, this.excludes);
/* 1762 */       this.areNonPatternSetsReady = true;
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
/*      */   private TokenizedPattern[] fillNonPatternSet(Map<String, TokenizedPath> map, String[] patterns) {
/* 1776 */     List<TokenizedPattern> al = new ArrayList<>(patterns.length);
/* 1777 */     for (String pattern : patterns) {
/* 1778 */       if (SelectorUtils.hasWildcards(pattern)) {
/* 1779 */         al.add(new TokenizedPattern(pattern));
/*      */       } else {
/* 1781 */         String s = isCaseSensitive() ? pattern : pattern.toUpperCase();
/* 1782 */         map.put(s, new TokenizedPath(s));
/*      */       } 
/*      */     } 
/* 1785 */     return al.<TokenizedPattern>toArray(new TokenizedPattern[0]);
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
/*      */   private boolean causesIllegalSymlinkLoop(String dirName, File parent, Deque<String> directoryNamesFollowed) {
/*      */     try {
/*      */       Path dirPath;
/* 1803 */       if (parent == null) {
/* 1804 */         dirPath = Paths.get(dirName, new String[0]);
/*      */       } else {
/* 1806 */         dirPath = Paths.get(parent.toPath().toString(), new String[] { dirName });
/*      */       } 
/* 1808 */       if (directoryNamesFollowed.size() >= this.maxLevelsOfSymlinks && 
/* 1809 */         Collections.frequency(directoryNamesFollowed, dirName) >= this.maxLevelsOfSymlinks && 
/* 1810 */         Files.isSymbolicLink(dirPath)) {
/*      */         
/* 1812 */         List<String> files = new ArrayList<>();
/* 1813 */         File f = FILE_UTILS.resolveFile(parent, dirName);
/* 1814 */         String target = f.getCanonicalPath();
/* 1815 */         files.add(target);
/*      */         
/* 1817 */         StringBuilder relPath = new StringBuilder();
/* 1818 */         for (String dir : directoryNamesFollowed) {
/* 1819 */           relPath.append("../");
/* 1820 */           if (dirName.equals(dir)) {
/* 1821 */             f = FILE_UTILS.resolveFile(parent, relPath + dir);
/* 1822 */             files.add(f.getCanonicalPath());
/* 1823 */             if (files.size() > this.maxLevelsOfSymlinks && 
/* 1824 */               Collections.frequency(files, target) > this.maxLevelsOfSymlinks) {
/* 1825 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1830 */       return false;
/* 1831 */     } catch (IOException ex) {
/* 1832 */       throw new BuildException("Caught error while checking for symbolic links", ex);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/DirectoryScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */