/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.ICC_Profile;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jpeg
/*     */   extends Image
/*     */ {
/*     */   public static final int NOT_A_MARKER = -1;
/*     */   public static final int VALID_MARKER = 0;
/*  72 */   public static final int[] VALID_MARKERS = new int[] { 192, 193, 194 };
/*     */ 
/*     */   
/*     */   public static final int UNSUPPORTED_MARKER = 1;
/*     */ 
/*     */   
/*  78 */   public static final int[] UNSUPPORTED_MARKERS = new int[] { 195, 197, 198, 199, 200, 201, 202, 203, 205, 206, 207 };
/*     */ 
/*     */   
/*     */   public static final int NOPARAM_MARKER = 2;
/*     */ 
/*     */   
/*  84 */   public static final int[] NOPARAM_MARKERS = new int[] { 208, 209, 210, 211, 212, 213, 214, 215, 216, 1 };
/*     */ 
/*     */   
/*     */   public static final int M_APP0 = 224;
/*     */ 
/*     */   
/*     */   public static final int M_APP2 = 226;
/*     */   
/*     */   public static final int M_APPE = 238;
/*     */   
/*     */   public static final int M_APPD = 237;
/*     */   
/*  96 */   public static final byte[] JFIF_ID = new byte[] { 74, 70, 73, 70, 0 };
/*     */ 
/*     */   
/*  99 */   public static final byte[] PS_8BIM_RESO = new byte[] { 56, 66, 73, 77, 3, -19 };
/*     */   
/*     */   private byte[][] icc;
/*     */ 
/*     */   
/*     */   Jpeg(Image image) {
/* 105 */     super(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jpeg(URL url) throws BadElementException, IOException {
/* 116 */     super(url);
/* 117 */     processParameters();
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
/*     */   public Jpeg(byte[] img) throws BadElementException, IOException {
/* 129 */     super((URL)null);
/* 130 */     this.rawData = img;
/* 131 */     this.originalData = img;
/* 132 */     processParameters();
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
/*     */   public Jpeg(byte[] img, float width, float height) throws BadElementException, IOException {
/* 146 */     this(img);
/* 147 */     this.scaledWidth = width;
/* 148 */     this.scaledHeight = height;
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
/*     */   private static final int getShort(InputStream is) throws IOException {
/* 161 */     return (is.read() << 8) + is.read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int marker(int marker) {
/*     */     int i;
/* 171 */     for (i = 0; i < VALID_MARKERS.length; i++) {
/* 172 */       if (marker == VALID_MARKERS[i]) {
/* 173 */         return 0;
/*     */       }
/*     */     } 
/* 176 */     for (i = 0; i < NOPARAM_MARKERS.length; i++) {
/* 177 */       if (marker == NOPARAM_MARKERS[i]) {
/* 178 */         return 2;
/*     */       }
/*     */     } 
/* 181 */     for (i = 0; i < UNSUPPORTED_MARKERS.length; i++) {
/* 182 */       if (marker == UNSUPPORTED_MARKERS[i]) {
/* 183 */         return 1;
/*     */       }
/*     */     } 
/* 186 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processParameters() throws BadElementException, IOException {
/* 197 */     this.type = 32;
/* 198 */     this.originalType = 1;
/* 199 */     InputStream is = null;
/*     */     try {
/*     */       String errorID;
/* 202 */       if (this.rawData == null) {
/* 203 */         is = this.url.openStream();
/* 204 */         errorID = this.url.toString();
/*     */       } else {
/*     */         
/* 207 */         is = new ByteArrayInputStream(this.rawData);
/* 208 */         errorID = "Byte array";
/*     */       } 
/* 210 */       if (is.read() != 255 || is.read() != 216) {
/* 211 */         throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.jpeg.file", new Object[] { errorID }));
/*     */       }
/* 213 */       boolean firstPass = true;
/*     */       
/*     */       while (true) {
/* 216 */         int v = is.read();
/* 217 */         if (v < 0)
/* 218 */           throw new IOException(MessageLocalization.getComposedMessage("premature.eof.while.reading.jpg", new Object[0])); 
/* 219 */         if (v == 255) {
/* 220 */           int marker = is.read();
/* 221 */           if (firstPass && marker == 224) {
/* 222 */             firstPass = false;
/* 223 */             int len = getShort(is);
/* 224 */             if (len < 16) {
/* 225 */               Utilities.skip(is, len - 2);
/*     */               continue;
/*     */             } 
/* 228 */             byte[] bcomp = new byte[JFIF_ID.length];
/* 229 */             int r = is.read(bcomp);
/* 230 */             if (r != bcomp.length)
/* 231 */               throw new BadElementException(MessageLocalization.getComposedMessage("1.corrupted.jfif.marker", new Object[] { errorID })); 
/* 232 */             boolean found = true;
/* 233 */             for (int k = 0; k < bcomp.length; k++) {
/* 234 */               if (bcomp[k] != JFIF_ID[k]) {
/* 235 */                 found = false;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 239 */             if (!found) {
/* 240 */               Utilities.skip(is, len - 2 - bcomp.length);
/*     */               continue;
/*     */             } 
/* 243 */             Utilities.skip(is, 2);
/* 244 */             int units = is.read();
/* 245 */             int dx = getShort(is);
/* 246 */             int dy = getShort(is);
/* 247 */             if (units == 1) {
/* 248 */               this.dpiX = dx;
/* 249 */               this.dpiY = dy;
/*     */             }
/* 251 */             else if (units == 2) {
/* 252 */               this.dpiX = (int)(dx * 2.54F + 0.5F);
/* 253 */               this.dpiY = (int)(dy * 2.54F + 0.5F);
/*     */             } 
/* 255 */             Utilities.skip(is, len - 2 - bcomp.length - 7);
/*     */             continue;
/*     */           } 
/* 258 */           if (marker == 238) {
/* 259 */             int len = getShort(is) - 2;
/* 260 */             byte[] byteappe = new byte[len];
/* 261 */             for (int k = 0; k < len; k++) {
/* 262 */               byteappe[k] = (byte)is.read();
/*     */             }
/* 264 */             if (byteappe.length >= 12) {
/* 265 */               String appe = new String(byteappe, 0, 5, "ISO-8859-1");
/* 266 */               if (appe.equals("Adobe")) {
/* 267 */                 this.invert = true;
/*     */               }
/*     */             } 
/*     */             continue;
/*     */           } 
/* 272 */           if (marker == 226) {
/* 273 */             int len = getShort(is) - 2;
/* 274 */             byte[] byteapp2 = new byte[len];
/* 275 */             for (int k = 0; k < len; k++) {
/* 276 */               byteapp2[k] = (byte)is.read();
/*     */             }
/* 278 */             if (byteapp2.length >= 14) {
/* 279 */               String app2 = new String(byteapp2, 0, 11, "ISO-8859-1");
/* 280 */               if (app2.equals("ICC_PROFILE")) {
/* 281 */                 int order = byteapp2[12] & 0xFF;
/* 282 */                 int count = byteapp2[13] & 0xFF;
/*     */                 
/* 284 */                 if (order < 1)
/* 285 */                   order = 1; 
/* 286 */                 if (count < 1)
/* 287 */                   count = 1; 
/* 288 */                 if (this.icc == null)
/* 289 */                   this.icc = new byte[count][]; 
/* 290 */                 this.icc[order - 1] = byteapp2;
/*     */               } 
/*     */             } 
/*     */             continue;
/*     */           } 
/* 295 */           if (marker == 237) {
/* 296 */             int len = getShort(is) - 2;
/* 297 */             byte[] byteappd = new byte[len]; int k;
/* 298 */             for (k = 0; k < len; k++) {
/* 299 */               byteappd[k] = (byte)is.read();
/*     */             }
/*     */             
/* 302 */             k = 0;
/* 303 */             for (k = 0; k < len - PS_8BIM_RESO.length; k++) {
/* 304 */               boolean found = true;
/* 305 */               for (int j = 0; j < PS_8BIM_RESO.length; j++) {
/* 306 */                 if (byteappd[k + j] != PS_8BIM_RESO[j]) {
/* 307 */                   found = false;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 311 */               if (found) {
/*     */                 break;
/*     */               }
/*     */             } 
/* 315 */             k += PS_8BIM_RESO.length;
/* 316 */             if (k < len - PS_8BIM_RESO.length) {
/*     */ 
/*     */               
/* 319 */               byte namelength = byteappd[k];
/*     */               
/* 321 */               namelength = (byte)(namelength + 1);
/*     */               
/* 323 */               if (namelength % 2 == 1) {
/* 324 */                 namelength = (byte)(namelength + 1);
/*     */               }
/* 326 */               k += namelength;
/*     */               
/* 328 */               int resosize = (byteappd[k] << 24) + (byteappd[k + 1] << 16) + (byteappd[k + 2] << 8) + byteappd[k + 3];
/*     */               
/* 330 */               if (resosize != 16) {
/*     */                 continue;
/*     */               }
/*     */ 
/*     */               
/* 335 */               k += 4;
/* 336 */               int dx = (byteappd[k] << 8) + (byteappd[k + 1] & 0xFF);
/* 337 */               k += 2;
/*     */               
/* 339 */               k += 2;
/* 340 */               int unitsx = (byteappd[k] << 8) + (byteappd[k + 1] & 0xFF);
/* 341 */               k += 2;
/*     */               
/* 343 */               k += 2;
/* 344 */               int dy = (byteappd[k] << 8) + (byteappd[k + 1] & 0xFF);
/* 345 */               k += 2;
/*     */               
/* 347 */               k += 2;
/* 348 */               int unitsy = (byteappd[k] << 8) + (byteappd[k + 1] & 0xFF);
/*     */               
/* 350 */               if (unitsx == 1 || unitsx == 2) {
/* 351 */                 dx = (unitsx == 2) ? (int)(dx * 2.54F + 0.5F) : dx;
/*     */                 
/* 353 */                 if (this.dpiX == 0 || this.dpiX == dx)
/*     */                 {
/*     */ 
/*     */                   
/* 357 */                   this.dpiX = dx; } 
/*     */               } 
/* 359 */               if (unitsy == 1 || unitsy == 2) {
/* 360 */                 dy = (unitsy == 2) ? (int)(dy * 2.54F + 0.5F) : dy;
/*     */                 
/* 362 */                 if (this.dpiY != 0 && this.dpiY != dy) {
/*     */                   continue;
/*     */                 }
/*     */                 
/* 366 */                 this.dpiY = dy;
/*     */               } 
/*     */             } 
/*     */             continue;
/*     */           } 
/* 371 */           firstPass = false;
/* 372 */           int markertype = marker(marker);
/* 373 */           if (markertype == 0) {
/* 374 */             Utilities.skip(is, 2);
/* 375 */             if (is.read() != 8) {
/* 376 */               throw new BadElementException(MessageLocalization.getComposedMessage("1.must.have.8.bits.per.component", new Object[] { errorID }));
/*     */             }
/* 378 */             this.scaledHeight = getShort(is);
/* 379 */             setTop(this.scaledHeight);
/* 380 */             this.scaledWidth = getShort(is);
/* 381 */             setRight(this.scaledWidth);
/* 382 */             this.colorspace = is.read();
/* 383 */             this.bpc = 8;
/*     */             break;
/*     */           } 
/* 386 */           if (markertype == 1) {
/* 387 */             throw new BadElementException(MessageLocalization.getComposedMessage("1.unsupported.jpeg.marker.2", new Object[] { errorID, String.valueOf(marker) }));
/*     */           }
/* 389 */           if (markertype != 2) {
/* 390 */             Utilities.skip(is, getShort(is) - 2);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 396 */       if (is != null) {
/* 397 */         is.close();
/*     */       }
/*     */     } 
/* 400 */     this.plainWidth = getWidth();
/* 401 */     this.plainHeight = getHeight();
/* 402 */     if (this.icc != null) {
/* 403 */       int total = 0;
/* 404 */       for (int k = 0; k < this.icc.length; k++) {
/* 405 */         if (this.icc[k] == null) {
/* 406 */           this.icc = (byte[][])null;
/*     */           return;
/*     */         } 
/* 409 */         total += (this.icc[k]).length - 14;
/*     */       } 
/* 411 */       byte[] ficc = new byte[total];
/* 412 */       total = 0;
/* 413 */       for (int i = 0; i < this.icc.length; i++) {
/* 414 */         System.arraycopy(this.icc[i], 14, ficc, total, (this.icc[i]).length - 14);
/* 415 */         total += (this.icc[i]).length - 14;
/*     */       } 
/*     */       try {
/* 418 */         ICC_Profile icc_prof = ICC_Profile.getInstance(ficc, this.colorspace);
/* 419 */         tagICC(icc_prof);
/*     */       }
/* 421 */       catch (IllegalArgumentException illegalArgumentException) {}
/*     */ 
/*     */       
/* 424 */       this.icc = (byte[][])null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Jpeg.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */