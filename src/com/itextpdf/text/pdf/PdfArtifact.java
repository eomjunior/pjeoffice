/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.AccessibleElementId;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
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
/*     */ public class PdfArtifact
/*     */   implements IAccessibleElement
/*     */ {
/*  54 */   private static final HashSet<String> allowedArtifactTypes = new HashSet<String>(Arrays.asList(new String[] { "Pagination", "Layout", "Page", "Background" }));
/*     */   
/*  56 */   protected PdfName role = PdfName.ARTIFACT;
/*  57 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  58 */   protected AccessibleElementId id = new AccessibleElementId();
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/*  61 */     if (this.accessibleAttributes != null) {
/*  62 */       return this.accessibleAttributes.get(key);
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/*  68 */     if (this.accessibleAttributes == null)
/*  69 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/*  70 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/*  74 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/*  78 */     return this.role;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRole(PdfName role) {}
/*     */   
/*     */   public AccessibleElementId getId() {
/*  85 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/*  89 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   public PdfString getType() {
/*  97 */     return (this.accessibleAttributes == null) ? null : (PdfString)this.accessibleAttributes.get(PdfName.TYPE);
/*     */   }
/*     */   
/*     */   public void setType(PdfString type) {
/* 101 */     if (!allowedArtifactTypes.contains(type.toString()))
/* 102 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.artifact.type.1.is.invalid", new Object[] { type })); 
/* 103 */     setAccessibleAttribute(PdfName.TYPE, type);
/*     */   }
/*     */   
/*     */   public void setType(ArtifactType type) {
/* 107 */     PdfString artifactType = null;
/* 108 */     switch (type) {
/*     */       case BACKGROUND:
/* 110 */         artifactType = new PdfString("Background");
/*     */         break;
/*     */       case LAYOUT:
/* 113 */         artifactType = new PdfString("Layout");
/*     */         break;
/*     */       case PAGE:
/* 116 */         artifactType = new PdfString("Page");
/*     */         break;
/*     */       case PAGINATION:
/* 119 */         artifactType = new PdfString("Pagination");
/*     */         break;
/*     */     } 
/* 122 */     setAccessibleAttribute(PdfName.TYPE, artifactType);
/*     */   }
/*     */   
/*     */   public PdfArray getBBox() {
/* 126 */     return (this.accessibleAttributes == null) ? null : (PdfArray)this.accessibleAttributes.get(PdfName.BBOX);
/*     */   }
/*     */   
/*     */   public void setBBox(PdfArray bbox) {
/* 130 */     setAccessibleAttribute(PdfName.BBOX, bbox);
/*     */   }
/*     */   
/*     */   public PdfArray getAttached() {
/* 134 */     return (this.accessibleAttributes == null) ? null : (PdfArray)this.accessibleAttributes.get(PdfName.ATTACHED);
/*     */   }
/*     */   
/*     */   public void setAttached(PdfArray attached) {
/* 138 */     setAccessibleAttribute(PdfName.ATTACHED, attached);
/*     */   }
/*     */   
/*     */   public enum ArtifactType {
/* 142 */     PAGINATION,
/* 143 */     LAYOUT,
/* 144 */     PAGE,
/* 145 */     BACKGROUND;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfArtifact.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */