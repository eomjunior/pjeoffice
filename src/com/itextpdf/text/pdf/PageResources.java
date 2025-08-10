/*     */ package com.itextpdf.text.pdf;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ class PageResources
/*     */ {
/*  51 */   protected PdfDictionary fontDictionary = new PdfDictionary();
/*  52 */   protected PdfDictionary xObjectDictionary = new PdfDictionary();
/*  53 */   protected PdfDictionary colorDictionary = new PdfDictionary();
/*  54 */   protected PdfDictionary patternDictionary = new PdfDictionary();
/*  55 */   protected PdfDictionary shadingDictionary = new PdfDictionary();
/*  56 */   protected PdfDictionary extGStateDictionary = new PdfDictionary();
/*  57 */   protected PdfDictionary propertyDictionary = new PdfDictionary();
/*     */   protected HashSet<PdfName> forbiddenNames;
/*     */   protected PdfDictionary originalResources;
/*  60 */   protected int[] namePtr = new int[] { 0 };
/*     */ 
/*     */   
/*     */   protected HashMap<PdfName, PdfName> usedNames;
/*     */ 
/*     */   
/*     */   void setOriginalResources(PdfDictionary resources, int[] newNamePtr) {
/*  67 */     if (newNamePtr != null)
/*  68 */       this.namePtr = newNamePtr; 
/*  69 */     this.forbiddenNames = new HashSet<PdfName>();
/*  70 */     this.usedNames = new HashMap<PdfName, PdfName>();
/*  71 */     if (resources == null)
/*     */       return; 
/*  73 */     this.originalResources = new PdfDictionary();
/*  74 */     this.originalResources.merge(resources);
/*  75 */     for (PdfName element : resources.getKeys()) {
/*  76 */       PdfName key = element;
/*  77 */       PdfObject sub = PdfReader.getPdfObject(resources.get(key));
/*  78 */       if (sub != null && sub.isDictionary()) {
/*  79 */         PdfDictionary dic = (PdfDictionary)sub;
/*  80 */         for (PdfName element2 : dic.getKeys()) {
/*  81 */           this.forbiddenNames.add(element2);
/*     */         }
/*  83 */         PdfDictionary dic2 = new PdfDictionary();
/*  84 */         dic2.merge(dic);
/*  85 */         this.originalResources.put(key, dic2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   PdfName translateName(PdfName name) {
/*  91 */     PdfName translated = name;
/*  92 */     if (this.forbiddenNames != null) {
/*  93 */       translated = this.usedNames.get(name);
/*  94 */       if (translated == null) {
/*     */         do {
/*  96 */           this.namePtr[0] = this.namePtr[0] + 1; translated = new PdfName("Xi" + this.namePtr[0]);
/*  97 */         } while (this.forbiddenNames.contains(translated));
/*     */ 
/*     */         
/* 100 */         this.usedNames.put(name, translated);
/*     */       } 
/*     */     } 
/* 103 */     return translated;
/*     */   }
/*     */   
/*     */   PdfName addFont(PdfName name, PdfIndirectReference reference) {
/* 107 */     name = translateName(name);
/* 108 */     this.fontDictionary.put(name, reference);
/* 109 */     return name;
/*     */   }
/*     */   
/*     */   PdfName addXObject(PdfName name, PdfIndirectReference reference) {
/* 113 */     name = translateName(name);
/* 114 */     this.xObjectDictionary.put(name, reference);
/* 115 */     return name;
/*     */   }
/*     */   
/*     */   PdfName addColor(PdfName name, PdfIndirectReference reference) {
/* 119 */     name = translateName(name);
/* 120 */     this.colorDictionary.put(name, reference);
/* 121 */     return name;
/*     */   }
/*     */   
/*     */   void addDefaultColor(PdfName name, PdfObject obj) {
/* 125 */     if (obj == null || obj.isNull()) {
/* 126 */       this.colorDictionary.remove(name);
/*     */     } else {
/* 128 */       this.colorDictionary.put(name, obj);
/*     */     } 
/*     */   }
/*     */   void addDefaultColor(PdfDictionary dic) {
/* 132 */     this.colorDictionary.merge(dic);
/*     */   }
/*     */   
/*     */   void addDefaultColorDiff(PdfDictionary dic) {
/* 136 */     this.colorDictionary.mergeDifferent(dic);
/*     */   }
/*     */   
/*     */   PdfName addShading(PdfName name, PdfIndirectReference reference) {
/* 140 */     name = translateName(name);
/* 141 */     this.shadingDictionary.put(name, reference);
/* 142 */     return name;
/*     */   }
/*     */   
/*     */   PdfName addPattern(PdfName name, PdfIndirectReference reference) {
/* 146 */     name = translateName(name);
/* 147 */     this.patternDictionary.put(name, reference);
/* 148 */     return name;
/*     */   }
/*     */   
/*     */   PdfName addExtGState(PdfName name, PdfIndirectReference reference) {
/* 152 */     name = translateName(name);
/* 153 */     this.extGStateDictionary.put(name, reference);
/* 154 */     return name;
/*     */   }
/*     */   
/*     */   PdfName addProperty(PdfName name, PdfIndirectReference reference) {
/* 158 */     name = translateName(name);
/* 159 */     this.propertyDictionary.put(name, reference);
/* 160 */     return name;
/*     */   }
/*     */   
/*     */   PdfDictionary getResources() {
/* 164 */     PdfResources resources = new PdfResources();
/* 165 */     if (this.originalResources != null)
/* 166 */       resources.putAll(this.originalResources); 
/* 167 */     resources.add(PdfName.FONT, this.fontDictionary);
/* 168 */     resources.add(PdfName.XOBJECT, this.xObjectDictionary);
/* 169 */     resources.add(PdfName.COLORSPACE, this.colorDictionary);
/* 170 */     resources.add(PdfName.PATTERN, this.patternDictionary);
/* 171 */     resources.add(PdfName.SHADING, this.shadingDictionary);
/* 172 */     resources.add(PdfName.EXTGSTATE, this.extGStateDictionary);
/* 173 */     resources.add(PdfName.PROPERTIES, this.propertyDictionary);
/* 174 */     return resources;
/*     */   }
/*     */   
/*     */   boolean hasResources() {
/* 178 */     return (this.fontDictionary.size() > 0 || this.xObjectDictionary
/* 179 */       .size() > 0 || this.colorDictionary
/* 180 */       .size() > 0 || this.patternDictionary
/* 181 */       .size() > 0 || this.shadingDictionary
/* 182 */       .size() > 0 || this.extGStateDictionary
/* 183 */       .size() > 0 || this.propertyDictionary
/* 184 */       .size() > 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PageResources.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */