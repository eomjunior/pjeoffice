/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ import com.itextpdf.text.Version;
/*     */ import com.itextpdf.text.pdf.PdfDate;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMeta;
/*     */ import com.itextpdf.xmp.XMPMetaFactory;
/*     */ import com.itextpdf.xmp.XMPUtils;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ import com.itextpdf.xmp.options.SerializeOptions;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmpWriter
/*     */ {
/*     */   public static final String UTF8 = "UTF-8";
/*     */   public static final String UTF16 = "UTF-16";
/*     */   public static final String UTF16BE = "UTF-16BE";
/*     */   public static final String UTF16LE = "UTF-16LE";
/*     */   protected XMPMeta xmpMeta;
/*     */   protected OutputStream outputStream;
/*     */   protected SerializeOptions serializeOptions;
/*     */   
/*     */   public XmpWriter(OutputStream os, String utfEncoding, int extraSpace) throws IOException {
/*  88 */     this.outputStream = os;
/*  89 */     this.serializeOptions = new SerializeOptions();
/*  90 */     if ("UTF-16BE".equals(utfEncoding) || "UTF-16".equals(utfEncoding)) {
/*  91 */       this.serializeOptions.setEncodeUTF16BE(true);
/*  92 */     } else if ("UTF-16LE".equals(utfEncoding)) {
/*  93 */       this.serializeOptions.setEncodeUTF16LE(true);
/*  94 */     }  this.serializeOptions.setPadding(extraSpace);
/*  95 */     this.xmpMeta = XMPMetaFactory.create();
/*  96 */     this.xmpMeta.setObjectName("xmpmeta");
/*  97 */     this.xmpMeta.setObjectName("");
/*     */     try {
/*  99 */       this.xmpMeta.setProperty("http://purl.org/dc/elements/1.1/", "format", "application/pdf");
/* 100 */       this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "Producer", Version.getInstance().getVersion());
/* 101 */     } catch (XMPException xMPException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmpWriter(OutputStream os) throws IOException {
/* 110 */     this(os, "UTF-8", 2000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmpWriter(OutputStream os, PdfDictionary info) throws IOException {
/* 119 */     this(os);
/* 120 */     if (info != null)
/*     */     {
/*     */ 
/*     */       
/* 124 */       for (PdfName pdfName : info.getKeys()) {
/* 125 */         PdfName key = pdfName;
/* 126 */         PdfObject obj = info.get(key);
/* 127 */         if (obj == null)
/*     */           continue; 
/* 129 */         if (!obj.isString())
/*     */           continue; 
/* 131 */         String value = ((PdfString)obj).toUnicodeString();
/*     */         try {
/* 133 */           addDocInfoProperty(key, value);
/* 134 */         } catch (XMPException xmpExc) {
/* 135 */           throw new IOException(xmpExc.getMessage());
/*     */         } 
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
/*     */   public XmpWriter(OutputStream os, Map<String, String> info) throws IOException {
/* 148 */     this(os);
/* 149 */     if (info != null)
/*     */     {
/*     */       
/* 152 */       for (Map.Entry<String, String> entry : info.entrySet()) {
/* 153 */         String key = entry.getKey();
/* 154 */         String value = entry.getValue();
/* 155 */         if (value == null)
/*     */           continue; 
/*     */         try {
/* 158 */           addDocInfoProperty(key, value);
/* 159 */         } catch (XMPException xmpExc) {
/* 160 */           throw new IOException(xmpExc.getMessage());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public XMPMeta getXmpMeta() {
/* 167 */     return this.xmpMeta;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReadOnly() {
/* 172 */     this.serializeOptions.setReadOnlyPacket(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAbout(String about) {
/* 179 */     this.xmpMeta.setObjectName(about);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void addRdfDescription(String xmlns, String content) throws IOException {
/*     */     try {
/* 192 */       String str = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:Description rdf:about=\"" + this.xmpMeta.getObjectName() + "\" " + xmlns + ">" + content + "</rdf:Description></rdf:RDF>\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       XMPMeta extMeta = XMPMetaFactory.parseFromString(str);
/* 199 */       XMPUtils.appendProperties(extMeta, this.xmpMeta, true, true);
/* 200 */     } catch (XMPException xmpExc) {
/* 201 */       throw new IOException(xmpExc.getMessage());
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void addRdfDescription(XmpSchema s) throws IOException {
/*     */     try {
/* 218 */       String str = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"><rdf:Description rdf:about=\"" + this.xmpMeta.getObjectName() + "\" " + s.getXmlns() + ">" + s.toString() + "</rdf:Description></rdf:RDF>\n";
/*     */       
/* 220 */       XMPMeta extMeta = XMPMetaFactory.parseFromString(str);
/* 221 */       XMPUtils.appendProperties(extMeta, this.xmpMeta, true, true);
/* 222 */     } catch (XMPException xmpExc) {
/* 223 */       throw new IOException(xmpExc.getMessage());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String schemaNS, String propName, Object value) throws XMPException {
/* 239 */     this.xmpMeta.setProperty(schemaNS, propName, value);
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
/*     */ 
/*     */   
/*     */   public void appendArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
/* 254 */     this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(512), value, null);
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
/*     */ 
/*     */   
/*     */   public void appendOrderedArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
/* 269 */     this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(1024), value, null);
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
/*     */ 
/*     */   
/*     */   public void appendAlternateArrayItem(String schemaNS, String arrayName, String value) throws XMPException {
/* 284 */     this.xmpMeta.appendArrayItem(schemaNS, arrayName, new PropertyOptions(2048), value, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void serialize(OutputStream externalOutputStream) throws XMPException {
/* 292 */     XMPMetaFactory.serialize(this.xmpMeta, externalOutputStream, this.serializeOptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 300 */     if (this.outputStream == null)
/*     */       return; 
/*     */     try {
/* 303 */       XMPMetaFactory.serialize(this.xmpMeta, this.outputStream, this.serializeOptions);
/* 304 */       this.outputStream = null;
/* 305 */     } catch (XMPException xmpExc) {
/* 306 */       throw new IOException(xmpExc.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addDocInfoProperty(Object key, String value) throws XMPException {
/* 311 */     if (key instanceof String)
/* 312 */       key = new PdfName((String)key); 
/* 313 */     if (PdfName.TITLE.equals(key)) {
/* 314 */       this.xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "title", "x-default", "x-default", value);
/* 315 */     } else if (PdfName.AUTHOR.equals(key)) {
/* 316 */       this.xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "creator", new PropertyOptions(1024), value, null);
/* 317 */     } else if (PdfName.SUBJECT.equals(key)) {
/* 318 */       this.xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "description", "x-default", "x-default", value);
/* 319 */     } else if (PdfName.KEYWORDS.equals(key)) {
/* 320 */       for (String v : value.split(",|;")) {
/* 321 */         if (v.trim().length() > 0)
/* 322 */           this.xmpMeta.appendArrayItem("http://purl.org/dc/elements/1.1/", "subject", new PropertyOptions(512), v.trim(), null); 
/* 323 */       }  this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "Keywords", value);
/* 324 */     } else if (PdfName.PRODUCER.equals(key)) {
/* 325 */       this.xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", "Producer", value);
/* 326 */     } else if (PdfName.CREATOR.equals(key)) {
/* 327 */       this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "CreatorTool", value);
/* 328 */     } else if (PdfName.CREATIONDATE.equals(key)) {
/* 329 */       this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "CreateDate", PdfDate.getW3CDate(value));
/* 330 */     } else if (PdfName.MODDATE.equals(key)) {
/* 331 */       this.xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", "ModifyDate", PdfDate.getW3CDate(value));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */