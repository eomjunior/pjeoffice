/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.Document;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class TrueTypeFont
/*      */   extends BaseFont
/*      */ {
/*   67 */   static final String[] codePages = new String[] { "1252 Latin 1", "1250 Latin 2: Eastern Europe", "1251 Cyrillic", "1253 Greek", "1254 Turkish", "1255 Hebrew", "1256 Arabic", "1257 Windows Baltic", "1258 Vietnamese", null, null, null, null, null, null, null, "874 Thai", "932 JIS/Japan", "936 Chinese: Simplified chars--PRC and Singapore", "949 Korean Wansung", "950 Chinese: Traditional chars--Taiwan and Hong Kong", "1361 Korean Johab", null, null, null, null, null, null, null, "Macintosh Character Set (US Roman)", "OEM Character Set", "Symbol Character Set", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "869 IBM Greek", "866 MS-DOS Russian", "865 MS-DOS Nordic", "864 Arabic", "863 MS-DOS Canadian French", "862 Hebrew", "861 MS-DOS Icelandic", "860 MS-DOS Portuguese", "857 IBM Turkish", "855 IBM Cyrillic; primarily Russian", "852 Latin 2", "775 MS-DOS Baltic", "737 Greek; former 437 G", "708 Arabic; ASMO 708", "850 WE/Latin 1", "437 US" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean justNames = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashMap<String, int[]> tables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RandomAccessFileOrArray rf;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String fileName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean cff = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int cffOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int cffLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int directoryOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String ttcIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   protected String style = "";
/*      */ 
/*      */ 
/*      */   
/*  173 */   protected FontHeader head = new FontHeader();
/*      */ 
/*      */ 
/*      */   
/*  177 */   protected HorizontalHeader hhea = new HorizontalHeader();
/*      */ 
/*      */ 
/*      */   
/*  181 */   protected WindowsMetrics os_2 = new WindowsMetrics();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[] glyphWidthsByIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[][] bboxes;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashMap<Integer, int[]> cmap10;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashMap<Integer, int[]> cmap31;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected HashMap<Integer, int[]> cmapExt;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[] glyphIdToChar;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int maxGlyphId;
/*      */ 
/*      */ 
/*      */   
/*  219 */   protected IntHashtable kerning = new IntHashtable();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String fontName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String[][] subFamily;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String[][] fullName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String[][] allNameEntries;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String[][] familyName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected double italicAngle;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isFixedPitch = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int underlinePosition;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int underlineThickness;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TrueTypeFont() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class FontHeader
/*      */   {
/*      */     int flags;
/*      */ 
/*      */ 
/*      */     
/*      */     int unitsPerEm;
/*      */ 
/*      */ 
/*      */     
/*      */     short xMin;
/*      */ 
/*      */ 
/*      */     
/*      */     short yMin;
/*      */ 
/*      */ 
/*      */     
/*      */     short xMax;
/*      */ 
/*      */ 
/*      */     
/*      */     short yMax;
/*      */ 
/*      */ 
/*      */     
/*      */     int macStyle;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class HorizontalHeader
/*      */   {
/*      */     short Ascender;
/*      */ 
/*      */ 
/*      */     
/*      */     short Descender;
/*      */ 
/*      */ 
/*      */     
/*      */     short LineGap;
/*      */ 
/*      */ 
/*      */     
/*      */     int advanceWidthMax;
/*      */ 
/*      */ 
/*      */     
/*      */     short minLeftSideBearing;
/*      */ 
/*      */ 
/*      */     
/*      */     short minRightSideBearing;
/*      */ 
/*      */ 
/*      */     
/*      */     short xMaxExtent;
/*      */ 
/*      */ 
/*      */     
/*      */     short caretSlopeRise;
/*      */ 
/*      */ 
/*      */     
/*      */     short caretSlopeRun;
/*      */ 
/*      */ 
/*      */     
/*      */     int numberOfHMetrics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class WindowsMetrics
/*      */   {
/*      */     short xAvgCharWidth;
/*      */ 
/*      */ 
/*      */     
/*      */     int usWeightClass;
/*      */ 
/*      */ 
/*      */     
/*      */     int usWidthClass;
/*      */ 
/*      */ 
/*      */     
/*      */     short fsType;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySubscriptXSize;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySubscriptYSize;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySubscriptXOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySubscriptYOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySuperscriptXSize;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySuperscriptYSize;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySuperscriptXOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     short ySuperscriptYOffset;
/*      */ 
/*      */ 
/*      */     
/*      */     short yStrikeoutSize;
/*      */ 
/*      */ 
/*      */     
/*      */     short yStrikeoutPosition;
/*      */ 
/*      */ 
/*      */     
/*      */     short sFamilyClass;
/*      */ 
/*      */ 
/*      */     
/*  414 */     byte[] panose = new byte[10];
/*      */ 
/*      */ 
/*      */     
/*  418 */     byte[] achVendID = new byte[4];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int fsSelection;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int usFirstCharIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int usLastCharIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     short sTypoAscender;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     short sTypoDescender;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     short sTypoLineGap;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int usWinAscent;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int usWinDescent;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int ulCodePageRange1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int ulCodePageRange2;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int sCapHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrueTypeFont(String ttFile, String enc, boolean emb, byte[] ttfAfm, boolean justNames, boolean forceRead) throws DocumentException, IOException {
/*  484 */     this.justNames = justNames;
/*  485 */     String nameBase = getBaseName(ttFile);
/*  486 */     String ttcName = getTTCName(nameBase);
/*  487 */     if (nameBase.length() < ttFile.length()) {
/*  488 */       this.style = ttFile.substring(nameBase.length());
/*      */     }
/*  490 */     this.encoding = enc;
/*  491 */     this.embedded = emb;
/*  492 */     this.fileName = ttcName;
/*  493 */     this.fontType = 1;
/*  494 */     this.ttcIndex = "";
/*  495 */     if (ttcName.length() < nameBase.length())
/*  496 */       this.ttcIndex = nameBase.substring(ttcName.length() + 1); 
/*  497 */     if (this.fileName.toLowerCase().endsWith(".ttf") || this.fileName.toLowerCase().endsWith(".otf") || this.fileName.toLowerCase().endsWith(".ttc")) {
/*  498 */       process(ttfAfm, forceRead);
/*  499 */       if (!justNames && this.embedded && this.os_2.fsType == 2)
/*  500 */         throw new DocumentException(MessageLocalization.getComposedMessage("1.cannot.be.embedded.due.to.licensing.restrictions", new Object[] { this.fileName + this.style })); 
/*      */     } else {
/*  502 */       throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.ttf.otf.or.ttc.font.file", new Object[] { this.fileName + this.style }));
/*  503 */     }  if (!this.encoding.startsWith("#"))
/*  504 */       PdfEncodings.convertToBytes(" ", enc); 
/*  505 */     createEncoding();
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
/*      */   protected static String getTTCName(String name) {
/*  517 */     int idx = name.toLowerCase().indexOf(".ttc,");
/*  518 */     if (idx < 0) {
/*  519 */       return name;
/*      */     }
/*  521 */     return name.substring(0, idx + 4);
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
/*      */   void fillTables() throws DocumentException, IOException {
/*  533 */     int[] table_location = this.tables.get("head");
/*  534 */     if (table_location == null)
/*  535 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "head", this.fileName + this.style })); 
/*  536 */     this.rf.seek((table_location[0] + 16));
/*  537 */     this.head.flags = this.rf.readUnsignedShort();
/*  538 */     this.head.unitsPerEm = this.rf.readUnsignedShort();
/*  539 */     this.rf.skipBytes(16);
/*  540 */     this.head.xMin = this.rf.readShort();
/*  541 */     this.head.yMin = this.rf.readShort();
/*  542 */     this.head.xMax = this.rf.readShort();
/*  543 */     this.head.yMax = this.rf.readShort();
/*  544 */     this.head.macStyle = this.rf.readUnsignedShort();
/*      */     
/*  546 */     table_location = this.tables.get("hhea");
/*  547 */     if (table_location == null)
/*  548 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "hhea", this.fileName + this.style })); 
/*  549 */     this.rf.seek((table_location[0] + 4));
/*  550 */     this.hhea.Ascender = this.rf.readShort();
/*  551 */     this.hhea.Descender = this.rf.readShort();
/*  552 */     this.hhea.LineGap = this.rf.readShort();
/*  553 */     this.hhea.advanceWidthMax = this.rf.readUnsignedShort();
/*  554 */     this.hhea.minLeftSideBearing = this.rf.readShort();
/*  555 */     this.hhea.minRightSideBearing = this.rf.readShort();
/*  556 */     this.hhea.xMaxExtent = this.rf.readShort();
/*  557 */     this.hhea.caretSlopeRise = this.rf.readShort();
/*  558 */     this.hhea.caretSlopeRun = this.rf.readShort();
/*  559 */     this.rf.skipBytes(12);
/*  560 */     this.hhea.numberOfHMetrics = this.rf.readUnsignedShort();
/*      */     
/*  562 */     table_location = this.tables.get("OS/2");
/*  563 */     if (table_location != null) {
/*  564 */       this.rf.seek(table_location[0]);
/*  565 */       int version = this.rf.readUnsignedShort();
/*  566 */       this.os_2.xAvgCharWidth = this.rf.readShort();
/*  567 */       this.os_2.usWeightClass = this.rf.readUnsignedShort();
/*  568 */       this.os_2.usWidthClass = this.rf.readUnsignedShort();
/*  569 */       this.os_2.fsType = this.rf.readShort();
/*  570 */       this.os_2.ySubscriptXSize = this.rf.readShort();
/*  571 */       this.os_2.ySubscriptYSize = this.rf.readShort();
/*  572 */       this.os_2.ySubscriptXOffset = this.rf.readShort();
/*  573 */       this.os_2.ySubscriptYOffset = this.rf.readShort();
/*  574 */       this.os_2.ySuperscriptXSize = this.rf.readShort();
/*  575 */       this.os_2.ySuperscriptYSize = this.rf.readShort();
/*  576 */       this.os_2.ySuperscriptXOffset = this.rf.readShort();
/*  577 */       this.os_2.ySuperscriptYOffset = this.rf.readShort();
/*  578 */       this.os_2.yStrikeoutSize = this.rf.readShort();
/*  579 */       this.os_2.yStrikeoutPosition = this.rf.readShort();
/*  580 */       this.os_2.sFamilyClass = this.rf.readShort();
/*  581 */       this.rf.readFully(this.os_2.panose);
/*  582 */       this.rf.skipBytes(16);
/*  583 */       this.rf.readFully(this.os_2.achVendID);
/*  584 */       this.os_2.fsSelection = this.rf.readUnsignedShort();
/*  585 */       this.os_2.usFirstCharIndex = this.rf.readUnsignedShort();
/*  586 */       this.os_2.usLastCharIndex = this.rf.readUnsignedShort();
/*  587 */       this.os_2.sTypoAscender = this.rf.readShort();
/*  588 */       this.os_2.sTypoDescender = this.rf.readShort();
/*  589 */       if (this.os_2.sTypoDescender > 0)
/*  590 */         this.os_2.sTypoDescender = (short)-this.os_2.sTypoDescender; 
/*  591 */       this.os_2.sTypoLineGap = this.rf.readShort();
/*  592 */       this.os_2.usWinAscent = this.rf.readUnsignedShort();
/*  593 */       this.os_2.usWinDescent = this.rf.readUnsignedShort();
/*  594 */       this.os_2.ulCodePageRange1 = 0;
/*  595 */       this.os_2.ulCodePageRange2 = 0;
/*  596 */       if (version > 0) {
/*  597 */         this.os_2.ulCodePageRange1 = this.rf.readInt();
/*  598 */         this.os_2.ulCodePageRange2 = this.rf.readInt();
/*      */       } 
/*  600 */       if (version > 1)
/*  601 */       { this.rf.skipBytes(2);
/*  602 */         this.os_2.sCapHeight = this.rf.readShort(); }
/*      */       else
/*  604 */       { this.os_2.sCapHeight = (int)(0.7D * this.head.unitsPerEm); } 
/*  605 */     } else if (this.tables.get("hhea") != null && this.tables.get("head") != null) {
/*      */       
/*  607 */       if (this.head.macStyle == 0) {
/*  608 */         this.os_2.usWeightClass = 700;
/*  609 */         this.os_2.usWidthClass = 5;
/*  610 */       } else if (this.head.macStyle == 5) {
/*  611 */         this.os_2.usWeightClass = 400;
/*  612 */         this.os_2.usWidthClass = 3;
/*  613 */       } else if (this.head.macStyle == 6) {
/*  614 */         this.os_2.usWeightClass = 400;
/*  615 */         this.os_2.usWidthClass = 7;
/*      */       } else {
/*  617 */         this.os_2.usWeightClass = 400;
/*  618 */         this.os_2.usWidthClass = 5;
/*      */       } 
/*  620 */       this.os_2.fsType = 0;
/*      */       
/*  622 */       this.os_2.ySubscriptYSize = 0;
/*  623 */       this.os_2.ySubscriptYOffset = 0;
/*  624 */       this.os_2.ySuperscriptYSize = 0;
/*  625 */       this.os_2.ySuperscriptYOffset = 0;
/*  626 */       this.os_2.yStrikeoutSize = 0;
/*  627 */       this.os_2.yStrikeoutPosition = 0;
/*  628 */       this.os_2.sTypoAscender = (short)(int)(this.hhea.Ascender - 0.21D * this.hhea.Ascender);
/*  629 */       this.os_2.sTypoDescender = (short)(int)-(Math.abs(this.hhea.Descender) - Math.abs(this.hhea.Descender) * 0.07D);
/*  630 */       this.os_2.sTypoLineGap = (short)(this.hhea.LineGap * 2);
/*  631 */       this.os_2.usWinAscent = this.hhea.Ascender;
/*  632 */       this.os_2.usWinDescent = this.hhea.Descender;
/*  633 */       this.os_2.ulCodePageRange1 = 0;
/*  634 */       this.os_2.ulCodePageRange2 = 0;
/*  635 */       this.os_2.sCapHeight = (int)(0.7D * this.head.unitsPerEm);
/*      */     } 
/*      */     
/*  638 */     table_location = this.tables.get("post");
/*  639 */     if (table_location == null) {
/*  640 */       this.italicAngle = -Math.atan2(this.hhea.caretSlopeRun, this.hhea.caretSlopeRise) * 180.0D / Math.PI;
/*      */     } else {
/*  642 */       this.rf.seek((table_location[0] + 4));
/*  643 */       short mantissa = this.rf.readShort();
/*  644 */       int fraction = this.rf.readUnsignedShort();
/*  645 */       this.italicAngle = mantissa + fraction / 16384.0D;
/*  646 */       this.underlinePosition = this.rf.readShort();
/*  647 */       this.underlineThickness = this.rf.readShort();
/*  648 */       this.isFixedPitch = (this.rf.readInt() != 0);
/*      */     } 
/*      */     
/*  651 */     table_location = this.tables.get("maxp");
/*  652 */     if (table_location == null) {
/*  653 */       this.maxGlyphId = 65536;
/*      */     } else {
/*  655 */       this.rf.seek((table_location[0] + 4));
/*  656 */       this.maxGlyphId = this.rf.readUnsignedShort();
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
/*      */   String getBaseFont() throws DocumentException, IOException {
/*  669 */     int[] table_location = this.tables.get("name");
/*  670 */     if (table_location == null)
/*  671 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "name", this.fileName + this.style })); 
/*  672 */     this.rf.seek((table_location[0] + 2));
/*  673 */     int numRecords = this.rf.readUnsignedShort();
/*  674 */     int startOfStorage = this.rf.readUnsignedShort();
/*  675 */     for (int k = 0; k < numRecords; k++) {
/*  676 */       int platformID = this.rf.readUnsignedShort();
/*  677 */       int platformEncodingID = this.rf.readUnsignedShort();
/*  678 */       int languageID = this.rf.readUnsignedShort();
/*  679 */       int nameID = this.rf.readUnsignedShort();
/*  680 */       int length = this.rf.readUnsignedShort();
/*  681 */       int offset = this.rf.readUnsignedShort();
/*  682 */       if (nameID == 6) {
/*  683 */         this.rf.seek((table_location[0] + startOfStorage + offset));
/*  684 */         if (platformID == 0 || platformID == 3) {
/*  685 */           return readUnicodeString(length);
/*      */         }
/*  687 */         return readStandardString(length);
/*      */       } 
/*      */     } 
/*  690 */     File file = new File(this.fileName);
/*  691 */     return file.getName().replace(' ', '-');
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
/*      */   String[][] getNames(int id) throws DocumentException, IOException {
/*  703 */     int[] table_location = this.tables.get("name");
/*  704 */     if (table_location == null)
/*  705 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "name", this.fileName + this.style })); 
/*  706 */     this.rf.seek((table_location[0] + 2));
/*  707 */     int numRecords = this.rf.readUnsignedShort();
/*  708 */     int startOfStorage = this.rf.readUnsignedShort();
/*  709 */     ArrayList<String[]> names = (ArrayList)new ArrayList<String>();
/*  710 */     for (int k = 0; k < numRecords; k++) {
/*  711 */       int platformID = this.rf.readUnsignedShort();
/*  712 */       int platformEncodingID = this.rf.readUnsignedShort();
/*  713 */       int languageID = this.rf.readUnsignedShort();
/*  714 */       int nameID = this.rf.readUnsignedShort();
/*  715 */       int length = this.rf.readUnsignedShort();
/*  716 */       int offset = this.rf.readUnsignedShort();
/*  717 */       if (nameID == id) {
/*  718 */         String name; int pos = (int)this.rf.getFilePointer();
/*  719 */         this.rf.seek((table_location[0] + startOfStorage + offset));
/*      */         
/*  721 */         if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)) {
/*  722 */           name = readUnicodeString(length);
/*      */         } else {
/*  724 */           name = readStandardString(length);
/*      */         } 
/*  726 */         names.add(new String[] { String.valueOf(platformID), 
/*  727 */               String.valueOf(platformEncodingID), String.valueOf(languageID), name });
/*  728 */         this.rf.seek(pos);
/*      */       } 
/*      */     } 
/*  731 */     String[][] thisName = new String[names.size()][];
/*  732 */     for (int i = 0; i < names.size(); i++)
/*  733 */       thisName[i] = names.get(i); 
/*  734 */     return thisName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String[][] getAllNames() throws DocumentException, IOException {
/*  745 */     int[] table_location = this.tables.get("name");
/*  746 */     if (table_location == null)
/*  747 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "name", this.fileName + this.style })); 
/*  748 */     this.rf.seek((table_location[0] + 2));
/*  749 */     int numRecords = this.rf.readUnsignedShort();
/*  750 */     int startOfStorage = this.rf.readUnsignedShort();
/*  751 */     ArrayList<String[]> names = (ArrayList)new ArrayList<String>();
/*  752 */     for (int k = 0; k < numRecords; k++) {
/*  753 */       String name; int platformID = this.rf.readUnsignedShort();
/*  754 */       int platformEncodingID = this.rf.readUnsignedShort();
/*  755 */       int languageID = this.rf.readUnsignedShort();
/*  756 */       int nameID = this.rf.readUnsignedShort();
/*  757 */       int length = this.rf.readUnsignedShort();
/*  758 */       int offset = this.rf.readUnsignedShort();
/*  759 */       int pos = (int)this.rf.getFilePointer();
/*  760 */       this.rf.seek((table_location[0] + startOfStorage + offset));
/*      */       
/*  762 */       if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)) {
/*  763 */         name = readUnicodeString(length);
/*      */       } else {
/*  765 */         name = readStandardString(length);
/*      */       } 
/*  767 */       names.add(new String[] { String.valueOf(nameID), String.valueOf(platformID), 
/*  768 */             String.valueOf(platformEncodingID), String.valueOf(languageID), name });
/*  769 */       this.rf.seek(pos);
/*      */     } 
/*  771 */     String[][] thisName = new String[names.size()][];
/*  772 */     for (int i = 0; i < names.size(); i++)
/*  773 */       thisName[i] = names.get(i); 
/*  774 */     return thisName;
/*      */   }
/*      */ 
/*      */   
/*      */   void checkCff() {
/*  779 */     int[] table_location = this.tables.get("CFF ");
/*  780 */     if (table_location != null) {
/*  781 */       this.cff = true;
/*  782 */       this.cffOffset = table_location[0];
/*  783 */       this.cffLength = table_location[1];
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
/*      */   void process(byte[] ttfAfm, boolean preload) throws DocumentException, IOException {
/*  796 */     this.tables = (HashMap)new HashMap<String, int>();
/*      */     
/*  798 */     if (ttfAfm == null) {
/*  799 */       this.rf = new RandomAccessFileOrArray(this.fileName, preload, Document.plainRandomAccess);
/*      */     } else {
/*  801 */       this.rf = new RandomAccessFileOrArray(ttfAfm);
/*      */     } 
/*      */     try {
/*  804 */       if (this.ttcIndex.length() > 0) {
/*  805 */         int dirIdx = Integer.parseInt(this.ttcIndex);
/*  806 */         if (dirIdx < 0)
/*  807 */           throw new DocumentException(MessageLocalization.getComposedMessage("the.font.index.for.1.must.be.positive", new Object[] { this.fileName })); 
/*  808 */         String mainTag = readStandardString(4);
/*  809 */         if (!mainTag.equals("ttcf"))
/*  810 */           throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttc.file", new Object[] { this.fileName })); 
/*  811 */         this.rf.skipBytes(4);
/*  812 */         int dirCount = this.rf.readInt();
/*  813 */         if (dirIdx >= dirCount)
/*  814 */           throw new DocumentException(MessageLocalization.getComposedMessage("the.font.index.for.1.must.be.between.0.and.2.it.was.3", new Object[] { this.fileName, String.valueOf(dirCount - 1), String.valueOf(dirIdx) })); 
/*  815 */         this.rf.skipBytes(dirIdx * 4);
/*  816 */         this.directoryOffset = this.rf.readInt();
/*      */       } 
/*  818 */       this.rf.seek(this.directoryOffset);
/*  819 */       int ttId = this.rf.readInt();
/*  820 */       if (ttId != 65536 && ttId != 1330926671)
/*  821 */         throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttf.or.otf.file", new Object[] { this.fileName })); 
/*  822 */       int num_tables = this.rf.readUnsignedShort();
/*  823 */       this.rf.skipBytes(6);
/*  824 */       for (int k = 0; k < num_tables; k++) {
/*  825 */         String tag = readStandardString(4);
/*  826 */         this.rf.skipBytes(4);
/*  827 */         int[] table_location = new int[2];
/*  828 */         table_location[0] = this.rf.readInt();
/*  829 */         table_location[1] = this.rf.readInt();
/*  830 */         this.tables.put(tag, table_location);
/*      */       } 
/*  832 */       checkCff();
/*  833 */       this.fontName = getBaseFont();
/*  834 */       this.fullName = getNames(4);
/*      */       
/*  836 */       String[][] otfFamilyName = getNames(16);
/*  837 */       if (otfFamilyName.length > 0) {
/*  838 */         this.familyName = otfFamilyName;
/*      */       } else {
/*  840 */         this.familyName = getNames(1);
/*      */       } 
/*  842 */       String[][] otfSubFamily = getNames(17);
/*  843 */       if (otfFamilyName.length > 0) {
/*  844 */         this.subFamily = otfSubFamily;
/*      */       } else {
/*  846 */         this.subFamily = getNames(2);
/*      */       } 
/*  848 */       this.allNameEntries = getAllNames();
/*  849 */       if (!this.justNames) {
/*  850 */         fillTables();
/*  851 */         readGlyphWidths();
/*  852 */         readCMaps();
/*  853 */         readKerning();
/*  854 */         readBbox();
/*      */       } 
/*      */     } finally {
/*      */       
/*  858 */       if (!this.embedded) {
/*  859 */         this.rf.close();
/*  860 */         this.rf = null;
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
/*      */   protected String readStandardString(int length) throws IOException {
/*  874 */     return this.rf.readString(length, "Cp1252");
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
/*      */   protected String readUnicodeString(int length) throws IOException {
/*  887 */     StringBuffer buf = new StringBuffer();
/*  888 */     length /= 2;
/*  889 */     for (int k = 0; k < length; k++) {
/*  890 */       buf.append(this.rf.readChar());
/*      */     }
/*  892 */     return buf.toString();
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
/*      */   protected void readGlyphWidths() throws DocumentException, IOException {
/*  904 */     int[] table_location = this.tables.get("hmtx");
/*  905 */     if (table_location == null)
/*  906 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "hmtx", this.fileName + this.style })); 
/*  907 */     this.rf.seek(table_location[0]);
/*  908 */     this.glyphWidthsByIndex = new int[this.hhea.numberOfHMetrics];
/*  909 */     for (int k = 0; k < this.hhea.numberOfHMetrics; k++) {
/*  910 */       this.glyphWidthsByIndex[k] = this.rf.readUnsignedShort() * 1000 / this.head.unitsPerEm;
/*      */       
/*  912 */       int i = this.rf.readShort() * 1000 / this.head.unitsPerEm;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getGlyphWidth(int glyph) {
/*  923 */     if (glyph >= this.glyphWidthsByIndex.length)
/*  924 */       glyph = this.glyphWidthsByIndex.length - 1; 
/*  925 */     return this.glyphWidthsByIndex[glyph];
/*      */   }
/*      */ 
/*      */   
/*      */   private void readBbox() throws DocumentException, IOException {
/*  930 */     int[] locaTable, tableLocation = this.tables.get("head");
/*  931 */     if (tableLocation == null)
/*  932 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "head", this.fileName + this.style })); 
/*  933 */     this.rf.seek((tableLocation[0] + 51));
/*  934 */     boolean locaShortTable = (this.rf.readUnsignedShort() == 0);
/*  935 */     tableLocation = this.tables.get("loca");
/*  936 */     if (tableLocation == null)
/*      */       return; 
/*  938 */     this.rf.seek(tableLocation[0]);
/*      */     
/*  940 */     if (locaShortTable) {
/*  941 */       int entries = tableLocation[1] / 2;
/*  942 */       locaTable = new int[entries];
/*  943 */       for (int k = 0; k < entries; k++)
/*  944 */         locaTable[k] = this.rf.readUnsignedShort() * 2; 
/*      */     } else {
/*  946 */       int entries = tableLocation[1] / 4;
/*  947 */       locaTable = new int[entries];
/*  948 */       for (int k = 0; k < entries; k++)
/*  949 */         locaTable[k] = this.rf.readInt(); 
/*      */     } 
/*  951 */     tableLocation = this.tables.get("glyf");
/*  952 */     if (tableLocation == null)
/*  953 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "glyf", this.fileName + this.style })); 
/*  954 */     int tableGlyphOffset = tableLocation[0];
/*  955 */     this.bboxes = new int[locaTable.length - 1][];
/*  956 */     for (int glyph = 0; glyph < locaTable.length - 1; glyph++) {
/*  957 */       int start = locaTable[glyph];
/*  958 */       if (start != locaTable[glyph + 1]) {
/*  959 */         this.rf.seek((tableGlyphOffset + start + 2));
/*  960 */         (new int[4])[0] = this.rf
/*  961 */           .readShort() * 1000 / this.head.unitsPerEm; (new int[4])[1] = this.rf
/*  962 */           .readShort() * 1000 / this.head.unitsPerEm; (new int[4])[2] = this.rf
/*  963 */           .readShort() * 1000 / this.head.unitsPerEm; (new int[4])[3] = this.rf
/*  964 */           .readShort() * 1000 / this.head.unitsPerEm;
/*      */         this.bboxes[glyph] = new int[4];
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
/*      */   void readCMaps() throws DocumentException, IOException {
/*  978 */     int[] table_location = this.tables.get("cmap");
/*  979 */     if (table_location == null)
/*  980 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "cmap", this.fileName + this.style })); 
/*  981 */     this.rf.seek(table_location[0]);
/*  982 */     this.rf.skipBytes(2);
/*  983 */     int num_tables = this.rf.readUnsignedShort();
/*  984 */     this.fontSpecific = false;
/*  985 */     int map10 = 0;
/*  986 */     int map31 = 0;
/*  987 */     int map30 = 0;
/*  988 */     int mapExt = 0;
/*  989 */     for (int k = 0; k < num_tables; k++) {
/*  990 */       int platId = this.rf.readUnsignedShort();
/*  991 */       int platSpecId = this.rf.readUnsignedShort();
/*  992 */       int offset = this.rf.readInt();
/*  993 */       if (platId == 3 && platSpecId == 0) {
/*  994 */         this.fontSpecific = true;
/*  995 */         map30 = offset;
/*  996 */       } else if (platId == 3 && platSpecId == 1) {
/*  997 */         map31 = offset;
/*  998 */       } else if (platId == 3 && platSpecId == 10) {
/*  999 */         mapExt = offset;
/*      */       } 
/* 1001 */       if (platId == 1 && platSpecId == 0) {
/* 1002 */         map10 = offset;
/*      */       }
/*      */     } 
/* 1005 */     if (map10 > 0) {
/* 1006 */       this.rf.seek((table_location[0] + map10));
/* 1007 */       int format = this.rf.readUnsignedShort();
/* 1008 */       switch (format) {
/*      */         case 0:
/* 1010 */           this.cmap10 = readFormat0();
/*      */           break;
/*      */         case 4:
/* 1013 */           this.cmap10 = readFormat4();
/*      */           break;
/*      */         case 6:
/* 1016 */           this.cmap10 = readFormat6();
/*      */           break;
/*      */       } 
/*      */     } 
/* 1020 */     if (map31 > 0) {
/* 1021 */       this.rf.seek((table_location[0] + map31));
/* 1022 */       int format = this.rf.readUnsignedShort();
/* 1023 */       if (format == 4) {
/* 1024 */         this.cmap31 = readFormat4();
/*      */       }
/*      */     } 
/* 1027 */     if (map30 > 0) {
/* 1028 */       this.rf.seek((table_location[0] + map30));
/* 1029 */       int format = this.rf.readUnsignedShort();
/* 1030 */       if (format == 4) {
/* 1031 */         this.cmap10 = readFormat4();
/*      */       }
/*      */     } 
/* 1034 */     if (mapExt > 0) {
/* 1035 */       this.rf.seek((table_location[0] + mapExt));
/* 1036 */       int format = this.rf.readUnsignedShort();
/* 1037 */       switch (format) {
/*      */         case 0:
/* 1039 */           this.cmapExt = readFormat0();
/*      */           break;
/*      */         case 4:
/* 1042 */           this.cmapExt = readFormat4();
/*      */           break;
/*      */         case 6:
/* 1045 */           this.cmapExt = readFormat6();
/*      */           break;
/*      */         case 12:
/* 1048 */           this.cmapExt = readFormat12();
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   HashMap<Integer, int[]> readFormat12() throws IOException {
/* 1055 */     HashMap<Integer, int[]> h = (HashMap)new HashMap<Integer, int>();
/* 1056 */     this.rf.skipBytes(2);
/* 1057 */     int table_lenght = this.rf.readInt();
/* 1058 */     this.rf.skipBytes(4);
/* 1059 */     int nGroups = this.rf.readInt();
/* 1060 */     for (int k = 0; k < nGroups; k++) {
/* 1061 */       int startCharCode = this.rf.readInt();
/* 1062 */       int endCharCode = this.rf.readInt();
/* 1063 */       int startGlyphID = this.rf.readInt();
/* 1064 */       for (int i = startCharCode; i <= endCharCode; i++) {
/* 1065 */         int[] r = new int[2];
/* 1066 */         r[0] = startGlyphID;
/* 1067 */         r[1] = getGlyphWidth(r[0]);
/* 1068 */         h.put(Integer.valueOf(i), r);
/* 1069 */         startGlyphID++;
/*      */       } 
/*      */     } 
/* 1072 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   HashMap<Integer, int[]> readFormat0() throws IOException {
/* 1083 */     HashMap<Integer, int[]> h = (HashMap)new HashMap<Integer, int>();
/* 1084 */     this.rf.skipBytes(4);
/* 1085 */     for (int k = 0; k < 256; k++) {
/* 1086 */       int[] r = new int[2];
/* 1087 */       r[0] = this.rf.readUnsignedByte();
/* 1088 */       r[1] = getGlyphWidth(r[0]);
/* 1089 */       h.put(Integer.valueOf(k), r);
/*      */     } 
/* 1091 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   HashMap<Integer, int[]> readFormat4() throws IOException {
/* 1102 */     HashMap<Integer, int[]> h = (HashMap)new HashMap<Integer, int>();
/* 1103 */     int table_lenght = this.rf.readUnsignedShort();
/* 1104 */     this.rf.skipBytes(2);
/* 1105 */     int segCount = this.rf.readUnsignedShort() / 2;
/* 1106 */     this.rf.skipBytes(6);
/* 1107 */     int[] endCount = new int[segCount];
/* 1108 */     for (int k = 0; k < segCount; k++) {
/* 1109 */       endCount[k] = this.rf.readUnsignedShort();
/*      */     }
/* 1111 */     this.rf.skipBytes(2);
/* 1112 */     int[] startCount = new int[segCount];
/* 1113 */     for (int i = 0; i < segCount; i++) {
/* 1114 */       startCount[i] = this.rf.readUnsignedShort();
/*      */     }
/* 1116 */     int[] idDelta = new int[segCount];
/* 1117 */     for (int j = 0; j < segCount; j++) {
/* 1118 */       idDelta[j] = this.rf.readUnsignedShort();
/*      */     }
/* 1120 */     int[] idRO = new int[segCount];
/* 1121 */     for (int m = 0; m < segCount; m++) {
/* 1122 */       idRO[m] = this.rf.readUnsignedShort();
/*      */     }
/* 1124 */     int[] glyphId = new int[table_lenght / 2 - 8 - segCount * 4]; int n;
/* 1125 */     for (n = 0; n < glyphId.length; n++) {
/* 1126 */       glyphId[n] = this.rf.readUnsignedShort();
/*      */     }
/* 1128 */     for (n = 0; n < segCount; n++) {
/*      */       
/* 1130 */       for (int i1 = startCount[n]; i1 <= endCount[n] && i1 != 65535; i1++) {
/* 1131 */         int glyph; if (idRO[n] == 0) {
/* 1132 */           glyph = i1 + idDelta[n] & 0xFFFF;
/*      */         } else {
/* 1134 */           int idx = n + idRO[n] / 2 - segCount + i1 - startCount[n];
/* 1135 */           if (idx >= glyphId.length)
/*      */             continue; 
/* 1137 */           glyph = glyphId[idx] + idDelta[n] & 0xFFFF;
/*      */         } 
/* 1139 */         int[] r = new int[2];
/* 1140 */         r[0] = glyph;
/* 1141 */         r[1] = getGlyphWidth(r[0]);
/* 1142 */         h.put(Integer.valueOf(this.fontSpecific ? (((i1 & 0xFF00) == 61440) ? (i1 & 0xFF) : i1) : i1), r); continue;
/*      */       } 
/*      */     } 
/* 1145 */     return h;
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
/*      */   HashMap<Integer, int[]> readFormat6() throws IOException {
/* 1157 */     HashMap<Integer, int[]> h = (HashMap)new HashMap<Integer, int>();
/* 1158 */     this.rf.skipBytes(4);
/* 1159 */     int start_code = this.rf.readUnsignedShort();
/* 1160 */     int code_count = this.rf.readUnsignedShort();
/* 1161 */     for (int k = 0; k < code_count; k++) {
/* 1162 */       int[] r = new int[2];
/* 1163 */       r[0] = this.rf.readUnsignedShort();
/* 1164 */       r[1] = getGlyphWidth(r[0]);
/* 1165 */       h.put(Integer.valueOf(k + start_code), r);
/*      */     } 
/* 1167 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void readKerning() throws IOException {
/* 1177 */     int[] table_location = this.tables.get("kern");
/* 1178 */     if (table_location == null)
/*      */       return; 
/* 1180 */     this.rf.seek((table_location[0] + 2));
/* 1181 */     int nTables = this.rf.readUnsignedShort();
/* 1182 */     int checkpoint = table_location[0] + 4;
/* 1183 */     int length = 0;
/* 1184 */     for (int k = 0; k < nTables; k++) {
/* 1185 */       checkpoint += length;
/* 1186 */       this.rf.seek(checkpoint);
/* 1187 */       this.rf.skipBytes(2);
/* 1188 */       length = this.rf.readUnsignedShort();
/* 1189 */       int coverage = this.rf.readUnsignedShort();
/* 1190 */       if ((coverage & 0xFFF7) == 1) {
/* 1191 */         int nPairs = this.rf.readUnsignedShort();
/* 1192 */         this.rf.skipBytes(6);
/* 1193 */         for (int j = 0; j < nPairs; j++) {
/* 1194 */           int pair = this.rf.readInt();
/* 1195 */           int value = this.rf.readShort() * 1000 / this.head.unitsPerEm;
/* 1196 */           this.kerning.put(pair, value);
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
/*      */   public int getKerning(int char1, int char2) {
/* 1211 */     int[] metrics = getMetricsTT(char1);
/* 1212 */     if (metrics == null)
/* 1213 */       return 0; 
/* 1214 */     int c1 = metrics[0];
/* 1215 */     metrics = getMetricsTT(char2);
/* 1216 */     if (metrics == null)
/* 1217 */       return 0; 
/* 1218 */     int c2 = metrics[0];
/* 1219 */     return this.kerning.get((c1 << 16) + c2);
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
/*      */   int getRawWidth(int c, String name) {
/* 1232 */     int[] metric = getMetricsTT(c);
/* 1233 */     if (metric == null)
/* 1234 */       return 0; 
/* 1235 */     return metric[1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PdfDictionary getFontDescriptor(PdfIndirectReference fontStream, String subsetPrefix, PdfIndirectReference cidset) {
/* 1246 */     PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
/* 1247 */     dic.put(PdfName.ASCENT, new PdfNumber(this.os_2.sTypoAscender * 1000 / this.head.unitsPerEm));
/* 1248 */     dic.put(PdfName.CAPHEIGHT, new PdfNumber(this.os_2.sCapHeight * 1000 / this.head.unitsPerEm));
/* 1249 */     dic.put(PdfName.DESCENT, new PdfNumber(this.os_2.sTypoDescender * 1000 / this.head.unitsPerEm));
/* 1250 */     dic.put(PdfName.FONTBBOX, new PdfRectangle((this.head.xMin * 1000 / this.head.unitsPerEm), (this.head.yMin * 1000 / this.head.unitsPerEm), (this.head.xMax * 1000 / this.head.unitsPerEm), (this.head.yMax * 1000 / this.head.unitsPerEm)));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1255 */     if (cidset != null)
/* 1256 */       dic.put(PdfName.CIDSET, cidset); 
/* 1257 */     if (this.cff)
/* 1258 */     { if (this.encoding.startsWith("Identity-")) {
/* 1259 */         dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
/*      */       } else {
/* 1261 */         dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + this.style));
/*      */       }  }
/* 1263 */     else { dic.put(PdfName.FONTNAME, new PdfName(subsetPrefix + this.fontName + this.style)); }
/* 1264 */      dic.put(PdfName.ITALICANGLE, new PdfNumber(this.italicAngle));
/* 1265 */     dic.put(PdfName.STEMV, new PdfNumber(80));
/* 1266 */     if (fontStream != null)
/* 1267 */       if (this.cff) {
/* 1268 */         dic.put(PdfName.FONTFILE3, fontStream);
/*      */       } else {
/* 1270 */         dic.put(PdfName.FONTFILE2, fontStream);
/*      */       }  
/* 1272 */     int flags = 0;
/* 1273 */     if (this.isFixedPitch)
/* 1274 */       flags |= 0x1; 
/* 1275 */     flags |= this.fontSpecific ? 4 : 32;
/* 1276 */     if ((this.head.macStyle & 0x2) != 0)
/* 1277 */       flags |= 0x40; 
/* 1278 */     if ((this.head.macStyle & 0x1) != 0)
/* 1279 */       flags |= 0x40000; 
/* 1280 */     dic.put(PdfName.FLAGS, new PdfNumber(flags));
/*      */     
/* 1282 */     return dic;
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
/*      */   protected PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, String subsetPrefix, int firstChar, int lastChar, byte[] shortTag) {
/* 1296 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 1297 */     if (this.cff) {
/* 1298 */       dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1299 */       dic.put(PdfName.BASEFONT, new PdfName(this.fontName + this.style));
/*      */     } else {
/* 1301 */       dic.put(PdfName.SUBTYPE, PdfName.TRUETYPE);
/* 1302 */       dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + this.style));
/*      */     } 
/* 1304 */     if (!this.fontSpecific) {
/* 1305 */       for (int i = firstChar; i <= lastChar; i++) {
/* 1306 */         if (!this.differences[i].equals(".notdef")) {
/* 1307 */           firstChar = i;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1311 */       if (this.encoding.equals("Cp1252") || this.encoding.equals("MacRoman")) {
/* 1312 */         dic.put(PdfName.ENCODING, this.encoding.equals("Cp1252") ? PdfName.WIN_ANSI_ENCODING : PdfName.MAC_ROMAN_ENCODING);
/*      */       } else {
/* 1314 */         PdfDictionary enc = new PdfDictionary(PdfName.ENCODING);
/* 1315 */         PdfArray dif = new PdfArray();
/* 1316 */         boolean gap = true;
/* 1317 */         for (int j = firstChar; j <= lastChar; j++) {
/* 1318 */           if (shortTag[j] != 0) {
/* 1319 */             if (gap) {
/* 1320 */               dif.add(new PdfNumber(j));
/* 1321 */               gap = false;
/*      */             } 
/* 1323 */             dif.add(new PdfName(this.differences[j]));
/*      */           } else {
/* 1325 */             gap = true;
/*      */           } 
/* 1327 */         }  enc.put(PdfName.DIFFERENCES, dif);
/* 1328 */         dic.put(PdfName.ENCODING, enc);
/*      */       } 
/*      */     } 
/* 1331 */     dic.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
/* 1332 */     dic.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
/* 1333 */     PdfArray wd = new PdfArray();
/* 1334 */     for (int k = firstChar; k <= lastChar; k++) {
/* 1335 */       if (shortTag[k] == 0) {
/* 1336 */         wd.add(new PdfNumber(0));
/*      */       } else {
/* 1338 */         wd.add(new PdfNumber(this.widths[k]));
/*      */       } 
/* 1340 */     }  dic.put(PdfName.WIDTHS, wd);
/* 1341 */     if (fontDescriptor != null)
/* 1342 */       dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor); 
/* 1343 */     return dic;
/*      */   }
/*      */   
/*      */   protected byte[] getFullFont() throws IOException {
/* 1347 */     RandomAccessFileOrArray rf2 = null;
/*      */     try {
/* 1349 */       rf2 = new RandomAccessFileOrArray(this.rf);
/* 1350 */       rf2.reOpen();
/* 1351 */       byte[] b = new byte[(int)rf2.length()];
/* 1352 */       rf2.readFully(b);
/* 1353 */       return b;
/*      */     } finally {
/*      */       try {
/* 1356 */         if (rf2 != null) {
/* 1357 */           rf2.close();
/*      */         }
/* 1359 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected synchronized byte[] getSubSet(HashSet<Integer> glyphs, boolean subsetp) throws IOException, DocumentException {
/* 1365 */     TrueTypeFontSubSet sb = new TrueTypeFontSubSet(this.fileName, new RandomAccessFileOrArray(this.rf), glyphs, this.directoryOffset, true, !subsetp);
/* 1366 */     return sb.process();
/*      */   }
/*      */   
/*      */   protected static int[] compactRanges(ArrayList<int[]> ranges) {
/* 1370 */     ArrayList<int[]> simp = (ArrayList)new ArrayList<int>();
/* 1371 */     for (int k = 0; k < ranges.size(); k++) {
/* 1372 */       int[] r = ranges.get(k);
/* 1373 */       for (int j = 0; j < r.length; j += 2) {
/* 1374 */         simp.add(new int[] { Math.max(0, Math.min(r[j], r[j + 1])), Math.min(65535, Math.max(r[j], r[j + 1])) });
/*      */       } 
/*      */     } 
/* 1377 */     for (int k1 = 0; k1 < simp.size() - 1; k1++) {
/* 1378 */       for (int k2 = k1 + 1; k2 < simp.size(); k2++) {
/* 1379 */         int[] r1 = simp.get(k1);
/* 1380 */         int[] r2 = simp.get(k2);
/* 1381 */         if ((r1[0] >= r2[0] && r1[0] <= r2[1]) || (r1[1] >= r2[0] && r1[0] <= r2[1])) {
/* 1382 */           r1[0] = Math.min(r1[0], r2[0]);
/* 1383 */           r1[1] = Math.max(r1[1], r2[1]);
/* 1384 */           simp.remove(k2);
/* 1385 */           k2--;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1389 */     int[] s = new int[simp.size() * 2];
/* 1390 */     for (int i = 0; i < simp.size(); i++) {
/* 1391 */       int[] r = simp.get(i);
/* 1392 */       s[i * 2] = r[0];
/* 1393 */       s[i * 2 + 1] = r[1];
/*      */     } 
/* 1395 */     return s;
/*      */   }
/*      */   
/*      */   protected void addRangeUni(HashMap<Integer, int[]> longTag, boolean includeMetrics, boolean subsetp) {
/* 1399 */     if (!subsetp && (this.subsetRanges != null || this.directoryOffset > 0)) {
/* 1400 */       HashMap<Integer, int[]> usemap; (new int[2])[0] = 0; (new int[2])[1] = 65535; int[] rg = (this.subsetRanges == null && this.directoryOffset > 0) ? new int[2] : compactRanges(this.subsetRanges);
/*      */       
/* 1402 */       if (!this.fontSpecific && this.cmap31 != null) {
/* 1403 */         usemap = this.cmap31;
/* 1404 */       } else if (this.fontSpecific && this.cmap10 != null) {
/* 1405 */         usemap = this.cmap10;
/* 1406 */       } else if (this.cmap31 != null) {
/* 1407 */         usemap = this.cmap31;
/*      */       } else {
/* 1409 */         usemap = this.cmap10;
/* 1410 */       }  for (Map.Entry<Integer, int[]> e : usemap.entrySet()) {
/* 1411 */         int[] v = e.getValue();
/* 1412 */         Integer gi = Integer.valueOf(v[0]);
/* 1413 */         if (longTag.containsKey(gi))
/*      */           continue; 
/* 1415 */         int c = ((Integer)e.getKey()).intValue();
/* 1416 */         boolean skip = true;
/* 1417 */         for (int k = 0; k < rg.length; k += 2) {
/* 1418 */           if (c >= rg[k] && c <= rg[k + 1]) {
/* 1419 */             skip = false;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1423 */         if (!skip) {
/* 1424 */           (new int[3])[0] = v[0]; (new int[3])[1] = v[1]; (new int[3])[2] = c; longTag.put(gi, includeMetrics ? new int[3] : null);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected void addRangeUni(HashSet<Integer> longTag, boolean subsetp) {
/* 1430 */     if (!subsetp && (this.subsetRanges != null || this.directoryOffset > 0)) {
/* 1431 */       HashMap<Integer, int[]> usemap; (new int[2])[0] = 0; (new int[2])[1] = 65535; int[] rg = (this.subsetRanges == null && this.directoryOffset > 0) ? new int[2] : compactRanges(this.subsetRanges);
/*      */       
/* 1433 */       if (!this.fontSpecific && this.cmap31 != null) {
/* 1434 */         usemap = this.cmap31;
/* 1435 */       } else if (this.fontSpecific && this.cmap10 != null) {
/* 1436 */         usemap = this.cmap10;
/* 1437 */       } else if (this.cmap31 != null) {
/* 1438 */         usemap = this.cmap31;
/*      */       } else {
/* 1440 */         usemap = this.cmap10;
/* 1441 */       }  for (Map.Entry<Integer, int[]> e : usemap.entrySet()) {
/* 1442 */         int[] v = e.getValue();
/* 1443 */         Integer gi = Integer.valueOf(v[0]);
/* 1444 */         if (longTag.contains(gi))
/*      */           continue; 
/* 1446 */         int c = ((Integer)e.getKey()).intValue();
/* 1447 */         boolean skip = true;
/* 1448 */         for (int k = 0; k < rg.length; k += 2) {
/* 1449 */           if (c >= rg[k] && c <= rg[k + 1]) {
/* 1450 */             skip = false;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1454 */         if (!skip) {
/* 1455 */           longTag.add(gi);
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
/*      */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
/* 1471 */     int firstChar = ((Integer)params[0]).intValue();
/* 1472 */     int lastChar = ((Integer)params[1]).intValue();
/* 1473 */     byte[] shortTag = (byte[])params[2];
/* 1474 */     boolean subsetp = (((Boolean)params[3]).booleanValue() && this.subset);
/*      */     
/* 1476 */     if (!subsetp) {
/* 1477 */       firstChar = 0;
/* 1478 */       lastChar = shortTag.length - 1;
/* 1479 */       for (int k = 0; k < shortTag.length; k++)
/* 1480 */         shortTag[k] = 1; 
/*      */     } 
/* 1482 */     PdfIndirectReference ind_font = null;
/* 1483 */     PdfObject pobj = null;
/* 1484 */     PdfIndirectObject obj = null;
/* 1485 */     String subsetPrefix = "";
/* 1486 */     if (this.embedded) {
/* 1487 */       if (this.cff) {
/* 1488 */         pobj = new BaseFont.StreamFont(readCffFont(), "Type1C", this.compressionLevel);
/* 1489 */         obj = writer.addToBody(pobj);
/* 1490 */         ind_font = obj.getIndirectReference();
/*      */       } else {
/* 1492 */         if (subsetp)
/* 1493 */           subsetPrefix = createSubsetPrefix(); 
/* 1494 */         HashSet<Integer> glyphs = new HashSet<Integer>();
/* 1495 */         for (int k = firstChar; k <= lastChar; k++) {
/* 1496 */           if (shortTag[k] != 0) {
/* 1497 */             int[] metrics = null;
/* 1498 */             if (this.specialMap != null) {
/* 1499 */               int[] cd = GlyphList.nameToUnicode(this.differences[k]);
/* 1500 */               if (cd != null) {
/* 1501 */                 metrics = getMetricsTT(cd[0]);
/*      */               }
/* 1503 */             } else if (this.fontSpecific) {
/* 1504 */               metrics = getMetricsTT(k);
/*      */             } else {
/* 1506 */               metrics = getMetricsTT(this.unicodeDifferences[k]);
/*      */             } 
/* 1508 */             if (metrics != null)
/* 1509 */               glyphs.add(Integer.valueOf(metrics[0])); 
/*      */           } 
/*      */         } 
/* 1512 */         addRangeUni(glyphs, subsetp);
/* 1513 */         byte[] b = null;
/* 1514 */         if (subsetp || this.directoryOffset != 0 || this.subsetRanges != null) {
/* 1515 */           b = getSubSet(new HashSet<Integer>(glyphs), subsetp);
/*      */         } else {
/* 1517 */           b = getFullFont();
/*      */         } 
/* 1519 */         int[] lengths = { b.length };
/* 1520 */         pobj = new BaseFont.StreamFont(b, lengths, this.compressionLevel);
/* 1521 */         obj = writer.addToBody(pobj);
/* 1522 */         ind_font = obj.getIndirectReference();
/*      */       } 
/*      */     }
/* 1525 */     pobj = getFontDescriptor(ind_font, subsetPrefix, (PdfIndirectReference)null);
/* 1526 */     if (pobj != null) {
/* 1527 */       obj = writer.addToBody(pobj);
/* 1528 */       ind_font = obj.getIndirectReference();
/*      */     } 
/* 1530 */     pobj = getFontBaseType(ind_font, subsetPrefix, firstChar, lastChar, shortTag);
/* 1531 */     writer.addToBody(pobj, ref);
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
/*      */   protected byte[] readCffFont() throws IOException {
/* 1543 */     RandomAccessFileOrArray rf2 = new RandomAccessFileOrArray(this.rf);
/* 1544 */     byte[] b = new byte[this.cffLength];
/*      */     try {
/* 1546 */       rf2.reOpen();
/* 1547 */       rf2.seek(this.cffOffset);
/* 1548 */       rf2.readFully(b);
/*      */     } finally {
/*      */       try {
/* 1551 */         rf2.close();
/* 1552 */       } catch (Exception exception) {}
/*      */     } 
/*      */ 
/*      */     
/* 1556 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfStream getFullFontStream() throws IOException, DocumentException {
/* 1567 */     if (this.cff) {
/* 1568 */       return new BaseFont.StreamFont(readCffFont(), "Type1C", this.compressionLevel);
/*      */     }
/* 1570 */     byte[] b = getFullFont();
/* 1571 */     int[] lengths = { b.length };
/* 1572 */     return new BaseFont.StreamFont(b, lengths, this.compressionLevel);
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
/*      */   public float getFontDescriptor(int key, float fontSize) {
/* 1587 */     switch (key) {
/*      */       case 1:
/* 1589 */         return this.os_2.sTypoAscender * fontSize / this.head.unitsPerEm;
/*      */       case 2:
/* 1591 */         return this.os_2.sCapHeight * fontSize / this.head.unitsPerEm;
/*      */       case 3:
/* 1593 */         return this.os_2.sTypoDescender * fontSize / this.head.unitsPerEm;
/*      */       case 4:
/* 1595 */         return (float)this.italicAngle;
/*      */       case 5:
/* 1597 */         return fontSize * this.head.xMin / this.head.unitsPerEm;
/*      */       case 6:
/* 1599 */         return fontSize * this.head.yMin / this.head.unitsPerEm;
/*      */       case 7:
/* 1601 */         return fontSize * this.head.xMax / this.head.unitsPerEm;
/*      */       case 8:
/* 1603 */         return fontSize * this.head.yMax / this.head.unitsPerEm;
/*      */       case 9:
/* 1605 */         return fontSize * this.hhea.Ascender / this.head.unitsPerEm;
/*      */       case 10:
/* 1607 */         return fontSize * this.hhea.Descender / this.head.unitsPerEm;
/*      */       case 11:
/* 1609 */         return fontSize * this.hhea.LineGap / this.head.unitsPerEm;
/*      */       case 12:
/* 1611 */         return fontSize * this.hhea.advanceWidthMax / this.head.unitsPerEm;
/*      */       case 13:
/* 1613 */         return (this.underlinePosition - this.underlineThickness / 2) * fontSize / this.head.unitsPerEm;
/*      */       case 14:
/* 1615 */         return this.underlineThickness * fontSize / this.head.unitsPerEm;
/*      */       case 15:
/* 1617 */         return this.os_2.yStrikeoutPosition * fontSize / this.head.unitsPerEm;
/*      */       case 16:
/* 1619 */         return this.os_2.yStrikeoutSize * fontSize / this.head.unitsPerEm;
/*      */       case 17:
/* 1621 */         return this.os_2.ySubscriptYSize * fontSize / this.head.unitsPerEm;
/*      */       case 18:
/* 1623 */         return -this.os_2.ySubscriptYOffset * fontSize / this.head.unitsPerEm;
/*      */       case 19:
/* 1625 */         return this.os_2.ySuperscriptYSize * fontSize / this.head.unitsPerEm;
/*      */       case 20:
/* 1627 */         return this.os_2.ySuperscriptYOffset * fontSize / this.head.unitsPerEm;
/*      */       case 21:
/* 1629 */         return this.os_2.usWeightClass;
/*      */       case 22:
/* 1631 */         return this.os_2.usWidthClass;
/*      */     } 
/* 1633 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getMetricsTT(int c) {
/* 1643 */     if (this.cmapExt != null)
/* 1644 */       return this.cmapExt.get(Integer.valueOf(c)); 
/* 1645 */     if (!this.fontSpecific && this.cmap31 != null)
/* 1646 */       return this.cmap31.get(Integer.valueOf(c)); 
/* 1647 */     if (this.fontSpecific && this.cmap10 != null)
/* 1648 */       return this.cmap10.get(Integer.valueOf(c)); 
/* 1649 */     if (this.cmap31 != null)
/* 1650 */       return this.cmap31.get(Integer.valueOf(c)); 
/* 1651 */     if (this.cmap10 != null)
/* 1652 */       return this.cmap10.get(Integer.valueOf(c)); 
/* 1653 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPostscriptFontName() {
/* 1663 */     return this.fontName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCodePagesSupported() {
/* 1673 */     long cp = (this.os_2.ulCodePageRange2 << 32L) + (this.os_2.ulCodePageRange1 & 0xFFFFFFFFL);
/* 1674 */     int count = 0;
/* 1675 */     long bit = 1L;
/* 1676 */     for (int k = 0; k < 64; k++) {
/* 1677 */       if ((cp & bit) != 0L && codePages[k] != null)
/* 1678 */         count++; 
/* 1679 */       bit <<= 1L;
/*      */     } 
/* 1681 */     String[] ret = new String[count];
/* 1682 */     count = 0;
/* 1683 */     bit = 1L;
/* 1684 */     for (int i = 0; i < 64; i++) {
/* 1685 */       if ((cp & bit) != 0L && codePages[i] != null)
/* 1686 */         ret[count++] = codePages[i]; 
/* 1687 */       bit <<= 1L;
/*      */     } 
/* 1689 */     return ret;
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
/*      */   public String[][] getFullFontName() {
/* 1704 */     return this.fullName;
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
/*      */   public String getSubfamily() {
/* 1717 */     if (this.subFamily != null && this.subFamily.length > 0) {
/* 1718 */       return this.subFamily[0][3];
/*      */     }
/* 1720 */     return super.getSubfamily();
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
/*      */   public String[][] getAllNameEntries() {
/* 1735 */     return this.allNameEntries;
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
/*      */   public String[][] getFamilyFontName() {
/* 1750 */     return this.familyName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasKernPairs() {
/* 1760 */     return (this.kerning.size() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPostscriptFontName(String name) {
/* 1771 */     this.fontName = name;
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
/*      */   public boolean setKerning(int char1, int char2, int kern) {
/* 1784 */     int[] metrics = getMetricsTT(char1);
/* 1785 */     if (metrics == null)
/* 1786 */       return false; 
/* 1787 */     int c1 = metrics[0];
/* 1788 */     metrics = getMetricsTT(char2);
/* 1789 */     if (metrics == null)
/* 1790 */       return false; 
/* 1791 */     int c2 = metrics[0];
/* 1792 */     this.kerning.put((c1 << 16) + c2, kern);
/* 1793 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int[] getRawCharBBox(int c, String name) {
/* 1798 */     HashMap<Integer, int[]> map = null;
/* 1799 */     if (name == null || this.cmap31 == null) {
/* 1800 */       map = this.cmap10;
/*      */     } else {
/* 1802 */       map = this.cmap31;
/* 1803 */     }  if (map == null)
/* 1804 */       return null; 
/* 1805 */     int[] metric = map.get(Integer.valueOf(c));
/* 1806 */     if (metric == null || this.bboxes == null)
/* 1807 */       return null; 
/* 1808 */     return this.bboxes[metric[0]];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isWinAnsiSupported() {
/* 1817 */     return (this.cmap10 != null);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/TrueTypeFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */