/*      */ package org.apache.tools.ant.util;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.JarURLConnection;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.channels.Channel;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.NoSuchFileException;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.nio.file.attribute.PosixFileAttributeView;
/*      */ import java.nio.file.attribute.PosixFilePermission;
/*      */ import java.nio.file.attribute.PosixFilePermissions;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.PathTokenizer;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.launch.Locator;
/*      */ import org.apache.tools.ant.taskdefs.condition.Os;
/*      */ import org.apache.tools.ant.types.FilterChain;
/*      */ import org.apache.tools.ant.types.FilterSetCollection;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Touchable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FileUtils
/*      */ {
/*      */   private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
/*      */   private static final int EXPAND_SPACE = 50;
/*   77 */   private static final FileUtils PRIMARY_INSTANCE = new FileUtils();
/*      */ 
/*      */   
/*   80 */   private static Random rand = new Random(System.currentTimeMillis() + 
/*   81 */       Runtime.getRuntime().freeMemory());
/*      */   
/*   83 */   private static final boolean ON_NETWARE = Os.isFamily("netware");
/*   84 */   private static final boolean ON_DOS = Os.isFamily("dos");
/*   85 */   private static final boolean ON_WIN9X = Os.isFamily("win9x");
/*   86 */   private static final boolean ON_WINDOWS = Os.isFamily("windows");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final int BUF_SIZE = 8192;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000L;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000L;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1L;
/*      */ 
/*      */ 
/*      */   
/*  108 */   private static final FileAttribute[] TMPFILE_ATTRIBUTES = new FileAttribute[] {
/*      */       
/*  110 */       PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE))
/*      */     };
/*      */   
/*  113 */   private static final FileAttribute[] TMPDIR_ATTRIBUTES = new FileAttribute[] {
/*      */       
/*  115 */       PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE))
/*      */     };
/*      */   
/*  118 */   private static final FileAttribute[] NO_TMPFILE_ATTRIBUTES = new FileAttribute[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  126 */   private Object cacheFromUriLock = new Object();
/*  127 */   private String cacheFromUriRequest = null;
/*  128 */   private String cacheFromUriResponse = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String NULL_PLACEHOLDER = "null";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static FileUtils newFileUtils() {
/*  140 */     return new FileUtils();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FileUtils getFileUtils() {
/*  150 */     return PRIMARY_INSTANCE;
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
/*      */   public URL getFileURL(File file) throws MalformedURLException {
/*  168 */     return new URL(file.toURI().toASCIIString());
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
/*      */   public void copyFile(String sourceFile, String destFile) throws IOException {
/*  183 */     copyFile(new File(sourceFile), new File(destFile), (FilterSetCollection)null, false, false);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters) throws IOException {
/*  200 */     copyFile(new File(sourceFile), new File(destFile), filters, false, false);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
/*  217 */     copyFile(new File(sourceFile), new File(destFile), filters, overwrite, false);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
/*  244 */     copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
/*  270 */     copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified, encoding);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
/*  305 */     copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, encoding, project);
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
/*      */   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
/*  336 */     copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, inputEncoding, outputEncoding, project);
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
/*      */   public void copyFile(File sourceFile, File destFile) throws IOException {
/*  349 */     copyFile(sourceFile, destFile, (FilterSetCollection)null, false, false);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters) throws IOException {
/*  366 */     copyFile(sourceFile, destFile, filters, false, false);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
/*  386 */     copyFile(sourceFile, destFile, filters, overwrite, false);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
/*  411 */     copyFile(sourceFile, destFile, filters, overwrite, preserveLastModified, (String)null);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
/*  436 */     copyFile(sourceFile, destFile, filters, (Vector<FilterChain>)null, overwrite, preserveLastModified, encoding, (Project)null);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
/*  470 */     copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, encoding, encoding, project);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
/*  507 */     copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, false, inputEncoding, outputEncoding, project);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project) throws IOException {
/*  546 */     copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, false);
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
/*      */   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector<FilterChain> filterChains, boolean overwrite, boolean preserveLastModified, boolean append, String inputEncoding, String outputEncoding, Project project, boolean force) throws IOException {
/*  586 */     ResourceUtils.copyResource((Resource)new FileResource(sourceFile), (Resource)new FileResource(destFile), filters, filterChains, overwrite, preserveLastModified, append, inputEncoding, outputEncoding, project, force);
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
/*      */   public void setFileLastModified(File file, long time) {
/*  604 */     ResourceUtils.setLastModified((Touchable)new FileResource(file), time);
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
/*      */   public File resolveFile(File file, String filename) {
/*  629 */     if (!isAbsolutePath(filename)) {
/*  630 */       char sep = File.separatorChar;
/*  631 */       filename = filename.replace('/', sep).replace('\\', sep);
/*  632 */       if (isContextRelativePath(filename)) {
/*  633 */         file = null;
/*      */ 
/*      */         
/*  636 */         String udir = System.getProperty("user.dir");
/*  637 */         if (filename.charAt(0) == sep && udir.charAt(0) == sep) {
/*  638 */           filename = dissect(udir)[0] + filename.substring(1);
/*      */         }
/*      */       } 
/*  641 */       filename = (new File(file, filename)).getAbsolutePath();
/*      */     } 
/*  643 */     return normalize(filename);
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
/*      */   public static boolean isContextRelativePath(String filename) {
/*  658 */     if ((!ON_DOS && !ON_NETWARE) || filename.isEmpty()) {
/*  659 */       return false;
/*      */     }
/*  661 */     char sep = File.separatorChar;
/*  662 */     filename = filename.replace('/', sep).replace('\\', sep);
/*  663 */     char c = filename.charAt(0);
/*  664 */     int len = filename.length();
/*  665 */     return ((c == sep && (len == 1 || filename.charAt(1) != sep)) || (
/*  666 */       Character.isLetter(c) && len > 1 && filename
/*  667 */       .charAt(1) == ':' && (len == 2 || filename
/*  668 */       .charAt(2) != sep)));
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
/*      */   public static boolean isAbsolutePath(String filename) {
/*  682 */     if (filename.isEmpty()) {
/*  683 */       return false;
/*      */     }
/*  685 */     int len = filename.length();
/*  686 */     char sep = File.separatorChar;
/*  687 */     filename = filename.replace('/', sep).replace('\\', sep);
/*  688 */     char c = filename.charAt(0);
/*  689 */     if (!ON_DOS && !ON_NETWARE) {
/*  690 */       return (c == sep);
/*      */     }
/*  692 */     if (c == sep) {
/*      */       
/*  694 */       if (!ON_DOS || len <= 4 || filename.charAt(1) != sep) {
/*  695 */         return false;
/*      */       }
/*      */       
/*  698 */       int nextsep = filename.indexOf(sep, 2);
/*  699 */       return (nextsep > 2 && nextsep + 1 < len);
/*      */     } 
/*  701 */     int colon = filename.indexOf(':');
/*  702 */     return ((Character.isLetter(c) && colon == 1 && filename
/*  703 */       .length() > 2 && filename.charAt(2) == sep) || (ON_NETWARE && colon > 0));
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
/*      */   public static String translatePath(String toProcess) {
/*  725 */     if (toProcess == null || toProcess.isEmpty()) {
/*  726 */       return "";
/*      */     }
/*  728 */     StringBuilder path = new StringBuilder(toProcess.length() + 50);
/*  729 */     PathTokenizer tokenizer = new PathTokenizer(toProcess);
/*  730 */     while (tokenizer.hasMoreTokens()) {
/*  731 */       String pathComponent = tokenizer.nextToken();
/*  732 */       pathComponent = pathComponent.replace('/', File.separatorChar);
/*  733 */       pathComponent = pathComponent.replace('\\', File.separatorChar);
/*  734 */       if (path.length() > 0) {
/*  735 */         path.append(File.pathSeparatorChar);
/*      */       }
/*  737 */       path.append(pathComponent);
/*      */     } 
/*  739 */     return path.toString();
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
/*      */   public File normalize(String path) {
/*  766 */     Stack<String> s = new Stack<>();
/*  767 */     String[] dissect = dissect(path);
/*  768 */     s.push(dissect[0]);
/*      */     
/*  770 */     StringTokenizer tok = new StringTokenizer(dissect[1], File.separator);
/*  771 */     while (tok.hasMoreTokens()) {
/*  772 */       String thisToken = tok.nextToken();
/*  773 */       if (".".equals(thisToken)) {
/*      */         continue;
/*      */       }
/*  776 */       if ("..".equals(thisToken)) {
/*  777 */         if (s.size() < 2)
/*      */         {
/*  779 */           return new File(path);
/*      */         }
/*  781 */         s.pop(); continue;
/*      */       } 
/*  783 */       s.push(thisToken);
/*      */     } 
/*      */     
/*  786 */     StringBuilder sb = new StringBuilder();
/*  787 */     int size = s.size();
/*  788 */     for (int i = 0; i < size; i++) {
/*  789 */       if (i > 1)
/*      */       {
/*      */         
/*  792 */         sb.append(File.separatorChar);
/*      */       }
/*  794 */       sb.append(s.elementAt(i));
/*      */     } 
/*  796 */     return new File(sb.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] dissect(String path) {
/*      */     String root;
/*  807 */     char sep = File.separatorChar;
/*  808 */     path = path.replace('/', sep).replace('\\', sep);
/*      */ 
/*      */     
/*  811 */     if (!isAbsolutePath(path)) {
/*  812 */       throw new BuildException(path + " is not an absolute path");
/*      */     }
/*      */     
/*  815 */     int colon = path.indexOf(':');
/*  816 */     if (colon > 0 && (ON_DOS || ON_NETWARE)) {
/*      */       
/*  818 */       int next = colon + 1;
/*  819 */       root = path.substring(0, next);
/*  820 */       char[] ca = path.toCharArray();
/*  821 */       root = root + sep;
/*      */       
/*  823 */       next = (ca[next] == sep) ? (next + 1) : next;
/*      */       
/*  825 */       StringBuilder sbPath = new StringBuilder();
/*      */       
/*  827 */       for (int i = next; i < ca.length; i++) {
/*  828 */         if (ca[i] != sep || ca[i - 1] != sep) {
/*  829 */           sbPath.append(ca[i]);
/*      */         }
/*      */       } 
/*  832 */       path = sbPath.toString();
/*  833 */     } else if (path.length() > 1 && path.charAt(1) == sep) {
/*      */       
/*  835 */       int nextsep = path.indexOf(sep, 2);
/*  836 */       nextsep = path.indexOf(sep, nextsep + 1);
/*  837 */       root = (nextsep > 2) ? path.substring(0, nextsep + 1) : path;
/*  838 */       path = path.substring(root.length());
/*      */     } else {
/*  840 */       root = File.separator;
/*  841 */       path = path.substring(1);
/*      */     } 
/*  843 */     return new String[] { root, path };
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
/*      */   public String toVMSPath(File f) {
/*  858 */     String path = normalize(f.getAbsolutePath()).getPath();
/*  859 */     String name = f.getName();
/*  860 */     boolean isAbsolute = (path.charAt(0) == File.separatorChar);
/*      */ 
/*      */ 
/*      */     
/*  864 */     boolean isDirectory = (f.isDirectory() && !name.regionMatches(true, name.length() - 4, ".DIR", 0, 4));
/*      */     
/*  866 */     String device = null;
/*  867 */     StringBuilder directory = null;
/*  868 */     String file = null;
/*      */     
/*  870 */     int index = 0;
/*      */     
/*  872 */     if (isAbsolute) {
/*  873 */       index = path.indexOf(File.separatorChar, 1);
/*  874 */       if (index == -1) {
/*  875 */         return path.substring(1) + ":[000000]";
/*      */       }
/*  877 */       device = path.substring(1, index++);
/*      */     } 
/*  879 */     if (isDirectory) {
/*  880 */       directory = new StringBuilder(path.substring(index).replace(File.separatorChar, '.'));
/*      */     } else {
/*  882 */       int dirEnd = path.lastIndexOf(File.separatorChar);
/*  883 */       if (dirEnd == -1 || dirEnd < index) {
/*  884 */         file = path.substring(index);
/*      */       } else {
/*      */         
/*  887 */         directory = new StringBuilder(path.substring(index, dirEnd).replace(File.separatorChar, '.'));
/*  888 */         index = dirEnd + 1;
/*  889 */         if (path.length() > index) {
/*  890 */           file = path.substring(index);
/*      */         }
/*      */       } 
/*      */     } 
/*  894 */     if (!isAbsolute && directory != null) {
/*  895 */       directory.insert(0, '.');
/*      */     }
/*      */ 
/*      */     
/*  899 */     String osPath = ((device != null) ? (device + ":") : "") + ((directory != null) ? ("[" + directory + "]") : "") + ((file != null) ? file : "");
/*  900 */     return osPath;
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
/*      */   @Deprecated
/*      */   public File createTempFile(String prefix, String suffix, File parentDir) {
/*  930 */     return createTempFile(prefix, suffix, parentDir, false, false);
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
/*      */   @Deprecated
/*      */   public File createTempFile(String prefix, String suffix, File parentDir, boolean deleteOnExit, boolean createFile) {
/*  963 */     return createTempFile(null, prefix, suffix, parentDir, deleteOnExit, createFile);
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
/*      */   public File createTempFile(Project project, String prefix, String suffix, File parentDir, boolean deleteOnExit, boolean createFile) {
/*      */     File result;
/*  999 */     String p = null;
/* 1000 */     if (parentDir != null) {
/* 1001 */       p = parentDir.getPath();
/* 1002 */     } else if (project != null && project.getProperty("ant.tmpdir") != null) {
/* 1003 */       p = project.getProperty("ant.tmpdir");
/* 1004 */     } else if (project != null && deleteOnExit) {
/* 1005 */       if (project.getProperty("ant.auto.tmpdir") != null) {
/* 1006 */         p = project.getProperty("ant.auto.tmpdir");
/*      */       } else {
/*      */         
/* 1009 */         Path systemTempDirPath = (new File(System.getProperty("java.io.tmpdir"))).toPath();
/*      */         
/* 1011 */         PosixFileAttributeView systemTempDirPosixAttributes = Files.<PosixFileAttributeView>getFileAttributeView(systemTempDirPath, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/* 1012 */         if (systemTempDirPosixAttributes != null) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1017 */             File projectTempDir = Files.createTempDirectory(systemTempDirPath, "ant", (FileAttribute<?>[])TMPDIR_ATTRIBUTES).toFile();
/* 1018 */             projectTempDir.deleteOnExit();
/* 1019 */             p = projectTempDir.getAbsolutePath();
/* 1020 */             project.setProperty("ant.auto.tmpdir", p);
/* 1021 */           } catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1027 */     String parent = (p != null) ? p : System.getProperty("java.io.tmpdir");
/* 1028 */     if (prefix == null) {
/* 1029 */       prefix = "null";
/*      */     }
/* 1031 */     if (suffix == null) {
/* 1032 */       suffix = "null";
/*      */     }
/*      */     
/* 1035 */     if (createFile)
/*      */     { try {
/* 1037 */         Path parentPath = (new File(parent)).toPath();
/*      */         
/* 1039 */         PosixFileAttributeView parentPosixAttributes = Files.<PosixFileAttributeView>getFileAttributeView(parentPath, PosixFileAttributeView.class, new java.nio.file.LinkOption[0]);
/*      */ 
/*      */         
/* 1042 */         result = Files.createTempFile(parentPath, prefix, suffix, (parentPosixAttributes != null) ? (FileAttribute<?>[])TMPFILE_ATTRIBUTES : (FileAttribute<?>[])NO_TMPFILE_ATTRIBUTES).toFile();
/* 1043 */       } catch (IOException e) {
/* 1044 */         throw new BuildException("Could not create tempfile in " + parent, e);
/*      */       }  }
/*      */     else
/*      */     
/* 1048 */     { DecimalFormat fmt = new DecimalFormat("#####");
/* 1049 */       synchronized (rand)
/*      */       
/*      */       { while (true)
/* 1052 */         { result = new File(parent, prefix + fmt.format(rand.nextInt(2147483647)) + suffix);
/* 1053 */           if (!result.exists()) {
/*      */           
/*      */           } else {
/*      */             continue;
/* 1057 */           }  if (deleteOnExit) {
/* 1058 */             result.deleteOnExit();
/*      */           }
/* 1060 */           return result; }  }  }  if (deleteOnExit) result.deleteOnExit();  return result;
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
/*      */   @Deprecated
/*      */   public File createTempFile(String prefix, String suffix, File parentDir, boolean deleteOnExit) {
/* 1093 */     return createTempFile(prefix, suffix, parentDir, deleteOnExit, false);
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
/*      */   public boolean contentEquals(File f1, File f2) throws IOException {
/* 1107 */     return contentEquals(f1, f2, false);
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
/*      */   public boolean contentEquals(File f1, File f2, boolean textfile) throws IOException {
/* 1124 */     return ResourceUtils.contentEquals((Resource)new FileResource(f1), (Resource)new FileResource(f2), textfile);
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
/*      */   @Deprecated
/*      */   public File getParentFile(File f) {
/* 1138 */     return (f == null) ? null : f.getParentFile();
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
/*      */   public static String readFully(Reader rdr) throws IOException {
/* 1150 */     return readFully(rdr, 8192);
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
/*      */   public static String readFully(Reader rdr, int bufferSize) throws IOException {
/* 1166 */     if (bufferSize <= 0) {
/* 1167 */       throw new IllegalArgumentException("Buffer size must be greater than 0");
/*      */     }
/*      */     
/* 1170 */     char[] buffer = new char[bufferSize];
/* 1171 */     int bufferLength = 0;
/* 1172 */     StringBuilder textBuffer = new StringBuilder();
/* 1173 */     while (bufferLength != -1) {
/* 1174 */       bufferLength = rdr.read(buffer);
/* 1175 */       if (bufferLength > 0) {
/* 1176 */         textBuffer.append(buffer, 0, bufferLength);
/*      */       }
/*      */     } 
/* 1179 */     return (textBuffer.length() == 0) ? null : textBuffer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String safeReadFully(Reader reader) throws IOException {
/* 1190 */     String ret = readFully(reader);
/* 1191 */     return (ret == null) ? "" : ret;
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
/*      */   public boolean createNewFile(File f) throws IOException {
/* 1207 */     return f.createNewFile();
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
/*      */   public boolean createNewFile(File f, boolean mkdirs) throws IOException {
/* 1220 */     File parent = f.getParentFile();
/* 1221 */     if (mkdirs && !parent.exists()) {
/* 1222 */       parent.mkdirs();
/*      */     }
/* 1224 */     return f.createNewFile();
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
/*      */   @Deprecated
/*      */   public boolean isSymbolicLink(File parent, String name) throws IOException {
/* 1245 */     if (parent == null) {
/* 1246 */       return Files.isSymbolicLink(Paths.get(name, new String[0]));
/*      */     }
/* 1248 */     return Files.isSymbolicLink(Paths.get(parent.toPath().toString(), new String[] { name }));
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
/*      */   public String removeLeadingPath(File leading, File path) {
/* 1266 */     String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
/* 1267 */     String p = normalize(path.getAbsolutePath()).getAbsolutePath();
/* 1268 */     if (l.equals(p)) {
/* 1269 */       return "";
/*      */     }
/*      */ 
/*      */     
/* 1273 */     if (!l.endsWith(File.separator)) {
/* 1274 */       l = l + File.separator;
/*      */     }
/* 1276 */     return p.startsWith(l) ? p.substring(l.length()) : p;
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
/*      */   public boolean isLeadingPath(File leading, File path) {
/* 1295 */     String l = normalize(leading.getAbsolutePath()).getAbsolutePath();
/* 1296 */     String p = normalize(path.getAbsolutePath()).getAbsolutePath();
/* 1297 */     if (l.equals(p)) {
/* 1298 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 1302 */     if (!l.endsWith(File.separator)) {
/* 1303 */       l = l + File.separator;
/*      */     }
/*      */     
/* 1306 */     String up = File.separator + ".." + File.separator;
/* 1307 */     if (l.contains(up) || p.contains(up) || (p + File.separator).contains(up)) {
/* 1308 */       return false;
/*      */     }
/* 1310 */     return p.startsWith(l);
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
/*      */   public boolean isLeadingPath(File leading, File path, boolean resolveSymlinks) throws IOException {
/* 1327 */     if (!resolveSymlinks) {
/* 1328 */       return isLeadingPath(leading, path);
/*      */     }
/* 1330 */     File l = leading.getCanonicalFile();
/* 1331 */     File p = path.getCanonicalFile();
/*      */     while (true) {
/* 1333 */       if (l.equals(p)) {
/* 1334 */         return true;
/*      */       }
/* 1336 */       p = p.getParentFile();
/* 1337 */       if (p == null) {
/* 1338 */         return false;
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
/*      */   public String toURI(String path) {
/* 1360 */     return (new File(path)).toURI().toASCIIString();
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
/*      */   public String fromURI(String uri) {
/* 1376 */     synchronized (this.cacheFromUriLock) {
/* 1377 */       if (uri.equals(this.cacheFromUriRequest)) {
/* 1378 */         return this.cacheFromUriResponse;
/*      */       }
/* 1380 */       String path = Locator.fromURI(uri);
/* 1381 */       String ret = isAbsolutePath(path) ? normalize(path).getAbsolutePath() : path;
/* 1382 */       this.cacheFromUriRequest = uri;
/* 1383 */       this.cacheFromUriResponse = ret;
/* 1384 */       return ret;
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
/*      */   public boolean fileNameEquals(File f1, File f2) {
/* 1403 */     return normalize(f1.getAbsolutePath()).getAbsolutePath().equals(
/* 1404 */         normalize(f2.getAbsolutePath()).getAbsolutePath());
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
/*      */   public boolean areSame(File f1, File f2) throws IOException {
/* 1418 */     if (f1 == null && f2 == null) {
/* 1419 */       return true;
/*      */     }
/* 1421 */     if (f1 == null || f2 == null) {
/* 1422 */       return false;
/*      */     }
/* 1424 */     return (fileNameEquals(f1, f2) || isSameFile(f1, f2));
/*      */   }
/*      */   
/*      */   private boolean isSameFile(File f1, File f2) throws IOException {
/* 1428 */     if (f1.exists()) {
/*      */       try {
/* 1430 */         return (f2.exists() && Files.isSameFile(f1.toPath(), f2.toPath()));
/* 1431 */       } catch (NoSuchFileException e) {
/*      */         
/* 1433 */         return false;
/*      */       } 
/*      */     }
/* 1436 */     File f1Normalized = normalize(f1.getAbsolutePath());
/* 1437 */     File f2Normalized = normalize(f2.getAbsolutePath());
/* 1438 */     return f1Normalized.getCanonicalFile().equals(f2Normalized
/* 1439 */         .getCanonicalFile());
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
/*      */   public void rename(File from, File to) throws IOException {
/* 1459 */     from = normalize(from.getAbsolutePath()).getCanonicalFile();
/* 1460 */     to = normalize(to.getAbsolutePath());
/* 1461 */     if (!from.exists()) {
/* 1462 */       System.err.println("Cannot rename nonexistent file " + from);
/*      */       return;
/*      */     } 
/* 1465 */     if (from.getAbsolutePath().equals(to.getAbsolutePath())) {
/* 1466 */       System.err.println("Rename of " + from + " to " + to + " is a no-op.");
/*      */       return;
/*      */     } 
/* 1469 */     if (to.exists() && !areSame(from, to) && !tryHardToDelete(to)) {
/* 1470 */       throw new IOException("Failed to delete " + to + " while trying to rename " + from);
/*      */     }
/* 1472 */     File parent = to.getParentFile();
/* 1473 */     if (parent != null && !parent.isDirectory() && 
/* 1474 */       !parent.mkdirs() && !parent.isDirectory()) {
/* 1475 */       throw new IOException("Failed to create directory " + parent + " while trying to rename " + from);
/*      */     }
/*      */     
/* 1478 */     if (!from.renameTo(to)) {
/* 1479 */       copyFile(from, to);
/* 1480 */       if (!tryHardToDelete(from)) {
/* 1481 */         throw new IOException("Failed to delete " + from + " while trying to rename it.");
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
/*      */   public long getFileTimestampGranularity() {
/* 1495 */     if (ON_WIN9X) {
/* 1496 */       return 2000L;
/*      */     }
/* 1498 */     if (ON_WINDOWS) {
/* 1499 */       return 1L;
/*      */     }
/* 1501 */     if (ON_DOS) {
/* 1502 */       return 2000L;
/*      */     }
/* 1504 */     return 1000L;
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
/*      */   public boolean hasErrorInCase(File localFile) {
/* 1534 */     localFile = normalize(localFile.getAbsolutePath());
/* 1535 */     if (!localFile.exists()) {
/* 1536 */       return false;
/*      */     }
/* 1538 */     String localFileName = localFile.getName();
/* 1539 */     FilenameFilter ff = (dir, name) -> (name.equalsIgnoreCase(localFileName) && !name.equals(localFileName));
/*      */     
/* 1541 */     String[] names = localFile.getParentFile().list(ff);
/* 1542 */     return (names != null && names.length == 1);
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
/*      */   public boolean isUpToDate(File source, File dest, long granularity) {
/* 1558 */     if (!dest.exists())
/*      */     {
/* 1560 */       return false;
/*      */     }
/* 1562 */     long sourceTime = source.lastModified();
/* 1563 */     long destTime = dest.lastModified();
/* 1564 */     return isUpToDate(sourceTime, destTime, granularity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUpToDate(File source, File dest) {
/* 1575 */     return isUpToDate(source, dest, getFileTimestampGranularity());
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
/*      */   public boolean isUpToDate(long sourceTime, long destTime, long granularity) {
/* 1588 */     return (destTime != -1L && destTime >= sourceTime + granularity);
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
/*      */   public boolean isUpToDate(long sourceTime, long destTime) {
/* 1600 */     return isUpToDate(sourceTime, destTime, getFileTimestampGranularity());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(Writer device) {
/* 1609 */     close(device);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(Reader device) {
/* 1619 */     close(device);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(OutputStream device) {
/* 1629 */     close(device);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(InputStream device) {
/* 1639 */     close(device);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(Channel device) {
/* 1650 */     close(device);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void close(URLConnection conn) {
/* 1661 */     if (conn != null) {
/*      */       try {
/* 1663 */         if (conn instanceof JarURLConnection) {
/* 1664 */           JarURLConnection juc = (JarURLConnection)conn;
/* 1665 */           JarFile jf = juc.getJarFile();
/* 1666 */           jf.close();
/* 1667 */         } else if (conn instanceof HttpURLConnection) {
/* 1668 */           ((HttpURLConnection)conn).disconnect();
/*      */         } 
/* 1670 */       } catch (IOException iOException) {}
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
/*      */   public static void close(AutoCloseable ac) {
/* 1685 */     if (null != ac) {
/*      */       try {
/* 1687 */         ac.close();
/* 1688 */       } catch (Exception exception) {}
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
/*      */   public static void delete(File file) {
/* 1700 */     if (file != null) {
/* 1701 */       file.delete();
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
/*      */   public boolean tryHardToDelete(File f) {
/* 1715 */     return tryHardToDelete(f, ON_WINDOWS);
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
/*      */   public boolean tryHardToDelete(File f, boolean runGC) {
/* 1728 */     if (!f.delete()) {
/* 1729 */       if (runGC) {
/* 1730 */         System.gc();
/*      */       }
/*      */       try {
/* 1733 */         Thread.sleep(10L);
/* 1734 */       } catch (InterruptedException interruptedException) {}
/*      */ 
/*      */       
/* 1737 */       return f.delete();
/*      */     } 
/* 1739 */     return true;
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
/*      */   public static String getRelativePath(File fromFile, File toFile) throws Exception {
/* 1758 */     String fromPath = fromFile.getCanonicalPath();
/* 1759 */     String toPath = toFile.getCanonicalPath();
/*      */ 
/*      */     
/* 1762 */     String[] fromPathStack = getPathStack(fromPath);
/* 1763 */     String[] toPathStack = getPathStack(toPath);
/*      */     
/* 1765 */     if (0 < toPathStack.length && 0 < fromPathStack.length) {
/* 1766 */       if (!fromPathStack[0].equals(toPathStack[0]))
/*      */       {
/* 1768 */         return getPath(Arrays.asList(toPathStack));
/*      */       }
/*      */     } else {
/*      */       
/* 1772 */       return getPath(Arrays.asList(toPathStack));
/*      */     } 
/*      */ 
/*      */     
/* 1776 */     int minLength = Math.min(fromPathStack.length, toPathStack.length);
/* 1777 */     int same = 1;
/* 1778 */     while (same < minLength && fromPathStack[same].equals(toPathStack[same])) {
/* 1779 */       same++;
/*      */     }
/*      */     
/* 1782 */     List<String> relativePathStack = new ArrayList<>();
/*      */ 
/*      */ 
/*      */     
/* 1786 */     for (int i = same; i < fromPathStack.length; i++) {
/* 1787 */       relativePathStack.add("..");
/*      */     }
/*      */ 
/*      */     
/* 1791 */     relativePathStack.addAll(Arrays.<String>asList(toPathStack).subList(same, toPathStack.length));
/*      */     
/* 1793 */     return getPath(relativePathStack);
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
/*      */   public static String[] getPathStack(String path) {
/* 1805 */     String normalizedPath = path.replace(File.separatorChar, '/');
/*      */     
/* 1807 */     return normalizedPath.split("/");
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
/*      */   public static String getPath(List<String> pathStack) {
/* 1820 */     return getPath(pathStack, '/');
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
/*      */   public static String getPath(List<String> pathStack, char separatorChar) {
/* 1833 */     return pathStack.stream().collect(Collectors.joining(Character.toString(separatorChar)));
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
/*      */   public String getDefaultEncoding() {
/* 1845 */     InputStreamReader is = new InputStreamReader(new InputStream()
/*      */         {
/*      */           public int read()
/*      */           {
/* 1849 */             return -1;
/*      */           }
/*      */         });
/*      */     try {
/* 1853 */       return is.getEncoding();
/*      */     } finally {
/* 1855 */       close(is);
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
/*      */   public static OutputStream newOutputStream(Path path, boolean append) throws IOException {
/* 1868 */     if (append) {
/* 1869 */       return Files.newOutputStream(path, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE });
/*      */     }
/*      */     
/* 1872 */     return Files.newOutputStream(path, new OpenOption[0]);
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
/*      */   public static Optional<Boolean> isCaseSensitiveFileSystem(Path path) {
/*      */     boolean caseSensitive;
/* 1895 */     if (path == null) {
/* 1896 */       throw new IllegalArgumentException("Path cannot be null");
/*      */     }
/* 1898 */     String mixedCaseFileNamePrefix = "aNt";
/* 1899 */     Path mixedCaseTmpFile = null;
/*      */     
/*      */     try {
/* 1902 */       if (Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/* 1903 */         mixedCaseTmpFile = Files.createTempFile(path.getParent(), "aNt", null, (FileAttribute<?>[])new FileAttribute[0]);
/* 1904 */       } else if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/* 1905 */         mixedCaseTmpFile = Files.createTempFile(path, "aNt", null, (FileAttribute<?>[])new FileAttribute[0]);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1911 */         return (Optional)Optional.empty();
/*      */       } 
/* 1913 */       Path lowerCasePath = Paths.get(mixedCaseTmpFile.toString().toLowerCase(Locale.US), new String[0]);
/*      */       try {
/* 1915 */         caseSensitive = !Files.isSameFile(mixedCaseTmpFile, lowerCasePath);
/* 1916 */       } catch (NoSuchFileException nsfe) {
/*      */ 
/*      */ 
/*      */         
/* 1920 */         caseSensitive = true;
/*      */       } 
/* 1922 */     } catch (IOException ioe) {
/* 1923 */       System.err.println("Could not determine the case sensitivity of the filesystem for path " + path + " due to " + ioe);
/*      */       
/* 1925 */       return (Optional)Optional.empty();
/*      */     } finally {
/*      */       
/* 1928 */       if (mixedCaseTmpFile != null) {
/* 1929 */         delete(mixedCaseTmpFile.toFile());
/*      */       }
/*      */     } 
/* 1932 */     return Optional.of(Boolean.valueOf(caseSensitive));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */