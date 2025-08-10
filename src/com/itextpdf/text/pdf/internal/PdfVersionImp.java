/*     */ package com.itextpdf.text.pdf.internal;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.pdf.OutputStreamCounter;
/*     */ import com.itextpdf.text.pdf.PdfDeveloperExtension;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfVersion;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfVersionImp
/*     */   implements PdfVersion
/*     */ {
/*  65 */   public static final byte[][] HEADER = new byte[][] {
/*  66 */       DocWriter.getISOBytes("\n"), 
/*  67 */       DocWriter.getISOBytes("%PDF-"), 
/*  68 */       DocWriter.getISOBytes("\n%âãÏÓ\n")
/*     */     };
/*     */ 
/*     */   
/*     */   protected boolean headerWasWritten = false;
/*     */   
/*     */   protected boolean appendmode = false;
/*     */   
/*  76 */   protected char header_version = '4';
/*     */   
/*  78 */   protected PdfName catalog_version = null;
/*     */   
/*  80 */   protected char version = '4';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected PdfDictionary extensions = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPdfVersion(char version) {
/*  92 */     this.version = version;
/*  93 */     if (this.headerWasWritten || this.appendmode) {
/*  94 */       setPdfVersion(getVersionAsName(version));
/*     */     } else {
/*     */       
/*  97 */       this.header_version = version;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAtLeastPdfVersion(char version) {
/* 105 */     if (version > this.header_version) {
/* 106 */       setPdfVersion(version);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPdfVersion(PdfName version) {
/* 114 */     if (this.catalog_version == null || this.catalog_version.compareTo(version) < 0) {
/* 115 */       this.catalog_version = version;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppendmode(boolean appendmode) {
/* 123 */     this.appendmode = appendmode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeHeader(OutputStreamCounter os) throws IOException {
/* 131 */     if (this.appendmode) {
/* 132 */       os.write(HEADER[0]);
/*     */     } else {
/*     */       
/* 135 */       os.write(HEADER[1]);
/* 136 */       os.write(getVersionAsByteArray(this.header_version));
/* 137 */       os.write(HEADER[2]);
/* 138 */       this.headerWasWritten = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getVersionAsName(char version) {
/* 147 */     switch (version) {
/*     */       case '2':
/* 149 */         return PdfWriter.PDF_VERSION_1_2;
/*     */       case '3':
/* 151 */         return PdfWriter.PDF_VERSION_1_3;
/*     */       case '4':
/* 153 */         return PdfWriter.PDF_VERSION_1_4;
/*     */       case '5':
/* 155 */         return PdfWriter.PDF_VERSION_1_5;
/*     */       case '6':
/* 157 */         return PdfWriter.PDF_VERSION_1_6;
/*     */       case '7':
/* 159 */         return PdfWriter.PDF_VERSION_1_7;
/*     */     } 
/* 161 */     return PdfWriter.PDF_VERSION_1_4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getVersionAsByteArray(char version) {
/* 170 */     return DocWriter.getISOBytes(getVersionAsName(version).toString().substring(1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToCatalog(PdfDictionary catalog) {
/* 175 */     if (this.catalog_version != null) {
/* 176 */       catalog.put(PdfName.VERSION, (PdfObject)this.catalog_version);
/*     */     }
/* 178 */     if (this.extensions != null) {
/* 179 */       catalog.put(PdfName.EXTENSIONS, (PdfObject)this.extensions);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeveloperExtension(PdfDeveloperExtension de) {
/* 188 */     if (this.extensions == null) {
/* 189 */       this.extensions = new PdfDictionary();
/*     */     } else {
/*     */       
/* 192 */       PdfDictionary extension = this.extensions.getAsDict(de.getPrefix());
/* 193 */       if (extension != null) {
/* 194 */         int diff = de.getBaseversion().compareTo(extension.getAsName(PdfName.BASEVERSION));
/* 195 */         if (diff < 0)
/*     */           return; 
/* 197 */         diff = de.getExtensionLevel() - extension.getAsNumber(PdfName.EXTENSIONLEVEL).intValue();
/* 198 */         if (diff <= 0)
/*     */           return; 
/*     */       } 
/*     */     } 
/* 202 */     this.extensions.put(de.getPrefix(), (PdfObject)de.getDeveloperExtensions());
/*     */   }
/*     */   
/*     */   public char getVersion() {
/* 206 */     return this.version;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/internal/PdfVersionImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */