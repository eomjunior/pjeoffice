/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfOutline
/*     */   extends PdfDictionary
/*     */ {
/*     */   private PdfIndirectReference reference;
/*  73 */   private int count = 0;
/*     */ 
/*     */   
/*     */   private PdfOutline parent;
/*     */ 
/*     */   
/*     */   private PdfDestination destination;
/*     */ 
/*     */   
/*     */   private PdfAction action;
/*     */ 
/*     */   
/*  85 */   protected ArrayList<PdfOutline> kids = new ArrayList<PdfOutline>();
/*     */ 
/*     */   
/*     */   protected PdfWriter writer;
/*     */ 
/*     */   
/*     */   private String tag;
/*     */ 
/*     */   
/*     */   private boolean open;
/*     */ 
/*     */   
/*     */   private BaseColor color;
/*     */   
/*  99 */   private int style = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfOutline(PdfWriter writer) {
/* 112 */     super(OUTLINES);
/* 113 */     this.open = true;
/* 114 */     this.parent = null;
/* 115 */     this.writer = writer;
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, String title) {
/* 130 */     this(parent, action, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, String title, boolean open) {
/* 145 */     this.action = action;
/* 146 */     initOutline(parent, title, open);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, String title) {
/* 161 */     this(parent, destination, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, String title, boolean open) {
/* 176 */     this.destination = destination;
/* 177 */     initOutline(parent, title, open);
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, PdfString title) {
/* 191 */     this(parent, action, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, PdfString title, boolean open) {
/* 205 */     this(parent, action, title.toString(), open);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title) {
/* 220 */     this(parent, destination, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title, boolean open) {
/* 234 */     this(parent, destination, title.toString(), true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, Paragraph title) {
/* 249 */     this(parent, action, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfAction action, Paragraph title, boolean open) {
/* 264 */     StringBuffer buf = new StringBuffer();
/* 265 */     for (Chunk chunk : title.getChunks()) {
/* 266 */       buf.append(chunk.getContent());
/*     */     }
/* 268 */     this.action = action;
/* 269 */     initOutline(parent, buf.toString(), open);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title) {
/* 284 */     this(parent, destination, title, true);
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
/*     */   public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title, boolean open) {
/* 299 */     StringBuffer buf = new StringBuffer();
/* 300 */     for (Object element : title.getChunks()) {
/* 301 */       Chunk chunk = (Chunk)element;
/* 302 */       buf.append(chunk.getContent());
/*     */     } 
/* 304 */     this.destination = destination;
/* 305 */     initOutline(parent, buf.toString(), open);
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
/*     */   void initOutline(PdfOutline parent, String title, boolean open) {
/* 317 */     this.open = open;
/* 318 */     this.parent = parent;
/* 319 */     this.writer = parent.writer;
/* 320 */     put(PdfName.TITLE, new PdfString(title, "UnicodeBig"));
/* 321 */     parent.addKid(this);
/* 322 */     if (this.destination != null && !this.destination.hasPage()) {
/* 323 */       setDestinationPage(this.writer.getCurrentPage());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndirectReference(PdfIndirectReference reference) {
/* 333 */     this.reference = reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference indirectReference() {
/* 343 */     return this.reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfOutline parent() {
/* 353 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setDestinationPage(PdfIndirectReference pageReference) {
/* 364 */     if (this.destination == null) {
/* 365 */       return false;
/*     */     }
/* 367 */     return this.destination.addPage(pageReference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDestination getPdfDestination() {
/* 375 */     return this.destination;
/*     */   }
/*     */   
/*     */   int getCount() {
/* 379 */     return this.count;
/*     */   }
/*     */   
/*     */   void setCount(int count) {
/* 383 */     this.count = count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int level() {
/* 393 */     if (this.parent == null) {
/* 394 */       return 0;
/*     */     }
/* 396 */     return this.parent.level() + 1;
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
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 409 */     if (this.color != null && !this.color.equals(BaseColor.BLACK)) {
/* 410 */       put(PdfName.C, new PdfArray(new float[] { this.color.getRed() / 255.0F, this.color.getGreen() / 255.0F, this.color.getBlue() / 255.0F }));
/*     */     }
/* 412 */     int flag = 0;
/* 413 */     if ((this.style & 0x1) != 0)
/* 414 */       flag |= 0x2; 
/* 415 */     if ((this.style & 0x2) != 0)
/* 416 */       flag |= 0x1; 
/* 417 */     if (flag != 0)
/* 418 */       put(PdfName.F, new PdfNumber(flag)); 
/* 419 */     if (this.parent != null) {
/* 420 */       put(PdfName.PARENT, this.parent.indirectReference());
/*     */     }
/* 422 */     if (this.destination != null && this.destination.hasPage()) {
/* 423 */       put(PdfName.DEST, this.destination);
/*     */     }
/* 425 */     if (this.action != null)
/* 426 */       put(PdfName.A, this.action); 
/* 427 */     if (this.count != 0) {
/* 428 */       put(PdfName.COUNT, new PdfNumber(this.count));
/*     */     }
/* 430 */     super.toPdf(writer, os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addKid(PdfOutline outline) {
/* 438 */     this.kids.add(outline);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<PdfOutline> getKids() {
/* 446 */     return this.kids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKids(ArrayList<PdfOutline> kids) {
/* 454 */     this.kids = kids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/* 461 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(String tag) {
/* 468 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 476 */     PdfString title = (PdfString)get(PdfName.TITLE);
/* 477 */     return title.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {
/* 485 */     put(PdfName.TITLE, new PdfString(title, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 492 */     return this.open;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpen(boolean open) {
/* 499 */     this.open = open;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getColor() {
/* 507 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(BaseColor color) {
/* 515 */     this.color = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStyle() {
/* 523 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(int style) {
/* 531 */     this.style = style;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */