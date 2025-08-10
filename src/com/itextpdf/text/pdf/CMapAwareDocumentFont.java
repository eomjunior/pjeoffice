/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.AbstractCMap;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapByteCid;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapSequence;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CidLocation;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.IdentityToUnicode;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMapAwareDocumentFont
/*     */   extends DocumentFont
/*     */ {
/*  73 */   private static final Logger LOGGER = LoggerFactory.getLogger(CMapAwareDocumentFont.class);
/*     */ 
/*     */   
/*     */   private PdfDictionary fontDic;
/*     */ 
/*     */   
/*     */   private int spaceWidth;
/*     */ 
/*     */   
/*     */   private CMapToUnicode toUnicodeCmap;
/*     */   
/*     */   private CMapByteCid byteCid;
/*     */   
/*     */   private CMapCidUni cidUni;
/*     */   
/*     */   private char[] cidbyte2uni;
/*     */   
/*     */   private Map<Integer, Integer> uni2cid;
/*     */ 
/*     */   
/*     */   public CMapAwareDocumentFont(PdfDictionary font) {
/*  94 */     super(font);
/*  95 */     this.fontDic = font;
/*  96 */     initFont();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CMapAwareDocumentFont(PRIndirectReference refFont) {
/* 104 */     super(refFont);
/* 105 */     this.fontDic = (PdfDictionary)PdfReader.getPdfObjectRelease(refFont);
/* 106 */     initFont();
/*     */   }
/*     */   
/*     */   private void initFont() {
/* 110 */     processToUnicode();
/*     */     
/*     */     try {
/* 113 */       processUni2Byte();
/*     */       
/* 115 */       this.spaceWidth = super.getWidth(32);
/* 116 */       if (this.spaceWidth == 0) {
/* 117 */         this.spaceWidth = computeAverageWidth();
/*     */       }
/* 119 */       if (this.cjkEncoding != null) {
/* 120 */         this.byteCid = CMapCache.getCachedCMapByteCid(this.cjkEncoding);
/* 121 */         this.cidUni = CMapCache.getCachedCMapCidUni(this.uniMap);
/*     */       }
/*     */     
/* 124 */     } catch (Exception ex) {
/* 125 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processToUnicode() {
/* 133 */     PdfObject toUni = PdfReader.getPdfObjectRelease(this.fontDic.get(PdfName.TOUNICODE));
/* 134 */     if (toUni instanceof PRStream) {
/*     */       try {
/* 136 */         byte[] touni = PdfReader.getStreamBytes((PRStream)toUni);
/* 137 */         CidLocationFromByte lb = new CidLocationFromByte(touni);
/* 138 */         this.toUnicodeCmap = new CMapToUnicode();
/* 139 */         CMapParserEx.parseCid("", (AbstractCMap)this.toUnicodeCmap, (CidLocation)lb);
/* 140 */         this.uni2cid = this.toUnicodeCmap.createReverseMapping();
/* 141 */       } catch (IOException e) {
/* 142 */         this.toUnicodeCmap = null;
/* 143 */         this.uni2cid = null;
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 148 */     else if (this.isType0) {
/*     */       
/*     */       try {
/* 151 */         PdfName encodingName = this.fontDic.getAsName(PdfName.ENCODING);
/* 152 */         if (encodingName == null)
/*     */           return; 
/* 154 */         String enc = PdfName.decodeName(encodingName.toString());
/* 155 */         if (!enc.equals("Identity-H"))
/*     */           return; 
/* 157 */         PdfArray df = (PdfArray)PdfReader.getPdfObjectRelease(this.fontDic.get(PdfName.DESCENDANTFONTS));
/* 158 */         PdfDictionary cidft = (PdfDictionary)PdfReader.getPdfObjectRelease(df.getPdfObject(0));
/* 159 */         PdfDictionary cidinfo = cidft.getAsDict(PdfName.CIDSYSTEMINFO);
/* 160 */         if (cidinfo == null)
/*     */           return; 
/* 162 */         PdfString ordering = cidinfo.getAsString(PdfName.ORDERING);
/* 163 */         if (ordering == null)
/*     */           return; 
/* 165 */         CMapToUnicode touni = IdentityToUnicode.GetMapFromOrdering(ordering.toUnicodeString());
/* 166 */         if (touni == null)
/*     */           return; 
/* 168 */         this.toUnicodeCmap = touni;
/* 169 */         this.uni2cid = this.toUnicodeCmap.createReverseMapping();
/* 170 */       } catch (IOException e) {
/* 171 */         this.toUnicodeCmap = null;
/* 172 */         this.uni2cid = null;
/*     */       } 
/*     */     } 
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
/*     */   private void processUni2Byte() throws IOException {
/* 189 */     IntHashtable byte2uni = getByte2Uni();
/* 190 */     int[] e = byte2uni.toOrderedKeys();
/* 191 */     if (e.length == 0) {
/*     */       return;
/*     */     }
/* 194 */     this.cidbyte2uni = new char[256];
/* 195 */     for (int k = 0; k < e.length; k++) {
/* 196 */       int key = e[k];
/* 197 */       if (key <= 255) {
/* 198 */         this.cidbyte2uni[key] = (char)byte2uni.get(key);
/*     */       } else {
/*     */         
/* 201 */         LOGGER.warn("Font has illegal differences array.");
/*     */       } 
/*     */     } 
/* 204 */     if (this.toUnicodeCmap != null) {
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
/* 222 */       Map<Integer, Integer> dm = this.toUnicodeCmap.createDirectMapping();
/* 223 */       for (Map.Entry<Integer, Integer> kv : dm.entrySet()) {
/* 224 */         if (((Integer)kv.getKey()).intValue() < 256) {
/* 225 */           this.cidbyte2uni[((Integer)kv.getKey()).intValue()] = (char)((Integer)kv.getValue()).intValue();
/*     */         }
/*     */       } 
/*     */     } 
/* 229 */     IntHashtable diffmap = getDiffmap();
/* 230 */     if (diffmap != null) {
/*     */       
/* 232 */       e = diffmap.toOrderedKeys();
/* 233 */       for (int i = 0; i < e.length; i++) {
/* 234 */         int n = diffmap.get(e[i]);
/* 235 */         if (n < 256) {
/* 236 */           this.cidbyte2uni[n] = (char)e[i];
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int computeAverageWidth() {
/* 250 */     int count = 0;
/* 251 */     int total = 0;
/* 252 */     for (int i = 0; i < this.widths.length; i++) {
/* 253 */       if (this.widths[i] != 0) {
/* 254 */         total += this.widths[i];
/* 255 */         count++;
/*     */       } 
/*     */     } 
/* 258 */     return (count != 0) ? (total / count) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth(int char1) {
/* 268 */     if (char1 == 32)
/* 269 */       return (this.spaceWidth != 0) ? this.spaceWidth : this.defaultWidth; 
/* 270 */     return super.getWidth(char1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String decodeSingleCID(byte[] bytes, int offset, int len) {
/* 281 */     if (this.toUnicodeCmap != null) {
/* 282 */       if (offset + len > bytes.length)
/* 283 */         throw new ArrayIndexOutOfBoundsException(MessageLocalization.getComposedMessage("invalid.index.1", offset + len)); 
/* 284 */       String s = this.toUnicodeCmap.lookup(bytes, offset, len);
/* 285 */       if (s != null)
/* 286 */         return s; 
/* 287 */       if (len != 1 || this.cidbyte2uni == null) {
/* 288 */         return null;
/*     */       }
/*     */     } 
/* 291 */     if (len == 1) {
/* 292 */       if (this.cidbyte2uni == null) {
/* 293 */         return "";
/*     */       }
/* 295 */       return new String(this.cidbyte2uni, 0xFF & bytes[offset], 1);
/*     */     } 
/*     */     
/* 298 */     throw new Error("Multi-byte glyphs not implemented yet");
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
/*     */   public String decode(byte[] cidbytes, int offset, int len) {
/* 310 */     StringBuilder sb = new StringBuilder();
/* 311 */     if (this.toUnicodeCmap == null && this.byteCid != null) {
/* 312 */       CMapSequence seq = new CMapSequence(cidbytes, offset, len);
/* 313 */       String cid = this.byteCid.decodeSequence(seq);
/* 314 */       for (int k = 0; k < cid.length(); k++) {
/* 315 */         int c = this.cidUni.lookup(cid.charAt(k));
/* 316 */         if (c > 0) {
/* 317 */           sb.append(Utilities.convertFromUtf32(c));
/*     */         }
/*     */       } 
/*     */     } else {
/* 321 */       for (int i = offset; i < offset + len; i++) {
/* 322 */         String rslt = decodeSingleCID(cidbytes, i, 1);
/* 323 */         if (rslt == null && i < offset + len - 1) {
/* 324 */           rslt = decodeSingleCID(cidbytes, i, 2);
/* 325 */           i++;
/*     */         } 
/* 327 */         if (rslt != null)
/* 328 */           sb.append(rslt); 
/*     */       } 
/*     */     } 
/* 331 */     return sb.toString();
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
/*     */   public String encode(byte[] bytes, int offset, int len) {
/* 343 */     return decode(bytes, offset, len);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/CMapAwareDocumentFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */