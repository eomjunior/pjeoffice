/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Rectangle;
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
/*     */ public class PdfFormField
/*     */   extends PdfAnnotation
/*     */ {
/*     */   public static final int FF_READ_ONLY = 1;
/*     */   public static final int FF_REQUIRED = 2;
/*     */   public static final int FF_NO_EXPORT = 4;
/*     */   public static final int FF_NO_TOGGLE_TO_OFF = 16384;
/*     */   public static final int FF_RADIO = 32768;
/*     */   public static final int FF_PUSHBUTTON = 65536;
/*     */   public static final int FF_MULTILINE = 4096;
/*     */   public static final int FF_PASSWORD = 8192;
/*     */   public static final int FF_COMBO = 131072;
/*     */   public static final int FF_EDIT = 262144;
/*     */   public static final int FF_FILESELECT = 1048576;
/*     */   public static final int FF_MULTISELECT = 2097152;
/*     */   public static final int FF_DONOTSPELLCHECK = 4194304;
/*     */   public static final int FF_DONOTSCROLL = 8388608;
/*     */   public static final int FF_COMB = 16777216;
/*     */   public static final int FF_RADIOSINUNISON = 33554432;
/*     */   public static final int FF_RICHTEXT = 33554432;
/*     */   public static final int Q_LEFT = 0;
/*     */   public static final int Q_CENTER = 1;
/*     */   public static final int Q_RIGHT = 2;
/*     */   public static final int MK_NO_ICON = 0;
/*     */   public static final int MK_NO_CAPTION = 1;
/*     */   public static final int MK_CAPTION_BELOW = 2;
/*     */   public static final int MK_CAPTION_ABOVE = 3;
/*     */   public static final int MK_CAPTION_RIGHT = 4;
/*     */   public static final int MK_CAPTION_LEFT = 5;
/*     */   public static final int MK_CAPTION_OVERLAID = 6;
/*  86 */   public static final PdfName IF_SCALE_ALWAYS = PdfName.A;
/*  87 */   public static final PdfName IF_SCALE_BIGGER = PdfName.B;
/*  88 */   public static final PdfName IF_SCALE_SMALLER = PdfName.S;
/*  89 */   public static final PdfName IF_SCALE_NEVER = PdfName.N;
/*  90 */   public static final PdfName IF_SCALE_ANAMORPHIC = PdfName.A;
/*  91 */   public static final PdfName IF_SCALE_PROPORTIONAL = PdfName.P;
/*     */   public static final boolean MULTILINE = true;
/*     */   public static final boolean SINGLELINE = false;
/*     */   public static final boolean PLAINTEXT = false;
/*     */   public static final boolean PASSWORD = true;
/*  96 */   static PdfName[] mergeTarget = new PdfName[] { PdfName.FONT, PdfName.XOBJECT, PdfName.COLORSPACE, PdfName.PATTERN };
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfFormField parent;
/*     */ 
/*     */   
/*     */   protected ArrayList<PdfFormField> kids;
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField(PdfWriter writer, float llx, float lly, float urx, float ury, PdfAction action) {
/* 108 */     super(writer, llx, lly, urx, ury, action);
/* 109 */     put(PdfName.TYPE, PdfName.ANNOT);
/* 110 */     put(PdfName.SUBTYPE, PdfName.WIDGET);
/* 111 */     this.annotation = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PdfFormField(PdfWriter writer) {
/* 116 */     super(writer, (Rectangle)null);
/* 117 */     this.form = true;
/* 118 */     this.annotation = false;
/* 119 */     this.role = PdfName.FORM;
/*     */   }
/*     */   
/*     */   public void setWidget(Rectangle rect, PdfName highlight) {
/* 123 */     put(PdfName.TYPE, PdfName.ANNOT);
/* 124 */     put(PdfName.SUBTYPE, PdfName.WIDGET);
/* 125 */     put(PdfName.RECT, new PdfRectangle(rect));
/* 126 */     this.annotation = true;
/* 127 */     if (highlight != null && !highlight.equals(HIGHLIGHT_INVERT))
/* 128 */       put(PdfName.H, highlight); 
/*     */   }
/*     */   
/*     */   public static PdfFormField createEmpty(PdfWriter writer) {
/* 132 */     PdfFormField field = new PdfFormField(writer);
/* 133 */     return field;
/*     */   }
/*     */   
/*     */   public void setButton(int flags) {
/* 137 */     put(PdfName.FT, PdfName.BTN);
/* 138 */     if (flags != 0)
/* 139 */       put(PdfName.FF, new PdfNumber(flags)); 
/*     */   }
/*     */   
/*     */   protected static PdfFormField createButton(PdfWriter writer, int flags) {
/* 143 */     PdfFormField field = new PdfFormField(writer);
/* 144 */     field.setButton(flags);
/* 145 */     return field;
/*     */   }
/*     */   
/*     */   public static PdfFormField createPushButton(PdfWriter writer) {
/* 149 */     return createButton(writer, 65536);
/*     */   }
/*     */   
/*     */   public static PdfFormField createCheckBox(PdfWriter writer) {
/* 153 */     return createButton(writer, 0);
/*     */   }
/*     */   
/*     */   public static PdfFormField createRadioButton(PdfWriter writer, boolean noToggleToOff) {
/* 157 */     return createButton(writer, 32768 + (noToggleToOff ? 16384 : 0));
/*     */   }
/*     */   
/*     */   public static PdfFormField createTextField(PdfWriter writer, boolean multiline, boolean password, int maxLen) {
/* 161 */     PdfFormField field = new PdfFormField(writer);
/* 162 */     field.put(PdfName.FT, PdfName.TX);
/* 163 */     int flags = multiline ? 4096 : 0;
/* 164 */     flags += password ? 8192 : 0;
/* 165 */     field.put(PdfName.FF, new PdfNumber(flags));
/* 166 */     if (maxLen > 0)
/* 167 */       field.put(PdfName.MAXLEN, new PdfNumber(maxLen)); 
/* 168 */     return field;
/*     */   }
/*     */   
/*     */   protected static PdfFormField createChoice(PdfWriter writer, int flags, PdfArray options, int topIndex) {
/* 172 */     PdfFormField field = new PdfFormField(writer);
/* 173 */     field.put(PdfName.FT, PdfName.CH);
/* 174 */     field.put(PdfName.FF, new PdfNumber(flags));
/* 175 */     field.put(PdfName.OPT, options);
/* 176 */     if (topIndex > 0)
/* 177 */       field.put(PdfName.TI, new PdfNumber(topIndex)); 
/* 178 */     return field;
/*     */   }
/*     */   
/*     */   public static PdfFormField createList(PdfWriter writer, String[] options, int topIndex) {
/* 182 */     return createChoice(writer, 0, processOptions(options), topIndex);
/*     */   }
/*     */   
/*     */   public static PdfFormField createList(PdfWriter writer, String[][] options, int topIndex) {
/* 186 */     return createChoice(writer, 0, processOptions(options), topIndex);
/*     */   }
/*     */   
/*     */   public static PdfFormField createCombo(PdfWriter writer, boolean edit, String[] options, int topIndex) {
/* 190 */     return createChoice(writer, 131072 + (edit ? 262144 : 0), processOptions(options), topIndex);
/*     */   }
/*     */   
/*     */   public static PdfFormField createCombo(PdfWriter writer, boolean edit, String[][] options, int topIndex) {
/* 194 */     return createChoice(writer, 131072 + (edit ? 262144 : 0), processOptions(options), topIndex);
/*     */   }
/*     */   
/*     */   protected static PdfArray processOptions(String[] options) {
/* 198 */     PdfArray array = new PdfArray();
/* 199 */     for (int k = 0; k < options.length; k++) {
/* 200 */       array.add(new PdfString(options[k], "UnicodeBig"));
/*     */     }
/* 202 */     return array;
/*     */   }
/*     */   
/*     */   protected static PdfArray processOptions(String[][] options) {
/* 206 */     PdfArray array = new PdfArray();
/* 207 */     for (int k = 0; k < options.length; k++) {
/* 208 */       String[] subOption = options[k];
/* 209 */       PdfArray ar2 = new PdfArray(new PdfString(subOption[0], "UnicodeBig"));
/* 210 */       ar2.add(new PdfString(subOption[1], "UnicodeBig"));
/* 211 */       array.add(ar2);
/*     */     } 
/* 213 */     return array;
/*     */   }
/*     */   
/*     */   public static PdfFormField createSignature(PdfWriter writer) {
/* 217 */     PdfFormField field = new PdfFormField(writer);
/* 218 */     field.put(PdfName.FT, PdfName.SIG);
/* 219 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getParent() {
/* 226 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void addKid(PdfFormField field) {
/* 230 */     field.parent = this;
/* 231 */     if (this.kids == null)
/* 232 */       this.kids = new ArrayList<PdfFormField>(); 
/* 233 */     this.kids.add(field);
/*     */   }
/*     */   
/*     */   public ArrayList<PdfFormField> getKids() {
/* 237 */     return this.kids;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int setFieldFlags(int flags) {
/*     */     int old;
/* 246 */     PdfNumber obj = (PdfNumber)get(PdfName.FF);
/*     */     
/* 248 */     if (obj == null) {
/* 249 */       old = 0;
/*     */     } else {
/* 251 */       old = obj.intValue();
/* 252 */     }  int v = old | flags;
/* 253 */     put(PdfName.FF, new PdfNumber(v));
/* 254 */     return old;
/*     */   }
/*     */   
/*     */   public void setValueAsString(String s) {
/* 258 */     put(PdfName.V, new PdfString(s, "UnicodeBig"));
/*     */   }
/*     */   
/*     */   public void setValueAsName(String s) {
/* 262 */     put(PdfName.V, new PdfName(s));
/*     */   }
/*     */   
/*     */   public void setValue(PdfSignature sig) {
/* 266 */     put(PdfName.V, sig);
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
/*     */   public void setRichValue(String rv) {
/* 279 */     put(PdfName.RV, new PdfString(rv));
/*     */   }
/*     */   
/*     */   public void setDefaultValueAsString(String s) {
/* 283 */     put(PdfName.DV, new PdfString(s, "UnicodeBig"));
/*     */   }
/*     */   
/*     */   public void setDefaultValueAsName(String s) {
/* 287 */     put(PdfName.DV, new PdfName(s));
/*     */   }
/*     */   
/*     */   public void setFieldName(String s) {
/* 291 */     if (s != null) {
/* 292 */       put(PdfName.T, new PdfString(s, "UnicodeBig"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserName(String s) {
/* 300 */     put(PdfName.TU, new PdfString(s, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappingName(String s) {
/* 308 */     put(PdfName.TM, new PdfString(s, "UnicodeBig"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuadding(int v) {
/* 316 */     put(PdfName.Q, new PdfNumber(v));
/*     */   }
/*     */   
/*     */   static void mergeResources(PdfDictionary result, PdfDictionary source, PdfStamperImp writer) {
/* 320 */     PdfDictionary dic = null;
/* 321 */     PdfDictionary res = null;
/* 322 */     PdfName target = null;
/* 323 */     for (int k = 0; k < mergeTarget.length; k++) {
/* 324 */       target = mergeTarget[k];
/* 325 */       PdfDictionary pdfDict = source.getAsDict(target);
/* 326 */       if ((dic = pdfDict) != null) {
/* 327 */         if ((res = (PdfDictionary)PdfReader.getPdfObject(result.get(target), result)) == null) {
/* 328 */           res = new PdfDictionary();
/*     */         }
/* 330 */         res.mergeDifferent(dic);
/* 331 */         result.put(target, res);
/* 332 */         if (writer != null)
/* 333 */           writer.markUsed(res); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void mergeResources(PdfDictionary result, PdfDictionary source) {
/* 339 */     mergeResources(result, source, (PdfStamperImp)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUsed() {
/* 344 */     this.used = true;
/* 345 */     if (this.parent != null)
/* 346 */       put(PdfName.PARENT, this.parent.getIndirectReference()); 
/* 347 */     if (this.kids != null) {
/* 348 */       PdfArray array = new PdfArray();
/* 349 */       for (int k = 0; k < this.kids.size(); k++)
/* 350 */         array.add(((PdfFormField)this.kids.get(k)).getIndirectReference()); 
/* 351 */       put(PdfName.KIDS, array);
/*     */     } 
/* 353 */     if (this.templates == null)
/*     */       return; 
/* 355 */     PdfDictionary dic = new PdfDictionary();
/* 356 */     for (PdfTemplate template : this.templates) {
/* 357 */       mergeResources(dic, (PdfDictionary)template.getResources());
/*     */     }
/* 359 */     put(PdfName.DR, dic);
/*     */   }
/*     */   
/*     */   public static PdfAnnotation shallowDuplicate(PdfAnnotation annot) {
/*     */     PdfAnnotation dup;
/* 364 */     if (annot.isForm()) {
/* 365 */       dup = new PdfFormField(annot.writer);
/* 366 */       PdfFormField dupField = (PdfFormField)dup;
/* 367 */       PdfFormField srcField = (PdfFormField)annot;
/* 368 */       dupField.parent = srcField.parent;
/* 369 */       dupField.kids = srcField.kids;
/*     */     } else {
/*     */       
/* 372 */       dup = annot.writer.createAnnotation(null, (PdfName)annot.get(PdfName.SUBTYPE));
/* 373 */     }  dup.merge(annot);
/* 374 */     dup.form = annot.form;
/* 375 */     dup.annotation = annot.annotation;
/* 376 */     dup.templates = annot.templates;
/* 377 */     return dup;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfFormField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */