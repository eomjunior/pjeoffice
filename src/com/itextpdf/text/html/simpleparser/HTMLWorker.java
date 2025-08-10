/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.DocListener;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.FontProvider;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.List;
/*     */ import com.itextpdf.text.ListItem;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.TextElementArray;
/*     */ import com.itextpdf.text.html.HtmlUtilities;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.PdfPCell;
/*     */ import com.itextpdf.text.pdf.PdfPTable;
/*     */ import com.itextpdf.text.pdf.draw.LineSeparator;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HTMLWorker
/*     */   implements SimpleXMLDocHandler, DocListener
/*     */ {
/*  85 */   private static Logger LOGGER = LoggerFactory.getLogger(HTMLWorker.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DocListener document;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, HTMLTagProcessor> tags;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   private StyleSheet style = new StyleSheet(); protected Stack<Element> stack; protected Paragraph currentParagraph; private final ChainedProperties chain; public static final String IMG_PROVIDER = "img_provider"; public static final String IMG_PROCESSOR = "img_interface"; public static final String IMG_STORE = "img_static"; public static final String IMG_BASEURL = "img_baseurl"; public static final String FONT_PROVIDER = "font_factory"; public static final String LINK_PROVIDER = "alink_interface"; private Map<String, Object> providers; private final ElementFactory factory; private final Stack<boolean[]> tableState; private boolean pendingTR; private boolean pendingTD;
/*     */   private boolean pendingLI;
/*     */   private boolean insidePRE;
/*     */   protected boolean skipText;
/*     */   protected List<Element> objectList;
/*     */   
/*     */   public HTMLWorker(DocListener document) {
/* 109 */     this(document, null, null);
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
/*     */   public void setSupportedTags(Map<String, HTMLTagProcessor> tags) {
/* 131 */     if (tags == null)
/* 132 */       tags = new HTMLTagProcessors(); 
/* 133 */     this.tags = tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyleSheet(StyleSheet style) {
/* 141 */     if (style == null)
/* 142 */       style = new StyleSheet(); 
/* 143 */     this.style = style;
/*     */   } public void startDocument() { HashMap<String, String> attrs = new HashMap<String, String>(); this.style.applyStyle("body", attrs); this.chain.addToChain("body", attrs); } public void startElement(String tag, Map<String, String> attrs) { HTMLTagProcessor htmlTag = this.tags.get(tag); if (htmlTag == null)
/*     */       return;  this.style.applyStyle(tag, attrs); StyleSheet.resolveStyleAttribute(attrs, this.chain); try { htmlTag.startElement(this, tag, attrs); } catch (DocumentException e) { throw new ExceptionConverter(e); } catch (IOException e) { throw new ExceptionConverter(e); }
/*     */      }
/*     */   public void text(String content) { if (this.skipText)
/*     */       return;  if (this.currentParagraph == null)
/*     */       this.currentParagraph = createParagraph();  if (!this.insidePRE) { if (content.trim().length() == 0 && content.indexOf(' ') < 0)
/*     */         return;  content = HtmlUtilities.eliminateWhiteSpace(content); }
/*     */      Chunk chunk = createChunk(content); this.currentParagraph.add((Element)chunk); }
/* 152 */   public void parse(Reader reader) throws IOException { LOGGER.info("Please note, there is a more extended version of the HTMLWorker available in the iText XMLWorker");
/* 153 */     SimpleXMLParser.parse(this, null, reader, true); } public void endElement(String tag) { HTMLTagProcessor htmlTag = this.tags.get(tag); if (htmlTag == null)
/*     */       return;  try { htmlTag.endElement(this, tag); } catch (DocumentException e) { throw new ExceptionConverter(e); }  }
/*     */   public void endDocument() { try { for (int k = 0; k < this.stack.size(); k++)
/*     */         this.document.add(this.stack.elementAt(k));  if (this.currentParagraph != null)
/*     */         this.document.add((Element)this.currentParagraph);  this.currentParagraph = null; }
/*     */     catch (Exception e) { throw new ExceptionConverter(e); }
/*     */      }
/*     */   public void newLine() { if (this.currentParagraph == null)
/*     */       this.currentParagraph = new Paragraph();  this.currentParagraph.add((Element)createChunk("\n")); }
/* 162 */   public HTMLWorker(DocListener document, Map<String, HTMLTagProcessor> tags, StyleSheet style) { this.stack = new Stack<Element>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.chain = new ChainedProperties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     this.providers = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 402 */     this.factory = new ElementFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 659 */     this.tableState = (Stack)new Stack<boolean>();
/*     */ 
/*     */     
/* 662 */     this.pendingTR = false;
/*     */ 
/*     */     
/* 665 */     this.pendingTD = false;
/*     */ 
/*     */     
/* 668 */     this.pendingLI = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 674 */     this.insidePRE = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 680 */     this.skipText = false; this.document = document; setSupportedTags(tags); setStyleSheet(style); }
/*     */   public void carriageReturn() throws DocumentException { if (this.currentParagraph == null)
/*     */       return;  if (this.stack.empty()) { this.document.add((Element)this.currentParagraph); }
/*     */     else { Element obj = this.stack.pop(); if (obj instanceof TextElementArray) { TextElementArray current = (TextElementArray)obj; current.add((Element)this.currentParagraph); }
/*     */        this.stack.push(obj); }
/*     */      this.currentParagraph = null; }
/*     */   public void flushContent() { pushToStack((Element)this.currentParagraph); this.currentParagraph = new Paragraph(); }
/*     */   public void pushToStack(Element element) { if (element != null)
/* 688 */       this.stack.push(element);  } public void pushTableState() { this.tableState.push(new boolean[] { this.pendingTR, this.pendingTD }); }
/*     */   public void updateChain(String tag, Map<String, String> attrs) { this.chain.addToChain(tag, attrs); }
/*     */   public void updateChain(String tag) { this.chain.removeChain(tag); }
/*     */   public void setProviders(Map<String, Object> providers) { if (providers == null) return;  this.providers = providers; FontProvider ff = null; if (providers != null) ff = (FontProvider)providers.get("font_factory");  if (ff != null) this.factory.setFontProvider(ff);  }
/*     */   public Chunk createChunk(String content) { return this.factory.createChunk(content, this.chain); }
/*     */   public Paragraph createParagraph() { return this.factory.createParagraph(this.chain); }
/*     */   public List createList(String tag) { return this.factory.createList(tag, this.chain); }
/*     */   public ListItem createListItem() { return this.factory.createListItem(this.chain); }
/*     */   public LineSeparator createLineSeparator(Map<String, String> attrs) { return this.factory.createLineSeparator(attrs, this.currentParagraph.getLeading() / 2.0F); }
/* 697 */   public Image createImage(Map<String, String> attrs) throws DocumentException, IOException { String src = attrs.get("src"); if (src == null) return null;  Image img = this.factory.createImage(src, attrs, this.chain, this.document, (ImageProvider)this.providers.get("img_provider"), (ImageStore)this.providers.get("img_static"), (String)this.providers.get("img_baseurl")); return img; } public void popTableState() { boolean[] state = this.tableState.pop();
/* 698 */     this.pendingTR = state[0];
/* 699 */     this.pendingTD = state[1]; } public CellWrapper createCell(String tag) { return new CellWrapper(tag, this.chain); }
/*     */   public void processLink() { if (this.currentParagraph == null)
/*     */       this.currentParagraph = new Paragraph();  LinkProcessor i = (LinkProcessor)this.providers.get("alink_interface"); if (i == null || !i.process(this.currentParagraph, this.chain)) { String href = this.chain.getProperty("href"); if (href != null)
/*     */         for (Chunk ck : this.currentParagraph.getChunks())
/*     */           ck.setAnchor(href);   }  if (this.stack.isEmpty()) { Paragraph tmp = new Paragraph(new Phrase((Phrase)this.currentParagraph)); this.currentParagraph = tmp; } else { Paragraph tmp = (Paragraph)this.stack.pop(); tmp.add((Element)new Phrase((Phrase)this.currentParagraph)); this.currentParagraph = tmp; }  }
/*     */   public void processList() throws DocumentException { if (this.stack.empty())
/*     */       return;  Element obj = this.stack.pop(); if (!(obj instanceof List)) { this.stack.push(obj); return; }  if (this.stack.empty()) { this.document.add(obj); } else { ((TextElementArray)this.stack.peek()).add(obj); }
/*     */      }
/* 707 */   public boolean isPendingTR() { return this.pendingTR; } public void processListItem() throws DocumentException { if (this.stack.empty())
/*     */       return;  Element obj = this.stack.pop(); if (!(obj instanceof ListItem)) { this.stack.push(obj); return; }  if (this.stack.empty()) { this.document.add(obj); return; }  ListItem item = (ListItem)obj; Element list = this.stack.pop(); if (!(list instanceof List)) { this.stack.push(list); return; }
/*     */      ((List)list).add((Element)item); item.adjustListSymbolFont(); this.stack.push(list); }
/*     */   public void processImage(Image img, Map<String, String> attrs) throws DocumentException { ImageProcessor processor = (ImageProcessor)this.providers.get("img_interface"); if (processor == null || !processor.process(img, attrs, this.chain, this.document)) { String align = attrs.get("align"); if (align != null)
/*     */         carriageReturn();  if (this.currentParagraph == null)
/*     */         this.currentParagraph = createParagraph();  this.currentParagraph.add((Element)new Chunk(img, 0.0F, 0.0F, true)); this.currentParagraph.setAlignment(HtmlUtilities.alignmentValue(align)); if (align != null)
/*     */         carriageReturn();  }
/*     */      }
/* 715 */   public void setPendingTR(boolean pendingTR) { this.pendingTR = pendingTR; } public void processTable() throws DocumentException { TableWrapper table = (TableWrapper)this.stack.pop();
/*     */     PdfPTable tb = table.createTable();
/*     */     tb.setSplitRows(true);
/*     */     if (this.stack.empty()) {
/*     */       this.document.add((Element)tb);
/*     */     } else {
/*     */       ((TextElementArray)this.stack.peek()).add((Element)tb);
/*     */     }  }
/* 723 */   public boolean isPendingTD() { return this.pendingTD; } public void processRow() { ArrayList<PdfPCell> row = new ArrayList<PdfPCell>(); ArrayList<Float> cellWidths = new ArrayList<Float>(); boolean percentage = false; float totalWidth = 0.0F; int zeroWidth = 0; TableWrapper table = null; while (true) { Element obj = this.stack.pop(); if (obj instanceof CellWrapper) { CellWrapper cell = (CellWrapper)obj; float width = cell.getWidth(); cellWidths.add(new Float(width)); percentage |= cell.isPercentage(); if (width == 0.0F) { zeroWidth++; } else { totalWidth += width; }  row.add(cell.getCell()); }  if (obj instanceof TableWrapper) { table = (TableWrapper)obj; table.addRow(row); if (cellWidths.size() > 0) { totalWidth = 100.0F - totalWidth; Collections.reverse(cellWidths); float[] widths = new float[cellWidths.size()]; boolean hasZero = false; for (int i = 0; i < widths.length; i++) { widths[i] = ((Float)cellWidths.get(i)).floatValue(); if (widths[i] == 0.0F && percentage && zeroWidth > 0)
/*     */               widths[i] = totalWidth / zeroWidth;  if (widths[i] == 0.0F) { hasZero = true; break; }
/*     */              }
/*     */            if (!hasZero)
/*     */             table.setColWidths(widths);  }
/*     */          this.stack.push(table); return; }
/*     */        }
/*     */      }
/* 731 */   public void setPendingTD(boolean pendingTD) { this.pendingTD = pendingTD; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPendingLI() {
/* 739 */     return this.pendingLI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPendingLI(boolean pendingLI) {
/* 747 */     this.pendingLI = pendingLI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInsidePRE() {
/* 755 */     return this.insidePRE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInsidePRE(boolean insidePRE) {
/* 763 */     this.insidePRE = insidePRE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSkipText() {
/* 771 */     return this.skipText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipText(boolean skipText) {
/* 779 */     this.skipText = skipText;
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
/*     */   public static List<Element> parseToList(Reader reader, StyleSheet style) throws IOException {
/* 796 */     return parseToList(reader, style, null);
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
/*     */   public static List<Element> parseToList(Reader reader, StyleSheet style, HashMap<String, Object> providers) throws IOException {
/* 809 */     return parseToList(reader, style, null, providers);
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
/*     */   public static List<Element> parseToList(Reader reader, StyleSheet style, Map<String, HTMLTagProcessor> tags, HashMap<String, Object> providers) throws IOException {
/* 824 */     HTMLWorker worker = new HTMLWorker(null, tags, style);
/* 825 */     worker.document = worker;
/* 826 */     worker.setProviders(providers);
/* 827 */     worker.objectList = new ArrayList<Element>();
/* 828 */     worker.parse(reader);
/* 829 */     return worker.objectList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Element element) throws DocumentException {
/* 838 */     this.objectList.add(element);
/* 839 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean newPage() {
/* 852 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetPageCount() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMarginMirroring(boolean marginMirroring) {
/* 871 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
/* 879 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
/* 887 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageCount(int pageN) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setPageSize(Rectangle pageSize) {
/* 900 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setInterfaceProps(HashMap<String, Object> providers) {
/* 911 */     setProviders(providers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Map<String, Object> getInterfaceProps() {
/* 919 */     return this.providers;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/HTMLWorker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */