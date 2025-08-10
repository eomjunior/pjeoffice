/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class PRAcroForm
/*     */   extends PdfDictionary
/*     */ {
/*     */   ArrayList<FieldInformation> fields;
/*     */   ArrayList<PdfDictionary> stack;
/*     */   HashMap<String, FieldInformation> fieldByName;
/*     */   PdfReader reader;
/*     */   
/*     */   public static class FieldInformation
/*     */   {
/*     */     String fieldName;
/*     */     PdfDictionary info;
/*     */     PRIndirectReference ref;
/*     */     
/*     */     FieldInformation(String fieldName, PdfDictionary info, PRIndirectReference ref) {
/*  67 */       this.fieldName = fieldName;
/*  68 */       this.info = info;
/*  69 */       this.ref = ref;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getWidgetName() {
/*  77 */       PdfObject name = this.info.get(PdfName.NM);
/*  78 */       if (name != null)
/*  79 */         return name.toString(); 
/*  80 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/*  88 */       return this.fieldName;
/*     */     }
/*     */     
/*     */     public PdfDictionary getInfo() {
/*  92 */       return this.info;
/*     */     }
/*     */     
/*     */     public PRIndirectReference getRef() {
/*  96 */       return this.ref;
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
/*     */   public PRAcroForm(PdfReader reader) {
/* 109 */     this.reader = reader;
/* 110 */     this.fields = new ArrayList<FieldInformation>();
/* 111 */     this.fieldByName = new HashMap<String, FieldInformation>();
/* 112 */     this.stack = new ArrayList<PdfDictionary>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 120 */     return this.fields.size();
/*     */   }
/*     */   
/*     */   public ArrayList<FieldInformation> getFields() {
/* 124 */     return this.fields;
/*     */   }
/*     */   
/*     */   public FieldInformation getField(String name) {
/* 128 */     return this.fieldByName.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PRIndirectReference getRefByName(String name) {
/* 137 */     FieldInformation fi = this.fieldByName.get(name);
/* 138 */     if (fi == null) return null; 
/* 139 */     return fi.getRef();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readAcroForm(PdfDictionary root) {
/* 146 */     if (root == null)
/*     */       return; 
/* 148 */     this.hashMap = root.hashMap;
/* 149 */     pushAttrib(root);
/* 150 */     PdfArray fieldlist = (PdfArray)PdfReader.getPdfObjectRelease(root.get(PdfName.FIELDS));
/* 151 */     if (fieldlist != null) {
/* 152 */       iterateFields(fieldlist, null, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iterateFields(PdfArray fieldlist, PRIndirectReference fieldDict, String parentPath) {
/* 163 */     for (Iterator<PdfObject> it = fieldlist.listIterator(); it.hasNext(); ) {
/* 164 */       PRIndirectReference ref = (PRIndirectReference)it.next();
/* 165 */       PdfDictionary dict = (PdfDictionary)PdfReader.getPdfObjectRelease(ref);
/*     */ 
/*     */       
/* 168 */       PRIndirectReference myFieldDict = fieldDict;
/* 169 */       String fullPath = parentPath;
/* 170 */       PdfString tField = (PdfString)dict.get(PdfName.T);
/* 171 */       boolean isFieldDict = (tField != null);
/*     */       
/* 173 */       if (isFieldDict) {
/* 174 */         myFieldDict = ref;
/* 175 */         if (parentPath == null) {
/* 176 */           fullPath = tField.toString();
/*     */         } else {
/*     */           
/* 179 */           fullPath = parentPath + '.' + tField.toString();
/*     */         } 
/*     */       } 
/*     */       
/* 183 */       PdfArray kids = (PdfArray)dict.get(PdfName.KIDS);
/* 184 */       if (kids != null) {
/* 185 */         pushAttrib(dict);
/* 186 */         iterateFields(kids, myFieldDict, fullPath);
/* 187 */         this.stack.remove(this.stack.size() - 1);
/*     */         continue;
/*     */       } 
/* 190 */       if (myFieldDict != null) {
/* 191 */         PdfDictionary mergedDict = this.stack.get(this.stack.size() - 1);
/* 192 */         if (isFieldDict) {
/* 193 */           mergedDict = mergeAttrib(mergedDict, dict);
/*     */         }
/* 195 */         mergedDict.put(PdfName.T, new PdfString(fullPath));
/* 196 */         FieldInformation fi = new FieldInformation(fullPath, mergedDict, myFieldDict);
/* 197 */         this.fields.add(fi);
/* 198 */         this.fieldByName.put(fullPath, fi);
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
/*     */   protected PdfDictionary mergeAttrib(PdfDictionary parent, PdfDictionary child) {
/* 210 */     PdfDictionary targ = new PdfDictionary();
/* 211 */     if (parent != null) targ.putAll(parent);
/*     */     
/* 213 */     for (PdfName element : child.getKeys()) {
/* 214 */       PdfName key = element;
/* 215 */       if (key.equals(PdfName.DR) || key.equals(PdfName.DA) || key
/* 216 */         .equals(PdfName.Q) || key.equals(PdfName.FF) || key
/* 217 */         .equals(PdfName.DV) || key.equals(PdfName.V) || key
/* 218 */         .equals(PdfName.FT) || key.equals(PdfName.NM) || key
/* 219 */         .equals(PdfName.F)) {
/* 220 */         targ.put(key, child.get(key));
/*     */       }
/*     */     } 
/* 223 */     return targ;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushAttrib(PdfDictionary dict) {
/* 229 */     PdfDictionary dic = null;
/* 230 */     if (!this.stack.isEmpty()) {
/* 231 */       dic = this.stack.get(this.stack.size() - 1);
/*     */     }
/* 233 */     dic = mergeAttrib(dic, dict);
/* 234 */     this.stack.add(dic);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PRAcroForm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */