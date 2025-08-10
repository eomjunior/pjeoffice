/*      */ package org.apache.tools.bzip2;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CBZip2OutputStream
/*      */   extends OutputStream
/*      */   implements BZip2Constants
/*      */ {
/*      */   public static final int MIN_BLOCKSIZE = 1;
/*      */   public static final int MAX_BLOCKSIZE = 9;
/*      */   protected static final int SETMASK = 2097152;
/*      */   protected static final int CLEARMASK = -2097153;
/*      */   protected static final int GREATER_ICOST = 15;
/*      */   protected static final int LESSER_ICOST = 0;
/*      */   protected static final int SMALL_THRESH = 20;
/*      */   protected static final int DEPTH_THRESH = 10;
/*      */   protected static final int WORK_FACTOR = 30;
/*      */   protected static final int QSORT_STACK_SIZE = 1000;
/*  207 */   private static final int[] INCS = new int[] { 1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484 };
/*      */ 
/*      */ 
/*      */   
/*      */   private int last;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int blockSize100k;
/*      */ 
/*      */ 
/*      */   
/*      */   private int bsBuff;
/*      */ 
/*      */   
/*      */   private int bsLive;
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void hbMakeCodeLengths(char[] len, int[] freq, int alphaSize, int maxLen) {
/*  227 */     int[] heap = new int[516];
/*  228 */     int[] weight = new int[516];
/*  229 */     int[] parent = new int[516];
/*      */     
/*  231 */     for (int i = alphaSize; --i >= 0;) {
/*  232 */       weight[i + 1] = ((freq[i] == 0) ? 1 : freq[i]) << 8;
/*      */     }
/*      */     
/*  235 */     for (boolean tooLong = true; tooLong; ) {
/*  236 */       tooLong = false;
/*      */       
/*  238 */       int nNodes = alphaSize;
/*  239 */       int nHeap = 0;
/*  240 */       heap[0] = 0;
/*  241 */       weight[0] = 0;
/*  242 */       parent[0] = -2;
/*      */       int j;
/*  244 */       for (j = 1; j <= alphaSize; j++) {
/*  245 */         parent[j] = -1;
/*  246 */         nHeap++;
/*  247 */         heap[nHeap] = j;
/*      */         
/*  249 */         int zz = nHeap;
/*  250 */         int tmp = heap[zz];
/*  251 */         while (weight[tmp] < weight[heap[zz >> 1]]) {
/*  252 */           heap[zz] = heap[zz >> 1];
/*  253 */           zz >>= 1;
/*      */         } 
/*  255 */         heap[zz] = tmp;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  260 */       while (nHeap > 1) {
/*  261 */         int n1 = heap[1];
/*  262 */         heap[1] = heap[nHeap];
/*  263 */         nHeap--;
/*      */         
/*  265 */         int yy = 0;
/*  266 */         int zz = 1;
/*  267 */         int tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  270 */           yy = zz << 1;
/*      */           
/*  272 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  276 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  278 */             yy++;
/*      */           }
/*      */           
/*  281 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  285 */           heap[zz] = heap[yy];
/*  286 */           zz = yy;
/*      */         } 
/*      */         
/*  289 */         heap[zz] = tmp;
/*      */         
/*  291 */         int n2 = heap[1];
/*  292 */         heap[1] = heap[nHeap];
/*  293 */         nHeap--;
/*      */         
/*  295 */         yy = 0;
/*  296 */         zz = 1;
/*  297 */         tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  300 */           yy = zz << 1;
/*      */           
/*  302 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  306 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  308 */             yy++;
/*      */           }
/*      */           
/*  311 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  315 */           heap[zz] = heap[yy];
/*  316 */           zz = yy;
/*      */         } 
/*      */         
/*  319 */         heap[zz] = tmp;
/*  320 */         nNodes++;
/*  321 */         parent[n2] = nNodes; parent[n1] = nNodes;
/*      */         
/*  323 */         int weight_n1 = weight[n1];
/*  324 */         int weight_n2 = weight[n2];
/*  325 */         weight[nNodes] = (weight_n1 & 0xFFFFFF00) + (weight_n2 & 0xFFFFFF00) | 1 + (
/*      */ 
/*      */           
/*  328 */           ((weight_n1 & 0xFF) > (weight_n2 & 0xFF)) ? (
/*      */           
/*  330 */           weight_n1 & 0xFF) : (
/*  331 */           weight_n2 & 0xFF));
/*      */ 
/*      */         
/*  334 */         parent[nNodes] = -1;
/*  335 */         nHeap++;
/*  336 */         heap[nHeap] = nNodes;
/*      */         
/*  338 */         tmp = 0;
/*  339 */         zz = nHeap;
/*  340 */         tmp = heap[zz];
/*  341 */         int weight_tmp = weight[tmp];
/*  342 */         while (weight_tmp < weight[heap[zz >> 1]]) {
/*  343 */           heap[zz] = heap[zz >> 1];
/*  344 */           zz >>= 1;
/*      */         } 
/*  346 */         heap[zz] = tmp;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  352 */       for (j = 1; j <= alphaSize; j++) {
/*  353 */         int m = 0;
/*  354 */         int k = j;
/*      */         int parent_k;
/*  356 */         while ((parent_k = parent[k]) >= 0) {
/*  357 */           k = parent_k;
/*  358 */           m++;
/*      */         } 
/*      */         
/*  361 */         len[j - 1] = (char)m;
/*  362 */         if (m > maxLen) {
/*  363 */           tooLong = true;
/*      */         }
/*      */       } 
/*      */       
/*  367 */       if (tooLong) {
/*  368 */         for (j = 1; j < alphaSize; j++) {
/*  369 */           int k = weight[j] >> 8;
/*  370 */           k = 1 + (k >> 1);
/*  371 */           weight[j] = k << 8;
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
/*      */   private static void hbMakeCodeLengths(byte[] len, int[] freq, Data dat, int alphaSize, int maxLen) {
/*  384 */     int[] heap = dat.heap;
/*  385 */     int[] weight = dat.weight;
/*  386 */     int[] parent = dat.parent;
/*      */     
/*  388 */     for (int i = alphaSize; --i >= 0;) {
/*  389 */       weight[i + 1] = ((freq[i] == 0) ? 1 : freq[i]) << 8;
/*      */     }
/*      */     
/*  392 */     for (boolean tooLong = true; tooLong; ) {
/*  393 */       tooLong = false;
/*      */       
/*  395 */       int nNodes = alphaSize;
/*  396 */       int nHeap = 0;
/*  397 */       heap[0] = 0;
/*  398 */       weight[0] = 0;
/*  399 */       parent[0] = -2;
/*      */       int j;
/*  401 */       for (j = 1; j <= alphaSize; j++) {
/*  402 */         parent[j] = -1;
/*  403 */         nHeap++;
/*  404 */         heap[nHeap] = j;
/*      */         
/*  406 */         int zz = nHeap;
/*  407 */         int tmp = heap[zz];
/*  408 */         while (weight[tmp] < weight[heap[zz >> 1]]) {
/*  409 */           heap[zz] = heap[zz >> 1];
/*  410 */           zz >>= 1;
/*      */         } 
/*  412 */         heap[zz] = tmp;
/*      */       } 
/*      */       
/*  415 */       while (nHeap > 1) {
/*  416 */         int n1 = heap[1];
/*  417 */         heap[1] = heap[nHeap];
/*  418 */         nHeap--;
/*      */         
/*  420 */         int yy = 0;
/*  421 */         int zz = 1;
/*  422 */         int tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  425 */           yy = zz << 1;
/*      */           
/*  427 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  431 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  433 */             yy++;
/*      */           }
/*      */           
/*  436 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  440 */           heap[zz] = heap[yy];
/*  441 */           zz = yy;
/*      */         } 
/*      */         
/*  444 */         heap[zz] = tmp;
/*      */         
/*  446 */         int n2 = heap[1];
/*  447 */         heap[1] = heap[nHeap];
/*  448 */         nHeap--;
/*      */         
/*  450 */         yy = 0;
/*  451 */         zz = 1;
/*  452 */         tmp = heap[1];
/*      */         
/*      */         while (true) {
/*  455 */           yy = zz << 1;
/*      */           
/*  457 */           if (yy > nHeap) {
/*      */             break;
/*      */           }
/*      */           
/*  461 */           if (yy < nHeap && weight[heap[yy + 1]] < weight[heap[yy]])
/*      */           {
/*  463 */             yy++;
/*      */           }
/*      */           
/*  466 */           if (weight[tmp] < weight[heap[yy]]) {
/*      */             break;
/*      */           }
/*      */           
/*  470 */           heap[zz] = heap[yy];
/*  471 */           zz = yy;
/*      */         } 
/*      */         
/*  474 */         heap[zz] = tmp;
/*  475 */         nNodes++;
/*  476 */         parent[n2] = nNodes; parent[n1] = nNodes;
/*      */         
/*  478 */         int weight_n1 = weight[n1];
/*  479 */         int weight_n2 = weight[n2];
/*  480 */         weight[nNodes] = (weight_n1 & 0xFFFFFF00) + (weight_n2 & 0xFFFFFF00) | 1 + (
/*      */           
/*  482 */           ((weight_n1 & 0xFF) > (weight_n2 & 0xFF)) ? (
/*      */           
/*  484 */           weight_n1 & 0xFF) : (
/*  485 */           weight_n2 & 0xFF));
/*      */         
/*  487 */         parent[nNodes] = -1;
/*  488 */         nHeap++;
/*  489 */         heap[nHeap] = nNodes;
/*      */         
/*  491 */         tmp = 0;
/*  492 */         zz = nHeap;
/*  493 */         tmp = heap[zz];
/*  494 */         int weight_tmp = weight[tmp];
/*  495 */         while (weight_tmp < weight[heap[zz >> 1]]) {
/*  496 */           heap[zz] = heap[zz >> 1];
/*  497 */           zz >>= 1;
/*      */         } 
/*  499 */         heap[zz] = tmp;
/*      */       } 
/*      */ 
/*      */       
/*  503 */       for (j = 1; j <= alphaSize; j++) {
/*  504 */         int m = 0;
/*  505 */         int k = j;
/*      */         int parent_k;
/*  507 */         while ((parent_k = parent[k]) >= 0) {
/*  508 */           k = parent_k;
/*  509 */           m++;
/*      */         } 
/*      */         
/*  512 */         len[j - 1] = (byte)m;
/*  513 */         if (m > maxLen) {
/*  514 */           tooLong = true;
/*      */         }
/*      */       } 
/*      */       
/*  518 */       if (tooLong) {
/*  519 */         for (j = 1; j < alphaSize; j++) {
/*  520 */           int k = weight[j] >> 8;
/*  521 */           k = 1 + (k >> 1);
/*  522 */           weight[j] = k << 8;
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
/*      */ 
/*      */ 
/*      */   
/*  541 */   private final CRC crc = new CRC();
/*      */   
/*      */   private int nInUse;
/*      */   
/*      */   private int nMTF;
/*      */   
/*  547 */   private int currentChar = -1;
/*  548 */   private int runLength = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private int blockCRC;
/*      */ 
/*      */ 
/*      */   
/*      */   private int combinedCRC;
/*      */ 
/*      */ 
/*      */   
/*      */   private final int allowableBlockSize;
/*      */ 
/*      */   
/*      */   private Data data;
/*      */ 
/*      */   
/*      */   private BlockSort blockSorter;
/*      */ 
/*      */   
/*      */   private OutputStream out;
/*      */ 
/*      */ 
/*      */   
/*      */   public static int chooseBlockSize(long inputLength) {
/*  574 */     return (inputLength > 0L) ? 
/*  575 */       (int)Math.min(inputLength / 132000L + 1L, 9L) : 9;
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
/*      */   public CBZip2OutputStream(OutputStream out) throws IOException {
/*  596 */     this(out, 9);
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
/*      */   public CBZip2OutputStream(OutputStream out, int blockSize) throws IOException {
/*  628 */     if (blockSize < 1) {
/*  629 */       throw new IllegalArgumentException("blockSize(" + blockSize + ") < 1");
/*      */     }
/*      */     
/*  632 */     if (blockSize > 9) {
/*  633 */       throw new IllegalArgumentException("blockSize(" + blockSize + ") > 9");
/*      */     }
/*      */ 
/*      */     
/*  637 */     this.blockSize100k = blockSize;
/*  638 */     this.out = out;
/*      */ 
/*      */     
/*  641 */     this.allowableBlockSize = this.blockSize100k * 100000 - 20;
/*  642 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(int b) throws IOException {
/*  648 */     if (this.out != null) {
/*  649 */       write0(b);
/*      */     } else {
/*  651 */       throw new IOException("closed");
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
/*      */   private void writeRun() throws IOException {
/*  669 */     int lastShadow = this.last;
/*      */     
/*  671 */     if (lastShadow < this.allowableBlockSize) {
/*  672 */       int currentCharShadow = this.currentChar;
/*  673 */       Data dataShadow = this.data;
/*  674 */       dataShadow.inUse[currentCharShadow] = true;
/*  675 */       byte ch = (byte)currentCharShadow;
/*      */       
/*  677 */       int runLengthShadow = this.runLength;
/*  678 */       this.crc.updateCRC(currentCharShadow, runLengthShadow);
/*  679 */       byte[] block = dataShadow.block;
/*      */       
/*  681 */       switch (runLengthShadow) {
/*      */         case 1:
/*  683 */           block[lastShadow + 2] = ch;
/*  684 */           this.last = lastShadow + 1;
/*      */           return;
/*      */         case 2:
/*  687 */           block[lastShadow + 2] = ch;
/*  688 */           block[lastShadow + 3] = ch;
/*  689 */           this.last = lastShadow + 2;
/*      */           return;
/*      */         case 3:
/*  692 */           block[lastShadow + 2] = ch;
/*  693 */           block[lastShadow + 3] = ch;
/*  694 */           block[lastShadow + 4] = ch;
/*  695 */           this.last = lastShadow + 3;
/*      */           return;
/*      */       } 
/*  698 */       runLengthShadow -= 4;
/*  699 */       dataShadow.inUse[runLengthShadow] = true;
/*  700 */       block[lastShadow + 2] = ch;
/*  701 */       block[lastShadow + 3] = ch;
/*  702 */       block[lastShadow + 4] = ch;
/*  703 */       block[lastShadow + 5] = ch;
/*  704 */       block[lastShadow + 6] = (byte)runLengthShadow;
/*  705 */       this.last = lastShadow + 5;
/*      */     }
/*      */     else {
/*      */       
/*  709 */       endBlock();
/*  710 */       initBlock();
/*  711 */       writeRun();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/*  720 */     finish();
/*  721 */     super.finalize();
/*      */   }
/*      */ 
/*      */   
/*      */   public void finish() throws IOException {
/*  726 */     if (this.out != null) {
/*      */       try {
/*  728 */         if (this.runLength > 0) {
/*  729 */           writeRun();
/*      */         }
/*  731 */         this.currentChar = -1;
/*  732 */         endBlock();
/*  733 */         endCompression();
/*      */       } finally {
/*  735 */         this.out = null;
/*  736 */         this.data = null;
/*  737 */         this.blockSorter = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  744 */     if (this.out != null) {
/*  745 */       OutputStream outShadow = this.out;
/*  746 */       finish();
/*  747 */       outShadow.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {
/*  753 */     OutputStream outShadow = this.out;
/*  754 */     if (outShadow != null) {
/*  755 */       outShadow.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() throws IOException {
/*  764 */     this.data = new Data(this.blockSize100k);
/*  765 */     this.blockSorter = new BlockSort(this.data);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  771 */     bsPutUByte(104);
/*  772 */     bsPutUByte(48 + this.blockSize100k);
/*      */     
/*  774 */     this.combinedCRC = 0;
/*  775 */     initBlock();
/*      */   }
/*      */ 
/*      */   
/*      */   private void initBlock() {
/*  780 */     this.crc.initialiseCRC();
/*  781 */     this.last = -1;
/*      */ 
/*      */     
/*  784 */     boolean[] inUse = this.data.inUse;
/*  785 */     for (int i = 256; --i >= 0;) {
/*  786 */       inUse[i] = false;
/*      */     }
/*      */   }
/*      */   
/*      */   private void endBlock() throws IOException {
/*  791 */     this.blockCRC = this.crc.getFinalCRC();
/*  792 */     this.combinedCRC = this.combinedCRC << 1 | this.combinedCRC >>> 31;
/*  793 */     this.combinedCRC ^= this.blockCRC;
/*      */ 
/*      */     
/*  796 */     if (this.last == -1) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  801 */     blockSort();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  814 */     bsPutUByte(49);
/*  815 */     bsPutUByte(65);
/*  816 */     bsPutUByte(89);
/*  817 */     bsPutUByte(38);
/*  818 */     bsPutUByte(83);
/*  819 */     bsPutUByte(89);
/*      */ 
/*      */     
/*  822 */     bsPutInt(this.blockCRC);
/*      */ 
/*      */     
/*  825 */     bsW(1, 0);
/*      */ 
/*      */     
/*  828 */     moveToFrontCodeAndSend();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endCompression() throws IOException {
/*  838 */     bsPutUByte(23);
/*  839 */     bsPutUByte(114);
/*  840 */     bsPutUByte(69);
/*  841 */     bsPutUByte(56);
/*  842 */     bsPutUByte(80);
/*  843 */     bsPutUByte(144);
/*      */     
/*  845 */     bsPutInt(this.combinedCRC);
/*  846 */     bsFinishedWithStream();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getBlockSize() {
/*  855 */     return this.blockSize100k;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(byte[] buf, int offs, int len) throws IOException {
/*  861 */     if (offs < 0) {
/*  862 */       throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
/*      */     }
/*  864 */     if (len < 0) {
/*  865 */       throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
/*      */     }
/*  867 */     if (offs + len > buf.length) {
/*  868 */       throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > buf.length(" + buf.length + ").");
/*      */     }
/*      */ 
/*      */     
/*  872 */     if (this.out == null) {
/*  873 */       throw new IOException("stream closed");
/*      */     }
/*      */     
/*  876 */     for (int hi = offs + len; offs < hi;) {
/*  877 */       write0(buf[offs++]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void write0(int b) throws IOException {
/*  886 */     if (this.currentChar != -1) {
/*  887 */       b &= 0xFF;
/*  888 */       if (this.currentChar == b) {
/*  889 */         if (++this.runLength > 254) {
/*  890 */           writeRun();
/*  891 */           this.currentChar = -1;
/*  892 */           this.runLength = 0;
/*      */         } 
/*      */       } else {
/*      */         
/*  896 */         writeRun();
/*  897 */         this.runLength = 1;
/*  898 */         this.currentChar = b;
/*      */       } 
/*      */     } else {
/*  901 */       this.currentChar = b & 0xFF;
/*  902 */       this.runLength++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void hbAssignCodes(int[] code, byte[] length, int minLen, int maxLen, int alphaSize) {
/*  909 */     int vec = 0;
/*  910 */     for (int n = minLen; n <= maxLen; n++) {
/*  911 */       for (int i = 0; i < alphaSize; i++) {
/*  912 */         if ((length[i] & 0xFF) == n) {
/*  913 */           code[i] = vec;
/*  914 */           vec++;
/*      */         } 
/*      */       } 
/*  917 */       vec <<= 1;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void bsFinishedWithStream() throws IOException {
/*  922 */     while (this.bsLive > 0) {
/*  923 */       int ch = this.bsBuff >> 24;
/*  924 */       this.out.write(ch);
/*  925 */       this.bsBuff <<= 8;
/*  926 */       this.bsLive -= 8;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void bsW(int n, int v) throws IOException {
/*  931 */     OutputStream outShadow = this.out;
/*  932 */     int bsLiveShadow = this.bsLive;
/*  933 */     int bsBuffShadow = this.bsBuff;
/*      */     
/*  935 */     while (bsLiveShadow >= 8) {
/*  936 */       outShadow.write(bsBuffShadow >> 24);
/*  937 */       bsBuffShadow <<= 8;
/*  938 */       bsLiveShadow -= 8;
/*      */     } 
/*      */     
/*  941 */     this.bsBuff = bsBuffShadow | v << 32 - bsLiveShadow - n;
/*  942 */     this.bsLive = bsLiveShadow + n;
/*      */   }
/*      */   
/*      */   private void bsPutUByte(int c) throws IOException {
/*  946 */     bsW(8, c);
/*      */   }
/*      */   
/*      */   private void bsPutInt(int u) throws IOException {
/*  950 */     bsW(8, u >> 24 & 0xFF);
/*  951 */     bsW(8, u >> 16 & 0xFF);
/*  952 */     bsW(8, u >> 8 & 0xFF);
/*  953 */     bsW(8, u & 0xFF);
/*      */   }
/*      */   
/*      */   private void sendMTFValues() throws IOException {
/*  957 */     byte[][] len = this.data.sendMTFValues_len;
/*  958 */     int alphaSize = this.nInUse + 2;
/*      */     
/*  960 */     for (int t = 6; --t >= 0; ) {
/*  961 */       byte[] len_t = len[t];
/*  962 */       for (int v = alphaSize; --v >= 0;) {
/*  963 */         len_t[v] = 15;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  970 */     int nGroups = (this.nMTF < 200) ? 2 : ((this.nMTF < 600) ? 3 : ((this.nMTF < 1200) ? 4 : ((this.nMTF < 2400) ? 5 : 6)));
/*      */ 
/*      */     
/*  973 */     sendMTFValues0(nGroups, alphaSize);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  978 */     int nSelectors = sendMTFValues1(nGroups, alphaSize);
/*      */ 
/*      */     
/*  981 */     sendMTFValues2(nGroups, nSelectors);
/*      */ 
/*      */     
/*  984 */     sendMTFValues3(nGroups, alphaSize);
/*      */ 
/*      */     
/*  987 */     sendMTFValues4();
/*      */ 
/*      */     
/*  990 */     sendMTFValues5(nGroups, nSelectors);
/*      */ 
/*      */     
/*  993 */     sendMTFValues6(nGroups, alphaSize);
/*      */ 
/*      */     
/*  996 */     sendMTFValues7();
/*      */   }
/*      */   
/*      */   private void sendMTFValues0(int nGroups, int alphaSize) {
/* 1000 */     byte[][] len = this.data.sendMTFValues_len;
/* 1001 */     int[] mtfFreq = this.data.mtfFreq;
/*      */     
/* 1003 */     int remF = this.nMTF;
/* 1004 */     int gs = 0;
/*      */     
/* 1006 */     for (int nPart = nGroups; nPart > 0; nPart--) {
/* 1007 */       int tFreq = remF / nPart;
/* 1008 */       int ge = gs - 1;
/* 1009 */       int aFreq = 0;
/*      */       
/* 1011 */       while (aFreq < tFreq && ge < alphaSize - 1) {
/* 1012 */         aFreq += mtfFreq[++ge];
/*      */       }
/*      */       
/* 1015 */       if (ge > gs && nPart != nGroups && nPart != 1 && (nGroups - nPart & 0x1) != 0) {
/* 1016 */         aFreq -= mtfFreq[ge--];
/*      */       }
/*      */       
/* 1019 */       byte[] len_np = len[nPart - 1];
/* 1020 */       for (int v = alphaSize; --v >= 0; ) {
/* 1021 */         if (v >= gs && v <= ge) {
/* 1022 */           len_np[v] = 0; continue;
/*      */         } 
/* 1024 */         len_np[v] = 15;
/*      */       } 
/*      */ 
/*      */       
/* 1028 */       gs = ge + 1;
/* 1029 */       remF -= aFreq;
/*      */     } 
/*      */   }
/*      */   
/*      */   private int sendMTFValues1(int nGroups, int alphaSize) {
/* 1034 */     Data dataShadow = this.data;
/* 1035 */     int[][] rfreq = dataShadow.sendMTFValues_rfreq;
/* 1036 */     int[] fave = dataShadow.sendMTFValues_fave;
/* 1037 */     short[] cost = dataShadow.sendMTFValues_cost;
/* 1038 */     char[] sfmap = dataShadow.sfmap;
/* 1039 */     byte[] selector = dataShadow.selector;
/* 1040 */     byte[][] len = dataShadow.sendMTFValues_len;
/* 1041 */     byte[] len_0 = len[0];
/* 1042 */     byte[] len_1 = len[1];
/* 1043 */     byte[] len_2 = len[2];
/* 1044 */     byte[] len_3 = len[3];
/* 1045 */     byte[] len_4 = len[4];
/* 1046 */     byte[] len_5 = len[5];
/* 1047 */     int nMTFShadow = this.nMTF;
/*      */     
/* 1049 */     int nSelectors = 0;
/*      */     
/* 1051 */     for (int iter = 0; iter < 4; iter++) {
/* 1052 */       for (int i = nGroups; --i >= 0; ) {
/* 1053 */         fave[i] = 0;
/* 1054 */         int[] rfreqt = rfreq[i];
/* 1055 */         for (int j = alphaSize; --j >= 0;) {
/* 1056 */           rfreqt[j] = 0;
/*      */         }
/*      */       } 
/*      */       
/* 1060 */       nSelectors = 0;
/*      */       int gs;
/* 1062 */       for (gs = 0; gs < this.nMTF; ) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1070 */         int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
/*      */         
/* 1072 */         if (nGroups == 6) {
/*      */ 
/*      */           
/* 1075 */           short cost0 = 0;
/* 1076 */           short cost1 = 0;
/* 1077 */           short cost2 = 0;
/* 1078 */           short cost3 = 0;
/* 1079 */           short cost4 = 0;
/* 1080 */           short cost5 = 0;
/*      */           
/* 1082 */           for (int m = gs; m <= ge; m++) {
/* 1083 */             int icv = sfmap[m];
/* 1084 */             cost0 = (short)(cost0 + (len_0[icv] & 0xFF));
/* 1085 */             cost1 = (short)(cost1 + (len_1[icv] & 0xFF));
/* 1086 */             cost2 = (short)(cost2 + (len_2[icv] & 0xFF));
/* 1087 */             cost3 = (short)(cost3 + (len_3[icv] & 0xFF));
/* 1088 */             cost4 = (short)(cost4 + (len_4[icv] & 0xFF));
/* 1089 */             cost5 = (short)(cost5 + (len_5[icv] & 0xFF));
/*      */           } 
/*      */           
/* 1092 */           cost[0] = cost0;
/* 1093 */           cost[1] = cost1;
/* 1094 */           cost[2] = cost2;
/* 1095 */           cost[3] = cost3;
/* 1096 */           cost[4] = cost4;
/* 1097 */           cost[5] = cost5;
/*      */         } else {
/*      */           
/* 1100 */           for (int n = nGroups; --n >= 0;) {
/* 1101 */             cost[n] = 0;
/*      */           }
/*      */           
/* 1104 */           for (int m = gs; m <= ge; m++) {
/* 1105 */             int icv = sfmap[m];
/* 1106 */             for (int i1 = nGroups; --i1 >= 0;) {
/* 1107 */               cost[i1] = (short)(cost[i1] + (len[i1][icv] & 0xFF));
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1116 */         int bt = -1;
/* 1117 */         for (int j = nGroups, bc = 999999999; --j >= 0; ) {
/* 1118 */           int cost_t = cost[j];
/* 1119 */           if (cost_t < bc) {
/* 1120 */             bc = cost_t;
/* 1121 */             bt = j;
/*      */           } 
/*      */         } 
/*      */         
/* 1125 */         fave[bt] = fave[bt] + 1;
/* 1126 */         selector[nSelectors] = (byte)bt;
/* 1127 */         nSelectors++;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1132 */         int[] rfreq_bt = rfreq[bt];
/* 1133 */         for (int k = gs; k <= ge; k++) {
/* 1134 */           rfreq_bt[sfmap[k]] = rfreq_bt[sfmap[k]] + 1;
/*      */         }
/*      */         
/* 1137 */         gs = ge + 1;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1143 */       for (int t = 0; t < nGroups; t++) {
/* 1144 */         hbMakeCodeLengths(len[t], rfreq[t], this.data, alphaSize, 20);
/*      */       }
/*      */     } 
/*      */     
/* 1148 */     return nSelectors;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendMTFValues2(int nGroups, int nSelectors) {
/* 1154 */     Data dataShadow = this.data;
/* 1155 */     byte[] pos = dataShadow.sendMTFValues2_pos;
/*      */     int i;
/* 1157 */     for (i = nGroups; --i >= 0;) {
/* 1158 */       pos[i] = (byte)i;
/*      */     }
/*      */     
/* 1161 */     for (i = 0; i < nSelectors; i++) {
/* 1162 */       byte ll_i = dataShadow.selector[i];
/* 1163 */       byte tmp = pos[0];
/* 1164 */       int j = 0;
/*      */       
/* 1166 */       while (ll_i != tmp) {
/* 1167 */         j++;
/* 1168 */         byte tmp2 = tmp;
/* 1169 */         tmp = pos[j];
/* 1170 */         pos[j] = tmp2;
/*      */       } 
/*      */       
/* 1173 */       pos[0] = tmp;
/* 1174 */       dataShadow.selectorMtf[i] = (byte)j;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendMTFValues3(int nGroups, int alphaSize) {
/* 1179 */     int[][] code = this.data.sendMTFValues_code;
/* 1180 */     byte[][] len = this.data.sendMTFValues_len;
/*      */     
/* 1182 */     for (int t = 0; t < nGroups; t++) {
/* 1183 */       int minLen = 32;
/* 1184 */       int maxLen = 0;
/* 1185 */       byte[] len_t = len[t];
/* 1186 */       for (int i = alphaSize; --i >= 0; ) {
/* 1187 */         int l = len_t[i] & 0xFF;
/* 1188 */         if (l > maxLen) {
/* 1189 */           maxLen = l;
/*      */         }
/* 1191 */         if (l < minLen) {
/* 1192 */           minLen = l;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1199 */       hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void sendMTFValues4() throws IOException {
/* 1204 */     boolean[] inUse = this.data.inUse;
/* 1205 */     boolean[] inUse16 = this.data.sentMTFValues4_inUse16;
/*      */     int i;
/* 1207 */     for (i = 16; --i >= 0; ) {
/* 1208 */       inUse16[i] = false;
/* 1209 */       int i16 = i * 16;
/* 1210 */       for (int k = 16; --k >= 0;) {
/* 1211 */         if (inUse[i16 + k]) {
/* 1212 */           inUse16[i] = true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1218 */     for (i = 0; i < 16; i++) {
/* 1219 */       bsW(1, inUse16[i] ? 1 : 0);
/*      */     }
/*      */     
/* 1222 */     OutputStream outShadow = this.out;
/* 1223 */     int bsLiveShadow = this.bsLive;
/* 1224 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1226 */     for (int j = 0; j < 16; j++) {
/* 1227 */       if (inUse16[j]) {
/* 1228 */         int i16 = j * 16;
/* 1229 */         for (int k = 0; k < 16; k++) {
/*      */           
/* 1231 */           while (bsLiveShadow >= 8) {
/* 1232 */             outShadow.write(bsBuffShadow >> 24);
/* 1233 */             bsBuffShadow <<= 8;
/* 1234 */             bsLiveShadow -= 8;
/*      */           } 
/* 1236 */           if (inUse[i16 + k]) {
/* 1237 */             bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
/*      */           }
/* 1239 */           bsLiveShadow++;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1244 */     this.bsBuff = bsBuffShadow;
/* 1245 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendMTFValues5(int nGroups, int nSelectors) throws IOException {
/* 1250 */     bsW(3, nGroups);
/* 1251 */     bsW(15, nSelectors);
/*      */     
/* 1253 */     OutputStream outShadow = this.out;
/* 1254 */     byte[] selectorMtf = this.data.selectorMtf;
/*      */     
/* 1256 */     int bsLiveShadow = this.bsLive;
/* 1257 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1259 */     for (int i = 0; i < nSelectors; i++) {
/* 1260 */       for (int j = 0, hj = selectorMtf[i] & 0xFF; j < hj; j++) {
/*      */         
/* 1262 */         while (bsLiveShadow >= 8) {
/* 1263 */           outShadow.write(bsBuffShadow >> 24);
/* 1264 */           bsBuffShadow <<= 8;
/* 1265 */           bsLiveShadow -= 8;
/*      */         } 
/* 1267 */         bsBuffShadow |= 1 << 32 - bsLiveShadow - 1;
/* 1268 */         bsLiveShadow++;
/*      */       } 
/*      */ 
/*      */       
/* 1272 */       while (bsLiveShadow >= 8) {
/* 1273 */         outShadow.write(bsBuffShadow >> 24);
/* 1274 */         bsBuffShadow <<= 8;
/* 1275 */         bsLiveShadow -= 8;
/*      */       } 
/*      */       
/* 1278 */       bsLiveShadow++;
/*      */     } 
/*      */     
/* 1281 */     this.bsBuff = bsBuffShadow;
/* 1282 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */ 
/*      */   
/*      */   private void sendMTFValues6(int nGroups, int alphaSize) throws IOException {
/* 1287 */     byte[][] len = this.data.sendMTFValues_len;
/* 1288 */     OutputStream outShadow = this.out;
/*      */     
/* 1290 */     int bsLiveShadow = this.bsLive;
/* 1291 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1293 */     for (int t = 0; t < nGroups; t++) {
/* 1294 */       byte[] len_t = len[t];
/* 1295 */       int curr = len_t[0] & 0xFF;
/*      */ 
/*      */       
/* 1298 */       while (bsLiveShadow >= 8) {
/* 1299 */         outShadow.write(bsBuffShadow >> 24);
/* 1300 */         bsBuffShadow <<= 8;
/* 1301 */         bsLiveShadow -= 8;
/*      */       } 
/* 1303 */       bsBuffShadow |= curr << 32 - bsLiveShadow - 5;
/* 1304 */       bsLiveShadow += 5;
/*      */       
/* 1306 */       for (int i = 0; i < alphaSize; i++) {
/* 1307 */         int lti = len_t[i] & 0xFF;
/* 1308 */         while (curr < lti) {
/*      */           
/* 1310 */           while (bsLiveShadow >= 8) {
/* 1311 */             outShadow.write(bsBuffShadow >> 24);
/* 1312 */             bsBuffShadow <<= 8;
/* 1313 */             bsLiveShadow -= 8;
/*      */           } 
/* 1315 */           bsBuffShadow |= 2 << 32 - bsLiveShadow - 2;
/* 1316 */           bsLiveShadow += 2;
/*      */           
/* 1318 */           curr++;
/*      */         } 
/*      */         
/* 1321 */         while (curr > lti) {
/*      */           
/* 1323 */           while (bsLiveShadow >= 8) {
/* 1324 */             outShadow.write(bsBuffShadow >> 24);
/* 1325 */             bsBuffShadow <<= 8;
/* 1326 */             bsLiveShadow -= 8;
/*      */           } 
/* 1328 */           bsBuffShadow |= 3 << 32 - bsLiveShadow - 2;
/* 1329 */           bsLiveShadow += 2;
/*      */           
/* 1331 */           curr--;
/*      */         } 
/*      */ 
/*      */         
/* 1335 */         while (bsLiveShadow >= 8) {
/* 1336 */           outShadow.write(bsBuffShadow >> 24);
/* 1337 */           bsBuffShadow <<= 8;
/* 1338 */           bsLiveShadow -= 8;
/*      */         } 
/*      */         
/* 1341 */         bsLiveShadow++;
/*      */       } 
/*      */     } 
/*      */     
/* 1345 */     this.bsBuff = bsBuffShadow;
/* 1346 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */   
/*      */   private void sendMTFValues7() throws IOException {
/* 1350 */     Data dataShadow = this.data;
/* 1351 */     byte[][] len = dataShadow.sendMTFValues_len;
/* 1352 */     int[][] code = dataShadow.sendMTFValues_code;
/* 1353 */     OutputStream outShadow = this.out;
/* 1354 */     byte[] selector = dataShadow.selector;
/* 1355 */     char[] sfmap = dataShadow.sfmap;
/* 1356 */     int nMTFShadow = this.nMTF;
/*      */     
/* 1358 */     int selCtr = 0;
/*      */     
/* 1360 */     int bsLiveShadow = this.bsLive;
/* 1361 */     int bsBuffShadow = this.bsBuff;
/*      */     
/* 1363 */     for (int gs = 0; gs < nMTFShadow; ) {
/* 1364 */       int ge = Math.min(gs + 50 - 1, nMTFShadow - 1);
/* 1365 */       int selector_selCtr = selector[selCtr] & 0xFF;
/* 1366 */       int[] code_selCtr = code[selector_selCtr];
/* 1367 */       byte[] len_selCtr = len[selector_selCtr];
/*      */       
/* 1369 */       while (gs <= ge) {
/* 1370 */         int sfmap_i = sfmap[gs];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1376 */         while (bsLiveShadow >= 8) {
/* 1377 */           outShadow.write(bsBuffShadow >> 24);
/* 1378 */           bsBuffShadow <<= 8;
/* 1379 */           bsLiveShadow -= 8;
/*      */         } 
/* 1381 */         int n = len_selCtr[sfmap_i] & 0xFF;
/* 1382 */         bsBuffShadow |= code_selCtr[sfmap_i] << 32 - bsLiveShadow - n;
/* 1383 */         bsLiveShadow += n;
/*      */         
/* 1385 */         gs++;
/*      */       } 
/*      */       
/* 1388 */       gs = ge + 1;
/* 1389 */       selCtr++;
/*      */     } 
/*      */     
/* 1392 */     this.bsBuff = bsBuffShadow;
/* 1393 */     this.bsLive = bsLiveShadow;
/*      */   }
/*      */   
/*      */   private void moveToFrontCodeAndSend() throws IOException {
/* 1397 */     bsW(24, this.data.origPtr);
/* 1398 */     generateMTFValues();
/* 1399 */     sendMTFValues();
/*      */   }
/*      */   
/*      */   private void blockSort() {
/* 1403 */     this.blockSorter.blockSort(this.data, this.last);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateMTFValues() {
/* 1414 */     int lastShadow = this.last;
/* 1415 */     Data dataShadow = this.data;
/* 1416 */     boolean[] inUse = dataShadow.inUse;
/* 1417 */     byte[] block = dataShadow.block;
/* 1418 */     int[] fmap = dataShadow.fmap;
/* 1419 */     char[] sfmap = dataShadow.sfmap;
/* 1420 */     int[] mtfFreq = dataShadow.mtfFreq;
/* 1421 */     byte[] unseqToSeq = dataShadow.unseqToSeq;
/* 1422 */     byte[] yy = dataShadow.generateMTFValues_yy;
/*      */ 
/*      */     
/* 1425 */     int nInUseShadow = 0;
/* 1426 */     for (int i = 0; i < 256; i++) {
/* 1427 */       if (inUse[i]) {
/* 1428 */         unseqToSeq[i] = (byte)nInUseShadow;
/* 1429 */         nInUseShadow++;
/*      */       } 
/*      */     } 
/* 1432 */     this.nInUse = nInUseShadow;
/*      */     
/* 1434 */     int eob = nInUseShadow + 1;
/*      */     int j;
/* 1436 */     for (j = eob; j >= 0; j--) {
/* 1437 */       mtfFreq[j] = 0;
/*      */     }
/*      */     
/* 1440 */     for (j = nInUseShadow; --j >= 0;) {
/* 1441 */       yy[j] = (byte)j;
/*      */     }
/*      */     
/* 1444 */     int wr = 0;
/* 1445 */     int zPend = 0;
/*      */     
/* 1447 */     for (int k = 0; k <= lastShadow; k++) {
/* 1448 */       byte ll_i = unseqToSeq[block[fmap[k]] & 0xFF];
/* 1449 */       byte tmp = yy[0];
/* 1450 */       int m = 0;
/*      */       
/* 1452 */       while (ll_i != tmp) {
/* 1453 */         m++;
/* 1454 */         byte tmp2 = tmp;
/* 1455 */         tmp = yy[m];
/* 1456 */         yy[m] = tmp2;
/*      */       } 
/* 1458 */       yy[0] = tmp;
/*      */       
/* 1460 */       if (m == 0) {
/* 1461 */         zPend++;
/*      */       } else {
/* 1463 */         if (zPend > 0) {
/* 1464 */           zPend--;
/*      */           while (true) {
/* 1466 */             if ((zPend & 0x1) == 0) {
/* 1467 */               sfmap[wr] = Character.MIN_VALUE;
/* 1468 */               wr++;
/* 1469 */               mtfFreq[0] = mtfFreq[0] + 1;
/*      */             } else {
/* 1471 */               sfmap[wr] = '\001';
/* 1472 */               wr++;
/* 1473 */               mtfFreq[1] = mtfFreq[1] + 1;
/*      */             } 
/*      */             
/* 1476 */             if (zPend >= 2) {
/* 1477 */               zPend = zPend - 2 >> 1;
/*      */               continue;
/*      */             } 
/*      */             break;
/*      */           } 
/* 1482 */           zPend = 0;
/*      */         } 
/* 1484 */         sfmap[wr] = (char)(m + 1);
/* 1485 */         wr++;
/* 1486 */         mtfFreq[m + 1] = mtfFreq[m + 1] + 1;
/*      */       } 
/*      */     } 
/*      */     
/* 1490 */     if (zPend > 0) {
/* 1491 */       zPend--;
/*      */       while (true) {
/* 1493 */         if ((zPend & 0x1) == 0) {
/* 1494 */           sfmap[wr] = Character.MIN_VALUE;
/* 1495 */           wr++;
/* 1496 */           mtfFreq[0] = mtfFreq[0] + 1;
/*      */         } else {
/* 1498 */           sfmap[wr] = '\001';
/* 1499 */           wr++;
/* 1500 */           mtfFreq[1] = mtfFreq[1] + 1;
/*      */         } 
/*      */         
/* 1503 */         if (zPend >= 2) {
/* 1504 */           zPend = zPend - 2 >> 1;
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/* 1511 */     sfmap[wr] = (char)eob;
/* 1512 */     mtfFreq[eob] = mtfFreq[eob] + 1;
/* 1513 */     this.nMTF = wr + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static final class Data
/*      */   {
/* 1520 */     final boolean[] inUse = new boolean[256];
/* 1521 */     final byte[] unseqToSeq = new byte[256];
/* 1522 */     final int[] mtfFreq = new int[258];
/* 1523 */     final byte[] selector = new byte[18002];
/* 1524 */     final byte[] selectorMtf = new byte[18002];
/*      */     
/* 1526 */     final byte[] generateMTFValues_yy = new byte[256];
/* 1527 */     final byte[][] sendMTFValues_len = new byte[6][258];
/*      */     
/* 1529 */     final int[][] sendMTFValues_rfreq = new int[6][258];
/*      */     
/* 1531 */     final int[] sendMTFValues_fave = new int[6];
/* 1532 */     final short[] sendMTFValues_cost = new short[6];
/* 1533 */     final int[][] sendMTFValues_code = new int[6][258];
/*      */     
/* 1535 */     final byte[] sendMTFValues2_pos = new byte[6];
/* 1536 */     final boolean[] sentMTFValues4_inUse16 = new boolean[16];
/*      */     
/* 1538 */     final int[] heap = new int[260];
/* 1539 */     final int[] weight = new int[516];
/* 1540 */     final int[] parent = new int[516];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final byte[] block;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int[] fmap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final char[] sfmap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int origPtr;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Data(int blockSize100k) {
/* 1568 */       int n = blockSize100k * 100000;
/* 1569 */       this.block = new byte[n + 1 + 20];
/* 1570 */       this.fmap = new int[n];
/* 1571 */       this.sfmap = new char[2 * n];
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/bzip2/CBZip2OutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */