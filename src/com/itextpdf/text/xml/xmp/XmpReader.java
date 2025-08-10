/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.xml.XmlDomWriter;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class XmpReader
/*     */ {
/*     */   public static final String EXTRASPACE = "                                                                                                   \n";
/*     */   public static final String XPACKET_PI_BEGIN = "<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n";
/*     */   public static final String XPACKET_PI_END_W = "<?xpacket end=\"w\"?>";
/*     */   private Document domDocument;
/*     */   
/*     */   public XmpReader(byte[] bytes) throws SAXException, IOException {
/*     */     try {
/*  99 */       DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
/* 100 */       fact.setNamespaceAware(true);
/* 101 */       DocumentBuilder db = fact.newDocumentBuilder();
/* 102 */       db.setEntityResolver(new SafeEmptyEntityResolver());
/* 103 */       ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
/* 104 */       this.domDocument = db.parse(bais);
/* 105 */     } catch (ParserConfigurationException e) {
/* 106 */       throw new ExceptionConverter(e);
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
/*     */   public boolean replaceNode(String namespaceURI, String localName, String value) {
/* 119 */     NodeList nodes = this.domDocument.getElementsByTagNameNS(namespaceURI, localName);
/*     */     
/* 121 */     if (nodes.getLength() == 0)
/* 122 */       return false; 
/* 123 */     for (int i = 0; i < nodes.getLength(); i++) {
/* 124 */       Node node = nodes.item(i);
/* 125 */       setNodeText(this.domDocument, node, value);
/*     */     } 
/* 127 */     return true;
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
/*     */   public boolean replaceDescriptionAttribute(String namespaceURI, String localName, String value) {
/* 139 */     NodeList descNodes = this.domDocument.getElementsByTagNameNS("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "Description");
/* 140 */     if (descNodes.getLength() == 0) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     for (int i = 0; i < descNodes.getLength(); i++) {
/* 145 */       Node node = descNodes.item(i);
/* 146 */       Node attr = node.getAttributes().getNamedItemNS(namespaceURI, localName);
/* 147 */       if (attr != null) {
/* 148 */         attr.setNodeValue(value);
/* 149 */         return true;
/*     */       } 
/*     */     } 
/* 152 */     return false;
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
/*     */   public boolean add(String parent, String namespaceURI, String localName, String value) {
/* 165 */     NodeList nodes = this.domDocument.getElementsByTagName(parent);
/* 166 */     if (nodes.getLength() == 0) {
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 171 */     for (int i = 0; i < nodes.getLength(); i++) {
/* 172 */       Node pNode = nodes.item(i);
/* 173 */       NamedNodeMap attrs = pNode.getAttributes();
/* 174 */       for (int j = 0; j < attrs.getLength(); j++) {
/* 175 */         Node node = attrs.item(j);
/* 176 */         if (namespaceURI.equals(node.getNodeValue())) {
/* 177 */           String prefix = node.getLocalName();
/* 178 */           node = this.domDocument.createElementNS(namespaceURI, localName);
/* 179 */           node.setPrefix(prefix);
/* 180 */           node.appendChild(this.domDocument.createTextNode(value));
/* 181 */           pNode.appendChild(node);
/* 182 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setNodeText(Document domDocument, Node n, String value) {
/* 197 */     if (n == null)
/* 198 */       return false; 
/* 199 */     Node nc = null;
/* 200 */     while ((nc = n.getFirstChild()) != null) {
/* 201 */       n.removeChild(nc);
/*     */     }
/* 203 */     n.appendChild(domDocument.createTextNode(value));
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] serializeDoc() throws IOException {
/* 211 */     XmlDomWriter xw = new XmlDomWriter();
/* 212 */     ByteArrayOutputStream fout = new ByteArrayOutputStream();
/* 213 */     xw.setOutput(fout, null);
/* 214 */     fout.write("<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>\n".getBytes("UTF-8"));
/* 215 */     fout.flush();
/* 216 */     NodeList xmpmeta = this.domDocument.getElementsByTagName("x:xmpmeta");
/* 217 */     xw.write(xmpmeta.item(0));
/* 218 */     fout.flush();
/* 219 */     for (int i = 0; i < 20; i++) {
/* 220 */       fout.write("                                                                                                   \n".getBytes());
/*     */     }
/* 222 */     fout.write("<?xpacket end=\"w\"?>".getBytes());
/* 223 */     fout.close();
/* 224 */     return fout.toByteArray();
/*     */   }
/*     */   
/*     */   private static class SafeEmptyEntityResolver implements EntityResolver {
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 229 */       return new InputSource(new StringReader(""));
/*     */     }
/*     */     
/*     */     private SafeEmptyEntityResolver() {}
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/XmpReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */