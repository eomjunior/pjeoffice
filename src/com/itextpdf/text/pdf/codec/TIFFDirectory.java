/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TIFFDirectory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -168636766193675380L;
/*     */   boolean isBigEndian;
/*     */   int numEntries;
/*     */   TIFFField[] fields;
/*  92 */   Hashtable<Integer, Integer> fieldIndex = new Hashtable<Integer, Integer>();
/*     */ 
/*     */   
/*  95 */   long IFDOffset = 8L;
/*     */ 
/*     */   
/*  98 */   long nextIFDOffset = 0L;
/*     */ 
/*     */   
/*     */   TIFFDirectory() {}
/*     */   
/*     */   private static boolean isValidEndianTag(int endian) {
/* 104 */     return (endian == 18761 || endian == 19789);
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
/*     */   public TIFFDirectory(RandomAccessFileOrArray stream, int directory) throws IOException {
/* 120 */     long global_save_offset = stream.getFilePointer();
/*     */ 
/*     */ 
/*     */     
/* 124 */     stream.seek(0L);
/* 125 */     int endian = stream.readUnsignedShort();
/* 126 */     if (!isValidEndianTag(endian)) {
/* 127 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
/*     */     }
/* 129 */     this.isBigEndian = (endian == 19789);
/*     */     
/* 131 */     int magic = readUnsignedShort(stream);
/* 132 */     if (magic != 42) {
/* 133 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.magic.number.should.be.42", new Object[0]));
/*     */     }
/*     */ 
/*     */     
/* 137 */     long ifd_offset = readUnsignedInt(stream);
/*     */     
/* 139 */     for (int i = 0; i < directory; i++) {
/* 140 */       if (ifd_offset == 0L) {
/* 141 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("directory.number.too.large", new Object[0]));
/*     */       }
/*     */       
/* 144 */       stream.seek(ifd_offset);
/* 145 */       int entries = readUnsignedShort(stream);
/* 146 */       stream.skip((12 * entries));
/*     */       
/* 148 */       ifd_offset = readUnsignedInt(stream);
/*     */     } 
/*     */     
/* 151 */     stream.seek(ifd_offset);
/* 152 */     initialize(stream);
/* 153 */     stream.seek(global_save_offset);
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
/*     */   public TIFFDirectory(RandomAccessFileOrArray stream, long ifd_offset, int directory) throws IOException {
/* 172 */     long global_save_offset = stream.getFilePointer();
/* 173 */     stream.seek(0L);
/* 174 */     int endian = stream.readUnsignedShort();
/* 175 */     if (!isValidEndianTag(endian)) {
/* 176 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
/*     */     }
/* 178 */     this.isBigEndian = (endian == 19789);
/*     */ 
/*     */     
/* 181 */     stream.seek(ifd_offset);
/*     */ 
/*     */     
/* 184 */     int dirNum = 0;
/* 185 */     while (dirNum < directory) {
/*     */       
/* 187 */       int numEntries = readUnsignedShort(stream);
/*     */ 
/*     */       
/* 190 */       stream.seek(ifd_offset + (12 * numEntries));
/*     */ 
/*     */       
/* 193 */       ifd_offset = readUnsignedInt(stream);
/*     */ 
/*     */       
/* 196 */       stream.seek(ifd_offset);
/*     */ 
/*     */       
/* 199 */       dirNum++;
/*     */     } 
/*     */     
/* 202 */     initialize(stream);
/* 203 */     stream.seek(global_save_offset);
/*     */   }
/*     */   
/* 206 */   private static final int[] sizeOfType = new int[] { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(RandomAccessFileOrArray stream) throws IOException {
/* 223 */     long nextTagOffset = 0L;
/* 224 */     long maxOffset = stream.length();
/*     */ 
/*     */     
/* 227 */     this.IFDOffset = stream.getFilePointer();
/*     */     
/* 229 */     this.numEntries = readUnsignedShort(stream);
/* 230 */     this.fields = new TIFFField[this.numEntries];
/*     */     
/* 232 */     for (int i = 0; i < this.numEntries && nextTagOffset < maxOffset; i++) {
/* 233 */       int tag = readUnsignedShort(stream);
/* 234 */       int type = readUnsignedShort(stream);
/* 235 */       int count = (int)readUnsignedInt(stream);
/* 236 */       boolean processTag = true;
/*     */ 
/*     */       
/* 239 */       nextTagOffset = stream.getFilePointer() + 4L;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 244 */         if (count * sizeOfType[type] > 4) {
/* 245 */           long valueOffset = readUnsignedInt(stream);
/*     */ 
/*     */           
/* 248 */           if (valueOffset < maxOffset) {
/* 249 */             stream.seek(valueOffset);
/*     */           }
/*     */           else {
/*     */             
/* 253 */             processTag = false;
/*     */           } 
/*     */         } 
/* 256 */       } catch (ArrayIndexOutOfBoundsException ae) {
/*     */         
/* 258 */         processTag = false;
/*     */       } 
/*     */       
/* 261 */       if (processTag) {
/* 262 */         int j; byte[] bvalues; char[] cvalues; long lvalues[], llvalues[][]; short[] svalues; int ivalues[], iivalues[][]; float[] fvalues; double[] dvalues; this.fieldIndex.put(Integer.valueOf(tag), Integer.valueOf(i));
/* 263 */         Object obj = null;
/*     */         
/* 265 */         switch (type) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 6:
/*     */           case 7:
/* 270 */             bvalues = new byte[count];
/* 271 */             stream.readFully(bvalues, 0, count);
/*     */             
/* 273 */             if (type == 2) {
/*     */ 
/*     */               
/* 276 */               int index = 0, prevIndex = 0;
/* 277 */               ArrayList<String> v = new ArrayList<String>();
/*     */               
/* 279 */               while (index < count) {
/*     */                 
/* 281 */                 while (index < count && bvalues[index++] != 0);
/*     */ 
/*     */                 
/* 284 */                 v.add(new String(bvalues, prevIndex, index - prevIndex));
/*     */                 
/* 286 */                 prevIndex = index;
/*     */               } 
/*     */               
/* 289 */               count = v.size();
/* 290 */               String[] strings = new String[count];
/* 291 */               for (int c = 0; c < count; c++) {
/* 292 */                 strings[c] = v.get(c);
/*     */               }
/*     */               
/* 295 */               obj = strings; break;
/*     */             } 
/* 297 */             obj = bvalues;
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case 3:
/* 303 */             cvalues = new char[count];
/* 304 */             for (j = 0; j < count; j++) {
/* 305 */               cvalues[j] = (char)readUnsignedShort(stream);
/*     */             }
/* 307 */             obj = cvalues;
/*     */             break;
/*     */           
/*     */           case 4:
/* 311 */             lvalues = new long[count];
/* 312 */             for (j = 0; j < count; j++) {
/* 313 */               lvalues[j] = readUnsignedInt(stream);
/*     */             }
/* 315 */             obj = lvalues;
/*     */             break;
/*     */           
/*     */           case 5:
/* 319 */             llvalues = new long[count][2];
/* 320 */             for (j = 0; j < count; j++) {
/* 321 */               llvalues[j][0] = readUnsignedInt(stream);
/* 322 */               llvalues[j][1] = readUnsignedInt(stream);
/*     */             } 
/* 324 */             obj = llvalues;
/*     */             break;
/*     */           
/*     */           case 8:
/* 328 */             svalues = new short[count];
/* 329 */             for (j = 0; j < count; j++) {
/* 330 */               svalues[j] = readShort(stream);
/*     */             }
/* 332 */             obj = svalues;
/*     */             break;
/*     */           
/*     */           case 9:
/* 336 */             ivalues = new int[count];
/* 337 */             for (j = 0; j < count; j++) {
/* 338 */               ivalues[j] = readInt(stream);
/*     */             }
/* 340 */             obj = ivalues;
/*     */             break;
/*     */           
/*     */           case 10:
/* 344 */             iivalues = new int[count][2];
/* 345 */             for (j = 0; j < count; j++) {
/* 346 */               iivalues[j][0] = readInt(stream);
/* 347 */               iivalues[j][1] = readInt(stream);
/*     */             } 
/* 349 */             obj = iivalues;
/*     */             break;
/*     */           
/*     */           case 11:
/* 353 */             fvalues = new float[count];
/* 354 */             for (j = 0; j < count; j++) {
/* 355 */               fvalues[j] = readFloat(stream);
/*     */             }
/* 357 */             obj = fvalues;
/*     */             break;
/*     */           
/*     */           case 12:
/* 361 */             dvalues = new double[count];
/* 362 */             for (j = 0; j < count; j++) {
/* 363 */               dvalues[j] = readDouble(stream);
/*     */             }
/* 365 */             obj = dvalues;
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 372 */         this.fields[i] = new TIFFField(tag, type, count, obj);
/*     */       } 
/*     */       
/* 375 */       stream.seek(nextTagOffset);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 380 */       this.nextIFDOffset = readUnsignedInt(stream);
/*     */     }
/* 382 */     catch (Exception e) {
/*     */       
/* 384 */       this.nextIFDOffset = 0L;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumEntries() {
/* 390 */     return this.numEntries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TIFFField getField(int tag) {
/* 398 */     Integer i = this.fieldIndex.get(Integer.valueOf(tag));
/* 399 */     if (i == null) {
/* 400 */       return null;
/*     */     }
/* 402 */     return this.fields[i.intValue()];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTagPresent(int tag) {
/* 410 */     return this.fieldIndex.containsKey(Integer.valueOf(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getTags() {
/* 418 */     int[] tags = new int[this.fieldIndex.size()];
/* 419 */     Enumeration<Integer> e = this.fieldIndex.keys();
/* 420 */     int i = 0;
/*     */     
/* 422 */     while (e.hasMoreElements()) {
/* 423 */       tags[i++] = ((Integer)e.nextElement()).intValue();
/*     */     }
/*     */     
/* 426 */     return tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TIFFField[] getFields() {
/* 434 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getFieldAsByte(int tag, int index) {
/* 444 */     Integer i = this.fieldIndex.get(Integer.valueOf(tag));
/* 445 */     byte[] b = this.fields[i.intValue()].getAsBytes();
/* 446 */     return b[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getFieldAsByte(int tag) {
/* 456 */     return getFieldAsByte(tag, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFieldAsLong(int tag, int index) {
/* 466 */     Integer i = this.fieldIndex.get(Integer.valueOf(tag));
/* 467 */     return this.fields[i.intValue()].getAsLong(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFieldAsLong(int tag) {
/* 477 */     return getFieldAsLong(tag, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFieldAsFloat(int tag, int index) {
/* 487 */     Integer i = this.fieldIndex.get(Integer.valueOf(tag));
/* 488 */     return this.fields[i.intValue()].getAsFloat(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFieldAsFloat(int tag) {
/* 497 */     return getFieldAsFloat(tag, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFieldAsDouble(int tag, int index) {
/* 507 */     Integer i = this.fieldIndex.get(Integer.valueOf(tag));
/* 508 */     return this.fields[i.intValue()].getAsDouble(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFieldAsDouble(int tag) {
/* 517 */     return getFieldAsDouble(tag, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short readShort(RandomAccessFileOrArray stream) throws IOException {
/* 524 */     if (this.isBigEndian) {
/* 525 */       return stream.readShort();
/*     */     }
/* 527 */     return stream.readShortLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int readUnsignedShort(RandomAccessFileOrArray stream) throws IOException {
/* 533 */     if (this.isBigEndian) {
/* 534 */       return stream.readUnsignedShort();
/*     */     }
/* 536 */     return stream.readUnsignedShortLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int readInt(RandomAccessFileOrArray stream) throws IOException {
/* 542 */     if (this.isBigEndian) {
/* 543 */       return stream.readInt();
/*     */     }
/* 545 */     return stream.readIntLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long readUnsignedInt(RandomAccessFileOrArray stream) throws IOException {
/* 551 */     if (this.isBigEndian) {
/* 552 */       return stream.readUnsignedInt();
/*     */     }
/* 554 */     return stream.readUnsignedIntLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long readLong(RandomAccessFileOrArray stream) throws IOException {
/* 560 */     if (this.isBigEndian) {
/* 561 */       return stream.readLong();
/*     */     }
/* 563 */     return stream.readLongLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float readFloat(RandomAccessFileOrArray stream) throws IOException {
/* 569 */     if (this.isBigEndian) {
/* 570 */       return stream.readFloat();
/*     */     }
/* 572 */     return stream.readFloatLE();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double readDouble(RandomAccessFileOrArray stream) throws IOException {
/* 578 */     if (this.isBigEndian) {
/* 579 */       return stream.readDouble();
/*     */     }
/* 581 */     return stream.readDoubleLE();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int readUnsignedShort(RandomAccessFileOrArray stream, boolean isBigEndian) throws IOException {
/* 588 */     if (isBigEndian) {
/* 589 */       return stream.readUnsignedShort();
/*     */     }
/* 591 */     return stream.readUnsignedShortLE();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long readUnsignedInt(RandomAccessFileOrArray stream, boolean isBigEndian) throws IOException {
/* 598 */     if (isBigEndian) {
/* 599 */       return stream.readUnsignedInt();
/*     */     }
/* 601 */     return stream.readUnsignedIntLE();
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
/*     */   public static int getNumDirectories(RandomAccessFileOrArray stream) throws IOException {
/* 613 */     long pointer = stream.getFilePointer();
/*     */     
/* 615 */     stream.seek(0L);
/* 616 */     int endian = stream.readUnsignedShort();
/* 617 */     if (!isValidEndianTag(endian)) {
/* 618 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.endianness.tag.not.0x4949.or.0x4d4d", new Object[0]));
/*     */     }
/* 620 */     boolean isBigEndian = (endian == 19789);
/* 621 */     int magic = readUnsignedShort(stream, isBigEndian);
/* 622 */     if (magic != 42) {
/* 623 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.magic.number.should.be.42", new Object[0]));
/*     */     }
/*     */     
/* 626 */     stream.seek(4L);
/* 627 */     long offset = readUnsignedInt(stream, isBigEndian);
/*     */     
/* 629 */     int numDirectories = 0;
/* 630 */     while (offset != 0L) {
/* 631 */       numDirectories++;
/*     */ 
/*     */       
/*     */       try {
/* 635 */         stream.seek(offset);
/* 636 */         int entries = readUnsignedShort(stream, isBigEndian);
/* 637 */         stream.skip((12 * entries));
/* 638 */         offset = readUnsignedInt(stream, isBigEndian);
/* 639 */       } catch (EOFException eof) {
/* 640 */         numDirectories--;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 645 */     stream.seek(pointer);
/* 646 */     return numDirectories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBigEndian() {
/* 655 */     return this.isBigEndian;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIFDOffset() {
/* 663 */     return this.IFDOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNextIFDOffset() {
/* 671 */     return this.nextIFDOffset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TIFFDirectory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */