/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.DocListener;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.FontFactory;
/*     */ import com.itextpdf.text.FontProvider;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.List;
/*     */ import com.itextpdf.text.ListItem;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import com.itextpdf.text.pdf.HyphenationAuto;
/*     */ import com.itextpdf.text.pdf.HyphenationEvent;
/*     */ import com.itextpdf.text.pdf.draw.LineSeparator;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ElementFactory
/*     */ {
/*  86 */   private FontProvider provider = (FontProvider)FontFactory.getFontImp();
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
/*     */   public void setFontProvider(FontProvider provider) {
/* 100 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FontProvider getFontProvider() {
/* 109 */     return this.provider;
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
/*     */   public Font getFont(ChainedProperties chain) {
/* 121 */     String face = chain.getProperty("face");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 127 */     if (face == null || face.trim().length() == 0) {
/* 128 */       face = chain.getProperty("font-family");
/*     */     }
/*     */ 
/*     */     
/* 132 */     if (face != null) {
/* 133 */       StringTokenizer tok = new StringTokenizer(face, ",");
/* 134 */       while (tok.hasMoreTokens()) {
/* 135 */         face = tok.nextToken().trim();
/* 136 */         if (face.startsWith("\""))
/* 137 */           face = face.substring(1); 
/* 138 */         if (face.endsWith("\""))
/* 139 */           face = face.substring(0, face.length() - 1); 
/* 140 */         if (this.provider.isRegistered(face)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     String encoding = chain.getProperty("encoding");
/* 147 */     if (encoding == null) {
/* 148 */       encoding = "Cp1252";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 153 */     String value = chain.getProperty("size");
/* 154 */     float size = 12.0F;
/* 155 */     if (value != null) {
/* 156 */       size = Float.parseFloat(value);
/*     */     }
/*     */     
/* 159 */     int style = 0;
/*     */ 
/*     */     
/* 162 */     String decoration = chain.getProperty("text-decoration");
/* 163 */     if (decoration != null && decoration.trim().length() != 0) {
/* 164 */       if ("underline".equals(decoration)) {
/* 165 */         style |= 0x4;
/* 166 */       } else if ("line-through".equals(decoration)) {
/* 167 */         style |= 0x8;
/*     */       } 
/*     */     }
/*     */     
/* 171 */     if (chain.hasProperty("i")) {
/* 172 */       style |= 0x2;
/*     */     }
/* 174 */     if (chain.hasProperty("b")) {
/* 175 */       style |= 0x1;
/*     */     }
/* 177 */     if (chain.hasProperty("u")) {
/* 178 */       style |= 0x4;
/*     */     }
/* 180 */     if (chain.hasProperty("s")) {
/* 181 */       style |= 0x8;
/*     */     }
/*     */     
/* 184 */     BaseColor color = HtmlUtilities.decodeColor(chain.getProperty("color"));
/*     */ 
/*     */     
/* 187 */     return this.provider.getFont(face, encoding, true, size, style, color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chunk createChunk(String content, ChainedProperties chain) {
/* 198 */     Font font = getFont(chain);
/* 199 */     Chunk ck = new Chunk(content, font);
/* 200 */     if (chain.hasProperty("sub")) {
/* 201 */       ck.setTextRise(-font.getSize() / 2.0F);
/* 202 */     } else if (chain.hasProperty("sup")) {
/* 203 */       ck.setTextRise(font.getSize() / 2.0F);
/* 204 */     }  ck.setHyphenation(getHyphenation(chain));
/* 205 */     return ck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph createParagraph(ChainedProperties chain) {
/* 215 */     Paragraph paragraph = new Paragraph();
/* 216 */     updateElement(paragraph, chain);
/* 217 */     return paragraph;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListItem createListItem(ChainedProperties chain) {
/* 227 */     ListItem item = new ListItem();
/* 228 */     updateElement((Paragraph)item, chain);
/* 229 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateElement(Paragraph paragraph, ChainedProperties chain) {
/* 240 */     String value = chain.getProperty("align");
/* 241 */     paragraph.setAlignment(HtmlUtilities.alignmentValue(value));
/*     */     
/* 243 */     paragraph.setHyphenation(getHyphenation(chain));
/*     */     
/* 245 */     setParagraphLeading(paragraph, chain.getProperty("leading"));
/*     */     
/* 247 */     value = chain.getProperty("after");
/* 248 */     if (value != null) {
/*     */       try {
/* 250 */         paragraph.setSpacingBefore(Float.parseFloat(value));
/* 251 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 255 */     value = chain.getProperty("after");
/* 256 */     if (value != null) {
/*     */       try {
/* 258 */         paragraph.setSpacingAfter(Float.parseFloat(value));
/* 259 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 263 */     value = chain.getProperty("extraparaspace");
/* 264 */     if (value != null) {
/*     */       try {
/* 266 */         paragraph.setExtraParagraphSpace(Float.parseFloat(value));
/* 267 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 271 */     value = chain.getProperty("indent");
/* 272 */     if (value != null) {
/*     */       try {
/* 274 */         paragraph.setIndentationLeft(Float.parseFloat(value));
/* 275 */       } catch (Exception exception) {}
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
/*     */   protected static void setParagraphLeading(Paragraph paragraph, String leading) {
/* 287 */     if (leading == null) {
/* 288 */       paragraph.setLeading(0.0F, 1.5F);
/*     */       return;
/*     */     } 
/*     */     try {
/* 292 */       StringTokenizer tk = new StringTokenizer(leading, " ,");
/*     */       
/* 294 */       String v = tk.nextToken();
/* 295 */       float v1 = Float.parseFloat(v);
/* 296 */       if (!tk.hasMoreTokens()) {
/* 297 */         paragraph.setLeading(v1, 0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 301 */       v = tk.nextToken();
/* 302 */       float v2 = Float.parseFloat(v);
/* 303 */       paragraph.setLeading(v1, v2);
/* 304 */     } catch (Exception e) {
/*     */       
/* 306 */       paragraph.setLeading(0.0F, 1.5F);
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
/*     */   public HyphenationEvent getHyphenation(ChainedProperties chain) {
/*     */     int leftMin;
/* 319 */     String value = chain.getProperty("hyphenation");
/*     */     
/* 321 */     if (value == null || value.length() == 0) {
/* 322 */       return null;
/*     */     }
/*     */     
/* 325 */     int pos = value.indexOf('_');
/* 326 */     if (pos == -1) {
/* 327 */       return (HyphenationEvent)new HyphenationAuto(value, null, 2, 2);
/*     */     }
/*     */     
/* 330 */     String lang = value.substring(0, pos);
/* 331 */     String country = value.substring(pos + 1);
/*     */     
/* 333 */     pos = country.indexOf(',');
/* 334 */     if (pos == -1) {
/* 335 */       return (HyphenationEvent)new HyphenationAuto(lang, country, 2, 2);
/*     */     }
/*     */ 
/*     */     
/* 339 */     int rightMin = 2;
/* 340 */     value = country.substring(pos + 1);
/* 341 */     country = country.substring(0, pos);
/* 342 */     pos = value.indexOf(',');
/* 343 */     if (pos == -1) {
/* 344 */       leftMin = Integer.parseInt(value);
/*     */     } else {
/* 346 */       leftMin = Integer.parseInt(value.substring(0, pos));
/* 347 */       rightMin = Integer.parseInt(value.substring(pos + 1));
/*     */     } 
/* 349 */     return (HyphenationEvent)new HyphenationAuto(lang, country, leftMin, rightMin);
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
/*     */   public LineSeparator createLineSeparator(Map<String, String> attrs, float offset) {
/* 361 */     float lineWidth = 1.0F;
/* 362 */     String size = attrs.get("size");
/* 363 */     if (size != null) {
/* 364 */       float tmpSize = HtmlUtilities.parseLength(size, 12.0F);
/* 365 */       if (tmpSize > 0.0F) {
/* 366 */         lineWidth = tmpSize;
/*     */       }
/*     */     } 
/* 369 */     String width = attrs.get("width");
/* 370 */     float percentage = 100.0F;
/* 371 */     if (width != null) {
/* 372 */       float tmpWidth = HtmlUtilities.parseLength(width, 12.0F);
/* 373 */       if (tmpWidth > 0.0F) percentage = tmpWidth; 
/* 374 */       if (!width.endsWith("%")) {
/* 375 */         percentage = 100.0F;
/*     */       }
/*     */     } 
/* 378 */     BaseColor lineColor = null;
/*     */     
/* 380 */     int align = HtmlUtilities.alignmentValue(attrs.get("align"));
/* 381 */     return new LineSeparator(lineWidth, percentage, lineColor, align, offset);
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
/*     */   public Image createImage(String src, Map<String, String> attrs, ChainedProperties chain, DocListener document, ImageProvider img_provider, HashMap<String, Image> img_store, String img_baseurl) throws DocumentException, IOException {
/* 404 */     Image img = null;
/*     */     
/* 406 */     if (img_provider != null) {
/* 407 */       img = img_provider.getImage(src, attrs, chain, document);
/*     */     }
/* 409 */     if (img == null && img_store != null) {
/* 410 */       Image tim = img_store.get(src);
/* 411 */       if (tim != null)
/* 412 */         img = Image.getInstance(tim); 
/*     */     } 
/* 414 */     if (img != null) {
/* 415 */       return img;
/*     */     }
/*     */     
/* 418 */     if (!src.startsWith("http") && img_baseurl != null) {
/* 419 */       src = img_baseurl + src;
/*     */     }
/* 421 */     else if (img == null && !src.startsWith("http")) {
/* 422 */       String path = chain.getProperty("image_path");
/* 423 */       if (path == null)
/* 424 */         path = ""; 
/* 425 */       src = (new File(path, src)).getPath();
/*     */     } 
/* 427 */     img = Image.getInstance(src);
/* 428 */     if (img == null) {
/* 429 */       return null;
/*     */     }
/* 431 */     float actualFontSize = HtmlUtilities.parseLength(chain
/* 432 */         .getProperty("size"), 12.0F);
/*     */     
/* 434 */     if (actualFontSize <= 0.0F)
/* 435 */       actualFontSize = 12.0F; 
/* 436 */     String width = attrs.get("width");
/* 437 */     float widthInPoints = HtmlUtilities.parseLength(width, actualFontSize);
/* 438 */     String height = attrs.get("height");
/* 439 */     float heightInPoints = HtmlUtilities.parseLength(height, actualFontSize);
/* 440 */     if (widthInPoints > 0.0F && heightInPoints > 0.0F) {
/* 441 */       img.scaleAbsolute(widthInPoints, heightInPoints);
/* 442 */     } else if (widthInPoints > 0.0F) {
/*     */       
/* 444 */       heightInPoints = img.getHeight() * widthInPoints / img.getWidth();
/* 445 */       img.scaleAbsolute(widthInPoints, heightInPoints);
/* 446 */     } else if (heightInPoints > 0.0F) {
/*     */       
/* 448 */       widthInPoints = img.getWidth() * heightInPoints / img.getHeight();
/* 449 */       img.scaleAbsolute(widthInPoints, heightInPoints);
/*     */     } 
/*     */     
/* 452 */     String before = chain.getProperty("before");
/* 453 */     if (before != null)
/* 454 */       img.setSpacingBefore(Float.parseFloat(before)); 
/* 455 */     String after = chain.getProperty("after");
/* 456 */     if (after != null)
/* 457 */       img.setSpacingAfter(Float.parseFloat(after)); 
/* 458 */     img.setWidthPercentage(0.0F);
/* 459 */     return img;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List createList(String tag, ChainedProperties chain) {
/*     */     List list;
/* 469 */     if ("ul".equalsIgnoreCase(tag)) {
/* 470 */       list = new List(false);
/* 471 */       list.setListSymbol("• ");
/*     */     } else {
/*     */       
/* 474 */       list = new List(true);
/*     */     } 
/*     */     try {
/* 477 */       list.setIndentationLeft((new Float(chain.getProperty("indent"))).floatValue());
/* 478 */     } catch (Exception e) {
/* 479 */       list.setAutoindent(true);
/*     */     } 
/* 481 */     return list;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/ElementFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */