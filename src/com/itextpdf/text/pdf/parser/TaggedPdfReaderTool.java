/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.xml.XMLUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaggedPdfReaderTool
/*     */ {
/*     */   protected PdfReader reader;
/*     */   protected PrintWriter out;
/*     */   
/*     */   public void convertToXml(PdfReader reader, OutputStream os, String charset) throws IOException {
/*  81 */     this.reader = reader;
/*  82 */     OutputStreamWriter outs = new OutputStreamWriter(os, charset);
/*  83 */     this.out = new PrintWriter(outs);
/*     */     
/*  85 */     PdfDictionary catalog = reader.getCatalog();
/*  86 */     PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
/*  87 */     if (struct == null) {
/*  88 */       throw new IOException(MessageLocalization.getComposedMessage("no.structtreeroot.found", new Object[0]));
/*     */     }
/*  90 */     inspectChild(struct.getDirectObject(PdfName.K));
/*  91 */     this.out.flush();
/*  92 */     this.out.close();
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
/*     */   public void convertToXml(PdfReader reader, OutputStream os) throws IOException {
/* 106 */     convertToXml(reader, os, "UTF-8");
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
/*     */   public void inspectChild(PdfObject k) throws IOException {
/* 118 */     if (k == null)
/*     */       return; 
/* 120 */     if (k instanceof PdfArray) {
/* 121 */       inspectChildArray((PdfArray)k);
/* 122 */     } else if (k instanceof PdfDictionary) {
/* 123 */       inspectChildDictionary((PdfDictionary)k);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inspectChildArray(PdfArray k) throws IOException {
/* 134 */     if (k == null)
/*     */       return; 
/* 136 */     for (int i = 0; i < k.size(); i++) {
/* 137 */       inspectChild(k.getDirectObject(i));
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
/*     */   public void inspectChildDictionary(PdfDictionary k) throws IOException {
/* 149 */     inspectChildDictionary(k, false);
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
/*     */   public void inspectChildDictionary(PdfDictionary k, boolean inspectAttributes) throws IOException {
/* 161 */     if (k == null)
/*     */       return; 
/* 163 */     PdfName s = k.getAsName(PdfName.S);
/* 164 */     if (s != null) {
/* 165 */       String tagN = PdfName.decodeName(s.toString());
/* 166 */       String tag = fixTagName(tagN);
/* 167 */       this.out.print("<");
/* 168 */       this.out.print(tag);
/* 169 */       if (inspectAttributes) {
/* 170 */         PdfDictionary a = k.getAsDict(PdfName.A);
/* 171 */         if (a != null) {
/* 172 */           Set<PdfName> keys = a.getKeys();
/* 173 */           for (PdfName key : keys) {
/* 174 */             this.out.print(' ');
/* 175 */             PdfObject value = a.get(key);
/* 176 */             value = PdfReader.getPdfObject(value);
/* 177 */             this.out.print(xmlName(key));
/* 178 */             this.out.print("=\"");
/* 179 */             this.out.print(value.toString());
/* 180 */             this.out.print("\"");
/*     */           } 
/*     */         } 
/*     */       } 
/* 184 */       this.out.print(">");
/* 185 */       PdfObject alt = k.get(PdfName.ALT);
/* 186 */       if (alt != null && alt.toString() != null) {
/* 187 */         this.out.print("<alt><![CDATA[");
/* 188 */         this.out.print(alt.toString().replaceAll("[\\000]*", ""));
/* 189 */         this.out.print("]]></alt>");
/*     */       } 
/* 191 */       PdfDictionary dict = k.getAsDict(PdfName.PG);
/* 192 */       if (dict != null)
/* 193 */         parseTag(tagN, k.getDirectObject(PdfName.K), dict); 
/* 194 */       inspectChild(k.getDirectObject(PdfName.K));
/* 195 */       this.out.print("</");
/* 196 */       this.out.print(tag);
/* 197 */       this.out.println(">");
/*     */     } else {
/* 199 */       inspectChild(k.getDirectObject(PdfName.K));
/*     */     } 
/*     */   }
/*     */   protected String xmlName(PdfName name) {
/* 203 */     String xmlName = name.toString().replaceFirst("/", "");
/*     */     
/* 205 */     xmlName = Character.toLowerCase(xmlName.charAt(0)) + xmlName.substring(1);
/* 206 */     return xmlName;
/*     */   }
/*     */   
/*     */   private static String fixTagName(String tag) {
/* 210 */     StringBuilder sb = new StringBuilder();
/* 211 */     for (int k = 0; k < tag.length(); k++) {
/* 212 */       char c = tag.charAt(k);
/* 213 */       boolean nameStart = (c == ':' || (c >= 'A' && c <= 'Z') || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'À' && c <= 'Ö') || (c >= 'Ø' && c <= 'ö') || (c >= 'ø' && c <= '˿') || (c >= 'Ͱ' && c <= 'ͽ') || (c >= 'Ϳ' && c <= '῿') || (c >= '‌' && c <= '‍') || (c >= '⁰' && c <= '↏') || (c >= 'Ⰰ' && c <= '⿯') || (c >= '、' && c <= '퟿') || (c >= '豈' && c <= '﷏') || (c >= 'ﷰ' && c <= '�'));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       boolean nameMiddle = (c == '-' || c == '.' || (c >= '0' && c <= '9') || c == '·' || (c >= '̀' && c <= 'ͯ') || (c >= '‿' && c <= '⁀') || nameStart);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       if (k == 0) {
/* 238 */         if (!nameStart) {
/* 239 */           c = '_';
/*     */         }
/*     */       }
/* 242 */       else if (!nameMiddle) {
/* 243 */         c = '-';
/*     */       } 
/* 245 */       sb.append(c);
/*     */     } 
/* 247 */     return sb.toString();
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
/*     */ 
/*     */   
/*     */   public void parseTag(String tag, PdfObject object, PdfDictionary page) throws IOException {
/* 264 */     if (object instanceof PdfNumber) {
/* 265 */       PdfNumber mcid = (PdfNumber)object;
/* 266 */       RenderFilter filter = new MarkedContentRenderFilter(mcid.intValue());
/* 267 */       TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
/* 268 */       FilteredTextRenderListener listener = new FilteredTextRenderListener(strategy, new RenderFilter[] { filter });
/*     */       
/* 270 */       PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
/*     */       
/* 272 */       processor.processContent(PdfReader.getPageContent(page), page
/* 273 */           .getAsDict(PdfName.RESOURCES));
/* 274 */       this.out.print(XMLUtil.escapeXML(listener.getResultantText(), true));
/*     */ 
/*     */     
/*     */     }
/* 278 */     else if (object instanceof PdfArray) {
/* 279 */       PdfArray arr = (PdfArray)object;
/* 280 */       int n = arr.size();
/* 281 */       for (int i = 0; i < n; i++) {
/* 282 */         parseTag(tag, arr.getPdfObject(i), page);
/* 283 */         if (i < n - 1) {
/* 284 */           this.out.println();
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 289 */     else if (object instanceof PdfDictionary) {
/* 290 */       PdfDictionary mcr = (PdfDictionary)object;
/* 291 */       parseTag(tag, mcr.getDirectObject(PdfName.MCID), mcr
/* 292 */           .getAsDict(PdfName.PG));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/TaggedPdfReaderTool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */