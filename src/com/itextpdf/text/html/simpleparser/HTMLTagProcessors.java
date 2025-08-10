/*     */ package com.itextpdf.text.html.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Element;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class HTMLTagProcessors
/*     */   extends HashMap<String, HTMLTagProcessor>
/*     */ {
/*     */   public HTMLTagProcessors() {
/*  66 */     put("a", A);
/*  67 */     put("b", EM_STRONG_STRIKE_SUP_SUP);
/*  68 */     put("body", DIV);
/*  69 */     put("br", BR);
/*  70 */     put("div", DIV);
/*  71 */     put("em", EM_STRONG_STRIKE_SUP_SUP);
/*  72 */     put("font", SPAN);
/*  73 */     put("h1", H);
/*  74 */     put("h2", H);
/*  75 */     put("h3", H);
/*  76 */     put("h4", H);
/*  77 */     put("h5", H);
/*  78 */     put("h6", H);
/*  79 */     put("hr", HR);
/*  80 */     put("i", EM_STRONG_STRIKE_SUP_SUP);
/*  81 */     put("img", IMG);
/*  82 */     put("li", LI);
/*  83 */     put("ol", UL_OL);
/*  84 */     put("p", DIV);
/*  85 */     put("pre", PRE);
/*  86 */     put("s", EM_STRONG_STRIKE_SUP_SUP);
/*  87 */     put("span", SPAN);
/*  88 */     put("strike", EM_STRONG_STRIKE_SUP_SUP);
/*  89 */     put("strong", EM_STRONG_STRIKE_SUP_SUP);
/*  90 */     put("sub", EM_STRONG_STRIKE_SUP_SUP);
/*  91 */     put("sup", EM_STRONG_STRIKE_SUP_SUP);
/*  92 */     put("table", TABLE);
/*  93 */     put("td", TD);
/*  94 */     put("th", TD);
/*  95 */     put("tr", TR);
/*  96 */     put("u", EM_STRONG_STRIKE_SUP_SUP);
/*  97 */     put("ul", UL_OL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static final HTMLTagProcessor EM_STRONG_STRIKE_SUP_SUP = new HTMLTagProcessor()
/*     */     {
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs)
/*     */       {
/* 109 */         tag = mapTag(tag);
/* 110 */         attrs.put(tag, null);
/* 111 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {
/* 117 */         tag = mapTag(tag);
/* 118 */         worker.updateChain(tag);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private String mapTag(String tag) {
/* 127 */         if ("em".equalsIgnoreCase(tag))
/* 128 */           return "i"; 
/* 129 */         if ("strong".equalsIgnoreCase(tag))
/* 130 */           return "b"; 
/* 131 */         if ("strike".equalsIgnoreCase(tag))
/* 132 */           return "s"; 
/* 133 */         return tag;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final HTMLTagProcessor A = new HTMLTagProcessor()
/*     */     {
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs)
/*     */       {
/* 146 */         worker.updateChain(tag, attrs);
/* 147 */         worker.flushContent();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {
/* 153 */         worker.processLink();
/* 154 */         worker.updateChain(tag);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static final HTMLTagProcessor BR = new HTMLTagProcessor()
/*     */     {
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs)
/*     */       {
/* 166 */         worker.newLine();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 176 */   public static final HTMLTagProcessor UL_OL = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 182 */         worker.carriageReturn();
/* 183 */         if (worker.isPendingLI())
/* 184 */           worker.endElement("li"); 
/* 185 */         worker.setSkipText(true);
/* 186 */         worker.updateChain(tag, attrs);
/* 187 */         worker.pushToStack((Element)worker.createList(tag));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 194 */         worker.carriageReturn();
/* 195 */         if (worker.isPendingLI())
/* 196 */           worker.endElement("li"); 
/* 197 */         worker.setSkipText(false);
/* 198 */         worker.updateChain(tag);
/* 199 */         worker.processList();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 204 */   public static final HTMLTagProcessor HR = new HTMLTagProcessor()
/*     */     {
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
/* 207 */         worker.carriageReturn();
/* 208 */         worker.pushToStack((Element)worker.createLineSeparator(attrs));
/*     */       }
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {}
/*     */     };
/*     */ 
/*     */   
/* 216 */   public static final HTMLTagProcessor SPAN = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs)
/*     */       {
/* 222 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {
/* 229 */         worker.updateChain(tag);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 234 */   public static final HTMLTagProcessor H = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 240 */         worker.carriageReturn();
/* 241 */         if (!attrs.containsKey("size")) {
/* 242 */           int v = 7 - Integer.parseInt(tag.substring(1));
/* 243 */           attrs.put("size", Integer.toString(v));
/*     */         } 
/* 245 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 252 */         worker.carriageReturn();
/* 253 */         worker.updateChain(tag);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 258 */   public static final HTMLTagProcessor LI = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 264 */         worker.carriageReturn();
/* 265 */         if (worker.isPendingLI())
/* 266 */           worker.endElement(tag); 
/* 267 */         worker.setSkipText(false);
/* 268 */         worker.setPendingLI(true);
/* 269 */         worker.updateChain(tag, attrs);
/* 270 */         worker.pushToStack((Element)worker.createListItem());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 277 */         worker.carriageReturn();
/* 278 */         worker.setPendingLI(false);
/* 279 */         worker.setSkipText(true);
/* 280 */         worker.updateChain(tag);
/* 281 */         worker.processListItem();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 286 */   public static final HTMLTagProcessor PRE = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 292 */         worker.carriageReturn();
/* 293 */         if (!attrs.containsKey("face")) {
/* 294 */           attrs.put("face", "Courier");
/*     */         }
/* 296 */         worker.updateChain(tag, attrs);
/* 297 */         worker.setInsidePRE(true);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 304 */         worker.carriageReturn();
/* 305 */         worker.updateChain(tag);
/* 306 */         worker.setInsidePRE(false);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 311 */   public static final HTMLTagProcessor DIV = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 317 */         worker.carriageReturn();
/* 318 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 325 */         worker.carriageReturn();
/* 326 */         worker.updateChain(tag);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 332 */   public static final HTMLTagProcessor TABLE = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 339 */         worker.carriageReturn();
/* 340 */         TableWrapper table = new TableWrapper(attrs);
/* 341 */         worker.pushToStack(table);
/* 342 */         worker.pushTableState();
/* 343 */         worker.setPendingTD(false);
/* 344 */         worker.setPendingTR(false);
/* 345 */         worker.setSkipText(true);
/*     */         
/* 347 */         attrs.remove("align");
/*     */         
/* 349 */         attrs.put("colspan", "1");
/* 350 */         attrs.put("rowspan", "1");
/* 351 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 358 */         worker.carriageReturn();
/* 359 */         if (worker.isPendingTR())
/* 360 */           worker.endElement("tr"); 
/* 361 */         worker.updateChain(tag);
/* 362 */         worker.processTable();
/* 363 */         worker.popTableState();
/* 364 */         worker.setSkipText(false);
/*     */       }
/*     */     };
/*     */   
/* 368 */   public static final HTMLTagProcessor TR = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 375 */         worker.carriageReturn();
/* 376 */         if (worker.isPendingTR())
/* 377 */           worker.endElement(tag); 
/* 378 */         worker.setSkipText(true);
/* 379 */         worker.setPendingTR(true);
/* 380 */         worker.updateChain(tag, attrs);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 387 */         worker.carriageReturn();
/* 388 */         if (worker.isPendingTD())
/* 389 */           worker.endElement("td"); 
/* 390 */         worker.setPendingTR(false);
/* 391 */         worker.updateChain(tag);
/* 392 */         worker.processRow();
/* 393 */         worker.setSkipText(true);
/*     */       }
/*     */     };
/*     */   
/* 397 */   public static final HTMLTagProcessor TD = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException
/*     */       {
/* 404 */         worker.carriageReturn();
/* 405 */         if (worker.isPendingTD())
/* 406 */           worker.endElement(tag); 
/* 407 */         worker.setSkipText(false);
/* 408 */         worker.setPendingTD(true);
/* 409 */         worker.updateChain("td", attrs);
/* 410 */         worker.pushToStack((Element)worker.createCell(tag));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) throws DocumentException {
/* 417 */         worker.carriageReturn();
/* 418 */         worker.setPendingTD(false);
/* 419 */         worker.updateChain("td");
/* 420 */         worker.setSkipText(true);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 425 */   public static final HTMLTagProcessor IMG = new HTMLTagProcessor()
/*     */     {
/*     */ 
/*     */       
/*     */       public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException, IOException
/*     */       {
/* 431 */         worker.updateChain(tag, attrs);
/* 432 */         worker.processImage(worker.createImage(attrs), attrs);
/* 433 */         worker.updateChain(tag);
/*     */       }
/*     */       
/*     */       public void endElement(HTMLWorker worker, String tag) {}
/*     */     };
/*     */   
/*     */   private static final long serialVersionUID = -959260811961222824L;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/html/simpleparser/HTMLTagProcessors.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */