/*     */ package org.apache.tools.tar;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public class TarInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private static final int SMALL_BUFFER_SIZE = 256;
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int LARGE_BUFFER_SIZE = 32768;
/*     */   private static final int BYTE_MASK = 255;
/*  51 */   private final byte[] SKIP_BUF = new byte[8192];
/*  52 */   private final byte[] SMALL_BUF = new byte[256];
/*     */ 
/*     */   
/*     */   protected boolean debug;
/*     */ 
/*     */   
/*     */   protected boolean hasHitEOF;
/*     */ 
/*     */   
/*     */   protected long entrySize;
/*     */ 
/*     */   
/*     */   protected long entryOffset;
/*     */ 
/*     */   
/*     */   protected byte[] readBuf;
/*     */   
/*     */   protected TarBuffer buffer;
/*     */   
/*     */   protected TarEntry currEntry;
/*     */   
/*     */   protected byte[] oneBuf;
/*     */   
/*     */   private final ZipEncoding encoding;
/*     */ 
/*     */   
/*     */   public TarInputStream(InputStream is) {
/*  79 */     this(is, 10240, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarInputStream(InputStream is, String encoding) {
/*  88 */     this(is, 10240, 512, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarInputStream(InputStream is, int blockSize) {
/*  97 */     this(is, blockSize, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarInputStream(InputStream is, int blockSize, String encoding) {
/* 107 */     this(is, blockSize, 512, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarInputStream(InputStream is, int blockSize, int recordSize) {
/* 117 */     this(is, blockSize, recordSize, null);
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
/*     */   public TarInputStream(InputStream is, int blockSize, int recordSize, String encoding) {
/* 129 */     super(is);
/* 130 */     this.buffer = new TarBuffer(is, blockSize, recordSize);
/* 131 */     this.readBuf = null;
/* 132 */     this.oneBuf = new byte[1];
/* 133 */     this.debug = false;
/* 134 */     this.hasHitEOF = false;
/* 135 */     this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 144 */     this.debug = debug;
/* 145 */     this.buffer.setDebug(debug);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 154 */     this.buffer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecordSize() {
/* 163 */     return this.buffer.getRecordSize();
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
/*     */   public int available() throws IOException {
/* 180 */     if (isDirectory()) {
/* 181 */       return 0;
/*     */     }
/* 183 */     if (this.entrySize - this.entryOffset > 2147483647L) {
/* 184 */       return Integer.MAX_VALUE;
/*     */     }
/* 186 */     return (int)(this.entrySize - this.entryOffset);
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
/*     */   public long skip(long numToSkip) throws IOException {
/* 201 */     if (numToSkip <= 0L || isDirectory()) {
/* 202 */       return 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     long skip = numToSkip;
/* 209 */     while (skip > 0L) {
/*     */       
/* 211 */       int realSkip = (int)((skip > this.SKIP_BUF.length) ? this.SKIP_BUF.length : skip);
/* 212 */       int numRead = read(this.SKIP_BUF, 0, realSkip);
/* 213 */       if (numRead == -1) {
/*     */         break;
/*     */       }
/* 216 */       skip -= numRead;
/*     */     } 
/* 218 */     return numToSkip - skip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 228 */     return false;
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
/*     */   public void mark(int markLimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarEntry getNextEntry() throws IOException {
/* 261 */     if (this.hasHitEOF) {
/* 262 */       return null;
/*     */     }
/*     */     
/* 265 */     if (this.currEntry != null) {
/* 266 */       long numToSkip = this.entrySize - this.entryOffset;
/*     */       
/* 268 */       if (this.debug) {
/* 269 */         System.err.println("TarInputStream: SKIP currENTRY '" + this.currEntry
/* 270 */             .getName() + "' SZ " + this.entrySize + " OFF " + this.entryOffset + "  skipping " + numToSkip + " bytes");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 276 */       while (numToSkip > 0L) {
/* 277 */         long skipped = skip(numToSkip);
/* 278 */         if (skipped <= 0L) {
/* 279 */           throw new IOException("failed to skip current tar entry");
/*     */         }
/*     */         
/* 282 */         numToSkip -= skipped;
/*     */       } 
/*     */       
/* 285 */       this.readBuf = null;
/*     */     } 
/*     */     
/* 288 */     byte[] headerBuf = getRecord();
/*     */     
/* 290 */     if (this.hasHitEOF) {
/* 291 */       this.currEntry = null;
/* 292 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 296 */       this.currEntry = new TarEntry(headerBuf, this.encoding);
/* 297 */     } catch (IllegalArgumentException e) {
/* 298 */       throw new IOException("Error detected parsing the header", e);
/*     */     } 
/* 300 */     if (this.debug) {
/* 301 */       System.err.println("TarInputStream: SET CURRENTRY '" + this.currEntry
/* 302 */           .getName() + "' size = " + this.currEntry
/*     */           
/* 304 */           .getSize());
/*     */     }
/*     */     
/* 307 */     this.entryOffset = 0L;
/* 308 */     this.entrySize = this.currEntry.getSize();
/*     */     
/* 310 */     if (this.currEntry.isGNULongLinkEntry()) {
/* 311 */       byte[] longLinkData = getLongNameData();
/* 312 */       if (longLinkData == null)
/*     */       {
/*     */ 
/*     */         
/* 316 */         return null;
/*     */       }
/* 318 */       this.currEntry.setLinkName(this.encoding.decode(longLinkData));
/*     */     } 
/*     */     
/* 321 */     if (this.currEntry.isGNULongNameEntry()) {
/* 322 */       byte[] longNameData = getLongNameData();
/* 323 */       if (longNameData == null)
/*     */       {
/*     */ 
/*     */         
/* 327 */         return null;
/*     */       }
/* 329 */       this.currEntry.setName(this.encoding.decode(longNameData));
/*     */     } 
/*     */     
/* 332 */     if (this.currEntry.isPaxHeader()) {
/* 333 */       paxHeaders();
/*     */     }
/*     */     
/* 336 */     if (this.currEntry.isGNUSparse()) {
/* 337 */       readGNUSparse();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 344 */     this.entrySize = this.currEntry.getSize();
/* 345 */     return this.currEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] getLongNameData() throws IOException {
/* 356 */     ByteArrayOutputStream longName = new ByteArrayOutputStream();
/* 357 */     int length = 0;
/* 358 */     while ((length = read(this.SMALL_BUF)) >= 0) {
/* 359 */       longName.write(this.SMALL_BUF, 0, length);
/*     */     }
/* 361 */     getNextEntry();
/* 362 */     if (this.currEntry == null)
/*     */     {
/*     */       
/* 365 */       return null;
/*     */     }
/* 367 */     byte[] longNameData = longName.toByteArray();
/*     */     
/* 369 */     length = longNameData.length;
/* 370 */     while (length > 0 && longNameData[length - 1] == 0) {
/* 371 */       length--;
/*     */     }
/* 373 */     if (length != longNameData.length) {
/* 374 */       byte[] l = new byte[length];
/* 375 */       System.arraycopy(longNameData, 0, l, 0, length);
/* 376 */       longNameData = l;
/*     */     } 
/* 378 */     return longNameData;
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
/*     */   private byte[] getRecord() throws IOException {
/* 394 */     if (this.hasHitEOF) {
/* 395 */       return null;
/*     */     }
/*     */     
/* 398 */     byte[] headerBuf = this.buffer.readRecord();
/*     */     
/* 400 */     if (headerBuf == null) {
/* 401 */       if (this.debug) {
/* 402 */         System.err.println("READ NULL RECORD");
/*     */       }
/* 404 */       this.hasHitEOF = true;
/* 405 */     } else if (this.buffer.isEOFRecord(headerBuf)) {
/* 406 */       if (this.debug) {
/* 407 */         System.err.println("READ EOF RECORD");
/*     */       }
/* 409 */       this.hasHitEOF = true;
/*     */     } 
/*     */     
/* 412 */     return this.hasHitEOF ? null : headerBuf;
/*     */   }
/*     */   
/*     */   private void paxHeaders() throws IOException {
/* 416 */     Map<String, String> headers = parsePaxHeaders(this);
/* 417 */     getNextEntry();
/* 418 */     applyPaxHeadersToCurrentEntry(headers);
/*     */   }
/*     */   Map<String, String> parsePaxHeaders(InputStream i) throws IOException {
/*     */     int ch;
/* 422 */     Map<String, String> headers = new HashMap<>();
/*     */ 
/*     */     
/*     */     do {
/* 426 */       int len = 0;
/* 427 */       int read = 0;
/* 428 */       while ((ch = i.read()) != -1) {
/* 429 */         read++;
/* 430 */         if (ch == 32) {
/*     */           
/* 432 */           ByteArrayOutputStream coll = new ByteArrayOutputStream();
/* 433 */           while ((ch = i.read()) != -1) {
/* 434 */             read++;
/* 435 */             if (ch == 61) {
/* 436 */               String keyword = coll.toString("UTF-8");
/*     */               
/* 438 */               int restLen = len - read;
/* 439 */               ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 440 */               int got = 0;
/* 441 */               while (got < restLen && (ch = i.read()) != -1) {
/* 442 */                 bos.write((byte)ch);
/* 443 */                 got++;
/*     */               } 
/* 445 */               bos.close();
/* 446 */               if (got != restLen) {
/* 447 */                 throw new IOException("Failed to read Paxheader. Expected " + restLen + " bytes, read " + got);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 453 */               byte[] rest = bos.toByteArray();
/*     */               
/* 455 */               String value = new String(rest, 0, restLen - 1, StandardCharsets.UTF_8);
/*     */               
/* 457 */               headers.put(keyword, value);
/*     */               break;
/*     */             } 
/* 460 */             coll.write((byte)ch);
/*     */           } 
/*     */           break;
/*     */         } 
/* 464 */         len *= 10;
/* 465 */         len += ch - 48;
/*     */       } 
/* 467 */     } while (ch != -1);
/*     */ 
/*     */ 
/*     */     
/* 471 */     return headers;
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
/*     */   private void applyPaxHeadersToCurrentEntry(Map<String, String> headers) {
/* 486 */     headers.forEach((key, val) -> {
/*     */           switch (key) {
/*     */             case "path":
/*     */               this.currEntry.setName(val);
/*     */               break;
/*     */             case "linkpath":
/*     */               this.currEntry.setLinkName(val);
/*     */               break;
/*     */             case "gid":
/*     */               this.currEntry.setGroupId(Long.parseLong(val));
/*     */               break;
/*     */             case "gname":
/*     */               this.currEntry.setGroupName(val);
/*     */               break;
/*     */             case "uid":
/*     */               this.currEntry.setUserId(Long.parseLong(val));
/*     */               break;
/*     */             case "uname":
/*     */               this.currEntry.setUserName(val);
/*     */               break;
/*     */             case "size":
/*     */               this.currEntry.setSize(Long.parseLong(val));
/*     */               break;
/*     */             case "mtime":
/*     */               this.currEntry.setModTime((long)(Double.parseDouble(val) * 1000.0D));
/*     */               break;
/*     */             case "SCHILY.devminor":
/*     */               this.currEntry.setDevMinor(Integer.parseInt(val));
/*     */               break;
/*     */             case "SCHILY.devmajor":
/*     */               this.currEntry.setDevMajor(Integer.parseInt(val));
/*     */               break;
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
/*     */   private void readGNUSparse() throws IOException {
/* 535 */     if (this.currEntry.isExtended()) {
/*     */       TarArchiveSparseEntry entry;
/*     */       do {
/* 538 */         byte[] headerBuf = getRecord();
/* 539 */         if (this.hasHitEOF) {
/* 540 */           this.currEntry = null;
/*     */           break;
/*     */         } 
/* 543 */         entry = new TarArchiveSparseEntry(headerBuf);
/*     */ 
/*     */       
/*     */       }
/* 547 */       while (entry.isExtended());
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
/*     */   public int read() throws IOException {
/* 561 */     int num = read(this.oneBuf, 0, 1);
/* 562 */     return (num == -1) ? -1 : (this.oneBuf[0] & 0xFF);
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
/*     */   public int read(byte[] buf, int offset, int numToRead) throws IOException {
/* 580 */     int totalRead = 0;
/*     */     
/* 582 */     if (this.entryOffset >= this.entrySize || isDirectory()) {
/* 583 */       return -1;
/*     */     }
/*     */     
/* 586 */     if (numToRead + this.entryOffset > this.entrySize) {
/* 587 */       numToRead = (int)(this.entrySize - this.entryOffset);
/*     */     }
/*     */     
/* 590 */     if (this.readBuf != null) {
/*     */       
/* 592 */       int sz = (numToRead > this.readBuf.length) ? this.readBuf.length : numToRead;
/*     */       
/* 594 */       System.arraycopy(this.readBuf, 0, buf, offset, sz);
/*     */       
/* 596 */       if (sz >= this.readBuf.length) {
/* 597 */         this.readBuf = null;
/*     */       } else {
/* 599 */         int newLen = this.readBuf.length - sz;
/* 600 */         byte[] newBuf = new byte[newLen];
/*     */         
/* 602 */         System.arraycopy(this.readBuf, sz, newBuf, 0, newLen);
/*     */         
/* 604 */         this.readBuf = newBuf;
/*     */       } 
/*     */       
/* 607 */       totalRead += sz;
/* 608 */       numToRead -= sz;
/* 609 */       offset += sz;
/*     */     } 
/*     */     
/* 612 */     while (numToRead > 0) {
/* 613 */       byte[] rec = this.buffer.readRecord();
/*     */       
/* 615 */       if (rec == null)
/*     */       {
/* 617 */         throw new IOException("unexpected EOF with " + numToRead + " bytes unread");
/*     */       }
/*     */ 
/*     */       
/* 621 */       int sz = numToRead;
/* 622 */       int recLen = rec.length;
/*     */       
/* 624 */       if (recLen > sz) {
/* 625 */         System.arraycopy(rec, 0, buf, offset, sz);
/*     */         
/* 627 */         this.readBuf = new byte[recLen - sz];
/*     */         
/* 629 */         System.arraycopy(rec, sz, this.readBuf, 0, recLen - sz);
/*     */       } else {
/* 631 */         sz = recLen;
/*     */         
/* 633 */         System.arraycopy(rec, 0, buf, offset, recLen);
/*     */       } 
/*     */       
/* 636 */       totalRead += sz;
/* 637 */       numToRead -= sz;
/* 638 */       offset += sz;
/*     */     } 
/*     */     
/* 641 */     this.entryOffset += totalRead;
/*     */     
/* 643 */     return totalRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyEntryContents(OutputStream out) throws IOException {
/* 654 */     byte[] buf = new byte[32768];
/*     */     
/*     */     while (true) {
/* 657 */       int numRead = read(buf, 0, buf.length);
/*     */       
/* 659 */       if (numRead == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 663 */       out.write(buf, 0, numRead);
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
/*     */   public boolean canReadEntryData(TarEntry te) {
/* 676 */     return !te.isGNUSparse();
/*     */   }
/*     */   
/*     */   private boolean isDirectory() {
/* 680 */     return (this.currEntry != null && this.currEntry.isDirectory());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */