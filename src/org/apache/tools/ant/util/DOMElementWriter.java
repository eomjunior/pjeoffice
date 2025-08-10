/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DOMElementWriter
/*     */ {
/*     */   private static final int HEX = 16;
/*  52 */   private static final String[] WS_ENTITIES = new String[5]; private static final String NS = "ns";
/*     */   static {
/*  54 */     for (int i = 9; i < 14; i++) {
/*  55 */       WS_ENTITIES[i - 9] = "&#x" + Integer.toHexString(i) + ";";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean xmlDeclaration = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private XmlNamespacePolicy namespacePolicy = XmlNamespacePolicy.IGNORE;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private Map<String, String> nsPrefixMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private int nextPrefix = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private Map<Element, List<String>> nsURIByElement = new HashMap<>();
/*     */ 
/*     */   
/*     */   protected String[] knownEntities;
/*     */ 
/*     */ 
/*     */   
/*     */   public static class XmlNamespacePolicy
/*     */   {
/*     */     private boolean qualifyElements;
/*     */ 
/*     */     
/*     */     private boolean qualifyAttributes;
/*     */     
/*  97 */     public static final XmlNamespacePolicy IGNORE = new XmlNamespacePolicy(false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     public static final XmlNamespacePolicy ONLY_QUALIFY_ELEMENTS = new XmlNamespacePolicy(true, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     public static final XmlNamespacePolicy QUALIFY_ALL = new XmlNamespacePolicy(true, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public XmlNamespacePolicy(boolean qualifyElements, boolean qualifyAttributes) {
/* 118 */       this.qualifyElements = qualifyElements;
/* 119 */       this.qualifyAttributes = qualifyAttributes;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public DOMElementWriter(boolean xmlDeclaration) {
/* 138 */     this(xmlDeclaration, XmlNamespacePolicy.IGNORE);
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
/*     */   public DOMElementWriter() {
/* 160 */     this.knownEntities = new String[] { "gt", "amp", "lt", "apos", "quot" }; } public DOMElementWriter(boolean xmlDeclaration, XmlNamespacePolicy namespacePolicy) { this.knownEntities = new String[] { "gt", "amp", "lt", "apos", "quot" };
/*     */     this.xmlDeclaration = xmlDeclaration;
/*     */     this.namespacePolicy = namespacePolicy; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(Element root, OutputStream out) throws IOException {
/* 174 */     Writer wri = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 175 */     writeXMLDeclaration(wri);
/* 176 */     write(root, wri, 0, "  ");
/* 177 */     wri.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeXMLDeclaration(Writer wri) throws IOException {
/* 187 */     if (this.xmlDeclaration) {
/* 188 */       wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(Element element, Writer out, int indent, String indentWith) throws IOException {
/* 207 */     NodeList children = element.getChildNodes();
/* 208 */     boolean hasChildren = (children.getLength() > 0);
/* 209 */     boolean hasChildElements = false;
/* 210 */     openElement(element, out, indent, indentWith, hasChildren);
/*     */     
/* 212 */     if (hasChildren) {
/* 213 */       for (int i = 0; i < children.getLength(); i++) {
/* 214 */         String data; Node child = children.item(i);
/*     */         
/* 216 */         switch (child.getNodeType()) {
/*     */           
/*     */           case 1:
/* 219 */             hasChildElements = true;
/* 220 */             if (i == 0) {
/* 221 */               out.write(System.lineSeparator());
/*     */             }
/* 223 */             write((Element)child, out, indent + 1, indentWith);
/*     */             break;
/*     */           
/*     */           case 3:
/* 227 */             out.write(encode(child.getNodeValue()));
/*     */             break;
/*     */           
/*     */           case 8:
/* 231 */             out.write("<!--");
/* 232 */             out.write(encode(child.getNodeValue()));
/* 233 */             out.write("-->");
/*     */             break;
/*     */           
/*     */           case 4:
/* 237 */             out.write("<![CDATA[");
/* 238 */             encodedata(out, ((Text)child).getData());
/* 239 */             out.write("]]>");
/*     */             break;
/*     */           
/*     */           case 5:
/* 243 */             out.write(38);
/* 244 */             out.write(child.getNodeName());
/* 245 */             out.write(59);
/*     */             break;
/*     */           
/*     */           case 7:
/* 249 */             out.write("<?");
/* 250 */             out.write(child.getNodeName());
/* 251 */             data = child.getNodeValue();
/* 252 */             if (data != null && !data.isEmpty()) {
/* 253 */               out.write(32);
/* 254 */               out.write(data);
/*     */             } 
/* 256 */             out.write("?>");
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 262 */       closeElement(element, out, indent, indentWith, hasChildElements);
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
/*     */ 
/*     */   
/*     */   public void openElement(Element element, Writer out, int indent, String indentWith) throws IOException {
/* 280 */     openElement(element, out, indent, indentWith, true);
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
/*     */   public void openElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
/* 300 */     for (int i = 0; i < indent; i++) {
/* 301 */       out.write(indentWith);
/*     */     }
/*     */ 
/*     */     
/* 305 */     out.write("<");
/* 306 */     if (this.namespacePolicy.qualifyElements) {
/* 307 */       String uri = getNamespaceURI(element);
/* 308 */       String prefix = this.nsPrefixMap.get(uri);
/* 309 */       if (prefix == null) {
/* 310 */         if (this.nsPrefixMap.isEmpty()) {
/*     */           
/* 312 */           prefix = "";
/*     */         } else {
/* 314 */           prefix = "ns" + this.nextPrefix++;
/*     */         } 
/* 316 */         this.nsPrefixMap.put(uri, prefix);
/* 317 */         addNSDefinition(element, uri);
/*     */       } 
/* 319 */       if (!prefix.isEmpty()) {
/* 320 */         out.write(prefix);
/* 321 */         out.write(":");
/*     */       } 
/*     */     } 
/* 324 */     out.write(element.getTagName());
/*     */ 
/*     */     
/* 327 */     NamedNodeMap attrs = element.getAttributes();
/* 328 */     for (int j = 0; j < attrs.getLength(); j++) {
/* 329 */       Attr attr = (Attr)attrs.item(j);
/* 330 */       out.write(" ");
/* 331 */       if (this.namespacePolicy.qualifyAttributes) {
/* 332 */         String uri = getNamespaceURI(attr);
/* 333 */         String prefix = this.nsPrefixMap.get(uri);
/* 334 */         if (prefix == null) {
/* 335 */           prefix = "ns" + this.nextPrefix++;
/* 336 */           this.nsPrefixMap.put(uri, prefix);
/* 337 */           addNSDefinition(element, uri);
/*     */         } 
/* 339 */         out.write(prefix);
/* 340 */         out.write(":");
/*     */       } 
/* 342 */       out.write(attr.getName());
/* 343 */       out.write("=\"");
/* 344 */       out.write(encodeAttributeValue(attr.getValue()));
/* 345 */       out.write("\"");
/*     */     } 
/*     */ 
/*     */     
/* 349 */     List<String> uris = this.nsURIByElement.get(element);
/* 350 */     if (uris != null) {
/* 351 */       for (String uri : uris) {
/* 352 */         String prefix = this.nsPrefixMap.get(uri);
/* 353 */         out.write(" xmlns");
/* 354 */         if (!prefix.isEmpty()) {
/* 355 */           out.write(":");
/* 356 */           out.write(prefix);
/*     */         } 
/* 358 */         out.write("=\"");
/* 359 */         out.write(uri);
/* 360 */         out.write("\"");
/*     */       } 
/*     */     }
/*     */     
/* 364 */     if (hasChildren) {
/* 365 */       out.write(">");
/*     */     } else {
/* 367 */       removeNSDefinitions(element);
/* 368 */       out.write(String.format(" />%n", new Object[0]));
/* 369 */       out.flush();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
/* 390 */     if (hasChildren) {
/* 391 */       for (int i = 0; i < indent; i++) {
/* 392 */         out.write(indentWith);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 397 */     out.write("</");
/* 398 */     if (this.namespacePolicy.qualifyElements) {
/* 399 */       String uri = getNamespaceURI(element);
/* 400 */       String prefix = this.nsPrefixMap.get(uri);
/* 401 */       if (prefix != null && !prefix.isEmpty()) {
/* 402 */         out.write(prefix);
/* 403 */         out.write(":");
/*     */       } 
/* 405 */       removeNSDefinitions(element);
/*     */     } 
/* 407 */     out.write(element.getTagName());
/* 408 */     out.write(String.format(">%n", new Object[0]));
/* 409 */     out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(String value) {
/* 419 */     return encode(value, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encodeAttributeValue(String value) {
/* 430 */     return encode(value, true);
/*     */   }
/*     */   
/*     */   private String encode(String value, boolean encodeWhitespace) {
/* 434 */     StringBuilder sb = new StringBuilder(value.length());
/* 435 */     for (char c : value.toCharArray()) {
/* 436 */       switch (c) {
/*     */         case '<':
/* 438 */           sb.append("&lt;");
/*     */           break;
/*     */         case '>':
/* 441 */           sb.append("&gt;");
/*     */           break;
/*     */         case '\'':
/* 444 */           sb.append("&apos;");
/*     */           break;
/*     */         case '"':
/* 447 */           sb.append("&quot;");
/*     */           break;
/*     */         case '&':
/* 450 */           sb.append("&amp;");
/*     */           break;
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/* 455 */           if (encodeWhitespace) {
/* 456 */             sb.append(WS_ENTITIES[c - 9]); break;
/*     */           } 
/* 458 */           sb.append(c);
/*     */           break;
/*     */         
/*     */         default:
/* 462 */           if (isLegalCharacter(c)) {
/* 463 */             sb.append(c);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     } 
/* 468 */     return sb.substring(0);
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
/*     */   public String encodedata(String value) {
/* 487 */     StringWriter out = new StringWriter();
/*     */     try {
/* 489 */       encodedata(out, value);
/* 490 */     } catch (IOException ex) {
/* 491 */       throw new RuntimeException(ex);
/*     */     } 
/* 493 */     return out.toString();
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
/*     */   public void encodedata(Writer out, String value) throws IOException {
/* 514 */     int len = value.length();
/* 515 */     int prevEnd = 0;
/* 516 */     int cdataEndPos = value.indexOf("]]>");
/* 517 */     while (prevEnd < len) {
/* 518 */       int end = (cdataEndPos < 0) ? len : cdataEndPos;
/*     */       
/* 520 */       int prevLegalCharPos = prevEnd;
/* 521 */       while (prevLegalCharPos < end) {
/* 522 */         int illegalCharPos = prevLegalCharPos;
/* 523 */         while (illegalCharPos < end && 
/* 524 */           isLegalCharacter(value.charAt(illegalCharPos))) {
/* 525 */           illegalCharPos++;
/*     */         }
/* 527 */         out.write(value, prevLegalCharPos, illegalCharPos - prevLegalCharPos);
/* 528 */         prevLegalCharPos = illegalCharPos + 1;
/*     */       } 
/*     */       
/* 531 */       if (cdataEndPos >= 0) {
/* 532 */         out.write("]]]]><![CDATA[>");
/* 533 */         prevEnd = cdataEndPos + 3;
/* 534 */         cdataEndPos = value.indexOf("]]>", prevEnd); continue;
/*     */       } 
/* 536 */       prevEnd = end;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference(String ent) {
/* 547 */     if (ent.charAt(0) != '&' || !ent.endsWith(";")) {
/* 548 */       return false;
/*     */     }
/*     */     
/* 551 */     if (ent.charAt(1) == '#') {
/* 552 */       if (ent.charAt(2) == 'x') {
/*     */         
/*     */         try {
/* 555 */           Integer.parseInt(ent.substring(3, ent.length() - 1), 16);
/*     */           
/* 557 */           return true;
/* 558 */         } catch (NumberFormatException nfe) {
/* 559 */           return false;
/*     */         } 
/*     */       }
/*     */       try {
/* 563 */         Integer.parseInt(ent.substring(2, ent.length() - 1));
/* 564 */         return true;
/* 565 */       } catch (NumberFormatException nfe) {
/* 566 */         return false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 571 */     String name = ent.substring(1, ent.length() - 1);
/* 572 */     for (String knownEntity : this.knownEntities) {
/* 573 */       if (name.equals(knownEntity)) {
/* 574 */         return true;
/*     */       }
/*     */     } 
/* 577 */     return false;
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
/*     */   public boolean isLegalCharacter(char c) {
/* 591 */     return isLegalXmlCharacter(c);
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
/*     */   public static boolean isLegalXmlCharacter(char c) {
/* 606 */     if (c == '\t' || c == '\n' || c == '\r') {
/* 607 */       return true;
/*     */     }
/* 609 */     if (c < ' ') {
/* 610 */       return false;
/*     */     }
/* 612 */     if (c <= '퟿') {
/* 613 */       return true;
/*     */     }
/* 615 */     if (c < '') {
/* 616 */       return false;
/*     */     }
/* 618 */     return (c <= '�');
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeNSDefinitions(Element element) {
/* 623 */     List<String> uris = this.nsURIByElement.get(element);
/* 624 */     if (uris != null) {
/* 625 */       Objects.requireNonNull(this.nsPrefixMap); uris.forEach(this.nsPrefixMap::remove);
/* 626 */       this.nsURIByElement.remove(element);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addNSDefinition(Element element, String uri) {
/* 631 */     ((List<String>)this.nsURIByElement.computeIfAbsent(element, e -> new ArrayList()))
/* 632 */       .add(uri);
/*     */   }
/*     */   
/*     */   private static String getNamespaceURI(Node n) {
/* 636 */     String uri = n.getNamespaceURI();
/* 637 */     return (uri == null) ? "" : uri;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/DOMElementWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */