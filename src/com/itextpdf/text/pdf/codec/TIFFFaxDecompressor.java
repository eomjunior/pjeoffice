/*      */ package com.itextpdf.text.pdf.codec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TIFFFaxDecompressor
/*      */ {
/*      */   protected int fillOrder;
/*      */   protected int compression;
/*      */   private int t4Options;
/*      */   private int t6Options;
/*      */   public int fails;
/*   76 */   protected int uncompressedMode = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   81 */   protected int fillBits = 0;
/*      */   
/*      */   protected int oneD;
/*      */   
/*      */   private byte[] data;
/*      */   
/*      */   private int bitPointer;
/*      */   private int bytePointer;
/*      */   private byte[] buffer;
/*      */   private int w;
/*      */   private int h;
/*      */   private int bitsPerScanline;
/*      */   private int lineBitNum;
/*   94 */   private int changingElemSize = 0;
/*      */   
/*      */   private int[] prevChangingElems;
/*      */   private int[] currChangingElems;
/*   98 */   private int lastChangingElement = 0;
/*   99 */   static int[] table1 = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   static int[] table2 = new int[] { 0, 128, 192, 224, 240, 248, 252, 254, 255 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  122 */   static byte[] flipTable = new byte[] { 0, Byte.MIN_VALUE, 64, -64, 32, -96, 96, -32, 16, -112, 80, -48, 48, -80, 112, -16, 8, -120, 72, -56, 40, -88, 104, -24, 24, -104, 88, -40, 56, -72, 120, -8, 4, -124, 68, -60, 36, -92, 100, -28, 20, -108, 84, -44, 52, -76, 116, -12, 12, -116, 76, -52, 44, -84, 108, -20, 28, -100, 92, -36, 60, -68, 124, -4, 2, -126, 66, -62, 34, -94, 98, -30, 18, -110, 82, -46, 50, -78, 114, -14, 10, -118, 74, -54, 42, -86, 106, -22, 26, -102, 90, -38, 58, -70, 122, -6, 6, -122, 70, -58, 38, -90, 102, -26, 22, -106, 86, -42, 54, -74, 118, -10, 14, -114, 78, -50, 46, -82, 110, -18, 30, -98, 94, -34, 62, -66, 126, -2, 1, -127, 65, -63, 33, -95, 97, -31, 17, -111, 81, -47, 49, -79, 113, -15, 9, -119, 73, -55, 41, -87, 105, -23, 25, -103, 89, -39, 57, -71, 121, -7, 5, -123, 69, -59, 37, -91, 101, -27, 21, -107, 85, -43, 53, -75, 117, -11, 13, -115, 77, -51, 45, -83, 109, -19, 29, -99, 93, -35, 61, -67, 125, -3, 3, -125, 67, -61, 35, -93, 99, -29, 19, -109, 83, -45, 51, -77, 115, -13, 11, -117, 75, -53, 43, -85, 107, -21, 27, -101, 91, -37, 59, -69, 123, -5, 7, -121, 71, -57, 39, -89, 103, -25, 23, -105, 87, -41, 55, -73, 119, -9, 15, -113, 79, -49, 47, -81, 111, -17, 31, -97, 95, -33, 63, -65, Byte.MAX_VALUE, -1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  156 */   static short[] white = new short[] { 6430, 6400, 6400, 6400, 3225, 3225, 3225, 3225, 944, 944, 944, 944, 976, 976, 976, 976, 1456, 1456, 1456, 1456, 1488, 1488, 1488, 1488, 718, 718, 718, 718, 718, 718, 718, 718, 750, 750, 750, 750, 750, 750, 750, 750, 1520, 1520, 1520, 1520, 1552, 1552, 1552, 1552, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 428, 654, 654, 654, 654, 654, 654, 654, 654, 1072, 1072, 1072, 1072, 1104, 1104, 1104, 1104, 1136, 1136, 1136, 1136, 1168, 1168, 1168, 1168, 1200, 1200, 1200, 1200, 1232, 1232, 1232, 1232, 622, 622, 622, 622, 622, 622, 622, 622, 1008, 1008, 1008, 1008, 1040, 1040, 1040, 1040, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 396, 1712, 1712, 1712, 1712, 1744, 1744, 1744, 1744, 846, 846, 846, 846, 846, 846, 846, 846, 1264, 1264, 1264, 1264, 1296, 1296, 1296, 1296, 1328, 1328, 1328, 1328, 1360, 1360, 1360, 1360, 1392, 1392, 1392, 1392, 1424, 1424, 1424, 1424, 686, 686, 686, 686, 686, 686, 686, 686, 910, 910, 910, 910, 910, 910, 910, 910, 1968, 1968, 1968, 1968, 2000, 2000, 2000, 2000, 2032, 2032, 2032, 2032, 16, 16, 16, 16, 10257, 10257, 10257, 10257, 12305, 12305, 12305, 12305, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 330, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 362, 878, 878, 878, 878, 878, 878, 878, 878, 1904, 1904, 1904, 1904, 1936, 1936, 1936, 1936, -18413, -18413, -16365, -16365, -14317, -14317, -10221, -10221, 590, 590, 590, 590, 590, 590, 590, 590, 782, 782, 782, 782, 782, 782, 782, 782, 1584, 1584, 1584, 1584, 1616, 1616, 1616, 1616, 1648, 1648, 1648, 1648, 1680, 1680, 1680, 1680, 814, 814, 814, 814, 814, 814, 814, 814, 1776, 1776, 1776, 1776, 1808, 1808, 1808, 1808, 1840, 1840, 1840, 1840, 1872, 1872, 1872, 1872, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, 6157, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, -12275, 14353, 14353, 14353, 14353, 16401, 16401, 16401, 16401, 22547, 22547, 24595, 24595, 20497, 20497, 20497, 20497, 18449, 18449, 18449, 18449, 26643, 26643, 28691, 28691, 30739, 30739, -32749, -32749, -30701, -30701, -28653, -28653, -26605, -26605, -24557, -24557, -22509, -22509, -20461, -20461, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 8207, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 104, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 4107, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 266, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 298, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 556, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 136, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 168, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 460, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 492, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 2059, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 200, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232, 232 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  414 */   static short[] additionalMakeup = new short[] { 28679, 28679, 31752, -32759, -31735, -30711, -29687, -28663, 29703, 29703, 30727, 30727, -27639, -26615, -25591, -24567 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  421 */   static short[] initBlack = new short[] { 3226, 6412, 200, 168, 38, 38, 134, 134, 100, 100, 100, 100, 68, 68, 68, 68 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  428 */   static short[] twoBitBlack = new short[] { 292, 260, 226, 226 };
/*      */   
/*  430 */   static short[] black = new short[] { 62, 62, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 3225, 588, 588, 588, 588, 588, 588, 588, 588, 1680, 1680, 20499, 22547, 24595, 26643, 1776, 1776, 1808, 1808, -24557, -22509, -20461, -18413, 1904, 1904, 1936, 1936, -16365, -14317, 782, 782, 782, 782, 814, 814, 814, 814, -12269, -10221, 10257, 10257, 12305, 12305, 14353, 14353, 16403, 18451, 1712, 1712, 1744, 1744, 28691, 30739, -32749, -30701, -28653, -26605, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 2061, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 424, 750, 750, 750, 750, 1616, 1616, 1648, 1648, 1424, 1424, 1456, 1456, 1488, 1488, 1520, 1520, 1840, 1840, 1872, 1872, 1968, 1968, 8209, 8209, 524, 524, 524, 524, 524, 524, 524, 524, 556, 556, 556, 556, 556, 556, 556, 556, 1552, 1552, 1584, 1584, 2000, 2000, 2032, 2032, 976, 976, 1008, 1008, 1040, 1040, 1072, 1072, 1296, 1296, 1328, 1328, 718, 718, 718, 718, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 456, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 326, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 358, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 490, 4113, 4113, 6161, 6161, 848, 848, 880, 880, 912, 912, 944, 944, 622, 622, 622, 622, 654, 654, 654, 654, 1104, 1104, 1136, 1136, 1168, 1168, 1200, 1200, 1232, 1232, 1264, 1264, 686, 686, 686, 686, 1360, 1360, 1392, 1392, 12, 12, 12, 12, 12, 12, 12, 12, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390, 390 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  559 */   static byte[] twoDCodes = new byte[] { 80, 88, 23, 71, 30, 30, 62, 62, 4, 4, 4, 4, 4, 4, 4, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void SetOptions(int fillOrder, int compression, int t4Options, int t6Options) {
/*  601 */     this.fillOrder = fillOrder;
/*  602 */     this.compression = compression;
/*  603 */     this.t4Options = t4Options;
/*  604 */     this.t6Options = t6Options;
/*  605 */     this.oneD = t4Options & 0x1;
/*  606 */     this.uncompressedMode = (t4Options & 0x2) >> 1;
/*  607 */     this.fillBits = (t4Options & 0x4) >> 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public void decodeRaw(byte[] buffer, byte[] compData, int w, int h) {
/*  612 */     this.buffer = buffer;
/*  613 */     this.data = compData;
/*  614 */     this.w = w;
/*  615 */     this.h = h;
/*  616 */     this.bitsPerScanline = w;
/*  617 */     this.lineBitNum = 0;
/*      */     
/*  619 */     this.bitPointer = 0;
/*  620 */     this.bytePointer = 0;
/*  621 */     this.prevChangingElems = new int[w + 1];
/*  622 */     this.currChangingElems = new int[w + 1];
/*      */     
/*  624 */     this.fails = 0;
/*      */     
/*      */     try {
/*  627 */       if (this.compression == 2) {
/*  628 */         decodeRLE();
/*  629 */       } else if (this.compression == 3) {
/*  630 */         decodeT4();
/*  631 */       } else if (this.compression == 4) {
/*  632 */         this.uncompressedMode = (this.t6Options & 0x2) >> 1;
/*  633 */         decodeT6();
/*      */       } else {
/*  635 */         throw new RuntimeException("Unknown compression type " + this.compression);
/*      */       } 
/*  637 */     } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void decodeRLE() {
/*  643 */     for (int i = 0; i < this.h; i++) {
/*      */       
/*  645 */       decodeNextScanline();
/*      */ 
/*      */       
/*  648 */       if (this.bitPointer != 0) {
/*  649 */         this.bytePointer++;
/*  650 */         this.bitPointer = 0;
/*      */       } 
/*      */ 
/*      */       
/*  654 */       this.lineBitNum += this.bitsPerScanline;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void decodeNextScanline() {
/*  659 */     int bits = 0, code = 0, isT = 0;
/*      */     
/*  661 */     boolean isWhite = true;
/*      */     
/*  663 */     int bitOffset = 0;
/*      */ 
/*      */     
/*  666 */     this.changingElemSize = 0;
/*      */ 
/*      */     
/*  669 */     while (bitOffset < this.w) {
/*      */ 
/*      */       
/*  672 */       int runOffset = bitOffset;
/*      */       
/*  674 */       while (isWhite && bitOffset < this.w) {
/*      */         
/*  676 */         int current = nextNBits(10);
/*  677 */         int entry = white[current];
/*      */ 
/*      */         
/*  680 */         isT = entry & 0x1;
/*  681 */         bits = entry >>> 1 & 0xF;
/*      */         
/*  683 */         if (bits == 12) {
/*      */           
/*  685 */           int twoBits = nextLesserThan8Bits(2);
/*      */           
/*  687 */           current = current << 2 & 0xC | twoBits;
/*  688 */           entry = additionalMakeup[current];
/*  689 */           bits = entry >>> 1 & 0x7;
/*  690 */           code = entry >>> 4 & 0xFFF;
/*  691 */           bitOffset += code;
/*      */           
/*  693 */           updatePointer(4 - bits); continue;
/*  694 */         }  if (bits == 0) {
/*  695 */           this.fails++; continue;
/*      */         } 
/*  697 */         if (bits == 15) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  702 */           this.fails++;
/*      */           
/*      */           return;
/*      */         } 
/*  706 */         code = entry >>> 5 & 0x7FF;
/*  707 */         bitOffset += code;
/*      */         
/*  709 */         updatePointer(10 - bits);
/*  710 */         if (isT == 0) {
/*  711 */           isWhite = false;
/*  712 */           this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  718 */       if (bitOffset == this.w) {
/*      */ 
/*      */ 
/*      */         
/*  722 */         int runLength = bitOffset - runOffset;
/*  723 */         if (isWhite && runLength != 0 && runLength % 64 == 0 && 
/*      */           
/*  725 */           nextNBits(8) != 53) {
/*  726 */           this.fails++;
/*  727 */           updatePointer(8);
/*      */         } 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/*  733 */       runOffset = bitOffset;
/*      */       
/*  735 */       while (!isWhite && bitOffset < this.w) {
/*      */         
/*  737 */         int current = nextLesserThan8Bits(4);
/*  738 */         int entry = initBlack[current];
/*      */ 
/*      */         
/*  741 */         isT = entry & 0x1;
/*  742 */         bits = entry >>> 1 & 0xF;
/*  743 */         code = entry >>> 5 & 0x7FF;
/*      */         
/*  745 */         if (code == 100) {
/*  746 */           current = nextNBits(9);
/*  747 */           entry = black[current];
/*      */ 
/*      */           
/*  750 */           isT = entry & 0x1;
/*  751 */           bits = entry >>> 1 & 0xF;
/*  752 */           code = entry >>> 5 & 0x7FF;
/*      */           
/*  754 */           if (bits == 12) {
/*      */             
/*  756 */             updatePointer(5);
/*  757 */             current = nextLesserThan8Bits(4);
/*  758 */             entry = additionalMakeup[current];
/*  759 */             bits = entry >>> 1 & 0x7;
/*  760 */             code = entry >>> 4 & 0xFFF;
/*      */             
/*  762 */             setToBlack(bitOffset, code);
/*  763 */             bitOffset += code;
/*      */             
/*  765 */             updatePointer(4 - bits); continue;
/*  766 */           }  if (bits == 15) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  771 */             this.fails++;
/*      */             return;
/*      */           } 
/*  774 */           setToBlack(bitOffset, code);
/*  775 */           bitOffset += code;
/*      */           
/*  777 */           updatePointer(9 - bits);
/*  778 */           if (isT == 0) {
/*  779 */             isWhite = true;
/*  780 */             this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */           }  continue;
/*      */         } 
/*  783 */         if (code == 200) {
/*      */           
/*  785 */           current = nextLesserThan8Bits(2);
/*  786 */           entry = twoBitBlack[current];
/*  787 */           code = entry >>> 5 & 0x7FF;
/*  788 */           bits = entry >>> 1 & 0xF;
/*      */           
/*  790 */           setToBlack(bitOffset, code);
/*  791 */           bitOffset += code;
/*      */           
/*  793 */           updatePointer(2 - bits);
/*  794 */           isWhite = true;
/*  795 */           this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */           continue;
/*      */         } 
/*  798 */         setToBlack(bitOffset, code);
/*  799 */         bitOffset += code;
/*      */         
/*  801 */         updatePointer(4 - bits);
/*  802 */         isWhite = true;
/*  803 */         this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  808 */       if (bitOffset == this.w) {
/*      */ 
/*      */ 
/*      */         
/*  812 */         int runLength = bitOffset - runOffset;
/*  813 */         if (!isWhite && runLength != 0 && runLength % 64 == 0 && 
/*      */           
/*  815 */           nextNBits(10) != 55) {
/*  816 */           this.fails++;
/*  817 */           updatePointer(10);
/*      */         } 
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  823 */     this.currChangingElems[this.changingElemSize++] = bitOffset;
/*      */   }
/*      */   
/*      */   public void decodeT4() {
/*  827 */     int height = this.h;
/*      */ 
/*      */     
/*  830 */     int[] b = new int[2];
/*      */ 
/*      */     
/*  833 */     int currIndex = 0;
/*      */ 
/*      */     
/*  836 */     if (this.data.length < 2) {
/*  837 */       throw new RuntimeException("Insufficient data to read initial EOL.");
/*      */     }
/*      */ 
/*      */     
/*  841 */     int next12 = nextNBits(12);
/*  842 */     if (next12 != 1) {
/*  843 */       this.fails++;
/*      */     }
/*  845 */     updatePointer(12);
/*      */ 
/*      */     
/*  848 */     int modeFlag = 0;
/*  849 */     int lines = -1;
/*  850 */     while (modeFlag != 1) {
/*      */       try {
/*  852 */         modeFlag = findNextLine();
/*  853 */         lines++;
/*  854 */       } catch (Exception eofe) {
/*  855 */         throw new RuntimeException("No reference line present.");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  863 */     decodeNextScanline();
/*  864 */     lines++;
/*  865 */     this.lineBitNum += this.bitsPerScanline;
/*      */     
/*  867 */     while (lines < height) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  872 */         modeFlag = findNextLine();
/*  873 */       } catch (Exception eofe) {
/*  874 */         this.fails++;
/*      */         break;
/*      */       } 
/*  877 */       if (modeFlag == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  882 */         int[] temp = this.prevChangingElems;
/*  883 */         this.prevChangingElems = this.currChangingElems;
/*  884 */         this.currChangingElems = temp;
/*  885 */         currIndex = 0;
/*      */ 
/*      */         
/*  888 */         int a0 = -1;
/*  889 */         boolean isWhite = true;
/*  890 */         int bitOffset = 0;
/*      */         
/*  892 */         this.lastChangingElement = 0;
/*      */         
/*  894 */         while (bitOffset < this.w) {
/*      */           
/*  896 */           getNextChangingElement(a0, isWhite, b);
/*      */           
/*  898 */           int b1 = b[0];
/*  899 */           int b2 = b[1];
/*      */ 
/*      */           
/*  902 */           int entry = nextLesserThan8Bits(7);
/*      */ 
/*      */           
/*  905 */           entry = twoDCodes[entry] & 0xFF;
/*      */ 
/*      */           
/*  908 */           int code = (entry & 0x78) >>> 3;
/*  909 */           int bits = entry & 0x7;
/*      */           
/*  911 */           if (code == 0) {
/*  912 */             if (!isWhite) {
/*  913 */               setToBlack(bitOffset, b2 - bitOffset);
/*      */             }
/*  915 */             bitOffset = a0 = b2;
/*      */ 
/*      */             
/*  918 */             updatePointer(7 - bits); continue;
/*  919 */           }  if (code == 1) {
/*      */             
/*  921 */             updatePointer(7 - bits);
/*      */ 
/*      */ 
/*      */             
/*  925 */             if (isWhite) {
/*  926 */               int number = decodeWhiteCodeWord();
/*  927 */               bitOffset += number;
/*  928 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */               
/*  930 */               number = decodeBlackCodeWord();
/*  931 */               setToBlack(bitOffset, number);
/*  932 */               bitOffset += number;
/*  933 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */             } else {
/*  935 */               int number = decodeBlackCodeWord();
/*  936 */               setToBlack(bitOffset, number);
/*  937 */               bitOffset += number;
/*  938 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */               
/*  940 */               number = decodeWhiteCodeWord();
/*  941 */               bitOffset += number;
/*  942 */               this.currChangingElems[currIndex++] = bitOffset;
/*      */             } 
/*      */             
/*  945 */             a0 = bitOffset; continue;
/*  946 */           }  if (code <= 8) {
/*      */             
/*  948 */             int a1 = b1 + code - 5;
/*      */             
/*  950 */             this.currChangingElems[currIndex++] = a1;
/*      */ 
/*      */ 
/*      */             
/*  954 */             if (!isWhite) {
/*  955 */               setToBlack(bitOffset, a1 - bitOffset);
/*      */             }
/*  957 */             bitOffset = a0 = a1;
/*  958 */             isWhite = !isWhite;
/*      */             
/*  960 */             updatePointer(7 - bits); continue;
/*      */           } 
/*  962 */           this.fails++;
/*      */           
/*  964 */           int numLinesTested = 0;
/*  965 */           while (modeFlag != 1) {
/*      */             try {
/*  967 */               modeFlag = findNextLine();
/*  968 */               numLinesTested++;
/*  969 */             } catch (Exception eofe) {
/*      */               return;
/*      */             } 
/*      */           } 
/*  973 */           lines += numLinesTested - 1;
/*  974 */           updatePointer(13);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  981 */         this.currChangingElems[currIndex++] = bitOffset;
/*  982 */         this.changingElemSize = currIndex;
/*      */       } else {
/*      */         
/*  985 */         decodeNextScanline();
/*      */       } 
/*      */       
/*  988 */       this.lineBitNum += this.bitsPerScanline;
/*  989 */       lines++;
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized void decodeT6() {
/*  994 */     int height = this.h;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1004 */     int[] b = new int[2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     int[] cce = this.currChangingElems;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1016 */     this.changingElemSize = 0;
/* 1017 */     cce[this.changingElemSize++] = this.w;
/* 1018 */     cce[this.changingElemSize++] = this.w;
/*      */ 
/*      */ 
/*      */     
/* 1022 */     for (int lines = 0; lines < height; lines++) {
/*      */       
/* 1024 */       int a0 = -1;
/* 1025 */       boolean isWhite = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1030 */       int[] temp = this.prevChangingElems;
/* 1031 */       this.prevChangingElems = this.currChangingElems;
/* 1032 */       cce = this.currChangingElems = temp;
/* 1033 */       int currIndex = 0;
/*      */ 
/*      */       
/* 1036 */       int bitOffset = 0;
/*      */ 
/*      */       
/* 1039 */       this.lastChangingElement = 0;
/*      */ 
/*      */       
/* 1042 */       while (bitOffset < this.w) {
/*      */         
/* 1044 */         getNextChangingElement(a0, isWhite, b);
/* 1045 */         int b1 = b[0];
/* 1046 */         int b2 = b[1];
/*      */ 
/*      */         
/* 1049 */         int entry = nextLesserThan8Bits(7);
/*      */         
/* 1051 */         entry = twoDCodes[entry] & 0xFF;
/*      */ 
/*      */         
/* 1054 */         int code = (entry & 0x78) >>> 3;
/* 1055 */         int bits = entry & 0x7;
/*      */         
/* 1057 */         if (code == 0) {
/*      */           
/* 1059 */           if (!isWhite) {
/* 1060 */             if (b2 > this.w) {
/* 1061 */               b2 = this.w;
/*      */             }
/* 1063 */             setToBlack(bitOffset, b2 - bitOffset);
/*      */           } 
/* 1065 */           bitOffset = a0 = b2;
/*      */ 
/*      */           
/* 1068 */           updatePointer(7 - bits); continue;
/* 1069 */         }  if (code == 1) {
/*      */           
/* 1071 */           updatePointer(7 - bits);
/*      */ 
/*      */ 
/*      */           
/* 1075 */           if (isWhite) {
/*      */             
/* 1077 */             int number = decodeWhiteCodeWord();
/* 1078 */             bitOffset += number;
/* 1079 */             cce[currIndex++] = bitOffset;
/*      */             
/* 1081 */             number = decodeBlackCodeWord();
/* 1082 */             if (number > this.w - bitOffset) {
/* 1083 */               number = this.w - bitOffset;
/*      */             }
/* 1085 */             setToBlack(bitOffset, number);
/* 1086 */             bitOffset += number;
/* 1087 */             cce[currIndex++] = bitOffset;
/*      */           } else {
/*      */             
/* 1090 */             int number = decodeBlackCodeWord();
/* 1091 */             if (number > this.w - bitOffset) {
/* 1092 */               number = this.w - bitOffset;
/*      */             }
/* 1094 */             setToBlack(bitOffset, number);
/* 1095 */             bitOffset += number;
/* 1096 */             cce[currIndex++] = bitOffset;
/*      */             
/* 1098 */             number = decodeWhiteCodeWord();
/* 1099 */             bitOffset += number;
/* 1100 */             cce[currIndex++] = bitOffset;
/*      */           } 
/*      */           
/* 1103 */           a0 = bitOffset; continue;
/* 1104 */         }  if (code <= 8) {
/* 1105 */           int a1 = b1 + code - 5;
/* 1106 */           cce[currIndex++] = a1;
/*      */ 
/*      */ 
/*      */           
/* 1110 */           if (!isWhite) {
/* 1111 */             if (a1 > this.w) {
/* 1112 */               a1 = this.w;
/*      */             }
/* 1114 */             setToBlack(bitOffset, a1 - bitOffset);
/*      */           } 
/* 1116 */           bitOffset = a0 = a1;
/* 1117 */           isWhite = !isWhite;
/*      */           
/* 1119 */           updatePointer(7 - bits); continue;
/* 1120 */         }  if (code == 11) {
/* 1121 */           int entranceCode = nextLesserThan8Bits(3);
/*      */           
/* 1123 */           int zeros = 0;
/* 1124 */           boolean exit = false;
/*      */           
/* 1126 */           while (!exit) {
/* 1127 */             while (nextLesserThan8Bits(1) != 1) {
/* 1128 */               zeros++;
/*      */             }
/*      */             
/* 1131 */             if (zeros > 5) {
/*      */ 
/*      */ 
/*      */               
/* 1135 */               zeros -= 6;
/*      */               
/* 1137 */               if (!isWhite && zeros > 0) {
/* 1138 */                 cce[currIndex++] = bitOffset;
/*      */               }
/*      */ 
/*      */               
/* 1142 */               bitOffset += zeros;
/* 1143 */               if (zeros > 0)
/*      */               {
/* 1145 */                 isWhite = true;
/*      */               }
/*      */ 
/*      */ 
/*      */               
/* 1150 */               if (nextLesserThan8Bits(1) == 0) {
/* 1151 */                 if (!isWhite) {
/* 1152 */                   cce[currIndex++] = bitOffset;
/*      */                 }
/* 1154 */                 isWhite = true;
/*      */               } else {
/* 1156 */                 if (isWhite) {
/* 1157 */                   cce[currIndex++] = bitOffset;
/*      */                 }
/* 1159 */                 isWhite = false;
/*      */               } 
/*      */               
/* 1162 */               exit = true;
/*      */             } 
/*      */             
/* 1165 */             if (zeros == 5) {
/* 1166 */               if (!isWhite) {
/* 1167 */                 cce[currIndex++] = bitOffset;
/*      */               }
/* 1169 */               bitOffset += zeros;
/*      */ 
/*      */               
/* 1172 */               isWhite = true; continue;
/*      */             } 
/* 1174 */             bitOffset += zeros;
/*      */             
/* 1176 */             cce[currIndex++] = bitOffset;
/* 1177 */             setToBlack(bitOffset, 1);
/* 1178 */             bitOffset++;
/*      */ 
/*      */             
/* 1181 */             isWhite = false;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1190 */       if (currIndex <= this.w) {
/* 1191 */         cce[currIndex++] = bitOffset;
/*      */       }
/*      */       
/* 1194 */       this.changingElemSize = currIndex;
/*      */       
/* 1196 */       this.lineBitNum += this.bitsPerScanline;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setToBlack(int bitNum, int numBits) {
/* 1202 */     bitNum += this.lineBitNum;
/*      */     
/* 1204 */     int lastBit = bitNum + numBits;
/* 1205 */     int byteNum = bitNum >> 3;
/*      */ 
/*      */     
/* 1208 */     int shift = bitNum & 0x7;
/* 1209 */     if (shift > 0) {
/* 1210 */       int maskVal = 1 << 7 - shift;
/* 1211 */       byte val = this.buffer[byteNum];
/* 1212 */       while (maskVal > 0 && bitNum < lastBit) {
/* 1213 */         val = (byte)(val | maskVal);
/* 1214 */         maskVal >>= 1;
/* 1215 */         bitNum++;
/*      */       } 
/* 1217 */       this.buffer[byteNum] = val;
/*      */     } 
/*      */ 
/*      */     
/* 1221 */     byteNum = bitNum >> 3;
/* 1222 */     while (bitNum < lastBit - 7) {
/* 1223 */       this.buffer[byteNum++] = -1;
/* 1224 */       bitNum += 8;
/*      */     } 
/*      */ 
/*      */     
/* 1228 */     while (bitNum < lastBit) {
/* 1229 */       byteNum = bitNum >> 3;
/* 1230 */       this.buffer[byteNum] = (byte)(this.buffer[byteNum] | 1 << 7 - (bitNum & 0x7));
/* 1231 */       bitNum++;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int decodeWhiteCodeWord() {
/* 1237 */     int code = -1;
/* 1238 */     int runLength = 0;
/* 1239 */     boolean isWhite = true;
/*      */     
/* 1241 */     while (isWhite) {
/* 1242 */       int current = nextNBits(10);
/* 1243 */       int entry = white[current];
/*      */ 
/*      */       
/* 1246 */       int isT = entry & 0x1;
/* 1247 */       int bits = entry >>> 1 & 0xF;
/*      */       
/* 1249 */       if (bits == 12) {
/*      */         
/* 1251 */         int twoBits = nextLesserThan8Bits(2);
/*      */         
/* 1253 */         current = current << 2 & 0xC | twoBits;
/* 1254 */         entry = additionalMakeup[current];
/* 1255 */         bits = entry >>> 1 & 0x7;
/* 1256 */         code = entry >>> 4 & 0xFFF;
/* 1257 */         runLength += code;
/* 1258 */         updatePointer(4 - bits); continue;
/* 1259 */       }  if (bits == 0)
/* 1260 */         throw new RuntimeException("Error 0"); 
/* 1261 */       if (bits == 15) {
/* 1262 */         throw new RuntimeException("Error 1");
/*      */       }
/*      */       
/* 1265 */       code = entry >>> 5 & 0x7FF;
/* 1266 */       runLength += code;
/* 1267 */       updatePointer(10 - bits);
/* 1268 */       if (isT == 0) {
/* 1269 */         isWhite = false;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1274 */     return runLength;
/*      */   }
/*      */ 
/*      */   
/*      */   private int decodeBlackCodeWord() {
/* 1279 */     int code = -1;
/* 1280 */     int runLength = 0;
/* 1281 */     boolean isWhite = false;
/*      */     
/* 1283 */     while (!isWhite) {
/* 1284 */       int current = nextLesserThan8Bits(4);
/* 1285 */       int entry = initBlack[current];
/*      */ 
/*      */       
/* 1288 */       int isT = entry & 0x1;
/* 1289 */       int bits = entry >>> 1 & 0xF;
/* 1290 */       code = entry >>> 5 & 0x7FF;
/*      */       
/* 1292 */       if (code == 100) {
/* 1293 */         current = nextNBits(9);
/* 1294 */         entry = black[current];
/*      */ 
/*      */         
/* 1297 */         isT = entry & 0x1;
/* 1298 */         bits = entry >>> 1 & 0xF;
/* 1299 */         code = entry >>> 5 & 0x7FF;
/*      */         
/* 1301 */         if (bits == 12) {
/*      */           
/* 1303 */           updatePointer(5);
/* 1304 */           current = nextLesserThan8Bits(4);
/* 1305 */           entry = additionalMakeup[current];
/* 1306 */           bits = entry >>> 1 & 0x7;
/* 1307 */           code = entry >>> 4 & 0xFFF;
/* 1308 */           runLength += code;
/*      */           
/* 1310 */           updatePointer(4 - bits); continue;
/* 1311 */         }  if (bits == 15)
/*      */         {
/* 1313 */           throw new RuntimeException("Error 2");
/*      */         }
/* 1315 */         runLength += code;
/* 1316 */         updatePointer(9 - bits);
/* 1317 */         if (isT == 0)
/* 1318 */           isWhite = true; 
/*      */         continue;
/*      */       } 
/* 1321 */       if (code == 200) {
/*      */         
/* 1323 */         current = nextLesserThan8Bits(2);
/* 1324 */         entry = twoBitBlack[current];
/* 1325 */         code = entry >>> 5 & 0x7FF;
/* 1326 */         runLength += code;
/* 1327 */         bits = entry >>> 1 & 0xF;
/* 1328 */         updatePointer(2 - bits);
/* 1329 */         isWhite = true;
/*      */         continue;
/*      */       } 
/* 1332 */       runLength += code;
/* 1333 */       updatePointer(4 - bits);
/* 1334 */       isWhite = true;
/*      */     } 
/*      */ 
/*      */     
/* 1338 */     return runLength;
/*      */   }
/*      */ 
/*      */   
/*      */   private int findNextLine() {
/* 1343 */     int bitIndexMax = this.data.length * 8 - 1;
/* 1344 */     int bitIndexMax12 = bitIndexMax - 12;
/* 1345 */     int bitIndex = this.bytePointer * 8 + this.bitPointer;
/*      */ 
/*      */     
/* 1348 */     while (bitIndex <= bitIndexMax12) {
/*      */       
/* 1350 */       int next12Bits = nextNBits(12);
/* 1351 */       bitIndex += 12;
/*      */ 
/*      */ 
/*      */       
/* 1355 */       while (next12Bits != 1 && bitIndex < bitIndexMax) {
/*      */ 
/*      */         
/* 1358 */         next12Bits = (next12Bits & 0x7FF) << 1 | nextLesserThan8Bits(1) & 0x1;
/* 1359 */         bitIndex++;
/*      */       } 
/*      */       
/* 1362 */       if (next12Bits == 1) {
/* 1363 */         if (this.oneD == 1) {
/* 1364 */           if (bitIndex < bitIndexMax)
/*      */           {
/* 1366 */             return nextLesserThan8Bits(1); } 
/*      */           continue;
/*      */         } 
/* 1369 */         return 1;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1375 */     throw new RuntimeException();
/*      */   }
/*      */ 
/*      */   
/*      */   private void getNextChangingElement(int a0, boolean isWhite, int[] ret) {
/* 1380 */     int[] pce = this.prevChangingElems;
/* 1381 */     int ces = this.changingElemSize;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1386 */     int start = (this.lastChangingElement > 0) ? (this.lastChangingElement - 1) : 0;
/* 1387 */     if (isWhite) {
/* 1388 */       start &= 0xFFFFFFFE;
/*      */     } else {
/* 1390 */       start |= 0x1;
/*      */     } 
/*      */     
/* 1393 */     int i = start;
/* 1394 */     for (; i < ces; i += 2) {
/* 1395 */       int temp = pce[i];
/* 1396 */       if (temp > a0) {
/* 1397 */         this.lastChangingElement = i;
/* 1398 */         ret[0] = temp;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1403 */     if (i + 1 < ces) {
/* 1404 */       ret[1] = pce[i + 1];
/*      */     }
/*      */   }
/*      */   
/*      */   private int nextNBits(int bitsToGet) {
/*      */     byte b, next, next2next;
/* 1410 */     int l = this.data.length - 1;
/* 1411 */     int bp = this.bytePointer;
/*      */     
/* 1413 */     if (this.fillOrder == 1) {
/* 1414 */       b = this.data[bp];
/*      */       
/* 1416 */       if (bp == l) {
/* 1417 */         next = 0;
/* 1418 */         next2next = 0;
/* 1419 */       } else if (bp + 1 == l) {
/* 1420 */         next = this.data[bp + 1];
/* 1421 */         next2next = 0;
/*      */       } else {
/* 1423 */         next = this.data[bp + 1];
/* 1424 */         next2next = this.data[bp + 2];
/*      */       } 
/* 1426 */     } else if (this.fillOrder == 2) {
/* 1427 */       b = flipTable[this.data[bp] & 0xFF];
/*      */       
/* 1429 */       if (bp == l) {
/* 1430 */         next = 0;
/* 1431 */         next2next = 0;
/* 1432 */       } else if (bp + 1 == l) {
/* 1433 */         next = flipTable[this.data[bp + 1] & 0xFF];
/* 1434 */         next2next = 0;
/*      */       } else {
/* 1436 */         next = flipTable[this.data[bp + 1] & 0xFF];
/* 1437 */         next2next = flipTable[this.data[bp + 2] & 0xFF];
/*      */       } 
/*      */     } else {
/* 1440 */       throw new RuntimeException("Invalid FillOrder");
/*      */     } 
/*      */     
/* 1443 */     int bitsLeft = 8 - this.bitPointer;
/* 1444 */     int bitsFromNextByte = bitsToGet - bitsLeft;
/* 1445 */     int bitsFromNext2NextByte = 0;
/* 1446 */     if (bitsFromNextByte > 8) {
/* 1447 */       bitsFromNext2NextByte = bitsFromNextByte - 8;
/* 1448 */       bitsFromNextByte = 8;
/*      */     } 
/*      */     
/* 1451 */     this.bytePointer++;
/*      */     
/* 1453 */     int i1 = (b & table1[bitsLeft]) << bitsToGet - bitsLeft;
/* 1454 */     int i2 = (next & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
/*      */     
/* 1456 */     int i3 = 0;
/* 1457 */     if (bitsFromNext2NextByte != 0) {
/* 1458 */       i2 <<= bitsFromNext2NextByte;
/* 1459 */       i3 = (next2next & table2[bitsFromNext2NextByte]) >>> 8 - bitsFromNext2NextByte;
/*      */       
/* 1461 */       i2 |= i3;
/* 1462 */       this.bytePointer++;
/* 1463 */       this.bitPointer = bitsFromNext2NextByte;
/*      */     }
/* 1465 */     else if (bitsFromNextByte == 8) {
/* 1466 */       this.bitPointer = 0;
/* 1467 */       this.bytePointer++;
/*      */     } else {
/* 1469 */       this.bitPointer = bitsFromNextByte;
/*      */     } 
/*      */ 
/*      */     
/* 1473 */     int i = i1 | i2;
/* 1474 */     return i;
/*      */   }
/*      */   
/*      */   private int nextLesserThan8Bits(int bitsToGet) {
/*      */     byte b, next;
/* 1479 */     int i1, l = this.data.length - 1;
/* 1480 */     int bp = this.bytePointer;
/*      */     
/* 1482 */     if (this.fillOrder == 1) {
/* 1483 */       b = this.data[bp];
/* 1484 */       if (bp == l) {
/* 1485 */         next = 0;
/*      */       } else {
/* 1487 */         next = this.data[bp + 1];
/*      */       } 
/* 1489 */     } else if (this.fillOrder == 2) {
/* 1490 */       b = flipTable[this.data[bp] & 0xFF];
/* 1491 */       if (bp == l) {
/* 1492 */         next = 0;
/*      */       } else {
/* 1494 */         next = flipTable[this.data[bp + 1] & 0xFF];
/*      */       } 
/*      */     } else {
/* 1497 */       throw new RuntimeException("Invalid FillOrder");
/*      */     } 
/*      */     
/* 1500 */     int bitsLeft = 8 - this.bitPointer;
/* 1501 */     int bitsFromNextByte = bitsToGet - bitsLeft;
/*      */     
/* 1503 */     int shift = bitsLeft - bitsToGet;
/*      */     
/* 1505 */     if (shift >= 0) {
/* 1506 */       i1 = (b & table1[bitsLeft]) >>> shift;
/* 1507 */       this.bitPointer += bitsToGet;
/* 1508 */       if (this.bitPointer == 8) {
/* 1509 */         this.bitPointer = 0;
/* 1510 */         this.bytePointer++;
/*      */       } 
/*      */     } else {
/* 1513 */       i1 = (b & table1[bitsLeft]) << -shift;
/* 1514 */       int i2 = (next & table2[bitsFromNextByte]) >>> 8 - bitsFromNextByte;
/*      */       
/* 1516 */       i1 |= i2;
/* 1517 */       this.bytePointer++;
/* 1518 */       this.bitPointer = bitsFromNextByte;
/*      */     } 
/*      */     
/* 1521 */     return i1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updatePointer(int bitsToMoveBack) {
/* 1526 */     if (bitsToMoveBack > 8) {
/* 1527 */       this.bytePointer -= bitsToMoveBack / 8;
/* 1528 */       bitsToMoveBack %= 8;
/*      */     } 
/*      */     
/* 1531 */     int i = this.bitPointer - bitsToMoveBack;
/* 1532 */     if (i < 0) {
/* 1533 */       this.bytePointer--;
/* 1534 */       this.bitPointer = 8 + i;
/*      */     } else {
/* 1536 */       this.bitPointer = i;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TIFFFaxDecompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */