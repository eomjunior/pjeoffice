/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.xml.XMLUtil;
/*     */ import com.itextpdf.text.xml.simpleparser.IanaEncodings;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpleBookmark
/*     */   implements SimpleXMLDocHandler
/*     */ {
/*     */   private ArrayList<HashMap<String, Object>> topList;
/* 110 */   private final Stack<HashMap<String, Object>> attr = new Stack<HashMap<String, Object>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<HashMap<String, Object>> bookmarkDepth(PdfReader reader, PdfDictionary outline, IntHashtable pages, boolean processCurrentOutlineOnly) {
/* 117 */     ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
/* 118 */     while (outline != null) {
/* 119 */       HashMap<String, Object> map = new HashMap<String, Object>();
/* 120 */       PdfString title = (PdfString)PdfReader.getPdfObjectRelease(outline.get(PdfName.TITLE));
/* 121 */       map.put("Title", title.toUnicodeString());
/* 122 */       PdfArray color = (PdfArray)PdfReader.getPdfObjectRelease(outline.get(PdfName.C));
/* 123 */       if (color != null && color.size() == 3) {
/* 124 */         ByteBuffer out = new ByteBuffer();
/* 125 */         out.append(color.getAsNumber(0).floatValue()).append(' ');
/* 126 */         out.append(color.getAsNumber(1).floatValue()).append(' ');
/* 127 */         out.append(color.getAsNumber(2).floatValue());
/* 128 */         map.put("Color", PdfEncodings.convertToString(out.toByteArray(), null));
/*     */       } 
/* 130 */       PdfNumber style = (PdfNumber)PdfReader.getPdfObjectRelease(outline.get(PdfName.F));
/* 131 */       if (style != null) {
/* 132 */         int f = style.intValue();
/* 133 */         String s = "";
/* 134 */         if ((f & 0x1) != 0)
/* 135 */           s = s + "italic "; 
/* 136 */         if ((f & 0x2) != 0)
/* 137 */           s = s + "bold "; 
/* 138 */         s = s.trim();
/* 139 */         if (s.length() != 0)
/* 140 */           map.put("Style", s); 
/*     */       } 
/* 142 */       PdfNumber count = (PdfNumber)PdfReader.getPdfObjectRelease(outline.get(PdfName.COUNT));
/* 143 */       if (count != null && count.intValue() < 0)
/* 144 */         map.put("Open", "false"); 
/*     */       try {
/* 146 */         PdfObject dest = PdfReader.getPdfObjectRelease(outline.get(PdfName.DEST));
/* 147 */         if (dest != null) {
/* 148 */           mapGotoBookmark(map, dest, pages);
/*     */         } else {
/*     */           
/* 151 */           PdfDictionary action = (PdfDictionary)PdfReader.getPdfObjectRelease(outline.get(PdfName.A));
/* 152 */           if (action != null) {
/* 153 */             if (PdfName.GOTO.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.S)))) {
/* 154 */               dest = PdfReader.getPdfObjectRelease(action.get(PdfName.D));
/* 155 */               if (dest != null) {
/* 156 */                 mapGotoBookmark(map, dest, pages);
/*     */               }
/*     */             }
/* 159 */             else if (PdfName.URI.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.S)))) {
/* 160 */               map.put("Action", "URI");
/* 161 */               map.put("URI", ((PdfString)PdfReader.getPdfObjectRelease(action.get(PdfName.URI))).toUnicodeString());
/*     */             }
/* 163 */             else if (PdfName.JAVASCRIPT.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.S)))) {
/* 164 */               map.put("Action", "JS");
/* 165 */               map.put("Code", PdfReader.getPdfObjectRelease(action.get(PdfName.JS)).toString());
/*     */             }
/* 167 */             else if (PdfName.GOTOR.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.S)))) {
/* 168 */               dest = PdfReader.getPdfObjectRelease(action.get(PdfName.D));
/* 169 */               if (dest != null) {
/* 170 */                 if (dest.isString()) {
/* 171 */                   map.put("Named", dest.toString());
/* 172 */                 } else if (dest.isName()) {
/* 173 */                   map.put("NamedN", PdfName.decodeName(dest.toString()));
/* 174 */                 } else if (dest.isArray()) {
/* 175 */                   PdfArray arr = (PdfArray)dest;
/* 176 */                   StringBuffer s = new StringBuffer();
/* 177 */                   s.append(arr.getPdfObject(0).toString());
/* 178 */                   s.append(' ').append(arr.getPdfObject(1).toString());
/* 179 */                   for (int k = 2; k < arr.size(); k++)
/* 180 */                     s.append(' ').append(arr.getPdfObject(k).toString()); 
/* 181 */                   map.put("Page", s.toString());
/*     */                 } 
/*     */               }
/* 184 */               map.put("Action", "GoToR");
/* 185 */               PdfObject file = PdfReader.getPdfObjectRelease(action.get(PdfName.F));
/* 186 */               if (file != null)
/* 187 */                 if (file.isString()) {
/* 188 */                   map.put("File", ((PdfString)file).toUnicodeString());
/* 189 */                 } else if (file.isDictionary()) {
/* 190 */                   file = PdfReader.getPdfObject(((PdfDictionary)file).get(PdfName.F));
/* 191 */                   if (file.isString()) {
/* 192 */                     map.put("File", ((PdfString)file).toUnicodeString());
/*     */                   }
/*     */                 }  
/* 195 */               PdfObject newWindow = PdfReader.getPdfObjectRelease(action.get(PdfName.NEWWINDOW));
/* 196 */               if (newWindow != null) {
/* 197 */                 map.put("NewWindow", newWindow.toString());
/*     */               }
/* 199 */             } else if (PdfName.LAUNCH.equals(PdfReader.getPdfObjectRelease(action.get(PdfName.S)))) {
/* 200 */               map.put("Action", "Launch");
/* 201 */               PdfObject file = PdfReader.getPdfObjectRelease(action.get(PdfName.F));
/* 202 */               if (file == null)
/* 203 */                 file = PdfReader.getPdfObjectRelease(action.get(PdfName.WIN)); 
/* 204 */               if (file != null) {
/* 205 */                 if (file.isString()) {
/* 206 */                   map.put("File", ((PdfString)file).toUnicodeString());
/* 207 */                 } else if (file.isDictionary()) {
/* 208 */                   file = PdfReader.getPdfObjectRelease(((PdfDictionary)file).get(PdfName.F));
/* 209 */                   if (file.isString()) {
/* 210 */                     map.put("File", ((PdfString)file).toUnicodeString());
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/* 217 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 220 */       PdfDictionary first = (PdfDictionary)PdfReader.getPdfObjectRelease(outline.get(PdfName.FIRST));
/* 221 */       if (first != null) {
/* 222 */         map.put("Kids", bookmarkDepth(reader, first, pages, false));
/*     */       }
/* 224 */       list.add(map);
/* 225 */       if (!processCurrentOutlineOnly) {
/* 226 */         outline = (PdfDictionary)PdfReader.getPdfObjectRelease(outline.get(PdfName.NEXT)); continue;
/*     */       } 
/* 228 */       outline = null;
/*     */     } 
/* 230 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void mapGotoBookmark(HashMap<String, Object> map, PdfObject dest, IntHashtable pages) {
/* 235 */     if (dest.isString()) {
/* 236 */       map.put("Named", dest.toString());
/* 237 */     } else if (dest.isName()) {
/* 238 */       map.put("Named", PdfName.decodeName(dest.toString()));
/* 239 */     } else if (dest.isArray()) {
/* 240 */       map.put("Page", makeBookmarkParam((PdfArray)dest, pages));
/* 241 */     }  map.put("Action", "GoTo");
/*     */   }
/*     */ 
/*     */   
/*     */   private static String makeBookmarkParam(PdfArray dest, IntHashtable pages) {
/* 246 */     StringBuffer s = new StringBuffer();
/* 247 */     PdfObject obj = dest.getPdfObject(0);
/* 248 */     if (obj.isNumber()) {
/* 249 */       s.append(((PdfNumber)obj).intValue() + 1);
/*     */     } else {
/* 251 */       s.append(pages.get(getNumber((PdfIndirectReference)obj)));
/* 252 */     }  s.append(' ').append(dest.getPdfObject(1).toString().substring(1));
/* 253 */     for (int k = 2; k < dest.size(); k++)
/* 254 */       s.append(' ').append(dest.getPdfObject(k).toString()); 
/* 255 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getNumber(PdfIndirectReference indirect) {
/* 266 */     PdfDictionary pdfObj = (PdfDictionary)PdfReader.getPdfObjectRelease(indirect);
/* 267 */     if (pdfObj.contains(PdfName.TYPE) && pdfObj.get(PdfName.TYPE).equals(PdfName.PAGES) && pdfObj.contains(PdfName.KIDS)) {
/*     */       
/* 269 */       PdfArray kids = (PdfArray)pdfObj.get(PdfName.KIDS);
/* 270 */       indirect = (PdfIndirectReference)kids.getPdfObject(0);
/*     */     } 
/* 272 */     return indirect.getNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<HashMap<String, Object>> getBookmark(PdfReader reader) {
/* 283 */     PdfDictionary catalog = reader.getCatalog();
/* 284 */     PdfObject obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.OUTLINES));
/* 285 */     if (obj == null || !obj.isDictionary())
/* 286 */       return null; 
/* 287 */     PdfDictionary outlines = (PdfDictionary)obj;
/* 288 */     return getBookmark(reader, outlines, false);
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
/*     */   public static List<HashMap<String, Object>> getBookmark(PdfReader reader, PdfDictionary outline, boolean includeRoot) {
/* 301 */     PdfDictionary catalog = reader.getCatalog();
/* 302 */     if (outline == null)
/* 303 */       return null; 
/* 304 */     IntHashtable pages = new IntHashtable();
/* 305 */     int numPages = reader.getNumberOfPages();
/* 306 */     for (int k = 1; k <= numPages; k++) {
/* 307 */       pages.put(reader.getPageOrigRef(k).getNumber(), k);
/* 308 */       reader.releasePage(k);
/*     */     } 
/* 310 */     if (includeRoot) {
/* 311 */       return bookmarkDepth(reader, outline, pages, true);
/*     */     }
/* 313 */     return bookmarkDepth(reader, (PdfDictionary)PdfReader.getPdfObjectRelease(outline.get(PdfName.FIRST)), pages, false);
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
/*     */   public static void eliminatePages(List<HashMap<String, Object>> list, int[] pageRange) {
/* 325 */     if (list == null)
/*     */       return; 
/* 327 */     for (Iterator<HashMap<String, Object>> it = list.listIterator(); it.hasNext(); ) {
/* 328 */       HashMap<String, Object> map = it.next();
/* 329 */       boolean hit = false;
/* 330 */       if ("GoTo".equals(map.get("Action"))) {
/* 331 */         String page = (String)map.get("Page");
/* 332 */         if (page != null) {
/* 333 */           int pageNum; page = page.trim();
/* 334 */           int idx = page.indexOf(' ');
/*     */           
/* 336 */           if (idx < 0) {
/* 337 */             pageNum = Integer.parseInt(page);
/*     */           } else {
/* 339 */             pageNum = Integer.parseInt(page.substring(0, idx));
/* 340 */           }  int len = pageRange.length & 0xFFFFFFFE;
/* 341 */           for (int k = 0; k < len; k += 2) {
/* 342 */             if (pageNum >= pageRange[k] && pageNum <= pageRange[k + 1]) {
/* 343 */               hit = true;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 349 */       List<HashMap<String, Object>> kids = (List<HashMap<String, Object>>)map.get("Kids");
/* 350 */       if (kids != null) {
/* 351 */         eliminatePages(kids, pageRange);
/* 352 */         if (kids.isEmpty()) {
/* 353 */           map.remove("Kids");
/* 354 */           kids = null;
/*     */         } 
/*     */       } 
/* 357 */       if (hit) {
/* 358 */         if (kids == null) {
/* 359 */           it.remove(); continue;
/*     */         } 
/* 361 */         map.remove("Action");
/* 362 */         map.remove("Page");
/* 363 */         map.remove("Named");
/*     */       } 
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
/*     */   
/*     */   public static void shiftPageNumbers(List<HashMap<String, Object>> list, int pageShift, int[] pageRange) {
/* 381 */     if (list == null)
/*     */       return; 
/* 383 */     for (Iterator<HashMap<String, Object>> it = list.listIterator(); it.hasNext(); ) {
/* 384 */       HashMap<String, Object> map = it.next();
/* 385 */       if ("GoTo".equals(map.get("Action"))) {
/* 386 */         String page = (String)map.get("Page");
/* 387 */         if (page != null) {
/* 388 */           int pageNum; page = page.trim();
/* 389 */           int idx = page.indexOf(' ');
/*     */           
/* 391 */           if (idx < 0) {
/* 392 */             pageNum = Integer.parseInt(page);
/*     */           } else {
/* 394 */             pageNum = Integer.parseInt(page.substring(0, idx));
/* 395 */           }  boolean hit = false;
/* 396 */           if (pageRange == null) {
/* 397 */             hit = true;
/*     */           } else {
/* 399 */             int len = pageRange.length & 0xFFFFFFFE;
/* 400 */             for (int k = 0; k < len; k += 2) {
/* 401 */               if (pageNum >= pageRange[k] && pageNum <= pageRange[k + 1]) {
/* 402 */                 hit = true;
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/* 407 */           if (hit)
/* 408 */             if (idx < 0) {
/* 409 */               page = Integer.toString(pageNum + pageShift);
/*     */             } else {
/* 411 */               page = (pageNum + pageShift) + page.substring(idx);
/*     */             }  
/* 413 */           map.put("Page", page);
/*     */         } 
/*     */       } 
/* 416 */       List<HashMap<String, Object>> kids = (List<HashMap<String, Object>>)map.get("Kids");
/* 417 */       if (kids != null)
/* 418 */         shiftPageNumbers(kids, pageShift, pageRange); 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void createOutlineAction(PdfDictionary outline, HashMap<String, Object> map, PdfWriter writer, boolean namedAsNames) {
/*     */     try {
/* 424 */       String action = (String)map.get("Action");
/* 425 */       if ("GoTo".equals(action)) {
/*     */         String p;
/* 427 */         if ((p = (String)map.get("Named")) != null) {
/* 428 */           if (namedAsNames) {
/* 429 */             outline.put(PdfName.DEST, new PdfName(p));
/*     */           } else {
/* 431 */             outline.put(PdfName.DEST, new PdfString(p, null));
/*     */           } 
/* 433 */         } else if ((p = (String)map.get("Page")) != null) {
/* 434 */           PdfArray ar = new PdfArray();
/* 435 */           StringTokenizer tk = new StringTokenizer(p);
/* 436 */           int n = Integer.parseInt(tk.nextToken());
/* 437 */           ar.add(writer.getPageReference(n));
/* 438 */           if (!tk.hasMoreTokens()) {
/* 439 */             ar.add(PdfName.XYZ);
/* 440 */             ar.add(new float[] { 0.0F, 10000.0F, 0.0F });
/*     */           } else {
/*     */             
/* 443 */             String fn = tk.nextToken();
/* 444 */             if (fn.startsWith("/"))
/* 445 */               fn = fn.substring(1); 
/* 446 */             ar.add(new PdfName(fn));
/* 447 */             for (int k = 0; k < 4 && tk.hasMoreTokens(); k++) {
/* 448 */               fn = tk.nextToken();
/* 449 */               if (fn.equals("null")) {
/* 450 */                 ar.add(PdfNull.PDFNULL);
/*     */               } else {
/* 452 */                 ar.add(new PdfNumber(fn));
/*     */               } 
/*     */             } 
/* 455 */           }  outline.put(PdfName.DEST, ar);
/*     */         }
/*     */       
/* 458 */       } else if ("GoToR".equals(action)) {
/*     */         
/* 460 */         PdfDictionary dic = new PdfDictionary(); String p;
/* 461 */         if ((p = (String)map.get("Named")) != null) {
/* 462 */           dic.put(PdfName.D, new PdfString(p, null));
/* 463 */         } else if ((p = (String)map.get("NamedN")) != null) {
/* 464 */           dic.put(PdfName.D, new PdfName(p));
/* 465 */         } else if ((p = (String)map.get("Page")) != null) {
/* 466 */           PdfArray ar = new PdfArray();
/* 467 */           StringTokenizer tk = new StringTokenizer(p);
/* 468 */           ar.add(new PdfNumber(tk.nextToken()));
/* 469 */           if (!tk.hasMoreTokens()) {
/* 470 */             ar.add(PdfName.XYZ);
/* 471 */             ar.add(new float[] { 0.0F, 10000.0F, 0.0F });
/*     */           } else {
/*     */             
/* 474 */             String fn = tk.nextToken();
/* 475 */             if (fn.startsWith("/"))
/* 476 */               fn = fn.substring(1); 
/* 477 */             ar.add(new PdfName(fn));
/* 478 */             for (int k = 0; k < 4 && tk.hasMoreTokens(); k++) {
/* 479 */               fn = tk.nextToken();
/* 480 */               if (fn.equals("null")) {
/* 481 */                 ar.add(PdfNull.PDFNULL);
/*     */               } else {
/* 483 */                 ar.add(new PdfNumber(fn));
/*     */               } 
/*     */             } 
/* 486 */           }  dic.put(PdfName.D, ar);
/*     */         } 
/* 488 */         String file = (String)map.get("File");
/* 489 */         if (dic.size() > 0 && file != null) {
/* 490 */           dic.put(PdfName.S, PdfName.GOTOR);
/* 491 */           dic.put(PdfName.F, new PdfString(file));
/* 492 */           String nw = (String)map.get("NewWindow");
/* 493 */           if (nw != null)
/* 494 */             if (nw.equals("true")) {
/* 495 */               dic.put(PdfName.NEWWINDOW, PdfBoolean.PDFTRUE);
/* 496 */             } else if (nw.equals("false")) {
/* 497 */               dic.put(PdfName.NEWWINDOW, PdfBoolean.PDFFALSE);
/*     */             }  
/* 499 */           outline.put(PdfName.A, dic);
/*     */         }
/*     */       
/* 502 */       } else if ("URI".equals(action)) {
/* 503 */         String uri = (String)map.get("URI");
/* 504 */         if (uri != null) {
/* 505 */           PdfDictionary dic = new PdfDictionary();
/* 506 */           dic.put(PdfName.S, PdfName.URI);
/* 507 */           dic.put(PdfName.URI, new PdfString(uri));
/* 508 */           outline.put(PdfName.A, dic);
/*     */         }
/*     */       
/* 511 */       } else if ("JS".equals(action)) {
/* 512 */         String code = (String)map.get("Code");
/* 513 */         if (code != null) {
/* 514 */           outline.put(PdfName.A, PdfAction.javaScript(code, writer));
/*     */         }
/*     */       }
/* 517 */       else if ("Launch".equals(action)) {
/* 518 */         String file = (String)map.get("File");
/* 519 */         if (file != null) {
/* 520 */           PdfDictionary dic = new PdfDictionary();
/* 521 */           dic.put(PdfName.S, PdfName.LAUNCH);
/* 522 */           dic.put(PdfName.F, new PdfString(file));
/* 523 */           outline.put(PdfName.A, dic);
/*     */         }
/*     */       
/*     */       } 
/* 527 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] iterateOutlines(PdfWriter writer, PdfIndirectReference parent, List<HashMap<String, Object>> kids, boolean namedAsNames) throws IOException {
/* 534 */     PdfIndirectReference[] refs = new PdfIndirectReference[kids.size()];
/* 535 */     for (int k = 0; k < refs.length; k++)
/* 536 */       refs[k] = writer.getPdfIndirectReference(); 
/* 537 */     int ptr = 0;
/* 538 */     int count = 0;
/* 539 */     for (Iterator<HashMap<String, Object>> it = kids.listIterator(); it.hasNext(); ptr++) {
/* 540 */       HashMap<String, Object> map = it.next();
/* 541 */       Object[] lower = null;
/* 542 */       List<HashMap<String, Object>> subKid = (List<HashMap<String, Object>>)map.get("Kids");
/* 543 */       if (subKid != null && !subKid.isEmpty())
/* 544 */         lower = iterateOutlines(writer, refs[ptr], subKid, namedAsNames); 
/* 545 */       PdfDictionary outline = new PdfDictionary();
/* 546 */       count++;
/* 547 */       if (lower != null) {
/* 548 */         outline.put(PdfName.FIRST, (PdfIndirectReference)lower[0]);
/* 549 */         outline.put(PdfName.LAST, (PdfIndirectReference)lower[1]);
/* 550 */         int n = ((Integer)lower[2]).intValue();
/* 551 */         if ("false".equals(map.get("Open"))) {
/* 552 */           outline.put(PdfName.COUNT, new PdfNumber(-n));
/*     */         } else {
/*     */           
/* 555 */           outline.put(PdfName.COUNT, new PdfNumber(n));
/* 556 */           count += n;
/*     */         } 
/*     */       } 
/* 559 */       outline.put(PdfName.PARENT, parent);
/* 560 */       if (ptr > 0)
/* 561 */         outline.put(PdfName.PREV, refs[ptr - 1]); 
/* 562 */       if (ptr < refs.length - 1)
/* 563 */         outline.put(PdfName.NEXT, refs[ptr + 1]); 
/* 564 */       outline.put(PdfName.TITLE, new PdfString((String)map.get("Title"), "UnicodeBig"));
/* 565 */       String color = (String)map.get("Color");
/* 566 */       if (color != null) {
/*     */         try {
/* 568 */           PdfArray arr = new PdfArray();
/* 569 */           StringTokenizer tk = new StringTokenizer(color);
/* 570 */           for (int i = 0; i < 3; i++) {
/* 571 */             float f = Float.parseFloat(tk.nextToken());
/* 572 */             if (f < 0.0F) f = 0.0F; 
/* 573 */             if (f > 1.0F) f = 1.0F; 
/* 574 */             arr.add(new PdfNumber(f));
/*     */           } 
/* 576 */           outline.put(PdfName.C, arr);
/* 577 */         } catch (Exception exception) {}
/*     */       }
/* 579 */       String style = (String)map.get("Style");
/* 580 */       if (style != null) {
/* 581 */         style = style.toLowerCase();
/* 582 */         int bits = 0;
/* 583 */         if (style.indexOf("italic") >= 0)
/* 584 */           bits |= 0x1; 
/* 585 */         if (style.indexOf("bold") >= 0)
/* 586 */           bits |= 0x2; 
/* 587 */         if (bits != 0)
/* 588 */           outline.put(PdfName.F, new PdfNumber(bits)); 
/*     */       } 
/* 590 */       createOutlineAction(outline, map, writer, namedAsNames);
/* 591 */       writer.addToBody(outline, refs[ptr]);
/*     */     } 
/* 593 */     return new Object[] { refs[0], refs[refs.length - 1], Integer.valueOf(count) };
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
/*     */   public static void exportToXMLNode(List<HashMap<String, Object>> list, Writer out, int indent, boolean onlyASCII) throws IOException {
/* 609 */     String dep = "";
/* 610 */     if (indent != -1)
/* 611 */       for (int k = 0; k < indent; k++) {
/* 612 */         dep = dep + "  ";
/*     */       } 
/* 614 */     for (HashMap<String, Object> map : list) {
/* 615 */       String title = null;
/* 616 */       out.write(dep);
/* 617 */       out.write("<Title ");
/* 618 */       List<HashMap<String, Object>> kids = null;
/* 619 */       for (Map.Entry<String, Object> entry : map.entrySet()) {
/* 620 */         String key = entry.getKey();
/* 621 */         if (key.equals("Title")) {
/* 622 */           title = (String)entry.getValue();
/*     */           continue;
/*     */         } 
/* 625 */         if (key.equals("Kids")) {
/* 626 */           kids = (List<HashMap<String, Object>>)entry.getValue();
/*     */           
/*     */           continue;
/*     */         } 
/* 630 */         out.write(key);
/* 631 */         out.write("=\"");
/* 632 */         String value = (String)entry.getValue();
/* 633 */         if (key.equals("Named") || key.equals("NamedN"))
/* 634 */           value = SimpleNamedDestination.escapeBinaryString(value); 
/* 635 */         out.write(XMLUtil.escapeXML(value, onlyASCII));
/* 636 */         out.write("\" ");
/*     */       } 
/*     */       
/* 639 */       out.write(">");
/* 640 */       if (title == null)
/* 641 */         title = ""; 
/* 642 */       out.write(XMLUtil.escapeXML(title, onlyASCII));
/* 643 */       if (kids != null) {
/* 644 */         out.write("\n");
/* 645 */         exportToXMLNode(kids, out, (indent == -1) ? indent : (indent + 1), onlyASCII);
/* 646 */         out.write(dep);
/*     */       } 
/* 648 */       out.write("</Title>\n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void exportToXML(List<HashMap<String, Object>> list, OutputStream out, String encoding, boolean onlyASCII) throws IOException {
/* 681 */     String jenc = IanaEncodings.getJavaEncoding(encoding);
/* 682 */     Writer wrt = new BufferedWriter(new OutputStreamWriter(out, jenc));
/* 683 */     exportToXML(list, wrt, encoding, onlyASCII);
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
/*     */   public static void exportToXML(List<HashMap<String, Object>> list, Writer wrt, String encoding, boolean onlyASCII) throws IOException {
/* 697 */     wrt.write("<?xml version=\"1.0\" encoding=\"");
/* 698 */     wrt.write(XMLUtil.escapeXML(encoding, onlyASCII));
/* 699 */     wrt.write("\"?>\n<Bookmark>\n");
/* 700 */     exportToXMLNode(list, wrt, 1, onlyASCII);
/* 701 */     wrt.write("</Bookmark>\n");
/* 702 */     wrt.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<HashMap<String, Object>> importFromXML(InputStream in) throws IOException {
/* 712 */     SimpleBookmark book = new SimpleBookmark();
/* 713 */     SimpleXMLParser.parse(book, in);
/* 714 */     return book.topList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<HashMap<String, Object>> importFromXML(Reader in) throws IOException {
/* 724 */     SimpleBookmark book = new SimpleBookmark();
/* 725 */     SimpleXMLParser.parse(book, in);
/* 726 */     return book.topList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */ 
/*     */   
/*     */   public void endElement(String tag) {
/* 734 */     if (tag.equals("Bookmark")) {
/* 735 */       if (this.attr.isEmpty()) {
/*     */         return;
/*     */       }
/* 738 */       throw new RuntimeException(MessageLocalization.getComposedMessage("bookmark.end.tag.out.of.place", new Object[0]));
/*     */     } 
/* 740 */     if (!tag.equals("Title"))
/* 741 */       throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.end.tag.1", new Object[] { tag })); 
/* 742 */     HashMap<String, Object> attributes = this.attr.pop();
/* 743 */     String title = (String)attributes.get("Title");
/* 744 */     attributes.put("Title", title.trim());
/* 745 */     String named = (String)attributes.get("Named");
/* 746 */     if (named != null)
/* 747 */       attributes.put("Named", SimpleNamedDestination.unEscapeBinaryString(named)); 
/* 748 */     named = (String)attributes.get("NamedN");
/* 749 */     if (named != null)
/* 750 */       attributes.put("NamedN", SimpleNamedDestination.unEscapeBinaryString(named)); 
/* 751 */     if (this.attr.isEmpty()) {
/* 752 */       this.topList.add(attributes);
/*     */     } else {
/* 754 */       HashMap<String, Object> parent = this.attr.peek();
/* 755 */       List<HashMap<String, Object>> kids = (List<HashMap<String, Object>>)parent.get("Kids");
/* 756 */       if (kids == null) {
/* 757 */         kids = new ArrayList<HashMap<String, Object>>();
/* 758 */         parent.put("Kids", kids);
/*     */       } 
/* 760 */       kids.add(attributes);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public void startElement(String tag, Map<String, String> h) {
/* 768 */     if (this.topList == null) {
/* 769 */       if (tag.equals("Bookmark")) {
/* 770 */         this.topList = new ArrayList<HashMap<String, Object>>();
/*     */         
/*     */         return;
/*     */       } 
/* 774 */       throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.bookmark.1", new Object[] { tag }));
/*     */     } 
/* 776 */     if (!tag.equals("Title"))
/* 777 */       throw new RuntimeException(MessageLocalization.getComposedMessage("tag.1.not.allowed", new Object[] { tag })); 
/* 778 */     HashMap<String, Object> attributes = new HashMap<String, Object>(h);
/* 779 */     attributes.put("Title", "");
/* 780 */     attributes.remove("Kids");
/* 781 */     this.attr.push(attributes);
/*     */   }
/*     */   
/*     */   public void text(String str) {
/* 785 */     if (this.attr.isEmpty())
/*     */       return; 
/* 787 */     HashMap<String, Object> attributes = this.attr.peek();
/* 788 */     String title = (String)attributes.get("Title");
/* 789 */     title = title + str;
/* 790 */     attributes.put("Title", title);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/SimpleBookmark.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */