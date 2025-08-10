/*      */ package com.itextpdf.text.pdf.codec;
/*      */ 
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Base64
/*      */ {
/*      */   public static final int NO_OPTIONS = 0;
/*      */   public static final int ENCODE = 1;
/*      */   public static final int DECODE = 0;
/*      */   public static final int GZIP = 2;
/*      */   public static final int DONT_BREAK_LINES = 8;
/*      */   public static final int URL_SAFE = 16;
/*      */   public static final int ORDERED = 32;
/*      */   private static final int MAX_LINE_LENGTH = 76;
/*      */   private static final byte EQUALS_SIGN = 61;
/*      */   private static final byte NEW_LINE = 10;
/*      */   private static final String PREFERRED_ENCODING = "UTF-8";
/*      */   private static final byte WHITE_SPACE_ENC = -5;
/*      */   private static final byte EQUALS_SIGN_ENC = -1;
/*  191 */   private static final byte[] _STANDARD_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   private static final byte[] _STANDARD_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  253 */   private static final byte[] _URL_SAFE_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  270 */   private static final byte[] _URL_SAFE_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  317 */   private static final byte[] _ORDERED_ALPHABET = new byte[] { 45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  336 */   private static final byte[] _ORDERED_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final byte[] getAlphabet(int options) {
/*  387 */     if ((options & 0x10) == 16) return _URL_SAFE_ALPHABET; 
/*  388 */     if ((options & 0x20) == 32) return _ORDERED_ALPHABET; 
/*  389 */     return _STANDARD_ALPHABET;
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
/*      */   private static final byte[] getDecodabet(int options) {
/*  402 */     if ((options & 0x10) == 16) return _URL_SAFE_DECODABET; 
/*  403 */     if ((options & 0x20) == 32) return _ORDERED_DECODABET; 
/*  404 */     return _STANDARD_DECODABET;
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
/*      */   private static final void usage(String msg) {
/*  445 */     System.err.println(msg);
/*  446 */     System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
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
/*      */   private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
/*  469 */     encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
/*  470 */     return b4;
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
/*      */   private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
/*  500 */     byte[] ALPHABET = getAlphabet(options);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  513 */     int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);
/*      */ 
/*      */ 
/*      */     
/*  517 */     switch (numSigBytes) {
/*      */       case 3:
/*  519 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  520 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  521 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  522 */         destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
/*  523 */         return destination;
/*      */       
/*      */       case 2:
/*  526 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  527 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  528 */         destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
/*  529 */         destination[destOffset + 3] = 61;
/*  530 */         return destination;
/*      */       
/*      */       case 1:
/*  533 */         destination[destOffset] = ALPHABET[inBuff >>> 18];
/*  534 */         destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
/*  535 */         destination[destOffset + 2] = 61;
/*  536 */         destination[destOffset + 3] = 61;
/*  537 */         return destination;
/*      */     } 
/*      */     
/*  540 */     return destination;
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
/*      */   public static String encodeObject(Serializable serializableObject) {
/*  558 */     return encodeObject(serializableObject, 0);
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
/*      */   public static String encodeObject(Serializable serializableObject, int options) {
/*  588 */     ByteArrayOutputStream baos = null;
/*  589 */     java.io.OutputStream b64os = null;
/*  590 */     ObjectOutputStream oos = null;
/*  591 */     GZIPOutputStream gzos = null;
/*      */ 
/*      */     
/*  594 */     int gzip = options & 0x2;
/*  595 */     int dontBreakLines = options & 0x8;
/*      */ 
/*      */     
/*      */     try {
/*  599 */       baos = new ByteArrayOutputStream();
/*  600 */       b64os = new OutputStream(baos, 0x1 | options);
/*      */ 
/*      */       
/*  603 */       if (gzip == 2) {
/*  604 */         gzos = new GZIPOutputStream(b64os);
/*  605 */         oos = new ObjectOutputStream(gzos);
/*      */       } else {
/*      */         
/*  608 */         oos = new ObjectOutputStream(b64os);
/*      */       } 
/*  610 */       oos.writeObject(serializableObject);
/*      */     }
/*  612 */     catch (IOException e) {
/*  613 */       e.printStackTrace();
/*  614 */       return null;
/*      */     } finally {
/*      */       
/*  617 */       try { oos.close(); } catch (Exception exception) {} 
/*  618 */       try { gzos.close(); } catch (Exception exception) {} 
/*  619 */       try { b64os.close(); } catch (Exception exception) {} 
/*  620 */       try { baos.close(); } catch (Exception exception) {}
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  625 */       return new String(baos.toByteArray(), "UTF-8");
/*      */     }
/*  627 */     catch (UnsupportedEncodingException uue) {
/*  628 */       return new String(baos.toByteArray());
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
/*      */   public static String encodeBytes(byte[] source) {
/*  643 */     return encodeBytes(source, 0, source.length, 0);
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
/*      */   public static String encodeBytes(byte[] source, int options) {
/*  669 */     return encodeBytes(source, 0, source.length, options);
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
/*      */   public static String encodeBytes(byte[] source, int off, int len) {
/*  683 */     return encodeBytes(source, off, len, 0);
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
/*      */   public static String encodeBytes(byte[] source, int off, int len, int options) {
/*  713 */     int dontBreakLines = options & 0x8;
/*  714 */     int gzip = options & 0x2;
/*      */ 
/*      */     
/*  717 */     if (gzip == 2) {
/*  718 */       ByteArrayOutputStream baos = null;
/*  719 */       GZIPOutputStream gzos = null;
/*  720 */       OutputStream b64os = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  725 */         baos = new ByteArrayOutputStream();
/*  726 */         b64os = new OutputStream(baos, 0x1 | options);
/*  727 */         gzos = new GZIPOutputStream(b64os);
/*      */         
/*  729 */         gzos.write(source, off, len);
/*  730 */         gzos.close();
/*      */       }
/*  732 */       catch (IOException iOException) {
/*  733 */         iOException.printStackTrace();
/*  734 */         return null;
/*      */       } finally {
/*      */         
/*  737 */         try { gzos.close(); } catch (Exception exception) {} 
/*  738 */         try { b64os.close(); } catch (Exception exception) {} 
/*  739 */         try { baos.close(); } catch (Exception exception) {}
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  744 */         return new String(baos.toByteArray(), "UTF-8");
/*      */       }
/*  746 */       catch (UnsupportedEncodingException uue) {
/*  747 */         return new String(baos.toByteArray());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  754 */     boolean breakLines = (dontBreakLines == 0);
/*      */     
/*  756 */     int len43 = len * 4 / 3;
/*  757 */     byte[] outBuff = new byte[len43 + ((len % 3 > 0) ? 4 : 0) + (breakLines ? (len43 / 76) : 0)];
/*      */ 
/*      */     
/*  760 */     int d = 0;
/*  761 */     int e = 0;
/*  762 */     int len2 = len - 2;
/*  763 */     int lineLength = 0;
/*  764 */     for (; d < len2; d += 3, e += 4) {
/*  765 */       encode3to4(source, d + off, 3, outBuff, e, options);
/*      */       
/*  767 */       lineLength += 4;
/*  768 */       if (breakLines && lineLength == 76) {
/*  769 */         outBuff[e + 4] = 10;
/*  770 */         e++;
/*  771 */         lineLength = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  775 */     if (d < len) {
/*  776 */       encode3to4(source, d + off, len - d, outBuff, e, options);
/*  777 */       e += 4;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  783 */       return new String(outBuff, 0, e, "UTF-8");
/*      */     }
/*  785 */     catch (UnsupportedEncodingException uue) {
/*  786 */       return new String(outBuff, 0, e);
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
/*      */   private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
/*  826 */     byte[] DECODABET = getDecodabet(options);
/*      */ 
/*      */     
/*  829 */     if (source[srcOffset + 2] == 61) {
/*      */ 
/*      */ 
/*      */       
/*  833 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12;
/*      */ 
/*      */       
/*  836 */       destination[destOffset] = (byte)(outBuff >>> 16);
/*  837 */       return 1;
/*      */     } 
/*      */ 
/*      */     
/*  841 */     if (source[srcOffset + 3] == 61) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  846 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6;
/*      */ 
/*      */ 
/*      */       
/*  850 */       destination[destOffset] = (byte)(outBuff >>> 16);
/*  851 */       destination[destOffset + 1] = (byte)(outBuff >>> 8);
/*  852 */       return 2;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  863 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | DECODABET[source[srcOffset + 3]] & 0xFF;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  869 */       destination[destOffset] = (byte)(outBuff >> 16);
/*  870 */       destination[destOffset + 1] = (byte)(outBuff >> 8);
/*  871 */       destination[destOffset + 2] = (byte)outBuff;
/*      */       
/*  873 */       return 3;
/*  874 */     } catch (Exception e) {
/*  875 */       System.out.println("" + source[srcOffset] + ": " + DECODABET[source[srcOffset]]);
/*  876 */       System.out.println("" + source[srcOffset + 1] + ": " + DECODABET[source[srcOffset + 1]]);
/*  877 */       System.out.println("" + source[srcOffset + 2] + ": " + DECODABET[source[srcOffset + 2]]);
/*  878 */       System.out.println("" + source[srcOffset + 3] + ": " + DECODABET[source[srcOffset + 3]]);
/*  879 */       return -1;
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
/*      */   public static byte[] decode(byte[] source, int off, int len, int options) {
/*  899 */     byte[] DECODABET = getDecodabet(options);
/*      */     
/*  901 */     int len34 = len * 3 / 4;
/*  902 */     byte[] outBuff = new byte[len34];
/*  903 */     int outBuffPosn = 0;
/*      */     
/*  905 */     byte[] b4 = new byte[4];
/*  906 */     int b4Posn = 0;
/*  907 */     int i = 0;
/*  908 */     byte sbiCrop = 0;
/*  909 */     byte sbiDecode = 0;
/*  910 */     for (i = off; i < off + len; i++) {
/*  911 */       sbiCrop = (byte)(source[i] & Byte.MAX_VALUE);
/*  912 */       sbiDecode = DECODABET[sbiCrop];
/*      */       
/*  914 */       if (sbiDecode >= -5) {
/*      */         
/*  916 */         if (sbiDecode >= -1) {
/*  917 */           b4[b4Posn++] = sbiCrop;
/*  918 */           if (b4Posn > 3) {
/*  919 */             outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
/*  920 */             b4Posn = 0;
/*      */ 
/*      */             
/*  923 */             if (sbiCrop == 61) {
/*      */               break;
/*      */             }
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/*  931 */         System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
/*  932 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/*  936 */     byte[] out = new byte[outBuffPosn];
/*  937 */     System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
/*  938 */     return out;
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
/*      */   public static byte[] decode(String s) {
/*  953 */     return decode(s, 0);
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
/*      */   public static byte[] decode(String s, int options) {
/*      */     try {
/*  969 */       bytes = s.getBytes("UTF-8");
/*      */     }
/*  971 */     catch (UnsupportedEncodingException uee) {
/*  972 */       bytes = s.getBytes();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  977 */     byte[] bytes = decode(bytes, 0, bytes.length, options);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  982 */     if (bytes != null && bytes.length >= 4) {
/*      */       
/*  984 */       int head = bytes[0] & 0xFF | bytes[1] << 8 & 0xFF00;
/*  985 */       if (35615 == head) {
/*  986 */         ByteArrayInputStream bais = null;
/*  987 */         GZIPInputStream gzis = null;
/*  988 */         ByteArrayOutputStream baos = null;
/*  989 */         byte[] buffer = new byte[2048];
/*  990 */         int length = 0;
/*      */         
/*      */         try {
/*  993 */           baos = new ByteArrayOutputStream();
/*  994 */           bais = new ByteArrayInputStream(bytes);
/*  995 */           gzis = new GZIPInputStream(bais);
/*      */           
/*  997 */           while ((length = gzis.read(buffer)) >= 0) {
/*  998 */             baos.write(buffer, 0, length);
/*      */           }
/*      */ 
/*      */           
/* 1002 */           bytes = baos.toByteArray();
/*      */         
/*      */         }
/* 1005 */         catch (IOException iOException) {
/*      */         
/*      */         } finally {
/*      */           
/* 1009 */           try { baos.close(); } catch (Exception exception) {} 
/* 1010 */           try { gzis.close(); } catch (Exception exception) {} 
/* 1011 */           try { bais.close(); } catch (Exception exception) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1017 */     return bytes;
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
/*      */   public static Object decodeToObject(String encodedObject) {
/* 1033 */     byte[] objBytes = decode(encodedObject);
/*      */     
/* 1035 */     ByteArrayInputStream bais = null;
/* 1036 */     ObjectInputStream ois = null;
/* 1037 */     Object obj = null;
/*      */     
/*      */     try {
/* 1040 */       bais = new ByteArrayInputStream(objBytes);
/* 1041 */       ois = new ObjectInputStream(bais);
/*      */       
/* 1043 */       obj = ois.readObject();
/*      */     }
/* 1045 */     catch (IOException e) {
/* 1046 */       e.printStackTrace();
/*      */     }
/* 1048 */     catch (ClassNotFoundException e) {
/* 1049 */       e.printStackTrace();
/*      */     } finally {
/*      */       
/* 1052 */       try { bais.close(); } catch (Exception exception) {} 
/* 1053 */       try { ois.close(); } catch (Exception exception) {}
/*      */     } 
/*      */     
/* 1056 */     return obj;
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
/*      */   public static boolean encodeToFile(byte[] dataToEncode, String filename) {
/* 1071 */     boolean success = false;
/* 1072 */     OutputStream bos = null;
/*      */     try {
/* 1074 */       bos = new OutputStream(new FileOutputStream(filename), 1);
/*      */       
/* 1076 */       bos.write(dataToEncode);
/* 1077 */       success = true;
/*      */     }
/* 1079 */     catch (IOException e) {
/*      */       
/* 1081 */       success = false;
/*      */     } finally {
/*      */       
/* 1084 */       try { bos.close(); } catch (Exception exception) {}
/*      */     } 
/*      */     
/* 1087 */     return success;
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
/*      */   public static boolean decodeToFile(String dataToDecode, String filename) {
/* 1101 */     boolean success = false;
/* 1102 */     OutputStream bos = null;
/*      */     try {
/* 1104 */       bos = new OutputStream(new FileOutputStream(filename), 0);
/*      */       
/* 1106 */       bos.write(dataToDecode.getBytes("UTF-8"));
/* 1107 */       success = true;
/*      */     }
/* 1109 */     catch (IOException e) {
/* 1110 */       success = false;
/*      */     } finally {
/*      */       
/* 1113 */       try { bos.close(); } catch (Exception exception) {}
/*      */     } 
/*      */     
/* 1116 */     return success;
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
/*      */   public static byte[] decodeFromFile(String filename) {
/* 1132 */     byte[] decodedData = null;
/* 1133 */     InputStream bis = null;
/*      */     
/*      */     try {
/* 1136 */       File file = new File(filename);
/* 1137 */       byte[] buffer = null;
/* 1138 */       int length = 0;
/* 1139 */       int numBytes = 0;
/*      */ 
/*      */       
/* 1142 */       if (file.length() > 2147483647L) {
/* 1143 */         System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");
/* 1144 */         return null;
/*      */       } 
/* 1146 */       buffer = new byte[(int)file.length()];
/*      */ 
/*      */       
/* 1149 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1154 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1155 */         length += numBytes;
/*      */       }
/*      */       
/* 1158 */       decodedData = new byte[length];
/* 1159 */       System.arraycopy(buffer, 0, decodedData, 0, length);
/*      */     
/*      */     }
/* 1162 */     catch (IOException e) {
/* 1163 */       System.err.println("Error decoding from file " + filename);
/*      */     } finally {
/*      */       
/* 1166 */       if (null != bis) {
/* 1167 */         try { bis.close(); } catch (Exception exception) {}
/*      */       }
/*      */     } 
/*      */     
/* 1171 */     return decodedData;
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
/*      */   public static String encodeFromFile(String filename) {
/* 1186 */     String encodedData = null;
/* 1187 */     InputStream bis = null;
/*      */     
/*      */     try {
/* 1190 */       File file = new File(filename);
/* 1191 */       byte[] buffer = new byte[Math.max((int)(file.length() * 1.4D), 40)];
/* 1192 */       int length = 0;
/* 1193 */       int numBytes = 0;
/*      */ 
/*      */       
/* 1196 */       bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1201 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1202 */         length += numBytes;
/*      */       }
/*      */       
/* 1205 */       encodedData = new String(buffer, 0, length, "UTF-8");
/*      */     
/*      */     }
/* 1208 */     catch (IOException e) {
/* 1209 */       System.err.println("Error encoding from file " + filename);
/*      */     } finally {
/*      */       
/* 1212 */       try { bis.close(); } catch (Exception exception) {}
/*      */     } 
/*      */     
/* 1215 */     return encodedData;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void encodeFileToFile(String infile, String outfile) {
/* 1226 */     String encoded = encodeFromFile(infile);
/* 1227 */     java.io.OutputStream out = null;
/*      */     try {
/* 1229 */       out = new BufferedOutputStream(new FileOutputStream(outfile));
/*      */       
/* 1231 */       out.write(encoded.getBytes("US-ASCII"));
/*      */     }
/* 1233 */     catch (IOException ex) {
/* 1234 */       ex.printStackTrace();
/*      */     } finally {
/*      */       
/* 1237 */       try { out.close(); } catch (Exception exception) {}
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
/*      */   public static void decodeFileToFile(String infile, String outfile) {
/* 1250 */     byte[] decoded = decodeFromFile(infile);
/* 1251 */     java.io.OutputStream out = null;
/*      */     try {
/* 1253 */       out = new BufferedOutputStream(new FileOutputStream(outfile));
/*      */       
/* 1255 */       out.write(decoded);
/*      */     }
/* 1257 */     catch (IOException ex) {
/* 1258 */       ex.printStackTrace();
/*      */     } finally {
/*      */       
/* 1261 */       try { out.close(); } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InputStream
/*      */     extends FilterInputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int numSigBytes;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */     
/*      */     private int options;
/*      */     
/*      */     private byte[] alphabet;
/*      */     
/*      */     private byte[] decodabet;
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in) {
/* 1298 */       this(in, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InputStream(java.io.InputStream in, int options) {
/* 1324 */       super(in);
/* 1325 */       this.breakLines = ((options & 0x8) != 8);
/* 1326 */       this.encode = ((options & 0x1) == 1);
/* 1327 */       this.bufferLength = this.encode ? 4 : 3;
/* 1328 */       this.buffer = new byte[this.bufferLength];
/* 1329 */       this.position = -1;
/* 1330 */       this.lineLength = 0;
/* 1331 */       this.options = options;
/* 1332 */       this.alphabet = Base64.getAlphabet(options);
/* 1333 */       this.decodabet = Base64.getDecodabet(options);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/* 1345 */       if (this.position < 0) {
/* 1346 */         if (this.encode) {
/* 1347 */           byte[] b3 = new byte[3];
/* 1348 */           int numBinaryBytes = 0;
/* 1349 */           for (int i = 0; i < 3; i++) {
/*      */             try {
/* 1351 */               int b = this.in.read();
/*      */ 
/*      */               
/* 1354 */               if (b >= 0) {
/* 1355 */                 b3[i] = (byte)b;
/* 1356 */                 numBinaryBytes++;
/*      */               }
/*      */             
/*      */             }
/* 1360 */             catch (IOException e) {
/*      */               
/* 1362 */               if (i == 0) {
/* 1363 */                 throw e;
/*      */               }
/*      */             } 
/*      */           } 
/*      */           
/* 1368 */           if (numBinaryBytes > 0) {
/* 1369 */             Base64.encode3to4(b3, 0, numBinaryBytes, this.buffer, 0, this.options);
/* 1370 */             this.position = 0;
/* 1371 */             this.numSigBytes = 4;
/*      */           } else {
/*      */             
/* 1374 */             return -1;
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1380 */           byte[] b4 = new byte[4];
/* 1381 */           int i = 0;
/* 1382 */           for (i = 0; i < 4; i++) {
/*      */             
/* 1384 */             int b = 0; do {
/* 1385 */               b = this.in.read();
/* 1386 */             } while (b >= 0 && this.decodabet[b & 0x7F] <= -5);
/*      */             
/* 1388 */             if (b < 0) {
/*      */               break;
/*      */             }
/* 1391 */             b4[i] = (byte)b;
/*      */           } 
/*      */           
/* 1394 */           if (i == 4) {
/* 1395 */             this.numSigBytes = Base64.decode4to3(b4, 0, this.buffer, 0, this.options);
/* 1396 */             this.position = 0;
/*      */           } else {
/* 1398 */             if (i == 0) {
/* 1399 */               return -1;
/*      */             }
/*      */ 
/*      */             
/* 1403 */             throw new IOException(MessageLocalization.getComposedMessage("improperly.padded.base64.input", new Object[0]));
/*      */           } 
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1410 */       if (this.position >= 0) {
/*      */         
/* 1412 */         if (this.position >= this.numSigBytes) {
/* 1413 */           return -1;
/*      */         }
/* 1415 */         if (this.encode && this.breakLines && this.lineLength >= 76) {
/* 1416 */           this.lineLength = 0;
/* 1417 */           return 10;
/*      */         } 
/*      */         
/* 1420 */         this.lineLength++;
/*      */ 
/*      */ 
/*      */         
/* 1424 */         int b = this.buffer[this.position++];
/*      */         
/* 1426 */         if (this.position >= this.bufferLength) {
/* 1427 */           this.position = -1;
/*      */         }
/* 1429 */         return b & 0xFF;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1437 */       throw new IOException(MessageLocalization.getComposedMessage("error.in.base64.code.reading.stream", new Object[0]));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read(byte[] dest, int off, int len) throws IOException {
/*      */       int i;
/* 1457 */       for (i = 0; i < len; i++) {
/* 1458 */         int b = read();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1463 */         if (b >= 0)
/* 1464 */         { dest[off + i] = (byte)b; }
/* 1465 */         else { if (i == 0)
/* 1466 */             return -1; 
/*      */           break; }
/*      */       
/*      */       } 
/* 1470 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class OutputStream
/*      */     extends FilterOutputStream
/*      */   {
/*      */     private boolean encode;
/*      */ 
/*      */     
/*      */     private int position;
/*      */ 
/*      */     
/*      */     private byte[] buffer;
/*      */ 
/*      */     
/*      */     private int bufferLength;
/*      */ 
/*      */     
/*      */     private int lineLength;
/*      */ 
/*      */     
/*      */     private boolean breakLines;
/*      */ 
/*      */     
/*      */     private byte[] b4;
/*      */ 
/*      */     
/*      */     private boolean suspendEncoding;
/*      */ 
/*      */     
/*      */     private int options;
/*      */ 
/*      */     
/*      */     private byte[] alphabet;
/*      */     
/*      */     private byte[] decodabet;
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out) {
/* 1512 */       this(out, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public OutputStream(java.io.OutputStream out, int options) {
/* 1537 */       super(out);
/* 1538 */       this.breakLines = ((options & 0x8) != 8);
/* 1539 */       this.encode = ((options & 0x1) == 1);
/* 1540 */       this.bufferLength = this.encode ? 3 : 4;
/* 1541 */       this.buffer = new byte[this.bufferLength];
/* 1542 */       this.position = 0;
/* 1543 */       this.lineLength = 0;
/* 1544 */       this.suspendEncoding = false;
/* 1545 */       this.b4 = new byte[4];
/* 1546 */       this.options = options;
/* 1547 */       this.alphabet = Base64.getAlphabet(options);
/* 1548 */       this.decodabet = Base64.getDecodabet(options);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(int theByte) throws IOException {
/* 1566 */       if (this.suspendEncoding) {
/* 1567 */         this.out.write(theByte);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1572 */       if (this.encode) {
/* 1573 */         this.buffer[this.position++] = (byte)theByte;
/* 1574 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1576 */           this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
/*      */           
/* 1578 */           this.lineLength += 4;
/* 1579 */           if (this.breakLines && this.lineLength >= 76) {
/* 1580 */             this.out.write(10);
/* 1581 */             this.lineLength = 0;
/*      */           } 
/*      */           
/* 1584 */           this.position = 0;
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 1591 */       else if (this.decodabet[theByte & 0x7F] > -5) {
/* 1592 */         this.buffer[this.position++] = (byte)theByte;
/* 1593 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1595 */           int len = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);
/* 1596 */           this.out.write(this.b4, 0, len);
/*      */           
/* 1598 */           this.position = 0;
/*      */         }
/*      */       
/* 1601 */       } else if (this.decodabet[theByte & 0x7F] != -5) {
/* 1602 */         throw new IOException(MessageLocalization.getComposedMessage("invalid.character.in.base64.data", new Object[0]));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(byte[] theBytes, int off, int len) throws IOException {
/* 1620 */       if (this.suspendEncoding) {
/* 1621 */         this.out.write(theBytes, off, len);
/*      */         
/*      */         return;
/*      */       } 
/* 1625 */       for (int i = 0; i < len; i++) {
/* 1626 */         write(theBytes[off + i]);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void flushBase64() throws IOException {
/* 1638 */       if (this.position > 0) {
/* 1639 */         if (this.encode) {
/* 1640 */           this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
/* 1641 */           this.position = 0;
/*      */         } else {
/*      */           
/* 1644 */           throw new IOException(MessageLocalization.getComposedMessage("base64.input.not.properly.padded", new Object[0]));
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 1658 */       flushBase64();
/*      */ 
/*      */ 
/*      */       
/* 1662 */       super.close();
/*      */       
/* 1664 */       this.buffer = null;
/* 1665 */       this.out = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void suspendEncoding() throws IOException {
/* 1678 */       flushBase64();
/* 1679 */       this.suspendEncoding = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void resumeEncoding() {
/* 1691 */       this.suspendEncoding = false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/Base64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */