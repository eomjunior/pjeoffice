/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.ExceptionConverter;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CFFFont
/*      */ {
/*   85 */   static final String[] operatorNames = new String[] { "version", "Notice", "FullName", "FamilyName", "Weight", "FontBBox", "BlueValues", "OtherBlues", "FamilyBlues", "FamilyOtherBlues", "StdHW", "StdVW", "UNKNOWN_12", "UniqueID", "XUID", "charset", "Encoding", "CharStrings", "Private", "Subrs", "defaultWidthX", "nominalWidthX", "UNKNOWN_22", "UNKNOWN_23", "UNKNOWN_24", "UNKNOWN_25", "UNKNOWN_26", "UNKNOWN_27", "UNKNOWN_28", "UNKNOWN_29", "UNKNOWN_30", "UNKNOWN_31", "Copyright", "isFixedPitch", "ItalicAngle", "UnderlinePosition", "UnderlineThickness", "PaintType", "CharstringType", "FontMatrix", "StrokeWidth", "BlueScale", "BlueShift", "BlueFuzz", "StemSnapH", "StemSnapV", "ForceBold", "UNKNOWN_12_15", "UNKNOWN_12_16", "LanguageGroup", "ExpansionFactor", "initialRandomSeed", "SyntheticBase", "PostScript", "BaseFontName", "BaseFontBlend", "UNKNOWN_12_24", "UNKNOWN_12_25", "UNKNOWN_12_26", "UNKNOWN_12_27", "UNKNOWN_12_28", "UNKNOWN_12_29", "ROS", "CIDFontVersion", "CIDFontRevision", "CIDFontType", "CIDCount", "UIDBase", "FDArray", "FDSelect", "FontName" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   static final String[] standardStrings = new String[] { ".notdef", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quoteright", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "exclamdown", "cent", "sterling", "fraction", "yen", "florin", "section", "currency", "quotesingle", "quotedblleft", "guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl", "endash", "dagger", "daggerdbl", "periodcentered", "paragraph", "bullet", "quotesinglbase", "quotedblbase", "quotedblright", "guillemotright", "ellipsis", "perthousand", "questiondown", "grave", "acute", "circumflex", "tilde", "macron", "breve", "dotaccent", "dieresis", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "emdash", "AE", "ordfeminine", "Lslash", "Oslash", "OE", "ordmasculine", "ae", "dotlessi", "lslash", "oslash", "oe", "germandbls", "onesuperior", "logicalnot", "mu", "trademark", "Eth", "onehalf", "plusminus", "Thorn", "onequarter", "divide", "brokenbar", "degree", "thorn", "threequarters", "twosuperior", "registered", "minus", "eth", "multiply", "threesuperior", "copyright", "Aacute", "Acircumflex", "Adieresis", "Agrave", "Aring", "Atilde", "Ccedilla", "Eacute", "Ecircumflex", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Ntilde", "Oacute", "Ocircumflex", "Odieresis", "Ograve", "Otilde", "Scaron", "Uacute", "Ucircumflex", "Udieresis", "Ugrave", "Yacute", "Ydieresis", "Zcaron", "aacute", "acircumflex", "adieresis", "agrave", "aring", "atilde", "ccedilla", "eacute", "ecircumflex", "edieresis", "egrave", "iacute", "icircumflex", "idieresis", "igrave", "ntilde", "oacute", "ocircumflex", "odieresis", "ograve", "otilde", "scaron", "uacute", "ucircumflex", "udieresis", "ugrave", "yacute", "ydieresis", "zcaron", "exclamsmall", "Hungarumlautsmall", "dollaroldstyle", "dollarsuperior", "ampersandsmall", "Acutesmall", "parenleftsuperior", "parenrightsuperior", "twodotenleader", "onedotenleader", "zerooldstyle", "oneoldstyle", "twooldstyle", "threeoldstyle", "fouroldstyle", "fiveoldstyle", "sixoldstyle", "sevenoldstyle", "eightoldstyle", "nineoldstyle", "commasuperior", "threequartersemdash", "periodsuperior", "questionsmall", "asuperior", "bsuperior", "centsuperior", "dsuperior", "esuperior", "isuperior", "lsuperior", "msuperior", "nsuperior", "osuperior", "rsuperior", "ssuperior", "tsuperior", "ff", "ffi", "ffl", "parenleftinferior", "parenrightinferior", "Circumflexsmall", "hyphensuperior", "Gravesmall", "Asmall", "Bsmall", "Csmall", "Dsmall", "Esmall", "Fsmall", "Gsmall", "Hsmall", "Ismall", "Jsmall", "Ksmall", "Lsmall", "Msmall", "Nsmall", "Osmall", "Psmall", "Qsmall", "Rsmall", "Ssmall", "Tsmall", "Usmall", "Vsmall", "Wsmall", "Xsmall", "Ysmall", "Zsmall", "colonmonetary", "onefitted", "rupiah", "Tildesmall", "exclamdownsmall", "centoldstyle", "Lslashsmall", "Scaronsmall", "Zcaronsmall", "Dieresissmall", "Brevesmall", "Caronsmall", "Dotaccentsmall", "Macronsmall", "figuredash", "hypheninferior", "Ogoneksmall", "Ringsmall", "Cedillasmall", "questiondownsmall", "oneeighth", "threeeighths", "fiveeighths", "seveneighths", "onethird", "twothirds", "zerosuperior", "foursuperior", "fivesuperior", "sixsuperior", "sevensuperior", "eightsuperior", "ninesuperior", "zeroinferior", "oneinferior", "twoinferior", "threeinferior", "fourinferior", "fiveinferior", "sixinferior", "seveninferior", "eightinferior", "nineinferior", "centinferior", "dollarinferior", "periodinferior", "commainferior", "Agravesmall", "Aacutesmall", "Acircumflexsmall", "Atildesmall", "Adieresissmall", "Aringsmall", "AEsmall", "Ccedillasmall", "Egravesmall", "Eacutesmall", "Ecircumflexsmall", "Edieresissmall", "Igravesmall", "Iacutesmall", "Icircumflexsmall", "Idieresissmall", "Ethsmall", "Ntildesmall", "Ogravesmall", "Oacutesmall", "Ocircumflexsmall", "Otildesmall", "Odieresissmall", "OEsmall", "Oslashsmall", "Ugravesmall", "Uacutesmall", "Ucircumflexsmall", "Udieresissmall", "Yacutesmall", "Thornsmall", "Ydieresissmall", "001.000", "001.001", "001.002", "001.003", "Black", "Bold", "Book", "Light", "Medium", "Regular", "Roman", "Semibold" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int nextIndexOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(char sid) {
/*  180 */     if (sid < standardStrings.length) return standardStrings[sid]; 
/*  181 */     if (sid >= standardStrings.length + this.stringOffsets.length - 1) return null; 
/*  182 */     int j = sid - standardStrings.length;
/*      */     
/*  184 */     int p = getPosition();
/*  185 */     seek(this.stringOffsets[j]);
/*  186 */     StringBuffer s = new StringBuffer();
/*  187 */     for (int k = this.stringOffsets[j]; k < this.stringOffsets[j + 1]; k++) {
/*  188 */       s.append(getCard8());
/*      */     }
/*  190 */     seek(p);
/*  191 */     return s.toString();
/*      */   }
/*      */   
/*      */   char getCard8() {
/*      */     try {
/*  196 */       byte i = this.buf.readByte();
/*  197 */       return (char)(i & 0xFF);
/*      */     }
/*  199 */     catch (Exception e) {
/*  200 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   char getCard16() {
/*      */     try {
/*  206 */       return this.buf.readChar();
/*      */     }
/*  208 */     catch (Exception e) {
/*  209 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   int getOffset(int offSize) {
/*  214 */     int offset = 0;
/*  215 */     for (int i = 0; i < offSize; i++) {
/*  216 */       offset *= 256;
/*  217 */       offset += getCard8();
/*      */     } 
/*  219 */     return offset;
/*      */   }
/*      */   
/*      */   void seek(int offset) {
/*      */     try {
/*  224 */       this.buf.seek(offset);
/*      */     }
/*  226 */     catch (Exception e) {
/*  227 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   short getShort() {
/*      */     try {
/*  233 */       return this.buf.readShort();
/*      */     }
/*  235 */     catch (Exception e) {
/*  236 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   int getInt() {
/*      */     try {
/*  242 */       return this.buf.readInt();
/*      */     }
/*  244 */     catch (Exception e) {
/*  245 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   int getPosition() {
/*      */     try {
/*  251 */       return (int)this.buf.getFilePointer();
/*      */     }
/*  253 */     catch (Exception e) {
/*  254 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int[] getIndex(int nextIndexOffset) {
/*  265 */     seek(nextIndexOffset);
/*  266 */     int count = getCard16();
/*  267 */     int[] offsets = new int[count + 1];
/*      */     
/*  269 */     if (count == 0) {
/*  270 */       offsets[0] = -1;
/*  271 */       nextIndexOffset += 2;
/*  272 */       return offsets;
/*      */     } 
/*      */     
/*  275 */     int indexOffSize = getCard8();
/*      */     
/*  277 */     for (int j = 0; j <= count; j++)
/*      */     {
/*  279 */       offsets[j] = nextIndexOffset + 2 + 1 + (count + 1) * indexOffSize - 1 + 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  287 */         getOffset(indexOffSize);
/*      */     }
/*      */     
/*  290 */     return offsets;
/*      */   }
/*      */ 
/*      */   
/*  294 */   protected Object[] args = new Object[48];
/*  295 */   protected int arg_count = 0; protected RandomAccessFileOrArray buf; private int offSize;
/*      */   
/*      */   protected void getDictItem() {
/*  298 */     for (int i = 0; i < this.arg_count; ) { this.args[i] = null; i++; }
/*  299 */      this.arg_count = 0;
/*  300 */     this.key = null;
/*  301 */     boolean gotKey = false;
/*      */     
/*  303 */     while (!gotKey) {
/*  304 */       char b0 = getCard8();
/*  305 */       if (b0 == '\035') {
/*  306 */         int item = getInt();
/*  307 */         this.args[this.arg_count] = Integer.valueOf(item);
/*  308 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  312 */       if (b0 == '\034') {
/*  313 */         short item = getShort();
/*  314 */         this.args[this.arg_count] = Integer.valueOf(item);
/*  315 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  319 */       if (b0 >= ' ' && b0 <= 'ö') {
/*  320 */         byte item = (byte)(b0 - 139);
/*  321 */         this.args[this.arg_count] = Integer.valueOf(item);
/*  322 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  326 */       if (b0 >= '÷' && b0 <= 'ú') {
/*  327 */         char b1 = getCard8();
/*  328 */         short item = (short)((b0 - 247) * 256 + b1 + 108);
/*  329 */         this.args[this.arg_count] = Integer.valueOf(item);
/*  330 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  334 */       if (b0 >= 'û' && b0 <= 'þ') {
/*  335 */         char b1 = getCard8();
/*  336 */         short item = (short)(-(b0 - 251) * 256 - b1 - 108);
/*  337 */         this.args[this.arg_count] = Integer.valueOf(item);
/*  338 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  342 */       if (b0 == '\036') {
/*  343 */         StringBuilder item = new StringBuilder("");
/*  344 */         boolean done = false;
/*  345 */         char buffer = Character.MIN_VALUE;
/*  346 */         byte avail = 0;
/*  347 */         int nibble = 0;
/*  348 */         while (!done) {
/*      */           
/*  350 */           if (avail == 0) { buffer = getCard8(); avail = 2; }
/*  351 */            if (avail == 1) { nibble = buffer / 16; avail = (byte)(avail - 1); }
/*  352 */            if (avail == 2) { nibble = buffer % 16; avail = (byte)(avail - 1); }
/*  353 */            switch (nibble) { case 10:
/*  354 */               item.append("."); continue;
/*  355 */             case 11: item.append("E"); continue;
/*  356 */             case 12: item.append("E-"); continue;
/*  357 */             case 14: item.append("-"); continue;
/*  358 */             case 15: done = true; continue; }
/*      */           
/*  360 */           if (nibble >= 0 && nibble <= 9) {
/*  361 */             item.append(String.valueOf(nibble)); continue;
/*      */           } 
/*  363 */           item.append("<NIBBLE ERROR: ").append(nibble).append('>');
/*  364 */           done = true;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  369 */         this.args[this.arg_count] = item.toString();
/*  370 */         this.arg_count++;
/*      */         
/*      */         continue;
/*      */       } 
/*  374 */       if (b0 <= '\025') {
/*  375 */         gotKey = true;
/*  376 */         if (b0 != '\f') { this.key = operatorNames[b0]; continue; }
/*  377 */          this.key = operatorNames[32 + getCard8()];
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected int nameIndexOffset; protected int topdictIndexOffset;
/*      */   protected int stringIndexOffset;
/*      */   protected int gsubrIndexOffset;
/*      */   protected int[] nameOffsets;
/*      */   protected int[] topdictOffsets;
/*      */   protected int[] stringOffsets;
/*      */   protected int[] gsubrOffsets;
/*      */   protected Font[] fonts;
/*      */   
/*  390 */   protected static abstract class Item { protected int myOffset = -1;
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  393 */       this.myOffset = currentOffset[0];
/*      */     }
/*      */     
/*      */     public void emit(byte[] buffer) {}
/*      */     
/*      */     public void xref() {} }
/*      */ 
/*      */   
/*      */   protected static abstract class OffsetItem
/*      */     extends Item {
/*      */     public int value;
/*      */     
/*      */     public void set(int offset) {
/*  406 */       this.value = offset;
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class RangeItem
/*      */     extends Item {
/*      */     public int offset;
/*      */     public int length;
/*      */     private RandomAccessFileOrArray buf;
/*      */     
/*      */     public RangeItem(RandomAccessFileOrArray buf, int offset, int length) {
/*  417 */       this.offset = offset;
/*  418 */       this.length = length;
/*  419 */       this.buf = buf;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  423 */       super.increment(currentOffset);
/*  424 */       currentOffset[0] = currentOffset[0] + this.length;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*      */       try {
/*  430 */         this.buf.seek(this.offset);
/*  431 */         for (int i = this.myOffset; i < this.myOffset + this.length; i++) {
/*  432 */           buffer[i] = this.buf.readByte();
/*      */         }
/*  434 */       } catch (Exception e) {
/*  435 */         throw new ExceptionConverter(e);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final class IndexOffsetItem
/*      */     extends OffsetItem
/*      */   {
/*      */     public final int size;
/*      */ 
/*      */     
/*      */     public IndexOffsetItem(int size, int value)
/*      */     {
/*  449 */       this.size = size; this.value = value; } public IndexOffsetItem(int size) {
/*  450 */       this.size = size;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  454 */       super.increment(currentOffset);
/*  455 */       currentOffset[0] = currentOffset[0] + this.size;
/*      */     }
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  459 */       int i = 0;
/*  460 */       switch (this.size) {
/*      */         case 4:
/*  462 */           buffer[this.myOffset + i] = (byte)(this.value >>> 24 & 0xFF);
/*  463 */           i++;
/*      */         case 3:
/*  465 */           buffer[this.myOffset + i] = (byte)(this.value >>> 16 & 0xFF);
/*  466 */           i++;
/*      */         case 2:
/*  468 */           buffer[this.myOffset + i] = (byte)(this.value >>> 8 & 0xFF);
/*  469 */           i++;
/*      */         case 1:
/*  471 */           buffer[this.myOffset + i] = (byte)(this.value >>> 0 & 0xFF);
/*  472 */           i++;
/*      */           break;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected static final class IndexBaseItem
/*      */     extends Item {}
/*      */ 
/*      */   
/*      */   protected static final class IndexMarkerItem
/*      */     extends Item
/*      */   {
/*      */     private CFFFont.OffsetItem offItem;
/*      */     
/*      */     private CFFFont.IndexBaseItem indexBase;
/*      */ 
/*      */     
/*      */     public IndexMarkerItem(CFFFont.OffsetItem offItem, CFFFont.IndexBaseItem indexBase) {
/*  492 */       this.offItem = offItem;
/*  493 */       this.indexBase = indexBase;
/*      */     }
/*      */ 
/*      */     
/*      */     public void xref() {
/*  498 */       this.offItem.set(this.myOffset - this.indexBase.myOffset + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class SubrMarkerItem
/*      */     extends Item
/*      */   {
/*      */     private CFFFont.OffsetItem offItem;
/*      */     private CFFFont.IndexBaseItem indexBase;
/*      */     
/*      */     public SubrMarkerItem(CFFFont.OffsetItem offItem, CFFFont.IndexBaseItem indexBase) {
/*  509 */       this.offItem = offItem;
/*  510 */       this.indexBase = indexBase;
/*      */     }
/*      */ 
/*      */     
/*      */     public void xref() {
/*  515 */       this.offItem.set(this.myOffset - this.indexBase.myOffset);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class DictOffsetItem
/*      */     extends OffsetItem
/*      */   {
/*  525 */     public final int size = 5;
/*      */ 
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  529 */       super.increment(currentOffset);
/*  530 */       currentOffset[0] = currentOffset[0] + this.size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  535 */       if (this.size == 5) {
/*  536 */         buffer[this.myOffset] = 29;
/*  537 */         buffer[this.myOffset + 1] = (byte)(this.value >>> 24 & 0xFF);
/*  538 */         buffer[this.myOffset + 2] = (byte)(this.value >>> 16 & 0xFF);
/*  539 */         buffer[this.myOffset + 3] = (byte)(this.value >>> 8 & 0xFF);
/*  540 */         buffer[this.myOffset + 4] = (byte)(this.value >>> 0 & 0xFF);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class UInt24Item
/*      */     extends Item {
/*      */     public int value;
/*      */     
/*      */     public UInt24Item(int value) {
/*  550 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  554 */       super.increment(currentOffset);
/*  555 */       currentOffset[0] = currentOffset[0] + 3;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  560 */       buffer[this.myOffset + 0] = (byte)(this.value >>> 16 & 0xFF);
/*  561 */       buffer[this.myOffset + 1] = (byte)(this.value >>> 8 & 0xFF);
/*  562 */       buffer[this.myOffset + 2] = (byte)(this.value >>> 0 & 0xFF);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class UInt32Item
/*      */     extends Item {
/*      */     public int value;
/*      */     
/*      */     public UInt32Item(int value) {
/*  571 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  575 */       super.increment(currentOffset);
/*  576 */       currentOffset[0] = currentOffset[0] + 4;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  581 */       buffer[this.myOffset + 0] = (byte)(this.value >>> 24 & 0xFF);
/*  582 */       buffer[this.myOffset + 1] = (byte)(this.value >>> 16 & 0xFF);
/*  583 */       buffer[this.myOffset + 2] = (byte)(this.value >>> 8 & 0xFF);
/*  584 */       buffer[this.myOffset + 3] = (byte)(this.value >>> 0 & 0xFF);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class UInt16Item
/*      */     extends Item {
/*      */     public char value;
/*      */     
/*      */     public UInt16Item(char value) {
/*  593 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  597 */       super.increment(currentOffset);
/*  598 */       currentOffset[0] = currentOffset[0] + 2;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  603 */       buffer[this.myOffset + 0] = (byte)(this.value >>> 8 & 0xFF);
/*  604 */       buffer[this.myOffset + 1] = (byte)(this.value >>> 0 & 0xFF);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class UInt8Item
/*      */     extends Item {
/*      */     public char value;
/*      */     
/*      */     public UInt8Item(char value) {
/*  613 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  617 */       super.increment(currentOffset);
/*  618 */       currentOffset[0] = currentOffset[0] + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  623 */       buffer[this.myOffset + 0] = (byte)(this.value >>> 0 & 0xFF);
/*      */     } }
/*      */   
/*      */   protected static final class StringItem extends Item { public String s;
/*      */     
/*      */     public StringItem(String s) {
/*  629 */       this.s = s;
/*      */     }
/*      */     
/*      */     public void increment(int[] currentOffset) {
/*  633 */       super.increment(currentOffset);
/*  634 */       currentOffset[0] = currentOffset[0] + this.s.length();
/*      */     }
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  638 */       for (int i = 0; i < this.s.length(); i++) {
/*  639 */         buffer[this.myOffset + i] = (byte)(this.s.charAt(i) & 0xFF);
/*      */       }
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class DictNumberItem
/*      */     extends Item
/*      */   {
/*      */     public final int value;
/*      */     
/*  651 */     public int size = 5; public DictNumberItem(int value) {
/*  652 */       this.value = value;
/*      */     }
/*      */     public void increment(int[] currentOffset) {
/*  655 */       super.increment(currentOffset);
/*  656 */       currentOffset[0] = currentOffset[0] + this.size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void emit(byte[] buffer) {
/*  661 */       if (this.size == 5) {
/*  662 */         buffer[this.myOffset] = 29;
/*  663 */         buffer[this.myOffset + 1] = (byte)(this.value >>> 24 & 0xFF);
/*  664 */         buffer[this.myOffset + 2] = (byte)(this.value >>> 16 & 0xFF);
/*  665 */         buffer[this.myOffset + 3] = (byte)(this.value >>> 8 & 0xFF);
/*  666 */         buffer[this.myOffset + 4] = (byte)(this.value >>> 0 & 0xFF);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected static final class MarkerItem
/*      */     extends Item
/*      */   {
/*      */     CFFFont.OffsetItem p;
/*      */     
/*      */     public MarkerItem(CFFFont.OffsetItem pointerToMarker) {
/*  677 */       this.p = pointerToMarker;
/*      */     }
/*      */     public void xref() {
/*  680 */       this.p.set(this.myOffset);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RangeItem getEntireIndexRange(int indexOffset) {
/*  691 */     seek(indexOffset);
/*  692 */     int count = getCard16();
/*  693 */     if (count == 0) {
/*  694 */       return new RangeItem(this.buf, indexOffset, 2);
/*      */     }
/*  696 */     int indexOffSize = getCard8();
/*  697 */     seek(indexOffset + 2 + 1 + count * indexOffSize);
/*  698 */     int size = getOffset(indexOffSize) - 1;
/*  699 */     return new RangeItem(this.buf, indexOffset, 3 + (count + 1) * indexOffSize + size);
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
/*      */   public byte[] getCID(String fontName) {
/*      */     int j;
/*  720 */     for (j = 0; j < this.fonts.length && 
/*  721 */       !fontName.equals((this.fonts[j]).name); j++);
/*  722 */     if (j == this.fonts.length) return null;
/*      */     
/*  724 */     LinkedList<Item> l = new LinkedList<Item>();
/*      */ 
/*      */ 
/*      */     
/*  728 */     seek(0);
/*      */     
/*  730 */     int major = getCard8();
/*  731 */     int minor = getCard8();
/*  732 */     int hdrSize = getCard8();
/*  733 */     int offSize = getCard8();
/*  734 */     this.nextIndexOffset = hdrSize;
/*      */     
/*  736 */     l.addLast(new RangeItem(this.buf, 0, hdrSize));
/*      */     
/*  738 */     int nglyphs = -1, nstrings = -1;
/*  739 */     if (!(this.fonts[j]).isCID) {
/*      */       
/*  741 */       seek((this.fonts[j]).charstringsOffset);
/*  742 */       nglyphs = getCard16();
/*  743 */       seek(this.stringIndexOffset);
/*  744 */       nstrings = getCard16() + standardStrings.length;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  750 */     l.addLast(new UInt16Item('\001'));
/*  751 */     l.addLast(new UInt8Item('\001'));
/*  752 */     l.addLast(new UInt8Item('\001'));
/*  753 */     l.addLast(new UInt8Item((char)(1 + (this.fonts[j]).name.length())));
/*  754 */     l.addLast(new StringItem((this.fonts[j]).name));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  759 */     l.addLast(new UInt16Item('\001'));
/*  760 */     l.addLast(new UInt8Item('\002'));
/*  761 */     l.addLast(new UInt16Item('\001'));
/*  762 */     OffsetItem topdictIndex1Ref = new IndexOffsetItem(2);
/*  763 */     l.addLast(topdictIndex1Ref);
/*  764 */     IndexBaseItem topdictBase = new IndexBaseItem();
/*  765 */     l.addLast(topdictBase);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  777 */     OffsetItem charsetRef = new DictOffsetItem();
/*  778 */     OffsetItem charstringsRef = new DictOffsetItem();
/*  779 */     OffsetItem fdarrayRef = new DictOffsetItem();
/*  780 */     OffsetItem fdselectRef = new DictOffsetItem();
/*      */     
/*  782 */     if (!(this.fonts[j]).isCID) {
/*      */       
/*  784 */       l.addLast(new DictNumberItem(nstrings));
/*  785 */       l.addLast(new DictNumberItem(nstrings + 1));
/*  786 */       l.addLast(new DictNumberItem(0));
/*  787 */       l.addLast(new UInt8Item('\f'));
/*  788 */       l.addLast(new UInt8Item('\036'));
/*      */       
/*  790 */       l.addLast(new DictNumberItem(nglyphs));
/*  791 */       l.addLast(new UInt8Item('\f'));
/*  792 */       l.addLast(new UInt8Item('"'));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  798 */     l.addLast(fdarrayRef);
/*  799 */     l.addLast(new UInt8Item('\f'));
/*  800 */     l.addLast(new UInt8Item('$'));
/*      */     
/*  802 */     l.addLast(fdselectRef);
/*  803 */     l.addLast(new UInt8Item('\f'));
/*  804 */     l.addLast(new UInt8Item('%'));
/*      */     
/*  806 */     l.addLast(charsetRef);
/*  807 */     l.addLast(new UInt8Item('\017'));
/*      */     
/*  809 */     l.addLast(charstringsRef);
/*  810 */     l.addLast(new UInt8Item('\021'));
/*      */     
/*  812 */     seek(this.topdictOffsets[j]);
/*  813 */     while (getPosition() < this.topdictOffsets[j + 1]) {
/*  814 */       int p1 = getPosition();
/*  815 */       getDictItem();
/*  816 */       int p2 = getPosition();
/*  817 */       if (this.key == "Encoding" || this.key == "Private" || this.key == "FDSelect" || this.key == "FDArray" || this.key == "charset" || this.key == "CharStrings") {
/*      */         continue;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  826 */       l.add(new RangeItem(this.buf, p1, p2 - p1));
/*      */     } 
/*      */ 
/*      */     
/*  830 */     l.addLast(new IndexMarkerItem(topdictIndex1Ref, topdictBase));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  836 */     if ((this.fonts[j]).isCID) {
/*  837 */       l.addLast(getEntireIndexRange(this.stringIndexOffset));
/*      */     } else {
/*  839 */       byte stringsIndexOffSize; String fdFontName = (this.fonts[j]).name + "-OneRange";
/*  840 */       if (fdFontName.length() > 127)
/*  841 */         fdFontName = fdFontName.substring(0, 127); 
/*  842 */       String extraStrings = "AdobeIdentity" + fdFontName;
/*      */       
/*  844 */       int origStringsLen = this.stringOffsets[this.stringOffsets.length - 1] - this.stringOffsets[0];
/*      */       
/*  846 */       int stringsBaseOffset = this.stringOffsets[0] - 1;
/*      */ 
/*      */       
/*  849 */       if (origStringsLen + extraStrings.length() <= 255) { stringsIndexOffSize = 1; }
/*  850 */       else if (origStringsLen + extraStrings.length() <= 65535) { stringsIndexOffSize = 2; }
/*  851 */       else if (origStringsLen + extraStrings.length() <= 16777215) { stringsIndexOffSize = 3; }
/*  852 */       else { stringsIndexOffSize = 4; }
/*      */       
/*  854 */       l.addLast(new UInt16Item((char)(this.stringOffsets.length - 1 + 3)));
/*  855 */       l.addLast(new UInt8Item((char)stringsIndexOffSize));
/*  856 */       for (int stringOffset : this.stringOffsets) {
/*  857 */         l.addLast(new IndexOffsetItem(stringsIndexOffSize, stringOffset - stringsBaseOffset));
/*      */       }
/*  859 */       int currentStringsOffset = this.stringOffsets[this.stringOffsets.length - 1] - stringsBaseOffset;
/*      */ 
/*      */       
/*  862 */       currentStringsOffset += "Adobe".length();
/*  863 */       l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/*  864 */       currentStringsOffset += "Identity".length();
/*  865 */       l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/*  866 */       currentStringsOffset += fdFontName.length();
/*  867 */       l.addLast(new IndexOffsetItem(stringsIndexOffSize, currentStringsOffset));
/*      */       
/*  869 */       l.addLast(new RangeItem(this.buf, this.stringOffsets[0], origStringsLen));
/*  870 */       l.addLast(new StringItem(extraStrings));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  875 */     l.addLast(getEntireIndexRange(this.gsubrIndexOffset));
/*      */ 
/*      */ 
/*      */     
/*  879 */     if (!(this.fonts[j]).isCID) {
/*      */ 
/*      */ 
/*      */       
/*  883 */       l.addLast(new MarkerItem(fdselectRef));
/*  884 */       l.addLast(new UInt8Item('\003'));
/*  885 */       l.addLast(new UInt16Item('\001'));
/*      */       
/*  887 */       l.addLast(new UInt16Item(false));
/*  888 */       l.addLast(new UInt8Item(false));
/*      */       
/*  890 */       l.addLast(new UInt16Item((char)nglyphs));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  895 */       l.addLast(new MarkerItem(charsetRef));
/*  896 */       l.addLast(new UInt8Item('\002'));
/*      */       
/*  898 */       l.addLast(new UInt16Item('\001'));
/*  899 */       l.addLast(new UInt16Item((char)(nglyphs - 1)));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  904 */       l.addLast(new MarkerItem(fdarrayRef));
/*  905 */       l.addLast(new UInt16Item('\001'));
/*  906 */       l.addLast(new UInt8Item('\001'));
/*  907 */       l.addLast(new UInt8Item('\001'));
/*      */       
/*  909 */       OffsetItem privateIndex1Ref = new IndexOffsetItem(1);
/*  910 */       l.addLast(privateIndex1Ref);
/*  911 */       IndexBaseItem privateBase = new IndexBaseItem();
/*  912 */       l.addLast(privateBase);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  921 */       l.addLast(new DictNumberItem((this.fonts[j]).privateLength));
/*  922 */       OffsetItem privateRef = new DictOffsetItem();
/*  923 */       l.addLast(privateRef);
/*  924 */       l.addLast(new UInt8Item('\022'));
/*      */       
/*  926 */       l.addLast(new IndexMarkerItem(privateIndex1Ref, privateBase));
/*      */ 
/*      */ 
/*      */       
/*  930 */       l.addLast(new MarkerItem(privateRef));
/*      */ 
/*      */ 
/*      */       
/*  934 */       l.addLast(new RangeItem(this.buf, (this.fonts[j]).privateOffset, (this.fonts[j]).privateLength));
/*  935 */       if ((this.fonts[j]).privateSubrs >= 0)
/*      */       {
/*  937 */         l.addLast(getEntireIndexRange((this.fonts[j]).privateSubrs));
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  943 */     l.addLast(new MarkerItem(charstringsRef));
/*  944 */     l.addLast(getEntireIndexRange((this.fonts[j]).charstringsOffset));
/*      */ 
/*      */ 
/*      */     
/*  948 */     int[] currentOffset = new int[1];
/*  949 */     currentOffset[0] = 0;
/*      */     
/*  951 */     Iterator<Item> listIter = l.iterator();
/*  952 */     while (listIter.hasNext()) {
/*  953 */       Item item = listIter.next();
/*  954 */       item.increment(currentOffset);
/*      */     } 
/*      */     
/*  957 */     listIter = l.iterator();
/*  958 */     while (listIter.hasNext()) {
/*  959 */       Item item = listIter.next();
/*  960 */       item.xref();
/*      */     } 
/*      */     
/*  963 */     int size = currentOffset[0];
/*  964 */     byte[] b = new byte[size];
/*      */     
/*  966 */     listIter = l.iterator();
/*  967 */     while (listIter.hasNext()) {
/*  968 */       Item item = listIter.next();
/*  969 */       item.emit(b);
/*      */     } 
/*      */     
/*  972 */     return b;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCID(String fontName) {
/*  978 */     for (int j = 0; j < this.fonts.length; j++) {
/*  979 */       if (fontName.equals((this.fonts[j]).name)) return (this.fonts[j]).isCID; 
/*  980 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean exists(String fontName) {
/*  985 */     for (int j = 0; j < this.fonts.length; j++) {
/*  986 */       if (fontName.equals((this.fonts[j]).name)) return true; 
/*  987 */     }  return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getNames() {
/*  992 */     String[] names = new String[this.fonts.length];
/*  993 */     for (int i = 0; i < this.fonts.length; i++)
/*  994 */       names[i] = (this.fonts[i]).name; 
/*  995 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final class Font
/*      */   {
/*      */     public String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String fullName;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isCID = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1019 */     public int privateOffset = -1;
/* 1020 */     public int privateLength = -1;
/* 1021 */     public int privateSubrs = -1;
/* 1022 */     public int charstringsOffset = -1;
/* 1023 */     public int encodingOffset = -1;
/* 1024 */     public int charsetOffset = -1;
/* 1025 */     public int fdarrayOffset = -1;
/* 1026 */     public int fdselectOffset = -1;
/*      */     
/*      */     public int[] fdprivateOffsets;
/*      */     
/*      */     public int[] fdprivateLengths;
/*      */     public int[] fdprivateSubrs;
/*      */     public int nglyphs;
/*      */     public int nstrings;
/*      */     public int CharsetLength;
/*      */     public int[] charstringsOffsets;
/*      */     public int[] charset;
/*      */     public int[] FDSelect;
/*      */     public int FDSelectLength;
/*      */     public int FDSelectFormat;
/* 1040 */     public int CharstringType = 2;
/*      */     
/*      */     public int FDArrayCount;
/*      */     
/*      */     public int FDArrayOffsize;
/*      */     
/*      */     public int[] FDArrayOffsets;
/*      */     
/*      */     public int[] PrivateSubrsOffset;
/*      */     public int[][] PrivateSubrsOffsetsArray;
/*      */     public int[] SubrsOffsets;
/*      */   }
/*      */   
/*      */   public CFFFont(RandomAccessFileOrArray inputbuffer) {
/* 1054 */     this.buf = inputbuffer;
/* 1055 */     seek(0);
/*      */ 
/*      */     
/* 1058 */     int major = getCard8();
/* 1059 */     int minor = getCard8();
/*      */ 
/*      */ 
/*      */     
/* 1063 */     int hdrSize = getCard8();
/*      */     
/* 1065 */     this.offSize = getCard8();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1071 */     this.nameIndexOffset = hdrSize;
/* 1072 */     this.nameOffsets = getIndex(this.nameIndexOffset);
/* 1073 */     this.topdictIndexOffset = this.nameOffsets[this.nameOffsets.length - 1];
/* 1074 */     this.topdictOffsets = getIndex(this.topdictIndexOffset);
/* 1075 */     this.stringIndexOffset = this.topdictOffsets[this.topdictOffsets.length - 1];
/* 1076 */     this.stringOffsets = getIndex(this.stringIndexOffset);
/* 1077 */     this.gsubrIndexOffset = this.stringOffsets[this.stringOffsets.length - 1];
/* 1078 */     this.gsubrOffsets = getIndex(this.gsubrIndexOffset);
/*      */     
/* 1080 */     this.fonts = new Font[this.nameOffsets.length - 1];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int j;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1094 */     for (j = 0; j < this.nameOffsets.length - 1; j++) {
/* 1095 */       this.fonts[j] = new Font();
/* 1096 */       seek(this.nameOffsets[j]);
/* 1097 */       (this.fonts[j]).name = "";
/* 1098 */       for (int k = this.nameOffsets[j]; k < this.nameOffsets[j + 1]; k++) {
/* 1099 */         (this.fonts[j]).name += getCard8();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1123 */     for (j = 0; j < this.topdictOffsets.length - 1; j++) {
/* 1124 */       seek(this.topdictOffsets[j]);
/* 1125 */       while (getPosition() < this.topdictOffsets[j + 1]) {
/* 1126 */         getDictItem();
/* 1127 */         if (this.key == "FullName") {
/*      */           
/* 1129 */           (this.fonts[j]).fullName = getString((char)((Integer)this.args[0]).intValue()); continue;
/*      */         } 
/* 1131 */         if (this.key == "ROS") {
/* 1132 */           (this.fonts[j]).isCID = true; continue;
/* 1133 */         }  if (this.key == "Private") {
/* 1134 */           (this.fonts[j]).privateLength = ((Integer)this.args[0]).intValue();
/* 1135 */           (this.fonts[j]).privateOffset = ((Integer)this.args[1]).intValue(); continue;
/*      */         } 
/* 1137 */         if (this.key == "charset") {
/* 1138 */           (this.fonts[j]).charsetOffset = ((Integer)this.args[0]).intValue();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1148 */         if (this.key == "CharStrings") {
/* 1149 */           (this.fonts[j]).charstringsOffset = ((Integer)this.args[0]).intValue();
/*      */ 
/*      */           
/* 1152 */           int p = getPosition();
/* 1153 */           (this.fonts[j]).charstringsOffsets = getIndex((this.fonts[j]).charstringsOffset);
/* 1154 */           seek(p); continue;
/* 1155 */         }  if (this.key == "FDArray") {
/* 1156 */           (this.fonts[j]).fdarrayOffset = ((Integer)this.args[0]).intValue(); continue;
/* 1157 */         }  if (this.key == "FDSelect") {
/* 1158 */           (this.fonts[j]).fdselectOffset = ((Integer)this.args[0]).intValue(); continue;
/* 1159 */         }  if (this.key == "CharstringType") {
/* 1160 */           (this.fonts[j]).CharstringType = ((Integer)this.args[0]).intValue();
/*      */         }
/*      */       } 
/*      */       
/* 1164 */       if ((this.fonts[j]).privateOffset >= 0) {
/*      */         
/* 1166 */         seek((this.fonts[j]).privateOffset);
/* 1167 */         while (getPosition() < (this.fonts[j]).privateOffset + (this.fonts[j]).privateLength) {
/* 1168 */           getDictItem();
/* 1169 */           if (this.key == "Subrs")
/*      */           {
/*      */             
/* 1172 */             (this.fonts[j]).privateSubrs = ((Integer)this.args[0]).intValue() + (this.fonts[j]).privateOffset;
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 1177 */       if ((this.fonts[j]).fdarrayOffset >= 0) {
/* 1178 */         int[] fdarrayOffsets = getIndex((this.fonts[j]).fdarrayOffset);
/*      */         
/* 1180 */         (this.fonts[j]).fdprivateOffsets = new int[fdarrayOffsets.length - 1];
/* 1181 */         (this.fonts[j]).fdprivateLengths = new int[fdarrayOffsets.length - 1];
/*      */ 
/*      */ 
/*      */         
/* 1185 */         for (int k = 0; k < fdarrayOffsets.length - 1; k++) {
/* 1186 */           seek(fdarrayOffsets[k]);
/* 1187 */           while (getPosition() < fdarrayOffsets[k + 1]) {
/* 1188 */             getDictItem();
/* 1189 */             if (this.key == "Private") {
/* 1190 */               (this.fonts[j]).fdprivateLengths[k] = ((Integer)this.args[0]).intValue();
/* 1191 */               (this.fonts[j]).fdprivateOffsets[k] = ((Integer)this.args[1]).intValue();
/*      */             } 
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
/*      */   void ReadEncoding(int nextIndexOffset) {
/* 1204 */     seek(nextIndexOffset);
/* 1205 */     int format = getCard8();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/CFFFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */