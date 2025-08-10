/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.xml.XMLUtil;
/*     */ import com.itextpdf.text.xml.simpleparser.IanaEncodings;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpleNamedDestination
/*     */   implements SimpleXMLDocHandler
/*     */ {
/*     */   private HashMap<String, String> xmlNames;
/*     */   private HashMap<String, String> xmlLast;
/*     */   
/*     */   public static HashMap<String, String> getNamedDestination(PdfReader reader, boolean fromNames) {
/*  76 */     IntHashtable pages = new IntHashtable();
/*  77 */     int numPages = reader.getNumberOfPages();
/*  78 */     for (int k = 1; k <= numPages; k++)
/*  79 */       pages.put(reader.getPageOrigRef(k).getNumber(), k); 
/*  80 */     HashMap<String, PdfObject> names = fromNames ? reader.getNamedDestinationFromNames() : reader.getNamedDestinationFromStrings();
/*  81 */     HashMap<String, String> n2 = new HashMap<String, String>(names.size());
/*  82 */     for (Map.Entry<String, PdfObject> entry : names.entrySet()) {
/*  83 */       PdfArray arr = (PdfArray)entry.getValue();
/*  84 */       StringBuffer s = new StringBuffer();
/*     */       try {
/*  86 */         s.append(pages.get(arr.getAsIndirectObject(0).getNumber()));
/*  87 */         s.append(' ').append(arr.getPdfObject(1).toString().substring(1));
/*  88 */         for (int i = 2; i < arr.size(); i++)
/*  89 */           s.append(' ').append(arr.getPdfObject(i).toString()); 
/*  90 */         n2.put(entry.getKey(), s.toString());
/*     */       }
/*  92 */       catch (Exception exception) {}
/*     */     } 
/*     */     
/*  95 */     return n2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void exportToXML(HashMap<String, String> names, OutputStream out, String encoding, boolean onlyASCII) throws IOException {
/* 118 */     String jenc = IanaEncodings.getJavaEncoding(encoding);
/* 119 */     Writer wrt = new BufferedWriter(new OutputStreamWriter(out, jenc));
/* 120 */     exportToXML(names, wrt, encoding, onlyASCII);
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
/*     */   public static void exportToXML(HashMap<String, String> names, Writer wrt, String encoding, boolean onlyASCII) throws IOException {
/* 134 */     wrt.write("<?xml version=\"1.0\" encoding=\"");
/* 135 */     wrt.write(XMLUtil.escapeXML(encoding, onlyASCII));
/* 136 */     wrt.write("\"?>\n<Destination>\n");
/* 137 */     for (Map.Entry<String, String> entry : names.entrySet()) {
/* 138 */       String key = entry.getKey();
/* 139 */       String value = entry.getValue();
/* 140 */       wrt.write("  <Name Page=\"");
/* 141 */       wrt.write(XMLUtil.escapeXML(value, onlyASCII));
/* 142 */       wrt.write("\">");
/* 143 */       wrt.write(XMLUtil.escapeXML(escapeBinaryString(key), onlyASCII));
/* 144 */       wrt.write("</Name>\n");
/*     */     } 
/* 146 */     wrt.write("</Destination>\n");
/* 147 */     wrt.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashMap<String, String> importFromXML(InputStream in) throws IOException {
/* 157 */     SimpleNamedDestination names = new SimpleNamedDestination();
/* 158 */     SimpleXMLParser.parse(names, in);
/* 159 */     return names.xmlNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashMap<String, String> importFromXML(Reader in) throws IOException {
/* 169 */     SimpleNamedDestination names = new SimpleNamedDestination();
/* 170 */     SimpleXMLParser.parse(names, in);
/* 171 */     return names.xmlNames;
/*     */   }
/*     */   
/*     */   static PdfArray createDestinationArray(String value, PdfWriter writer) {
/* 175 */     PdfArray ar = new PdfArray();
/* 176 */     StringTokenizer tk = new StringTokenizer(value);
/* 177 */     int n = Integer.parseInt(tk.nextToken());
/* 178 */     ar.add(writer.getPageReference(n));
/* 179 */     if (!tk.hasMoreTokens()) {
/* 180 */       ar.add(PdfName.XYZ);
/* 181 */       ar.add(new float[] { 0.0F, 10000.0F, 0.0F });
/*     */     } else {
/*     */       
/* 184 */       String fn = tk.nextToken();
/* 185 */       if (fn.startsWith("/"))
/* 186 */         fn = fn.substring(1); 
/* 187 */       ar.add(new PdfName(fn));
/* 188 */       for (int k = 0; k < 4 && tk.hasMoreTokens(); k++) {
/* 189 */         fn = tk.nextToken();
/* 190 */         if (fn.equals("null")) {
/* 191 */           ar.add(PdfNull.PDFNULL);
/*     */         } else {
/* 193 */           ar.add(new PdfNumber(fn));
/*     */         } 
/*     */       } 
/* 196 */     }  return ar;
/*     */   }
/*     */   
/*     */   public static PdfDictionary outputNamedDestinationAsNames(HashMap<String, String> names, PdfWriter writer) {
/* 200 */     PdfDictionary dic = new PdfDictionary();
/* 201 */     for (Map.Entry<String, String> entry : names.entrySet()) {
/*     */       try {
/* 203 */         String key = entry.getKey();
/* 204 */         String value = entry.getValue();
/* 205 */         PdfArray ar = createDestinationArray(value, writer);
/* 206 */         PdfName kn = new PdfName(key);
/* 207 */         dic.put(kn, ar);
/*     */       }
/* 209 */       catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 213 */     return dic;
/*     */   }
/*     */   
/*     */   public static PdfDictionary outputNamedDestinationAsStrings(HashMap<String, String> names, PdfWriter writer) throws IOException {
/* 217 */     HashMap<String, PdfObject> n2 = new HashMap<String, PdfObject>(names.size());
/* 218 */     for (Map.Entry<String, String> entry : names.entrySet()) {
/*     */       try {
/* 220 */         String value = entry.getValue();
/* 221 */         PdfArray ar = createDestinationArray(value, writer);
/* 222 */         n2.put(entry.getKey(), writer.addToBody(ar).getIndirectReference());
/*     */       }
/* 224 */       catch (Exception exception) {}
/*     */     } 
/*     */     
/* 227 */     return PdfNameTree.writeTree(n2, writer);
/*     */   }
/*     */   
/*     */   public static String escapeBinaryString(String s) {
/* 231 */     StringBuffer buf = new StringBuffer();
/* 232 */     char[] cc = s.toCharArray();
/* 233 */     int len = cc.length;
/* 234 */     for (int k = 0; k < len; k++) {
/* 235 */       char c = cc[k];
/* 236 */       if (c < ' ') {
/* 237 */         buf.append('\\');
/* 238 */         String octal = "00" + Integer.toOctalString(c);
/* 239 */         buf.append(octal.substring(octal.length() - 3));
/*     */       }
/* 241 */       else if (c == '\\') {
/* 242 */         buf.append("\\\\");
/*     */       } else {
/* 244 */         buf.append(c);
/*     */       } 
/* 246 */     }  return buf.toString();
/*     */   }
/*     */   
/*     */   public static String unEscapeBinaryString(String s) {
/* 250 */     StringBuffer buf = new StringBuffer();
/* 251 */     char[] cc = s.toCharArray();
/* 252 */     int len = cc.length;
/* 253 */     for (int k = 0; k < len; k++) {
/* 254 */       char c = cc[k];
/* 255 */       if (c == '\\') {
/* 256 */         if (++k >= len) {
/* 257 */           buf.append('\\');
/*     */           break;
/*     */         } 
/* 260 */         c = cc[k];
/* 261 */         if (c >= '0' && c <= '7') {
/* 262 */           int n = c - 48;
/* 263 */           k++;
/* 264 */           for (int j = 0; j < 2 && k < len; ) {
/* 265 */             c = cc[k];
/* 266 */             if (c >= '0' && c <= '7') {
/* 267 */               k++;
/* 268 */               n = n * 8 + c - 48;
/*     */               
/*     */               j++;
/*     */             } 
/*     */           } 
/*     */           
/* 274 */           k--;
/* 275 */           buf.append((char)n);
/*     */         } else {
/*     */           
/* 278 */           buf.append(c);
/*     */         } 
/*     */       } else {
/* 281 */         buf.append(c);
/*     */       } 
/* 283 */     }  return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */   
/*     */   public void endElement(String tag) {
/* 290 */     if (tag.equals("Destination")) {
/* 291 */       if (this.xmlLast == null && this.xmlNames != null) {
/*     */         return;
/*     */       }
/* 294 */       throw new RuntimeException(MessageLocalization.getComposedMessage("destination.end.tag.out.of.place", new Object[0]));
/*     */     } 
/* 296 */     if (!tag.equals("Name"))
/* 297 */       throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.end.tag.1", new Object[] { tag })); 
/* 298 */     if (this.xmlLast == null || this.xmlNames == null)
/* 299 */       throw new RuntimeException(MessageLocalization.getComposedMessage("name.end.tag.out.of.place", new Object[0])); 
/* 300 */     if (!this.xmlLast.containsKey("Page"))
/* 301 */       throw new RuntimeException(MessageLocalization.getComposedMessage("page.attribute.missing", new Object[0])); 
/* 302 */     this.xmlNames.put(unEscapeBinaryString(this.xmlLast.get("Name")), this.xmlLast.get("Page"));
/* 303 */     this.xmlLast = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public void startElement(String tag, Map<String, String> h) {
/* 310 */     if (this.xmlNames == null) {
/* 311 */       if (tag.equals("Destination")) {
/* 312 */         this.xmlNames = new HashMap<String, String>();
/*     */         
/*     */         return;
/*     */       } 
/* 316 */       throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.destination", new Object[0]));
/*     */     } 
/* 318 */     if (!tag.equals("Name"))
/* 319 */       throw new RuntimeException(MessageLocalization.getComposedMessage("tag.1.not.allowed", new Object[] { tag })); 
/* 320 */     if (this.xmlLast != null)
/* 321 */       throw new RuntimeException(MessageLocalization.getComposedMessage("nested.tags.are.not.allowed", new Object[0])); 
/* 322 */     this.xmlLast = new HashMap<String, String>(h);
/* 323 */     this.xmlLast.put("Name", "");
/*     */   }
/*     */   
/*     */   public void text(String str) {
/* 327 */     if (this.xmlLast == null)
/*     */       return; 
/* 329 */     String name = this.xmlLast.get("Name");
/* 330 */     name = name + str;
/* 331 */     this.xmlLast.put("Name", name);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/SimpleNamedDestination.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */