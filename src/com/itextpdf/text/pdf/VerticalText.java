/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public class VerticalText
/*     */ {
/*     */   public static final int NO_MORE_TEXT = 1;
/*     */   public static final int NO_MORE_COLUMN = 2;
/*  68 */   protected ArrayList<PdfChunk> chunks = new ArrayList<PdfChunk>();
/*     */ 
/*     */   
/*     */   protected PdfContentByte text;
/*     */ 
/*     */   
/*  74 */   protected int alignment = 0;
/*     */ 
/*     */   
/*  77 */   protected int currentChunkMarker = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfChunk currentStandbyChunk;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String splittedChunkText;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float leading;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float startX;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float startY;
/*     */ 
/*     */ 
/*     */   
/*     */   protected int maxLines;
/*     */ 
/*     */ 
/*     */   
/*     */   protected float height;
/*     */ 
/*     */ 
/*     */   
/*     */   private Float curCharSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(Phrase phrase) {
/* 118 */     for (Chunk c : phrase.getChunks()) {
/* 119 */       this.chunks.add(new PdfChunk(c, null));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addText(Chunk chunk) {
/* 128 */     this.chunks.add(new PdfChunk(chunk, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerticalLayout(float startX, float startY, float height, int maxLines, float leading) {
/* 139 */     this.startX = startX;
/* 140 */     this.startY = startY;
/* 141 */     this.height = height;
/* 142 */     this.maxLines = maxLines;
/* 143 */     setLeading(leading);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeading(float leading) {
/* 150 */     this.leading = leading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLeading() {
/* 157 */     return this.leading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfLine createLine(float width) {
/* 166 */     if (this.chunks.isEmpty())
/* 167 */       return null; 
/* 168 */     this.splittedChunkText = null;
/* 169 */     this.currentStandbyChunk = null;
/* 170 */     PdfLine line = new PdfLine(0.0F, width, this.alignment, 0.0F);
/*     */     
/* 172 */     for (this.currentChunkMarker = 0; this.currentChunkMarker < this.chunks.size(); this.currentChunkMarker++) {
/* 173 */       PdfChunk original = this.chunks.get(this.currentChunkMarker);
/* 174 */       String total = original.toString();
/* 175 */       this.currentStandbyChunk = line.add(original);
/* 176 */       if (this.currentStandbyChunk != null) {
/* 177 */         this.splittedChunkText = original.toString();
/* 178 */         original.setValue(total);
/* 179 */         return line;
/*     */       } 
/*     */     } 
/* 182 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shortenChunkArray() {
/* 189 */     if (this.currentChunkMarker < 0)
/*     */       return; 
/* 191 */     if (this.currentChunkMarker >= this.chunks.size()) {
/* 192 */       this.chunks.clear();
/*     */       return;
/*     */     } 
/* 195 */     PdfChunk split = this.chunks.get(this.currentChunkMarker);
/* 196 */     split.setValue(this.splittedChunkText);
/* 197 */     this.chunks.set(this.currentChunkMarker, this.currentStandbyChunk);
/* 198 */     for (int j = this.currentChunkMarker - 1; j >= 0; j--) {
/* 199 */       this.chunks.remove(j);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int go() {
/* 208 */     return go(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int go(boolean simulate) {
/* 218 */     boolean dirty = false;
/* 219 */     PdfContentByte graphics = null;
/* 220 */     if (this.text != null) {
/* 221 */       graphics = this.text.getDuplicate();
/*     */     }
/* 223 */     else if (!simulate) {
/* 224 */       throw new NullPointerException(MessageLocalization.getComposedMessage("verticaltext.go.with.simulate.eq.eq.false.and.text.eq.eq.null", new Object[0]));
/* 225 */     }  int status = 0;
/*     */     while (true) {
/* 227 */       if (this.maxLines <= 0) {
/* 228 */         status = 2;
/* 229 */         if (this.chunks.isEmpty())
/* 230 */           status |= 0x1; 
/*     */         break;
/*     */       } 
/* 233 */       if (this.chunks.isEmpty()) {
/* 234 */         status = 1;
/*     */         break;
/*     */       } 
/* 237 */       PdfLine line = createLine(this.height);
/* 238 */       if (!simulate && !dirty) {
/* 239 */         this.text.beginText();
/* 240 */         dirty = true;
/*     */       } 
/* 242 */       shortenChunkArray();
/* 243 */       if (!simulate) {
/* 244 */         this.text.setTextMatrix(this.startX, this.startY - line.indentLeft());
/* 245 */         writeLine(line, this.text, graphics);
/*     */       } 
/* 247 */       this.maxLines--;
/* 248 */       this.startX -= this.leading;
/*     */     } 
/* 250 */     if (dirty) {
/* 251 */       this.text.endText();
/* 252 */       this.text.add(graphics);
/*     */     } 
/* 254 */     return status;
/*     */   }
/*     */   public VerticalText(PdfContentByte text) {
/* 257 */     this.curCharSpace = Float.valueOf(0.0F);
/*     */     this.text = text;
/*     */   } void writeLine(PdfLine line, PdfContentByte text, PdfContentByte graphics) {
/* 260 */     PdfFont currentFont = null;
/*     */     
/* 262 */     for (Iterator<PdfChunk> j = line.iterator(); j.hasNext(); ) {
/* 263 */       PdfChunk chunk = j.next();
/*     */       
/* 265 */       if (!chunk.isImage() && chunk.font().compareTo(currentFont) != 0) {
/* 266 */         currentFont = chunk.font();
/* 267 */         text.setFontAndSize(currentFont.getFont(), currentFont.size());
/*     */       } 
/* 269 */       Object[] textRender = (Object[])chunk.getAttribute("TEXTRENDERMODE");
/* 270 */       int tr = 0;
/* 271 */       float strokeWidth = 1.0F;
/* 272 */       BaseColor color = chunk.color();
/* 273 */       BaseColor strokeColor = null;
/* 274 */       if (textRender != null) {
/* 275 */         tr = ((Integer)textRender[0]).intValue() & 0x3;
/* 276 */         if (tr != 0)
/* 277 */           text.setTextRenderingMode(tr); 
/* 278 */         if (tr == 1 || tr == 2) {
/* 279 */           strokeWidth = ((Float)textRender[1]).floatValue();
/* 280 */           if (strokeWidth != 1.0F)
/* 281 */             text.setLineWidth(strokeWidth); 
/* 282 */           strokeColor = (BaseColor)textRender[2];
/* 283 */           if (strokeColor == null)
/* 284 */             strokeColor = color; 
/* 285 */           if (strokeColor != null) {
/* 286 */             text.setColorStroke(strokeColor);
/*     */           }
/*     */         } 
/*     */       } 
/* 290 */       Float charSpace = (Float)chunk.getAttribute("CHAR_SPACING");
/*     */       
/* 292 */       if (charSpace != null && !this.curCharSpace.equals(charSpace)) {
/* 293 */         this.curCharSpace = Float.valueOf(charSpace.floatValue());
/* 294 */         text.setCharacterSpacing(this.curCharSpace.floatValue());
/*     */       } 
/* 296 */       if (color != null) {
/* 297 */         text.setColorFill(color);
/*     */       }
/* 299 */       text.showText(chunk.toString());
/*     */       
/* 301 */       if (color != null)
/* 302 */         text.resetRGBColorFill(); 
/* 303 */       if (tr != 0)
/* 304 */         text.setTextRenderingMode(0); 
/* 305 */       if (strokeColor != null)
/* 306 */         text.resetRGBColorStroke(); 
/* 307 */       if (strokeWidth != 1.0F) {
/* 308 */         text.setLineWidth(1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrigin(float startX, float startY) {
/* 317 */     this.startX = startX;
/* 318 */     this.startY = startY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginX() {
/* 326 */     return this.startX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOriginY() {
/* 333 */     return this.startY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLines() {
/* 341 */     return this.maxLines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLines(int maxLines) {
/* 348 */     this.maxLines = maxLines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 355 */     return this.height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeight(float height) {
/* 362 */     this.height = height;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int alignment) {
/* 370 */     this.alignment = alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAlignment() {
/* 378 */     return this.alignment;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/VerticalText.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */