/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.Chunk;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.TabStop;
/*      */ import com.itextpdf.text.Utilities;
/*      */ import com.itextpdf.text.pdf.draw.DrawInterface;
/*      */ import com.itextpdf.text.pdf.draw.LineSeparator;
/*      */ import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BidiLine
/*      */ {
/*      */   protected int runDirection;
/*   64 */   protected int pieceSize = 256;
/*   65 */   protected char[] text = new char[this.pieceSize];
/*   66 */   protected PdfChunk[] detailChunks = new PdfChunk[this.pieceSize];
/*   67 */   protected int totalTextLength = 0;
/*      */   
/*   69 */   protected byte[] orderLevels = new byte[this.pieceSize];
/*   70 */   protected int[] indexChars = new int[this.pieceSize];
/*      */   
/*   72 */   protected ArrayList<PdfChunk> chunks = new ArrayList<PdfChunk>();
/*   73 */   protected int indexChunk = 0;
/*   74 */   protected int indexChunkChar = 0;
/*   75 */   protected int currentChar = 0;
/*      */   
/*      */   protected int storedRunDirection;
/*   78 */   protected char[] storedText = new char[0];
/*   79 */   protected PdfChunk[] storedDetailChunks = new PdfChunk[0];
/*   80 */   protected int storedTotalTextLength = 0;
/*      */   
/*   82 */   protected byte[] storedOrderLevels = new byte[0];
/*   83 */   protected int[] storedIndexChars = new int[0];
/*      */   
/*   85 */   protected int storedIndexChunk = 0;
/*   86 */   protected int storedIndexChunkChar = 0;
/*   87 */   protected int storedCurrentChar = 0;
/*      */   
/*      */   protected boolean isWordSplit = false;
/*      */   
/*      */   protected boolean shortStore;
/*      */   
/*   93 */   protected static final IntHashtable mirrorChars = new IntHashtable();
/*      */ 
/*      */   
/*      */   protected int arabicOptions;
/*      */ 
/*      */ 
/*      */   
/*      */   public BidiLine(BidiLine org) {
/*  101 */     this.runDirection = org.runDirection;
/*  102 */     this.pieceSize = org.pieceSize;
/*  103 */     this.text = (char[])org.text.clone();
/*  104 */     this.detailChunks = (PdfChunk[])org.detailChunks.clone();
/*  105 */     this.totalTextLength = org.totalTextLength;
/*      */     
/*  107 */     this.orderLevels = (byte[])org.orderLevels.clone();
/*  108 */     this.indexChars = (int[])org.indexChars.clone();
/*      */     
/*  110 */     this.chunks = new ArrayList<PdfChunk>(org.chunks);
/*  111 */     this.indexChunk = org.indexChunk;
/*  112 */     this.indexChunkChar = org.indexChunkChar;
/*  113 */     this.currentChar = org.currentChar;
/*      */     
/*  115 */     this.storedRunDirection = org.storedRunDirection;
/*  116 */     this.storedText = (char[])org.storedText.clone();
/*  117 */     this.storedDetailChunks = (PdfChunk[])org.storedDetailChunks.clone();
/*  118 */     this.storedTotalTextLength = org.storedTotalTextLength;
/*      */     
/*  120 */     this.storedOrderLevels = (byte[])org.storedOrderLevels.clone();
/*  121 */     this.storedIndexChars = (int[])org.storedIndexChars.clone();
/*      */     
/*  123 */     this.storedIndexChunk = org.storedIndexChunk;
/*  124 */     this.storedIndexChunkChar = org.storedIndexChunkChar;
/*  125 */     this.storedCurrentChar = org.storedCurrentChar;
/*      */     
/*  127 */     this.shortStore = org.shortStore;
/*  128 */     this.arabicOptions = org.arabicOptions;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  132 */     return (this.currentChar >= this.totalTextLength && this.indexChunk >= this.chunks.size());
/*      */   }
/*      */   
/*      */   public void clearChunks() {
/*  136 */     this.chunks.clear();
/*  137 */     this.totalTextLength = 0;
/*  138 */     this.currentChar = 0;
/*      */   }
/*      */   
/*      */   public boolean getParagraph(int runDirection) {
/*  142 */     this.runDirection = runDirection;
/*  143 */     this.currentChar = 0;
/*  144 */     this.totalTextLength = 0;
/*      */     
/*  146 */     boolean hasText = false;
/*      */ 
/*      */ 
/*      */     
/*  150 */     for (; this.indexChunk < this.chunks.size(); this.indexChunk++) {
/*  151 */       PdfChunk ck = this.chunks.get(this.indexChunk);
/*  152 */       BaseFont bf = ck.font().getFont();
/*  153 */       String s = ck.toString();
/*  154 */       int len = s.length();
/*  155 */       for (; this.indexChunkChar < len; this.indexChunkChar++) {
/*  156 */         char c = s.charAt(this.indexChunkChar);
/*  157 */         char uniC = (char)bf.getUnicodeEquivalent(c);
/*  158 */         if (uniC == '\r' || uniC == '\n') {
/*      */           
/*  160 */           if (uniC == '\r' && this.indexChunkChar + 1 < len && s.charAt(this.indexChunkChar + 1) == '\n')
/*  161 */             this.indexChunkChar++; 
/*  162 */           this.indexChunkChar++;
/*  163 */           if (this.indexChunkChar >= len) {
/*  164 */             this.indexChunkChar = 0;
/*  165 */             this.indexChunk++;
/*      */           } 
/*  167 */           hasText = true;
/*  168 */           if (this.totalTextLength == 0)
/*  169 */             this.detailChunks[0] = ck; 
/*      */           break;
/*      */         } 
/*  172 */         addPiece(c, ck);
/*      */       } 
/*  174 */       if (hasText)
/*      */         break; 
/*  176 */       this.indexChunkChar = 0;
/*      */     } 
/*  178 */     if (this.totalTextLength == 0) {
/*  179 */       return hasText;
/*      */     }
/*      */     
/*  182 */     this.totalTextLength = trimRight(0, this.totalTextLength - 1) + 1;
/*  183 */     if (this.totalTextLength == 0) {
/*  184 */       return true;
/*      */     }
/*      */     
/*  187 */     if (runDirection != 1) {
/*  188 */       byte paragraphEmbeddingLevel; if (this.orderLevels.length < this.totalTextLength) {
/*  189 */         this.orderLevels = new byte[this.pieceSize];
/*  190 */         this.indexChars = new int[this.pieceSize];
/*      */       } 
/*  192 */       ArabicLigaturizer.processNumbers(this.text, 0, this.totalTextLength, this.arabicOptions);
/*      */       
/*  194 */       switch (runDirection) {
/*      */         case 2:
/*  196 */           paragraphEmbeddingLevel = 0;
/*      */           break;
/*      */         case 3:
/*  199 */           paragraphEmbeddingLevel = 1;
/*      */           break;
/*      */         
/*      */         default:
/*  203 */           paragraphEmbeddingLevel = -1;
/*      */           break;
/*      */       } 
/*  206 */       BidiOrder order = new BidiOrder(this.text, 0, this.totalTextLength, paragraphEmbeddingLevel);
/*  207 */       byte[] od = order.getLevels();
/*  208 */       for (int k = 0; k < this.totalTextLength; k++) {
/*  209 */         this.orderLevels[k] = od[k];
/*  210 */         this.indexChars[k] = k;
/*      */       } 
/*  212 */       doArabicShapping();
/*  213 */       mirrorGlyphs();
/*      */     } 
/*  215 */     this.totalTextLength = trimRightEx(0, this.totalTextLength - 1) + 1;
/*  216 */     return true;
/*      */   }
/*      */   
/*      */   public void addChunk(PdfChunk chunk) {
/*  220 */     this.chunks.add(chunk);
/*      */   }
/*      */   
/*      */   public void addChunks(ArrayList<PdfChunk> chunks) {
/*  224 */     this.chunks.addAll(chunks);
/*      */   }
/*      */   
/*      */   public void addPiece(char c, PdfChunk chunk) {
/*  228 */     if (this.totalTextLength >= this.pieceSize) {
/*  229 */       char[] tempText = this.text;
/*  230 */       PdfChunk[] tempDetailChunks = this.detailChunks;
/*  231 */       this.pieceSize *= 2;
/*  232 */       this.text = new char[this.pieceSize];
/*  233 */       this.detailChunks = new PdfChunk[this.pieceSize];
/*  234 */       System.arraycopy(tempText, 0, this.text, 0, this.totalTextLength);
/*  235 */       System.arraycopy(tempDetailChunks, 0, this.detailChunks, 0, this.totalTextLength);
/*      */     } 
/*  237 */     this.text[this.totalTextLength] = c;
/*  238 */     this.detailChunks[this.totalTextLength++] = chunk;
/*      */   }
/*      */   
/*      */   public void save() {
/*  242 */     if (this.indexChunk > 0) {
/*  243 */       if (this.indexChunk >= this.chunks.size()) {
/*  244 */         this.chunks.clear();
/*      */       } else {
/*  246 */         this.indexChunk--; for (; this.indexChunk >= 0; this.indexChunk--)
/*  247 */           this.chunks.remove(this.indexChunk); 
/*      */       } 
/*  249 */       this.indexChunk = 0;
/*      */     } 
/*  251 */     this.storedRunDirection = this.runDirection;
/*  252 */     this.storedTotalTextLength = this.totalTextLength;
/*  253 */     this.storedIndexChunk = this.indexChunk;
/*  254 */     this.storedIndexChunkChar = this.indexChunkChar;
/*  255 */     this.storedCurrentChar = this.currentChar;
/*  256 */     this.shortStore = (this.currentChar < this.totalTextLength);
/*  257 */     if (!this.shortStore) {
/*      */       
/*  259 */       if (this.storedText.length < this.totalTextLength) {
/*  260 */         this.storedText = new char[this.totalTextLength];
/*  261 */         this.storedDetailChunks = new PdfChunk[this.totalTextLength];
/*      */       } 
/*  263 */       System.arraycopy(this.text, 0, this.storedText, 0, this.totalTextLength);
/*  264 */       System.arraycopy(this.detailChunks, 0, this.storedDetailChunks, 0, this.totalTextLength);
/*      */     } 
/*  266 */     if (this.runDirection != 1) {
/*  267 */       if (this.storedOrderLevels.length < this.totalTextLength) {
/*  268 */         this.storedOrderLevels = new byte[this.totalTextLength];
/*  269 */         this.storedIndexChars = new int[this.totalTextLength];
/*      */       } 
/*  271 */       System.arraycopy(this.orderLevels, this.currentChar, this.storedOrderLevels, this.currentChar, this.totalTextLength - this.currentChar);
/*  272 */       System.arraycopy(this.indexChars, this.currentChar, this.storedIndexChars, this.currentChar, this.totalTextLength - this.currentChar);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void restore() {
/*  277 */     this.runDirection = this.storedRunDirection;
/*  278 */     this.totalTextLength = this.storedTotalTextLength;
/*  279 */     this.indexChunk = this.storedIndexChunk;
/*  280 */     this.indexChunkChar = this.storedIndexChunkChar;
/*  281 */     this.currentChar = this.storedCurrentChar;
/*  282 */     if (!this.shortStore) {
/*      */       
/*  284 */       System.arraycopy(this.storedText, 0, this.text, 0, this.totalTextLength);
/*  285 */       System.arraycopy(this.storedDetailChunks, 0, this.detailChunks, 0, this.totalTextLength);
/*      */     } 
/*  287 */     if (this.runDirection != 1) {
/*  288 */       System.arraycopy(this.storedOrderLevels, this.currentChar, this.orderLevels, this.currentChar, this.totalTextLength - this.currentChar);
/*  289 */       System.arraycopy(this.storedIndexChars, this.currentChar, this.indexChars, this.currentChar, this.totalTextLength - this.currentChar);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void mirrorGlyphs() {
/*  294 */     for (int k = 0; k < this.totalTextLength; k++) {
/*  295 */       if ((this.orderLevels[k] & 0x1) == 1) {
/*  296 */         int mirror = mirrorChars.get(this.text[k]);
/*  297 */         if (mirror != 0)
/*  298 */           this.text[k] = (char)mirror; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void doArabicShapping() {
/*  304 */     int src = 0;
/*  305 */     int dest = 0;
/*      */     while (true) {
/*  307 */       if (src < this.totalTextLength) {
/*  308 */         char c = this.text[src];
/*  309 */         if (c < '؀' || c > 'ۿ') {
/*      */           
/*  311 */           if (src != dest) {
/*  312 */             this.text[dest] = this.text[src];
/*  313 */             this.detailChunks[dest] = this.detailChunks[src];
/*  314 */             this.orderLevels[dest] = this.orderLevels[src];
/*      */           } 
/*  316 */           src++;
/*  317 */           dest++; continue;
/*      */         } 
/*  319 */       }  if (src >= this.totalTextLength) {
/*  320 */         this.totalTextLength = dest;
/*      */         return;
/*      */       } 
/*  323 */       int startArabicIdx = src;
/*  324 */       src++;
/*  325 */       while (src < this.totalTextLength) {
/*  326 */         char c = this.text[src];
/*  327 */         if (c < '؀' || c > 'ۿ')
/*      */           break; 
/*  329 */         src++;
/*      */       } 
/*  331 */       int arabicWordSize = src - startArabicIdx;
/*  332 */       int size = ArabicLigaturizer.arabic_shape(this.text, startArabicIdx, arabicWordSize, this.text, dest, arabicWordSize, this.arabicOptions);
/*  333 */       if (startArabicIdx != dest) {
/*  334 */         for (int k = 0; k < size; k++) {
/*  335 */           this.detailChunks[dest] = this.detailChunks[startArabicIdx];
/*  336 */           this.orderLevels[dest++] = this.orderLevels[startArabicIdx++];
/*      */         } 
/*      */         continue;
/*      */       } 
/*  340 */       dest += size;
/*      */     } 
/*      */   }
/*      */   
/*      */   public PdfLine processLine(float leftX, float width, int alignment, int runDirection, int arabicOptions, float minY, float yLine, float descender) {
/*  345 */     this.isWordSplit = false;
/*  346 */     this.arabicOptions = arabicOptions;
/*  347 */     save();
/*  348 */     boolean isRTL = (runDirection == 3);
/*  349 */     if (this.currentChar >= this.totalTextLength) {
/*  350 */       boolean hasText = getParagraph(runDirection);
/*  351 */       if (!hasText)
/*  352 */         return null; 
/*  353 */       if (this.totalTextLength == 0) {
/*  354 */         ArrayList<PdfChunk> ar = new ArrayList<PdfChunk>();
/*  355 */         PdfChunk pdfChunk = new PdfChunk("", this.detailChunks[0]);
/*  356 */         ar.add(pdfChunk);
/*  357 */         return new PdfLine(0.0F, 0.0F, width, alignment, true, ar, isRTL);
/*      */       } 
/*      */     } 
/*  360 */     float originalWidth = width;
/*  361 */     int lastSplit = -1;
/*  362 */     if (this.currentChar != 0) {
/*  363 */       this.currentChar = trimLeftEx(this.currentChar, this.totalTextLength - 1);
/*      */     }
/*  365 */     int oldCurrentChar = this.currentChar;
/*  366 */     int uniC = 0;
/*  367 */     PdfChunk ck = null;
/*  368 */     float charWidth = 0.0F;
/*  369 */     PdfChunk lastValidChunk = null;
/*  370 */     TabStop tabStop = null;
/*  371 */     List<TabStop> rtlTabsToBeAligned = new ArrayList<TabStop>();
/*  372 */     float tabStopAnchorPosition = Float.NaN;
/*  373 */     float tabPosition = Float.NaN;
/*  374 */     boolean surrogate = false;
/*  375 */     for (; this.currentChar < this.totalTextLength; this.currentChar++) {
/*  376 */       ck = this.detailChunks[this.currentChar];
/*  377 */       if (ck.isImage() && minY < yLine) {
/*  378 */         Image img = ck.getImage();
/*  379 */         if (img.isScaleToFitHeight() && yLine + 2.0F * descender - img.getScaledHeight() - ck.getImageOffsetY() - img.getSpacingBefore() < minY) {
/*  380 */           float scalePercent = (yLine + 2.0F * descender - ck.getImageOffsetY() - img.getSpacingBefore() - minY) / img.getScaledHeight();
/*  381 */           ck.setImageScalePercentage(scalePercent);
/*      */         } 
/*      */       } 
/*  384 */       surrogate = Utilities.isSurrogatePair(this.text, this.currentChar);
/*  385 */       if (surrogate) {
/*  386 */         uniC = ck.getUnicodeEquivalent(Utilities.convertToUtf32(this.text, this.currentChar));
/*      */       } else {
/*  388 */         uniC = ck.getUnicodeEquivalent(this.text[this.currentChar]);
/*  389 */       }  if (!PdfChunk.noPrint(uniC)) {
/*      */         
/*  391 */         if (surrogate) {
/*  392 */           charWidth = ck.getCharWidth(uniC);
/*      */         }
/*  394 */         else if (ck.isImage()) {
/*  395 */           charWidth = ck.getImageWidth();
/*      */         } else {
/*  397 */           charWidth = ck.getCharWidth(this.text[this.currentChar]);
/*      */         } 
/*      */         
/*  400 */         if (width - charWidth < 0.0F)
/*      */         {
/*      */           
/*  403 */           if (lastValidChunk == null && ck.isImage()) {
/*  404 */             Image img = ck.getImage();
/*  405 */             if (img.isScaleToFitLineWhenOverflow()) {
/*      */ 
/*      */               
/*  408 */               float scalePercent = width / img.getWidth();
/*  409 */               ck.setImageScalePercentage(scalePercent);
/*  410 */               charWidth = width;
/*      */             } 
/*      */           } 
/*      */         }
/*  414 */         if (ck.isTab()) {
/*  415 */           if (ck.isAttribute("TABSETTINGS")) {
/*  416 */             lastSplit = this.currentChar;
/*  417 */             if (tabStop != null) {
/*  418 */               width = processTabStop(tabStop, tabPosition, originalWidth, width, tabStopAnchorPosition, isRTL, rtlTabsToBeAligned);
/*      */             }
/*      */             
/*  421 */             tabStop = PdfChunk.getTabStop(ck, originalWidth - width);
/*  422 */             if (tabStop.getPosition() > originalWidth) {
/*  423 */               tabStop = null;
/*      */               break;
/*      */             } 
/*  426 */             ck.setTabStop(tabStop);
/*  427 */             if (!isRTL && tabStop.getAlignment() == TabStop.Alignment.LEFT) {
/*  428 */               width = originalWidth - tabStop.getPosition();
/*  429 */               tabStop = null;
/*  430 */               tabPosition = Float.NaN;
/*  431 */               tabStopAnchorPosition = Float.NaN;
/*      */             } else {
/*  433 */               tabPosition = originalWidth - width;
/*  434 */               tabStopAnchorPosition = Float.NaN;
/*      */             } 
/*      */           } else {
/*  437 */             Object[] tab = (Object[])ck.getAttribute("TAB");
/*      */             
/*  439 */             float tabStopPosition = ((Float)tab[1]).floatValue();
/*  440 */             boolean newLine = ((Boolean)tab[2]).booleanValue();
/*  441 */             if (newLine && tabStopPosition < originalWidth - width) {
/*  442 */               return new PdfLine(0.0F, originalWidth, width, alignment, true, createArrayOfPdfChunks(oldCurrentChar, this.currentChar - 1), isRTL);
/*      */             }
/*  444 */             this.detailChunks[this.currentChar].adjustLeft(leftX);
/*  445 */             width = originalWidth - tabStopPosition;
/*      */           }
/*      */         
/*  448 */         } else if (ck.isSeparator()) {
/*  449 */           Object[] sep = (Object[])ck.getAttribute("SEPARATOR");
/*  450 */           DrawInterface di = (DrawInterface)sep[0];
/*  451 */           Boolean vertical = (Boolean)sep[1];
/*  452 */           if (vertical.booleanValue() && di instanceof LineSeparator) {
/*  453 */             float separatorWidth = originalWidth * ((LineSeparator)di).getPercentage() / 100.0F;
/*  454 */             width -= separatorWidth;
/*  455 */             if (width < 0.0F) {
/*  456 */               width = 0.0F;
/*      */             }
/*      */           } 
/*      */         } else {
/*  460 */           boolean splitChar = ck.isExtSplitCharacter(oldCurrentChar, this.currentChar, this.totalTextLength, this.text, this.detailChunks);
/*  461 */           if (splitChar && Character.isWhitespace((char)uniC))
/*  462 */             lastSplit = this.currentChar; 
/*  463 */           if (width - charWidth < 0.0F)
/*      */             break; 
/*  465 */           if (tabStop != null && tabStop.getAlignment() == TabStop.Alignment.ANCHOR && Float.isNaN(tabStopAnchorPosition) && tabStop.getAnchorChar() == (char)uniC) {
/*  466 */             tabStopAnchorPosition = originalWidth - width;
/*      */           }
/*  468 */           width -= charWidth;
/*  469 */           if (splitChar)
/*  470 */             lastSplit = this.currentChar; 
/*      */         } 
/*  472 */         lastValidChunk = ck;
/*  473 */         if (surrogate)
/*  474 */           this.currentChar++; 
/*      */       } 
/*  476 */     }  if (lastValidChunk == null) {
/*      */       
/*  478 */       this.currentChar++;
/*  479 */       if (surrogate)
/*  480 */         this.currentChar++; 
/*  481 */       return new PdfLine(0.0F, originalWidth, 0.0F, alignment, false, createArrayOfPdfChunks(this.currentChar - 1, this.currentChar - 1), isRTL);
/*      */     } 
/*      */     
/*  484 */     if (tabStop != null) {
/*  485 */       width = processTabStop(tabStop, tabPosition, originalWidth, width, tabStopAnchorPosition, isRTL, rtlTabsToBeAligned);
/*      */     }
/*  487 */     if (rtlTabsToBeAligned != null) {
/*  488 */       for (TabStop rtlTabStop : rtlTabsToBeAligned) {
/*  489 */         rtlTabStop.setPosition(originalWidth - width - rtlTabStop.getPosition());
/*      */       }
/*      */     }
/*      */     
/*  493 */     if (this.currentChar >= this.totalTextLength)
/*      */     {
/*  495 */       return new PdfLine(0.0F, originalWidth, width, alignment, true, createArrayOfPdfChunks(oldCurrentChar, this.totalTextLength - 1), isRTL);
/*      */     }
/*  497 */     int newCurrentChar = trimRightEx(oldCurrentChar, this.currentChar - 1);
/*  498 */     if (newCurrentChar < oldCurrentChar)
/*      */     {
/*  500 */       return new PdfLine(0.0F, originalWidth, width, alignment, false, createArrayOfPdfChunks(oldCurrentChar, this.currentChar - 1), isRTL);
/*      */     }
/*  502 */     if (newCurrentChar == this.currentChar - 1) {
/*  503 */       HyphenationEvent he = (HyphenationEvent)lastValidChunk.getAttribute("HYPHENATION");
/*  504 */       if (he != null) {
/*  505 */         int[] word = getWord(oldCurrentChar, newCurrentChar);
/*  506 */         if (word != null) {
/*  507 */           float testWidth = width + getWidth(word[0], this.currentChar - 1);
/*  508 */           String pre = he.getHyphenatedWordPre(new String(this.text, word[0], word[1] - word[0]), lastValidChunk.font().getFont(), lastValidChunk.font().size(), testWidth);
/*  509 */           String post = he.getHyphenatedWordPost();
/*  510 */           if (pre.length() > 0) {
/*  511 */             PdfChunk extra = new PdfChunk(pre, lastValidChunk);
/*  512 */             this.currentChar = word[1] - post.length();
/*  513 */             return new PdfLine(0.0F, originalWidth, testWidth - lastValidChunk.width(pre), alignment, false, createArrayOfPdfChunks(oldCurrentChar, word[0] - 1, extra), isRTL);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  518 */     if (lastSplit == -1)
/*  519 */       this.isWordSplit = true; 
/*  520 */     if (lastSplit == -1 || lastSplit >= newCurrentChar)
/*      */     {
/*  522 */       return new PdfLine(0.0F, originalWidth, width + getWidth(newCurrentChar + 1, this.currentChar - 1, originalWidth), alignment, false, createArrayOfPdfChunks(oldCurrentChar, newCurrentChar), isRTL);
/*      */     }
/*      */     
/*  525 */     this.currentChar = lastSplit + 1;
/*  526 */     newCurrentChar = trimRightEx(oldCurrentChar, lastSplit);
/*  527 */     if (newCurrentChar < oldCurrentChar)
/*      */     {
/*  529 */       newCurrentChar = this.currentChar - 1;
/*      */     }
/*  531 */     return new PdfLine(0.0F, originalWidth, originalWidth - getWidth(oldCurrentChar, newCurrentChar, originalWidth), alignment, false, createArrayOfPdfChunks(oldCurrentChar, newCurrentChar), isRTL);
/*      */   }
/*      */   
/*      */   private float processTabStop(TabStop tabStop, float tabPosition, float originalWidth, float width, float tabStopAnchorPosition, boolean isRTL, List<TabStop> rtlTabsToBeAligned) {
/*  535 */     float tabStopPosition = tabStop.getPosition(tabPosition, originalWidth - width, tabStopAnchorPosition);
/*  536 */     width -= tabStopPosition - tabPosition;
/*  537 */     if (width < 0.0F) {
/*  538 */       tabStopPosition += width;
/*  539 */       width = 0.0F;
/*      */     } 
/*  541 */     if (!isRTL) {
/*  542 */       tabStop.setPosition(tabStopPosition);
/*      */     } else {
/*  544 */       tabStop.setPosition(tabPosition);
/*  545 */       rtlTabsToBeAligned.add(tabStop);
/*      */     } 
/*  547 */     return width;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWordSplit() {
/*  555 */     return this.isWordSplit;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidth(int startIdx, int lastIdx) {
/*  564 */     return getWidth(startIdx, lastIdx, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidth(int startIdx, int lastIdx, float originalWidth) {
/*  574 */     char c = Character.MIN_VALUE;
/*  575 */     PdfChunk ck = null;
/*  576 */     float width = 0.0F;
/*  577 */     TabStop tabStop = null;
/*  578 */     float tabStopAnchorPosition = Float.NaN;
/*  579 */     float tabPosition = Float.NaN;
/*  580 */     for (; startIdx <= lastIdx; startIdx++) {
/*  581 */       boolean surrogate = Utilities.isSurrogatePair(this.text, startIdx);
/*  582 */       if (this.detailChunks[startIdx].isTab() && this.detailChunks[startIdx]
/*      */         
/*  584 */         .isAttribute("TABSETTINGS")) {
/*  585 */         if (tabStop != null) {
/*  586 */           float tabStopPosition = tabStop.getPosition(tabPosition, width, tabStopAnchorPosition);
/*  587 */           width = tabStopPosition + width - tabPosition;
/*  588 */           tabStop.setPosition(tabStopPosition);
/*      */         } 
/*  590 */         tabStop = this.detailChunks[startIdx].getTabStop();
/*  591 */         if (tabStop == null) {
/*  592 */           tabStop = PdfChunk.getTabStop(this.detailChunks[startIdx], width);
/*  593 */           tabPosition = width;
/*  594 */           tabStopAnchorPosition = Float.NaN;
/*      */         } else {
/*  596 */           if (this.runDirection == 3) {
/*  597 */             width = originalWidth - tabStop.getPosition();
/*      */           } else {
/*  599 */             width = tabStop.getPosition();
/*      */           } 
/*  601 */           tabStop = null;
/*  602 */           tabPosition = Float.NaN;
/*  603 */           tabStopAnchorPosition = Float.NaN;
/*      */         } 
/*  605 */       } else if (surrogate) {
/*  606 */         width += this.detailChunks[startIdx].getCharWidth(Utilities.convertToUtf32(this.text, startIdx));
/*  607 */         startIdx++;
/*      */       } else {
/*      */         
/*  610 */         c = this.text[startIdx];
/*  611 */         ck = this.detailChunks[startIdx];
/*  612 */         if (!PdfChunk.noPrint(ck.getUnicodeEquivalent(c))) {
/*      */           
/*  614 */           if (tabStop != null && tabStop.getAlignment() != TabStop.Alignment.ANCHOR && Float.isNaN(tabStopAnchorPosition) && tabStop.getAnchorChar() == (char)ck.getUnicodeEquivalent(c)) {
/*  615 */             tabStopAnchorPosition = width;
/*      */           }
/*  617 */           width += this.detailChunks[startIdx].getCharWidth(c);
/*      */         } 
/*      */       } 
/*  620 */     }  if (tabStop != null) {
/*  621 */       float tabStopPosition = tabStop.getPosition(tabPosition, width, tabStopAnchorPosition);
/*  622 */       width = tabStopPosition + width - tabPosition;
/*  623 */       tabStop.setPosition(tabStopPosition);
/*      */     } 
/*  625 */     return width;
/*      */   }
/*      */   
/*      */   public ArrayList<PdfChunk> createArrayOfPdfChunks(int startIdx, int endIdx) {
/*  629 */     return createArrayOfPdfChunks(startIdx, endIdx, null);
/*      */   }
/*      */   
/*      */   public ArrayList<PdfChunk> createArrayOfPdfChunks(int startIdx, int endIdx, PdfChunk extraPdfChunk) {
/*  633 */     boolean bidi = (this.runDirection != 1);
/*  634 */     if (bidi) {
/*  635 */       reorder(startIdx, endIdx);
/*      */     }
/*  637 */     ArrayList<PdfChunk> ar = new ArrayList<PdfChunk>();
/*  638 */     PdfChunk refCk = this.detailChunks[startIdx];
/*  639 */     PdfChunk ck = null;
/*  640 */     StringBuffer buf = new StringBuffer();
/*      */     
/*  642 */     int idx = 0;
/*  643 */     for (; startIdx <= endIdx; startIdx++) {
/*  644 */       idx = bidi ? this.indexChars[startIdx] : startIdx;
/*  645 */       char c = this.text[idx];
/*  646 */       ck = this.detailChunks[idx];
/*  647 */       if (!PdfChunk.noPrint(ck.getUnicodeEquivalent(c)))
/*      */       {
/*  649 */         if (ck.isImage() || ck.isSeparator() || ck.isTab()) {
/*  650 */           if (buf.length() > 0) {
/*  651 */             ar.add(new PdfChunk(buf.toString(), refCk));
/*  652 */             buf = new StringBuffer();
/*      */           } 
/*  654 */           ar.add(ck);
/*      */         }
/*  656 */         else if (ck == refCk) {
/*  657 */           buf.append(c);
/*      */         } else {
/*      */           
/*  660 */           if (buf.length() > 0) {
/*  661 */             ar.add(new PdfChunk(buf.toString(), refCk));
/*  662 */             buf = new StringBuffer();
/*      */           } 
/*  664 */           if (!ck.isImage() && !ck.isSeparator() && !ck.isTab())
/*  665 */             buf.append(c); 
/*  666 */           refCk = ck;
/*      */         }  } 
/*      */     } 
/*  669 */     if (buf.length() > 0) {
/*  670 */       ar.add(new PdfChunk(buf.toString(), refCk));
/*      */     }
/*  672 */     if (extraPdfChunk != null)
/*  673 */       ar.add(extraPdfChunk); 
/*  674 */     return ar;
/*      */   }
/*      */   
/*      */   public int[] getWord(int startIdx, int idx) {
/*  678 */     int last = idx;
/*  679 */     int first = idx;
/*      */     
/*  681 */     for (; last < this.totalTextLength && (
/*  682 */       Character.isLetter(this.text[last]) || Character.isDigit(this.text[last]) || this.text[last] == '­'); last++);
/*      */ 
/*      */     
/*  685 */     if (last == idx) {
/*  686 */       return null;
/*      */     }
/*  688 */     for (; first >= startIdx && (
/*  689 */       Character.isLetter(this.text[first]) || Character.isDigit(this.text[first]) || this.text[first] == '­'); first--);
/*      */ 
/*      */     
/*  692 */     first++;
/*  693 */     return new int[] { first, last };
/*      */   }
/*      */   
/*      */   public int trimRight(int startIdx, int endIdx) {
/*  697 */     int idx = endIdx;
/*      */     
/*  699 */     for (; idx >= startIdx; idx--) {
/*  700 */       char c = (char)this.detailChunks[idx].getUnicodeEquivalent(this.text[idx]);
/*  701 */       if (!isWS(c))
/*      */         break; 
/*      */     } 
/*  704 */     return idx;
/*      */   }
/*      */   
/*      */   public int trimLeft(int startIdx, int endIdx) {
/*  708 */     int idx = startIdx;
/*      */     
/*  710 */     for (; idx <= endIdx; idx++) {
/*  711 */       char c = (char)this.detailChunks[idx].getUnicodeEquivalent(this.text[idx]);
/*  712 */       if (!isWS(c))
/*      */         break; 
/*      */     } 
/*  715 */     return idx;
/*      */   }
/*      */   
/*      */   public int trimRightEx(int startIdx, int endIdx) {
/*  719 */     int idx = endIdx;
/*  720 */     char c = Character.MIN_VALUE;
/*  721 */     for (; idx >= startIdx; idx--) {
/*  722 */       c = (char)this.detailChunks[idx].getUnicodeEquivalent(this.text[idx]);
/*  723 */       if (!isWS(c) && !PdfChunk.noPrint(c)) {
/*  724 */         if (this.detailChunks[idx].isTab() && this.detailChunks[idx]
/*      */           
/*  726 */           .isAttribute("TABSETTINGS")) {
/*  727 */           Object[] tab = (Object[])this.detailChunks[idx].getAttribute("TAB");
/*  728 */           boolean isWhitespace = ((Boolean)tab[1]).booleanValue();
/*  729 */           if (isWhitespace)
/*      */             continue; 
/*      */         }  break;
/*      */       } 
/*      */       continue;
/*      */     } 
/*  735 */     return idx;
/*      */   }
/*      */   
/*      */   public int trimLeftEx(int startIdx, int endIdx) {
/*  739 */     int idx = startIdx;
/*  740 */     char c = Character.MIN_VALUE;
/*  741 */     for (; idx <= endIdx; idx++) {
/*  742 */       c = (char)this.detailChunks[idx].getUnicodeEquivalent(this.text[idx]);
/*  743 */       if (!isWS(c) && !PdfChunk.noPrint(c)) {
/*  744 */         if (this.detailChunks[idx].isTab() && this.detailChunks[idx]
/*      */           
/*  746 */           .isAttribute("TABSETTINGS")) {
/*  747 */           Object[] tab = (Object[])this.detailChunks[idx].getAttribute("TAB");
/*  748 */           boolean isWhitespace = ((Boolean)tab[1]).booleanValue();
/*  749 */           if (isWhitespace)
/*      */             continue; 
/*      */         }  break;
/*      */       } 
/*      */       continue;
/*      */     } 
/*  755 */     return idx;
/*      */   }
/*      */   
/*      */   public void reorder(int start, int end) {
/*  759 */     byte maxLevel = this.orderLevels[start];
/*  760 */     byte minLevel = maxLevel;
/*  761 */     byte onlyOddLevels = maxLevel;
/*  762 */     byte onlyEvenLevels = maxLevel;
/*  763 */     for (int k = start + 1; k <= end; k++) {
/*  764 */       byte b = this.orderLevels[k];
/*  765 */       if (b > maxLevel) {
/*  766 */         maxLevel = b;
/*  767 */       } else if (b < minLevel) {
/*  768 */         minLevel = b;
/*  769 */       }  onlyOddLevels = (byte)(onlyOddLevels & b);
/*  770 */       onlyEvenLevels = (byte)(onlyEvenLevels | b);
/*      */     } 
/*  772 */     if ((onlyEvenLevels & 0x1) == 0)
/*      */       return; 
/*  774 */     if ((onlyOddLevels & 0x1) == 1) {
/*  775 */       flip(start, end + 1);
/*      */       return;
/*      */     } 
/*  778 */     minLevel = (byte)(minLevel | 0x1);
/*  779 */     while (maxLevel >= minLevel) {
/*  780 */       int pstart = start;
/*      */       while (true) {
/*  782 */         if (pstart > end || 
/*  783 */           this.orderLevels[pstart] >= maxLevel) {
/*      */ 
/*      */           
/*  786 */           if (pstart > end) {
/*      */             maxLevel = (byte)(maxLevel - 1); continue;
/*  788 */           }  int pend = pstart + 1;
/*  789 */           for (; pend <= end && 
/*  790 */             this.orderLevels[pend] >= maxLevel; pend++);
/*      */ 
/*      */           
/*  793 */           flip(pstart, pend);
/*  794 */           pstart = pend + 1;
/*      */           continue;
/*      */         } 
/*      */         pstart++;
/*      */       } 
/*      */     }  } public void flip(int start, int end) {
/*  800 */     int mid = (start + end) / 2;
/*  801 */     end--;
/*  802 */     for (; start < mid; start++, end--) {
/*  803 */       int temp = this.indexChars[start];
/*  804 */       this.indexChars[start] = this.indexChars[end];
/*  805 */       this.indexChars[end] = temp;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean isWS(char c) {
/*  810 */     return (c <= ' ');
/*      */   }
/*      */   
/*      */   static {
/*  814 */     mirrorChars.put(40, 41);
/*  815 */     mirrorChars.put(41, 40);
/*  816 */     mirrorChars.put(60, 62);
/*  817 */     mirrorChars.put(62, 60);
/*  818 */     mirrorChars.put(91, 93);
/*  819 */     mirrorChars.put(93, 91);
/*  820 */     mirrorChars.put(123, 125);
/*  821 */     mirrorChars.put(125, 123);
/*  822 */     mirrorChars.put(171, 187);
/*  823 */     mirrorChars.put(187, 171);
/*  824 */     mirrorChars.put(8249, 8250);
/*  825 */     mirrorChars.put(8250, 8249);
/*  826 */     mirrorChars.put(8261, 8262);
/*  827 */     mirrorChars.put(8262, 8261);
/*  828 */     mirrorChars.put(8317, 8318);
/*  829 */     mirrorChars.put(8318, 8317);
/*  830 */     mirrorChars.put(8333, 8334);
/*  831 */     mirrorChars.put(8334, 8333);
/*  832 */     mirrorChars.put(8712, 8715);
/*  833 */     mirrorChars.put(8713, 8716);
/*  834 */     mirrorChars.put(8714, 8717);
/*  835 */     mirrorChars.put(8715, 8712);
/*  836 */     mirrorChars.put(8716, 8713);
/*  837 */     mirrorChars.put(8717, 8714);
/*  838 */     mirrorChars.put(8725, 10741);
/*  839 */     mirrorChars.put(8764, 8765);
/*  840 */     mirrorChars.put(8765, 8764);
/*  841 */     mirrorChars.put(8771, 8909);
/*  842 */     mirrorChars.put(8786, 8787);
/*  843 */     mirrorChars.put(8787, 8786);
/*  844 */     mirrorChars.put(8788, 8789);
/*  845 */     mirrorChars.put(8789, 8788);
/*  846 */     mirrorChars.put(8804, 8805);
/*  847 */     mirrorChars.put(8805, 8804);
/*  848 */     mirrorChars.put(8806, 8807);
/*  849 */     mirrorChars.put(8807, 8806);
/*  850 */     mirrorChars.put(8808, 8809);
/*  851 */     mirrorChars.put(8809, 8808);
/*  852 */     mirrorChars.put(8810, 8811);
/*  853 */     mirrorChars.put(8811, 8810);
/*  854 */     mirrorChars.put(8814, 8815);
/*  855 */     mirrorChars.put(8815, 8814);
/*  856 */     mirrorChars.put(8816, 8817);
/*  857 */     mirrorChars.put(8817, 8816);
/*  858 */     mirrorChars.put(8818, 8819);
/*  859 */     mirrorChars.put(8819, 8818);
/*  860 */     mirrorChars.put(8820, 8821);
/*  861 */     mirrorChars.put(8821, 8820);
/*  862 */     mirrorChars.put(8822, 8823);
/*  863 */     mirrorChars.put(8823, 8822);
/*  864 */     mirrorChars.put(8824, 8825);
/*  865 */     mirrorChars.put(8825, 8824);
/*  866 */     mirrorChars.put(8826, 8827);
/*  867 */     mirrorChars.put(8827, 8826);
/*  868 */     mirrorChars.put(8828, 8829);
/*  869 */     mirrorChars.put(8829, 8828);
/*  870 */     mirrorChars.put(8830, 8831);
/*  871 */     mirrorChars.put(8831, 8830);
/*  872 */     mirrorChars.put(8832, 8833);
/*  873 */     mirrorChars.put(8833, 8832);
/*  874 */     mirrorChars.put(8834, 8835);
/*  875 */     mirrorChars.put(8835, 8834);
/*  876 */     mirrorChars.put(8836, 8837);
/*  877 */     mirrorChars.put(8837, 8836);
/*  878 */     mirrorChars.put(8838, 8839);
/*  879 */     mirrorChars.put(8839, 8838);
/*  880 */     mirrorChars.put(8840, 8841);
/*  881 */     mirrorChars.put(8841, 8840);
/*  882 */     mirrorChars.put(8842, 8843);
/*  883 */     mirrorChars.put(8843, 8842);
/*  884 */     mirrorChars.put(8847, 8848);
/*  885 */     mirrorChars.put(8848, 8847);
/*  886 */     mirrorChars.put(8849, 8850);
/*  887 */     mirrorChars.put(8850, 8849);
/*  888 */     mirrorChars.put(8856, 10680);
/*  889 */     mirrorChars.put(8866, 8867);
/*  890 */     mirrorChars.put(8867, 8866);
/*  891 */     mirrorChars.put(8870, 10974);
/*  892 */     mirrorChars.put(8872, 10980);
/*  893 */     mirrorChars.put(8873, 10979);
/*  894 */     mirrorChars.put(8875, 10981);
/*  895 */     mirrorChars.put(8880, 8881);
/*  896 */     mirrorChars.put(8881, 8880);
/*  897 */     mirrorChars.put(8882, 8883);
/*  898 */     mirrorChars.put(8883, 8882);
/*  899 */     mirrorChars.put(8884, 8885);
/*  900 */     mirrorChars.put(8885, 8884);
/*  901 */     mirrorChars.put(8886, 8887);
/*  902 */     mirrorChars.put(8887, 8886);
/*  903 */     mirrorChars.put(8905, 8906);
/*  904 */     mirrorChars.put(8906, 8905);
/*  905 */     mirrorChars.put(8907, 8908);
/*  906 */     mirrorChars.put(8908, 8907);
/*  907 */     mirrorChars.put(8909, 8771);
/*  908 */     mirrorChars.put(8912, 8913);
/*  909 */     mirrorChars.put(8913, 8912);
/*  910 */     mirrorChars.put(8918, 8919);
/*  911 */     mirrorChars.put(8919, 8918);
/*  912 */     mirrorChars.put(8920, 8921);
/*  913 */     mirrorChars.put(8921, 8920);
/*  914 */     mirrorChars.put(8922, 8923);
/*  915 */     mirrorChars.put(8923, 8922);
/*  916 */     mirrorChars.put(8924, 8925);
/*  917 */     mirrorChars.put(8925, 8924);
/*  918 */     mirrorChars.put(8926, 8927);
/*  919 */     mirrorChars.put(8927, 8926);
/*  920 */     mirrorChars.put(8928, 8929);
/*  921 */     mirrorChars.put(8929, 8928);
/*  922 */     mirrorChars.put(8930, 8931);
/*  923 */     mirrorChars.put(8931, 8930);
/*  924 */     mirrorChars.put(8932, 8933);
/*  925 */     mirrorChars.put(8933, 8932);
/*  926 */     mirrorChars.put(8934, 8935);
/*  927 */     mirrorChars.put(8935, 8934);
/*  928 */     mirrorChars.put(8936, 8937);
/*  929 */     mirrorChars.put(8937, 8936);
/*  930 */     mirrorChars.put(8938, 8939);
/*  931 */     mirrorChars.put(8939, 8938);
/*  932 */     mirrorChars.put(8940, 8941);
/*  933 */     mirrorChars.put(8941, 8940);
/*  934 */     mirrorChars.put(8944, 8945);
/*  935 */     mirrorChars.put(8945, 8944);
/*  936 */     mirrorChars.put(8946, 8954);
/*  937 */     mirrorChars.put(8947, 8955);
/*  938 */     mirrorChars.put(8948, 8956);
/*  939 */     mirrorChars.put(8950, 8957);
/*  940 */     mirrorChars.put(8951, 8958);
/*  941 */     mirrorChars.put(8954, 8946);
/*  942 */     mirrorChars.put(8955, 8947);
/*  943 */     mirrorChars.put(8956, 8948);
/*  944 */     mirrorChars.put(8957, 8950);
/*  945 */     mirrorChars.put(8958, 8951);
/*  946 */     mirrorChars.put(8968, 8969);
/*  947 */     mirrorChars.put(8969, 8968);
/*  948 */     mirrorChars.put(8970, 8971);
/*  949 */     mirrorChars.put(8971, 8970);
/*  950 */     mirrorChars.put(9001, 9002);
/*  951 */     mirrorChars.put(9002, 9001);
/*  952 */     mirrorChars.put(10088, 10089);
/*  953 */     mirrorChars.put(10089, 10088);
/*  954 */     mirrorChars.put(10090, 10091);
/*  955 */     mirrorChars.put(10091, 10090);
/*  956 */     mirrorChars.put(10092, 10093);
/*  957 */     mirrorChars.put(10093, 10092);
/*  958 */     mirrorChars.put(10094, 10095);
/*  959 */     mirrorChars.put(10095, 10094);
/*  960 */     mirrorChars.put(10096, 10097);
/*  961 */     mirrorChars.put(10097, 10096);
/*  962 */     mirrorChars.put(10098, 10099);
/*  963 */     mirrorChars.put(10099, 10098);
/*  964 */     mirrorChars.put(10100, 10101);
/*  965 */     mirrorChars.put(10101, 10100);
/*  966 */     mirrorChars.put(10197, 10198);
/*  967 */     mirrorChars.put(10198, 10197);
/*  968 */     mirrorChars.put(10205, 10206);
/*  969 */     mirrorChars.put(10206, 10205);
/*  970 */     mirrorChars.put(10210, 10211);
/*  971 */     mirrorChars.put(10211, 10210);
/*  972 */     mirrorChars.put(10212, 10213);
/*  973 */     mirrorChars.put(10213, 10212);
/*  974 */     mirrorChars.put(10214, 10215);
/*  975 */     mirrorChars.put(10215, 10214);
/*  976 */     mirrorChars.put(10216, 10217);
/*  977 */     mirrorChars.put(10217, 10216);
/*  978 */     mirrorChars.put(10218, 10219);
/*  979 */     mirrorChars.put(10219, 10218);
/*  980 */     mirrorChars.put(10627, 10628);
/*  981 */     mirrorChars.put(10628, 10627);
/*  982 */     mirrorChars.put(10629, 10630);
/*  983 */     mirrorChars.put(10630, 10629);
/*  984 */     mirrorChars.put(10631, 10632);
/*  985 */     mirrorChars.put(10632, 10631);
/*  986 */     mirrorChars.put(10633, 10634);
/*  987 */     mirrorChars.put(10634, 10633);
/*  988 */     mirrorChars.put(10635, 10636);
/*  989 */     mirrorChars.put(10636, 10635);
/*  990 */     mirrorChars.put(10637, 10640);
/*  991 */     mirrorChars.put(10638, 10639);
/*  992 */     mirrorChars.put(10639, 10638);
/*  993 */     mirrorChars.put(10640, 10637);
/*  994 */     mirrorChars.put(10641, 10642);
/*  995 */     mirrorChars.put(10642, 10641);
/*  996 */     mirrorChars.put(10643, 10644);
/*  997 */     mirrorChars.put(10644, 10643);
/*  998 */     mirrorChars.put(10645, 10646);
/*  999 */     mirrorChars.put(10646, 10645);
/* 1000 */     mirrorChars.put(10647, 10648);
/* 1001 */     mirrorChars.put(10648, 10647);
/* 1002 */     mirrorChars.put(10680, 8856);
/* 1003 */     mirrorChars.put(10688, 10689);
/* 1004 */     mirrorChars.put(10689, 10688);
/* 1005 */     mirrorChars.put(10692, 10693);
/* 1006 */     mirrorChars.put(10693, 10692);
/* 1007 */     mirrorChars.put(10703, 10704);
/* 1008 */     mirrorChars.put(10704, 10703);
/* 1009 */     mirrorChars.put(10705, 10706);
/* 1010 */     mirrorChars.put(10706, 10705);
/* 1011 */     mirrorChars.put(10708, 10709);
/* 1012 */     mirrorChars.put(10709, 10708);
/* 1013 */     mirrorChars.put(10712, 10713);
/* 1014 */     mirrorChars.put(10713, 10712);
/* 1015 */     mirrorChars.put(10714, 10715);
/* 1016 */     mirrorChars.put(10715, 10714);
/* 1017 */     mirrorChars.put(10741, 8725);
/* 1018 */     mirrorChars.put(10744, 10745);
/* 1019 */     mirrorChars.put(10745, 10744);
/* 1020 */     mirrorChars.put(10748, 10749);
/* 1021 */     mirrorChars.put(10749, 10748);
/* 1022 */     mirrorChars.put(10795, 10796);
/* 1023 */     mirrorChars.put(10796, 10795);
/* 1024 */     mirrorChars.put(10797, 10796);
/* 1025 */     mirrorChars.put(10798, 10797);
/* 1026 */     mirrorChars.put(10804, 10805);
/* 1027 */     mirrorChars.put(10805, 10804);
/* 1028 */     mirrorChars.put(10812, 10813);
/* 1029 */     mirrorChars.put(10813, 10812);
/* 1030 */     mirrorChars.put(10852, 10853);
/* 1031 */     mirrorChars.put(10853, 10852);
/* 1032 */     mirrorChars.put(10873, 10874);
/* 1033 */     mirrorChars.put(10874, 10873);
/* 1034 */     mirrorChars.put(10877, 10878);
/* 1035 */     mirrorChars.put(10878, 10877);
/* 1036 */     mirrorChars.put(10879, 10880);
/* 1037 */     mirrorChars.put(10880, 10879);
/* 1038 */     mirrorChars.put(10881, 10882);
/* 1039 */     mirrorChars.put(10882, 10881);
/* 1040 */     mirrorChars.put(10883, 10884);
/* 1041 */     mirrorChars.put(10884, 10883);
/* 1042 */     mirrorChars.put(10891, 10892);
/* 1043 */     mirrorChars.put(10892, 10891);
/* 1044 */     mirrorChars.put(10897, 10898);
/* 1045 */     mirrorChars.put(10898, 10897);
/* 1046 */     mirrorChars.put(10899, 10900);
/* 1047 */     mirrorChars.put(10900, 10899);
/* 1048 */     mirrorChars.put(10901, 10902);
/* 1049 */     mirrorChars.put(10902, 10901);
/* 1050 */     mirrorChars.put(10903, 10904);
/* 1051 */     mirrorChars.put(10904, 10903);
/* 1052 */     mirrorChars.put(10905, 10906);
/* 1053 */     mirrorChars.put(10906, 10905);
/* 1054 */     mirrorChars.put(10907, 10908);
/* 1055 */     mirrorChars.put(10908, 10907);
/* 1056 */     mirrorChars.put(10913, 10914);
/* 1057 */     mirrorChars.put(10914, 10913);
/* 1058 */     mirrorChars.put(10918, 10919);
/* 1059 */     mirrorChars.put(10919, 10918);
/* 1060 */     mirrorChars.put(10920, 10921);
/* 1061 */     mirrorChars.put(10921, 10920);
/* 1062 */     mirrorChars.put(10922, 10923);
/* 1063 */     mirrorChars.put(10923, 10922);
/* 1064 */     mirrorChars.put(10924, 10925);
/* 1065 */     mirrorChars.put(10925, 10924);
/* 1066 */     mirrorChars.put(10927, 10928);
/* 1067 */     mirrorChars.put(10928, 10927);
/* 1068 */     mirrorChars.put(10931, 10932);
/* 1069 */     mirrorChars.put(10932, 10931);
/* 1070 */     mirrorChars.put(10939, 10940);
/* 1071 */     mirrorChars.put(10940, 10939);
/* 1072 */     mirrorChars.put(10941, 10942);
/* 1073 */     mirrorChars.put(10942, 10941);
/* 1074 */     mirrorChars.put(10943, 10944);
/* 1075 */     mirrorChars.put(10944, 10943);
/* 1076 */     mirrorChars.put(10945, 10946);
/* 1077 */     mirrorChars.put(10946, 10945);
/* 1078 */     mirrorChars.put(10947, 10948);
/* 1079 */     mirrorChars.put(10948, 10947);
/* 1080 */     mirrorChars.put(10949, 10950);
/* 1081 */     mirrorChars.put(10950, 10949);
/* 1082 */     mirrorChars.put(10957, 10958);
/* 1083 */     mirrorChars.put(10958, 10957);
/* 1084 */     mirrorChars.put(10959, 10960);
/* 1085 */     mirrorChars.put(10960, 10959);
/* 1086 */     mirrorChars.put(10961, 10962);
/* 1087 */     mirrorChars.put(10962, 10961);
/* 1088 */     mirrorChars.put(10963, 10964);
/* 1089 */     mirrorChars.put(10964, 10963);
/* 1090 */     mirrorChars.put(10965, 10966);
/* 1091 */     mirrorChars.put(10966, 10965);
/* 1092 */     mirrorChars.put(10974, 8870);
/* 1093 */     mirrorChars.put(10979, 8873);
/* 1094 */     mirrorChars.put(10980, 8872);
/* 1095 */     mirrorChars.put(10981, 8875);
/* 1096 */     mirrorChars.put(10988, 10989);
/* 1097 */     mirrorChars.put(10989, 10988);
/* 1098 */     mirrorChars.put(10999, 11000);
/* 1099 */     mirrorChars.put(11000, 10999);
/* 1100 */     mirrorChars.put(11001, 11002);
/* 1101 */     mirrorChars.put(11002, 11001);
/* 1102 */     mirrorChars.put(12296, 12297);
/* 1103 */     mirrorChars.put(12297, 12296);
/* 1104 */     mirrorChars.put(12298, 12299);
/* 1105 */     mirrorChars.put(12299, 12298);
/* 1106 */     mirrorChars.put(12300, 12301);
/* 1107 */     mirrorChars.put(12301, 12300);
/* 1108 */     mirrorChars.put(12302, 12303);
/* 1109 */     mirrorChars.put(12303, 12302);
/* 1110 */     mirrorChars.put(12304, 12305);
/* 1111 */     mirrorChars.put(12305, 12304);
/* 1112 */     mirrorChars.put(12308, 12309);
/* 1113 */     mirrorChars.put(12309, 12308);
/* 1114 */     mirrorChars.put(12310, 12311);
/* 1115 */     mirrorChars.put(12311, 12310);
/* 1116 */     mirrorChars.put(12312, 12313);
/* 1117 */     mirrorChars.put(12313, 12312);
/* 1118 */     mirrorChars.put(12314, 12315);
/* 1119 */     mirrorChars.put(12315, 12314);
/* 1120 */     mirrorChars.put(65288, 65289);
/* 1121 */     mirrorChars.put(65289, 65288);
/* 1122 */     mirrorChars.put(65308, 65310);
/* 1123 */     mirrorChars.put(65310, 65308);
/* 1124 */     mirrorChars.put(65339, 65341);
/* 1125 */     mirrorChars.put(65341, 65339);
/* 1126 */     mirrorChars.put(65371, 65373);
/* 1127 */     mirrorChars.put(65373, 65371);
/* 1128 */     mirrorChars.put(65375, 65376);
/* 1129 */     mirrorChars.put(65376, 65375);
/* 1130 */     mirrorChars.put(65378, 65379);
/* 1131 */     mirrorChars.put(65379, 65378);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String processLTR(String s, int runDirection, int arabicOptions) {
/* 1142 */     BidiLine bidi = new BidiLine();
/* 1143 */     bidi.addChunk(new PdfChunk(new Chunk(s), null));
/* 1144 */     bidi.arabicOptions = arabicOptions;
/* 1145 */     bidi.getParagraph(runDirection);
/* 1146 */     ArrayList<PdfChunk> arr = bidi.createArrayOfPdfChunks(0, bidi.totalTextLength - 1);
/* 1147 */     StringBuilder sb = new StringBuilder();
/* 1148 */     for (PdfChunk ck : arr) {
/* 1149 */       sb.append(ck.toString());
/*      */     }
/* 1151 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public BidiLine() {}
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BidiLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */