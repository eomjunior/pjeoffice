/*      */ package com.itextpdf.text.pdf;
/*      */ 
/*      */ import com.itextpdf.text.BaseColor;
/*      */ import com.itextpdf.text.DocumentException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.Rectangle;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.io.RASInputStream;
/*      */ import com.itextpdf.text.io.RandomAccessSource;
/*      */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*      */ import com.itextpdf.text.io.WindowRandomAccessSource;
/*      */ import com.itextpdf.text.pdf.codec.Base64;
/*      */ import com.itextpdf.text.pdf.security.PdfPKCS7;
/*      */ import com.itextpdf.text.xml.XmlToTxt;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.w3c.dom.Node;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AcroFields
/*      */ {
/*      */   public static final int DA_FONT = 0;
/*      */   public static final int DA_SIZE = 1;
/*      */   public static final int DA_COLOR = 2;
/*      */   public static final int FIELD_TYPE_NONE = 0;
/*      */   public static final int FIELD_TYPE_PUSHBUTTON = 1;
/*      */   public static final int FIELD_TYPE_CHECKBOX = 2;
/*      */   public static final int FIELD_TYPE_RADIOBUTTON = 3;
/*      */   public static final int FIELD_TYPE_TEXT = 4;
/*      */   public static final int FIELD_TYPE_LIST = 5;
/*      */   public static final int FIELD_TYPE_COMBO = 6;
/*      */   public static final int FIELD_TYPE_SIGNATURE = 7;
/*  119 */   private static final HashMap<String, String[]> stdFieldFontNames = (HashMap)new HashMap<String, String>();
/*  120 */   private static final PdfName[] buttonRemove = new PdfName[] { PdfName.MK, PdfName.F, PdfName.FF, PdfName.Q, PdfName.BS, PdfName.BORDER }; PdfReader reader;
/*      */   
/*      */   static {
/*  123 */     stdFieldFontNames.put("CoBO", new String[] { "Courier-BoldOblique" });
/*  124 */     stdFieldFontNames.put("CoBo", new String[] { "Courier-Bold" });
/*  125 */     stdFieldFontNames.put("CoOb", new String[] { "Courier-Oblique" });
/*  126 */     stdFieldFontNames.put("Cour", new String[] { "Courier" });
/*  127 */     stdFieldFontNames.put("HeBO", new String[] { "Helvetica-BoldOblique" });
/*  128 */     stdFieldFontNames.put("HeBo", new String[] { "Helvetica-Bold" });
/*  129 */     stdFieldFontNames.put("HeOb", new String[] { "Helvetica-Oblique" });
/*  130 */     stdFieldFontNames.put("Helv", new String[] { "Helvetica" });
/*  131 */     stdFieldFontNames.put("Symb", new String[] { "Symbol" });
/*  132 */     stdFieldFontNames.put("TiBI", new String[] { "Times-BoldItalic" });
/*  133 */     stdFieldFontNames.put("TiBo", new String[] { "Times-Bold" });
/*  134 */     stdFieldFontNames.put("TiIt", new String[] { "Times-Italic" });
/*  135 */     stdFieldFontNames.put("TiRo", new String[] { "Times-Roman" });
/*  136 */     stdFieldFontNames.put("ZaDb", new String[] { "ZapfDingbats" });
/*  137 */     stdFieldFontNames.put("HySm", new String[] { "HYSMyeongJo-Medium", "UniKS-UCS2-H" });
/*  138 */     stdFieldFontNames.put("HyGo", new String[] { "HYGoThic-Medium", "UniKS-UCS2-H" });
/*  139 */     stdFieldFontNames.put("KaGo", new String[] { "HeiseiKakuGo-W5", "UniKS-UCS2-H" });
/*  140 */     stdFieldFontNames.put("KaMi", new String[] { "HeiseiMin-W3", "UniJIS-UCS2-H" });
/*  141 */     stdFieldFontNames.put("MHei", new String[] { "MHei-Medium", "UniCNS-UCS2-H" });
/*  142 */     stdFieldFontNames.put("MSun", new String[] { "MSung-Light", "UniCNS-UCS2-H" });
/*  143 */     stdFieldFontNames.put("STSo", new String[] { "STSong-Light", "UniGB-UCS2-H" });
/*      */   }
/*      */ 
/*      */   
/*      */   PdfWriter writer;
/*      */   Map<String, Item> fields;
/*      */   private int topFirst;
/*      */   private HashMap<String, int[]> sigNames;
/*      */   private boolean append;
/*  152 */   private HashMap<Integer, BaseFont> extensionFonts = new HashMap<Integer, BaseFont>();
/*      */   
/*      */   private XfaForm xfa;
/*      */   
/*      */   private boolean lastWasString;
/*      */   
/*      */   private boolean generateAppearances = true;
/*  159 */   private HashMap<String, BaseFont> localFonts = new HashMap<String, BaseFont>();
/*      */ 
/*      */   
/*      */   private float extraMarginLeft;
/*      */   
/*      */   private float extraMarginTop;
/*      */   
/*      */   private ArrayList<BaseFont> substitutionFonts;
/*      */   
/*      */   private ArrayList<String> orderedSignatureNames;
/*      */   
/*      */   private int totalRevisions;
/*      */   
/*      */   private Map<String, TextField> fieldCache;
/*      */ 
/*      */   
/*      */   public static Object[] splitDAelements(String da) {
/*      */     try {
/*  177 */       PRTokeniser tk = new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(PdfEncodings.convertToBytes(da, (String)null))));
/*  178 */       ArrayList<String> stack = new ArrayList<String>();
/*  179 */       Object[] ret = new Object[3];
/*  180 */       while (tk.nextToken()) {
/*  181 */         if (tk.getTokenType() == PRTokeniser.TokenType.COMMENT)
/*      */           continue; 
/*  183 */         if (tk.getTokenType() == PRTokeniser.TokenType.OTHER) {
/*  184 */           String operator = tk.getStringValue();
/*  185 */           if (operator.equals("Tf")) {
/*  186 */             if (stack.size() >= 2) {
/*  187 */               ret[0] = stack.get(stack.size() - 2);
/*  188 */               ret[1] = new Float(stack.get(stack.size() - 1));
/*      */             } 
/*  190 */           } else if (operator.equals("g")) {
/*  191 */             if (stack.size() >= 1) {
/*  192 */               float gray = (new Float(stack.get(stack.size() - 1))).floatValue();
/*  193 */               if (gray != 0.0F)
/*  194 */                 ret[2] = new GrayColor(gray); 
/*      */             } 
/*  196 */           } else if (operator.equals("rg")) {
/*  197 */             if (stack.size() >= 3) {
/*  198 */               float red = (new Float(stack.get(stack.size() - 3))).floatValue();
/*  199 */               float green = (new Float(stack.get(stack.size() - 2))).floatValue();
/*  200 */               float blue = (new Float(stack.get(stack.size() - 1))).floatValue();
/*  201 */               ret[2] = new BaseColor(red, green, blue);
/*      */             } 
/*  203 */           } else if (operator.equals("k") && 
/*  204 */             stack.size() >= 4) {
/*  205 */             float cyan = (new Float(stack.get(stack.size() - 4))).floatValue();
/*  206 */             float magenta = (new Float(stack.get(stack.size() - 3))).floatValue();
/*  207 */             float yellow = (new Float(stack.get(stack.size() - 2))).floatValue();
/*  208 */             float black = (new Float(stack.get(stack.size() - 1))).floatValue();
/*  209 */             ret[2] = new CMYKColor(cyan, magenta, yellow, black);
/*      */           } 
/*      */           
/*  212 */           stack.clear(); continue;
/*      */         } 
/*  214 */         stack.add(tk.getStringValue());
/*      */       } 
/*  216 */       return ret;
/*  217 */     } catch (IOException ioe) {
/*  218 */       throw new ExceptionConverter(ioe);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void clearSigDic(PdfDictionary dic) {
/*  223 */     dic.remove(PdfName.AP);
/*  224 */     dic.remove(PdfName.AS);
/*  225 */     dic.remove(PdfName.V);
/*  226 */     dic.remove(PdfName.DV);
/*  227 */     dic.remove(PdfName.SV);
/*  228 */     dic.remove(PdfName.FF);
/*  229 */     dic.put(PdfName.F, new PdfNumber(4));
/*      */   }
/*      */   
/*      */   AcroFields(PdfReader reader, PdfWriter writer) {
/*  233 */     this.reader = reader;
/*  234 */     this.writer = writer;
/*      */     try {
/*  236 */       this.xfa = new XfaForm(reader);
/*  237 */     } catch (Exception e) {
/*  238 */       throw new ExceptionConverter(e);
/*      */     } 
/*  240 */     if (writer instanceof PdfStamperImp) {
/*  241 */       this.append = ((PdfStamperImp)writer).isAppend();
/*      */     }
/*  243 */     fill();
/*      */   }
/*      */   
/*      */   void fill() {
/*  247 */     this.fields = new LinkedHashMap<String, Item>();
/*  248 */     PdfDictionary top = (PdfDictionary)PdfReader.getPdfObjectRelease(this.reader.getCatalog().get(PdfName.ACROFORM));
/*  249 */     if (top == null)
/*      */       return; 
/*  251 */     PdfBoolean needappearances = top.getAsBoolean(PdfName.NEEDAPPEARANCES);
/*  252 */     if (needappearances == null || !needappearances.booleanValue()) {
/*  253 */       setGenerateAppearances(true);
/*      */     } else {
/*  255 */       setGenerateAppearances(false);
/*  256 */     }  PdfArray arrfds = (PdfArray)PdfReader.getPdfObjectRelease(top.get(PdfName.FIELDS));
/*  257 */     if (arrfds == null || arrfds.size() == 0)
/*      */       return; 
/*  259 */     for (int k = 1; k <= this.reader.getNumberOfPages(); k++) {
/*  260 */       PdfDictionary page = this.reader.getPageNRelease(k);
/*  261 */       PdfArray annots = (PdfArray)PdfReader.getPdfObjectRelease(page.get(PdfName.ANNOTS), page);
/*  262 */       if (annots != null)
/*      */       {
/*  264 */         for (int i = 0; i < annots.size(); i++) {
/*  265 */           PdfDictionary annot = annots.getAsDict(i);
/*  266 */           if (annot == null) {
/*  267 */             PdfReader.releaseLastXrefPartial(annots.getAsIndirectObject(i));
/*      */           
/*      */           }
/*  270 */           else if (!PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE))) {
/*  271 */             PdfReader.releaseLastXrefPartial(annots.getAsIndirectObject(i));
/*      */           } else {
/*      */             
/*  274 */             PdfDictionary widget = annot;
/*  275 */             PdfDictionary dic = new PdfDictionary();
/*  276 */             dic.putAll(annot);
/*  277 */             String name = "";
/*  278 */             PdfDictionary value = null;
/*  279 */             PdfObject lastV = null;
/*  280 */             while (annot != null) {
/*  281 */               dic.mergeDifferent(annot);
/*  282 */               PdfString t = annot.getAsString(PdfName.T);
/*  283 */               if (t != null)
/*  284 */                 name = t.toUnicodeString() + "." + name; 
/*  285 */               if (lastV == null && annot.get(PdfName.V) != null)
/*  286 */                 lastV = PdfReader.getPdfObjectRelease(annot.get(PdfName.V)); 
/*  287 */               if (value == null && t != null) {
/*  288 */                 value = annot;
/*  289 */                 if (annot.get(PdfName.V) == null && lastV != null)
/*  290 */                   value.put(PdfName.V, lastV); 
/*      */               } 
/*  292 */               annot = annot.getAsDict(PdfName.PARENT);
/*      */             } 
/*  294 */             if (name.length() > 0)
/*  295 */               name = name.substring(0, name.length() - 1); 
/*  296 */             Item item = this.fields.get(name);
/*  297 */             if (item == null) {
/*  298 */               item = new Item();
/*  299 */               this.fields.put(name, item);
/*      */             } 
/*  301 */             if (value == null) {
/*  302 */               item.addValue(widget);
/*      */             } else {
/*  304 */               item.addValue(value);
/*  305 */             }  item.addWidget(widget);
/*  306 */             item.addWidgetRef(annots.getAsIndirectObject(i));
/*  307 */             if (top != null)
/*  308 */               dic.mergeDifferent(top); 
/*  309 */             item.addMerged(dic);
/*  310 */             item.addPage(k);
/*  311 */             item.addTabOrder(i);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*  316 */     PdfNumber sigFlags = top.getAsNumber(PdfName.SIGFLAGS);
/*  317 */     if (sigFlags == null || (sigFlags.intValue() & 0x1) != 1)
/*      */       return; 
/*  319 */     for (int j = 0; j < arrfds.size(); j++) {
/*  320 */       PdfDictionary annot = arrfds.getAsDict(j);
/*  321 */       if (annot == null) {
/*  322 */         PdfReader.releaseLastXrefPartial(arrfds.getAsIndirectObject(j));
/*      */       
/*      */       }
/*  325 */       else if (!PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE))) {
/*  326 */         PdfReader.releaseLastXrefPartial(arrfds.getAsIndirectObject(j));
/*      */       } else {
/*      */         
/*  329 */         PdfArray kids = (PdfArray)PdfReader.getPdfObjectRelease(annot.get(PdfName.KIDS));
/*  330 */         if (kids == null) {
/*      */           
/*  332 */           PdfDictionary dic = new PdfDictionary();
/*  333 */           dic.putAll(annot);
/*  334 */           PdfString t = annot.getAsString(PdfName.T);
/*  335 */           if (t != null) {
/*      */             
/*  337 */             String name = t.toUnicodeString();
/*  338 */             if (!this.fields.containsKey(name)) {
/*      */               
/*  340 */               Item item = new Item();
/*  341 */               this.fields.put(name, item);
/*  342 */               item.addValue(dic);
/*  343 */               item.addWidget(dic);
/*  344 */               item.addWidgetRef(arrfds.getAsIndirectObject(j));
/*  345 */               item.addMerged(dic);
/*  346 */               item.addPage(-1);
/*  347 */               item.addTabOrder(-1);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
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
/*      */   public String[] getAppearanceStates(String fieldName) {
/*  364 */     Item fd = this.fields.get(fieldName);
/*  365 */     if (fd == null)
/*  366 */       return null; 
/*  367 */     HashSet<String> names = new LinkedHashSet<String>();
/*  368 */     PdfDictionary vals = fd.getValue(0);
/*  369 */     PdfString stringOpt = vals.getAsString(PdfName.OPT);
/*      */ 
/*      */     
/*  372 */     if (stringOpt != null) {
/*  373 */       names.add(stringOpt.toUnicodeString());
/*      */     } else {
/*  375 */       PdfArray arrayOpt = vals.getAsArray(PdfName.OPT);
/*  376 */       if (arrayOpt != null)
/*  377 */         for (int i = 0; i < arrayOpt.size(); i++) {
/*  378 */           PdfArray pdfArray; PdfObject pdfObject = arrayOpt.getDirectObject(i);
/*  379 */           PdfString valStr = null;
/*      */           
/*  381 */           switch (pdfObject.type()) {
/*      */             case 5:
/*  383 */               pdfArray = (PdfArray)pdfObject;
/*  384 */               valStr = pdfArray.getAsString(1);
/*      */               break;
/*      */             case 3:
/*  387 */               valStr = (PdfString)pdfObject;
/*      */               break;
/*      */           } 
/*      */           
/*  391 */           if (valStr != null) {
/*  392 */             names.add(valStr.toUnicodeString());
/*      */           }
/*      */         }  
/*      */     } 
/*  396 */     for (int k = 0; k < fd.size(); k++) {
/*  397 */       PdfDictionary dic = fd.getWidget(k);
/*  398 */       dic = dic.getAsDict(PdfName.AP);
/*  399 */       if (dic != null) {
/*      */         
/*  401 */         dic = dic.getAsDict(PdfName.N);
/*  402 */         if (dic != null)
/*      */         {
/*  404 */           for (PdfName element : dic.getKeys()) {
/*  405 */             String name = PdfName.decodeName(((PdfName)element).toString());
/*  406 */             names.add(name);
/*      */           }  } 
/*      */       } 
/*  409 */     }  String[] out = new String[names.size()];
/*  410 */     return (String[])names.toArray((Object[])out);
/*      */   }
/*      */   
/*      */   private String[] getListOption(String fieldName, int idx) {
/*  414 */     Item fd = getFieldItem(fieldName);
/*  415 */     if (fd == null)
/*  416 */       return null; 
/*  417 */     PdfArray ar = fd.getMerged(0).getAsArray(PdfName.OPT);
/*  418 */     if (ar == null)
/*  419 */       return null; 
/*  420 */     String[] ret = new String[ar.size()];
/*  421 */     for (int k = 0; k < ar.size(); k++) {
/*  422 */       PdfObject obj = ar.getDirectObject(k);
/*      */       try {
/*  424 */         if (obj.isArray()) {
/*  425 */           obj = ((PdfArray)obj).getDirectObject(idx);
/*      */         }
/*  427 */         if (obj.isString())
/*  428 */         { ret[k] = ((PdfString)obj).toUnicodeString(); }
/*      */         else
/*  430 */         { ret[k] = obj.toString(); } 
/*  431 */       } catch (Exception e) {
/*  432 */         ret[k] = "";
/*      */       } 
/*      */     } 
/*  435 */     return ret;
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
/*      */   public String[] getListOptionExport(String fieldName) {
/*  447 */     return getListOption(fieldName, 0);
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
/*      */   public String[] getListOptionDisplay(String fieldName) {
/*  459 */     return getListOption(fieldName, 1);
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
/*      */   public boolean setListOption(String fieldName, String[] exportValues, String[] displayValues) {
/*  485 */     if (exportValues == null && displayValues == null)
/*  486 */       return false; 
/*  487 */     if (exportValues != null && displayValues != null && exportValues.length != displayValues.length)
/*  488 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.export.and.the.display.array.must.have.the.same.size", new Object[0])); 
/*  489 */     int ftype = getFieldType(fieldName);
/*  490 */     if (ftype != 6 && ftype != 5)
/*  491 */       return false; 
/*  492 */     Item fd = this.fields.get(fieldName);
/*  493 */     String[] sing = null;
/*  494 */     if (exportValues == null && displayValues != null) {
/*  495 */       sing = displayValues;
/*  496 */     } else if (exportValues != null && displayValues == null) {
/*  497 */       sing = exportValues;
/*  498 */     }  PdfArray opt = new PdfArray();
/*  499 */     if (sing != null) {
/*  500 */       for (int k = 0; k < sing.length; k++)
/*  501 */         opt.add(new PdfString(sing[k], "UnicodeBig")); 
/*      */     } else {
/*  503 */       for (int k = 0; k < exportValues.length; k++) {
/*  504 */         PdfArray a = new PdfArray();
/*  505 */         a.add(new PdfString(exportValues[k], "UnicodeBig"));
/*  506 */         a.add(new PdfString(displayValues[k], "UnicodeBig"));
/*  507 */         opt.add(a);
/*      */       } 
/*      */     } 
/*  510 */     fd.writeToAll(PdfName.OPT, opt, 5);
/*  511 */     return true;
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
/*      */   public int getFieldType(String fieldName) {
/*  527 */     Item fd = getFieldItem(fieldName);
/*  528 */     if (fd == null)
/*  529 */       return 0; 
/*  530 */     PdfDictionary merged = fd.getMerged(0);
/*  531 */     PdfName type = merged.getAsName(PdfName.FT);
/*  532 */     if (type == null)
/*  533 */       return 0; 
/*  534 */     int ff = 0;
/*  535 */     PdfNumber ffo = merged.getAsNumber(PdfName.FF);
/*  536 */     if (ffo != null) {
/*  537 */       ff = ffo.intValue();
/*      */     }
/*  539 */     if (PdfName.BTN.equals(type)) {
/*  540 */       if ((ff & 0x10000) != 0)
/*  541 */         return 1; 
/*  542 */       if ((ff & 0x8000) != 0) {
/*  543 */         return 3;
/*      */       }
/*  545 */       return 2;
/*  546 */     }  if (PdfName.TX.equals(type))
/*  547 */       return 4; 
/*  548 */     if (PdfName.CH.equals(type)) {
/*  549 */       if ((ff & 0x20000) != 0) {
/*  550 */         return 6;
/*      */       }
/*  552 */       return 5;
/*  553 */     }  if (PdfName.SIG.equals(type)) {
/*  554 */       return 7;
/*      */     }
/*  556 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void exportAsFdf(FdfWriter writer) {
/*  565 */     for (Map.Entry<String, Item> entry : this.fields.entrySet()) {
/*  566 */       Item item = entry.getValue();
/*  567 */       String name = entry.getKey();
/*  568 */       PdfObject v = item.getMerged(0).get(PdfName.V);
/*  569 */       if (v == null)
/*      */         continue; 
/*  571 */       String value = getField(name);
/*  572 */       if (this.lastWasString) {
/*  573 */         writer.setFieldAsString(name, value); continue;
/*      */       } 
/*  575 */       writer.setFieldAsName(name, value);
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
/*      */   public boolean renameField(String oldName, String newName) {
/*  589 */     int idx1 = oldName.lastIndexOf('.') + 1;
/*  590 */     int idx2 = newName.lastIndexOf('.') + 1;
/*  591 */     if (idx1 != idx2)
/*  592 */       return false; 
/*  593 */     if (!oldName.substring(0, idx1).equals(newName.substring(0, idx2)))
/*  594 */       return false; 
/*  595 */     if (this.fields.containsKey(newName))
/*  596 */       return false; 
/*  597 */     Item item = this.fields.get(oldName);
/*  598 */     if (item == null)
/*  599 */       return false; 
/*  600 */     newName = newName.substring(idx2);
/*  601 */     PdfString ss = new PdfString(newName, "UnicodeBig");
/*      */     
/*  603 */     item.writeToAll(PdfName.T, ss, 5);
/*  604 */     item.markUsed(this, 4);
/*      */     
/*  606 */     this.fields.remove(oldName);
/*  607 */     this.fields.put(newName, item);
/*      */     
/*  609 */     return true;
/*      */   }
/*      */   
/*      */   public void decodeGenericDictionary(PdfDictionary merged, BaseField tx) throws IOException, DocumentException {
/*  613 */     int flags = 0;
/*      */     
/*  615 */     PdfString da = merged.getAsString(PdfName.DA);
/*  616 */     if (da != null) {
/*  617 */       boolean fontfallback = false;
/*  618 */       Object[] dab = splitDAelements(da.toUnicodeString());
/*  619 */       if (dab[1] != null)
/*  620 */         tx.setFontSize(((Float)dab[1]).floatValue()); 
/*  621 */       if (dab[2] != null)
/*  622 */         tx.setTextColor((BaseColor)dab[2]); 
/*  623 */       if (dab[0] != null) {
/*  624 */         PdfDictionary dr = merged.getAsDict(PdfName.DR);
/*  625 */         if (dr != null) {
/*  626 */           PdfDictionary font = dr.getAsDict(PdfName.FONT);
/*  627 */           if (font != null) {
/*  628 */             PdfObject po = font.get(new PdfName((String)dab[0]));
/*  629 */             if (po != null && po.type() == 10) {
/*  630 */               PRIndirectReference por = (PRIndirectReference)po;
/*  631 */               BaseFont bp = new DocumentFont((PRIndirectReference)po, dr.getAsDict(PdfName.ENCODING));
/*  632 */               tx.setFont(bp);
/*  633 */               Integer porkey = Integer.valueOf(por.getNumber());
/*  634 */               BaseFont porf = this.extensionFonts.get(porkey);
/*  635 */               if (porf == null && 
/*  636 */                 !this.extensionFonts.containsKey(porkey)) {
/*  637 */                 PdfDictionary fo = (PdfDictionary)PdfReader.getPdfObject(po);
/*  638 */                 PdfDictionary fd = fo.getAsDict(PdfName.FONTDESCRIPTOR);
/*  639 */                 if (fd != null) {
/*  640 */                   PRStream prs = (PRStream)PdfReader.getPdfObject(fd.get(PdfName.FONTFILE2));
/*  641 */                   if (prs == null)
/*  642 */                     prs = (PRStream)PdfReader.getPdfObject(fd.get(PdfName.FONTFILE3)); 
/*  643 */                   if (prs == null) {
/*  644 */                     this.extensionFonts.put(porkey, null);
/*      */                   } else {
/*      */                     try {
/*  647 */                       porf = BaseFont.createFont("font.ttf", "Identity-H", true, false, PdfReader.getStreamBytes(prs), null);
/*  648 */                     } catch (Exception exception) {}
/*      */                     
/*  650 */                     this.extensionFonts.put(porkey, porf);
/*      */                   } 
/*      */                 } 
/*      */               } 
/*      */               
/*  655 */               if (tx instanceof TextField)
/*  656 */                 ((TextField)tx).setExtensionFont(porf); 
/*      */             } else {
/*  658 */               fontfallback = true;
/*      */             } 
/*      */           } else {
/*      */             
/*  662 */             fontfallback = true;
/*      */           } 
/*      */         } else {
/*  665 */           fontfallback = true;
/*      */         } 
/*      */       } 
/*  668 */       if (fontfallback) {
/*  669 */         BaseFont bf = this.localFonts.get(dab[0]);
/*  670 */         if (bf == null) {
/*  671 */           String[] fn = stdFieldFontNames.get(dab[0]);
/*  672 */           if (fn != null) {
/*      */             try {
/*  674 */               String enc = "winansi";
/*  675 */               if (fn.length > 1)
/*  676 */                 enc = fn[1]; 
/*  677 */               bf = BaseFont.createFont(fn[0], enc, false);
/*  678 */               tx.setFont(bf);
/*  679 */             } catch (Exception exception) {}
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/*  684 */           tx.setFont(bf);
/*      */         } 
/*      */       } 
/*      */     } 
/*  688 */     PdfDictionary mk = merged.getAsDict(PdfName.MK);
/*  689 */     if (mk != null) {
/*  690 */       PdfArray ar = mk.getAsArray(PdfName.BC);
/*  691 */       BaseColor border = getMKColor(ar);
/*  692 */       tx.setBorderColor(border);
/*  693 */       if (border != null)
/*  694 */         tx.setBorderWidth(1.0F); 
/*  695 */       ar = mk.getAsArray(PdfName.BG);
/*  696 */       tx.setBackgroundColor(getMKColor(ar));
/*  697 */       PdfNumber rotation = mk.getAsNumber(PdfName.R);
/*  698 */       if (rotation != null) {
/*  699 */         tx.setRotation(rotation.intValue());
/*      */       }
/*      */     } 
/*  702 */     PdfNumber nfl = merged.getAsNumber(PdfName.F);
/*  703 */     flags = 0;
/*  704 */     tx.setVisibility(2);
/*  705 */     if (nfl != null) {
/*  706 */       flags = nfl.intValue();
/*  707 */       if ((flags & 0x4) != 0 && (flags & 0x2) != 0) {
/*  708 */         tx.setVisibility(1);
/*  709 */       } else if ((flags & 0x4) != 0 && (flags & 0x20) != 0) {
/*  710 */         tx.setVisibility(3);
/*  711 */       } else if ((flags & 0x4) != 0) {
/*  712 */         tx.setVisibility(0);
/*      */       } 
/*      */     } 
/*  715 */     nfl = merged.getAsNumber(PdfName.FF);
/*  716 */     flags = 0;
/*  717 */     if (nfl != null)
/*  718 */       flags = nfl.intValue(); 
/*  719 */     tx.setOptions(flags);
/*  720 */     if ((flags & 0x1000000) != 0) {
/*  721 */       PdfNumber maxLen = merged.getAsNumber(PdfName.MAXLEN);
/*  722 */       int len = 0;
/*  723 */       if (maxLen != null)
/*  724 */         len = maxLen.intValue(); 
/*  725 */       tx.setMaxCharacterLength(len);
/*      */     } 
/*      */     
/*  728 */     nfl = merged.getAsNumber(PdfName.Q);
/*  729 */     if (nfl != null) {
/*  730 */       if (nfl.intValue() == 1) {
/*  731 */         tx.setAlignment(1);
/*  732 */       } else if (nfl.intValue() == 2) {
/*  733 */         tx.setAlignment(2);
/*      */       } 
/*      */     }
/*  736 */     PdfDictionary bs = merged.getAsDict(PdfName.BS);
/*  737 */     if (bs != null)
/*  738 */     { PdfNumber w = bs.getAsNumber(PdfName.W);
/*  739 */       if (w != null)
/*  740 */         tx.setBorderWidth(w.floatValue()); 
/*  741 */       PdfName s = bs.getAsName(PdfName.S);
/*  742 */       if (PdfName.D.equals(s)) {
/*  743 */         tx.setBorderStyle(1);
/*  744 */       } else if (PdfName.B.equals(s)) {
/*  745 */         tx.setBorderStyle(2);
/*  746 */       } else if (PdfName.I.equals(s)) {
/*  747 */         tx.setBorderStyle(3);
/*  748 */       } else if (PdfName.U.equals(s)) {
/*  749 */         tx.setBorderStyle(4);
/*      */       }  }
/*  751 */     else { PdfArray bd = merged.getAsArray(PdfName.BORDER);
/*  752 */       if (bd != null) {
/*  753 */         if (bd.size() >= 3)
/*  754 */           tx.setBorderWidth(bd.getAsNumber(2).floatValue()); 
/*  755 */         if (bd.size() >= 4)
/*  756 */           tx.setBorderStyle(1); 
/*      */       }  }
/*      */   
/*      */   }
/*      */   
/*      */   PdfAppearance getAppearance(PdfDictionary merged, String[] values, String fieldName) throws IOException, DocumentException {
/*  762 */     PdfName fieldType = merged.getAsName(PdfName.FT);
/*      */     
/*  764 */     if (PdfName.BTN.equals(fieldType)) {
/*  765 */       PdfNumber fieldFlags = merged.getAsNumber(PdfName.FF);
/*  766 */       boolean isRadio = (fieldFlags != null && (fieldFlags.intValue() & 0x8000) != 0);
/*  767 */       RadioCheckField field = new RadioCheckField(this.writer, null, null, null);
/*  768 */       decodeGenericDictionary(merged, field);
/*      */       
/*  770 */       PdfArray rect = merged.getAsArray(PdfName.RECT);
/*  771 */       Rectangle box = PdfReader.getNormalizedRectangle(rect);
/*  772 */       if (field.getRotation() == 90 || field.getRotation() == 270)
/*  773 */         box = box.rotate(); 
/*  774 */       field.setBox(box);
/*  775 */       if (!isRadio)
/*  776 */         field.setCheckType(3); 
/*  777 */       return field.getAppearance(isRadio, !merged.getAsName(PdfName.AS).equals(PdfName.Off));
/*      */     } 
/*      */     
/*  780 */     this.topFirst = 0;
/*  781 */     String text = (values.length > 0) ? values[0] : null;
/*      */     
/*  783 */     TextField tx = null;
/*  784 */     if (this.fieldCache == null || !this.fieldCache.containsKey(fieldName)) {
/*  785 */       tx = new TextField(this.writer, null, null);
/*  786 */       tx.setExtraMargin(this.extraMarginLeft, this.extraMarginTop);
/*  787 */       tx.setBorderWidth(0.0F);
/*  788 */       tx.setSubstitutionFonts(this.substitutionFonts);
/*  789 */       decodeGenericDictionary(merged, tx);
/*      */       
/*  791 */       PdfArray rect = merged.getAsArray(PdfName.RECT);
/*  792 */       Rectangle box = PdfReader.getNormalizedRectangle(rect);
/*  793 */       if (tx.getRotation() == 90 || tx.getRotation() == 270)
/*  794 */         box = box.rotate(); 
/*  795 */       tx.setBox(box);
/*  796 */       if (this.fieldCache != null)
/*  797 */         this.fieldCache.put(fieldName, tx); 
/*      */     } else {
/*  799 */       tx = this.fieldCache.get(fieldName);
/*  800 */       tx.setWriter(this.writer);
/*      */     } 
/*  802 */     if (PdfName.TX.equals(fieldType)) {
/*  803 */       if (values.length > 0 && values[0] != null) {
/*  804 */         tx.setText(values[0]);
/*      */       }
/*  806 */       return tx.getAppearance();
/*      */     } 
/*  808 */     if (!PdfName.CH.equals(fieldType))
/*  809 */       throw new DocumentException(MessageLocalization.getComposedMessage("an.appearance.was.requested.without.a.variable.text.field", new Object[0])); 
/*  810 */     PdfArray opt = merged.getAsArray(PdfName.OPT);
/*  811 */     int flags = 0;
/*  812 */     PdfNumber nfl = merged.getAsNumber(PdfName.FF);
/*  813 */     if (nfl != null)
/*  814 */       flags = nfl.intValue(); 
/*  815 */     if ((flags & 0x20000) != 0 && opt == null) {
/*  816 */       tx.setText(text);
/*  817 */       return tx.getAppearance();
/*      */     } 
/*  819 */     if (opt != null) {
/*  820 */       String[] choices = new String[opt.size()];
/*  821 */       String[] choicesExp = new String[opt.size()]; int k;
/*  822 */       for (k = 0; k < opt.size(); k++) {
/*  823 */         PdfObject obj = opt.getPdfObject(k);
/*  824 */         if (obj.isString()) {
/*  825 */           choicesExp[k] = ((PdfString)obj).toUnicodeString(); choices[k] = ((PdfString)obj).toUnicodeString();
/*      */         } else {
/*  827 */           PdfArray a = (PdfArray)obj;
/*  828 */           choicesExp[k] = a.getAsString(0).toUnicodeString();
/*  829 */           choices[k] = a.getAsString(1).toUnicodeString();
/*      */         } 
/*      */       } 
/*  832 */       if ((flags & 0x20000) != 0) {
/*  833 */         for (k = 0; k < choices.length; k++) {
/*  834 */           if (text.equals(choicesExp[k])) {
/*  835 */             text = choices[k];
/*      */             break;
/*      */           } 
/*      */         } 
/*  839 */         tx.setText(text);
/*  840 */         return tx.getAppearance();
/*      */       } 
/*  842 */       ArrayList<Integer> indexes = new ArrayList<Integer>();
/*  843 */       for (int i = 0; i < choicesExp.length; i++) {
/*  844 */         for (int j = 0; j < values.length; j++) {
/*  845 */           String val = values[j];
/*  846 */           if (val != null && val.equals(choicesExp[i])) {
/*  847 */             indexes.add(Integer.valueOf(i));
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*  852 */       tx.setChoices(choices);
/*  853 */       tx.setChoiceExports(choicesExp);
/*  854 */       tx.setChoiceSelections(indexes);
/*      */     } 
/*  856 */     PdfAppearance app = tx.getListAppearance();
/*  857 */     this.topFirst = tx.getTopFirst();
/*  858 */     return app;
/*      */   }
/*      */   
/*      */   PdfAppearance getAppearance(PdfDictionary merged, String text, String fieldName) throws IOException, DocumentException {
/*  862 */     String[] valueArr = new String[1];
/*  863 */     valueArr[0] = text;
/*  864 */     return getAppearance(merged, valueArr, fieldName);
/*      */   }
/*      */   
/*      */   BaseColor getMKColor(PdfArray ar) {
/*  868 */     if (ar == null)
/*  869 */       return null; 
/*  870 */     switch (ar.size()) {
/*      */       case 1:
/*  872 */         return new GrayColor(ar.getAsNumber(0).floatValue());
/*      */       case 3:
/*  874 */         return new BaseColor(ExtendedColor.normalize(ar.getAsNumber(0).floatValue()), ExtendedColor.normalize(ar.getAsNumber(1).floatValue()), ExtendedColor.normalize(ar.getAsNumber(2).floatValue()));
/*      */       case 4:
/*  876 */         return new CMYKColor(ar.getAsNumber(0).floatValue(), ar.getAsNumber(1).floatValue(), ar.getAsNumber(2).floatValue(), ar.getAsNumber(3).floatValue());
/*      */     } 
/*  878 */     return null;
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
/*      */   public String getFieldRichValue(String name) {
/*  890 */     if (this.xfa.isXfaPresent()) {
/*  891 */       return null;
/*      */     }
/*      */     
/*  894 */     Item item = this.fields.get(name);
/*  895 */     if (item == null) {
/*  896 */       return null;
/*      */     }
/*      */     
/*  899 */     PdfDictionary merged = item.getMerged(0);
/*  900 */     PdfString rich = merged.getAsString(PdfName.RV);
/*      */     
/*  902 */     String markup = null;
/*  903 */     if (rich != null) {
/*  904 */       markup = rich.toString();
/*      */     }
/*      */     
/*  907 */     return markup;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getField(String name) {
/*  917 */     if (this.xfa.isXfaPresent()) {
/*  918 */       name = this.xfa.findFieldName(name, this);
/*  919 */       if (name == null)
/*  920 */         return null; 
/*  921 */       name = XfaForm.Xml2Som.getShortName(name);
/*  922 */       return XfaForm.getNodeText(this.xfa.findDatasetsNode(name));
/*      */     } 
/*  924 */     Item item = this.fields.get(name);
/*  925 */     if (item == null)
/*  926 */       return null; 
/*  927 */     this.lastWasString = false;
/*  928 */     PdfDictionary mergedDict = item.getMerged(0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  933 */     PdfObject v = PdfReader.getPdfObject(mergedDict.get(PdfName.V));
/*  934 */     if (v == null)
/*  935 */       return ""; 
/*  936 */     if (v instanceof PRStream) {
/*      */       
/*      */       try {
/*  939 */         byte[] valBytes = PdfReader.getStreamBytes((PRStream)v);
/*  940 */         return new String(valBytes);
/*  941 */       } catch (IOException e) {
/*  942 */         throw new ExceptionConverter(e);
/*      */       } 
/*      */     }
/*      */     
/*  946 */     PdfName type = mergedDict.getAsName(PdfName.FT);
/*  947 */     if (PdfName.BTN.equals(type)) {
/*  948 */       PdfNumber ff = mergedDict.getAsNumber(PdfName.FF);
/*  949 */       int flags = 0;
/*  950 */       if (ff != null)
/*  951 */         flags = ff.intValue(); 
/*  952 */       if ((flags & 0x10000) != 0)
/*  953 */         return ""; 
/*  954 */       String value = "";
/*  955 */       if (v instanceof PdfName) {
/*  956 */         value = PdfName.decodeName(v.toString());
/*  957 */       } else if (v instanceof PdfString) {
/*  958 */         value = ((PdfString)v).toUnicodeString();
/*  959 */       }  PdfArray opts = item.getValue(0).getAsArray(PdfName.OPT);
/*  960 */       if (opts != null) {
/*  961 */         int idx = 0;
/*      */         try {
/*  963 */           idx = Integer.parseInt(value);
/*  964 */           PdfString ps = opts.getAsString(idx);
/*  965 */           value = ps.toUnicodeString();
/*  966 */           this.lastWasString = true;
/*  967 */         } catch (Exception exception) {}
/*      */       } 
/*      */       
/*  970 */       return value;
/*      */     } 
/*  972 */     if (v instanceof PdfString) {
/*  973 */       this.lastWasString = true;
/*  974 */       return ((PdfString)v).toUnicodeString();
/*  975 */     }  if (v instanceof PdfName) {
/*  976 */       return PdfName.decodeName(v.toString());
/*      */     }
/*  978 */     return "";
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
/*      */   public String[] getListSelection(String name) {
/*  990 */     String s = getField(name);
/*  991 */     if (s == null) {
/*  992 */       ret = new String[0];
/*      */     } else {
/*  994 */       ret = new String[] { s };
/*      */     } 
/*  996 */     Item item = this.fields.get(name);
/*  997 */     if (item == null) {
/*  998 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1003 */     PdfArray values = item.getMerged(0).getAsArray(PdfName.I);
/* 1004 */     if (values == null)
/* 1005 */       return ret; 
/* 1006 */     String[] ret = new String[values.size()];
/* 1007 */     String[] options = getListOptionExport(name);
/*      */     
/* 1009 */     int idx = 0;
/* 1010 */     for (Iterator<PdfObject> i = values.listIterator(); i.hasNext(); ) {
/* 1011 */       PdfNumber n = (PdfNumber)i.next();
/* 1012 */       ret[idx++] = options[n.intValue()];
/*      */     } 
/* 1014 */     return ret;
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
/*      */   public boolean setFieldProperty(String field, String name, Object value, int[] inst) {
/* 1038 */     if (this.writer == null)
/* 1039 */       throw new RuntimeException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0])); 
/*      */     try {
/* 1041 */       Item item = this.fields.get(field);
/* 1042 */       if (item == null)
/* 1043 */         return false; 
/* 1044 */       InstHit hit = new InstHit(inst);
/*      */ 
/*      */       
/* 1047 */       if (name.equalsIgnoreCase("textfont"))
/* 1048 */       { for (int k = 0; k < item.size(); k++) {
/* 1049 */           if (hit.isHit(k)) {
/* 1050 */             PdfDictionary merged = item.getMerged(k);
/* 1051 */             PdfString da = merged.getAsString(PdfName.DA);
/* 1052 */             PdfDictionary dr = merged.getAsDict(PdfName.DR);
/* 1053 */             if (da != null) {
/* 1054 */               if (dr == null) {
/* 1055 */                 dr = new PdfDictionary();
/* 1056 */                 merged.put(PdfName.DR, dr);
/*      */               } 
/* 1058 */               Object[] dao = splitDAelements(da.toUnicodeString());
/* 1059 */               PdfAppearance cb = new PdfAppearance();
/* 1060 */               if (dao[0] != null) {
/* 1061 */                 BaseFont bf = (BaseFont)value;
/* 1062 */                 PdfName psn = PdfAppearance.stdFieldFontNames.get(bf.getPostscriptFontName());
/* 1063 */                 if (psn == null) {
/* 1064 */                   psn = new PdfName(bf.getPostscriptFontName());
/*      */                 }
/* 1066 */                 PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
/* 1067 */                 if (fonts == null) {
/* 1068 */                   fonts = new PdfDictionary();
/* 1069 */                   dr.put(PdfName.FONT, fonts);
/*      */                 } 
/* 1071 */                 PdfIndirectReference fref = (PdfIndirectReference)fonts.get(psn);
/* 1072 */                 PdfDictionary top = this.reader.getCatalog().getAsDict(PdfName.ACROFORM);
/* 1073 */                 markUsed(top);
/* 1074 */                 dr = top.getAsDict(PdfName.DR);
/* 1075 */                 if (dr == null) {
/* 1076 */                   dr = new PdfDictionary();
/* 1077 */                   top.put(PdfName.DR, dr);
/*      */                 } 
/* 1079 */                 markUsed(dr);
/* 1080 */                 PdfDictionary fontsTop = dr.getAsDict(PdfName.FONT);
/* 1081 */                 if (fontsTop == null) {
/* 1082 */                   fontsTop = new PdfDictionary();
/* 1083 */                   dr.put(PdfName.FONT, fontsTop);
/*      */                 } 
/* 1085 */                 markUsed(fontsTop);
/* 1086 */                 PdfIndirectReference frefTop = (PdfIndirectReference)fontsTop.get(psn);
/* 1087 */                 if (frefTop != null) {
/* 1088 */                   if (fref == null)
/* 1089 */                     fonts.put(psn, frefTop); 
/* 1090 */                 } else if (fref == null) {
/*      */                   FontDetails fd;
/* 1092 */                   if (bf.getFontType() == 4) {
/* 1093 */                     fd = new FontDetails(null, ((DocumentFont)bf).getIndirectReference(), bf);
/*      */                   } else {
/* 1095 */                     bf.setSubset(false);
/* 1096 */                     fd = this.writer.addSimple(bf);
/* 1097 */                     this.localFonts.put(psn.toString().substring(1), bf);
/*      */                   } 
/* 1099 */                   fontsTop.put(psn, fd.getIndirectReference());
/* 1100 */                   fonts.put(psn, fd.getIndirectReference());
/*      */                 } 
/* 1102 */                 ByteBuffer buf = cb.getInternalBuffer();
/* 1103 */                 buf.append(psn.getBytes()).append(' ').append(((Float)dao[1]).floatValue()).append(" Tf ");
/* 1104 */                 if (dao[2] != null)
/* 1105 */                   cb.setColorFill((BaseColor)dao[2]); 
/* 1106 */                 PdfString s = new PdfString(cb.toString());
/* 1107 */                 item.getMerged(k).put(PdfName.DA, s);
/* 1108 */                 item.getWidget(k).put(PdfName.DA, s);
/* 1109 */                 markUsed(item.getWidget(k));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }  }
/* 1114 */       else if (name.equalsIgnoreCase("textcolor"))
/* 1115 */       { for (int k = 0; k < item.size(); k++) {
/* 1116 */           if (hit.isHit(k)) {
/* 1117 */             PdfDictionary merged = item.getMerged(k);
/* 1118 */             PdfString da = merged.getAsString(PdfName.DA);
/* 1119 */             if (da != null) {
/* 1120 */               Object[] dao = splitDAelements(da.toUnicodeString());
/* 1121 */               PdfAppearance cb = new PdfAppearance();
/* 1122 */               if (dao[0] != null) {
/* 1123 */                 ByteBuffer buf = cb.getInternalBuffer();
/* 1124 */                 buf.append((new PdfName((String)dao[0])).getBytes()).append(' ').append(((Float)dao[1]).floatValue()).append(" Tf ");
/* 1125 */                 cb.setColorFill((BaseColor)value);
/* 1126 */                 PdfString s = new PdfString(cb.toString());
/* 1127 */                 item.getMerged(k).put(PdfName.DA, s);
/* 1128 */                 item.getWidget(k).put(PdfName.DA, s);
/* 1129 */                 markUsed(item.getWidget(k));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }  }
/* 1134 */       else if (name.equalsIgnoreCase("textsize"))
/* 1135 */       { for (int k = 0; k < item.size(); k++) {
/* 1136 */           if (hit.isHit(k)) {
/* 1137 */             PdfDictionary merged = item.getMerged(k);
/* 1138 */             PdfString da = merged.getAsString(PdfName.DA);
/* 1139 */             if (da != null) {
/* 1140 */               Object[] dao = splitDAelements(da.toUnicodeString());
/* 1141 */               PdfAppearance cb = new PdfAppearance();
/* 1142 */               if (dao[0] != null) {
/* 1143 */                 ByteBuffer buf = cb.getInternalBuffer();
/* 1144 */                 buf.append((new PdfName((String)dao[0])).getBytes()).append(' ').append(((Float)value).floatValue()).append(" Tf ");
/* 1145 */                 if (dao[2] != null)
/* 1146 */                   cb.setColorFill((BaseColor)dao[2]); 
/* 1147 */                 PdfString s = new PdfString(cb.toString());
/* 1148 */                 item.getMerged(k).put(PdfName.DA, s);
/* 1149 */                 item.getWidget(k).put(PdfName.DA, s);
/* 1150 */                 markUsed(item.getWidget(k));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }  }
/* 1155 */       else if (name.equalsIgnoreCase("bgcolor") || name.equalsIgnoreCase("bordercolor"))
/* 1156 */       { PdfName dname = name.equalsIgnoreCase("bgcolor") ? PdfName.BG : PdfName.BC;
/* 1157 */         for (int k = 0; k < item.size(); k++) {
/* 1158 */           if (hit.isHit(k)) {
/* 1159 */             PdfDictionary merged = item.getMerged(k);
/* 1160 */             PdfDictionary mk = merged.getAsDict(PdfName.MK);
/* 1161 */             if (mk == null) {
/* 1162 */               if (value == null)
/* 1163 */                 return true; 
/* 1164 */               mk = new PdfDictionary();
/* 1165 */               item.getMerged(k).put(PdfName.MK, mk);
/* 1166 */               item.getWidget(k).put(PdfName.MK, mk);
/* 1167 */               markUsed(item.getWidget(k));
/*      */             } else {
/* 1169 */               markUsed(mk);
/*      */             } 
/* 1171 */             if (value == null) {
/* 1172 */               mk.remove(dname);
/*      */             } else {
/* 1174 */               mk.put(dname, PdfFormField.getMKColor((BaseColor)value));
/*      */             } 
/*      */           } 
/*      */         }  }
/* 1178 */       else { return false; }
/* 1179 */        return true;
/* 1180 */     } catch (Exception e) {
/* 1181 */       throw new ExceptionConverter(e);
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
/*      */   public boolean setFieldProperty(String field, String name, int value, int[] inst) {
/* 1213 */     if (this.writer == null)
/* 1214 */       throw new RuntimeException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0])); 
/* 1215 */     Item item = this.fields.get(field);
/* 1216 */     if (item == null)
/* 1217 */       return false; 
/* 1218 */     InstHit hit = new InstHit(inst);
/* 1219 */     if (name.equalsIgnoreCase("flags")) {
/* 1220 */       PdfNumber num = new PdfNumber(value);
/* 1221 */       for (int k = 0; k < item.size(); k++) {
/* 1222 */         if (hit.isHit(k)) {
/* 1223 */           item.getMerged(k).put(PdfName.F, num);
/* 1224 */           item.getWidget(k).put(PdfName.F, num);
/* 1225 */           markUsed(item.getWidget(k));
/*      */         } 
/*      */       } 
/* 1228 */     } else if (name.equalsIgnoreCase("setflags")) {
/* 1229 */       for (int k = 0; k < item.size(); k++) {
/* 1230 */         if (hit.isHit(k)) {
/* 1231 */           PdfNumber num = item.getWidget(k).getAsNumber(PdfName.F);
/* 1232 */           int val = 0;
/* 1233 */           if (num != null)
/* 1234 */             val = num.intValue(); 
/* 1235 */           num = new PdfNumber(val | value);
/* 1236 */           item.getMerged(k).put(PdfName.F, num);
/* 1237 */           item.getWidget(k).put(PdfName.F, num);
/* 1238 */           markUsed(item.getWidget(k));
/*      */         } 
/*      */       } 
/* 1241 */     } else if (name.equalsIgnoreCase("clrflags")) {
/* 1242 */       for (int k = 0; k < item.size(); k++) {
/* 1243 */         if (hit.isHit(k)) {
/* 1244 */           PdfDictionary widget = item.getWidget(k);
/* 1245 */           PdfNumber num = widget.getAsNumber(PdfName.F);
/* 1246 */           int val = 0;
/* 1247 */           if (num != null)
/* 1248 */             val = num.intValue(); 
/* 1249 */           num = new PdfNumber(val & (value ^ 0xFFFFFFFF));
/* 1250 */           item.getMerged(k).put(PdfName.F, num);
/* 1251 */           widget.put(PdfName.F, num);
/* 1252 */           markUsed(widget);
/*      */         } 
/*      */       } 
/* 1255 */     } else if (name.equalsIgnoreCase("fflags")) {
/* 1256 */       PdfNumber num = new PdfNumber(value);
/* 1257 */       for (int k = 0; k < item.size(); k++) {
/* 1258 */         if (hit.isHit(k)) {
/* 1259 */           item.getMerged(k).put(PdfName.FF, num);
/* 1260 */           item.getValue(k).put(PdfName.FF, num);
/* 1261 */           markUsed(item.getValue(k));
/*      */         } 
/*      */       } 
/* 1264 */     } else if (name.equalsIgnoreCase("setfflags")) {
/* 1265 */       for (int k = 0; k < item.size(); k++) {
/* 1266 */         if (hit.isHit(k)) {
/* 1267 */           PdfDictionary valDict = item.getValue(k);
/* 1268 */           PdfNumber num = valDict.getAsNumber(PdfName.FF);
/* 1269 */           int val = 0;
/* 1270 */           if (num != null)
/* 1271 */             val = num.intValue(); 
/* 1272 */           num = new PdfNumber(val | value);
/* 1273 */           item.getMerged(k).put(PdfName.FF, num);
/* 1274 */           valDict.put(PdfName.FF, num);
/* 1275 */           markUsed(valDict);
/*      */         } 
/*      */       } 
/* 1278 */     } else if (name.equalsIgnoreCase("clrfflags")) {
/* 1279 */       for (int k = 0; k < item.size(); k++) {
/* 1280 */         if (hit.isHit(k)) {
/* 1281 */           PdfDictionary valDict = item.getValue(k);
/* 1282 */           PdfNumber num = valDict.getAsNumber(PdfName.FF);
/* 1283 */           int val = 0;
/* 1284 */           if (num != null)
/* 1285 */             val = num.intValue(); 
/* 1286 */           num = new PdfNumber(val & (value ^ 0xFFFFFFFF));
/* 1287 */           item.getMerged(k).put(PdfName.FF, num);
/* 1288 */           valDict.put(PdfName.FF, num);
/* 1289 */           markUsed(valDict);
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1293 */       return false;
/* 1294 */     }  return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mergeXfaData(Node n) throws IOException, DocumentException {
/* 1305 */     XfaForm.Xml2SomDatasets data = new XfaForm.Xml2SomDatasets(n);
/* 1306 */     for (String string : data.getOrder()) {
/* 1307 */       String name = string;
/* 1308 */       String text = XfaForm.getNodeText(data.getName2Node().get(name));
/* 1309 */       setField(name, text);
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
/*      */   public void setFields(FdfReader fdf) throws IOException, DocumentException {
/* 1321 */     HashMap<String, PdfDictionary> fd = fdf.getFields();
/* 1322 */     for (String f : fd.keySet()) {
/* 1323 */       String v = fdf.getFieldValue(f);
/* 1324 */       if (v != null) {
/* 1325 */         setField(f, v);
/*      */       }
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
/*      */   public boolean regenerateField(String name) throws IOException, DocumentException {
/* 1343 */     String value = getField(name);
/* 1344 */     return setField(name, value, value);
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
/*      */   public boolean setField(String name, String value) throws IOException, DocumentException {
/* 1358 */     return setField(name, value, (String)null);
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
/*      */   public boolean setField(String name, String value, boolean saveAppearance) throws IOException, DocumentException {
/* 1373 */     return setField(name, value, null, saveAppearance);
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
/*      */   public boolean setFieldRichValue(String name, String richValue) throws DocumentException, IOException {
/* 1390 */     if (this.writer == null)
/*      */     {
/* 1392 */       throw new DocumentException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0]));
/*      */     }
/*      */     
/* 1395 */     Item item = getFieldItem(name);
/* 1396 */     if (item == null)
/*      */     {
/* 1398 */       return false;
/*      */     }
/*      */     
/* 1401 */     if (getFieldType(name) != 4)
/*      */     {
/* 1403 */       return false;
/*      */     }
/*      */     
/* 1406 */     PdfDictionary merged = item.getMerged(0);
/* 1407 */     PdfNumber ffNum = merged.getAsNumber(PdfName.FF);
/* 1408 */     int flagVal = 0;
/* 1409 */     if (ffNum != null) {
/* 1410 */       flagVal = ffNum.intValue();
/*      */     }
/* 1412 */     if ((flagVal & 0x2000000) == 0)
/*      */     {
/* 1414 */       return false;
/*      */     }
/*      */     
/* 1417 */     PdfString richString = new PdfString(richValue);
/* 1418 */     item.writeToAll(PdfName.RV, richString, 5);
/*      */     
/* 1420 */     InputStream is = new ByteArrayInputStream(richValue.getBytes());
/* 1421 */     PdfString valueString = new PdfString(XmlToTxt.parse(is));
/* 1422 */     item.writeToAll(PdfName.V, valueString, 5);
/* 1423 */     return true;
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
/*      */   public boolean setField(String name, String value, String display) throws IOException, DocumentException {
/* 1442 */     return setField(name, value, display, false);
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
/*      */   public boolean setField(String name, String value, String display, boolean saveAppearance) throws IOException, DocumentException {
/* 1462 */     if (this.writer == null)
/* 1463 */       throw new DocumentException(MessageLocalization.getComposedMessage("this.acrofields.instance.is.read.only", new Object[0])); 
/* 1464 */     if (this.xfa.isXfaPresent()) {
/* 1465 */       name = this.xfa.findFieldName(name, this);
/* 1466 */       if (name == null)
/* 1467 */         return false; 
/* 1468 */       String shortName = XfaForm.Xml2Som.getShortName(name);
/* 1469 */       Node xn = this.xfa.findDatasetsNode(shortName);
/* 1470 */       if (xn == null) {
/* 1471 */         xn = this.xfa.getDatasetsSom().insertNode(this.xfa.getDatasetsNode(), shortName);
/*      */       }
/* 1473 */       this.xfa.setNodeText(xn, value);
/*      */     } 
/* 1475 */     Item item = this.fields.get(name);
/* 1476 */     if (item == null)
/* 1477 */       return false; 
/* 1478 */     PdfDictionary merged = item.getMerged(0);
/* 1479 */     PdfName type = merged.getAsName(PdfName.FT);
/* 1480 */     if (PdfName.TX.equals(type)) {
/* 1481 */       PdfNumber maxLen = merged.getAsNumber(PdfName.MAXLEN);
/* 1482 */       int len = 0;
/* 1483 */       if (maxLen != null)
/* 1484 */         len = maxLen.intValue(); 
/* 1485 */       if (len > 0)
/* 1486 */         value = value.substring(0, Math.min(len, value.length())); 
/*      */     } 
/* 1488 */     if (display == null)
/* 1489 */       display = value; 
/* 1490 */     if (PdfName.TX.equals(type) || PdfName.CH.equals(type)) {
/* 1491 */       PdfString v = new PdfString(value, "UnicodeBig");
/* 1492 */       for (int idx = 0; idx < item.size(); idx++) {
/* 1493 */         PdfDictionary valueDic = item.getValue(idx);
/* 1494 */         valueDic.put(PdfName.V, v);
/* 1495 */         valueDic.remove(PdfName.I);
/* 1496 */         markUsed(valueDic);
/* 1497 */         merged = item.getMerged(idx);
/* 1498 */         merged.remove(PdfName.I);
/* 1499 */         merged.put(PdfName.V, v);
/* 1500 */         PdfDictionary widget = item.getWidget(idx);
/* 1501 */         if (this.generateAppearances) {
/* 1502 */           PdfAppearance app = getAppearance(merged, display, name);
/* 1503 */           if (PdfName.CH.equals(type)) {
/* 1504 */             PdfNumber n = new PdfNumber(this.topFirst);
/* 1505 */             widget.put(PdfName.TI, n);
/* 1506 */             merged.put(PdfName.TI, n);
/*      */           } 
/* 1508 */           PdfDictionary appDic = widget.getAsDict(PdfName.AP);
/* 1509 */           if (appDic == null) {
/* 1510 */             appDic = new PdfDictionary();
/* 1511 */             widget.put(PdfName.AP, appDic);
/* 1512 */             merged.put(PdfName.AP, appDic);
/*      */           } 
/* 1514 */           appDic.put(PdfName.N, app.getIndirectReference());
/* 1515 */           this.writer.releaseTemplate(app);
/*      */         } else {
/* 1517 */           widget.remove(PdfName.AP);
/* 1518 */           merged.remove(PdfName.AP);
/*      */         } 
/* 1520 */         markUsed(widget);
/*      */       } 
/* 1522 */       return true;
/* 1523 */     }  if (PdfName.BTN.equals(type)) {
/* 1524 */       PdfName vt; PdfNumber ff = item.getMerged(0).getAsNumber(PdfName.FF);
/* 1525 */       int flags = 0;
/* 1526 */       if (ff != null)
/* 1527 */         flags = ff.intValue(); 
/* 1528 */       if ((flags & 0x10000) != 0) {
/*      */         Image img;
/*      */         
/*      */         try {
/* 1532 */           img = Image.getInstance(Base64.decode(value));
/* 1533 */         } catch (Exception e) {
/* 1534 */           return false;
/*      */         } 
/* 1536 */         PushbuttonField pb = getNewPushbuttonFromField(name);
/* 1537 */         pb.setImage(img);
/* 1538 */         replacePushbuttonField(name, pb.getField());
/* 1539 */         return true;
/*      */       } 
/* 1541 */       PdfName v = new PdfName(value);
/* 1542 */       ArrayList<String> lopt = new ArrayList<String>();
/* 1543 */       PdfArray opts = item.getValue(0).getAsArray(PdfName.OPT);
/* 1544 */       if (opts != null)
/* 1545 */         for (int k = 0; k < opts.size(); k++) {
/* 1546 */           PdfString valStr = opts.getAsString(k);
/* 1547 */           if (valStr != null) {
/* 1548 */             lopt.add(valStr.toUnicodeString());
/*      */           } else {
/* 1550 */             lopt.add(null);
/*      */           } 
/*      */         }  
/* 1553 */       int vidx = lopt.indexOf(value);
/*      */       
/* 1555 */       if (vidx >= 0) {
/* 1556 */         vt = new PdfName(String.valueOf(vidx));
/*      */       } else {
/* 1558 */         vt = v;
/* 1559 */       }  for (int idx = 0; idx < item.size(); idx++) {
/* 1560 */         merged = item.getMerged(idx);
/* 1561 */         PdfDictionary widget = item.getWidget(idx);
/* 1562 */         PdfDictionary valDict = item.getValue(idx);
/* 1563 */         markUsed(item.getValue(idx));
/* 1564 */         valDict.put(PdfName.V, vt);
/* 1565 */         merged.put(PdfName.V, vt);
/* 1566 */         markUsed(widget);
/* 1567 */         PdfDictionary appDic = widget.getAsDict(PdfName.AP);
/* 1568 */         if (appDic == null)
/* 1569 */           return false; 
/* 1570 */         PdfDictionary normal = appDic.getAsDict(PdfName.N);
/* 1571 */         if (isInAP(normal, vt) || normal == null) {
/* 1572 */           merged.put(PdfName.AS, vt);
/* 1573 */           widget.put(PdfName.AS, vt);
/*      */         } else {
/* 1575 */           merged.put(PdfName.AS, PdfName.Off);
/* 1576 */           widget.put(PdfName.AS, PdfName.Off);
/*      */         } 
/* 1578 */         if (this.generateAppearances && !saveAppearance) {
/* 1579 */           PdfAppearance app = getAppearance(merged, display, name);
/* 1580 */           if (normal != null) {
/* 1581 */             normal.put(merged.getAsName(PdfName.AS), app.getIndirectReference());
/*      */           } else {
/* 1583 */             appDic.put(PdfName.N, app.getIndirectReference());
/* 1584 */           }  this.writer.releaseTemplate(app);
/*      */         } 
/*      */       } 
/* 1587 */       return true;
/*      */     } 
/* 1589 */     return false;
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
/*      */   public boolean setListSelection(String name, String[] value) throws IOException, DocumentException {
/* 1602 */     Item item = getFieldItem(name);
/* 1603 */     if (item == null)
/* 1604 */       return false; 
/* 1605 */     PdfDictionary merged = item.getMerged(0);
/* 1606 */     PdfName type = merged.getAsName(PdfName.FT);
/* 1607 */     if (!PdfName.CH.equals(type)) {
/* 1608 */       return false;
/*      */     }
/* 1610 */     String[] options = getListOptionExport(name);
/* 1611 */     PdfArray array = new PdfArray();
/* 1612 */     for (String element : value) {
/* 1613 */       for (int j = 0; j < options.length; j++) {
/* 1614 */         if (options[j].equals(element)) {
/* 1615 */           array.add(new PdfNumber(j));
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1620 */     item.writeToAll(PdfName.I, array, 5);
/*      */     
/* 1622 */     PdfArray vals = new PdfArray();
/* 1623 */     for (int i = 0; i < value.length; i++) {
/* 1624 */       vals.add(new PdfString(value[i]));
/*      */     }
/* 1626 */     item.writeToAll(PdfName.V, vals, 5);
/*      */     
/* 1628 */     PdfAppearance app = getAppearance(merged, value, name);
/*      */     
/* 1630 */     PdfDictionary apDic = new PdfDictionary();
/* 1631 */     apDic.put(PdfName.N, app.getIndirectReference());
/* 1632 */     item.writeToAll(PdfName.AP, apDic, 3);
/*      */     
/* 1634 */     this.writer.releaseTemplate(app);
/*      */     
/* 1636 */     item.markUsed(this, 6);
/* 1637 */     return true;
/*      */   }
/*      */   
/*      */   boolean isInAP(PdfDictionary nDic, PdfName check) {
/* 1641 */     return (nDic != null && nDic.get(check) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Item> getFields() {
/* 1651 */     return this.fields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFields(XfdfReader xfdf) throws IOException, DocumentException {
/* 1662 */     HashMap<String, String> fd = xfdf.getFields();
/* 1663 */     for (String f : fd.keySet()) {
/* 1664 */       String v = xfdf.getFieldValue(f);
/* 1665 */       if (v != null)
/* 1666 */         setField(f, v); 
/* 1667 */       List<String> l = xfdf.getListValues(f);
/* 1668 */       if (l != null) {
/* 1669 */         setListSelection(v, l.<String>toArray(new String[l.size()]));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Item getFieldItem(String name) {
/* 1681 */     if (this.xfa.isXfaPresent()) {
/* 1682 */       name = this.xfa.findFieldName(name, this);
/* 1683 */       if (name == null)
/* 1684 */         return null; 
/*      */     } 
/* 1686 */     return this.fields.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTranslatedFieldName(String name) {
/* 1696 */     if (this.xfa.isXfaPresent()) {
/* 1697 */       String namex = this.xfa.findFieldName(name, this);
/* 1698 */       if (namex != null)
/* 1699 */         name = namex; 
/*      */     } 
/* 1701 */     return name;
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
/*      */   public List<FieldPosition> getFieldPositions(String name) {
/* 1713 */     Item item = getFieldItem(name);
/* 1714 */     if (item == null)
/* 1715 */       return null; 
/* 1716 */     ArrayList<FieldPosition> ret = new ArrayList<FieldPosition>();
/* 1717 */     for (int k = 0; k < item.size(); k++) {
/*      */       try {
/* 1719 */         PdfDictionary wd = item.getWidget(k);
/* 1720 */         PdfArray rect = wd.getAsArray(PdfName.RECT);
/* 1721 */         if (rect != null)
/*      */         
/* 1723 */         { Rectangle r = PdfReader.getNormalizedRectangle(rect);
/* 1724 */           int page = item.getPage(k).intValue();
/* 1725 */           int rotation = this.reader.getPageRotation(page);
/* 1726 */           FieldPosition fp = new FieldPosition();
/* 1727 */           fp.page = page;
/* 1728 */           if (rotation != 0) {
/* 1729 */             Rectangle pageSize = this.reader.getPageSize(page);
/* 1730 */             switch (rotation) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               case 270:
/* 1736 */                 r = new Rectangle(pageSize.getTop() - r.getBottom(), r.getLeft(), pageSize.getTop() - r.getTop(), r.getRight());
/*      */                 break;
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               case 180:
/* 1743 */                 r = new Rectangle(pageSize.getRight() - r.getLeft(), pageSize.getTop() - r.getBottom(), pageSize.getRight() - r.getRight(), pageSize.getTop() - r.getTop());
/*      */                 break;
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               case 90:
/* 1750 */                 r = new Rectangle(r.getBottom(), pageSize.getRight() - r.getLeft(), r.getTop(), pageSize.getRight() - r.getRight());
/*      */                 break;
/*      */             } 
/* 1753 */             r.normalize();
/*      */           } 
/* 1755 */           fp.position = r;
/* 1756 */           ret.add(fp); } 
/* 1757 */       } catch (Exception exception) {}
/*      */     } 
/*      */ 
/*      */     
/* 1761 */     return ret;
/*      */   }
/*      */   
/*      */   private int removeRefFromArray(PdfArray array, PdfObject refo) {
/* 1765 */     if (refo == null || !refo.isIndirect())
/* 1766 */       return array.size(); 
/* 1767 */     PdfIndirectReference ref = (PdfIndirectReference)refo;
/* 1768 */     for (int j = 0; j < array.size(); j++) {
/* 1769 */       PdfObject obj = array.getPdfObject(j);
/* 1770 */       if (obj.isIndirect())
/*      */       {
/* 1772 */         if (((PdfIndirectReference)obj).getNumber() == ref.getNumber())
/* 1773 */           array.remove(j--);  } 
/*      */     } 
/* 1775 */     return array.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeFieldsFromPage(int page) {
/* 1785 */     if (page < 1)
/* 1786 */       return false; 
/* 1787 */     String[] names = new String[this.fields.size()];
/* 1788 */     this.fields.keySet().toArray((Object[])names);
/* 1789 */     boolean found = false;
/* 1790 */     for (int k = 0; k < names.length; k++) {
/* 1791 */       boolean fr = removeField(names[k], page);
/* 1792 */       found = (found || fr);
/*      */     } 
/* 1794 */     return found;
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
/*      */   public boolean removeField(String name, int page) {
/* 1807 */     Item item = getFieldItem(name);
/* 1808 */     if (item == null)
/* 1809 */       return false; 
/* 1810 */     PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(this.reader.getCatalog().get(PdfName.ACROFORM), this.reader.getCatalog());
/*      */     
/* 1812 */     if (acroForm == null)
/* 1813 */       return false; 
/* 1814 */     PdfArray arrayf = acroForm.getAsArray(PdfName.FIELDS);
/* 1815 */     if (arrayf == null)
/* 1816 */       return false; 
/* 1817 */     for (int k = 0; k < item.size(); k++) {
/* 1818 */       int pageV = item.getPage(k).intValue();
/* 1819 */       if (page == -1 || page == pageV) {
/*      */         
/* 1821 */         PdfIndirectReference ref = item.getWidgetRef(k);
/* 1822 */         PdfDictionary wd = item.getWidget(k);
/* 1823 */         PdfDictionary pageDic = this.reader.getPageN(pageV);
/* 1824 */         PdfArray annots = (pageDic != null) ? pageDic.getAsArray(PdfName.ANNOTS) : null;
/* 1825 */         if (annots != null)
/* 1826 */           if (removeRefFromArray(annots, ref) == 0) {
/* 1827 */             pageDic.remove(PdfName.ANNOTS);
/* 1828 */             markUsed(pageDic);
/*      */           } else {
/* 1830 */             markUsed(annots);
/*      */           }  
/* 1832 */         PdfReader.killIndirect(ref);
/* 1833 */         PdfIndirectReference kid = ref;
/* 1834 */         while ((ref = wd.getAsIndirectObject(PdfName.PARENT)) != null) {
/* 1835 */           wd = wd.getAsDict(PdfName.PARENT);
/* 1836 */           if (wd == null)
/* 1837 */             break;  PdfArray kids = wd.getAsArray(PdfName.KIDS);
/* 1838 */           if (removeRefFromArray(kids, kid) != 0)
/*      */             break; 
/* 1840 */           kid = ref;
/* 1841 */           PdfReader.killIndirect(ref);
/*      */         } 
/* 1843 */         if (ref == null) {
/* 1844 */           removeRefFromArray(arrayf, kid);
/* 1845 */           markUsed(arrayf);
/*      */         } 
/* 1847 */         if (page != -1) {
/* 1848 */           item.remove(k);
/* 1849 */           k--;
/*      */         } 
/*      */       } 
/* 1852 */     }  if (page == -1 || item.size() == 0)
/* 1853 */       this.fields.remove(name); 
/* 1854 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeField(String name) {
/* 1864 */     return removeField(name, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGenerateAppearances() {
/* 1873 */     return this.generateAppearances;
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
/*      */   public void setGenerateAppearances(boolean generateAppearances) {
/* 1885 */     this.generateAppearances = generateAppearances;
/* 1886 */     PdfDictionary top = this.reader.getCatalog().getAsDict(PdfName.ACROFORM);
/* 1887 */     if (generateAppearances) {
/* 1888 */       top.remove(PdfName.NEEDAPPEARANCES);
/*      */     } else {
/* 1890 */       top.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean clearSignatureField(String name) {
/* 1901 */     this.sigNames = null;
/* 1902 */     getSignatureNames();
/* 1903 */     if (!this.sigNames.containsKey(name))
/* 1904 */       return false; 
/* 1905 */     Item sig = this.fields.get(name);
/* 1906 */     sig.markUsed(this, 6);
/* 1907 */     int n = sig.size();
/* 1908 */     for (int k = 0; k < n; k++) {
/* 1909 */       clearSigDic(sig.getMerged(k));
/* 1910 */       clearSigDic(sig.getWidget(k));
/* 1911 */       clearSigDic(sig.getValue(k));
/*      */     } 
/* 1913 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<String> getSignatureNames() {
/* 1922 */     if (this.sigNames != null)
/* 1923 */       return new ArrayList<String>(this.orderedSignatureNames); 
/* 1924 */     this.sigNames = (HashMap)new HashMap<String, int>();
/* 1925 */     this.orderedSignatureNames = new ArrayList<String>();
/* 1926 */     ArrayList<Object[]> sorter = new ArrayList();
/* 1927 */     for (Map.Entry<String, Item> entry : this.fields.entrySet()) {
/* 1928 */       Item item = entry.getValue();
/* 1929 */       PdfDictionary merged = item.getMerged(0);
/* 1930 */       if (!PdfName.SIG.equals(merged.get(PdfName.FT)))
/*      */         continue; 
/* 1932 */       PdfDictionary v = merged.getAsDict(PdfName.V);
/* 1933 */       if (v == null)
/*      */         continue; 
/* 1935 */       PdfString contents = v.getAsString(PdfName.CONTENTS);
/* 1936 */       if (contents == null)
/*      */         continue; 
/* 1938 */       PdfArray ro = v.getAsArray(PdfName.BYTERANGE);
/* 1939 */       if (ro == null)
/*      */         continue; 
/* 1941 */       int rangeSize = ro.size();
/* 1942 */       if (rangeSize < 2)
/*      */         continue; 
/* 1944 */       int length = ro.getAsNumber(rangeSize - 1).intValue() + ro.getAsNumber(rangeSize - 2).intValue();
/* 1945 */       sorter.add(new Object[] { entry.getKey(), { length, 0 } });
/*      */     } 
/* 1947 */     Collections.sort(sorter, new SorterComparator());
/* 1948 */     if (!sorter.isEmpty()) {
/* 1949 */       if (((int[])((Object[])sorter.get(sorter.size() - 1))[1])[0] == this.reader.getFileLength()) {
/* 1950 */         this.totalRevisions = sorter.size();
/*      */       } else {
/* 1952 */         this.totalRevisions = sorter.size() + 1;
/* 1953 */       }  for (int k = 0; k < sorter.size(); k++) {
/* 1954 */         Object[] objs = sorter.get(k);
/* 1955 */         String name = (String)objs[0];
/* 1956 */         int[] p = (int[])objs[1];
/* 1957 */         p[1] = k + 1;
/* 1958 */         this.sigNames.put(name, p);
/* 1959 */         this.orderedSignatureNames.add(name);
/*      */       } 
/*      */     } 
/* 1962 */     return new ArrayList<String>(this.orderedSignatureNames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<String> getBlankSignatureNames() {
/* 1971 */     getSignatureNames();
/* 1972 */     ArrayList<String> sigs = new ArrayList<String>();
/* 1973 */     for (Map.Entry<String, Item> entry : this.fields.entrySet()) {
/* 1974 */       Item item = entry.getValue();
/* 1975 */       PdfDictionary merged = item.getMerged(0);
/* 1976 */       if (!PdfName.SIG.equals(merged.getAsName(PdfName.FT)))
/*      */         continue; 
/* 1978 */       if (this.sigNames.containsKey(entry.getKey()))
/*      */         continue; 
/* 1980 */       sigs.add(entry.getKey());
/*      */     } 
/* 1982 */     return sigs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfDictionary getSignatureDictionary(String name) {
/* 1993 */     getSignatureNames();
/* 1994 */     name = getTranslatedFieldName(name);
/* 1995 */     if (!this.sigNames.containsKey(name))
/* 1996 */       return null; 
/* 1997 */     Item item = this.fields.get(name);
/* 1998 */     PdfDictionary merged = item.getMerged(0);
/* 1999 */     return merged.getAsDict(PdfName.V);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfIndirectReference getNormalAppearance(String name) {
/* 2009 */     getSignatureNames();
/* 2010 */     name = getTranslatedFieldName(name);
/* 2011 */     Item item = this.fields.get(name);
/* 2012 */     if (item == null)
/* 2013 */       return null; 
/* 2014 */     PdfDictionary merged = item.getMerged(0);
/* 2015 */     PdfDictionary ap = merged.getAsDict(PdfName.AP);
/* 2016 */     if (ap == null)
/* 2017 */       return null; 
/* 2018 */     PdfIndirectReference ref = ap.getAsIndirectObject(PdfName.N);
/* 2019 */     if (ref == null)
/* 2020 */       return null; 
/* 2021 */     return ref;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean signatureCoversWholeDocument(String name) {
/* 2032 */     getSignatureNames();
/* 2033 */     name = getTranslatedFieldName(name);
/* 2034 */     if (!this.sigNames.containsKey(name))
/* 2035 */       return false; 
/*      */     try {
/* 2037 */       ContentsChecker signatureReader = new ContentsChecker(this.reader.getSafeFile());
/* 2038 */       return signatureReader.checkWhetherSignatureCoversWholeDocument(this.reader.getAcroFields().getFieldItem(name));
/* 2039 */     } catch (IOException e) {
/*      */       
/* 2041 */       return false;
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
/*      */   public PdfPKCS7 verifySignature(String name) {
/* 2074 */     return verifySignature(name, null);
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
/*      */   public PdfPKCS7 verifySignature(String name, String provider) {
/* 2107 */     PdfDictionary v = getSignatureDictionary(name);
/* 2108 */     if (v == null)
/* 2109 */       return null; 
/*      */     try {
/* 2111 */       PdfName sub = v.getAsName(PdfName.SUBFILTER);
/* 2112 */       PdfString contents = v.getAsString(PdfName.CONTENTS);
/* 2113 */       PdfPKCS7 pk = null;
/* 2114 */       if (sub.equals(PdfName.ADBE_X509_RSA_SHA1)) {
/* 2115 */         PdfString cert = v.getAsString(PdfName.CERT);
/* 2116 */         if (cert == null)
/* 2117 */           cert = v.getAsArray(PdfName.CERT).getAsString(0); 
/* 2118 */         if (!this.reader.isEncrypted()) {
/* 2119 */           pk = new PdfPKCS7(contents.getOriginalBytes(), cert.getBytes(), provider);
/*      */         } else {
/* 2121 */           pk = new PdfPKCS7(contents.getBytes(), cert.getBytes(), provider);
/*      */         }
/*      */       
/* 2124 */       } else if (!this.reader.isEncrypted()) {
/* 2125 */         pk = new PdfPKCS7(contents.getOriginalBytes(), sub, provider);
/*      */       } else {
/* 2127 */         pk = new PdfPKCS7(contents.getBytes(), sub, provider);
/*      */       } 
/*      */ 
/*      */       
/* 2131 */       updateByteRange(pk, v);
/* 2132 */       PdfString str = v.getAsString(PdfName.M);
/* 2133 */       if (str != null)
/* 2134 */         pk.setSignDate(PdfDate.decode(str.toString())); 
/* 2135 */       PdfObject obj = PdfReader.getPdfObject(v.get(PdfName.NAME));
/* 2136 */       if (obj != null)
/* 2137 */         if (obj.isString()) {
/* 2138 */           pk.setSignName(((PdfString)obj).toUnicodeString());
/* 2139 */         } else if (obj.isName()) {
/* 2140 */           pk.setSignName(PdfName.decodeName(obj.toString()));
/*      */         }  
/* 2142 */       str = v.getAsString(PdfName.REASON);
/* 2143 */       if (str != null)
/* 2144 */         pk.setReason(str.toUnicodeString()); 
/* 2145 */       str = v.getAsString(PdfName.LOCATION);
/* 2146 */       if (str != null)
/* 2147 */         pk.setLocation(str.toUnicodeString()); 
/* 2148 */       return pk;
/* 2149 */     } catch (Exception e) {
/* 2150 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   private void updateByteRange(PdfPKCS7 pkcs7, PdfDictionary v) {
/*      */     RASInputStream rASInputStream;
/* 2155 */     PdfArray b = v.getAsArray(PdfName.BYTERANGE);
/* 2156 */     RandomAccessFileOrArray rf = this.reader.getSafeFile();
/* 2157 */     InputStream rg = null;
/*      */     try {
/* 2159 */       rASInputStream = new RASInputStream((new RandomAccessSourceFactory()).createRanged(rf.createSourceView(), b.asLongArray()));
/* 2160 */       byte[] buf = new byte[8192];
/*      */       int rd;
/* 2162 */       while ((rd = rASInputStream.read(buf, 0, buf.length)) > 0) {
/* 2163 */         pkcs7.update(buf, 0, rd);
/*      */       }
/* 2165 */     } catch (Exception e) {
/* 2166 */       throw new ExceptionConverter(e);
/*      */     } finally {
/*      */       try {
/* 2169 */         if (rASInputStream != null) rASInputStream.close(); 
/* 2170 */       } catch (IOException e) {
/*      */         
/* 2172 */         throw new ExceptionConverter(e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void markUsed(PdfObject obj) {
/* 2178 */     if (!this.append)
/*      */       return; 
/* 2180 */     ((PdfStamperImp)this.writer).markUsed(obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTotalRevisions() {
/* 2189 */     getSignatureNames();
/* 2190 */     return this.totalRevisions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getRevision(String field) {
/* 2200 */     getSignatureNames();
/* 2201 */     field = getTranslatedFieldName(field);
/* 2202 */     if (!this.sigNames.containsKey(field))
/* 2203 */       return 0; 
/* 2204 */     return ((int[])this.sigNames.get(field))[1];
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
/*      */   public InputStream extractRevision(String field) throws IOException {
/* 2216 */     getSignatureNames();
/* 2217 */     field = getTranslatedFieldName(field);
/* 2218 */     if (!this.sigNames.containsKey(field))
/* 2219 */       return null; 
/* 2220 */     int length = ((int[])this.sigNames.get(field))[0];
/* 2221 */     RandomAccessFileOrArray raf = this.reader.getSafeFile();
/* 2222 */     return (InputStream)new RASInputStream((RandomAccessSource)new WindowRandomAccessSource(raf.createSourceView(), 0L, length));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, TextField> getFieldCache() {
/* 2232 */     return this.fieldCache;
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
/*      */   public void setFieldCache(Map<String, TextField> fieldCache) {
/* 2262 */     this.fieldCache = fieldCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtraMargin(float extraMarginLeft, float extraMarginTop) {
/* 2272 */     this.extraMarginLeft = extraMarginLeft;
/* 2273 */     this.extraMarginTop = extraMarginTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSubstitutionFont(BaseFont font) {
/* 2283 */     if (this.substitutionFonts == null)
/* 2284 */       this.substitutionFonts = new ArrayList<BaseFont>(); 
/* 2285 */     this.substitutionFonts.add(font);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ArrayList<BaseFont> getSubstitutionFonts() {
/* 2295 */     return this.substitutionFonts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSubstitutionFonts(ArrayList<BaseFont> substitutionFonts) {
/* 2305 */     this.substitutionFonts = substitutionFonts;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XfaForm getXfa() {
/* 2314 */     return this.xfa;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeXfa() {
/* 2321 */     PdfDictionary root = this.reader.getCatalog();
/* 2322 */     PdfDictionary acroform = root.getAsDict(PdfName.ACROFORM);
/* 2323 */     acroform.remove(PdfName.XFA);
/*      */     try {
/* 2325 */       this.xfa = new XfaForm(this.reader);
/* 2326 */     } catch (Exception e) {
/* 2327 */       throw new ExceptionConverter(e);
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
/*      */   public PushbuttonField getNewPushbuttonFromField(String field) {
/* 2341 */     return getNewPushbuttonFromField(field, 0);
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
/*      */   public PushbuttonField getNewPushbuttonFromField(String field, int order) {
/*      */     try {
/* 2356 */       if (getFieldType(field) != 1)
/* 2357 */         return null; 
/* 2358 */       Item item = getFieldItem(field);
/* 2359 */       if (order >= item.size())
/* 2360 */         return null; 
/* 2361 */       List<FieldPosition> pos = getFieldPositions(field);
/* 2362 */       Rectangle box = ((FieldPosition)pos.get(order)).position;
/* 2363 */       PushbuttonField newButton = new PushbuttonField(this.writer, box, null);
/* 2364 */       PdfDictionary dic = item.getMerged(order);
/* 2365 */       decodeGenericDictionary(dic, newButton);
/* 2366 */       PdfDictionary mk = dic.getAsDict(PdfName.MK);
/* 2367 */       if (mk != null) {
/* 2368 */         PdfString text = mk.getAsString(PdfName.CA);
/* 2369 */         if (text != null)
/* 2370 */           newButton.setText(text.toUnicodeString()); 
/* 2371 */         PdfNumber tp = mk.getAsNumber(PdfName.TP);
/* 2372 */         if (tp != null)
/* 2373 */           newButton.setLayout(tp.intValue() + 1); 
/* 2374 */         PdfDictionary ifit = mk.getAsDict(PdfName.IF);
/* 2375 */         if (ifit != null) {
/* 2376 */           PdfName sw = ifit.getAsName(PdfName.SW);
/* 2377 */           if (sw != null) {
/* 2378 */             int scale = 1;
/* 2379 */             if (sw.equals(PdfName.B)) {
/* 2380 */               scale = 3;
/* 2381 */             } else if (sw.equals(PdfName.S)) {
/* 2382 */               scale = 4;
/* 2383 */             } else if (sw.equals(PdfName.N)) {
/* 2384 */               scale = 2;
/* 2385 */             }  newButton.setScaleIcon(scale);
/*      */           } 
/* 2387 */           sw = ifit.getAsName(PdfName.S);
/* 2388 */           if (sw != null && 
/* 2389 */             sw.equals(PdfName.A)) {
/* 2390 */             newButton.setProportionalIcon(false);
/*      */           }
/* 2392 */           PdfArray aj = ifit.getAsArray(PdfName.A);
/* 2393 */           if (aj != null && aj.size() == 2) {
/* 2394 */             float left = aj.getAsNumber(0).floatValue();
/* 2395 */             float bottom = aj.getAsNumber(1).floatValue();
/* 2396 */             newButton.setIconHorizontalAdjustment(left);
/* 2397 */             newButton.setIconVerticalAdjustment(bottom);
/*      */           } 
/* 2399 */           PdfBoolean fb = ifit.getAsBoolean(PdfName.FB);
/* 2400 */           if (fb != null && fb.booleanValue())
/* 2401 */             newButton.setIconFitToBounds(true); 
/*      */         } 
/* 2403 */         PdfObject i = mk.get(PdfName.I);
/* 2404 */         if (i != null && i.isIndirect())
/* 2405 */           newButton.setIconReference((PRIndirectReference)i); 
/*      */       } 
/* 2407 */       return newButton;
/* 2408 */     } catch (Exception e) {
/* 2409 */       throw new ExceptionConverter(e);
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
/*      */   public boolean replacePushbuttonField(String field, PdfFormField button) {
/* 2424 */     return replacePushbuttonField(field, button, 0);
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
/*      */   public boolean replacePushbuttonField(String field, PdfFormField button, int order) {
/* 2440 */     if (getFieldType(field) != 1)
/* 2441 */       return false; 
/* 2442 */     Item item = getFieldItem(field);
/* 2443 */     if (order >= item.size())
/* 2444 */       return false; 
/* 2445 */     PdfDictionary merged = item.getMerged(order);
/* 2446 */     PdfDictionary values = item.getValue(order);
/* 2447 */     PdfDictionary widgets = item.getWidget(order);
/* 2448 */     for (int k = 0; k < buttonRemove.length; k++) {
/* 2449 */       merged.remove(buttonRemove[k]);
/* 2450 */       values.remove(buttonRemove[k]);
/* 2451 */       widgets.remove(buttonRemove[k]);
/*      */     } 
/* 2453 */     for (PdfName element : button.getKeys()) {
/* 2454 */       PdfName key = element;
/* 2455 */       if (key.equals(PdfName.T))
/*      */         continue; 
/* 2457 */       if (key.equals(PdfName.FF)) {
/* 2458 */         values.put(key, button.get(key));
/*      */       } else {
/* 2460 */         widgets.put(key, button.get(key));
/* 2461 */       }  merged.put(key, button.get(key));
/* 2462 */       markUsed(values);
/* 2463 */       markUsed(widgets);
/*      */     } 
/* 2465 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doesSignatureFieldExist(String name) {
/* 2476 */     return (getBlankSignatureNames().contains(name) || getSignatureNames().contains(name));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Item
/*      */   {
/*      */     public static final int WRITE_MERGED = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int WRITE_WIDGET = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int WRITE_VALUE = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2510 */     protected ArrayList<PdfDictionary> values = new ArrayList<PdfDictionary>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2516 */     protected ArrayList<PdfDictionary> widgets = new ArrayList<PdfDictionary>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2522 */     protected ArrayList<PdfIndirectReference> widget_refs = new ArrayList<PdfIndirectReference>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2529 */     protected ArrayList<PdfDictionary> merged = new ArrayList<PdfDictionary>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2536 */     protected ArrayList<Integer> page = new ArrayList<Integer>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2542 */     protected ArrayList<Integer> tabOrder = new ArrayList<Integer>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void writeToAll(PdfName key, PdfObject value, int writeFlags) {
/* 2555 */       PdfDictionary curDict = null;
/* 2556 */       if ((writeFlags & 0x1) != 0) {
/* 2557 */         for (int i = 0; i < this.merged.size(); i++) {
/* 2558 */           curDict = getMerged(i);
/* 2559 */           curDict.put(key, value);
/*      */         } 
/*      */       }
/* 2562 */       if ((writeFlags & 0x2) != 0) {
/* 2563 */         for (int i = 0; i < this.widgets.size(); i++) {
/* 2564 */           curDict = getWidget(i);
/* 2565 */           curDict.put(key, value);
/*      */         } 
/*      */       }
/* 2568 */       if ((writeFlags & 0x4) != 0) {
/* 2569 */         for (int i = 0; i < this.values.size(); i++) {
/* 2570 */           curDict = getValue(i);
/* 2571 */           curDict.put(key, value);
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
/*      */     public void markUsed(AcroFields parentFields, int writeFlags) {
/* 2583 */       if ((writeFlags & 0x4) != 0) {
/* 2584 */         for (int i = 0; i < size(); i++) {
/* 2585 */           parentFields.markUsed(getValue(i));
/*      */         }
/*      */       }
/* 2588 */       if ((writeFlags & 0x2) != 0) {
/* 2589 */         for (int i = 0; i < size(); i++) {
/* 2590 */           parentFields.markUsed(getWidget(i));
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
/*      */     public int size() {
/* 2603 */       return this.values.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void remove(int killIdx) {
/* 2614 */       this.values.remove(killIdx);
/* 2615 */       this.widgets.remove(killIdx);
/* 2616 */       this.widget_refs.remove(killIdx);
/* 2617 */       this.merged.remove(killIdx);
/* 2618 */       this.page.remove(killIdx);
/* 2619 */       this.tabOrder.remove(killIdx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfDictionary getValue(int idx) {
/* 2630 */       return this.values.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addValue(PdfDictionary value) {
/* 2640 */       this.values.add(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfDictionary getWidget(int idx) {
/* 2651 */       return this.widgets.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addWidget(PdfDictionary widget) {
/* 2661 */       this.widgets.add(widget);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PdfIndirectReference getWidgetRef(int idx) {
/* 2672 */       return this.widget_refs.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addWidgetRef(PdfIndirectReference widgRef) {
/* 2682 */       this.widget_refs.add(widgRef);
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
/*      */     public PdfDictionary getMerged(int idx) {
/* 2696 */       return this.merged.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addMerged(PdfDictionary mergeDict) {
/* 2706 */       this.merged.add(mergeDict);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Integer getPage(int idx) {
/* 2717 */       return this.page.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addPage(int pg) {
/* 2727 */       this.page.add(Integer.valueOf(pg));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void forcePage(int idx, int pg) {
/* 2737 */       this.page.set(idx, Integer.valueOf(pg));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Integer getTabOrder(int idx) {
/* 2748 */       return this.tabOrder.get(idx);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addTabOrder(int order) {
/* 2758 */       this.tabOrder.add(Integer.valueOf(order));
/*      */     }
/*      */   }
/*      */   
/*      */   private static class InstHit {
/*      */     IntHashtable hits;
/*      */     
/*      */     public InstHit(int[] inst) {
/* 2766 */       if (inst == null)
/*      */         return; 
/* 2768 */       this.hits = new IntHashtable();
/* 2769 */       for (int k = 0; k < inst.length; k++)
/* 2770 */         this.hits.put(inst[k], 1); 
/*      */     }
/*      */     
/*      */     public boolean isHit(int n) {
/* 2774 */       if (this.hits == null)
/* 2775 */         return true; 
/* 2776 */       return this.hits.containsKey(n);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FieldPosition
/*      */   {
/*      */     public int page;
/*      */     public Rectangle position;
/*      */   }
/*      */   
/*      */   private static class SorterComparator
/*      */     implements Comparator<Object[]>
/*      */   {
/*      */     private SorterComparator() {}
/*      */     
/*      */     public int compare(Object[] o1, Object[] o2) {
/* 2792 */       int n1 = ((int[])o1[1])[0];
/* 2793 */       int n2 = ((int[])o2[1])[0];
/* 2794 */       return n1 - n2;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ContentsChecker
/*      */     extends PdfReader
/*      */   {
/*      */     private long contentsStart;
/*      */     private long contentsEnd;
/* 2803 */     private int currentLevel = 0;
/* 2804 */     private int contentsLevel = 1;
/*      */     
/*      */     private boolean searchInV = true;
/*      */     private boolean rangeIsCorrect = false;
/*      */     
/*      */     public ContentsChecker(RandomAccessFileOrArray raf) throws IOException {
/* 2810 */       super(raf, (byte[])null);
/*      */     } public boolean checkWhetherSignatureCoversWholeDocument(AcroFields.Item signatureField) {
/*      */       PdfDictionary signature;
/*      */       int objNum;
/* 2814 */       this.rangeIsCorrect = false;
/*      */ 
/*      */       
/* 2817 */       if (signatureField.getValue(0).get(PdfName.V) instanceof PRIndirectReference) {
/* 2818 */         objNum = ((PdfIndirectReference)signatureField.getValue(0).get(PdfName.V)).number;
/* 2819 */         signature = (PdfDictionary)getPdfObject(objNum);
/* 2820 */         this.searchInV = true;
/*      */       } else {
/* 2822 */         signature = (PdfDictionary)signatureField.getValue(0).get(PdfName.V);
/* 2823 */         objNum = (signatureField.getWidgetRef(0)).number;
/* 2824 */         this.searchInV = false;
/* 2825 */         this.contentsLevel++;
/*      */       } 
/*      */       
/* 2828 */       long[] byteRange = ((PdfArray)signature.get(PdfName.BYTERANGE)).asLongArray();
/* 2829 */       if (4 != byteRange.length || 0L != byteRange[0] || getFileLength() != byteRange[2] + byteRange[3]) {
/* 2830 */         return false;
/*      */       }
/*      */       
/* 2833 */       this.contentsStart = byteRange[1];
/* 2834 */       this.contentsEnd = byteRange[2];
/*      */       
/* 2836 */       long signatureOffset = this.xref[2 * objNum];
/*      */       try {
/* 2838 */         this.tokens.seek(signatureOffset);
/* 2839 */         this.tokens.nextValidToken();
/* 2840 */         this.tokens.nextValidToken();
/* 2841 */         this.tokens.nextValidToken();
/* 2842 */         readPRObject();
/* 2843 */       } catch (Exception e) {
/*      */         
/* 2845 */         return false;
/*      */       } 
/* 2847 */       return this.rangeIsCorrect;
/*      */     }
/*      */ 
/*      */     
/*      */     protected PdfDictionary readDictionary() throws IOException {
/* 2852 */       this.currentLevel++;
/* 2853 */       PdfDictionary dic = new PdfDictionary();
/* 2854 */       while (!this.rangeIsCorrect) {
/* 2855 */         PdfObject obj; this.tokens.nextValidToken();
/* 2856 */         if (this.tokens.getTokenType() == PRTokeniser.TokenType.END_DIC) {
/* 2857 */           this.currentLevel--;
/*      */           break;
/*      */         } 
/* 2860 */         if (this.tokens.getTokenType() != PRTokeniser.TokenType.NAME) {
/* 2861 */           this.tokens.throwError(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", new Object[] { this.tokens.getStringValue() }));
/*      */         }
/* 2863 */         PdfName name = new PdfName(this.tokens.getStringValue(), false);
/*      */         
/* 2865 */         if (PdfName.CONTENTS.equals(name) && this.searchInV && this.contentsLevel == this.currentLevel) {
/* 2866 */           int ch; long startPosition = this.tokens.getFilePointer();
/*      */           
/* 2868 */           int whiteSpacesCount = -1;
/*      */           do {
/* 2870 */             ch = this.tokens.read();
/* 2871 */             whiteSpacesCount++;
/* 2872 */           } while (ch != -1 && PRTokeniser.isWhitespace(ch));
/* 2873 */           this.tokens.seek(startPosition);
/* 2874 */           obj = readPRObject();
/* 2875 */           long endPosition = this.tokens.getFilePointer();
/* 2876 */           if (endPosition == this.contentsEnd && startPosition + whiteSpacesCount == this.contentsStart) {
/* 2877 */             this.rangeIsCorrect = true;
/*      */           }
/* 2879 */         } else if (PdfName.V.equals(name) && !this.searchInV && 1 == this.currentLevel) {
/* 2880 */           this.searchInV = true;
/* 2881 */           obj = readPRObject();
/* 2882 */           this.searchInV = false;
/*      */         } else {
/* 2884 */           obj = readPRObject();
/*      */         } 
/* 2886 */         int type = obj.type();
/* 2887 */         if (-type == PRTokeniser.TokenType.END_DIC.ordinal())
/* 2888 */           this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.gt.gt", new Object[0])); 
/* 2889 */         if (-type == PRTokeniser.TokenType.END_ARRAY.ordinal())
/* 2890 */           this.tokens.throwError(MessageLocalization.getComposedMessage("unexpected.close.bracket", new Object[0])); 
/* 2891 */         dic.put(name, obj);
/*      */       } 
/* 2893 */       return dic;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/AcroFields.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */