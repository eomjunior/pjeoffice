/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Annotation
/*     */   implements Element
/*     */ {
/*     */   public static final int TEXT = 0;
/*     */   public static final int URL_NET = 1;
/*     */   public static final int URL_AS_STRING = 2;
/*     */   public static final int FILE_DEST = 3;
/*     */   public static final int FILE_PAGE = 4;
/*     */   public static final int NAMED_DEST = 5;
/*     */   public static final int LAUNCH = 6;
/*     */   public static final int SCREEN = 7;
/*     */   public static final String TITLE = "title";
/*     */   public static final String CONTENT = "content";
/*     */   public static final String URL = "url";
/*     */   public static final String FILE = "file";
/*     */   public static final String DESTINATION = "destination";
/*     */   public static final String PAGE = "page";
/*     */   public static final String NAMED = "named";
/*     */   public static final String APPLICATION = "application";
/*     */   public static final String PARAMETERS = "parameters";
/*     */   public static final String OPERATION = "operation";
/*     */   public static final String DEFAULTDIR = "defaultdir";
/*     */   public static final String LLX = "llx";
/*     */   public static final String LLY = "lly";
/*     */   public static final String URX = "urx";
/*     */   public static final String URY = "ury";
/*     */   public static final String MIMETYPE = "mime";
/*     */   protected int annotationtype;
/* 139 */   protected HashMap<String, Object> annotationAttributes = new HashMap<String, Object>();
/*     */ 
/*     */   
/* 142 */   protected float llx = Float.NaN;
/*     */ 
/*     */   
/* 145 */   protected float lly = Float.NaN;
/*     */ 
/*     */   
/* 148 */   protected float urx = Float.NaN;
/*     */ 
/*     */   
/* 151 */   protected float ury = Float.NaN;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Annotation(float llx, float lly, float urx, float ury) {
/* 169 */     this.llx = llx;
/* 170 */     this.lly = lly;
/* 171 */     this.urx = urx;
/* 172 */     this.ury = ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation(Annotation an) {
/* 180 */     this.annotationtype = an.annotationtype;
/* 181 */     this.annotationAttributes = an.annotationAttributes;
/* 182 */     this.llx = an.llx;
/* 183 */     this.lly = an.lly;
/* 184 */     this.urx = an.urx;
/* 185 */     this.ury = an.ury;
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
/*     */   public Annotation(String title, String text) {
/* 198 */     this.annotationtype = 0;
/* 199 */     this.annotationAttributes.put("title", title);
/* 200 */     this.annotationAttributes.put("content", text);
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
/*     */   public Annotation(String title, String text, float llx, float lly, float urx, float ury) {
/* 222 */     this(llx, lly, urx, ury);
/* 223 */     this.annotationtype = 0;
/* 224 */     this.annotationAttributes.put("title", title);
/* 225 */     this.annotationAttributes.put("content", text);
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, URL url) {
/* 243 */     this(llx, lly, urx, ury);
/* 244 */     this.annotationtype = 1;
/* 245 */     this.annotationAttributes.put("url", url);
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, String url) {
/* 263 */     this(llx, lly, urx, ury);
/* 264 */     this.annotationtype = 2;
/* 265 */     this.annotationAttributes.put("file", url);
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, String file, String dest) {
/* 286 */     this(llx, lly, urx, ury);
/* 287 */     this.annotationtype = 3;
/* 288 */     this.annotationAttributes.put("file", file);
/* 289 */     this.annotationAttributes.put("destination", dest);
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, String moviePath, String mimeType, boolean showOnDisplay) {
/* 308 */     this(llx, lly, urx, ury);
/* 309 */     this.annotationtype = 7;
/* 310 */     this.annotationAttributes.put("file", moviePath);
/* 311 */     this.annotationAttributes.put("mime", mimeType);
/* 312 */     this.annotationAttributes.put("parameters", new boolean[] { false, showOnDisplay });
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, String file, int page) {
/* 334 */     this(llx, lly, urx, ury);
/* 335 */     this.annotationtype = 4;
/* 336 */     this.annotationAttributes.put("file", file);
/* 337 */     this.annotationAttributes.put("page", Integer.valueOf(page));
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
/*     */   public Annotation(float llx, float lly, float urx, float ury, int named) {
/* 355 */     this(llx, lly, urx, ury);
/* 356 */     this.annotationtype = 5;
/* 357 */     this.annotationAttributes.put("named", Integer.valueOf(named));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation(float llx, float lly, float urx, float ury, String application, String parameters, String operation, String defaultdir) {
/* 383 */     this(llx, lly, urx, ury);
/* 384 */     this.annotationtype = 6;
/* 385 */     this.annotationAttributes.put("application", application);
/* 386 */     this.annotationAttributes.put("parameters", parameters);
/* 387 */     this.annotationAttributes.put("operation", operation);
/* 388 */     this.annotationAttributes.put("defaultdir", defaultdir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 399 */     return 29;
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
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 412 */       return listener.add(this);
/* 413 */     } catch (DocumentException de) {
/* 414 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 425 */     return new ArrayList<Chunk>();
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
/*     */   public void setDimensions(float llx, float lly, float urx, float ury) {
/* 443 */     this.llx = llx;
/* 444 */     this.lly = lly;
/* 445 */     this.urx = urx;
/* 446 */     this.ury = ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float llx() {
/* 457 */     return this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float lly() {
/* 466 */     return this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float urx() {
/* 475 */     return this.urx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float ury() {
/* 484 */     return this.ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float llx(float def) {
/* 495 */     if (Float.isNaN(this.llx))
/* 496 */       return def; 
/* 497 */     return this.llx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float lly(float def) {
/* 508 */     if (Float.isNaN(this.lly))
/* 509 */       return def; 
/* 510 */     return this.lly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float urx(float def) {
/* 521 */     if (Float.isNaN(this.urx))
/* 522 */       return def; 
/* 523 */     return this.urx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float ury(float def) {
/* 534 */     if (Float.isNaN(this.ury))
/* 535 */       return def; 
/* 536 */     return this.ury;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int annotationType() {
/* 545 */     return this.annotationtype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String title() {
/* 554 */     String s = (String)this.annotationAttributes.get("title");
/* 555 */     if (s == null)
/* 556 */       s = ""; 
/* 557 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String content() {
/* 566 */     String s = (String)this.annotationAttributes.get("content");
/* 567 */     if (s == null)
/* 568 */       s = ""; 
/* 569 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, Object> attributes() {
/* 578 */     return this.annotationAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 586 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 594 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Annotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */