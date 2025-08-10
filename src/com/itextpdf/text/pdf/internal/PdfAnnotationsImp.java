/*     */ package com.itextpdf.text.pdf.internal;
/*     */ 
/*     */ import com.itextpdf.text.Annotation;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.PdfAcroForm;
/*     */ import com.itextpdf.text.pdf.PdfAction;
/*     */ import com.itextpdf.text.pdf.PdfAnnotation;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfFileSpecification;
/*     */ import com.itextpdf.text.pdf.PdfFormField;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfRectangle;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import com.itextpdf.text.pdf.PdfTemplate;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
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
/*     */ public class PdfAnnotationsImp
/*     */ {
/*     */   protected PdfAcroForm acroForm;
/*  78 */   protected ArrayList<PdfAnnotation> annotations = new ArrayList<PdfAnnotation>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected ArrayList<PdfAnnotation> delayedAnnotations = new ArrayList<PdfAnnotation>();
/*     */ 
/*     */   
/*     */   public PdfAnnotationsImp(PdfWriter writer) {
/*  88 */     this.acroForm = new PdfAcroForm(writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValidAcroForm() {
/*  95 */     return this.acroForm.isValid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAcroForm getAcroForm() {
/* 103 */     return this.acroForm;
/*     */   }
/*     */   
/*     */   public void setSigFlags(int f) {
/* 107 */     this.acroForm.setSigFlags(f);
/*     */   }
/*     */   
/*     */   public void addCalculationOrder(PdfFormField formField) {
/* 111 */     this.acroForm.addCalculationOrder(formField);
/*     */   }
/*     */   
/*     */   public void addAnnotation(PdfAnnotation annot) {
/* 115 */     if (annot.isForm()) {
/* 116 */       PdfFormField field = (PdfFormField)annot;
/* 117 */       if (field.getParent() == null) {
/* 118 */         addFormFieldRaw(field);
/*     */       }
/*     */     } else {
/* 121 */       this.annotations.add(annot);
/*     */     } 
/*     */   }
/*     */   public void addPlainAnnotation(PdfAnnotation annot) {
/* 125 */     this.annotations.add(annot);
/*     */   }
/*     */   
/*     */   void addFormFieldRaw(PdfFormField field) {
/* 129 */     this.annotations.add(field);
/* 130 */     ArrayList<PdfFormField> kids = field.getKids();
/* 131 */     if (kids != null)
/* 132 */       for (int k = 0; k < kids.size(); k++) {
/* 133 */         PdfFormField kid = kids.get(k);
/* 134 */         if (!kid.isUsed()) {
/* 135 */           addFormFieldRaw(kid);
/*     */         }
/*     */       }  
/*     */   }
/*     */   
/*     */   public boolean hasUnusedAnnotations() {
/* 141 */     return !this.annotations.isEmpty();
/*     */   }
/*     */   
/*     */   public void resetAnnotations() {
/* 145 */     this.annotations = this.delayedAnnotations;
/* 146 */     this.delayedAnnotations = new ArrayList<PdfAnnotation>();
/*     */   }
/*     */   
/*     */   public PdfArray rotateAnnotations(PdfWriter writer, Rectangle pageSize) {
/* 150 */     PdfArray array = new PdfArray();
/* 151 */     int rotation = pageSize.getRotation() % 360;
/* 152 */     int currentPage = writer.getCurrentPageNumber();
/* 153 */     for (int k = 0; k < this.annotations.size(); k++) {
/* 154 */       PdfAnnotation dic = this.annotations.get(k);
/* 155 */       int page = dic.getPlaceInPage();
/* 156 */       if (page > currentPage) {
/* 157 */         this.delayedAnnotations.add(dic);
/*     */       } else {
/*     */         
/* 160 */         if (dic.isForm()) {
/* 161 */           if (!dic.isUsed()) {
/* 162 */             HashSet<PdfTemplate> templates = dic.getTemplates();
/* 163 */             if (templates != null)
/* 164 */               this.acroForm.addFieldTemplates(templates); 
/*     */           } 
/* 166 */           PdfFormField field = (PdfFormField)dic;
/* 167 */           if (field.getParent() == null)
/* 168 */             this.acroForm.addDocumentField(field.getIndirectReference()); 
/*     */         } 
/* 170 */         if (dic.isAnnotation()) {
/* 171 */           array.add((PdfObject)dic.getIndirectReference());
/* 172 */           if (!dic.isUsed()) {
/* 173 */             PdfRectangle rect; PdfArray tmp = dic.getAsArray(PdfName.RECT);
/*     */             
/* 175 */             if (tmp.size() == 4) {
/* 176 */               rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue(), tmp.getAsNumber(2).floatValue(), tmp.getAsNumber(3).floatValue());
/*     */             } else {
/*     */               
/* 179 */               rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue());
/*     */             } 
/* 181 */             switch (rotation) {
/*     */               case 90:
/* 183 */                 dic.put(PdfName.RECT, (PdfObject)new PdfRectangle(pageSize
/* 184 */                       .getTop() - rect.bottom(), rect
/* 185 */                       .left(), pageSize
/* 186 */                       .getTop() - rect.top(), rect
/* 187 */                       .right()));
/*     */                 break;
/*     */               case 180:
/* 190 */                 dic.put(PdfName.RECT, (PdfObject)new PdfRectangle(pageSize
/* 191 */                       .getRight() - rect.left(), pageSize
/* 192 */                       .getTop() - rect.bottom(), pageSize
/* 193 */                       .getRight() - rect.right(), pageSize
/* 194 */                       .getTop() - rect.top()));
/*     */                 break;
/*     */               case 270:
/* 197 */                 dic.put(PdfName.RECT, (PdfObject)new PdfRectangle(rect
/* 198 */                       .bottom(), pageSize
/* 199 */                       .getRight() - rect.left(), rect
/* 200 */                       .top(), pageSize
/* 201 */                       .getRight() - rect.right()));
/*     */                 break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 206 */         if (!dic.isUsed()) {
/* 207 */           dic.setUsed();
/*     */           try {
/* 209 */             writer.addToBody((PdfObject)dic, dic.getIndirectReference());
/*     */           }
/* 211 */           catch (IOException e) {
/* 212 */             throw new ExceptionConverter(e);
/*     */           } 
/*     */         } 
/*     */       } 
/* 216 */     }  return array; } public static PdfAnnotation convertAnnotation(PdfWriter writer, Annotation annot, Rectangle defaultRect) throws IOException { boolean[] sparams; String fname;
/*     */     String mimetype;
/*     */     PdfFileSpecification fs;
/*     */     PdfAnnotation ann;
/* 220 */     switch (annot.annotationType()) {
/*     */       case 1:
/* 222 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((URL)annot.attributes().get("url")), null);
/*     */       case 2:
/* 224 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String)annot.attributes().get("file")), null);
/*     */       case 3:
/* 226 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String)annot.attributes().get("file"), (String)annot.attributes().get("destination")), null);
/*     */       case 7:
/* 228 */         sparams = (boolean[])annot.attributes().get("parameters");
/* 229 */         fname = (String)annot.attributes().get("file");
/* 230 */         mimetype = (String)annot.attributes().get("mime");
/*     */         
/* 232 */         if (sparams[0]) {
/* 233 */           fs = PdfFileSpecification.fileEmbedded(writer, fname, fname, null);
/*     */         } else {
/* 235 */           fs = PdfFileSpecification.fileExtern(writer, fname);
/* 236 */         }  ann = PdfAnnotation.createScreen(writer, new Rectangle(annot.llx(), annot.lly(), annot.urx(), annot.ury()), fname, fs, mimetype, sparams[1]);
/*     */         
/* 238 */         return ann;
/*     */       case 4:
/* 240 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String)annot.attributes().get("file"), ((Integer)annot.attributes().get("page")).intValue()), null);
/*     */       case 5:
/* 242 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction(((Integer)annot.attributes().get("named")).intValue()), null);
/*     */       case 6:
/* 244 */         return writer.createAnnotation(annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String)annot.attributes().get("application"), (String)annot.attributes().get("parameters"), (String)annot.attributes().get("operation"), (String)annot.attributes().get("defaultdir")), null);
/*     */     } 
/* 246 */     return writer.createAnnotation(defaultRect.getLeft(), defaultRect.getBottom(), defaultRect.getRight(), defaultRect.getTop(), new PdfString(annot.title(), "UnicodeBig"), new PdfString(annot.content(), "UnicodeBig"), null); }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/internal/PdfAnnotationsImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */