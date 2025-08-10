/*      */ package org.apache.tools.bzip2;
/*      */ 
/*      */ import java.util.BitSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class BlockSort
/*      */ {
/*      */   private static final int QSORT_STACK_SIZE = 1000;
/*      */   private static final int FALLBACK_QSORT_STACK_SIZE = 100;
/*      */   private static final int STACK_SIZE = 1000;
/*      */   private int workDone;
/*      */   private int workLimit;
/*      */   private boolean firstAttempt;
/*  133 */   private final int[] stack_ll = new int[1000];
/*  134 */   private final int[] stack_hh = new int[1000];
/*  135 */   private final int[] stack_dd = new int[1000];
/*      */   
/*  137 */   private final int[] mainSort_runningOrder = new int[256];
/*  138 */   private final int[] mainSort_copy = new int[256];
/*  139 */   private final boolean[] mainSort_bigDone = new boolean[256];
/*      */   
/*  141 */   private final int[] ftab = new int[65537];
/*      */   
/*      */   private final char[] quadrant;
/*      */   
/*      */   private static final int FALLBACK_QSORT_SMALL_THRESH = 10;
/*      */   
/*      */   private int[] eclass;
/*      */ 
/*      */   
/*      */   BlockSort(CBZip2OutputStream.Data data) {
/*  151 */     this.quadrant = data.sfmap;
/*      */   }
/*      */   
/*      */   void blockSort(CBZip2OutputStream.Data data, int last) {
/*  155 */     this.workLimit = 30 * last;
/*  156 */     this.workDone = 0;
/*  157 */     this.firstAttempt = true;
/*      */     
/*  159 */     if (last + 1 < 10000) {
/*  160 */       fallbackSort(data, last);
/*      */     } else {
/*  162 */       mainSort(data, last);
/*      */       
/*  164 */       if (this.firstAttempt && this.workDone > this.workLimit) {
/*  165 */         fallbackSort(data, last);
/*      */       }
/*      */     } 
/*      */     
/*  169 */     int[] fmap = data.fmap;
/*  170 */     data.origPtr = -1;
/*  171 */     for (int i = 0; i <= last; i++) {
/*  172 */       if (fmap[i] == 0) {
/*  173 */         data.origPtr = i;
/*      */         break;
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
/*      */   final void fallbackSort(CBZip2OutputStream.Data data, int last) {
/*  188 */     data.block[0] = data.block[last + 1];
/*  189 */     fallbackSort(data.fmap, data.block, last + 1); int i;
/*  190 */     for (i = 0; i < last + 1; i++) {
/*  191 */       data.fmap[i] = data.fmap[i] - 1;
/*      */     }
/*  193 */     for (i = 0; i < last + 1; i++) {
/*  194 */       if (data.fmap[i] == -1) {
/*  195 */         data.fmap[i] = last;
/*      */         break;
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
/*      */   private void fallbackSimpleSort(int[] fmap, int[] eclass, int lo, int hi) {
/*  272 */     if (lo == hi) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  277 */     if (hi - lo > 3) {
/*  278 */       for (int j = hi - 4; j >= lo; j--) {
/*  279 */         int tmp = fmap[j];
/*  280 */         int ec_tmp = eclass[tmp]; int k;
/*  281 */         for (k = j + 4; k <= hi && ec_tmp > eclass[fmap[k]]; 
/*  282 */           k += 4) {
/*  283 */           fmap[k - 4] = fmap[k];
/*      */         }
/*  285 */         fmap[k - 4] = tmp;
/*      */       } 
/*      */     }
/*      */     
/*  289 */     for (int i = hi - 1; i >= lo; i--) {
/*  290 */       int tmp = fmap[i];
/*  291 */       int ec_tmp = eclass[tmp]; int j;
/*  292 */       for (j = i + 1; j <= hi && ec_tmp > eclass[fmap[j]]; j++) {
/*  293 */         fmap[j - 1] = fmap[j];
/*      */       }
/*  295 */       fmap[j - 1] = tmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fswap(int[] fmap, int zz1, int zz2) {
/*  305 */     int zztmp = fmap[zz1];
/*  306 */     fmap[zz1] = fmap[zz2];
/*  307 */     fmap[zz2] = zztmp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fvswap(int[] fmap, int yyp1, int yyp2, int yyn) {
/*  314 */     while (yyn > 0) {
/*  315 */       fswap(fmap, yyp1, yyp2);
/*  316 */       yyp1++;
/*  317 */       yyp2++;
/*  318 */       yyn--;
/*      */     } 
/*      */   }
/*      */   
/*      */   private int fmin(int a, int b) {
/*  323 */     return (a < b) ? a : b;
/*      */   }
/*      */   
/*      */   private void fpush(int sp, int lz, int hz) {
/*  327 */     this.stack_ll[sp] = lz;
/*  328 */     this.stack_hh[sp] = hz;
/*      */   }
/*      */   
/*      */   private int[] fpop(int sp) {
/*  332 */     return new int[] { this.stack_ll[sp], this.stack_hh[sp] };
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
/*      */   private void fallbackQSort3(int[] fmap, int[] eclass, int loSt, int hiSt) {
/*  351 */     long r = 0L;
/*  352 */     int sp = 0;
/*  353 */     fpush(sp++, loSt, hiSt);
/*      */     
/*  355 */     while (sp > 0) {
/*  356 */       long med; int[] s = fpop(--sp);
/*  357 */       int lo = s[0];
/*  358 */       int hi = s[1];
/*      */       
/*  360 */       if (hi - lo < 10) {
/*  361 */         fallbackSimpleSort(fmap, eclass, lo, hi);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  372 */       r = (r * 7621L + 1L) % 32768L;
/*  373 */       long r3 = r % 3L;
/*  374 */       if (r3 == 0L) {
/*  375 */         med = eclass[fmap[lo]];
/*  376 */       } else if (r3 == 1L) {
/*  377 */         med = eclass[fmap[lo + hi >>> 1]];
/*      */       } else {
/*  379 */         med = eclass[fmap[hi]];
/*      */       } 
/*      */       
/*  382 */       int ltLo = lo, unLo = ltLo;
/*  383 */       int gtHi = hi, unHi = gtHi;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  389 */         if (unLo <= unHi) {
/*      */ 
/*      */           
/*  392 */           int i = eclass[fmap[unLo]] - (int)med;
/*  393 */           if (i == 0) {
/*  394 */             fswap(fmap, unLo, ltLo);
/*  395 */             ltLo++;
/*  396 */             unLo++;
/*      */             continue;
/*      */           } 
/*  399 */           if (i <= 0) {
/*      */ 
/*      */             
/*  402 */             unLo++; continue;
/*      */           } 
/*      */         } 
/*  405 */         while (unLo <= unHi) {
/*      */ 
/*      */           
/*  408 */           int i = eclass[fmap[unHi]] - (int)med;
/*  409 */           if (i == 0) {
/*  410 */             fswap(fmap, unHi, gtHi);
/*  411 */             gtHi--;
/*  412 */             unHi--;
/*      */             continue;
/*      */           } 
/*  415 */           if (i < 0) {
/*      */             break;
/*      */           }
/*  418 */           unHi--;
/*      */         } 
/*  420 */         if (unLo > unHi) {
/*      */           break;
/*      */         }
/*  423 */         fswap(fmap, unLo, unHi);
/*  424 */         unLo++;
/*  425 */         unHi--;
/*      */       } 
/*      */       
/*  428 */       if (gtHi < ltLo) {
/*      */         continue;
/*      */       }
/*      */       
/*  432 */       int n = fmin(ltLo - lo, unLo - ltLo);
/*  433 */       fvswap(fmap, lo, unLo - n, n);
/*  434 */       int m = fmin(hi - gtHi, gtHi - unHi);
/*  435 */       fvswap(fmap, unHi + 1, hi - m + 1, m);
/*      */       
/*  437 */       n = lo + unLo - ltLo - 1;
/*  438 */       m = hi - gtHi - unHi + 1;
/*      */       
/*  440 */       if (n - lo > hi - m) {
/*  441 */         fpush(sp++, lo, n);
/*  442 */         fpush(sp++, m, hi); continue;
/*      */       } 
/*  444 */       fpush(sp++, m, hi);
/*  445 */       fpush(sp++, lo, n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int[] getEclass() {
/*  456 */     if (this.eclass == null) {
/*  457 */       this.eclass = new int[this.quadrant.length / 2];
/*      */     }
/*  459 */     return this.eclass;
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
/*      */   final void fallbackSort(int[] fmap, byte[] block, int nblock) {
/*  480 */     int nNotDone, ftab[] = new int[257];
/*      */ 
/*      */ 
/*      */     
/*  484 */     int[] eclass = getEclass();
/*      */     int i;
/*  486 */     for (i = 0; i < nblock; i++) {
/*  487 */       eclass[i] = 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  493 */     for (i = 0; i < nblock; i++) {
/*  494 */       ftab[block[i] & 0xFF] = ftab[block[i] & 0xFF] + 1;
/*      */     }
/*  496 */     for (i = 1; i < 257; i++) {
/*  497 */       ftab[i] = ftab[i] + ftab[i - 1];
/*      */     }
/*      */     
/*  500 */     for (i = 0; i < nblock; i++) {
/*  501 */       int j = block[i] & 0xFF;
/*  502 */       int k = ftab[j] - 1;
/*  503 */       ftab[j] = k;
/*  504 */       fmap[k] = i;
/*      */     } 
/*      */     
/*  507 */     int nBhtab = 64 + nblock;
/*  508 */     BitSet bhtab = new BitSet(nBhtab);
/*  509 */     for (i = 0; i < 256; i++) {
/*  510 */       bhtab.set(ftab[i]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  520 */     for (i = 0; i < 32; i++) {
/*  521 */       bhtab.set(nblock + 2 * i);
/*  522 */       bhtab.clear(nblock + 2 * i + 1);
/*      */     } 
/*      */ 
/*      */     
/*  526 */     int H = 1;
/*      */     
/*      */     do {
/*  529 */       int j = 0;
/*  530 */       for (i = 0; i < nblock; i++) {
/*  531 */         if (bhtab.get(i)) {
/*  532 */           j = i;
/*      */         }
/*  534 */         int k = fmap[i] - H;
/*  535 */         if (k < 0) {
/*  536 */           k += nblock;
/*      */         }
/*  538 */         eclass[k] = j;
/*      */       } 
/*      */       
/*  541 */       nNotDone = 0;
/*  542 */       int r = -1;
/*      */ 
/*      */       
/*      */       while (true) {
/*  546 */         int k = r + 1;
/*  547 */         k = bhtab.nextClearBit(k);
/*  548 */         int l = k - 1;
/*  549 */         if (l >= nblock) {
/*      */           break;
/*      */         }
/*  552 */         k = bhtab.nextSetBit(k + 1);
/*  553 */         r = k - 1;
/*  554 */         if (r >= nblock) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/*  559 */         if (r > l) {
/*  560 */           nNotDone += r - l + 1;
/*  561 */           fallbackQSort3(fmap, eclass, l, r);
/*      */ 
/*      */           
/*  564 */           int cc = -1;
/*  565 */           for (i = l; i <= r; i++) {
/*  566 */             int cc1 = eclass[fmap[i]];
/*  567 */             if (cc != cc1) {
/*  568 */               bhtab.set(i);
/*  569 */               cc = cc1;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  575 */       H *= 2;
/*  576 */     } while (H <= nblock && nNotDone != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  586 */   private static final int[] INCS = new int[] { 1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524, 88573, 265720, 797161, 2391484 };
/*      */ 
/*      */   
/*      */   private static final int SMALL_THRESH = 20;
/*      */ 
/*      */   
/*      */   private static final int DEPTH_THRESH = 10;
/*      */ 
/*      */   
/*      */   private static final int WORK_FACTOR = 30;
/*      */   
/*      */   private static final int SETMASK = 2097152;
/*      */   
/*      */   private static final int CLEARMASK = -2097153;
/*      */ 
/*      */   
/*      */   private boolean mainSimpleSort(CBZip2OutputStream.Data dataShadow, int lo, int hi, int d, int lastShadow) {
/*  603 */     int bigN = hi - lo + 1;
/*  604 */     if (bigN < 2) {
/*  605 */       return (this.firstAttempt && this.workDone > this.workLimit);
/*      */     }
/*      */     
/*  608 */     int hp = 0;
/*  609 */     while (INCS[hp] < bigN) {
/*  610 */       hp++;
/*      */     }
/*      */     
/*  613 */     int[] fmap = dataShadow.fmap;
/*  614 */     char[] quadrant = this.quadrant;
/*  615 */     byte[] block = dataShadow.block;
/*  616 */     int lastPlus1 = lastShadow + 1;
/*  617 */     boolean firstAttemptShadow = this.firstAttempt;
/*  618 */     int workLimitShadow = this.workLimit;
/*  619 */     int workDoneShadow = this.workDone;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  624 */     label98: while (--hp >= 0) {
/*  625 */       int h = INCS[hp];
/*  626 */       int mj = lo + h - 1;
/*      */       
/*  628 */       for (int i = lo + h; i <= hi; ) {
/*      */         
/*  630 */         for (int k = 3; i <= hi && --k >= 0; i++) {
/*  631 */           int v = fmap[i];
/*  632 */           int vd = v + d;
/*  633 */           int j = i;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  645 */           boolean onceRun = false;
/*  646 */           int a = 0;
/*      */           
/*      */           while (true) {
/*  649 */             if (onceRun) {
/*  650 */               fmap[j] = a;
/*  651 */               if ((j -= h) <= mj) {
/*      */                 break;
/*      */               }
/*      */             } else {
/*  655 */               onceRun = true;
/*      */             } 
/*      */             
/*  658 */             a = fmap[j - h];
/*  659 */             int i1 = a + d;
/*  660 */             int i2 = vd;
/*      */ 
/*      */ 
/*      */             
/*  664 */             if (block[i1 + 1] == block[i2 + 1]) {
/*  665 */               if (block[i1 + 2] == block[i2 + 2]) {
/*  666 */                 if (block[i1 + 3] == block[i2 + 3]) {
/*  667 */                   if (block[i1 + 4] == block[i2 + 4]) {
/*  668 */                     if (block[i1 + 5] == block[i2 + 5]) {
/*  669 */                       i1 += 6; i2 += 6; if (block[i1] == block[i2]) {
/*  670 */                         int x = lastShadow;
/*  671 */                         while (x > 0) {
/*  672 */                           x -= 4;
/*      */                           
/*  674 */                           if (block[i1 + 1] == block[i2 + 1]) {
/*  675 */                             if (quadrant[i1] == quadrant[i2]) {
/*  676 */                               if (block[i1 + 2] == block[i2 + 2]) {
/*  677 */                                 if (quadrant[i1 + 1] == quadrant[i2 + 1]) {
/*  678 */                                   if (block[i1 + 3] == block[i2 + 3]) {
/*  679 */                                     if (quadrant[i1 + 2] == quadrant[i2 + 2]) {
/*  680 */                                       if (block[i1 + 4] == block[i2 + 4]) {
/*  681 */                                         if (quadrant[i1 + 3] == quadrant[i2 + 3]) {
/*  682 */                                           i1 += 4; if (i1 >= lastPlus1) {
/*  683 */                                             i1 -= lastPlus1;
/*      */                                           }
/*  685 */                                           i2 += 4; if (i2 >= lastPlus1) {
/*  686 */                                             i2 -= lastPlus1;
/*      */                                           }
/*  688 */                                           workDoneShadow++; continue;
/*  689 */                                         }  if (quadrant[i1 + 3] > quadrant[i2 + 3]) {
/*      */                                           continue;
/*      */                                         }
/*      */                                         break;
/*      */                                       } 
/*  694 */                                       if ((block[i1 + 4] & 0xFF) > (block[i2 + 4] & 0xFF)) {
/*      */                                         continue;
/*      */                                       }
/*      */                                       break;
/*      */                                     } 
/*  699 */                                     if (quadrant[i1 + 2] > quadrant[i2 + 2]) {
/*      */                                       continue;
/*      */                                     }
/*      */                                     break;
/*      */                                   } 
/*  704 */                                   if ((block[i1 + 3] & 0xFF) > (block[i2 + 3] & 0xFF)) {
/*      */                                     continue;
/*      */                                   }
/*      */                                   break;
/*      */                                 } 
/*  709 */                                 if (quadrant[i1 + 1] > quadrant[i2 + 1]) {
/*      */                                   continue;
/*      */                                 }
/*      */                                 break;
/*      */                               } 
/*  714 */                               if ((block[i1 + 2] & 0xFF) > (block[i2 + 2] & 0xFF)) {
/*      */                                 continue;
/*      */                               }
/*      */                               break;
/*      */                             } 
/*  719 */                             if (quadrant[i1] > quadrant[i2]) {
/*      */                               continue;
/*      */                             }
/*      */                             break;
/*      */                           } 
/*  724 */                           if ((block[i1 + 1] & 0xFF) > (block[i2 + 1] & 0xFF));
/*      */                         } 
/*      */ 
/*      */ 
/*      */                         
/*      */                         break;
/*      */                       } 
/*      */ 
/*      */                       
/*  733 */                       if ((block[i1] & 0xFF) <= (block[i2] & 0xFF))
/*      */                         break;  continue;
/*      */                     } 
/*  736 */                     if ((block[i1 + 5] & 0xFF) <= (block[i2 + 5] & 0xFF))
/*      */                       break;  continue;
/*      */                   } 
/*  739 */                   if ((block[i1 + 4] & 0xFF) <= (block[i2 + 4] & 0xFF))
/*      */                     break;  continue;
/*      */                 } 
/*  742 */                 if ((block[i1 + 3] & 0xFF) <= (block[i2 + 3] & 0xFF))
/*      */                   break;  continue;
/*      */               } 
/*  745 */               if ((block[i1 + 2] & 0xFF) <= (block[i2 + 2] & 0xFF))
/*      */                 break;  continue;
/*      */             } 
/*  748 */             if ((block[i1 + 1] & 0xFF) <= (block[i2 + 1] & 0xFF)) {
/*      */               break;
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  755 */           fmap[j] = v;
/*      */         } 
/*      */         
/*  758 */         if (firstAttemptShadow && i <= hi && workDoneShadow > workLimitShadow) {
/*      */           break label98;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  765 */     this.workDone = workDoneShadow;
/*  766 */     return (firstAttemptShadow && workDoneShadow > workLimitShadow);
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
/*      */   private static void vswap(int[] fmap, int p1, int p2, int n) {
/*  778 */     n += p1;
/*  779 */     while (p1 < n) {
/*  780 */       int t = fmap[p1];
/*  781 */       fmap[p1++] = fmap[p2];
/*  782 */       fmap[p2++] = t;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static byte med3(byte a, byte b, byte c) {
/*  787 */     return (a < b) ? ((b < c) ? b : ((a < c) ? c : a)) : ((b > c) ? b : ((a > c) ? c : 
/*  788 */       a));
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
/*      */   private void mainQSort3(CBZip2OutputStream.Data dataShadow, int loSt, int hiSt, int dSt, int last) {
/*  801 */     int[] stack_ll = this.stack_ll;
/*  802 */     int[] stack_hh = this.stack_hh;
/*  803 */     int[] stack_dd = this.stack_dd;
/*  804 */     int[] fmap = dataShadow.fmap;
/*  805 */     byte[] block = dataShadow.block;
/*      */     
/*  807 */     stack_ll[0] = loSt;
/*  808 */     stack_hh[0] = hiSt;
/*  809 */     stack_dd[0] = dSt;
/*      */     
/*  811 */     for (int sp = 1; --sp >= 0; ) {
/*  812 */       int lo = stack_ll[sp];
/*  813 */       int hi = stack_hh[sp];
/*  814 */       int d = stack_dd[sp];
/*      */       
/*  816 */       if (hi - lo < 20 || d > 10) {
/*  817 */         if (mainSimpleSort(dataShadow, lo, hi, d, last))
/*      */           return; 
/*      */         continue;
/*      */       } 
/*  821 */       int d1 = d + 1;
/*  822 */       int med = med3(block[fmap[lo] + d1], block[fmap[hi] + d1], block[fmap[lo + hi >>> 1] + d1]) & 0xFF;
/*      */ 
/*      */       
/*  825 */       int unLo = lo;
/*  826 */       int unHi = hi;
/*  827 */       int ltLo = lo;
/*  828 */       int gtHi = hi;
/*      */       
/*      */       while (true) {
/*  831 */         if (unLo <= unHi) {
/*  832 */           int i = (block[fmap[unLo] + d1] & 0xFF) - med;
/*      */           
/*  834 */           if (i == 0) {
/*  835 */             int temp = fmap[unLo];
/*  836 */             fmap[unLo++] = fmap[ltLo];
/*  837 */             fmap[ltLo++] = temp; continue;
/*  838 */           }  if (i < 0) {
/*  839 */             unLo++;
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */         
/*  845 */         while (unLo <= unHi) {
/*  846 */           int i = (block[fmap[unHi] + d1] & 0xFF) - med;
/*      */           
/*  848 */           if (i == 0) {
/*  849 */             int temp = fmap[unHi];
/*  850 */             fmap[unHi--] = fmap[gtHi];
/*  851 */             fmap[gtHi--] = temp; continue;
/*  852 */           }  if (i > 0) {
/*  853 */             unHi--;
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  859 */         if (unLo <= unHi) {
/*  860 */           int temp = fmap[unLo];
/*  861 */           fmap[unLo++] = fmap[unHi];
/*  862 */           fmap[unHi--] = temp;
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  868 */       if (gtHi < ltLo) {
/*  869 */         stack_ll[sp] = lo;
/*  870 */         stack_hh[sp] = hi;
/*  871 */         stack_dd[sp] = d1;
/*  872 */         sp++;
/*      */         continue;
/*      */       } 
/*  875 */       int n = (ltLo - lo < unLo - ltLo) ? (ltLo - lo) : (unLo - ltLo);
/*  876 */       vswap(fmap, lo, unLo - n, n);
/*      */       
/*  878 */       int m = (hi - gtHi < gtHi - unHi) ? (hi - gtHi) : (gtHi - unHi);
/*  879 */       vswap(fmap, unLo, hi - m + 1, m);
/*      */       
/*  881 */       n = lo + unLo - ltLo - 1;
/*  882 */       m = hi - gtHi - unHi + 1;
/*      */       
/*  884 */       stack_ll[sp] = lo;
/*  885 */       stack_hh[sp] = n;
/*  886 */       stack_dd[sp] = d;
/*  887 */       sp++;
/*      */       
/*  889 */       stack_ll[sp] = n + 1;
/*  890 */       stack_hh[sp] = m - 1;
/*  891 */       stack_dd[sp] = d1;
/*  892 */       sp++;
/*      */       
/*  894 */       stack_ll[sp] = m;
/*  895 */       stack_hh[sp] = hi;
/*  896 */       stack_dd[sp] = d;
/*  897 */       sp++;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void mainSort(CBZip2OutputStream.Data dataShadow, int lastShadow) {
/*  908 */     int[] runningOrder = this.mainSort_runningOrder;
/*  909 */     int[] copy = this.mainSort_copy;
/*  910 */     boolean[] bigDone = this.mainSort_bigDone;
/*  911 */     int[] ftab = this.ftab;
/*  912 */     byte[] block = dataShadow.block;
/*  913 */     int[] fmap = dataShadow.fmap;
/*  914 */     char[] quadrant = this.quadrant;
/*  915 */     int workLimitShadow = this.workLimit;
/*  916 */     boolean firstAttemptShadow = this.firstAttempt;
/*      */     
/*      */     int i;
/*  919 */     for (i = 65537; --i >= 0;) {
/*  920 */       ftab[i] = 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  928 */     for (i = 0; i < 20; i++) {
/*  929 */       block[lastShadow + i + 2] = block[i % (lastShadow + 1) + 1];
/*      */     }
/*  931 */     for (i = lastShadow + 20 + 1; --i >= 0;) {
/*  932 */       quadrant[i] = Character.MIN_VALUE;
/*      */     }
/*  934 */     block[0] = block[lastShadow + 1];
/*      */ 
/*      */ 
/*      */     
/*  938 */     int c1 = block[0] & 0xFF; int k;
/*  939 */     for (k = 0; k <= lastShadow; k++) {
/*  940 */       int c2 = block[k + 1] & 0xFF;
/*  941 */       ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] + 1;
/*  942 */       c1 = c2;
/*      */     } 
/*      */     
/*  945 */     for (k = 1; k <= 65536; k++) {
/*  946 */       ftab[k] = ftab[k] + ftab[k - 1];
/*      */     }
/*      */     
/*  949 */     c1 = block[1] & 0xFF;
/*  950 */     for (k = 0; k < lastShadow; k++) {
/*  951 */       int c2 = block[k + 2] & 0xFF;
/*  952 */       ftab[(c1 << 8) + c2] = ftab[(c1 << 8) + c2] - 1; fmap[ftab[(c1 << 8) + c2] - 1] = k;
/*  953 */       c1 = c2;
/*      */     } 
/*      */     
/*  956 */     ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] = ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1; fmap[ftab[((block[lastShadow + 1] & 0xFF) << 8) + (block[1] & 0xFF)] - 1] = lastShadow;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  962 */     for (k = 256; --k >= 0; ) {
/*  963 */       bigDone[k] = false;
/*  964 */       runningOrder[k] = k;
/*      */     } 
/*      */     
/*  967 */     for (int h = 364; h != 1; ) {
/*  968 */       h /= 3;
/*  969 */       for (int m = h; m <= 255; m++) {
/*  970 */         int vv = runningOrder[m];
/*  971 */         int a = ftab[vv + 1 << 8] - ftab[vv << 8];
/*  972 */         int b = h - 1;
/*  973 */         int n = m; int ro;
/*  974 */         for (ro = runningOrder[n - h]; ftab[ro + 1 << 8] - ftab[ro << 8] > a; ro = runningOrder[n - h]) {
/*      */           
/*  976 */           runningOrder[n] = ro;
/*  977 */           n -= h;
/*  978 */           if (n <= b) {
/*      */             break;
/*      */           }
/*      */         } 
/*  982 */         runningOrder[n] = vv;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  989 */     for (int j = 0; j <= 255; j++) {
/*      */ 
/*      */ 
/*      */       
/*  993 */       int ss = runningOrder[j];
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       int m;
/*      */ 
/*      */ 
/*      */       
/* 1002 */       for (m = 0; m <= 255; m++) {
/* 1003 */         int sb = (ss << 8) + m;
/* 1004 */         int ftab_sb = ftab[sb];
/* 1005 */         if ((ftab_sb & 0x200000) != 2097152) {
/* 1006 */           int lo = ftab_sb & 0xFFDFFFFF;
/* 1007 */           int hi = (ftab[sb + 1] & 0xFFDFFFFF) - 1;
/* 1008 */           if (hi > lo) {
/* 1009 */             mainQSort3(dataShadow, lo, hi, 2, lastShadow);
/* 1010 */             if (firstAttemptShadow && this.workDone > workLimitShadow) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */           
/* 1015 */           ftab[sb] = ftab_sb | 0x200000;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1023 */       for (m = 0; m <= 255; m++) {
/* 1024 */         copy[m] = ftab[(m << 8) + ss] & 0xFFDFFFFF;
/*      */       }
/*      */       int hj;
/* 1027 */       for (m = ftab[ss << 8] & 0xFFDFFFFF, hj = ftab[ss + 1 << 8] & 0xFFDFFFFF; m < hj; m++) {
/* 1028 */         int fmap_j = fmap[m];
/* 1029 */         c1 = block[fmap_j] & 0xFF;
/* 1030 */         if (!bigDone[c1]) {
/* 1031 */           fmap[copy[c1]] = (fmap_j == 0) ? lastShadow : (fmap_j - 1);
/* 1032 */           copy[c1] = copy[c1] + 1;
/*      */         } 
/*      */       } 
/*      */       
/* 1036 */       for (m = 256; --m >= 0;) {
/* 1037 */         ftab[(m << 8) + ss] = ftab[(m << 8) + ss] | 0x200000;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1048 */       bigDone[ss] = true;
/*      */       
/* 1050 */       if (j < 255) {
/* 1051 */         int bbStart = ftab[ss << 8] & 0xFFDFFFFF;
/* 1052 */         int bbSize = (ftab[ss + 1 << 8] & 0xFFDFFFFF) - bbStart;
/* 1053 */         int shifts = 0;
/*      */         
/* 1055 */         while (bbSize >> shifts > 65534) {
/* 1056 */           shifts++;
/*      */         }
/*      */         
/* 1059 */         for (int n = 0; n < bbSize; n++) {
/* 1060 */           int a2update = fmap[bbStart + n];
/* 1061 */           char qVal = (char)(n >> shifts);
/* 1062 */           quadrant[a2update] = qVal;
/* 1063 */           if (a2update < 20)
/* 1064 */             quadrant[a2update + lastShadow + 1] = qVal; 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/bzip2/BlockSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */