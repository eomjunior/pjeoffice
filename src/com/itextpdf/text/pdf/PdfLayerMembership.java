/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfLayerMembership
/*     */   extends PdfDictionary
/*     */   implements PdfOCG
/*     */ {
/*  63 */   public static final PdfName ALLON = new PdfName("AllOn");
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static final PdfName ANYON = new PdfName("AnyOn");
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final PdfName ANYOFF = new PdfName("AnyOff");
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final PdfName ALLOFF = new PdfName("AllOff");
/*     */   
/*     */   PdfIndirectReference ref;
/*  78 */   PdfArray members = new PdfArray();
/*  79 */   HashSet<PdfLayer> layers = new HashSet<PdfLayer>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfLayerMembership(PdfWriter writer) {
/*  86 */     super(PdfName.OCMD);
/*  87 */     put(PdfName.OCGS, this.members);
/*  88 */     this.ref = writer.getPdfIndirectReference();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getRef() {
/*  96 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMember(PdfLayer layer) {
/* 104 */     if (!this.layers.contains(layer)) {
/* 105 */       this.members.add(layer.getRef());
/* 106 */       this.layers.add(layer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<PdfLayer> getLayers() {
/* 115 */     return this.layers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisibilityPolicy(PdfName type) {
/* 125 */     put(PdfName.P, type);
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
/*     */   public void setVisibilityExpression(PdfVisibilityExpression ve) {
/* 137 */     put(PdfName.VE, ve);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject getPdfObject() {
/* 145 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLayerMembership.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */