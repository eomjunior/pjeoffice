/*     */ package com.itextpdf.text.pdf.fonts.otf;
/*     */ 
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public abstract class OpenTypeFontTableReader
/*     */ {
/*  66 */   protected static final Logger LOG = LoggerFactory.getLogger(OpenTypeFontTableReader.class);
/*     */   
/*     */   protected final RandomAccessFileOrArray rf;
/*     */   
/*     */   protected final int tableLocation;
/*     */   
/*     */   private List<String> supportedLanguages;
/*     */   
/*     */   public OpenTypeFontTableReader(RandomAccessFileOrArray rf, int tableLocation) throws IOException {
/*  75 */     this.rf = rf;
/*  76 */     this.tableLocation = tableLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Language getSupportedLanguage() throws FontReadingException {
/*  81 */     Language[] allLangs = Language.values();
/*     */     
/*  83 */     for (String supportedLang : this.supportedLanguages) {
/*  84 */       for (Language lang : allLangs) {
/*  85 */         if (lang.isSupported(supportedLang)) {
/*  86 */           return lang;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     throw new FontReadingException("Unsupported languages " + this.supportedLanguages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void startReadingTable() throws FontReadingException {
/*     */     try {
/* 102 */       TableHeader header = readHeader();
/*     */       
/* 104 */       readScriptListTable(this.tableLocation + header.scriptListOffset);
/*     */ 
/*     */       
/* 107 */       readFeatureListTable(this.tableLocation + header.featureListOffset);
/*     */ 
/*     */       
/* 110 */       readLookupListTable(this.tableLocation + header.lookupListOffset);
/* 111 */     } catch (IOException e) {
/* 112 */       throw new FontReadingException("Error reading font file", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void readSubTable(int paramInt1, int paramInt2) throws IOException;
/*     */ 
/*     */   
/*     */   private void readLookupListTable(int lookupListTableLocation) throws IOException {
/* 121 */     this.rf.seek(lookupListTableLocation);
/* 122 */     int lookupCount = this.rf.readShort();
/*     */     
/* 124 */     List<Integer> lookupTableOffsets = new ArrayList<Integer>();
/*     */     int i;
/* 126 */     for (i = 0; i < lookupCount; i++) {
/* 127 */       int lookupTableOffset = this.rf.readShort();
/* 128 */       lookupTableOffsets.add(Integer.valueOf(lookupTableOffset));
/*     */     } 
/*     */ 
/*     */     
/* 132 */     for (i = 0; i < lookupCount; i++) {
/*     */       
/* 134 */       int lookupTableOffset = ((Integer)lookupTableOffsets.get(i)).intValue();
/* 135 */       readLookupTable(lookupListTableLocation + lookupTableOffset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLookupTable(int lookupTableLocation) throws IOException {
/* 141 */     this.rf.seek(lookupTableLocation);
/* 142 */     int lookupType = this.rf.readShort();
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.rf.skipBytes(2);
/*     */     
/* 148 */     int subTableCount = this.rf.readShort();
/*     */ 
/*     */     
/* 151 */     List<Integer> subTableOffsets = new ArrayList<Integer>();
/*     */     
/* 153 */     for (int i = 0; i < subTableCount; i++) {
/* 154 */       int subTableOffset = this.rf.readShort();
/* 155 */       subTableOffsets.add(Integer.valueOf(subTableOffset));
/*     */     } 
/*     */     
/* 158 */     for (Iterator<Integer> iterator = subTableOffsets.iterator(); iterator.hasNext(); ) { int subTableOffset = ((Integer)iterator.next()).intValue();
/*     */       
/* 160 */       readSubTable(lookupType, lookupTableLocation + subTableOffset); }
/*     */   
/*     */   }
/*     */   
/*     */   protected final List<Integer> readCoverageFormat(int coverageLocation) throws IOException {
/*     */     List<Integer> glyphIds;
/* 166 */     this.rf.seek(coverageLocation);
/* 167 */     int coverageFormat = this.rf.readShort();
/*     */ 
/*     */ 
/*     */     
/* 171 */     if (coverageFormat == 1) {
/* 172 */       int glyphCount = this.rf.readShort();
/*     */       
/* 174 */       glyphIds = new ArrayList<Integer>(glyphCount);
/*     */       
/* 176 */       for (int i = 0; i < glyphCount; i++) {
/* 177 */         int coverageGlyphId = this.rf.readShort();
/* 178 */         glyphIds.add(Integer.valueOf(coverageGlyphId));
/*     */       }
/*     */     
/* 181 */     } else if (coverageFormat == 2) {
/*     */       
/* 183 */       int rangeCount = this.rf.readShort();
/*     */       
/* 185 */       glyphIds = new ArrayList<Integer>();
/*     */       
/* 187 */       for (int i = 0; i < rangeCount; i++) {
/* 188 */         readRangeRecord(glyphIds);
/*     */       }
/*     */     } else {
/*     */       
/* 192 */       throw new UnsupportedOperationException("Invalid coverage format: " + coverageFormat);
/*     */     } 
/*     */ 
/*     */     
/* 196 */     return Collections.unmodifiableList(glyphIds);
/*     */   }
/*     */   
/*     */   private void readRangeRecord(List<Integer> glyphIds) throws IOException {
/* 200 */     int startGlyphId = this.rf.readShort();
/* 201 */     int endGlyphId = this.rf.readShort();
/* 202 */     int startCoverageIndex = this.rf.readShort();
/*     */     
/* 204 */     for (int glyphId = startGlyphId; glyphId <= endGlyphId; glyphId++) {
/* 205 */       glyphIds.add(Integer.valueOf(glyphId));
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
/*     */   private void readScriptListTable(int scriptListTableLocationOffset) throws IOException {
/* 218 */     this.rf.seek(scriptListTableLocationOffset);
/*     */     
/* 220 */     int scriptCount = this.rf.readShort();
/*     */     
/* 222 */     Map<String, Integer> scriptRecords = new HashMap<String, Integer>(scriptCount);
/*     */ 
/*     */     
/* 225 */     for (int i = 0; i < scriptCount; i++) {
/* 226 */       readScriptRecord(scriptListTableLocationOffset, scriptRecords);
/*     */     }
/*     */     
/* 229 */     List<String> supportedLanguages = new ArrayList<String>(scriptCount);
/*     */     
/* 231 */     for (String scriptName : scriptRecords.keySet()) {
/* 232 */       readScriptTable(((Integer)scriptRecords.get(scriptName)).intValue());
/* 233 */       supportedLanguages.add(scriptName);
/*     */     } 
/*     */     
/* 236 */     this.supportedLanguages = Collections.unmodifiableList(supportedLanguages);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readScriptRecord(int scriptListTableLocationOffset, Map<String, Integer> scriptRecords) throws IOException {
/* 241 */     String scriptTag = this.rf.readString(4, "utf-8");
/*     */     
/* 243 */     int scriptOffset = this.rf.readShort();
/*     */     
/* 245 */     scriptRecords.put(scriptTag, Integer.valueOf(scriptListTableLocationOffset + scriptOffset));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readScriptTable(int scriptTableLocationOffset) throws IOException {
/* 251 */     this.rf.seek(scriptTableLocationOffset);
/* 252 */     int defaultLangSys = this.rf.readShort();
/* 253 */     int langSysCount = this.rf.readShort();
/*     */     
/* 255 */     if (langSysCount > 0) {
/* 256 */       Map<String, Integer> langSysRecords = new LinkedHashMap<String, Integer>(langSysCount);
/*     */ 
/*     */       
/* 259 */       for (int i = 0; i < langSysCount; i++) {
/* 260 */         readLangSysRecord(langSysRecords);
/*     */       }
/*     */ 
/*     */       
/* 264 */       for (String langSysTag : langSysRecords.keySet()) {
/* 265 */         readLangSysTable(scriptTableLocationOffset + ((Integer)langSysRecords
/* 266 */             .get(langSysTag)).intValue());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 271 */     readLangSysTable(scriptTableLocationOffset + defaultLangSys);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLangSysRecord(Map<String, Integer> langSysRecords) throws IOException {
/* 276 */     String langSysTag = this.rf.readString(4, "utf-8");
/* 277 */     int langSys = this.rf.readShort();
/* 278 */     langSysRecords.put(langSysTag, Integer.valueOf(langSys));
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLangSysTable(int langSysTableLocationOffset) throws IOException {
/* 283 */     this.rf.seek(langSysTableLocationOffset);
/* 284 */     int lookupOrderOffset = this.rf.readShort();
/* 285 */     LOG.debug("lookupOrderOffset=" + lookupOrderOffset);
/* 286 */     int reqFeatureIndex = this.rf.readShort();
/* 287 */     LOG.debug("reqFeatureIndex=" + reqFeatureIndex);
/* 288 */     int featureCount = this.rf.readShort();
/*     */     
/* 290 */     List<Short> featureListIndices = new ArrayList<Short>(featureCount);
/* 291 */     for (int i = 0; i < featureCount; i++) {
/* 292 */       featureListIndices.add(Short.valueOf(this.rf.readShort()));
/*     */     }
/*     */     
/* 295 */     LOG.debug("featureListIndices=" + featureListIndices);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFeatureListTable(int featureListTableLocationOffset) throws IOException {
/* 301 */     this.rf.seek(featureListTableLocationOffset);
/* 302 */     int featureCount = this.rf.readShort();
/* 303 */     LOG.debug("featureCount=" + featureCount);
/*     */     
/* 305 */     Map<String, Short> featureRecords = new LinkedHashMap<String, Short>(featureCount);
/*     */     
/* 307 */     for (int i = 0; i < featureCount; i++) {
/* 308 */       featureRecords.put(this.rf.readString(4, "utf-8"), Short.valueOf(this.rf.readShort()));
/*     */     }
/*     */     
/* 311 */     for (String featureName : featureRecords.keySet()) {
/* 312 */       LOG.debug("*************featureName=" + featureName);
/* 313 */       readFeatureTable(featureListTableLocationOffset + ((Short)featureRecords
/* 314 */           .get(featureName)).shortValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFeatureTable(int featureTableLocationOffset) throws IOException {
/* 321 */     this.rf.seek(featureTableLocationOffset);
/* 322 */     int featureParamsOffset = this.rf.readShort();
/* 323 */     LOG.debug("featureParamsOffset=" + featureParamsOffset);
/*     */     
/* 325 */     int lookupCount = this.rf.readShort();
/* 326 */     LOG.debug("lookupCount=" + lookupCount);
/*     */     
/* 328 */     List<Short> lookupListIndices = new ArrayList<Short>(lookupCount);
/* 329 */     for (int i = 0; i < lookupCount; i++) {
/* 330 */       lookupListIndices.add(Short.valueOf(this.rf.readShort()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TableHeader readHeader() throws IOException {
/* 338 */     this.rf.seek(this.tableLocation);
/*     */     
/* 340 */     int version = this.rf.readInt();
/*     */     
/* 342 */     int scriptListOffset = this.rf.readUnsignedShort();
/* 343 */     int featureListOffset = this.rf.readUnsignedShort();
/* 344 */     int lookupListOffset = this.rf.readUnsignedShort();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 351 */     TableHeader header = new TableHeader(version, scriptListOffset, featureListOffset, lookupListOffset);
/*     */ 
/*     */     
/* 354 */     return header;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/otf/OpenTypeFontTableReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */