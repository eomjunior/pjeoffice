/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ public class PdfStructureTreeRoot
/*     */   extends PdfDictionary
/*     */   implements IPdfStructureElement
/*     */ {
/*  59 */   private HashMap<Integer, PdfObject> parentTree = new HashMap<Integer, PdfObject>();
/*     */   private PdfIndirectReference reference;
/*  61 */   private PdfDictionary classMap = null;
/*  62 */   protected HashMap<PdfName, PdfObject> classes = null;
/*  63 */   private HashMap<Integer, PdfIndirectReference> numTree = null;
/*     */ 
/*     */   
/*     */   private HashMap<String, PdfObject> idTreeMap;
/*     */ 
/*     */   
/*     */   private PdfWriter writer;
/*     */ 
/*     */   
/*     */   PdfStructureTreeRoot(PdfWriter writer) {
/*  73 */     super(PdfName.STRUCTTREEROOT);
/*  74 */     this.writer = writer;
/*  75 */     this.reference = writer.getPdfIndirectReference();
/*     */   }
/*     */   
/*     */   private void createNumTree() throws IOException {
/*  79 */     if (this.numTree != null)
/*  80 */       return;  this.numTree = new HashMap<Integer, PdfIndirectReference>();
/*  81 */     for (Integer i : this.parentTree.keySet()) {
/*  82 */       PdfObject obj = this.parentTree.get(i);
/*  83 */       if (obj.isArray()) {
/*  84 */         PdfArray ar = (PdfArray)obj;
/*  85 */         this.numTree.put(i, this.writer.addToBody(ar).getIndirectReference()); continue;
/*  86 */       }  if (obj instanceof PdfIndirectReference) {
/*  87 */         this.numTree.put(i, (PdfIndirectReference)obj);
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
/*     */   public void mapRole(PdfName used, PdfName standard) {
/*  99 */     PdfDictionary rm = (PdfDictionary)get(PdfName.ROLEMAP);
/* 100 */     if (rm == null) {
/* 101 */       rm = new PdfDictionary();
/* 102 */       put(PdfName.ROLEMAP, rm);
/*     */     } 
/* 104 */     rm.put(used, standard);
/*     */   }
/*     */   
/*     */   public void mapClass(PdfName name, PdfObject object) {
/* 108 */     if (this.classMap == null) {
/* 109 */       this.classMap = new PdfDictionary();
/* 110 */       this.classes = new HashMap<PdfName, PdfObject>();
/*     */     } 
/* 112 */     this.classes.put(name, object);
/*     */   }
/*     */   
/*     */   void putIDTree(String record, PdfObject reference) {
/* 116 */     if (this.idTreeMap == null)
/* 117 */       this.idTreeMap = new HashMap<String, PdfObject>(); 
/* 118 */     this.idTreeMap.put(record, reference);
/*     */   }
/*     */   
/*     */   public PdfObject getMappedClass(PdfName name) {
/* 122 */     if (this.classes == null)
/* 123 */       return null; 
/* 124 */     return this.classes.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfWriter getWriter() {
/* 132 */     return this.writer;
/*     */   }
/*     */   
/*     */   public HashMap<Integer, PdfIndirectReference> getNumTree() throws IOException {
/* 136 */     if (this.numTree == null) createNumTree(); 
/* 137 */     return this.numTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getReference() {
/* 146 */     return this.reference;
/*     */   }
/*     */   
/*     */   void setPageMark(int page, PdfIndirectReference struc) {
/* 150 */     Integer i = Integer.valueOf(page);
/* 151 */     PdfArray ar = (PdfArray)this.parentTree.get(i);
/* 152 */     if (ar == null) {
/* 153 */       ar = new PdfArray();
/* 154 */       this.parentTree.put(i, ar);
/*     */     } 
/* 156 */     ar.add(struc);
/*     */   }
/*     */   
/*     */   void setAnnotationMark(int structParentIndex, PdfIndirectReference struc) {
/* 160 */     this.parentTree.put(Integer.valueOf(structParentIndex), struc);
/*     */   }
/*     */   
/*     */   void buildTree() throws IOException {
/* 164 */     createNumTree();
/* 165 */     PdfDictionary dicTree = PdfNumberTree.writeTree(this.numTree, this.writer);
/* 166 */     if (dicTree != null)
/* 167 */       put(PdfName.PARENTTREE, this.writer.addToBody(dicTree).getIndirectReference()); 
/* 168 */     if (this.classMap != null && !this.classes.isEmpty()) {
/* 169 */       for (Map.Entry<PdfName, PdfObject> entry : this.classes.entrySet()) {
/* 170 */         PdfObject value = entry.getValue();
/* 171 */         if (value.isDictionary()) {
/* 172 */           this.classMap.put(entry.getKey(), this.writer.addToBody(value).getIndirectReference()); continue;
/* 173 */         }  if (value.isArray()) {
/* 174 */           PdfArray newArray = new PdfArray();
/* 175 */           PdfArray array = (PdfArray)value;
/* 176 */           for (int i = 0; i < array.size(); i++) {
/* 177 */             if (array.getPdfObject(i).isDictionary())
/* 178 */               newArray.add(this.writer.addToBody(array.getAsDict(i)).getIndirectReference()); 
/*     */           } 
/* 180 */           this.classMap.put(entry.getKey(), newArray);
/*     */         } 
/*     */       } 
/* 183 */       put(PdfName.CLASSMAP, this.writer.addToBody(this.classMap).getIndirectReference());
/*     */     } 
/* 185 */     if (this.idTreeMap != null && !this.idTreeMap.isEmpty()) {
/* 186 */       PdfDictionary dic = PdfNameTree.writeTree(this.idTreeMap, this.writer);
/* 187 */       put(PdfName.IDTREE, dic);
/*     */     } 
/* 189 */     this.writer.addToBody(this, this.reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject getAttribute(PdfName name) {
/* 198 */     PdfDictionary attr = getAsDict(PdfName.A);
/* 199 */     if (attr != null && 
/* 200 */       attr.contains(name)) {
/* 201 */       return attr.get(name);
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(PdfName name, PdfObject obj) {
/* 211 */     PdfDictionary attr = getAsDict(PdfName.A);
/* 212 */     if (attr == null) {
/* 213 */       attr = new PdfDictionary();
/* 214 */       put(PdfName.A, attr);
/*     */     } 
/* 216 */     attr.put(name, obj);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStructureTreeRoot.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */