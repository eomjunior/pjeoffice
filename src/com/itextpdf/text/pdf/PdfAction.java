/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.collection.PdfTargetDictionary;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfAction
/*     */   extends PdfDictionary
/*     */ {
/*     */   public static final int FIRSTPAGE = 1;
/*     */   public static final int PREVPAGE = 2;
/*     */   public static final int NEXTPAGE = 3;
/*     */   public static final int LASTPAGE = 4;
/*     */   public static final int PRINTDIALOG = 5;
/*     */   public static final int SUBMIT_EXCLUDE = 1;
/*     */   public static final int SUBMIT_INCLUDE_NO_VALUE_FIELDS = 2;
/*     */   public static final int SUBMIT_HTML_FORMAT = 4;
/*     */   public static final int SUBMIT_HTML_GET = 8;
/*     */   public static final int SUBMIT_COORDINATES = 16;
/*     */   public static final int SUBMIT_XFDF = 32;
/*     */   public static final int SUBMIT_INCLUDE_APPEND_SAVES = 64;
/*     */   public static final int SUBMIT_INCLUDE_ANNOTATIONS = 128;
/*     */   public static final int SUBMIT_PDF = 256;
/*     */   public static final int SUBMIT_CANONICAL_FORMAT = 512;
/*     */   public static final int SUBMIT_EXCL_NON_USER_ANNOTS = 1024;
/*     */   public static final int SUBMIT_EXCL_F_KEY = 2048;
/*     */   public static final int SUBMIT_EMBED_FORM = 8196;
/*     */   public static final int RESET_EXCLUDE = 1;
/*     */   
/*     */   public PdfAction() {}
/*     */   
/*     */   public PdfAction(URL url) {
/* 123 */     this(url.toExternalForm());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(URL url, boolean isMap) {
/* 132 */     this(url.toExternalForm(), isMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(String url) {
/* 142 */     this(url, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(String url, boolean isMap) {
/* 152 */     put(PdfName.S, PdfName.URI);
/* 153 */     put(PdfName.URI, new PdfString(url));
/* 154 */     if (isMap) {
/* 155 */       put(PdfName.ISMAP, PdfBoolean.PDFTRUE);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfAction(PdfIndirectReference destination) {
/* 164 */     put(PdfName.S, PdfName.GOTO);
/* 165 */     put(PdfName.D, destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(String filename, String name) {
/* 175 */     put(PdfName.S, PdfName.GOTOR);
/* 176 */     put(PdfName.F, new PdfString(filename));
/* 177 */     put(PdfName.D, new PdfString(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(String filename, int page) {
/* 187 */     put(PdfName.S, PdfName.GOTOR);
/* 188 */     put(PdfName.F, new PdfString(filename));
/* 189 */     put(PdfName.D, new PdfLiteral("[" + (page - 1) + " /FitH 10000]"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAction(int named) {
/* 197 */     put(PdfName.S, PdfName.NAMED);
/* 198 */     switch (named) {
/*     */       case 1:
/* 200 */         put(PdfName.N, PdfName.FIRSTPAGE);
/*     */         return;
/*     */       case 4:
/* 203 */         put(PdfName.N, PdfName.LASTPAGE);
/*     */         return;
/*     */       case 3:
/* 206 */         put(PdfName.N, PdfName.NEXTPAGE);
/*     */         return;
/*     */       case 2:
/* 209 */         put(PdfName.N, PdfName.PREVPAGE);
/*     */         return;
/*     */       case 5:
/* 212 */         put(PdfName.S, PdfName.JAVASCRIPT);
/* 213 */         put(PdfName.JS, new PdfString("this.print(true);\r"));
/*     */         return;
/*     */     } 
/* 216 */     throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.named.action", new Object[0]));
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
/*     */   public PdfAction(String application, String parameters, String operation, String defaultDir) {
/* 231 */     put(PdfName.S, PdfName.LAUNCH);
/* 232 */     if (parameters == null && operation == null && defaultDir == null) {
/* 233 */       put(PdfName.F, new PdfString(application));
/*     */     } else {
/* 235 */       PdfDictionary dic = new PdfDictionary();
/* 236 */       dic.put(PdfName.F, new PdfString(application));
/* 237 */       if (parameters != null)
/* 238 */         dic.put(PdfName.P, new PdfString(parameters)); 
/* 239 */       if (operation != null)
/* 240 */         dic.put(PdfName.O, new PdfString(operation)); 
/* 241 */       if (defaultDir != null)
/* 242 */         dic.put(PdfName.D, new PdfString(defaultDir)); 
/* 243 */       put(PdfName.WIN, dic);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createLaunch(String application, String parameters, String operation, String defaultDir) {
/* 259 */     return new PdfAction(application, parameters, operation, defaultDir);
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
/*     */   public static PdfAction rendition(String file, PdfFileSpecification fs, String mimeType, PdfIndirectReference ref) throws IOException {
/* 271 */     PdfAction js = new PdfAction();
/* 272 */     js.put(PdfName.S, PdfName.RENDITION);
/* 273 */     js.put(PdfName.R, new PdfRendition(file, fs, mimeType));
/* 274 */     js.put(new PdfName("OP"), new PdfNumber(0));
/* 275 */     js.put(new PdfName("AN"), ref);
/* 276 */     return js;
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
/*     */   public static PdfAction javaScript(String code, PdfWriter writer, boolean unicode) {
/* 290 */     PdfAction js = new PdfAction();
/* 291 */     js.put(PdfName.S, PdfName.JAVASCRIPT);
/* 292 */     if (unicode && code.length() < 50) {
/* 293 */       js.put(PdfName.JS, new PdfString(code, "UnicodeBig"));
/*     */     }
/* 295 */     else if (!unicode && code.length() < 100) {
/* 296 */       js.put(PdfName.JS, new PdfString(code));
/*     */     } else {
/*     */       
/*     */       try {
/* 300 */         byte[] b = PdfEncodings.convertToBytes(code, unicode ? "UnicodeBig" : "PDF");
/* 301 */         PdfStream stream = new PdfStream(b);
/* 302 */         stream.flateCompress(writer.getCompressionLevel());
/* 303 */         js.put(PdfName.JS, writer.addToBody(stream).getIndirectReference());
/*     */       }
/* 305 */       catch (Exception e) {
/* 306 */         js.put(PdfName.JS, new PdfString(code));
/*     */       } 
/*     */     } 
/* 309 */     return js;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction javaScript(String code, PdfWriter writer) {
/* 320 */     return javaScript(code, writer, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static PdfAction createHide(PdfObject obj, boolean hide) {
/* 330 */     PdfAction action = new PdfAction();
/* 331 */     action.put(PdfName.S, PdfName.HIDE);
/* 332 */     action.put(PdfName.T, obj);
/* 333 */     if (!hide)
/* 334 */       action.put(PdfName.H, PdfBoolean.PDFFALSE); 
/* 335 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createHide(PdfAnnotation annot, boolean hide) {
/* 345 */     return createHide(annot.getIndirectReference(), hide);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createHide(String name, boolean hide) {
/* 355 */     return createHide(new PdfString(name), hide);
/*     */   }
/*     */   
/*     */   static PdfArray buildArray(Object[] names) {
/* 359 */     PdfArray array = new PdfArray();
/* 360 */     for (int k = 0; k < names.length; k++) {
/* 361 */       Object obj = names[k];
/* 362 */       if (obj instanceof String) {
/* 363 */         array.add(new PdfString((String)obj));
/* 364 */       } else if (obj instanceof PdfAnnotation) {
/* 365 */         array.add(((PdfAnnotation)obj).getIndirectReference());
/*     */       } else {
/* 367 */         throw new RuntimeException(MessageLocalization.getComposedMessage("the.array.must.contain.string.or.pdfannotation", new Object[0]));
/*     */       } 
/* 369 */     }  return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createHide(Object[] names, boolean hide) {
/* 379 */     return createHide(buildArray(names), hide);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createSubmitForm(String file, Object[] names, int flags) {
/* 390 */     PdfAction action = new PdfAction();
/* 391 */     action.put(PdfName.S, PdfName.SUBMITFORM);
/* 392 */     PdfDictionary dic = new PdfDictionary();
/* 393 */     dic.put(PdfName.F, new PdfString(file));
/* 394 */     dic.put(PdfName.FS, PdfName.URL);
/* 395 */     action.put(PdfName.F, dic);
/* 396 */     if (names != null)
/* 397 */       action.put(PdfName.FIELDS, buildArray(names)); 
/* 398 */     action.put(PdfName.FLAGS, new PdfNumber(flags));
/* 399 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createResetForm(Object[] names, int flags) {
/* 409 */     PdfAction action = new PdfAction();
/* 410 */     action.put(PdfName.S, PdfName.RESETFORM);
/* 411 */     if (names != null)
/* 412 */       action.put(PdfName.FIELDS, buildArray(names)); 
/* 413 */     action.put(PdfName.FLAGS, new PdfNumber(flags));
/* 414 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction createImportData(String file) {
/* 423 */     PdfAction action = new PdfAction();
/* 424 */     action.put(PdfName.S, PdfName.IMPORTDATA);
/* 425 */     action.put(PdfName.F, new PdfString(file));
/* 426 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void next(PdfAction na) {
/* 433 */     PdfObject nextAction = get(PdfName.NEXT);
/* 434 */     if (nextAction == null) {
/* 435 */       put(PdfName.NEXT, na);
/* 436 */     } else if (nextAction.isDictionary()) {
/* 437 */       PdfArray array = new PdfArray(nextAction);
/* 438 */       array.add(na);
/* 439 */       put(PdfName.NEXT, array);
/*     */     } else {
/*     */       
/* 442 */       ((PdfArray)nextAction).add(na);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction gotoLocalPage(int page, PdfDestination dest, PdfWriter writer) {
/* 453 */     PdfIndirectReference ref = writer.getPageReference(page);
/* 454 */     PdfDestination d = new PdfDestination(dest);
/* 455 */     d.addPage(ref);
/* 456 */     PdfAction action = new PdfAction();
/* 457 */     action.put(PdfName.S, PdfName.GOTO);
/* 458 */     action.put(PdfName.D, d);
/* 459 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction gotoLocalPage(String dest, boolean isName) {
/* 469 */     PdfAction action = new PdfAction();
/* 470 */     action.put(PdfName.S, PdfName.GOTO);
/* 471 */     if (isName) {
/* 472 */       action.put(PdfName.D, new PdfName(dest));
/*     */     } else {
/* 474 */       action.put(PdfName.D, new PdfString(dest, "UnicodeBig"));
/* 475 */     }  return action;
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
/*     */   public static PdfAction gotoRemotePage(String filename, String dest, boolean isName, boolean newWindow) {
/* 487 */     PdfAction action = new PdfAction();
/* 488 */     action.put(PdfName.F, new PdfString(filename));
/* 489 */     action.put(PdfName.S, PdfName.GOTOR);
/* 490 */     if (isName) {
/* 491 */       action.put(PdfName.D, new PdfName(dest));
/*     */     } else {
/* 493 */       action.put(PdfName.D, new PdfString(dest, "UnicodeBig"));
/* 494 */     }  if (newWindow)
/* 495 */       action.put(PdfName.NEWWINDOW, PdfBoolean.PDFTRUE); 
/* 496 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAction gotoEmbedded(String filename, PdfTargetDictionary target, String dest, boolean isName, boolean newWindow) {
/* 507 */     if (isName) {
/* 508 */       return gotoEmbedded(filename, target, new PdfName(dest), newWindow);
/*     */     }
/* 510 */     return gotoEmbedded(filename, target, new PdfString(dest, "UnicodeBig"), newWindow);
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
/*     */   public static PdfAction gotoEmbedded(String filename, PdfTargetDictionary target, PdfObject dest, boolean newWindow) {
/* 522 */     PdfAction action = new PdfAction();
/* 523 */     action.put(PdfName.S, PdfName.GOTOE);
/* 524 */     action.put(PdfName.T, (PdfObject)target);
/* 525 */     action.put(PdfName.D, dest);
/* 526 */     action.put(PdfName.NEWWINDOW, new PdfBoolean(newWindow));
/* 527 */     if (filename != null) {
/* 528 */       action.put(PdfName.F, new PdfString(filename));
/*     */     }
/* 530 */     return action;
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
/*     */   public static PdfAction setOCGstate(ArrayList<Object> state, boolean preserveRB) {
/* 556 */     PdfAction action = new PdfAction();
/* 557 */     action.put(PdfName.S, PdfName.SETOCGSTATE);
/* 558 */     PdfArray a = new PdfArray();
/* 559 */     for (int k = 0; k < state.size(); k++) {
/* 560 */       Object o = state.get(k);
/* 561 */       if (o != null)
/*     */       {
/* 563 */         if (o instanceof PdfIndirectReference) {
/* 564 */           a.add((PdfIndirectReference)o);
/* 565 */         } else if (o instanceof PdfLayer) {
/* 566 */           a.add(((PdfLayer)o).getRef());
/* 567 */         } else if (o instanceof PdfName) {
/* 568 */           a.add((PdfName)o);
/* 569 */         } else if (o instanceof String) {
/* 570 */           PdfName name = null;
/* 571 */           String s = (String)o;
/* 572 */           if (s.equalsIgnoreCase("on")) {
/* 573 */             name = PdfName.ON;
/* 574 */           } else if (s.equalsIgnoreCase("off")) {
/* 575 */             name = PdfName.OFF;
/* 576 */           } else if (s.equalsIgnoreCase("toggle")) {
/* 577 */             name = PdfName.TOGGLE;
/*     */           } else {
/* 579 */             throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.string.1.was.passed.in.state.only.on.off.and.toggle.are.allowed", new Object[] { s }));
/* 580 */           }  a.add(name);
/*     */         } else {
/*     */           
/* 583 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.type.was.passed.in.state.1", new Object[] { o.getClass().getName() }));
/*     */         }  } 
/* 585 */     }  action.put(PdfName.STATE, a);
/* 586 */     if (!preserveRB)
/* 587 */       action.put(PdfName.PRESERVERB, PdfBoolean.PDFFALSE); 
/* 588 */     return action;
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 593 */     PdfWriter.checkPdfIsoConformance(writer, 14, this);
/* 594 */     super.toPdf(writer, os);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */