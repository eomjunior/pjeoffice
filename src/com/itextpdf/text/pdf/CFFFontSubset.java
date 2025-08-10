/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CFFFontSubset
/*      */   extends CFFFont
/*      */ {
/*   68 */   static final String[] SubrsFunctions = new String[] { "RESERVED_0", "hstem", "RESERVED_2", "vstem", "vmoveto", "rlineto", "hlineto", "vlineto", "rrcurveto", "RESERVED_9", "callsubr", "return", "escape", "RESERVED_13", "endchar", "RESERVED_15", "RESERVED_16", "RESERVED_17", "hstemhm", "hintmask", "cntrmask", "rmoveto", "hmoveto", "vstemhm", "rcurveline", "rlinecurve", "vvcurveto", "hhcurveto", "shortint", "callgsubr", "vhcurveto", "hvcurveto" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   78 */   static final String[] SubrsEscapeFuncs = new String[] { "RESERVED_0", "RESERVED_1", "RESERVED_2", "and", "or", "not", "RESERVED_6", "RESERVED_7", "RESERVED_8", "abs", "add", "sub", "div", "RESERVED_13", "neg", "eq", "RESERVED_16", "RESERVED_17", "drop", "RESERVED_19", "put", "get", "ifelse", "random", "mul", "RESERVED_25", "sqrt", "dup", "exch", "index", "roll", "RESERVED_31", "RESERVED_32", "RESERVED_33", "hflex", "flex", "hflex1", "flex1", "RESERVED_REST" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final byte ENDCHAR_OP = 14;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final byte RETURN_OP = 11;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   HashMap<Integer, int[]> GlyphsUsed;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ArrayList<Integer> glyphsInList;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   HashSet<Integer> FDArrayUsed = new HashSet<Integer>();
/*      */ 
/*      */ 
/*      */   
/*      */   HashMap<Integer, int[]>[] hSubrsUsed;
/*      */ 
/*      */ 
/*      */   
/*      */   ArrayList<Integer>[] lSubrsUsed;
/*      */ 
/*      */ 
/*      */   
/*  116 */   HashMap<Integer, int[]> hGSubrsUsed = (HashMap)new HashMap<Integer, int>();
/*      */ 
/*      */ 
/*      */   
/*  120 */   ArrayList<Integer> lGSubrsUsed = new ArrayList<Integer>();
/*      */ 
/*      */ 
/*      */   
/*  124 */   HashMap<Integer, int[]> hSubrsUsedNonCID = (HashMap)new HashMap<Integer, int>();
/*      */ 
/*      */ 
/*      */   
/*  128 */   ArrayList<Integer> lSubrsUsedNonCID = new ArrayList<Integer>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   byte[][] NewLSubrsIndex;
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] NewSubrsIndexNonCID;
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] NewGSubrsIndex;
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] NewCharStringsIndex;
/*      */ 
/*      */ 
/*      */   
/*  149 */   int GBias = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   LinkedList<CFFFont.Item> OutputList;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  159 */   int NumOfHints = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CFFFontSubset(RandomAccessFileOrArray rf, HashMap<Integer, int[]> GlyphsUsed) {
/*  169 */     super(rf);
/*  170 */     this.GlyphsUsed = GlyphsUsed;
/*      */     
/*  172 */     this.glyphsInList = new ArrayList<Integer>(GlyphsUsed.keySet());
/*      */ 
/*      */     
/*  175 */     for (int i = 0; i < this.fonts.length; i++) {
/*      */ 
/*      */       
/*  178 */       seek((this.fonts[i]).charstringsOffset);
/*  179 */       (this.fonts[i]).nglyphs = getCard16();
/*      */ 
/*      */       
/*  182 */       seek(this.stringIndexOffset);
/*  183 */       (this.fonts[i]).nstrings = getCard16() + standardStrings.length;
/*      */ 
/*      */       
/*  186 */       (this.fonts[i]).charstringsOffsets = getIndex((this.fonts[i]).charstringsOffset);
/*      */ 
/*      */       
/*  189 */       if ((this.fonts[i]).fdselectOffset >= 0) {
/*      */ 
/*      */         
/*  192 */         readFDSelect(i);
/*      */         
/*  194 */         BuildFDArrayUsed(i);
/*      */       } 
/*  196 */       if ((this.fonts[i]).isCID)
/*      */       {
/*  198 */         ReadFDArray(i);
/*      */       }
/*  200 */       (this.fonts[i]).CharsetLength = CountCharset((this.fonts[i]).charsetOffset, (this.fonts[i]).nglyphs);
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
/*      */   int CountCharset(int Offset, int NumofGlyphs) {
/*  212 */     int Length = 0;
/*  213 */     seek(Offset);
/*      */     
/*  215 */     int format = getCard8();
/*      */     
/*  217 */     switch (format) {
/*      */       case 0:
/*  219 */         Length = 1 + 2 * NumofGlyphs;
/*      */         break;
/*      */       case 1:
/*  222 */         Length = 1 + 3 * CountRange(NumofGlyphs, 1);
/*      */         break;
/*      */       case 2:
/*  225 */         Length = 1 + 4 * CountRange(NumofGlyphs, 2);
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  230 */     return Length;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int CountRange(int NumofGlyphs, int Type) {
/*  240 */     int num = 0;
/*      */     
/*  242 */     int i = 1;
/*  243 */     while (i < NumofGlyphs) {
/*  244 */       int nLeft; num++;
/*  245 */       char Sid = getCard16();
/*  246 */       if (Type == 1) {
/*  247 */         nLeft = getCard8();
/*      */       } else {
/*  249 */         nLeft = getCard16();
/*  250 */       }  i += nLeft + 1;
/*      */     } 
/*  252 */     return num;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void readFDSelect(int Font) {
/*  263 */     int i, nRanges, l, first, j, NumOfGlyphs = (this.fonts[Font]).nglyphs;
/*  264 */     int[] FDSelect = new int[NumOfGlyphs];
/*      */     
/*  266 */     seek((this.fonts[Font]).fdselectOffset);
/*      */     
/*  268 */     (this.fonts[Font]).FDSelectFormat = getCard8();
/*      */     
/*  270 */     switch ((this.fonts[Font]).FDSelectFormat) {
/*      */ 
/*      */       
/*      */       case 0:
/*  274 */         for (i = 0; i < NumOfGlyphs; i++)
/*      */         {
/*  276 */           FDSelect[i] = getCard8();
/*      */         }
/*      */ 
/*      */         
/*  280 */         (this.fonts[Font]).FDSelectLength = (this.fonts[Font]).nglyphs + 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  285 */         nRanges = getCard16();
/*  286 */         l = 0;
/*      */         
/*  288 */         first = getCard16();
/*  289 */         for (j = 0; j < nRanges; j++) {
/*      */ 
/*      */           
/*  292 */           int fd = getCard8();
/*      */           
/*  294 */           int last = getCard16();
/*      */           
/*  296 */           int steps = last - first;
/*  297 */           for (int k = 0; k < steps; k++) {
/*      */             
/*  299 */             FDSelect[l] = fd;
/*  300 */             l++;
/*      */           } 
/*      */           
/*  303 */           first = last;
/*      */         } 
/*      */         
/*  306 */         (this.fonts[Font]).FDSelectLength = 3 + nRanges * 3 + 2;
/*      */         break;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  312 */     (this.fonts[Font]).FDSelect = FDSelect;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void BuildFDArrayUsed(int Font) {
/*  321 */     int[] FDSelect = (this.fonts[Font]).FDSelect;
/*      */     
/*  323 */     for (int i = 0; i < this.glyphsInList.size(); i++) {
/*      */ 
/*      */       
/*  326 */       int glyph = ((Integer)this.glyphsInList.get(i)).intValue();
/*      */       
/*  328 */       int FD = FDSelect[glyph];
/*      */       
/*  330 */       this.FDArrayUsed.add(Integer.valueOf(FD));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ReadFDArray(int Font) {
/*  340 */     seek((this.fonts[Font]).fdarrayOffset);
/*  341 */     (this.fonts[Font]).FDArrayCount = getCard16();
/*  342 */     (this.fonts[Font]).FDArrayOffsize = getCard8();
/*      */ 
/*      */     
/*  345 */     if ((this.fonts[Font]).FDArrayOffsize < 4)
/*  346 */       (this.fonts[Font]).FDArrayOffsize++; 
/*  347 */     (this.fonts[Font]).FDArrayOffsets = getIndex((this.fonts[Font]).fdarrayOffset);
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
/*      */   public byte[] Process(String fontName) throws IOException {
/*      */     try {
/*  362 */       this.buf.reOpen();
/*      */       
/*      */       int j;
/*  365 */       for (j = 0; j < this.fonts.length && 
/*  366 */         !fontName.equals((this.fonts[j]).name); j++);
/*  367 */       if (j == this.fonts.length) return null;
/*      */ 
/*      */       
/*  370 */       if (this.gsubrIndexOffset >= 0) {
/*  371 */         this.GBias = CalcBias(this.gsubrIndexOffset, j);
/*      */       }
/*      */       
/*  374 */       BuildNewCharString(j);
/*      */       
/*  376 */       BuildNewLGSubrs(j);
/*      */       
/*  378 */       byte[] Ret = BuildNewFile(j);
/*  379 */       return Ret;
/*      */     } finally {
/*      */       
/*      */       try {
/*  383 */         this.buf.close();
/*      */       }
/*  385 */       catch (Exception exception) {}
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
/*      */   protected int CalcBias(int Offset, int Font) {
/*  400 */     seek(Offset);
/*  401 */     int nSubrs = getCard16();
/*      */     
/*  403 */     if ((this.fonts[Font]).CharstringType == 1) {
/*  404 */       return 0;
/*      */     }
/*  406 */     if (nSubrs < 1240)
/*  407 */       return 107; 
/*  408 */     if (nSubrs < 33900) {
/*  409 */       return 1131;
/*      */     }
/*  411 */     return 32768;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void BuildNewCharString(int FontIndex) throws IOException {
/*  421 */     this.NewCharStringsIndex = BuildNewIndex((this.fonts[FontIndex]).charstringsOffsets, this.GlyphsUsed, (byte)14);
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
/*      */   protected void BuildNewLGSubrs(int Font) throws IOException {
/*  435 */     if ((this.fonts[Font]).isCID) {
/*      */ 
/*      */ 
/*      */       
/*  439 */       this.hSubrsUsed = (HashMap<Integer, int[]>[])new HashMap[(this.fonts[Font]).fdprivateOffsets.length];
/*  440 */       this.lSubrsUsed = (ArrayList<Integer>[])new ArrayList[(this.fonts[Font]).fdprivateOffsets.length];
/*      */       
/*  442 */       this.NewLSubrsIndex = new byte[(this.fonts[Font]).fdprivateOffsets.length][];
/*      */       
/*  444 */       (this.fonts[Font]).PrivateSubrsOffset = new int[(this.fonts[Font]).fdprivateOffsets.length];
/*      */       
/*  446 */       (this.fonts[Font]).PrivateSubrsOffsetsArray = new int[(this.fonts[Font]).fdprivateOffsets.length][];
/*      */ 
/*      */       
/*  449 */       ArrayList<Integer> FDInList = new ArrayList<Integer>(this.FDArrayUsed);
/*      */       
/*  451 */       for (int j = 0; j < FDInList.size(); j++)
/*      */       {
/*      */         
/*  454 */         int FD = ((Integer)FDInList.get(j)).intValue();
/*  455 */         this.hSubrsUsed[FD] = (HashMap)new HashMap<Integer, int>();
/*  456 */         this.lSubrsUsed[FD] = new ArrayList<Integer>();
/*      */ 
/*      */         
/*  459 */         BuildFDSubrsOffsets(Font, FD);
/*      */         
/*  461 */         if ((this.fonts[Font]).PrivateSubrsOffset[FD] >= 0)
/*      */         {
/*      */ 
/*      */           
/*  465 */           BuildSubrUsed(Font, FD, (this.fonts[Font]).PrivateSubrsOffset[FD], (this.fonts[Font]).PrivateSubrsOffsetsArray[FD], this.hSubrsUsed[FD], this.lSubrsUsed[FD]);
/*      */           
/*  467 */           this.NewLSubrsIndex[FD] = BuildNewIndex((this.fonts[Font]).PrivateSubrsOffsetsArray[FD], this.hSubrsUsed[FD], (byte)11);
/*      */         }
/*      */       
/*      */       }
/*      */     
/*  472 */     } else if ((this.fonts[Font]).privateSubrs >= 0) {
/*      */ 
/*      */       
/*  475 */       (this.fonts[Font]).SubrsOffsets = getIndex((this.fonts[Font]).privateSubrs);
/*      */ 
/*      */       
/*  478 */       BuildSubrUsed(Font, -1, (this.fonts[Font]).privateSubrs, (this.fonts[Font]).SubrsOffsets, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID);
/*      */     } 
/*      */ 
/*      */     
/*  482 */     BuildGSubrsUsed(Font);
/*  483 */     if ((this.fonts[Font]).privateSubrs >= 0)
/*      */     {
/*  485 */       this.NewSubrsIndexNonCID = BuildNewIndex((this.fonts[Font]).SubrsOffsets, this.hSubrsUsedNonCID, (byte)11);
/*      */     }
/*  487 */     this.NewGSubrsIndex = BuildNewIndexAndCopyAllGSubrs(this.gsubrOffsets, (byte)11);
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
/*      */   protected void BuildFDSubrsOffsets(int Font, int FD) {
/*  499 */     (this.fonts[Font]).PrivateSubrsOffset[FD] = -1;
/*      */     
/*  501 */     seek((this.fonts[Font]).fdprivateOffsets[FD]);
/*      */     
/*  503 */     while (getPosition() < (this.fonts[Font]).fdprivateOffsets[FD] + (this.fonts[Font]).fdprivateLengths[FD]) {
/*      */       
/*  505 */       getDictItem();
/*      */       
/*  507 */       if (this.key == "Subrs") {
/*  508 */         (this.fonts[Font]).PrivateSubrsOffset[FD] = ((Integer)this.args[0]).intValue() + (this.fonts[Font]).fdprivateOffsets[FD];
/*      */       }
/*      */     } 
/*  511 */     if ((this.fonts[Font]).PrivateSubrsOffset[FD] >= 0) {
/*  512 */       (this.fonts[Font]).PrivateSubrsOffsetsArray[FD] = getIndex((this.fonts[Font]).PrivateSubrsOffset[FD]);
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
/*      */   protected void BuildSubrUsed(int Font, int FD, int SubrOffset, int[] SubrsOffsets, HashMap<Integer, int[]> hSubr, ArrayList<Integer> lSubr) {
/*  530 */     int LBias = CalcBias(SubrOffset, Font);
/*      */     
/*      */     int i;
/*  533 */     for (i = 0; i < this.glyphsInList.size(); i++) {
/*      */       
/*  535 */       int glyph = ((Integer)this.glyphsInList.get(i)).intValue();
/*  536 */       int Start = (this.fonts[Font]).charstringsOffsets[glyph];
/*  537 */       int End = (this.fonts[Font]).charstringsOffsets[glyph + 1];
/*      */ 
/*      */       
/*  540 */       if (FD >= 0) {
/*      */         
/*  542 */         EmptyStack();
/*  543 */         this.NumOfHints = 0;
/*      */         
/*  545 */         int GlyphFD = (this.fonts[Font]).FDSelect[glyph];
/*      */         
/*  547 */         if (GlyphFD == FD)
/*      */         {
/*  549 */           ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  554 */         ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
/*      */       } 
/*      */     } 
/*  557 */     for (i = 0; i < lSubr.size(); i++) {
/*      */ 
/*      */       
/*  560 */       int Subr = ((Integer)lSubr.get(i)).intValue();
/*      */       
/*  562 */       if (Subr < SubrsOffsets.length - 1 && Subr >= 0) {
/*      */ 
/*      */         
/*  565 */         int Start = SubrsOffsets[Subr];
/*  566 */         int End = SubrsOffsets[Subr + 1];
/*  567 */         ReadASubr(Start, End, this.GBias, LBias, hSubr, lSubr, SubrsOffsets);
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
/*      */   protected void BuildGSubrsUsed(int Font) {
/*  579 */     int LBias = 0;
/*  580 */     int SizeOfNonCIDSubrsUsed = 0;
/*  581 */     if ((this.fonts[Font]).privateSubrs >= 0) {
/*      */       
/*  583 */       LBias = CalcBias((this.fonts[Font]).privateSubrs, Font);
/*  584 */       SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
/*      */     } 
/*      */ 
/*      */     
/*  588 */     for (int i = 0; i < this.lGSubrsUsed.size(); i++) {
/*      */ 
/*      */       
/*  591 */       int Subr = ((Integer)this.lGSubrsUsed.get(i)).intValue();
/*  592 */       if (Subr < this.gsubrOffsets.length - 1 && Subr >= 0) {
/*      */ 
/*      */         
/*  595 */         int Start = this.gsubrOffsets[Subr];
/*  596 */         int End = this.gsubrOffsets[Subr + 1];
/*      */         
/*  598 */         if ((this.fonts[Font]).isCID) {
/*  599 */           ReadASubr(Start, End, this.GBias, 0, this.hGSubrsUsed, this.lGSubrsUsed, (int[])null);
/*      */         } else {
/*      */           
/*  602 */           ReadASubr(Start, End, this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, (this.fonts[Font]).SubrsOffsets);
/*  603 */           if (SizeOfNonCIDSubrsUsed < this.lSubrsUsedNonCID.size()) {
/*      */             
/*  605 */             for (int j = SizeOfNonCIDSubrsUsed; j < this.lSubrsUsedNonCID.size(); j++) {
/*      */ 
/*      */               
/*  608 */               int LSubr = ((Integer)this.lSubrsUsedNonCID.get(j)).intValue();
/*  609 */               if (LSubr < (this.fonts[Font]).SubrsOffsets.length - 1 && LSubr >= 0) {
/*      */ 
/*      */                 
/*  612 */                 int LStart = (this.fonts[Font]).SubrsOffsets[LSubr];
/*  613 */                 int LEnd = (this.fonts[Font]).SubrsOffsets[LSubr + 1];
/*  614 */                 ReadASubr(LStart, LEnd, this.GBias, LBias, this.hSubrsUsedNonCID, this.lSubrsUsedNonCID, (this.fonts[Font]).SubrsOffsets);
/*      */               } 
/*      */             } 
/*  617 */             SizeOfNonCIDSubrsUsed = this.lSubrsUsedNonCID.size();
/*      */           } 
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
/*      */   protected void ReadASubr(int begin, int end, int GBias, int LBias, HashMap<Integer, int[]> hSubr, ArrayList<Integer> lSubr, int[] LSubrsOffsets) {
/*  638 */     EmptyStack();
/*  639 */     this.NumOfHints = 0;
/*      */     
/*  641 */     seek(begin);
/*  642 */     while (getPosition() < end) {
/*      */ 
/*      */       
/*  645 */       ReadCommand();
/*  646 */       int pos = getPosition();
/*  647 */       Object TopElement = null;
/*  648 */       if (this.arg_count > 0)
/*  649 */         TopElement = this.args[this.arg_count - 1]; 
/*  650 */       int NumOfArgs = this.arg_count;
/*      */       
/*  652 */       HandelStack();
/*      */       
/*  654 */       if (this.key == "callsubr") {
/*      */ 
/*      */         
/*  657 */         if (NumOfArgs > 0) {
/*      */ 
/*      */           
/*  660 */           int Subr = ((Integer)TopElement).intValue() + LBias;
/*      */           
/*  662 */           if (!hSubr.containsKey(Integer.valueOf(Subr))) {
/*      */             
/*  664 */             hSubr.put(Integer.valueOf(Subr), null);
/*  665 */             lSubr.add(Integer.valueOf(Subr));
/*      */           } 
/*  667 */           CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
/*  668 */           seek(pos);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  672 */       if (this.key == "callgsubr") {
/*      */ 
/*      */         
/*  675 */         if (NumOfArgs > 0) {
/*      */ 
/*      */           
/*  678 */           int Subr = ((Integer)TopElement).intValue() + GBias;
/*      */           
/*  680 */           if (!this.hGSubrsUsed.containsKey(Integer.valueOf(Subr))) {
/*      */             
/*  682 */             this.hGSubrsUsed.put(Integer.valueOf(Subr), null);
/*  683 */             this.lGSubrsUsed.add(Integer.valueOf(Subr));
/*      */           } 
/*  685 */           CalcHints(this.gsubrOffsets[Subr], this.gsubrOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
/*  686 */           seek(pos);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  690 */       if (this.key == "hstem" || this.key == "vstem" || this.key == "hstemhm" || this.key == "vstemhm") {
/*      */         
/*  692 */         this.NumOfHints += NumOfArgs / 2;
/*      */         continue;
/*      */       } 
/*  695 */       if (this.key == "hintmask" || this.key == "cntrmask") {
/*      */ 
/*      */ 
/*      */         
/*  699 */         this.NumOfHints += NumOfArgs / 2;
/*      */         
/*  701 */         int SizeOfMask = this.NumOfHints / 8;
/*  702 */         if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
/*  703 */           SizeOfMask++;
/*      */         }
/*  705 */         for (int i = 0; i < SizeOfMask; i++) {
/*  706 */           getCard8();
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
/*      */   protected void HandelStack() {
/*  718 */     int StackHandel = StackOpp();
/*  719 */     if (StackHandel < 2) {
/*      */ 
/*      */       
/*  722 */       if (StackHandel == 1) {
/*  723 */         PushStack();
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  728 */         StackHandel *= -1;
/*  729 */         for (int i = 0; i < StackHandel; i++) {
/*  730 */           PopStack();
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  736 */       EmptyStack();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int StackOpp() {
/*  745 */     if (this.key == "ifelse")
/*  746 */       return -3; 
/*  747 */     if (this.key == "roll" || this.key == "put")
/*  748 */       return -2; 
/*  749 */     if (this.key == "callsubr" || this.key == "callgsubr" || this.key == "add" || this.key == "sub" || this.key == "div" || this.key == "mul" || this.key == "drop" || this.key == "and" || this.key == "or" || this.key == "eq")
/*      */     {
/*      */       
/*  752 */       return -1; } 
/*  753 */     if (this.key == "abs" || this.key == "neg" || this.key == "sqrt" || this.key == "exch" || this.key == "index" || this.key == "get" || this.key == "not" || this.key == "return")
/*      */     {
/*  755 */       return 0; } 
/*  756 */     if (this.key == "random" || this.key == "dup")
/*  757 */       return 1; 
/*  758 */     return 2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void EmptyStack() {
/*  768 */     for (int i = 0; i < this.arg_count; ) { this.args[i] = null; i++; }
/*  769 */      this.arg_count = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void PopStack() {
/*  778 */     if (this.arg_count > 0) {
/*      */       
/*  780 */       this.args[this.arg_count - 1] = null;
/*  781 */       this.arg_count--;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void PushStack() {
/*  791 */     this.arg_count++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ReadCommand() {
/*  799 */     this.key = null;
/*  800 */     boolean gotKey = false;
/*      */     
/*  802 */     while (!gotKey) {
/*      */       
/*  804 */       char b0 = getCard8();
/*      */       
/*  806 */       if (b0 == '\034') {
/*      */         
/*  808 */         int first = getCard8();
/*  809 */         int second = getCard8();
/*  810 */         this.args[this.arg_count] = Integer.valueOf(first << 8 | second);
/*  811 */         this.arg_count++;
/*      */         continue;
/*      */       } 
/*  814 */       if (b0 >= ' ' && b0 <= 'ö') {
/*      */         
/*  816 */         this.args[this.arg_count] = Integer.valueOf(b0 - 139);
/*  817 */         this.arg_count++;
/*      */         continue;
/*      */       } 
/*  820 */       if (b0 >= '÷' && b0 <= 'ú') {
/*      */         
/*  822 */         int w = getCard8();
/*  823 */         this.args[this.arg_count] = Integer.valueOf((b0 - 247) * 256 + w + 108);
/*  824 */         this.arg_count++;
/*      */         continue;
/*      */       } 
/*  827 */       if (b0 >= 'û' && b0 <= 'þ') {
/*      */         
/*  829 */         int w = getCard8();
/*  830 */         this.args[this.arg_count] = Integer.valueOf(-(b0 - 251) * 256 - w - 108);
/*  831 */         this.arg_count++;
/*      */         continue;
/*      */       } 
/*  834 */       if (b0 == 'ÿ') {
/*      */         
/*  836 */         int first = getCard8();
/*  837 */         int second = getCard8();
/*  838 */         int third = getCard8();
/*  839 */         int fourth = getCard8();
/*  840 */         this.args[this.arg_count] = Integer.valueOf(first << 24 | second << 16 | third << 8 | fourth);
/*  841 */         this.arg_count++;
/*      */         continue;
/*      */       } 
/*  844 */       if (b0 <= '\037' && b0 != '\034') {
/*      */         
/*  846 */         gotKey = true;
/*      */ 
/*      */         
/*  849 */         if (b0 == '\f') {
/*      */           
/*  851 */           int b1 = getCard8();
/*  852 */           if (b1 > SubrsEscapeFuncs.length - 1)
/*  853 */             b1 = SubrsEscapeFuncs.length - 1; 
/*  854 */           this.key = SubrsEscapeFuncs[b1];
/*      */           continue;
/*      */         } 
/*  857 */         this.key = SubrsFunctions[b0];
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
/*      */   protected int CalcHints(int begin, int end, int LBias, int GBias, int[] LSubrsOffsets) {
/*  876 */     seek(begin);
/*  877 */     while (getPosition() < end) {
/*      */ 
/*      */       
/*  880 */       ReadCommand();
/*  881 */       int pos = getPosition();
/*  882 */       Object TopElement = null;
/*  883 */       if (this.arg_count > 0)
/*  884 */         TopElement = this.args[this.arg_count - 1]; 
/*  885 */       int NumOfArgs = this.arg_count;
/*      */       
/*  887 */       HandelStack();
/*      */       
/*  889 */       if (this.key == "callsubr") {
/*      */         
/*  891 */         if (NumOfArgs > 0) {
/*      */           
/*  893 */           int Subr = ((Integer)TopElement).intValue() + LBias;
/*  894 */           CalcHints(LSubrsOffsets[Subr], LSubrsOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
/*  895 */           seek(pos);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  899 */       if (this.key == "callgsubr") {
/*      */         
/*  901 */         if (NumOfArgs > 0) {
/*      */           
/*  903 */           int Subr = ((Integer)TopElement).intValue() + GBias;
/*  904 */           CalcHints(this.gsubrOffsets[Subr], this.gsubrOffsets[Subr + 1], LBias, GBias, LSubrsOffsets);
/*  905 */           seek(pos);
/*      */         } 
/*      */         continue;
/*      */       } 
/*  909 */       if (this.key == "hstem" || this.key == "vstem" || this.key == "hstemhm" || this.key == "vstemhm") {
/*      */         
/*  911 */         this.NumOfHints += NumOfArgs / 2; continue;
/*      */       } 
/*  913 */       if (this.key == "hintmask" || this.key == "cntrmask") {
/*      */ 
/*      */         
/*  916 */         int SizeOfMask = this.NumOfHints / 8;
/*  917 */         if (this.NumOfHints % 8 != 0 || SizeOfMask == 0) {
/*  918 */           SizeOfMask++;
/*      */         }
/*  920 */         for (int i = 0; i < SizeOfMask; i++)
/*  921 */           getCard8(); 
/*      */       } 
/*      */     } 
/*  924 */     return this.NumOfHints;
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
/*      */   protected byte[] BuildNewIndex(int[] Offsets, HashMap<Integer, int[]> Used, byte OperatorForUnusedEntries) throws IOException {
/*  939 */     int unusedCount = 0;
/*  940 */     int Offset = 0;
/*  941 */     int[] NewOffsets = new int[Offsets.length];
/*      */     
/*  943 */     for (int i = 0; i < Offsets.length; i++) {
/*      */       
/*  945 */       NewOffsets[i] = Offset;
/*      */ 
/*      */       
/*  948 */       if (Used.containsKey(Integer.valueOf(i))) {
/*  949 */         Offset += Offsets[i + 1] - Offsets[i];
/*      */       } else {
/*      */         
/*  952 */         unusedCount++;
/*      */       } 
/*      */     } 
/*      */     
/*  956 */     byte[] NewObjects = new byte[Offset + unusedCount];
/*      */     
/*  958 */     int unusedOffset = 0;
/*  959 */     for (int j = 0; j < Offsets.length - 1; j++) {
/*      */       
/*  961 */       int start = NewOffsets[j];
/*  962 */       int end = NewOffsets[j + 1];
/*  963 */       NewOffsets[j] = start + unusedOffset;
/*      */ 
/*      */       
/*  966 */       if (start != end) {
/*      */ 
/*      */ 
/*      */         
/*  970 */         this.buf.seek(Offsets[j]);
/*      */         
/*  972 */         this.buf.readFully(NewObjects, start + unusedOffset, end - start);
/*      */       } else {
/*  974 */         NewObjects[start + unusedOffset] = OperatorForUnusedEntries;
/*  975 */         unusedOffset++;
/*      */       } 
/*      */     } 
/*  978 */     NewOffsets[Offsets.length - 1] = NewOffsets[Offsets.length - 1] + unusedOffset;
/*      */     
/*  980 */     return AssembleIndex(NewOffsets, NewObjects);
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
/*      */   protected byte[] BuildNewIndexAndCopyAllGSubrs(int[] Offsets, byte OperatorForUnusedEntries) throws IOException {
/*  993 */     int unusedCount = 0;
/*  994 */     int Offset = 0;
/*  995 */     int[] NewOffsets = new int[Offsets.length];
/*      */     
/*  997 */     for (int i = 0; i < Offsets.length - 1; i++) {
/*  998 */       NewOffsets[i] = Offset;
/*  999 */       Offset += Offsets[i + 1] - Offsets[i];
/*      */     } 
/*      */     
/* 1002 */     NewOffsets[Offsets.length - 1] = Offset;
/* 1003 */     unusedCount++;
/*      */ 
/*      */     
/* 1006 */     byte[] NewObjects = new byte[Offset + unusedCount];
/*      */     
/* 1008 */     int unusedOffset = 0;
/* 1009 */     for (int j = 0; j < Offsets.length - 1; j++) {
/* 1010 */       int start = NewOffsets[j];
/* 1011 */       int end = NewOffsets[j + 1];
/* 1012 */       NewOffsets[j] = start + unusedOffset;
/*      */ 
/*      */       
/* 1015 */       if (start != end) {
/*      */ 
/*      */         
/* 1018 */         this.buf.seek(Offsets[j]);
/*      */         
/* 1020 */         this.buf.readFully(NewObjects, start + unusedOffset, end - start);
/*      */       } else {
/* 1022 */         NewObjects[start + unusedOffset] = OperatorForUnusedEntries;
/* 1023 */         unusedOffset++;
/*      */       } 
/*      */     } 
/* 1026 */     NewOffsets[Offsets.length - 1] = NewOffsets[Offsets.length - 1] + unusedOffset;
/*      */     
/* 1028 */     return AssembleIndex(NewOffsets, NewObjects);
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
/*      */   protected byte[] AssembleIndex(int[] NewOffsets, byte[] NewObjects) {
/*      */     byte Offsize;
/* 1041 */     char Count = (char)(NewOffsets.length - 1);
/*      */     
/* 1043 */     int Size = NewOffsets[NewOffsets.length - 1];
/*      */ 
/*      */     
/* 1046 */     if (Size < 255) { Offsize = 1; }
/* 1047 */     else if (Size < 65535) { Offsize = 2; }
/* 1048 */     else if (Size < 16777215) { Offsize = 3; }
/* 1049 */     else { Offsize = 4; }
/*      */ 
/*      */     
/* 1052 */     byte[] NewIndex = new byte[3 + Offsize * (Count + 1) + NewObjects.length];
/*      */     
/* 1054 */     int Place = 0;
/*      */     
/* 1056 */     NewIndex[Place++] = (byte)(Count >>> 8 & 0xFF);
/* 1057 */     NewIndex[Place++] = (byte)(Count >>> 0 & 0xFF);
/*      */     
/* 1059 */     NewIndex[Place++] = Offsize;
/*      */     
/* 1061 */     for (int newOffset : NewOffsets) {
/*      */       
/* 1063 */       int Num = newOffset - NewOffsets[0] + 1;
/*      */       
/* 1065 */       switch (Offsize) {
/*      */         case 4:
/* 1067 */           NewIndex[Place++] = (byte)(Num >>> 24 & 0xFF);
/*      */         case 3:
/* 1069 */           NewIndex[Place++] = (byte)(Num >>> 16 & 0xFF);
/*      */         case 2:
/* 1071 */           NewIndex[Place++] = (byte)(Num >>> 8 & 0xFF);
/*      */         case 1:
/* 1073 */           NewIndex[Place++] = (byte)(Num >>> 0 & 0xFF);
/*      */           break;
/*      */       } 
/*      */     } 
/* 1077 */     for (byte newObject : NewObjects) {
/* 1078 */       NewIndex[Place++] = newObject;
/*      */     }
/*      */     
/* 1081 */     return NewIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected byte[] BuildNewFile(int Font) {
/* 1092 */     this.OutputList = new LinkedList<CFFFont.Item>();
/*      */ 
/*      */     
/* 1095 */     CopyHeader();
/*      */ 
/*      */     
/* 1098 */     BuildIndexHeader(1, 1, 1);
/* 1099 */     this.OutputList.addLast(new CFFFont.UInt8Item((char)(1 + (this.fonts[Font]).name.length())));
/* 1100 */     this.OutputList.addLast(new CFFFont.StringItem((this.fonts[Font]).name));
/*      */ 
/*      */     
/* 1103 */     BuildIndexHeader(1, 2, 1);
/* 1104 */     CFFFont.OffsetItem topdictIndex1Ref = new CFFFont.IndexOffsetItem(2);
/* 1105 */     this.OutputList.addLast(topdictIndex1Ref);
/* 1106 */     CFFFont.IndexBaseItem topdictBase = new CFFFont.IndexBaseItem();
/* 1107 */     this.OutputList.addLast(topdictBase);
/*      */ 
/*      */     
/* 1110 */     CFFFont.OffsetItem charsetRef = new CFFFont.DictOffsetItem();
/* 1111 */     CFFFont.OffsetItem charstringsRef = new CFFFont.DictOffsetItem();
/* 1112 */     CFFFont.OffsetItem fdarrayRef = new CFFFont.DictOffsetItem();
/* 1113 */     CFFFont.OffsetItem fdselectRef = new CFFFont.DictOffsetItem();
/* 1114 */     CFFFont.OffsetItem privateRef = new CFFFont.DictOffsetItem();
/*      */ 
/*      */     
/* 1117 */     if (!(this.fonts[Font]).isCID) {
/*      */       
/* 1119 */       this.OutputList.addLast(new CFFFont.DictNumberItem((this.fonts[Font]).nstrings));
/* 1120 */       this.OutputList.addLast(new CFFFont.DictNumberItem((this.fonts[Font]).nstrings + 1));
/* 1121 */       this.OutputList.addLast(new CFFFont.DictNumberItem(0));
/* 1122 */       this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
/* 1123 */       this.OutputList.addLast(new CFFFont.UInt8Item('\036'));
/*      */       
/* 1125 */       this.OutputList.addLast(new CFFFont.DictNumberItem((this.fonts[Font]).nglyphs));
/* 1126 */       this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
/* 1127 */       this.OutputList.addLast(new CFFFont.UInt8Item('"'));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1133 */     seek(this.topdictOffsets[Font]);
/*      */     
/* 1135 */     while (getPosition() < this.topdictOffsets[Font + 1]) {
/* 1136 */       int p1 = getPosition();
/* 1137 */       getDictItem();
/* 1138 */       int p2 = getPosition();
/*      */       
/* 1140 */       if (this.key == "Encoding" || this.key == "Private" || this.key == "FDSelect" || this.key == "FDArray" || this.key == "charset" || this.key == "CharStrings") {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1150 */       this.OutputList.add(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
/*      */     } 
/*      */ 
/*      */     
/* 1154 */     CreateKeys(fdarrayRef, fdselectRef, charsetRef, charstringsRef);
/*      */ 
/*      */     
/* 1157 */     this.OutputList.addLast(new CFFFont.IndexMarkerItem(topdictIndex1Ref, topdictBase));
/*      */ 
/*      */ 
/*      */     
/* 1161 */     if ((this.fonts[Font]).isCID) {
/* 1162 */       this.OutputList.addLast(getEntireIndexRange(this.stringIndexOffset));
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1167 */       CreateNewStringIndex(Font);
/*      */     } 
/*      */     
/* 1170 */     this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.NewGSubrsIndex), 0, this.NewGSubrsIndex.length));
/*      */ 
/*      */ 
/*      */     
/* 1174 */     if ((this.fonts[Font]).isCID) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1179 */       this.OutputList.addLast(new CFFFont.MarkerItem(fdselectRef));
/*      */       
/* 1181 */       if ((this.fonts[Font]).fdselectOffset >= 0) {
/* 1182 */         this.OutputList.addLast(new CFFFont.RangeItem(this.buf, (this.fonts[Font]).fdselectOffset, (this.fonts[Font]).FDSelectLength));
/*      */       } else {
/*      */         
/* 1185 */         CreateFDSelect(fdselectRef, (this.fonts[Font]).nglyphs);
/*      */       } 
/*      */ 
/*      */       
/* 1189 */       this.OutputList.addLast(new CFFFont.MarkerItem(charsetRef));
/* 1190 */       this.OutputList.addLast(new CFFFont.RangeItem(this.buf, (this.fonts[Font]).charsetOffset, (this.fonts[Font]).CharsetLength));
/*      */ 
/*      */ 
/*      */       
/* 1194 */       if ((this.fonts[Font]).fdarrayOffset >= 0)
/*      */       {
/*      */         
/* 1197 */         this.OutputList.addLast(new CFFFont.MarkerItem(fdarrayRef));
/*      */         
/* 1199 */         Reconstruct(Font);
/*      */       }
/*      */       else
/*      */       {
/* 1203 */         CreateFDArray(fdarrayRef, privateRef, Font);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1210 */       CreateFDSelect(fdselectRef, (this.fonts[Font]).nglyphs);
/*      */       
/* 1212 */       CreateCharset(charsetRef, (this.fonts[Font]).nglyphs);
/*      */       
/* 1214 */       CreateFDArray(fdarrayRef, privateRef, Font);
/*      */     } 
/*      */ 
/*      */     
/* 1218 */     if ((this.fonts[Font]).privateOffset >= 0) {
/*      */ 
/*      */       
/* 1221 */       CFFFont.IndexBaseItem PrivateBase = new CFFFont.IndexBaseItem();
/* 1222 */       this.OutputList.addLast(PrivateBase);
/* 1223 */       this.OutputList.addLast(new CFFFont.MarkerItem(privateRef));
/*      */       
/* 1225 */       CFFFont.OffsetItem Subr = new CFFFont.DictOffsetItem();
/*      */       
/* 1227 */       CreateNonCIDPrivate(Font, Subr);
/*      */       
/* 1229 */       CreateNonCIDSubrs(Font, PrivateBase, Subr);
/*      */     } 
/*      */ 
/*      */     
/* 1233 */     this.OutputList.addLast(new CFFFont.MarkerItem(charstringsRef));
/*      */ 
/*      */     
/* 1236 */     this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.NewCharStringsIndex), 0, this.NewCharStringsIndex.length));
/*      */ 
/*      */     
/* 1239 */     int[] currentOffset = new int[1];
/* 1240 */     currentOffset[0] = 0;
/*      */     
/* 1242 */     Iterator<CFFFont.Item> listIter = this.OutputList.iterator();
/* 1243 */     while (listIter.hasNext()) {
/* 1244 */       CFFFont.Item item = listIter.next();
/* 1245 */       item.increment(currentOffset);
/*      */     } 
/*      */     
/* 1248 */     listIter = this.OutputList.iterator();
/* 1249 */     while (listIter.hasNext()) {
/* 1250 */       CFFFont.Item item = listIter.next();
/* 1251 */       item.xref();
/*      */     } 
/*      */     
/* 1254 */     int size = currentOffset[0];
/* 1255 */     byte[] b = new byte[size];
/*      */ 
/*      */     
/* 1258 */     listIter = this.OutputList.iterator();
/* 1259 */     while (listIter.hasNext()) {
/* 1260 */       CFFFont.Item item = listIter.next();
/* 1261 */       item.emit(b);
/*      */     } 
/*      */     
/* 1264 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void CopyHeader() {
/* 1272 */     seek(0);
/* 1273 */     int major = getCard8();
/* 1274 */     int minor = getCard8();
/* 1275 */     int hdrSize = getCard8();
/* 1276 */     int offSize = getCard8();
/* 1277 */     this.nextIndexOffset = hdrSize;
/* 1278 */     this.OutputList.addLast(new CFFFont.RangeItem(this.buf, 0, hdrSize));
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
/*      */   protected void BuildIndexHeader(int Count, int Offsize, int First) {
/* 1290 */     this.OutputList.addLast(new CFFFont.UInt16Item((char)Count));
/*      */     
/* 1292 */     this.OutputList.addLast(new CFFFont.UInt8Item((char)Offsize));
/*      */     
/* 1294 */     switch (Offsize) {
/*      */       case 1:
/* 1296 */         this.OutputList.addLast(new CFFFont.UInt8Item((char)First));
/*      */         break;
/*      */       case 2:
/* 1299 */         this.OutputList.addLast(new CFFFont.UInt16Item((char)First));
/*      */         break;
/*      */       case 3:
/* 1302 */         this.OutputList.addLast(new CFFFont.UInt24Item((char)First));
/*      */         break;
/*      */       case 4:
/* 1305 */         this.OutputList.addLast(new CFFFont.UInt32Item((char)First));
/*      */         break;
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
/*      */   protected void CreateKeys(CFFFont.OffsetItem fdarrayRef, CFFFont.OffsetItem fdselectRef, CFFFont.OffsetItem charsetRef, CFFFont.OffsetItem charstringsRef) {
/* 1322 */     this.OutputList.addLast(fdarrayRef);
/* 1323 */     this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
/* 1324 */     this.OutputList.addLast(new CFFFont.UInt8Item('$'));
/*      */     
/* 1326 */     this.OutputList.addLast(fdselectRef);
/* 1327 */     this.OutputList.addLast(new CFFFont.UInt8Item('\f'));
/* 1328 */     this.OutputList.addLast(new CFFFont.UInt8Item('%'));
/*      */     
/* 1330 */     this.OutputList.addLast(charsetRef);
/* 1331 */     this.OutputList.addLast(new CFFFont.UInt8Item('\017'));
/*      */     
/* 1333 */     this.OutputList.addLast(charstringsRef);
/* 1334 */     this.OutputList.addLast(new CFFFont.UInt8Item('\021'));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void CreateNewStringIndex(int Font) {
/*      */     byte stringsIndexOffSize;
/* 1344 */     String fdFontName = (this.fonts[Font]).name + "-OneRange";
/* 1345 */     if (fdFontName.length() > 127)
/* 1346 */       fdFontName = fdFontName.substring(0, 127); 
/* 1347 */     String extraStrings = "AdobeIdentity" + fdFontName;
/*      */     
/* 1349 */     int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
/*      */     
/* 1351 */     int stringsBaseOffset = this.stringOffsets[0] - 1;
/*      */ 
/*      */     
/* 1354 */     if (origStringsLen + extraStrings.length() <= 255) { stringsIndexOffSize = 1; }
/* 1355 */     else if (origStringsLen + extraStrings.length() <= 65535) { stringsIndexOffSize = 2; }
/* 1356 */     else if (origStringsLen + extraStrings.length() <= 16777215) { stringsIndexOffSize = 3; }
/* 1357 */     else { stringsIndexOffSize = 4; }
/*      */     
/* 1359 */     this.OutputList.addLast(new CFFFont.UInt16Item((char)(this.stringOffsets.length - 1 + 3)));
/* 1360 */     this.OutputList.addLast(new CFFFont.UInt8Item((char)stringsIndexOffSize));
/* 1361 */     for (int stringOffset : this.stringOffsets) {
/* 1362 */       this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
/*      */     }
/* 1364 */     int currentStringsOffset = this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset;
/*      */ 
/*      */     
/* 1367 */     currentStringsOffset += "Adobe".length();
/* 1368 */     this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/* 1369 */     currentStringsOffset += "Identity".length();
/* 1370 */     this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/* 1371 */     currentStringsOffset += fdFontName.length();
/* 1372 */     this.OutputList.addLast(new CFFFont.IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/*      */     
/* 1374 */     this.OutputList.addLast(new CFFFont.RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
/* 1375 */     this.OutputList.addLast(new CFFFont.StringItem(extraStrings));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void CreateFDSelect(CFFFont.OffsetItem fdselectRef, int nglyphs) {
/* 1386 */     this.OutputList.addLast(new CFFFont.MarkerItem(fdselectRef));
/* 1387 */     this.OutputList.addLast(new CFFFont.UInt8Item('\003'));
/* 1388 */     this.OutputList.addLast(new CFFFont.UInt16Item('\001'));
/*      */     
/* 1390 */     this.OutputList.addLast(new CFFFont.UInt16Item(false));
/* 1391 */     this.OutputList.addLast(new CFFFont.UInt8Item(false));
/*      */     
/* 1393 */     this.OutputList.addLast(new CFFFont.UInt16Item((char)nglyphs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void CreateCharset(CFFFont.OffsetItem charsetRef, int nglyphs) {
/* 1404 */     this.OutputList.addLast(new CFFFont.MarkerItem(charsetRef));
/* 1405 */     this.OutputList.addLast(new CFFFont.UInt8Item('\002'));
/* 1406 */     this.OutputList.addLast(new CFFFont.UInt16Item('\001'));
/* 1407 */     this.OutputList.addLast(new CFFFont.UInt16Item((char)(nglyphs - 1)));
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
/*      */   protected void CreateFDArray(CFFFont.OffsetItem fdarrayRef, CFFFont.OffsetItem privateRef, int Font) {
/* 1420 */     this.OutputList.addLast(new CFFFont.MarkerItem(fdarrayRef));
/*      */     
/* 1422 */     BuildIndexHeader(1, 1, 1);
/*      */ 
/*      */     
/* 1425 */     CFFFont.OffsetItem privateIndex1Ref = new CFFFont.IndexOffsetItem(1);
/* 1426 */     this.OutputList.addLast(privateIndex1Ref);
/* 1427 */     CFFFont.IndexBaseItem privateBase = new CFFFont.IndexBaseItem();
/*      */     
/* 1429 */     this.OutputList.addLast(privateBase);
/*      */ 
/*      */     
/* 1432 */     int NewSize = (this.fonts[Font]).privateLength;
/*      */     
/* 1434 */     int OrgSubrsOffsetSize = CalcSubrOffsetSize((this.fonts[Font]).privateOffset, (this.fonts[Font]).privateLength);
/*      */     
/* 1436 */     if (OrgSubrsOffsetSize != 0)
/* 1437 */       NewSize += 5 - OrgSubrsOffsetSize; 
/* 1438 */     this.OutputList.addLast(new CFFFont.DictNumberItem(NewSize));
/* 1439 */     this.OutputList.addLast(privateRef);
/* 1440 */     this.OutputList.addLast(new CFFFont.UInt8Item('\022'));
/*      */     
/* 1442 */     this.OutputList.addLast(new CFFFont.IndexMarkerItem(privateIndex1Ref, privateBase));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void Reconstruct(int Font) {
/* 1452 */     CFFFont.DictOffsetItem[] arrayOfDictOffsetItem1 = new CFFFont.DictOffsetItem[(this.fonts[Font]).FDArrayOffsets.length - 1];
/* 1453 */     CFFFont.IndexBaseItem[] fdPrivateBase = new CFFFont.IndexBaseItem[(this.fonts[Font]).fdprivateOffsets.length];
/* 1454 */     CFFFont.DictOffsetItem[] arrayOfDictOffsetItem2 = new CFFFont.DictOffsetItem[(this.fonts[Font]).fdprivateOffsets.length];
/*      */     
/* 1456 */     ReconstructFDArray(Font, (CFFFont.OffsetItem[])arrayOfDictOffsetItem1);
/* 1457 */     ReconstructPrivateDict(Font, (CFFFont.OffsetItem[])arrayOfDictOffsetItem1, fdPrivateBase, (CFFFont.OffsetItem[])arrayOfDictOffsetItem2);
/* 1458 */     ReconstructPrivateSubrs(Font, fdPrivateBase, (CFFFont.OffsetItem[])arrayOfDictOffsetItem2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void ReconstructFDArray(int Font, CFFFont.OffsetItem[] fdPrivate) {
/* 1469 */     BuildIndexHeader((this.fonts[Font]).FDArrayCount, (this.fonts[Font]).FDArrayOffsize, 1);
/*      */ 
/*      */     
/* 1472 */     CFFFont.IndexOffsetItem[] arrayOfIndexOffsetItem = new CFFFont.IndexOffsetItem[(this.fonts[Font]).FDArrayOffsets.length - 1];
/* 1473 */     for (int i = 0; i < (this.fonts[Font]).FDArrayOffsets.length - 1; i++) {
/*      */       
/* 1475 */       arrayOfIndexOffsetItem[i] = new CFFFont.IndexOffsetItem((this.fonts[Font]).FDArrayOffsize);
/* 1476 */       this.OutputList.addLast(arrayOfIndexOffsetItem[i]);
/*      */     } 
/*      */ 
/*      */     
/* 1480 */     CFFFont.IndexBaseItem fdArrayBase = new CFFFont.IndexBaseItem();
/* 1481 */     this.OutputList.addLast(fdArrayBase);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1487 */     for (int k = 0; k < (this.fonts[Font]).FDArrayOffsets.length - 1; k++) {
/*      */ 
/*      */ 
/*      */       
/* 1491 */       seek((this.fonts[Font]).FDArrayOffsets[k]);
/* 1492 */       while (getPosition() < (this.fonts[Font]).FDArrayOffsets[k + 1]) {
/*      */         
/* 1494 */         int p1 = getPosition();
/* 1495 */         getDictItem();
/* 1496 */         int p2 = getPosition();
/*      */ 
/*      */         
/* 1499 */         if (this.key == "Private") {
/*      */           
/* 1501 */           int NewSize = ((Integer)this.args[0]).intValue();
/*      */           
/* 1503 */           int OrgSubrsOffsetSize = CalcSubrOffsetSize((this.fonts[Font]).fdprivateOffsets[k], (this.fonts[Font]).fdprivateLengths[k]);
/*      */           
/* 1505 */           if (OrgSubrsOffsetSize != 0) {
/* 1506 */             NewSize += 5 - OrgSubrsOffsetSize;
/*      */           }
/* 1508 */           this.OutputList.addLast(new CFFFont.DictNumberItem(NewSize));
/* 1509 */           fdPrivate[k] = new CFFFont.DictOffsetItem();
/* 1510 */           this.OutputList.addLast(fdPrivate[k]);
/* 1511 */           this.OutputList.addLast(new CFFFont.UInt8Item('\022'));
/*      */           
/* 1513 */           seek(p2);
/*      */           
/*      */           continue;
/*      */         } 
/* 1517 */         this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
/*      */       } 
/*      */ 
/*      */       
/* 1521 */       this.OutputList.addLast(new CFFFont.IndexMarkerItem(arrayOfIndexOffsetItem[k], fdArrayBase));
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
/*      */   void ReconstructPrivateDict(int Font, CFFFont.OffsetItem[] fdPrivate, CFFFont.IndexBaseItem[] fdPrivateBase, CFFFont.OffsetItem[] fdSubrs) {
/* 1538 */     for (int i = 0; i < (this.fonts[Font]).fdprivateOffsets.length; i++) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1543 */       this.OutputList.addLast(new CFFFont.MarkerItem(fdPrivate[i]));
/* 1544 */       fdPrivateBase[i] = new CFFFont.IndexBaseItem();
/* 1545 */       this.OutputList.addLast(fdPrivateBase[i]);
/*      */       
/* 1547 */       seek((this.fonts[Font]).fdprivateOffsets[i]);
/* 1548 */       while (getPosition() < (this.fonts[Font]).fdprivateOffsets[i] + (this.fonts[Font]).fdprivateLengths[i]) {
/*      */         
/* 1550 */         int p1 = getPosition();
/* 1551 */         getDictItem();
/* 1552 */         int p2 = getPosition();
/*      */ 
/*      */         
/* 1555 */         if (this.key == "Subrs") {
/* 1556 */           fdSubrs[i] = new CFFFont.DictOffsetItem();
/* 1557 */           this.OutputList.addLast(fdSubrs[i]);
/* 1558 */           this.OutputList.addLast(new CFFFont.UInt8Item('\023'));
/*      */           
/*      */           continue;
/*      */         } 
/* 1562 */         this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
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
/*      */   void ReconstructPrivateSubrs(int Font, CFFFont.IndexBaseItem[] fdPrivateBase, CFFFont.OffsetItem[] fdSubrs) {
/* 1579 */     for (int i = 0; i < (this.fonts[Font]).fdprivateLengths.length; i++) {
/*      */ 
/*      */ 
/*      */       
/* 1583 */       if (fdSubrs[i] != null && (this.fonts[Font]).PrivateSubrsOffset[i] >= 0) {
/*      */         
/* 1585 */         this.OutputList.addLast(new CFFFont.SubrMarkerItem(fdSubrs[i], fdPrivateBase[i]));
/* 1586 */         if (this.NewLSubrsIndex[i] != null) {
/* 1587 */           this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.NewLSubrsIndex[i]), 0, (this.NewLSubrsIndex[i]).length));
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
/*      */   int CalcSubrOffsetSize(int Offset, int Size) {
/* 1602 */     int OffsetSize = 0;
/*      */     
/* 1604 */     seek(Offset);
/*      */     
/* 1606 */     while (getPosition() < Offset + Size) {
/*      */       
/* 1608 */       int p1 = getPosition();
/* 1609 */       getDictItem();
/* 1610 */       int p2 = getPosition();
/*      */       
/* 1612 */       if (this.key == "Subrs")
/*      */       {
/* 1614 */         OffsetSize = p2 - p1 - 1;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1619 */     return OffsetSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int countEntireIndexRange(int indexOffset) {
/* 1630 */     seek(indexOffset);
/*      */     
/* 1632 */     int count = getCard16();
/*      */     
/* 1634 */     if (count == 0) {
/* 1635 */       return 2;
/*      */     }
/*      */ 
/*      */     
/* 1639 */     int indexOffSize = getCard8();
/*      */     
/* 1641 */     seek(indexOffset + 2 + 1 + count * indexOffSize);
/*      */     
/* 1643 */     int size = getOffset(indexOffSize) - 1;
/*      */     
/* 1645 */     return 3 + (count + 1) * indexOffSize + size;
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
/*      */   void CreateNonCIDPrivate(int Font, CFFFont.OffsetItem Subr) {
/* 1658 */     seek((this.fonts[Font]).privateOffset);
/* 1659 */     while (getPosition() < (this.fonts[Font]).privateOffset + (this.fonts[Font]).privateLength) {
/*      */       
/* 1661 */       int p1 = getPosition();
/* 1662 */       getDictItem();
/* 1663 */       int p2 = getPosition();
/*      */ 
/*      */       
/* 1666 */       if (this.key == "Subrs") {
/* 1667 */         this.OutputList.addLast(Subr);
/* 1668 */         this.OutputList.addLast(new CFFFont.UInt8Item('\023'));
/*      */         
/*      */         continue;
/*      */       } 
/* 1672 */       this.OutputList.addLast(new CFFFont.RangeItem(this.buf, p1, p2 - p1));
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
/*      */   void CreateNonCIDSubrs(int Font, CFFFont.IndexBaseItem PrivateBase, CFFFont.OffsetItem Subrs) {
/* 1686 */     this.OutputList.addLast(new CFFFont.SubrMarkerItem(Subrs, PrivateBase));
/*      */     
/* 1688 */     if (this.NewSubrsIndexNonCID != null)
/* 1689 */       this.OutputList.addLast(new CFFFont.RangeItem(new RandomAccessFileOrArray(this.NewSubrsIndexNonCID), 0, this.NewSubrsIndexNonCID.length)); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/CFFFontSubset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */