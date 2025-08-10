/*     */ package org.apache.tools.tar;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.zip.ZipEncoding;
/*     */ import org.apache.tools.zip.ZipEncodingHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*     */   public static final int LONGFILE_ERROR = 0;
/*     */   public static final int LONGFILE_TRUNCATE = 1;
/*     */   public static final int LONGFILE_GNU = 2;
/*     */   public static final int LONGFILE_POSIX = 3;
/*     */   public static final int BIGNUMBER_ERROR = 0;
/*     */   public static final int BIGNUMBER_STAR = 1;
/*     */   public static final int BIGNUMBER_POSIX = 2;
/*     */   protected boolean debug;
/*     */   protected long currSize;
/*     */   protected String currName;
/*     */   protected long currBytes;
/*     */   protected byte[] oneBuf;
/*     */   protected byte[] recordBuf;
/*     */   protected int assemLen;
/*     */   protected byte[] assemBuf;
/*     */   protected TarBuffer buffer;
/*  78 */   protected int longFileMode = 0;
/*     */ 
/*     */   
/*  81 */   private int bigNumberMode = 0;
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */   
/*     */   private boolean haveUnclosedEntry = false;
/*     */   
/*     */   private boolean finished = false;
/*     */   
/*     */   private final ZipEncoding encoding;
/*     */   
/*     */   private boolean addPaxHeadersForNonAsciiNames = false;
/*     */   
/*  95 */   private static final ZipEncoding ASCII = ZipEncodingHelper.getZipEncoding("ASCII");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarOutputStream(OutputStream os) {
/* 103 */     this(os, 10240, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarOutputStream(OutputStream os, String encoding) {
/* 113 */     this(os, 10240, 512, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarOutputStream(OutputStream os, int blockSize) {
/* 123 */     this(os, blockSize, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarOutputStream(OutputStream os, int blockSize, String encoding) {
/* 134 */     this(os, blockSize, 512, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarOutputStream(OutputStream os, int blockSize, int recordSize) {
/* 145 */     this(os, blockSize, recordSize, null);
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
/*     */   public TarOutputStream(OutputStream os, int blockSize, int recordSize, String encoding) {
/* 158 */     super(os);
/* 159 */     this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
/*     */     
/* 161 */     this.buffer = new TarBuffer(os, blockSize, recordSize);
/* 162 */     this.debug = false;
/* 163 */     this.assemLen = 0;
/* 164 */     this.assemBuf = new byte[recordSize];
/* 165 */     this.recordBuf = new byte[recordSize];
/* 166 */     this.oneBuf = new byte[1];
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
/*     */   public void setLongFileMode(int longFileMode) {
/* 178 */     this.longFileMode = longFileMode;
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
/*     */   public void setBigNumberMode(int bigNumberMode) {
/* 190 */     this.bigNumberMode = bigNumberMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddPaxHeadersForNonAsciiNames(boolean b) {
/* 199 */     this.addPaxHeadersForNonAsciiNames = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debugF) {
/* 208 */     this.debug = debugF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferDebug(boolean debug) {
/* 217 */     this.buffer.setDebug(debug);
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
/*     */   public void finish() throws IOException {
/* 230 */     if (this.finished) {
/* 231 */       throw new IOException("This archive has already been finished");
/*     */     }
/*     */     
/* 234 */     if (this.haveUnclosedEntry) {
/* 235 */       throw new IOException("This archives contains unclosed entries.");
/*     */     }
/* 237 */     writeEOFRecord();
/* 238 */     writeEOFRecord();
/* 239 */     this.buffer.flushBlock();
/* 240 */     this.finished = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 251 */     if (!this.finished) {
/* 252 */       finish();
/*     */     }
/*     */     
/* 255 */     if (!this.closed) {
/* 256 */       this.buffer.close();
/* 257 */       this.out.close();
/* 258 */       this.closed = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecordSize() {
/* 268 */     return this.buffer.getRecordSize();
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
/*     */   public void putNextEntry(TarEntry entry) throws IOException {
/* 284 */     if (this.finished) {
/* 285 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 287 */     Map<String, String> paxHeaders = new HashMap<>();
/* 288 */     String entryName = entry.getName();
/* 289 */     boolean paxHeaderContainsPath = handleLongName(entry, entryName, paxHeaders, "path", (byte)76, "file name");
/*     */ 
/*     */     
/* 292 */     String linkName = entry.getLinkName();
/*     */     
/* 294 */     boolean paxHeaderContainsLinkPath = (linkName != null && !linkName.isEmpty() && handleLongName(entry, linkName, paxHeaders, "linkpath", (byte)75, "link name"));
/*     */ 
/*     */     
/* 297 */     if (this.bigNumberMode == 2) {
/* 298 */       addPaxHeadersForBigNumbers(paxHeaders, entry);
/* 299 */     } else if (this.bigNumberMode != 1) {
/* 300 */       failForBigNumbers(entry);
/*     */     } 
/*     */     
/* 303 */     if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && 
/* 304 */       !ASCII.canEncode(entryName)) {
/* 305 */       paxHeaders.put("path", entryName);
/*     */     }
/*     */     
/* 308 */     if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && (entry
/* 309 */       .isLink() || entry.isSymbolicLink()) && 
/* 310 */       !ASCII.canEncode(linkName)) {
/* 311 */       paxHeaders.put("linkpath", linkName);
/*     */     }
/*     */     
/* 314 */     if (paxHeaders.size() > 0) {
/* 315 */       writePaxHeaders(entry, entryName, paxHeaders);
/*     */     }
/*     */     
/* 318 */     entry.writeEntryHeader(this.recordBuf, this.encoding, (this.bigNumberMode == 1));
/*     */     
/* 320 */     this.buffer.writeRecord(this.recordBuf);
/*     */     
/* 322 */     this.currBytes = 0L;
/*     */     
/* 324 */     if (entry.isDirectory()) {
/* 325 */       this.currSize = 0L;
/*     */     } else {
/* 327 */       this.currSize = entry.getSize();
/*     */     } 
/* 329 */     this.currName = entryName;
/* 330 */     this.haveUnclosedEntry = true;
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
/*     */   public void closeEntry() throws IOException {
/* 344 */     if (this.finished) {
/* 345 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 347 */     if (!this.haveUnclosedEntry) {
/* 348 */       throw new IOException("No current entry to close");
/*     */     }
/* 350 */     if (this.assemLen > 0) {
/* 351 */       for (int i = this.assemLen; i < this.assemBuf.length; i++) {
/* 352 */         this.assemBuf[i] = 0;
/*     */       }
/*     */       
/* 355 */       this.buffer.writeRecord(this.assemBuf);
/*     */       
/* 357 */       this.currBytes += this.assemLen;
/* 358 */       this.assemLen = 0;
/*     */     } 
/*     */     
/* 361 */     if (this.currBytes < this.currSize) {
/* 362 */       throw new IOException("entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 367 */     this.haveUnclosedEntry = false;
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
/*     */   public void write(int b) throws IOException {
/* 380 */     this.oneBuf[0] = (byte)b;
/*     */     
/* 382 */     write(this.oneBuf, 0, 1);
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
/*     */   public void write(byte[] wBuf) throws IOException {
/* 395 */     write(wBuf, 0, wBuf.length);
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
/*     */   public void write(byte[] wBuf, int wOffset, int numToWrite) throws IOException {
/* 414 */     if (this.currBytes + numToWrite > this.currSize) {
/* 415 */       throw new IOException("request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
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
/* 429 */     if (this.assemLen > 0) {
/* 430 */       if (this.assemLen + numToWrite >= this.recordBuf.length) {
/* 431 */         int aLen = this.recordBuf.length - this.assemLen;
/*     */         
/* 433 */         System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
/*     */         
/* 435 */         System.arraycopy(wBuf, wOffset, this.recordBuf, this.assemLen, aLen);
/*     */         
/* 437 */         this.buffer.writeRecord(this.recordBuf);
/*     */         
/* 439 */         this.currBytes += this.recordBuf.length;
/* 440 */         wOffset += aLen;
/* 441 */         numToWrite -= aLen;
/* 442 */         this.assemLen = 0;
/*     */       } else {
/* 444 */         System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
/*     */ 
/*     */         
/* 447 */         wOffset += numToWrite;
/* 448 */         this.assemLen += numToWrite;
/* 449 */         numToWrite = 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 458 */     while (numToWrite > 0) {
/* 459 */       if (numToWrite < this.recordBuf.length) {
/* 460 */         System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
/*     */ 
/*     */         
/* 463 */         this.assemLen += numToWrite;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 468 */       this.buffer.writeRecord(wBuf, wOffset);
/*     */       
/* 470 */       int num = this.recordBuf.length;
/*     */       
/* 472 */       this.currBytes += num;
/* 473 */       numToWrite -= num;
/* 474 */       wOffset += num;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writePaxHeaders(TarEntry entry, String entryName, Map<String, String> headers) throws IOException {
/* 484 */     String name = "./PaxHeaders.X/" + stripTo7Bits(entryName);
/* 485 */     if (name.length() >= 100) {
/* 486 */       name = name.substring(0, 99);
/*     */     }
/* 488 */     while (name.endsWith("/"))
/*     */     {
/*     */       
/* 491 */       name = name.substring(0, name.length() - 1);
/*     */     }
/* 493 */     TarEntry pex = new TarEntry(name, (byte)120);
/*     */     
/* 495 */     transferModTime(entry, pex);
/*     */     
/* 497 */     StringWriter w = new StringWriter();
/* 498 */     for (Map.Entry<String, String> h : headers.entrySet()) {
/* 499 */       String key = h.getKey();
/* 500 */       String value = h.getValue();
/* 501 */       int len = key.length() + value.length() + 3 + 2;
/*     */ 
/*     */       
/* 504 */       String line = len + " " + key + "=" + value + "\n";
/* 505 */       int actualLength = (line.getBytes(StandardCharsets.UTF_8)).length;
/* 506 */       while (len != actualLength) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 512 */         len = actualLength;
/* 513 */         line = len + " " + key + "=" + value + "\n";
/* 514 */         actualLength = (line.getBytes(StandardCharsets.UTF_8)).length;
/*     */       } 
/* 516 */       w.write(line);
/*     */     } 
/* 518 */     byte[] data = w.toString().getBytes(StandardCharsets.UTF_8);
/* 519 */     pex.setSize(data.length);
/* 520 */     putNextEntry(pex);
/* 521 */     write(data);
/* 522 */     closeEntry();
/*     */   }
/*     */   
/*     */   private String stripTo7Bits(String name) {
/* 526 */     StringBuilder result = new StringBuilder(name.length());
/* 527 */     for (char ch : name.toCharArray()) {
/* 528 */       char stripped = (char)(ch & 0x7F);
/* 529 */       if (stripped != '\000') {
/* 530 */         result.append(stripped);
/*     */       }
/*     */     } 
/* 533 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeEOFRecord() throws IOException {
/* 541 */     Arrays.fill(this.recordBuf, (byte)0);
/*     */     
/* 543 */     this.buffer.writeRecord(this.recordBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addPaxHeadersForBigNumbers(Map<String, String> paxHeaders, TarEntry entry) {
/* 548 */     addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(), 8589934591L);
/*     */     
/* 550 */     addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getLongGroupId(), 2097151L);
/*     */     
/* 552 */     addPaxHeaderForBigNumber(paxHeaders, "mtime", entry
/* 553 */         .getModTime().getTime() / 1000L, 8589934591L);
/*     */     
/* 555 */     addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getLongUserId(), 2097151L);
/*     */ 
/*     */     
/* 558 */     addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry
/* 559 */         .getDevMajor(), 2097151L);
/* 560 */     addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry
/* 561 */         .getDevMinor(), 2097151L);
/*     */     
/* 563 */     failForBigNumber("mode", entry.getMode(), 2097151L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPaxHeaderForBigNumber(Map<String, String> paxHeaders, String header, long value, long maxValue) {
/* 569 */     if (value < 0L || value > maxValue) {
/* 570 */       paxHeaders.put(header, String.valueOf(value));
/*     */     }
/*     */   }
/*     */   
/*     */   private void failForBigNumbers(TarEntry entry) {
/* 575 */     failForBigNumber("entry size", entry.getSize(), 8589934591L);
/* 576 */     failForBigNumberWithPosixMessage("group id", entry.getLongGroupId(), 2097151L);
/* 577 */     failForBigNumber("last modification time", entry
/* 578 */         .getModTime().getTime() / 1000L, 8589934591L);
/*     */     
/* 580 */     failForBigNumber("user id", entry.getLongUserId(), 2097151L);
/* 581 */     failForBigNumber("mode", entry.getMode(), 2097151L);
/* 582 */     failForBigNumber("major device number", entry.getDevMajor(), 2097151L);
/*     */     
/* 584 */     failForBigNumber("minor device number", entry.getDevMinor(), 2097151L);
/*     */   }
/*     */ 
/*     */   
/*     */   private void failForBigNumber(String field, long value, long maxValue) {
/* 589 */     failForBigNumber(field, value, maxValue, "");
/*     */   }
/*     */   
/*     */   private void failForBigNumberWithPosixMessage(String field, long value, long maxValue) {
/* 593 */     failForBigNumber(field, value, maxValue, " Use STAR or POSIX extensions to overcome this limit");
/*     */   }
/*     */   
/*     */   private void failForBigNumber(String field, long value, long maxValue, String additionalMsg) {
/* 597 */     if (value < 0L || value > maxValue) {
/* 598 */       throw new RuntimeException(field + " '" + value + "' is too big ( > " + maxValue + " )");
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
/*     */   private boolean handleLongName(TarEntry entry, String name, Map<String, String> paxHeaders, String paxHeaderName, byte linkType, String fieldName) throws IOException {
/* 630 */     ByteBuffer encodedName = this.encoding.encode(name);
/* 631 */     int len = encodedName.limit() - encodedName.position();
/* 632 */     if (len >= 100) {
/*     */       
/* 634 */       if (this.longFileMode == 3) {
/* 635 */         paxHeaders.put(paxHeaderName, name);
/* 636 */         return true;
/* 637 */       }  if (this.longFileMode == 2) {
/*     */ 
/*     */         
/* 640 */         TarEntry longLinkEntry = new TarEntry("././@LongLink", linkType);
/*     */ 
/*     */         
/* 643 */         longLinkEntry.setSize((len + 1));
/* 644 */         transferModTime(entry, longLinkEntry);
/* 645 */         putNextEntry(longLinkEntry);
/* 646 */         write(encodedName.array(), encodedName.arrayOffset(), len);
/* 647 */         write(0);
/* 648 */         closeEntry();
/* 649 */       } else if (this.longFileMode != 1) {
/* 650 */         throw new RuntimeException(fieldName + " '" + name + "' is too long ( > " + 'd' + " bytes)");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 655 */     return false;
/*     */   }
/*     */   
/*     */   private void transferModTime(TarEntry from, TarEntry to) {
/* 659 */     Date fromModTime = from.getModTime();
/* 660 */     long fromModTimeSeconds = fromModTime.getTime() / 1000L;
/* 661 */     if (fromModTimeSeconds < 0L || fromModTimeSeconds > 8589934591L) {
/* 662 */       fromModTime = new Date(0L);
/*     */     }
/* 664 */     to.setModTime(fromModTime);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */