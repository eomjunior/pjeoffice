/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.pdf.DocumentFont;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextRenderInfo
/*     */ {
/*     */   private final PdfString string;
/*  67 */   private String text = null;
/*     */   private final Matrix textToUserSpaceTransformMatrix;
/*     */   private final GraphicsState gs;
/*  70 */   private Float unscaledWidth = null;
/*  71 */   private double[] fontMatrix = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Collection<MarkedContentInfo> markedContentInfos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TextRenderInfo(PdfString string, GraphicsState gs, Matrix textMatrix, Collection<MarkedContentInfo> markedContentInfo) {
/*  86 */     this.string = string;
/*  87 */     this.textToUserSpaceTransformMatrix = textMatrix.multiply(gs.ctm);
/*  88 */     this.gs = gs;
/*  89 */     this.markedContentInfos = new ArrayList<MarkedContentInfo>(markedContentInfo);
/*  90 */     this.fontMatrix = gs.font.getFontMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TextRenderInfo(TextRenderInfo parent, PdfString string, float horizontalOffset) {
/* 101 */     this.string = string;
/* 102 */     this.textToUserSpaceTransformMatrix = (new Matrix(horizontalOffset, 0.0F)).multiply(parent.textToUserSpaceTransformMatrix);
/* 103 */     this.gs = parent.gs;
/* 104 */     this.markedContentInfos = parent.markedContentInfos;
/* 105 */     this.fontMatrix = this.gs.font.getFontMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 112 */     if (this.text == null)
/* 113 */       this.text = decode(this.string); 
/* 114 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfString getPdfString() {
/* 120 */     return this.string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMcid(int mcid) {
/* 130 */     return hasMcid(mcid, false);
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
/*     */   public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
/* 142 */     if (checkTheTopmostLevelOnly) {
/* 143 */       if (this.markedContentInfos instanceof ArrayList) {
/* 144 */         Integer infoMcid = getMcid();
/* 145 */         return (infoMcid != null) ? ((infoMcid.intValue() == mcid)) : false;
/*     */       } 
/*     */     } else {
/* 148 */       for (MarkedContentInfo info : this.markedContentInfos) {
/* 149 */         if (info.hasMcid() && 
/* 150 */           info.getMcid() == mcid)
/* 151 */           return true; 
/*     */       } 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getMcid() {
/* 161 */     if (this.markedContentInfos instanceof ArrayList) {
/* 162 */       ArrayList<MarkedContentInfo> mci = (ArrayList<MarkedContentInfo>)this.markedContentInfos;
/* 163 */       MarkedContentInfo info = (mci.size() > 0) ? mci.get(mci.size() - 1) : null;
/* 164 */       return (info != null && info.hasMcid()) ? Integer.valueOf(info.getMcid()) : null;
/*     */     } 
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getUnscaledWidth() {
/* 173 */     if (this.unscaledWidth == null)
/* 174 */       this.unscaledWidth = Float.valueOf(getPdfStringWidth(this.string, false)); 
/* 175 */     return this.unscaledWidth.floatValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSegment getBaseline() {
/* 185 */     return getUnscaledBaselineWithOffset(0.0F + this.gs.rise).transformBy(this.textToUserSpaceTransformMatrix);
/*     */   }
/*     */   
/*     */   public LineSegment getUnscaledBaseline() {
/* 189 */     return getUnscaledBaselineWithOffset(0.0F + this.gs.rise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSegment getAscentLine() {
/* 199 */     float ascent = this.gs.getFont().getFontDescriptor(1, this.gs.getFontSize());
/* 200 */     return getUnscaledBaselineWithOffset(ascent + this.gs.rise).transformBy(this.textToUserSpaceTransformMatrix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSegment getDescentLine() {
/* 211 */     float descent = this.gs.getFont().getFontDescriptor(3, this.gs.getFontSize());
/* 212 */     return getUnscaledBaselineWithOffset(descent + this.gs.rise).transformBy(this.textToUserSpaceTransformMatrix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LineSegment getUnscaledBaselineWithOffset(float yOffset) {
/* 218 */     String unicodeStr = this.string.toUnicodeString();
/*     */ 
/*     */     
/* 221 */     float correctedUnscaledWidth = getUnscaledWidth() - (this.gs.characterSpacing + ((unicodeStr.length() > 0 && unicodeStr.charAt(unicodeStr.length() - 1) == ' ') ? this.gs.wordSpacing : 0.0F)) * this.gs.horizontalScaling;
/*     */     
/* 223 */     return new LineSegment(new Vector(0.0F, yOffset, 1.0F), new Vector(correctedUnscaledWidth, yOffset, 1.0F));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DocumentFont getFont() {
/* 232 */     return (DocumentFont)this.gs.getFont();
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
/*     */   public float getRise() {
/* 259 */     if (this.gs.rise == 0.0F) return 0.0F;
/*     */     
/* 261 */     return convertHeightFromTextSpaceToUserSpace(this.gs.rise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float convertWidthFromTextSpaceToUserSpace(float width) {
/* 271 */     LineSegment textSpace = new LineSegment(new Vector(0.0F, 0.0F, 1.0F), new Vector(width, 0.0F, 1.0F));
/* 272 */     LineSegment userSpace = textSpace.transformBy(this.textToUserSpaceTransformMatrix);
/* 273 */     return userSpace.getLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float convertHeightFromTextSpaceToUserSpace(float height) {
/* 283 */     LineSegment textSpace = new LineSegment(new Vector(0.0F, 0.0F, 1.0F), new Vector(0.0F, height, 1.0F));
/* 284 */     LineSegment userSpace = textSpace.transformBy(this.textToUserSpaceTransformMatrix);
/* 285 */     return userSpace.getLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSingleSpaceWidth() {
/* 292 */     return convertWidthFromTextSpaceToUserSpace(getUnscaledFontSpaceWidth());
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
/*     */   public int getTextRenderMode() {
/* 311 */     return this.gs.renderMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getFillColor() {
/* 318 */     return this.gs.fillColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getStrokeColor() {
/* 326 */     return this.gs.strokeColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getUnscaledFontSpaceWidth() {
/* 336 */     char charToUse = ' ';
/* 337 */     if (this.gs.font.getWidth(charToUse) == 0)
/* 338 */       charToUse = ' '; 
/* 339 */     return getStringWidth(String.valueOf(charToUse));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getStringWidth(String string) {
/* 348 */     float totalWidth = 0.0F;
/* 349 */     for (int i = 0; i < string.length(); i++) {
/* 350 */       char c = string.charAt(i);
/* 351 */       float w = this.gs.font.getWidth(c) / 1000.0F;
/* 352 */       float wordSpacing = (c == ' ') ? this.gs.wordSpacing : 0.0F;
/* 353 */       totalWidth += (w * this.gs.fontSize + this.gs.characterSpacing + wordSpacing) * this.gs.horizontalScaling;
/*     */     } 
/* 355 */     return totalWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float getPdfStringWidth(PdfString string, boolean singleCharString) {
/* 364 */     if (singleCharString) {
/* 365 */       float[] widthAndWordSpacing = getWidthAndWordSpacing(string, singleCharString);
/* 366 */       return (widthAndWordSpacing[0] * this.gs.fontSize + this.gs.characterSpacing + widthAndWordSpacing[1]) * this.gs.horizontalScaling;
/*     */     } 
/* 368 */     float totalWidth = 0.0F;
/* 369 */     for (PdfString str : splitString(string)) {
/* 370 */       totalWidth += getPdfStringWidth(str, true);
/*     */     }
/* 372 */     return totalWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TextRenderInfo> getCharacterRenderInfos() {
/* 382 */     List<TextRenderInfo> rslt = new ArrayList<TextRenderInfo>(this.string.length());
/* 383 */     PdfString[] strings = splitString(this.string);
/* 384 */     float totalWidth = 0.0F;
/* 385 */     for (int i = 0; i < strings.length; i++) {
/* 386 */       float[] widthAndWordSpacing = getWidthAndWordSpacing(strings[i], true);
/* 387 */       TextRenderInfo subInfo = new TextRenderInfo(this, strings[i], totalWidth);
/* 388 */       rslt.add(subInfo);
/* 389 */       totalWidth += (widthAndWordSpacing[0] * this.gs.fontSize + this.gs.characterSpacing + widthAndWordSpacing[1]) * this.gs.horizontalScaling;
/*     */     } 
/* 391 */     for (TextRenderInfo tri : rslt)
/* 392 */       tri.getUnscaledWidth(); 
/* 393 */     return rslt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float[] getWidthAndWordSpacing(PdfString string, boolean singleCharString) {
/* 403 */     if (!singleCharString)
/* 404 */       throw new UnsupportedOperationException(); 
/* 405 */     float[] result = new float[2];
/* 406 */     String decoded = decode(string);
/* 407 */     result[0] = (float)(this.gs.font.getWidth(getCharCode(decoded)) * this.fontMatrix[0]);
/* 408 */     result[1] = decoded.equals(" ") ? this.gs.wordSpacing : 0.0F;
/* 409 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String decode(PdfString in) {
/* 419 */     byte[] bytes = in.getBytes();
/* 420 */     return this.gs.font.decode(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getCharCode(String string) {
/*     */     try {
/* 431 */       byte[] b = string.getBytes("UTF-16BE");
/* 432 */       int value = 0;
/* 433 */       for (int i = 0; i < b.length - 1; i++) {
/* 434 */         value += b[i] & 0xFF;
/* 435 */         value <<= 8;
/*     */       } 
/* 437 */       if (b.length > 0) {
/* 438 */         value += b[b.length - 1] & 0xFF;
/*     */       }
/* 440 */       return value;
/* 441 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*     */       
/* 443 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PdfString[] splitString(PdfString string) {
/* 452 */     List<PdfString> strings = new ArrayList<PdfString>();
/* 453 */     String stringValue = string.toString();
/* 454 */     for (int i = 0; i < stringValue.length(); i++) {
/* 455 */       PdfString newString = new PdfString(stringValue.substring(i, i + 1), string.getEncoding());
/* 456 */       String text = decode(newString);
/* 457 */       if (text.length() == 0 && i < stringValue.length() - 1) {
/* 458 */         newString = new PdfString(stringValue.substring(i, i + 2), string.getEncoding());
/* 459 */         i++;
/*     */       } 
/* 461 */       strings.add(newString);
/*     */     } 
/* 463 */     return strings.<PdfString>toArray(new PdfString[strings.size()]);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/TextRenderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */