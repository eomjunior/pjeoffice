/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.InvalidPdfException;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class BaseFont
/*      */ {
/*      */   public static final String COURIER = "Courier";
/*      */   public static final String COURIER_BOLD = "Courier-Bold";
/*      */   public static final String COURIER_OBLIQUE = "Courier-Oblique";
/*      */   public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
/*      */   public static final String HELVETICA = "Helvetica";
/*      */   public static final String HELVETICA_BOLD = "Helvetica-Bold";
/*      */   public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
/*      */   public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";
/*      */   public static final String SYMBOL = "Symbol";
/*      */   public static final String TIMES_ROMAN = "Times-Roman";
/*      */   public static final String TIMES_BOLD = "Times-Bold";
/*      */   public static final String TIMES_ITALIC = "Times-Italic";
/*      */   public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
/*      */   public static final String ZAPFDINGBATS = "ZapfDingbats";
/*      */   public static final int ASCENT = 1;
/*      */   public static final int CAPHEIGHT = 2;
/*      */   public static final int DESCENT = 3;
/*      */   public static final int ITALICANGLE = 4;
/*      */   public static final int BBOXLLX = 5;
/*      */   public static final int BBOXLLY = 6;
/*      */   public static final int BBOXURX = 7;
/*      */   public static final int BBOXURY = 8;
/*      */   public static final int AWT_ASCENT = 9;
/*      */   public static final int AWT_DESCENT = 10;
/*      */   public static final int AWT_LEADING = 11;
/*      */   public static final int AWT_MAXADVANCE = 12;
/*      */   public static final int UNDERLINE_POSITION = 13;
/*      */   public static final int UNDERLINE_THICKNESS = 14;
/*      */   public static final int STRIKETHROUGH_POSITION = 15;
/*      */   public static final int STRIKETHROUGH_THICKNESS = 16;
/*      */   public static final int SUBSCRIPT_SIZE = 17;
/*      */   public static final int SUBSCRIPT_OFFSET = 18;
/*      */   public static final int SUPERSCRIPT_SIZE = 19;
/*      */   public static final int SUPERSCRIPT_OFFSET = 20;
/*      */   public static final int WEIGHT_CLASS = 21;
/*      */   public static final int WIDTH_CLASS = 22;
/*      */   public static final int FONT_WEIGHT = 23;
/*      */   public static final int FONT_TYPE_T1 = 0;
/*      */   public static final int FONT_TYPE_TT = 1;
/*      */   public static final int FONT_TYPE_CJK = 2;
/*      */   public static final int FONT_TYPE_TTUNI = 3;
/*      */   public static final int FONT_TYPE_DOCUMENT = 4;
/*      */   public static final int FONT_TYPE_T3 = 5;
/*      */   public static final String IDENTITY_H = "Identity-H";
/*      */   public static final String IDENTITY_V = "Identity-V";
/*      */   public static final String CP1250 = "Cp1250";
/*      */   public static final String CP1252 = "Cp1252";
/*      */   public static final String CP1257 = "Cp1257";
/*      */   public static final String WINANSI = "Cp1252";
/*      */   public static final String MACROMAN = "MacRoman";
/*  231 */   public static final int[] CHAR_RANGE_LATIN = new int[] { 0, 383, 8192, 8303, 8352, 8399, 64256, 64262 };
/*  232 */   public static final int[] CHAR_RANGE_ARABIC = new int[] { 0, 127, 1536, 1663, 8352, 8399, 64336, 64511, 65136, 65279 };
/*  233 */   public static final int[] CHAR_RANGE_HEBREW = new int[] { 0, 127, 1424, 1535, 8352, 8399, 64285, 64335 };
/*  234 */   public static final int[] CHAR_RANGE_CYRILLIC = new int[] { 0, 127, 1024, 1327, 8192, 8303, 8352, 8399 };
/*      */ 
/*      */   
/*  237 */   public static final double[] DEFAULT_FONT_MATRIX = new double[] { 0.001D, 0.0D, 0.0D, 0.001D, 0.0D, 0.0D };
/*      */ 
/*      */   
/*      */   public static final boolean EMBEDDED = true;
/*      */ 
/*      */   
/*      */   public static final boolean NOT_EMBEDDED = false;
/*      */ 
/*      */   
/*      */   public static final boolean CACHED = true;
/*      */ 
/*      */   
/*      */   public static final boolean NOT_CACHED = false;
/*      */ 
/*      */   
/*      */   public static final String RESOURCE_PATH = "com/itextpdf/text/pdf/fonts/";
/*      */ 
/*      */   
/*      */   public static final char CID_NEWLINE = '翿';
/*      */ 
/*      */   
/*      */   public static final char PARAGRAPH_SEPARATOR = ' ';
/*      */ 
/*      */   
/*      */   protected ArrayList<int[]> subsetRanges;
/*      */ 
/*      */   
/*      */   int fontType;
/*      */ 
/*      */   
/*      */   public static final String notdef = ".notdef";
/*      */   
/*  269 */   protected int[] widths = new int[256];
/*      */ 
/*      */   
/*  272 */   protected String[] differences = new String[256];
/*      */   
/*  274 */   protected char[] unicodeDifferences = new char[256];
/*      */   
/*  276 */   protected int[][] charBBoxes = new int[256][];
/*      */ 
/*      */ 
/*      */   
/*      */   protected String encoding;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean embedded;
/*      */ 
/*      */   
/*  287 */   protected int compressionLevel = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean fontSpecific = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  297 */   protected static ConcurrentHashMap<String, BaseFont> fontCache = new ConcurrentHashMap<String, BaseFont>();
/*      */ 
/*      */   
/*  300 */   protected static final HashMap<String, PdfName> BuiltinFonts14 = new HashMap<String, PdfName>();
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean forceWidthsOutput = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean directTextToByte = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean subset = true;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean fastWinansi = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected IntHashtable specialMap;
/*      */ 
/*      */   
/*      */   protected boolean vertical = false;
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  328 */     BuiltinFonts14.put("Courier", PdfName.COURIER);
/*  329 */     BuiltinFonts14.put("Courier-Bold", PdfName.COURIER_BOLD);
/*  330 */     BuiltinFonts14.put("Courier-BoldOblique", PdfName.COURIER_BOLDOBLIQUE);
/*  331 */     BuiltinFonts14.put("Courier-Oblique", PdfName.COURIER_OBLIQUE);
/*  332 */     BuiltinFonts14.put("Helvetica", PdfName.HELVETICA);
/*  333 */     BuiltinFonts14.put("Helvetica-Bold", PdfName.HELVETICA_BOLD);
/*  334 */     BuiltinFonts14.put("Helvetica-BoldOblique", PdfName.HELVETICA_BOLDOBLIQUE);
/*  335 */     BuiltinFonts14.put("Helvetica-Oblique", PdfName.HELVETICA_OBLIQUE);
/*  336 */     BuiltinFonts14.put("Symbol", PdfName.SYMBOL);
/*  337 */     BuiltinFonts14.put("Times-Roman", PdfName.TIMES_ROMAN);
/*  338 */     BuiltinFonts14.put("Times-Bold", PdfName.TIMES_BOLD);
/*  339 */     BuiltinFonts14.put("Times-BoldItalic", PdfName.TIMES_BOLDITALIC);
/*  340 */     BuiltinFonts14.put("Times-Italic", PdfName.TIMES_ITALIC);
/*  341 */     BuiltinFonts14.put("ZapfDingbats", PdfName.ZAPFDINGBATS);
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
/*      */   static class StreamFont
/*      */     extends PdfStream
/*      */   {
/*      */     public StreamFont(byte[] contents, int[] lengths, int compressionLevel) throws DocumentException {
/*      */       try {
/*  359 */         this.bytes = contents;
/*  360 */         put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*  361 */         for (int k = 0; k < lengths.length; k++) {
/*  362 */           put(new PdfName("Length" + (k + 1)), new PdfNumber(lengths[k]));
/*      */         }
/*  364 */         flateCompress(compressionLevel);
/*      */       }
/*  366 */       catch (Exception e) {
/*  367 */         throw new DocumentException(e);
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
/*      */     public StreamFont(byte[] contents, String subType, int compressionLevel) throws DocumentException {
/*      */       try {
/*  381 */         this.bytes = contents;
/*  382 */         put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*  383 */         if (subType != null)
/*  384 */           put(PdfName.SUBTYPE, new PdfName(subType)); 
/*  385 */         flateCompress(compressionLevel);
/*      */       }
/*  387 */       catch (Exception e) {
/*  388 */         throw new DocumentException(e);
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
/*      */   public static BaseFont createFont() throws DocumentException, IOException {
/*  408 */     return createFont("Helvetica", "Cp1252", false);
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
/*      */   public static BaseFont createFont(String name, String encoding, boolean embedded) throws DocumentException, IOException {
/*  460 */     return createFont(name, encoding, embedded, true, null, null, false);
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
/*      */   public static BaseFont createFont(String name, String encoding, boolean embedded, boolean forceRead) throws DocumentException, IOException {
/*  514 */     return createFont(name, encoding, embedded, true, null, null, forceRead);
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
/*      */   public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb) throws DocumentException, IOException {
/*  568 */     return createFont(name, encoding, embedded, cached, ttfAfm, pfb, false);
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
/*      */   public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb, boolean noThrow) throws DocumentException, IOException {
/*  625 */     return createFont(name, encoding, embedded, cached, ttfAfm, pfb, noThrow, false);
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
/*      */   public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte[] ttfAfm, byte[] pfb, boolean noThrow, boolean forceRead) throws DocumentException, IOException {
/*  683 */     String nameBase = getBaseName(name);
/*  684 */     encoding = normalizeEncoding(encoding);
/*  685 */     boolean isBuiltinFonts14 = BuiltinFonts14.containsKey(name);
/*  686 */     boolean isCJKFont = isBuiltinFonts14 ? false : CJKFont.isCJKFont(nameBase, encoding);
/*  687 */     if (isBuiltinFonts14 || isCJKFont) {
/*  688 */       embedded = false;
/*  689 */     } else if (encoding.equals("Identity-H") || encoding.equals("Identity-V")) {
/*  690 */       embedded = true;
/*  691 */     }  BaseFont fontFound = null;
/*  692 */     BaseFont fontBuilt = null;
/*  693 */     String key = name + "\n" + encoding + "\n" + embedded;
/*  694 */     if (cached) {
/*  695 */       fontFound = fontCache.get(key);
/*  696 */       if (fontFound != null)
/*  697 */         return fontFound; 
/*      */     } 
/*  699 */     if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm") || name.toLowerCase().endsWith(".pfm"))
/*  700 */     { fontBuilt = new Type1Font(name, encoding, embedded, ttfAfm, pfb, forceRead);
/*  701 */       fontBuilt.fastWinansi = encoding.equals("Cp1252"); }
/*      */     
/*  703 */     else if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0)
/*  704 */     { if (encoding.equals("Identity-H") || encoding.equals("Identity-V")) {
/*  705 */         fontBuilt = new TrueTypeFontUnicode(name, encoding, embedded, ttfAfm, forceRead);
/*      */       } else {
/*  707 */         fontBuilt = new TrueTypeFont(name, encoding, embedded, ttfAfm, false, forceRead);
/*  708 */         fontBuilt.fastWinansi = encoding.equals("Cp1252");
/*      */       }
/*      */        }
/*  711 */     else if (isCJKFont)
/*  712 */     { fontBuilt = new CJKFont(name, encoding, embedded); }
/*  713 */     else { if (noThrow) {
/*  714 */         return null;
/*      */       }
/*  716 */       throw new DocumentException(MessageLocalization.getComposedMessage("font.1.with.2.is.not.recognized", new Object[] { name, encoding })); }
/*  717 */      if (cached) {
/*  718 */       fontFound = fontCache.get(key);
/*  719 */       if (fontFound != null)
/*  720 */         return fontFound; 
/*  721 */       fontCache.putIfAbsent(key, fontBuilt);
/*      */     } 
/*  723 */     return fontBuilt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BaseFont createFont(PRIndirectReference fontRef) {
/*  733 */     return new DocumentFont(fontRef);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVertical() {
/*  741 */     return this.vertical;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String getBaseName(String name) {
/*  750 */     if (name.endsWith(",Bold"))
/*  751 */       return name.substring(0, name.length() - 5); 
/*  752 */     if (name.endsWith(",Italic"))
/*  753 */       return name.substring(0, name.length() - 7); 
/*  754 */     if (name.endsWith(",BoldItalic")) {
/*  755 */       return name.substring(0, name.length() - 11);
/*      */     }
/*  757 */     return name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String normalizeEncoding(String enc) {
/*  767 */     if (enc.equals("winansi") || enc.equals(""))
/*  768 */       return "Cp1252"; 
/*  769 */     if (enc.equals("macroman")) {
/*  770 */       return "MacRoman";
/*      */     }
/*  772 */     return enc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void createEncoding() {
/*  779 */     if (this.encoding.startsWith("#")) {
/*  780 */       this.specialMap = new IntHashtable();
/*  781 */       StringTokenizer tok = new StringTokenizer(this.encoding.substring(1), " ,\t\n\r\f");
/*  782 */       if (tok.nextToken().equals("full")) {
/*  783 */         while (tok.hasMoreTokens()) {
/*  784 */           int orderK; String order = tok.nextToken();
/*  785 */           String name = tok.nextToken();
/*  786 */           char uni = (char)Integer.parseInt(tok.nextToken(), 16);
/*      */           
/*  788 */           if (order.startsWith("'")) {
/*  789 */             orderK = order.charAt(1);
/*      */           } else {
/*  791 */             orderK = Integer.parseInt(order);
/*  792 */           }  orderK %= 256;
/*  793 */           this.specialMap.put(uni, orderK);
/*  794 */           this.differences[orderK] = name;
/*  795 */           this.unicodeDifferences[orderK] = uni;
/*  796 */           this.widths[orderK] = getRawWidth(uni, name);
/*  797 */           this.charBBoxes[orderK] = getRawCharBBox(uni, name);
/*      */         } 
/*      */       } else {
/*      */         
/*  801 */         int i = 0;
/*  802 */         if (tok.hasMoreTokens())
/*  803 */           i = Integer.parseInt(tok.nextToken()); 
/*  804 */         while (tok.hasMoreTokens() && i < 256) {
/*  805 */           String hex = tok.nextToken();
/*  806 */           int uni = Integer.parseInt(hex, 16) % 65536;
/*  807 */           String name = GlyphList.unicodeToName(uni);
/*  808 */           if (name != null) {
/*  809 */             this.specialMap.put(uni, i);
/*  810 */             this.differences[i] = name;
/*  811 */             this.unicodeDifferences[i] = (char)uni;
/*  812 */             this.widths[i] = getRawWidth(uni, name);
/*  813 */             this.charBBoxes[i] = getRawCharBBox(uni, name);
/*  814 */             i++;
/*      */           } 
/*      */         } 
/*      */       } 
/*  818 */       for (int k = 0; k < 256; k++) {
/*  819 */         if (this.differences[k] == null) {
/*  820 */           this.differences[k] = ".notdef";
/*      */         }
/*      */       }
/*      */     
/*  824 */     } else if (this.fontSpecific) {
/*  825 */       for (int k = 0; k < 256; k++) {
/*  826 */         this.widths[k] = getRawWidth(k, null);
/*  827 */         this.charBBoxes[k] = getRawCharBBox(k, null);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  834 */       byte[] b = new byte[1];
/*  835 */       for (int k = 0; k < 256; k++) {
/*  836 */         char c; b[0] = (byte)k;
/*  837 */         String s = PdfEncodings.convertToString(b, this.encoding);
/*  838 */         if (s.length() > 0) {
/*  839 */           c = s.charAt(0);
/*      */         } else {
/*      */           
/*  842 */           c = '?';
/*      */         } 
/*  844 */         String name = GlyphList.unicodeToName(c);
/*  845 */         if (name == null)
/*  846 */           name = ".notdef"; 
/*  847 */         this.differences[k] = name;
/*  848 */         this.unicodeDifferences[k] = c;
/*  849 */         this.widths[k] = getRawWidth(c, name);
/*  850 */         this.charBBoxes[k] = getRawCharBBox(c, name);
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
/*      */   public int getWidth(int char1) {
/*  887 */     if (this.fastWinansi) {
/*  888 */       if (char1 < 128 || (char1 >= 160 && char1 <= 255)) {
/*  889 */         return this.widths[char1];
/*      */       }
/*  891 */       return this.widths[PdfEncodings.winansi.get(char1)];
/*      */     } 
/*      */     
/*  894 */     int total = 0;
/*  895 */     byte[] mbytes = convertToBytes(char1);
/*  896 */     for (int k = 0; k < mbytes.length; k++)
/*  897 */       total += this.widths[0xFF & mbytes[k]]; 
/*  898 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getWidth(String text) {
/*  908 */     int total = 0;
/*  909 */     if (this.fastWinansi) {
/*  910 */       int len = text.length();
/*  911 */       for (int i = 0; i < len; i++) {
/*  912 */         char char1 = text.charAt(i);
/*  913 */         if (char1 < '' || (char1 >= ' ' && char1 <= 'ÿ')) {
/*  914 */           total += this.widths[char1];
/*      */         } else {
/*  916 */           total += this.widths[PdfEncodings.winansi.get(char1)];
/*      */         } 
/*  918 */       }  return total;
/*      */     } 
/*      */     
/*  921 */     byte[] mbytes = convertToBytes(text);
/*  922 */     for (int k = 0; k < mbytes.length; k++) {
/*  923 */       total += this.widths[0xFF & mbytes[k]];
/*      */     }
/*  925 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDescent(String text) {
/*  935 */     int min = 0;
/*  936 */     char[] chars = text.toCharArray();
/*  937 */     for (int k = 0; k < chars.length; k++) {
/*  938 */       int[] bbox = getCharBBox(chars[k]);
/*  939 */       if (bbox != null && bbox[1] < min)
/*  940 */         min = bbox[1]; 
/*      */     } 
/*  942 */     return min;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAscent(String text) {
/*  952 */     int max = 0;
/*  953 */     char[] chars = text.toCharArray();
/*  954 */     for (int k = 0; k < chars.length; k++) {
/*  955 */       int[] bbox = getCharBBox(chars[k]);
/*  956 */       if (bbox != null && bbox[3] > max)
/*  957 */         max = bbox[3]; 
/*      */     } 
/*  959 */     return max;
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
/*      */   public float getDescentPoint(String text, float fontSize) {
/*  971 */     return getDescent(text) * 0.001F * fontSize;
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
/*      */   public float getAscentPoint(String text, float fontSize) {
/*  983 */     return getAscent(text) * 0.001F * fontSize;
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
/*      */   public float getWidthPointKerned(String text, float fontSize) {
/*  995 */     float size = getWidth(text) * 0.001F * fontSize;
/*  996 */     if (!hasKernPairs())
/*  997 */       return size; 
/*  998 */     int len = text.length() - 1;
/*  999 */     int kern = 0;
/* 1000 */     char[] c = text.toCharArray();
/* 1001 */     for (int k = 0; k < len; k++) {
/* 1002 */       kern += getKerning(c[k], c[k + 1]);
/*      */     }
/* 1004 */     return size + kern * 0.001F * fontSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidthPoint(String text, float fontSize) {
/* 1014 */     return getWidth(text) * 0.001F * fontSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWidthPoint(int char1, float fontSize) {
/* 1024 */     return getWidth(char1) * 0.001F * fontSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] convertToBytes(String text) {
/* 1034 */     if (this.directTextToByte)
/* 1035 */       return PdfEncodings.convertToBytes(text, (String)null); 
/* 1036 */     if (this.specialMap != null) {
/* 1037 */       byte[] b = new byte[text.length()];
/* 1038 */       int ptr = 0;
/* 1039 */       int length = text.length();
/* 1040 */       for (int k = 0; k < length; k++) {
/* 1041 */         char c = text.charAt(k);
/* 1042 */         if (this.specialMap.containsKey(c))
/* 1043 */           b[ptr++] = (byte)this.specialMap.get(c); 
/*      */       } 
/* 1045 */       if (ptr < length) {
/* 1046 */         byte[] b2 = new byte[ptr];
/* 1047 */         System.arraycopy(b, 0, b2, 0, ptr);
/* 1048 */         return b2;
/*      */       } 
/*      */       
/* 1051 */       return b;
/*      */     } 
/* 1053 */     return PdfEncodings.convertToBytes(text, this.encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   byte[] convertToBytes(int char1) {
/* 1063 */     if (this.directTextToByte)
/* 1064 */       return PdfEncodings.convertToBytes((char)char1, (String)null); 
/* 1065 */     if (this.specialMap != null) {
/* 1066 */       if (this.specialMap.containsKey(char1)) {
/* 1067 */         return new byte[] { (byte)this.specialMap.get(char1) };
/*      */       }
/* 1069 */       return new byte[0];
/*      */     } 
/* 1071 */     return PdfEncodings.convertToBytes((char)char1, this.encoding);
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
/*      */   public String getEncoding() {
/* 1096 */     return this.encoding;
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
/*      */   public void setFontDescriptor(int key, float value) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFontType() {
/* 1125 */     return this.fontType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmbedded() {
/* 1132 */     return this.embedded;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFontSpecific() {
/* 1139 */     return this.fontSpecific;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String createSubsetPrefix() {
/* 1146 */     StringBuilder s = new StringBuilder("");
/* 1147 */     for (int k = 0; k < 6; k++)
/* 1148 */       s.append((char)(int)(Math.random() * 26.0D + 65.0D)); 
/* 1149 */     return s + "+";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   char getUnicodeDifferences(int index) {
/* 1157 */     return this.unicodeDifferences[index];
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
/*      */   public String getSubfamily() {
/* 1173 */     return "";
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
/*      */   public static String[][] getFullFontName(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
/* 1211 */     String nameBase = getBaseName(name);
/* 1212 */     BaseFont fontBuilt = null;
/* 1213 */     if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
/* 1214 */       fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
/*      */     } else {
/* 1216 */       fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
/* 1217 */     }  return fontBuilt.getFullFontName();
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
/*      */   public static Object[] getAllFontNames(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
/* 1229 */     String nameBase = getBaseName(name);
/* 1230 */     BaseFont fontBuilt = null;
/* 1231 */     if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
/* 1232 */       fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
/*      */     } else {
/* 1234 */       fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
/* 1235 */     }  return new Object[] { fontBuilt.getPostscriptFontName(), fontBuilt.getFamilyFontName(), fontBuilt.getFullFontName() };
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
/*      */   public static String[][] getAllNameEntries(String name, String encoding, byte[] ttfAfm) throws DocumentException, IOException {
/* 1248 */     String nameBase = getBaseName(name);
/* 1249 */     BaseFont fontBuilt = null;
/* 1250 */     if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
/* 1251 */       fontBuilt = new TrueTypeFont(name, "Cp1252", false, ttfAfm, true, false);
/*      */     } else {
/* 1253 */       fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
/* 1254 */     }  return fontBuilt.getAllNameEntries();
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
/*      */   public String[] getCodePagesSupported() {
/* 1272 */     return new String[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] enumerateTTCNames(String ttcFile) throws DocumentException, IOException {
/* 1283 */     return (new EnumerateTTC(ttcFile)).getNames();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] enumerateTTCNames(byte[] ttcArray) throws DocumentException, IOException {
/* 1294 */     return (new EnumerateTTC(ttcArray)).getNames();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getWidths() {
/* 1301 */     return this.widths;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getDifferences() {
/* 1308 */     return this.differences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char[] getUnicodeDifferences() {
/* 1315 */     return this.unicodeDifferences;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isForceWidthsOutput() {
/* 1322 */     return this.forceWidthsOutput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setForceWidthsOutput(boolean forceWidthsOutput) {
/* 1331 */     this.forceWidthsOutput = forceWidthsOutput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDirectTextToByte() {
/* 1339 */     return this.directTextToByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDirectTextToByte(boolean directTextToByte) {
/* 1348 */     this.directTextToByte = directTextToByte;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSubset() {
/* 1356 */     return this.subset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSubset(boolean subset) {
/* 1367 */     this.subset = subset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getUnicodeEquivalent(int c) {
/* 1377 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCidCode(int c) {
/* 1386 */     return c;
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
/*      */   public boolean charExists(int c) {
/* 1401 */     byte[] b = convertToBytes(c);
/* 1402 */     return (b.length > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setCharAdvance(int c, int advance) {
/* 1413 */     byte[] b = convertToBytes(c);
/* 1414 */     if (b.length == 0)
/* 1415 */       return false; 
/* 1416 */     this.widths[0xFF & b[0]] = advance;
/* 1417 */     return true;
/*      */   }
/*      */   
/*      */   private static void addFont(PRIndirectReference fontRef, IntHashtable hits, ArrayList<Object[]> fonts) {
/* 1421 */     PdfObject obj = PdfReader.getPdfObject(fontRef);
/* 1422 */     if (obj == null || !obj.isDictionary())
/*      */       return; 
/* 1424 */     PdfDictionary font = (PdfDictionary)obj;
/* 1425 */     PdfName subtype = font.getAsName(PdfName.SUBTYPE);
/* 1426 */     if (!PdfName.TYPE1.equals(subtype) && !PdfName.TRUETYPE.equals(subtype) && !PdfName.TYPE0.equals(subtype))
/*      */       return; 
/* 1428 */     PdfName name = font.getAsName(PdfName.BASEFONT);
/* 1429 */     fonts.add(new Object[] { PdfName.decodeName(name.toString()), fontRef });
/* 1430 */     hits.put(fontRef.getNumber(), 1);
/*      */   }
/*      */   
/*      */   private static void recourseFonts(PdfDictionary page, IntHashtable hits, ArrayList<Object[]> fonts, int level, HashSet<PdfDictionary> visitedResources) {
/* 1434 */     level++;
/* 1435 */     if (level > 50)
/*      */       return; 
/* 1437 */     if (page == null)
/*      */       return; 
/* 1439 */     PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
/* 1440 */     if (resources == null)
/*      */       return; 
/* 1442 */     PdfDictionary font = resources.getAsDict(PdfName.FONT);
/* 1443 */     if (font != null) {
/* 1444 */       for (PdfName key : font.getKeys()) {
/* 1445 */         PdfObject ft = font.get(key);
/* 1446 */         if (ft == null || !ft.isIndirect())
/*      */           continue; 
/* 1448 */         int hit = ((PRIndirectReference)ft).getNumber();
/* 1449 */         if (hits.containsKey(hit))
/*      */           continue; 
/* 1451 */         addFont((PRIndirectReference)ft, hits, fonts);
/*      */       } 
/*      */     }
/* 1454 */     PdfDictionary xobj = resources.getAsDict(PdfName.XOBJECT);
/* 1455 */     if (xobj != null) {
/* 1456 */       if (visitedResources.add(xobj)) {
/* 1457 */         for (PdfName key : xobj.getKeys()) {
/* 1458 */           PdfObject po = xobj.getDirectObject(key);
/* 1459 */           if (po instanceof PdfDictionary)
/* 1460 */             recourseFonts((PdfDictionary)po, hits, fonts, level, visitedResources); 
/*      */         } 
/* 1462 */         visitedResources.remove(xobj);
/*      */       } else {
/* 1464 */         throw new ExceptionConverter(new InvalidPdfException(MessageLocalization.getComposedMessage("illegal.resources.tree", new Object[0])));
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
/*      */   public static ArrayList<Object[]> getDocumentFonts(PdfReader reader) {
/* 1476 */     IntHashtable hits = new IntHashtable();
/* 1477 */     ArrayList<Object[]> fonts = new ArrayList();
/* 1478 */     int npages = reader.getNumberOfPages();
/* 1479 */     for (int k = 1; k <= npages; k++)
/* 1480 */       recourseFonts(reader.getPageN(k), hits, fonts, 1, new HashSet<PdfDictionary>()); 
/* 1481 */     return fonts;
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
/*      */   public static ArrayList<Object[]> getDocumentFonts(PdfReader reader, int page) {
/* 1493 */     IntHashtable hits = new IntHashtable();
/* 1494 */     ArrayList<Object[]> fonts = new ArrayList();
/* 1495 */     recourseFonts(reader.getPageN(page), hits, fonts, 1, new HashSet<PdfDictionary>());
/* 1496 */     return fonts;
/*      */   }
/*      */   
/*      */   static PdfDictionary createBuiltInFontDictionary(String name) {
/* 1500 */     return createBuiltInFontDictionary(BuiltinFonts14.get(name));
/*      */   }
/*      */   
/*      */   private static PdfDictionary createBuiltInFontDictionary(PdfName name) {
/* 1504 */     if (name == null) {
/* 1505 */       return null;
/*      */     }
/* 1507 */     PdfDictionary dictionary = new PdfDictionary();
/* 1508 */     dictionary.put(PdfName.TYPE, PdfName.FONT);
/* 1509 */     dictionary.put(PdfName.BASEFONT, name);
/* 1510 */     dictionary.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 1511 */     return dictionary;
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
/*      */   public int[] getCharBBox(int c) {
/* 1524 */     byte[] b = convertToBytes(c);
/* 1525 */     if (b.length == 0) {
/* 1526 */       return null;
/*      */     }
/* 1528 */     return this.charBBoxes[b[0] & 0xFF];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double[] getFontMatrix() {
/* 1537 */     return DEFAULT_FONT_MATRIX;
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
/*      */   public void correctArabicAdvance() {
/*      */     char c;
/* 1550 */     for (c = 'ً'; c <= '٘'; c = (char)(c + 1))
/* 1551 */       setCharAdvance(c, 0); 
/* 1552 */     setCharAdvance(1648, 0);
/* 1553 */     for (c = 'ۖ'; c <= 'ۜ'; c = (char)(c + 1))
/* 1554 */       setCharAdvance(c, 0); 
/* 1555 */     for (c = '۟'; c <= 'ۤ'; c = (char)(c + 1))
/* 1556 */       setCharAdvance(c, 0); 
/* 1557 */     for (c = 'ۧ'; c <= 'ۨ'; c = (char)(c + 1))
/* 1558 */       setCharAdvance(c, 0); 
/* 1559 */     for (c = '۪'; c <= 'ۭ'; c = (char)(c + 1)) {
/* 1560 */       setCharAdvance(c, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSubsetRange(int[] range) {
/* 1570 */     if (this.subsetRanges == null)
/* 1571 */       this.subsetRanges = (ArrayList)new ArrayList<int>(); 
/* 1572 */     this.subsetRanges.add(range);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCompressionLevel() {
/* 1581 */     return this.compressionLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCompressionLevel(int compressionLevel) {
/* 1590 */     if (compressionLevel < 0 || compressionLevel > 9) {
/* 1591 */       this.compressionLevel = -1;
/*      */     } else {
/* 1593 */       this.compressionLevel = compressionLevel;
/*      */     } 
/*      */   }
/*      */   
/*      */   abstract int getRawWidth(int paramInt, String paramString);
/*      */   
/*      */   public abstract int getKerning(int paramInt1, int paramInt2);
/*      */   
/*      */   public abstract boolean setKerning(int paramInt1, int paramInt2, int paramInt3);
/*      */   
/*      */   abstract void writeFont(PdfWriter paramPdfWriter, PdfIndirectReference paramPdfIndirectReference, Object[] paramArrayOfObject) throws DocumentException, IOException;
/*      */   
/*      */   abstract PdfStream getFullFontStream() throws IOException, DocumentException;
/*      */   
/*      */   public abstract float getFontDescriptor(int paramInt, float paramFloat);
/*      */   
/*      */   public abstract String getPostscriptFontName();
/*      */   
/*      */   public abstract void setPostscriptFontName(String paramString);
/*      */   
/*      */   public abstract String[][] getFullFontName();
/*      */   
/*      */   public abstract String[][] getAllNameEntries();
/*      */   
/*      */   public abstract String[][] getFamilyFontName();
/*      */   
/*      */   public abstract boolean hasKernPairs();
/*      */   
/*      */   protected abstract int[] getRawCharBBox(int paramInt, String paramString);
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BaseFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */