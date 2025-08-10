/*      */ package com.itextpdf.text.pdf.parser;
/*      */ 
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.pdf.CMYKColor;
/*      */ import com.itextpdf.text.pdf.CMapAwareDocumentFont;
/*      */ import com.itextpdf.text.pdf.GrayColor;
/*      */ import com.itextpdf.text.pdf.PRIndirectReference;
/*      */ import com.itextpdf.text.pdf.PRTokeniser;
/*      */ import com.itextpdf.text.pdf.PdfArray;
/*      */ import com.itextpdf.text.pdf.PdfContentParser;
/*      */ import com.itextpdf.text.pdf.PdfDictionary;
/*      */ import com.itextpdf.text.pdf.PdfIndirectReference;
/*      */ import com.itextpdf.text.pdf.PdfLiteral;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfNumber;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfReader;
/*      */ import com.itextpdf.text.pdf.PdfStream;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*      */ import java.io.IOException;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Stack;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfContentStreamProcessor
/*      */ {
/*      */   public static final String DEFAULTOPERATOR = "DefaultOperator";
/*      */   private final Map<String, ContentOperator> operators;
/*      */   private ResourceDictionary resources;
/*   95 */   private final Stack<GraphicsState> gsStack = new Stack<GraphicsState>();
/*      */ 
/*      */   
/*      */   private Matrix textMatrix;
/*      */ 
/*      */   
/*      */   private Matrix textLineMatrix;
/*      */ 
/*      */   
/*      */   private final RenderListener renderListener;
/*      */ 
/*      */   
/*      */   private final Map<PdfName, XObjectDoHandler> xobjectDoHandlers;
/*      */   
/*  109 */   private final Map<Integer, WeakReference<CMapAwareDocumentFont>> cachedFonts = new HashMap<Integer, WeakReference<CMapAwareDocumentFont>>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   private final Stack<MarkedContentInfo> markedContentStack = new Stack<MarkedContentInfo>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfContentStreamProcessor(RenderListener renderListener) {
/*  123 */     this.renderListener = renderListener;
/*  124 */     this.operators = new HashMap<String, ContentOperator>();
/*  125 */     populateOperators();
/*  126 */     this.xobjectDoHandlers = new HashMap<PdfName, XObjectDoHandler>();
/*  127 */     populateXObjectDoHandlers();
/*  128 */     reset();
/*      */   }
/*      */   
/*      */   private void populateXObjectDoHandlers() {
/*  132 */     registerXObjectDoHandler(PdfName.DEFAULT, new IgnoreXObjectDoHandler());
/*  133 */     registerXObjectDoHandler(PdfName.FORM, new FormXObjectDoHandler());
/*  134 */     registerXObjectDoHandler(PdfName.IMAGE, new ImageXObjectDoHandler());
/*      */   }
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
/*      */   public XObjectDoHandler registerXObjectDoHandler(PdfName xobjectSubType, XObjectDoHandler handler) {
/*  148 */     return this.xobjectDoHandlers.put(xobjectSubType, handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CMapAwareDocumentFont getFont(PRIndirectReference ind) {
/*  158 */     Integer n = Integer.valueOf(ind.getNumber());
/*  159 */     WeakReference<CMapAwareDocumentFont> fontRef = this.cachedFonts.get(n);
/*  160 */     CMapAwareDocumentFont font = (fontRef == null) ? null : fontRef.get();
/*  161 */     if (font == null) {
/*  162 */       font = new CMapAwareDocumentFont(ind);
/*  163 */       this.cachedFonts.put(n, new WeakReference<CMapAwareDocumentFont>(font));
/*      */     } 
/*  165 */     return font;
/*      */   }
/*      */   
/*      */   private CMapAwareDocumentFont getFont(PdfDictionary fontResource) {
/*  169 */     return new CMapAwareDocumentFont(fontResource);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void populateOperators() {
/*  177 */     registerContentOperator("DefaultOperator", new IgnoreOperatorContentOperator());
/*      */     
/*  179 */     registerContentOperator("q", new PushGraphicsState());
/*  180 */     registerContentOperator("Q", new PopGraphicsState());
/*  181 */     registerContentOperator("g", new SetGrayFill());
/*  182 */     registerContentOperator("G", new SetGrayStroke());
/*  183 */     registerContentOperator("rg", new SetRGBFill());
/*  184 */     registerContentOperator("RG", new SetRGBStroke());
/*  185 */     registerContentOperator("k", new SetCMYKFill());
/*  186 */     registerContentOperator("K", new SetCMYKStroke());
/*  187 */     registerContentOperator("cs", new SetColorSpaceFill());
/*  188 */     registerContentOperator("CS", new SetColorSpaceStroke());
/*  189 */     registerContentOperator("sc", new SetColorFill());
/*  190 */     registerContentOperator("SC", new SetColorStroke());
/*  191 */     registerContentOperator("scn", new SetColorFill());
/*  192 */     registerContentOperator("SCN", new SetColorStroke());
/*  193 */     registerContentOperator("cm", new ModifyCurrentTransformationMatrix());
/*  194 */     registerContentOperator("gs", new ProcessGraphicsStateResource());
/*      */     
/*  196 */     SetTextCharacterSpacing tcOperator = new SetTextCharacterSpacing();
/*  197 */     registerContentOperator("Tc", tcOperator);
/*  198 */     SetTextWordSpacing twOperator = new SetTextWordSpacing();
/*  199 */     registerContentOperator("Tw", twOperator);
/*  200 */     registerContentOperator("Tz", new SetTextHorizontalScaling());
/*  201 */     SetTextLeading tlOperator = new SetTextLeading();
/*  202 */     registerContentOperator("TL", tlOperator);
/*  203 */     registerContentOperator("Tf", new SetTextFont());
/*  204 */     registerContentOperator("Tr", new SetTextRenderMode());
/*  205 */     registerContentOperator("Ts", new SetTextRise());
/*      */     
/*  207 */     registerContentOperator("BT", new BeginText());
/*  208 */     registerContentOperator("ET", new EndText());
/*  209 */     registerContentOperator("BMC", new BeginMarkedContent());
/*  210 */     registerContentOperator("BDC", new BeginMarkedContentDictionary());
/*  211 */     registerContentOperator("EMC", new EndMarkedContent());
/*      */     
/*  213 */     TextMoveStartNextLine tdOperator = new TextMoveStartNextLine();
/*  214 */     registerContentOperator("Td", tdOperator);
/*  215 */     registerContentOperator("TD", new TextMoveStartNextLineWithLeading(tdOperator, tlOperator));
/*  216 */     registerContentOperator("Tm", new TextSetTextMatrix());
/*  217 */     TextMoveNextLine tstarOperator = new TextMoveNextLine(tdOperator);
/*  218 */     registerContentOperator("T*", tstarOperator);
/*      */     
/*  220 */     ShowText tjOperator = new ShowText();
/*  221 */     registerContentOperator("Tj", tjOperator);
/*  222 */     MoveNextLineAndShowText tickOperator = new MoveNextLineAndShowText(tstarOperator, tjOperator);
/*  223 */     registerContentOperator("'", tickOperator);
/*  224 */     registerContentOperator("\"", new MoveNextLineAndShowTextWithSpacing(twOperator, tcOperator, tickOperator));
/*  225 */     registerContentOperator("TJ", new ShowTextArray());
/*      */     
/*  227 */     registerContentOperator("Do", new Do());
/*      */     
/*  229 */     registerContentOperator("w", new SetLineWidth());
/*  230 */     registerContentOperator("J", new SetLineCap());
/*  231 */     registerContentOperator("j", new SetLineJoin());
/*  232 */     registerContentOperator("M", new SetMiterLimit());
/*  233 */     registerContentOperator("d", new SetLineDashPattern());
/*      */ 
/*      */     
/*  236 */     if (this.renderListener instanceof ExtRenderListener) {
/*  237 */       int fillStroke = 3;
/*  238 */       registerContentOperator("m", new MoveTo());
/*  239 */       registerContentOperator("l", new LineTo());
/*  240 */       registerContentOperator("c", new Curve());
/*  241 */       registerContentOperator("v", new CurveFirstPointDuplicated());
/*  242 */       registerContentOperator("y", new CurveFourhPointDuplicated());
/*  243 */       registerContentOperator("h", new CloseSubpath());
/*  244 */       registerContentOperator("re", new Rectangle());
/*  245 */       registerContentOperator("S", new PaintPath(1, -1, false));
/*  246 */       registerContentOperator("s", new PaintPath(1, -1, true));
/*  247 */       registerContentOperator("f", new PaintPath(2, 1, false));
/*  248 */       registerContentOperator("F", new PaintPath(2, 1, false));
/*  249 */       registerContentOperator("f*", new PaintPath(2, 2, false));
/*  250 */       registerContentOperator("B", new PaintPath(fillStroke, 1, false));
/*  251 */       registerContentOperator("B*", new PaintPath(fillStroke, 2, false));
/*  252 */       registerContentOperator("b", new PaintPath(fillStroke, 1, true));
/*  253 */       registerContentOperator("b*", new PaintPath(fillStroke, 2, true));
/*  254 */       registerContentOperator("n", new PaintPath(0, -1, false));
/*  255 */       registerContentOperator("W", new ClipPath(1));
/*  256 */       registerContentOperator("W*", new ClipPath(2));
/*      */     } 
/*      */   }
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
/*      */   public ContentOperator registerContentOperator(String operatorString, ContentOperator operator) {
/*  271 */     return this.operators.put(operatorString, operator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<String> getRegisteredOperatorStrings() {
/*  279 */     return new ArrayList<String>(this.operators.keySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/*  286 */     this.gsStack.removeAllElements();
/*  287 */     this.gsStack.add(new GraphicsState());
/*  288 */     this.textMatrix = null;
/*  289 */     this.textLineMatrix = null;
/*  290 */     this.resources = new ResourceDictionary();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GraphicsState gs() {
/*  298 */     return this.gsStack.peek();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void invokeOperator(PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/*  307 */     ContentOperator op = this.operators.get(operator.toString());
/*  308 */     if (op == null)
/*  309 */       op = this.operators.get("DefaultOperator"); 
/*  310 */     op.invoke(this, operator, operands);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void beginMarkedContent(PdfName tag, PdfDictionary dict) {
/*  320 */     this.markedContentStack.push(new MarkedContentInfo(tag, dict));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endMarkedContent() {
/*  328 */     this.markedContentStack.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void beginText() {
/*  335 */     this.renderListener.beginTextBlock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void endText() {
/*  342 */     this.renderListener.endTextBlock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void displayPdfString(PdfString string) {
/*  351 */     TextRenderInfo renderInfo = new TextRenderInfo(string, gs(), this.textMatrix, this.markedContentStack);
/*      */     
/*  353 */     this.renderListener.renderText(renderInfo);
/*      */     
/*  355 */     this.textMatrix = (new Matrix(renderInfo.getUnscaledWidth(), 0.0F)).multiply(this.textMatrix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void displayXObject(PdfName xobjectName) throws IOException {
/*  366 */     PdfDictionary xobjects = this.resources.getAsDict(PdfName.XOBJECT);
/*  367 */     PdfObject xobject = PdfReader.getPdfObjectRelease(xobjects.get(xobjectName));
/*  368 */     PdfStream xobjectStream = (PdfStream)xobject;
/*      */     
/*  370 */     PdfName subType = xobjectStream.getAsName(PdfName.SUBTYPE);
/*  371 */     if (xobject.isStream()) {
/*  372 */       XObjectDoHandler handler = this.xobjectDoHandlers.get(subType);
/*  373 */       if (handler == null)
/*  374 */         handler = this.xobjectDoHandlers.get(PdfName.DEFAULT); 
/*  375 */       handler.handleXObject(this, xobjectStream, xobjects.getAsIndirectObject(xobjectName), this.markedContentStack);
/*      */     } else {
/*  377 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("XObject.1.is.not.a.stream", new Object[] { xobjectName }));
/*      */     } 
/*      */   }
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
/*      */   private void paintPath(int operation, int rule, boolean close) {
/*  395 */     if (close) {
/*  396 */       modifyPath(6, null);
/*      */     }
/*      */     
/*  399 */     PathPaintingRenderInfo renderInfo = new PathPaintingRenderInfo(operation, rule, gs());
/*  400 */     ((ExtRenderListener)this.renderListener).renderPath(renderInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyPath(int operation, List<Float> segmentData) {
/*  411 */     PathConstructionRenderInfo renderInfo = new PathConstructionRenderInfo(operation, segmentData, gs().getCtm());
/*  412 */     ((ExtRenderListener)this.renderListener).modifyPath(renderInfo);
/*      */   }
/*      */   
/*      */   private void clipPath(int rule) {
/*  416 */     ((ExtRenderListener)this.renderListener).clipPath(rule);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void applyTextAdjust(float tj) {
/*  424 */     float adjustBy = -tj / 1000.0F * (gs()).fontSize * (gs()).horizontalScaling;
/*      */     
/*  426 */     this.textMatrix = (new Matrix(adjustBy, 0.0F)).multiply(this.textMatrix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void processContent(byte[] contentBytes, PdfDictionary resources) {
/*  436 */     this.resources.push(resources);
/*      */     try {
/*  438 */       PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(contentBytes)));
/*  439 */       PdfContentParser ps = new PdfContentParser(tokeniser);
/*  440 */       ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
/*  441 */       while (ps.parse(operands).size() > 0) {
/*  442 */         PdfLiteral operator = (PdfLiteral)operands.get(operands.size() - 1);
/*  443 */         if ("BI".equals(operator.toString())) {
/*      */           
/*  445 */           PdfDictionary colorSpaceDic = (resources != null) ? resources.getAsDict(PdfName.COLORSPACE) : null;
/*  446 */           handleInlineImage(InlineImageUtils.parseInlineImage(ps, colorSpaceDic), colorSpaceDic); continue;
/*      */         } 
/*  448 */         invokeOperator(operator, operands);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  453 */     catch (Exception e) {
/*  454 */       throw new ExceptionConverter(e);
/*      */     } 
/*  456 */     this.resources.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleInlineImage(InlineImageInfo info, PdfDictionary colorSpaceDic) {
/*  466 */     ImageRenderInfo renderInfo = ImageRenderInfo.createForEmbeddedImage(gs(), info, colorSpaceDic, this.markedContentStack);
/*  467 */     this.renderListener.renderImage(renderInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RenderListener getRenderListener() {
/*  476 */     return this.renderListener;
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ResourceDictionary
/*      */     extends PdfDictionary
/*      */   {
/*  483 */     private final List<PdfDictionary> resourcesStack = new ArrayList<PdfDictionary>();
/*      */ 
/*      */ 
/*      */     
/*      */     public void push(PdfDictionary resources) {
/*  488 */       this.resourcesStack.add(resources);
/*      */     }
/*      */     
/*      */     public void pop() {
/*  492 */       this.resourcesStack.remove(this.resourcesStack.size() - 1);
/*      */     }
/*      */ 
/*      */     
/*      */     public PdfObject getDirectObject(PdfName key) {
/*  497 */       for (int i = this.resourcesStack.size() - 1; i >= 0; i--) {
/*  498 */         PdfDictionary subResource = this.resourcesStack.get(i);
/*  499 */         if (subResource != null) {
/*  500 */           PdfObject obj = subResource.getDirectObject(key);
/*  501 */           if (obj != null) return obj; 
/*      */         } 
/*      */       } 
/*  504 */       return super.getDirectObject(key);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class IgnoreOperatorContentOperator
/*      */     implements ContentOperator
/*      */   {
/*      */     private IgnoreOperatorContentOperator() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {}
/*      */   }
/*      */   
/*      */   private static class ShowTextArray
/*      */     implements ContentOperator
/*      */   {
/*      */     private ShowTextArray() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  522 */       PdfArray array = (PdfArray)operands.get(0);
/*  523 */       float tj = 0.0F;
/*  524 */       for (Iterator<PdfObject> i = array.listIterator(); i.hasNext(); ) {
/*  525 */         PdfObject entryObj = i.next();
/*  526 */         if (entryObj instanceof PdfString) {
/*  527 */           processor.displayPdfString((PdfString)entryObj);
/*  528 */           tj = 0.0F; continue;
/*      */         } 
/*  530 */         tj = ((PdfNumber)entryObj).floatValue();
/*  531 */         processor.applyTextAdjust(tj);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MoveNextLineAndShowTextWithSpacing
/*      */     implements ContentOperator
/*      */   {
/*      */     private final PdfContentStreamProcessor.SetTextWordSpacing setTextWordSpacing;
/*      */     
/*      */     private final PdfContentStreamProcessor.SetTextCharacterSpacing setTextCharacterSpacing;
/*      */     
/*      */     private final PdfContentStreamProcessor.MoveNextLineAndShowText moveNextLineAndShowText;
/*      */     
/*      */     public MoveNextLineAndShowTextWithSpacing(PdfContentStreamProcessor.SetTextWordSpacing setTextWordSpacing, PdfContentStreamProcessor.SetTextCharacterSpacing setTextCharacterSpacing, PdfContentStreamProcessor.MoveNextLineAndShowText moveNextLineAndShowText) {
/*  547 */       this.setTextWordSpacing = setTextWordSpacing;
/*  548 */       this.setTextCharacterSpacing = setTextCharacterSpacing;
/*  549 */       this.moveNextLineAndShowText = moveNextLineAndShowText;
/*      */     }
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  553 */       PdfNumber aw = (PdfNumber)operands.get(0);
/*  554 */       PdfNumber ac = (PdfNumber)operands.get(1);
/*  555 */       PdfString string = (PdfString)operands.get(2);
/*      */       
/*  557 */       ArrayList<PdfObject> twOperands = new ArrayList<PdfObject>(1);
/*  558 */       twOperands.add(0, aw);
/*  559 */       this.setTextWordSpacing.invoke(processor, null, twOperands);
/*      */       
/*  561 */       ArrayList<PdfObject> tcOperands = new ArrayList<PdfObject>(1);
/*  562 */       tcOperands.add(0, ac);
/*  563 */       this.setTextCharacterSpacing.invoke(processor, null, tcOperands);
/*      */       
/*  565 */       ArrayList<PdfObject> tickOperands = new ArrayList<PdfObject>(1);
/*  566 */       tickOperands.add(0, string);
/*  567 */       this.moveNextLineAndShowText.invoke(processor, null, tickOperands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class MoveNextLineAndShowText
/*      */     implements ContentOperator
/*      */   {
/*      */     private final PdfContentStreamProcessor.TextMoveNextLine textMoveNextLine;
/*      */     private final PdfContentStreamProcessor.ShowText showText;
/*      */     
/*      */     public MoveNextLineAndShowText(PdfContentStreamProcessor.TextMoveNextLine textMoveNextLine, PdfContentStreamProcessor.ShowText showText) {
/*  578 */       this.textMoveNextLine = textMoveNextLine;
/*  579 */       this.showText = showText;
/*      */     }
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  583 */       this.textMoveNextLine.invoke(processor, null, new ArrayList<PdfObject>(0));
/*  584 */       this.showText.invoke(processor, null, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ShowText
/*      */     implements ContentOperator {
/*      */     private ShowText() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  593 */       PdfString string = (PdfString)operands.get(0);
/*      */       
/*  595 */       processor.displayPdfString(string);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TextMoveNextLine
/*      */     implements ContentOperator
/*      */   {
/*      */     private final PdfContentStreamProcessor.TextMoveStartNextLine moveStartNextLine;
/*      */     
/*      */     public TextMoveNextLine(PdfContentStreamProcessor.TextMoveStartNextLine moveStartNextLine) {
/*  606 */       this.moveStartNextLine = moveStartNextLine;
/*      */     }
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  610 */       ArrayList<PdfObject> tdoperands = new ArrayList<PdfObject>(2);
/*  611 */       tdoperands.add(0, new PdfNumber(0));
/*  612 */       tdoperands.add(1, new PdfNumber(-(processor.gs()).leading));
/*  613 */       this.moveStartNextLine.invoke(processor, null, tdoperands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TextSetTextMatrix
/*      */     implements ContentOperator {
/*      */     private TextSetTextMatrix() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  622 */       float a = ((PdfNumber)operands.get(0)).floatValue();
/*  623 */       float b = ((PdfNumber)operands.get(1)).floatValue();
/*  624 */       float c = ((PdfNumber)operands.get(2)).floatValue();
/*  625 */       float d = ((PdfNumber)operands.get(3)).floatValue();
/*  626 */       float e = ((PdfNumber)operands.get(4)).floatValue();
/*  627 */       float f = ((PdfNumber)operands.get(5)).floatValue();
/*      */       
/*  629 */       processor.textLineMatrix = new Matrix(a, b, c, d, e, f);
/*  630 */       processor.textMatrix = processor.textLineMatrix;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TextMoveStartNextLineWithLeading
/*      */     implements ContentOperator
/*      */   {
/*      */     private final PdfContentStreamProcessor.TextMoveStartNextLine moveStartNextLine;
/*      */     private final PdfContentStreamProcessor.SetTextLeading setTextLeading;
/*      */     
/*      */     public TextMoveStartNextLineWithLeading(PdfContentStreamProcessor.TextMoveStartNextLine moveStartNextLine, PdfContentStreamProcessor.SetTextLeading setTextLeading) {
/*  641 */       this.moveStartNextLine = moveStartNextLine;
/*  642 */       this.setTextLeading = setTextLeading;
/*      */     }
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  645 */       float ty = ((PdfNumber)operands.get(1)).floatValue();
/*      */       
/*  647 */       ArrayList<PdfObject> tlOperands = new ArrayList<PdfObject>(1);
/*  648 */       tlOperands.add(0, new PdfNumber(-ty));
/*  649 */       this.setTextLeading.invoke(processor, null, tlOperands);
/*  650 */       this.moveStartNextLine.invoke(processor, null, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TextMoveStartNextLine
/*      */     implements ContentOperator {
/*      */     private TextMoveStartNextLine() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  659 */       float tx = ((PdfNumber)operands.get(0)).floatValue();
/*  660 */       float ty = ((PdfNumber)operands.get(1)).floatValue();
/*      */       
/*  662 */       Matrix translationMatrix = new Matrix(tx, ty);
/*  663 */       processor.textMatrix = translationMatrix.multiply(processor.textLineMatrix);
/*  664 */       processor.textLineMatrix = processor.textMatrix;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextFont implements ContentOperator {
/*      */     private SetTextFont() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*      */       CMapAwareDocumentFont font;
/*  673 */       PdfName fontResourceName = (PdfName)operands.get(0);
/*  674 */       float size = ((PdfNumber)operands.get(1)).floatValue();
/*      */       
/*  676 */       PdfDictionary fontsDictionary = processor.resources.getAsDict(PdfName.FONT);
/*      */       
/*  678 */       PdfObject fontObject = fontsDictionary.get(fontResourceName);
/*  679 */       if (fontObject instanceof PdfDictionary) {
/*  680 */         font = processor.getFont((PdfDictionary)fontObject);
/*      */       } else {
/*  682 */         font = processor.getFont((PRIndirectReference)fontObject);
/*      */       } 
/*  684 */       (processor.gs()).font = font;
/*  685 */       (processor.gs()).fontSize = size;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextRenderMode
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetTextRenderMode() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  695 */       PdfNumber render = (PdfNumber)operands.get(0);
/*  696 */       (processor.gs()).renderMode = render.intValue();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextRise
/*      */     implements ContentOperator {
/*      */     private SetTextRise() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  705 */       PdfNumber rise = (PdfNumber)operands.get(0);
/*  706 */       (processor.gs()).rise = rise.floatValue();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextLeading
/*      */     implements ContentOperator {
/*      */     private SetTextLeading() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  715 */       PdfNumber leading = (PdfNumber)operands.get(0);
/*  716 */       (processor.gs()).leading = leading.floatValue();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextHorizontalScaling
/*      */     implements ContentOperator {
/*      */     private SetTextHorizontalScaling() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  725 */       PdfNumber scale = (PdfNumber)operands.get(0);
/*  726 */       (processor.gs()).horizontalScaling = scale.floatValue() / 100.0F;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextCharacterSpacing
/*      */     implements ContentOperator {
/*      */     private SetTextCharacterSpacing() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  735 */       PdfNumber charSpace = (PdfNumber)operands.get(0);
/*  736 */       (processor.gs()).characterSpacing = charSpace.floatValue();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetTextWordSpacing
/*      */     implements ContentOperator {
/*      */     private SetTextWordSpacing() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  745 */       PdfNumber wordSpace = (PdfNumber)operands.get(0);
/*  746 */       (processor.gs()).wordSpacing = wordSpace.floatValue();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ProcessGraphicsStateResource
/*      */     implements ContentOperator
/*      */   {
/*      */     private ProcessGraphicsStateResource() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  756 */       PdfName dictionaryName = (PdfName)operands.get(0);
/*  757 */       PdfDictionary extGState = processor.resources.getAsDict(PdfName.EXTGSTATE);
/*  758 */       if (extGState == null)
/*  759 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("resources.do.not.contain.extgstate.entry.unable.to.process.operator.1", new Object[] { operator })); 
/*  760 */       PdfDictionary gsDic = extGState.getAsDict(dictionaryName);
/*  761 */       if (gsDic == null) {
/*  762 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.an.unknown.graphics.state.dictionary", new Object[] { dictionaryName }));
/*      */       }
/*      */       
/*  765 */       PdfArray fontParameter = gsDic.getAsArray(PdfName.FONT);
/*  766 */       if (fontParameter != null) {
/*  767 */         CMapAwareDocumentFont font = processor.getFont((PRIndirectReference)fontParameter.getPdfObject(0));
/*  768 */         float size = fontParameter.getAsNumber(1).floatValue();
/*      */         
/*  770 */         (processor.gs()).font = font;
/*  771 */         (processor.gs()).fontSize = size;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PushGraphicsState
/*      */     implements ContentOperator {
/*      */     private PushGraphicsState() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  781 */       GraphicsState gs = processor.gsStack.peek();
/*  782 */       GraphicsState copy = new GraphicsState(gs);
/*  783 */       processor.gsStack.push(copy);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ModifyCurrentTransformationMatrix
/*      */     implements ContentOperator {
/*      */     private ModifyCurrentTransformationMatrix() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  792 */       float a = ((PdfNumber)operands.get(0)).floatValue();
/*  793 */       float b = ((PdfNumber)operands.get(1)).floatValue();
/*  794 */       float c = ((PdfNumber)operands.get(2)).floatValue();
/*  795 */       float d = ((PdfNumber)operands.get(3)).floatValue();
/*  796 */       float e = ((PdfNumber)operands.get(4)).floatValue();
/*  797 */       float f = ((PdfNumber)operands.get(5)).floatValue();
/*  798 */       Matrix matrix = new Matrix(a, b, c, d, e, f);
/*  799 */       GraphicsState gs = processor.gsStack.peek();
/*  800 */       gs.ctm = matrix.multiply(gs.ctm);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BaseColor getColor(PdfName colorSpace, List<PdfObject> operands) {
/*  808 */     if (PdfName.DEVICEGRAY.equals(colorSpace)) {
/*  809 */       return getColor(1, operands);
/*      */     }
/*  811 */     if (PdfName.DEVICERGB.equals(colorSpace)) {
/*  812 */       return getColor(3, operands);
/*      */     }
/*  814 */     if (PdfName.DEVICECMYK.equals(colorSpace)) {
/*  815 */       return getColor(4, operands);
/*      */     }
/*  817 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BaseColor getColor(int nOperands, List<PdfObject> operands) {
/*  824 */     float[] c = new float[nOperands];
/*  825 */     for (int i = 0; i < nOperands; i++) {
/*  826 */       c[i] = ((PdfNumber)operands.get(i)).floatValue();
/*      */       
/*  828 */       if (c[i] > 1.0F) {
/*  829 */         c[i] = 1.0F;
/*      */       }
/*  831 */       else if (c[i] < 0.0F) {
/*  832 */         c[i] = 0.0F;
/*      */       } 
/*      */     } 
/*  835 */     switch (nOperands) {
/*      */       case 1:
/*  837 */         return (BaseColor)new GrayColor(c[0]);
/*      */       case 3:
/*  839 */         return new BaseColor(c[0], c[1], c[2]);
/*      */       case 4:
/*  841 */         return (BaseColor)new CMYKColor(c[0], c[1], c[2], c[3]);
/*      */     } 
/*  843 */     return null;
/*      */   }
/*      */   
/*      */   private static class SetGrayFill
/*      */     implements ContentOperator {
/*      */     private SetGrayFill() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  851 */       (processor.gs()).fillColor = PdfContentStreamProcessor.getColor(1, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetGrayStroke
/*      */     implements ContentOperator {
/*      */     private SetGrayStroke() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  860 */       (processor.gs()).strokeColor = PdfContentStreamProcessor.getColor(1, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetRGBFill
/*      */     implements ContentOperator {
/*      */     private SetRGBFill() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  869 */       (processor.gs()).fillColor = PdfContentStreamProcessor.getColor(3, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetRGBStroke
/*      */     implements ContentOperator {
/*      */     private SetRGBStroke() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  878 */       (processor.gs()).strokeColor = PdfContentStreamProcessor.getColor(3, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetCMYKFill
/*      */     implements ContentOperator {
/*      */     private SetCMYKFill() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  887 */       (processor.gs()).fillColor = PdfContentStreamProcessor.getColor(4, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetCMYKStroke
/*      */     implements ContentOperator {
/*      */     private SetCMYKStroke() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  896 */       (processor.gs()).strokeColor = PdfContentStreamProcessor.getColor(4, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetColorSpaceFill
/*      */     implements ContentOperator {
/*      */     private SetColorSpaceFill() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  905 */       (processor.gs()).colorSpaceFill = (PdfName)operands.get(0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetColorSpaceStroke
/*      */     implements ContentOperator {
/*      */     private SetColorSpaceStroke() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  914 */       (processor.gs()).colorSpaceStroke = (PdfName)operands.get(0);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetColorFill
/*      */     implements ContentOperator {
/*      */     private SetColorFill() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  923 */       (processor.gs()).fillColor = PdfContentStreamProcessor.getColor((processor.gs()).colorSpaceFill, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetColorStroke
/*      */     implements ContentOperator {
/*      */     private SetColorStroke() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  932 */       (processor.gs()).strokeColor = PdfContentStreamProcessor.getColor((processor.gs()).colorSpaceStroke, operands);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PopGraphicsState
/*      */     implements ContentOperator {
/*      */     private PopGraphicsState() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  941 */       processor.gsStack.pop();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BeginText
/*      */     implements ContentOperator {
/*      */     private BeginText() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  950 */       processor.textMatrix = new Matrix();
/*  951 */       processor.textLineMatrix = processor.textMatrix;
/*  952 */       processor.beginText();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class EndText
/*      */     implements ContentOperator {
/*      */     private EndText() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
/*  961 */       processor.textMatrix = null;
/*  962 */       processor.textLineMatrix = null;
/*  963 */       processor.endText();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class BeginMarkedContent
/*      */     implements ContentOperator
/*      */   {
/*      */     private BeginMarkedContent() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/*  976 */       processor.beginMarkedContent((PdfName)operands.get(0), new PdfDictionary());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class BeginMarkedContentDictionary
/*      */     implements ContentOperator
/*      */   {
/*      */     private BeginMarkedContentDictionary() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/*  991 */       PdfObject properties = operands.get(1);
/*      */       
/*  993 */       processor.beginMarkedContent((PdfName)operands.get(0), getPropertiesDictionary(properties, processor.resources));
/*      */     }
/*      */     
/*      */     private PdfDictionary getPropertiesDictionary(PdfObject operand1, PdfContentStreamProcessor.ResourceDictionary resources) {
/*  997 */       if (operand1.isDictionary()) {
/*  998 */         return (PdfDictionary)operand1;
/*      */       }
/* 1000 */       PdfName dictionaryName = (PdfName)operand1;
/* 1001 */       return resources.getAsDict(dictionaryName);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class EndMarkedContent
/*      */     implements ContentOperator
/*      */   {
/*      */     private EndMarkedContent() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1013 */       processor.endMarkedContent();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Do
/*      */     implements ContentOperator {
/*      */     private Do() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws IOException {
/* 1022 */       PdfName xobjectName = (PdfName)operands.get(0);
/* 1023 */       processor.displayXObject(xobjectName);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetLineWidth
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetLineWidth() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral oper, ArrayList<PdfObject> operands) {
/* 1033 */       float lineWidth = ((PdfNumber)operands.get(0)).floatValue();
/* 1034 */       processor.gs().setLineWidth(lineWidth);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetLineCap
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetLineCap() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral oper, ArrayList<PdfObject> operands) {
/* 1044 */       int lineCap = ((PdfNumber)operands.get(0)).intValue();
/* 1045 */       processor.gs().setLineCapStyle(lineCap);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetLineJoin
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetLineJoin() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral oper, ArrayList<PdfObject> operands) {
/* 1055 */       int lineJoin = ((PdfNumber)operands.get(0)).intValue();
/* 1056 */       processor.gs().setLineJoinStyle(lineJoin);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SetMiterLimit
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetMiterLimit() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral oper, ArrayList<PdfObject> operands) {
/* 1066 */       float miterLimit = ((PdfNumber)operands.get(0)).floatValue();
/* 1067 */       processor.gs().setMiterLimit(miterLimit);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class SetLineDashPattern
/*      */     implements ContentOperator
/*      */   {
/*      */     private SetLineDashPattern() {}
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral oper, ArrayList<PdfObject> operands) {
/* 1078 */       LineDashPattern pattern = new LineDashPattern((PdfArray)operands.get(0), ((PdfNumber)operands.get(1)).floatValue());
/* 1079 */       processor.gs().setLineDashPattern(pattern);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MoveTo
/*      */     implements ContentOperator
/*      */   {
/*      */     private MoveTo() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1091 */       float x = ((PdfNumber)operands.get(0)).floatValue();
/* 1092 */       float y = ((PdfNumber)operands.get(1)).floatValue();
/* 1093 */       processor.modifyPath(1, Arrays.asList(new Float[] { Float.valueOf(x), Float.valueOf(y) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class LineTo
/*      */     implements ContentOperator
/*      */   {
/*      */     private LineTo() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1105 */       float x = ((PdfNumber)operands.get(0)).floatValue();
/* 1106 */       float y = ((PdfNumber)operands.get(1)).floatValue();
/* 1107 */       processor.modifyPath(2, Arrays.asList(new Float[] { Float.valueOf(x), Float.valueOf(y) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Curve
/*      */     implements ContentOperator
/*      */   {
/*      */     private Curve() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1119 */       float x1 = ((PdfNumber)operands.get(0)).floatValue();
/* 1120 */       float y1 = ((PdfNumber)operands.get(1)).floatValue();
/* 1121 */       float x2 = ((PdfNumber)operands.get(2)).floatValue();
/* 1122 */       float y2 = ((PdfNumber)operands.get(3)).floatValue();
/* 1123 */       float x3 = ((PdfNumber)operands.get(4)).floatValue();
/* 1124 */       float y3 = ((PdfNumber)operands.get(5)).floatValue();
/* 1125 */       processor.modifyPath(3, Arrays.asList(new Float[] { Float.valueOf(x1), Float.valueOf(y1), Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(x3), Float.valueOf(y3) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class CurveFirstPointDuplicated
/*      */     implements ContentOperator
/*      */   {
/*      */     private CurveFirstPointDuplicated() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1137 */       float x2 = ((PdfNumber)operands.get(0)).floatValue();
/* 1138 */       float y2 = ((PdfNumber)operands.get(1)).floatValue();
/* 1139 */       float x3 = ((PdfNumber)operands.get(2)).floatValue();
/* 1140 */       float y3 = ((PdfNumber)operands.get(3)).floatValue();
/* 1141 */       processor.modifyPath(4, Arrays.asList(new Float[] { Float.valueOf(x2), Float.valueOf(y2), Float.valueOf(x3), Float.valueOf(y3) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class CurveFourhPointDuplicated
/*      */     implements ContentOperator
/*      */   {
/*      */     private CurveFourhPointDuplicated() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1153 */       float x1 = ((PdfNumber)operands.get(0)).floatValue();
/* 1154 */       float y1 = ((PdfNumber)operands.get(1)).floatValue();
/* 1155 */       float x3 = ((PdfNumber)operands.get(2)).floatValue();
/* 1156 */       float y3 = ((PdfNumber)operands.get(3)).floatValue();
/* 1157 */       processor.modifyPath(5, Arrays.asList(new Float[] { Float.valueOf(x1), Float.valueOf(y1), Float.valueOf(x3), Float.valueOf(y3) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class CloseSubpath
/*      */     implements ContentOperator
/*      */   {
/*      */     private CloseSubpath() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1169 */       processor.modifyPath(6, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Rectangle
/*      */     implements ContentOperator
/*      */   {
/*      */     private Rectangle() {}
/*      */ 
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1181 */       float x = ((PdfNumber)operands.get(0)).floatValue();
/* 1182 */       float y = ((PdfNumber)operands.get(1)).floatValue();
/* 1183 */       float w = ((PdfNumber)operands.get(2)).floatValue();
/* 1184 */       float h = ((PdfNumber)operands.get(3)).floatValue();
/* 1185 */       processor.modifyPath(7, Arrays.asList(new Float[] { Float.valueOf(x), Float.valueOf(y), Float.valueOf(w), Float.valueOf(h) }));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaintPath
/*      */     implements ContentOperator
/*      */   {
/*      */     private int operation;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int rule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean close;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PaintPath(int operation, int rule, boolean close) {
/* 1212 */       this.operation = operation;
/* 1213 */       this.rule = rule;
/* 1214 */       this.close = close;
/*      */     }
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1218 */       processor.paintPath(this.operation, this.rule, this.close);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ClipPath
/*      */     implements ContentOperator
/*      */   {
/*      */     private int rule;
/*      */ 
/*      */ 
/*      */     
/*      */     public ClipPath(int rule) {
/* 1233 */       this.rule = rule;
/*      */     }
/*      */     
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1237 */       processor.clipPath(this.rule);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class EndPath
/*      */     implements ContentOperator
/*      */   {
/*      */     public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
/* 1249 */       processor.paintPath(0, -1, false);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class FormXObjectDoHandler
/*      */     implements XObjectDoHandler {
/*      */     private FormXObjectDoHandler() {}
/*      */     
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream stream, PdfIndirectReference ref) {
/* 1258 */       handleXObject(processor, stream, ref, null);
/*      */     }
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream stream, PdfIndirectReference ref, Stack<MarkedContentInfo> markedContentStack) {
/*      */       byte[] contentBytes;
/* 1262 */       PdfDictionary resources = stream.getAsDict(PdfName.RESOURCES);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1269 */         contentBytes = ContentByteUtils.getContentBytesFromContentObject((PdfObject)stream);
/* 1270 */       } catch (IOException e1) {
/* 1271 */         throw new ExceptionConverter(e1);
/*      */       } 
/* 1273 */       PdfArray matrix = stream.getAsArray(PdfName.MATRIX);
/*      */       
/* 1275 */       (new PdfContentStreamProcessor.PushGraphicsState()).invoke(processor, null, null);
/*      */       
/* 1277 */       if (matrix != null) {
/* 1278 */         float a = matrix.getAsNumber(0).floatValue();
/* 1279 */         float b = matrix.getAsNumber(1).floatValue();
/* 1280 */         float c = matrix.getAsNumber(2).floatValue();
/* 1281 */         float d = matrix.getAsNumber(3).floatValue();
/* 1282 */         float e = matrix.getAsNumber(4).floatValue();
/* 1283 */         float f = matrix.getAsNumber(5).floatValue();
/* 1284 */         Matrix formMatrix = new Matrix(a, b, c, d, e, f);
/*      */         
/* 1286 */         (processor.gs()).ctm = formMatrix.multiply((processor.gs()).ctm);
/*      */       } 
/*      */       
/* 1289 */       processor.processContent(contentBytes, resources);
/*      */       
/* 1291 */       (new PdfContentStreamProcessor.PopGraphicsState()).invoke(processor, null, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ImageXObjectDoHandler
/*      */     implements XObjectDoHandler
/*      */   {
/*      */     private ImageXObjectDoHandler() {}
/*      */ 
/*      */     
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {
/* 1303 */       PdfDictionary colorSpaceDic = processor.resources.getAsDict(PdfName.COLORSPACE);
/* 1304 */       ImageRenderInfo renderInfo = ImageRenderInfo.createForXObject(processor.gs(), ref, colorSpaceDic, null);
/* 1305 */       processor.renderListener.renderImage(renderInfo);
/*      */     }
/*      */     
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref, Stack<MarkedContentInfo> markedContentStack) {
/* 1309 */       PdfDictionary colorSpaceDic = processor.resources.getAsDict(PdfName.COLORSPACE);
/* 1310 */       ImageRenderInfo renderInfo = ImageRenderInfo.createForXObject(processor.gs(), ref, colorSpaceDic, markedContentStack);
/* 1311 */       processor.renderListener.renderImage(renderInfo);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class IgnoreXObjectDoHandler implements XObjectDoHandler {
/*      */     private IgnoreXObjectDoHandler() {}
/*      */     
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {}
/*      */     
/*      */     public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref, Stack<MarkedContentInfo> markedContentStack) {}
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PdfContentStreamProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */