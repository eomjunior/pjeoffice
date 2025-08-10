/*      */ package org.apache.tools.zip;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.InflaterInputStream;
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
/*      */ public class ZipFile
/*      */   implements Closeable
/*      */ {
/*      */   private static final int HASH_SIZE = 509;
/*      */   static final int NIBLET_MASK = 15;
/*      */   static final int BYTE_SHIFT = 8;
/*      */   private static final int POS_0 = 0;
/*      */   private static final int POS_1 = 1;
/*      */   private static final int POS_2 = 2;
/*      */   private static final int POS_3 = 3;
/*   89 */   private final List<ZipEntry> entries = new LinkedList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   94 */   private final Map<String, LinkedList<ZipEntry>> nameMap = new HashMap<>(509); private final String encoding; private final ZipEncoding zipEncoding; private final String archiveName; private final RandomAccessFile archive; private final boolean useUnicodeExtraFields; private volatile boolean closed;
/*      */   
/*      */   private static final class OffsetEntry { private OffsetEntry() {}
/*      */     
/*   98 */     private long headerOffset = -1L;
/*   99 */     private long dataOffset = -1L; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  138 */   private final byte[] DWORD_BUF = new byte[8];
/*  139 */   private final byte[] WORD_BUF = new byte[4];
/*  140 */   private final byte[] CFH_BUF = new byte[42];
/*  141 */   private final byte[] SHORT_BUF = new byte[2];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int CFH_LEN = 42;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File f) throws IOException {
/*  152 */     this(f, (String)null);
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
/*      */   public ZipFile(String name) throws IOException {
/*  164 */     this(new File(name), (String)null);
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
/*      */   public ZipFile(String name, String encoding) throws IOException {
/*  178 */     this(new File(name), encoding, true);
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
/*      */   public ZipFile(File f, String encoding) throws IOException {
/*  192 */     this(f, encoding, true);
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
/*      */   public String getEncoding() {
/*  238 */     return this.encoding;
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
/*      */   public void close() throws IOException {
/*  250 */     this.closed = true;
/*      */     
/*  252 */     this.archive.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void closeQuietly(ZipFile zipfile) {
/*  261 */     if (zipfile != null) {
/*      */       try {
/*  263 */         zipfile.close();
/*  264 */       } catch (IOException iOException) {}
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
/*      */   public Enumeration<ZipEntry> getEntries() {
/*  279 */     return Collections.enumeration(this.entries);
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
/*      */   public Enumeration<ZipEntry> getEntriesInPhysicalOrder() {
/*  293 */     return (Enumeration<ZipEntry>)this.entries.stream().sorted(this.OFFSET_COMPARATOR).collect(
/*  294 */         Collectors.collectingAndThen(Collectors.toList(), Collections::enumeration));
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
/*      */   public ZipEntry getEntry(String name) {
/*  310 */     LinkedList<ZipEntry> entriesOfThatName = this.nameMap.get(name);
/*  311 */     return (entriesOfThatName != null) ? entriesOfThatName.getFirst() : null;
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
/*      */   public Iterable<ZipEntry> getEntries(String name) {
/*  324 */     List<ZipEntry> entriesOfThatName = this.nameMap.get(name);
/*  325 */     return (entriesOfThatName != null) ? entriesOfThatName : 
/*  326 */       Collections.<ZipEntry>emptyList();
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
/*      */   public Iterable<ZipEntry> getEntriesInPhysicalOrder(String name) {
/*  339 */     if (this.nameMap.containsKey(name)) {
/*  340 */       return (Iterable<ZipEntry>)((LinkedList<ZipEntry>)this.nameMap.get(name)).stream().sorted(this.OFFSET_COMPARATOR)
/*  341 */         .collect(Collectors.toList());
/*      */     }
/*  343 */     return Collections.emptyList();
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
/*      */   public boolean canReadEntryData(ZipEntry ze) {
/*  356 */     return ZipUtil.canHandleEntryData(ze);
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
/*      */   public InputStream getInputStream(ZipEntry ze) throws IOException, ZipException {
/*      */     final Inflater inflater;
/*  369 */     if (!(ze instanceof Entry)) {
/*  370 */       return null;
/*      */     }
/*      */     
/*  373 */     OffsetEntry offsetEntry = ((Entry)ze).getOffsetEntry();
/*  374 */     ZipUtil.checkRequestedFeatures(ze);
/*  375 */     long start = offsetEntry.dataOffset;
/*      */ 
/*      */ 
/*      */     
/*  379 */     BoundedInputStream bis = new BoundedInputStream(start, ze.getCompressedSize());
/*  380 */     switch (ze.getMethod()) {
/*      */       case 0:
/*  382 */         return bis;
/*      */       case 8:
/*  384 */         bis.addDummy();
/*  385 */         inflater = new Inflater(true);
/*  386 */         return new InflaterInputStream(bis, inflater)
/*      */           {
/*      */             public void close() throws IOException {
/*  389 */               super.close();
/*  390 */               inflater.end();
/*      */             }
/*      */           };
/*      */     } 
/*  394 */     throw new ZipException("Found unsupported compression method " + ze
/*  395 */         .getMethod());
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  400 */     return this.archiveName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/*      */     try {
/*  411 */       if (!this.closed) {
/*  412 */         System.err.printf("Cleaning up unclosed %s for archive %s%n", new Object[] {
/*  413 */               getClass().getSimpleName(), this.archiveName });
/*  414 */         close();
/*      */       } 
/*      */     } finally {
/*  417 */       super.finalize();
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
/*  444 */   private static final long CFH_SIG = ZipLong.getValue(ZipOutputStream.CFH_SIG);
/*      */   
/*      */   private static final int MIN_EOCD_SIZE = 22;
/*      */   
/*      */   private static final int MAX_EOCD_SIZE = 65557;
/*      */   
/*      */   private static final int CFD_LOCATOR_OFFSET = 16;
/*      */   
/*      */   private static final int ZIP64_EOCDL_LENGTH = 20;
/*      */   private static final int ZIP64_EOCDL_LOCATOR_OFFSET = 8;
/*      */   private static final int ZIP64_EOCD_CFD_LOCATOR_OFFSET = 48;
/*      */   private static final long LFH_OFFSET_FOR_FILENAME_LENGTH = 26L;
/*      */   private final Comparator<ZipEntry> OFFSET_COMPARATOR;
/*      */   
/*      */   private Map<ZipEntry, NameAndComment> populateFromCentralDirectory() throws IOException {
/*  459 */     Map<ZipEntry, NameAndComment> noUTF8Flag = new HashMap<>();
/*      */     
/*  461 */     positionAtCentralDirectory();
/*      */     
/*  463 */     this.archive.readFully(this.WORD_BUF);
/*  464 */     long sig = ZipLong.getValue(this.WORD_BUF);
/*      */     
/*  466 */     if (sig != CFH_SIG && startsWithLocalFileHeader()) {
/*  467 */       throw new IOException("central directory is empty, can't expand corrupt archive.");
/*      */     }
/*      */ 
/*      */     
/*  471 */     while (sig == CFH_SIG) {
/*  472 */       readCentralDirectoryEntry(noUTF8Flag);
/*  473 */       this.archive.readFully(this.WORD_BUF);
/*  474 */       sig = ZipLong.getValue(this.WORD_BUF);
/*      */     } 
/*  476 */     return noUTF8Flag;
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
/*      */   private void readCentralDirectoryEntry(Map<ZipEntry, NameAndComment> noUTF8Flag) throws IOException {
/*  491 */     this.archive.readFully(this.CFH_BUF);
/*  492 */     int off = 0;
/*  493 */     OffsetEntry offset = new OffsetEntry();
/*  494 */     Entry ze = new Entry(offset);
/*      */     
/*  496 */     int versionMadeBy = ZipShort.getValue(this.CFH_BUF, off);
/*  497 */     off += 2;
/*  498 */     ze.setPlatform(versionMadeBy >> 8 & 0xF);
/*      */     
/*  500 */     off += 2;
/*      */     
/*  502 */     GeneralPurposeBit gpFlag = GeneralPurposeBit.parse(this.CFH_BUF, off);
/*  503 */     boolean hasUTF8Flag = gpFlag.usesUTF8ForNames();
/*      */     
/*  505 */     ZipEncoding entryEncoding = hasUTF8Flag ? ZipEncodingHelper.UTF8_ZIP_ENCODING : this.zipEncoding;
/*  506 */     ze.setGeneralPurposeBit(gpFlag);
/*      */     
/*  508 */     off += 2;
/*      */     
/*  510 */     ze.setMethod(ZipShort.getValue(this.CFH_BUF, off));
/*  511 */     off += 2;
/*      */     
/*  513 */     long time = ZipUtil.dosToJavaTime(ZipLong.getValue(this.CFH_BUF, off));
/*  514 */     ze.setTime(time);
/*  515 */     off += 4;
/*      */     
/*  517 */     ze.setCrc(ZipLong.getValue(this.CFH_BUF, off));
/*  518 */     off += 4;
/*      */     
/*  520 */     ze.setCompressedSize(ZipLong.getValue(this.CFH_BUF, off));
/*  521 */     off += 4;
/*      */     
/*  523 */     ze.setSize(ZipLong.getValue(this.CFH_BUF, off));
/*  524 */     off += 4;
/*      */     
/*  526 */     int fileNameLen = ZipShort.getValue(this.CFH_BUF, off);
/*  527 */     off += 2;
/*      */     
/*  529 */     int extraLen = ZipShort.getValue(this.CFH_BUF, off);
/*  530 */     off += 2;
/*      */     
/*  532 */     int commentLen = ZipShort.getValue(this.CFH_BUF, off);
/*  533 */     off += 2;
/*      */     
/*  535 */     int diskStart = ZipShort.getValue(this.CFH_BUF, off);
/*  536 */     off += 2;
/*      */     
/*  538 */     ze.setInternalAttributes(ZipShort.getValue(this.CFH_BUF, off));
/*  539 */     off += 2;
/*      */     
/*  541 */     ze.setExternalAttributes(ZipLong.getValue(this.CFH_BUF, off));
/*  542 */     off += 4;
/*      */     
/*  544 */     if (this.archive.length() - this.archive.getFilePointer() < fileNameLen) {
/*  545 */       throw new EOFException();
/*      */     }
/*  547 */     byte[] fileName = new byte[fileNameLen];
/*  548 */     this.archive.readFully(fileName);
/*  549 */     ze.setName(entryEncoding.decode(fileName), fileName);
/*      */ 
/*      */     
/*  552 */     offset.headerOffset = ZipLong.getValue(this.CFH_BUF, off);
/*      */     
/*  554 */     this.entries.add(ze);
/*      */     
/*  556 */     if (this.archive.length() - this.archive.getFilePointer() < extraLen) {
/*  557 */       throw new EOFException();
/*      */     }
/*  559 */     byte[] cdExtraData = new byte[extraLen];
/*  560 */     this.archive.readFully(cdExtraData);
/*  561 */     ze.setCentralDirectoryExtra(cdExtraData);
/*      */     
/*  563 */     setSizesAndOffsetFromZip64Extra(ze, offset, diskStart);
/*      */     
/*  565 */     if (this.archive.length() - this.archive.getFilePointer() < commentLen) {
/*  566 */       throw new EOFException();
/*      */     }
/*  568 */     byte[] comment = new byte[commentLen];
/*  569 */     this.archive.readFully(comment);
/*  570 */     ze.setComment(entryEncoding.decode(comment));
/*      */     
/*  572 */     if (!hasUTF8Flag && this.useUnicodeExtraFields) {
/*  573 */       noUTF8Flag.put(ze, new NameAndComment(fileName, comment));
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
/*      */   private void setSizesAndOffsetFromZip64Extra(ZipEntry ze, OffsetEntry offset, int diskStart) throws IOException {
/*  595 */     Zip64ExtendedInformationExtraField z64 = (Zip64ExtendedInformationExtraField)ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
/*  596 */     if (z64 != null) {
/*  597 */       boolean hasUncompressedSize = (ze.getSize() == 4294967295L);
/*  598 */       boolean hasCompressedSize = (ze.getCompressedSize() == 4294967295L);
/*      */       
/*  600 */       boolean hasRelativeHeaderOffset = (offset.headerOffset == 4294967295L);
/*  601 */       z64.reparseCentralDirectoryData(hasUncompressedSize, hasCompressedSize, hasRelativeHeaderOffset, (diskStart == 65535));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  606 */       if (hasUncompressedSize) {
/*  607 */         ze.setSize(z64.getSize().getLongValue());
/*  608 */       } else if (hasCompressedSize) {
/*  609 */         z64.setSize(new ZipEightByteInteger(ze.getSize()));
/*      */       } 
/*      */       
/*  612 */       if (hasCompressedSize) {
/*  613 */         ze.setCompressedSize(z64.getCompressedSize().getLongValue());
/*  614 */       } else if (hasUncompressedSize) {
/*  615 */         z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
/*      */       } 
/*      */       
/*  618 */       if (hasRelativeHeaderOffset) {
/*  619 */         offset.headerOffset = z64
/*  620 */           .getRelativeHeaderOffset().getLongValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtCentralDirectory() throws IOException {
/*  725 */     positionAtEndOfCentralDirectoryRecord();
/*  726 */     boolean found = false;
/*      */     
/*  728 */     boolean searchedForZip64EOCD = (this.archive.getFilePointer() > 20L);
/*  729 */     if (searchedForZip64EOCD) {
/*  730 */       this.archive.seek(this.archive.getFilePointer() - 20L);
/*  731 */       this.archive.readFully(this.WORD_BUF);
/*  732 */       found = Arrays.equals(ZipOutputStream.ZIP64_EOCD_LOC_SIG, this.WORD_BUF);
/*      */     } 
/*  734 */     if (!found) {
/*      */       
/*  736 */       if (searchedForZip64EOCD) {
/*  737 */         skipBytes(16);
/*      */       }
/*  739 */       positionAtCentralDirectory32();
/*      */     } else {
/*  741 */       positionAtCentralDirectory64();
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
/*      */   private void positionAtCentralDirectory64() throws IOException {
/*  753 */     skipBytes(4);
/*      */     
/*  755 */     this.archive.readFully(this.DWORD_BUF);
/*  756 */     this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
/*  757 */     this.archive.readFully(this.WORD_BUF);
/*  758 */     if (!Arrays.equals(this.WORD_BUF, ZipOutputStream.ZIP64_EOCD_SIG)) {
/*  759 */       throw new ZipException("archive's ZIP64 end of central directory locator is corrupt.");
/*      */     }
/*      */     
/*  762 */     skipBytes(44);
/*      */     
/*  764 */     this.archive.readFully(this.DWORD_BUF);
/*  765 */     this.archive.seek(ZipEightByteInteger.getLongValue(this.DWORD_BUF));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtCentralDirectory32() throws IOException {
/*  775 */     skipBytes(16);
/*  776 */     this.archive.readFully(this.WORD_BUF);
/*  777 */     this.archive.seek(ZipLong.getValue(this.WORD_BUF));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionAtEndOfCentralDirectoryRecord() throws IOException {
/*  786 */     boolean found = tryToLocateSignature(22L, 65557L, ZipOutputStream.EOCD_SIG);
/*      */     
/*  788 */     if (!found) {
/*  789 */       throw new ZipException("archive is not a ZIP archive");
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
/*      */   private boolean tryToLocateSignature(long minDistanceFromEnd, long maxDistanceFromEnd, byte[] sig) throws IOException {
/*  801 */     boolean found = false;
/*  802 */     long off = this.archive.length() - minDistanceFromEnd;
/*      */     
/*  804 */     long stopSearching = Math.max(0L, this.archive.length() - maxDistanceFromEnd);
/*  805 */     if (off >= 0L) {
/*  806 */       for (; off >= stopSearching; off--) {
/*  807 */         this.archive.seek(off);
/*  808 */         int curr = this.archive.read();
/*  809 */         if (curr == -1) {
/*      */           break;
/*      */         }
/*  812 */         if (curr == sig[0]) {
/*  813 */           curr = this.archive.read();
/*  814 */           if (curr == sig[1]) {
/*  815 */             curr = this.archive.read();
/*  816 */             if (curr == sig[2]) {
/*  817 */               curr = this.archive.read();
/*  818 */               if (curr == sig[3]) {
/*  819 */                 found = true;
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*  827 */     if (found) {
/*  828 */       this.archive.seek(off);
/*      */     }
/*  830 */     return found;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipBytes(int count) throws IOException {
/*  838 */     int totalSkipped = 0;
/*  839 */     while (totalSkipped < count) {
/*  840 */       int skippedNow = this.archive.skipBytes(count - totalSkipped);
/*  841 */       if (skippedNow <= 0) {
/*  842 */         throw new EOFException();
/*      */       }
/*  844 */       totalSkipped += skippedNow;
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
/*      */   private void resolveLocalFileHeaderData(Map<ZipEntry, NameAndComment> entriesWithoutUTF8Flag) throws IOException {
/*  873 */     for (ZipEntry zipEntry : this.entries) {
/*      */ 
/*      */       
/*  876 */       Entry ze = (Entry)zipEntry;
/*  877 */       OffsetEntry offsetEntry = ze.getOffsetEntry();
/*  878 */       long offset = offsetEntry.headerOffset;
/*  879 */       this.archive.seek(offset + 26L);
/*  880 */       this.archive.readFully(this.SHORT_BUF);
/*  881 */       int fileNameLen = ZipShort.getValue(this.SHORT_BUF);
/*  882 */       this.archive.readFully(this.SHORT_BUF);
/*  883 */       int extraFieldLen = ZipShort.getValue(this.SHORT_BUF);
/*  884 */       int lenToSkip = fileNameLen;
/*  885 */       while (lenToSkip > 0) {
/*  886 */         int skipped = this.archive.skipBytes(lenToSkip);
/*  887 */         if (skipped <= 0) {
/*  888 */           throw new IOException("failed to skip file name in local file header");
/*      */         }
/*      */         
/*  891 */         lenToSkip -= skipped;
/*      */       } 
/*  893 */       if (this.archive.length() - this.archive.getFilePointer() < extraFieldLen) {
/*  894 */         throw new EOFException();
/*      */       }
/*  896 */       byte[] localExtraData = new byte[extraFieldLen];
/*  897 */       this.archive.readFully(localExtraData);
/*      */       try {
/*  899 */         ze.setExtra(localExtraData);
/*  900 */       } catch (RuntimeException ex) {
/*  901 */         ZipException z = new ZipException("Invalid extra data in entry " + ze.getName());
/*  902 */         z.initCause(ex);
/*  903 */         throw z;
/*      */       } 
/*  905 */       offsetEntry.dataOffset = offset + 26L + 2L + 2L + fileNameLen + extraFieldLen;
/*      */ 
/*      */       
/*  908 */       if (entriesWithoutUTF8Flag.containsKey(ze)) {
/*  909 */         NameAndComment nc = entriesWithoutUTF8Flag.get(ze);
/*  910 */         ZipUtil.setNameAndCommentFromExtraFields(ze, nc.name, nc
/*  911 */             .comment);
/*      */       } 
/*      */       
/*  914 */       String name = ze.getName();
/*  915 */       LinkedList<ZipEntry> entriesOfThatName = this.nameMap.computeIfAbsent(name, k -> new LinkedList());
/*  916 */       entriesOfThatName.addLast(ze);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean startsWithLocalFileHeader() throws IOException {
/*  925 */     this.archive.seek(0L);
/*  926 */     this.archive.readFully(this.WORD_BUF);
/*  927 */     return Arrays.equals(this.WORD_BUF, ZipOutputStream.LFH_SIG);
/*      */   }
/*      */ 
/*      */   
/*      */   private class BoundedInputStream
/*      */     extends InputStream
/*      */   {
/*      */     private long remaining;
/*      */     
/*      */     private long loc;
/*      */     
/*      */     private boolean addDummyByte = false;
/*      */     
/*      */     BoundedInputStream(long start, long remaining) {
/*  941 */       this.remaining = remaining;
/*  942 */       this.loc = start;
/*      */     }
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/*  947 */       if (this.remaining-- <= 0L) {
/*  948 */         if (this.addDummyByte) {
/*  949 */           this.addDummyByte = false;
/*  950 */           return 0;
/*      */         } 
/*  952 */         return -1;
/*      */       } 
/*  954 */       synchronized (ZipFile.this.archive) {
/*  955 */         ZipFile.this.archive.seek(this.loc++);
/*  956 */         return ZipFile.this.archive.read();
/*      */       } 
/*      */     }
/*      */     
/*      */     public int read(byte[] b, int off, int len) throws IOException {
/*      */       int ret;
/*  962 */       if (this.remaining <= 0L) {
/*  963 */         if (this.addDummyByte) {
/*  964 */           this.addDummyByte = false;
/*  965 */           b[off] = 0;
/*  966 */           return 1;
/*      */         } 
/*  968 */         return -1;
/*      */       } 
/*      */       
/*  971 */       if (len <= 0) {
/*  972 */         return 0;
/*      */       }
/*      */       
/*  975 */       if (len > this.remaining) {
/*  976 */         len = (int)this.remaining;
/*      */       }
/*      */       
/*  979 */       synchronized (ZipFile.this.archive) {
/*  980 */         ZipFile.this.archive.seek(this.loc);
/*  981 */         ret = ZipFile.this.archive.read(b, off, len);
/*      */       } 
/*  983 */       if (ret > 0) {
/*  984 */         this.loc += ret;
/*  985 */         this.remaining -= ret;
/*      */       } 
/*  987 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addDummy() {
/*  995 */       this.addDummyByte = true;
/*      */     } }
/*      */   
/*      */   private static final class NameAndComment {
/*      */     private final byte[] name;
/*      */     private final byte[] comment;
/*      */     
/*      */     private NameAndComment(byte[] name, byte[] comment) {
/* 1003 */       this.name = name;
/* 1004 */       this.comment = comment;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ZipFile(File f, String encoding, boolean useUnicodeExtraFields) throws IOException
/*      */   {
/* 1016 */     this.OFFSET_COMPARATOR = ((e1, e2) -> {
/*      */         if (e1 == e2)
/*      */           return 0;  Entry ent1 = (e1 instanceof Entry) ? (Entry)e1 : null; Entry ent2 = (e2 instanceof Entry) ? (Entry)e2 : null;
/*      */         if (ent1 == null)
/*      */           return 1; 
/*      */         if (ent2 == null)
/*      */           return -1; 
/*      */         long val = (ent1.getOffsetEntry()).headerOffset - (ent2.getOffsetEntry()).headerOffset;
/*      */         return (val == 0L) ? 0 : ((val < 0L) ? -1 : 1);
/*      */       });
/*      */     this.archiveName = f.getAbsolutePath();
/*      */     this.encoding = encoding;
/*      */     this.zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
/*      */     this.useUnicodeExtraFields = useUnicodeExtraFields;
/*      */     this.archive = new RandomAccessFile(f, "r");
/*      */     boolean success = false;
/*      */     try {
/*      */       Map<ZipEntry, NameAndComment> entriesWithoutUTF8Flag = populateFromCentralDirectory();
/*      */       resolveLocalFileHeaderData(entriesWithoutUTF8Flag);
/*      */       success = true;
/*      */     } finally {
/*      */       this.closed = !success;
/*      */       if (!success)
/*      */         try {
/*      */           this.archive.close();
/*      */         } catch (IOException iOException) {} 
/* 1042 */     }  } private static class Entry extends ZipEntry { Entry(ZipFile.OffsetEntry offset) { this.offsetEntry = offset; }
/*      */     
/*      */     private final ZipFile.OffsetEntry offsetEntry;
/*      */     ZipFile.OffsetEntry getOffsetEntry() {
/* 1046 */       return this.offsetEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1051 */       return 3 * super.hashCode() + 
/* 1052 */         (int)(this.offsetEntry.headerOffset % 2147483647L);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/* 1057 */       if (super.equals(other)) {
/*      */         
/* 1059 */         Entry otherEntry = (Entry)other;
/* 1060 */         return (this.offsetEntry.headerOffset == otherEntry.offsetEntry
/* 1061 */           .headerOffset && this.offsetEntry
/* 1062 */           .dataOffset == otherEntry.offsetEntry
/* 1063 */           .dataOffset);
/*      */       } 
/* 1065 */       return false;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */