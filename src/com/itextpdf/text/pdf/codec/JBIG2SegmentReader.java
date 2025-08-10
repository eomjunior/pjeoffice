/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JBIG2SegmentReader
/*     */ {
/*     */   public static final int SYMBOL_DICTIONARY = 0;
/*     */   public static final int INTERMEDIATE_TEXT_REGION = 4;
/*     */   public static final int IMMEDIATE_TEXT_REGION = 6;
/*     */   public static final int IMMEDIATE_LOSSLESS_TEXT_REGION = 7;
/*     */   public static final int PATTERN_DICTIONARY = 16;
/*     */   public static final int INTERMEDIATE_HALFTONE_REGION = 20;
/*     */   public static final int IMMEDIATE_HALFTONE_REGION = 22;
/*     */   public static final int IMMEDIATE_LOSSLESS_HALFTONE_REGION = 23;
/*     */   public static final int INTERMEDIATE_GENERIC_REGION = 36;
/*     */   public static final int IMMEDIATE_GENERIC_REGION = 38;
/*     */   public static final int IMMEDIATE_LOSSLESS_GENERIC_REGION = 39;
/*     */   public static final int INTERMEDIATE_GENERIC_REFINEMENT_REGION = 40;
/*     */   public static final int IMMEDIATE_GENERIC_REFINEMENT_REGION = 42;
/*     */   public static final int IMMEDIATE_LOSSLESS_GENERIC_REFINEMENT_REGION = 43;
/*     */   public static final int PAGE_INFORMATION = 48;
/*     */   public static final int END_OF_PAGE = 49;
/*     */   public static final int END_OF_STRIPE = 50;
/*     */   public static final int END_OF_FILE = 51;
/*     */   public static final int PROFILES = 52;
/*     */   public static final int TABLES = 53;
/*     */   public static final int EXTENSION = 62;
/*  96 */   private final SortedMap<Integer, JBIG2Segment> segments = new TreeMap<Integer, JBIG2Segment>();
/*  97 */   private final SortedMap<Integer, JBIG2Page> pages = new TreeMap<Integer, JBIG2Page>();
/*  98 */   private final SortedSet<JBIG2Segment> globals = new TreeSet<JBIG2Segment>();
/*     */   private RandomAccessFileOrArray ra;
/*     */   private boolean sequential;
/*     */   private boolean number_of_pages_known;
/* 102 */   private int number_of_pages = -1;
/*     */   
/*     */   private boolean read = false;
/*     */ 
/*     */   
/*     */   public static class JBIG2Segment
/*     */     implements Comparable<JBIG2Segment>
/*     */   {
/*     */     public final int segmentNumber;
/*     */     
/* 112 */     public long dataLength = -1L;
/* 113 */     public int page = -1;
/* 114 */     public int[] referredToSegmentNumbers = null;
/* 115 */     public boolean[] segmentRetentionFlags = null;
/* 116 */     public int type = -1;
/*     */     public boolean deferredNonRetain = false;
/* 118 */     public int countOfReferredToSegments = -1;
/* 119 */     public byte[] data = null;
/* 120 */     public byte[] headerData = null;
/*     */     public boolean page_association_size = false;
/* 122 */     public int page_association_offset = -1;
/*     */     
/*     */     public JBIG2Segment(int segment_number) {
/* 125 */       this.segmentNumber = segment_number;
/*     */     }
/*     */     
/*     */     public int compareTo(JBIG2Segment s) {
/* 129 */       return this.segmentNumber - s.segmentNumber;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class JBIG2Page
/*     */   {
/*     */     public final int page;
/*     */     
/*     */     private final JBIG2SegmentReader sr;
/*     */     
/* 141 */     private final SortedMap<Integer, JBIG2SegmentReader.JBIG2Segment> segs = new TreeMap<Integer, JBIG2SegmentReader.JBIG2Segment>();
/* 142 */     public int pageBitmapWidth = -1;
/* 143 */     public int pageBitmapHeight = -1;
/*     */     public JBIG2Page(int page, JBIG2SegmentReader sr) {
/* 145 */       this.page = page;
/* 146 */       this.sr = sr;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] getData(boolean for_embedding) throws IOException {
/* 157 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/* 158 */       for (Integer sn : this.segs.keySet()) {
/* 159 */         JBIG2SegmentReader.JBIG2Segment s = this.segs.get(sn);
/*     */ 
/*     */ 
/*     */         
/* 163 */         if (for_embedding && (s.type == 51 || s.type == 49)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 168 */         if (for_embedding) {
/*     */           
/* 170 */           byte[] headerData_emb = JBIG2SegmentReader.copyByteArray(s.headerData);
/* 171 */           if (s.page_association_size) {
/* 172 */             headerData_emb[s.page_association_offset] = 0;
/* 173 */             headerData_emb[s.page_association_offset + 1] = 0;
/* 174 */             headerData_emb[s.page_association_offset + 2] = 0;
/* 175 */             headerData_emb[s.page_association_offset + 3] = 1;
/*     */           } else {
/* 177 */             headerData_emb[s.page_association_offset] = 1;
/*     */           } 
/* 179 */           os.write(headerData_emb);
/*     */         } else {
/* 181 */           os.write(s.headerData);
/*     */         } 
/* 183 */         os.write(s.data);
/*     */       } 
/* 185 */       os.close();
/* 186 */       return os.toByteArray();
/*     */     }
/*     */     public void addSegment(JBIG2SegmentReader.JBIG2Segment s) {
/* 189 */       this.segs.put(Integer.valueOf(s.segmentNumber), s);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public JBIG2SegmentReader(RandomAccessFileOrArray ra) throws IOException {
/* 195 */     this.ra = ra;
/*     */   }
/*     */   
/*     */   public static byte[] copyByteArray(byte[] b) {
/* 199 */     byte[] bc = new byte[b.length];
/* 200 */     System.arraycopy(b, 0, bc, 0, b.length);
/* 201 */     return bc;
/*     */   }
/*     */   
/*     */   public void read() throws IOException {
/* 205 */     if (this.read) {
/* 206 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("already.attempted.a.read.on.this.jbig2.file", new Object[0]));
/*     */     }
/* 208 */     this.read = true;
/*     */     
/* 210 */     readFileHeader();
/*     */     
/* 212 */     if (this.sequential) {
/*     */       
/*     */       do {
/* 215 */         JBIG2Segment tmp = readHeader();
/* 216 */         readSegment(tmp);
/* 217 */         this.segments.put(Integer.valueOf(tmp.segmentNumber), tmp);
/* 218 */       } while (this.ra.getFilePointer() < this.ra.length());
/*     */     } else {
/*     */       
/*     */       while (true) {
/*     */         
/* 223 */         JBIG2Segment tmp = readHeader();
/* 224 */         this.segments.put(Integer.valueOf(tmp.segmentNumber), tmp);
/* 225 */         if (tmp.type == 51) {
/* 226 */           Iterator<Integer> segs = this.segments.keySet().iterator();
/* 227 */           while (segs.hasNext())
/* 228 */             readSegment(this.segments.get(segs.next())); 
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } void readSegment(JBIG2Segment s) throws IOException {
/* 234 */     int ptr = (int)this.ra.getFilePointer();
/*     */     
/* 236 */     if (s.dataLength == 4294967295L) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 241 */     byte[] data = new byte[(int)s.dataLength];
/* 242 */     this.ra.read(data);
/* 243 */     s.data = data;
/*     */     
/* 245 */     if (s.type == 48) {
/* 246 */       int last = (int)this.ra.getFilePointer();
/* 247 */       this.ra.seek(ptr);
/* 248 */       int page_bitmap_width = this.ra.readInt();
/* 249 */       int page_bitmap_height = this.ra.readInt();
/* 250 */       this.ra.seek(last);
/* 251 */       JBIG2Page p = this.pages.get(Integer.valueOf(s.page));
/* 252 */       if (p == null) {
/* 253 */         throw new IllegalStateException(MessageLocalization.getComposedMessage("referring.to.widht.height.of.page.we.havent.seen.yet.1", s.page));
/*     */       }
/*     */       
/* 256 */       p.pageBitmapWidth = page_bitmap_width;
/* 257 */       p.pageBitmapHeight = page_bitmap_height;
/*     */     } 
/*     */   }
/*     */   
/*     */   JBIG2Segment readHeader() throws IOException {
/* 262 */     int segment_page_association, ptr = (int)this.ra.getFilePointer();
/*     */     
/* 264 */     int segment_number = this.ra.readInt();
/* 265 */     JBIG2Segment s = new JBIG2Segment(segment_number);
/*     */ 
/*     */     
/* 268 */     int segment_header_flags = this.ra.read();
/* 269 */     boolean deferred_non_retain = ((segment_header_flags & 0x80) == 128);
/* 270 */     s.deferredNonRetain = deferred_non_retain;
/* 271 */     boolean page_association_size = ((segment_header_flags & 0x40) == 64);
/* 272 */     int segment_type = segment_header_flags & 0x3F;
/* 273 */     s.type = segment_type;
/*     */ 
/*     */     
/* 276 */     int referred_to_byte0 = this.ra.read();
/* 277 */     int count_of_referred_to_segments = (referred_to_byte0 & 0xE0) >> 5;
/* 278 */     int[] referred_to_segment_numbers = null;
/* 279 */     boolean[] segment_retention_flags = null;
/*     */     
/* 281 */     if (count_of_referred_to_segments == 7) {
/*     */       
/* 283 */       this.ra.seek(this.ra.getFilePointer() - 1L);
/* 284 */       count_of_referred_to_segments = this.ra.readInt() & 0x1FFFFFFF;
/* 285 */       segment_retention_flags = new boolean[count_of_referred_to_segments + 1];
/* 286 */       int j = 0;
/* 287 */       int referred_to_current_byte = 0;
/*     */       do {
/* 289 */         int k = j % 8;
/* 290 */         if (k == 0) {
/* 291 */           referred_to_current_byte = this.ra.read();
/*     */         }
/* 293 */         segment_retention_flags[j] = ((1 << k & referred_to_current_byte) >> k == 1);
/* 294 */         ++j;
/* 295 */       } while (j <= count_of_referred_to_segments);
/*     */     }
/* 297 */     else if (count_of_referred_to_segments <= 4) {
/*     */       
/* 299 */       segment_retention_flags = new boolean[count_of_referred_to_segments + 1];
/* 300 */       referred_to_byte0 &= 0x1F;
/* 301 */       for (int j = 0; j <= count_of_referred_to_segments; j++) {
/* 302 */         segment_retention_flags[j] = ((1 << j & referred_to_byte0) >> j == 1);
/*     */       }
/*     */     }
/* 305 */     else if (count_of_referred_to_segments == 5 || count_of_referred_to_segments == 6) {
/* 306 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("count.of.referred.to.segments.had.bad.value.in.header.for.segment.1.starting.at.2", new Object[] { String.valueOf(segment_number), String.valueOf(ptr) }));
/*     */     } 
/* 308 */     s.segmentRetentionFlags = segment_retention_flags;
/* 309 */     s.countOfReferredToSegments = count_of_referred_to_segments;
/*     */ 
/*     */     
/* 312 */     referred_to_segment_numbers = new int[count_of_referred_to_segments + 1];
/* 313 */     for (int i = 1; i <= count_of_referred_to_segments; i++) {
/* 314 */       if (segment_number <= 256) {
/* 315 */         referred_to_segment_numbers[i] = this.ra.read();
/* 316 */       } else if (segment_number <= 65536) {
/* 317 */         referred_to_segment_numbers[i] = this.ra.readUnsignedShort();
/*     */       } else {
/* 319 */         referred_to_segment_numbers[i] = (int)this.ra.readUnsignedInt();
/*     */       } 
/*     */     } 
/* 322 */     s.referredToSegmentNumbers = referred_to_segment_numbers;
/*     */ 
/*     */ 
/*     */     
/* 326 */     int page_association_offset = (int)this.ra.getFilePointer() - ptr;
/* 327 */     if (page_association_size) {
/* 328 */       segment_page_association = this.ra.readInt();
/*     */     } else {
/* 330 */       segment_page_association = this.ra.read();
/*     */     } 
/* 332 */     if (segment_page_association < 0) {
/* 333 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("page.1.invalid.for.segment.2.starting.at.3", new Object[] { String.valueOf(segment_page_association), String.valueOf(segment_number), String.valueOf(ptr) }));
/*     */     }
/* 335 */     s.page = segment_page_association;
/*     */     
/* 337 */     s.page_association_size = page_association_size;
/* 338 */     s.page_association_offset = page_association_offset;
/*     */     
/* 340 */     if (segment_page_association > 0 && !this.pages.containsKey(Integer.valueOf(segment_page_association))) {
/* 341 */       this.pages.put(Integer.valueOf(segment_page_association), new JBIG2Page(segment_page_association, this));
/*     */     }
/* 343 */     if (segment_page_association > 0) {
/* 344 */       ((JBIG2Page)this.pages.get(Integer.valueOf(segment_page_association))).addSegment(s);
/*     */     } else {
/* 346 */       this.globals.add(s);
/*     */     } 
/*     */ 
/*     */     
/* 350 */     long segment_data_length = this.ra.readUnsignedInt();
/*     */     
/* 352 */     s.dataLength = segment_data_length;
/*     */     
/* 354 */     int end_ptr = (int)this.ra.getFilePointer();
/* 355 */     this.ra.seek(ptr);
/* 356 */     byte[] header_data = new byte[end_ptr - ptr];
/* 357 */     this.ra.read(header_data);
/* 358 */     s.headerData = header_data;
/*     */     
/* 360 */     return s;
/*     */   }
/*     */   
/*     */   void readFileHeader() throws IOException {
/* 364 */     this.ra.seek(0L);
/* 365 */     byte[] idstring = new byte[8];
/* 366 */     this.ra.read(idstring);
/*     */     
/* 368 */     byte[] refidstring = { -105, 74, 66, 50, 13, 10, 26, 10 };
/*     */     
/* 370 */     for (int i = 0; i < idstring.length; i++) {
/* 371 */       if (idstring[i] != refidstring[i]) {
/* 372 */         throw new IllegalStateException(MessageLocalization.getComposedMessage("file.header.idstring.not.good.at.byte.1", i));
/*     */       }
/*     */     } 
/*     */     
/* 376 */     int fileheaderflags = this.ra.read();
/*     */     
/* 378 */     this.sequential = ((fileheaderflags & 0x1) == 1);
/* 379 */     this.number_of_pages_known = ((fileheaderflags & 0x2) == 0);
/*     */     
/* 381 */     if ((fileheaderflags & 0xFC) != 0) {
/* 382 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("file.header.flags.bits.2.7.not.0", new Object[0]));
/*     */     }
/*     */     
/* 385 */     if (this.number_of_pages_known) {
/* 386 */       this.number_of_pages = this.ra.readInt();
/*     */     }
/*     */   }
/*     */   
/*     */   public int numberOfPages() {
/* 391 */     return this.pages.size();
/*     */   }
/*     */   
/*     */   public int getPageHeight(int i) {
/* 395 */     return ((JBIG2Page)this.pages.get(Integer.valueOf(i))).pageBitmapHeight;
/*     */   }
/*     */   
/*     */   public int getPageWidth(int i) {
/* 399 */     return ((JBIG2Page)this.pages.get(Integer.valueOf(i))).pageBitmapWidth;
/*     */   }
/*     */   
/*     */   public JBIG2Page getPage(int page) {
/* 403 */     return this.pages.get(Integer.valueOf(page));
/*     */   }
/*     */   
/*     */   public byte[] getGlobal(boolean for_embedding) {
/* 407 */     ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */     try {
/* 409 */       for (JBIG2Segment element : this.globals) {
/* 410 */         JBIG2Segment s = element;
/* 411 */         if (for_embedding && (s.type == 51 || s.type == 49)) {
/*     */           continue;
/*     */         }
/*     */         
/* 415 */         os.write(s.headerData);
/* 416 */         os.write(s.data);
/*     */       } 
/* 418 */       os.close();
/* 419 */     } catch (IOException e) {
/* 420 */       e.printStackTrace();
/*     */     } 
/* 422 */     if (os.size() <= 0) {
/* 423 */       return null;
/*     */     }
/* 425 */     return os.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 430 */     if (this.read) {
/* 431 */       return "Jbig2SegmentReader: number of pages: " + numberOfPages();
/*     */     }
/* 433 */     return "Jbig2SegmentReader in indeterminate state.";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/JBIG2SegmentReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */