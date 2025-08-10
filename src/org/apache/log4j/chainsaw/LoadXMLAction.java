/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.xml.sax.InputSource;
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
/*     */ class LoadXMLAction
/*     */   extends AbstractAction
/*     */ {
/*  42 */   private static final Logger LOG = Logger.getLogger(LoadXMLAction.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final JFrame mParent;
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final JFileChooser mChooser = new JFileChooser();
/*     */   LoadXMLAction(JFrame aParent, MyTableModel aModel) throws SAXException, ParserConfigurationException {
/*  52 */     this.mChooser.setMultiSelectionEnabled(false);
/*  53 */     this.mChooser.setFileSelectionMode(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.mParent = aParent;
/*  71 */     this.mHandler = new XMLFileHandler(aModel);
/*  72 */     SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/*     */     
/*  74 */     saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
/*  75 */     saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
/*  76 */     this.mParser = saxParserFactory.newSAXParser().getXMLReader();
/*  77 */     this.mParser.setContentHandler(this.mHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   private final XMLReader mParser;
/*     */   
/*     */   private final XMLFileHandler mHandler;
/*     */   
/*     */   public void actionPerformed(ActionEvent aIgnore) {
/*  86 */     LOG.info("load file called");
/*  87 */     if (this.mChooser.showOpenDialog(this.mParent) == 0) {
/*  88 */       LOG.info("Need to load a file");
/*  89 */       File chosen = this.mChooser.getSelectedFile();
/*  90 */       LOG.info("loading the contents of " + chosen.getAbsolutePath());
/*     */       try {
/*  92 */         int num = loadFile(chosen.getAbsolutePath());
/*  93 */         JOptionPane.showMessageDialog(this.mParent, "Loaded " + num + " events.", "CHAINSAW", 1);
/*     */       }
/*  95 */       catch (Exception e) {
/*  96 */         LOG.warn("caught an exception loading the file", e);
/*  97 */         JOptionPane.showMessageDialog(this.mParent, "Error parsing file - " + e.getMessage(), "CHAINSAW", 0);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private int loadFile(String aFile) throws SAXException, IOException {
/* 112 */     synchronized (this.mParser) {
/*     */       
/* 114 */       StringBuilder buf = new StringBuilder();
/* 115 */       buf.append("<?xml version=\"1.0\" standalone=\"yes\"?>\n");
/* 116 */       buf.append("<!DOCTYPE log4j:eventSet ");
/* 117 */       buf.append("[<!ENTITY data SYSTEM \"file:///");
/* 118 */       buf.append(aFile);
/* 119 */       buf.append("\">]>\n");
/* 120 */       buf.append("<log4j:eventSet xmlns:log4j=\"Claira\">\n");
/* 121 */       buf.append("&data;\n");
/* 122 */       buf.append("</log4j:eventSet>\n");
/*     */       
/* 124 */       InputSource is = new InputSource(new StringReader(buf.toString()));
/* 125 */       this.mParser.parse(is);
/* 126 */       return this.mHandler.getNumEvents();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/LoadXMLAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */