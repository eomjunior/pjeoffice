/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.graph.Traverser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.DirectoryIteratorException;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SecureDirectoryStream;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class MoreFiles
/*     */ {
/*     */   public static ByteSource asByteSource(Path path, OpenOption... options) {
/*  86 */     return new PathByteSource(path, options);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class PathByteSource
/*     */     extends ByteSource
/*     */   {
/*  93 */     private static final LinkOption[] FOLLOW_LINKS = new LinkOption[0];
/*     */     
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     private final boolean followLinks;
/*     */     
/*     */     private PathByteSource(Path path, OpenOption... options) {
/* 100 */       this.path = (Path)Preconditions.checkNotNull(path);
/* 101 */       this.options = (OpenOption[])options.clone();
/* 102 */       this.followLinks = followLinks(this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean followLinks(OpenOption[] options) {
/* 107 */       for (OpenOption option : options) {
/* 108 */         if (option == LinkOption.NOFOLLOW_LINKS) {
/* 109 */           return false;
/*     */         }
/*     */       } 
/* 112 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 117 */       return Files.newInputStream(this.path, this.options);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicFileAttributes readAttributes() throws IOException {
/* 124 */       (new LinkOption[1])[0] = LinkOption.NOFOLLOW_LINKS; return Files.readAttributes(this.path, BasicFileAttributes.class, this.followLinks ? FOLLOW_LINKS : new LinkOption[1]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> sizeIfKnown() {
/*     */       BasicFileAttributes attrs;
/*     */       try {
/* 131 */         attrs = readAttributes();
/* 132 */       } catch (IOException e) {
/*     */         
/* 134 */         return Optional.absent();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 139 */       if (attrs.isDirectory() || attrs.isSymbolicLink()) {
/* 140 */         return Optional.absent();
/*     */       }
/*     */       
/* 143 */       return Optional.of(Long.valueOf(attrs.size()));
/*     */     }
/*     */ 
/*     */     
/*     */     public long size() throws IOException {
/* 148 */       BasicFileAttributes attrs = readAttributes();
/*     */ 
/*     */ 
/*     */       
/* 152 */       if (attrs.isDirectory())
/* 153 */         throw new IOException("can't read: is a directory"); 
/* 154 */       if (attrs.isSymbolicLink()) {
/* 155 */         throw new IOException("can't read: is a symbolic link");
/*     */       }
/*     */       
/* 158 */       return attrs.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] read() throws IOException {
/* 163 */       SeekableByteChannel channel = Files.newByteChannel(this.path, this.options); 
/* 164 */       try { byte[] arrayOfByte = ByteStreams.toByteArray(Channels.newInputStream(channel), channel.size());
/* 165 */         if (channel != null) channel.close();  return arrayOfByte; }
/*     */       catch (Throwable throwable) { if (channel != null)
/*     */           try { channel.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/* 170 */        } public CharSource asCharSource(Charset charset) { if (this.options.length == 0)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 175 */         return new ByteSource.AsCharSource(charset)
/*     */           {
/*     */             public Stream<String> lines() throws IOException
/*     */             {
/* 179 */               return Files.lines(MoreFiles.PathByteSource.this.path, this.charset);
/*     */             }
/*     */           };
/*     */       }
/*     */       
/* 184 */       return super.asCharSource(charset); }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 189 */       return "MoreFiles.asByteSource(" + this.path + ", " + Arrays.toString((Object[])this.options) + ")";
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
/*     */   public static ByteSink asByteSink(Path path, OpenOption... options) {
/* 204 */     return new PathByteSink(path, options);
/*     */   }
/*     */   
/*     */   private static final class PathByteSink
/*     */     extends ByteSink {
/*     */     private final Path path;
/*     */     private final OpenOption[] options;
/*     */     
/*     */     private PathByteSink(Path path, OpenOption... options) {
/* 213 */       this.path = (Path)Preconditions.checkNotNull(path);
/* 214 */       this.options = (OpenOption[])options.clone();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputStream openStream() throws IOException {
/* 220 */       return Files.newOutputStream(this.path, this.options);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 225 */       return "MoreFiles.asByteSink(" + this.path + ", " + Arrays.toString((Object[])this.options) + ")";
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
/*     */   public static CharSource asCharSource(Path path, Charset charset, OpenOption... options) {
/* 239 */     return asByteSource(path, options).asCharSource(charset);
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
/*     */   public static CharSink asCharSink(Path path, Charset charset, OpenOption... options) {
/* 253 */     return asByteSink(path, options).asCharSink(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImmutableList<Path> listFiles(Path dir) throws IOException {
/*     */     
/* 265 */     try { DirectoryStream<Path> stream = Files.newDirectoryStream(dir); 
/* 266 */       try { ImmutableList<Path> immutableList = ImmutableList.copyOf(stream);
/* 267 */         if (stream != null) stream.close();  return immutableList; } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (DirectoryIteratorException e)
/* 268 */     { throw e.getCause(); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Traverser<Path> fileTraverser() {
/* 296 */     return Traverser.forTree(MoreFiles::fileTreeChildren);
/*     */   }
/*     */   
/*     */   private static Iterable<Path> fileTreeChildren(Path dir) {
/* 300 */     if (Files.isDirectory(dir, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/*     */       try {
/* 302 */         return (Iterable<Path>)listFiles(dir);
/* 303 */       } catch (IOException e) {
/*     */         
/* 305 */         throw new DirectoryIteratorException(e);
/*     */       } 
/*     */     }
/* 308 */     return (Iterable<Path>)ImmutableList.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isDirectory(LinkOption... options) {
/* 316 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 317 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 320 */           return Files.isDirectory(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 325 */           return "MoreFiles.isDirectory(" + Arrays.toString((Object[])optionsCopy) + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDirectory(SecureDirectoryStream<Path> dir, Path name, LinkOption... options) throws IOException {
/* 333 */     return ((BasicFileAttributeView)dir.<BasicFileAttributeView>getFileAttributeView(name, BasicFileAttributeView.class, options))
/* 334 */       .readAttributes()
/* 335 */       .isDirectory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Path> isRegularFile(LinkOption... options) {
/* 343 */     final LinkOption[] optionsCopy = (LinkOption[])options.clone();
/* 344 */     return new Predicate<Path>()
/*     */       {
/*     */         public boolean apply(Path input) {
/* 347 */           return Files.isRegularFile(input, optionsCopy);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 352 */           return "MoreFiles.isRegularFile(" + Arrays.toString((Object[])optionsCopy) + ")";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equal(Path path1, Path path2) throws IOException {
/* 365 */     Preconditions.checkNotNull(path1);
/* 366 */     Preconditions.checkNotNull(path2);
/* 367 */     if (Files.isSameFile(path1, path2)) {
/* 368 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     ByteSource source1 = asByteSource(path1, new OpenOption[0]);
/* 377 */     ByteSource source2 = asByteSource(path2, new OpenOption[0]);
/* 378 */     long len1 = ((Long)source1.sizeIfKnown().or(Long.valueOf(0L))).longValue();
/* 379 */     long len2 = ((Long)source2.sizeIfKnown().or(Long.valueOf(0L))).longValue();
/* 380 */     if (len1 != 0L && len2 != 0L && len1 != len2) {
/* 381 */       return false;
/*     */     }
/* 383 */     return source1.contentEquals(source2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void touch(Path path) throws IOException {
/* 392 */     Preconditions.checkNotNull(path);
/*     */     
/*     */     try {
/* 395 */       Files.setLastModifiedTime(path, FileTime.fromMillis(System.currentTimeMillis()));
/* 396 */     } catch (NoSuchFileException e) {
/*     */       try {
/* 398 */         Files.createFile(path, (FileAttribute<?>[])new FileAttribute[0]);
/* 399 */       } catch (FileAlreadyExistsException fileAlreadyExistsException) {}
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
/*     */   public static void createParentDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
/* 424 */     Path normalizedAbsolutePath = path.toAbsolutePath().normalize();
/* 425 */     Path parent = normalizedAbsolutePath.getParent();
/* 426 */     if (parent == null) {
/*     */       return;
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
/* 438 */     if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 439 */       Files.createDirectories(parent, attrs);
/* 440 */       if (!Files.isDirectory(parent, new LinkOption[0])) {
/* 441 */         throw new IOException("Unable to create parent directories of " + path);
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
/*     */   public static String getFileExtension(Path path) {
/* 459 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 462 */     if (name == null) {
/* 463 */       return "";
/*     */     }
/*     */     
/* 466 */     String fileName = name.toString();
/* 467 */     int dotIndex = fileName.lastIndexOf('.');
/* 468 */     return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNameWithoutExtension(Path path) {
/* 477 */     Path name = path.getFileName();
/*     */ 
/*     */     
/* 480 */     if (name == null) {
/* 481 */       return "";
/*     */     }
/*     */     
/* 484 */     String fileName = name.toString();
/* 485 */     int dotIndex = fileName.lastIndexOf('.');
/* 486 */     return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
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
/*     */   public static void deleteRecursively(Path path, RecursiveDeleteOption... options) throws IOException {
/* 519 */     Path parentPath = getParentPath(path);
/* 520 */     if (parentPath == null) {
/* 521 */       throw new FileSystemException(path.toString(), null, "can't delete recursively");
/*     */     }
/*     */     
/* 524 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 526 */       boolean sdsSupported = false;
/* 527 */       DirectoryStream<Path> parent = Files.newDirectoryStream(parentPath); 
/* 528 */       try { if (parent instanceof SecureDirectoryStream) {
/* 529 */           sdsSupported = true;
/*     */           
/* 531 */           exceptions = deleteRecursivelySecure((SecureDirectoryStream<Path>)parent, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 537 */               Objects.<Path>requireNonNull(path.getFileName()));
/*     */         } 
/* 539 */         if (parent != null) parent.close();  } catch (Throwable throwable) { if (parent != null)
/*     */           try { parent.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 541 */        if (!sdsSupported) {
/* 542 */         checkAllowsInsecure(path, options);
/* 543 */         exceptions = deleteRecursivelyInsecure(path);
/*     */       } 
/* 545 */     } catch (IOException e) {
/* 546 */       if (exceptions == null) {
/* 547 */         throw e;
/*     */       }
/* 549 */       exceptions.add(e);
/*     */     } 
/*     */ 
/*     */     
/* 553 */     if (exceptions != null) {
/* 554 */       throwDeleteFailed(path, exceptions);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void deleteDirectoryContents(Path path, RecursiveDeleteOption... options) throws IOException {
/* 591 */     Collection<IOException> exceptions = null; 
/* 592 */     try { DirectoryStream<Path> stream = Files.newDirectoryStream(path); 
/* 593 */       try { if (stream instanceof SecureDirectoryStream) {
/* 594 */           SecureDirectoryStream<Path> sds = (SecureDirectoryStream<Path>)stream;
/* 595 */           exceptions = deleteDirectoryContentsSecure(sds);
/*     */         } else {
/* 597 */           checkAllowsInsecure(path, options);
/* 598 */           exceptions = deleteDirectoryContentsInsecure(stream);
/*     */         } 
/* 600 */         if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 601 */     { if (exceptions == null) {
/* 602 */         throw e;
/*     */       }
/* 604 */       exceptions.add(e); }
/*     */ 
/*     */ 
/*     */     
/* 608 */     if (exceptions != null) {
/* 609 */       throwDeleteFailed(path, exceptions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Collection<IOException> deleteRecursivelySecure(SecureDirectoryStream<Path> dir, Path path) {
/* 620 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 622 */       if (isDirectory(dir, path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 623 */         SecureDirectoryStream<Path> childDir = dir.newDirectoryStream(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }); 
/* 624 */         try { exceptions = deleteDirectoryContentsSecure(childDir);
/* 625 */           if (childDir != null) childDir.close();  } catch (Throwable throwable) { if (childDir != null)
/*     */             try { childDir.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/* 629 */          if (exceptions == null) {
/* 630 */           dir.deleteDirectory(path);
/*     */         }
/*     */       } else {
/* 633 */         dir.deleteFile(path);
/*     */       } 
/*     */       
/* 636 */       return exceptions;
/* 637 */     } catch (IOException e) {
/* 638 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Collection<IOException> deleteDirectoryContentsSecure(SecureDirectoryStream<Path> dir) {
/* 649 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 651 */       for (Path path : dir) {
/* 652 */         exceptions = concat(exceptions, deleteRecursivelySecure(dir, path.getFileName()));
/*     */       }
/*     */       
/* 655 */       return exceptions;
/* 656 */     } catch (DirectoryIteratorException e) {
/* 657 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Collection<IOException> deleteRecursivelyInsecure(Path path) {
/* 667 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 669 */       if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 670 */         DirectoryStream<Path> stream = Files.newDirectoryStream(path); 
/* 671 */         try { exceptions = deleteDirectoryContentsInsecure(stream);
/* 672 */           if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null)
/*     */             try { stream.close(); }
/*     */             catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */               throw throwable; }
/*     */       
/* 677 */       }  if (exceptions == null) {
/* 678 */         Files.delete(path);
/*     */       }
/*     */       
/* 681 */       return exceptions;
/* 682 */     } catch (IOException e) {
/* 683 */       return addException(exceptions, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Collection<IOException> deleteDirectoryContentsInsecure(DirectoryStream<Path> dir) {
/* 695 */     Collection<IOException> exceptions = null;
/*     */     try {
/* 697 */       for (Path entry : dir) {
/* 698 */         exceptions = concat(exceptions, deleteRecursivelyInsecure(entry));
/*     */       }
/*     */       
/* 701 */       return exceptions;
/* 702 */     } catch (DirectoryIteratorException e) {
/* 703 */       return addException(exceptions, e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Path getParentPath(Path path) {
/* 714 */     Path parent = path.getParent();
/*     */ 
/*     */     
/* 717 */     if (parent != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 723 */       return parent;
/*     */     }
/*     */ 
/*     */     
/* 727 */     if (path.getNameCount() == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 739 */       return null;
/*     */     }
/*     */     
/* 742 */     return path.getFileSystem().getPath(".", new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkAllowsInsecure(Path path, RecursiveDeleteOption[] options) throws InsecureRecursiveDeleteException {
/* 749 */     if (!Arrays.<RecursiveDeleteOption>asList(options).contains(RecursiveDeleteOption.ALLOW_INSECURE)) {
/* 750 */       throw new InsecureRecursiveDeleteException(path.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Collection<IOException> addException(@CheckForNull Collection<IOException> exceptions, IOException e) {
/* 760 */     if (exceptions == null) {
/* 761 */       exceptions = new ArrayList<>();
/*     */     }
/* 763 */     exceptions.add(e);
/* 764 */     return exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Collection<IOException> concat(@CheckForNull Collection<IOException> exceptions, @CheckForNull Collection<IOException> other) {
/* 776 */     if (exceptions == null)
/* 777 */       return other; 
/* 778 */     if (other != null) {
/* 779 */       exceptions.addAll(other);
/*     */     }
/* 781 */     return exceptions;
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
/*     */   private static void throwDeleteFailed(Path path, Collection<IOException> exceptions) throws FileSystemException {
/* 794 */     NoSuchFileException pathNotFound = pathNotFound(path, exceptions);
/* 795 */     if (pathNotFound != null) {
/* 796 */       throw pathNotFound;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 803 */     FileSystemException deleteFailed = new FileSystemException(path.toString(), null, "failed to delete one or more files; see suppressed exceptions for details");
/*     */ 
/*     */     
/* 806 */     for (IOException e : exceptions) {
/* 807 */       deleteFailed.addSuppressed(e);
/*     */     }
/* 809 */     throw deleteFailed;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static NoSuchFileException pathNotFound(Path path, Collection<IOException> exceptions) {
/* 814 */     if (exceptions.size() != 1) {
/* 815 */       return null;
/*     */     }
/* 817 */     IOException exception = (IOException)Iterables.getOnlyElement(exceptions);
/* 818 */     if (!(exception instanceof NoSuchFileException)) {
/* 819 */       return null;
/*     */     }
/* 821 */     NoSuchFileException noSuchFileException = (NoSuchFileException)exception;
/* 822 */     String exceptionFile = noSuchFileException.getFile();
/* 823 */     if (exceptionFile == null)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 828 */       return null;
/*     */     }
/* 830 */     Path parentPath = getParentPath(path);
/* 831 */     if (parentPath == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 851 */       return null;
/*     */     }
/*     */     
/* 854 */     Path pathResolvedFromParent = parentPath.resolve(Objects.<Path>requireNonNull(path.getFileName()));
/* 855 */     if (exceptionFile.equals(pathResolvedFromParent.toString())) {
/* 856 */       return noSuchFileException;
/*     */     }
/* 858 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/MoreFiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */