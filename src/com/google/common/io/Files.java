/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.graph.SuccessorsFunction;
/*     */ import com.google.common.graph.Traverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.InlineMe;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class Files
/*     */ {
/*     */   public static BufferedReader newReader(File file, Charset charset) throws FileNotFoundException {
/*  90 */     Preconditions.checkNotNull(file);
/*  91 */     Preconditions.checkNotNull(charset);
/*  92 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   public static BufferedWriter newWriter(File file, Charset charset) throws FileNotFoundException {
/* 108 */     Preconditions.checkNotNull(file);
/* 109 */     Preconditions.checkNotNull(charset);
/* 110 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteSource asByteSource(File file) {
/* 119 */     return new FileByteSource(file);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class FileByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 129 */       this.file = (File)Preconditions.checkNotNull(file);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileInputStream openStream() throws IOException {
/* 134 */       return new FileInputStream(this.file);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/* 139 */       if (this.file.isFile()) {
/* 140 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 142 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 148 */       if (!this.file.isFile()) {
/* 149 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 151 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 156 */       Closer closer = Closer.create();
/*     */       try {
/* 158 */         FileInputStream in = closer.<FileInputStream>register(openStream());
/* 159 */         return ByteStreams.toByteArray(in, in.getChannel().size());
/* 160 */       } catch (Throwable e) {
/* 161 */         throw closer.rethrow(e);
/*     */       } finally {
/* 163 */         closer.close();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 169 */       return "Files.asByteSource(" + this.file + ")";
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes) {
/* 182 */     return new FileByteSink(file, modes);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink
/*     */     extends ByteSink {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 191 */       this.file = (File)Preconditions.checkNotNull(file);
/* 192 */       this.modes = ImmutableSet.copyOf((Object[])modes);
/*     */     }
/*     */ 
/*     */     
/*     */     public FileOutputStream openStream() throws IOException {
/* 197 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 202 */       return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(File file, Charset charset) {
/* 213 */     return asByteSource(file).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes) {
/* 225 */     return asByteSink(file, modes).asCharSink(charset);
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
/*     */   public static byte[] toByteArray(File file) throws IOException {
/* 240 */     return asByteSource(file).read();
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
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "Files.asCharSource(file, charset).read()", imports = {"com.google.common.io.Files"})
/*     */   public static String toString(File file, Charset charset) throws IOException {
/* 258 */     return asCharSource(file, charset).read();
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
/*     */   public static void write(byte[] from, File to) throws IOException {
/* 272 */     asByteSink(to, new FileWriteMode[0]).write(from);
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
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "Files.asCharSink(to, charset).write(from)", imports = {"com.google.common.io.Files"})
/*     */   public static void write(CharSequence from, File to, Charset charset) throws IOException {
/* 290 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   public static void copy(File from, OutputStream to) throws IOException {
/* 304 */     asByteSource(from).copyTo(to);
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
/*     */   public static void copy(File from, File to) throws IOException {
/* 327 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 328 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "Files.asCharSource(from, charset).copyTo(to)", imports = {"com.google.common.io.Files"})
/*     */   public static void copy(File from, Charset charset, Appendable to) throws IOException {
/* 347 */     asCharSource(from, charset).copyTo(to);
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
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "Files.asCharSink(to, charset, FileWriteMode.APPEND).write(from)", imports = {"com.google.common.io.FileWriteMode", "com.google.common.io.Files"})
/*     */   public static void append(CharSequence from, File to, Charset charset) throws IOException {
/* 367 */     asCharSink(to, charset, new FileWriteMode[] { FileWriteMode.APPEND }).write(from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(File file1, File file2) throws IOException {
/* 376 */     Preconditions.checkNotNull(file1);
/* 377 */     Preconditions.checkNotNull(file2);
/* 378 */     if (file1 == file2 || file1.equals(file2)) {
/* 379 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 387 */     long len1 = file1.length();
/* 388 */     long len2 = file2.length();
/* 389 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 390 */       return false;
/*     */     }
/* 392 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static File createTempDir() {
/* 438 */     return TempFileCreator.INSTANCE.createTempDir();
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
/*     */   public static void touch(File file) throws IOException {
/* 450 */     Preconditions.checkNotNull(file);
/* 451 */     if (!file.createNewFile() && !file.setLastModified(System.currentTimeMillis())) {
/* 452 */       throw new IOException("Unable to update modification time of " + file);
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
/*     */   public static void createParentDirs(File file) throws IOException {
/* 466 */     Preconditions.checkNotNull(file);
/* 467 */     File parent = file.getCanonicalFile().getParentFile();
/* 468 */     if (parent == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 477 */     parent.mkdirs();
/* 478 */     if (!parent.isDirectory()) {
/* 479 */       throw new IOException("Unable to create parent directories of " + file);
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
/*     */   
/*     */   public static void move(File from, File to) throws IOException {
/* 496 */     Preconditions.checkNotNull(from);
/* 497 */     Preconditions.checkNotNull(to);
/* 498 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 500 */     if (!from.renameTo(to)) {
/* 501 */       copy(from, to);
/* 502 */       if (!from.delete()) {
/* 503 */         if (!to.delete()) {
/* 504 */           throw new IOException("Unable to delete " + to);
/*     */         }
/* 506 */         throw new IOException("Unable to delete " + from);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CheckForNull
/*     */   @InlineMe(replacement = "Files.asCharSource(file, charset).readFirstLine()", imports = {"com.google.common.io.Files"})
/*     */   public static String readFirstLine(File file, Charset charset) throws IOException {
/* 529 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   public static List<String> readLines(File file, Charset charset) throws IOException {
/* 551 */     return asCharSource(file, charset)
/* 552 */       .<List<String>>readLines(new LineProcessor<List<String>>()
/*     */         {
/* 554 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 558 */             this.result.add(line);
/* 559 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 564 */             return this.result;
/*     */           }
/*     */         });
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
/*     */   @Deprecated
/*     */   @ParametricNullness
/*     */   @InlineMe(replacement = "Files.asCharSource(file, charset).readLines(callback)", imports = {"com.google.common.io.Files"})
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback) throws IOException {
/* 590 */     return asCharSource(file, charset).readLines(callback);
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
/*     */   @Deprecated
/*     */   @ParametricNullness
/*     */   @InlineMe(replacement = "Files.asByteSource(file).read(processor)", imports = {"com.google.common.io.Files"})
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor) throws IOException {
/* 613 */     return asByteSource(file).read(processor);
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
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "Files.asByteSource(file).hash(hashFunction)", imports = {"com.google.common.io.Files"})
/*     */   public static HashCode hash(File file, HashFunction hashFunction) throws IOException {
/* 632 */     return asByteSource(file).hash(hashFunction);
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
/*     */   public static MappedByteBuffer map(File file) throws IOException {
/* 651 */     Preconditions.checkNotNull(file);
/* 652 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode) throws IOException {
/* 673 */     return mapInternal(file, mode, -1L);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 696 */     Preconditions.checkArgument((size >= 0L), "size (%s) may not be negative", size);
/* 697 */     return mapInternal(file, mode, size);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MappedByteBuffer mapInternal(File file, FileChannel.MapMode mode, long size) throws IOException {
/* 702 */     Preconditions.checkNotNull(file);
/* 703 */     Preconditions.checkNotNull(mode);
/*     */     
/* 705 */     Closer closer = Closer.create();
/*     */     
/*     */     try {
/* 708 */       RandomAccessFile raf = closer.<RandomAccessFile>register(new RandomAccessFile(file, (mode == FileChannel.MapMode.READ_ONLY) ? "r" : "rw"));
/* 709 */       FileChannel channel = closer.<FileChannel>register(raf.getChannel());
/* 710 */       return channel.map(mode, 0L, (size == -1L) ? channel.size() : size);
/* 711 */     } catch (Throwable e) {
/* 712 */       throw closer.rethrow(e);
/*     */     } finally {
/* 714 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simplifyPath(String pathname) {
/* 739 */     Preconditions.checkNotNull(pathname);
/* 740 */     if (pathname.length() == 0) {
/* 741 */       return ".";
/*     */     }
/*     */ 
/*     */     
/* 745 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 746 */     List<String> path = new ArrayList<>();
/*     */ 
/*     */     
/* 749 */     for (String component : components) {
/* 750 */       switch (component) {
/*     */         case ".":
/*     */           continue;
/*     */         case "..":
/* 754 */           if (path.size() > 0 && !((String)path.get(path.size() - 1)).equals("..")) {
/* 755 */             path.remove(path.size() - 1); continue;
/*     */           } 
/* 757 */           path.add("..");
/*     */           continue;
/*     */       } 
/*     */       
/* 761 */       path.add(component);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 767 */     String result = Joiner.on('/').join(path);
/* 768 */     if (pathname.charAt(0) == '/') {
/* 769 */       result = "/" + result;
/*     */     }
/*     */     
/* 772 */     while (result.startsWith("/../")) {
/* 773 */       result = result.substring(3);
/*     */     }
/* 775 */     if (result.equals("/..")) {
/* 776 */       result = "/";
/* 777 */     } else if ("".equals(result)) {
/* 778 */       result = ".";
/*     */     } 
/*     */     
/* 781 */     return result;
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
/*     */   public static String getFileExtension(String fullName) {
/* 799 */     Preconditions.checkNotNull(fullName);
/* 800 */     String fileName = (new File(fullName)).getName();
/* 801 */     int dotIndex = fileName.lastIndexOf('.');
/* 802 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
/*     */   public static String getNameWithoutExtension(String file) {
/* 816 */     Preconditions.checkNotNull(file);
/* 817 */     String fileName = (new File(file)).getName();
/* 818 */     int dotIndex = fileName.lastIndexOf('.');
/* 819 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static Traverser<File> fileTraverser() {
/* 845 */     return Traverser.forTree(FILE_TREE);
/*     */   }
/*     */   
/* 848 */   private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction<File>()
/*     */     {
/*     */       
/*     */       public Iterable<File> successors(File file)
/*     */       {
/* 853 */         if (file.isDirectory()) {
/* 854 */           File[] files = file.listFiles();
/* 855 */           if (files != null) {
/* 856 */             return Collections.unmodifiableList(Arrays.asList(files));
/*     */           }
/*     */         } 
/*     */         
/* 860 */         return (Iterable<File>)ImmutableList.of();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isDirectory() {
/* 870 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<File> isFile() {
/* 879 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private enum FilePredicate implements Predicate<File> {
/* 883 */     IS_DIRECTORY
/*     */     {
/*     */       public boolean apply(File file) {
/* 886 */         return file.isDirectory();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 891 */         return "Files.isDirectory()";
/*     */       }
/*     */     },
/*     */     
/* 895 */     IS_FILE
/*     */     {
/*     */       public boolean apply(File file) {
/* 898 */         return file.isFile();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 903 */         return "Files.isFile()";
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */