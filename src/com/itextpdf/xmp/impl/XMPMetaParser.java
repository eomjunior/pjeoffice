/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMeta;
/*     */ import com.itextpdf.xmp.options.ParseOptions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
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
/*     */ public class XMPMetaParser
/*     */ {
/*  69 */   private static final Object XMP_RDF = new Object();
/*     */   
/*  71 */   private static DocumentBuilderFactory factory = createDocumentBuilderFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMPMeta parse(Object input, ParseOptions options) throws XMPException {
/*  95 */     ParameterAsserts.assertNotNull(input);
/*  96 */     options = (options != null) ? options : new ParseOptions();
/*     */     
/*  98 */     Document document = parseXml(input, options);
/*     */     
/* 100 */     boolean xmpmetaRequired = options.getRequireXMPMeta();
/* 101 */     Object[] result = new Object[3];
/* 102 */     result = findRootNode(document, xmpmetaRequired, result);
/*     */     
/* 104 */     if (result != null && result[1] == XMP_RDF) {
/*     */       
/* 106 */       XMPMetaImpl xmp = ParseRDF.parse((Node)result[0]);
/* 107 */       xmp.setPacketHeader((String)result[2]);
/*     */ 
/*     */       
/* 110 */       if (!options.getOmitNormalization())
/*     */       {
/* 112 */         return XMPNormalizer.process(xmp, options);
/*     */       }
/*     */ 
/*     */       
/* 116 */       return xmp;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     return new XMPMetaImpl();
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
/*     */ 
/*     */   
/*     */   private static Document parseXml(Object input, ParseOptions options) throws XMPException {
/* 147 */     if (input instanceof InputStream)
/*     */     {
/* 149 */       return parseXmlFromInputStream((InputStream)input, options);
/*     */     }
/* 151 */     if (input instanceof byte[])
/*     */     {
/* 153 */       return parseXmlFromBytebuffer(new ByteBuffer((byte[])input), options);
/*     */     }
/*     */ 
/*     */     
/* 157 */     return parseXmlFromString((String)input, options);
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
/*     */   private static Document parseXmlFromInputStream(InputStream stream, ParseOptions options) throws XMPException {
/* 174 */     if (!options.getAcceptLatin1() && !options.getFixControlChars())
/*     */     {
/* 176 */       return parseInputSource(new InputSource(stream));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 183 */       ByteBuffer buffer = new ByteBuffer(stream);
/* 184 */       return parseXmlFromBytebuffer(buffer, options);
/*     */     }
/* 186 */     catch (IOException e) {
/*     */       
/* 188 */       throw new XMPException("Error reading the XML-file", 204, e);
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
/*     */   private static Document parseXmlFromBytebuffer(ByteBuffer buffer, ParseOptions options) throws XMPException {
/* 207 */     InputSource source = new InputSource(buffer.getByteStream());
/*     */     
/*     */     try {
/* 210 */       return parseInputSource(source);
/*     */     }
/* 212 */     catch (XMPException e) {
/*     */       
/* 214 */       if (e.getErrorCode() == 201 || e
/* 215 */         .getErrorCode() == 204) {
/*     */         
/* 217 */         if (options.getAcceptLatin1())
/*     */         {
/* 219 */           buffer = Latin1Converter.convert(buffer);
/*     */         }
/*     */         
/* 222 */         if (options.getFixControlChars()) {
/*     */           
/*     */           try {
/*     */             
/* 226 */             String encoding = buffer.getEncoding();
/*     */ 
/*     */             
/* 229 */             Reader fixReader = new FixASCIIControlsReader(new InputStreamReader(buffer.getByteStream(), encoding));
/* 230 */             return parseInputSource(new InputSource(fixReader));
/*     */           }
/* 232 */           catch (UnsupportedEncodingException e1) {
/*     */ 
/*     */             
/* 235 */             throw new XMPException("Unsupported Encoding", 9, e);
/*     */           } 
/*     */         }
/*     */         
/* 239 */         source = new InputSource(buffer.getByteStream());
/* 240 */         return parseInputSource(source);
/*     */       } 
/*     */ 
/*     */       
/* 244 */       throw e;
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
/*     */   private static Document parseXmlFromString(String input, ParseOptions options) throws XMPException {
/* 262 */     InputSource source = new InputSource(new StringReader(input));
/*     */     
/*     */     try {
/* 265 */       return parseInputSource(source);
/*     */     }
/* 267 */     catch (XMPException e) {
/*     */       
/* 269 */       if (e.getErrorCode() == 201 && options.getFixControlChars()) {
/*     */         
/* 271 */         source = new InputSource(new FixASCIIControlsReader(new StringReader(input)));
/* 272 */         return parseInputSource(source);
/*     */       } 
/*     */ 
/*     */       
/* 276 */       throw e;
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
/*     */   private static Document parseInputSource(InputSource source) throws XMPException {
/*     */     try {
/* 292 */       DocumentBuilder builder = factory.newDocumentBuilder();
/* 293 */       builder.setErrorHandler(null);
/* 294 */       return builder.parse(source);
/*     */     }
/* 296 */     catch (SAXException e) {
/*     */       
/* 298 */       throw new XMPException("XML parsing failure", 201, e);
/*     */     }
/* 300 */     catch (ParserConfigurationException e) {
/*     */       
/* 302 */       throw new XMPException("XML Parser not correctly configured", 0, e);
/*     */     
/*     */     }
/* 305 */     catch (IOException e) {
/*     */       
/* 307 */       throw new XMPException("Error reading the XML-file", 204, e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object[] findRootNode(Node root, boolean xmpmetaRequired, Object[] result) {
/* 346 */     NodeList children = root.getChildNodes();
/* 347 */     for (int i = 0; i < children.getLength(); i++) {
/*     */       
/* 349 */       root = children.item(i);
/* 350 */       if (7 == root.getNodeType() && "xpacket"
/* 351 */         .equals(((ProcessingInstruction)root).getTarget())) {
/*     */ 
/*     */ 
/*     */         
/* 355 */         if (result != null)
/*     */         {
/* 357 */           result[2] = ((ProcessingInstruction)root).getData();
/*     */         }
/*     */       }
/* 360 */       else if (3 != root.getNodeType() && 7 != root
/* 361 */         .getNodeType()) {
/*     */         
/* 363 */         String rootNS = root.getNamespaceURI();
/* 364 */         String rootLocal = root.getLocalName();
/* 365 */         if (("xmpmeta"
/*     */           
/* 367 */           .equals(rootLocal) || "xapmeta"
/* 368 */           .equals(rootLocal)) && "adobe:ns:meta/"
/*     */           
/* 370 */           .equals(rootNS))
/*     */         {
/*     */ 
/*     */           
/* 374 */           return findRootNode(root, false, result);
/*     */         }
/* 376 */         if (!xmpmetaRequired && "RDF"
/* 377 */           .equals(rootLocal) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
/* 378 */           .equals(rootNS)) {
/*     */           
/* 380 */           if (result != null) {
/*     */             
/* 382 */             result[0] = root;
/* 383 */             result[1] = XMP_RDF;
/*     */           } 
/* 385 */           return result;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 390 */         Object[] newResult = findRootNode(root, xmpmetaRequired, result);
/* 391 */         if (newResult != null)
/*     */         {
/* 393 */           return newResult;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DocumentBuilderFactory createDocumentBuilderFactory() {
/* 415 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 416 */     factory.setNamespaceAware(true);
/* 417 */     factory.setIgnoringComments(true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 423 */       factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 428 */       factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
/*     */       
/* 430 */       factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
/*     */       
/* 432 */       factory.setXIncludeAware(false);
/* 433 */       factory.setExpandEntityReferences(false);
/*     */     
/*     */     }
/* 436 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 441 */     return factory;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPMetaParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */