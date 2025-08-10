/*      */ package org.apache.tools.zip;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.file.Files;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.zip.CRC32;
/*      */ import java.util.zip.Deflater;
/*      */ import java.util.zip.ZipException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ZipOutputStream
/*      */   extends FilterOutputStream
/*      */ {
/*      */   private static final int BUFFER_SIZE = 512;
/*      */   private static final int LFH_SIG_OFFSET = 0;
/*      */   private static final int LFH_VERSION_NEEDED_OFFSET = 4;
/*      */   private static final int LFH_GPB_OFFSET = 6;
/*      */   private static final int LFH_METHOD_OFFSET = 8;
/*      */   private static final int LFH_TIME_OFFSET = 10;
/*      */   private static final int LFH_CRC_OFFSET = 14;
/*      */   private static final int LFH_COMPRESSED_SIZE_OFFSET = 18;
/*      */   private static final int LFH_ORIGINAL_SIZE_OFFSET = 22;
/*      */   private static final int LFH_FILENAME_LENGTH_OFFSET = 26;
/*      */   private static final int LFH_EXTRA_LENGTH_OFFSET = 28;
/*      */   private static final int LFH_FILENAME_OFFSET = 30;
/*      */   private static final int CFH_SIG_OFFSET = 0;
/*      */   private static final int CFH_VERSION_MADE_BY_OFFSET = 4;
/*      */   private static final int CFH_VERSION_NEEDED_OFFSET = 6;
/*      */   private static final int CFH_GPB_OFFSET = 8;
/*      */   private static final int CFH_METHOD_OFFSET = 10;
/*      */   private static final int CFH_TIME_OFFSET = 12;
/*      */   private static final int CFH_CRC_OFFSET = 16;
/*      */   private static final int CFH_COMPRESSED_SIZE_OFFSET = 20;
/*      */   private static final int CFH_ORIGINAL_SIZE_OFFSET = 24;
/*      */   private static final int CFH_FILENAME_LENGTH_OFFSET = 28;
/*      */   private static final int CFH_EXTRA_LENGTH_OFFSET = 30;
/*      */   private static final int CFH_COMMENT_LENGTH_OFFSET = 32;
/*      */   private static final int CFH_DISK_NUMBER_OFFSET = 34;
/*      */   private static final int CFH_INTERNAL_ATTRIBUTES_OFFSET = 36;
/*      */   private static final int CFH_EXTERNAL_ATTRIBUTES_OFFSET = 38;
/*      */   private static final int CFH_LFH_OFFSET = 42;
/*      */   private static final int CFH_FILENAME_OFFSET = 46;
/*      */   private boolean finished = false;
/*      */   private static final int DEFLATER_BLOCK_SIZE = 8192;
/*      */   public static final int DEFLATED = 8;
/*      */   public static final int DEFAULT_COMPRESSION = -1;
/*      */   public static final int STORED = 0;
/*  146 */   static final String DEFAULT_ENCODING = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static final int EFS_FLAG = 2048;
/*      */ 
/*      */ 
/*      */   
/*  156 */   private static final byte[] EMPTY = new byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CurrentEntry entry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  170 */   private String comment = "";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  177 */   private int level = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasCompressionLevelChanged = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  192 */   private int method = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  199 */   private final List<ZipEntry> entries = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  206 */   private final CRC32 crc = new CRC32();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  213 */   private long written = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  220 */   private long cdOffset = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  227 */   private long cdLength = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  234 */   private static final byte[] ZERO = new byte[] { 0, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  241 */   private static final byte[] LZERO = new byte[] { 0, 0, 0, 0 };
/*      */   
/*  243 */   private static final byte[] ONE = ZipLong.getBytes(1L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  250 */   private final Map<ZipEntry, Long> offsets = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  262 */   private String encoding = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  272 */   private ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(DEFAULT_ENCODING);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  280 */   protected final Deflater def = new Deflater(this.level, true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  292 */   protected byte[] buf = new byte[512];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final RandomAccessFile raf;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useUTF8Flag = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fallbackToUTF8 = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  317 */   private UnicodeExtraFieldPolicy createUnicodeExtraFields = UnicodeExtraFieldPolicy.NEVER;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasUsedZip64 = false;
/*      */ 
/*      */   
/*  324 */   private Zip64Mode zip64Mode = Zip64Mode.AsNeeded;
/*      */   
/*  326 */   private final Calendar calendarInstance = Calendar.getInstance();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  331 */   private final byte[] oneByte = new byte[1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipOutputStream(OutputStream out) {
/*  339 */     super(out);
/*  340 */     this.raf = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipOutputStream(File file) throws IOException {
/*  351 */     super(null);
/*  352 */     RandomAccessFile ranf = null;
/*      */     try {
/*  354 */       ranf = new RandomAccessFile(file, "rw");
/*  355 */       ranf.setLength(0L);
/*  356 */     } catch (IOException e) {
/*  357 */       if (ranf != null) {
/*      */         try {
/*  359 */           ranf.close();
/*  360 */         } catch (IOException iOException) {}
/*      */ 
/*      */         
/*  363 */         ranf = null;
/*      */       } 
/*  365 */       this.out = Files.newOutputStream(file.toPath(), new java.nio.file.OpenOption[0]);
/*      */     } 
/*  367 */     this.raf = ranf;
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
/*      */   public boolean isSeekable() {
/*  381 */     return (this.raf != null);
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
/*      */   public void setEncoding(String encoding) {
/*  395 */     this.encoding = encoding;
/*  396 */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*  397 */     if (this.useUTF8Flag && !ZipEncodingHelper.isUTF8(encoding)) {
/*  398 */       this.useUTF8Flag = false;
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
/*      */   public String getEncoding() {
/*  410 */     return this.encoding;
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
/*      */   public void setUseLanguageEncodingFlag(boolean b) {
/*  422 */     this.useUTF8Flag = (b && ZipEncodingHelper.isUTF8(this.encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy b) {
/*  433 */     this.createUnicodeExtraFields = b;
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
/*      */   public void setFallbackToUTF8(boolean b) {
/*  445 */     this.fallbackToUTF8 = b;
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
/*      */   public void setUseZip64(Zip64Mode mode) {
/*  494 */     this.zip64Mode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void finish() throws IOException {
/*  504 */     if (this.finished) {
/*  505 */       throw new IOException("This archive has already been finished");
/*      */     }
/*      */     
/*  508 */     if (this.entry != null) {
/*  509 */       closeEntry();
/*      */     }
/*      */     
/*  512 */     this.cdOffset = this.written;
/*  513 */     writeCentralDirectoryInChunks();
/*  514 */     this.cdLength = this.written - this.cdOffset;
/*  515 */     writeZip64CentralDirectory();
/*  516 */     writeCentralDirectoryEnd();
/*  517 */     this.offsets.clear();
/*  518 */     this.entries.clear();
/*  519 */     this.def.end();
/*  520 */     this.finished = true;
/*      */   }
/*      */   
/*      */   private void writeCentralDirectoryInChunks() throws IOException {
/*  524 */     int NUM_PER_WRITE = 1000;
/*  525 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(70000);
/*  526 */     int count = 0;
/*  527 */     for (ZipEntry ze : this.entries) {
/*  528 */       byteArrayOutputStream.write(createCentralFileHeader(ze));
/*  529 */       if (++count > 1000) {
/*  530 */         writeCounted(byteArrayOutputStream.toByteArray());
/*  531 */         byteArrayOutputStream.reset();
/*  532 */         count = 0;
/*      */       } 
/*      */     } 
/*  535 */     writeCounted(byteArrayOutputStream.toByteArray());
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
/*      */   public void closeEntry() throws IOException {
/*  548 */     preClose();
/*      */     
/*  550 */     flushDeflater();
/*      */     
/*  552 */     Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
/*  553 */     long bytesWritten = this.written - this.entry.dataStart;
/*  554 */     long realCrc = this.crc.getValue();
/*  555 */     this.crc.reset();
/*      */ 
/*      */     
/*  558 */     boolean actuallyNeedsZip64 = handleSizesAndCrc(bytesWritten, realCrc, effectiveMode);
/*      */     
/*  560 */     closeEntry(actuallyNeedsZip64);
/*      */   }
/*      */   
/*      */   private void closeEntry(boolean actuallyNeedsZip64) throws IOException {
/*  564 */     if (this.raf != null) {
/*  565 */       rewriteSizesAndCrc(actuallyNeedsZip64);
/*      */     }
/*      */     
/*  568 */     writeDataDescriptor(this.entry.entry);
/*  569 */     this.entry = null;
/*      */   }
/*      */   
/*      */   private void preClose() throws IOException {
/*  573 */     if (this.finished) {
/*  574 */       throw new IOException("Stream has already been finished");
/*      */     }
/*      */     
/*  577 */     if (this.entry == null) {
/*  578 */       throw new IOException("No current entry to close");
/*      */     }
/*      */     
/*  581 */     if (!this.entry.hasWritten) {
/*  582 */       write(EMPTY, 0, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void flushDeflater() throws IOException {
/*  590 */     if (this.entry.entry.getMethod() == 8) {
/*  591 */       this.def.finish();
/*  592 */       while (!this.def.finished()) {
/*  593 */         deflate();
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
/*      */   private boolean handleSizesAndCrc(long bytesWritten, long crc, Zip64Mode effectiveMode) throws ZipException {
/*  613 */     if (this.entry.entry.getMethod() == 8) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  618 */       this.entry.entry.setSize(this.entry.bytesRead);
/*  619 */       this.entry.entry.setCompressedSize(bytesWritten);
/*  620 */       this.entry.entry.setCrc(crc);
/*      */       
/*  622 */       this.def.reset();
/*  623 */     } else if (this.raf == null) {
/*  624 */       if (this.entry.entry.getCrc() != crc) {
/*  625 */         throw new ZipException("bad CRC checksum for entry " + this.entry
/*  626 */             .entry.getName() + ": " + 
/*  627 */             Long.toHexString(this.entry.entry.getCrc()) + " instead of " + 
/*      */             
/*  629 */             Long.toHexString(crc));
/*      */       }
/*      */       
/*  632 */       if (this.entry.entry.getSize() != bytesWritten) {
/*  633 */         throw new ZipException("bad size for entry " + this.entry
/*  634 */             .entry.getName() + ": " + this.entry
/*  635 */             .entry.getSize() + " instead of " + bytesWritten);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/*  640 */       this.entry.entry.setSize(bytesWritten);
/*  641 */       this.entry.entry.setCompressedSize(bytesWritten);
/*  642 */       this.entry.entry.setCrc(crc);
/*      */     } 
/*      */     
/*  645 */     return checkIfNeedsZip64(effectiveMode);
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
/*      */   private boolean checkIfNeedsZip64(Zip64Mode effectiveMode) throws ZipException {
/*  660 */     boolean actuallyNeedsZip64 = isZip64Required(this.entry.entry, effectiveMode);
/*      */     
/*  662 */     if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never) {
/*  663 */       throw new Zip64RequiredException(
/*  664 */           Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
/*      */     }
/*  666 */     return actuallyNeedsZip64;
/*      */   }
/*      */   
/*      */   private boolean isZip64Required(ZipEntry entry1, Zip64Mode requestedMode) {
/*  670 */     return (requestedMode == Zip64Mode.Always || isTooLageForZip32(entry1));
/*      */   }
/*      */   
/*      */   private boolean isTooLageForZip32(ZipEntry zipArchiveEntry) {
/*  674 */     return (zipArchiveEntry.getSize() >= 4294967295L || zipArchiveEntry
/*  675 */       .getCompressedSize() >= 4294967295L);
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
/*      */   private void rewriteSizesAndCrc(boolean actuallyNeedsZip64) throws IOException {
/*  687 */     long save = this.raf.getFilePointer();
/*      */     
/*  689 */     this.raf.seek(this.entry.localDataStart);
/*  690 */     writeOut(ZipLong.getBytes(this.entry.entry.getCrc()));
/*  691 */     if (!hasZip64Extra(this.entry.entry) || !actuallyNeedsZip64) {
/*  692 */       writeOut(ZipLong.getBytes(this.entry.entry.getCompressedSize()));
/*  693 */       writeOut(ZipLong.getBytes(this.entry.entry.getSize()));
/*      */     } else {
/*  695 */       writeOut(ZipLong.ZIP64_MAGIC.getBytes());
/*  696 */       writeOut(ZipLong.ZIP64_MAGIC.getBytes());
/*      */     } 
/*      */     
/*  699 */     if (hasZip64Extra(this.entry.entry)) {
/*      */       
/*  701 */       this.raf.seek(this.entry.localDataStart + 12L + 4L + 
/*  702 */           getName(this.entry.entry).limit() + 4L);
/*      */ 
/*      */       
/*  705 */       writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getSize()));
/*  706 */       writeOut(ZipEightByteInteger.getBytes(this.entry.entry.getCompressedSize()));
/*      */       
/*  708 */       if (!actuallyNeedsZip64) {
/*      */ 
/*      */         
/*  711 */         this.raf.seek(this.entry.localDataStart - 10L);
/*  712 */         writeOut(ZipShort.getBytes(10));
/*      */ 
/*      */ 
/*      */         
/*  716 */         this.entry.entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*      */         
/*  718 */         this.entry.entry.setExtra();
/*      */ 
/*      */ 
/*      */         
/*  722 */         if (this.entry.causedUseOfZip64) {
/*  723 */           this.hasUsedZip64 = false;
/*      */         }
/*      */       } 
/*      */     } 
/*  727 */     this.raf.seek(save);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void putNextEntry(ZipEntry archiveEntry) throws IOException {
/*  737 */     if (this.finished) {
/*  738 */       throw new IOException("Stream has already been finished");
/*      */     }
/*      */     
/*  741 */     if (this.entry != null) {
/*  742 */       closeEntry();
/*      */     }
/*      */     
/*  745 */     this.entry = new CurrentEntry(archiveEntry);
/*  746 */     this.entries.add(this.entry.entry);
/*      */     
/*  748 */     setDefaults(this.entry.entry);
/*      */     
/*  750 */     Zip64Mode effectiveMode = getEffectiveZip64Mode(this.entry.entry);
/*  751 */     validateSizeInformation(effectiveMode);
/*      */     
/*  753 */     if (shouldAddZip64Extra(this.entry.entry, effectiveMode)) {
/*      */       
/*  755 */       Zip64ExtendedInformationExtraField z64 = getZip64Extra(this.entry.entry);
/*      */ 
/*      */ 
/*      */       
/*  759 */       ZipEightByteInteger size = ZipEightByteInteger.ZERO;
/*  760 */       ZipEightByteInteger compressedSize = ZipEightByteInteger.ZERO;
/*  761 */       if (this.entry.entry.getMethod() == 0 && this.entry
/*  762 */         .entry.getSize() != -1L) {
/*      */         
/*  764 */         size = new ZipEightByteInteger(this.entry.entry.getSize());
/*  765 */         compressedSize = size;
/*      */       } 
/*  767 */       z64.setSize(size);
/*  768 */       z64.setCompressedSize(compressedSize);
/*  769 */       this.entry.entry.setExtra();
/*      */     } 
/*      */     
/*  772 */     if (this.entry.entry.getMethod() == 8 && this.hasCompressionLevelChanged) {
/*  773 */       this.def.setLevel(this.level);
/*  774 */       this.hasCompressionLevelChanged = false;
/*      */     } 
/*  776 */     writeLocalFileHeader(this.entry.entry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setDefaults(ZipEntry entry) {
/*  786 */     if (entry.getMethod() == -1) {
/*  787 */       entry.setMethod(this.method);
/*      */     }
/*      */     
/*  790 */     if (entry.getTime() == -1L) {
/*  791 */       entry.setTime(System.currentTimeMillis());
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
/*      */   private void validateSizeInformation(Zip64Mode effectiveMode) throws ZipException {
/*  806 */     if (this.entry.entry.getMethod() == 0 && this.raf == null) {
/*  807 */       if (this.entry.entry.getSize() == -1L) {
/*  808 */         throw new ZipException("uncompressed size is required for STORED method when not writing to a file");
/*      */       }
/*      */ 
/*      */       
/*  812 */       if (this.entry.entry.getCrc() == -1L) {
/*  813 */         throw new ZipException("crc checksum is required for STORED method when not writing to a file");
/*      */       }
/*      */       
/*  816 */       this.entry.entry.setCompressedSize(this.entry.entry.getSize());
/*      */     } 
/*      */     
/*  819 */     if ((this.entry.entry.getSize() >= 4294967295L || this.entry
/*  820 */       .entry.getCompressedSize() >= 4294967295L) && effectiveMode == Zip64Mode.Never)
/*      */     {
/*  822 */       throw new Zip64RequiredException(
/*  823 */           Zip64RequiredException.getEntryTooBigMessage(this.entry.entry));
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
/*      */   private boolean shouldAddZip64Extra(ZipEntry entry, Zip64Mode mode) {
/*  845 */     return (mode == Zip64Mode.Always || entry
/*  846 */       .getSize() >= 4294967295L || entry
/*  847 */       .getCompressedSize() >= 4294967295L || (entry
/*  848 */       .getSize() == -1L && this.raf != null && mode != Zip64Mode.Never));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComment(String comment) {
/*  858 */     this.comment = comment;
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
/*      */   public void setLevel(int level) {
/*  872 */     if (level < -1 || level > 9)
/*      */     {
/*  874 */       throw new IllegalArgumentException("Invalid compression level: " + level);
/*      */     }
/*      */     
/*  877 */     if (this.level == level) {
/*      */       return;
/*      */     }
/*  880 */     this.hasCompressionLevelChanged = true;
/*  881 */     this.level = level;
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
/*      */   public void setMethod(int method) {
/*  893 */     this.method = method;
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
/*      */   public boolean canWriteEntryData(ZipEntry ae) {
/*  906 */     return ZipUtil.canHandleEntryData(ae);
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
/*      */   public void write(int b) throws IOException {
/*  918 */     this.oneByte[0] = (byte)(b & 0xFF);
/*  919 */     write(this.oneByte, 0, 1);
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
/*      */   public void write(byte[] b, int offset, int length) throws IOException {
/*  932 */     if (this.entry == null) {
/*  933 */       throw new IllegalStateException("No current entry");
/*      */     }
/*  935 */     ZipUtil.checkRequestedFeatures(this.entry.entry);
/*  936 */     this.entry.hasWritten = true;
/*  937 */     if (this.entry.entry.getMethod() == 8) {
/*  938 */       writeDeflated(b, offset, length);
/*      */     } else {
/*  940 */       writeCounted(b, offset, length);
/*      */     } 
/*  942 */     this.crc.update(b, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeCounted(byte[] data) throws IOException {
/*  952 */     writeCounted(data, 0, data.length);
/*      */   }
/*      */   
/*      */   private void writeCounted(byte[] data, int offset, int length) throws IOException {
/*  956 */     writeOut(data, offset, length);
/*  957 */     this.written += length;
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
/*      */   private void writeDeflated(byte[] b, int offset, int length) throws IOException {
/*  969 */     if (length > 0 && !this.def.finished()) {
/*  970 */       this.entry.bytesRead += length;
/*  971 */       if (length <= 8192) {
/*  972 */         this.def.setInput(b, offset, length);
/*  973 */         deflateUntilInputIsNeeded();
/*      */       } else {
/*  975 */         int fullblocks = length / 8192;
/*  976 */         for (int i = 0; i < fullblocks; i++) {
/*  977 */           this.def.setInput(b, offset + i * 8192, 8192);
/*      */           
/*  979 */           deflateUntilInputIsNeeded();
/*      */         } 
/*  981 */         int done = fullblocks * 8192;
/*  982 */         if (done < length) {
/*  983 */           this.def.setInput(b, offset + done, length - done);
/*  984 */           deflateUntilInputIsNeeded();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/* 1001 */     if (!this.finished) {
/* 1002 */       finish();
/*      */     }
/* 1004 */     destroy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {
/* 1015 */     if (this.out != null) {
/* 1016 */       this.out.flush();
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
/* 1028 */   protected static final byte[] LFH_SIG = ZipLong.LFH_SIG.getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1034 */   protected static final byte[] DD_SIG = ZipLong.DD_SIG.getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1040 */   protected static final byte[] CFH_SIG = ZipLong.CFH_SIG.getBytes();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1046 */   protected static final byte[] EOCD_SIG = ZipLong.getBytes(101010256L);
/*      */ 
/*      */ 
/*      */   
/* 1050 */   static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(101075792L);
/*      */ 
/*      */ 
/*      */   
/* 1054 */   static final byte[] ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(117853008L);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void deflate() throws IOException {
/* 1063 */     int len = this.def.deflate(this.buf, 0, this.buf.length);
/* 1064 */     if (len > 0) {
/* 1065 */       writeCounted(this.buf, 0, len);
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
/*      */   protected void writeLocalFileHeader(ZipEntry ze) throws IOException {
/* 1078 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1079 */     ByteBuffer name = getName(ze);
/*      */     
/* 1081 */     if (this.createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
/* 1082 */       addUnicodeExtraFields(ze, encodable, name);
/*      */     }
/*      */     
/* 1085 */     byte[] localHeader = createLocalFileHeader(ze, name, encodable);
/* 1086 */     long localHeaderStart = this.written;
/* 1087 */     this.offsets.put(ze, Long.valueOf(localHeaderStart));
/* 1088 */     this.entry.localDataStart = localHeaderStart + 14L;
/* 1089 */     writeCounted(localHeader);
/* 1090 */     this.entry.dataStart = this.written;
/*      */   }
/*      */   
/*      */   private byte[] createLocalFileHeader(ZipEntry ze, ByteBuffer name, boolean encodable) {
/* 1094 */     byte[] extra = ze.getLocalFileDataExtra();
/* 1095 */     int nameLen = name.limit() - name.position();
/* 1096 */     int len = 30 + nameLen + extra.length;
/* 1097 */     byte[] buf = new byte[len];
/*      */     
/* 1099 */     System.arraycopy(LFH_SIG, 0, buf, 0, 4);
/*      */ 
/*      */     
/* 1102 */     int zipMethod = ze.getMethod();
/*      */     
/* 1104 */     ZipShort.putShort(versionNeededToExtract(zipMethod, hasZip64Extra(ze)), buf, 4);
/*      */ 
/*      */ 
/*      */     
/* 1108 */     GeneralPurposeBit generalPurposeBit = getGeneralPurposeBits(zipMethod, (!encodable && this.fallbackToUTF8));
/* 1109 */     generalPurposeBit.encode(buf, 6);
/*      */ 
/*      */     
/* 1112 */     ZipShort.putShort(zipMethod, buf, 8);
/*      */     
/* 1114 */     ZipUtil.toDosTime(this.calendarInstance, ze.getTime(), buf, 10);
/*      */ 
/*      */     
/* 1117 */     if (zipMethod == 8 || this.raf != null) {
/* 1118 */       System.arraycopy(LZERO, 0, buf, 14, 4);
/*      */     } else {
/* 1120 */       ZipLong.putLong(ze.getCrc(), buf, 14);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1125 */     if (hasZip64Extra(this.entry.entry)) {
/*      */ 
/*      */ 
/*      */       
/* 1129 */       ZipLong.ZIP64_MAGIC.putLong(buf, 18);
/* 1130 */       ZipLong.ZIP64_MAGIC.putLong(buf, 22);
/* 1131 */     } else if (zipMethod == 8 || this.raf != null) {
/* 1132 */       System.arraycopy(LZERO, 0, buf, 18, 4);
/* 1133 */       System.arraycopy(LZERO, 0, buf, 22, 4);
/*      */     } else {
/* 1135 */       ZipLong.putLong(ze.getSize(), buf, 18);
/* 1136 */       ZipLong.putLong(ze.getSize(), buf, 22);
/*      */     } 
/*      */     
/* 1139 */     ZipShort.putShort(nameLen, buf, 26);
/*      */ 
/*      */     
/* 1142 */     ZipShort.putShort(extra.length, buf, 28);
/*      */ 
/*      */     
/* 1145 */     System.arraycopy(name.array(), name.arrayOffset(), buf, 30, nameLen);
/*      */ 
/*      */     
/* 1148 */     System.arraycopy(extra, 0, buf, 30 + nameLen, extra.length);
/* 1149 */     return buf;
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
/*      */   private void addUnicodeExtraFields(ZipEntry ze, boolean encodable, ByteBuffer name) throws IOException {
/* 1164 */     if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !encodable)
/*      */     {
/* 1166 */       ze.addExtraField(new UnicodePathExtraField(ze.getName(), name
/* 1167 */             .array(), name
/* 1168 */             .arrayOffset(), name
/* 1169 */             .limit() - name
/* 1170 */             .position()));
/*      */     }
/*      */     
/* 1173 */     String comm = ze.getComment();
/* 1174 */     if (comm == null || comm.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1178 */     if (this.createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || 
/* 1179 */       !this.zipEncoding.canEncode(comm)) {
/* 1180 */       ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
/* 1181 */       ze.addExtraField(new UnicodeCommentExtraField(comm, commentB
/* 1182 */             .array(), commentB.arrayOffset(), commentB
/* 1183 */             .limit() - commentB.position()));
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
/*      */   protected void writeDataDescriptor(ZipEntry ze) throws IOException {
/* 1195 */     if (ze.getMethod() != 8 || this.raf != null) {
/*      */       return;
/*      */     }
/* 1198 */     writeCounted(DD_SIG);
/* 1199 */     writeCounted(ZipLong.getBytes(ze.getCrc()));
/* 1200 */     if (!hasZip64Extra(ze)) {
/* 1201 */       writeCounted(ZipLong.getBytes(ze.getCompressedSize()));
/* 1202 */       writeCounted(ZipLong.getBytes(ze.getSize()));
/*      */     } else {
/* 1204 */       writeCounted(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
/* 1205 */       writeCounted(ZipEightByteInteger.getBytes(ze.getSize()));
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
/*      */   protected void writeCentralFileHeader(ZipEntry ze) throws IOException {
/* 1219 */     byte[] centralFileHeader = createCentralFileHeader(ze);
/* 1220 */     writeCounted(centralFileHeader);
/*      */   }
/*      */   
/*      */   private byte[] createCentralFileHeader(ZipEntry ze) throws IOException {
/* 1224 */     long lfhOffset = ((Long)this.offsets.get(ze)).longValue();
/*      */ 
/*      */     
/* 1227 */     boolean needsZip64Extra = (hasZip64Extra(ze) || ze.getCompressedSize() >= 4294967295L || ze.getSize() >= 4294967295L || lfhOffset >= 4294967295L);
/*      */ 
/*      */     
/* 1230 */     if (needsZip64Extra && this.zip64Mode == Zip64Mode.Never)
/*      */     {
/*      */ 
/*      */       
/* 1234 */       throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1239 */     handleZip64Extra(ze, lfhOffset, needsZip64Extra);
/*      */     
/* 1241 */     return createCentralFileHeader(ze, getName(ze), lfhOffset, needsZip64Extra);
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
/*      */   private byte[] createCentralFileHeader(ZipEntry ze, ByteBuffer name, long lfhOffset, boolean needsZip64Extra) throws IOException {
/* 1254 */     byte[] extra = ze.getCentralDirectoryExtra();
/*      */ 
/*      */     
/* 1257 */     String comm = ze.getComment();
/* 1258 */     if (comm == null) {
/* 1259 */       comm = "";
/*      */     }
/*      */     
/* 1262 */     ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
/* 1263 */     int nameLen = name.limit() - name.position();
/* 1264 */     int commentLen = commentB.limit() - commentB.position();
/* 1265 */     int len = 46 + nameLen + extra.length + commentLen;
/* 1266 */     byte[] buf = new byte[len];
/*      */     
/* 1268 */     System.arraycopy(CFH_SIG, 0, buf, 0, 4);
/*      */ 
/*      */ 
/*      */     
/* 1272 */     ZipShort.putShort(ze.getPlatform() << 8 | (!this.hasUsedZip64 ? 20 : 45), buf, 4);
/*      */ 
/*      */     
/* 1275 */     int zipMethod = ze.getMethod();
/* 1276 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1277 */     ZipShort.putShort(versionNeededToExtract(zipMethod, needsZip64Extra), buf, 6);
/* 1278 */     getGeneralPurposeBits(zipMethod, (!encodable && this.fallbackToUTF8)).encode(buf, 8);
/*      */ 
/*      */     
/* 1281 */     ZipShort.putShort(zipMethod, buf, 10);
/*      */ 
/*      */ 
/*      */     
/* 1285 */     ZipUtil.toDosTime(this.calendarInstance, ze.getTime(), buf, 12);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1290 */     ZipLong.putLong(ze.getCrc(), buf, 16);
/* 1291 */     if (ze.getCompressedSize() >= 4294967295L || ze
/* 1292 */       .getSize() >= 4294967295L) {
/* 1293 */       ZipLong.ZIP64_MAGIC.putLong(buf, 20);
/* 1294 */       ZipLong.ZIP64_MAGIC.putLong(buf, 24);
/*      */     } else {
/* 1296 */       ZipLong.putLong(ze.getCompressedSize(), buf, 20);
/* 1297 */       ZipLong.putLong(ze.getSize(), buf, 24);
/*      */     } 
/*      */     
/* 1300 */     ZipShort.putShort(nameLen, buf, 28);
/*      */ 
/*      */     
/* 1303 */     ZipShort.putShort(extra.length, buf, 30);
/*      */     
/* 1305 */     ZipShort.putShort(commentLen, buf, 32);
/*      */ 
/*      */     
/* 1308 */     System.arraycopy(ZERO, 0, buf, 34, 2);
/*      */ 
/*      */     
/* 1311 */     ZipShort.putShort(ze.getInternalAttributes(), buf, 36);
/*      */ 
/*      */     
/* 1314 */     ZipLong.putLong(ze.getExternalAttributes(), buf, 38);
/*      */ 
/*      */     
/* 1317 */     ZipLong.putLong(Math.min(lfhOffset, 4294967295L), buf, 42);
/*      */ 
/*      */     
/* 1320 */     System.arraycopy(name.array(), name.arrayOffset(), buf, 46, nameLen);
/*      */     
/* 1322 */     int extraStart = 46 + nameLen;
/* 1323 */     System.arraycopy(extra, 0, buf, extraStart, extra.length);
/*      */     
/* 1325 */     int commentStart = extraStart + extra.length;
/*      */ 
/*      */     
/* 1328 */     System.arraycopy(commentB.array(), commentB.arrayOffset(), buf, commentStart, commentLen);
/* 1329 */     return buf;
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
/*      */   private void handleZip64Extra(ZipEntry ze, long lfhOffset, boolean needsZip64Extra) {
/* 1342 */     if (needsZip64Extra) {
/* 1343 */       Zip64ExtendedInformationExtraField z64 = getZip64Extra(ze);
/* 1344 */       if (ze.getCompressedSize() >= 4294967295L || ze
/* 1345 */         .getSize() >= 4294967295L) {
/* 1346 */         z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
/* 1347 */         z64.setSize(new ZipEightByteInteger(ze.getSize()));
/*      */       } else {
/*      */         
/* 1350 */         z64.setCompressedSize(null);
/* 1351 */         z64.setSize(null);
/*      */       } 
/* 1353 */       if (lfhOffset >= 4294967295L) {
/* 1354 */         z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset));
/*      */       }
/* 1356 */       ze.setExtra();
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
/*      */   protected void writeCentralDirectoryEnd() throws IOException {
/* 1369 */     writeCounted(EOCD_SIG);
/*      */ 
/*      */     
/* 1372 */     writeCounted(ZERO);
/* 1373 */     writeCounted(ZERO);
/*      */ 
/*      */     
/* 1376 */     int numberOfEntries = this.entries.size();
/* 1377 */     if (numberOfEntries > 65535 && this.zip64Mode == Zip64Mode.Never)
/*      */     {
/* 1379 */       throw new Zip64RequiredException("archive contains more than 65535 entries.");
/*      */     }
/*      */     
/* 1382 */     if (this.cdOffset > 4294967295L && this.zip64Mode == Zip64Mode.Never) {
/* 1383 */       throw new Zip64RequiredException("archive's size exceeds the limit of 4GByte.");
/*      */     }
/*      */ 
/*      */     
/* 1387 */     byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, 65535));
/*      */     
/* 1389 */     writeCounted(num);
/* 1390 */     writeCounted(num);
/*      */ 
/*      */     
/* 1393 */     writeCounted(ZipLong.getBytes(Math.min(this.cdLength, 4294967295L)));
/* 1394 */     writeCounted(ZipLong.getBytes(Math.min(this.cdOffset, 4294967295L)));
/*      */ 
/*      */     
/* 1397 */     ByteBuffer data = this.zipEncoding.encode(this.comment);
/* 1398 */     int dataLen = data.limit() - data.position();
/* 1399 */     writeCounted(ZipShort.getBytes(dataLen));
/* 1400 */     writeCounted(data.array(), data.arrayOffset(), dataLen);
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
/*      */   @Deprecated
/*      */   protected static ZipLong toDosTime(Date time) {
/* 1413 */     return ZipUtil.toDosTime(time);
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
/*      */   @Deprecated
/*      */   protected static byte[] toDosTime(long t) {
/* 1428 */     return ZipUtil.toDosTime(t);
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
/*      */   protected byte[] getBytes(String name) throws ZipException {
/*      */     try {
/* 1444 */       ByteBuffer b = ZipEncodingHelper.getZipEncoding(this.encoding).encode(name);
/* 1445 */       byte[] result = new byte[b.limit()];
/* 1446 */       System.arraycopy(b.array(), b.arrayOffset(), result, 0, result.length);
/*      */       
/* 1448 */       return result;
/* 1449 */     } catch (IOException ex) {
/* 1450 */       throw new ZipException("Failed to encode name: " + ex.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeZip64CentralDirectory() throws IOException {
/* 1461 */     if (this.zip64Mode == Zip64Mode.Never) {
/*      */       return;
/*      */     }
/*      */     
/* 1465 */     if (!this.hasUsedZip64 && (this.cdOffset >= 4294967295L || this.cdLength >= 4294967295L || this.entries
/*      */       
/* 1467 */       .size() >= 65535))
/*      */     {
/* 1469 */       this.hasUsedZip64 = true;
/*      */     }
/*      */     
/* 1472 */     if (!this.hasUsedZip64) {
/*      */       return;
/*      */     }
/*      */     
/* 1476 */     long offset = this.written;
/*      */     
/* 1478 */     writeOut(ZIP64_EOCD_SIG);
/*      */ 
/*      */     
/* 1481 */     writeOut(
/* 1482 */         ZipEightByteInteger.getBytes(44L));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1493 */     writeOut(ZipShort.getBytes(45));
/* 1494 */     writeOut(ZipShort.getBytes(45));
/*      */ 
/*      */     
/* 1497 */     writeOut(LZERO);
/* 1498 */     writeOut(LZERO);
/*      */ 
/*      */     
/* 1501 */     byte[] num = ZipEightByteInteger.getBytes(this.entries.size());
/* 1502 */     writeOut(num);
/* 1503 */     writeOut(num);
/*      */ 
/*      */     
/* 1506 */     writeOut(ZipEightByteInteger.getBytes(this.cdLength));
/* 1507 */     writeOut(ZipEightByteInteger.getBytes(this.cdOffset));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1512 */     writeOut(ZIP64_EOCD_LOC_SIG);
/*      */ 
/*      */     
/* 1515 */     writeOut(LZERO);
/*      */     
/* 1517 */     writeOut(ZipEightByteInteger.getBytes(offset));
/*      */     
/* 1519 */     writeOut(ONE);
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
/*      */   protected final void writeOut(byte[] data) throws IOException {
/* 1531 */     writeOut(data, 0, data.length);
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
/*      */   protected final void writeOut(byte[] data, int offset, int length) throws IOException {
/* 1546 */     if (this.raf != null) {
/* 1547 */       this.raf.write(data, offset, length);
/*      */     } else {
/* 1549 */       this.out.write(data, offset, length);
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
/*      */   @Deprecated
/*      */   protected static long adjustToLong(int i) {
/* 1564 */     return ZipUtil.adjustToLong(i);
/*      */   }
/*      */   
/*      */   private void deflateUntilInputIsNeeded() throws IOException {
/* 1568 */     while (!this.def.needsInput()) {
/* 1569 */       deflate();
/*      */     }
/*      */   }
/*      */   
/*      */   private GeneralPurposeBit getGeneralPurposeBits(int zipMethod, boolean utfFallback) {
/* 1574 */     GeneralPurposeBit b = new GeneralPurposeBit();
/* 1575 */     b.useUTF8ForNames((this.useUTF8Flag || utfFallback));
/* 1576 */     if (isDeflatedToOutputStream(zipMethod)) {
/* 1577 */       b.useDataDescriptor(true);
/*      */     }
/* 1579 */     return b;
/*      */   }
/*      */   
/*      */   private int versionNeededToExtract(int zipMethod, boolean zip64) {
/* 1583 */     if (zip64) {
/* 1584 */       return 45;
/*      */     }
/*      */ 
/*      */     
/* 1588 */     return isDeflatedToOutputStream(zipMethod) ? 
/* 1589 */       20 : 10;
/*      */   }
/*      */   
/*      */   private boolean isDeflatedToOutputStream(int zipMethod) {
/* 1593 */     return (zipMethod == 8 && this.raf == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Zip64ExtendedInformationExtraField getZip64Extra(ZipEntry ze) {
/* 1604 */     if (this.entry != null) {
/* 1605 */       this.entry.causedUseOfZip64 = !this.hasUsedZip64;
/*      */     }
/* 1607 */     this.hasUsedZip64 = true;
/*      */ 
/*      */     
/* 1610 */     Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*      */     
/* 1612 */     if (z64 == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1619 */       z64 = new Zip64ExtendedInformationExtraField();
/*      */     }
/*      */ 
/*      */     
/* 1623 */     ze.addAsFirstExtraField(z64);
/*      */     
/* 1625 */     return z64;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasZip64Extra(ZipEntry ze) {
/* 1636 */     return (ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) != null);
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
/*      */   private Zip64Mode getEffectiveZip64Mode(ZipEntry ze) {
/* 1650 */     if (this.zip64Mode != Zip64Mode.AsNeeded || this.raf != null || ze
/*      */       
/* 1652 */       .getMethod() != 8 || ze
/* 1653 */       .getSize() != -1L) {
/* 1654 */       return this.zip64Mode;
/*      */     }
/* 1656 */     return Zip64Mode.Never;
/*      */   }
/*      */   
/*      */   private ZipEncoding getEntryEncoding(ZipEntry ze) {
/* 1660 */     boolean encodable = this.zipEncoding.canEncode(ze.getName());
/* 1661 */     return (!encodable && this.fallbackToUTF8) ? 
/* 1662 */       ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
/*      */   }
/*      */   
/*      */   private ByteBuffer getName(ZipEntry ze) throws IOException {
/* 1666 */     return getEntryEncoding(ze).encode(ze.getName());
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
/*      */   void destroy() throws IOException {
/* 1679 */     if (this.raf != null) {
/* 1680 */       this.raf.close();
/*      */     }
/* 1682 */     if (this.out != null) {
/* 1683 */       this.out.close();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class UnicodeExtraFieldPolicy
/*      */   {
/* 1695 */     public static final UnicodeExtraFieldPolicy ALWAYS = new UnicodeExtraFieldPolicy("always");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1700 */     public static final UnicodeExtraFieldPolicy NEVER = new UnicodeExtraFieldPolicy("never");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1706 */     public static final UnicodeExtraFieldPolicy NOT_ENCODEABLE = new UnicodeExtraFieldPolicy("not encodeable");
/*      */     
/*      */     private final String name;
/*      */     
/*      */     private UnicodeExtraFieldPolicy(String n) {
/* 1711 */       this.name = n;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1715 */       return this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class CurrentEntry
/*      */   {
/*      */     private final ZipEntry entry;
/*      */     
/*      */     private long localDataStart;
/*      */     
/*      */     private long dataStart;
/*      */     
/*      */     private long bytesRead;
/*      */     
/*      */     private boolean causedUseOfZip64;
/*      */     
/*      */     private boolean hasWritten;
/*      */     
/*      */     private CurrentEntry(ZipEntry entry) {
/* 1735 */       this.localDataStart = 0L;
/*      */ 
/*      */ 
/*      */       
/* 1739 */       this.dataStart = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1744 */       this.bytesRead = 0L;
/*      */ 
/*      */ 
/*      */       
/* 1748 */       this.causedUseOfZip64 = false;
/*      */       this.entry = entry;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */