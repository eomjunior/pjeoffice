/*     */ package com.itextpdf.text.pdf.fonts.otf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.Glyph;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlyphSubstitutionTableReader
/*     */   extends OpenTypeFontTableReader
/*     */ {
/*     */   private final int[] glyphWidthsByIndex;
/*     */   private final Map<Integer, Character> glyphToCharacterMap;
/*     */   private Map<Integer, List<Integer>> rawLigatureSubstitutionMap;
/*     */   
/*     */   public GlyphSubstitutionTableReader(RandomAccessFileOrArray rf, int gsubTableLocation, Map<Integer, Character> glyphToCharacterMap, int[] glyphWidthsByIndex) throws IOException {
/*  76 */     super(rf, gsubTableLocation);
/*  77 */     this.glyphWidthsByIndex = glyphWidthsByIndex;
/*  78 */     this.glyphToCharacterMap = glyphToCharacterMap;
/*     */   }
/*     */   
/*     */   public void read() throws FontReadingException {
/*  82 */     this.rawLigatureSubstitutionMap = new LinkedHashMap<Integer, List<Integer>>();
/*  83 */     startReadingTable();
/*     */   }
/*     */   
/*     */   public Map<String, Glyph> getGlyphSubstitutionMap() throws FontReadingException {
/*  87 */     Map<String, Glyph> glyphSubstitutionMap = new LinkedHashMap<String, Glyph>();
/*     */     
/*  89 */     for (Integer glyphIdToReplace : this.rawLigatureSubstitutionMap.keySet()) {
/*  90 */       List<Integer> constituentGlyphs = this.rawLigatureSubstitutionMap.get(glyphIdToReplace);
/*  91 */       StringBuilder chars = new StringBuilder(constituentGlyphs.size());
/*     */       
/*  93 */       for (Integer constituentGlyphId : constituentGlyphs) {
/*  94 */         chars.append(getTextFromGlyph(constituentGlyphId.intValue(), this.glyphToCharacterMap));
/*     */       }
/*     */       
/*  97 */       Glyph glyph = new Glyph(glyphIdToReplace.intValue(), this.glyphWidthsByIndex[glyphIdToReplace.intValue()], chars.toString());
/*     */       
/*  99 */       glyphSubstitutionMap.put(glyph.chars, glyph);
/*     */     } 
/*     */     
/* 102 */     return Collections.unmodifiableMap(glyphSubstitutionMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getTextFromGlyph(int glyphId, Map<Integer, Character> glyphToCharacterMap) throws FontReadingException {
/* 107 */     StringBuilder chars = new StringBuilder(1);
/*     */     
/* 109 */     Character c = glyphToCharacterMap.get(Integer.valueOf(glyphId));
/*     */     
/* 111 */     if (c == null) {
/*     */       
/* 113 */       List<Integer> constituentGlyphs = this.rawLigatureSubstitutionMap.get(Integer.valueOf(glyphId));
/*     */       
/* 115 */       if (constituentGlyphs == null || constituentGlyphs.isEmpty()) {
/* 116 */         throw new FontReadingException("No corresponding character or simple glyphs found for GlyphID=" + glyphId);
/*     */       }
/*     */       
/* 119 */       for (Iterator<Integer> iterator = constituentGlyphs.iterator(); iterator.hasNext(); ) { int constituentGlyphId = ((Integer)iterator.next()).intValue();
/* 120 */         chars.append(getTextFromGlyph(constituentGlyphId, glyphToCharacterMap)); }
/*     */     
/*     */     } else {
/*     */       
/* 124 */       chars.append(c.charValue());
/*     */     } 
/*     */     
/* 127 */     return chars.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
/* 133 */     if (lookupType == 1) {
/* 134 */       readSingleSubstitutionSubtable(subTableLocation);
/* 135 */     } else if (lookupType == 4) {
/* 136 */       readLigatureSubstitutionSubtable(subTableLocation);
/*     */     } else {
/* 138 */       System.err.println("LookupType " + lookupType + " is not yet handled for " + GlyphSubstitutionTableReader.class.getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readSingleSubstitutionSubtable(int subTableLocation) throws IOException {
/* 147 */     this.rf.seek(subTableLocation);
/*     */     
/* 149 */     int substFormat = this.rf.readShort();
/* 150 */     LOG.debug("substFormat=" + substFormat);
/*     */     
/* 152 */     if (substFormat == 1) {
/* 153 */       int coverage = this.rf.readShort();
/* 154 */       LOG.debug("coverage=" + coverage);
/*     */       
/* 156 */       int deltaGlyphID = this.rf.readShort();
/* 157 */       LOG.debug("deltaGlyphID=" + deltaGlyphID);
/*     */       
/* 159 */       List<Integer> coverageGlyphIds = readCoverageFormat(subTableLocation + coverage);
/*     */       
/* 161 */       for (Iterator<Integer> iterator = coverageGlyphIds.iterator(); iterator.hasNext(); ) { int coverageGlyphId = ((Integer)iterator.next()).intValue();
/* 162 */         int substituteGlyphId = coverageGlyphId + deltaGlyphID;
/* 163 */         this.rawLigatureSubstitutionMap.put(Integer.valueOf(substituteGlyphId), Arrays.asList(new Integer[] { Integer.valueOf(coverageGlyphId) })); }
/*     */     
/* 165 */     } else if (substFormat == 2) {
/* 166 */       int coverage = this.rf.readShort();
/* 167 */       LOG.debug("coverage=" + coverage);
/* 168 */       int glyphCount = this.rf.readUnsignedShort();
/* 169 */       int[] substitute = new int[glyphCount];
/* 170 */       for (int k = 0; k < glyphCount; k++) {
/* 171 */         substitute[k] = this.rf.readUnsignedShort();
/*     */       }
/* 173 */       List<Integer> coverageGlyphIds = readCoverageFormat(subTableLocation + coverage);
/* 174 */       for (int i = 0; i < glyphCount; i++) {
/* 175 */         this.rawLigatureSubstitutionMap.put(Integer.valueOf(substitute[i]), Arrays.asList(new Integer[] { coverageGlyphIds.get(i) }));
/*     */       } 
/*     */     } else {
/*     */       
/* 179 */       throw new IllegalArgumentException("Bad substFormat: " + substFormat);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readLigatureSubstitutionSubtable(int ligatureSubstitutionSubtableLocation) throws IOException {
/* 187 */     this.rf.seek(ligatureSubstitutionSubtableLocation);
/* 188 */     int substFormat = this.rf.readShort();
/* 189 */     LOG.debug("substFormat=" + substFormat);
/*     */     
/* 191 */     if (substFormat != 1) {
/* 192 */       throw new IllegalArgumentException("The expected SubstFormat is 1");
/*     */     }
/*     */     
/* 195 */     int coverage = this.rf.readShort();
/* 196 */     LOG.debug("coverage=" + coverage);
/*     */     
/* 198 */     int ligSetCount = this.rf.readShort();
/*     */     
/* 200 */     List<Integer> ligatureOffsets = new ArrayList<Integer>(ligSetCount);
/*     */     
/* 202 */     for (int i = 0; i < ligSetCount; i++) {
/* 203 */       int ligatureOffset = this.rf.readShort();
/* 204 */       ligatureOffsets.add(Integer.valueOf(ligatureOffset));
/*     */     } 
/*     */     
/* 207 */     List<Integer> coverageGlyphIds = readCoverageFormat(ligatureSubstitutionSubtableLocation + coverage);
/*     */     
/* 209 */     if (ligSetCount != coverageGlyphIds.size()) {
/* 210 */       throw new IllegalArgumentException("According to the OpenTypeFont specifications, the coverage count should be equal to the no. of LigatureSetTables");
/*     */     }
/*     */     
/* 213 */     for (int j = 0; j < ligSetCount; j++) {
/*     */       
/* 215 */       int coverageGlyphId = ((Integer)coverageGlyphIds.get(j)).intValue();
/* 216 */       int ligatureOffset = ((Integer)ligatureOffsets.get(j)).intValue();
/* 217 */       LOG.debug("ligatureOffset=" + ligatureOffset);
/* 218 */       readLigatureSetTable(ligatureSubstitutionSubtableLocation + ligatureOffset, coverageGlyphId);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLigatureSetTable(int ligatureSetTableLocation, int coverageGlyphId) throws IOException {
/* 224 */     this.rf.seek(ligatureSetTableLocation);
/* 225 */     int ligatureCount = this.rf.readShort();
/* 226 */     LOG.debug("ligatureCount=" + ligatureCount);
/*     */     
/* 228 */     List<Integer> ligatureOffsets = new ArrayList<Integer>(ligatureCount);
/*     */     
/* 230 */     for (int i = 0; i < ligatureCount; i++) {
/* 231 */       int ligatureOffset = this.rf.readShort();
/* 232 */       ligatureOffsets.add(Integer.valueOf(ligatureOffset));
/*     */     } 
/*     */     
/* 235 */     for (Iterator<Integer> iterator = ligatureOffsets.iterator(); iterator.hasNext(); ) { int ligatureOffset = ((Integer)iterator.next()).intValue();
/* 236 */       readLigatureTable(ligatureSetTableLocation + ligatureOffset, coverageGlyphId); }
/*     */   
/*     */   }
/*     */   
/*     */   private void readLigatureTable(int ligatureTableLocation, int coverageGlyphId) throws IOException {
/* 241 */     this.rf.seek(ligatureTableLocation);
/* 242 */     int ligGlyph = this.rf.readShort();
/* 243 */     LOG.debug("ligGlyph=" + ligGlyph);
/*     */     
/* 245 */     int compCount = this.rf.readShort();
/*     */     
/* 247 */     List<Integer> glyphIdList = new ArrayList<Integer>();
/*     */     
/* 249 */     glyphIdList.add(Integer.valueOf(coverageGlyphId));
/*     */     
/* 251 */     for (int i = 0; i < compCount - 1; i++) {
/* 252 */       int glyphId = this.rf.readShort();
/* 253 */       glyphIdList.add(Integer.valueOf(glyphId));
/*     */     } 
/*     */     
/* 256 */     LOG.debug("glyphIdList=" + glyphIdList);
/*     */     
/* 258 */     List<Integer> previousValue = this.rawLigatureSubstitutionMap.put(Integer.valueOf(ligGlyph), glyphIdList);
/*     */     
/* 260 */     if (previousValue != null)
/* 261 */       LOG.warn("!!!!!!!!!!glyphId=" + ligGlyph + ",\npreviousValue=" + previousValue + ",\ncurrentVal=" + glyphIdList); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/otf/GlyphSubstitutionTableReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */