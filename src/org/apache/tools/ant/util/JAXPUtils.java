/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXPUtils
/*     */ {
/*  51 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static SAXParserFactory parserFactory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private static SAXParserFactory nsParserFactory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static DocumentBuilderFactory builderFactory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized SAXParserFactory getParserFactory() throws BuildException {
/*  87 */     if (parserFactory == null) {
/*  88 */       parserFactory = newParserFactory();
/*     */     }
/*  90 */     return parserFactory;
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
/*     */   public static synchronized SAXParserFactory getNSParserFactory() throws BuildException {
/* 105 */     if (nsParserFactory == null) {
/* 106 */       nsParserFactory = newParserFactory();
/* 107 */       nsParserFactory.setNamespaceAware(true);
/*     */     } 
/* 109 */     return nsParserFactory;
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
/*     */   public static SAXParserFactory newParserFactory() throws BuildException {
/*     */     try {
/* 122 */       return SAXParserFactory.newInstance();
/* 123 */     } catch (FactoryConfigurationError e) {
/* 124 */       throw new BuildException("XML parser factory has not been configured correctly: " + e
/*     */           
/* 126 */           .getMessage(), e);
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
/*     */   public static Parser getParser() throws BuildException {
/*     */     try {
/* 141 */       return newSAXParser(getParserFactory()).getParser();
/* 142 */     } catch (SAXException e) {
/* 143 */       throw convertToBuildException(e);
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
/*     */   public static XMLReader getXMLReader() throws BuildException {
/*     */     try {
/* 158 */       return newSAXParser(getParserFactory()).getXMLReader();
/* 159 */     } catch (SAXException e) {
/* 160 */       throw convertToBuildException(e);
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
/*     */   public static XMLReader getNamespaceXMLReader() throws BuildException {
/*     */     try {
/* 174 */       return newSAXParser(getNSParserFactory()).getXMLReader();
/* 175 */     } catch (SAXException e) {
/* 176 */       throw convertToBuildException(e);
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
/*     */   public static String getSystemId(File file) {
/* 190 */     return FILE_UTILS.toURI(file.getAbsolutePath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DocumentBuilder getDocumentBuilder() throws BuildException {
/*     */     try {
/* 202 */       return getDocumentBuilderFactory().newDocumentBuilder();
/* 203 */     } catch (ParserConfigurationException e) {
/* 204 */       throw new BuildException(e);
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
/*     */   private static SAXParser newSAXParser(SAXParserFactory factory) throws BuildException {
/*     */     try {
/* 217 */       return factory.newSAXParser();
/* 218 */     } catch (ParserConfigurationException e) {
/* 219 */       throw new BuildException("Cannot create parser for the given configuration: " + e
/* 220 */           .getMessage(), e);
/* 221 */     } catch (SAXException e) {
/* 222 */       throw convertToBuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static BuildException convertToBuildException(SAXException e) {
/* 232 */     Exception nested = e.getException();
/* 233 */     if (nested != null) {
/* 234 */       return new BuildException(nested);
/*     */     }
/* 236 */     return new BuildException(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized DocumentBuilderFactory getDocumentBuilderFactory() throws BuildException {
/* 247 */     if (builderFactory == null) {
/*     */       try {
/* 249 */         builderFactory = DocumentBuilderFactory.newInstance();
/* 250 */       } catch (FactoryConfigurationError e) {
/* 251 */         throw new BuildException("Document builder factory has not been configured correctly: " + e
/*     */             
/* 253 */             .getMessage(), e);
/*     */       } 
/*     */     }
/* 256 */     return builderFactory;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/JAXPUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */