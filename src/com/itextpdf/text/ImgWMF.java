/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import com.itextpdf.text.pdf.PdfTemplate;
/*     */ import com.itextpdf.text.pdf.codec.wmf.InputMeta;
/*     */ import com.itextpdf.text.pdf.codec.wmf.MetaDo;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImgWMF
/*     */   extends Image
/*     */ {
/*     */   ImgWMF(Image image) {
/*  69 */     super(image);
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
/*     */   public ImgWMF(URL url) throws BadElementException, IOException {
/*  81 */     super(url);
/*  82 */     processParameters();
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
/*     */   
/*     */   public ImgWMF(String filename) throws BadElementException, MalformedURLException, IOException {
/*  95 */     this(Utilities.toURL(filename));
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
/*     */   public ImgWMF(byte[] img) throws BadElementException, IOException {
/* 107 */     super((URL)null);
/* 108 */     this.rawData = img;
/* 109 */     this.originalData = img;
/* 110 */     processParameters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processParameters() throws BadElementException, IOException {
/* 120 */     this.type = 35;
/* 121 */     this.originalType = 6;
/* 122 */     InputStream is = null;
/*     */     try {
/*     */       String errorID;
/* 125 */       if (this.rawData == null) {
/* 126 */         is = this.url.openStream();
/* 127 */         errorID = this.url.toString();
/*     */       } else {
/*     */         
/* 130 */         is = new ByteArrayInputStream(this.rawData);
/* 131 */         errorID = "Byte array";
/*     */       } 
/* 133 */       InputMeta in = new InputMeta(is);
/* 134 */       if (in.readInt() != -1698247209) {
/* 135 */         throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.placeable.windows.metafile", new Object[] { errorID }));
/*     */       }
/* 137 */       in.readWord();
/* 138 */       int left = in.readShort();
/* 139 */       int top = in.readShort();
/* 140 */       int right = in.readShort();
/* 141 */       int bottom = in.readShort();
/* 142 */       int inch = in.readWord();
/* 143 */       this.dpiX = 72;
/* 144 */       this.dpiY = 72;
/* 145 */       this.scaledHeight = (bottom - top) / inch * 72.0F;
/* 146 */       setTop(this.scaledHeight);
/* 147 */       this.scaledWidth = (right - left) / inch * 72.0F;
/* 148 */       setRight(this.scaledWidth);
/*     */     } finally {
/*     */       
/* 151 */       if (is != null) {
/* 152 */         is.close();
/*     */       }
/* 154 */       this.plainWidth = getWidth();
/* 155 */       this.plainHeight = getHeight();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readWMF(PdfTemplate template) throws IOException, DocumentException {
/* 165 */     setTemplateData(template);
/* 166 */     template.setWidth(getWidth());
/* 167 */     template.setHeight(getHeight());
/* 168 */     InputStream is = null;
/*     */     try {
/* 170 */       if (this.rawData == null) {
/* 171 */         is = this.url.openStream();
/*     */       } else {
/*     */         
/* 174 */         is = new ByteArrayInputStream(this.rawData);
/*     */       } 
/* 176 */       MetaDo meta = new MetaDo(is, (PdfContentByte)template);
/* 177 */       meta.readAll();
/*     */     } finally {
/*     */       
/* 180 */       if (is != null)
/* 181 */         is.close(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ImgWMF.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */