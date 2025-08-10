/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.AccessibleElementId;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfTemplate
/*     */   extends PdfContentByte
/*     */   implements IAccessibleElement
/*     */ {
/*     */   public static final int TYPE_TEMPLATE = 1;
/*     */   public static final int TYPE_IMPORTED = 2;
/*     */   public static final int TYPE_PATTERN = 3;
/*     */   protected int type;
/*     */   protected PdfIndirectReference thisReference;
/*     */   protected PageResources pageResources;
/*  69 */   protected Rectangle bBox = new Rectangle(0.0F, 0.0F);
/*     */ 
/*     */   
/*     */   protected PdfArray matrix;
/*     */ 
/*     */   
/*     */   protected PdfTransparencyGroup group;
/*     */ 
/*     */   
/*     */   protected PdfOCG layer;
/*     */ 
/*     */   
/*     */   protected PdfIndirectReference pageReference;
/*     */   
/*     */   protected boolean contentTagged = false;
/*     */   
/*  85 */   private PdfDictionary additional = null;
/*     */   
/*  87 */   protected PdfName role = PdfName.FIGURE;
/*  88 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  89 */   private AccessibleElementId id = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfTemplate() {
/*  96 */     super(null);
/*  97 */     this.type = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfTemplate(PdfWriter wr) {
/* 107 */     super(wr);
/* 108 */     this.type = 1;
/* 109 */     this.pageResources = new PageResources();
/* 110 */     this.pageResources.addDefaultColor(wr.getDefaultColorspace());
/* 111 */     this.thisReference = this.writer.getPdfIndirectReference();
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
/*     */   public static PdfTemplate createTemplate(PdfWriter writer, float width, float height) {
/* 128 */     return createTemplate(writer, width, height, (PdfName)null);
/*     */   }
/*     */   
/*     */   static PdfTemplate createTemplate(PdfWriter writer, float width, float height, PdfName forcedName) {
/* 132 */     PdfTemplate template = new PdfTemplate(writer);
/* 133 */     template.setWidth(width);
/* 134 */     template.setHeight(height);
/* 135 */     writer.addDirectTemplateSimple(template, forcedName);
/* 136 */     return template;
/*     */   }
/*     */   
/*     */   public boolean isTagged() {
/* 140 */     return (super.isTagged() && this.contentTagged);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWidth(float width) {
/* 150 */     this.bBox.setLeft(0.0F);
/* 151 */     this.bBox.setRight(width);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeight(float height) {
/* 161 */     this.bBox.setBottom(0.0F);
/* 162 */     this.bBox.setTop(height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth() {
/* 171 */     return this.bBox.getWidth();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHeight() {
/* 181 */     return this.bBox.getHeight();
/*     */   }
/*     */   
/*     */   public Rectangle getBoundingBox() {
/* 185 */     return this.bBox;
/*     */   }
/*     */   
/*     */   public void setBoundingBox(Rectangle bBox) {
/* 189 */     this.bBox = bBox;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLayer(PdfOCG layer) {
/* 197 */     this.layer = layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfOCG getLayer() {
/* 205 */     return this.layer;
/*     */   }
/*     */   
/*     */   public void setMatrix(float a, float b, float c, float d, float e, float f) {
/* 209 */     this.matrix = new PdfArray();
/* 210 */     this.matrix.add(new PdfNumber(a));
/* 211 */     this.matrix.add(new PdfNumber(b));
/* 212 */     this.matrix.add(new PdfNumber(c));
/* 213 */     this.matrix.add(new PdfNumber(d));
/* 214 */     this.matrix.add(new PdfNumber(e));
/* 215 */     this.matrix.add(new PdfNumber(f));
/*     */   }
/*     */   
/*     */   PdfArray getMatrix() {
/* 219 */     return this.matrix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getIndirectReference() {
/* 230 */     if (this.thisReference == null) {
/* 231 */       this.thisReference = this.writer.getPdfIndirectReference();
/*     */     }
/* 233 */     return this.thisReference;
/*     */   }
/*     */   
/*     */   public void beginVariableText() {
/* 237 */     this.content.append("/Tx BMC ");
/*     */   }
/*     */   
/*     */   public void endVariableText() {
/* 241 */     this.content.append("EMC ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfObject getResources() {
/* 251 */     return getPageResources().getResources();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFormXObject(int compressionLevel) throws IOException {
/* 262 */     return new PdfFormXObject(this, compressionLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfContentByte getDuplicate() {
/* 272 */     PdfTemplate tpl = new PdfTemplate();
/* 273 */     tpl.writer = this.writer;
/* 274 */     tpl.pdf = this.pdf;
/* 275 */     tpl.thisReference = this.thisReference;
/* 276 */     tpl.pageResources = this.pageResources;
/* 277 */     tpl.bBox = new Rectangle(this.bBox);
/* 278 */     tpl.group = this.group;
/* 279 */     tpl.layer = this.layer;
/* 280 */     if (this.matrix != null) {
/* 281 */       tpl.matrix = new PdfArray(this.matrix);
/*     */     }
/* 283 */     tpl.separator = this.separator;
/* 284 */     tpl.additional = this.additional;
/* 285 */     tpl.contentTagged = this.contentTagged;
/* 286 */     tpl.duplicatedFrom = this;
/* 287 */     return tpl;
/*     */   }
/*     */   
/*     */   public int getType() {
/* 291 */     return this.type;
/*     */   }
/*     */   
/*     */   PageResources getPageResources() {
/* 295 */     return this.pageResources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTransparencyGroup getGroup() {
/* 303 */     return this.group;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroup(PdfTransparencyGroup group) {
/* 311 */     this.group = group;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getAdditional() {
/* 322 */     return this.additional;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditional(PdfDictionary additional) {
/* 333 */     this.additional = additional;
/*     */   }
/*     */   
/*     */   public PdfIndirectReference getCurrentPage() {
/* 337 */     return (this.pageReference == null) ? this.writer.getCurrentPage() : this.pageReference;
/*     */   }
/*     */   
/*     */   public PdfIndirectReference getPageReference() {
/* 341 */     return this.pageReference;
/*     */   }
/*     */   
/*     */   public void setPageReference(PdfIndirectReference pageReference) {
/* 345 */     this.pageReference = pageReference;
/*     */   }
/*     */   
/*     */   public boolean isContentTagged() {
/* 349 */     return this.contentTagged;
/*     */   }
/*     */   
/*     */   public void setContentTagged(boolean contentTagged) {
/* 353 */     this.contentTagged = contentTagged;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 357 */     if (this.accessibleAttributes != null) {
/* 358 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 360 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 364 */     if (this.accessibleAttributes == null)
/* 365 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 366 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 370 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 374 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 378 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 382 */     if (this.id == null)
/* 383 */       this.id = new AccessibleElementId(); 
/* 384 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 388 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 392 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */