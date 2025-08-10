/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class BidiOrder
/*      */ {
/*      */   private byte[] initialTypes;
/*      */   private byte[] embeddings;
/*  158 */   private byte paragraphEmbeddingLevel = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   private int textLength;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] resultTypes;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] resultLevels;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte L = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final byte LRE = 1;
/*      */ 
/*      */   
/*      */   public static final byte LRO = 2;
/*      */ 
/*      */   
/*      */   public static final byte R = 3;
/*      */ 
/*      */   
/*      */   public static final byte AL = 4;
/*      */ 
/*      */   
/*      */   public static final byte RLE = 5;
/*      */ 
/*      */   
/*      */   public static final byte RLO = 6;
/*      */ 
/*      */   
/*      */   public static final byte PDF = 7;
/*      */ 
/*      */   
/*      */   public static final byte EN = 8;
/*      */ 
/*      */   
/*      */   public static final byte ES = 9;
/*      */ 
/*      */   
/*      */   public static final byte ET = 10;
/*      */ 
/*      */   
/*      */   public static final byte AN = 11;
/*      */ 
/*      */   
/*      */   public static final byte CS = 12;
/*      */ 
/*      */   
/*      */   public static final byte NSM = 13;
/*      */ 
/*      */   
/*      */   public static final byte BN = 14;
/*      */ 
/*      */   
/*      */   public static final byte B = 15;
/*      */ 
/*      */   
/*      */   public static final byte S = 16;
/*      */ 
/*      */   
/*      */   public static final byte WS = 17;
/*      */ 
/*      */   
/*      */   public static final byte ON = 18;
/*      */ 
/*      */   
/*      */   public static final byte TYPE_MIN = 0;
/*      */ 
/*      */   
/*      */   public static final byte TYPE_MAX = 18;
/*      */ 
/*      */ 
/*      */   
/*      */   public BidiOrder(byte[] types) {
/*  240 */     validateTypes(types);
/*      */     
/*  242 */     this.initialTypes = (byte[])types.clone();
/*      */     
/*  244 */     runAlgorithm();
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
/*      */   public BidiOrder(byte[] types, byte paragraphEmbeddingLevel) {
/*  256 */     validateTypes(types);
/*  257 */     validateParagraphEmbeddingLevel(paragraphEmbeddingLevel);
/*      */     
/*  259 */     this.initialTypes = (byte[])types.clone();
/*  260 */     this.paragraphEmbeddingLevel = paragraphEmbeddingLevel;
/*      */     
/*  262 */     runAlgorithm();
/*      */   }
/*      */   
/*      */   public BidiOrder(char[] text, int offset, int length, byte paragraphEmbeddingLevel) {
/*  266 */     this.initialTypes = new byte[length];
/*  267 */     for (int k = 0; k < length; k++) {
/*  268 */       this.initialTypes[k] = rtypes[text[offset + k]];
/*      */     }
/*  270 */     validateParagraphEmbeddingLevel(paragraphEmbeddingLevel);
/*      */     
/*  272 */     this.paragraphEmbeddingLevel = paragraphEmbeddingLevel;
/*      */     
/*  274 */     runAlgorithm();
/*      */   }
/*      */   
/*      */   public static final byte getDirection(char c) {
/*  278 */     return rtypes[c];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void runAlgorithm() {
/*  287 */     this.textLength = this.initialTypes.length;
/*      */ 
/*      */ 
/*      */     
/*  291 */     this.resultTypes = (byte[])this.initialTypes.clone();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  298 */     if (this.paragraphEmbeddingLevel == -1) {
/*  299 */       determineParagraphEmbeddingLevel();
/*      */     }
/*      */ 
/*      */     
/*  303 */     this.resultLevels = new byte[this.textLength];
/*  304 */     setLevels(0, this.textLength, this.paragraphEmbeddingLevel);
/*      */ 
/*      */ 
/*      */     
/*  308 */     determineExplicitEmbeddingLevels();
/*      */ 
/*      */     
/*  311 */     this.textLength = removeExplicitCodes();
/*      */ 
/*      */ 
/*      */     
/*  315 */     byte prevLevel = this.paragraphEmbeddingLevel;
/*  316 */     int start = 0;
/*  317 */     while (start < this.textLength) {
/*  318 */       byte level = this.resultLevels[start];
/*  319 */       byte prevType = typeForLevel(Math.max(prevLevel, level));
/*      */       
/*  321 */       int limit = start + 1;
/*  322 */       while (limit < this.textLength && this.resultLevels[limit] == level) {
/*  323 */         limit++;
/*      */       }
/*      */       
/*  326 */       byte succLevel = (limit < this.textLength) ? this.resultLevels[limit] : this.paragraphEmbeddingLevel;
/*  327 */       byte succType = typeForLevel(Math.max(succLevel, level));
/*      */ 
/*      */ 
/*      */       
/*  331 */       resolveWeakTypes(start, limit, level, prevType, succType);
/*      */ 
/*      */ 
/*      */       
/*  335 */       resolveNeutralTypes(start, limit, level, prevType, succType);
/*      */ 
/*      */ 
/*      */       
/*  339 */       resolveImplicitLevels(start, limit, level, prevType, succType);
/*      */       
/*  341 */       prevLevel = level;
/*  342 */       start = limit;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  350 */     this.textLength = reinsertExplicitCodes(this.textLength);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void determineParagraphEmbeddingLevel() {
/*  361 */     byte strongType = -1;
/*      */ 
/*      */     
/*  364 */     for (int i = 0; i < this.textLength; i++) {
/*  365 */       byte t = this.resultTypes[i];
/*  366 */       if (t == 0 || t == 4 || t == 3) {
/*  367 */         strongType = t;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  373 */     if (strongType == -1) {
/*      */       
/*  375 */       this.paragraphEmbeddingLevel = 0;
/*  376 */     } else if (strongType == 0) {
/*  377 */       this.paragraphEmbeddingLevel = 0;
/*      */     } else {
/*  379 */       this.paragraphEmbeddingLevel = 1;
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
/*      */   private void determineExplicitEmbeddingLevels() {
/*  392 */     this.embeddings = processEmbeddings(this.resultTypes, this.paragraphEmbeddingLevel);
/*      */     
/*  394 */     for (int i = 0; i < this.textLength; i++) {
/*  395 */       byte level = this.embeddings[i];
/*  396 */       if ((level & 0x80) != 0) {
/*  397 */         level = (byte)(level & Byte.MAX_VALUE);
/*  398 */         this.resultTypes[i] = typeForLevel(level);
/*      */       } 
/*  400 */       this.resultLevels[i] = level;
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
/*      */   private int removeExplicitCodes() {
/*  412 */     int w = 0;
/*  413 */     for (int i = 0; i < this.textLength; i++) {
/*  414 */       byte t = this.initialTypes[i];
/*  415 */       if (t != 1 && t != 5 && t != 2 && t != 6 && t != 7 && t != 14) {
/*  416 */         this.embeddings[w] = this.embeddings[i];
/*  417 */         this.resultTypes[w] = this.resultTypes[i];
/*  418 */         this.resultLevels[w] = this.resultLevels[i];
/*  419 */         w++;
/*      */       } 
/*      */     } 
/*  422 */     return w;
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
/*      */   private int reinsertExplicitCodes(int textLength) {
/*      */     int i;
/*  436 */     for (i = this.initialTypes.length; --i >= 0; ) {
/*  437 */       byte t = this.initialTypes[i];
/*  438 */       if (t == 1 || t == 5 || t == 2 || t == 6 || t == 7 || t == 14) {
/*  439 */         this.embeddings[i] = 0;
/*  440 */         this.resultTypes[i] = t;
/*  441 */         this.resultLevels[i] = -1; continue;
/*      */       } 
/*  443 */       textLength--;
/*  444 */       this.embeddings[i] = this.embeddings[textLength];
/*  445 */       this.resultTypes[i] = this.resultTypes[textLength];
/*  446 */       this.resultLevels[i] = this.resultLevels[textLength];
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  454 */     if (this.resultLevels[0] == -1) {
/*  455 */       this.resultLevels[0] = this.paragraphEmbeddingLevel;
/*      */     }
/*  457 */     for (i = 1; i < this.initialTypes.length; i++) {
/*  458 */       if (this.resultLevels[i] == -1) {
/*  459 */         this.resultLevels[i] = this.resultLevels[i - 1];
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  466 */     return this.initialTypes.length;
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
/*      */   private static byte[] processEmbeddings(byte[] resultTypes, byte paragraphEmbeddingLevel) {
/*  479 */     int EXPLICIT_LEVEL_LIMIT = 62;
/*      */     
/*  481 */     int textLength = resultTypes.length;
/*  482 */     byte[] embeddings = new byte[textLength];
/*      */ 
/*      */ 
/*      */     
/*  486 */     byte[] embeddingValueStack = new byte[62];
/*  487 */     int stackCounter = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  494 */     int overflowAlmostCounter = 0;
/*      */ 
/*      */ 
/*      */     
/*  498 */     int overflowCounter = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  503 */     byte currentEmbeddingLevel = paragraphEmbeddingLevel;
/*  504 */     byte currentEmbeddingValue = paragraphEmbeddingLevel;
/*      */ 
/*      */     
/*  507 */     for (int i = 0; i < textLength; i++) {
/*      */       
/*  509 */       embeddings[i] = currentEmbeddingValue;
/*      */       
/*  511 */       byte t = resultTypes[i];
/*      */ 
/*      */       
/*  514 */       switch (t) {
/*      */         
/*      */         case 1:
/*      */         case 2:
/*      */         case 5:
/*      */         case 6:
/*  520 */           if (overflowCounter == 0) {
/*      */             byte newLevel;
/*  522 */             if (t == 5 || t == 6) {
/*  523 */               newLevel = (byte)(currentEmbeddingLevel + 1 | 0x1);
/*      */             } else {
/*  525 */               newLevel = (byte)(currentEmbeddingLevel + 2 & 0xFFFFFFFE);
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  530 */             if (newLevel < 62) {
/*  531 */               embeddingValueStack[stackCounter] = currentEmbeddingValue;
/*  532 */               stackCounter++;
/*      */               
/*  534 */               currentEmbeddingLevel = newLevel;
/*  535 */               if (t == 2 || t == 6) {
/*  536 */                 currentEmbeddingValue = (byte)(newLevel | 0x80);
/*      */               } else {
/*  538 */                 currentEmbeddingValue = newLevel;
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  543 */               embeddings[i] = currentEmbeddingValue;
/*      */ 
/*      */               
/*      */               break;
/*      */             } 
/*      */ 
/*      */             
/*  550 */             if (currentEmbeddingLevel == 60) {
/*  551 */               overflowAlmostCounter++;
/*      */               
/*      */               break;
/*      */             } 
/*      */           } 
/*      */           
/*  557 */           overflowCounter++;
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 7:
/*  567 */           if (overflowCounter > 0) {
/*  568 */             overflowCounter--; break;
/*  569 */           }  if (overflowAlmostCounter > 0 && currentEmbeddingLevel != 61) {
/*  570 */             overflowAlmostCounter--; break;
/*  571 */           }  if (stackCounter > 0) {
/*  572 */             stackCounter--;
/*  573 */             currentEmbeddingValue = embeddingValueStack[stackCounter];
/*  574 */             currentEmbeddingLevel = (byte)(currentEmbeddingValue & Byte.MAX_VALUE);
/*      */           } 
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 15:
/*  583 */           stackCounter = 0;
/*  584 */           overflowCounter = 0;
/*  585 */           overflowAlmostCounter = 0;
/*  586 */           currentEmbeddingLevel = paragraphEmbeddingLevel;
/*  587 */           currentEmbeddingValue = paragraphEmbeddingLevel;
/*      */           
/*  589 */           embeddings[i] = paragraphEmbeddingLevel;
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/*  597 */     return embeddings;
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
/*      */   private void resolveWeakTypes(int start, int limit, byte level, byte sor, byte eor) {
/*  611 */     byte preceedingCharacterType = sor; int i;
/*  612 */     for (i = start; i < limit; i++) {
/*  613 */       byte t = this.resultTypes[i];
/*  614 */       if (t == 13) {
/*  615 */         this.resultTypes[i] = preceedingCharacterType;
/*      */       } else {
/*  617 */         preceedingCharacterType = t;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  623 */     for (i = start; i < limit; i++) {
/*  624 */       if (this.resultTypes[i] == 8) {
/*  625 */         for (int j = i - 1; j >= start; j--) {
/*  626 */           byte t = this.resultTypes[j];
/*  627 */           if (t == 0 || t == 3 || t == 4) {
/*  628 */             if (t == 4) {
/*  629 */               this.resultTypes[i] = 11;
/*      */             }
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  638 */     for (i = start; i < limit; i++) {
/*  639 */       if (this.resultTypes[i] == 4) {
/*  640 */         this.resultTypes[i] = 3;
/*      */       }
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  656 */     for (i = start + 1; i < limit - 1; i++) {
/*  657 */       if (this.resultTypes[i] == 9 || this.resultTypes[i] == 12) {
/*  658 */         byte prevSepType = this.resultTypes[i - 1];
/*  659 */         byte succSepType = this.resultTypes[i + 1];
/*  660 */         if (prevSepType == 8 && succSepType == 8) {
/*  661 */           this.resultTypes[i] = 8;
/*  662 */         } else if (this.resultTypes[i] == 12 && prevSepType == 11 && succSepType == 11) {
/*  663 */           this.resultTypes[i] = 11;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  669 */     for (i = start; i < limit; i++) {
/*  670 */       if (this.resultTypes[i] == 10) {
/*      */         
/*  672 */         int runstart = i;
/*  673 */         int runlimit = findRunLimit(runstart, limit, new byte[] { 10 });
/*      */ 
/*      */         
/*  676 */         byte t = (runstart == start) ? sor : this.resultTypes[runstart - 1];
/*      */         
/*  678 */         if (t != 8) {
/*  679 */           t = (runlimit == limit) ? eor : this.resultTypes[runlimit];
/*      */         }
/*      */         
/*  682 */         if (t == 8) {
/*  683 */           setTypes(runstart, runlimit, (byte)8);
/*      */         }
/*      */ 
/*      */         
/*  687 */         i = runlimit;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  692 */     for (i = start; i < limit; i++) {
/*  693 */       byte t = this.resultTypes[i];
/*  694 */       if (t == 9 || t == 10 || t == 12) {
/*  695 */         this.resultTypes[i] = 18;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  700 */     for (i = start; i < limit; i++) {
/*  701 */       if (this.resultTypes[i] == 8) {
/*      */         
/*  703 */         byte prevStrongType = sor;
/*  704 */         for (int j = i - 1; j >= start; j--) {
/*  705 */           byte t = this.resultTypes[j];
/*  706 */           if (t == 0 || t == 3) {
/*  707 */             prevStrongType = t;
/*      */             break;
/*      */           } 
/*      */         } 
/*  711 */         if (prevStrongType == 0) {
/*  712 */           this.resultTypes[i] = 0;
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
/*      */   private void resolveNeutralTypes(int start, int limit, byte level, byte sor, byte eor) {
/*  724 */     for (int i = start; i < limit; i++) {
/*  725 */       byte t = this.resultTypes[i];
/*  726 */       if (t == 17 || t == 18 || t == 15 || t == 16) {
/*      */         byte leadingType, trailingType, resolvedType;
/*  728 */         int runstart = i;
/*  729 */         int runlimit = findRunLimit(runstart, limit, new byte[] { 15, 16, 17, 18 });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  735 */         if (runstart == start) {
/*  736 */           leadingType = sor;
/*      */         } else {
/*  738 */           leadingType = this.resultTypes[runstart - 1];
/*  739 */           if (leadingType != 0 && leadingType != 3)
/*      */           {
/*  741 */             if (leadingType == 11) {
/*  742 */               leadingType = 3;
/*  743 */             } else if (leadingType == 8) {
/*      */ 
/*      */               
/*  746 */               leadingType = 3;
/*      */             } 
/*      */           }
/*      */         } 
/*  750 */         if (runlimit == limit) {
/*  751 */           trailingType = eor;
/*      */         } else {
/*  753 */           trailingType = this.resultTypes[runlimit];
/*  754 */           if (trailingType != 0 && trailingType != 3)
/*      */           {
/*  756 */             if (trailingType == 11) {
/*  757 */               trailingType = 3;
/*  758 */             } else if (trailingType == 8) {
/*  759 */               trailingType = 3;
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*  764 */         if (leadingType == trailingType) {
/*      */           
/*  766 */           resolvedType = leadingType;
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  771 */           resolvedType = typeForLevel(level);
/*      */         } 
/*      */         
/*  774 */         setTypes(runstart, runlimit, resolvedType);
/*      */ 
/*      */         
/*  777 */         i = runlimit;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void resolveImplicitLevels(int start, int limit, byte level, byte sor, byte eor) {
/*  787 */     if ((level & 0x1) == 0) {
/*  788 */       for (int i = start; i < limit; i++) {
/*  789 */         byte t = this.resultTypes[i];
/*      */         
/*  791 */         if (t != 0)
/*      */         {
/*  793 */           if (t == 3) {
/*  794 */             this.resultLevels[i] = (byte)(this.resultLevels[i] + 1);
/*      */           } else {
/*  796 */             this.resultLevels[i] = (byte)(this.resultLevels[i] + 2);
/*      */           }  } 
/*      */       } 
/*      */     } else {
/*  800 */       for (int i = start; i < limit; i++) {
/*  801 */         byte t = this.resultTypes[i];
/*      */         
/*  803 */         if (t != 3)
/*      */         {
/*      */           
/*  806 */           this.resultLevels[i] = (byte)(this.resultLevels[i] + 1);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getLevels() {
/*  817 */     return getLevels(new int[] { this.textLength });
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
/*      */   public byte[] getLevels(int[] linebreaks) {
/*  848 */     validateLineBreaks(linebreaks, this.textLength);
/*      */     
/*  850 */     byte[] result = (byte[])this.resultLevels.clone();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  855 */     for (int i = 0; i < result.length; i++) {
/*  856 */       byte t = this.initialTypes[i];
/*  857 */       if (t == 15 || t == 16) {
/*      */         
/*  859 */         result[i] = this.paragraphEmbeddingLevel;
/*      */ 
/*      */         
/*  862 */         for (int k = i - 1; k >= 0 && 
/*  863 */           isWhitespace(this.initialTypes[k]); k--) {
/*  864 */           result[k] = this.paragraphEmbeddingLevel;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  873 */     int start = 0;
/*  874 */     for (int j = 0; j < linebreaks.length; j++) {
/*  875 */       int limit = linebreaks[j];
/*  876 */       for (int k = limit - 1; k >= start && 
/*  877 */         isWhitespace(this.initialTypes[k]); k--) {
/*  878 */         result[k] = this.paragraphEmbeddingLevel;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  884 */       start = limit;
/*      */     } 
/*      */     
/*  887 */     return result;
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
/*      */   public int[] getReordering(int[] linebreaks) {
/*  909 */     validateLineBreaks(linebreaks, this.textLength);
/*      */     
/*  911 */     byte[] levels = getLevels(linebreaks);
/*      */     
/*  913 */     return computeMultilineReordering(levels, linebreaks);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] computeMultilineReordering(byte[] levels, int[] linebreaks) {
/*  921 */     int[] result = new int[levels.length];
/*      */     
/*  923 */     int start = 0;
/*  924 */     for (int i = 0; i < linebreaks.length; i++) {
/*  925 */       int limit = linebreaks[i];
/*      */       
/*  927 */       byte[] templevels = new byte[limit - start];
/*  928 */       System.arraycopy(levels, start, templevels, 0, templevels.length);
/*      */       
/*  930 */       int[] temporder = computeReordering(templevels);
/*  931 */       for (int j = 0; j < temporder.length; j++) {
/*  932 */         result[start + j] = temporder[j] + start;
/*      */       }
/*      */       
/*  935 */       start = limit;
/*      */     } 
/*      */     
/*  938 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int[] computeReordering(byte[] levels) {
/*  948 */     int lineLength = levels.length;
/*      */     
/*  950 */     int[] result = new int[lineLength];
/*      */ 
/*      */     
/*  953 */     for (int i = 0; i < lineLength; i++) {
/*  954 */       result[i] = i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  960 */     byte highestLevel = 0;
/*  961 */     byte lowestOddLevel = 63;
/*  962 */     for (int j = 0; j < lineLength; j++) {
/*  963 */       byte b = levels[j];
/*  964 */       if (b > highestLevel) {
/*  965 */         highestLevel = b;
/*      */       }
/*  967 */       if ((b & 0x1) != 0 && b < lowestOddLevel) {
/*  968 */         lowestOddLevel = b;
/*      */       }
/*      */     } 
/*      */     
/*  972 */     for (int level = highestLevel; level >= lowestOddLevel; level--) {
/*  973 */       for (int k = 0; k < lineLength; k++) {
/*  974 */         if (levels[k] >= level) {
/*      */           
/*  976 */           int start = k;
/*  977 */           int limit = k + 1;
/*  978 */           while (limit < lineLength && levels[limit] >= level) {
/*  979 */             limit++;
/*      */           }
/*      */ 
/*      */           
/*  983 */           for (int m = start, n = limit - 1; m < n; m++, n--) {
/*  984 */             int temp = result[m];
/*  985 */             result[m] = result[n];
/*  986 */             result[n] = temp;
/*      */           } 
/*      */ 
/*      */           
/*  990 */           k = limit;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  995 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getBaseLevel() {
/* 1002 */     return this.paragraphEmbeddingLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isWhitespace(byte biditype) {
/* 1011 */     switch (biditype) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 14:
/*      */       case 17:
/* 1019 */         return true;
/*      */     } 
/* 1021 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte typeForLevel(int level) {
/* 1029 */     return ((level & 0x1) == 0) ? 0 : 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int findRunLimit(int index, int limit, byte[] validSet) {
/* 1037 */     index--;
/*      */     
/* 1039 */     label13: while (++index < limit) {
/* 1040 */       byte t = this.resultTypes[index];
/* 1041 */       for (int i = 0; i < validSet.length; i++) {
/* 1042 */         if (t == validSet[i]) {
/*      */           continue label13;
/*      */         }
/*      */       } 
/*      */       
/* 1047 */       return index;
/*      */     } 
/* 1049 */     return limit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int findRunStart(int index, byte[] validSet) {
/* 1058 */     label12: while (--index >= 0) {
/* 1059 */       byte t = this.resultTypes[index];
/* 1060 */       for (int i = 0; i < validSet.length; i++) {
/* 1061 */         if (t == validSet[i]) {
/*      */           continue label12;
/*      */         }
/*      */       } 
/* 1065 */       return index + 1;
/*      */     } 
/* 1067 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setTypes(int start, int limit, byte newType) {
/* 1074 */     for (int i = start; i < limit; i++) {
/* 1075 */       this.resultTypes[i] = newType;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setLevels(int start, int limit, byte newLevel) {
/* 1083 */     for (int i = start; i < limit; i++) {
/* 1084 */       this.resultLevels[i] = newLevel;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateTypes(byte[] types) {
/* 1094 */     if (types == null)
/* 1095 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("types.is.null", new Object[0])); 
/*      */     int i;
/* 1097 */     for (i = 0; i < types.length; i++) {
/* 1098 */       if (types[i] < 0 || types[i] > 18) {
/* 1099 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.type.value.at.1.2", new Object[] { String.valueOf(i), String.valueOf(types[i]) }));
/*      */       }
/*      */     } 
/* 1102 */     for (i = 0; i < types.length - 1; i++) {
/* 1103 */       if (types[i] == 15) {
/* 1104 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("b.type.before.end.of.paragraph.at.index.1", i));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateParagraphEmbeddingLevel(byte paragraphEmbeddingLevel) {
/* 1114 */     if (paragraphEmbeddingLevel != -1 && paragraphEmbeddingLevel != 0 && paragraphEmbeddingLevel != 1)
/*      */     {
/*      */       
/* 1117 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.paragraph.embedding.level.1", paragraphEmbeddingLevel));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void validateLineBreaks(int[] linebreaks, int textLength) {
/* 1125 */     int prev = 0;
/* 1126 */     for (int i = 0; i < linebreaks.length; i++) {
/* 1127 */       int next = linebreaks[i];
/* 1128 */       if (next <= prev) {
/* 1129 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bad.linebreak.1.at.index.2", new Object[] { String.valueOf(next), String.valueOf(i) }));
/*      */       }
/* 1131 */       prev = next;
/*      */     } 
/* 1133 */     if (prev != textLength) {
/* 1134 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("last.linebreak.must.be.at.1", textLength));
/*      */     }
/*      */   }
/*      */   
/* 1138 */   private static final byte[] rtypes = new byte[65536];
/*      */   
/* 1140 */   private static char[] baseTypes = new char[] { Character.MIN_VALUE, '\b', '\016', '\t', '\t', '\020', '\n', '\n', '\017', '\013', '\013', '\020', '\f', '\f', '\021', '\r', '\r', '\017', '\016', '\033', '\016', '\034', '\036', '\017', '\037', '\037', '\020', ' ', ' ', '\021', '!', '"', '\022', '#', '%', '\n', '&', '*', '\022', '+', '+', '\n', ',', ',', '\f', '-', '-', '\n', '.', '.', '\f', '/', '/', '\t', '0', '9', '\b', ':', ':', '\f', ';', '@', '\022', 'A', 'Z', Character.MIN_VALUE, '[', '`', '\022', 'a', 'z', Character.MIN_VALUE, '{', '~', '\022', '', '', '\016', '', '', '\017', '', '', '\016', ' ', ' ', '\f', '¡', '¡', '\022', '¢', '¥', '\n', '¦', '©', '\022', 'ª', 'ª', Character.MIN_VALUE, '«', '¯', '\022', '°', '±', '\n', '²', '³', '\b', '´', '´', '\022', 'µ', 'µ', Character.MIN_VALUE, '¶', '¸', '\022', '¹', '¹', '\b', 'º', 'º', Character.MIN_VALUE, '»', '¿', '\022', 'À', 'Ö', Character.MIN_VALUE, '×', '×', '\022', 'Ø', 'ö', Character.MIN_VALUE, '÷', '÷', '\022', 'ø', 'ʸ', Character.MIN_VALUE, 'ʹ', 'ʺ', '\022', 'ʻ', 'ˁ', Character.MIN_VALUE, '˂', 'ˏ', '\022', 'ː', 'ˑ', Character.MIN_VALUE, '˒', '˟', '\022', 'ˠ', 'ˤ', Character.MIN_VALUE, '˥', '˭', '\022', 'ˮ', 'ˮ', Character.MIN_VALUE, '˯', '˿', '\022', '̀', '͗', '\r', '͘', '͜', Character.MIN_VALUE, '͝', 'ͯ', '\r', 'Ͱ', 'ͳ', Character.MIN_VALUE, 'ʹ', '͵', '\022', 'Ͷ', 'ͽ', Character.MIN_VALUE, ';', ';', '\022', 'Ϳ', '΃', Character.MIN_VALUE, '΄', '΅', '\022', 'Ά', 'Ά', Character.MIN_VALUE, '·', '·', '\022', 'Έ', 'ϵ', Character.MIN_VALUE, '϶', '϶', '\022', 'Ϸ', '҂', Character.MIN_VALUE, '҃', '҆', '\r', '҇', '҇', Character.MIN_VALUE, '҈', '҉', '\r', 'Ҋ', '։', Character.MIN_VALUE, '֊', '֊', '\022', '֋', '֐', Character.MIN_VALUE, '֑', '֡', '\r', '֢', '֢', Character.MIN_VALUE, '֣', 'ֹ', '\r', 'ֺ', 'ֺ', Character.MIN_VALUE, 'ֻ', 'ֽ', '\r', '־', '־', '\003', 'ֿ', 'ֿ', '\r', '׀', '׀', '\003', 'ׁ', 'ׂ', '\r', '׃', '׃', '\003', 'ׄ', 'ׄ', '\r', 'ׅ', '׏', Character.MIN_VALUE, 'א', 'ת', '\003', '׫', 'ׯ', Character.MIN_VALUE, 'װ', '״', '\003', '׵', '׿', Character.MIN_VALUE, '؀', '؃', '\004', '؄', '؋', Character.MIN_VALUE, '،', '،', '\f', '؍', '؍', '\004', '؎', '؏', '\022', 'ؐ', 'ؕ', '\r', 'ؖ', 'ؚ', Character.MIN_VALUE, '؛', '؛', '\004', '؜', '؞', Character.MIN_VALUE, '؟', '؟', '\004', 'ؠ', 'ؠ', Character.MIN_VALUE, 'ء', 'غ', '\004', 'ػ', 'ؿ', Character.MIN_VALUE, 'ـ', 'ي', '\004', 'ً', '٘', '\r', 'ٙ', 'ٟ', Character.MIN_VALUE, '٠', '٩', '\013', '٪', '٪', '\n', '٫', '٬', '\013', '٭', 'ٯ', '\004', 'ٰ', 'ٰ', '\r', 'ٱ', 'ە', '\004', 'ۖ', 'ۜ', '\r', '۝', '۝', '\004', '۞', 'ۤ', '\r', 'ۥ', 'ۦ', '\004', 'ۧ', 'ۨ', '\r', '۩', '۩', '\022', '۪', 'ۭ', '\r', 'ۮ', 'ۯ', '\004', '۰', '۹', '\b', 'ۺ', '܍', '\004', '܎', '܎', Character.MIN_VALUE, '܏', '܏', '\016', 'ܐ', 'ܐ', '\004', 'ܑ', 'ܑ', '\r', 'ܒ', 'ܯ', '\004', 'ܰ', '݊', '\r', '݋', '݌', Character.MIN_VALUE, 'ݍ', 'ݏ', '\004', 'ݐ', 'ݿ', Character.MIN_VALUE, 'ހ', 'ޥ', '\004', 'ަ', 'ް', '\r', 'ޱ', 'ޱ', '\004', '޲', 'ऀ', Character.MIN_VALUE, 'ँ', 'ं', '\r', 'ः', 'ऻ', Character.MIN_VALUE, '़', '़', '\r', 'ऽ', 'ी', Character.MIN_VALUE, 'ु', 'ै', '\r', 'ॉ', 'ौ', Character.MIN_VALUE, '्', '्', '\r', 'ॎ', 'ॐ', Character.MIN_VALUE, '॑', '॔', '\r', 'ॕ', 'ॡ', Character.MIN_VALUE, 'ॢ', 'ॣ', '\r', '।', 'ঀ', Character.MIN_VALUE, 'ঁ', 'ঁ', '\r', 'ং', '঻', Character.MIN_VALUE, '়', '়', '\r', 'ঽ', 'ী', Character.MIN_VALUE, 'ু', 'ৄ', '\r', '৅', 'ৌ', Character.MIN_VALUE, '্', '্', '\r', 'ৎ', 'ৡ', Character.MIN_VALUE, 'ৢ', 'ৣ', '\r', '৤', 'ৱ', Character.MIN_VALUE, '৲', '৳', '\n', '৴', '਀', Character.MIN_VALUE, 'ਁ', 'ਂ', '\r', 'ਃ', '਻', Character.MIN_VALUE, '਼', '਼', '\r', '਽', 'ੀ', Character.MIN_VALUE, 'ੁ', 'ੂ', '\r', '੃', '੆', Character.MIN_VALUE, 'ੇ', 'ੈ', '\r', '੉', '੊', Character.MIN_VALUE, 'ੋ', '੍', '\r', '੎', '੯', Character.MIN_VALUE, 'ੰ', 'ੱ', '\r', 'ੲ', '઀', Character.MIN_VALUE, 'ઁ', 'ં', '\r', 'ઃ', '઻', Character.MIN_VALUE, '઼', '઼', '\r', 'ઽ', 'ી', Character.MIN_VALUE, 'ુ', 'ૅ', '\r', '૆', '૆', Character.MIN_VALUE, 'ે', 'ૈ', '\r', 'ૉ', 'ૌ', Character.MIN_VALUE, '્', '્', '\r', '૎', 'ૡ', Character.MIN_VALUE, 'ૢ', 'ૣ', '\r', '૤', '૰', Character.MIN_VALUE, '૱', '૱', '\n', '૲', '଀', Character.MIN_VALUE, 'ଁ', 'ଁ', '\r', 'ଂ', '଻', Character.MIN_VALUE, '଼', '଼', '\r', 'ଽ', 'ା', Character.MIN_VALUE, 'ି', 'ି', '\r', 'ୀ', 'ୀ', Character.MIN_VALUE, 'ୁ', 'ୃ', '\r', 'ୄ', 'ୌ', Character.MIN_VALUE, '୍', '୍', '\r', '୎', '୕', Character.MIN_VALUE, 'ୖ', 'ୖ', '\r', 'ୗ', '஁', Character.MIN_VALUE, 'ஂ', 'ஂ', '\r', 'ஃ', 'ி', Character.MIN_VALUE, 'ீ', 'ீ', '\r', 'ு', 'ௌ', Character.MIN_VALUE, '்', '்', '\r', '௎', '௲', Character.MIN_VALUE, '௳', '௸', '\022', '௹', '௹', '\n', '௺', '௺', '\022', '௻', 'ఽ', Character.MIN_VALUE, 'ా', 'ీ', '\r', 'ు', '౅', Character.MIN_VALUE, 'ె', 'ై', '\r', '౉', '౉', Character.MIN_VALUE, 'ొ', '్', '\r', '౎', '౔', Character.MIN_VALUE, 'ౕ', 'ౖ', '\r', '౗', '಻', Character.MIN_VALUE, '಼', '಼', '\r', 'ಽ', 'ೋ', Character.MIN_VALUE, 'ೌ', '್', '\r', '೎', 'ീ', Character.MIN_VALUE, 'ു', 'ൃ', '\r', 'ൄ', 'ൌ', Character.MIN_VALUE, '്', '്', '\r', 'ൎ', '෉', Character.MIN_VALUE, '්', '්', '\r', '෋', 'ෑ', Character.MIN_VALUE, 'ි', 'ු', '\r', '෕', '෕', Character.MIN_VALUE, 'ූ', 'ූ', '\r', '෗', 'ะ', Character.MIN_VALUE, 'ั', 'ั', '\r', 'า', 'ำ', Character.MIN_VALUE, 'ิ', 'ฺ', '\r', '฻', '฾', Character.MIN_VALUE, '฿', '฿', '\n', 'เ', 'ๆ', Character.MIN_VALUE, '็', '๎', '\r', '๏', 'ະ', Character.MIN_VALUE, 'ັ', 'ັ', '\r', 'າ', 'ຳ', Character.MIN_VALUE, 'ິ', 'ູ', '\r', '຺', '຺', Character.MIN_VALUE, 'ົ', 'ຼ', '\r', 'ຽ', '໇', Character.MIN_VALUE, '່', 'ໍ', '\r', '໎', '༗', Character.MIN_VALUE, '༘', '༙', '\r', '༚', '༴', Character.MIN_VALUE, '༵', '༵', '\r', '༶', '༶', Character.MIN_VALUE, '༷', '༷', '\r', '༸', '༸', Character.MIN_VALUE, '༹', '༹', '\r', '༺', '༽', '\022', '༾', '཰', Character.MIN_VALUE, 'ཱ', 'ཾ', '\r', 'ཿ', 'ཿ', Character.MIN_VALUE, 'ྀ', '྄', '\r', '྅', '྅', Character.MIN_VALUE, '྆', '྇', '\r', 'ྈ', 'ྏ', Character.MIN_VALUE, 'ྐ', 'ྗ', '\r', '྘', '྘', Character.MIN_VALUE, 'ྙ', 'ྼ', '\r', '྽', '࿅', Character.MIN_VALUE, '࿆', '࿆', '\r', '࿇', 'ာ', Character.MIN_VALUE, 'ိ', 'ူ', '\r', 'ေ', 'ေ', Character.MIN_VALUE, 'ဲ', 'ဲ', '\r', 'ဳ', 'ဵ', Character.MIN_VALUE, 'ံ', '့', '\r', 'း', 'း', Character.MIN_VALUE, '္', '္', '\r', '်', 'ၗ', Character.MIN_VALUE, 'ၘ', 'ၙ', '\r', 'ၚ', 'ᙿ', Character.MIN_VALUE, ' ', ' ', '\021', 'ᚁ', 'ᚚ', Character.MIN_VALUE, '᚛', '᚜', '\022', '᚝', 'ᜑ', Character.MIN_VALUE, 'ᜒ', '᜔', '\r', '᜕', 'ᜱ', Character.MIN_VALUE, 'ᜲ', '᜴', '\r', '᜵', 'ᝑ', Character.MIN_VALUE, 'ᝒ', 'ᝓ', '\r', '᝔', '᝱', Character.MIN_VALUE, 'ᝲ', 'ᝳ', '\r', '᝴', 'ា', Character.MIN_VALUE, 'ិ', 'ួ', '\r', 'ើ', 'ៅ', Character.MIN_VALUE, 'ំ', 'ំ', '\r', 'ះ', 'ៈ', Character.MIN_VALUE, '៉', '៓', '\r', '។', '៚', Character.MIN_VALUE, '៛', '៛', '\n', 'ៜ', 'ៜ', Character.MIN_VALUE, '៝', '៝', '\r', '៞', '៯', Character.MIN_VALUE, '៰', '៹', '\022', '៺', '៿', Character.MIN_VALUE, '᠀', '᠊', '\022', '᠋', '᠍', '\r', '᠎', '᠎', '\021', '᠏', 'ᢨ', Character.MIN_VALUE, 'ᢩ', 'ᢩ', '\r', 'ᢪ', '᤟', Character.MIN_VALUE, 'ᤠ', 'ᤢ', '\r', 'ᤣ', 'ᤦ', Character.MIN_VALUE, 'ᤧ', 'ᤫ', '\r', '᤬', 'ᤱ', Character.MIN_VALUE, 'ᤲ', 'ᤲ', '\r', 'ᤳ', 'ᤸ', Character.MIN_VALUE, '᤹', '᤻', '\r', '᤼', '᤿', Character.MIN_VALUE, '᥀', '᥀', '\022', '᥁', '᥃', Character.MIN_VALUE, '᥄', '᥅', '\022', '᥆', '᧟', Character.MIN_VALUE, '᧠', '᧿', '\022', 'ᨀ', 'ᾼ', Character.MIN_VALUE, '᾽', '᾽', '\022', 'ι', 'ι', Character.MIN_VALUE, '᾿', '῁', '\022', 'ῂ', 'ῌ', Character.MIN_VALUE, '῍', '῏', '\022', 'ῐ', '῜', Character.MIN_VALUE, '῝', '῟', '\022', 'ῠ', 'Ῥ', Character.MIN_VALUE, '῭', '`', '\022', '῰', 'ῼ', Character.MIN_VALUE, '´', '῾', '\022', '῿', '῿', Character.MIN_VALUE, ' ', ' ', '\021', '​', '‍', '\016', '‎', '‎', Character.MIN_VALUE, '‏', '‏', '\003', '‐', '‧', '\022', ' ', ' ', '\021', ' ', ' ', '\017', '‪', '‪', '\001', '‫', '‫', '\005', '‬', '‬', '\007', '‭', '‭', '\002', '‮', '‮', '\006', ' ', ' ', '\021', '‰', '‴', '\n', '‵', '⁔', '\022', '⁕', '⁖', Character.MIN_VALUE, '⁗', '⁗', '\022', '⁘', '⁞', Character.MIN_VALUE, ' ', ' ', '\021', '⁠', '⁣', '\016', '⁤', '⁩', Character.MIN_VALUE, '⁪', '⁯', '\016', '⁰', '⁰', '\b', 'ⁱ', '⁳', Character.MIN_VALUE, '⁴', '⁹', '\b', '⁺', '⁻', '\n', '⁼', '⁾', '\022', 'ⁿ', 'ⁿ', Character.MIN_VALUE, '₀', '₉', '\b', '₊', '₋', '\n', '₌', '₎', '\022', '₏', '₟', Character.MIN_VALUE, '₠', '₱', '\n', '₲', '⃏', Character.MIN_VALUE, '⃐', '⃪', '\r', '⃫', '⃿', Character.MIN_VALUE, '℀', '℁', '\022', 'ℂ', 'ℂ', Character.MIN_VALUE, '℃', '℆', '\022', 'ℇ', 'ℇ', Character.MIN_VALUE, '℈', '℉', '\022', 'ℊ', 'ℓ', Character.MIN_VALUE, '℔', '℔', '\022', 'ℕ', 'ℕ', Character.MIN_VALUE, '№', '℘', '\022', 'ℙ', 'ℝ', Character.MIN_VALUE, '℞', '℣', '\022', 'ℤ', 'ℤ', Character.MIN_VALUE, '℥', '℥', '\022', 'Ω', 'Ω', Character.MIN_VALUE, '℧', '℧', '\022', 'ℨ', 'ℨ', Character.MIN_VALUE, '℩', '℩', '\022', 'K', 'ℭ', Character.MIN_VALUE, '℮', '℮', '\n', 'ℯ', 'ℱ', Character.MIN_VALUE, 'Ⅎ', 'Ⅎ', '\022', 'ℳ', 'ℹ', Character.MIN_VALUE, '℺', '℻', '\022', 'ℼ', 'ℿ', Character.MIN_VALUE, '⅀', '⅄', '\022', 'ⅅ', 'ⅉ', Character.MIN_VALUE, '⅊', '⅋', '\022', '⅌', '⅒', Character.MIN_VALUE, '⅓', '⅟', '\022', 'Ⅰ', '↏', Character.MIN_VALUE, '←', '∑', '\022', '−', '∓', '\n', '∔', '⌵', '\022', '⌶', '⍺', Character.MIN_VALUE, '⍻', '⎔', '\022', '⎕', '⎕', Character.MIN_VALUE, '⎖', '⏐', '\022', '⏑', '⏿', Character.MIN_VALUE, '␀', '␦', '\022', '␧', '␿', Character.MIN_VALUE, '⑀', '⑊', '\022', '⑋', '⑟', Character.MIN_VALUE, '①', '⒛', '\b', '⒜', 'ⓩ', Character.MIN_VALUE, '⓪', '⓪', '\b', '⓫', '☗', '\022', '☘', '☘', Character.MIN_VALUE, '☙', '♽', '\022', '♾', '♿', Character.MIN_VALUE, '⚀', '⚑', '\022', '⚒', '⚟', Character.MIN_VALUE, '⚠', '⚡', '\022', '⚢', '✀', Character.MIN_VALUE, '✁', '✄', '\022', '✅', '✅', Character.MIN_VALUE, '✆', '✉', '\022', '✊', '✋', Character.MIN_VALUE, '✌', '✧', '\022', '✨', '✨', Character.MIN_VALUE, '✩', '❋', '\022', '❌', '❌', Character.MIN_VALUE, '❍', '❍', '\022', '❎', '❎', Character.MIN_VALUE, '❏', '❒', '\022', '❓', '❕', Character.MIN_VALUE, '❖', '❖', '\022', '❗', '❗', Character.MIN_VALUE, '❘', '❞', '\022', '❟', '❠', Character.MIN_VALUE, '❡', '➔', '\022', '➕', '➗', Character.MIN_VALUE, '➘', '➯', '\022', '➰', '➰', Character.MIN_VALUE, '➱', '➾', '\022', '➿', '⟏', Character.MIN_VALUE, '⟐', '⟫', '\022', '⟬', '⟯', Character.MIN_VALUE, '⟰', '⬍', '\022', '⬎', '⹿', Character.MIN_VALUE, '⺀', '⺙', '\022', '⺚', '⺚', Character.MIN_VALUE, '⺛', '⻳', '\022', '⻴', '⻿', Character.MIN_VALUE, '⼀', '⿕', '\022', '⿖', '⿯', Character.MIN_VALUE, '⿰', '⿻', '\022', '⿼', '⿿', Character.MIN_VALUE, '　', '　', '\021', '、', '〄', '\022', '々', '〇', Character.MIN_VALUE, '〈', '〠', '\022', '〡', '〩', Character.MIN_VALUE, '〪', '〯', '\r', '〰', '〰', '\022', '〱', '〵', Character.MIN_VALUE, '〶', '〷', '\022', '〸', '〼', Character.MIN_VALUE, '〽', '〿', '\022', '぀', '゘', Character.MIN_VALUE, '゙', '゚', '\r', '゛', '゜', '\022', 'ゝ', 'ゟ', Character.MIN_VALUE, '゠', '゠', '\022', 'ァ', 'ヺ', Character.MIN_VALUE, '・', '・', '\022', 'ー', '㈜', Character.MIN_VALUE, '㈝', '㈞', '\022', '㈟', '㉏', Character.MIN_VALUE, '㉐', '㉟', '\022', '㉠', '㉻', Character.MIN_VALUE, '㉼', '㉽', '\022', '㉾', '㊰', Character.MIN_VALUE, '㊱', '㊿', '\022', '㋀', '㋋', Character.MIN_VALUE, '㋌', '㋏', '\022', '㋐', '㍶', Character.MIN_VALUE, '㍷', '㍺', '\022', '㍻', '㏝', Character.MIN_VALUE, '㏞', '㏟', '\022', '㏠', '㏾', Character.MIN_VALUE, '㏿', '㏿', '\022', '㐀', '䶿', Character.MIN_VALUE, '䷀', '䷿', '\022', '一', '꒏', Character.MIN_VALUE, '꒐', '꓆', '\022', '꓇', '﬜', Character.MIN_VALUE, 'יִ', 'יִ', '\003', 'ﬞ', 'ﬞ', '\r', 'ײַ', 'ﬨ', '\003', '﬩', '﬩', '\n', 'שׁ', 'זּ', '\003', '﬷', '﬷', Character.MIN_VALUE, 'טּ', 'לּ', '\003', '﬽', '﬽', Character.MIN_VALUE, 'מּ', 'מּ', '\003', '﬿', '﬿', Character.MIN_VALUE, 'נּ', 'סּ', '\003', '﭂', '﭂', Character.MIN_VALUE, 'ףּ', 'פּ', '\003', '﭅', '﭅', Character.MIN_VALUE, 'צּ', 'ﭏ', '\003', 'ﭐ', 'ﮱ', '\004', '﮲', '﯒', Character.MIN_VALUE, 'ﯓ', 'ﴽ', '\004', '﴾', '﴿', '\022', '﵀', '﵏', Character.MIN_VALUE, 'ﵐ', 'ﶏ', '\004', '﶐', '﶑', Character.MIN_VALUE, 'ﶒ', 'ﷇ', '\004', '﷈', '﷯', Character.MIN_VALUE, 'ﷰ', '﷼', '\004', '﷽', '﷽', '\022', '﷾', '﷿', Character.MIN_VALUE, '︀', '️', '\r', '︐', '︟', Character.MIN_VALUE, '︠', '︣', '\r', '︤', '︯', Character.MIN_VALUE, '︰', '﹏', '\022', '﹐', '﹐', '\f', '﹑', '﹑', '\022', '﹒', '﹒', '\f', '﹓', '﹓', Character.MIN_VALUE, '﹔', '﹔', '\022', '﹕', '﹕', '\f', '﹖', '﹞', '\022', '﹟', '﹟', '\n', '﹠', '﹡', '\022', '﹢', '﹣', '\n', '﹤', '﹦', '\022', '﹧', '﹧', Character.MIN_VALUE, '﹨', '﹨', '\022', '﹩', '﹪', '\n', '﹫', '﹫', '\022', '﹬', '﹯', Character.MIN_VALUE, 'ﹰ', 'ﹴ', '\004', '﹵', '﹵', Character.MIN_VALUE, 'ﹶ', 'ﻼ', '\004', '﻽', '﻾', Character.MIN_VALUE, '﻿', '﻿', '\016', '＀', '＀', Character.MIN_VALUE, '！', '＂', '\022', '＃', '％', '\n', '＆', '＊', '\022', '＋', '＋', '\n', '，', '，', '\f', '－', '－', '\n', '．', '．', '\f', '／', '／', '\t', '０', '９', '\b', '：', '：', '\f', '；', '＠', '\022', 'Ａ', 'Ｚ', Character.MIN_VALUE, '［', '｀', '\022', 'ａ', 'ｚ', Character.MIN_VALUE, '｛', '･', '\022', 'ｦ', '￟', Character.MIN_VALUE, '￠', '￡', '\n', '￢', '￤', '\022', '￥', '￦', '\n', '￧', '￧', Character.MIN_VALUE, '￨', '￮', '\022', '￯', '￸', Character.MIN_VALUE, '￹', '￻', '\016', '￼', '�', '\022', '￾', Character.MAX_VALUE, Character.MIN_VALUE };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/* 1283 */     for (int k = 0; k < baseTypes.length; k++) {
/* 1284 */       int start = baseTypes[k];
/* 1285 */       int end = baseTypes[++k];
/* 1286 */       byte b = (byte)baseTypes[++k];
/* 1287 */       while (start <= end)
/* 1288 */         rtypes[start++] = b; 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BidiOrder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */