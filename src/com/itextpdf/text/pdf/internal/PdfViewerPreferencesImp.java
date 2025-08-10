/*     */ package com.itextpdf.text.pdf.internal;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfBoolean;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfViewerPreferencesImp
/*     */   implements PdfViewerPreferences
/*     */ {
/*  62 */   public static final PdfName[] VIEWER_PREFERENCES = new PdfName[] { PdfName.HIDETOOLBAR, PdfName.HIDEMENUBAR, PdfName.HIDEWINDOWUI, PdfName.FITWINDOW, PdfName.CENTERWINDOW, PdfName.DISPLAYDOCTITLE, PdfName.NONFULLSCREENPAGEMODE, PdfName.DIRECTION, PdfName.VIEWAREA, PdfName.VIEWCLIP, PdfName.PRINTAREA, PdfName.PRINTCLIP, PdfName.PRINTSCALING, PdfName.DUPLEX, PdfName.PICKTRAYBYPDFSIZE, PdfName.PRINTPAGERANGE, PdfName.NUMCOPIES };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static final PdfName[] NONFULLSCREENPAGEMODE_PREFERENCES = new PdfName[] { PdfName.USENONE, PdfName.USEOUTLINES, PdfName.USETHUMBS, PdfName.USEOC };
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final PdfName[] DIRECTION_PREFERENCES = new PdfName[] { PdfName.L2R, PdfName.R2L };
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static final PdfName[] PAGE_BOUNDARIES = new PdfName[] { PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.BLEEDBOX, PdfName.TRIMBOX, PdfName.ARTBOX };
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static final PdfName[] PRINTSCALING_PREFERENCES = new PdfName[] { PdfName.APPDEFAULT, PdfName.NONE };
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static final PdfName[] DUPLEX_PREFERENCES = new PdfName[] { PdfName.SIMPLEX, PdfName.DUPLEXFLIPSHORTEDGE, PdfName.DUPLEXFLIPLONGEDGE };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   private int pageLayoutAndMode = 0;
/*     */ 
/*     */   
/* 108 */   private PdfDictionary viewerPreferences = new PdfDictionary();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int viewerPreferencesMask = 16773120;
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPageLayoutAndMode() {
/* 117 */     return this.pageLayoutAndMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getViewerPreferences() {
/* 124 */     return this.viewerPreferences;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewerPreferences(int preferences) {
/* 135 */     this.pageLayoutAndMode |= preferences;
/*     */ 
/*     */     
/* 138 */     if ((preferences & 0xFFF000) != 0) {
/* 139 */       this.pageLayoutAndMode = 0xFF000FFF & this.pageLayoutAndMode;
/* 140 */       if ((preferences & 0x1000) != 0)
/* 141 */         this.viewerPreferences.put(PdfName.HIDETOOLBAR, (PdfObject)PdfBoolean.PDFTRUE); 
/* 142 */       if ((preferences & 0x2000) != 0)
/* 143 */         this.viewerPreferences.put(PdfName.HIDEMENUBAR, (PdfObject)PdfBoolean.PDFTRUE); 
/* 144 */       if ((preferences & 0x4000) != 0)
/* 145 */         this.viewerPreferences.put(PdfName.HIDEWINDOWUI, (PdfObject)PdfBoolean.PDFTRUE); 
/* 146 */       if ((preferences & 0x8000) != 0)
/* 147 */         this.viewerPreferences.put(PdfName.FITWINDOW, (PdfObject)PdfBoolean.PDFTRUE); 
/* 148 */       if ((preferences & 0x10000) != 0)
/* 149 */         this.viewerPreferences.put(PdfName.CENTERWINDOW, (PdfObject)PdfBoolean.PDFTRUE); 
/* 150 */       if ((preferences & 0x20000) != 0) {
/* 151 */         this.viewerPreferences.put(PdfName.DISPLAYDOCTITLE, (PdfObject)PdfBoolean.PDFTRUE);
/*     */       }
/* 153 */       if ((preferences & 0x40000) != 0) {
/* 154 */         this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, (PdfObject)PdfName.USENONE);
/* 155 */       } else if ((preferences & 0x80000) != 0) {
/* 156 */         this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, (PdfObject)PdfName.USEOUTLINES);
/* 157 */       } else if ((preferences & 0x100000) != 0) {
/* 158 */         this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, (PdfObject)PdfName.USETHUMBS);
/* 159 */       } else if ((preferences & 0x200000) != 0) {
/* 160 */         this.viewerPreferences.put(PdfName.NONFULLSCREENPAGEMODE, (PdfObject)PdfName.USEOC);
/*     */       } 
/* 162 */       if ((preferences & 0x400000) != 0) {
/* 163 */         this.viewerPreferences.put(PdfName.DIRECTION, (PdfObject)PdfName.L2R);
/* 164 */       } else if ((preferences & 0x800000) != 0) {
/* 165 */         this.viewerPreferences.put(PdfName.DIRECTION, (PdfObject)PdfName.R2L);
/*     */       } 
/* 167 */       if ((preferences & 0x1000000) != 0) {
/* 168 */         this.viewerPreferences.put(PdfName.PRINTSCALING, (PdfObject)PdfName.NONE);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getIndex(PdfName key) {
/* 179 */     for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
/* 180 */       if (VIEWER_PREFERENCES[i].equals(key))
/* 181 */         return i; 
/*     */     } 
/* 183 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPossibleValue(PdfName value, PdfName[] accepted) {
/* 190 */     for (int i = 0; i < accepted.length; i++) {
/* 191 */       if (accepted[i].equals(value)) {
/* 192 */         return true;
/*     */       }
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addViewerPreference(PdfName key, PdfObject value) {
/* 202 */     switch (getIndex(key)) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 14:
/* 210 */         if (value instanceof PdfBoolean) {
/* 211 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 6:
/* 215 */         if (value instanceof PdfName && 
/* 216 */           isPossibleValue((PdfName)value, NONFULLSCREENPAGEMODE_PREFERENCES)) {
/* 217 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 7:
/* 221 */         if (value instanceof PdfName && 
/* 222 */           isPossibleValue((PdfName)value, DIRECTION_PREFERENCES)) {
/* 223 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/* 230 */         if (value instanceof PdfName && 
/* 231 */           isPossibleValue((PdfName)value, PAGE_BOUNDARIES)) {
/* 232 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 12:
/* 236 */         if (value instanceof PdfName && 
/* 237 */           isPossibleValue((PdfName)value, PRINTSCALING_PREFERENCES)) {
/* 238 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 13:
/* 242 */         if (value instanceof PdfName && 
/* 243 */           isPossibleValue((PdfName)value, DUPLEX_PREFERENCES)) {
/* 244 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 15:
/* 248 */         if (value instanceof com.itextpdf.text.pdf.PdfArray) {
/* 249 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */       case 16:
/* 253 */         if (value instanceof com.itextpdf.text.pdf.PdfNumber) {
/* 254 */           this.viewerPreferences.put(key, value);
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToCatalog(PdfDictionary catalog) {
/* 268 */     catalog.remove(PdfName.PAGELAYOUT);
/* 269 */     if ((this.pageLayoutAndMode & 0x1) != 0) {
/* 270 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.SINGLEPAGE);
/* 271 */     } else if ((this.pageLayoutAndMode & 0x2) != 0) {
/* 272 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.ONECOLUMN);
/* 273 */     } else if ((this.pageLayoutAndMode & 0x4) != 0) {
/* 274 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.TWOCOLUMNLEFT);
/* 275 */     } else if ((this.pageLayoutAndMode & 0x8) != 0) {
/* 276 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.TWOCOLUMNRIGHT);
/* 277 */     } else if ((this.pageLayoutAndMode & 0x10) != 0) {
/* 278 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.TWOPAGELEFT);
/* 279 */     } else if ((this.pageLayoutAndMode & 0x20) != 0) {
/* 280 */       catalog.put(PdfName.PAGELAYOUT, (PdfObject)PdfName.TWOPAGERIGHT);
/*     */     } 
/*     */     
/* 283 */     catalog.remove(PdfName.PAGEMODE);
/* 284 */     if ((this.pageLayoutAndMode & 0x40) != 0) {
/* 285 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.USENONE);
/* 286 */     } else if ((this.pageLayoutAndMode & 0x80) != 0) {
/* 287 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.USEOUTLINES);
/* 288 */     } else if ((this.pageLayoutAndMode & 0x100) != 0) {
/* 289 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.USETHUMBS);
/* 290 */     } else if ((this.pageLayoutAndMode & 0x200) != 0) {
/* 291 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.FULLSCREEN);
/* 292 */     } else if ((this.pageLayoutAndMode & 0x400) != 0) {
/* 293 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.USEOC);
/* 294 */     } else if ((this.pageLayoutAndMode & 0x800) != 0) {
/* 295 */       catalog.put(PdfName.PAGEMODE, (PdfObject)PdfName.USEATTACHMENTS);
/*     */     } 
/*     */     
/* 298 */     catalog.remove(PdfName.VIEWERPREFERENCES);
/* 299 */     if (this.viewerPreferences.size() > 0) {
/* 300 */       catalog.put(PdfName.VIEWERPREFERENCES, (PdfObject)this.viewerPreferences);
/*     */     }
/*     */   }
/*     */   
/*     */   public static PdfViewerPreferencesImp getViewerPreferences(PdfDictionary catalog) {
/* 305 */     PdfViewerPreferencesImp preferences = new PdfViewerPreferencesImp();
/* 306 */     int prefs = 0;
/* 307 */     PdfName name = null;
/*     */     
/* 309 */     PdfObject obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGELAYOUT));
/* 310 */     if (obj != null && obj.isName()) {
/* 311 */       name = (PdfName)obj;
/* 312 */       if (name.equals(PdfName.SINGLEPAGE)) {
/* 313 */         prefs |= 0x1;
/* 314 */       } else if (name.equals(PdfName.ONECOLUMN)) {
/* 315 */         prefs |= 0x2;
/* 316 */       } else if (name.equals(PdfName.TWOCOLUMNLEFT)) {
/* 317 */         prefs |= 0x4;
/* 318 */       } else if (name.equals(PdfName.TWOCOLUMNRIGHT)) {
/* 319 */         prefs |= 0x8;
/* 320 */       } else if (name.equals(PdfName.TWOPAGELEFT)) {
/* 321 */         prefs |= 0x10;
/* 322 */       } else if (name.equals(PdfName.TWOPAGERIGHT)) {
/* 323 */         prefs |= 0x20;
/*     */       } 
/*     */     } 
/* 326 */     obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.PAGEMODE));
/* 327 */     if (obj != null && obj.isName()) {
/* 328 */       name = (PdfName)obj;
/* 329 */       if (name.equals(PdfName.USENONE)) {
/* 330 */         prefs |= 0x40;
/* 331 */       } else if (name.equals(PdfName.USEOUTLINES)) {
/* 332 */         prefs |= 0x80;
/* 333 */       } else if (name.equals(PdfName.USETHUMBS)) {
/* 334 */         prefs |= 0x100;
/* 335 */       } else if (name.equals(PdfName.FULLSCREEN)) {
/* 336 */         prefs |= 0x200;
/* 337 */       } else if (name.equals(PdfName.USEOC)) {
/* 338 */         prefs |= 0x400;
/* 339 */       } else if (name.equals(PdfName.USEATTACHMENTS)) {
/* 340 */         prefs |= 0x800;
/*     */       } 
/*     */     } 
/* 343 */     preferences.setViewerPreferences(prefs);
/*     */     
/* 345 */     obj = PdfReader.getPdfObjectRelease(catalog
/* 346 */         .get(PdfName.VIEWERPREFERENCES));
/* 347 */     if (obj != null && obj.isDictionary()) {
/* 348 */       PdfDictionary vp = (PdfDictionary)obj;
/* 349 */       for (int i = 0; i < VIEWER_PREFERENCES.length; i++) {
/* 350 */         obj = PdfReader.getPdfObjectRelease(vp.get(VIEWER_PREFERENCES[i]));
/* 351 */         preferences.addViewerPreference(VIEWER_PREFERENCES[i], obj);
/*     */       } 
/*     */     } 
/* 354 */     return preferences;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/internal/PdfViewerPreferencesImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */