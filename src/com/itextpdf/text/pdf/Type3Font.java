/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ public class Type3Font
/*     */   extends BaseFont
/*     */ {
/*     */   private boolean[] usedSlot;
/*  57 */   private IntHashtable widths3 = new IntHashtable();
/*  58 */   private HashMap<Integer, Type3Glyph> char2glyph = new HashMap<Integer, Type3Glyph>();
/*     */   private PdfWriter writer;
/*  60 */   private float llx = Float.NaN;
/*  61 */   private PageResources pageResources = new PageResources();
/*     */   
/*     */   private float lly;
/*     */   
/*     */   private float urx;
/*     */   
/*     */   private float ury;
/*     */   
/*     */   private boolean colorized;
/*     */   
/*     */   public Type3Font(PdfWriter writer, char[] chars, boolean colorized) {
/*  72 */     this(writer, colorized);
/*     */   }
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
/*     */   public Type3Font(PdfWriter writer, boolean colorized) {
/* 103 */     this.writer = writer;
/* 104 */     this.colorized = colorized;
/* 105 */     this.fontType = 5;
/* 106 */     this.usedSlot = new boolean[256];
/*     */   }
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
/*     */   public PdfContentByte defineGlyph(char c, float wx, float llx, float lly, float urx, float ury) {
/* 124 */     if (c == '\000' || c > 'ÿ')
/* 125 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.doesn.t.belong.in.this.type3.font", c)); 
/* 126 */     this.usedSlot[c] = true;
/* 127 */     Integer ck = Integer.valueOf(c);
/* 128 */     Type3Glyph glyph = this.char2glyph.get(ck);
/* 129 */     if (glyph != null)
/* 130 */       return glyph; 
/* 131 */     this.widths3.put(c, (int)wx);
/* 132 */     if (!this.colorized) {
/* 133 */       if (Float.isNaN(this.llx)) {
/* 134 */         this.llx = llx;
/* 135 */         this.lly = lly;
/* 136 */         this.urx = urx;
/* 137 */         this.ury = ury;
/*     */       } else {
/*     */         
/* 140 */         this.llx = Math.min(this.llx, llx);
/* 141 */         this.lly = Math.min(this.lly, lly);
/* 142 */         this.urx = Math.max(this.urx, urx);
/* 143 */         this.ury = Math.max(this.ury, ury);
/*     */       } 
/*     */     }
/* 146 */     glyph = new Type3Glyph(this.writer, this.pageResources, wx, llx, lly, urx, ury, this.colorized);
/* 147 */     this.char2glyph.put(ck, glyph);
/* 148 */     return glyph;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[][] getFamilyFontName() {
/* 153 */     return getFullFontName();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFontDescriptor(int key, float fontSize) {
/* 158 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[][] getFullFontName() {
/* 163 */     return new String[][] { { "", "", "", "" } };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[][] getAllNameEntries() {
/* 171 */     return new String[][] { { "4", "", "", "", "" } };
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKerning(int char1, int char2) {
/* 176 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostscriptFontName() {
/* 181 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   protected int[] getRawCharBBox(int c, String name) {
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   int getRawWidth(int c, String name) {
/* 191 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasKernPairs() {
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setKerning(int char1, int char2, int kern) {
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostscriptFontName(String name) {}
/*     */ 
/*     */   
/*     */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
/* 210 */     if (this.writer != writer) {
/* 211 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("type3.font.used.with.the.wrong.pdfwriter", new Object[0]));
/*     */     }
/*     */     
/* 214 */     int firstChar = 0;
/* 215 */     for (; firstChar < this.usedSlot.length && !this.usedSlot[firstChar]; firstChar++);
/*     */     
/* 217 */     if (firstChar == this.usedSlot.length) {
/* 218 */       throw new DocumentException(MessageLocalization.getComposedMessage("no.glyphs.defined.for.type3.font", new Object[0]));
/*     */     }
/* 220 */     int lastChar = this.usedSlot.length - 1;
/* 221 */     for (; lastChar >= firstChar && !this.usedSlot[lastChar]; lastChar--);
/*     */     
/* 223 */     int[] widths = new int[lastChar - firstChar + 1];
/* 224 */     int[] invOrd = new int[lastChar - firstChar + 1];
/*     */     
/* 226 */     int invOrdIndx = 0, w = 0;
/* 227 */     for (int u = firstChar; u <= lastChar; u++, w++) {
/* 228 */       if (this.usedSlot[u]) {
/* 229 */         invOrd[invOrdIndx++] = u;
/* 230 */         widths[w] = this.widths3.get(u);
/*     */       } 
/*     */     } 
/* 233 */     PdfArray diffs = new PdfArray();
/* 234 */     PdfDictionary charprocs = new PdfDictionary();
/* 235 */     int last = -1;
/* 236 */     for (int k = 0; k < invOrdIndx; k++) {
/* 237 */       int c = invOrd[k];
/* 238 */       if (c > last) {
/* 239 */         last = c;
/* 240 */         diffs.add(new PdfNumber(last));
/*     */       } 
/* 242 */       last++;
/* 243 */       int c2 = invOrd[k];
/* 244 */       String s = GlyphList.unicodeToName(c2);
/* 245 */       if (s == null)
/* 246 */         s = "a" + c2; 
/* 247 */       PdfName n = new PdfName(s);
/* 248 */       diffs.add(n);
/* 249 */       Type3Glyph glyph = this.char2glyph.get(Integer.valueOf(c2));
/* 250 */       PdfStream stream = new PdfStream(glyph.toPdf(null));
/* 251 */       stream.flateCompress(this.compressionLevel);
/* 252 */       PdfIndirectReference refp = writer.addToBody(stream).getIndirectReference();
/* 253 */       charprocs.put(n, refp);
/*     */     } 
/* 255 */     PdfDictionary font = new PdfDictionary(PdfName.FONT);
/* 256 */     font.put(PdfName.SUBTYPE, PdfName.TYPE3);
/* 257 */     if (this.colorized) {
/* 258 */       font.put(PdfName.FONTBBOX, new PdfRectangle(0.0F, 0.0F, 0.0F, 0.0F));
/*     */     } else {
/* 260 */       font.put(PdfName.FONTBBOX, new PdfRectangle(this.llx, this.lly, this.urx, this.ury));
/* 261 */     }  font.put(PdfName.FONTMATRIX, new PdfArray(new float[] { 0.001F, 0.0F, 0.0F, 0.001F, 0.0F, 0.0F }));
/* 262 */     font.put(PdfName.CHARPROCS, writer.addToBody(charprocs).getIndirectReference());
/* 263 */     PdfDictionary encoding = new PdfDictionary();
/* 264 */     encoding.put(PdfName.DIFFERENCES, diffs);
/* 265 */     font.put(PdfName.ENCODING, writer.addToBody(encoding).getIndirectReference());
/* 266 */     font.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
/* 267 */     font.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
/* 268 */     font.put(PdfName.WIDTHS, writer.addToBody(new PdfArray(widths)).getIndirectReference());
/* 269 */     if (this.pageResources.hasResources())
/* 270 */       font.put(PdfName.RESOURCES, writer.addToBody(this.pageResources.getResources()).getIndirectReference()); 
/* 271 */     writer.addToBody(font, ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFullFontStream() {
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] convertToBytes(String text) {
/* 287 */     char[] cc = text.toCharArray();
/* 288 */     byte[] b = new byte[cc.length];
/* 289 */     int p = 0;
/* 290 */     for (int k = 0; k < cc.length; k++) {
/* 291 */       char c = cc[k];
/* 292 */       if (charExists(c))
/* 293 */         b[p++] = (byte)c; 
/*     */     } 
/* 295 */     if (b.length == p)
/* 296 */       return b; 
/* 297 */     byte[] b2 = new byte[p];
/* 298 */     System.arraycopy(b, 0, b2, 0, p);
/* 299 */     return b2;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] convertToBytes(int char1) {
/* 304 */     if (charExists(char1))
/* 305 */       return new byte[] { (byte)char1 }; 
/* 306 */     return new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth(int char1) {
/* 311 */     if (!this.widths3.containsKey(char1))
/* 312 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.is.not.defined.in.a.type3.font", char1)); 
/* 313 */     return this.widths3.get(char1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth(String text) {
/* 318 */     char[] c = text.toCharArray();
/* 319 */     int total = 0;
/* 320 */     for (int k = 0; k < c.length; k++)
/* 321 */       total += getWidth(c[k]); 
/* 322 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getCharBBox(int c) {
/* 327 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charExists(int c) {
/* 332 */     if (c > 0 && c < 256) {
/* 333 */       return this.usedSlot[c];
/*     */     }
/* 335 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setCharAdvance(int c, int advance) {
/* 341 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Type3Font.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */