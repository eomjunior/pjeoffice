/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EnumerateTTC
/*     */   extends TrueTypeFont
/*     */ {
/*     */   protected String[] names;
/*     */   
/*     */   EnumerateTTC(String ttcFile) throws DocumentException, IOException {
/*  60 */     this.fileName = ttcFile;
/*  61 */     this.rf = new RandomAccessFileOrArray(ttcFile);
/*  62 */     findNames();
/*     */   }
/*     */   
/*     */   EnumerateTTC(byte[] ttcArray) throws DocumentException, IOException {
/*  66 */     this.fileName = "Byte array TTC";
/*  67 */     this.rf = new RandomAccessFileOrArray(ttcArray);
/*  68 */     findNames();
/*     */   }
/*     */   
/*     */   void findNames() throws DocumentException, IOException {
/*  72 */     this.tables = (HashMap)new HashMap<String, int>();
/*     */     
/*     */     try {
/*  75 */       String mainTag = readStandardString(4);
/*  76 */       if (!mainTag.equals("ttcf"))
/*  77 */         throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttc.file", new Object[] { this.fileName })); 
/*  78 */       this.rf.skipBytes(4);
/*  79 */       int dirCount = this.rf.readInt();
/*  80 */       this.names = new String[dirCount];
/*  81 */       int dirPos = (int)this.rf.getFilePointer();
/*  82 */       for (int dirIdx = 0; dirIdx < dirCount; dirIdx++) {
/*  83 */         this.tables.clear();
/*  84 */         this.rf.seek(dirPos);
/*  85 */         this.rf.skipBytes(dirIdx * 4);
/*  86 */         this.directoryOffset = this.rf.readInt();
/*  87 */         this.rf.seek(this.directoryOffset);
/*  88 */         if (this.rf.readInt() != 65536)
/*  89 */           throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.a.valid.ttf.file", new Object[] { this.fileName })); 
/*  90 */         int num_tables = this.rf.readUnsignedShort();
/*  91 */         this.rf.skipBytes(6);
/*  92 */         for (int k = 0; k < num_tables; k++) {
/*  93 */           String tag = readStandardString(4);
/*  94 */           this.rf.skipBytes(4);
/*  95 */           int[] table_location = new int[2];
/*  96 */           table_location[0] = this.rf.readInt();
/*  97 */           table_location[1] = this.rf.readInt();
/*  98 */           this.tables.put(tag, table_location);
/*     */         } 
/* 100 */         this.names[dirIdx] = getBaseFont();
/*     */       } 
/*     */     } finally {
/*     */       
/* 104 */       if (this.rf != null)
/* 105 */         this.rf.close(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   String[] getNames() {
/* 110 */     return this.names;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/EnumerateTTC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */