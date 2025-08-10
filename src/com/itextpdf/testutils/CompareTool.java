/*      */ package com.itextpdf.testutils;
/*      */ 
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.pdf.PRStream;
/*      */ import com.itextpdf.text.pdf.PRTokeniser;
/*      */ import com.itextpdf.text.pdf.PdfAnnotation;
/*      */ import com.itextpdf.text.pdf.PdfArray;
/*      */ import com.itextpdf.text.pdf.PdfBoolean;
/*      */ import com.itextpdf.text.pdf.PdfContentByte;
/*      */ import com.itextpdf.text.pdf.PdfContentParser;
/*      */ import com.itextpdf.text.pdf.PdfDictionary;
/*      */ import com.itextpdf.text.pdf.PdfIndirectReference;
/*      */ import com.itextpdf.text.pdf.PdfLiteral;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfNumber;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfReader;
/*      */ import com.itextpdf.text.pdf.PdfStamper;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*      */ import com.itextpdf.text.pdf.RefKey;
/*      */ import com.itextpdf.text.pdf.parser.ContentByteUtils;
/*      */ import com.itextpdf.text.pdf.parser.ImageRenderInfo;
/*      */ import com.itextpdf.text.pdf.parser.InlineImageInfo;
/*      */ import com.itextpdf.text.pdf.parser.InlineImageUtils;
/*      */ import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
/*      */ import com.itextpdf.text.pdf.parser.RenderListener;
/*      */ import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
/*      */ import com.itextpdf.text.pdf.parser.TaggedPdfReaderTool;
/*      */ import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
/*      */ import com.itextpdf.text.pdf.parser.TextRenderInfo;
/*      */ import com.itextpdf.text.xml.XMLUtil;
/*      */ import com.itextpdf.xmp.XMPException;
/*      */ import com.itextpdf.xmp.XMPMeta;
/*      */ import com.itextpdf.xmp.XMPMetaFactory;
/*      */ import com.itextpdf.xmp.XMPUtils;
/*      */ import com.itextpdf.xmp.options.SerializeOptions;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeSet;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CompareTool
/*      */ {
/*      */   private String gsExec;
/*      */   private String compareExec;
/*      */   private static final String renderedImageExtension = "png";
/*      */   private static final String pageNumberPattern = "%03d";
/*      */   
/*      */   private class ObjectPath
/*      */   {
/*      */     protected RefKey baseCmpObject;
/*      */     protected RefKey baseOutObject;
/*  138 */     protected Stack<PathItem> path = new Stack<PathItem>();
/*  139 */     protected Stack<Pair<RefKey>> indirects = new Stack<Pair<RefKey>>();
/*      */ 
/*      */     
/*      */     public ObjectPath() {}
/*      */     
/*      */     protected ObjectPath(RefKey baseCmpObject, RefKey baseOutObject) {
/*  145 */       this.baseCmpObject = baseCmpObject;
/*  146 */       this.baseOutObject = baseOutObject;
/*      */     }
/*      */     
/*      */     private ObjectPath(RefKey baseCmpObject, RefKey baseOutObject, Stack<PathItem> path) {
/*  150 */       this.baseCmpObject = baseCmpObject;
/*  151 */       this.baseOutObject = baseOutObject;
/*  152 */       this.path = path;
/*      */     }
/*      */     
/*      */     private class Pair<T> {
/*      */       private T first;
/*      */       private T second;
/*      */       
/*      */       public Pair(T first, T second) {
/*  160 */         this.first = first;
/*  161 */         this.second = second;
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  166 */         return this.first.hashCode() * 31 + this.second.hashCode();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  171 */         return (obj instanceof Pair && this.first.equals(((Pair)obj).first) && this.second.equals(((Pair)obj).second));
/*      */       } }
/*      */     
/*      */     private abstract class PathItem {
/*      */       private PathItem() {}
/*      */       
/*      */       protected abstract Node toXmlNode(Document param2Document);
/*      */     }
/*      */     
/*      */     private class DictPathItem extends PathItem {
/*      */       public DictPathItem(String key) {
/*  182 */         this.key = key;
/*      */       }
/*      */       String key;
/*      */       
/*      */       public String toString() {
/*  187 */         return "Dict key: " + this.key;
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  192 */         return this.key.hashCode();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  197 */         return (obj instanceof DictPathItem && this.key.equals(((DictPathItem)obj).key));
/*      */       }
/*      */ 
/*      */       
/*      */       protected Node toXmlNode(Document document) {
/*  202 */         Node element = document.createElement("dictKey");
/*  203 */         element.appendChild(document.createTextNode(this.key));
/*  204 */         return element;
/*      */       }
/*      */     }
/*      */     
/*      */     private class ArrayPathItem extends PathItem { int index;
/*      */       
/*      */       public ArrayPathItem(int index) {
/*  211 */         this.index = index;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  216 */         return "Array index: " + String.valueOf(this.index);
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  221 */         return this.index;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  226 */         return (obj instanceof ArrayPathItem && this.index == ((ArrayPathItem)obj).index);
/*      */       }
/*      */ 
/*      */       
/*      */       protected Node toXmlNode(Document document) {
/*  231 */         Node element = document.createElement("arrayIndex");
/*  232 */         element.appendChild(document.createTextNode(String.valueOf(this.index)));
/*  233 */         return element;
/*      */       } }
/*      */     
/*      */     private class OffsetPathItem extends PathItem {
/*      */       int offset;
/*      */       
/*      */       public OffsetPathItem(int offset) {
/*  240 */         this.offset = offset;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  245 */         return "Offset: " + String.valueOf(this.offset);
/*      */       }
/*      */ 
/*      */       
/*      */       public int hashCode() {
/*  250 */         return this.offset;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean equals(Object obj) {
/*  255 */         return (obj instanceof OffsetPathItem && this.offset == ((OffsetPathItem)obj).offset);
/*      */       }
/*      */ 
/*      */       
/*      */       protected Node toXmlNode(Document document) {
/*  260 */         Node element = document.createElement("offset");
/*  261 */         element.appendChild(document.createTextNode(String.valueOf(this.offset)));
/*  262 */         return element;
/*      */       }
/*      */     }
/*      */     
/*      */     public ObjectPath resetDirectPath(RefKey baseCmpObject, RefKey baseOutObject) {
/*  267 */       ObjectPath newPath = new ObjectPath(baseCmpObject, baseOutObject);
/*  268 */       newPath.indirects = (Stack<Pair<RefKey>>)this.indirects.clone();
/*  269 */       newPath.indirects.add(new Pair<RefKey>(baseCmpObject, baseOutObject));
/*  270 */       return newPath;
/*      */     }
/*      */     
/*      */     public boolean isComparing(RefKey baseCmpObject, RefKey baseOutObject) {
/*  274 */       return this.indirects.contains(new Pair<RefKey>(baseCmpObject, baseOutObject));
/*      */     }
/*      */     
/*      */     public void pushArrayItemToPath(int index) {
/*  278 */       this.path.add(new ArrayPathItem(index));
/*      */     }
/*      */     
/*      */     public void pushDictItemToPath(String key) {
/*  282 */       this.path.add(new DictPathItem(key));
/*      */     }
/*      */     
/*      */     public void pushOffsetToPath(int offset) {
/*  286 */       this.path.add(new OffsetPathItem(offset));
/*      */     }
/*      */     
/*      */     public void pop() {
/*  290 */       this.path.pop();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  295 */       StringBuilder sb = new StringBuilder();
/*  296 */       sb.append(String.format("Base cmp object: %s obj. Base out object: %s obj", new Object[] { this.baseCmpObject, this.baseOutObject }));
/*  297 */       for (PathItem pathItem : this.path) {
/*  298 */         sb.append("\n");
/*  299 */         sb.append(pathItem.toString());
/*      */       } 
/*  301 */       return sb.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  306 */       int hashCode1 = (this.baseCmpObject != null) ? this.baseCmpObject.hashCode() : 1;
/*  307 */       int hashCode2 = (this.baseOutObject != null) ? this.baseOutObject.hashCode() : 1;
/*  308 */       int hashCode = hashCode1 * 31 + hashCode2;
/*  309 */       for (PathItem pathItem : this.path) {
/*  310 */         hashCode *= 31;
/*  311 */         hashCode += pathItem.hashCode();
/*      */       } 
/*  313 */       return hashCode;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/*  318 */       return (obj instanceof ObjectPath && this.baseCmpObject.equals(((ObjectPath)obj).baseCmpObject) && this.baseOutObject.equals(((ObjectPath)obj).baseOutObject) && this.path
/*  319 */         .equals(((ObjectPath)obj).path));
/*      */     }
/*      */ 
/*      */     
/*      */     protected Object clone() {
/*  324 */       return new ObjectPath(this.baseCmpObject, this.baseOutObject, (Stack<PathItem>)this.path.clone());
/*      */     }
/*      */     
/*      */     public Node toXmlNode(Document document) {
/*  328 */       Element element = document.createElement("path");
/*  329 */       Element baseNode = document.createElement("base");
/*  330 */       baseNode.setAttribute("cmp", this.baseCmpObject.toString() + " obj");
/*  331 */       baseNode.setAttribute("out", this.baseOutObject.toString() + " obj");
/*  332 */       element.appendChild(baseNode);
/*  333 */       for (PathItem pathItem : this.path) {
/*  334 */         element.appendChild(pathItem.toXmlNode(document));
/*      */       }
/*  336 */       return element;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class CompareResult
/*      */   {
/*  342 */     protected Map<CompareTool.ObjectPath, String> differences = new LinkedHashMap<CompareTool.ObjectPath, String>();
/*  343 */     protected int messageLimit = 1;
/*      */     
/*      */     public CompareResult(int messageLimit) {
/*  346 */       this.messageLimit = messageLimit;
/*      */     }
/*      */     
/*      */     public boolean isOk() {
/*  350 */       return (this.differences.size() == 0);
/*      */     }
/*      */     
/*      */     public int getErrorCount() {
/*  354 */       return this.differences.size();
/*      */     }
/*      */     
/*      */     protected boolean isMessageLimitReached() {
/*  358 */       return (this.differences.size() >= this.messageLimit);
/*      */     }
/*      */     
/*      */     public String getReport() {
/*  362 */       StringBuilder sb = new StringBuilder();
/*  363 */       boolean firstEntry = true;
/*  364 */       for (Map.Entry<CompareTool.ObjectPath, String> entry : this.differences.entrySet()) {
/*  365 */         if (!firstEntry)
/*  366 */           sb.append("-----------------------------").append("\n"); 
/*  367 */         CompareTool.ObjectPath diffPath = entry.getKey();
/*  368 */         sb.append(entry.getValue()).append("\n").append(diffPath.toString()).append("\n");
/*  369 */         firstEntry = false;
/*      */       } 
/*  371 */       return sb.toString();
/*      */     }
/*      */     
/*      */     protected void addError(CompareTool.ObjectPath path, String message) {
/*  375 */       if (this.differences.size() < this.messageLimit) {
/*  376 */         this.differences.put((CompareTool.ObjectPath)path.clone(), message);
/*      */       }
/*      */     }
/*      */     
/*      */     public void writeReportToXml(OutputStream stream) throws ParserConfigurationException, TransformerException {
/*  381 */       Document xmlReport = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
/*  382 */       Element root = xmlReport.createElement("report");
/*  383 */       Element errors = xmlReport.createElement("errors");
/*  384 */       errors.setAttribute("count", String.valueOf(this.differences.size()));
/*  385 */       root.appendChild(errors);
/*  386 */       for (Map.Entry<CompareTool.ObjectPath, String> entry : this.differences.entrySet()) {
/*  387 */         Node errorNode = xmlReport.createElement("error");
/*  388 */         Node message = xmlReport.createElement("message");
/*  389 */         message.appendChild(xmlReport.createTextNode(entry.getValue()));
/*  390 */         Node path = ((CompareTool.ObjectPath)entry.getKey()).toXmlNode(xmlReport);
/*  391 */         errorNode.appendChild(message);
/*  392 */         errorNode.appendChild(path);
/*  393 */         errors.appendChild(errorNode);
/*      */       } 
/*  395 */       xmlReport.appendChild(root);
/*      */       
/*  397 */       TransformerFactory tFactory = TransformerFactory.newInstance();
/*      */       try {
/*  399 */         tFactory.setAttribute("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.valueOf(true));
/*  400 */       } catch (Exception exception) {}
/*  401 */       Transformer transformer = tFactory.newTransformer();
/*  402 */       transformer.setOutputProperty("indent", "yes");
/*  403 */       DOMSource source = new DOMSource(xmlReport);
/*  404 */       StreamResult result = new StreamResult(stream);
/*  405 */       transformer.transform(source, result);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  413 */   private static final Pattern pageListRegexp = Pattern.compile("^(\\d+,)*\\d+$");
/*      */   
/*      */   private static final String tempFilePrefix = "itext_gs_io_temp";
/*      */   
/*  417 */   private final String gsParams = " -dNOPAUSE -dBATCH -dSAFER -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>";
/*      */   
/*  419 */   private final String compareParams = " \"<image1>\" \"<image2>\" \"<difference>\"";
/*      */   
/*      */   private static final String cannotOpenTargetDirectory = "Cannot open target directory for <filename>.";
/*      */   
/*      */   private static final String gsFailed = "GhostScript failed for <filename>.";
/*      */   
/*      */   private static final String unexpectedNumberOfPages = "Unexpected number of pages for <filename>.";
/*      */   
/*      */   private static final String differentPages = "File <filename> differs on page <pagenumber>.";
/*      */   
/*      */   private static final String undefinedGsPath = "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs9.14/bin/gswin32c.exe\")";
/*      */   
/*      */   private static final String ignoredAreasPrefix = "ignored_areas_";
/*      */   private String cmpPdf;
/*      */   private String cmpPdfName;
/*      */   private String cmpImage;
/*      */   private String outPdf;
/*      */   private String outPdfName;
/*      */   private String outImage;
/*      */   List<PdfDictionary> outPages;
/*      */   List<RefKey> outPagesRef;
/*      */   List<PdfDictionary> cmpPages;
/*      */   List<RefKey> cmpPagesRef;
/*  442 */   private int compareByContentErrorsLimit = 1;
/*      */   private boolean generateCompareByContentXmlReport = false;
/*  444 */   private String xmlReportName = "report";
/*  445 */   private double floatComparisonError = 0.0D;
/*      */   
/*      */   private boolean absoluteError = true;
/*      */   
/*      */   public CompareTool() {
/*  450 */     this.gsExec = System.getProperty("gsExec");
/*  451 */     if (this.gsExec == null) {
/*  452 */       this.gsExec = System.getenv("gsExec");
/*      */     }
/*  454 */     this.compareExec = System.getProperty("compareExec");
/*  455 */     if (this.compareExec == null) {
/*  456 */       this.compareExec = System.getenv("compareExec");
/*      */     }
/*      */   }
/*      */   
/*      */   private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
/*  461 */     return compare(outPath, differenceImagePrefix, ignoredAreas, (List<Integer>)null);
/*      */   }
/*      */ 
/*      */   
/*      */   private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas, List<Integer> equalPages) throws IOException, InterruptedException, DocumentException {
/*  466 */     if (this.gsExec == null)
/*  467 */       return "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs9.14/bin/gswin32c.exe\")"; 
/*  468 */     if (!(new File(this.gsExec)).exists()) {
/*  469 */       return (new File(this.gsExec)).getAbsolutePath() + " does not exist";
/*      */     }
/*  471 */     if (!outPath.endsWith("/"))
/*  472 */       outPath = outPath + "/"; 
/*  473 */     File targetDir = new File(outPath);
/*      */ 
/*      */ 
/*      */     
/*  477 */     if (!targetDir.exists()) {
/*  478 */       targetDir.mkdirs();
/*      */     } else {
/*  480 */       File[] imageFiles = targetDir.listFiles(new PngFileFilter());
/*  481 */       for (File file : imageFiles) {
/*  482 */         file.delete();
/*      */       }
/*  484 */       File[] cmpImageFiles = targetDir.listFiles(new CmpPngFileFilter());
/*  485 */       for (File file : cmpImageFiles) {
/*  486 */         file.delete();
/*      */       }
/*      */     } 
/*  489 */     File diffFile = new File(outPath + differenceImagePrefix);
/*  490 */     if (diffFile.exists()) {
/*  491 */       diffFile.delete();
/*      */     }
/*      */     
/*  494 */     if (ignoredAreas != null && !ignoredAreas.isEmpty()) {
/*  495 */       PdfReader cmpReader = new PdfReader(this.cmpPdf);
/*  496 */       PdfReader outReader = new PdfReader(this.outPdf);
/*  497 */       PdfStamper outStamper = new PdfStamper(outReader, new FileOutputStream(outPath + "ignored_areas_" + this.outPdfName));
/*  498 */       PdfStamper cmpStamper = new PdfStamper(cmpReader, new FileOutputStream(outPath + "ignored_areas_" + this.cmpPdfName));
/*      */       
/*  500 */       for (Map.Entry<Integer, List<Rectangle>> entry : ignoredAreas.entrySet()) {
/*  501 */         int pageNumber = ((Integer)entry.getKey()).intValue();
/*  502 */         List<Rectangle> rectangles = entry.getValue();
/*      */         
/*  504 */         if (rectangles != null && !rectangles.isEmpty()) {
/*  505 */           PdfContentByte outCB = outStamper.getOverContent(pageNumber);
/*  506 */           PdfContentByte cmpCB = cmpStamper.getOverContent(pageNumber);
/*      */           
/*  508 */           for (Rectangle rect : rectangles) {
/*  509 */             rect.setBackgroundColor(BaseColor.BLACK);
/*  510 */             outCB.rectangle(rect);
/*  511 */             cmpCB.rectangle(rect);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  516 */       outStamper.close();
/*  517 */       cmpStamper.close();
/*      */       
/*  519 */       outReader.close();
/*  520 */       cmpReader.close();
/*      */       
/*  522 */       init(outPath + "ignored_areas_" + this.outPdfName, outPath + "ignored_areas_" + this.cmpPdfName);
/*      */     } 
/*      */     
/*  525 */     String cmpPdfTempCopy = null;
/*  526 */     String replacementImagesDirectory = null;
/*  527 */     String outPdfTempCopy = null;
/*  528 */     if (targetDir.exists()) {
/*  529 */       replacementImagesDirectory = CompareToolUtil.createTempDirectory("itext_gs_io_temp");
/*  530 */       cmpPdfTempCopy = CompareToolUtil.createTempCopy(this.cmpPdf, "itext_gs_io_temp", null);
/*  531 */       outPdfTempCopy = CompareToolUtil.createTempCopy(this.outPdf, "itext_gs_io_temp", null);
/*  532 */       int exitValue = runGhostscriptAndGetExitCode(cmpPdfTempCopy, CompareToolUtil.buildPath(replacementImagesDirectory, new String[] { "cmp_itext_gs_io_temp%03d.png" }));
/*      */ 
/*      */       
/*  535 */       if (exitValue == 0) {
/*  536 */         exitValue = runGhostscriptAndGetExitCode(outPdfTempCopy, CompareToolUtil.buildPath(replacementImagesDirectory, new String[] { "itext_gs_io_temp%03d.png" }));
/*      */         
/*  538 */         if (exitValue == 0) {
/*  539 */           File tempTargetDir = new File(replacementImagesDirectory);
/*  540 */           File[] imageFiles = tempTargetDir.listFiles(new PngFileFilter());
/*  541 */           File[] cmpImageFiles = tempTargetDir.listFiles(new CmpPngFileFilter());
/*  542 */           boolean bUnexpectedNumberOfPages = false;
/*  543 */           if (imageFiles.length != cmpImageFiles.length) {
/*  544 */             bUnexpectedNumberOfPages = true;
/*      */           }
/*  546 */           int cnt = Math.min(imageFiles.length, cmpImageFiles.length);
/*  547 */           if (cnt < 1) {
/*  548 */             return "No files for comparing!!!\nThe result or sample pdf file is not processed by GhostScript.";
/*      */           }
/*  550 */           Arrays.sort(imageFiles, new ImageNameComparator());
/*  551 */           Arrays.sort(cmpImageFiles, new ImageNameComparator());
/*  552 */           String differentPagesFail = null;
/*  553 */           for (int i = 0; i < cnt; i++) {
/*  554 */             CompareToolUtil.copy(imageFiles[i].getAbsolutePath(), CompareToolUtil.buildPath(targetDir.getAbsolutePath(), new String[] { this.outPdfName + "-" + (i + 1) + "." + "png" }));
/*      */             
/*  556 */             CompareToolUtil.copy(cmpImageFiles[i].getAbsolutePath(), CompareToolUtil.buildPath(targetDir.getAbsolutePath(), new String[] { "cmp_" + this.outPdfName + "-" + (i + 1) + "." + "png" }));
/*      */             
/*  558 */             if (equalPages == null || !equalPages.contains(Integer.valueOf(i))) {
/*      */               
/*  560 */               System.out.print("Comparing page " + Integer.toString(i + 1) + " (" + imageFiles[i].getAbsolutePath() + ")...");
/*  561 */               FileInputStream is1 = new FileInputStream(imageFiles[i]);
/*  562 */               FileInputStream is2 = new FileInputStream(cmpImageFiles[i]);
/*  563 */               boolean cmpResult = compareStreams(is1, is2);
/*  564 */               is1.close();
/*  565 */               is2.close();
/*  566 */               if (!cmpResult) {
/*  567 */                 if (this.compareExec != null) {
/*  568 */                   getClass();
/*  569 */                   String compareParams = " \"<image1>\" \"<image2>\" \"<difference>\"".replace("<image1>", imageFiles[i].getAbsolutePath()).replace("<image2>", cmpImageFiles[i].getAbsolutePath()).replace("<difference>", 
/*  570 */                       CompareToolUtil.buildPath(replacementImagesDirectory, new String[] { "diff" + (i + 1) + "." + "png" }));
/*      */ 
/*      */                   
/*  573 */                   Process p = CompareToolUtil.runProcess(this.compareExec, compareParams);
/*  574 */                   BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream())); String line;
/*  575 */                   while ((line = bre.readLine()) != null) {
/*  576 */                     System.out.println(line);
/*      */                   }
/*  578 */                   bre.close();
/*  579 */                   int cmpExitValue = p.waitFor();
/*      */                   
/*  581 */                   if (cmpExitValue == 0) {
/*  582 */                     if (differentPagesFail == null) {
/*  583 */                       differentPagesFail = "File <filename> differs on page <pagenumber>.".replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1));
/*  584 */                       differentPagesFail = differentPagesFail + "\nPlease, examine " + outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png for more details.";
/*      */                     } else {
/*  586 */                       differentPagesFail = "File " + this.outPdf + " differs.\nPlease, examine difference images for more details.";
/*      */                     } 
/*      */                   } else {
/*      */                     
/*  590 */                     differentPagesFail = "File <filename> differs on page <pagenumber>.".replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1));
/*      */                   } 
/*      */                 } else {
/*  593 */                   differentPagesFail = "File <filename> differs on page <pagenumber>.".replace("<filename>", this.outPdf).replace("<pagenumber>", Integer.toString(i + 1));
/*  594 */                   differentPagesFail = differentPagesFail + "\nYou can optionally specify path to ImageMagick compare tool (e.g. -DcompareExec=\"C:/Program Files/ImageMagick-6.5.4-2/compare.exe\") to visualize differences.";
/*      */                   break;
/*      */                 } 
/*  597 */                 System.out.println(differentPagesFail);
/*      */               } else {
/*  599 */                 System.out.println("done.");
/*      */               } 
/*  601 */               CompareToolUtil.removeFiles(new String[] { imageFiles[i].getAbsolutePath(), cmpImageFiles[i].getAbsolutePath() });
/*      */             } 
/*  603 */           }  File[] diffFiles = tempTargetDir.listFiles();
/*  604 */           for (int j = 0; j < diffFiles.length; j++) {
/*  605 */             System.out.println(targetDir.getAbsolutePath());
/*  606 */             CompareToolUtil.copy(diffFiles[j].getAbsolutePath(), CompareToolUtil.buildPath(targetDir.getAbsolutePath(), new String[] { differenceImagePrefix + "-" + (j + 1) + "." + "png" }));
/*      */             
/*  608 */             diffFiles[j].delete();
/*      */           } 
/*  610 */           tempTargetDir.delete();
/*  611 */           if (differentPagesFail != null) {
/*  612 */             return differentPagesFail;
/*      */           }
/*  614 */           if (bUnexpectedNumberOfPages) {
/*  615 */             return "Unexpected number of pages for <filename>.".replace("<filename>", this.outPdf);
/*      */           }
/*      */         } else {
/*  618 */           return "GhostScript failed for <filename>.".replace("<filename>", this.outPdf);
/*      */         } 
/*      */       } else {
/*  621 */         return "GhostScript failed for <filename>.".replace("<filename>", this.cmpPdf);
/*      */       } 
/*      */     } else {
/*  624 */       return "Cannot open target directory for <filename>.".replace("<filename>", this.outPdf);
/*      */     } 
/*  626 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private int runGhostscriptAndGetExitCode(String replacementPdf, String replacementImagesDirectory) throws IOException, InterruptedException {
/*  631 */     getClass();
/*  632 */     String gsParams = " -dNOPAUSE -dBATCH -dSAFER -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>".replace("<outputfile>", replacementImagesDirectory).replace("<inputfile>", replacementPdf);
/*  633 */     Process p = CompareToolUtil.runProcess(this.gsExec, gsParams);
/*  634 */     BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*  635 */     BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
/*      */     String line;
/*  637 */     while ((line = bri.readLine()) != null) {
/*  638 */       System.out.println(line);
/*      */     }
/*  640 */     bri.close();
/*  641 */     while ((line = bre.readLine()) != null) {
/*  642 */       System.out.println(line);
/*      */     }
/*  644 */     bre.close();
/*  645 */     int exitValue = p.waitFor();
/*  646 */     return exitValue;
/*      */   }
/*      */   
/*      */   public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
/*  650 */     init(outPdf, cmpPdf);
/*  651 */     return compare(outPath, differenceImagePrefix, ignoredAreas);
/*      */   }
/*      */   
/*      */   public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws IOException, InterruptedException, DocumentException {
/*  655 */     return compare(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareTool setCompareByContentErrorsLimit(int compareByContentMaxErrorCount) {
/*  664 */     this.compareByContentErrorsLimit = compareByContentMaxErrorCount;
/*  665 */     return this;
/*      */   }
/*      */   
/*      */   public void setGenerateCompareByContentXmlReport(boolean generateCompareByContentXmlReport) {
/*  669 */     this.generateCompareByContentXmlReport = generateCompareByContentXmlReport;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareTool setFloatAbsoluteError(float error) {
/*  678 */     this.floatComparisonError = error;
/*  679 */     this.absoluteError = true;
/*  680 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompareTool setFloatRelativeError(float error) {
/*  689 */     this.floatComparisonError = error;
/*  690 */     this.absoluteError = false;
/*  691 */     return this;
/*      */   }
/*      */   
/*      */   public String getXmlReportName() {
/*  695 */     return this.xmlReportName;
/*      */   }
/*      */   
/*      */   public void setXmlReportName(String xmlReportName) {
/*  699 */     this.xmlReportName = xmlReportName;
/*      */   }
/*      */   
/*      */   protected String compareByContent(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
/*  703 */     System.out.print("[itext] INFO  Comparing by content..........");
/*  704 */     PdfReader outReader = new PdfReader(this.outPdf);
/*  705 */     this.outPages = new ArrayList<PdfDictionary>();
/*  706 */     this.outPagesRef = new ArrayList<RefKey>();
/*  707 */     loadPagesFromReader(outReader, this.outPages, this.outPagesRef);
/*      */     
/*  709 */     PdfReader cmpReader = new PdfReader(this.cmpPdf);
/*  710 */     this.cmpPages = new ArrayList<PdfDictionary>();
/*  711 */     this.cmpPagesRef = new ArrayList<RefKey>();
/*  712 */     loadPagesFromReader(cmpReader, this.cmpPages, this.cmpPagesRef);
/*      */     
/*  714 */     if (this.outPages.size() != this.cmpPages.size()) {
/*  715 */       return compare(outPath, differenceImagePrefix, ignoredAreas);
/*      */     }
/*  717 */     CompareResult compareResult = new CompareResult(this.compareByContentErrorsLimit);
/*  718 */     List<Integer> equalPages = new ArrayList<Integer>(this.cmpPages.size());
/*  719 */     for (int i = 0; i < this.cmpPages.size(); i++) {
/*  720 */       ObjectPath currentPath = new ObjectPath(this.cmpPagesRef.get(i), this.outPagesRef.get(i));
/*  721 */       if (compareDictionariesExtended(this.outPages.get(i), this.cmpPages.get(i), currentPath, compareResult)) {
/*  722 */         equalPages.add(Integer.valueOf(i));
/*      */       }
/*      */     } 
/*  725 */     PdfObject outStructTree = outReader.getCatalog().get(PdfName.STRUCTTREEROOT);
/*  726 */     PdfObject cmpStructTree = cmpReader.getCatalog().get(PdfName.STRUCTTREEROOT);
/*  727 */     RefKey outStructTreeRef = (outStructTree == null) ? null : new RefKey((PdfIndirectReference)outStructTree);
/*  728 */     RefKey cmpStructTreeRef = (cmpStructTree == null) ? null : new RefKey((PdfIndirectReference)cmpStructTree);
/*  729 */     compareObjects(outStructTree, cmpStructTree, new ObjectPath(outStructTreeRef, cmpStructTreeRef), compareResult);
/*      */     
/*  731 */     PdfObject outOcProperties = outReader.getCatalog().get(PdfName.OCPROPERTIES);
/*  732 */     PdfObject cmpOcProperties = cmpReader.getCatalog().get(PdfName.OCPROPERTIES);
/*  733 */     RefKey outOcPropertiesRef = (outOcProperties instanceof PdfIndirectReference) ? new RefKey((PdfIndirectReference)outOcProperties) : null;
/*  734 */     RefKey cmpOcPropertiesRef = (cmpOcProperties instanceof PdfIndirectReference) ? new RefKey((PdfIndirectReference)cmpOcProperties) : null;
/*  735 */     compareObjects(outOcProperties, cmpOcProperties, new ObjectPath(outOcPropertiesRef, cmpOcPropertiesRef), compareResult);
/*      */     
/*  737 */     outReader.close();
/*  738 */     cmpReader.close();
/*      */     
/*  740 */     if (this.generateCompareByContentXmlReport) {
/*      */       try {
/*  742 */         compareResult.writeReportToXml(new FileOutputStream(outPath + "/" + this.xmlReportName + ".xml"));
/*  743 */       } catch (Exception exception) {}
/*      */     }
/*      */     
/*  746 */     if (equalPages.size() == this.cmpPages.size() && compareResult.isOk()) {
/*  747 */       System.out.println("OK");
/*  748 */       System.out.flush();
/*  749 */       return null;
/*      */     } 
/*  751 */     System.out.println("Fail");
/*  752 */     System.out.flush();
/*  753 */     String compareByContentReport = "Compare by content report:\n" + compareResult.getReport();
/*  754 */     System.out.println(compareByContentReport);
/*  755 */     System.out.flush();
/*  756 */     String message = compare(outPath, differenceImagePrefix, ignoredAreas, equalPages);
/*  757 */     if (message == null || message.length() == 0)
/*  758 */       return "Compare by content fails. No visual differences"; 
/*  759 */     return message;
/*      */   }
/*      */ 
/*      */   
/*      */   public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
/*  764 */     init(outPdf, cmpPdf);
/*  765 */     return compareByContent(outPath, differenceImagePrefix, ignoredAreas);
/*      */   }
/*      */   
/*      */   public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws DocumentException, InterruptedException, IOException {
/*  769 */     return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
/*      */   }
/*      */   
/*      */   private void loadPagesFromReader(PdfReader reader, List<PdfDictionary> pages, List<RefKey> pagesRef) {
/*  773 */     PdfObject pagesDict = reader.getCatalog().get(PdfName.PAGES);
/*  774 */     addPagesFromDict(pagesDict, pages, pagesRef);
/*      */   }
/*      */   
/*      */   private void addPagesFromDict(PdfObject dictRef, List<PdfDictionary> pages, List<RefKey> pagesRef) {
/*  778 */     PdfDictionary dict = (PdfDictionary)PdfReader.getPdfObject(dictRef);
/*  779 */     if (dict.isPages()) {
/*  780 */       PdfArray kids = dict.getAsArray(PdfName.KIDS);
/*  781 */       if (kids == null)
/*  782 */         return;  for (PdfObject kid : kids) {
/*  783 */         addPagesFromDict(kid, pages, pagesRef);
/*      */       }
/*  785 */     } else if (dict.isPage()) {
/*  786 */       pages.add(dict);
/*  787 */       pagesRef.add(new RefKey((PdfIndirectReference)dictRef));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean compareObjects(PdfObject outObj, PdfObject cmpObj, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/*  792 */     PdfObject outDirectObj = PdfReader.getPdfObject(outObj);
/*  793 */     PdfObject cmpDirectObj = PdfReader.getPdfObject(cmpObj);
/*      */     
/*  795 */     if (cmpDirectObj == null && outDirectObj == null) {
/*  796 */       return true;
/*      */     }
/*  798 */     if (outDirectObj == null) {
/*  799 */       compareResult.addError(currentPath, "Expected object was not found.");
/*  800 */       return false;
/*  801 */     }  if (cmpDirectObj == null) {
/*  802 */       compareResult.addError(currentPath, "Found object which was not expected to be found.");
/*  803 */       return false;
/*  804 */     }  if (cmpDirectObj.type() != outDirectObj.type()) {
/*  805 */       compareResult.addError(currentPath, String.format("Types do not match. Expected: %s. Found: %s.", new Object[] { cmpDirectObj.getClass().getSimpleName(), outDirectObj.getClass().getSimpleName() }));
/*  806 */       return false;
/*      */     } 
/*      */     
/*  809 */     if (cmpObj.isIndirect() && outObj.isIndirect()) {
/*  810 */       if (currentPath.isComparing(new RefKey((PdfIndirectReference)cmpObj), new RefKey((PdfIndirectReference)outObj)))
/*  811 */         return true; 
/*  812 */       currentPath = currentPath.resetDirectPath(new RefKey((PdfIndirectReference)cmpObj), new RefKey((PdfIndirectReference)outObj));
/*      */     } 
/*      */     
/*  815 */     if (cmpDirectObj.isDictionary() && ((PdfDictionary)cmpDirectObj).isPage()) {
/*  816 */       if (!outDirectObj.isDictionary() || !((PdfDictionary)outDirectObj).isPage()) {
/*  817 */         if (compareResult != null && currentPath != null)
/*  818 */           compareResult.addError(currentPath, "Expected a page. Found not a page."); 
/*  819 */         return false;
/*      */       } 
/*  821 */       RefKey cmpRefKey = new RefKey((PdfIndirectReference)cmpObj);
/*  822 */       RefKey outRefKey = new RefKey((PdfIndirectReference)outObj);
/*      */       
/*  824 */       if (this.cmpPagesRef.contains(cmpRefKey) && this.cmpPagesRef.indexOf(cmpRefKey) == this.outPagesRef.indexOf(outRefKey))
/*  825 */         return true; 
/*  826 */       if (compareResult != null && currentPath != null)
/*  827 */         compareResult.addError(currentPath, String.format("The dictionaries refer to different pages. Expected page number: %s. Found: %s", new Object[] {
/*  828 */                 Integer.valueOf(this.cmpPagesRef.indexOf(cmpRefKey)), Integer.valueOf(this.outPagesRef.indexOf(outRefKey)) })); 
/*  829 */       return false;
/*      */     } 
/*      */     
/*  832 */     if (cmpDirectObj.isDictionary()) {
/*  833 */       if (!compareDictionariesExtended((PdfDictionary)outDirectObj, (PdfDictionary)cmpDirectObj, currentPath, compareResult))
/*  834 */         return false; 
/*  835 */     } else if (cmpDirectObj.isStream()) {
/*  836 */       if (!compareStreamsExtended((PRStream)outDirectObj, (PRStream)cmpDirectObj, currentPath, compareResult))
/*  837 */         return false; 
/*  838 */     } else if (cmpDirectObj.isArray()) {
/*  839 */       if (!compareArraysExtended((PdfArray)outDirectObj, (PdfArray)cmpDirectObj, currentPath, compareResult))
/*  840 */         return false; 
/*  841 */     } else if (cmpDirectObj.isName()) {
/*  842 */       if (!compareNamesExtended((PdfName)outDirectObj, (PdfName)cmpDirectObj, currentPath, compareResult))
/*  843 */         return false; 
/*  844 */     } else if (cmpDirectObj.isNumber()) {
/*  845 */       if (!compareNumbersExtended((PdfNumber)outDirectObj, (PdfNumber)cmpDirectObj, currentPath, compareResult))
/*  846 */         return false; 
/*  847 */     } else if (cmpDirectObj.isString()) {
/*  848 */       if (!compareStringsExtended((PdfString)outDirectObj, (PdfString)cmpDirectObj, currentPath, compareResult))
/*  849 */         return false; 
/*  850 */     } else if (cmpDirectObj.isBoolean()) {
/*  851 */       if (!compareBooleansExtended((PdfBoolean)outDirectObj, (PdfBoolean)cmpDirectObj, currentPath, compareResult))
/*  852 */         return false; 
/*  853 */     } else if (cmpDirectObj instanceof PdfLiteral) {
/*  854 */       if (!compareLiteralsExtended((PdfLiteral)outDirectObj, (PdfLiteral)cmpDirectObj, currentPath, compareResult))
/*  855 */         return false; 
/*  856 */     } else if (!outDirectObj.isNull() || !cmpDirectObj.isNull()) {
/*      */       
/*  858 */       throw new UnsupportedOperationException();
/*      */     } 
/*  860 */     return true;
/*      */   }
/*      */   
/*      */   public boolean compareDictionaries(PdfDictionary outDict, PdfDictionary cmpDict) throws IOException {
/*  864 */     return compareDictionariesExtended(outDict, cmpDict, null, null);
/*      */   }
/*      */   
/*      */   private boolean compareDictionariesExtended(PdfDictionary outDict, PdfDictionary cmpDict, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/*  868 */     if ((cmpDict != null && outDict == null) || (outDict != null && cmpDict == null)) {
/*  869 */       compareResult.addError(currentPath, "One of the dictionaries is null, the other is not.");
/*  870 */       return false;
/*      */     } 
/*  872 */     boolean dictsAreSame = true;
/*      */     
/*  874 */     Set<PdfName> mergedKeys = new TreeSet<PdfName>(cmpDict.getKeys());
/*  875 */     mergedKeys.addAll(outDict.getKeys());
/*      */     
/*  877 */     for (PdfName key : mergedKeys) {
/*  878 */       if (key.compareTo(PdfName.PARENT) == 0 || key.compareTo(PdfName.P) == 0 || (
/*  879 */         outDict.isStream() && cmpDict.isStream() && (key.equals(PdfName.FILTER) || key.equals(PdfName.LENGTH))))
/*  880 */         continue;  if (key.compareTo(PdfName.BASEFONT) == 0 || key.compareTo(PdfName.FONTNAME) == 0) {
/*  881 */         PdfObject cmpObj = cmpDict.getDirectObject(key);
/*  882 */         if (cmpObj.isName() && cmpObj.toString().indexOf('+') > 0) {
/*  883 */           PdfObject outObj = outDict.getDirectObject(key);
/*  884 */           if (!outObj.isName() || outObj.toString().indexOf('+') == -1) {
/*  885 */             if (compareResult != null && currentPath != null)
/*  886 */               compareResult.addError(currentPath, String.format("PdfDictionary %s entry: Expected: %s. Found: %s", new Object[] { key.toString(), cmpObj.toString(), outObj.toString() })); 
/*  887 */             dictsAreSame = false;
/*      */           } 
/*  889 */           String cmpName = cmpObj.toString().substring(cmpObj.toString().indexOf('+'));
/*  890 */           String outName = outObj.toString().substring(outObj.toString().indexOf('+'));
/*  891 */           if (!cmpName.equals(outName)) {
/*  892 */             if (compareResult != null && currentPath != null)
/*  893 */               compareResult.addError(currentPath, String.format("PdfDictionary %s entry: Expected: %s. Found: %s", new Object[] { key.toString(), cmpObj.toString(), outObj.toString() })); 
/*  894 */             dictsAreSame = false;
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/*      */       } 
/*  900 */       if (this.floatComparisonError != 0.0D && cmpDict.isPage() && outDict.isPage() && key.equals(PdfName.CONTENTS)) {
/*  901 */         if (!compareContentStreamsByParsingExtended(outDict.getDirectObject(key), cmpDict.getDirectObject(key), (PdfDictionary)outDict
/*  902 */             .getDirectObject(PdfName.RESOURCES), (PdfDictionary)cmpDict.getDirectObject(PdfName.RESOURCES), currentPath, compareResult))
/*      */         {
/*  904 */           dictsAreSame = false;
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*  909 */       if (currentPath != null)
/*  910 */         currentPath.pushDictItemToPath(key.toString()); 
/*  911 */       dictsAreSame = (compareObjects(outDict.get(key), cmpDict.get(key), currentPath, compareResult) && dictsAreSame);
/*  912 */       if (currentPath != null)
/*  913 */         currentPath.pop(); 
/*  914 */       if (!dictsAreSame && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached()))
/*  915 */         return false; 
/*      */     } 
/*  917 */     return dictsAreSame;
/*      */   }
/*      */   
/*      */   public boolean compareContentStreamsByParsing(PdfObject outObj, PdfObject cmpObj) throws IOException {
/*  921 */     return compareContentStreamsByParsingExtended(outObj, cmpObj, null, null, null, null);
/*      */   }
/*      */   
/*      */   public boolean compareContentStreamsByParsing(PdfObject outObj, PdfObject cmpObj, PdfDictionary outResources, PdfDictionary cmpResources) throws IOException {
/*  925 */     return compareContentStreamsByParsingExtended(outObj, cmpObj, outResources, cmpResources, null, null);
/*      */   }
/*      */   
/*      */   private boolean compareContentStreamsByParsingExtended(PdfObject outObj, PdfObject cmpObj, PdfDictionary outResources, PdfDictionary cmpResources, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/*  929 */     if (outObj.type() != outObj.type()) {
/*  930 */       compareResult.addError(currentPath, String.format("PdfObject. Types are different. Expected: %s. Found: %s", new Object[] {
/*  931 */               Integer.valueOf(cmpObj.type()), Integer.valueOf(outObj.type()) }));
/*  932 */       return false;
/*      */     } 
/*      */     
/*  935 */     if (outObj.isArray()) {
/*  936 */       PdfArray outArr = (PdfArray)outObj;
/*  937 */       PdfArray cmpArr = (PdfArray)cmpObj;
/*  938 */       if (cmpArr.size() != outArr.size()) {
/*  939 */         compareResult.addError(currentPath, String.format("PdfArray. Sizes are different. Expected: %s. Found: %s", new Object[] {
/*  940 */                 Integer.valueOf(cmpArr.size()), Integer.valueOf(outArr.size()) }));
/*  941 */         return false;
/*      */       } 
/*  943 */       for (int i = 0; i < cmpArr.size(); i++) {
/*  944 */         if (!compareContentStreamsByParsingExtended(outArr.getPdfObject(i), cmpArr.getPdfObject(i), outResources, cmpResources, currentPath, compareResult)) {
/*  945 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  951 */     PRTokeniser cmpTokeniser = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(ContentByteUtils.getContentBytesFromContentObject(cmpObj))));
/*      */     
/*  953 */     PRTokeniser outTokeniser = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(ContentByteUtils.getContentBytesFromContentObject(outObj))));
/*      */     
/*  955 */     PdfContentParser cmpPs = new PdfContentParser(cmpTokeniser);
/*  956 */     PdfContentParser outPs = new PdfContentParser(outTokeniser);
/*      */     
/*  958 */     ArrayList<PdfObject> cmpOperands = new ArrayList<PdfObject>();
/*  959 */     ArrayList<PdfObject> outOperands = new ArrayList<PdfObject>();
/*      */     
/*  961 */     while (cmpPs.parse(cmpOperands).size() > 0) {
/*  962 */       outPs.parse(outOperands);
/*  963 */       if (cmpOperands.size() != outOperands.size()) {
/*  964 */         compareResult.addError(currentPath, String.format("PdfObject. Different commands lengths. Expected: %s. Found: %s", new Object[] {
/*  965 */                 Integer.valueOf(cmpOperands.size()), Integer.valueOf(outOperands.size()) }));
/*  966 */         return false;
/*      */       } 
/*  968 */       if (cmpOperands.size() == 1 && compareLiterals((PdfLiteral)cmpOperands.get(0), new PdfLiteral("BI")) && compareLiterals((PdfLiteral)outOperands.get(0), new PdfLiteral("BI"))) {
/*  969 */         PRStream cmpStr = (PRStream)cmpObj;
/*  970 */         PRStream outStr = (PRStream)outObj;
/*  971 */         if (null != outStr.getDirectObject(PdfName.RESOURCES) && null != cmpStr.getDirectObject(PdfName.RESOURCES)) {
/*  972 */           outResources = (PdfDictionary)outStr.getDirectObject(PdfName.RESOURCES);
/*  973 */           cmpResources = (PdfDictionary)cmpStr.getDirectObject(PdfName.RESOURCES);
/*      */         } 
/*  975 */         if (!compareInlineImagesExtended(outPs, cmpPs, outResources, cmpResources, currentPath, compareResult)) {
/*  976 */           return false;
/*      */         }
/*      */         continue;
/*      */       } 
/*  980 */       for (int i = 0; i < cmpOperands.size(); i++) {
/*  981 */         if (!compareObjects(outOperands.get(i), cmpOperands.get(i), currentPath, compareResult)) {
/*  982 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  986 */     return true;
/*      */   }
/*      */   
/*      */   private boolean compareInlineImagesExtended(PdfContentParser outPs, PdfContentParser cmpPs, PdfDictionary outDict, PdfDictionary cmpDict, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/*  990 */     InlineImageInfo cmpInfo = InlineImageUtils.parseInlineImage(cmpPs, cmpDict);
/*  991 */     InlineImageInfo outInfo = InlineImageUtils.parseInlineImage(outPs, outDict);
/*  992 */     return (compareObjects((PdfObject)outInfo.getImageDictionary(), (PdfObject)cmpInfo.getImageDictionary(), currentPath, compareResult) && 
/*  993 */       Arrays.equals(outInfo.getSamples(), cmpInfo.getSamples()));
/*      */   }
/*      */   
/*      */   public boolean compareStreams(PRStream outStream, PRStream cmpStream) throws IOException {
/*  997 */     return compareStreamsExtended(outStream, cmpStream, null, null);
/*      */   }
/*      */   
/*      */   private boolean compareStreamsExtended(PRStream outStream, PRStream cmpStream, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/* 1001 */     boolean decodeStreams = PdfName.FLATEDECODE.equals(outStream.get(PdfName.FILTER));
/* 1002 */     byte[] outStreamBytes = PdfReader.getStreamBytesRaw(outStream);
/* 1003 */     byte[] cmpStreamBytes = PdfReader.getStreamBytesRaw(cmpStream);
/* 1004 */     if (decodeStreams) {
/* 1005 */       outStreamBytes = PdfReader.decodeBytes(outStreamBytes, (PdfDictionary)outStream);
/* 1006 */       cmpStreamBytes = PdfReader.decodeBytes(cmpStreamBytes, (PdfDictionary)cmpStream);
/*      */     } 
/* 1008 */     if (this.floatComparisonError != 0.0D && PdfName.XOBJECT
/* 1009 */       .equals(cmpStream.getDirectObject(PdfName.TYPE)) && PdfName.XOBJECT
/* 1010 */       .equals(outStream.getDirectObject(PdfName.TYPE)) && PdfName.FORM
/* 1011 */       .equals(cmpStream.getDirectObject(PdfName.SUBTYPE)) && PdfName.FORM
/* 1012 */       .equals(outStream.getDirectObject(PdfName.SUBTYPE))) {
/* 1013 */       return (compareContentStreamsByParsingExtended((PdfObject)outStream, (PdfObject)cmpStream, outStream.getAsDict(PdfName.RESOURCES), cmpStream.getAsDict(PdfName.RESOURCES), currentPath, compareResult) && 
/* 1014 */         compareDictionariesExtended((PdfDictionary)outStream, (PdfDictionary)cmpStream, currentPath, compareResult));
/*      */     }
/* 1016 */     if (Arrays.equals(outStreamBytes, cmpStreamBytes)) {
/* 1017 */       return compareDictionariesExtended((PdfDictionary)outStream, (PdfDictionary)cmpStream, currentPath, compareResult);
/*      */     }
/* 1019 */     if (cmpStreamBytes.length != outStreamBytes.length) {
/* 1020 */       if (compareResult != null && currentPath != null) {
/* 1021 */         compareResult.addError(currentPath, String.format("PRStream. Lengths are different. Expected: %s. Found: %s", new Object[] { Integer.valueOf(cmpStreamBytes.length), Integer.valueOf(outStreamBytes.length) }));
/*      */       }
/*      */     } else {
/* 1024 */       for (int i = 0; i < cmpStreamBytes.length; i++) {
/* 1025 */         if (cmpStreamBytes[i] != outStreamBytes[i]) {
/* 1026 */           int l = Math.max(0, i - 10);
/* 1027 */           int r = Math.min(cmpStreamBytes.length, i + 10);
/* 1028 */           if (compareResult != null && currentPath != null) {
/* 1029 */             currentPath.pushOffsetToPath(i);
/* 1030 */             compareResult.addError(currentPath, String.format("PRStream. The bytes differ at index %s. Expected: %s (%s). Found: %s (%s)", new Object[] {
/* 1031 */                     Integer.valueOf(i), new String(new byte[] { cmpStreamBytes[i] }), (new String(cmpStreamBytes, l, r - l)).replaceAll("\\n", "\\\\n"), new String(new byte[] { outStreamBytes[i] }), (new String(outStreamBytes, l, r - l))
/* 1032 */                     .replaceAll("\\n", "\\\\n") }));
/* 1033 */             currentPath.pop();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1038 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean compareArrays(PdfArray outArray, PdfArray cmpArray) throws IOException {
/* 1044 */     return compareArraysExtended(outArray, cmpArray, null, null);
/*      */   }
/*      */   
/*      */   private boolean compareArraysExtended(PdfArray outArray, PdfArray cmpArray, ObjectPath currentPath, CompareResult compareResult) throws IOException {
/* 1048 */     if (outArray == null) {
/* 1049 */       if (compareResult != null && currentPath != null)
/* 1050 */         compareResult.addError(currentPath, "Found null. Expected PdfArray."); 
/* 1051 */       return false;
/* 1052 */     }  if (outArray.size() != cmpArray.size()) {
/* 1053 */       if (compareResult != null && currentPath != null)
/* 1054 */         compareResult.addError(currentPath, String.format("PdfArrays. Lengths are different. Expected: %s. Found: %s.", new Object[] { Integer.valueOf(cmpArray.size()), Integer.valueOf(outArray.size()) })); 
/* 1055 */       return false;
/*      */     } 
/* 1057 */     boolean arraysAreEqual = true;
/* 1058 */     for (int i = 0; i < cmpArray.size(); i++) {
/* 1059 */       if (currentPath != null)
/* 1060 */         currentPath.pushArrayItemToPath(i); 
/* 1061 */       arraysAreEqual = (compareObjects(outArray.getPdfObject(i), cmpArray.getPdfObject(i), currentPath, compareResult) && arraysAreEqual);
/* 1062 */       if (currentPath != null)
/* 1063 */         currentPath.pop(); 
/* 1064 */       if (!arraysAreEqual && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached())) {
/* 1065 */         return false;
/*      */       }
/*      */     } 
/* 1068 */     return arraysAreEqual;
/*      */   }
/*      */   
/*      */   public boolean compareNames(PdfName outName, PdfName cmpName) {
/* 1072 */     return (cmpName.compareTo(outName) == 0);
/*      */   }
/*      */   
/*      */   private boolean compareNamesExtended(PdfName outName, PdfName cmpName, ObjectPath currentPath, CompareResult compareResult) {
/* 1076 */     if (cmpName.compareTo(outName) == 0) {
/* 1077 */       return true;
/*      */     }
/* 1079 */     if (compareResult != null && currentPath != null)
/* 1080 */       compareResult.addError(currentPath, String.format("PdfName. Expected: %s. Found: %s", new Object[] { cmpName.toString(), outName.toString() })); 
/* 1081 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean compareNumbers(PdfNumber outNumber, PdfNumber cmpNumber) {
/* 1086 */     double difference = Math.abs(outNumber.doubleValue() - cmpNumber.doubleValue());
/* 1087 */     if (!this.absoluteError && cmpNumber.doubleValue() != 0.0D) {
/* 1088 */       difference /= cmpNumber.doubleValue();
/*      */     }
/* 1090 */     return (difference <= this.floatComparisonError);
/*      */   }
/*      */   
/*      */   private boolean compareNumbersExtended(PdfNumber outNumber, PdfNumber cmpNumber, ObjectPath currentPath, CompareResult compareResult) {
/* 1094 */     if (compareNumbers(outNumber, cmpNumber)) {
/* 1095 */       return true;
/*      */     }
/* 1097 */     if (compareResult != null && currentPath != null)
/* 1098 */       compareResult.addError(currentPath, String.format("PdfNumber. Expected: %s. Found: %s", new Object[] { cmpNumber, outNumber })); 
/* 1099 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean compareStrings(PdfString outString, PdfString cmpString) {
/* 1104 */     return Arrays.equals(cmpString.getBytes(), outString.getBytes());
/*      */   }
/*      */   
/*      */   private boolean compareStringsExtended(PdfString outString, PdfString cmpString, ObjectPath currentPath, CompareResult compareResult) {
/* 1108 */     if (Arrays.equals(cmpString.getBytes(), outString.getBytes())) {
/* 1109 */       return true;
/*      */     }
/* 1111 */     String cmpStr = cmpString.toUnicodeString();
/* 1112 */     String outStr = outString.toUnicodeString();
/* 1113 */     if (cmpStr.length() != outStr.length()) {
/* 1114 */       if (compareResult != null && currentPath != null)
/* 1115 */         compareResult.addError(currentPath, String.format("PdfString. Lengths are different. Expected: %s. Found: %s", new Object[] { Integer.valueOf(cmpStr.length()), Integer.valueOf(outStr.length()) })); 
/*      */     } else {
/* 1117 */       for (int i = 0; i < cmpStr.length(); i++) {
/* 1118 */         if (cmpStr.charAt(i) != outStr.charAt(i)) {
/* 1119 */           int l = Math.max(0, i - 10);
/* 1120 */           int r = Math.min(cmpStr.length(), i + 10);
/* 1121 */           if (compareResult != null && currentPath != null) {
/* 1122 */             currentPath.pushOffsetToPath(i);
/* 1123 */             compareResult.addError(currentPath, String.format("PdfString. Characters differ at position %s. Expected: %s (%s). Found: %s (%s).", new Object[] {
/* 1124 */                     Integer.valueOf(i), Character.toString(cmpStr.charAt(i)), cmpStr.substring(l, r).replace("\n", "\\n"), 
/* 1125 */                     Character.toString(outStr.charAt(i)), outStr.substring(l, r).replace("\n", "\\n") }));
/* 1126 */             currentPath.pop();
/*      */           } 
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1132 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean compareLiterals(PdfLiteral outLiteral, PdfLiteral cmpLiteral) {
/* 1137 */     return Arrays.equals(cmpLiteral.getBytes(), outLiteral.getBytes());
/*      */   }
/*      */   
/*      */   private boolean compareLiteralsExtended(PdfLiteral outLiteral, PdfLiteral cmpLiteral, ObjectPath currentPath, CompareResult compareResult) {
/* 1141 */     if (compareLiterals(outLiteral, cmpLiteral)) {
/* 1142 */       return true;
/*      */     }
/* 1144 */     if (compareResult != null && currentPath != null) {
/* 1145 */       compareResult.addError(currentPath, String.format("PdfLiteral. Expected: %s. Found: %s", new Object[] { cmpLiteral, outLiteral }));
/*      */     }
/* 1147 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean compareBooleans(PdfBoolean outBoolean, PdfBoolean cmpBoolean) {
/* 1152 */     return Arrays.equals(cmpBoolean.getBytes(), outBoolean.getBytes());
/*      */   }
/*      */   
/*      */   private boolean compareBooleansExtended(PdfBoolean outBoolean, PdfBoolean cmpBoolean, ObjectPath currentPath, CompareResult compareResult) {
/* 1156 */     if (cmpBoolean.booleanValue() == outBoolean.booleanValue()) {
/* 1157 */       return true;
/*      */     }
/* 1159 */     if (compareResult != null && currentPath != null)
/* 1160 */       compareResult.addError(currentPath, String.format("PdfBoolean. Expected: %s. Found: %s.", new Object[] { Boolean.valueOf(cmpBoolean.booleanValue()), Boolean.valueOf(outBoolean.booleanValue()) })); 
/* 1161 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String compareXmp(byte[] xmp1, byte[] xmp2) {
/* 1166 */     return compareXmp(xmp1, xmp2, false);
/*      */   }
/*      */   
/*      */   public String compareXmp(byte[] xmp1, byte[] xmp2, boolean ignoreDateAndProducerProperties) {
/*      */     try {
/* 1171 */       if (ignoreDateAndProducerProperties) {
/* 1172 */         XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(xmp1);
/*      */         
/* 1174 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "CreateDate", true, true);
/* 1175 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "ModifyDate", true, true);
/* 1176 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "MetadataDate", true, true);
/* 1177 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/pdf/1.3/", "Producer", true, true);
/*      */         
/* 1179 */         xmp1 = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(8192));
/*      */         
/* 1181 */         xmpMeta = XMPMetaFactory.parseFromBuffer(xmp2);
/* 1182 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "CreateDate", true, true);
/* 1183 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "ModifyDate", true, true);
/* 1184 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/xap/1.0/", "MetadataDate", true, true);
/* 1185 */         XMPUtils.removeProperties(xmpMeta, "http://ns.adobe.com/pdf/1.3/", "Producer", true, true);
/*      */         
/* 1187 */         xmp2 = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(8192));
/*      */       } 
/*      */       
/* 1190 */       if (!compareXmls(xmp1, xmp2)) {
/* 1191 */         return "The XMP packages different!";
/*      */       }
/* 1193 */     } catch (XMPException xmpExc) {
/* 1194 */       return "XMP parsing failure!";
/* 1195 */     } catch (IOException ioExc) {
/* 1196 */       return "XMP parsing failure!";
/* 1197 */     } catch (ParserConfigurationException parseExc) {
/* 1198 */       return "XMP parsing failure!";
/* 1199 */     } catch (SAXException parseExc) {
/* 1200 */       return "XMP parsing failure!";
/*      */     } 
/* 1202 */     return null;
/*      */   }
/*      */   
/*      */   public String compareXmp(String outPdf, String cmpPdf) {
/* 1206 */     return compareXmp(outPdf, cmpPdf, false);
/*      */   }
/*      */   
/*      */   public String compareXmp(String outPdf, String cmpPdf, boolean ignoreDateAndProducerProperties) {
/* 1210 */     init(outPdf, cmpPdf);
/* 1211 */     PdfReader cmpReader = null;
/* 1212 */     PdfReader outReader = null;
/*      */     try {
/* 1214 */       cmpReader = new PdfReader(this.cmpPdf);
/* 1215 */       outReader = new PdfReader(this.outPdf);
/* 1216 */       byte[] cmpBytes = cmpReader.getMetadata(), outBytes = outReader.getMetadata();
/* 1217 */       return compareXmp(cmpBytes, outBytes, ignoreDateAndProducerProperties);
/* 1218 */     } catch (IOException e) {
/* 1219 */       return "XMP parsing failure!";
/*      */     } finally {
/*      */       
/* 1222 */       if (cmpReader != null)
/* 1223 */         cmpReader.close(); 
/* 1224 */       if (outReader != null)
/* 1225 */         outReader.close(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean compareXmls(byte[] xml1, byte[] xml2) throws ParserConfigurationException, SAXException, IOException {
/* 1230 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 1231 */     dbf.setNamespaceAware(true);
/* 1232 */     dbf.setCoalescing(true);
/* 1233 */     dbf.setIgnoringElementContentWhitespace(true);
/* 1234 */     dbf.setIgnoringComments(true);
/* 1235 */     DocumentBuilder db = dbf.newDocumentBuilder();
/* 1236 */     db.setEntityResolver(new SafeEmptyEntityResolver());
/*      */     
/* 1238 */     Document doc1 = db.parse(new ByteArrayInputStream(xml1));
/* 1239 */     doc1.normalizeDocument();
/*      */     
/* 1241 */     Document doc2 = db.parse(new ByteArrayInputStream(xml2));
/* 1242 */     doc2.normalizeDocument();
/*      */     
/* 1244 */     return doc2.isEqualNode(doc1);
/*      */   }
/*      */   
/*      */   public String compareDocumentInfo(String outPdf, String cmpPdf) throws IOException {
/* 1248 */     System.out.print("[itext] INFO  Comparing document info.......");
/* 1249 */     String message = null;
/* 1250 */     PdfReader outReader = new PdfReader(outPdf);
/* 1251 */     PdfReader cmpReader = new PdfReader(cmpPdf);
/* 1252 */     String[] cmpInfo = convertInfo(cmpReader.getInfo());
/* 1253 */     String[] outInfo = convertInfo(outReader.getInfo());
/* 1254 */     for (int i = 0; i < cmpInfo.length; i++) {
/* 1255 */       if (!cmpInfo[i].equals(outInfo[i])) {
/* 1256 */         message = "Document info fail";
/*      */         break;
/*      */       } 
/*      */     } 
/* 1260 */     outReader.close();
/* 1261 */     cmpReader.close();
/*      */     
/* 1263 */     if (message == null) {
/* 1264 */       System.out.println("OK");
/*      */     } else {
/* 1266 */       System.out.println("Fail");
/* 1267 */     }  System.out.flush();
/* 1268 */     return message;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean linksAreSame(PdfAnnotation.PdfImportedLink cmpLink, PdfAnnotation.PdfImportedLink outLink) {
/* 1274 */     if (cmpLink.getDestinationPage() != outLink.getDestinationPage())
/* 1275 */       return false; 
/* 1276 */     if (!cmpLink.getRect().toString().equals(outLink.getRect().toString())) {
/* 1277 */       return false;
/*      */     }
/* 1279 */     Map<PdfName, PdfObject> cmpParams = cmpLink.getParameters();
/* 1280 */     Map<PdfName, PdfObject> outParams = outLink.getParameters();
/* 1281 */     if (cmpParams.size() != outParams.size()) {
/* 1282 */       return false;
/*      */     }
/* 1284 */     for (Map.Entry<PdfName, PdfObject> cmpEntry : cmpParams.entrySet()) {
/* 1285 */       PdfObject cmpObj = cmpEntry.getValue();
/* 1286 */       if (!outParams.containsKey(cmpEntry.getKey()))
/* 1287 */         return false; 
/* 1288 */       PdfObject outObj = outParams.get(cmpEntry.getKey());
/* 1289 */       if (cmpObj.type() != outObj.type()) {
/* 1290 */         return false;
/*      */       }
/* 1292 */       switch (cmpObj.type()) {
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*      */         case 4:
/*      */         case 8:
/* 1298 */           if (!cmpObj.toString().equals(outObj.toString())) {
/* 1299 */             return false;
/*      */           }
/*      */       } 
/*      */     
/*      */     } 
/* 1304 */     return true;
/*      */   }
/*      */   
/*      */   public String compareLinks(String outPdf, String cmpPdf) throws IOException {
/* 1308 */     System.out.print("[itext] INFO  Comparing link annotations....");
/* 1309 */     String message = null;
/* 1310 */     PdfReader outReader = new PdfReader(outPdf);
/* 1311 */     PdfReader cmpReader = new PdfReader(cmpPdf);
/* 1312 */     for (int i = 0; i < outReader.getNumberOfPages() && i < cmpReader.getNumberOfPages(); i++) {
/* 1313 */       List<PdfAnnotation.PdfImportedLink> outLinks = outReader.getLinks(i + 1);
/* 1314 */       List<PdfAnnotation.PdfImportedLink> cmpLinks = cmpReader.getLinks(i + 1);
/* 1315 */       if (cmpLinks.size() != outLinks.size()) {
/* 1316 */         message = String.format("Different number of links on page %d.", new Object[] { Integer.valueOf(i + 1) });
/*      */         break;
/*      */       } 
/* 1319 */       for (int j = 0; j < cmpLinks.size(); j++) {
/* 1320 */         if (!linksAreSame(cmpLinks.get(j), outLinks.get(j))) {
/* 1321 */           message = String.format("Different links on page %d.\n%s\n%s", new Object[] { Integer.valueOf(i + 1), ((PdfAnnotation.PdfImportedLink)cmpLinks.get(j)).toString(), ((PdfAnnotation.PdfImportedLink)outLinks.get(j)).toString() });
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1326 */     outReader.close();
/* 1327 */     cmpReader.close();
/* 1328 */     if (message == null) {
/* 1329 */       System.out.println("OK");
/*      */     } else {
/* 1331 */       System.out.println("Fail");
/* 1332 */     }  System.out.flush();
/* 1333 */     return message;
/*      */   }
/*      */   
/*      */   public String compareTagStructures(String outPdf, String cmpPdf) throws IOException, ParserConfigurationException, SAXException {
/* 1337 */     System.out.print("[itext] INFO  Comparing tag structures......");
/*      */     
/* 1339 */     String outXml = outPdf.replace(".pdf", ".xml");
/* 1340 */     String cmpXml = outPdf.replace(".pdf", ".cmp.xml");
/*      */     
/* 1342 */     String message = null;
/* 1343 */     PdfReader reader = new PdfReader(outPdf);
/* 1344 */     FileOutputStream xmlOut1 = new FileOutputStream(outXml);
/* 1345 */     (new CmpTaggedPdfReaderTool()).convertToXml(reader, xmlOut1);
/* 1346 */     reader.close();
/* 1347 */     reader = new PdfReader(cmpPdf);
/* 1348 */     FileOutputStream xmlOut2 = new FileOutputStream(cmpXml);
/* 1349 */     (new CmpTaggedPdfReaderTool()).convertToXml(reader, xmlOut2);
/* 1350 */     reader.close();
/* 1351 */     if (!compareXmls(outXml, cmpXml)) {
/* 1352 */       message = "The tag structures are different.";
/*      */     }
/* 1354 */     xmlOut1.close();
/* 1355 */     xmlOut2.close();
/* 1356 */     if (message == null) {
/* 1357 */       System.out.println("OK");
/*      */     } else {
/* 1359 */       System.out.println("Fail");
/* 1360 */     }  System.out.flush();
/* 1361 */     return message;
/*      */   }
/*      */   
/*      */   private String[] convertInfo(HashMap<String, String> info) {
/* 1365 */     String[] convertedInfo = { "", "", "", "" };
/* 1366 */     for (Map.Entry<String, String> entry : info.entrySet()) {
/* 1367 */       if ("title".equalsIgnoreCase(entry.getKey())) {
/* 1368 */         convertedInfo[0] = entry.getValue(); continue;
/* 1369 */       }  if ("author".equalsIgnoreCase(entry.getKey())) {
/* 1370 */         convertedInfo[1] = entry.getValue(); continue;
/* 1371 */       }  if ("subject".equalsIgnoreCase(entry.getKey())) {
/* 1372 */         convertedInfo[2] = entry.getValue(); continue;
/* 1373 */       }  if ("keywords".equalsIgnoreCase(entry.getKey())) {
/* 1374 */         convertedInfo[3] = entry.getValue();
/*      */       }
/*      */     } 
/* 1377 */     return convertedInfo;
/*      */   }
/*      */   
/*      */   public boolean compareXmls(String xml1, String xml2) throws ParserConfigurationException, SAXException, IOException {
/* 1381 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 1382 */     dbf.setNamespaceAware(true);
/* 1383 */     dbf.setCoalescing(true);
/* 1384 */     dbf.setIgnoringElementContentWhitespace(true);
/* 1385 */     dbf.setIgnoringComments(true);
/* 1386 */     DocumentBuilder db = dbf.newDocumentBuilder();
/* 1387 */     db.setEntityResolver(new SafeEmptyEntityResolver());
/*      */     
/* 1389 */     Document doc1 = db.parse(new File(xml1));
/* 1390 */     doc1.normalizeDocument();
/*      */     
/* 1392 */     Document doc2 = db.parse(new File(xml2));
/* 1393 */     doc2.normalizeDocument();
/*      */     
/* 1395 */     return doc2.isEqualNode(doc1);
/*      */   }
/*      */   
/*      */   private void init(String outPdf, String cmpPdf) {
/* 1399 */     this.outPdf = outPdf;
/* 1400 */     this.cmpPdf = cmpPdf;
/* 1401 */     this.outPdfName = (new File(outPdf)).getName();
/* 1402 */     this.cmpPdfName = (new File(cmpPdf)).getName();
/* 1403 */     this.outImage = this.outPdfName + "%03d" + "." + "png";
/* 1404 */     if (this.cmpPdfName.startsWith("cmp_")) {
/* 1405 */       this.cmpImage = this.cmpPdfName + "%03d" + "." + "png";
/*      */     } else {
/* 1407 */       this.cmpImage = "cmp_" + this.cmpPdfName + "%03d" + "." + "png";
/*      */     } 
/*      */   }
/*      */   private boolean compareStreams(InputStream is1, InputStream is2) throws IOException {
/*      */     int len1;
/* 1412 */     byte[] buffer1 = new byte[65536];
/* 1413 */     byte[] buffer2 = new byte[65536];
/*      */ 
/*      */     
/*      */     do {
/* 1417 */       len1 = is1.read(buffer1);
/* 1418 */       int len2 = is2.read(buffer2);
/* 1419 */       if (len1 != len2)
/* 1420 */         return false; 
/* 1421 */       if (!Arrays.equals(buffer1, buffer2))
/* 1422 */         return false; 
/* 1423 */     } while (len1 != -1);
/*      */ 
/*      */     
/* 1426 */     return true;
/*      */   }
/*      */   
/*      */   class PngFileFilter
/*      */     implements FileFilter {
/*      */     public boolean accept(File pathname) {
/* 1432 */       String ap = pathname.getAbsolutePath();
/* 1433 */       boolean b1 = ap.endsWith(".png");
/* 1434 */       boolean b2 = ap.contains("cmp_");
/* 1435 */       return (b1 && !b2);
/*      */     }
/*      */   }
/*      */   
/*      */   class CmpPngFileFilter implements FileFilter {
/*      */     public boolean accept(File pathname) {
/* 1441 */       String ap = pathname.getAbsolutePath();
/* 1442 */       boolean b1 = ap.endsWith(".png");
/* 1443 */       boolean b2 = ap.contains("cmp_");
/* 1444 */       return (b1 && b2);
/*      */     }
/*      */   }
/*      */   
/*      */   class ImageNameComparator implements Comparator<File> {
/*      */     public int compare(File f1, File f2) {
/* 1450 */       String f1Name = f1.getAbsolutePath();
/* 1451 */       String f2Name = f2.getAbsolutePath();
/* 1452 */       return f1Name.compareTo(f2Name);
/*      */     }
/*      */   }
/*      */   
/*      */   class CmpTaggedPdfReaderTool
/*      */     extends TaggedPdfReaderTool {
/* 1458 */     Map<PdfDictionary, Map<Integer, String>> parsedTags = new HashMap<PdfDictionary, Map<Integer, String>>();
/*      */ 
/*      */ 
/*      */     
/*      */     public void parseTag(String tag, PdfObject object, PdfDictionary page) throws IOException {
/* 1463 */       if (object instanceof PdfNumber) {
/*      */         
/* 1465 */         if (!this.parsedTags.containsKey(page)) {
/* 1466 */           CompareTool.CmpMarkedContentRenderFilter listener = new CompareTool.CmpMarkedContentRenderFilter();
/*      */           
/* 1468 */           PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
/*      */           
/* 1470 */           processor.processContent(PdfReader.getPageContent(page), page
/* 1471 */               .getAsDict(PdfName.RESOURCES));
/*      */           
/* 1473 */           this.parsedTags.put(page, listener.getParsedTagContent());
/*      */         } 
/*      */         
/* 1476 */         String tagContent = "";
/* 1477 */         if (((Map)this.parsedTags.get(page)).containsKey(Integer.valueOf(((PdfNumber)object).intValue()))) {
/* 1478 */           tagContent = (String)((Map)this.parsedTags.get(page)).get(Integer.valueOf(((PdfNumber)object).intValue()));
/*      */         }
/* 1480 */         this.out.print(XMLUtil.escapeXML(tagContent, true));
/*      */       } else {
/*      */         
/* 1483 */         super.parseTag(tag, object, page);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void inspectChildDictionary(PdfDictionary k) throws IOException {
/* 1489 */       inspectChildDictionary(k, true);
/*      */     }
/*      */   }
/*      */   
/*      */   class CmpMarkedContentRenderFilter
/*      */     implements RenderListener {
/* 1495 */     Map<Integer, TextExtractionStrategy> tagsByMcid = new HashMap<Integer, TextExtractionStrategy>();
/*      */     
/*      */     public Map<Integer, String> getParsedTagContent() {
/* 1498 */       Map<Integer, String> content = new HashMap<Integer, String>();
/* 1499 */       for (Iterator<Integer> iterator = this.tagsByMcid.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/* 1500 */         content.put(Integer.valueOf(id), ((TextExtractionStrategy)this.tagsByMcid.get(Integer.valueOf(id))).getResultantText()); }
/*      */       
/* 1502 */       return content;
/*      */     }
/*      */     
/*      */     public void beginTextBlock() {
/* 1506 */       for (Iterator<Integer> iterator = this.tagsByMcid.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/* 1507 */         ((TextExtractionStrategy)this.tagsByMcid.get(Integer.valueOf(id))).beginTextBlock(); }
/*      */     
/*      */     }
/*      */     
/*      */     public void renderText(TextRenderInfo renderInfo) {
/* 1512 */       Integer mcid = renderInfo.getMcid();
/* 1513 */       if (mcid != null && this.tagsByMcid.containsKey(mcid)) {
/* 1514 */         ((TextExtractionStrategy)this.tagsByMcid.get(mcid)).renderText(renderInfo);
/*      */       }
/* 1516 */       else if (mcid != null) {
/* 1517 */         this.tagsByMcid.put(mcid, new SimpleTextExtractionStrategy());
/* 1518 */         ((TextExtractionStrategy)this.tagsByMcid.get(mcid)).renderText(renderInfo);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void endTextBlock() {
/* 1523 */       for (Iterator<Integer> iterator = this.tagsByMcid.keySet().iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
/* 1524 */         ((TextExtractionStrategy)this.tagsByMcid.get(Integer.valueOf(id))).endTextBlock(); }
/*      */     
/*      */     }
/*      */     
/*      */     public void renderImage(ImageRenderInfo renderInfo) {}
/*      */   }
/*      */   
/*      */   private static class SafeEmptyEntityResolver
/*      */     implements EntityResolver {
/*      */     private SafeEmptyEntityResolver() {}
/*      */     
/*      */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 1536 */       return new InputSource(new StringReader(""));
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/testutils/CompareTool.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */