/*      */ package org.apache.tools.bzip2;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CBZip2InputStream
/*      */   extends InputStream
/*      */   implements BZip2Constants
/*      */ {
/*      */   private int last;
/*      */   private int origPtr;
/*      */   private int blockSize100k;
/*      */   private boolean blockRandomised;
/*      */   private int bsBuff;
/*      */   private int bsLive;
/*   68 */   private final CRC crc = new CRC();
/*      */   
/*      */   private int nInUse;
/*      */   
/*      */   private InputStream in;
/*      */   
/*      */   private final boolean decompressConcatenated;
/*   75 */   private int currentChar = -1;
/*      */   
/*      */   private static final int EOF = 0;
/*      */   
/*      */   private static final int START_BLOCK_STATE = 1;
/*      */   private static final int RAND_PART_A_STATE = 2;
/*      */   private static final int RAND_PART_B_STATE = 3;
/*      */   private static final int RAND_PART_C_STATE = 4;
/*      */   private static final int NO_RAND_PART_A_STATE = 5;
/*      */   private static final int NO_RAND_PART_B_STATE = 6;
/*      */   private static final int NO_RAND_PART_C_STATE = 7;
/*   86 */   private int currentState = 1;
/*      */ 
/*      */   
/*      */   private int storedBlockCRC;
/*      */ 
/*      */   
/*      */   private int storedCombinedCRC;
/*      */ 
/*      */   
/*      */   private int computedBlockCRC;
/*      */ 
/*      */   
/*      */   private int computedCombinedCRC;
/*      */ 
/*      */   
/*      */   private int su_count;
/*      */ 
/*      */   
/*      */   private int su_ch2;
/*      */ 
/*      */   
/*      */   private int su_chPrev;
/*      */ 
/*      */   
/*      */   private int su_i2;
/*      */ 
/*      */   
/*      */   private int su_j2;
/*      */ 
/*      */   
/*      */   private int su_rNToGo;
/*      */ 
/*      */   
/*      */   private int su_rTPos;
/*      */   
/*      */   private int su_tPos;
/*      */   
/*      */   private char su_z;
/*      */   
/*      */   private Data data;
/*      */ 
/*      */   
/*      */   public CBZip2InputStream(InputStream in) throws IOException {
/*  129 */     this(in, false);
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
/*      */   public CBZip2InputStream(InputStream in, boolean decompressConcatenated) throws IOException {
/*  159 */     this.in = in;
/*  160 */     this.decompressConcatenated = decompressConcatenated;
/*      */     
/*  162 */     init(true);
/*  163 */     initBlock();
/*  164 */     setupBlock();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int read() throws IOException {
/*  170 */     if (this.in != null) {
/*  171 */       return read0();
/*      */     }
/*  173 */     throw new IOException("stream closed");
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
/*      */   public int read(byte[] dest, int offs, int len) throws IOException {
/*  185 */     if (offs < 0) {
/*  186 */       throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
/*      */     }
/*  188 */     if (len < 0) {
/*  189 */       throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
/*      */     }
/*  191 */     if (offs + len > dest.length) {
/*  192 */       throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length + ").");
/*      */     }
/*      */ 
/*      */     
/*  196 */     if (this.in == null) {
/*  197 */       throw new IOException("stream closed");
/*      */     }
/*      */     
/*  200 */     int hi = offs + len;
/*  201 */     int destOffs = offs; int b;
/*  202 */     while (destOffs < hi && (b = read0()) >= 0) {
/*  203 */       dest[destOffs++] = (byte)b;
/*      */     }
/*      */     
/*  206 */     return (destOffs == offs) ? -1 : (destOffs - offs);
/*      */   }
/*      */   
/*      */   private void makeMaps() {
/*  210 */     boolean[] inUse = this.data.inUse;
/*  211 */     byte[] seqToUnseq = this.data.seqToUnseq;
/*      */     
/*  213 */     int nInUseShadow = 0;
/*      */     
/*  215 */     for (int i = 0; i < 256; i++) {
/*  216 */       if (inUse[i]) {
/*  217 */         seqToUnseq[nInUseShadow++] = (byte)i;
/*      */       }
/*      */     } 
/*      */     
/*  221 */     this.nInUse = nInUseShadow;
/*      */   }
/*      */   
/*      */   private int read0() throws IOException {
/*  225 */     int retChar = this.currentChar;
/*      */     
/*  227 */     switch (this.currentState) {
/*      */       case 0:
/*  229 */         return -1;
/*      */       
/*      */       case 1:
/*  232 */         throw new IllegalStateException();
/*      */       
/*      */       case 2:
/*  235 */         throw new IllegalStateException();
/*      */       
/*      */       case 3:
/*  238 */         setupRandPartB();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  260 */         return retChar;case 4: setupRandPartC(); return retChar;case 5: throw new IllegalStateException();case 6: setupNoRandPartB(); return retChar;case 7: setupNoRandPartC(); return retChar;
/*      */     } 
/*      */     throw new IllegalStateException();
/*      */   } private boolean init(boolean isFirstStream) throws IOException {
/*  264 */     if (null == this.in) {
/*  265 */       throw new IOException("No InputStream");
/*      */     }
/*      */     
/*  268 */     if (isFirstStream) {
/*  269 */       if (this.in.available() == 0) {
/*  270 */         throw new IOException("Empty InputStream");
/*      */       }
/*      */     } else {
/*  273 */       int magic0 = this.in.read();
/*  274 */       if (magic0 == -1) {
/*  275 */         return false;
/*      */       }
/*  277 */       int magic1 = this.in.read();
/*  278 */       if (magic0 != 66 || magic1 != 90) {
/*  279 */         throw new IOException("Garbage after a valid BZip2 stream");
/*      */       }
/*      */     } 
/*      */     
/*  283 */     int magic2 = this.in.read();
/*  284 */     if (magic2 != 104) {
/*  285 */       throw new IOException(isFirstStream ? 
/*  286 */           "Stream is not in the BZip2 format" : 
/*  287 */           "Garbage after a valid BZip2 stream");
/*      */     }
/*      */     
/*  290 */     int blockSize = this.in.read();
/*  291 */     if (blockSize < 49 || blockSize > 57) {
/*  292 */       throw new IOException("Stream is not BZip2 formatted: illegal blocksize " + (char)blockSize);
/*      */     }
/*      */ 
/*      */     
/*  296 */     this.blockSize100k = blockSize - 48;
/*      */     
/*  298 */     this.bsLive = 0;
/*  299 */     this.computedCombinedCRC = 0;
/*      */     
/*  301 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initBlock() throws IOException {
/*      */     char magic0, magic1, magic2, magic3, magic4, magic5;
/*      */     while (true) {
/*  314 */       magic0 = bsGetUByte();
/*  315 */       magic1 = bsGetUByte();
/*  316 */       magic2 = bsGetUByte();
/*  317 */       magic3 = bsGetUByte();
/*  318 */       magic4 = bsGetUByte();
/*  319 */       magic5 = bsGetUByte();
/*      */ 
/*      */       
/*  322 */       if (magic0 != '\027' || magic1 != 'r' || magic2 != 'E' || magic3 != '8' || magic4 != 'P' || magic5 != '') {
/*      */         break;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  330 */       if (complete()) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  335 */     if (magic0 != '1' || magic1 != 'A' || magic2 != 'Y' || magic3 != '&' || magic4 != 'S' || magic5 != 'Y') {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  342 */       this.currentState = 0;
/*  343 */       throw new IOException("bad block header");
/*      */     } 
/*  345 */     this.storedBlockCRC = bsGetInt();
/*  346 */     this.blockRandomised = (bsR(1) == 1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  352 */     if (this.data == null) {
/*  353 */       this.data = new Data(this.blockSize100k);
/*      */     }
/*      */ 
/*      */     
/*  357 */     getAndMoveToFrontDecode();
/*      */     
/*  359 */     this.crc.initialiseCRC();
/*  360 */     this.currentState = 1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void endBlock() {
/*  365 */     this.computedBlockCRC = this.crc.getFinalCRC();
/*      */ 
/*      */     
/*  368 */     if (this.storedBlockCRC != this.computedBlockCRC) {
/*      */ 
/*      */       
/*  371 */       this.computedCombinedCRC = this.storedCombinedCRC << 1 | this.storedCombinedCRC >>> 31;
/*      */ 
/*      */       
/*  374 */       this.computedCombinedCRC ^= this.storedBlockCRC;
/*      */       
/*  376 */       reportCRCError();
/*      */     } 
/*      */     
/*  379 */     this.computedCombinedCRC = this.computedCombinedCRC << 1 | this.computedCombinedCRC >>> 31;
/*      */ 
/*      */     
/*  382 */     this.computedCombinedCRC ^= this.computedBlockCRC;
/*      */   }
/*      */   
/*      */   private boolean complete() throws IOException {
/*  386 */     this.storedCombinedCRC = bsGetInt();
/*  387 */     this.currentState = 0;
/*  388 */     this.data = null;
/*      */     
/*  390 */     if (this.storedCombinedCRC != this.computedCombinedCRC) {
/*  391 */       reportCRCError();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  396 */     return (!this.decompressConcatenated || !init(false));
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  401 */     InputStream inShadow = this.in;
/*  402 */     if (inShadow != null) {
/*      */       try {
/*  404 */         if (inShadow != System.in) {
/*  405 */           inShadow.close();
/*      */         }
/*      */       } finally {
/*  408 */         this.data = null;
/*  409 */         this.in = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private int bsR(int n) throws IOException {
/*  415 */     int bsLiveShadow = this.bsLive;
/*  416 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  418 */     if (bsLiveShadow < n) {
/*  419 */       InputStream inShadow = this.in;
/*      */       do {
/*  421 */         int thech = inShadow.read();
/*      */         
/*  423 */         if (thech < 0) {
/*  424 */           throw new IOException("unexpected end of stream");
/*      */         }
/*      */         
/*  427 */         bsBuffShadow = bsBuffShadow << 8 | thech;
/*  428 */         bsLiveShadow += 8;
/*  429 */       } while (bsLiveShadow < n);
/*      */       
/*  431 */       this.bsBuff = bsBuffShadow;
/*      */     } 
/*      */     
/*  434 */     this.bsLive = bsLiveShadow - n;
/*  435 */     return bsBuffShadow >> bsLiveShadow - n & (1 << n) - 1;
/*      */   }
/*      */   
/*      */   private boolean bsGetBit() throws IOException {
/*  439 */     int bsLiveShadow = this.bsLive;
/*  440 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  442 */     if (bsLiveShadow < 1) {
/*  443 */       int thech = this.in.read();
/*      */       
/*  445 */       if (thech < 0) {
/*  446 */         throw new IOException("unexpected end of stream");
/*      */       }
/*      */       
/*  449 */       bsBuffShadow = bsBuffShadow << 8 | thech;
/*  450 */       bsLiveShadow += 8;
/*  451 */       this.bsBuff = bsBuffShadow;
/*      */     } 
/*      */     
/*  454 */     this.bsLive = bsLiveShadow - 1;
/*  455 */     return ((bsBuffShadow >> bsLiveShadow - 1 & 0x1) != 0);
/*      */   }
/*      */   
/*      */   private char bsGetUByte() throws IOException {
/*  459 */     return (char)bsR(8);
/*      */   }
/*      */   
/*      */   private int bsGetInt() throws IOException {
/*  463 */     return ((bsR(8) << 8 | bsR(8)) << 8 | bsR(8)) << 8 | bsR(8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void hbCreateDecodeTables(int[] limit, int[] base, int[] perm, char[] length, int minLen, int maxLen, int alphaSize) {
/*      */     int i;
/*      */     int pp;
/*  476 */     for (i = minLen, pp = 0; i <= maxLen; i++) {
/*  477 */       for (int k = 0; k < alphaSize; k++) {
/*  478 */         if (length[k] == i) {
/*  479 */           perm[pp++] = k;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  484 */     for (i = 23; --i > 0; ) {
/*  485 */       base[i] = 0;
/*  486 */       limit[i] = 0;
/*      */     } 
/*      */     
/*  489 */     for (i = 0; i < alphaSize; i++) {
/*  490 */       base[length[i] + 1] = base[length[i] + 1] + 1;
/*      */     }
/*      */     int b;
/*  493 */     for (i = 1, b = base[0]; i < 23; i++) {
/*  494 */       b += base[i];
/*  495 */       base[i] = b;
/*      */     }  int vec;
/*      */     int j;
/*  498 */     for (i = minLen, vec = 0, j = base[i]; i <= maxLen; i++) {
/*  499 */       int nb = base[i + 1];
/*  500 */       vec += nb - j;
/*  501 */       j = nb;
/*  502 */       limit[i] = vec - 1;
/*  503 */       vec <<= 1;
/*      */     } 
/*      */     
/*  506 */     for (i = minLen + 1; i <= maxLen; i++) {
/*  507 */       base[i] = (limit[i - 1] + 1 << 1) - base[i];
/*      */     }
/*      */   }
/*      */   
/*      */   private void recvDecodingTables() throws IOException {
/*  512 */     Data dataShadow = this.data;
/*  513 */     boolean[] inUse = dataShadow.inUse;
/*  514 */     byte[] pos = dataShadow.recvDecodingTables_pos;
/*  515 */     byte[] selector = dataShadow.selector;
/*  516 */     byte[] selectorMtf = dataShadow.selectorMtf;
/*      */     
/*  518 */     int inUse16 = 0;
/*      */     
/*      */     int i;
/*  521 */     for (i = 0; i < 16; i++) {
/*  522 */       if (bsGetBit()) {
/*  523 */         inUse16 |= 1 << i;
/*      */       }
/*      */     } 
/*      */     
/*  527 */     for (i = 256; --i >= 0;) {
/*  528 */       inUse[i] = false;
/*      */     }
/*      */     
/*  531 */     for (i = 0; i < 16; i++) {
/*  532 */       if ((inUse16 & 1 << i) != 0) {
/*  533 */         int i16 = i << 4;
/*  534 */         for (int m = 0; m < 16; m++) {
/*  535 */           if (bsGetBit()) {
/*  536 */             inUse[i16 + m] = true;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  542 */     makeMaps();
/*  543 */     int alphaSize = this.nInUse + 2;
/*      */ 
/*      */     
/*  546 */     int nGroups = bsR(3);
/*  547 */     int nSelectors = bsR(15);
/*      */     
/*  549 */     for (int k = 0; k < nSelectors; k++) {
/*  550 */       int m = 0;
/*  551 */       while (bsGetBit()) {
/*  552 */         m++;
/*      */       }
/*  554 */       selectorMtf[k] = (byte)m;
/*      */     } 
/*      */ 
/*      */     
/*  558 */     for (int v = nGroups; --v >= 0;) {
/*  559 */       pos[v] = (byte)v;
/*      */     }
/*      */     
/*  562 */     for (int j = 0; j < nSelectors; j++) {
/*  563 */       int m = selectorMtf[j] & 0xFF;
/*  564 */       byte tmp = pos[m];
/*  565 */       while (m > 0) {
/*      */         
/*  567 */         pos[m] = pos[m - 1];
/*  568 */         m--;
/*      */       } 
/*  570 */       pos[0] = tmp;
/*  571 */       selector[j] = tmp;
/*      */     } 
/*      */     
/*  574 */     char[][] len = dataShadow.temp_charArray2d;
/*      */ 
/*      */     
/*  577 */     for (int t = 0; t < nGroups; t++) {
/*  578 */       int curr = bsR(5);
/*  579 */       char[] len_t = len[t];
/*  580 */       for (int m = 0; m < alphaSize; m++) {
/*  581 */         while (bsGetBit()) {
/*  582 */           curr += bsGetBit() ? -1 : 1;
/*      */         }
/*  584 */         len_t[m] = (char)curr;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  589 */     createHuffmanDecodingTables(alphaSize, nGroups);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createHuffmanDecodingTables(int alphaSize, int nGroups) {
/*  597 */     Data dataShadow = this.data;
/*  598 */     char[][] len = dataShadow.temp_charArray2d;
/*  599 */     int[] minLens = dataShadow.minLens;
/*  600 */     int[][] limit = dataShadow.limit;
/*  601 */     int[][] base = dataShadow.base;
/*  602 */     int[][] perm = dataShadow.perm;
/*      */     
/*  604 */     for (int t = 0; t < nGroups; t++) {
/*  605 */       int minLen = 32;
/*  606 */       int maxLen = 0;
/*  607 */       char[] len_t = len[t];
/*  608 */       for (int i = alphaSize; --i >= 0; ) {
/*  609 */         char lent = len_t[i];
/*  610 */         if (lent > maxLen) {
/*  611 */           maxLen = lent;
/*      */         }
/*  613 */         if (lent < minLen) {
/*  614 */           minLen = lent;
/*      */         }
/*      */       } 
/*  617 */       hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
/*      */       
/*  619 */       minLens[t] = minLen;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void getAndMoveToFrontDecode() throws IOException {
/*  624 */     this.origPtr = bsR(24);
/*  625 */     recvDecodingTables();
/*      */     
/*  627 */     InputStream inShadow = this.in;
/*  628 */     Data dataShadow = this.data;
/*  629 */     byte[] ll8 = dataShadow.ll8;
/*  630 */     int[] unzftab = dataShadow.unzftab;
/*  631 */     byte[] selector = dataShadow.selector;
/*  632 */     byte[] seqToUnseq = dataShadow.seqToUnseq;
/*  633 */     char[] yy = dataShadow.getAndMoveToFrontDecode_yy;
/*  634 */     int[] minLens = dataShadow.minLens;
/*  635 */     int[][] limit = dataShadow.limit;
/*  636 */     int[][] base = dataShadow.base;
/*  637 */     int[][] perm = dataShadow.perm;
/*  638 */     int limitLast = this.blockSize100k * 100000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  646 */     for (int i = 256; --i >= 0; ) {
/*  647 */       yy[i] = (char)i;
/*  648 */       unzftab[i] = 0;
/*      */     } 
/*      */     
/*  651 */     int groupNo = 0;
/*  652 */     int groupPos = 49;
/*  653 */     int eob = this.nInUse + 1;
/*  654 */     int nextSym = getAndMoveToFrontDecode0(0);
/*  655 */     int bsBuffShadow = this.bsBuff;
/*  656 */     int bsLiveShadow = this.bsLive;
/*  657 */     int lastShadow = -1;
/*  658 */     int zt = selector[groupNo] & 0xFF;
/*  659 */     int[] base_zt = base[zt];
/*  660 */     int[] limit_zt = limit[zt];
/*  661 */     int[] perm_zt = perm[zt];
/*  662 */     int minLens_zt = minLens[zt];
/*      */     
/*  664 */     while (nextSym != eob) {
/*  665 */       if (nextSym == 0 || nextSym == 1) {
/*  666 */         int s = -1;
/*      */         int n;
/*  668 */         for (n = 1;; n <<= 1) {
/*  669 */           if (nextSym == 0) {
/*  670 */             s += n;
/*  671 */           } else if (nextSym == 1) {
/*  672 */             s += n << 1;
/*      */           } else {
/*      */             break;
/*      */           } 
/*      */           
/*  677 */           if (groupPos == 0) {
/*  678 */             groupPos = 49;
/*  679 */             zt = selector[++groupNo] & 0xFF;
/*  680 */             base_zt = base[zt];
/*  681 */             limit_zt = limit[zt];
/*  682 */             perm_zt = perm[zt];
/*  683 */             minLens_zt = minLens[zt];
/*      */           } else {
/*  685 */             groupPos--;
/*      */           } 
/*      */           
/*  688 */           int j = minLens_zt;
/*      */ 
/*      */ 
/*      */           
/*  692 */           while (bsLiveShadow < j) {
/*  693 */             int thech = inShadow.read();
/*  694 */             if (thech >= 0) {
/*  695 */               bsBuffShadow = bsBuffShadow << 8 | thech;
/*  696 */               bsLiveShadow += 8; continue;
/*      */             } 
/*  698 */             throw new IOException("unexpected end of stream");
/*      */           } 
/*      */           
/*  701 */           int k = bsBuffShadow >> bsLiveShadow - j & (1 << j) - 1;
/*  702 */           bsLiveShadow -= j;
/*      */           
/*  704 */           while (k > limit_zt[j]) {
/*  705 */             j++;
/*  706 */             while (bsLiveShadow < 1) {
/*  707 */               int thech = inShadow.read();
/*  708 */               if (thech >= 0) {
/*  709 */                 bsBuffShadow = bsBuffShadow << 8 | thech;
/*  710 */                 bsLiveShadow += 8; continue;
/*      */               } 
/*  712 */               throw new IOException("unexpected end of stream");
/*      */             } 
/*      */             
/*  715 */             bsLiveShadow--;
/*  716 */             k = k << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
/*      */           } 
/*  718 */           nextSym = perm_zt[k - base_zt[j]];
/*      */         } 
/*      */         
/*  721 */         byte ch = seqToUnseq[yy[0]];
/*  722 */         unzftab[ch & 0xFF] = unzftab[ch & 0xFF] + s + 1;
/*      */         
/*  724 */         while (s-- >= 0) {
/*  725 */           ll8[++lastShadow] = ch;
/*      */         }
/*      */         
/*  728 */         if (lastShadow >= limitLast)
/*  729 */           throw new IOException("block overrun"); 
/*      */         continue;
/*      */       } 
/*  732 */       if (++lastShadow >= limitLast) {
/*  733 */         throw new IOException("block overrun");
/*      */       }
/*      */       
/*  736 */       char tmp = yy[nextSym - 1];
/*  737 */       unzftab[seqToUnseq[tmp] & 0xFF] = unzftab[seqToUnseq[tmp] & 0xFF] + 1;
/*  738 */       ll8[lastShadow] = seqToUnseq[tmp];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  745 */       if (nextSym <= 16) {
/*  746 */         for (int j = nextSym - 1; j > 0;) {
/*  747 */           yy[j] = yy[--j];
/*      */         }
/*      */       } else {
/*  750 */         System.arraycopy(yy, 0, yy, 1, nextSym - 1);
/*      */       } 
/*      */       
/*  753 */       yy[0] = tmp;
/*      */       
/*  755 */       if (groupPos == 0) {
/*  756 */         groupPos = 49;
/*  757 */         zt = selector[++groupNo] & 0xFF;
/*  758 */         base_zt = base[zt];
/*  759 */         limit_zt = limit[zt];
/*  760 */         perm_zt = perm[zt];
/*  761 */         minLens_zt = minLens[zt];
/*      */       } else {
/*  763 */         groupPos--;
/*      */       } 
/*      */       
/*  766 */       int zn = minLens_zt;
/*      */ 
/*      */ 
/*      */       
/*  770 */       while (bsLiveShadow < zn) {
/*  771 */         int thech = inShadow.read();
/*  772 */         if (thech >= 0) {
/*  773 */           bsBuffShadow = bsBuffShadow << 8 | thech;
/*  774 */           bsLiveShadow += 8; continue;
/*      */         } 
/*  776 */         throw new IOException("unexpected end of stream");
/*      */       } 
/*      */       
/*  779 */       int zvec = bsBuffShadow >> bsLiveShadow - zn & (1 << zn) - 1;
/*  780 */       bsLiveShadow -= zn;
/*      */       
/*  782 */       while (zvec > limit_zt[zn]) {
/*  783 */         zn++;
/*  784 */         while (bsLiveShadow < 1) {
/*  785 */           int thech = inShadow.read();
/*  786 */           if (thech >= 0) {
/*  787 */             bsBuffShadow = bsBuffShadow << 8 | thech;
/*  788 */             bsLiveShadow += 8; continue;
/*      */           } 
/*  790 */           throw new IOException("unexpected end of stream");
/*      */         } 
/*      */         
/*  793 */         bsLiveShadow--;
/*  794 */         zvec = zvec << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
/*      */       } 
/*  796 */       nextSym = perm_zt[zvec - base_zt[zn]];
/*      */     } 
/*      */ 
/*      */     
/*  800 */     this.last = lastShadow;
/*  801 */     this.bsLive = bsLiveShadow;
/*  802 */     this.bsBuff = bsBuffShadow;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getAndMoveToFrontDecode0(int groupNo) throws IOException {
/*  807 */     InputStream inShadow = this.in;
/*  808 */     Data dataShadow = this.data;
/*  809 */     int zt = dataShadow.selector[groupNo] & 0xFF;
/*  810 */     int[] limit_zt = dataShadow.limit[zt];
/*  811 */     int zn = dataShadow.minLens[zt];
/*  812 */     int zvec = bsR(zn);
/*  813 */     int bsLiveShadow = this.bsLive;
/*  814 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  816 */     while (zvec > limit_zt[zn]) {
/*  817 */       zn++;
/*  818 */       while (bsLiveShadow < 1) {
/*  819 */         int thech = inShadow.read();
/*      */         
/*  821 */         if (thech >= 0) {
/*  822 */           bsBuffShadow = bsBuffShadow << 8 | thech;
/*  823 */           bsLiveShadow += 8; continue;
/*      */         } 
/*  825 */         throw new IOException("unexpected end of stream");
/*      */       } 
/*      */       
/*  828 */       bsLiveShadow--;
/*  829 */       zvec = zvec << 1 | bsBuffShadow >> bsLiveShadow & 0x1;
/*      */     } 
/*      */     
/*  832 */     this.bsLive = bsLiveShadow;
/*  833 */     this.bsBuff = bsBuffShadow;
/*      */     
/*  835 */     return dataShadow.perm[zt][zvec - dataShadow.base[zt][zn]];
/*      */   }
/*      */   
/*      */   private void setupBlock() throws IOException {
/*  839 */     if (this.data == null) {
/*      */       return;
/*      */     }
/*      */     
/*  843 */     int[] cftab = this.data.cftab;
/*  844 */     int[] tt = this.data.initTT(this.last + 1);
/*  845 */     byte[] ll8 = this.data.ll8;
/*  846 */     cftab[0] = 0;
/*  847 */     System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);
/*      */     int i, c;
/*  849 */     for (i = 1, c = cftab[0]; i <= 256; i++) {
/*  850 */       c += cftab[i];
/*  851 */       cftab[i] = c;
/*      */     } 
/*      */     int lastShadow;
/*  854 */     for (i = 0, lastShadow = this.last; i <= lastShadow; i++) {
/*  855 */       cftab[ll8[i] & 0xFF] = cftab[ll8[i] & 0xFF] + 1; tt[cftab[ll8[i] & 0xFF]] = i;
/*      */     } 
/*      */     
/*  858 */     if (this.origPtr < 0 || this.origPtr >= tt.length) {
/*  859 */       throw new IOException("stream corrupted");
/*      */     }
/*      */     
/*  862 */     this.su_tPos = tt[this.origPtr];
/*  863 */     this.su_count = 0;
/*  864 */     this.su_i2 = 0;
/*  865 */     this.su_ch2 = 256;
/*      */     
/*  867 */     if (this.blockRandomised) {
/*  868 */       this.su_rNToGo = 0;
/*  869 */       this.su_rTPos = 0;
/*  870 */       setupRandPartA();
/*      */     } else {
/*  872 */       setupNoRandPartA();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupRandPartA() throws IOException {
/*  877 */     if (this.su_i2 <= this.last) {
/*  878 */       this.su_chPrev = this.su_ch2;
/*  879 */       int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
/*  880 */       this.su_tPos = this.data.tt[this.su_tPos];
/*  881 */       if (this.su_rNToGo == 0) {
/*  882 */         this.su_rNToGo = BZip2Constants.rNums[this.su_rTPos] - 1;
/*  883 */         if (++this.su_rTPos == 512) {
/*  884 */           this.su_rTPos = 0;
/*      */         }
/*      */       } else {
/*  887 */         this.su_rNToGo--;
/*      */       } 
/*  889 */       this.su_ch2 = su_ch2Shadow ^= (this.su_rNToGo == 1) ? 1 : 0;
/*  890 */       this.su_i2++;
/*  891 */       this.currentChar = su_ch2Shadow;
/*  892 */       this.currentState = 3;
/*  893 */       this.crc.updateCRC(su_ch2Shadow);
/*      */     } else {
/*  895 */       endBlock();
/*  896 */       initBlock();
/*  897 */       setupBlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupNoRandPartA() throws IOException {
/*  902 */     if (this.su_i2 <= this.last) {
/*  903 */       this.su_chPrev = this.su_ch2;
/*  904 */       int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xFF;
/*  905 */       this.su_ch2 = su_ch2Shadow;
/*  906 */       this.su_tPos = this.data.tt[this.su_tPos];
/*  907 */       this.su_i2++;
/*  908 */       this.currentChar = su_ch2Shadow;
/*  909 */       this.currentState = 6;
/*  910 */       this.crc.updateCRC(su_ch2Shadow);
/*      */     } else {
/*  912 */       this.currentState = 5;
/*  913 */       endBlock();
/*  914 */       initBlock();
/*  915 */       setupBlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupRandPartB() throws IOException {
/*  920 */     if (this.su_ch2 != this.su_chPrev) {
/*  921 */       this.currentState = 2;
/*  922 */       this.su_count = 1;
/*  923 */       setupRandPartA();
/*  924 */     } else if (++this.su_count >= 4) {
/*  925 */       this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
/*  926 */       this.su_tPos = this.data.tt[this.su_tPos];
/*  927 */       if (this.su_rNToGo == 0) {
/*  928 */         this.su_rNToGo = BZip2Constants.rNums[this.su_rTPos] - 1;
/*  929 */         if (++this.su_rTPos == 512) {
/*  930 */           this.su_rTPos = 0;
/*      */         }
/*      */       } else {
/*  933 */         this.su_rNToGo--;
/*      */       } 
/*  935 */       this.su_j2 = 0;
/*  936 */       this.currentState = 4;
/*  937 */       if (this.su_rNToGo == 1) {
/*  938 */         this.su_z = (char)(this.su_z ^ 0x1);
/*      */       }
/*  940 */       setupRandPartC();
/*      */     } else {
/*  942 */       this.currentState = 2;
/*  943 */       setupRandPartA();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupRandPartC() throws IOException {
/*  948 */     if (this.su_j2 < this.su_z) {
/*  949 */       this.currentChar = this.su_ch2;
/*  950 */       this.crc.updateCRC(this.su_ch2);
/*  951 */       this.su_j2++;
/*      */     } else {
/*  953 */       this.currentState = 2;
/*  954 */       this.su_i2++;
/*  955 */       this.su_count = 0;
/*  956 */       setupRandPartA();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupNoRandPartB() throws IOException {
/*  961 */     if (this.su_ch2 != this.su_chPrev) {
/*  962 */       this.su_count = 1;
/*  963 */       setupNoRandPartA();
/*  964 */     } else if (++this.su_count >= 4) {
/*  965 */       this.su_z = (char)(this.data.ll8[this.su_tPos] & 0xFF);
/*  966 */       this.su_tPos = this.data.tt[this.su_tPos];
/*  967 */       this.su_j2 = 0;
/*  968 */       setupNoRandPartC();
/*      */     } else {
/*  970 */       setupNoRandPartA();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setupNoRandPartC() throws IOException {
/*  975 */     if (this.su_j2 < this.su_z) {
/*  976 */       int su_ch2Shadow = this.su_ch2;
/*  977 */       this.currentChar = su_ch2Shadow;
/*  978 */       this.crc.updateCRC(su_ch2Shadow);
/*  979 */       this.su_j2++;
/*  980 */       this.currentState = 7;
/*      */     } else {
/*  982 */       this.su_i2++;
/*  983 */       this.su_count = 0;
/*  984 */       setupNoRandPartA();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class Data
/*      */   {
/*  991 */     final boolean[] inUse = new boolean[256];
/*      */     
/*  993 */     final byte[] seqToUnseq = new byte[256];
/*  994 */     final byte[] selector = new byte[18002];
/*  995 */     final byte[] selectorMtf = new byte[18002];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1001 */     final int[] unzftab = new int[256];
/*      */     
/* 1003 */     final int[][] limit = new int[6][258];
/* 1004 */     final int[][] base = new int[6][258];
/* 1005 */     final int[][] perm = new int[6][258];
/* 1006 */     final int[] minLens = new int[6];
/*      */     
/* 1008 */     final int[] cftab = new int[257];
/* 1009 */     final char[] getAndMoveToFrontDecode_yy = new char[256];
/* 1010 */     final char[][] temp_charArray2d = new char[6][258];
/* 1011 */     final byte[] recvDecodingTables_pos = new byte[6];
/*      */ 
/*      */ 
/*      */     
/*      */     int[] tt;
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] ll8;
/*      */ 
/*      */ 
/*      */     
/*      */     Data(int blockSize100k) {
/* 1024 */       this.ll8 = new byte[blockSize100k * 100000];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int[] initTT(int length) {
/* 1036 */       int[] ttShadow = this.tt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1042 */       if (ttShadow == null || ttShadow.length < length) {
/* 1043 */         this.tt = ttShadow = new int[length];
/*      */       }
/*      */       
/* 1046 */       return ttShadow;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void reportCRCError() {
/* 1056 */     System.err.println("BZip2 CRC error");
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/bzip2/CBZip2InputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */