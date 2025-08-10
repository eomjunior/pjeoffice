/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.awt.FontMapper;
/*      */ import com.itextpdf.awt.PdfGraphics2D;
/*      */ import com.itextpdf.awt.PdfPrinterGraphics2D;
/*      */ import com.itextpdf.awt.geom.AffineTransform;
/*      */ import com.itextpdf.awt.geom.Point2D;
/*      */ import com.itextpdf.text.AccessibleElementId;
/*      */ import com.itextpdf.text.Annotation;
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.ImgJBIG2;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import com.itextpdf.text.pdf.internal.PdfAnnotationsImp;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
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
/*      */ public class PdfContentByte
/*      */ {
/*      */   public static final int ALIGN_CENTER = 1;
/*      */   public static final int ALIGN_LEFT = 0;
/*      */   public static final int ALIGN_RIGHT = 2;
/*      */   public static final int LINE_CAP_BUTT = 0;
/*      */   public static final int LINE_CAP_ROUND = 1;
/*      */   public static final int LINE_CAP_PROJECTING_SQUARE = 2;
/*      */   public static final int LINE_JOIN_MITER = 0;
/*      */   public static final int LINE_JOIN_ROUND = 1;
/*      */   public static final int LINE_JOIN_BEVEL = 2;
/*      */   public static final int TEXT_RENDER_MODE_FILL = 0;
/*      */   public static final int TEXT_RENDER_MODE_STROKE = 1;
/*      */   public static final int TEXT_RENDER_MODE_FILL_STROKE = 2;
/*      */   public static final int TEXT_RENDER_MODE_INVISIBLE = 3;
/*      */   public static final int TEXT_RENDER_MODE_FILL_CLIP = 4;
/*      */   public static final int TEXT_RENDER_MODE_STROKE_CLIP = 5;
/*      */   public static final int TEXT_RENDER_MODE_FILL_STROKE_CLIP = 6;
/*      */   public static final int TEXT_RENDER_MODE_CLIP = 7;
/*      */   
/*      */   public static class GraphicState
/*      */   {
/*      */     FontDetails fontDetails;
/*      */     ColorDetails colorDetails;
/*      */     float size;
/*   95 */     protected float xTLM = 0.0F;
/*      */     
/*   97 */     protected float yTLM = 0.0F;
/*      */     
/*   99 */     protected float aTLM = 1.0F;
/*  100 */     protected float bTLM = 0.0F;
/*  101 */     protected float cTLM = 0.0F;
/*  102 */     protected float dTLM = 1.0F;
/*      */     
/*  104 */     protected float tx = 0.0F;
/*      */ 
/*      */     
/*  107 */     protected float leading = 0.0F;
/*      */ 
/*      */     
/*  110 */     protected float scale = 100.0F;
/*      */ 
/*      */     
/*  113 */     protected float charSpace = 0.0F;
/*      */ 
/*      */     
/*  116 */     protected float wordSpace = 0.0F;
/*      */     
/*  118 */     protected BaseColor colorFill = new GrayColor(0);
/*  119 */     protected BaseColor colorStroke = new GrayColor(0);
/*  120 */     protected int textRenderMode = 0;
/*  121 */     protected AffineTransform CTM = new AffineTransform();
/*  122 */     protected PdfObject extGState = null;
/*      */ 
/*      */     
/*      */     GraphicState() {}
/*      */     
/*      */     GraphicState(GraphicState cp) {
/*  128 */       copyParameters(cp);
/*      */     }
/*      */     
/*      */     void copyParameters(GraphicState cp) {
/*  132 */       this.fontDetails = cp.fontDetails;
/*  133 */       this.colorDetails = cp.colorDetails;
/*  134 */       this.size = cp.size;
/*  135 */       this.xTLM = cp.xTLM;
/*  136 */       this.yTLM = cp.yTLM;
/*  137 */       this.aTLM = cp.aTLM;
/*  138 */       this.bTLM = cp.bTLM;
/*  139 */       this.cTLM = cp.cTLM;
/*  140 */       this.dTLM = cp.dTLM;
/*  141 */       this.tx = cp.tx;
/*  142 */       this.leading = cp.leading;
/*  143 */       this.scale = cp.scale;
/*  144 */       this.charSpace = cp.charSpace;
/*  145 */       this.wordSpace = cp.wordSpace;
/*  146 */       this.colorFill = cp.colorFill;
/*  147 */       this.colorStroke = cp.colorStroke;
/*  148 */       this.CTM = new AffineTransform(cp.CTM);
/*  149 */       this.textRenderMode = cp.textRenderMode;
/*  150 */       this.extGState = cp.extGState;
/*      */     }
/*      */     
/*      */     void restore(GraphicState restore) {
/*  154 */       copyParameters(restore);
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
/*  198 */   private static final float[] unitRect = new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 1.0F, 1.0F };
/*      */ 
/*      */ 
/*      */   
/*  202 */   protected ByteBuffer content = new ByteBuffer();
/*      */   
/*  204 */   protected int markedContentSize = 0;
/*      */ 
/*      */   
/*      */   protected PdfWriter writer;
/*      */ 
/*      */   
/*      */   protected PdfDocument pdf;
/*      */ 
/*      */   
/*  213 */   protected GraphicState state = new GraphicState();
/*      */ 
/*      */   
/*  216 */   protected ArrayList<GraphicState> stateList = new ArrayList<GraphicState>();
/*      */ 
/*      */ 
/*      */   
/*      */   protected ArrayList<Integer> layerDepth;
/*      */ 
/*      */   
/*  223 */   protected int separator = 10;
/*      */   
/*  225 */   private int mcDepth = 0;
/*      */   
/*      */   private boolean inText = false;
/*      */   private boolean suppressTagging = false;
/*  229 */   private static HashMap<PdfName, String> abrev = new HashMap<PdfName, String>();
/*      */   
/*  231 */   private ArrayList<IAccessibleElement> mcElements = new ArrayList<IAccessibleElement>();
/*      */   
/*  233 */   protected PdfContentByte duplicatedFrom = null;
/*      */   
/*      */   static {
/*  236 */     abrev.put(PdfName.BITSPERCOMPONENT, "/BPC ");
/*  237 */     abrev.put(PdfName.COLORSPACE, "/CS ");
/*  238 */     abrev.put(PdfName.DECODE, "/D ");
/*  239 */     abrev.put(PdfName.DECODEPARMS, "/DP ");
/*  240 */     abrev.put(PdfName.FILTER, "/F ");
/*  241 */     abrev.put(PdfName.HEIGHT, "/H ");
/*  242 */     abrev.put(PdfName.IMAGEMASK, "/IM ");
/*  243 */     abrev.put(PdfName.INTENT, "/Intent ");
/*  244 */     abrev.put(PdfName.INTERPOLATE, "/I ");
/*  245 */     abrev.put(PdfName.WIDTH, "/W ");
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
/*      */   public PdfContentByte(PdfWriter wr) {
/*  257 */     if (wr != null) {
/*  258 */       this.writer = wr;
/*  259 */       this.pdf = this.writer.getPdfDocument();
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
/*      */   public String toString() {
/*  273 */     return this.content.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTaggingSuppressed() {
/*  282 */     return this.suppressTagging;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfContentByte setSuppressTagging(boolean suppressTagging) {
/*  291 */     this.suppressTagging = suppressTagging;
/*  292 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTagged() {
/*  300 */     return (this.writer != null && this.writer.isTagged() && !isTaggingSuppressed());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteBuffer getInternalBuffer() {
/*  308 */     return this.content;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toPdf(PdfWriter writer) {
/*  318 */     sanityCheck();
/*  319 */     return this.content.toByteArray();
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
/*      */   public void add(PdfContentByte other) {
/*  331 */     if (other.writer != null && this.writer != other.writer)
/*  332 */       throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.writers.are.you.mixing.two.documents", new Object[0])); 
/*  333 */     this.content.append(other.content);
/*  334 */     this.markedContentSize += other.markedContentSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getXTLM() {
/*  343 */     return this.state.xTLM;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getYTLM() {
/*  352 */     return this.state.yTLM;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getLeading() {
/*  361 */     return this.state.leading;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getCharacterSpacing() {
/*  370 */     return this.state.charSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWordSpacing() {
/*  379 */     return this.state.wordSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getHorizontalScaling() {
/*  388 */     return this.state.scale;
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
/*      */   public void setFlatness(float flatness) {
/*  401 */     setFlatness(flatness);
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
/*      */   public void setFlatness(double flatness) {
/*  414 */     if (flatness >= 0.0D && flatness <= 100.0D) {
/*  415 */       this.content.append(flatness).append(" i").append_i(this.separator);
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
/*      */   public void setLineCap(int style) {
/*  430 */     if (style >= 0 && style <= 2) {
/*  431 */       this.content.append(style).append(" J").append_i(this.separator);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRenderingIntent(PdfName ri) {
/*  441 */     this.content.append(ri.getBytes()).append(" ri").append_i(this.separator);
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
/*      */   public void setLineDash(float phase) {
/*  456 */     setLineDash(phase);
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
/*      */   public void setLineDash(double phase) {
/*  471 */     this.content.append("[] ").append(phase).append(" d").append_i(this.separator);
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
/*      */   public void setLineDash(float unitsOn, float phase) {
/*  487 */     setLineDash(unitsOn, phase);
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
/*      */   public void setLineDash(double unitsOn, double phase) {
/*  503 */     this.content.append("[").append(unitsOn).append("] ").append(phase).append(" d").append_i(this.separator);
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
/*      */   public void setLineDash(float unitsOn, float unitsOff, float phase) {
/*  520 */     setLineDash(unitsOn, unitsOff, phase);
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
/*      */   public void setLineDash(double unitsOn, double unitsOff, double phase) {
/*  537 */     this.content.append("[").append(unitsOn).append(' ').append(unitsOff).append("] ").append(phase).append(" d").append_i(this.separator);
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
/*      */   public final void setLineDash(float[] array, float phase) {
/*  553 */     this.content.append("[");
/*  554 */     for (int i = 0; i < array.length; i++) {
/*  555 */       this.content.append(array[i]);
/*  556 */       if (i < array.length - 1) this.content.append(' '); 
/*      */     } 
/*  558 */     this.content.append("] ").append(phase).append(" d").append_i(this.separator);
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
/*      */   public final void setLineDash(double[] array, double phase) {
/*  574 */     this.content.append("[");
/*  575 */     for (int i = 0; i < array.length; i++) {
/*  576 */       this.content.append(array[i]);
/*  577 */       if (i < array.length - 1) this.content.append(' '); 
/*      */     } 
/*  579 */     this.content.append("] ").append(phase).append(" d").append_i(this.separator);
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
/*      */   public void setLineJoin(int style) {
/*  593 */     if (style >= 0 && style <= 2) {
/*  594 */       this.content.append(style).append(" j").append_i(this.separator);
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
/*      */   public void setLineWidth(float w) {
/*  608 */     setLineWidth(w);
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
/*      */   public void setLineWidth(double w) {
/*  621 */     this.content.append(w).append(" w").append_i(this.separator);
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
/*      */   public void setMiterLimit(float miterLimit) {
/*  636 */     setMiterLimit(miterLimit);
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
/*      */   public void setMiterLimit(double miterLimit) {
/*  651 */     if (miterLimit > 1.0D) {
/*  652 */       this.content.append(miterLimit).append(" M").append_i(this.separator);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clip() {
/*  663 */     if (this.inText && isTagged()) {
/*  664 */       endText();
/*      */     }
/*  666 */     this.content.append("W").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void eoClip() {
/*  675 */     if (this.inText && isTagged()) {
/*  676 */       endText();
/*      */     }
/*  678 */     this.content.append("W*").append_i(this.separator);
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
/*      */   public void setGrayFill(float gray) {
/*  691 */     saveColor(new GrayColor(gray), true);
/*  692 */     this.content.append(gray).append(" g").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetGrayFill() {
/*  700 */     saveColor(new GrayColor(0), true);
/*  701 */     this.content.append("0 g").append_i(this.separator);
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
/*      */   public void setGrayStroke(float gray) {
/*  714 */     saveColor(new GrayColor(gray), false);
/*  715 */     this.content.append(gray).append(" G").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetGrayStroke() {
/*  723 */     saveColor(new GrayColor(0), false);
/*  724 */     this.content.append("0 G").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void HelperRGB(float red, float green, float blue) {
/*  734 */     if (red < 0.0F) {
/*  735 */       red = 0.0F;
/*  736 */     } else if (red > 1.0F) {
/*  737 */       red = 1.0F;
/*  738 */     }  if (green < 0.0F) {
/*  739 */       green = 0.0F;
/*  740 */     } else if (green > 1.0F) {
/*  741 */       green = 1.0F;
/*  742 */     }  if (blue < 0.0F) {
/*  743 */       blue = 0.0F;
/*  744 */     } else if (blue > 1.0F) {
/*  745 */       blue = 1.0F;
/*  746 */     }  this.content.append(red).append(' ').append(green).append(' ').append(blue);
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
/*      */   
/*      */   public void setRGBColorFillF(float red, float green, float blue) {
/*  764 */     saveColor(new BaseColor(red, green, blue), true);
/*  765 */     HelperRGB(red, green, blue);
/*  766 */     this.content.append(" rg").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetRGBColorFill() {
/*  774 */     resetGrayFill();
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
/*      */   
/*      */   public void setRGBColorStrokeF(float red, float green, float blue) {
/*  792 */     saveColor(new BaseColor(red, green, blue), false);
/*  793 */     HelperRGB(red, green, blue);
/*  794 */     this.content.append(" RG").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetRGBColorStroke() {
/*  803 */     resetGrayStroke();
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
/*      */   private void HelperCMYK(float cyan, float magenta, float yellow, float black) {
/*  815 */     if (cyan < 0.0F) {
/*  816 */       cyan = 0.0F;
/*  817 */     } else if (cyan > 1.0F) {
/*  818 */       cyan = 1.0F;
/*  819 */     }  if (magenta < 0.0F) {
/*  820 */       magenta = 0.0F;
/*  821 */     } else if (magenta > 1.0F) {
/*  822 */       magenta = 1.0F;
/*  823 */     }  if (yellow < 0.0F) {
/*  824 */       yellow = 0.0F;
/*  825 */     } else if (yellow > 1.0F) {
/*  826 */       yellow = 1.0F;
/*  827 */     }  if (black < 0.0F) {
/*  828 */       black = 0.0F;
/*  829 */     } else if (black > 1.0F) {
/*  830 */       black = 1.0F;
/*  831 */     }  this.content.append(cyan).append(' ').append(magenta).append(' ').append(yellow).append(' ').append(black);
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
/*      */ 
/*      */   
/*      */   public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
/*  850 */     saveColor(new CMYKColor(cyan, magenta, yellow, black), true);
/*  851 */     HelperCMYK(cyan, magenta, yellow, black);
/*  852 */     this.content.append(" k").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetCMYKColorFill() {
/*  861 */     saveColor(new CMYKColor(0, 0, 0, 1), true);
/*  862 */     this.content.append("0 0 0 1 k").append_i(this.separator);
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
/*      */ 
/*      */   
/*      */   public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
/*  881 */     saveColor(new CMYKColor(cyan, magenta, yellow, black), false);
/*  882 */     HelperCMYK(cyan, magenta, yellow, black);
/*  883 */     this.content.append(" K").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetCMYKColorStroke() {
/*  892 */     saveColor(new CMYKColor(0, 0, 0, 1), false);
/*  893 */     this.content.append("0 0 0 1 K").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveTo(float x, float y) {
/*  904 */     moveTo(x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveTo(double x, double y) {
/*  915 */     if (this.inText) {
/*  916 */       if (isTagged()) {
/*  917 */         endText();
/*      */       } else {
/*  919 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/*  922 */     this.content.append(x).append(' ').append(y).append(" m").append_i(this.separator);
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
/*      */   public void lineTo(float x, float y) {
/*  934 */     lineTo(x, y);
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
/*      */   public void lineTo(double x, double y) {
/*  946 */     if (this.inText) {
/*  947 */       if (isTagged()) {
/*  948 */         endText();
/*      */       } else {
/*  950 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/*  953 */     this.content.append(x).append(' ').append(y).append(" l").append_i(this.separator);
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
/*      */   public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
/*  968 */     curveTo(x1, y1, x2, y2, x3, y3);
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
/*      */   public void curveTo(double x1, double y1, double x2, double y2, double x3, double y3) {
/*  983 */     if (this.inText) {
/*  984 */       if (isTagged()) {
/*  985 */         endText();
/*      */       } else {
/*  987 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/*  990 */     this.content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" c").append_i(this.separator);
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
/*      */   public void curveTo(float x2, float y2, float x3, float y3) {
/* 1003 */     curveTo(x2, y2, x3, y3);
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
/*      */   public void curveTo(double x2, double y2, double x3, double y3) {
/* 1015 */     if (this.inText) {
/* 1016 */       if (isTagged()) {
/* 1017 */         endText();
/*      */       } else {
/* 1019 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1022 */     this.content.append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" v").append_i(this.separator);
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
/*      */   public void curveFromTo(float x1, float y1, float x3, float y3) {
/* 1035 */     curveFromTo(x1, y1, x3, y3);
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
/*      */   public void curveFromTo(double x1, double y1, double x3, double y3) {
/* 1048 */     if (this.inText) {
/* 1049 */       if (isTagged()) {
/* 1050 */         endText();
/*      */       } else {
/* 1052 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1055 */     this.content.append(x1).append(' ').append(y1).append(' ').append(x3).append(' ').append(y3).append(" y").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void circle(float x, float y, float r) {
/* 1065 */     circle(x, y, r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void circle(double x, double y, double r) {
/* 1075 */     float b = 0.5523F;
/* 1076 */     moveTo(x + r, y);
/* 1077 */     curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r);
/* 1078 */     curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y);
/* 1079 */     curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r);
/* 1080 */     curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
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
/*      */   public void rectangle(float x, float y, float w, float h) {
/* 1093 */     rectangle(x, y, w, h);
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
/*      */   public void rectangle(double x, double y, double w, double h) {
/* 1106 */     if (this.inText) {
/* 1107 */       if (isTagged()) {
/* 1108 */         endText();
/*      */       } else {
/* 1110 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1113 */     this.content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re").append_i(this.separator);
/*      */   }
/*      */   
/*      */   private boolean compareColors(BaseColor c1, BaseColor c2) {
/* 1117 */     if (c1 == null && c2 == null)
/* 1118 */       return true; 
/* 1119 */     if (c1 == null || c2 == null)
/* 1120 */       return false; 
/* 1121 */     if (c1 instanceof ExtendedColor)
/* 1122 */       return c1.equals(c2); 
/* 1123 */     return c2.equals(c1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void variableRectangle(Rectangle rect) {
/* 1133 */     float t = rect.getTop();
/* 1134 */     float b = rect.getBottom();
/* 1135 */     float r = rect.getRight();
/* 1136 */     float l = rect.getLeft();
/* 1137 */     float wt = rect.getBorderWidthTop();
/* 1138 */     float wb = rect.getBorderWidthBottom();
/* 1139 */     float wr = rect.getBorderWidthRight();
/* 1140 */     float wl = rect.getBorderWidthLeft();
/* 1141 */     BaseColor ct = rect.getBorderColorTop();
/* 1142 */     BaseColor cb = rect.getBorderColorBottom();
/* 1143 */     BaseColor cr = rect.getBorderColorRight();
/* 1144 */     BaseColor cl = rect.getBorderColorLeft();
/* 1145 */     saveState();
/* 1146 */     setLineCap(0);
/* 1147 */     setLineJoin(0);
/* 1148 */     float clw = 0.0F;
/* 1149 */     boolean cdef = false;
/* 1150 */     BaseColor ccol = null;
/* 1151 */     boolean cdefi = false;
/* 1152 */     BaseColor cfil = null;
/*      */     
/* 1154 */     if (wt > 0.0F) {
/* 1155 */       setLineWidth(clw = wt);
/* 1156 */       cdef = true;
/* 1157 */       if (ct == null) {
/* 1158 */         resetRGBColorStroke();
/*      */       } else {
/* 1160 */         setColorStroke(ct);
/* 1161 */       }  ccol = ct;
/* 1162 */       moveTo(l, t - wt / 2.0F);
/* 1163 */       lineTo(r, t - wt / 2.0F);
/* 1164 */       stroke();
/*      */     } 
/*      */ 
/*      */     
/* 1168 */     if (wb > 0.0F) {
/* 1169 */       if (wb != clw)
/* 1170 */         setLineWidth(clw = wb); 
/* 1171 */       if (!cdef || !compareColors(ccol, cb)) {
/* 1172 */         cdef = true;
/* 1173 */         if (cb == null) {
/* 1174 */           resetRGBColorStroke();
/*      */         } else {
/* 1176 */           setColorStroke(cb);
/* 1177 */         }  ccol = cb;
/*      */       } 
/* 1179 */       moveTo(r, b + wb / 2.0F);
/* 1180 */       lineTo(l, b + wb / 2.0F);
/* 1181 */       stroke();
/*      */     } 
/*      */ 
/*      */     
/* 1185 */     if (wr > 0.0F) {
/* 1186 */       if (wr != clw)
/* 1187 */         setLineWidth(clw = wr); 
/* 1188 */       if (!cdef || !compareColors(ccol, cr)) {
/* 1189 */         cdef = true;
/* 1190 */         if (cr == null) {
/* 1191 */           resetRGBColorStroke();
/*      */         } else {
/* 1193 */           setColorStroke(cr);
/* 1194 */         }  ccol = cr;
/*      */       } 
/* 1196 */       boolean bt = compareColors(ct, cr);
/* 1197 */       boolean bb = compareColors(cb, cr);
/* 1198 */       moveTo(r - wr / 2.0F, bt ? t : (t - wt));
/* 1199 */       lineTo(r - wr / 2.0F, bb ? b : (b + wb));
/* 1200 */       stroke();
/* 1201 */       if (!bt || !bb) {
/* 1202 */         cdefi = true;
/* 1203 */         if (cr == null) {
/* 1204 */           resetRGBColorFill();
/*      */         } else {
/* 1206 */           setColorFill(cr);
/* 1207 */         }  cfil = cr;
/* 1208 */         if (!bt) {
/* 1209 */           moveTo(r, t);
/* 1210 */           lineTo(r, t - wt);
/* 1211 */           lineTo(r - wr, t - wt);
/* 1212 */           fill();
/*      */         } 
/* 1214 */         if (!bb) {
/* 1215 */           moveTo(r, b);
/* 1216 */           lineTo(r, b + wb);
/* 1217 */           lineTo(r - wr, b + wb);
/* 1218 */           fill();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1224 */     if (wl > 0.0F) {
/* 1225 */       if (wl != clw)
/* 1226 */         setLineWidth(wl); 
/* 1227 */       if (!cdef || !compareColors(ccol, cl))
/* 1228 */         if (cl == null) {
/* 1229 */           resetRGBColorStroke();
/*      */         } else {
/* 1231 */           setColorStroke(cl);
/*      */         }  
/* 1233 */       boolean bt = compareColors(ct, cl);
/* 1234 */       boolean bb = compareColors(cb, cl);
/* 1235 */       moveTo(l + wl / 2.0F, bt ? t : (t - wt));
/* 1236 */       lineTo(l + wl / 2.0F, bb ? b : (b + wb));
/* 1237 */       stroke();
/* 1238 */       if (!bt || !bb) {
/* 1239 */         if (!cdefi || !compareColors(cfil, cl))
/* 1240 */           if (cl == null) {
/* 1241 */             resetRGBColorFill();
/*      */           } else {
/* 1243 */             setColorFill(cl);
/*      */           }  
/* 1245 */         if (!bt) {
/* 1246 */           moveTo(l, t);
/* 1247 */           lineTo(l, t - wt);
/* 1248 */           lineTo(l + wl, t - wt);
/* 1249 */           fill();
/*      */         } 
/* 1251 */         if (!bb) {
/* 1252 */           moveTo(l, b);
/* 1253 */           lineTo(l, b + wb);
/* 1254 */           lineTo(l + wl, b + wb);
/* 1255 */           fill();
/*      */         } 
/*      */       } 
/*      */     } 
/* 1259 */     restoreState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rectangle(Rectangle rectangle) {
/* 1270 */     float x1 = rectangle.getLeft();
/* 1271 */     float y1 = rectangle.getBottom();
/* 1272 */     float x2 = rectangle.getRight();
/* 1273 */     float y2 = rectangle.getTop();
/*      */ 
/*      */     
/* 1276 */     BaseColor background = rectangle.getBackgroundColor();
/* 1277 */     if (background != null) {
/* 1278 */       saveState();
/* 1279 */       setColorFill(background);
/* 1280 */       rectangle(x1, y1, x2 - x1, y2 - y1);
/* 1281 */       fill();
/* 1282 */       restoreState();
/*      */     } 
/*      */ 
/*      */     
/* 1286 */     if (!rectangle.hasBorders()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1293 */     if (rectangle.isUseVariableBorders()) {
/* 1294 */       variableRectangle(rectangle);
/*      */     }
/*      */     else {
/*      */       
/* 1298 */       if (rectangle.getBorderWidth() != -1.0F) {
/* 1299 */         setLineWidth(rectangle.getBorderWidth());
/*      */       }
/*      */ 
/*      */       
/* 1303 */       BaseColor color = rectangle.getBorderColor();
/* 1304 */       if (color != null) {
/* 1305 */         setColorStroke(color);
/*      */       }
/*      */ 
/*      */       
/* 1309 */       if (rectangle.hasBorder(15)) {
/* 1310 */         rectangle(x1, y1, x2 - x1, y2 - y1);
/*      */       }
/*      */       else {
/*      */         
/* 1314 */         if (rectangle.hasBorder(8)) {
/* 1315 */           moveTo(x2, y1);
/* 1316 */           lineTo(x2, y2);
/*      */         } 
/* 1318 */         if (rectangle.hasBorder(4)) {
/* 1319 */           moveTo(x1, y1);
/* 1320 */           lineTo(x1, y2);
/*      */         } 
/* 1322 */         if (rectangle.hasBorder(2)) {
/* 1323 */           moveTo(x1, y1);
/* 1324 */           lineTo(x2, y1);
/*      */         } 
/* 1326 */         if (rectangle.hasBorder(1)) {
/* 1327 */           moveTo(x1, y2);
/* 1328 */           lineTo(x2, y2);
/*      */         } 
/*      */       } 
/*      */       
/* 1332 */       stroke();
/*      */       
/* 1334 */       if (color != null) {
/* 1335 */         resetRGBColorStroke();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void closePath() {
/* 1346 */     if (this.inText) {
/* 1347 */       if (isTagged()) {
/* 1348 */         endText();
/*      */       } else {
/* 1350 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1353 */     this.content.append("h").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void newPath() {
/* 1361 */     if (this.inText) {
/* 1362 */       if (isTagged()) {
/* 1363 */         endText();
/*      */       } else {
/* 1365 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1368 */     this.content.append("n").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stroke() {
/* 1376 */     if (this.inText) {
/* 1377 */       if (isTagged()) {
/* 1378 */         endText();
/*      */       } else {
/* 1380 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1383 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1384 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1385 */     this.content.append("S").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void closePathStroke() {
/* 1393 */     if (this.inText) {
/* 1394 */       if (isTagged()) {
/* 1395 */         endText();
/*      */       } else {
/* 1397 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1400 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1401 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1402 */     this.content.append("s").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fill() {
/* 1410 */     if (this.inText) {
/* 1411 */       if (isTagged()) {
/* 1412 */         endText();
/*      */       } else {
/* 1414 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1417 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1418 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1419 */     this.content.append("f").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void eoFill() {
/* 1427 */     if (this.inText) {
/* 1428 */       if (isTagged()) {
/* 1429 */         endText();
/*      */       } else {
/* 1431 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1434 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1435 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1436 */     this.content.append("f*").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillStroke() {
/* 1444 */     if (this.inText) {
/* 1445 */       if (isTagged()) {
/* 1446 */         endText();
/*      */       } else {
/* 1448 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1451 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1452 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1453 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1454 */     this.content.append("B").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void closePathFillStroke() {
/* 1462 */     if (this.inText) {
/* 1463 */       if (isTagged()) {
/* 1464 */         endText();
/*      */       } else {
/* 1466 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1469 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1470 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1471 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1472 */     this.content.append("b").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void eoFillStroke() {
/* 1480 */     if (this.inText) {
/* 1481 */       if (isTagged()) {
/* 1482 */         endText();
/*      */       } else {
/* 1484 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1487 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1488 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1489 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1490 */     this.content.append("B*").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void closePathEoFillStroke() {
/* 1498 */     if (this.inText) {
/* 1499 */       if (isTagged()) {
/* 1500 */         endText();
/*      */       } else {
/* 1502 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("path.construction.operator.inside.text.object", new Object[0]));
/*      */       } 
/*      */     }
/* 1505 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/* 1506 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/* 1507 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
/* 1508 */     this.content.append("b*").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addImage(Image image) throws DocumentException {
/* 1518 */     addImage(image, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addImage(Image image, boolean inlineImage) throws DocumentException {
/* 1529 */     if (!image.hasAbsoluteY())
/* 1530 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.image.must.have.absolute.positioning", new Object[0])); 
/* 1531 */     float[] matrix = image.matrix();
/* 1532 */     matrix[4] = image.getAbsoluteX() - matrix[4];
/* 1533 */     matrix[5] = image.getAbsoluteY() - matrix[5];
/* 1534 */     addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], inlineImage);
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
/*      */   public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
/* 1551 */     addImage(image, a, b, c, d, e, f, false);
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
/*      */   public void addImage(Image image, double a, double b, double c, double d, double e, double f) throws DocumentException {
/* 1568 */     addImage(image, a, b, c, d, e, f, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addImage(Image image, AffineTransform transform) throws DocumentException {
/* 1577 */     double[] matrix = new double[6];
/* 1578 */     transform.getMatrix(matrix);
/* 1579 */     addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], false);
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
/*      */ 
/*      */   
/*      */   public void addImage(Image image, float a, float b, float c, float d, float e, float f, boolean inlineImage) throws DocumentException {
/* 1598 */     addImage(image, a, b, c, d, e, f, inlineImage);
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
/*      */   
/*      */   public void addImage(Image image, double a, double b, double c, double d, double e, double f, boolean inlineImage) throws DocumentException {
/* 1616 */     addImage(image, a, b, c, d, e, f, inlineImage, false);
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addImage(Image image, double a, double b, double c, double d, double e, double f, boolean inlineImage, boolean isMCBlockOpened) throws DocumentException {
/*      */     try {
/* 1637 */       AffineTransform transform = new AffineTransform(a, b, c, d, e, f);
/*      */       
/* 1639 */       if (image.getLayer() != null)
/* 1640 */         beginLayer(image.getLayer()); 
/* 1641 */       if (isTagged()) {
/* 1642 */         if (this.inText)
/* 1643 */           endText(); 
/* 1644 */         Point2D.Float[] arrayOfFloat1 = { new Point2D.Float(0.0F, 0.0F), new Point2D.Float(1.0F, 0.0F), new Point2D.Float(1.0F, 1.0F), new Point2D.Float(0.0F, 1.0F) };
/* 1645 */         Point2D.Float[] arrayOfFloat2 = new Point2D.Float[4];
/* 1646 */         transform.transform((Point2D[])arrayOfFloat1, 0, (Point2D[])arrayOfFloat2, 0, 4);
/* 1647 */         float left = Float.MAX_VALUE;
/* 1648 */         float right = -3.4028235E38F;
/* 1649 */         float bottom = Float.MAX_VALUE;
/* 1650 */         float top = -3.4028235E38F;
/* 1651 */         for (int j = 0; j < 4; j++) {
/* 1652 */           if (arrayOfFloat2[j].getX() < left)
/* 1653 */             left = (float)arrayOfFloat2[j].getX(); 
/* 1654 */           if (arrayOfFloat2[j].getX() > right)
/* 1655 */             right = (float)arrayOfFloat2[j].getX(); 
/* 1656 */           if (arrayOfFloat2[j].getY() < bottom)
/* 1657 */             bottom = (float)arrayOfFloat2[j].getY(); 
/* 1658 */           if (arrayOfFloat2[j].getY() > top)
/* 1659 */             top = (float)arrayOfFloat2[j].getY(); 
/*      */         } 
/* 1661 */         image.setAccessibleAttribute(PdfName.BBOX, new PdfArray(new float[] { left, bottom, right, top }));
/*      */       } 
/* 1663 */       if (this.writer != null && image.isImgTemplate()) {
/* 1664 */         this.writer.addDirectImageSimple(image);
/* 1665 */         PdfTemplate template = image.getTemplateData();
/* 1666 */         if (image.getAccessibleAttributes() != null) {
/* 1667 */           for (PdfName key : image.getAccessibleAttributes().keySet()) {
/* 1668 */             template.setAccessibleAttribute(key, image.getAccessibleAttribute(key));
/*      */           }
/*      */         }
/* 1671 */         float w = template.getWidth();
/* 1672 */         float h = template.getHeight();
/* 1673 */         addTemplate(template, a / w, b / w, c / h, d / h, e, f, false, false);
/*      */       } else {
/*      */         
/* 1676 */         this.content.append("q ");
/*      */         
/* 1678 */         if (!transform.isIdentity()) {
/* 1679 */           this.content.append(a).append(' ');
/* 1680 */           this.content.append(b).append(' ');
/* 1681 */           this.content.append(c).append(' ');
/* 1682 */           this.content.append(d).append(' ');
/* 1683 */           this.content.append(e).append(' ');
/* 1684 */           this.content.append(f).append(" cm");
/*      */         } 
/*      */         
/* 1687 */         if (inlineImage) {
/* 1688 */           this.content.append("\nBI\n");
/* 1689 */           PdfImage pimage = new PdfImage(image, "", null);
/* 1690 */           if (image instanceof ImgJBIG2) {
/* 1691 */             byte[] globals = ((ImgJBIG2)image).getGlobalBytes();
/* 1692 */             if (globals != null) {
/* 1693 */               PdfDictionary decodeparms = new PdfDictionary();
/* 1694 */               decodeparms.put(PdfName.JBIG2GLOBALS, this.writer.getReferenceJBIG2Globals(globals));
/* 1695 */               pimage.put(PdfName.DECODEPARMS, decodeparms);
/*      */             } 
/*      */           } 
/* 1698 */           PdfWriter.checkPdfIsoConformance(this.writer, 17, pimage);
/* 1699 */           for (PdfName element : pimage.getKeys()) {
/* 1700 */             PdfName key = element;
/* 1701 */             PdfObject value = pimage.get(key);
/* 1702 */             String s = abrev.get(key);
/* 1703 */             if (s == null)
/*      */               continue; 
/* 1705 */             this.content.append(s);
/* 1706 */             boolean check = true;
/* 1707 */             if (key.equals(PdfName.COLORSPACE) && value.isArray()) {
/* 1708 */               PdfArray ar = (PdfArray)value;
/* 1709 */               if (ar.size() == 4 && PdfName.INDEXED
/* 1710 */                 .equals(ar.getAsName(0)) && ar
/* 1711 */                 .getPdfObject(1).isName() && ar
/* 1712 */                 .getPdfObject(2).isNumber() && ar
/* 1713 */                 .getPdfObject(3).isString())
/*      */               {
/* 1715 */                 check = false;
/*      */               }
/*      */             } 
/*      */             
/* 1719 */             if (check && key.equals(PdfName.COLORSPACE) && !value.isName()) {
/* 1720 */               PdfName cs = this.writer.getColorspaceName();
/* 1721 */               PageResources prs = getPageResources();
/* 1722 */               prs.addColor(cs, this.writer.addToBody(value).getIndirectReference());
/* 1723 */               value = cs;
/*      */             } 
/* 1725 */             value.toPdf(null, this.content);
/* 1726 */             this.content.append('\n');
/*      */           } 
/* 1728 */           ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 1729 */           pimage.writeContent(baos);
/* 1730 */           byte[] imageBytes = baos.toByteArray();
/* 1731 */           this.content.append(String.format("/L %s\n", new Object[] { Integer.valueOf(imageBytes.length) }));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1737 */           this.content.append("ID\n");
/* 1738 */           this.content.append(imageBytes);
/* 1739 */           this.content.append("\nEI\nQ").append_i(this.separator);
/*      */         }
/*      */         else {
/*      */           
/* 1743 */           PageResources prs = getPageResources();
/* 1744 */           Image maskImage = image.getImageMask();
/* 1745 */           if (maskImage != null) {
/* 1746 */             PdfName pdfName = this.writer.addDirectImageSimple(maskImage);
/* 1747 */             prs.addXObject(pdfName, this.writer.getImageReference(pdfName));
/*      */           } 
/* 1749 */           PdfName name = this.writer.addDirectImageSimple(image);
/* 1750 */           name = prs.addXObject(name, this.writer.getImageReference(name));
/* 1751 */           this.content.append(' ').append(name.getBytes()).append(" Do Q").append_i(this.separator);
/*      */         } 
/*      */       } 
/* 1754 */       if (image.hasBorders()) {
/* 1755 */         saveState();
/* 1756 */         float w = image.getWidth();
/* 1757 */         float h = image.getHeight();
/* 1758 */         concatCTM(a / w, b / w, c / h, d / h, e, f);
/* 1759 */         rectangle((Rectangle)image);
/* 1760 */         restoreState();
/*      */       } 
/* 1762 */       if (image.getLayer() != null)
/* 1763 */         endLayer(); 
/* 1764 */       Annotation annot = image.getAnnotation();
/* 1765 */       if (annot == null)
/*      */         return; 
/* 1767 */       double[] r = new double[unitRect.length];
/* 1768 */       for (int k = 0; k < unitRect.length; k += 2) {
/* 1769 */         r[k] = a * unitRect[k] + c * unitRect[k + 1] + e;
/* 1770 */         r[k + 1] = b * unitRect[k] + d * unitRect[k + 1] + f;
/*      */       } 
/* 1772 */       double llx = r[0];
/* 1773 */       double lly = r[1];
/* 1774 */       double urx = llx;
/* 1775 */       double ury = lly;
/* 1776 */       for (int i = 2; i < r.length; i += 2) {
/* 1777 */         llx = Math.min(llx, r[i]);
/* 1778 */         lly = Math.min(lly, r[i + 1]);
/* 1779 */         urx = Math.max(urx, r[i]);
/* 1780 */         ury = Math.max(ury, r[i + 1]);
/*      */       } 
/* 1782 */       annot = new Annotation(annot);
/* 1783 */       annot.setDimensions((float)llx, (float)lly, (float)urx, (float)ury);
/* 1784 */       PdfAnnotation an = PdfAnnotationsImp.convertAnnotation(this.writer, annot, new Rectangle((float)llx, (float)lly, (float)urx, (float)ury));
/* 1785 */       if (an == null)
/*      */         return; 
/* 1787 */       addAnnotation(an);
/*      */     }
/* 1789 */     catch (IOException ioe) {
/*      */       
/* 1791 */       String path = (image != null && image.getUrl() != null) ? image.getUrl().getPath() : MessageLocalization.getComposedMessage("unknown", new Object[0]);
/* 1792 */       throw new DocumentException(MessageLocalization.getComposedMessage("add.image.exception", new Object[] { path }), ioe);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/* 1801 */     reset(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset(boolean validateContent) {
/* 1810 */     this.content.reset();
/* 1811 */     this.markedContentSize = 0;
/* 1812 */     if (validateContent) {
/* 1813 */       sanityCheck();
/*      */     }
/* 1815 */     this.state = new GraphicState();
/* 1816 */     this.stateList = new ArrayList<GraphicState>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void beginText(boolean restoreTM) {
/* 1825 */     if (this.inText) {
/* 1826 */       if (!isTagged())
/*      */       {
/*      */         
/* 1829 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
/*      */       }
/*      */     } else {
/* 1832 */       this.inText = true;
/* 1833 */       this.content.append("BT").append_i(this.separator);
/* 1834 */       if (restoreTM) {
/* 1835 */         float xTLM = this.state.xTLM;
/* 1836 */         float tx = this.state.tx;
/* 1837 */         setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.tx, this.state.yTLM);
/* 1838 */         this.state.xTLM = xTLM;
/* 1839 */         this.state.tx = tx;
/*      */       } else {
/* 1841 */         this.state.xTLM = 0.0F;
/* 1842 */         this.state.yTLM = 0.0F;
/* 1843 */         this.state.tx = 0.0F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginText() {
/* 1852 */     beginText(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endText() {
/* 1859 */     if (!this.inText) {
/* 1860 */       if (!isTagged())
/*      */       {
/*      */         
/* 1863 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
/*      */       }
/*      */     } else {
/* 1866 */       this.inText = false;
/* 1867 */       this.content.append("ET").append_i(this.separator);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void saveState() {
/* 1876 */     PdfWriter.checkPdfIsoConformance(this.writer, 12, "q");
/* 1877 */     if (this.inText && isTagged()) {
/* 1878 */       endText();
/*      */     }
/* 1880 */     this.content.append("q").append_i(this.separator);
/* 1881 */     this.stateList.add(new GraphicState(this.state));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void restoreState() {
/* 1889 */     PdfWriter.checkPdfIsoConformance(this.writer, 12, "Q");
/* 1890 */     if (this.inText && isTagged()) {
/* 1891 */       endText();
/*      */     }
/* 1893 */     this.content.append("Q").append_i(this.separator);
/* 1894 */     int idx = this.stateList.size() - 1;
/* 1895 */     if (idx < 0)
/* 1896 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators", new Object[0])); 
/* 1897 */     this.state.restore(this.stateList.get(idx));
/* 1898 */     this.stateList.remove(idx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterSpacing(float charSpace) {
/* 1907 */     if (!this.inText && isTagged()) {
/* 1908 */       beginText(true);
/*      */     }
/* 1910 */     this.state.charSpace = charSpace;
/* 1911 */     this.content.append(charSpace).append(" Tc").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWordSpacing(float wordSpace) {
/* 1920 */     if (!this.inText && isTagged()) {
/* 1921 */       beginText(true);
/*      */     }
/* 1923 */     this.state.wordSpace = wordSpace;
/* 1924 */     this.content.append(wordSpace).append(" Tw").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHorizontalScaling(float scale) {
/* 1933 */     if (!this.inText && isTagged()) {
/* 1934 */       beginText(true);
/*      */     }
/* 1936 */     this.state.scale = scale;
/* 1937 */     this.content.append(scale).append(" Tz").append_i(this.separator);
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
/*      */   public void setLeading(float leading) {
/* 1949 */     if (!this.inText && isTagged()) {
/* 1950 */       beginText(true);
/*      */     }
/* 1952 */     this.state.leading = leading;
/* 1953 */     this.content.append(leading).append(" TL").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFontAndSize(BaseFont bf, float size) {
/* 1963 */     if (!this.inText && isTagged()) {
/* 1964 */       beginText(true);
/*      */     }
/* 1966 */     checkWriter();
/* 1967 */     if (size < 1.0E-4F && size > -1.0E-4F)
/* 1968 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("font.size.too.small.1", new Object[] { String.valueOf(size) })); 
/* 1969 */     this.state.size = size;
/* 1970 */     this.state.fontDetails = this.writer.addSimple(bf);
/* 1971 */     PageResources prs = getPageResources();
/* 1972 */     PdfName name = this.state.fontDetails.getFontName();
/* 1973 */     name = prs.addFont(name, this.state.fontDetails.getIndirectReference());
/* 1974 */     this.content.append(name.getBytes()).append(' ').append(size).append(" Tf").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextRenderingMode(int rendering) {
/* 1983 */     if (!this.inText && isTagged()) {
/* 1984 */       beginText(true);
/*      */     }
/* 1986 */     this.state.textRenderMode = rendering;
/* 1987 */     this.content.append(rendering).append(" Tr").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextRise(float rise) {
/* 1998 */     setTextRise(rise);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextRise(double rise) {
/* 2009 */     if (!this.inText && isTagged()) {
/* 2010 */       beginText(true);
/*      */     }
/* 2012 */     this.content.append(rise).append(" Ts").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void showText2(String text) {
/* 2022 */     if (this.state.fontDetails == null)
/* 2023 */       throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0])); 
/* 2024 */     byte[] b = this.state.fontDetails.convertToBytes(text);
/* 2025 */     StringUtils.escapeString(b, this.content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void showText(String text) {
/* 2034 */     checkState();
/* 2035 */     if (!this.inText && isTagged()) {
/* 2036 */       beginText(true);
/*      */     }
/* 2038 */     showText2(text);
/* 2039 */     updateTx(text, 0.0F);
/* 2040 */     this.content.append("Tj").append_i(this.separator);
/*      */   }
/*      */   
/*      */   public void showTextGid(String gids) {
/* 2044 */     checkState();
/* 2045 */     if (!this.inText && isTagged()) {
/* 2046 */       beginText(true);
/*      */     }
/* 2048 */     if (this.state.fontDetails == null)
/* 2049 */       throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0])); 
/* 2050 */     Object[] objs = this.state.fontDetails.convertToBytesGid(gids);
/* 2051 */     StringUtils.escapeString((byte[])objs[0], this.content);
/* 2052 */     this.state.tx += ((Integer)objs[2]).intValue() * 0.001F * this.state.size;
/* 2053 */     this.content.append("Tj").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static PdfTextArray getKernArray(String text, BaseFont font) {
/* 2063 */     PdfTextArray pa = new PdfTextArray();
/* 2064 */     StringBuffer acc = new StringBuffer();
/* 2065 */     int len = text.length() - 1;
/* 2066 */     char[] c = text.toCharArray();
/* 2067 */     if (len >= 0)
/* 2068 */       acc.append(c, 0, 1); 
/* 2069 */     for (int k = 0; k < len; k++) {
/* 2070 */       char c2 = c[k + 1];
/* 2071 */       int kern = font.getKerning(c[k], c2);
/* 2072 */       if (kern == 0) {
/* 2073 */         acc.append(c2);
/*      */       } else {
/*      */         
/* 2076 */         pa.add(acc.toString());
/* 2077 */         acc.setLength(0);
/* 2078 */         acc.append(c, k + 1, 1);
/* 2079 */         pa.add(-kern);
/*      */       } 
/*      */     } 
/* 2082 */     pa.add(acc.toString());
/* 2083 */     return pa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void showTextKerned(String text) {
/* 2092 */     if (this.state.fontDetails == null)
/* 2093 */       throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0])); 
/* 2094 */     BaseFont bf = this.state.fontDetails.getBaseFont();
/* 2095 */     if (bf.hasKernPairs()) {
/* 2096 */       showText(getKernArray(text, bf));
/*      */     } else {
/* 2098 */       showText(text);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void newlineShowText(String text) {
/* 2108 */     checkState();
/* 2109 */     if (!this.inText && isTagged()) {
/* 2110 */       beginText(true);
/*      */     }
/* 2112 */     this.state.yTLM -= this.state.leading;
/* 2113 */     showText2(text);
/* 2114 */     this.content.append("'").append_i(this.separator);
/* 2115 */     this.state.tx = this.state.xTLM;
/* 2116 */     updateTx(text, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void newlineShowText(float wordSpacing, float charSpacing, String text) {
/* 2127 */     checkState();
/* 2128 */     if (!this.inText && isTagged()) {
/* 2129 */       beginText(true);
/*      */     }
/* 2131 */     this.state.yTLM -= this.state.leading;
/* 2132 */     this.content.append(wordSpacing).append(' ').append(charSpacing);
/* 2133 */     showText2(text);
/* 2134 */     this.content.append("\"").append_i(this.separator);
/*      */ 
/*      */     
/* 2137 */     this.state.charSpace = charSpacing;
/* 2138 */     this.state.wordSpace = wordSpacing;
/* 2139 */     this.state.tx = this.state.xTLM;
/* 2140 */     updateTx(text, 0.0F);
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
/*      */   public void setTextMatrix(float a, float b, float c, float d, float x, float y) {
/* 2156 */     if (!this.inText && isTagged()) {
/* 2157 */       beginText(true);
/*      */     }
/* 2159 */     this.state.xTLM = x;
/* 2160 */     this.state.yTLM = y;
/* 2161 */     this.state.aTLM = a;
/* 2162 */     this.state.bTLM = b;
/* 2163 */     this.state.cTLM = c;
/* 2164 */     this.state.dTLM = d;
/* 2165 */     this.state.tx = this.state.xTLM;
/* 2166 */     this.content.append(a).append(' ').append(b).append_i(32)
/* 2167 */       .append(c).append_i(32).append(d).append_i(32)
/* 2168 */       .append(x).append_i(32).append(y).append(" Tm").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextMatrix(AffineTransform transform) {
/* 2177 */     double[] matrix = new double[6];
/* 2178 */     transform.getMatrix(matrix);
/* 2179 */     setTextMatrix((float)matrix[0], (float)matrix[1], (float)matrix[2], (float)matrix[3], (float)matrix[4], (float)matrix[5]);
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
/*      */   public void setTextMatrix(float x, float y) {
/* 2192 */     setTextMatrix(1.0F, 0.0F, 0.0F, 1.0F, x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void moveText(float x, float y) {
/* 2202 */     if (!this.inText && isTagged()) {
/* 2203 */       beginText(true);
/*      */     }
/* 2205 */     this.state.xTLM += x;
/* 2206 */     this.state.yTLM += y;
/* 2207 */     if (isTagged() && this.state.xTLM != this.state.tx) {
/* 2208 */       setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
/*      */     } else {
/* 2210 */       this.content.append(x).append(' ').append(y).append(" Td").append_i(this.separator);
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
/*      */   public void moveTextWithLeading(float x, float y) {
/* 2223 */     if (!this.inText && isTagged()) {
/* 2224 */       beginText(true);
/*      */     }
/* 2226 */     this.state.xTLM += x;
/* 2227 */     this.state.yTLM += y;
/* 2228 */     this.state.leading = -y;
/* 2229 */     if (isTagged() && this.state.xTLM != this.state.tx) {
/* 2230 */       setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
/*      */     } else {
/* 2232 */       this.content.append(x).append(' ').append(y).append(" TD").append_i(this.separator);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void newlineText() {
/* 2240 */     if (!this.inText && isTagged()) {
/* 2241 */       beginText(true);
/*      */     }
/* 2243 */     if (isTagged() && this.state.xTLM != this.state.tx) {
/* 2244 */       setTextMatrix(this.state.aTLM, this.state.bTLM, this.state.cTLM, this.state.dTLM, this.state.xTLM, this.state.yTLM);
/*      */     }
/* 2246 */     this.state.yTLM -= this.state.leading;
/* 2247 */     this.content.append("T*").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int size() {
/* 2256 */     return size(true);
/*      */   }
/*      */   
/*      */   int size(boolean includeMarkedContentSize) {
/* 2260 */     if (includeMarkedContentSize) {
/* 2261 */       return this.content.size();
/*      */     }
/* 2263 */     return this.content.size() - this.markedContentSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addOutline(PdfOutline outline, String name) {
/* 2273 */     checkWriter();
/* 2274 */     this.pdf.addOutline(outline, name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfOutline getRootOutline() {
/* 2282 */     checkWriter();
/* 2283 */     return this.pdf.getRootOutline();
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
/*      */   public float getEffectiveStringWidth(String text, boolean kerned) {
/*      */     float w;
/* 2297 */     BaseFont bf = this.state.fontDetails.getBaseFont();
/*      */ 
/*      */     
/* 2300 */     if (kerned) {
/* 2301 */       w = bf.getWidthPointKerned(text, this.state.size);
/*      */     } else {
/* 2303 */       w = bf.getWidthPoint(text, this.state.size);
/*      */     } 
/* 2305 */     if (this.state.charSpace != 0.0F && text.length() > 1) {
/* 2306 */       w += this.state.charSpace * (text.length() - 1);
/*      */     }
/*      */     
/* 2309 */     if (this.state.wordSpace != 0.0F && !bf.isVertical())
/* 2310 */       for (int i = 0; i < text.length() - 1; i++) {
/* 2311 */         if (text.charAt(i) == ' ') {
/* 2312 */           w += this.state.wordSpace;
/*      */         }
/*      */       }  
/* 2315 */     if (this.state.scale != 100.0D) {
/* 2316 */       w = w * this.state.scale / 100.0F;
/*      */     }
/*      */     
/* 2319 */     return w;
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
/*      */   private float getEffectiveStringWidth(String text, boolean kerned, float kerning) {
/*      */     float w;
/* 2334 */     BaseFont bf = this.state.fontDetails.getBaseFont();
/*      */     
/* 2336 */     if (kerned) {
/* 2337 */       w = bf.getWidthPointKerned(text, this.state.size);
/*      */     } else {
/* 2339 */       w = bf.getWidthPoint(text, this.state.size);
/* 2340 */     }  if (this.state.charSpace != 0.0F && text.length() > 0) {
/* 2341 */       w += this.state.charSpace * text.length();
/*      */     }
/* 2343 */     if (this.state.wordSpace != 0.0F && !bf.isVertical())
/* 2344 */       for (int i = 0; i < text.length(); i++) {
/* 2345 */         if (text.charAt(i) == ' ') {
/* 2346 */           w += this.state.wordSpace;
/*      */         }
/*      */       }  
/* 2349 */     w -= kerning / 1000.0F * this.state.size;
/* 2350 */     if (this.state.scale != 100.0D)
/* 2351 */       w = w * this.state.scale / 100.0F; 
/* 2352 */     return w;
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
/*      */   public void showTextAligned(int alignment, String text, float x, float y, float rotation) {
/* 2364 */     showTextAligned(alignment, text, x, y, rotation, false);
/*      */   }
/*      */   
/*      */   private void showTextAligned(int alignment, String text, float x, float y, float rotation, boolean kerned) {
/* 2368 */     if (this.state.fontDetails == null)
/* 2369 */       throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0])); 
/* 2370 */     if (rotation == 0.0F) {
/* 2371 */       switch (alignment) {
/*      */         case 1:
/* 2373 */           x -= getEffectiveStringWidth(text, kerned) / 2.0F;
/*      */           break;
/*      */         case 2:
/* 2376 */           x -= getEffectiveStringWidth(text, kerned);
/*      */           break;
/*      */       } 
/* 2379 */       setTextMatrix(x, y);
/* 2380 */       if (kerned) {
/* 2381 */         showTextKerned(text);
/*      */       } else {
/* 2383 */         showText(text);
/*      */       } 
/*      */     } else {
/* 2386 */       float len; double alpha = rotation * Math.PI / 180.0D;
/* 2387 */       float cos = (float)Math.cos(alpha);
/* 2388 */       float sin = (float)Math.sin(alpha);
/*      */       
/* 2390 */       switch (alignment) {
/*      */         case 1:
/* 2392 */           len = getEffectiveStringWidth(text, kerned) / 2.0F;
/* 2393 */           x -= len * cos;
/* 2394 */           y -= len * sin;
/*      */           break;
/*      */         case 2:
/* 2397 */           len = getEffectiveStringWidth(text, kerned);
/* 2398 */           x -= len * cos;
/* 2399 */           y -= len * sin;
/*      */           break;
/*      */       } 
/* 2402 */       setTextMatrix(cos, sin, -sin, cos, x, y);
/* 2403 */       if (kerned) {
/* 2404 */         showTextKerned(text);
/*      */       } else {
/* 2406 */         showText(text);
/* 2407 */       }  setTextMatrix(0.0F, 0.0F);
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
/*      */   public void showTextAlignedKerned(int alignment, String text, float x, float y, float rotation) {
/* 2420 */     showTextAligned(alignment, text, x, y, rotation, true);
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
/*      */   public void concatCTM(float a, float b, float c, float d, float e, float f) {
/* 2448 */     concatCTM(a, b, c, d, e, f);
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
/*      */   public void concatCTM(double a, double b, double c, double d, double e, double f) {
/* 2476 */     if (this.inText && isTagged()) {
/* 2477 */       endText();
/*      */     }
/* 2479 */     this.state.CTM.concatenate(new AffineTransform(a, b, c, d, e, f));
/* 2480 */     this.content.append(a).append(' ').append(b).append(' ').append(c).append(' ');
/* 2481 */     this.content.append(d).append(' ').append(e).append(' ').append(f).append(" cm").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void concatCTM(AffineTransform transform) {
/* 2489 */     double[] matrix = new double[6];
/* 2490 */     transform.getMatrix(matrix);
/* 2491 */     concatCTM(matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
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
/*      */   public static ArrayList<double[]> bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent) {
/* 2520 */     return bezierArc(x1, y1, x2, y2, startAng, extent);
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
/*      */   public static ArrayList<double[]> bezierArc(double x1, double y1, double x2, double y2, double startAng, double extent) {
/*      */     double fragAngle;
/*      */     int Nfrag;
/* 2549 */     if (x1 > x2) {
/* 2550 */       double tmp = x1;
/* 2551 */       x1 = x2;
/* 2552 */       x2 = tmp;
/*      */     } 
/* 2554 */     if (y2 > y1) {
/* 2555 */       double tmp = y1;
/* 2556 */       y1 = y2;
/* 2557 */       y2 = tmp;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2562 */     if (Math.abs(extent) <= 90.0D) {
/* 2563 */       fragAngle = extent;
/* 2564 */       Nfrag = 1;
/*      */     } else {
/*      */       
/* 2567 */       Nfrag = (int)Math.ceil(Math.abs(extent) / 90.0D);
/* 2568 */       fragAngle = extent / Nfrag;
/*      */     } 
/* 2570 */     double x_cen = (x1 + x2) / 2.0D;
/* 2571 */     double y_cen = (y1 + y2) / 2.0D;
/* 2572 */     double rx = (x2 - x1) / 2.0D;
/* 2573 */     double ry = (y2 - y1) / 2.0D;
/* 2574 */     double halfAng = fragAngle * Math.PI / 360.0D;
/* 2575 */     double kappa = Math.abs(1.3333333333333333D * (1.0D - Math.cos(halfAng)) / Math.sin(halfAng));
/* 2576 */     ArrayList<double[]> pointList = (ArrayList)new ArrayList<double>();
/* 2577 */     for (int i = 0; i < Nfrag; i++) {
/* 2578 */       double theta0 = (startAng + i * fragAngle) * Math.PI / 180.0D;
/* 2579 */       double theta1 = (startAng + (i + 1) * fragAngle) * Math.PI / 180.0D;
/* 2580 */       double cos0 = Math.cos(theta0);
/* 2581 */       double cos1 = Math.cos(theta1);
/* 2582 */       double sin0 = Math.sin(theta0);
/* 2583 */       double sin1 = Math.sin(theta1);
/* 2584 */       if (fragAngle > 0.0D) {
/* 2585 */         pointList.add(new double[] { x_cen + rx * cos0, y_cen - ry * sin0, x_cen + rx * (cos0 - kappa * sin0), y_cen - ry * (sin0 + kappa * cos0), x_cen + rx * (cos1 + kappa * sin1), y_cen - ry * (sin1 - kappa * cos1), x_cen + rx * cos1, y_cen - ry * sin1 });
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2595 */         pointList.add(new double[] { x_cen + rx * cos0, y_cen - ry * sin0, x_cen + rx * (cos0 + kappa * sin0), y_cen - ry * (sin0 - kappa * cos0), x_cen + rx * (cos1 - kappa * sin1), y_cen - ry * (sin1 + kappa * cos1), x_cen + rx * cos1, y_cen - ry * sin1 });
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2605 */     return pointList;
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
/*      */   public void arc(float x1, float y1, float x2, float y2, float startAng, float extent) {
/* 2621 */     arc(x1, y1, x2, y2, startAng, extent);
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
/*      */   public void arc(double x1, double y1, double x2, double y2, double startAng, double extent) {
/* 2637 */     ArrayList<double[]> ar = bezierArc(x1, y1, x2, y2, startAng, extent);
/* 2638 */     if (ar.isEmpty())
/*      */       return; 
/* 2640 */     double[] pt = ar.get(0);
/* 2641 */     moveTo(pt[0], pt[1]);
/* 2642 */     for (int k = 0; k < ar.size(); k++) {
/* 2643 */       pt = ar.get(k);
/* 2644 */       curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
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
/*      */   public void ellipse(float x1, float y1, float x2, float y2) {
/* 2657 */     ellipse(x1, y1, x2, y2);
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
/*      */   public void ellipse(double x1, double y1, double x2, double y2) {
/* 2669 */     arc(x1, y1, x2, y2, 0.0D, 360.0D);
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
/*      */   public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep) {
/* 2684 */     checkWriter();
/* 2685 */     if (xstep == 0.0F || ystep == 0.0F)
/* 2686 */       throw new RuntimeException(MessageLocalization.getComposedMessage("xstep.or.ystep.can.not.be.zero", new Object[0])); 
/* 2687 */     PdfPatternPainter painter = new PdfPatternPainter(this.writer);
/* 2688 */     painter.setWidth(width);
/* 2689 */     painter.setHeight(height);
/* 2690 */     painter.setXStep(xstep);
/* 2691 */     painter.setYStep(ystep);
/* 2692 */     this.writer.addSimplePattern(painter);
/* 2693 */     return painter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfPatternPainter createPattern(float width, float height) {
/* 2704 */     return createPattern(width, height, width, height);
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
/*      */   public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep, BaseColor color) {
/* 2720 */     checkWriter();
/* 2721 */     if (xstep == 0.0F || ystep == 0.0F)
/* 2722 */       throw new RuntimeException(MessageLocalization.getComposedMessage("xstep.or.ystep.can.not.be.zero", new Object[0])); 
/* 2723 */     PdfPatternPainter painter = new PdfPatternPainter(this.writer, color);
/* 2724 */     painter.setWidth(width);
/* 2725 */     painter.setHeight(height);
/* 2726 */     painter.setXStep(xstep);
/* 2727 */     painter.setYStep(ystep);
/* 2728 */     this.writer.addSimplePattern(painter);
/* 2729 */     return painter;
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
/*      */   public PdfPatternPainter createPattern(float width, float height, BaseColor color) {
/* 2742 */     return createPattern(width, height, width, height, color);
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
/*      */   public PdfTemplate createTemplate(float width, float height) {
/* 2758 */     return createTemplate(width, height, null);
/*      */   }
/*      */   
/*      */   PdfTemplate createTemplate(float width, float height, PdfName forcedName) {
/* 2762 */     checkWriter();
/* 2763 */     PdfTemplate template = new PdfTemplate(this.writer);
/* 2764 */     template.setWidth(width);
/* 2765 */     template.setHeight(height);
/* 2766 */     this.writer.addDirectTemplateSimple(template, forcedName);
/* 2767 */     return template;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfAppearance createAppearance(float width, float height) {
/* 2778 */     return createAppearance(width, height, null);
/*      */   }
/*      */   
/*      */   PdfAppearance createAppearance(float width, float height, PdfName forcedName) {
/* 2782 */     checkWriter();
/* 2783 */     PdfAppearance template = new PdfAppearance(this.writer);
/* 2784 */     template.setWidth(width);
/* 2785 */     template.setHeight(height);
/* 2786 */     this.writer.addDirectTemplateSimple(template, forcedName);
/* 2787 */     return template;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPSXObject(PdfPSXObject psobject) {
/* 2796 */     if (this.inText && isTagged()) {
/* 2797 */       endText();
/*      */     }
/* 2799 */     checkWriter();
/* 2800 */     PdfName name = this.writer.addDirectTemplateSimple(psobject, null);
/* 2801 */     PageResources prs = getPageResources();
/* 2802 */     name = prs.addXObject(name, psobject.getIndirectReference());
/* 2803 */     this.content.append(name.getBytes()).append(" Do").append_i(this.separator);
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
/*      */   public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
/* 2818 */     addTemplate(template, a, b, c, d, e, f, false);
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
/*      */   public void addTemplate(PdfTemplate template, double a, double b, double c, double d, double e, double f) {
/* 2833 */     addTemplate(template, a, b, c, d, e, f, false);
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
/*      */   public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f, boolean tagContent) {
/* 2850 */     addTemplate(template, a, b, c, d, e, f, tagContent);
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
/*      */   public void addTemplate(PdfTemplate template, double a, double b, double c, double d, double e, double f, boolean tagContent) {
/* 2867 */     addTemplate(template, a, b, c, d, e, f, true, tagContent);
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
/*      */   
/*      */   private void addTemplate(PdfTemplate template, double a, double b, double c, double d, double e, double f, boolean tagTemplate, boolean tagContent) {
/* 2885 */     checkWriter();
/* 2886 */     checkNoPattern(template);
/* 2887 */     PdfWriter.checkPdfIsoConformance(this.writer, 20, template);
/* 2888 */     PdfName name = this.writer.addDirectTemplateSimple(template, null);
/* 2889 */     PageResources prs = getPageResources();
/* 2890 */     name = prs.addXObject(name, template.getIndirectReference());
/* 2891 */     if (isTagged() && tagTemplate) {
/* 2892 */       if (this.inText)
/* 2893 */         endText(); 
/* 2894 */       if (template.isContentTagged() || (template.getPageReference() != null && tagContent)) {
/* 2895 */         throw new RuntimeException(MessageLocalization.getComposedMessage("template.with.tagged.could.not.be.used.more.than.once", new Object[0]));
/*      */       }
/*      */       
/* 2898 */       template.setPageReference(this.writer.getCurrentPage());
/*      */       
/* 2900 */       if (tagContent) {
/* 2901 */         template.setContentTagged(true);
/* 2902 */         ensureDocumentTagIsOpen();
/* 2903 */         ArrayList<IAccessibleElement> allMcElements = getMcElements();
/* 2904 */         if (allMcElements != null && allMcElements.size() > 0)
/* 2905 */           template.getMcElements().add(allMcElements.get(allMcElements.size() - 1)); 
/*      */       } else {
/* 2907 */         openMCBlock(template);
/*      */       } 
/*      */     } 
/*      */     
/* 2911 */     this.content.append("q ");
/* 2912 */     this.content.append(a).append(' ');
/* 2913 */     this.content.append(b).append(' ');
/* 2914 */     this.content.append(c).append(' ');
/* 2915 */     this.content.append(d).append(' ');
/* 2916 */     this.content.append(e).append(' ');
/* 2917 */     this.content.append(f).append(" cm ");
/* 2918 */     this.content.append(name.getBytes()).append(" Do Q").append_i(this.separator);
/*      */     
/* 2920 */     if (isTagged() && tagTemplate && !tagContent) {
/* 2921 */       closeMCBlock(template);
/* 2922 */       template.setId((AccessibleElementId)null);
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
/*      */   
/*      */   public PdfName addFormXObj(PdfStream formXObj, PdfName name, float a, float b, float c, float d, float e, float f) throws IOException {
/* 2941 */     return addFormXObj(formXObj, name, a, b, c, d, e, f);
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
/*      */   
/*      */   public PdfName addFormXObj(PdfStream formXObj, PdfName name, double a, double b, double c, double d, double e, double f) throws IOException {
/* 2959 */     checkWriter();
/* 2960 */     PdfWriter.checkPdfIsoConformance(this.writer, 9, formXObj);
/* 2961 */     PageResources prs = getPageResources();
/* 2962 */     PdfName translatedName = prs.addXObject(name, this.writer.addToBody(formXObj).getIndirectReference());
/* 2963 */     PdfArtifact artifact = null;
/* 2964 */     if (isTagged()) {
/* 2965 */       if (this.inText)
/* 2966 */         endText(); 
/* 2967 */       artifact = new PdfArtifact();
/* 2968 */       openMCBlock(artifact);
/*      */     } 
/*      */     
/* 2971 */     this.content.append("q ");
/* 2972 */     this.content.append(a).append(' ');
/* 2973 */     this.content.append(b).append(' ');
/* 2974 */     this.content.append(c).append(' ');
/* 2975 */     this.content.append(d).append(' ');
/* 2976 */     this.content.append(e).append(' ');
/* 2977 */     this.content.append(f).append(" cm ");
/* 2978 */     this.content.append(translatedName.getBytes()).append(" Do Q").append_i(this.separator);
/*      */     
/* 2980 */     if (isTagged()) {
/* 2981 */       closeMCBlock(artifact);
/*      */     }
/*      */     
/* 2984 */     return translatedName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTemplate(PdfTemplate template, AffineTransform transform) {
/* 2993 */     addTemplate(template, transform, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTemplate(PdfTemplate template, AffineTransform transform, boolean tagContent) {
/* 3004 */     double[] matrix = new double[6];
/* 3005 */     transform.getMatrix(matrix);
/* 3006 */     addTemplate(template, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], tagContent);
/*      */   }
/*      */ 
/*      */   
/*      */   void addTemplateReference(PdfIndirectReference template, PdfName name, float a, float b, float c, float d, float e, float f) {
/* 3011 */     addTemplateReference(template, name, a, b, c, d, e, f);
/*      */   }
/*      */   
/*      */   void addTemplateReference(PdfIndirectReference template, PdfName name, double a, double b, double c, double d, double e, double f) {
/* 3015 */     if (this.inText && isTagged()) {
/* 3016 */       endText();
/*      */     }
/* 3018 */     checkWriter();
/* 3019 */     PageResources prs = getPageResources();
/* 3020 */     name = prs.addXObject(name, template);
/* 3021 */     this.content.append("q ");
/* 3022 */     this.content.append(a).append(' ');
/* 3023 */     this.content.append(b).append(' ');
/* 3024 */     this.content.append(c).append(' ');
/* 3025 */     this.content.append(d).append(' ');
/* 3026 */     this.content.append(e).append(' ');
/* 3027 */     this.content.append(f).append(" cm ");
/* 3028 */     this.content.append(name.getBytes()).append(" Do Q").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTemplate(PdfTemplate template, float x, float y) {
/* 3039 */     addTemplate(template, 1.0F, 0.0F, 0.0F, 1.0F, x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTemplate(PdfTemplate template, double x, double y) {
/* 3050 */     addTemplate(template, 1.0D, 0.0D, 0.0D, 1.0D, x, y);
/*      */   }
/*      */   
/*      */   public void addTemplate(PdfTemplate template, float x, float y, boolean tagContent) {
/* 3054 */     addTemplate(template, 1.0F, 0.0F, 0.0F, 1.0F, x, y, tagContent);
/*      */   }
/*      */   
/*      */   public void addTemplate(PdfTemplate template, double x, double y, boolean tagContent) {
/* 3058 */     addTemplate(template, 1.0D, 0.0D, 0.0D, 1.0D, x, y, tagContent);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
/* 3080 */     saveColor(new CMYKColor(cyan, magenta, yellow, black), true);
/* 3081 */     this.content.append((cyan & 0xFF) / 255.0F);
/* 3082 */     this.content.append(' ');
/* 3083 */     this.content.append((magenta & 0xFF) / 255.0F);
/* 3084 */     this.content.append(' ');
/* 3085 */     this.content.append((yellow & 0xFF) / 255.0F);
/* 3086 */     this.content.append(' ');
/* 3087 */     this.content.append((black & 0xFF) / 255.0F);
/* 3088 */     this.content.append(" k").append_i(this.separator);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
/* 3108 */     saveColor(new CMYKColor(cyan, magenta, yellow, black), false);
/* 3109 */     this.content.append((cyan & 0xFF) / 255.0F);
/* 3110 */     this.content.append(' ');
/* 3111 */     this.content.append((magenta & 0xFF) / 255.0F);
/* 3112 */     this.content.append(' ');
/* 3113 */     this.content.append((yellow & 0xFF) / 255.0F);
/* 3114 */     this.content.append(' ');
/* 3115 */     this.content.append((black & 0xFF) / 255.0F);
/* 3116 */     this.content.append(" K").append_i(this.separator);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRGBColorFill(int red, int green, int blue) {
/* 3137 */     saveColor(new BaseColor(red, green, blue), true);
/* 3138 */     HelperRGB((red & 0xFF) / 255.0F, (green & 0xFF) / 255.0F, (blue & 0xFF) / 255.0F);
/* 3139 */     this.content.append(" rg").append_i(this.separator);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRGBColorStroke(int red, int green, int blue) {
/* 3159 */     saveColor(new BaseColor(red, green, blue), false);
/* 3160 */     HelperRGB((red & 0xFF) / 255.0F, (green & 0xFF) / 255.0F, (blue & 0xFF) / 255.0F);
/* 3161 */     this.content.append(" RG").append_i(this.separator);
/*      */   } public void setColorStroke(BaseColor color) {
/*      */     CMYKColor cmyk;
/*      */     SpotColor spot;
/*      */     PatternColor pat;
/*      */     ShadingColor shading;
/*      */     DeviceNColor devicen;
/*      */     LabColor lab;
/* 3169 */     int type = ExtendedColor.getType(color);
/* 3170 */     switch (type) {
/*      */       case 1:
/* 3172 */         setGrayStroke(((GrayColor)color).getGray());
/*      */         break;
/*      */       
/*      */       case 2:
/* 3176 */         cmyk = (CMYKColor)color;
/* 3177 */         setCMYKColorStrokeF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
/*      */         break;
/*      */       
/*      */       case 3:
/* 3181 */         spot = (SpotColor)color;
/* 3182 */         setColorStroke(spot.getPdfSpotColor(), spot.getTint());
/*      */         break;
/*      */       
/*      */       case 4:
/* 3186 */         pat = (PatternColor)color;
/* 3187 */         setPatternStroke(pat.getPainter());
/*      */         break;
/*      */       
/*      */       case 5:
/* 3191 */         shading = (ShadingColor)color;
/* 3192 */         setShadingStroke(shading.getPdfShadingPattern());
/*      */         break;
/*      */       
/*      */       case 6:
/* 3196 */         devicen = (DeviceNColor)color;
/* 3197 */         setColorStroke(devicen.getPdfDeviceNColor(), devicen.getTints());
/*      */         break;
/*      */       
/*      */       case 7:
/* 3201 */         lab = (LabColor)color;
/* 3202 */         setColorStroke(lab.getLabColorSpace(), lab.getL(), lab.getA(), lab.getB());
/*      */         break;
/*      */       
/*      */       default:
/* 3206 */         setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
/*      */         break;
/*      */     } 
/* 3209 */     int alpha = color.getAlpha();
/* 3210 */     if (alpha < 255) {
/* 3211 */       PdfGState gState = new PdfGState();
/* 3212 */       gState.setStrokeOpacity(alpha / 255.0F);
/* 3213 */       setGState(gState);
/*      */     } 
/*      */   } public void setColorFill(BaseColor color) {
/*      */     CMYKColor cmyk;
/*      */     SpotColor spot;
/*      */     PatternColor pat;
/*      */     ShadingColor shading;
/*      */     DeviceNColor devicen;
/*      */     LabColor lab;
/* 3222 */     int type = ExtendedColor.getType(color);
/* 3223 */     switch (type) {
/*      */       case 1:
/* 3225 */         setGrayFill(((GrayColor)color).getGray());
/*      */         break;
/*      */       
/*      */       case 2:
/* 3229 */         cmyk = (CMYKColor)color;
/* 3230 */         setCMYKColorFillF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
/*      */         break;
/*      */       
/*      */       case 3:
/* 3234 */         spot = (SpotColor)color;
/* 3235 */         setColorFill(spot.getPdfSpotColor(), spot.getTint());
/*      */         break;
/*      */       
/*      */       case 4:
/* 3239 */         pat = (PatternColor)color;
/* 3240 */         setPatternFill(pat.getPainter());
/*      */         break;
/*      */       
/*      */       case 5:
/* 3244 */         shading = (ShadingColor)color;
/* 3245 */         setShadingFill(shading.getPdfShadingPattern());
/*      */         break;
/*      */       
/*      */       case 6:
/* 3249 */         devicen = (DeviceNColor)color;
/* 3250 */         setColorFill(devicen.getPdfDeviceNColor(), devicen.getTints());
/*      */         break;
/*      */       
/*      */       case 7:
/* 3254 */         lab = (LabColor)color;
/* 3255 */         setColorFill(lab.getLabColorSpace(), lab.getL(), lab.getA(), lab.getB());
/*      */         break;
/*      */       
/*      */       default:
/* 3259 */         setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
/*      */         break;
/*      */     } 
/* 3262 */     int alpha = color.getAlpha();
/* 3263 */     if (alpha < 255) {
/* 3264 */       PdfGState gState = new PdfGState();
/* 3265 */       gState.setFillOpacity(alpha / 255.0F);
/* 3266 */       setGState(gState);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColorFill(PdfSpotColor sp, float tint) {
/* 3276 */     checkWriter();
/* 3277 */     this.state.colorDetails = this.writer.addSimple(sp);
/* 3278 */     PageResources prs = getPageResources();
/* 3279 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3280 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3281 */     saveColor(new SpotColor(sp, tint), true);
/* 3282 */     this.content.append(name.getBytes()).append(" cs ").append(tint).append(" scn").append_i(this.separator);
/*      */   }
/*      */   
/*      */   public void setColorFill(PdfDeviceNColor dn, float[] tints) {
/* 3286 */     checkWriter();
/* 3287 */     this.state.colorDetails = this.writer.addSimple(dn);
/* 3288 */     PageResources prs = getPageResources();
/* 3289 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3290 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3291 */     saveColor(new DeviceNColor(dn, tints), true);
/* 3292 */     this.content.append(name.getBytes()).append(" cs ");
/* 3293 */     for (float tint : tints)
/* 3294 */       this.content.append(tint + " "); 
/* 3295 */     this.content.append("scn").append_i(this.separator);
/*      */   }
/*      */   
/*      */   public void setColorFill(PdfLabColor lab, float l, float a, float b) {
/* 3299 */     checkWriter();
/* 3300 */     this.state.colorDetails = this.writer.addSimple(lab);
/* 3301 */     PageResources prs = getPageResources();
/* 3302 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3303 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3304 */     saveColor(new LabColor(lab, l, a, b), true);
/* 3305 */     this.content.append(name.getBytes()).append(" cs ");
/* 3306 */     this.content.append(l + " " + a + " " + b + " ");
/* 3307 */     this.content.append("scn").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColorStroke(PdfSpotColor sp, float tint) {
/* 3316 */     checkWriter();
/* 3317 */     this.state.colorDetails = this.writer.addSimple(sp);
/* 3318 */     PageResources prs = getPageResources();
/* 3319 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3320 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3321 */     saveColor(new SpotColor(sp, tint), false);
/* 3322 */     this.content.append(name.getBytes()).append(" CS ").append(tint).append(" SCN").append_i(this.separator);
/*      */   }
/*      */   
/*      */   public void setColorStroke(PdfDeviceNColor sp, float[] tints) {
/* 3326 */     checkWriter();
/* 3327 */     this.state.colorDetails = this.writer.addSimple(sp);
/* 3328 */     PageResources prs = getPageResources();
/* 3329 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3330 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3331 */     saveColor(new DeviceNColor(sp, tints), true);
/* 3332 */     this.content.append(name.getBytes()).append(" CS ");
/* 3333 */     for (float tint : tints)
/* 3334 */       this.content.append(tint + " "); 
/* 3335 */     this.content.append("SCN").append_i(this.separator);
/*      */   }
/*      */   
/*      */   public void setColorStroke(PdfLabColor lab, float l, float a, float b) {
/* 3339 */     checkWriter();
/* 3340 */     this.state.colorDetails = this.writer.addSimple(lab);
/* 3341 */     PageResources prs = getPageResources();
/* 3342 */     PdfName name = this.state.colorDetails.getColorSpaceName();
/* 3343 */     name = prs.addColor(name, this.state.colorDetails.getIndirectReference());
/* 3344 */     saveColor(new LabColor(lab, l, a, b), true);
/* 3345 */     this.content.append(name.getBytes()).append(" CS ");
/* 3346 */     this.content.append(l + " " + a + " " + b + " ");
/* 3347 */     this.content.append("SCN").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternFill(PdfPatternPainter p) {
/* 3355 */     if (p.isStencil()) {
/* 3356 */       setPatternFill(p, p.getDefaultColor());
/*      */       return;
/*      */     } 
/* 3359 */     checkWriter();
/* 3360 */     PageResources prs = getPageResources();
/* 3361 */     PdfName name = this.writer.addSimplePattern(p);
/* 3362 */     name = prs.addPattern(name, p.getIndirectReference());
/* 3363 */     saveColor(new PatternColor(p), true);
/* 3364 */     this.content.append(PdfName.PATTERN.getBytes()).append(" cs ").append(name.getBytes()).append(" scn").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void outputColorNumbers(BaseColor color, float tint) {
/*      */     CMYKColor cmyk;
/* 3372 */     PdfWriter.checkPdfIsoConformance(this.writer, 1, color);
/* 3373 */     int type = ExtendedColor.getType(color);
/* 3374 */     switch (type) {
/*      */       case 0:
/* 3376 */         this.content.append(color.getRed() / 255.0F);
/* 3377 */         this.content.append(' ');
/* 3378 */         this.content.append(color.getGreen() / 255.0F);
/* 3379 */         this.content.append(' ');
/* 3380 */         this.content.append(color.getBlue() / 255.0F);
/*      */         return;
/*      */       case 1:
/* 3383 */         this.content.append(((GrayColor)color).getGray());
/*      */         return;
/*      */       case 2:
/* 3386 */         cmyk = (CMYKColor)color;
/* 3387 */         this.content.append(cmyk.getCyan()).append(' ').append(cmyk.getMagenta());
/* 3388 */         this.content.append(' ').append(cmyk.getYellow()).append(' ').append(cmyk.getBlack());
/*      */         return;
/*      */       
/*      */       case 3:
/* 3392 */         this.content.append(tint);
/*      */         return;
/*      */     } 
/* 3395 */     throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.color.type", new Object[0]));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternFill(PdfPatternPainter p, BaseColor color) {
/* 3404 */     if (ExtendedColor.getType(color) == 3) {
/* 3405 */       setPatternFill(p, color, ((SpotColor)color).getTint());
/*      */     } else {
/* 3407 */       setPatternFill(p, color, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternFill(PdfPatternPainter p, BaseColor color, float tint) {
/* 3416 */     checkWriter();
/* 3417 */     if (!p.isStencil())
/* 3418 */       throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.pattern.was.expected", new Object[0])); 
/* 3419 */     PageResources prs = getPageResources();
/* 3420 */     PdfName name = this.writer.addSimplePattern(p);
/* 3421 */     name = prs.addPattern(name, p.getIndirectReference());
/* 3422 */     ColorDetails csDetail = this.writer.addSimplePatternColorspace(color);
/* 3423 */     PdfName cName = prs.addColor(csDetail.getColorSpaceName(), csDetail.getIndirectReference());
/* 3424 */     saveColor(new UncoloredPattern(p, color, tint), true);
/* 3425 */     this.content.append(cName.getBytes()).append(" cs").append_i(this.separator);
/* 3426 */     outputColorNumbers(color, tint);
/* 3427 */     this.content.append(' ').append(name.getBytes()).append(" scn").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternStroke(PdfPatternPainter p, BaseColor color) {
/* 3435 */     if (ExtendedColor.getType(color) == 3) {
/* 3436 */       setPatternStroke(p, color, ((SpotColor)color).getTint());
/*      */     } else {
/* 3438 */       setPatternStroke(p, color, 0.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternStroke(PdfPatternPainter p, BaseColor color, float tint) {
/* 3447 */     checkWriter();
/* 3448 */     if (!p.isStencil())
/* 3449 */       throw new RuntimeException(MessageLocalization.getComposedMessage("an.uncolored.pattern.was.expected", new Object[0])); 
/* 3450 */     PageResources prs = getPageResources();
/* 3451 */     PdfName name = this.writer.addSimplePattern(p);
/* 3452 */     name = prs.addPattern(name, p.getIndirectReference());
/* 3453 */     ColorDetails csDetail = this.writer.addSimplePatternColorspace(color);
/* 3454 */     PdfName cName = prs.addColor(csDetail.getColorSpaceName(), csDetail.getIndirectReference());
/* 3455 */     saveColor(new UncoloredPattern(p, color, tint), false);
/* 3456 */     this.content.append(cName.getBytes()).append(" CS").append_i(this.separator);
/* 3457 */     outputColorNumbers(color, tint);
/* 3458 */     this.content.append(' ').append(name.getBytes()).append(" SCN").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPatternStroke(PdfPatternPainter p) {
/* 3466 */     if (p.isStencil()) {
/* 3467 */       setPatternStroke(p, p.getDefaultColor());
/*      */       return;
/*      */     } 
/* 3470 */     checkWriter();
/* 3471 */     PageResources prs = getPageResources();
/* 3472 */     PdfName name = this.writer.addSimplePattern(p);
/* 3473 */     name = prs.addPattern(name, p.getIndirectReference());
/* 3474 */     saveColor(new PatternColor(p), false);
/* 3475 */     this.content.append(PdfName.PATTERN.getBytes()).append(" CS ").append(name.getBytes()).append(" SCN").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paintShading(PdfShading shading) {
/* 3483 */     this.writer.addSimpleShading(shading);
/* 3484 */     PageResources prs = getPageResources();
/* 3485 */     PdfName name = prs.addShading(shading.getShadingName(), shading.getShadingReference());
/* 3486 */     this.content.append(name.getBytes()).append(" sh").append_i(this.separator);
/* 3487 */     ColorDetails details = shading.getColorDetails();
/* 3488 */     if (details != null) {
/* 3489 */       prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paintShading(PdfShadingPattern shading) {
/* 3497 */     paintShading(shading.getShading());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShadingFill(PdfShadingPattern shading) {
/* 3505 */     this.writer.addSimpleShadingPattern(shading);
/* 3506 */     PageResources prs = getPageResources();
/* 3507 */     PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
/* 3508 */     saveColor(new ShadingColor(shading), true);
/* 3509 */     this.content.append(PdfName.PATTERN.getBytes()).append(" cs ").append(name.getBytes()).append(" scn").append_i(this.separator);
/* 3510 */     ColorDetails details = shading.getColorDetails();
/* 3511 */     if (details != null) {
/* 3512 */       prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShadingStroke(PdfShadingPattern shading) {
/* 3520 */     this.writer.addSimpleShadingPattern(shading);
/* 3521 */     PageResources prs = getPageResources();
/* 3522 */     PdfName name = prs.addPattern(shading.getPatternName(), shading.getPatternReference());
/* 3523 */     saveColor(new ShadingColor(shading), false);
/* 3524 */     this.content.append(PdfName.PATTERN.getBytes()).append(" CS ").append(name.getBytes()).append(" SCN").append_i(this.separator);
/* 3525 */     ColorDetails details = shading.getColorDetails();
/* 3526 */     if (details != null) {
/* 3527 */       prs.addColor(details.getColorSpaceName(), details.getIndirectReference());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkWriter() {
/* 3534 */     if (this.writer == null) {
/* 3535 */       throw new NullPointerException(MessageLocalization.getComposedMessage("the.writer.in.pdfcontentbyte.is.null", new Object[0]));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void showText(PdfTextArray text) {
/* 3543 */     checkState();
/* 3544 */     if (!this.inText && isTagged()) {
/* 3545 */       beginText(true);
/*      */     }
/* 3547 */     if (this.state.fontDetails == null)
/* 3548 */       throw new NullPointerException(MessageLocalization.getComposedMessage("font.and.size.must.be.set.before.writing.any.text", new Object[0])); 
/* 3549 */     this.content.append("[");
/* 3550 */     ArrayList<Object> arrayList = text.getArrayList();
/* 3551 */     boolean lastWasNumber = false;
/* 3552 */     for (Object obj : arrayList) {
/* 3553 */       if (obj instanceof String) {
/* 3554 */         showText2((String)obj);
/* 3555 */         updateTx((String)obj, 0.0F);
/* 3556 */         lastWasNumber = false;
/*      */         continue;
/*      */       } 
/* 3559 */       if (lastWasNumber) {
/* 3560 */         this.content.append(' ');
/*      */       } else {
/* 3562 */         lastWasNumber = true;
/* 3563 */       }  this.content.append(((Float)obj).floatValue());
/* 3564 */       updateTx("", ((Float)obj).floatValue());
/*      */     } 
/*      */     
/* 3567 */     this.content.append("]TJ").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfWriter getPdfWriter() {
/* 3575 */     return this.writer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDocument getPdfDocument() {
/* 3583 */     return this.pdf;
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
/*      */   public void localGoto(String name, float llx, float lly, float urx, float ury) {
/* 3596 */     this.pdf.localGoto(name, llx, lly, urx, ury);
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
/*      */   public boolean localDestination(String name, PdfDestination destination) {
/* 3609 */     return this.pdf.localDestination(name, destination);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfContentByte getDuplicate() {
/* 3619 */     PdfContentByte cb = new PdfContentByte(this.writer);
/* 3620 */     cb.duplicatedFrom = this;
/* 3621 */     return cb;
/*      */   }
/*      */   
/*      */   public PdfContentByte getDuplicate(boolean inheritGraphicState) {
/* 3625 */     PdfContentByte cb = getDuplicate();
/* 3626 */     if (inheritGraphicState) {
/* 3627 */       cb.state = this.state;
/* 3628 */       cb.stateList = this.stateList;
/*      */     } 
/* 3630 */     return cb;
/*      */   }
/*      */   
/*      */   public void inheritGraphicState(PdfContentByte parentCanvas) {
/* 3634 */     this.state = parentCanvas.state;
/* 3635 */     this.stateList = parentCanvas.stateList;
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
/*      */   public void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
/* 3648 */     this.pdf.remoteGoto(filename, name, llx, lly, urx, ury);
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
/*      */   public void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
/* 3661 */     this.pdf.remoteGoto(filename, page, llx, lly, urx, ury);
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
/*      */   public void roundRectangle(float x, float y, float w, float h, float r) {
/* 3674 */     roundRectangle(x, y, w, h, r);
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
/*      */   public void roundRectangle(double x, double y, double w, double h, double r) {
/* 3687 */     if (w < 0.0D) {
/* 3688 */       x += w;
/* 3689 */       w = -w;
/*      */     } 
/* 3691 */     if (h < 0.0D) {
/* 3692 */       y += h;
/* 3693 */       h = -h;
/*      */     } 
/* 3695 */     if (r < 0.0D)
/* 3696 */       r = -r; 
/* 3697 */     float b = 0.4477F;
/* 3698 */     moveTo(x + r, y);
/* 3699 */     lineTo(x + w - r, y);
/* 3700 */     curveTo(x + w - r * b, y, x + w, y + r * b, x + w, y + r);
/* 3701 */     lineTo(x + w, y + h - r);
/* 3702 */     curveTo(x + w, y + h - r * b, x + w - r * b, y + h, x + w - r, y + h);
/* 3703 */     lineTo(x + r, y + h);
/* 3704 */     curveTo(x + r * b, y + h, x, y + h - r * b, x, y + h - r);
/* 3705 */     lineTo(x, y + r);
/* 3706 */     curveTo(x, y + r * b, x + r * b, y, x + r, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
/* 3717 */     this.pdf.setAction(action, llx, lly, urx, ury);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLiteral(String s) {
/* 3724 */     this.content.append(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLiteral(char c) {
/* 3731 */     this.content.append(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLiteral(float n) {
/* 3738 */     this.content.append(n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkNoPattern(PdfTemplate t) {
/* 3745 */     if (t.getType() == 3) {
/* 3746 */       throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.use.of.a.pattern.a.template.was.expected", new Object[0]));
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
/*      */   public void drawRadioField(float llx, float lly, float urx, float ury, boolean on) {
/* 3758 */     drawRadioField(llx, lly, urx, ury, on);
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
/*      */   public void drawRadioField(double llx, double lly, double urx, double ury, boolean on) {
/* 3770 */     if (llx > urx) { double x = llx; llx = urx; urx = x; }
/* 3771 */      if (lly > ury) { double y = lly; lly = ury; ury = y; }
/* 3772 */      saveState();
/*      */     
/* 3774 */     setLineWidth(1.0F);
/* 3775 */     setLineCap(1);
/* 3776 */     setColorStroke(new BaseColor(192, 192, 192));
/* 3777 */     arc(llx + 1.0D, lly + 1.0D, urx - 1.0D, ury - 1.0D, 0.0D, 360.0D);
/* 3778 */     stroke();
/*      */     
/* 3780 */     setLineWidth(1.0F);
/* 3781 */     setLineCap(1);
/* 3782 */     setColorStroke(new BaseColor(160, 160, 160));
/* 3783 */     arc(llx + 0.5D, lly + 0.5D, urx - 0.5D, ury - 0.5D, 45.0D, 180.0D);
/* 3784 */     stroke();
/*      */     
/* 3786 */     setLineWidth(1.0F);
/* 3787 */     setLineCap(1);
/* 3788 */     setColorStroke(new BaseColor(0, 0, 0));
/* 3789 */     arc(llx + 1.5D, lly + 1.5D, urx - 1.5D, ury - 1.5D, 45.0D, 180.0D);
/* 3790 */     stroke();
/* 3791 */     if (on) {
/*      */       
/* 3793 */       setLineWidth(1.0F);
/* 3794 */       setLineCap(1);
/* 3795 */       setColorFill(new BaseColor(0, 0, 0));
/* 3796 */       arc(llx + 4.0D, lly + 4.0D, urx - 4.0D, ury - 4.0D, 0.0D, 360.0D);
/* 3797 */       fill();
/*      */     } 
/* 3799 */     restoreState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawTextField(float llx, float lly, float urx, float ury) {
/* 3810 */     drawTextField(llx, lly, urx, ury);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawTextField(double llx, double lly, double urx, double ury) {
/* 3821 */     if (llx > urx) { double x = llx; llx = urx; urx = x; }
/* 3822 */      if (lly > ury) { double y = lly; lly = ury; ury = y; }
/*      */     
/* 3824 */     saveState();
/* 3825 */     setColorStroke(new BaseColor(192, 192, 192));
/* 3826 */     setLineWidth(1.0F);
/* 3827 */     setLineCap(0);
/* 3828 */     rectangle(llx, lly, urx - llx, ury - lly);
/* 3829 */     stroke();
/*      */     
/* 3831 */     setLineWidth(1.0F);
/* 3832 */     setLineCap(0);
/* 3833 */     setColorFill(new BaseColor(255, 255, 255));
/* 3834 */     rectangle(llx + 0.5D, lly + 0.5D, urx - llx - 1.0D, ury - lly - 1.0D);
/* 3835 */     fill();
/*      */     
/* 3837 */     setColorStroke(new BaseColor(192, 192, 192));
/* 3838 */     setLineWidth(1.0F);
/* 3839 */     setLineCap(0);
/* 3840 */     moveTo(llx + 1.0D, lly + 1.5D);
/* 3841 */     lineTo(urx - 1.5D, lly + 1.5D);
/* 3842 */     lineTo(urx - 1.5D, ury - 1.0D);
/* 3843 */     stroke();
/*      */     
/* 3845 */     setColorStroke(new BaseColor(160, 160, 160));
/* 3846 */     setLineWidth(1.0F);
/* 3847 */     setLineCap(0);
/* 3848 */     moveTo(llx + 1.0D, lly + 1.0D);
/* 3849 */     lineTo(llx + 1.0D, ury - 1.0D);
/* 3850 */     lineTo(urx - 1.0D, ury - 1.0D);
/* 3851 */     stroke();
/*      */     
/* 3853 */     setColorStroke(new BaseColor(0, 0, 0));
/* 3854 */     setLineWidth(1.0F);
/* 3855 */     setLineCap(0);
/* 3856 */     moveTo(llx + 2.0D, lly + 2.0D);
/* 3857 */     lineTo(llx + 2.0D, ury - 2.0D);
/* 3858 */     lineTo(urx - 2.0D, ury - 2.0D);
/* 3859 */     stroke();
/* 3860 */     restoreState();
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
/*      */   public void drawButton(float llx, float lly, float urx, float ury, String text, BaseFont bf, float size) {
/* 3874 */     drawButton(llx, lly, urx, ury, text, bf, size);
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
/*      */   public void drawButton(double llx, double lly, double urx, double ury, String text, BaseFont bf, float size) {
/* 3888 */     if (llx > urx) { double x = llx; llx = urx; urx = x; }
/* 3889 */      if (lly > ury) { double y = lly; lly = ury; ury = y; }
/*      */     
/* 3891 */     saveState();
/* 3892 */     setColorStroke(new BaseColor(0, 0, 0));
/* 3893 */     setLineWidth(1.0F);
/* 3894 */     setLineCap(0);
/* 3895 */     rectangle(llx, lly, urx - llx, ury - lly);
/* 3896 */     stroke();
/*      */     
/* 3898 */     setLineWidth(1.0F);
/* 3899 */     setLineCap(0);
/* 3900 */     setColorFill(new BaseColor(192, 192, 192));
/* 3901 */     rectangle(llx + 0.5D, lly + 0.5D, urx - llx - 1.0D, ury - lly - 1.0D);
/* 3902 */     fill();
/*      */     
/* 3904 */     setColorStroke(new BaseColor(255, 255, 255));
/* 3905 */     setLineWidth(1.0F);
/* 3906 */     setLineCap(0);
/* 3907 */     moveTo(llx + 1.0D, lly + 1.0D);
/* 3908 */     lineTo(llx + 1.0D, ury - 1.0D);
/* 3909 */     lineTo(urx - 1.0D, ury - 1.0D);
/* 3910 */     stroke();
/*      */     
/* 3912 */     setColorStroke(new BaseColor(160, 160, 160));
/* 3913 */     setLineWidth(1.0F);
/* 3914 */     setLineCap(0);
/* 3915 */     moveTo(llx + 1.0D, lly + 1.0D);
/* 3916 */     lineTo(urx - 1.0D, lly + 1.0D);
/* 3917 */     lineTo(urx - 1.0D, ury - 1.0D);
/* 3918 */     stroke();
/*      */     
/* 3920 */     resetRGBColorFill();
/* 3921 */     beginText();
/* 3922 */     setFontAndSize(bf, size);
/* 3923 */     showTextAligned(1, text, (float)(llx + (urx - llx) / 2.0D), (float)(lly + (ury - lly - size) / 2.0D), 0.0F);
/* 3924 */     endText();
/* 3925 */     restoreState();
/*      */   }
/*      */   
/*      */   PageResources getPageResources() {
/* 3929 */     return this.pdf.getPageResources();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGState(PdfGState gstate) {
/* 3936 */     PdfObject[] obj = this.writer.addSimpleExtGState(gstate);
/* 3937 */     PageResources prs = getPageResources();
/* 3938 */     PdfName name = prs.addExtGState((PdfName)obj[0], (PdfIndirectReference)obj[1]);
/* 3939 */     this.state.extGState = gstate;
/* 3940 */     this.content.append(name.getBytes()).append(" gs").append_i(this.separator);
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
/*      */   public void beginLayer(PdfOCG layer) {
/* 3952 */     if (layer instanceof PdfLayer && ((PdfLayer)layer).getTitle() != null)
/* 3953 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.title.is.not.a.layer", new Object[0])); 
/* 3954 */     if (this.layerDepth == null)
/* 3955 */       this.layerDepth = new ArrayList<Integer>(); 
/* 3956 */     if (layer instanceof PdfLayerMembership) {
/* 3957 */       this.layerDepth.add(Integer.valueOf(1));
/* 3958 */       beginLayer2(layer);
/*      */       return;
/*      */     } 
/* 3961 */     int n = 0;
/* 3962 */     PdfLayer la = (PdfLayer)layer;
/* 3963 */     while (la != null) {
/* 3964 */       if (la.getTitle() == null) {
/* 3965 */         beginLayer2(la);
/* 3966 */         n++;
/*      */       } 
/* 3968 */       la = la.getParent();
/*      */     } 
/* 3970 */     this.layerDepth.add(Integer.valueOf(n));
/*      */   }
/*      */   
/*      */   private void beginLayer2(PdfOCG layer) {
/* 3974 */     PdfName name = (PdfName)this.writer.addSimpleProperty(layer, layer.getRef())[0];
/* 3975 */     PageResources prs = getPageResources();
/* 3976 */     name = prs.addProperty(name, layer.getRef());
/* 3977 */     this.content.append("/OC ").append(name.getBytes()).append(" BDC").append_i(this.separator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endLayer() {
/* 3984 */     int n = 1;
/* 3985 */     if (this.layerDepth != null && !this.layerDepth.isEmpty()) {
/* 3986 */       n = ((Integer)this.layerDepth.get(this.layerDepth.size() - 1)).intValue();
/* 3987 */       this.layerDepth.remove(this.layerDepth.size() - 1);
/*      */     } else {
/* 3989 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.layer.operators", new Object[0]));
/*      */     } 
/* 3991 */     while (n-- > 0) {
/* 3992 */       this.content.append("EMC").append_i(this.separator);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transform(AffineTransform af) {
/* 4000 */     if (this.inText && isTagged()) {
/* 4001 */       endText();
/*      */     }
/* 4003 */     double[] matrix = new double[6];
/* 4004 */     af.getMatrix(matrix);
/* 4005 */     this.state.CTM.concatenate(af);
/* 4006 */     this.content.append(matrix[0]).append(' ').append(matrix[1]).append(' ').append(matrix[2]).append(' ');
/* 4007 */     this.content.append(matrix[3]).append(' ').append(matrix[4]).append(' ').append(matrix[5]).append(" cm").append_i(this.separator);
/*      */   }
/*      */   
/*      */   void addAnnotation(PdfAnnotation annot) {
/* 4011 */     boolean needToTag = (isTagged() && annot.getRole() != null && (!(annot instanceof PdfFormField) || ((PdfFormField)annot).getKids() == null));
/* 4012 */     if (needToTag) {
/* 4013 */       openMCBlock(annot);
/*      */     }
/* 4015 */     this.writer.addAnnotation(annot);
/* 4016 */     if (needToTag) {
/* 4017 */       PdfStructureElement strucElem = this.pdf.getStructElement(annot.getId());
/* 4018 */       if (strucElem != null) {
/* 4019 */         int structParent = this.pdf.getStructParentIndex(annot);
/* 4020 */         annot.put(PdfName.STRUCTPARENT, new PdfNumber(structParent));
/* 4021 */         strucElem.setAnnotation(annot, getCurrentPage());
/* 4022 */         this.writer.getStructureTreeRoot().setAnnotationMark(structParent, strucElem.getReference());
/*      */       } 
/* 4024 */       closeMCBlock(annot);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addAnnotation(PdfAnnotation annot, boolean applyCTM) {
/* 4029 */     if (applyCTM && this.state.CTM.getType() != 0) {
/* 4030 */       annot.applyCTM(this.state.CTM);
/*      */     }
/* 4032 */     addAnnotation(annot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultColorspace(PdfName name, PdfObject obj) {
/* 4042 */     PageResources prs = getPageResources();
/* 4043 */     prs.addDefaultColor(name, obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginMarkedContentSequence(PdfStructureElement struc) {
/* 4053 */     beginMarkedContentSequence(struc, null);
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
/*      */   private void beginMarkedContentSequence(PdfStructureElement struc, String expansion) {
/* 4065 */     PdfObject obj = struc.get(PdfName.K);
/* 4066 */     int[] structParentMarkPoint = this.pdf.getStructParentIndexAndNextMarkPoint(getCurrentPage());
/* 4067 */     int structParent = structParentMarkPoint[0];
/* 4068 */     int mark = structParentMarkPoint[1];
/* 4069 */     if (obj != null) {
/* 4070 */       PdfArray ar = null;
/* 4071 */       if (obj.isNumber()) {
/* 4072 */         ar = new PdfArray();
/* 4073 */         ar.add(obj);
/* 4074 */         struc.put(PdfName.K, ar);
/*      */       }
/* 4076 */       else if (obj.isArray()) {
/* 4077 */         ar = (PdfArray)obj;
/*      */       } else {
/*      */         
/* 4080 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("unknown.object.at.k.1", new Object[] { obj.getClass().toString() }));
/* 4081 */       }  if (ar.getAsNumber(0) != null) {
/* 4082 */         PdfDictionary dic = new PdfDictionary(PdfName.MCR);
/* 4083 */         dic.put(PdfName.PG, getCurrentPage());
/* 4084 */         dic.put(PdfName.MCID, new PdfNumber(mark));
/* 4085 */         ar.add(dic);
/*      */       } 
/* 4087 */       struc.setPageMark(this.pdf.getStructParentIndex(getCurrentPage()), -1);
/*      */     } else {
/*      */       
/* 4090 */       struc.setPageMark(structParent, mark);
/* 4091 */       struc.put(PdfName.PG, getCurrentPage());
/*      */     } 
/* 4093 */     setMcDepth(getMcDepth() + 1);
/* 4094 */     int contentSize = this.content.size();
/* 4095 */     this.content.append(struc.get(PdfName.S).getBytes()).append(" <</MCID ").append(mark);
/* 4096 */     if (null != expansion) {
/* 4097 */       this.content.append("/E (").append(expansion).append(")");
/*      */     }
/* 4099 */     this.content.append(">> BDC").append_i(this.separator);
/* 4100 */     this.markedContentSize += this.content.size() - contentSize;
/*      */   }
/*      */   
/*      */   protected PdfIndirectReference getCurrentPage() {
/* 4104 */     return this.writer.getCurrentPage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endMarkedContentSequence() {
/* 4111 */     if (getMcDepth() == 0) {
/* 4112 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.marked.content.operators", new Object[0]));
/*      */     }
/* 4114 */     int contentSize = this.content.size();
/* 4115 */     setMcDepth(getMcDepth() - 1);
/* 4116 */     this.content.append("EMC").append_i(this.separator);
/* 4117 */     this.markedContentSize += this.content.size() - contentSize;
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
/*      */   public void beginMarkedContentSequence(PdfName tag, PdfDictionary property, boolean inline) {
/* 4129 */     int contentSize = this.content.size();
/* 4130 */     if (property == null) {
/* 4131 */       this.content.append(tag.getBytes()).append(" BMC").append_i(this.separator);
/* 4132 */       setMcDepth(getMcDepth() + 1);
/*      */     } else {
/* 4134 */       this.content.append(tag.getBytes()).append(' ');
/* 4135 */       if (inline) {
/*      */         try {
/* 4137 */           property.toPdf(this.writer, this.content);
/*      */         }
/* 4139 */         catch (Exception e) {
/* 4140 */           throw new ExceptionConverter(e);
/*      */         } 
/*      */       } else {
/*      */         PdfObject[] objs;
/* 4144 */         if (this.writer.propertyExists(property)) {
/* 4145 */           objs = this.writer.addSimpleProperty(property, null);
/*      */         } else {
/* 4147 */           objs = this.writer.addSimpleProperty(property, this.writer.getPdfIndirectReference());
/* 4148 */         }  PdfName name = (PdfName)objs[0];
/* 4149 */         PageResources prs = getPageResources();
/* 4150 */         name = prs.addProperty(name, (PdfIndirectReference)objs[1]);
/* 4151 */         this.content.append(name.getBytes());
/*      */       } 
/* 4153 */       this.content.append(" BDC").append_i(this.separator);
/* 4154 */       setMcDepth(getMcDepth() + 1);
/*      */     } 
/* 4156 */     this.markedContentSize += this.content.size() - contentSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginMarkedContentSequence(PdfName tag) {
/* 4164 */     beginMarkedContentSequence(tag, null, false);
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
/*      */   public void sanityCheck() {
/* 4179 */     if (getMcDepth() != 0) {
/* 4180 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.marked.content.operators", new Object[0]));
/*      */     }
/* 4182 */     if (this.inText) {
/* 4183 */       if (isTagged()) {
/* 4184 */         endText();
/*      */       } else {
/* 4186 */         throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.begin.end.text.operators", new Object[0]));
/*      */       } 
/*      */     }
/* 4189 */     if (this.layerDepth != null && !this.layerDepth.isEmpty()) {
/* 4190 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.layer.operators", new Object[0]));
/*      */     }
/* 4192 */     if (!this.stateList.isEmpty()) {
/* 4193 */       throw new IllegalPdfSyntaxException(MessageLocalization.getComposedMessage("unbalanced.save.restore.state.operators", new Object[0]));
/*      */     }
/*      */   }
/*      */   
/*      */   public void openMCBlock(IAccessibleElement element) {
/* 4198 */     if (isTagged()) {
/* 4199 */       ensureDocumentTagIsOpen();
/* 4200 */       if (element != null && 
/* 4201 */         !getMcElements().contains(element)) {
/* 4202 */         PdfStructureElement structureElement = openMCBlockInt(element);
/* 4203 */         getMcElements().add(element);
/* 4204 */         if (structureElement != null) {
/* 4205 */           this.pdf.saveStructElement(element.getId(), structureElement);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private PdfDictionary getParentStructureElement() {
/* 4213 */     PdfDictionary parent = null;
/* 4214 */     if (getMcElements().size() > 0)
/* 4215 */       parent = this.pdf.getStructElement(((IAccessibleElement)getMcElements().get(getMcElements().size() - 1)).getId()); 
/* 4216 */     if (parent == null) {
/* 4217 */       parent = this.writer.getStructureTreeRoot();
/*      */     }
/* 4219 */     return parent;
/*      */   }
/*      */   
/*      */   private PdfStructureElement openMCBlockInt(IAccessibleElement element) {
/* 4223 */     PdfStructureElement structureElement = null;
/* 4224 */     if (isTagged()) {
/* 4225 */       IAccessibleElement parent = null;
/* 4226 */       if (getMcElements().size() > 0)
/* 4227 */         parent = getMcElements().get(getMcElements().size() - 1); 
/* 4228 */       this.writer.checkElementRole(element, parent);
/* 4229 */       if (element.getRole() != null) {
/* 4230 */         if (!PdfName.ARTIFACT.equals(element.getRole())) {
/* 4231 */           structureElement = this.pdf.getStructElement(element.getId());
/* 4232 */           if (structureElement == null) {
/* 4233 */             structureElement = new PdfStructureElement(getParentStructureElement(), element.getRole(), element.getId());
/*      */           }
/*      */         } 
/* 4236 */         if (PdfName.ARTIFACT.equals(element.getRole())) {
/* 4237 */           HashMap<PdfName, PdfObject> properties = element.getAccessibleAttributes();
/* 4238 */           PdfDictionary propertiesDict = null;
/* 4239 */           if (properties != null && !properties.isEmpty()) {
/* 4240 */             propertiesDict = new PdfDictionary();
/* 4241 */             for (Map.Entry<PdfName, PdfObject> entry : properties.entrySet()) {
/* 4242 */               propertiesDict.put(entry.getKey(), entry.getValue());
/*      */             }
/*      */           } 
/* 4245 */           boolean inTextLocal = this.inText;
/* 4246 */           if (this.inText)
/* 4247 */             endText(); 
/* 4248 */           beginMarkedContentSequence(element.getRole(), propertiesDict, true);
/* 4249 */           if (inTextLocal) {
/* 4250 */             beginText(true);
/*      */           }
/* 4252 */         } else if (this.writer.needToBeMarkedInContent(element)) {
/* 4253 */           boolean inTextLocal = this.inText;
/* 4254 */           if (this.inText)
/* 4255 */             endText(); 
/* 4256 */           if (null != element.getAccessibleAttributes() && null != element.getAccessibleAttribute(PdfName.E)) {
/* 4257 */             beginMarkedContentSequence(structureElement, element.getAccessibleAttribute(PdfName.E).toString());
/* 4258 */             element.setAccessibleAttribute(PdfName.E, null);
/*      */           } else {
/* 4260 */             beginMarkedContentSequence(structureElement);
/*      */           } 
/* 4262 */           if (inTextLocal) {
/* 4263 */             beginText(true);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 4268 */     return structureElement;
/*      */   }
/*      */   
/*      */   public void closeMCBlock(IAccessibleElement element) {
/* 4272 */     if (isTagged() && element != null && 
/* 4273 */       getMcElements().contains(element)) {
/* 4274 */       closeMCBlockInt(element);
/* 4275 */       getMcElements().remove(element);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void closeMCBlockInt(IAccessibleElement element) {
/* 4281 */     if (isTagged() && element.getRole() != null) {
/* 4282 */       PdfStructureElement structureElement = this.pdf.getStructElement(element.getId());
/* 4283 */       if (structureElement != null) {
/* 4284 */         structureElement.writeAttributes(element);
/*      */       }
/* 4286 */       if (this.writer.needToBeMarkedInContent(element)) {
/* 4287 */         boolean inTextLocal = this.inText;
/* 4288 */         if (this.inText)
/* 4289 */           endText(); 
/* 4290 */         endMarkedContentSequence();
/* 4291 */         if (inTextLocal)
/* 4292 */           beginText(true); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void ensureDocumentTagIsOpen() {
/* 4298 */     if (this.pdf.openMCDocument) {
/* 4299 */       this.pdf.openMCDocument = false;
/* 4300 */       this.writer.getDirectContentUnder().openMCBlock((IAccessibleElement)this.pdf);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected ArrayList<IAccessibleElement> saveMCBlocks() {
/* 4305 */     ArrayList<IAccessibleElement> mc = new ArrayList<IAccessibleElement>();
/* 4306 */     if (isTagged()) {
/* 4307 */       mc = getMcElements();
/* 4308 */       for (int i = 0; i < mc.size(); i++) {
/* 4309 */         closeMCBlockInt(mc.get(i));
/*      */       }
/* 4311 */       setMcElements(new ArrayList<IAccessibleElement>());
/*      */     } 
/* 4313 */     return mc;
/*      */   }
/*      */   
/*      */   protected void restoreMCBlocks(ArrayList<IAccessibleElement> mcElements) {
/* 4317 */     if (isTagged() && mcElements != null) {
/* 4318 */       setMcElements(mcElements);
/* 4319 */       for (int i = 0; i < getMcElements().size(); i++) {
/* 4320 */         openMCBlockInt(getMcElements().get(i));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int getMcDepth() {
/* 4326 */     if (this.duplicatedFrom != null) {
/* 4327 */       return this.duplicatedFrom.getMcDepth();
/*      */     }
/* 4329 */     return this.mcDepth;
/*      */   }
/*      */   
/*      */   protected void setMcDepth(int value) {
/* 4333 */     if (this.duplicatedFrom != null) {
/* 4334 */       this.duplicatedFrom.setMcDepth(value);
/*      */     } else {
/* 4336 */       this.mcDepth = value;
/*      */     } 
/*      */   }
/*      */   protected ArrayList<IAccessibleElement> getMcElements() {
/* 4340 */     if (this.duplicatedFrom != null) {
/* 4341 */       return this.duplicatedFrom.getMcElements();
/*      */     }
/* 4343 */     return this.mcElements;
/*      */   }
/*      */   
/*      */   protected void setMcElements(ArrayList<IAccessibleElement> value) {
/* 4347 */     if (this.duplicatedFrom != null) {
/* 4348 */       this.duplicatedFrom.setMcElements(value);
/*      */     } else {
/* 4350 */       this.mcElements = value;
/*      */     } 
/*      */   }
/*      */   protected void updateTx(String text, float Tj) {
/* 4354 */     this.state.tx += getEffectiveStringWidth(text, false, Tj);
/*      */   }
/*      */   
/*      */   private void saveColor(BaseColor color, boolean fill) {
/* 4358 */     if (fill) {
/* 4359 */       this.state.colorFill = color;
/*      */     } else {
/* 4361 */       this.state.colorStroke = color;
/*      */     } 
/*      */   }
/*      */   
/*      */   static class UncoloredPattern extends PatternColor {
/*      */     protected BaseColor color;
/*      */     protected float tint;
/*      */     
/*      */     protected UncoloredPattern(PdfPatternPainter p, BaseColor color, float tint) {
/* 4370 */       super(p);
/* 4371 */       this.color = color;
/* 4372 */       this.tint = tint;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 4377 */       return (obj instanceof UncoloredPattern && ((UncoloredPattern)obj).painter.equals(this.painter) && ((UncoloredPattern)obj).color.equals(this.color) && ((UncoloredPattern)obj).tint == this.tint);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean getInText() {
/* 4383 */     return this.inText;
/*      */   }
/*      */   
/*      */   protected void checkState() {
/* 4387 */     boolean stroke = false;
/* 4388 */     boolean fill = false;
/* 4389 */     if (this.state.textRenderMode == 0) {
/* 4390 */       fill = true;
/* 4391 */     } else if (this.state.textRenderMode == 1) {
/* 4392 */       stroke = true;
/* 4393 */     } else if (this.state.textRenderMode == 2) {
/* 4394 */       fill = true;
/* 4395 */       stroke = true;
/*      */     } 
/* 4397 */     if (fill) {
/* 4398 */       PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorFill);
/*      */     }
/* 4400 */     if (stroke) {
/* 4401 */       PdfWriter.checkPdfIsoConformance(this.writer, 1, this.state.colorStroke);
/*      */     }
/* 4403 */     PdfWriter.checkPdfIsoConformance(this.writer, 6, this.state.extGState);
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
/*      */   public Graphics2D createGraphicsShapes(float width, float height) {
/* 4416 */     return (Graphics2D)new PdfGraphics2D(this, width, height, true);
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
/*      */   public Graphics2D createPrinterGraphicsShapes(float width, float height, PrinterJob printerJob) {
/* 4428 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, true, printerJob);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Graphics2D createGraphics(float width, float height) {
/* 4439 */     return (Graphics2D)new PdfGraphics2D(this, width, height);
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
/*      */   public Graphics2D createPrinterGraphics(float width, float height, PrinterJob printerJob) {
/* 4451 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, printerJob);
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
/*      */   public Graphics2D createGraphics(float width, float height, boolean convertImagesToJPEG, float quality) {
/* 4464 */     return (Graphics2D)new PdfGraphics2D(this, width, height, null, false, convertImagesToJPEG, quality);
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
/*      */   public Graphics2D createPrinterGraphics(float width, float height, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
/* 4478 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, null, false, convertImagesToJPEG, quality, printerJob);
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
/*      */   public Graphics2D createGraphicsShapes(float width, float height, boolean convertImagesToJPEG, float quality) {
/* 4491 */     return (Graphics2D)new PdfGraphics2D(this, width, height, null, true, convertImagesToJPEG, quality);
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
/*      */   public Graphics2D createPrinterGraphicsShapes(float width, float height, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
/* 4505 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, null, true, convertImagesToJPEG, quality, printerJob);
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
/*      */   public Graphics2D createGraphics(float width, float height, FontMapper fontMapper) {
/* 4517 */     return (Graphics2D)new PdfGraphics2D(this, width, height, fontMapper);
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
/*      */   public Graphics2D createPrinterGraphics(float width, float height, FontMapper fontMapper, PrinterJob printerJob) {
/* 4530 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, fontMapper, printerJob);
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
/*      */   public Graphics2D createGraphics(float width, float height, FontMapper fontMapper, boolean convertImagesToJPEG, float quality) {
/* 4544 */     return (Graphics2D)new PdfGraphics2D(this, width, height, fontMapper, false, convertImagesToJPEG, quality);
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
/*      */   public Graphics2D createPrinterGraphics(float width, float height, FontMapper fontMapper, boolean convertImagesToJPEG, float quality, PrinterJob printerJob) {
/* 4559 */     return (Graphics2D)new PdfPrinterGraphics2D(this, width, height, fontMapper, false, convertImagesToJPEG, quality, printerJob);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addImage(Image image, AffineTransform transform) throws DocumentException {
/* 4570 */     double[] matrix = new double[6];
/* 4571 */     transform.getMatrix(matrix);
/* 4572 */     addImage(image, new AffineTransform(matrix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTemplate(PdfTemplate template, AffineTransform transform) {
/* 4582 */     double[] matrix = new double[6];
/* 4583 */     transform.getMatrix(matrix);
/* 4584 */     addTemplate(template, new AffineTransform(matrix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void concatCTM(AffineTransform transform) {
/* 4593 */     double[] matrix = new double[6];
/* 4594 */     transform.getMatrix(matrix);
/* 4595 */     concatCTM(new AffineTransform(matrix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTextMatrix(AffineTransform transform) {
/* 4605 */     double[] matrix = new double[6];
/* 4606 */     transform.getMatrix(matrix);
/* 4607 */     setTextMatrix(new AffineTransform(matrix));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transform(AffineTransform af) {
/* 4616 */     double[] matrix = new double[6];
/* 4617 */     af.getMatrix(matrix);
/* 4618 */     transform(new AffineTransform(matrix));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfContentByte.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */