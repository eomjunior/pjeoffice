/*      */ package com.itextpdf.awt;
/*      */ 
/*      */ import com.itextpdf.awt.geom.PolylineShape;
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.pdf.BaseFont;
/*      */ import com.itextpdf.text.pdf.ByteBuffer;
/*      */ import com.itextpdf.text.pdf.CMYKColor;
/*      */ import com.itextpdf.text.pdf.PdfAction;
/*      */ import com.itextpdf.text.pdf.PdfContentByte;
/*      */ import com.itextpdf.text.pdf.PdfGState;
/*      */ import com.itextpdf.text.pdf.PdfPatternPainter;
/*      */ import com.itextpdf.text.pdf.PdfShading;
/*      */ import com.itextpdf.text.pdf.PdfShadingPattern;
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.GradientPaint;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.MediaTracker;
/*      */ import java.awt.Paint;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.TexturePaint;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Arc2D;
/*      */ import java.awt.geom.Area;
/*      */ import java.awt.geom.Ellipse2D;
/*      */ import java.awt.geom.Line2D;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.RoundRectangle2D;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.BufferedImageOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.awt.image.renderable.RenderableImage;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfGraphics2D
/*      */   extends Graphics2D
/*      */ {
/*      */   private static final int FILL = 1;
/*      */   private static final int STROKE = 2;
/*      */   private static final int CLIP = 3;
/*  121 */   private BasicStroke strokeOne = new BasicStroke(1.0F);
/*      */   
/*  123 */   private static final AffineTransform IDENTITY = new AffineTransform();
/*      */   
/*      */   protected Font font;
/*      */   
/*      */   protected BaseFont baseFont;
/*      */   
/*      */   protected float fontSize;
/*      */   protected AffineTransform transform;
/*      */   protected Paint paint;
/*      */   protected Color background;
/*      */   protected float width;
/*      */   protected float height;
/*      */   protected Area clip;
/*  136 */   protected RenderingHints rhints = new RenderingHints(null);
/*      */   
/*      */   protected Stroke stroke;
/*      */   
/*      */   protected Stroke originalStroke;
/*      */   
/*      */   protected PdfContentByte cb;
/*      */   protected HashMap<String, BaseFont> baseFonts;
/*      */   protected boolean disposeCalled = false;
/*      */   protected FontMapper fontMapper;
/*      */   private ArrayList<Kid> kids;
/*      */   
/*      */   private static final class Kid
/*      */   {
/*      */     final int pos;
/*      */     final PdfGraphics2D graphics;
/*      */     
/*      */     Kid(int pos, PdfGraphics2D graphics) {
/*  154 */       this.pos = pos;
/*  155 */       this.graphics = graphics;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean kid = false;
/*      */   
/*      */   private Graphics2D dg2;
/*      */   
/*      */   private boolean onlyShapes = false;
/*      */   
/*      */   private Stroke oldStroke;
/*      */   
/*      */   private Paint paintFill;
/*      */   
/*      */   private Paint paintStroke;
/*      */   
/*      */   private MediaTracker mediaTracker;
/*      */   
/*      */   protected boolean underline;
/*      */   
/*      */   protected boolean strikethrough;
/*      */   
/*      */   protected PdfGState[] fillGState;
/*      */   
/*      */   protected PdfGState[] strokeGState;
/*  180 */   protected int currentFillGState = 255;
/*  181 */   protected int currentStrokeGState = 255;
/*      */   
/*      */   public static final int AFM_DIVISOR = 1000;
/*      */   
/*      */   private boolean convertImagesToJPEG = false;
/*  186 */   private float jpegQuality = 0.95F;
/*      */ 
/*      */ 
/*      */   
/*      */   private float alpha;
/*      */ 
/*      */ 
/*      */   
/*      */   private Composite composite;
/*      */ 
/*      */ 
/*      */   
/*      */   private Paint realPaint;
/*      */ 
/*      */ 
/*      */   
/*      */   private Graphics2D getDG2() {
/*  203 */     if (this.dg2 == null) {
/*  204 */       this.dg2 = (new BufferedImage(2, 2, 1)).createGraphics();
/*  205 */       this.dg2.setRenderingHints(this.rhints);
/*      */     } 
/*  207 */     return this.dg2;
/*      */   }
/*      */   
/*      */   private PdfGraphics2D() {
/*  211 */     setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*  212 */     setRenderingHint(HyperLinkKey.KEY_INSTANCE, HyperLinkKey.VALUE_HYPERLINKKEY_OFF);
/*      */   }
/*      */   
/*      */   public PdfGraphics2D(PdfContentByte cb, float width, float height) {
/*  216 */     this(cb, width, height, null, false, false, 0.0F);
/*      */   }
/*      */   
/*      */   public PdfGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper) {
/*  220 */     this(cb, width, height, fontMapper, false, false, 0.0F);
/*      */   }
/*      */   
/*      */   public PdfGraphics2D(PdfContentByte cb, float width, float height, boolean onlyShapes) {
/*  224 */     this(cb, width, height, null, onlyShapes, false, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfGraphics2D(PdfContentByte cb, float width, float height, FontMapper fontMapper, boolean onlyShapes, boolean convertImagesToJPEG, float quality) {
/*  232 */     this.fillGState = new PdfGState[256];
/*  233 */     this.strokeGState = new PdfGState[256];
/*  234 */     this.convertImagesToJPEG = convertImagesToJPEG;
/*  235 */     this.jpegQuality = quality;
/*  236 */     this.onlyShapes = onlyShapes;
/*  237 */     this.transform = new AffineTransform();
/*  238 */     this.baseFonts = new HashMap<String, BaseFont>();
/*  239 */     if (!onlyShapes) {
/*  240 */       this.fontMapper = fontMapper;
/*  241 */       if (this.fontMapper == null)
/*  242 */         this.fontMapper = new DefaultFontMapper(); 
/*      */     } 
/*  244 */     this.paint = Color.black;
/*  245 */     this.background = Color.white;
/*  246 */     setFont(new Font("sanserif", 0, 12));
/*  247 */     this.cb = cb;
/*  248 */     cb.saveState();
/*  249 */     this.width = width;
/*  250 */     this.height = height;
/*  251 */     this.clip = new Area(new Rectangle2D.Float(0.0F, 0.0F, width, height));
/*  252 */     clip(this.clip);
/*  253 */     this.originalStroke = this.stroke = this.oldStroke = this.strokeOne;
/*  254 */     setStrokeDiff(this.stroke, null);
/*  255 */     cb.saveState();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Shape s) {
/*  263 */     followPath(s, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
/*  271 */     return drawImage(img, (Image)null, xform, (Color)null, obs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
/*  279 */     BufferedImage result = img;
/*  280 */     if (op != null) {
/*  281 */       result = op.createCompatibleDestImage(img, img.getColorModel());
/*  282 */       result = op.filter(img, result);
/*      */     } 
/*  284 */     drawImage(result, x, y, (ImageObserver)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
/*  292 */     BufferedImage image = null;
/*  293 */     if (img instanceof BufferedImage) {
/*  294 */       image = (BufferedImage)img;
/*      */     } else {
/*  296 */       ColorModel cm = img.getColorModel();
/*  297 */       int width = img.getWidth();
/*  298 */       int height = img.getHeight();
/*  299 */       WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
/*  300 */       boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
/*  301 */       Hashtable<String, Object> properties = new Hashtable<String, Object>();
/*  302 */       String[] keys = img.getPropertyNames();
/*  303 */       if (keys != null) {
/*  304 */         for (String key : keys) {
/*  305 */           properties.put(key, img.getProperty(key));
/*      */         }
/*      */       }
/*  308 */       BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
/*  309 */       img.copyData(raster);
/*  310 */       image = result;
/*      */     } 
/*  312 */     drawImage(image, xform, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
/*  320 */     drawRenderedImage(img.createDefaultRendering(), xform);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawString(String s, int x, int y) {
/*  328 */     drawString(s, x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double asPoints(double d, int i) {
/*  338 */     return d * i / 1000.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doAttributes(AttributedCharacterIterator iter) {
/*  347 */     this.underline = false;
/*  348 */     this.strikethrough = false;
/*  349 */     for (AttributedCharacterIterator.Attribute attribute : iter.getAttributes().keySet()) {
/*  350 */       if (!(attribute instanceof TextAttribute))
/*      */         continue; 
/*  352 */       TextAttribute textattribute = (TextAttribute)attribute;
/*  353 */       if (textattribute.equals(TextAttribute.FONT)) {
/*  354 */         Font font = (Font)iter.getAttributes().get(textattribute);
/*  355 */         setFont(font); continue;
/*      */       } 
/*  357 */       if (textattribute.equals(TextAttribute.UNDERLINE)) {
/*  358 */         if (iter.getAttributes().get(textattribute) == TextAttribute.UNDERLINE_ON)
/*  359 */           this.underline = true;  continue;
/*      */       } 
/*  361 */       if (textattribute.equals(TextAttribute.STRIKETHROUGH)) {
/*  362 */         if (iter.getAttributes().get(textattribute) == TextAttribute.STRIKETHROUGH_ON)
/*  363 */           this.strikethrough = true;  continue;
/*      */       } 
/*  365 */       if (textattribute.equals(TextAttribute.SIZE)) {
/*  366 */         Object obj = iter.getAttributes().get(textattribute);
/*  367 */         if (obj instanceof Integer) {
/*  368 */           int i = ((Integer)obj).intValue();
/*  369 */           setFont(getFont().deriveFont(getFont().getStyle(), i)); continue;
/*      */         } 
/*  371 */         if (obj instanceof Float) {
/*  372 */           float f = ((Float)obj).floatValue();
/*  373 */           setFont(getFont().deriveFont(getFont().getStyle(), f));
/*      */         }  continue;
/*      */       } 
/*  376 */       if (textattribute.equals(TextAttribute.FOREGROUND)) {
/*  377 */         setColor((Color)iter.getAttributes().get(textattribute)); continue;
/*      */       } 
/*  379 */       if (textattribute.equals(TextAttribute.FAMILY)) {
/*  380 */         Font font = getFont();
/*  381 */         Map<TextAttribute, ?> fontAttributes = font.getAttributes();
/*  382 */         fontAttributes.put(TextAttribute.FAMILY, iter.getAttributes().get(textattribute));
/*  383 */         setFont(font.deriveFont((Map)fontAttributes)); continue;
/*      */       } 
/*  385 */       if (textattribute.equals(TextAttribute.POSTURE)) {
/*  386 */         Font font = getFont();
/*  387 */         Map<TextAttribute, ?> fontAttributes = font.getAttributes();
/*  388 */         fontAttributes.put(TextAttribute.POSTURE, iter.getAttributes().get(textattribute));
/*  389 */         setFont(font.deriveFont((Map)fontAttributes)); continue;
/*      */       } 
/*  391 */       if (textattribute.equals(TextAttribute.WEIGHT)) {
/*  392 */         Font font = getFont();
/*  393 */         Map<TextAttribute, ?> fontAttributes = font.getAttributes();
/*  394 */         fontAttributes.put(TextAttribute.WEIGHT, iter.getAttributes().get(textattribute));
/*  395 */         setFont(font.deriveFont((Map)fontAttributes));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawString(String s, float x, float y) {
/*  405 */     if (s.length() == 0)
/*      */       return; 
/*  407 */     setFillPaint();
/*  408 */     if (this.onlyShapes) {
/*  409 */       drawGlyphVector(this.font.layoutGlyphVector(getFontRenderContext(), s.toCharArray(), 0, s.length(), 0), x, y);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  414 */       boolean restoreTextRenderingMode = false;
/*      */ 
/*      */       
/*  417 */       AffineTransform at = getTransform();
/*      */       
/*  419 */       AffineTransform at2 = getTransform();
/*  420 */       at2.translate(x, y);
/*  421 */       at2.concatenate(this.font.getTransform());
/*  422 */       setTransform(at2);
/*  423 */       AffineTransform inverse = normalizeMatrix();
/*  424 */       AffineTransform flipper = AffineTransform.getScaleInstance(1.0D, -1.0D);
/*  425 */       inverse.concatenate(flipper);
/*  426 */       this.cb.beginText();
/*  427 */       this.cb.setFontAndSize(this.baseFont, this.fontSize);
/*      */       
/*  429 */       if (this.font.isItalic()) {
/*  430 */         float angle = this.baseFont.getFontDescriptor(4, 1000.0F);
/*  431 */         float angle2 = this.font.getItalicAngle();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  439 */         if (this.font.getFontName().equals(this.font.getName()) || (angle == 0.0F && angle2 == 0.0F)) {
/*      */ 
/*      */           
/*  442 */           if (angle2 == 0.0F) {
/*      */ 
/*      */ 
/*      */             
/*  446 */             angle2 = 10.0F;
/*      */           }
/*      */           else {
/*      */             
/*  450 */             angle2 = -angle2;
/*      */           } 
/*  452 */           if (angle == 0.0F) {
/*      */ 
/*      */             
/*  455 */             AffineTransform skewing = new AffineTransform();
/*  456 */             skewing.setTransform(1.0D, 0.0D, (float)Math.tan(angle2 * Math.PI / 180.0D), 1.0D, 0.0D, 0.0D);
/*  457 */             inverse.concatenate(skewing);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  463 */       double[] mx = new double[6];
/*  464 */       inverse.getMatrix(mx);
/*  465 */       this.cb.setTextMatrix((float)mx[0], (float)mx[1], (float)mx[2], (float)mx[3], (float)mx[4], (float)mx[5]);
/*  466 */       Float fontTextAttributeWidth = (Float)this.font.getAttributes().get(TextAttribute.WIDTH);
/*  467 */       fontTextAttributeWidth = (fontTextAttributeWidth == null) ? TextAttribute.WIDTH_REGULAR : fontTextAttributeWidth;
/*      */ 
/*      */       
/*  470 */       if (!TextAttribute.WIDTH_REGULAR.equals(fontTextAttributeWidth)) {
/*  471 */         this.cb.setHorizontalScaling(100.0F / fontTextAttributeWidth.floatValue());
/*      */       }
/*      */ 
/*      */       
/*  475 */       if (this.baseFont.getPostscriptFontName().toLowerCase().indexOf("bold") < 0) {
/*      */ 
/*      */         
/*  478 */         Float weight = (Float)this.font.getAttributes().get(TextAttribute.WEIGHT);
/*  479 */         if (weight == null) {
/*  480 */           weight = this.font.isBold() ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR;
/*      */         }
/*      */         
/*  483 */         if (this.font.isBold() && (weight
/*  484 */           .floatValue() >= TextAttribute.WEIGHT_SEMIBOLD.floatValue() || this.font
/*  485 */           .getFontName().equals(this.font.getName()))) {
/*      */           
/*  487 */           float strokeWidth = this.font.getSize2D() * (weight.floatValue() - TextAttribute.WEIGHT_REGULAR.floatValue()) / 20.0F;
/*  488 */           if (this.realPaint instanceof Color) {
/*  489 */             this.cb.setTextRenderingMode(2);
/*  490 */             this.cb.setLineWidth(strokeWidth);
/*  491 */             Color color = (Color)this.realPaint;
/*  492 */             int alpha = color.getAlpha();
/*  493 */             if (alpha != this.currentStrokeGState) {
/*  494 */               this.currentStrokeGState = alpha;
/*  495 */               PdfGState gs = this.strokeGState[alpha];
/*  496 */               if (gs == null) {
/*  497 */                 gs = new PdfGState();
/*  498 */                 gs.setStrokeOpacity(alpha / 255.0F);
/*  499 */                 this.strokeGState[alpha] = gs;
/*      */               } 
/*  501 */               this.cb.setGState(gs);
/*      */             } 
/*  503 */             this.cb.setColorStroke(prepareColor(color));
/*  504 */             restoreTextRenderingMode = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  509 */       double width = 0.0D;
/*  510 */       if (this.font.getSize2D() > 0.0F) {
/*  511 */         if (RenderingHints.VALUE_FRACTIONALMETRICS_OFF.equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS))) {
/*  512 */           width = this.font.getStringBounds(s, getFontRenderContext()).getWidth();
/*      */         } else {
/*  514 */           float scale = 1000.0F / this.font.getSize2D();
/*  515 */           Font derivedFont = this.font.deriveFont(AffineTransform.getScaleInstance(scale, scale));
/*  516 */           width = derivedFont.getStringBounds(s, getFontRenderContext()).getWidth();
/*  517 */           if (derivedFont.isTransformed()) {
/*  518 */             width /= scale;
/*      */           }
/*      */         } 
/*      */       }
/*  522 */       Object url = getRenderingHint(HyperLinkKey.KEY_INSTANCE);
/*  523 */       if (url != null && !url.equals(HyperLinkKey.VALUE_HYPERLINKKEY_OFF)) {
/*      */         
/*  525 */         double height = 0.0D;
/*  526 */         if (this.font.getSize2D() > 0.0F)
/*  527 */           if (RenderingHints.VALUE_FRACTIONALMETRICS_OFF.equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS))) {
/*  528 */             height = this.font.getStringBounds(s, getFontRenderContext()).getHeight();
/*      */           } else {
/*  530 */             float scale = 1000.0F / this.font.getSize2D();
/*  531 */             Font derivedFont = this.font.deriveFont(AffineTransform.getScaleInstance(scale, scale));
/*  532 */             height = derivedFont.getStringBounds(s, getFontRenderContext()).getHeight();
/*  533 */             if (derivedFont.isTransformed()) {
/*  534 */               height /= scale;
/*      */             }
/*      */           }  
/*  537 */         double leftX = this.cb.getXTLM();
/*  538 */         double leftY = this.cb.getYTLM();
/*  539 */         PdfAction action = new PdfAction(url.toString());
/*  540 */         this.cb.setAction(action, (float)leftX, (float)leftY, (float)(leftX + width), (float)(leftY + height));
/*      */       } 
/*  542 */       if (s.length() > 1) {
/*  543 */         float adv = ((float)width - this.baseFont.getWidthPoint(s, this.fontSize)) / (s.length() - 1);
/*  544 */         this.cb.setCharacterSpacing(adv);
/*      */       } 
/*  546 */       this.cb.showText(s);
/*  547 */       if (s.length() > 1) {
/*  548 */         this.cb.setCharacterSpacing(0.0F);
/*      */       }
/*  550 */       if (!TextAttribute.WIDTH_REGULAR.equals(fontTextAttributeWidth)) {
/*  551 */         this.cb.setHorizontalScaling(100.0F);
/*      */       }
/*      */       
/*  554 */       if (restoreTextRenderingMode) {
/*  555 */         this.cb.setTextRenderingMode(0);
/*      */       }
/*      */       
/*  558 */       this.cb.endText();
/*  559 */       setTransform(at);
/*  560 */       if (this.underline) {
/*      */ 
/*      */         
/*  563 */         int UnderlineThickness = 50;
/*      */         
/*  565 */         double d = asPoints(UnderlineThickness, (int)this.fontSize);
/*  566 */         Stroke savedStroke = this.originalStroke;
/*  567 */         setStroke(new BasicStroke((float)d));
/*      */ 
/*      */ 
/*      */         
/*  571 */         float lineY = (float)(y + d * 2.0D);
/*  572 */         Line2D line = new Line2D.Double(x, lineY, width + x, lineY);
/*  573 */         draw(line);
/*  574 */         setStroke(savedStroke);
/*      */       } 
/*  576 */       if (this.strikethrough) {
/*      */         
/*  578 */         int StrikethroughThickness = 50;
/*  579 */         int StrikethroughPosition = 350;
/*      */         
/*  581 */         double d = asPoints(StrikethroughThickness, (int)this.fontSize);
/*  582 */         double p = asPoints(StrikethroughPosition, (int)this.fontSize);
/*  583 */         Stroke savedStroke = this.originalStroke;
/*  584 */         setStroke(new BasicStroke((float)d));
/*  585 */         y = (float)(y + asPoints(StrikethroughThickness, (int)this.fontSize));
/*  586 */         Line2D line = new Line2D.Double(x, y - p, width + x, y - p);
/*  587 */         draw(line);
/*  588 */         setStroke(savedStroke);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawString(AttributedCharacterIterator iterator, int x, int y) {
/*  598 */     drawString(iterator, x, y);
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
/*      */   public void drawString(AttributedCharacterIterator iter, float x, float y) {
/*  613 */     StringBuffer stringbuffer = new StringBuffer(iter.getEndIndex()); char c;
/*  614 */     for (c = iter.first(); c != Character.MAX_VALUE; c = iter.next()) {
/*      */       
/*  616 */       if (iter.getIndex() == iter.getRunStart()) {
/*      */         
/*  618 */         if (stringbuffer.length() > 0) {
/*      */           
/*  620 */           drawString(stringbuffer.toString(), x, y);
/*  621 */           FontMetrics fontmetrics = getFontMetrics();
/*  622 */           x = (float)(x + fontmetrics.getStringBounds(stringbuffer.toString(), this).getWidth());
/*  623 */           stringbuffer.delete(0, stringbuffer.length());
/*      */         } 
/*  625 */         doAttributes(iter);
/*      */       } 
/*  627 */       stringbuffer.append(c);
/*      */     } 
/*      */     
/*  630 */     drawString(stringbuffer.toString(), x, y);
/*  631 */     this.underline = false;
/*  632 */     this.strikethrough = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawGlyphVector(GlyphVector g, float x, float y) {
/*  640 */     Shape s = g.getOutline(x, y);
/*  641 */     fill(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fill(Shape s) {
/*  649 */     followPath(s, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
/*  657 */     if (onStroke) {
/*  658 */       s = this.stroke.createStrokedShape(s);
/*      */     }
/*  660 */     s = this.transform.createTransformedShape(s);
/*  661 */     Area area = new Area(s);
/*  662 */     if (this.clip != null)
/*  663 */       area.intersect(this.clip); 
/*  664 */     return area.intersects(rect.x, rect.y, rect.width, rect.height);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GraphicsConfiguration getDeviceConfiguration() {
/*  672 */     return getDG2().getDeviceConfiguration();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComposite(Composite comp) {
/*  682 */     if (comp instanceof AlphaComposite) {
/*      */       
/*  684 */       AlphaComposite composite = (AlphaComposite)comp;
/*      */       
/*  686 */       if (composite.getRule() == 3) {
/*      */         
/*  688 */         this.alpha = composite.getAlpha();
/*  689 */         this.composite = composite;
/*      */         
/*  691 */         if (this.realPaint != null && this.realPaint instanceof Color) {
/*      */           
/*  693 */           Color c = (Color)this.realPaint;
/*  694 */           this
/*  695 */             .paint = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha() * this.alpha));
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*  701 */     this.composite = comp;
/*  702 */     this.alpha = 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaint(Paint paint) {
/*  712 */     if (paint == null)
/*      */       return; 
/*  714 */     this.paint = paint;
/*  715 */     this.realPaint = paint;
/*      */     
/*  717 */     if (this.composite instanceof AlphaComposite && paint instanceof Color) {
/*      */       
/*  719 */       AlphaComposite co = (AlphaComposite)this.composite;
/*      */       
/*  721 */       if (co.getRule() == 3) {
/*  722 */         Color c = (Color)paint;
/*  723 */         this.paint = new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha() * this.alpha));
/*  724 */         this.realPaint = paint;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Stroke transformStroke(Stroke stroke) {
/*  731 */     if (!(stroke instanceof BasicStroke))
/*  732 */       return stroke; 
/*  733 */     BasicStroke st = (BasicStroke)stroke;
/*  734 */     float scale = (float)Math.sqrt(Math.abs(this.transform.getDeterminant()));
/*  735 */     float[] dash = st.getDashArray();
/*  736 */     if (dash != null)
/*  737 */       for (int k = 0; k < dash.length; k++) {
/*  738 */         dash[k] = dash[k] * scale;
/*      */       } 
/*  740 */     return new BasicStroke(st.getLineWidth() * scale, st.getEndCap(), st.getLineJoin(), st.getMiterLimit(), dash, st.getDashPhase() * scale);
/*      */   }
/*      */   private void setStrokeDiff(Stroke newStroke, Stroke oldStroke) {
/*      */     boolean makeDash;
/*  744 */     if (newStroke == oldStroke)
/*      */       return; 
/*  746 */     if (!(newStroke instanceof BasicStroke))
/*      */       return; 
/*  748 */     BasicStroke nStroke = (BasicStroke)newStroke;
/*  749 */     boolean oldOk = oldStroke instanceof BasicStroke;
/*  750 */     BasicStroke oStroke = null;
/*  751 */     if (oldOk)
/*  752 */       oStroke = (BasicStroke)oldStroke; 
/*  753 */     if (!oldOk || nStroke.getLineWidth() != oStroke.getLineWidth())
/*  754 */       this.cb.setLineWidth(nStroke.getLineWidth()); 
/*  755 */     if (!oldOk || nStroke.getEndCap() != oStroke.getEndCap())
/*  756 */       switch (nStroke.getEndCap()) {
/*      */         case 0:
/*  758 */           this.cb.setLineCap(0);
/*      */           break;
/*      */         case 2:
/*  761 */           this.cb.setLineCap(2);
/*      */           break;
/*      */         default:
/*  764 */           this.cb.setLineCap(1);
/*      */           break;
/*      */       }  
/*  767 */     if (!oldOk || nStroke.getLineJoin() != oStroke.getLineJoin())
/*  768 */       switch (nStroke.getLineJoin()) {
/*      */         case 0:
/*  770 */           this.cb.setLineJoin(0);
/*      */           break;
/*      */         case 2:
/*  773 */           this.cb.setLineJoin(2);
/*      */           break;
/*      */         default:
/*  776 */           this.cb.setLineJoin(1);
/*      */           break;
/*      */       }  
/*  779 */     if (!oldOk || nStroke.getMiterLimit() != oStroke.getMiterLimit()) {
/*  780 */       this.cb.setMiterLimit(nStroke.getMiterLimit());
/*      */     }
/*  782 */     if (oldOk) {
/*  783 */       if (nStroke.getDashArray() != null) {
/*  784 */         if (nStroke.getDashPhase() != oStroke.getDashPhase()) {
/*  785 */           makeDash = true;
/*      */         }
/*  787 */         else if (!Arrays.equals(nStroke.getDashArray(), oStroke.getDashArray())) {
/*  788 */           makeDash = true;
/*      */         } else {
/*      */           
/*  791 */           makeDash = false;
/*      */         } 
/*  793 */       } else if (oStroke.getDashArray() != null) {
/*  794 */         makeDash = true;
/*      */       } else {
/*      */         
/*  797 */         makeDash = false;
/*      */       } 
/*      */     } else {
/*  800 */       makeDash = true;
/*      */     } 
/*  802 */     if (makeDash) {
/*  803 */       float[] dash = nStroke.getDashArray();
/*  804 */       if (dash == null) {
/*  805 */         this.cb.setLiteral("[]0 d\n");
/*      */       } else {
/*  807 */         this.cb.setLiteral('[');
/*  808 */         int lim = dash.length;
/*  809 */         for (int k = 0; k < lim; k++) {
/*  810 */           this.cb.setLiteral(dash[k]);
/*  811 */           this.cb.setLiteral(' ');
/*      */         } 
/*  813 */         this.cb.setLiteral(']');
/*  814 */         this.cb.setLiteral(nStroke.getDashPhase());
/*  815 */         this.cb.setLiteral(" d\n");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStroke(Stroke s) {
/*  825 */     this.originalStroke = s;
/*  826 */     this.stroke = transformStroke(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
/*  837 */     if (hintValue != null) {
/*  838 */       this.rhints.put(hintKey, hintValue);
/*      */     }
/*  840 */     else if (hintKey instanceof HyperLinkKey) {
/*  841 */       this.rhints.put(hintKey, HyperLinkKey.VALUE_HYPERLINKKEY_OFF);
/*      */     } else {
/*      */       
/*  844 */       this.rhints.remove(hintKey);
/*      */     } 
/*      */     
/*  847 */     if (this.dg2 != null) {
/*  848 */       this.dg2.setRenderingHint(hintKey, hintValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getRenderingHint(RenderingHints.Key hintKey) {
/*  858 */     return this.rhints.get(hintKey);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRenderingHints(Map<?, ?> hints) {
/*  866 */     this.rhints.clear();
/*  867 */     this.rhints.putAll(hints);
/*  868 */     if (this.dg2 != null) {
/*  869 */       this.dg2.setRenderingHints(hints);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addRenderingHints(Map<?, ?> hints) {
/*  878 */     this.rhints.putAll(hints);
/*  879 */     if (this.dg2 != null) {
/*  880 */       this.dg2.addRenderingHints(hints);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RenderingHints getRenderingHints() {
/*  889 */     return this.rhints;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void translate(int x, int y) {
/*  897 */     translate(x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void translate(double tx, double ty) {
/*  905 */     this.transform.translate(tx, ty);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rotate(double theta) {
/*  913 */     this.transform.rotate(theta);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rotate(double theta, double x, double y) {
/*  921 */     this.transform.rotate(theta, x, y);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void scale(double sx, double sy) {
/*  929 */     this.transform.scale(sx, sy);
/*  930 */     this.stroke = transformStroke(this.originalStroke);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void shear(double shx, double shy) {
/*  938 */     this.transform.shear(shx, shy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void transform(AffineTransform tx) {
/*  946 */     this.transform.concatenate(tx);
/*  947 */     this.stroke = transformStroke(this.originalStroke);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransform(AffineTransform t) {
/*  955 */     this.transform = new AffineTransform(t);
/*  956 */     this.stroke = transformStroke(this.originalStroke);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AffineTransform getTransform() {
/*  964 */     return new AffineTransform(this.transform);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Paint getPaint() {
/*  973 */     if (this.realPaint != null) {
/*  974 */       return this.realPaint;
/*      */     }
/*  976 */     return this.paint;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Composite getComposite() {
/*  985 */     return this.composite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBackground(Color color) {
/*  993 */     this.background = color;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getBackground() {
/* 1001 */     return this.background;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Stroke getStroke() {
/* 1009 */     return this.originalStroke;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FontRenderContext getFontRenderContext() {
/* 1018 */     boolean antialias = RenderingHints.VALUE_TEXT_ANTIALIAS_ON.equals(getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
/* 1019 */     boolean fractions = RenderingHints.VALUE_FRACTIONALMETRICS_ON.equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS));
/* 1020 */     return new FontRenderContext(new AffineTransform(), antialias, fractions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Graphics create() {
/* 1028 */     PdfGraphics2D g2 = new PdfGraphics2D();
/* 1029 */     g2.rhints.putAll(this.rhints);
/* 1030 */     g2.onlyShapes = this.onlyShapes;
/* 1031 */     g2.transform = new AffineTransform(this.transform);
/* 1032 */     g2.baseFonts = this.baseFonts;
/* 1033 */     g2.fontMapper = this.fontMapper;
/* 1034 */     g2.paint = this.paint;
/* 1035 */     g2.fillGState = this.fillGState;
/* 1036 */     g2.currentFillGState = this.currentFillGState;
/* 1037 */     g2.strokeGState = this.strokeGState;
/* 1038 */     g2.background = this.background;
/* 1039 */     g2.mediaTracker = this.mediaTracker;
/* 1040 */     g2.convertImagesToJPEG = this.convertImagesToJPEG;
/* 1041 */     g2.jpegQuality = this.jpegQuality;
/* 1042 */     g2.setFont(this.font);
/* 1043 */     g2.cb = this.cb.getDuplicate();
/* 1044 */     g2.cb.saveState();
/* 1045 */     g2.width = this.width;
/* 1046 */     g2.height = this.height;
/* 1047 */     g2.followPath(new Area(new Rectangle2D.Float(0.0F, 0.0F, this.width, this.height)), 3);
/* 1048 */     if (this.clip != null)
/* 1049 */       g2.clip = new Area(this.clip); 
/* 1050 */     g2.composite = this.composite;
/* 1051 */     g2.stroke = this.stroke;
/* 1052 */     g2.originalStroke = this.originalStroke;
/* 1053 */     g2.strokeOne = (BasicStroke)g2.transformStroke(g2.strokeOne);
/* 1054 */     g2.oldStroke = g2.strokeOne;
/* 1055 */     g2.setStrokeDiff(g2.oldStroke, null);
/* 1056 */     g2.cb.saveState();
/* 1057 */     if (g2.clip != null)
/* 1058 */       g2.followPath(g2.clip, 3); 
/* 1059 */     g2.kid = true;
/* 1060 */     if (this.kids == null)
/* 1061 */       this.kids = new ArrayList<Kid>(); 
/* 1062 */     this.kids.add(new Kid(this.cb.getInternalBuffer().size(), g2));
/* 1063 */     return g2;
/*      */   }
/*      */   
/*      */   public PdfContentByte getContent() {
/* 1067 */     return this.cb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getColor() {
/* 1074 */     if (this.paint instanceof Color) {
/* 1075 */       return (Color)this.paint;
/*      */     }
/* 1077 */     return Color.black;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setColor(Color color) {
/* 1086 */     setPaint(color);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPaintMode() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setXORMode(Color c1) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Font getFont() {
/* 1108 */     return this.font;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFont(Font f) {
/* 1119 */     if (f == null)
/*      */       return; 
/* 1121 */     if (this.onlyShapes) {
/* 1122 */       this.font = f;
/*      */       return;
/*      */     } 
/* 1125 */     if (f == this.font)
/*      */       return; 
/* 1127 */     this.font = f;
/* 1128 */     this.fontSize = f.getSize2D();
/* 1129 */     this.baseFont = getCachedBaseFont(f);
/*      */   }
/*      */   
/*      */   private BaseFont getCachedBaseFont(Font f) {
/* 1133 */     synchronized (this.baseFonts) {
/* 1134 */       BaseFont bf = this.baseFonts.get(f.getFontName());
/* 1135 */       if (bf == null) {
/* 1136 */         bf = this.fontMapper.awtToPdf(f);
/* 1137 */         this.baseFonts.put(f.getFontName(), bf);
/*      */       } 
/* 1139 */       return bf;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FontMetrics getFontMetrics(Font f) {
/* 1148 */     return getDG2().getFontMetrics(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Rectangle getClipBounds() {
/* 1156 */     if (this.clip == null)
/* 1157 */       return null; 
/* 1158 */     return getClip().getBounds();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clipRect(int x, int y, int width, int height) {
/* 1166 */     Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
/* 1167 */     clip(rect);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClip(int x, int y, int width, int height) {
/* 1175 */     Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
/* 1176 */     setClip(rect);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clip(Shape s) {
/* 1184 */     if (s == null) {
/* 1185 */       setClip(null);
/*      */       return;
/*      */     } 
/* 1188 */     s = this.transform.createTransformedShape(s);
/* 1189 */     if (this.clip == null) {
/* 1190 */       this.clip = new Area(s);
/*      */     } else {
/* 1192 */       this.clip.intersect(new Area(s));
/* 1193 */     }  followPath(s, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Shape getClip() {
/*      */     try {
/* 1202 */       return this.transform.createInverse().createTransformedShape(this.clip);
/*      */     }
/* 1204 */     catch (NoninvertibleTransformException e) {
/* 1205 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClip(Shape s) {
/* 1214 */     this.cb.restoreState();
/* 1215 */     this.cb.saveState();
/* 1216 */     if (s != null)
/* 1217 */       s = this.transform.createTransformedShape(s); 
/* 1218 */     if (s == null) {
/* 1219 */       this.clip = null;
/*      */     } else {
/*      */       
/* 1222 */       this.clip = new Area(s);
/* 1223 */       followPath(s, 3);
/*      */     } 
/* 1225 */     this.paintFill = this.paintStroke = null;
/* 1226 */     this.currentFillGState = this.currentStrokeGState = -1;
/* 1227 */     this.oldStroke = this.strokeOne;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyArea(int x, int y, int width, int height, int dx, int dy) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawLine(int x1, int y1, int x2, int y2) {
/* 1243 */     Line2D line = new Line2D.Double(x1, y1, x2, y2);
/* 1244 */     draw(line);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawRect(int x, int y, int width, int height) {
/* 1252 */     draw(new Rectangle(x, y, width, height));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillRect(int x, int y, int width, int height) {
/* 1260 */     fill(new Rectangle(x, y, width, height));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearRect(int x, int y, int width, int height) {
/* 1268 */     Paint temp = this.paint;
/* 1269 */     setPaint(this.background);
/* 1270 */     fillRect(x, y, width, height);
/* 1271 */     setPaint(temp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
/* 1279 */     RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
/* 1280 */     draw(rect);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
/* 1288 */     RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight);
/* 1289 */     fill(rect);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawOval(int x, int y, int width, int height) {
/* 1297 */     Ellipse2D oval = new Ellipse2D.Float(x, y, width, height);
/* 1298 */     draw(oval);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillOval(int x, int y, int width, int height) {
/* 1306 */     Ellipse2D oval = new Ellipse2D.Float(x, y, width, height);
/* 1307 */     fill(oval);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
/* 1315 */     Arc2D arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, 0);
/* 1316 */     draw(arc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
/* 1325 */     Arc2D arc = new Arc2D.Double(x, y, width, height, startAngle, arcAngle, 2);
/* 1326 */     fill(arc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawPolyline(int[] x, int[] y, int nPoints) {
/* 1334 */     PolylineShape polyline = new PolylineShape(x, y, nPoints);
/* 1335 */     draw((Shape)polyline);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
/* 1343 */     Polygon poly = new Polygon(xPoints, yPoints, nPoints);
/* 1344 */     draw(poly);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
/* 1352 */     Polygon poly = new Polygon();
/* 1353 */     for (int i = 0; i < nPoints; i++) {
/* 1354 */       poly.addPoint(xPoints[i], yPoints[i]);
/*      */     }
/* 1356 */     fill(poly);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
/* 1364 */     return drawImage(img, x, y, (Color)null, observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
/* 1372 */     return drawImage(img, x, y, width, height, null, observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
/* 1380 */     waitForImage(img);
/* 1381 */     return drawImage(img, x, y, img.getWidth(observer), img.getHeight(observer), bgcolor, observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
/* 1389 */     waitForImage(img);
/* 1390 */     double scalex = width / img.getWidth(observer);
/* 1391 */     double scaley = height / img.getHeight(observer);
/* 1392 */     AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
/* 1393 */     tx.scale(scalex, scaley);
/* 1394 */     return drawImage(img, (Image)null, tx, bgcolor, observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
/* 1402 */     return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
/* 1410 */     waitForImage(img);
/* 1411 */     double dwidth = dx2 - dx1;
/* 1412 */     double dheight = dy2 - dy1;
/* 1413 */     double swidth = sx2 - sx1;
/* 1414 */     double sheight = sy2 - sy1;
/*      */ 
/*      */     
/* 1417 */     if (dwidth == 0.0D || dheight == 0.0D || swidth == 0.0D || sheight == 0.0D) return true;
/*      */     
/* 1419 */     double scalex = dwidth / swidth;
/* 1420 */     double scaley = dheight / sheight;
/*      */     
/* 1422 */     double transx = sx1 * scalex;
/* 1423 */     double transy = sy1 * scaley;
/* 1424 */     AffineTransform tx = AffineTransform.getTranslateInstance(dx1 - transx, dy1 - transy);
/* 1425 */     tx.scale(scalex, scaley);
/*      */     
/* 1427 */     BufferedImage mask = new BufferedImage(img.getWidth(observer), img.getHeight(observer), 12);
/* 1428 */     Graphics g = mask.getGraphics();
/* 1429 */     g.fillRect(sx1, sy1, (int)swidth, (int)sheight);
/* 1430 */     drawImage(img, mask, tx, (Color)null, observer);
/* 1431 */     g.dispose();
/* 1432 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1440 */     if (this.kid)
/*      */       return; 
/* 1442 */     if (!this.disposeCalled) {
/* 1443 */       this.disposeCalled = true;
/* 1444 */       this.cb.restoreState();
/* 1445 */       this.cb.restoreState();
/* 1446 */       if (this.dg2 != null) {
/* 1447 */         this.dg2.dispose();
/* 1448 */         this.dg2 = null;
/*      */       } 
/* 1450 */       if (this.kids != null) {
/* 1451 */         ByteBuffer buf = new ByteBuffer();
/* 1452 */         internalDispose(buf);
/* 1453 */         ByteBuffer buf2 = this.cb.getInternalBuffer();
/* 1454 */         buf2.reset();
/* 1455 */         buf2.append(buf);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void internalDispose(ByteBuffer buf) {
/* 1461 */     int last = 0;
/* 1462 */     int pos = 0;
/* 1463 */     ByteBuffer buf2 = this.cb.getInternalBuffer();
/* 1464 */     if (this.kids != null) {
/* 1465 */       for (Kid kid : this.kids) {
/* 1466 */         pos = kid.pos;
/* 1467 */         PdfGraphics2D g2 = kid.graphics;
/* 1468 */         g2.cb.restoreState();
/* 1469 */         g2.cb.restoreState();
/* 1470 */         buf.append(buf2.getBuffer(), last, pos - last);
/* 1471 */         if (g2.dg2 != null) {
/* 1472 */           g2.dg2.dispose();
/* 1473 */           g2.dg2 = null;
/*      */         } 
/* 1475 */         g2.internalDispose(buf);
/* 1476 */         last = pos;
/*      */       } 
/*      */     }
/* 1479 */     buf.append(buf2.getBuffer(), last, buf2.size() - last);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void followPath(Shape s, int drawType) {
/*      */     PathIterator points;
/* 1491 */     if (s == null)
/* 1492 */       return;  if (drawType == 2 && 
/* 1493 */       !(this.stroke instanceof BasicStroke)) {
/* 1494 */       s = this.stroke.createStrokedShape(s);
/* 1495 */       followPath(s, 1);
/*      */       
/*      */       return;
/*      */     } 
/* 1499 */     if (drawType == 2) {
/* 1500 */       setStrokeDiff(this.stroke, this.oldStroke);
/* 1501 */       this.oldStroke = this.stroke;
/* 1502 */       setStrokePaint();
/*      */     }
/* 1504 */     else if (drawType == 1) {
/* 1505 */       setFillPaint();
/*      */     } 
/* 1507 */     int traces = 0;
/* 1508 */     if (drawType == 3) {
/* 1509 */       points = s.getPathIterator(IDENTITY);
/*      */     } else {
/* 1511 */       points = s.getPathIterator(this.transform);
/* 1512 */     }  float[] coords = new float[6];
/* 1513 */     double[] dcoords = new double[6];
/* 1514 */     while (!points.isDone()) {
/* 1515 */       traces++;
/*      */       
/* 1517 */       int segtype = points.currentSegment(dcoords);
/* 1518 */       int numpoints = (segtype == 4) ? 0 : ((segtype == 2) ? 2 : ((segtype == 3) ? 3 : 1));
/*      */ 
/*      */ 
/*      */       
/* 1522 */       for (int i = 0; i < numpoints * 2; i++) {
/* 1523 */         coords[i] = (float)dcoords[i];
/*      */       }
/*      */       
/* 1526 */       normalizeY(coords);
/* 1527 */       switch (segtype) {
/*      */         case 4:
/* 1529 */           this.cb.closePath();
/*      */           break;
/*      */         
/*      */         case 3:
/* 1533 */           this.cb.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
/*      */           break;
/*      */         
/*      */         case 1:
/* 1537 */           this.cb.lineTo(coords[0], coords[1]);
/*      */           break;
/*      */         
/*      */         case 0:
/* 1541 */           this.cb.moveTo(coords[0], coords[1]);
/*      */           break;
/*      */         
/*      */         case 2:
/* 1545 */           this.cb.curveTo(coords[0], coords[1], coords[2], coords[3]);
/*      */           break;
/*      */       } 
/* 1548 */       points.next();
/*      */     } 
/* 1550 */     switch (drawType) {
/*      */       case 1:
/* 1552 */         if (traces > 0)
/* 1553 */           if (points.getWindingRule() == 0) {
/* 1554 */             this.cb.eoFill();
/*      */           } else {
/* 1556 */             this.cb.fill();
/*      */           }  
/*      */         return;
/*      */       case 2:
/* 1560 */         if (traces > 0)
/* 1561 */           this.cb.stroke(); 
/*      */         return;
/*      */     } 
/* 1564 */     if (traces == 0)
/* 1565 */       this.cb.rectangle(0.0F, 0.0F, 0.0F, 0.0F); 
/* 1566 */     if (points.getWindingRule() == 0) {
/* 1567 */       this.cb.eoClip();
/*      */     } else {
/* 1569 */       this.cb.clip();
/* 1570 */     }  this.cb.newPath();
/*      */   }
/*      */ 
/*      */   
/*      */   private float normalizeY(float y) {
/* 1575 */     return this.height - y;
/*      */   }
/*      */   
/*      */   private void normalizeY(float[] coords) {
/* 1579 */     coords[1] = normalizeY(coords[1]);
/* 1580 */     coords[3] = normalizeY(coords[3]);
/* 1581 */     coords[5] = normalizeY(coords[5]);
/*      */   }
/*      */   
/*      */   protected AffineTransform normalizeMatrix() {
/* 1585 */     double[] mx = new double[6];
/* 1586 */     AffineTransform result = AffineTransform.getTranslateInstance(0.0D, 0.0D);
/* 1587 */     result.getMatrix(mx);
/* 1588 */     mx[3] = -1.0D;
/* 1589 */     mx[5] = this.height;
/* 1590 */     result = new AffineTransform(mx);
/* 1591 */     result.concatenate(this.transform);
/* 1592 */     return result;
/*      */   }
/*      */   
/*      */   private boolean drawImage(Image img, Image mask, AffineTransform xform, Color bgColor, ImageObserver obs) {
/* 1596 */     if (xform == null) {
/* 1597 */       xform = new AffineTransform();
/*      */     } else {
/* 1599 */       xform = new AffineTransform(xform);
/* 1600 */     }  xform.translate(0.0D, img.getHeight(obs));
/* 1601 */     xform.scale(img.getWidth(obs), img.getHeight(obs));
/*      */     
/* 1603 */     AffineTransform inverse = normalizeMatrix();
/* 1604 */     AffineTransform flipper = AffineTransform.getScaleInstance(1.0D, -1.0D);
/* 1605 */     inverse.concatenate(xform);
/* 1606 */     inverse.concatenate(flipper);
/*      */     
/* 1608 */     double[] mx = new double[6];
/* 1609 */     inverse.getMatrix(mx);
/* 1610 */     if (this.currentFillGState != 255) {
/* 1611 */       PdfGState gs = this.fillGState[255];
/* 1612 */       if (gs == null) {
/* 1613 */         gs = new PdfGState();
/* 1614 */         gs.setFillOpacity(1.0F);
/* 1615 */         this.fillGState[255] = gs;
/*      */       } 
/* 1617 */       this.cb.setGState(gs);
/*      */     } 
/*      */     
/*      */     try {
/* 1621 */       Image image = null;
/* 1622 */       if (!this.convertImagesToJPEG) {
/* 1623 */         image = Image.getInstance(img, bgColor);
/*      */       } else {
/*      */         
/* 1626 */         BufferedImage scaled = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
/* 1627 */         Graphics2D g3 = scaled.createGraphics();
/* 1628 */         g3.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
/* 1629 */         g3.dispose();
/*      */         
/* 1631 */         ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 1632 */         ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
/* 1633 */         iwparam.setCompressionMode(2);
/* 1634 */         iwparam.setCompressionQuality(this.jpegQuality);
/* 1635 */         ImageWriter iw = ImageIO.getImageWritersByFormatName("jpg").next();
/* 1636 */         ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
/* 1637 */         iw.setOutput(ios);
/* 1638 */         iw.write(null, new IIOImage(scaled, null, null), iwparam);
/* 1639 */         iw.dispose();
/* 1640 */         ios.close();
/*      */         
/* 1642 */         scaled.flush();
/* 1643 */         scaled = null;
/* 1644 */         image = Image.getInstance(baos.toByteArray());
/*      */       } 
/*      */       
/* 1647 */       if (mask != null) {
/* 1648 */         Image msk = Image.getInstance(mask, null, true);
/* 1649 */         msk.makeMask();
/* 1650 */         msk.setInverted(true);
/* 1651 */         image.setImageMask(msk);
/*      */       } 
/* 1653 */       this.cb.addImage(image, (float)mx[0], (float)mx[1], (float)mx[2], (float)mx[3], (float)mx[4], (float)mx[5]);
/* 1654 */       Object url = getRenderingHint(HyperLinkKey.KEY_INSTANCE);
/* 1655 */       if (url != null && !url.equals(HyperLinkKey.VALUE_HYPERLINKKEY_OFF)) {
/* 1656 */         PdfAction action = new PdfAction(url.toString());
/* 1657 */         this.cb.setAction(action, (float)mx[4], (float)mx[5], (float)(mx[0] + mx[4]), (float)(mx[3] + mx[5]));
/*      */       } 
/* 1659 */     } catch (Exception ex) {
/* 1660 */       throw new IllegalArgumentException(ex);
/*      */     } 
/* 1662 */     if (this.currentFillGState >= 0 && this.currentFillGState != 255) {
/* 1663 */       PdfGState gs = this.fillGState[this.currentFillGState];
/* 1664 */       this.cb.setGState(gs);
/*      */     } 
/* 1666 */     return true;
/*      */   }
/*      */   
/*      */   private boolean checkNewPaint(Paint oldPaint) {
/* 1670 */     if (this.paint == oldPaint)
/* 1671 */       return false; 
/* 1672 */     return (!(this.paint instanceof Color) || !this.paint.equals(oldPaint));
/*      */   }
/*      */   
/*      */   private void setFillPaint() {
/* 1676 */     if (checkNewPaint(this.paintFill)) {
/* 1677 */       this.paintFill = this.paint;
/* 1678 */       setPaint(false, 0.0D, 0.0D, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setStrokePaint() {
/* 1683 */     if (checkNewPaint(this.paintStroke)) {
/* 1684 */       this.paintStroke = this.paint;
/* 1685 */       setPaint(false, 0.0D, 0.0D, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static BaseColor prepareColor(Color color) {
/* 1690 */     if (color.getColorSpace().getType() == 9) {
/* 1691 */       float[] comp = color.getColorComponents(null);
/* 1692 */       return (BaseColor)new CMYKColor(comp[0], comp[1], comp[2], comp[3]);
/*      */     } 
/* 1694 */     return new BaseColor(color.getRGB());
/*      */   }
/*      */ 
/*      */   
/*      */   private void setPaint(boolean invert, double xoffset, double yoffset, boolean fill) {
/* 1699 */     if (this.paint instanceof Color) {
/* 1700 */       Color color = (Color)this.paint;
/* 1701 */       int alpha = color.getAlpha();
/* 1702 */       if (fill) {
/* 1703 */         if (alpha != this.currentFillGState) {
/* 1704 */           this.currentFillGState = alpha;
/* 1705 */           PdfGState gs = this.fillGState[alpha];
/* 1706 */           if (gs == null) {
/* 1707 */             gs = new PdfGState();
/* 1708 */             gs.setFillOpacity(alpha / 255.0F);
/* 1709 */             this.fillGState[alpha] = gs;
/*      */           } 
/* 1711 */           this.cb.setGState(gs);
/*      */         } 
/* 1713 */         this.cb.setColorFill(prepareColor(color));
/*      */       } else {
/*      */         
/* 1716 */         if (alpha != this.currentStrokeGState) {
/* 1717 */           this.currentStrokeGState = alpha;
/* 1718 */           PdfGState gs = this.strokeGState[alpha];
/* 1719 */           if (gs == null) {
/* 1720 */             gs = new PdfGState();
/* 1721 */             gs.setStrokeOpacity(alpha / 255.0F);
/* 1722 */             this.strokeGState[alpha] = gs;
/*      */           } 
/* 1724 */           this.cb.setGState(gs);
/*      */         } 
/* 1726 */         this.cb.setColorStroke(prepareColor(color));
/*      */       }
/*      */     
/* 1729 */     } else if (this.paint instanceof GradientPaint) {
/* 1730 */       GradientPaint gp = (GradientPaint)this.paint;
/* 1731 */       Point2D p1 = gp.getPoint1();
/* 1732 */       this.transform.transform(p1, p1);
/* 1733 */       Point2D p2 = gp.getPoint2();
/* 1734 */       this.transform.transform(p2, p2);
/* 1735 */       Color c1 = gp.getColor1();
/* 1736 */       Color c2 = gp.getColor2();
/* 1737 */       PdfShading shading = PdfShading.simpleAxial(this.cb.getPdfWriter(), (float)p1.getX(), normalizeY((float)p1.getY()), (float)p2.getX(), normalizeY((float)p2.getY()), new BaseColor(c1.getRGB()), new BaseColor(c2.getRGB()));
/* 1738 */       PdfShadingPattern pat = new PdfShadingPattern(shading);
/* 1739 */       if (fill) {
/* 1740 */         this.cb.setShadingFill(pat);
/*      */       } else {
/* 1742 */         this.cb.setShadingStroke(pat);
/*      */       } 
/* 1744 */     } else if (this.paint instanceof TexturePaint) {
/*      */       try {
/* 1746 */         TexturePaint tp = (TexturePaint)this.paint;
/* 1747 */         BufferedImage img = tp.getImage();
/* 1748 */         Rectangle2D rect = tp.getAnchorRect();
/* 1749 */         Image image = Image.getInstance(img, null);
/* 1750 */         PdfPatternPainter pattern = this.cb.createPattern(image.getWidth(), image.getHeight());
/* 1751 */         AffineTransform inverse = normalizeMatrix();
/* 1752 */         inverse.translate(rect.getX(), rect.getY());
/* 1753 */         inverse.scale(rect.getWidth() / image.getWidth(), -rect.getHeight() / image.getHeight());
/* 1754 */         double[] mx = new double[6];
/* 1755 */         inverse.getMatrix(mx);
/* 1756 */         pattern.setPatternMatrix((float)mx[0], (float)mx[1], (float)mx[2], (float)mx[3], (float)mx[4], (float)mx[5]);
/* 1757 */         image.setAbsolutePosition(0.0F, 0.0F);
/* 1758 */         pattern.addImage(image);
/* 1759 */         if (fill)
/* 1760 */         { this.cb.setPatternFill(pattern); }
/*      */         else
/* 1762 */         { this.cb.setPatternStroke(pattern); } 
/* 1763 */       } catch (Exception ex) {
/* 1764 */         if (fill) {
/* 1765 */           this.cb.setColorFill(BaseColor.GRAY);
/*      */         } else {
/* 1767 */           this.cb.setColorStroke(BaseColor.GRAY);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       try {
/* 1772 */         BufferedImage img = null;
/* 1773 */         int type = 6;
/* 1774 */         if (this.paint.getTransparency() == 1) {
/* 1775 */           type = 5;
/*      */         }
/* 1777 */         img = new BufferedImage((int)this.width, (int)this.height, type);
/* 1778 */         Graphics2D g = (Graphics2D)img.getGraphics();
/* 1779 */         g.transform(this.transform);
/* 1780 */         AffineTransform inv = this.transform.createInverse();
/* 1781 */         Shape fillRect = new Rectangle2D.Double(0.0D, 0.0D, img.getWidth(), img.getHeight());
/* 1782 */         fillRect = inv.createTransformedShape(fillRect);
/* 1783 */         g.setPaint(this.paint);
/* 1784 */         g.fill(fillRect);
/* 1785 */         if (invert) {
/* 1786 */           AffineTransform tx = new AffineTransform();
/* 1787 */           tx.scale(1.0D, -1.0D);
/* 1788 */           tx.translate(-xoffset, -yoffset);
/* 1789 */           g.drawImage(img, tx, (ImageObserver)null);
/*      */         } 
/* 1791 */         g.dispose();
/* 1792 */         g = null;
/* 1793 */         Image image = Image.getInstance(img, null);
/* 1794 */         PdfPatternPainter pattern = this.cb.createPattern(this.width, this.height);
/* 1795 */         image.setAbsolutePosition(0.0F, 0.0F);
/* 1796 */         pattern.addImage(image);
/* 1797 */         if (fill)
/* 1798 */         { this.cb.setPatternFill(pattern); }
/*      */         else
/* 1800 */         { this.cb.setPatternStroke(pattern); } 
/* 1801 */       } catch (Exception ex) {
/* 1802 */         if (fill) {
/* 1803 */           this.cb.setColorFill(BaseColor.GRAY);
/*      */         } else {
/* 1805 */           this.cb.setColorStroke(BaseColor.GRAY);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private synchronized void waitForImage(Image image) {
/* 1811 */     if (this.mediaTracker == null)
/* 1812 */       this.mediaTracker = new MediaTracker(new FakeComponent()); 
/* 1813 */     this.mediaTracker.addImage(image, 0);
/*      */     try {
/* 1815 */       this.mediaTracker.waitForID(0);
/*      */     }
/* 1817 */     catch (InterruptedException interruptedException) {}
/*      */ 
/*      */     
/* 1820 */     this.mediaTracker.removeImage(image);
/*      */   }
/*      */   
/*      */   private static class FakeComponent
/*      */     extends Component
/*      */   {
/*      */     private static final long serialVersionUID = 6450197945596086638L;
/*      */     
/*      */     private FakeComponent() {}
/*      */   }
/*      */   
/*      */   public static class HyperLinkKey
/*      */     extends RenderingHints.Key {
/* 1833 */     public static final HyperLinkKey KEY_INSTANCE = new HyperLinkKey(9999);
/* 1834 */     public static final Object VALUE_HYPERLINKKEY_OFF = "0";
/*      */     
/*      */     protected HyperLinkKey(int arg0) {
/* 1837 */       super(arg0);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isCompatibleValue(Object val) {
/* 1843 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1848 */       return "HyperLinkKey";
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/PdfGraphics2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */