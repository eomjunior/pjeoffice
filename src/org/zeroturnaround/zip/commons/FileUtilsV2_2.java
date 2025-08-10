/*      */ package org.zeroturnaround.zip.commons;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FileUtilsV2_2
/*      */ {
/*      */   public static final long ONE_KB = 1024L;
/*      */   public static final long ONE_MB = 1048576L;
/*      */   private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
/*      */   public static final long ONE_GB = 1073741824L;
/*      */   public static final long ONE_TB = 1099511627776L;
/*      */   public static final long ONE_PB = 1125899906842624L;
/*      */   public static final long ONE_EB = 1152921504606846976L;
/*  103 */   public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   public static final BigInteger ONE_YB = ONE_ZB.multiply(BigInteger.valueOf(1152921504606846976L));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   public static final File[] EMPTY_FILE_ARRAY = new File[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FileInputStream openInputStream(File file) throws IOException {
/*  135 */     if (file.exists()) {
/*  136 */       if (file.isDirectory()) {
/*  137 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  139 */       if (!file.canRead()) {
/*  140 */         throw new IOException("File '" + file + "' cannot be read");
/*      */       }
/*      */     } else {
/*  143 */       throw new FileNotFoundException("File '" + file + "' does not exist");
/*      */     } 
/*  145 */     return new FileInputStream(file);
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
/*      */   public static FileOutputStream openOutputStream(File file) throws IOException {
/*  170 */     return openOutputStream(file, false);
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
/*      */   public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
/*  196 */     if (file.exists()) {
/*  197 */       if (file.isDirectory()) {
/*  198 */         throw new IOException("File '" + file + "' exists but is a directory");
/*      */       }
/*  200 */       if (!file.canWrite()) {
/*  201 */         throw new IOException("File '" + file + "' cannot be written to");
/*      */       }
/*      */     } else {
/*  204 */       File parent = file.getParentFile();
/*  205 */       if (parent != null && 
/*  206 */         !parent.mkdirs() && !parent.isDirectory()) {
/*  207 */         throw new IOException("Directory '" + parent + "' could not be created");
/*      */       }
/*      */     } 
/*      */     
/*  211 */     return new FileOutputStream(file, append);
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
/*      */   public static boolean contentEquals(File file1, File file2) throws IOException {
/*  231 */     boolean file1Exists = file1.exists();
/*  232 */     if (file1Exists != file2.exists()) {
/*  233 */       return false;
/*      */     }
/*      */     
/*  236 */     if (!file1Exists)
/*      */     {
/*  238 */       return true;
/*      */     }
/*      */     
/*  241 */     if (file1.isDirectory() || file2.isDirectory())
/*      */     {
/*  243 */       throw new IOException("Can't compare directories, only files");
/*      */     }
/*      */     
/*  246 */     if (file1.length() != file2.length())
/*      */     {
/*  248 */       return false;
/*      */     }
/*      */     
/*  251 */     if (file1.getCanonicalFile().equals(file2.getCanonicalFile()))
/*      */     {
/*  253 */       return true;
/*      */     }
/*      */     
/*  256 */     InputStream input1 = null;
/*  257 */     InputStream input2 = null;
/*      */     try {
/*  259 */       input1 = new FileInputStream(file1);
/*  260 */       input2 = new FileInputStream(file2);
/*  261 */       return IOUtils.contentEquals(input1, input2);
/*      */     } finally {
/*      */       
/*  264 */       IOUtils.closeQuietly(input1);
/*  265 */       IOUtils.closeQuietly(input2);
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
/*      */   public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
/*  292 */     copyFileToDirectory(srcFile, destDir, true);
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
/*      */   public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
/*  321 */     if (destDir == null) {
/*  322 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/*  324 */     if (destDir.exists() && !destDir.isDirectory()) {
/*  325 */       throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
/*      */     }
/*  327 */     File destFile = new File(destDir, srcFile.getName());
/*  328 */     copyFile(srcFile, destFile, preserveFileDate);
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
/*      */   public static void copyFile(File srcFile, File destFile) throws IOException {
/*  353 */     copyFile(srcFile, destFile, true);
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
/*      */   public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/*  382 */     if (srcFile == null) {
/*  383 */       throw new NullPointerException("Source must not be null");
/*      */     }
/*  385 */     if (destFile == null) {
/*  386 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/*  388 */     if (!srcFile.exists()) {
/*  389 */       throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
/*      */     }
/*  391 */     if (srcFile.isDirectory()) {
/*  392 */       throw new IOException("Source '" + srcFile + "' exists but is a directory");
/*      */     }
/*  394 */     if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
/*  395 */       throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
/*      */     }
/*  397 */     File parentFile = destFile.getParentFile();
/*  398 */     if (parentFile != null && 
/*  399 */       !parentFile.mkdirs() && !parentFile.isDirectory()) {
/*  400 */       throw new IOException("Destination '" + parentFile + "' directory cannot be created");
/*      */     }
/*      */     
/*  403 */     if (destFile.exists() && !destFile.canWrite()) {
/*  404 */       throw new IOException("Destination '" + destFile + "' exists but is read-only");
/*      */     }
/*  406 */     doCopyFile(srcFile, destFile, preserveFileDate);
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
/*      */   private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
/*  418 */     if (destFile.exists() && destFile.isDirectory()) {
/*  419 */       throw new IOException("Destination '" + destFile + "' exists but is a directory");
/*      */     }
/*      */     
/*  422 */     FileInputStream fis = null;
/*  423 */     FileOutputStream fos = null;
/*  424 */     FileChannel input = null;
/*  425 */     FileChannel output = null;
/*      */     try {
/*  427 */       fis = new FileInputStream(srcFile);
/*  428 */       fos = new FileOutputStream(destFile);
/*  429 */       input = fis.getChannel();
/*  430 */       output = fos.getChannel();
/*  431 */       long size = input.size();
/*  432 */       long pos = 0L;
/*  433 */       long count = 0L;
/*  434 */       while (pos < size) {
/*  435 */         count = (size - pos > 31457280L) ? 31457280L : (size - pos);
/*  436 */         pos += output.transferFrom(input, pos, count);
/*      */       } 
/*      */     } finally {
/*  439 */       IOUtils.closeQuietly(output);
/*  440 */       IOUtils.closeQuietly(fos);
/*  441 */       IOUtils.closeQuietly(input);
/*  442 */       IOUtils.closeQuietly(fis);
/*      */     } 
/*      */     
/*  445 */     if (srcFile.length() != destFile.length()) {
/*  446 */       throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
/*      */     }
/*      */     
/*  449 */     if (preserveFileDate) {
/*  450 */       destFile.setLastModified(srcFile.lastModified());
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
/*      */   public static void copyDirectory(File srcDir, File destDir) throws IOException {
/*  479 */     copyDirectory(srcDir, destDir, true);
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
/*      */   public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
/*  510 */     copyDirectory(srcDir, destDir, null, preserveFileDate);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) throws IOException {
/*  561 */     if (srcDir == null) {
/*  562 */       throw new NullPointerException("Source must not be null");
/*      */     }
/*  564 */     if (destDir == null) {
/*  565 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/*  567 */     if (!srcDir.exists()) {
/*  568 */       throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
/*      */     }
/*  570 */     if (!srcDir.isDirectory()) {
/*  571 */       throw new IOException("Source '" + srcDir + "' exists but is not a directory");
/*      */     }
/*  573 */     if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
/*  574 */       throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
/*      */     }
/*      */ 
/*      */     
/*  578 */     List<String> exclusionList = null;
/*  579 */     if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
/*  580 */       File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/*  581 */       if (srcFiles != null && srcFiles.length > 0) {
/*  582 */         exclusionList = new ArrayList<String>(srcFiles.length);
/*  583 */         for (File srcFile : srcFiles) {
/*  584 */           File copiedFile = new File(destDir, srcFile.getName());
/*  585 */           exclusionList.add(copiedFile.getCanonicalPath());
/*      */         } 
/*      */       } 
/*      */     } 
/*  589 */     doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
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
/*      */   private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList) throws IOException {
/*  606 */     File[] srcFiles = (filter == null) ? srcDir.listFiles() : srcDir.listFiles(filter);
/*  607 */     if (srcFiles == null) {
/*  608 */       throw new IOException("Failed to list contents of " + srcDir);
/*      */     }
/*  610 */     if (destDir.exists()) {
/*  611 */       if (!destDir.isDirectory()) {
/*  612 */         throw new IOException("Destination '" + destDir + "' exists but is not a directory");
/*      */       }
/*      */     }
/*  615 */     else if (!destDir.mkdirs() && !destDir.isDirectory()) {
/*  616 */       throw new IOException("Destination '" + destDir + "' directory cannot be created");
/*      */     } 
/*      */     
/*  619 */     if (!destDir.canWrite()) {
/*  620 */       throw new IOException("Destination '" + destDir + "' cannot be written to");
/*      */     }
/*  622 */     for (File srcFile : srcFiles) {
/*  623 */       File dstFile = new File(destDir, srcFile.getName());
/*  624 */       if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
/*  625 */         if (srcFile.isDirectory()) {
/*  626 */           doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
/*      */         } else {
/*  628 */           doCopyFile(srcFile, dstFile, preserveFileDate);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  634 */     if (preserveFileDate) {
/*  635 */       destDir.setLastModified(srcDir.lastModified());
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
/*      */   public static void deleteDirectory(File directory) throws IOException {
/*  647 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/*  651 */     if (!isSymlink(directory)) {
/*  652 */       cleanDirectory(directory);
/*      */     }
/*      */     
/*  655 */     if (!directory.delete()) {
/*  656 */       String message = "Unable to delete directory " + directory + ".";
/*      */       
/*  658 */       throw new IOException(message);
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
/*      */   public static boolean deleteQuietly(File file) {
/*  678 */     if (file == null) {
/*  679 */       return false;
/*      */     }
/*      */     try {
/*  682 */       if (file.isDirectory()) {
/*  683 */         cleanDirectory(file);
/*      */       }
/*  685 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/*      */     try {
/*  689 */       return file.delete();
/*  690 */     } catch (Exception ignored) {
/*  691 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void cleanDirectory(File directory) throws IOException {
/*  702 */     if (!directory.exists()) {
/*  703 */       String message = directory + " does not exist";
/*  704 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  707 */     if (!directory.isDirectory()) {
/*  708 */       String message = directory + " is not a directory";
/*  709 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  712 */     File[] files = directory.listFiles();
/*  713 */     if (files == null) {
/*  714 */       throw new IOException("Failed to list contents of " + directory);
/*      */     }
/*      */     
/*  717 */     IOException exception = null;
/*  718 */     for (File file : files) {
/*      */       try {
/*  720 */         forceDelete(file);
/*  721 */       } catch (IOException ioe) {
/*  722 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/*  726 */     if (null != exception) {
/*  727 */       throw exception;
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
/*      */   public static String readFileToString(File file, String encoding) throws IOException {
/*  743 */     InputStream in = null;
/*      */     try {
/*  745 */       in = openInputStream(file);
/*  746 */       return IOUtils.toString(in, encoding);
/*      */     } finally {
/*  748 */       IOUtils.closeQuietly(in);
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
/*      */   public static String readFileToString(File file) throws IOException {
/*  763 */     return readFileToString(file, null);
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
/*      */   public static void forceDelete(File file) throws IOException {
/*  783 */     if (file.isDirectory()) {
/*  784 */       deleteDirectory(file);
/*      */     } else {
/*  786 */       boolean filePresent = file.exists();
/*  787 */       if (!file.delete()) {
/*  788 */         if (!filePresent) {
/*  789 */           throw new FileNotFoundException("File does not exist: " + file);
/*      */         }
/*  791 */         String message = "Unable to delete file: " + file;
/*      */         
/*  793 */         throw new IOException(message);
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
/*      */   public static void forceDeleteOnExit(File file) throws IOException {
/*  807 */     if (file.isDirectory()) {
/*  808 */       deleteDirectoryOnExit(file);
/*      */     } else {
/*  810 */       file.deleteOnExit();
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
/*      */   private static void deleteDirectoryOnExit(File directory) throws IOException {
/*  822 */     if (!directory.exists()) {
/*      */       return;
/*      */     }
/*      */     
/*  826 */     directory.deleteOnExit();
/*  827 */     if (!isSymlink(directory)) {
/*  828 */       cleanDirectoryOnExit(directory);
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
/*      */   private static void cleanDirectoryOnExit(File directory) throws IOException {
/*  840 */     if (!directory.exists()) {
/*  841 */       String message = directory + " does not exist";
/*  842 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  845 */     if (!directory.isDirectory()) {
/*  846 */       String message = directory + " is not a directory";
/*  847 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  850 */     File[] files = directory.listFiles();
/*  851 */     if (files == null) {
/*  852 */       throw new IOException("Failed to list contents of " + directory);
/*      */     }
/*      */     
/*  855 */     IOException exception = null;
/*  856 */     for (File file : files) {
/*      */       try {
/*  858 */         forceDeleteOnExit(file);
/*  859 */       } catch (IOException ioe) {
/*  860 */         exception = ioe;
/*      */       } 
/*      */     } 
/*      */     
/*  864 */     if (null != exception) {
/*  865 */       throw exception;
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
/*      */   public static void forceMkdir(File directory) throws IOException {
/*  881 */     if (directory.exists()) {
/*  882 */       if (!directory.isDirectory()) {
/*  883 */         String message = "File " + directory + " exists and is not a directory. Unable to create directory.";
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  888 */         throw new IOException(message);
/*      */       }
/*      */     
/*  891 */     } else if (!directory.mkdirs()) {
/*      */ 
/*      */       
/*  894 */       if (!directory.isDirectory()) {
/*      */         
/*  896 */         String message = "Unable to create directory " + directory;
/*      */         
/*  898 */         throw new IOException(message);
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
/*      */   public static long sizeOf(File file) {
/*  925 */     if (!file.exists()) {
/*  926 */       String message = file + " does not exist";
/*  927 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  930 */     if (file.isDirectory()) {
/*  931 */       return sizeOfDirectory(file);
/*      */     }
/*  933 */     return file.length();
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
/*      */   public static long sizeOfDirectory(File directory) {
/*  946 */     if (!directory.exists()) {
/*  947 */       String message = directory + " does not exist";
/*  948 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  951 */     if (!directory.isDirectory()) {
/*  952 */       String message = directory + " is not a directory";
/*  953 */       throw new IllegalArgumentException(message);
/*      */     } 
/*      */     
/*  956 */     long size = 0L;
/*      */     
/*  958 */     File[] files = directory.listFiles();
/*  959 */     if (files == null) {
/*  960 */       return 0L;
/*      */     }
/*  962 */     for (File file : files) {
/*  963 */       size += sizeOf(file);
/*      */     }
/*      */     
/*  966 */     return size;
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
/*      */   public static void moveDirectory(File srcDir, File destDir) throws IOException {
/*  983 */     if (srcDir == null) {
/*  984 */       throw new NullPointerException("Source must not be null");
/*      */     }
/*  986 */     if (destDir == null) {
/*  987 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/*  989 */     if (!srcDir.exists()) {
/*  990 */       throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
/*      */     }
/*  992 */     if (!srcDir.isDirectory()) {
/*  993 */       throw new IOException("Source '" + srcDir + "' is not a directory");
/*      */     }
/*  995 */     if (destDir.exists()) {
/*  996 */       throw new FileExistsException("Destination '" + destDir + "' already exists");
/*      */     }
/*  998 */     boolean rename = srcDir.renameTo(destDir);
/*  999 */     if (!rename) {
/* 1000 */       if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
/* 1001 */         throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
/*      */       }
/* 1003 */       copyDirectory(srcDir, destDir);
/* 1004 */       deleteDirectory(srcDir);
/* 1005 */       if (srcDir.exists()) {
/* 1006 */         throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
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
/*      */   public static void moveFile(File srcFile, File destFile) throws IOException {
/* 1026 */     if (srcFile == null) {
/* 1027 */       throw new NullPointerException("Source must not be null");
/*      */     }
/* 1029 */     if (destFile == null) {
/* 1030 */       throw new NullPointerException("Destination must not be null");
/*      */     }
/* 1032 */     if (!srcFile.exists()) {
/* 1033 */       throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
/*      */     }
/* 1035 */     if (srcFile.isDirectory()) {
/* 1036 */       throw new IOException("Source '" + srcFile + "' is a directory");
/*      */     }
/* 1038 */     if (destFile.exists()) {
/* 1039 */       throw new FileExistsException("Destination '" + destFile + "' already exists");
/*      */     }
/* 1041 */     if (destFile.isDirectory()) {
/* 1042 */       throw new IOException("Destination '" + destFile + "' is a directory");
/*      */     }
/* 1044 */     boolean rename = srcFile.renameTo(destFile);
/* 1045 */     if (!rename) {
/* 1046 */       copyFile(srcFile, destFile);
/* 1047 */       if (!srcFile.delete()) {
/* 1048 */         FileUtils.deleteQuietly(destFile);
/* 1049 */         throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
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
/*      */   public static boolean isSymlink(File file) throws IOException {
/* 1070 */     if (file == null) {
/* 1071 */       throw new NullPointerException("File must not be null");
/*      */     }
/* 1073 */     if (FilenameUtils.isSystemWindows()) {
/* 1074 */       return false;
/*      */     }
/* 1076 */     File fileInCanonicalDir = null;
/* 1077 */     if (file.getParent() == null) {
/* 1078 */       fileInCanonicalDir = file;
/*      */     } else {
/* 1080 */       File canonicalDir = file.getParentFile().getCanonicalFile();
/* 1081 */       fileInCanonicalDir = new File(canonicalDir, file.getName());
/*      */     } 
/*      */     
/* 1084 */     if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
/* 1085 */       return false;
/*      */     }
/* 1087 */     return true;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/commons/FileUtilsV2_2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */