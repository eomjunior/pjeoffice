/*     */ package com.itextpdf.text.pdf.fonts.otf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlyphPositioningTableReader
/*     */   extends OpenTypeFontTableReader
/*     */ {
/*     */   public GlyphPositioningTableReader(RandomAccessFileOrArray rf, int gposTableLocation) throws IOException {
/*  61 */     super(rf, gposTableLocation);
/*     */   }
/*     */   
/*     */   public void read() throws FontReadingException {
/*  65 */     startReadingTable();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
/*  71 */     if (lookupType == 1) {
/*  72 */       readLookUpType_1(subTableLocation);
/*  73 */     } else if (lookupType == 4) {
/*  74 */       readLookUpType_4(subTableLocation);
/*  75 */     } else if (lookupType == 8) {
/*  76 */       readLookUpType_8(subTableLocation);
/*     */     } else {
/*  78 */       System.err.println("The lookupType " + lookupType + " is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLookUpType_1(int lookupTableLocation) throws IOException {
/*  84 */     this.rf.seek(lookupTableLocation);
/*  85 */     int posFormat = this.rf.readShort();
/*     */     
/*  87 */     if (posFormat == 1) {
/*  88 */       LOG.debug("Reading `Look Up Type 1, Format 1` ....");
/*  89 */       int coverageOffset = this.rf.readShort();
/*  90 */       int valueFormat = this.rf.readShort();
/*     */ 
/*     */ 
/*     */       
/*  94 */       if ((valueFormat & 0x1) == 1) {
/*  95 */         int xPlacement = this.rf.readShort();
/*  96 */         LOG.debug("xPlacement=" + xPlacement);
/*     */       } 
/*     */ 
/*     */       
/* 100 */       if ((valueFormat & 0x2) == 2) {
/* 101 */         int yPlacement = this.rf.readShort();
/* 102 */         LOG.debug("yPlacement=" + yPlacement);
/*     */       } 
/*     */       
/* 105 */       List<Integer> glyphCodes = readCoverageFormat(lookupTableLocation + coverageOffset);
/*     */       
/* 107 */       LOG.debug("glyphCodes=" + glyphCodes);
/*     */     } else {
/* 109 */       System.err.println("The PosFormat " + posFormat + " for `LookupType 1` is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readLookUpType_4(int lookupTableLocation) throws IOException {
/* 115 */     this.rf.seek(lookupTableLocation);
/*     */     
/* 117 */     int posFormat = this.rf.readShort();
/*     */     
/* 119 */     if (posFormat == 1) {
/*     */       
/* 121 */       LOG.debug("Reading `Look Up Type 4, Format 1` ....");
/*     */       
/* 123 */       int markCoverageOffset = this.rf.readShort();
/* 124 */       int baseCoverageOffset = this.rf.readShort();
/* 125 */       int classCount = this.rf.readShort();
/* 126 */       int markArrayOffset = this.rf.readShort();
/* 127 */       int baseArrayOffset = this.rf.readShort();
/*     */       
/* 129 */       List<Integer> markCoverages = readCoverageFormat(lookupTableLocation + markCoverageOffset);
/* 130 */       LOG.debug("markCoverages=" + markCoverages);
/*     */       
/* 132 */       List<Integer> baseCoverages = readCoverageFormat(lookupTableLocation + baseCoverageOffset);
/* 133 */       LOG.debug("baseCoverages=" + baseCoverages);
/*     */       
/* 135 */       readMarkArrayTable(lookupTableLocation + markArrayOffset);
/*     */       
/* 137 */       readBaseArrayTable(lookupTableLocation + baseArrayOffset, classCount);
/*     */     } else {
/* 139 */       System.err.println("The posFormat " + posFormat + " is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readLookUpType_8(int lookupTableLocation) throws IOException {
/* 144 */     this.rf.seek(lookupTableLocation);
/*     */     
/* 146 */     int posFormat = this.rf.readShort();
/*     */     
/* 148 */     if (posFormat == 3) {
/* 149 */       LOG.debug("Reading `Look Up Type 8, Format 3` ....");
/* 150 */       readChainingContextPositioningFormat_3(lookupTableLocation);
/*     */     } else {
/* 152 */       System.err.println("The posFormat " + posFormat + " for `Look Up Type 8` is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readChainingContextPositioningFormat_3(int lookupTableLocation) throws IOException {
/* 157 */     int backtrackGlyphCount = this.rf.readShort();
/* 158 */     LOG.debug("backtrackGlyphCount=" + backtrackGlyphCount);
/* 159 */     List<Integer> backtrackGlyphOffsets = new ArrayList<Integer>(backtrackGlyphCount);
/*     */     
/* 161 */     for (int i = 0; i < backtrackGlyphCount; i++) {
/* 162 */       int backtrackGlyphOffset = this.rf.readShort();
/* 163 */       backtrackGlyphOffsets.add(Integer.valueOf(backtrackGlyphOffset));
/*     */     } 
/*     */     
/* 166 */     int inputGlyphCount = this.rf.readShort();
/* 167 */     LOG.debug("inputGlyphCount=" + inputGlyphCount);
/* 168 */     List<Integer> inputGlyphOffsets = new ArrayList<Integer>(inputGlyphCount);
/*     */     
/* 170 */     for (int j = 0; j < inputGlyphCount; j++) {
/* 171 */       int inputGlyphOffset = this.rf.readShort();
/* 172 */       inputGlyphOffsets.add(Integer.valueOf(inputGlyphOffset));
/*     */     } 
/*     */     
/* 175 */     int lookaheadGlyphCount = this.rf.readShort();
/* 176 */     LOG.debug("lookaheadGlyphCount=" + lookaheadGlyphCount);
/* 177 */     List<Integer> lookaheadGlyphOffsets = new ArrayList<Integer>(lookaheadGlyphCount);
/*     */     
/* 179 */     for (int k = 0; k < lookaheadGlyphCount; k++) {
/* 180 */       int lookaheadGlyphOffset = this.rf.readShort();
/* 181 */       lookaheadGlyphOffsets.add(Integer.valueOf(lookaheadGlyphOffset));
/*     */     } 
/*     */     
/* 184 */     int posCount = this.rf.readShort();
/* 185 */     LOG.debug("posCount=" + posCount);
/*     */     
/* 187 */     List<PosLookupRecord> posLookupRecords = new ArrayList<PosLookupRecord>(posCount);
/*     */     
/* 189 */     for (int m = 0; m < posCount; m++) {
/* 190 */       int sequenceIndex = this.rf.readShort();
/* 191 */       int lookupListIndex = this.rf.readShort();
/* 192 */       LOG.debug("sequenceIndex=" + sequenceIndex + ", lookupListIndex=" + lookupListIndex);
/* 193 */       posLookupRecords.add(new PosLookupRecord(sequenceIndex, lookupListIndex));
/*     */     } 
/*     */     Iterator<Integer> iterator;
/* 196 */     for (iterator = backtrackGlyphOffsets.iterator(); iterator.hasNext(); ) { int backtrackGlyphOffset = ((Integer)iterator.next()).intValue();
/* 197 */       List<Integer> backtrackGlyphs = readCoverageFormat(lookupTableLocation + backtrackGlyphOffset);
/* 198 */       LOG.debug("backtrackGlyphs=" + backtrackGlyphs); }
/*     */ 
/*     */     
/* 201 */     for (iterator = inputGlyphOffsets.iterator(); iterator.hasNext(); ) { int inputGlyphOffset = ((Integer)iterator.next()).intValue();
/* 202 */       List<Integer> inputGlyphs = readCoverageFormat(lookupTableLocation + inputGlyphOffset);
/* 203 */       LOG.debug("inputGlyphs=" + inputGlyphs); }
/*     */ 
/*     */     
/* 206 */     for (iterator = lookaheadGlyphOffsets.iterator(); iterator.hasNext(); ) { int lookaheadGlyphOffset = ((Integer)iterator.next()).intValue();
/* 207 */       List<Integer> lookaheadGlyphs = readCoverageFormat(lookupTableLocation + lookaheadGlyphOffset);
/* 208 */       LOG.debug("lookaheadGlyphs=" + lookaheadGlyphs); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void readMarkArrayTable(int markArrayLocation) throws IOException {
/* 214 */     this.rf.seek(markArrayLocation);
/* 215 */     int markCount = this.rf.readShort();
/* 216 */     List<MarkRecord> markRecords = new ArrayList<MarkRecord>();
/*     */     
/* 218 */     for (int i = 0; i < markCount; i++) {
/* 219 */       markRecords.add(readMarkRecord());
/*     */     }
/*     */     
/* 222 */     for (MarkRecord markRecord : markRecords) {
/* 223 */       readAnchorTable(markArrayLocation + markRecord.markAnchorOffset);
/*     */     }
/*     */   }
/*     */   
/*     */   private MarkRecord readMarkRecord() throws IOException {
/* 228 */     int markClass = this.rf.readShort();
/* 229 */     int markAnchorOffset = this.rf.readShort();
/* 230 */     return new MarkRecord(markClass, markAnchorOffset);
/*     */   }
/*     */   
/*     */   private void readAnchorTable(int anchorTableLocation) throws IOException {
/* 234 */     this.rf.seek(anchorTableLocation);
/* 235 */     int anchorFormat = this.rf.readShort();
/*     */     
/* 237 */     if (anchorFormat != 1) {
/* 238 */       System.err.println("The extra features of the AnchorFormat " + anchorFormat + " will not be used");
/*     */     }
/*     */     
/* 241 */     int x = this.rf.readShort();
/* 242 */     int y = this.rf.readShort();
/*     */   }
/*     */ 
/*     */   
/*     */   private void readBaseArrayTable(int baseArrayTableLocation, int classCount) throws IOException {
/* 247 */     this.rf.seek(baseArrayTableLocation);
/* 248 */     int baseCount = this.rf.readShort();
/* 249 */     Set<Integer> baseAnchors = new HashSet<Integer>();
/*     */     
/* 251 */     for (int i = 0; i < baseCount; i++) {
/*     */       
/* 253 */       for (int k = 0; k < classCount; k++) {
/* 254 */         int baseAnchor = this.rf.readShort();
/* 255 */         baseAnchors.add(Integer.valueOf(baseAnchor));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 261 */     for (Iterator<Integer> iterator = baseAnchors.iterator(); iterator.hasNext(); ) { int baseAnchor = ((Integer)iterator.next()).intValue();
/* 262 */       readAnchorTable(baseArrayTableLocation + baseAnchor); }
/*     */   
/*     */   }
/*     */   
/*     */   static class MarkRecord
/*     */   {
/*     */     final int markClass;
/*     */     final int markAnchorOffset;
/*     */     
/*     */     public MarkRecord(int markClass, int markAnchorOffset) {
/* 272 */       this.markClass = markClass;
/* 273 */       this.markAnchorOffset = markAnchorOffset;
/*     */     }
/*     */   }
/*     */   
/*     */   static class PosLookupRecord
/*     */   {
/*     */     final int sequenceIndex;
/*     */     final int lookupListIndex;
/*     */     
/*     */     public PosLookupRecord(int sequenceIndex, int lookupListIndex) {
/* 283 */       this.sequenceIndex = sequenceIndex;
/* 284 */       this.lookupListIndex = lookupListIndex;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/otf/GlyphPositioningTableReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */