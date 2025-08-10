/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TrueTypeFontSubSet
/*     */ {
/*  62 */   static final String[] tableNamesSimple = new String[] { "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep" };
/*     */   
/*  64 */   static final String[] tableNamesCmap = new String[] { "cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "prep" };
/*     */   
/*  66 */   static final String[] tableNamesExtra = new String[] { "OS/2", "cmap", "cvt ", "fpgm", "glyf", "head", "hhea", "hmtx", "loca", "maxp", "name, prep" };
/*     */   
/*  68 */   static final int[] entrySelectors = new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 };
/*     */   
/*     */   static final int TABLE_CHECKSUM = 0;
/*     */   
/*     */   static final int TABLE_OFFSET = 1;
/*     */   
/*     */   static final int TABLE_LENGTH = 2;
/*     */   
/*     */   static final int HEAD_LOCA_FORMAT_OFFSET = 51;
/*     */   
/*     */   static final int ARG_1_AND_2_ARE_WORDS = 1;
/*     */   
/*     */   static final int WE_HAVE_A_SCALE = 8;
/*     */   
/*     */   static final int MORE_COMPONENTS = 32;
/*     */   
/*     */   static final int WE_HAVE_AN_X_AND_Y_SCALE = 64;
/*     */   
/*     */   static final int WE_HAVE_A_TWO_BY_TWO = 128;
/*     */   
/*     */   protected HashMap<String, int[]> tableDirectory;
/*     */   
/*     */   protected RandomAccessFileOrArray rf;
/*     */   
/*     */   protected String fileName;
/*     */   
/*     */   protected boolean includeCmap;
/*     */   
/*     */   protected boolean includeExtras;
/*     */   
/*     */   protected boolean locaShortTable;
/*     */   
/*     */   protected int[] locaTable;
/*     */   
/*     */   protected HashSet<Integer> glyphsUsed;
/*     */   
/*     */   protected ArrayList<Integer> glyphsInList;
/*     */   protected int tableGlyphOffset;
/*     */   protected int[] newLocaTable;
/*     */   protected byte[] newLocaTableOut;
/*     */   protected byte[] newGlyfTable;
/*     */   protected int glyfTableRealSize;
/*     */   protected int locaTableRealSize;
/*     */   protected byte[] outFont;
/*     */   protected int fontPtr;
/*     */   protected int directoryOffset;
/*     */   
/*     */   TrueTypeFontSubSet(String fileName, RandomAccessFileOrArray rf, HashSet<Integer> glyphsUsed, int directoryOffset, boolean includeCmap, boolean includeExtras) {
/* 116 */     this.fileName = fileName;
/* 117 */     this.rf = rf;
/* 118 */     this.glyphsUsed = glyphsUsed;
/* 119 */     this.includeCmap = includeCmap;
/* 120 */     this.includeExtras = includeExtras;
/* 121 */     this.directoryOffset = directoryOffset;
/* 122 */     this.glyphsInList = new ArrayList<Integer>(glyphsUsed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] process() throws IOException, DocumentException {
/*     */     try {
/* 132 */       this.rf.reOpen();
/* 133 */       createTableDirectory();
/* 134 */       readLoca();
/* 135 */       flatGlyphs();
/* 136 */       createNewGlyphTables();
/* 137 */       locaTobytes();
/* 138 */       assembleFont();
/* 139 */       return this.outFont;
/*     */     } finally {
/*     */       
/*     */       try {
/* 143 */         this.rf.close();
/*     */       }
/* 145 */       catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assembleFont() throws IOException {
/*     */     String[] tableNames;
/* 153 */     int fullFontSize = 0;
/*     */     
/* 155 */     if (this.includeExtras) {
/* 156 */       tableNames = tableNamesExtra;
/*     */     }
/* 158 */     else if (this.includeCmap) {
/* 159 */       tableNames = tableNamesCmap;
/*     */     } else {
/* 161 */       tableNames = tableNamesSimple;
/*     */     } 
/* 163 */     int tablesUsed = 2;
/* 164 */     int len = 0;
/* 165 */     for (int k = 0; k < tableNames.length; k++) {
/* 166 */       String name = tableNames[k];
/* 167 */       if (!name.equals("glyf") && !name.equals("loca")) {
/*     */         
/* 169 */         int[] tableLocation = this.tableDirectory.get(name);
/* 170 */         if (tableLocation != null)
/*     */         
/* 172 */         { tablesUsed++;
/* 173 */           fullFontSize += tableLocation[2] + 3 & 0xFFFFFFFC; } 
/*     */       } 
/* 175 */     }  fullFontSize += this.newLocaTableOut.length;
/* 176 */     fullFontSize += this.newGlyfTable.length;
/* 177 */     int ref = 16 * tablesUsed + 12;
/* 178 */     fullFontSize += ref;
/* 179 */     this.outFont = new byte[fullFontSize];
/* 180 */     this.fontPtr = 0;
/* 181 */     writeFontInt(65536);
/* 182 */     writeFontShort(tablesUsed);
/* 183 */     int selector = entrySelectors[tablesUsed];
/* 184 */     writeFontShort((1 << selector) * 16);
/* 185 */     writeFontShort(selector);
/* 186 */     writeFontShort((tablesUsed - (1 << selector)) * 16); int i;
/* 187 */     for (i = 0; i < tableNames.length; i++) {
/* 188 */       String name = tableNames[i];
/* 189 */       int[] tableLocation = this.tableDirectory.get(name);
/* 190 */       if (tableLocation != null) {
/*     */         
/* 192 */         writeFontString(name);
/* 193 */         if (name.equals("glyf")) {
/* 194 */           writeFontInt(calculateChecksum(this.newGlyfTable));
/* 195 */           len = this.glyfTableRealSize;
/*     */         }
/* 197 */         else if (name.equals("loca")) {
/* 198 */           writeFontInt(calculateChecksum(this.newLocaTableOut));
/* 199 */           len = this.locaTableRealSize;
/*     */         } else {
/*     */           
/* 202 */           writeFontInt(tableLocation[0]);
/* 203 */           len = tableLocation[2];
/*     */         } 
/* 205 */         writeFontInt(ref);
/* 206 */         writeFontInt(len);
/* 207 */         ref += len + 3 & 0xFFFFFFFC;
/*     */       } 
/* 209 */     }  for (i = 0; i < tableNames.length; i++) {
/* 210 */       String name = tableNames[i];
/* 211 */       int[] tableLocation = this.tableDirectory.get(name);
/* 212 */       if (tableLocation != null)
/*     */       {
/* 214 */         if (name.equals("glyf")) {
/* 215 */           System.arraycopy(this.newGlyfTable, 0, this.outFont, this.fontPtr, this.newGlyfTable.length);
/* 216 */           this.fontPtr += this.newGlyfTable.length;
/* 217 */           this.newGlyfTable = null;
/*     */         }
/* 219 */         else if (name.equals("loca")) {
/* 220 */           System.arraycopy(this.newLocaTableOut, 0, this.outFont, this.fontPtr, this.newLocaTableOut.length);
/* 221 */           this.fontPtr += this.newLocaTableOut.length;
/* 222 */           this.newLocaTableOut = null;
/*     */         } else {
/*     */           
/* 225 */           this.rf.seek(tableLocation[1]);
/* 226 */           this.rf.readFully(this.outFont, this.fontPtr, tableLocation[2]);
/* 227 */           this.fontPtr += tableLocation[2] + 3 & 0xFFFFFFFC;
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createTableDirectory() throws IOException, DocumentException {
/* 233 */     this.tableDirectory = (HashMap)new HashMap<String, int>();
/* 234 */     this.rf.seek(this.directoryOffset);
/* 235 */     int id = this.rf.readInt();
/* 236 */     if (id != 65536)
/* 237 */       throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.true.type.file", new Object[] { this.fileName })); 
/* 238 */     int num_tables = this.rf.readUnsignedShort();
/* 239 */     this.rf.skipBytes(6);
/* 240 */     for (int k = 0; k < num_tables; k++) {
/* 241 */       String tag = readStandardString(4);
/* 242 */       int[] tableLocation = new int[3];
/* 243 */       tableLocation[0] = this.rf.readInt();
/* 244 */       tableLocation[1] = this.rf.readInt();
/* 245 */       tableLocation[2] = this.rf.readInt();
/* 246 */       this.tableDirectory.put(tag, tableLocation);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readLoca() throws IOException, DocumentException {
/* 252 */     int[] tableLocation = this.tableDirectory.get("head");
/* 253 */     if (tableLocation == null)
/* 254 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "head", this.fileName })); 
/* 255 */     this.rf.seek((tableLocation[1] + 51));
/* 256 */     this.locaShortTable = (this.rf.readUnsignedShort() == 0);
/* 257 */     tableLocation = this.tableDirectory.get("loca");
/* 258 */     if (tableLocation == null)
/* 259 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "loca", this.fileName })); 
/* 260 */     this.rf.seek(tableLocation[1]);
/* 261 */     if (this.locaShortTable) {
/* 262 */       int entries = tableLocation[2] / 2;
/* 263 */       this.locaTable = new int[entries];
/* 264 */       for (int k = 0; k < entries; k++) {
/* 265 */         this.locaTable[k] = this.rf.readUnsignedShort() * 2;
/*     */       }
/*     */     } else {
/* 268 */       int entries = tableLocation[2] / 4;
/* 269 */       this.locaTable = new int[entries];
/* 270 */       for (int k = 0; k < entries; k++)
/* 271 */         this.locaTable[k] = this.rf.readInt(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void createNewGlyphTables() throws IOException {
/* 276 */     this.newLocaTable = new int[this.locaTable.length];
/* 277 */     int[] activeGlyphs = new int[this.glyphsInList.size()];
/* 278 */     for (int k = 0; k < activeGlyphs.length; k++)
/* 279 */       activeGlyphs[k] = ((Integer)this.glyphsInList.get(k)).intValue(); 
/* 280 */     Arrays.sort(activeGlyphs);
/* 281 */     int glyfSize = 0;
/* 282 */     for (int i = 0; i < activeGlyphs.length; i++) {
/* 283 */       int glyph = activeGlyphs[i];
/* 284 */       glyfSize += this.locaTable[glyph + 1] - this.locaTable[glyph];
/*     */     } 
/* 286 */     this.glyfTableRealSize = glyfSize;
/* 287 */     glyfSize = glyfSize + 3 & 0xFFFFFFFC;
/* 288 */     this.newGlyfTable = new byte[glyfSize];
/* 289 */     int glyfPtr = 0;
/* 290 */     int listGlyf = 0;
/* 291 */     for (int j = 0; j < this.newLocaTable.length; j++) {
/* 292 */       this.newLocaTable[j] = glyfPtr;
/* 293 */       if (listGlyf < activeGlyphs.length && activeGlyphs[listGlyf] == j) {
/* 294 */         listGlyf++;
/* 295 */         this.newLocaTable[j] = glyfPtr;
/* 296 */         int start = this.locaTable[j];
/* 297 */         int len = this.locaTable[j + 1] - start;
/* 298 */         if (len > 0) {
/* 299 */           this.rf.seek((this.tableGlyphOffset + start));
/* 300 */           this.rf.readFully(this.newGlyfTable, glyfPtr, len);
/* 301 */           glyfPtr += len;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void locaTobytes() {
/* 308 */     if (this.locaShortTable) {
/* 309 */       this.locaTableRealSize = this.newLocaTable.length * 2;
/*     */     } else {
/* 311 */       this.locaTableRealSize = this.newLocaTable.length * 4;
/* 312 */     }  this.newLocaTableOut = new byte[this.locaTableRealSize + 3 & 0xFFFFFFFC];
/* 313 */     this.outFont = this.newLocaTableOut;
/* 314 */     this.fontPtr = 0;
/* 315 */     for (int k = 0; k < this.newLocaTable.length; k++) {
/* 316 */       if (this.locaShortTable) {
/* 317 */         writeFontShort(this.newLocaTable[k] / 2);
/*     */       } else {
/* 319 */         writeFontInt(this.newLocaTable[k]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flatGlyphs() throws IOException, DocumentException {
/* 326 */     int[] tableLocation = this.tableDirectory.get("glyf");
/* 327 */     if (tableLocation == null)
/* 328 */       throw new DocumentException(MessageLocalization.getComposedMessage("table.1.does.not.exist.in.2", new Object[] { "glyf", this.fileName })); 
/* 329 */     Integer glyph0 = Integer.valueOf(0);
/* 330 */     if (!this.glyphsUsed.contains(glyph0)) {
/* 331 */       this.glyphsUsed.add(glyph0);
/* 332 */       this.glyphsInList.add(glyph0);
/*     */     } 
/* 334 */     this.tableGlyphOffset = tableLocation[1];
/* 335 */     for (int k = 0; k < this.glyphsInList.size(); k++) {
/* 336 */       int glyph = ((Integer)this.glyphsInList.get(k)).intValue();
/* 337 */       checkGlyphComposite(glyph);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void checkGlyphComposite(int glyph) throws IOException {
/* 342 */     int start = this.locaTable[glyph];
/* 343 */     if (start == this.locaTable[glyph + 1])
/*     */       return; 
/* 345 */     this.rf.seek((this.tableGlyphOffset + start));
/* 346 */     int numContours = this.rf.readShort();
/* 347 */     if (numContours >= 0)
/*     */       return; 
/* 349 */     this.rf.skipBytes(8);
/*     */     while (true) {
/* 351 */       int skip, flags = this.rf.readUnsignedShort();
/* 352 */       Integer cGlyph = Integer.valueOf(this.rf.readUnsignedShort());
/* 353 */       if (!this.glyphsUsed.contains(cGlyph)) {
/* 354 */         this.glyphsUsed.add(cGlyph);
/* 355 */         this.glyphsInList.add(cGlyph);
/*     */       } 
/* 357 */       if ((flags & 0x20) == 0) {
/*     */         return;
/*     */       }
/* 360 */       if ((flags & 0x1) != 0) {
/* 361 */         skip = 4;
/*     */       } else {
/* 363 */         skip = 2;
/* 364 */       }  if ((flags & 0x8) != 0) {
/* 365 */         skip += 2;
/* 366 */       } else if ((flags & 0x40) != 0) {
/* 367 */         skip += 4;
/* 368 */       }  if ((flags & 0x80) != 0)
/* 369 */         skip += 8; 
/* 370 */       this.rf.skipBytes(skip);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String readStandardString(int length) throws IOException {
/* 381 */     byte[] buf = new byte[length];
/* 382 */     this.rf.readFully(buf);
/*     */     try {
/* 384 */       return new String(buf, "Cp1252");
/*     */     }
/* 386 */     catch (Exception e) {
/* 387 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void writeFontShort(int n) {
/* 392 */     this.outFont[this.fontPtr++] = (byte)(n >> 8);
/* 393 */     this.outFont[this.fontPtr++] = (byte)n;
/*     */   }
/*     */   
/*     */   protected void writeFontInt(int n) {
/* 397 */     this.outFont[this.fontPtr++] = (byte)(n >> 24);
/* 398 */     this.outFont[this.fontPtr++] = (byte)(n >> 16);
/* 399 */     this.outFont[this.fontPtr++] = (byte)(n >> 8);
/* 400 */     this.outFont[this.fontPtr++] = (byte)n;
/*     */   }
/*     */   
/*     */   protected void writeFontString(String s) {
/* 404 */     byte[] b = PdfEncodings.convertToBytes(s, "Cp1252");
/* 405 */     System.arraycopy(b, 0, this.outFont, this.fontPtr, b.length);
/* 406 */     this.fontPtr += b.length;
/*     */   }
/*     */   
/*     */   protected int calculateChecksum(byte[] b) {
/* 410 */     int len = b.length / 4;
/* 411 */     int v0 = 0;
/* 412 */     int v1 = 0;
/* 413 */     int v2 = 0;
/* 414 */     int v3 = 0;
/* 415 */     int ptr = 0;
/* 416 */     for (int k = 0; k < len; k++) {
/* 417 */       v3 += b[ptr++] & 0xFF;
/* 418 */       v2 += b[ptr++] & 0xFF;
/* 419 */       v1 += b[ptr++] & 0xFF;
/* 420 */       v0 += b[ptr++] & 0xFF;
/*     */     } 
/* 422 */     return v0 + (v1 << 8) + (v2 << 16) + (v3 << 24);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/TrueTypeFontSubSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */