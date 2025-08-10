/*      */ package com.itextpdf.text;
/*      */ 
/*      */ import com.itextpdf.awt.PdfGraphics2D;
/*      */ import com.itextpdf.text.api.Indentable;
/*      */ import com.itextpdf.text.api.Spaceable;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.pdf.ICC_Profile;
/*      */ import com.itextpdf.text.pdf.PRIndirectReference;
/*      */ import com.itextpdf.text.pdf.PdfArray;
/*      */ import com.itextpdf.text.pdf.PdfContentByte;
/*      */ import com.itextpdf.text.pdf.PdfDictionary;
/*      */ import com.itextpdf.text.pdf.PdfIndirectReference;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfNumber;
/*      */ import com.itextpdf.text.pdf.PdfOCG;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfReader;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import com.itextpdf.text.pdf.PdfTemplate;
/*      */ import com.itextpdf.text.pdf.PdfWriter;
/*      */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*      */ import com.itextpdf.text.pdf.codec.BmpImage;
/*      */ import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
/*      */ import com.itextpdf.text.pdf.codec.GifImage;
/*      */ import com.itextpdf.text.pdf.codec.JBIG2Image;
/*      */ import com.itextpdf.text.pdf.codec.PngImage;
/*      */ import com.itextpdf.text.pdf.codec.TiffImage;
/*      */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*      */ import com.itextpdf.text.pdf.interfaces.IAlternateDescription;
/*      */ import java.awt.Color;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.PixelGrabber;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.HashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Image
/*      */   extends Rectangle
/*      */   implements Indentable, Spaceable, IAccessibleElement, IAlternateDescription
/*      */ {
/*      */   public static final int DEFAULT = 0;
/*      */   public static final int RIGHT = 2;
/*      */   public static final int LEFT = 0;
/*      */   public static final int MIDDLE = 1;
/*      */   public static final int TEXTWRAP = 4;
/*      */   public static final int UNDERLYING = 8;
/*      */   public static final int AX = 0;
/*      */   public static final int AY = 1;
/*      */   public static final int BX = 2;
/*      */   public static final int BY = 3;
/*      */   public static final int CX = 4;
/*      */   public static final int CY = 5;
/*      */   public static final int DX = 6;
/*      */   public static final int DY = 7;
/*      */   public static final int ORIGINAL_NONE = 0;
/*      */   public static final int ORIGINAL_JPEG = 1;
/*      */   public static final int ORIGINAL_PNG = 2;
/*      */   public static final int ORIGINAL_GIF = 3;
/*      */   public static final int ORIGINAL_BMP = 4;
/*      */   public static final int ORIGINAL_TIFF = 5;
/*      */   public static final int ORIGINAL_WMF = 6;
/*      */   public static final int ORIGINAL_PS = 7;
/*      */   public static final int ORIGINAL_JPEG2000 = 8;
/*      */   public static final int ORIGINAL_JBIG2 = 9;
/*      */   protected int type;
/*      */   protected URL url;
/*      */   protected byte[] rawData;
/*  182 */   protected int bpc = 1;
/*      */ 
/*      */   
/*  185 */   protected PdfTemplate[] template = new PdfTemplate[1];
/*      */ 
/*      */   
/*      */   protected int alignment;
/*      */ 
/*      */   
/*      */   protected String alt;
/*      */ 
/*      */   
/*  194 */   protected float absoluteX = Float.NaN;
/*      */ 
/*      */   
/*  197 */   protected float absoluteY = Float.NaN;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float plainWidth;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float plainHeight;
/*      */ 
/*      */ 
/*      */   
/*      */   protected float scaledWidth;
/*      */ 
/*      */   
/*      */   protected float scaledHeight;
/*      */ 
/*      */   
/*  215 */   protected int compressionLevel = -1;
/*      */ 
/*      */   
/*  218 */   protected Long mySerialId = getSerialId();
/*      */   
/*  220 */   protected PdfName role = PdfName.FIGURE;
/*  221 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*  222 */   private AccessibleElementId id = null; private PdfIndirectReference directReference; public static Image getInstance(URL url) throws BadElementException, MalformedURLException, IOException { return getInstance(url, false); }
/*      */   public static Image getInstance(URL url, boolean recoverFromImageError) throws BadElementException, MalformedURLException, IOException { InputStream is = null; RandomAccessSourceFactory randomAccessSourceFactory = new RandomAccessSourceFactory(); try { is = url.openStream(); int c1 = is.read(); int c2 = is.read(); int c3 = is.read(); int c4 = is.read(); int c5 = is.read(); int c6 = is.read(); int c7 = is.read(); int c8 = is.read(); is.close(); is = null; if (c1 == 71 && c2 == 73 && c3 == 70) { GifImage gif = new GifImage(url); Image img = gif.getImage(1); return img; }  if (c1 == 255 && c2 == 216) return new Jpeg(url);  if (c1 == 0 && c2 == 0 && c3 == 0 && c4 == 12) return new Jpeg2000(url);  if (c1 == 255 && c2 == 79 && c3 == 255 && c4 == 81) return new Jpeg2000(url);  if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) return PngImage.getImage(url);  if (c1 == 215 && c2 == 205) return new ImgWMF(url);  if (c1 == 66 && c2 == 77) return BmpImage.getImage(url);  if ((c1 == 77 && c2 == 77 && c3 == 0 && c4 == 42) || (c1 == 73 && c2 == 73 && c3 == 42 && c4 == 0)) { RandomAccessFileOrArray ra = null; try { if (url.getProtocol().equals("file")) { String file = url.getFile(); file = Utilities.unEscapeURL(file); ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createBestSource(file)); } else { ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(url)); }  Image img = TiffImage.getTiffImage(ra, 1); img.url = url; return img; } catch (RuntimeException e) { if (recoverFromImageError) { Image img = TiffImage.getTiffImage(ra, recoverFromImageError, 1); img.url = url; return img; }  throw e; } finally { if (ra != null) ra.close();  }  }  if (c1 == 151 && c2 == 74 && c3 == 66 && c4 == 50 && c5 == 13 && c6 == 10 && c7 == 26 && c8 == 10) { RandomAccessFileOrArray ra = null; try { if (url.getProtocol().equals("file")) { String file = url.getFile(); file = Utilities.unEscapeURL(file); ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createBestSource(file)); } else { ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(url)); }  Image img = JBIG2Image.getJbig2Image(ra, 1); img.url = url; return img; } finally { if (ra != null) ra.close();  }  }  throw new IOException(MessageLocalization.getComposedMessage("unknown.image.format", new Object[] { url.toString() })); } finally { if (is != null) is.close();  }  }
/*      */   public static Image getInstance(String filename) throws BadElementException, MalformedURLException, IOException { return getInstance(Utilities.toURL(filename)); }
/*      */   public static Image getInstance(String filename, boolean recoverFromImageError) throws IOException, BadElementException { return getInstance(Utilities.toURL(filename), recoverFromImageError); }
/*      */   public static Image getInstance(byte[] imgb) throws BadElementException, MalformedURLException, IOException { return getInstance(imgb, false); }
/*      */   public static Image getInstance(byte[] imgb, boolean recoverFromImageError) throws BadElementException, MalformedURLException, IOException { InputStream is = null; RandomAccessSourceFactory randomAccessSourceFactory = new RandomAccessSourceFactory(); try { is = new ByteArrayInputStream(imgb); int c1 = is.read(); int c2 = is.read(); int c3 = is.read(); int c4 = is.read(); is.close(); is = null; if (c1 == 71 && c2 == 73 && c3 == 70) { GifImage gif = new GifImage(imgb); return gif.getImage(1); }  if (c1 == 255 && c2 == 216) return new Jpeg(imgb);  if (c1 == 0 && c2 == 0 && c3 == 0 && c4 == 12) return new Jpeg2000(imgb);  if (c1 == 255 && c2 == 79 && c3 == 255 && c4 == 81) return new Jpeg2000(imgb);  if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1] && c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) return PngImage.getImage(imgb);  if (c1 == 215 && c2 == 205) return new ImgWMF(imgb);  if (c1 == 66 && c2 == 77) return BmpImage.getImage(imgb);  if ((c1 == 77 && c2 == 77 && c3 == 0 && c4 == 42) || (c1 == 73 && c2 == 73 && c3 == 42 && c4 == 0)) { RandomAccessFileOrArray ra = null; try { ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(imgb)); Image img = TiffImage.getTiffImage(ra, 1); if (img.getOriginalData() == null) img.setOriginalData(imgb);  return img; } catch (RuntimeException e) { if (recoverFromImageError) { Image img = TiffImage.getTiffImage(ra, recoverFromImageError, 1); if (img.getOriginalData() == null) img.setOriginalData(imgb);  return img; }  throw e; } finally { if (ra != null)
/*      */             ra.close();  }  }  if (c1 == 151 && c2 == 74 && c3 == 66 && c4 == 50) { is = new ByteArrayInputStream(imgb); is.skip(4L); int c5 = is.read(); int c6 = is.read(); int c7 = is.read(); int c8 = is.read(); is.close(); if (c5 == 13 && c6 == 10 && c7 == 26 && c8 == 10) { RandomAccessFileOrArray ra = null; try { ra = new RandomAccessFileOrArray(randomAccessSourceFactory.createSource(imgb)); Image img = JBIG2Image.getJbig2Image(ra, 1); if (img.getOriginalData() == null)
/*      */               img.setOriginalData(imgb);  return img; } finally { if (ra != null)
/*      */               ra.close();  }  }  }  throw new IOException(MessageLocalization.getComposedMessage("the.byte.array.is.not.a.recognized.imageformat", new Object[0])); } finally { if (is != null)
/*      */         is.close();  }  }
/*      */   public static Image getInstance(int width, int height, int components, int bpc, byte[] data) throws BadElementException { return getInstance(width, height, components, bpc, data, (int[])null); }
/*      */   public static Image getInstance(int width, int height, byte[] data, byte[] globals) { return new ImgJBIG2(width, height, data, globals); }
/*  234 */   public Image(URL url) { super(0.0F, 0.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1271 */     this.indentationLeft = 0.0F;
/*      */ 
/*      */     
/* 1274 */     this.indentationRight = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1374 */     this.widthPercentage = 100.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1429 */     this.scaleToFitHeight = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1452 */     this.annotation = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1526 */     this.originalType = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1576 */     this.deflated = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1601 */     this.dpiX = 0;
/*      */ 
/*      */     
/* 1604 */     this.dpiY = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1640 */     this.XYRatio = 0.0F;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1664 */     this.colorspace = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1677 */     this.colortransform = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1688 */     this.invert = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1710 */     this.profile = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1741 */     this.additional = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1806 */     this.mask = false; this.url = url; this.alignment = 0; this.rotationRadians = 0.0F; } public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException { return getInstance(width, height, reverseBits, typeCCITT, parameters, data, (int[])null); } public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data, int[] transparency) throws BadElementException { if (transparency != null && transparency.length != 2) throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.2.with.ccitt.images", new Object[0]));  Image img = new ImgCCITT(width, height, reverseBits, typeCCITT, parameters, data); img.transparency = transparency; return img; } public static Image getInstance(int width, int height, int components, int bpc, byte[] data, int[] transparency) throws BadElementException { if (transparency != null && transparency.length != components * 2) throw new BadElementException(MessageLocalization.getComposedMessage("transparency.length.must.be.equal.to.componentes.2", new Object[0]));  if (components == 1 && bpc == 1) { byte[] g4 = CCITTG4Encoder.compress(data, width, height); return getInstance(width, height, false, 256, 1, g4, transparency); }  Image img = new ImgRaw(width, height, components, bpc, data); img.transparency = transparency; return img; } public static Image getInstance(PdfTemplate template) throws BadElementException { return new ImgTemplate(template); } public PdfIndirectReference getDirectReference() { return this.directReference; } public void setDirectReference(PdfIndirectReference directReference) { this.directReference = directReference; } public static Image getInstance(PRIndirectReference ref) throws BadElementException { PdfDictionary dic = (PdfDictionary)PdfReader.getPdfObjectRelease((PdfObject)ref); int width = ((PdfNumber)PdfReader.getPdfObjectRelease(dic.get(PdfName.WIDTH))).intValue(); int height = ((PdfNumber)PdfReader.getPdfObjectRelease(dic.get(PdfName.HEIGHT))).intValue(); Image imask = null; PdfObject obj = dic.get(PdfName.SMASK); if (obj != null && obj.isIndirect()) { imask = getInstance((PRIndirectReference)obj); } else { obj = dic.get(PdfName.MASK); if (obj != null && obj.isIndirect()) { PdfObject obj2 = PdfReader.getPdfObjectRelease(obj); if (obj2 instanceof PdfDictionary) imask = getInstance((PRIndirectReference)obj);  }  }  Image img = new ImgRaw(width, height, 1, 1, null); img.imageMask = imask; img.directReference = (PdfIndirectReference)ref; return img; } public static Image getInstance(Image image) { if (image == null) return null;  try { Class<? extends Image> cs = (Class)image.getClass(); Constructor<? extends Image> constructor = cs.getDeclaredConstructor(new Class[] { Image.class }); return constructor.newInstance(new Object[] { image }); } catch (Exception e) { throw new ExceptionConverter(e); }  } public int type() { return this.type; } public boolean isNestable() { return true; } public boolean isJpeg() { return (this.type == 32); } public boolean isImgRaw() { return (this.type == 34); } public boolean isImgTemplate() { return (this.type == 35); } public URL getUrl() { return this.url; } public void setUrl(URL url) { this.url = url; } public byte[] getRawData() { return this.rawData; } public int getBpc() { return this.bpc; } public PdfTemplate getTemplateData() { return this.template[0]; } public void setTemplateData(PdfTemplate template) { this.template[0] = template; } public int getAlignment() { return this.alignment; } public void setAlignment(int alignment) { this.alignment = alignment; } public String getAlt() { return this.alt; } public void setAlt(String alt) { this.alt = alt; setAccessibleAttribute(PdfName.ALT, (PdfObject)new PdfString(alt)); } public void setAbsolutePosition(float absoluteX, float absoluteY) { this.absoluteX = absoluteX; this.absoluteY = absoluteY; } public boolean hasAbsoluteX() { return !Float.isNaN(this.absoluteX); } public float getAbsoluteX() { return this.absoluteX; } public boolean hasAbsoluteY() { return !Float.isNaN(this.absoluteY); } public float getAbsoluteY() { return this.absoluteY; } public float getScaledWidth() { return this.scaledWidth; } public float getScaledHeight() { return this.scaledHeight; } public float getPlainWidth() { return this.plainWidth; } public float getPlainHeight() { return this.plainHeight; } public void scaleAbsolute(Rectangle rectangle) { scaleAbsolute(rectangle.getWidth(), rectangle.getHeight()); } public void scaleAbsolute(float newWidth, float newHeight) { this.plainWidth = newWidth; this.plainHeight = newHeight; float[] matrix = matrix(); this.scaledWidth = matrix[6] - matrix[4]; this.scaledHeight = matrix[7] - matrix[5]; setWidthPercentage(0.0F); } public void scaleAbsoluteWidth(float newWidth) { this.plainWidth = newWidth; float[] matrix = matrix(); this.scaledWidth = matrix[6] - matrix[4]; this.scaledHeight = matrix[7] - matrix[5]; setWidthPercentage(0.0F); } public void scaleAbsoluteHeight(float newHeight) { this.plainHeight = newHeight; float[] matrix = matrix(); this.scaledWidth = matrix[6] - matrix[4]; this.scaledHeight = matrix[7] - matrix[5]; setWidthPercentage(0.0F); } protected Image(Image image) { super(image); this.indentationLeft = 0.0F; this.indentationRight = 0.0F; this.widthPercentage = 100.0F; this.scaleToFitHeight = true; this.annotation = null; this.originalType = 0; this.deflated = false; this.dpiX = 0; this.dpiY = 0; this.XYRatio = 0.0F; this.colorspace = -1; this.colortransform = 1; this.invert = false; this.profile = null; this.additional = null; this.mask = false; this.type = image.type; this.url = image.url; this.rawData = image.rawData; this.bpc = image.bpc; this.template = image.template; this.alignment = image.alignment; this.alt = image.alt; this.absoluteX = image.absoluteX; this.absoluteY = image.absoluteY; this.plainWidth = image.plainWidth; this.plainHeight = image.plainHeight; this.scaledWidth = image.scaledWidth; this.scaledHeight = image.scaledHeight; this.mySerialId = image.mySerialId; this.directReference = image.directReference; this.rotationRadians = image.rotationRadians; this.initialRotation = image.initialRotation; this.indentationLeft = image.indentationLeft; this.indentationRight = image.indentationRight; this.spacingBefore = image.spacingBefore; this.spacingAfter = image.spacingAfter; this.widthPercentage = image.widthPercentage; this.scaleToFitLineWhenOverflow = image.scaleToFitLineWhenOverflow; this.scaleToFitHeight = image.scaleToFitHeight; this.annotation = image.annotation; this.layer = image.layer; this.interpolation = image.interpolation; this.originalType = image.originalType; this.originalData = image.originalData; this.deflated = image.deflated; this.dpiX = image.dpiX; this.dpiY = image.dpiY; this.XYRatio = image.XYRatio; this.colorspace = image.colorspace; this.invert = image.invert; this.profile = image.profile; this.additional = image.additional; this.mask = image.mask; this.imageMask = image.imageMask; this.smask = image.smask; this.transparency = image.transparency; this.role = image.role; if (image.accessibleAttributes != null)
/*      */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(image.accessibleAttributes);  setId(image.getId()); }
/*      */   public void scalePercent(float percent) { scalePercent(percent, percent); }
/*      */   public void scalePercent(float percentX, float percentY) { this.plainWidth = getWidth() * percentX / 100.0F; this.plainHeight = getHeight() * percentY / 100.0F; float[] matrix = matrix(); this.scaledWidth = matrix[6] - matrix[4]; this.scaledHeight = matrix[7] - matrix[5]; setWidthPercentage(0.0F); }
/*      */   public void scaleToFit(Rectangle rectangle) { scaleToFit(rectangle.getWidth(), rectangle.getHeight()); }
/*      */   public void scaleToFit(float fitWidth, float fitHeight) { scalePercent(100.0F); float percentX = fitWidth * 100.0F / getScaledWidth(); float percentY = fitHeight * 100.0F / getScaledHeight(); scalePercent((percentX < percentY) ? percentX : percentY); setWidthPercentage(0.0F); }
/*      */   public float[] matrix() { return matrix(1.0F); }
/*      */   public float[] matrix(float scalePercentage) { float[] matrix = new float[8]; float cosX = (float)Math.cos(this.rotationRadians); float sinX = (float)Math.sin(this.rotationRadians); matrix[0] = this.plainWidth * cosX * scalePercentage; matrix[1] = this.plainWidth * sinX * scalePercentage; matrix[2] = -this.plainHeight * sinX * scalePercentage; matrix[3] = this.plainHeight * cosX * scalePercentage; if (this.rotationRadians < 1.5707963267948966D) { matrix[4] = matrix[2]; matrix[5] = 0.0F; matrix[6] = matrix[0]; matrix[7] = matrix[1] + matrix[3]; }
/*      */     else if (this.rotationRadians < Math.PI)
/*      */     { matrix[4] = matrix[0] + matrix[2]; matrix[5] = matrix[3]; matrix[6] = 0.0F; matrix[7] = matrix[1]; }
/*      */     else if (this.rotationRadians < 4.71238898038469D)
/*      */     { matrix[4] = matrix[0]; matrix[5] = matrix[1] + matrix[3]; matrix[6] = matrix[2]; matrix[7] = 0.0F; }
/*      */     else
/*      */     { matrix[4] = 0.0F; matrix[5] = matrix[1]; matrix[6] = matrix[0] + matrix[2]; matrix[7] = matrix[3]; }
/* 1820 */      return matrix; } static long serialId = 0L; protected float rotationRadians; private float initialRotation; protected float indentationLeft; protected float indentationRight; protected float spacingBefore; protected float spacingAfter; protected float paddingTop; private float widthPercentage; protected boolean scaleToFitLineWhenOverflow; protected boolean scaleToFitHeight; protected Annotation annotation; protected PdfOCG layer; protected boolean interpolation; protected int originalType; protected byte[] originalData; protected boolean deflated; protected int dpiX; protected int dpiY; private float XYRatio; protected int colorspace; protected int colortransform; protected boolean invert; public boolean isMask() { return this.mask; } protected ICC_Profile profile; private PdfDictionary additional; protected boolean mask; protected Image imageMask; private boolean smask; protected int[] transparency; protected static synchronized Long getSerialId() { serialId++; return Long.valueOf(serialId); } public Long getMySerialId() { return this.mySerialId; } public float getImageRotation() { double d = 6.283185307179586D; float rot = (float)((this.rotationRadians - this.initialRotation) % d); if (rot < 0.0F) rot = (float)(rot + d);  return rot; } public void setRotation(float r) { double d = 6.283185307179586D; this.rotationRadians = (float)((r + this.initialRotation) % d); if (this.rotationRadians < 0.0F) this.rotationRadians = (float)(this.rotationRadians + d);  float[] matrix = matrix(); this.scaledWidth = matrix[6] - matrix[4]; this.scaledHeight = matrix[7] - matrix[5]; } public void setRotationDegrees(float deg) { double d = Math.PI; setRotation(deg / 180.0F * (float)d); } public float getInitialRotation() { return this.initialRotation; } public void setInitialRotation(float initialRotation) { float old_rot = this.rotationRadians - this.initialRotation; this.initialRotation = initialRotation; setRotation(old_rot); } public float getIndentationLeft() { return this.indentationLeft; } public void setIndentationLeft(float f) { this.indentationLeft = f; } public float getIndentationRight() { return this.indentationRight; } public void setIndentationRight(float f) { this.indentationRight = f; } public float getSpacingBefore() { return this.spacingBefore; } public void setSpacingBefore(float spacing) { this.spacingBefore = spacing; } public float getSpacingAfter() { return this.spacingAfter; } public void setSpacingAfter(float spacing) { this.spacingAfter = spacing; } public float getPaddingTop() { return this.paddingTop; } public void setPaddingTop(float paddingTop) { this.paddingTop = paddingTop; } public float getWidthPercentage() { return this.widthPercentage; } public void setWidthPercentage(float widthPercentage) { this.widthPercentage = widthPercentage; } public boolean isScaleToFitLineWhenOverflow() { return this.scaleToFitLineWhenOverflow; } public void setScaleToFitLineWhenOverflow(boolean scaleToFitLineWhenOverflow) { this.scaleToFitLineWhenOverflow = scaleToFitLineWhenOverflow; } public boolean isScaleToFitHeight() { return this.scaleToFitHeight; } public void setScaleToFitHeight(boolean scaleToFitHeight) { this.scaleToFitHeight = scaleToFitHeight; } public void setAnnotation(Annotation annotation) { this.annotation = annotation; } public Annotation getAnnotation() { return this.annotation; } public PdfOCG getLayer() { return this.layer; } public void setLayer(PdfOCG layer) { this.layer = layer; } public boolean isInterpolation() { return this.interpolation; } public void setInterpolation(boolean interpolation) { this.interpolation = interpolation; } public int getOriginalType() { return this.originalType; } public void setOriginalType(int originalType) { this.originalType = originalType; } public byte[] getOriginalData() { return this.originalData; } public void setOriginalData(byte[] originalData) { this.originalData = originalData; } public boolean isDeflated() { return this.deflated; } public void setDeflated(boolean deflated) { this.deflated = deflated; } public int getDpiX() { return this.dpiX; } public int getDpiY() { return this.dpiY; } public void setDpi(int dpiX, int dpiY) { this.dpiX = dpiX; this.dpiY = dpiY; } public float getXYRatio() { return this.XYRatio; } public void setXYRatio(float XYRatio) { this.XYRatio = XYRatio; } public int getColorspace() { return this.colorspace; } public void setColorTransform(int c) { this.colortransform = c; } public int getColorTransform() { return this.colortransform; }
/*      */   public boolean isInverted() { return this.invert; }
/*      */   public void setInverted(boolean invert) { this.invert = invert; }
/*      */   public void tagICC(ICC_Profile profile) { this.profile = profile; }
/*      */   public boolean hasICCProfile() { return (this.profile != null); }
/*      */   public ICC_Profile getICCProfile() { return this.profile; }
/*      */   public PdfDictionary getAdditional() { return this.additional; }
/*      */   public void setAdditional(PdfDictionary additional) { this.additional = additional; }
/*      */   public void simplifyColorspace() { PdfArray pdfArray1; if (this.additional == null) return;  PdfArray value = this.additional.getAsArray(PdfName.COLORSPACE); if (value == null) return;  PdfObject cs = simplifyColorspace(value); if (cs.isName()) { PdfObject newValue = cs; } else { pdfArray1 = value; PdfName first = value.getAsName(0); if (PdfName.INDEXED.equals(first) && value.size() >= 2) { PdfArray second = value.getAsArray(1); if (second != null) value.set(1, simplifyColorspace(second));  }  }  this.additional.put(PdfName.COLORSPACE, (PdfObject)pdfArray1); }
/*      */   private PdfObject simplifyColorspace(PdfArray obj) { if (obj == null) return (PdfObject)obj;  PdfName first = obj.getAsName(0); if (PdfName.CALGRAY.equals(first)) return (PdfObject)PdfName.DEVICEGRAY;  if (PdfName.CALRGB.equals(first)) return (PdfObject)PdfName.DEVICERGB;  return (PdfObject)obj; }
/* 1830 */   public void makeMask() throws DocumentException { if (!isMaskCandidate())
/* 1831 */       throw new DocumentException(MessageLocalization.getComposedMessage("this.image.can.not.be.an.image.mask", new Object[0])); 
/* 1832 */     this.mask = true; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMaskCandidate() {
/* 1842 */     if (this.type == 34 && 
/* 1843 */       this.bpc > 255) {
/* 1844 */       return true;
/*      */     }
/* 1846 */     return (this.colorspace == 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image getImageMask() {
/* 1855 */     return this.imageMask;
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
/*      */   public void setImageMask(Image mask) throws DocumentException {
/* 1867 */     if (this.mask)
/* 1868 */       throw new DocumentException(MessageLocalization.getComposedMessage("an.image.mask.cannot.contain.another.image.mask", new Object[0])); 
/* 1869 */     if (!mask.mask)
/* 1870 */       throw new DocumentException(MessageLocalization.getComposedMessage("the.image.mask.is.not.a.mask.did.you.do.makemask", new Object[0])); 
/* 1871 */     this.imageMask = mask;
/* 1872 */     this.smask = (mask.bpc > 1 && mask.bpc <= 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSmask() {
/* 1882 */     return this.smask;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSmask(boolean smask) {
/* 1892 */     this.smask = smask;
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
/*      */   public int[] getTransparency() {
/* 1905 */     return this.transparency;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTransparency(int[] transparency) {
/* 1915 */     this.transparency = transparency;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCompressionLevel() {
/* 1925 */     return this.compressionLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCompressionLevel(int compressionLevel) {
/* 1934 */     if (compressionLevel < 0 || compressionLevel > 9) {
/* 1935 */       this.compressionLevel = -1;
/*      */     } else {
/* 1937 */       this.compressionLevel = compressionLevel;
/*      */     } 
/*      */   }
/*      */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 1941 */     if (this.accessibleAttributes != null) {
/* 1942 */       return this.accessibleAttributes.get(key);
/*      */     }
/* 1944 */     return null;
/*      */   }
/*      */   
/*      */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 1948 */     if (this.accessibleAttributes == null)
/* 1949 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 1950 */     this.accessibleAttributes.put(key, value);
/*      */   }
/*      */   
/*      */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 1954 */     return this.accessibleAttributes;
/*      */   }
/*      */   
/*      */   public PdfName getRole() {
/* 1958 */     return this.role;
/*      */   }
/*      */   
/*      */   public void setRole(PdfName role) {
/* 1962 */     this.role = role;
/*      */   }
/*      */   
/*      */   public AccessibleElementId getId() {
/* 1966 */     if (this.id == null)
/* 1967 */       this.id = new AccessibleElementId(); 
/* 1968 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(AccessibleElementId id) {
/* 1972 */     this.id = id;
/*      */   }
/*      */   
/*      */   public boolean isInline() {
/* 1976 */     return true;
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
/*      */   public static Image getInstance(java.awt.Image image, Color color, boolean forceBW) throws BadElementException, IOException {
/* 2000 */     if (image instanceof BufferedImage) {
/* 2001 */       BufferedImage bi = (BufferedImage)image;
/* 2002 */       if (bi.getType() == 12 && bi
/* 2003 */         .getColorModel().getPixelSize() == 1) {
/* 2004 */         forceBW = true;
/*      */       }
/*      */     } 
/*      */     
/* 2008 */     PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
/*      */     
/*      */     try {
/* 2011 */       pg.grabPixels();
/* 2012 */     } catch (InterruptedException e) {
/* 2013 */       throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.interrupted.waiting.for.pixels", new Object[0]));
/*      */     } 
/* 2015 */     if ((pg.getStatus() & 0x80) != 0) {
/* 2016 */       throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.fetch.aborted.or.errored", new Object[0]));
/*      */     }
/* 2018 */     int w = pg.getWidth();
/* 2019 */     int h = pg.getHeight();
/* 2020 */     int[] pixels = (int[])pg.getPixels();
/* 2021 */     if (forceBW) {
/* 2022 */       int byteWidth = w / 8 + (((w & 0x7) != 0) ? 1 : 0);
/* 2023 */       byte[] arrayOfByte = new byte[byteWidth * h];
/*      */       
/* 2025 */       int i = 0;
/* 2026 */       int j = h * w;
/* 2027 */       int transColor = 1;
/* 2028 */       if (color != null)
/*      */       {
/* 2030 */         transColor = (color.getRed() + color.getGreen() + color.getBlue() < 384) ? 0 : 1;
/*      */       }
/* 2032 */       int[] arrayOfInt = null;
/* 2033 */       int cbyte = 128;
/* 2034 */       int wMarker = 0;
/* 2035 */       int currByte = 0;
/* 2036 */       if (color != null) {
/* 2037 */         for (int k = 0; k < j; k++) {
/* 2038 */           int alpha = pixels[k] >> 24 & 0xFF;
/* 2039 */           if (alpha < 250) {
/* 2040 */             if (transColor == 1) {
/* 2041 */               currByte |= cbyte;
/*      */             }
/* 2043 */           } else if ((pixels[k] & 0x888) != 0) {
/* 2044 */             currByte |= cbyte;
/*      */           } 
/* 2046 */           cbyte >>= 1;
/* 2047 */           if (cbyte == 0 || wMarker + 1 >= w) {
/* 2048 */             arrayOfByte[i++] = (byte)currByte;
/* 2049 */             cbyte = 128;
/* 2050 */             currByte = 0;
/*      */           } 
/* 2052 */           wMarker++;
/* 2053 */           if (wMarker >= w)
/* 2054 */             wMarker = 0; 
/*      */         } 
/*      */       } else {
/* 2057 */         for (int k = 0; k < j; k++) {
/* 2058 */           if (arrayOfInt == null) {
/* 2059 */             int alpha = pixels[k] >> 24 & 0xFF;
/* 2060 */             if (alpha == 0) {
/* 2061 */               arrayOfInt = new int[2];
/*      */               
/* 2063 */               arrayOfInt[1] = ((pixels[k] & 0x888) != 0) ? 255 : 0; arrayOfInt[0] = ((pixels[k] & 0x888) != 0) ? 255 : 0;
/*      */             } 
/*      */           } 
/* 2066 */           if ((pixels[k] & 0x888) != 0)
/* 2067 */             currByte |= cbyte; 
/* 2068 */           cbyte >>= 1;
/* 2069 */           if (cbyte == 0 || wMarker + 1 >= w) {
/* 2070 */             arrayOfByte[i++] = (byte)currByte;
/* 2071 */             cbyte = 128;
/* 2072 */             currByte = 0;
/*      */           } 
/* 2074 */           wMarker++;
/* 2075 */           if (wMarker >= w)
/* 2076 */             wMarker = 0; 
/*      */         } 
/*      */       } 
/* 2079 */       return getInstance(w, h, 1, 1, arrayOfByte, arrayOfInt);
/*      */     } 
/* 2081 */     byte[] pixelsByte = new byte[w * h * 3];
/* 2082 */     byte[] smask = null;
/*      */     
/* 2084 */     int index = 0;
/* 2085 */     int size = h * w;
/* 2086 */     int red = 255;
/* 2087 */     int green = 255;
/* 2088 */     int blue = 255;
/* 2089 */     if (color != null) {
/* 2090 */       red = color.getRed();
/* 2091 */       green = color.getGreen();
/* 2092 */       blue = color.getBlue();
/*      */     } 
/* 2094 */     int[] transparency = null;
/* 2095 */     if (color != null) {
/* 2096 */       for (int j = 0; j < size; j++) {
/* 2097 */         int alpha = pixels[j] >> 24 & 0xFF;
/* 2098 */         if (alpha < 250) {
/* 2099 */           pixelsByte[index++] = (byte)red;
/* 2100 */           pixelsByte[index++] = (byte)green;
/* 2101 */           pixelsByte[index++] = (byte)blue;
/*      */         } else {
/* 2103 */           pixelsByte[index++] = (byte)(pixels[j] >> 16 & 0xFF);
/* 2104 */           pixelsByte[index++] = (byte)(pixels[j] >> 8 & 0xFF);
/* 2105 */           pixelsByte[index++] = (byte)(pixels[j] & 0xFF);
/*      */         } 
/*      */       } 
/*      */     } else {
/* 2109 */       int transparentPixel = 0;
/* 2110 */       smask = new byte[w * h];
/* 2111 */       boolean shades = false;
/* 2112 */       for (int j = 0; j < size; j++) {
/* 2113 */         byte alpha = smask[j] = (byte)(pixels[j] >> 24 & 0xFF);
/*      */         
/* 2115 */         if (!shades) {
/* 2116 */           if (alpha != 0 && alpha != -1) {
/* 2117 */             shades = true;
/* 2118 */           } else if (transparency == null) {
/* 2119 */             if (alpha == 0) {
/* 2120 */               transparentPixel = pixels[j] & 0xFFFFFF;
/* 2121 */               transparency = new int[6];
/* 2122 */               transparency[1] = transparentPixel >> 16 & 0xFF; transparency[0] = transparentPixel >> 16 & 0xFF;
/* 2123 */               transparency[3] = transparentPixel >> 8 & 0xFF; transparency[2] = transparentPixel >> 8 & 0xFF;
/* 2124 */               transparency[5] = transparentPixel & 0xFF; transparency[4] = transparentPixel & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2129 */               for (int prevPixel = 0; prevPixel < j; prevPixel++) {
/* 2130 */                 if ((pixels[prevPixel] & 0xFFFFFF) == transparentPixel) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 2136 */                   shades = true;
/*      */                   break;
/*      */                 } 
/*      */               } 
/*      */             } 
/* 2141 */           } else if ((pixels[j] & 0xFFFFFF) != transparentPixel && alpha == 0) {
/* 2142 */             shades = true;
/* 2143 */           } else if ((pixels[j] & 0xFFFFFF) == transparentPixel && alpha != 0) {
/* 2144 */             shades = true;
/*      */           } 
/*      */         }
/* 2147 */         pixelsByte[index++] = (byte)(pixels[j] >> 16 & 0xFF);
/* 2148 */         pixelsByte[index++] = (byte)(pixels[j] >> 8 & 0xFF);
/* 2149 */         pixelsByte[index++] = (byte)(pixels[j] & 0xFF);
/*      */       } 
/* 2151 */       if (shades) {
/* 2152 */         transparency = null;
/*      */       } else {
/* 2154 */         smask = null;
/*      */       } 
/* 2156 */     }  Image img = getInstance(w, h, 3, 8, pixelsByte, transparency);
/* 2157 */     if (smask != null) {
/* 2158 */       Image sm = getInstance(w, h, 1, 8, smask);
/*      */       try {
/* 2160 */         sm.makeMask();
/* 2161 */         img.setImageMask(sm);
/* 2162 */       } catch (DocumentException de) {
/* 2163 */         throw new ExceptionConverter(de);
/*      */       } 
/*      */     } 
/* 2166 */     return img;
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
/*      */   public static Image getInstance(java.awt.Image image, Color color) throws BadElementException, IOException {
/* 2186 */     return getInstance(image, color, false);
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
/*      */   public static Image getInstance(PdfWriter writer, java.awt.Image awtImage, float quality) throws BadElementException, IOException {
/* 2205 */     return getInstance(new PdfContentByte(writer), awtImage, quality);
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
/*      */   public static Image getInstance(PdfContentByte cb, java.awt.Image awtImage, float quality) throws BadElementException, IOException {
/* 2224 */     PixelGrabber pg = new PixelGrabber(awtImage, 0, 0, -1, -1, true);
/*      */     
/*      */     try {
/* 2227 */       pg.grabPixels();
/* 2228 */     } catch (InterruptedException e) {
/* 2229 */       throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.interrupted.waiting.for.pixels", new Object[0]));
/*      */     } 
/* 2231 */     if ((pg.getStatus() & 0x80) != 0) {
/* 2232 */       throw new IOException(MessageLocalization.getComposedMessage("java.awt.image.fetch.aborted.or.errored", new Object[0]));
/*      */     }
/* 2234 */     int w = pg.getWidth();
/* 2235 */     int h = pg.getHeight();
/* 2236 */     PdfTemplate tp = cb.createTemplate(w, h);
/* 2237 */     PdfGraphics2D g2d = new PdfGraphics2D((PdfContentByte)tp, w, h, null, false, true, quality);
/* 2238 */     g2d.drawImage(awtImage, 0, 0, null);
/* 2239 */     g2d.dispose();
/* 2240 */     return getInstance(tp);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Image.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */