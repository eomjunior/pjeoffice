/*     */ package org.apache.tools.tar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarBuffer
/*     */ {
/*     */   public static final int DEFAULT_RCDSIZE = 512;
/*     */   public static final int DEFAULT_BLKSIZE = 10240;
/*     */   private InputStream inStream;
/*     */   private OutputStream outStream;
/*     */   private final int blockSize;
/*     */   private final int recordSize;
/*     */   private final int recsPerBlock;
/*     */   private final byte[] blockBuffer;
/*     */   private int currBlkIdx;
/*     */   private int currRecIdx;
/*     */   private boolean debug;
/*     */   
/*     */   public TarBuffer(InputStream inStream) {
/*  68 */     this(inStream, 10240);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarBuffer(InputStream inStream, int blockSize) {
/*  77 */     this(inStream, blockSize, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarBuffer(InputStream inStream, int blockSize, int recordSize) {
/*  87 */     this(inStream, null, blockSize, recordSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarBuffer(OutputStream outStream) {
/*  95 */     this(outStream, 10240);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarBuffer(OutputStream outStream, int blockSize) {
/* 104 */     this(outStream, blockSize, 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TarBuffer(OutputStream outStream, int blockSize, int recordSize) {
/* 114 */     this(null, outStream, blockSize, recordSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TarBuffer(InputStream inStream, OutputStream outStream, int blockSize, int recordSize) {
/* 121 */     this.inStream = inStream;
/* 122 */     this.outStream = outStream;
/* 123 */     this.debug = false;
/* 124 */     this.blockSize = blockSize;
/* 125 */     this.recordSize = recordSize;
/* 126 */     this.recsPerBlock = this.blockSize / this.recordSize;
/* 127 */     this.blockBuffer = new byte[this.blockSize];
/*     */     
/* 129 */     if (this.inStream != null) {
/* 130 */       this.currBlkIdx = -1;
/* 131 */       this.currRecIdx = this.recsPerBlock;
/*     */     } else {
/* 133 */       this.currBlkIdx = 0;
/* 134 */       this.currRecIdx = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockSize() {
/* 143 */     return this.blockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecordSize() {
/* 151 */     return this.recordSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 160 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEOFRecord(byte[] record) {
/* 171 */     for (int i = 0, sz = getRecordSize(); i < sz; i++) {
/* 172 */       if (record[i] != 0) {
/* 173 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipRecord() throws IOException {
/* 185 */     if (this.debug) {
/* 186 */       System.err.println("SkipRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */ 
/*     */     
/* 190 */     if (this.inStream == null) {
/* 191 */       throw new IOException("reading (via skip) from an output buffer");
/*     */     }
/*     */     
/* 194 */     if (this.currRecIdx >= this.recsPerBlock && !readBlock()) {
/*     */       return;
/*     */     }
/*     */     
/* 198 */     this.currRecIdx++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] readRecord() throws IOException {
/* 208 */     if (this.debug) {
/* 209 */       System.err.println("ReadRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */ 
/*     */     
/* 213 */     if (this.inStream == null) {
/* 214 */       if (this.outStream == null) {
/* 215 */         throw new IOException("input buffer is closed");
/*     */       }
/* 217 */       throw new IOException("reading from an output buffer");
/*     */     } 
/*     */     
/* 220 */     if (this.currRecIdx >= this.recsPerBlock && !readBlock()) {
/* 221 */       return null;
/*     */     }
/*     */     
/* 224 */     byte[] result = new byte[this.recordSize];
/*     */     
/* 226 */     System.arraycopy(this.blockBuffer, this.currRecIdx * this.recordSize, result, 0, this.recordSize);
/*     */ 
/*     */ 
/*     */     
/* 230 */     this.currRecIdx++;
/*     */     
/* 232 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean readBlock() throws IOException {
/* 239 */     if (this.debug) {
/* 240 */       System.err.println("ReadBlock: blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */     
/* 243 */     if (this.inStream == null) {
/* 244 */       throw new IOException("reading from an output buffer");
/*     */     }
/*     */     
/* 247 */     this.currRecIdx = 0;
/*     */     
/* 249 */     int offset = 0;
/* 250 */     int bytesNeeded = this.blockSize;
/*     */     
/* 252 */     while (bytesNeeded > 0) {
/* 253 */       long numBytes = this.inStream.read(this.blockBuffer, offset, bytesNeeded);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 269 */       if (numBytes == -1L) {
/* 270 */         if (offset == 0)
/*     */         {
/*     */ 
/*     */           
/* 274 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 282 */         Arrays.fill(this.blockBuffer, offset, offset + bytesNeeded, (byte)0);
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 287 */       offset = (int)(offset + numBytes);
/* 288 */       bytesNeeded = (int)(bytesNeeded - numBytes);
/*     */       
/* 290 */       if (numBytes != this.blockSize && 
/* 291 */         this.debug) {
/* 292 */         System.err.println("ReadBlock: INCOMPLETE READ " + numBytes + " of " + this.blockSize + " bytes read.");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     this.currBlkIdx++;
/*     */     
/* 301 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentBlockNum() {
/* 310 */     return this.currBlkIdx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCurrentRecordNum() {
/* 320 */     return this.currRecIdx - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeRecord(byte[] record) throws IOException {
/* 330 */     if (this.debug) {
/* 331 */       System.err.println("WriteRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */ 
/*     */     
/* 335 */     if (this.outStream == null) {
/* 336 */       if (this.inStream == null) {
/* 337 */         throw new IOException("Output buffer is closed");
/*     */       }
/* 339 */       throw new IOException("writing to an input buffer");
/*     */     } 
/*     */     
/* 342 */     if (record.length != this.recordSize) {
/* 343 */       throw new IOException("record to write has length '" + record.length + "' which is not the record size of '" + this.recordSize + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 349 */     if (this.currRecIdx >= this.recsPerBlock) {
/* 350 */       writeBlock();
/*     */     }
/*     */     
/* 353 */     System.arraycopy(record, 0, this.blockBuffer, this.currRecIdx * this.recordSize, this.recordSize);
/*     */ 
/*     */ 
/*     */     
/* 357 */     this.currRecIdx++;
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
/*     */   public void writeRecord(byte[] buf, int offset) throws IOException {
/* 370 */     if (this.debug) {
/* 371 */       System.err.println("WriteRecord: recIdx = " + this.currRecIdx + " blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */ 
/*     */     
/* 375 */     if (this.outStream == null) {
/* 376 */       if (this.inStream == null) {
/* 377 */         throw new IOException("Output buffer is closed");
/*     */       }
/* 379 */       throw new IOException("writing to an input buffer");
/*     */     } 
/*     */     
/* 382 */     if (offset + this.recordSize > buf.length) {
/* 383 */       throw new IOException("record has length '" + buf.length + "' with offset '" + offset + "' which is less than the record size of '" + this.recordSize + "'");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     if (this.currRecIdx >= this.recsPerBlock) {
/* 390 */       writeBlock();
/*     */     }
/*     */     
/* 393 */     System.arraycopy(buf, offset, this.blockBuffer, this.currRecIdx * this.recordSize, this.recordSize);
/*     */ 
/*     */ 
/*     */     
/* 397 */     this.currRecIdx++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeBlock() throws IOException {
/* 404 */     if (this.debug) {
/* 405 */       System.err.println("WriteBlock: blkIdx = " + this.currBlkIdx);
/*     */     }
/*     */     
/* 408 */     if (this.outStream == null) {
/* 409 */       throw new IOException("writing to an input buffer");
/*     */     }
/*     */     
/* 412 */     this.outStream.write(this.blockBuffer, 0, this.blockSize);
/* 413 */     this.outStream.flush();
/*     */     
/* 415 */     this.currRecIdx = 0;
/* 416 */     this.currBlkIdx++;
/* 417 */     Arrays.fill(this.blockBuffer, (byte)0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void flushBlock() throws IOException {
/* 424 */     if (this.debug) {
/* 425 */       System.err.println("TarBuffer.flushBlock() called.");
/*     */     }
/*     */     
/* 428 */     if (this.outStream == null) {
/* 429 */       throw new IOException("writing to an input buffer");
/*     */     }
/*     */     
/* 432 */     if (this.currRecIdx > 0) {
/* 433 */       writeBlock();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 443 */     if (this.debug) {
/* 444 */       System.err.println("TarBuffer.closeBuffer().");
/*     */     }
/*     */     
/* 447 */     if (this.outStream != null) {
/* 448 */       flushBlock();
/*     */       
/* 450 */       if (this.outStream != System.out && this.outStream != System.err) {
/*     */         
/* 452 */         this.outStream.close();
/*     */         
/* 454 */         this.outStream = null;
/*     */       } 
/* 456 */     } else if (this.inStream != null) {
/* 457 */       if (this.inStream != System.in) {
/* 458 */         this.inStream.close();
/*     */       }
/* 460 */       this.inStream = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */