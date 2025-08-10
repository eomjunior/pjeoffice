/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfLayer
/*     */   extends PdfDictionary
/*     */   implements PdfOCG
/*     */ {
/*     */   protected PdfIndirectReference ref;
/*     */   protected ArrayList<PdfLayer> children;
/*     */   protected PdfLayer parent;
/*     */   protected String title;
/*     */   private boolean on = true;
/*     */   private boolean onPanel = true;
/*     */   
/*     */   PdfLayer(String title) {
/*  74 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfLayer createTitle(String title, PdfWriter writer) {
/*  85 */     if (title == null)
/*  86 */       throw new NullPointerException(MessageLocalization.getComposedMessage("title.cannot.be.null", new Object[0])); 
/*  87 */     PdfLayer layer = new PdfLayer(title);
/*  88 */     writer.registerLayer(layer);
/*  89 */     return layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfLayer(String name, PdfWriter writer) throws IOException {
/*  98 */     super(PdfName.OCG);
/*  99 */     setName(name);
/* 100 */     if (writer instanceof PdfStamperImp) {
/* 101 */       this.ref = writer.addToBody(this).getIndirectReference();
/*     */     } else {
/* 103 */       this.ref = writer.getPdfIndirectReference();
/* 104 */     }  writer.registerLayer(this);
/*     */   }
/*     */   
/*     */   String getTitle() {
/* 108 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChild(PdfLayer child) {
/* 116 */     if (child.parent != null)
/* 117 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.layer.1.already.has.a.parent", new Object[] { child.getAsString(PdfName.NAME).toUnicodeString() })); 
/* 118 */     child.parent = this;
/* 119 */     if (this.children == null)
/* 120 */       this.children = new ArrayList<PdfLayer>(); 
/* 121 */     this.children.add(child);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfLayer getParent() {
/* 130 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<PdfLayer> getChildren() {
/* 138 */     return this.children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getRef() {
/* 146 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRef(PdfIndirectReference ref) {
/* 156 */     this.ref = ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 164 */     put(PdfName.NAME, new PdfString(name, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject getPdfObject() {
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOn() {
/* 180 */     return this.on;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOn(boolean on) {
/* 188 */     this.on = on;
/*     */   }
/*     */   
/*     */   private PdfDictionary getUsage() {
/* 192 */     PdfDictionary usage = getAsDict(PdfName.USAGE);
/* 193 */     if (usage == null) {
/* 194 */       usage = new PdfDictionary();
/* 195 */       put(PdfName.USAGE, usage);
/*     */     } 
/* 197 */     return usage;
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
/*     */   public void setCreatorInfo(String creator, String subtype) {
/* 210 */     PdfDictionary usage = getUsage();
/* 211 */     PdfDictionary dic = new PdfDictionary();
/* 212 */     dic.put(PdfName.CREATOR, new PdfString(creator, "UnicodeBig"));
/* 213 */     dic.put(PdfName.SUBTYPE, new PdfName(subtype));
/* 214 */     usage.put(PdfName.CREATORINFO, dic);
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
/*     */   public void setLanguage(String lang, boolean preferred) {
/* 226 */     PdfDictionary usage = getUsage();
/* 227 */     PdfDictionary dic = new PdfDictionary();
/* 228 */     dic.put(PdfName.LANG, new PdfString(lang, "UnicodeBig"));
/* 229 */     if (preferred)
/* 230 */       dic.put(PdfName.PREFERRED, PdfName.ON); 
/* 231 */     usage.put(PdfName.LANGUAGE, dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExport(boolean export) {
/* 242 */     PdfDictionary usage = getUsage();
/* 243 */     PdfDictionary dic = new PdfDictionary();
/* 244 */     dic.put(PdfName.EXPORTSTATE, export ? PdfName.ON : PdfName.OFF);
/* 245 */     usage.put(PdfName.EXPORT, dic);
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
/*     */   public void setZoom(float min, float max) {
/* 258 */     if (min <= 0.0F && max < 0.0F)
/*     */       return; 
/* 260 */     PdfDictionary usage = getUsage();
/* 261 */     PdfDictionary dic = new PdfDictionary();
/* 262 */     if (min > 0.0F)
/* 263 */       dic.put(PdfName.MIN_LOWER_CASE, new PdfNumber(min)); 
/* 264 */     if (max >= 0.0F)
/* 265 */       dic.put(PdfName.MAX_LOWER_CASE, new PdfNumber(max)); 
/* 266 */     usage.put(PdfName.ZOOM, dic);
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
/*     */   public void setPrint(String subtype, boolean printstate) {
/* 278 */     PdfDictionary usage = getUsage();
/* 279 */     PdfDictionary dic = new PdfDictionary();
/* 280 */     dic.put(PdfName.SUBTYPE, new PdfName(subtype));
/* 281 */     dic.put(PdfName.PRINTSTATE, printstate ? PdfName.ON : PdfName.OFF);
/* 282 */     usage.put(PdfName.PRINT, dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setView(boolean view) {
/* 291 */     PdfDictionary usage = getUsage();
/* 292 */     PdfDictionary dic = new PdfDictionary();
/* 293 */     dic.put(PdfName.VIEWSTATE, view ? PdfName.ON : PdfName.OFF);
/* 294 */     usage.put(PdfName.VIEW, dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPageElement(String pe) {
/* 304 */     PdfDictionary usage = getUsage();
/* 305 */     PdfDictionary dic = new PdfDictionary();
/* 306 */     dic.put(PdfName.SUBTYPE, new PdfName(pe));
/* 307 */     usage.put(PdfName.PAGEELEMENT, dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String type, String... names) {
/* 317 */     PdfDictionary usage = getUsage();
/* 318 */     PdfDictionary dic = new PdfDictionary();
/* 319 */     dic.put(PdfName.TYPE, new PdfName(type));
/* 320 */     PdfArray arr = new PdfArray();
/* 321 */     for (String s : names)
/* 322 */       arr.add(new PdfString(s, "UnicodeBig")); 
/* 323 */     usage.put(PdfName.NAME, arr);
/* 324 */     usage.put(PdfName.USER, dic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnPanel() {
/* 332 */     return this.onPanel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnPanel(boolean onPanel) {
/* 342 */     this.onPanel = onPanel;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLayer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */