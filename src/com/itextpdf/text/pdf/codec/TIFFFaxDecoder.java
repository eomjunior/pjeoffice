/*      */ package com.itextpdf.text.pdf.codec;
/*      */ 
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.exceptions.InvalidImageException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TIFFFaxDecoder
/*      */ {
/*      */   private int bitPointer;
/*      */   private int bytePointer;
/*      */   private byte[] data;
/*      */   private int w;
/*      */   private int h;
/*      */   private long fillOrder;
/*   63 */   private int changingElemSize = 0;
/*      */   
/*      */   private int[] prevChangingElems;
/*      */   
/*      */   private int[] currChangingElems;
/*   68 */   private int lastChangingElement = 0;
/*      */   
/*   70 */   private int compression = 2;
/*      */ 
/*      */   
/*   73 */   private int uncompressedMode = 0;
/*   74 */   private int fillBits = 0;
/*      */   
/*      */   private int oneD;
/*      */   
/*      */   private boolean recoverFromImageError;
/*      */   
/*   80 */   static int[] table1 = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   92 */   static int[] table2 = new int[] { 0, 128, 192, 224, 240, 248, 252, 254, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   static byte[] flipTable = new byte[] { 0, Byte.MIN_VALUE, 64, -64, 32, -96, 96, -32, 16, -112, 80, -48, 48, -80, 112, -16, 8, -120, 72, -56, 40, -88, 104, -24, 24, -104, 88, -40, 56, -72, 120, -8, 4, -124, 68, -60, 36, -92, 100, -28, 20, -108, 84, -44, 52, -76, 116, -12, 12, -116, 76, -52, 44, -84, 108, -20, 28, -100, 92, -36, 60, -68, 124, -4, 2, -126, 66, -62, 34, -94, 98, -30, 18, -110, 82, -46, 50, -78, 114, -14, 10, -118, 74, -54, 42, -86, 106, -22, 26, -102, 90, -38, 58, -70, 122, -6, 6, -122, 70, -58, 38, -90, 102, -26, 22, -106, 86, -42, 54, -74, 118, -10, 14, -114, 78, -50, 46, -82, 110, -18, 30, -98, 94, -34, 62, -66, 126, -2, 1, -127, 65, -63, 33, -95, 97, -31, 17, -111, 81, -47, 49, -79, 113, -15, 9, -119, 73, -55, 41, -87, 105, -23, 25, -103, 89, -39, 57, -71, 121, -7, 5, -123, 69, -59, 37, -91, 101, -27, 21, -107, 85, -43, 53, -75, 117, -11, 13, -115, 77, -51, 45, -83, 109, -19, 29, -99, 93, -35, 61, -67, 125, -3, 3, -125, 67, -61, 35, -93, 99, -29, 19, -109, 83, -45, 51, -77, 115, -13, 11, -117, 75, -53, 43, -85, 107, -21, 27, -101, 91, -37, 59, -69, 123, -5, 7, -121, 71, -57, 39, -89, 103, -25, 23, -105, 87, -41, 55, -73, 119, -9, 15, -113, 79, -49, 47, -81, 111, -17, 31, -97, 95, -33, 63, -65, Byte.MAX_VALUE, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   static short[] white = new short[] { 6430, 6400, 6400, 6400, 3225, 3225, 3225, 3225, 944, 944, 944, 944, 976, 976, 976, 976, 1456, 1456, 1456, 1456, 1488, 1488, 1488, 1488, 718, 718, 718, 718, 718, 718, 718, 718, 750, 750, 750, 750, 750, 750, 750, 750, 1520, 1520, 1520, 1520, 1552, 1552, 1552, 1552, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 654, 654, 654, 654, 654, 654, 654, 654, 1072, 1072, 1072, 1072, 1104, 1104, 1104, 1104, 1136, 1136, 1136, 1136, 1168, 1168, 1168, 1168, 1200, 1200, 1200, 1200, 1232, 1232, 1232, 1232, 622, 622, 622, 622, 622, 622, 622, 622, 1008, 1008, 1008, 1008, 1040, 1040, 1040, 1040, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 1712, 1712, 1712, 1712, 1744, 1744, 1744, 1744, 846, 846, 846, 846, 846, 846, 846, 846, 1264, 1264, 1264, 1264, 1296, 1296, 1296, 1296, 1328, 1328, 1328, 1328, 1360, 1360, 1360, 1360, 1392, 1392, 1392, 1392, 1424, 1424, 1424, 1424, 686, 686, 686, 686, 686, 686, 686, 686, 910, 910, 910, 910, 910, 910, 910, 910, 1968, 1968, 1968, 1968, 2000, 2000, 2000, 2000, 2032, 2032, 2032, 2032, 16, 16, 16, 16, 10257, 10257, 10257, 10257, 12305, 12305, 12305, 12305, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 878, 878, 878, 878, 878, 878, 878, 878, 1904, 1904, 1904, 1904, 1936, 1936, 1936, 1936, -18413, -18413, -16365, -16365, -14317, -14317, -10221, -10221, 590, 590, 590, 590, 590, 590, 590, 590, 782, 782, 782, 782, 782, 782, 782, 782, 1584, 1584, 1584, 1584, 1616, 1616, 1616, 1616, 1648, 1648, 1648, 1648, 1680, 1680, 1680, 1680, 814, 814, 814, 814, 814, 814, 814, 814, 1776, 1776, 1776, 1776, 1808, 1808, 1808, 1808, 1840, 1840, 1840, 1840, 1872, 1872, 1872, 1872, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, 14353, 14353, 14353, 14353, 16401, 16401, 16401, 16401, 22547, 22547, 24595, 24595, 20497, 20497, 20497, 20497, 18449, 18449, 18449, 18449, 26643, 26643, 28691, 28691, 30739, 30739, -32749, -32749, -30701, -30701, -28653, -28653, -26605, -26605, -24557, -24557, -22509, -22509, -20461, -20461, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  401 */   static short[] additionalMakeup = new short[] { 28679, 28679, 31752, -32759, -31735, -30711, -29687, -28663, 29703, 29703, 30727, 30727, -27639, -26615, -25591, -24567 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  409 */   static short[] initBlack = new short[] { 3226, 6412, 200, 168, 38, 38, 134, 134, 100, 100, 100, 100, 68, 68, 68, 68 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  417 */   static short[] twoBitBlack = new short[] { 292, 260, 226, 226 };
/*      */ 
/*      */   
/*  420 */   static short[] black = new short[] { 62, 62, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 588, 588, 588, 588, 588, 588, 588, 588, 1680, 1680, 20499, 22547, 24595, 26643, 1776, 1776, 1808, 1808, -24557, -22509, -20461, -18413, 1904, 1904, 1936, 1936, -16365, -14317, 782, 782, 782, 782, 814, 814, 814, 814, -12269, -10221, 10257, 10257, 12305, 12305, 14353, 14353, 16403, 18451, 1712, 1712, 1744, 1744, 28691, 30739, -32749, -30701, -28653, -26605, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 750, 750, 750, 750, 1616, 1616, 1648, 1648, 1424, 1424, 1456, 1456, 1488, 1488, 1520, 1520, 1840, 1840, 1872, 1872, 1968, 1968, 8209, 8209, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 1552, 1552, 1584, 1584, 2000, 2000, 2032, 2032, 976, 976, 1008, 1008, 1040, 1040, 1072, 1072, 1296, 1296, 1328, 1328, 718, 718, 718, 718, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 4113, 4113, 6161, 6161, 848, 848, 880, 880, 912, 912, 944, 944, 622, 622, 622, 622, 654, 654, 654, 654, 1104, 1104, 1136, 1136, 1168, 1168, 1200, 1200, 1232, 1232, 1264, 1264, 686, 686, 686, 686, 1360, 1360, 1392, 1392, 12, 12, 12, 12, 12, 12, 12, 12, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  551 */   static byte[] twoDCodes = new byte[] { 80, 88, 23, 71, 30, 30, 62, 62, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TIFFFaxDecoder(long fillOrder, int w, int h) {
/*  592 */     this.fillOrder = fillOrder;
/*  593 */     this.w = w;
/*  594 */     this.h = h;
/*      */     
/*  596 */     this.bitPointer = 0;
/*  597 */     this.bytePointer = 0;
/*  598 */     this.prevChangingElems = new int[2 * w];
/*  599 */     this.currChangingElems = new int[2 * w];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void reverseBits(byte[] b) {
/*  609 */     for (int k = 0; k < b.length; k++) {
/*  610 */       b[k] = flipTable[b[k] & 0xFF];
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void decode1D(byte[] buffer, byte[] compData, int startX, int height) {
/*  616 */     this.data = compData;
/*      */     
/*  618 */     int lineOffset = 0;
/*  619 */     int scanlineStride = (this.w + 7) / 8;
/*      */     
/*  621 */     this.bitPointer = 0;
/*  622 */     this.bytePointer = 0;
/*      */     
/*  624 */     for (int i = 0; i < height; i++) {
/*  625 */       decodeNextScanline(buffer, lineOffset, startX);
/*  626 */       lineOffset += scanlineStride;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void decodeNextScanline(byte[] buffer, int lineOffset, int bitOffset) {
/*  631 */     int bits = 0, code = 0, isT = 0;
/*      */     
/*  633 */     boolean isWhite = true;
/*      */ 
/*      */     
/*  636 */     this.changingElemSize = 0;
/*      */ 
/*      */     
/*  639 */     while (bitOffset < this.w) {
/*  640 */       while (isWhite) {
/*      */         
/*  642 */         int current = nextNBits(10);
/*  643 */         int entry = white[current];
/*      */ 
/*      */         
/*  646 */         isT = entry & 0x1;
/*  647 */         bits = entry >>> 1 & 0xF;
/*      */         
/*  649 */         if (bits == 12) {
/*      */           
/*  651 */           int twoBits = nextLesserThan8Bits(2);
/*      */           
/*  653 */           current = current << 2 & 0xC | twoBits;
/*  654 */           entry = additionalMakeup[current];
/*  655 */           bits = entry >>> 1 & 0x7;
/*  656 */           code = entry >>> 4 & 0xFFF;
/*  657 */           bitOffset += code;
/*      */           
/*  659 */           updatePointer(4 - bits); continue;
/*  660 */         }  if (bits == 0)
/*  661 */           throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.encountered", new Object[0])); 
/*  662 */         if (bits == 15) {
/*  663 */           throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.white.run", new Object[0]));
/*      */         }
/*      */         
/*  666 */         code = entry >>> 5 & 0x7FF;
/*  667 */         bitOffset += code;
/*      */         
/*  669 */         updatePointer(10 - bits);
/*  670 */         if (isT == 0) {
/*  671 */           isWhite = false;
/*  672 */           this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  679 */       if (bitOffset == this.w) {
/*  680 */         if (this.compression == 2) {
/*  681 */           advancePointer();
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*  686 */       while (!isWhite) {
/*      */         
/*  688 */         int current = nextLesserThan8Bits(4);
/*  689 */         int entry = initBlack[current];
/*      */ 
/*      */         
/*  692 */         isT = entry & 0x1;
/*  693 */         bits = entry >>> 1 & 0xF;
/*  694 */         code = entry >>> 5 & 0x7FF;
/*      */         
/*  696 */         if (code == 100) {
/*  697 */           current = nextNBits(9);
/*  698 */           entry = black[current];
/*      */ 
/*      */           
/*  701 */           isT = entry & 0x1;
/*  702 */           bits = entry >>> 1 & 0xF;
/*  703 */           code = entry >>> 5 & 0x7FF;
/*      */           
/*  705 */           if (bits == 12) {
/*      */             
/*  707 */             updatePointer(5);
/*  708 */             current = nextLesserThan8Bits(4);
/*  709 */             entry = additionalMakeup[current];
/*  710 */             bits = entry >>> 1 & 0x7;
/*  711 */             code = entry >>> 4 & 0xFFF;
/*      */             
/*  713 */             setToBlack(buffer, lineOffset, bitOffset, code);
/*  714 */             bitOffset += code;
/*      */             
/*  716 */             updatePointer(4 - bits); continue;
/*  717 */           }  if (bits == 15)
/*      */           {
/*  719 */             throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.black.run", new Object[0]));
/*      */           }
/*  721 */           setToBlack(buffer, lineOffset, bitOffset, code);
/*  722 */           bitOffset += code;
/*      */           
/*  724 */           updatePointer(9 - bits);
/*  725 */           if (isT == 0) {
/*  726 */             isWhite = true;
/*  727 */             this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */           }  continue;
/*      */         } 
/*  730 */         if (code == 200) {
/*      */           
/*  732 */           current = nextLesserThan8Bits(2);
/*  733 */           entry = twoBitBlack[current];
/*  734 */           code = entry >>> 5 & 0x7FF;
/*  735 */           bits = entry >>> 1 & 0xF;
/*      */           
/*  737 */           setToBlack(buffer, lineOffset, bitOffset, code);
/*  738 */           bitOffset += code;
/*      */           
/*  740 */           updatePointer(2 - bits);
/*  741 */           isWhite = true;
/*  742 */           this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */           continue;
/*      */         } 
/*  745 */         setToBlack(buffer, lineOffset, bitOffset, code);
/*  746 */         bitOffset += code;
/*      */         
/*  748 */         updatePointer(4 - bits);
/*  749 */         isWhite = true;
/*  750 */         this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  755 */       if (bitOffset == this.w) {
/*  756 */         if (this.compression == 2) {
/*  757 */           advancePointer();
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  763 */     this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void decode2D(byte[] buffer, byte[] compData, int startX, int height, long tiffT4Options) {
/*  769 */     this.data = compData;
/*  770 */     this.compression = 3;
/*      */     
/*  772 */     this.bitPointer = 0;
/*  773 */     this.bytePointer = 0;
/*      */     
/*  775 */     int scanlineStride = (this.w + 7) / 8;
/*      */ 
/*      */     
/*  778 */     int[] b = new int[2];
/*      */ 
/*      */     
/*  781 */     int currIndex = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  790 */     this.oneD = (int)(tiffT4Options & 0x1L);
/*  791 */     this.uncompressedMode = (int)((tiffT4Options & 0x2L) >> 1L);
/*  792 */     this.fillBits = (int)((tiffT4Options & 0x4L) >> 2L);
/*      */ 
/*      */     
/*  795 */     if (readEOL(true) != 1) {
/*  796 */       throw new RuntimeException(MessageLocalization.getComposedMessage("first.scanline.must.be.1d.encoded", new Object[0]));
/*      */     }
/*      */     
/*  799 */     int lineOffset = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  804 */     decodeNextScanline(buffer, lineOffset, startX);
/*  805 */     lineOffset += scanlineStride;
/*      */     
/*  807 */     for (int lines = 1; lines < height; lines++) {
/*      */ 
/*      */ 
/*      */       
/*  811 */       if (readEOL(false) == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  816 */         int[] temp = this.prevChangingElems;
/*  817 */         this.prevChangingElems = this.currChangingElems;
/*  818 */         this.currChangingElems = temp;
/*  819 */         currIndex = 0;
/*      */ 
/*      */         
/*  822 */         int a0 = -1;
/*  823 */         boolean isWhite = true;
/*  824 */         int bitOffset = startX;
/*      */         
/*  826 */         this.lastChangingElement = 0;
/*      */         
/*  828 */         while (bitOffset < this.w) {
/*      */           
/*  830 */           getNextChangingElement(a0, isWhite, b);
/*      */           
/*  832 */           int b1 = b[0];
/*  833 */           int b2 = b[1];
/*      */ 
/*      */           
/*  836 */           int entry = nextLesserThan8Bits(7);
/*      */ 
/*      */           
/*  839 */           entry = twoDCodes[entry] & 0xFF;
/*      */ 
/*      */           
/*  842 */           int code = (entry & 0x78) >>> 3;
/*  843 */           int bits = entry & 0x7;
/*      */           
/*  845 */           if (code == 0) {
/*  846 */             if (!isWhite) {
/*  847 */               setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
/*      */             }
/*      */             
/*  850 */             bitOffset = a0 = b2;
/*      */ 
/*      */             
/*  853 */             updatePointer(7 - bits); continue;
/*  854 */           }  if (code == 1) {
/*      */             
/*  856 */             updatePointer(7 - bits);
/*      */ 
/*      */ 
/*      */             
/*  860 */             if (isWhite) {
/*  861 */               int number = decodeWhiteCodeWord();
/*  862 */               bitOffset += number;
/*  863 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */               
/*  865 */               number = decodeBlackCodeWord();
/*  866 */               setToBlack(buffer, lineOffset, bitOffset, number);
/*  867 */               bitOffset += number;
/*  868 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */             } else {
/*  870 */               int number = decodeBlackCodeWord();
/*  871 */               setToBlack(buffer, lineOffset, bitOffset, number);
/*  872 */               bitOffset += number;
/*  873 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */               
/*  875 */               number = decodeWhiteCodeWord();
/*  876 */               bitOffset += number;
/*  877 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */             } 
/*      */             
/*  880 */             a0 = bitOffset; continue;
/*  881 */           }  if (code <= 8) {
/*      */             
/*  883 */             int a1 = b1 + code - 5;
/*      */             
/*  885 */             this.currChangingElems[currIndex++] = a1;
/*      */ 
/*      */ 
/*      */             
/*  889 */             if (!isWhite) {
/*  890 */               setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
/*      */             }
/*      */             
/*  893 */             bitOffset = a0 = a1;
/*  894 */             isWhite = !isWhite;
/*      */             
/*  896 */             updatePointer(7 - bits); continue;
/*      */           } 
/*  898 */           throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.code.encountered.while.decoding.2d.group.3.compressed.data", new Object[0]));
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  904 */         this.currChangingElems[currIndex++] = bitOffset;
/*  905 */         this.changingElemSize = currIndex;
/*      */       } else {
/*      */         
/*  908 */         decodeNextScanline(buffer, lineOffset, startX);
/*      */       } 
/*      */       
/*  911 */       lineOffset += scanlineStride;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void decodeT6(byte[] buffer, byte[] compData, int startX, int height, long tiffT6Options) {
/*  920 */     this.data = compData;
/*  921 */     this.compression = 4;
/*      */     
/*  923 */     this.bitPointer = 0;
/*  924 */     this.bytePointer = 0;
/*      */     
/*  926 */     int scanlineStride = (this.w + 7) / 8;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  935 */     int[] b = new int[2];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  940 */     this.uncompressedMode = (int)((tiffT6Options & 0x2L) >> 1L);
/*      */ 
/*      */     
/*  943 */     int[] cce = this.currChangingElems;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  948 */     this.changingElemSize = 0;
/*  949 */     cce[this.changingElemSize++] = this.w;
/*  950 */     cce[this.changingElemSize++] = this.w;
/*      */     
/*  952 */     int lineOffset = 0;
/*      */ 
/*      */     
/*  955 */     for (int lines = 0; lines < height; lines++) {
/*      */       
/*  957 */       int a0 = -1;
/*  958 */       boolean isWhite = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  963 */       int[] temp = this.prevChangingElems;
/*  964 */       this.prevChangingElems = this.currChangingElems;
/*  965 */       cce = this.currChangingElems = temp;
/*  966 */       int currIndex = 0;
/*      */ 
/*      */       
/*  969 */       int bitOffset = startX;
/*      */ 
/*      */       
/*  972 */       this.lastChangingElement = 0;
/*      */ 
/*      */ 
/*      */       
/*  976 */       while (bitOffset < this.w && this.bytePointer < this.data.length - 1) {
/*      */         
/*  978 */         getNextChangingElement(a0, isWhite, b);
/*  979 */         int b1 = b[0];
/*  980 */         int b2 = b[1];
/*      */ 
/*      */         
/*  983 */         int entry = nextLesserThan8Bits(7);
/*      */         
/*  985 */         entry = twoDCodes[entry] & 0xFF;
/*      */ 
/*      */         
/*  988 */         int code = (entry & 0x78) >>> 3;
/*  989 */         int bits = entry & 0x7;
/*      */         
/*  991 */         if (code == 0) {
/*      */           
/*  993 */           if (!isWhite) {
/*  994 */             setToBlack(buffer, lineOffset, bitOffset, b2 - bitOffset);
/*      */           }
/*      */           
/*  997 */           bitOffset = a0 = b2;
/*      */ 
/*      */           
/* 1000 */           updatePointer(7 - bits); continue;
/* 1001 */         }  if (code == 1) {
/*      */           
/* 1003 */           updatePointer(7 - bits);
/*      */ 
/*      */ 
/*      */           
/* 1007 */           if (isWhite) {
/*      */             
/* 1009 */             int number = decodeWhiteCodeWord();
/* 1010 */             bitOffset += number;
/* 1011 */             cce[currIndex++] = bitOffset;
/*      */             
/* 1013 */             number = decodeBlackCodeWord();
/* 1014 */             setToBlack(buffer, lineOffset, bitOffset, number);
/* 1015 */             bitOffset += number;
/* 1016 */             cce[currIndex++] = bitOffset;
/*      */           } else {
/*      */             
/* 1019 */             int number = decodeBlackCodeWord();
/* 1020 */             setToBlack(buffer, lineOffset, bitOffset, number);
/* 1021 */             bitOffset += number;
/* 1022 */             cce[currIndex++] = bitOffset;
/*      */             
/* 1024 */             number = decodeWhiteCodeWord();
/* 1025 */             bitOffset += number;
/* 1026 */             cce[currIndex++] = bitOffset;
/*      */           } 
/*      */           
/* 1029 */           a0 = bitOffset; continue;
/* 1030 */         }  if (code <= 8) {
/* 1031 */           int a1 = b1 + code - 5;
/* 1032 */           cce[currIndex++] = a1;
/*      */ 
/*      */ 
/*      */           
/* 1036 */           if (!isWhite) {
/* 1037 */             setToBlack(buffer, lineOffset, bitOffset, a1 - bitOffset);
/*      */           }
/*      */           
/* 1040 */           bitOffset = a0 = a1;
/* 1041 */           isWhite = !isWhite;
/*      */           
/* 1043 */           updatePointer(7 - bits); continue;
/* 1044 */         }  if (code == 11) {
/* 1045 */           if (nextLesserThan8Bits(3) != 7) {
/* 1046 */             throw new InvalidImageException(MessageLocalization.getComposedMessage("invalid.code.encountered.while.decoding.2d.group.4.compressed.data", new Object[0]));
/*      */           }
/*      */           
/* 1049 */           int zeros = 0;
/* 1050 */           boolean exit = false;
/*      */           
/* 1052 */           while (!exit) {
/* 1053 */             while (nextLesserThan8Bits(1) != 1) {
/* 1054 */               zeros++;
/*      */             }
/*      */             
/* 1057 */             if (zeros > 5) {
/*      */ 
/*      */ 
/*      */               
/* 1061 */               zeros -= 6;
/*      */               
/* 1063 */               if (!isWhite && zeros > 0) {
/* 1064 */                 cce[currIndex++] = bitOffset;
/*      */               }
/*      */ 
/*      */               
/* 1068 */               bitOffset += zeros;
/* 1069 */               if (zeros > 0)
/*      */               {
/* 1071 */                 isWhite = true;
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 1076 */               if (nextLesserThan8Bits(1) == 0) {
/* 1077 */                 if (!isWhite) {
/* 1078 */                   cce[currIndex++] = bitOffset;
/*      */                 }
/* 1080 */                 isWhite = true;
/*      */               } else {
/* 1082 */                 if (isWhite) {
/* 1083 */                   cce[currIndex++] = bitOffset;
/*      */                 }
/* 1085 */                 isWhite = false;
/*      */               } 
/*      */               
/* 1088 */               exit = true;
/*      */             } 
/*      */             
/* 1091 */             if (zeros == 5) {
/* 1092 */               if (!isWhite) {
/* 1093 */                 cce[currIndex++] = bitOffset;
/*      */               }
/* 1095 */               bitOffset += zeros;
/*      */ 
/*      */               
/* 1098 */               isWhite = true; continue;
/*      */             } 
/* 1100 */             bitOffset += zeros;
/*      */             
/* 1102 */             cce[currIndex++] = bitOffset;
/* 1103 */             setToBlack(buffer, lineOffset, bitOffset, 1);
/* 1104 */             bitOffset++;
/*      */ 
/*      */             
/* 1107 */             isWhite = false;
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */ 
/*      */         
/* 1116 */         bitOffset = this.w;
/* 1117 */         updatePointer(7 - bits);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1124 */       if (currIndex < cce.length) {
/* 1125 */         cce[currIndex++] = bitOffset;
/*      */       }
/*      */       
/* 1128 */       this.changingElemSize = currIndex;
/*      */       
/* 1130 */       lineOffset += scanlineStride;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void setToBlack(byte[] buffer, int lineOffset, int bitOffset, int numBits) {
/* 1137 */     int bitNum = 8 * lineOffset + bitOffset;
/* 1138 */     int lastBit = bitNum + numBits;
/*      */     
/* 1140 */     int byteNum = bitNum >> 3;
/*      */ 
/*      */     
/* 1143 */     int shift = bitNum & 0x7;
/* 1144 */     if (shift > 0) {
/* 1145 */       int maskVal = 1 << 7 - shift;
/* 1146 */       byte val = buffer[byteNum];
/* 1147 */       while (maskVal > 0 && bitNum < lastBit) {
/* 1148 */         val = (byte)(val | maskVal);
/* 1149 */         maskVal >>= 1;
/* 1150 */         bitNum++;
/*      */       } 
/* 1152 */       buffer[byteNum] = val;
/*      */     } 
/*      */ 
/*      */     
/* 1156 */     byteNum = bitNum >> 3;
/* 1157 */     while (bitNum < lastBit - 7) {
/* 1158 */       buffer[byteNum++] = -1;
/* 1159 */       bitNum += 8;
/*      */     } 
/*      */ 
/*      */     
/* 1163 */     while (bitNum < lastBit) {
/* 1164 */       byteNum = bitNum >> 3;
/* 1165 */       if (!this.recoverFromImageError || byteNum < buffer.length)
/*      */       {
/*      */         
/* 1168 */         buffer[byteNum] = (byte)(buffer[byteNum] | 1 << 7 - (bitNum & 0x7));
/*      */       }
/* 1170 */       bitNum++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int decodeWhiteCodeWord() {
/* 1176 */     int code = -1;
/* 1177 */     int runLength = 0;
/* 1178 */     boolean isWhite = true;
/*      */     
/* 1180 */     while (isWhite) {
/* 1181 */       int current = nextNBits(10);
/* 1182 */       int entry = white[current];
/*      */ 
/*      */       
/* 1185 */       int isT = entry & 0x1;
/* 1186 */       int bits = entry >>> 1 & 0xF;
/*      */       
/* 1188 */       if (bits == 12) {
/*      */         
/* 1190 */         int twoBits = nextLesserThan8Bits(2);
/*      */         
/* 1192 */         current = current << 2 & 0xC | twoBits;
/* 1193 */         entry = additionalMakeup[current];
/* 1194 */         bits = entry >>> 1 & 0x7;
/* 1195 */         code = entry >>> 4 & 0xFFF;
/* 1196 */         runLength += code;
/* 1197 */         updatePointer(4 - bits); continue;
/* 1198 */       }  if (bits == 0)
/* 1199 */         throw new InvalidImageException(MessageLocalization.getComposedMessage("invalid.code.encountered", new Object[0])); 
/* 1200 */       if (bits == 15) {
/* 1201 */         if (runLength == 0) {
/* 1202 */           isWhite = false; continue;
/*      */         } 
/* 1204 */         throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.white.run", new Object[0]));
/*      */       } 
/*      */ 
/*      */       
/* 1208 */       code = entry >>> 5 & 0x7FF;
/* 1209 */       runLength += code;
/* 1210 */       updatePointer(10 - bits);
/* 1211 */       if (isT == 0) {
/* 1212 */         isWhite = false;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1217 */     return runLength;
/*      */   }
/*      */ 
/*      */   
/*      */   private int decodeBlackCodeWord() {
/* 1222 */     int code = -1;
/* 1223 */     int runLength = 0;
/* 1224 */     boolean isWhite = false;
/*      */     
/* 1226 */     while (!isWhite) {
/* 1227 */       int current = nextLesserThan8Bits(4);
/* 1228 */       int entry = initBlack[current];
/*      */ 
/*      */       
/* 1231 */       int isT = entry & 0x1;
/* 1232 */       int bits = entry >>> 1 & 0xF;
/* 1233 */       code = entry >>> 5 & 0x7FF;
/*      */       
/* 1235 */       if (code == 100) {
/* 1236 */         current = nextNBits(9);
/* 1237 */         entry = black[current];
/*      */ 
/*      */         
/* 1240 */         isT = entry & 0x1;
/* 1241 */         bits = entry >>> 1 & 0xF;
/* 1242 */         code = entry >>> 5 & 0x7FF;
/*      */         
/* 1244 */         if (bits == 12) {
/*      */           
/* 1246 */           updatePointer(5);
/* 1247 */           current = nextLesserThan8Bits(4);
/* 1248 */           entry = additionalMakeup[current];
/* 1249 */           bits = entry >>> 1 & 0x7;
/* 1250 */           code = entry >>> 4 & 0xFFF;
/* 1251 */           runLength += code;
/*      */           
/* 1253 */           updatePointer(4 - bits); continue;
/* 1254 */         }  if (bits == 15)
/*      */         {
/* 1256 */           throw new RuntimeException(MessageLocalization.getComposedMessage("eol.code.word.encountered.in.black.run", new Object[0]));
/*      */         }
/* 1258 */         runLength += code;
/* 1259 */         updatePointer(9 - bits);
/* 1260 */         if (isT == 0)
/* 1261 */           isWhite = true; 
/*      */         continue;
/*      */       } 
/* 1264 */       if (code == 200) {
/*      */         
/* 1266 */         current = nextLesserThan8Bits(2);
/* 1267 */         entry = twoBitBlack[current];
/* 1268 */         code = entry >>> 5 & 0x7FF;
/* 1269 */         runLength += code;
/* 1270 */         bits = entry >>> 1 & 0xF;
/* 1271 */         updatePointer(2 - bits);
/* 1272 */         isWhite = true;
/*      */         continue;
/*      */       } 
/* 1275 */       runLength += code;
/* 1276 */       updatePointer(4 - bits);
/* 1277 */       isWhite = true;
/*      */     } 
/*      */ 
/*      */     
/* 1281 */     return runLength;
/*      */   }
/*      */   
/*      */   private int readEOL(boolean isFirstEOL) {
/* 1285 */     if (this.fillBits == 0) {
/* 1286 */       int next12Bits = nextNBits(12);
/* 1287 */       if (isFirstEOL && next12Bits == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1294 */         if (nextNBits(4) == 1) {
/*      */ 
/*      */ 
/*      */           
/* 1298 */           this.fillBits = 1;
/* 1299 */           return 1;
/*      */         } 
/*      */       }
/* 1302 */       if (next12Bits != 1) {
/* 1303 */         throw new RuntimeException(MessageLocalization.getComposedMessage("scanline.must.begin.with.eol.code.word", new Object[0]));
/*      */       }
/* 1305 */     } else if (this.fillBits == 1) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1311 */       int bitsLeft = 8 - this.bitPointer;
/*      */       
/* 1313 */       if (nextNBits(bitsLeft) != 0) {
/* 1314 */         throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1321 */       if (bitsLeft < 4 && 
/* 1322 */         nextNBits(8) != 0) {
/* 1323 */         throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       int n;
/*      */ 
/*      */       
/* 1331 */       while ((n = nextNBits(8)) != 1) {
/*      */         
/* 1333 */         if (n != 0) {
/* 1334 */           throw new RuntimeException(MessageLocalization.getComposedMessage("all.fill.bits.preceding.eol.code.must.be.0", new Object[0]));
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1340 */     if (this.oneD == 0) {
/* 1341 */       return 1;
/*      */     }
/*      */ 
/*      */     
/* 1345 */     return nextLesserThan8Bits(1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
/* 1351 */     int[] pce = this.prevChangingElems;
/* 1352 */     int ces = this.changingElemSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1357 */     int start = (this.lastChangingElement > 0) ? (this.lastChangingElement - 1) : 0;
/* 1358 */     if (isWhite) {
/* 1359 */       start &= 0xFFFFFFFE;
/*      */     } else {
/* 1361 */       start |= 0x1;
/*      */     } 
/*      */     
/* 1364 */     int i = start;
/* 1365 */     for (; i < ces; i += 2) {
/* 1366 */       int temp = pce[i];
/* 1367 */       if (temp > a0) {
/* 1368 */         this.lastChangingElement = i;
/* 1369 */         ret[0] = temp;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1374 */     if (i + 1 < ces) {
/* 1375 */       ret[1] = pce[i + 1];
/*      */     }
/*      */   }
/*      */   
/*      */   private int nextNBits(int bitsToGet) {
/*      */     byte b, next, next2next;
/* 1381 */     int l = this.data.length - 1;
/* 1382 */     int bp = this.bytePointer;
/*      */     
/* 1384 */     if (this.fillOrder == 1L) {
/* 1385 */       b = this.data[bp];
/*      */       
/* 1387 */       if (bp == l) {
/* 1388 */         next = 0;
/* 1389 */         next2next = 0;
/* 1390 */       } else if (bp + 1 == l) {
/* 1391 */         next = this.data[bp + 1];
/* 1392 */         next2next = 0;
/*      */       } else {
/* 1394 */         next = this.data[bp + 1];
/* 1395 */         next2next = this.data[bp + 2];
/*      */       } 
/* 1397 */     } else if (this.fillOrder == 2L) {
/* 1398 */       b = flipTable[this.data[bp] & 0xFF];
/*      */       
/* 1400 */       if (bp == l) {
/* 1401 */         next = 0;
/* 1402 */         next2next = 0;
/* 1403 */       } else if (bp + 1 == l) {
/* 1404 */         next = flipTable[this.data[bp + 1] & 0xFF];
/* 1405 */         next2next = 0;
/*      */       } else {
/* 1407 */         next = flipTable[this.data[bp + 1] & 0xFF];
/* 1408 */         next2next = flipTable[this.data[bp + 2] & 0xFF];
/*      */       } 
/*      */     } else {
/* 1411 */       throw new RuntimeException(MessageLocalization.getComposedMessage("tiff.fill.order.tag.must.be.either.1.or.2", new Object[0]));
/*      */     } 
/*      */     
/* 1414 */     int bitsLeft = 8 - this.bitPointer;
/* 1415 */     int bitsFromNextByte = bitsToGet - bitsLeft;
/* 1416 */     int bitsFromNext2NextByte = 0;
/* 1417 */     if (bitsFromNextByte > 8) {
/* 1418 */       bitsFromNext2NextByte = bitsFromNextByte - 8;
/* 1419 */       bitsFromNextByte = 8;
/*      */     } 
/*      */     
/* 1422 */     this.bytePointer++;
/*      */     
/* 1424 */     int i1 = (b & table1[bitsLeft]) << bitsToGet - bitsLeft;
/* 1425 */     int i2 = (next & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
/*      */     
/* 1427 */     int i3 = 0;
/* 1428 */     if (bitsFromNext2NextByte != 0) {
/* 1429 */       i2 <<= bitsFromNext2NextByte;
/* 1430 */       i3 = (next2next & table2[bitsFromNext2NextByte]) >>> 8 - bitsFromNext2NextByte;
/*      */       
/* 1432 */       i2 |= i3;
/* 1433 */       this.bytePointer++;
/* 1434 */       this.bitPointer = bitsFromNext2NextByte;
/*      */     }
/* 1436 */     else if (bitsFromNextByte == 8) {
/* 1437 */       this.bitPointer = 0;
/* 1438 */       this.bytePointer++;
/*      */     } else {
/* 1440 */       this.bitPointer = bitsFromNextByte;
/*      */     } 
/*      */ 
/*      */     
/* 1444 */     int i = i1 | i2;
/* 1445 */     return i;
/*      */   }
/*      */   private int nextLesserThan8Bits(int bitsToGet) {
/*      */     int i1;
/* 1449 */     byte b = 0, next = 0;
/* 1450 */     int l = this.data.length - 1;
/* 1451 */     int bp = this.bytePointer;
/*      */     
/* 1453 */     if (this.fillOrder == 1L) {
/* 1454 */       b = this.data[bp];
/* 1455 */       if (bp == l) {
/* 1456 */         next = 0;
/*      */       } else {
/* 1458 */         next = this.data[bp + 1];
/*      */       } 
/* 1460 */     } else if (this.fillOrder == 2L) {
/* 1461 */       if (!this.recoverFromImageError || bp < this.data.length) {
/*      */ 
/*      */         
/* 1464 */         b = flipTable[this.data[bp] & 0xFF];
/* 1465 */         if (bp == l) {
/* 1466 */           next = 0;
/*      */         } else {
/* 1468 */           next = flipTable[this.data[bp + 1] & 0xFF];
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1472 */       throw new RuntimeException(MessageLocalization.getComposedMessage("tiff.fill.order.tag.must.be.either.1.or.2", new Object[0]));
/*      */     } 
/*      */     
/* 1475 */     int bitsLeft = 8 - this.bitPointer;
/* 1476 */     int bitsFromNextByte = bitsToGet - bitsLeft;
/*      */     
/* 1478 */     int shift = bitsLeft - bitsToGet;
/*      */     
/* 1480 */     if (shift >= 0) {
/* 1481 */       i1 = (b & table1[bitsLeft]) >>> shift;
/* 1482 */       this.bitPointer += bitsToGet;
/* 1483 */       if (this.bitPointer == 8) {
/* 1484 */         this.bitPointer = 0;
/* 1485 */         this.bytePointer++;
/*      */       } 
/*      */     } else {
/* 1488 */       i1 = (b & table1[bitsLeft]) << -shift;
/* 1489 */       int i2 = (next & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
/*      */       
/* 1491 */       i1 |= i2;
/* 1492 */       this.bytePointer++;
/* 1493 */       this.bitPointer = bitsFromNextByte;
/*      */     } 
/*      */     
/* 1496 */     return i1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updatePointer(int bitsToMoveBack) {
/* 1501 */     int i = this.bitPointer - bitsToMoveBack;
/*      */     
/* 1503 */     if (i < 0) {
/* 1504 */       this.bytePointer--;
/* 1505 */       this.bitPointer = 8 + i;
/*      */     } else {
/* 1507 */       this.bitPointer = i;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean advancePointer() {
/* 1513 */     if (this.bitPointer != 0) {
/* 1514 */       this.bytePointer++;
/* 1515 */       this.bitPointer = 0;
/*      */     } 
/*      */     
/* 1518 */     return true;
/*      */   }
/*      */   
/*      */   public void setRecoverFromImageError(boolean recoverFromImageError) {
/* 1522 */     this.recoverFromImageError = recoverFromImageError;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TIFFFaxDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */