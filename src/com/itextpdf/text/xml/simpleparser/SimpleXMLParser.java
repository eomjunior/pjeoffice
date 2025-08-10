/*     */ package com.itextpdf.text.xml.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.xml.XMLUtil;
/*     */ import com.itextpdf.text.xml.simpleparser.handler.HTMLNewLineHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.handler.NeverNewLineHandler;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpleXMLParser
/*     */ {
/*     */   private static final int UNKNOWN = 0;
/*     */   private static final int TEXT = 1;
/*     */   private static final int TAG_ENCOUNTERED = 2;
/*     */   private static final int EXAMIN_TAG = 3;
/*     */   private static final int TAG_EXAMINED = 4;
/*     */   private static final int IN_CLOSETAG = 5;
/*     */   private static final int SINGLE_TAG = 6;
/*     */   private static final int CDATA = 7;
/*     */   private static final int COMMENT = 8;
/*     */   private static final int PI = 9;
/*     */   private static final int ENTITY = 10;
/*     */   private static final int QUOTE = 11;
/*     */   private static final int ATTRIBUTE_KEY = 12;
/*     */   private static final int ATTRIBUTE_EQUAL = 13;
/*     */   private static final int ATTRIBUTE_VALUE = 14;
/*     */   private final Stack<Integer> stack;
/* 127 */   private int character = 0;
/*     */   
/* 129 */   private int previousCharacter = -1;
/*     */   
/* 131 */   private int lines = 1;
/*     */   
/* 133 */   private int columns = 0;
/*     */ 
/*     */   
/*     */   private boolean eol = false;
/*     */ 
/*     */   
/*     */   private boolean nowhite = false;
/*     */ 
/*     */   
/*     */   private int state;
/*     */ 
/*     */   
/*     */   private final boolean html;
/*     */ 
/*     */   
/* 148 */   private final StringBuffer text = new StringBuffer();
/*     */   
/* 150 */   private final StringBuffer entity = new StringBuffer();
/*     */   
/* 152 */   private String tag = null;
/*     */   
/* 154 */   private HashMap<String, String> attributes = null;
/*     */   
/*     */   private final SimpleXMLDocHandler doc;
/*     */   
/*     */   private final SimpleXMLDocHandlerComment comment;
/*     */   
/* 160 */   private int nested = 0;
/*     */   
/* 162 */   private int quoteCharacter = 34;
/*     */   
/* 164 */   private String attributekey = null;
/*     */   
/* 166 */   private String attributevalue = null;
/*     */ 
/*     */   
/*     */   private NewLineHandler newLineHandler;
/*     */ 
/*     */   
/*     */   private SimpleXMLParser(SimpleXMLDocHandler doc, SimpleXMLDocHandlerComment comment, boolean html) {
/* 173 */     this.doc = doc;
/* 174 */     this.comment = comment;
/* 175 */     this.html = html;
/* 176 */     if (html) {
/* 177 */       this.newLineHandler = (NewLineHandler)new HTMLNewLineHandler();
/*     */     } else {
/* 179 */       this.newLineHandler = (NewLineHandler)new NeverNewLineHandler();
/*     */     } 
/* 181 */     this.stack = new Stack<Integer>();
/* 182 */     this.state = html ? 1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void go(Reader r) throws IOException {
/*     */     BufferedReader reader;
/* 191 */     if (r instanceof BufferedReader) {
/* 192 */       reader = (BufferedReader)r;
/*     */     } else {
/* 194 */       reader = new BufferedReader(r);
/* 195 */     }  this.doc.startDocument();
/*     */     
/*     */     while (true) {
/* 198 */       if (this.previousCharacter == -1) {
/* 199 */         this.character = reader.read();
/*     */       }
/*     */       else {
/*     */         
/* 203 */         this.character = this.previousCharacter;
/* 204 */         this.previousCharacter = -1;
/*     */       } 
/*     */ 
/*     */       
/* 208 */       if (this.character == -1) {
/* 209 */         if (this.html) {
/* 210 */           if (this.html && this.state == 1)
/* 211 */             flush(); 
/* 212 */           this.doc.endDocument();
/*     */         } else {
/* 214 */           throwException(MessageLocalization.getComposedMessage("missing.end.tag", new Object[0]));
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 220 */       if (this.character == 10 && this.eol) {
/* 221 */         this.eol = false; continue;
/*     */       } 
/* 223 */       if (this.eol) {
/* 224 */         this.eol = false;
/* 225 */       } else if (this.character == 10) {
/* 226 */         this.lines++;
/* 227 */         this.columns = 0;
/* 228 */       } else if (this.character == 13) {
/* 229 */         this.eol = true;
/* 230 */         this.character = 10;
/* 231 */         this.lines++;
/* 232 */         this.columns = 0;
/*     */       } else {
/* 234 */         this.columns++;
/*     */       } 
/*     */       
/* 237 */       switch (this.state) {
/*     */         
/*     */         case 0:
/* 240 */           if (this.character == 60) {
/* 241 */             saveState(1);
/* 242 */             this.state = 2;
/*     */           } 
/*     */ 
/*     */         
/*     */         case 1:
/* 247 */           if (this.character == 60) {
/* 248 */             flush();
/* 249 */             saveState(this.state);
/* 250 */             this.state = 2; continue;
/* 251 */           }  if (this.character == 38) {
/* 252 */             saveState(this.state);
/* 253 */             this.entity.setLength(0);
/* 254 */             this.state = 10;
/* 255 */             this.nowhite = true; continue;
/* 256 */           }  if (this.character == 32) {
/* 257 */             if (this.html && this.nowhite) {
/* 258 */               this.text.append(' ');
/* 259 */               this.nowhite = false; continue;
/*     */             } 
/* 261 */             if (this.nowhite) {
/* 262 */               this.text.append((char)this.character);
/*     */             }
/* 264 */             this.nowhite = false; continue;
/*     */           } 
/* 266 */           if (Character.isWhitespace((char)this.character)) {
/* 267 */             if (this.html) {
/*     */               continue;
/*     */             }
/* 270 */             if (this.nowhite) {
/* 271 */               this.text.append((char)this.character);
/*     */             }
/* 273 */             this.nowhite = false;
/*     */             continue;
/*     */           } 
/* 276 */           this.text.append((char)this.character);
/* 277 */           this.nowhite = true;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 283 */           initTag();
/* 284 */           if (this.character == 47) {
/* 285 */             this.state = 5; continue;
/* 286 */           }  if (this.character == 63) {
/* 287 */             restoreState();
/* 288 */             this.state = 9; continue;
/*     */           } 
/* 290 */           this.text.append((char)this.character);
/* 291 */           this.state = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 297 */           if (this.character == 62) {
/* 298 */             doTag();
/* 299 */             processTag(true);
/* 300 */             initTag();
/* 301 */             this.state = restoreState(); continue;
/* 302 */           }  if (this.character == 47) {
/* 303 */             this.state = 6; continue;
/* 304 */           }  if (this.character == 45 && this.text.toString().equals("!-")) {
/* 305 */             flush();
/* 306 */             this.state = 8; continue;
/* 307 */           }  if (this.character == 91 && this.text.toString().equals("![CDATA")) {
/* 308 */             flush();
/* 309 */             this.state = 7; continue;
/* 310 */           }  if (this.character == 69 && this.text.toString().equals("!DOCTYP")) {
/* 311 */             flush();
/* 312 */             this.state = 9; continue;
/* 313 */           }  if (Character.isWhitespace((char)this.character)) {
/* 314 */             doTag();
/* 315 */             this.state = 4; continue;
/*     */           } 
/* 317 */           this.text.append((char)this.character);
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 322 */           if (this.character == 62) {
/* 323 */             processTag(true);
/* 324 */             initTag();
/* 325 */             this.state = restoreState(); continue;
/* 326 */           }  if (this.character == 47) {
/* 327 */             this.state = 6; continue;
/* 328 */           }  if (Character.isWhitespace((char)this.character)) {
/*     */             continue;
/*     */           }
/* 331 */           this.text.append((char)this.character);
/* 332 */           this.state = 12;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 5:
/* 338 */           if (this.character == 62) {
/* 339 */             doTag();
/* 340 */             processTag(false);
/* 341 */             if (!this.html && this.nested == 0)
/* 342 */               return;  this.state = restoreState(); continue;
/*     */           } 
/* 344 */           if (!Character.isWhitespace((char)this.character)) {
/* 345 */             this.text.append((char)this.character);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 6:
/* 352 */           if (this.character != 62)
/* 353 */             throwException(MessageLocalization.getComposedMessage("expected.gt.for.tag.lt.1.gt", new Object[] { this.tag })); 
/* 354 */           doTag();
/* 355 */           processTag(true);
/* 356 */           processTag(false);
/* 357 */           initTag();
/* 358 */           if (!this.html && this.nested == 0) {
/* 359 */             this.doc.endDocument();
/*     */             return;
/*     */           } 
/* 362 */           this.state = restoreState();
/*     */ 
/*     */ 
/*     */         
/*     */         case 7:
/* 367 */           if (this.character == 62 && this.text
/* 368 */             .toString().endsWith("]]")) {
/* 369 */             this.text.setLength(this.text.length() - 2);
/* 370 */             flush();
/* 371 */             this.state = restoreState(); continue;
/*     */           } 
/* 373 */           this.text.append((char)this.character);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 8:
/* 379 */           if (this.character == 62 && this.text
/* 380 */             .toString().endsWith("--")) {
/* 381 */             this.text.setLength(this.text.length() - 2);
/* 382 */             flush();
/* 383 */             this.state = restoreState(); continue;
/*     */           } 
/* 385 */           this.text.append((char)this.character);
/*     */ 
/*     */ 
/*     */         
/*     */         case 9:
/* 390 */           if (this.character == 62) {
/* 391 */             this.state = restoreState();
/* 392 */             if (this.state == 1) this.state = 0;
/*     */           
/*     */           } 
/*     */ 
/*     */         
/*     */         case 10:
/* 398 */           if (this.character == 59) {
/* 399 */             this.state = restoreState();
/* 400 */             String cent = this.entity.toString();
/* 401 */             this.entity.setLength(0);
/* 402 */             char ce = EntitiesToUnicode.decodeEntity(cent);
/* 403 */             if (ce == '\000') {
/* 404 */               this.text.append('&').append(cent).append(';'); continue;
/*     */             } 
/* 406 */             this.text.append(ce); continue;
/* 407 */           }  if ((this.character != 35 && (this.character < 48 || this.character > 57) && (this.character < 97 || this.character > 122) && (this.character < 65 || this.character > 90)) || this.entity
/* 408 */             .length() >= 7) {
/* 409 */             this.state = restoreState();
/* 410 */             this.previousCharacter = this.character;
/* 411 */             this.text.append('&').append(this.entity.toString());
/* 412 */             this.entity.setLength(0);
/*     */             continue;
/*     */           } 
/* 415 */           this.entity.append((char)this.character);
/*     */ 
/*     */ 
/*     */         
/*     */         case 11:
/* 420 */           if (this.html && this.quoteCharacter == 32 && this.character == 62) {
/* 421 */             flush();
/* 422 */             processTag(true);
/* 423 */             initTag();
/* 424 */             this.state = restoreState(); continue;
/*     */           } 
/* 426 */           if (this.html && this.quoteCharacter == 32 && Character.isWhitespace((char)this.character)) {
/* 427 */             flush();
/* 428 */             this.state = 4; continue;
/*     */           } 
/* 430 */           if (this.html && this.quoteCharacter == 32) {
/* 431 */             this.text.append((char)this.character); continue;
/*     */           } 
/* 433 */           if (this.character == this.quoteCharacter) {
/* 434 */             flush();
/* 435 */             this.state = 4; continue;
/* 436 */           }  if (" \r\n\t".indexOf(this.character) >= 0) {
/* 437 */             this.text.append(' '); continue;
/* 438 */           }  if (this.character == 38) {
/* 439 */             saveState(this.state);
/* 440 */             this.state = 10;
/* 441 */             this.entity.setLength(0); continue;
/*     */           } 
/* 443 */           this.text.append((char)this.character);
/*     */ 
/*     */ 
/*     */         
/*     */         case 12:
/* 448 */           if (Character.isWhitespace((char)this.character)) {
/* 449 */             flush();
/* 450 */             this.state = 13; continue;
/* 451 */           }  if (this.character == 61) {
/* 452 */             flush();
/* 453 */             this.state = 14; continue;
/* 454 */           }  if (this.html && this.character == 62) {
/* 455 */             this.text.setLength(0);
/* 456 */             processTag(true);
/* 457 */             initTag();
/* 458 */             this.state = restoreState(); continue;
/*     */           } 
/* 460 */           this.text.append((char)this.character);
/*     */ 
/*     */ 
/*     */         
/*     */         case 13:
/* 465 */           if (this.character == 61) {
/* 466 */             this.state = 14; continue;
/* 467 */           }  if (Character.isWhitespace((char)this.character))
/*     */             continue; 
/* 469 */           if (this.html && this.character == 62) {
/* 470 */             this.text.setLength(0);
/* 471 */             processTag(true);
/* 472 */             initTag();
/* 473 */             this.state = restoreState(); continue;
/* 474 */           }  if (this.html && this.character == 47) {
/* 475 */             flush();
/* 476 */             this.state = 6; continue;
/* 477 */           }  if (this.html) {
/* 478 */             flush();
/* 479 */             this.text.append((char)this.character);
/* 480 */             this.state = 12; continue;
/*     */           } 
/* 482 */           throwException(MessageLocalization.getComposedMessage("error.in.attribute.processing", new Object[0]));
/*     */ 
/*     */ 
/*     */         
/*     */         case 14:
/* 487 */           if (this.character == 34 || this.character == 39) {
/* 488 */             this.quoteCharacter = this.character;
/* 489 */             this.state = 11; continue;
/* 490 */           }  if (Character.isWhitespace((char)this.character))
/*     */             continue; 
/* 492 */           if (this.html && this.character == 62) {
/* 493 */             flush();
/* 494 */             processTag(true);
/* 495 */             initTag();
/* 496 */             this.state = restoreState(); continue;
/* 497 */           }  if (this.html) {
/* 498 */             this.text.append((char)this.character);
/* 499 */             this.quoteCharacter = 32;
/* 500 */             this.state = 11; continue;
/*     */           } 
/* 502 */           throwException(MessageLocalization.getComposedMessage("error.in.attribute.processing", new Object[0]));
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
/*     */   private int restoreState() {
/* 514 */     if (!this.stack.empty()) {
/* 515 */       return ((Integer)this.stack.pop()).intValue();
/*     */     }
/* 517 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveState(int s) {
/* 524 */     this.stack.push(Integer.valueOf(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flush() {
/* 532 */     switch (this.state) {
/*     */       case 1:
/*     */       case 7:
/* 535 */         if (this.text.length() > 0) {
/* 536 */           this.doc.text(this.text.toString());
/*     */         }
/*     */         break;
/*     */       case 8:
/* 540 */         if (this.comment != null) {
/* 541 */           this.comment.comment(this.text.toString());
/*     */         }
/*     */         break;
/*     */       case 12:
/* 545 */         this.attributekey = this.text.toString();
/* 546 */         if (this.html)
/* 547 */           this.attributekey = this.attributekey.toLowerCase(); 
/*     */         break;
/*     */       case 11:
/*     */       case 14:
/* 551 */         this.attributevalue = this.text.toString();
/* 552 */         this.attributes.put(this.attributekey, this.attributevalue);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 557 */     this.text.setLength(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initTag() {
/* 563 */     this.tag = null;
/* 564 */     this.attributes = new HashMap<String, String>();
/*     */   }
/*     */   
/*     */   private void doTag() {
/* 568 */     if (this.tag == null)
/* 569 */       this.tag = this.text.toString(); 
/* 570 */     if (this.html)
/* 571 */       this.tag = this.tag.toLowerCase(); 
/* 572 */     this.text.setLength(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processTag(boolean start) {
/* 579 */     if (start) {
/* 580 */       this.nested++;
/* 581 */       this.doc.startElement(this.tag, this.attributes);
/*     */     }
/*     */     else {
/*     */       
/* 585 */       if (this.newLineHandler.isNewLineTag(this.tag)) {
/* 586 */         this.nowhite = false;
/*     */       }
/* 588 */       this.nested--;
/* 589 */       this.doc.endElement(this.tag);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void throwException(String s) throws IOException {
/* 594 */     throw new IOException(MessageLocalization.getComposedMessage("1.near.line.2.column.3", new Object[] { s, String.valueOf(this.lines), String.valueOf(this.columns) }));
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
/*     */   public static void parse(SimpleXMLDocHandler doc, SimpleXMLDocHandlerComment comment, Reader r, boolean html) throws IOException {
/* 606 */     SimpleXMLParser parser = new SimpleXMLParser(doc, comment, html);
/* 607 */     parser.go(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void parse(SimpleXMLDocHandler doc, InputStream in) throws IOException {
/* 617 */     byte[] b4 = new byte[4];
/* 618 */     int count = in.read(b4);
/* 619 */     if (count != 4)
/* 620 */       throw new IOException(MessageLocalization.getComposedMessage("insufficient.length", new Object[0])); 
/* 621 */     String encoding = XMLUtil.getEncodingName(b4);
/* 622 */     String decl = null;
/* 623 */     if (encoding.equals("UTF-8")) {
/* 624 */       StringBuffer sb = new StringBuffer();
/*     */       int c;
/* 626 */       while ((c = in.read()) != -1 && 
/* 627 */         c != 62)
/*     */       {
/* 629 */         sb.append((char)c);
/*     */       }
/* 631 */       decl = sb.toString();
/*     */     }
/* 633 */     else if (encoding.equals("CP037")) {
/* 634 */       ByteArrayOutputStream bi = new ByteArrayOutputStream();
/*     */       int c;
/* 636 */       while ((c = in.read()) != -1 && 
/* 637 */         c != 110)
/*     */       {
/* 639 */         bi.write(c);
/*     */       }
/* 641 */       decl = new String(bi.toByteArray(), "CP037");
/*     */     } 
/* 643 */     if (decl != null) {
/* 644 */       decl = getDeclaredEncoding(decl);
/* 645 */       if (decl != null)
/* 646 */         encoding = decl; 
/*     */     } 
/* 648 */     parse(doc, new InputStreamReader(in, IanaEncodings.getJavaEncoding(encoding)));
/*     */   }
/*     */   
/*     */   private static String getDeclaredEncoding(String decl) {
/* 652 */     if (decl == null)
/* 653 */       return null; 
/* 654 */     int idx = decl.indexOf("encoding");
/* 655 */     if (idx < 0)
/* 656 */       return null; 
/* 657 */     int idx1 = decl.indexOf('"', idx);
/* 658 */     int idx2 = decl.indexOf('\'', idx);
/* 659 */     if (idx1 == idx2)
/* 660 */       return null; 
/* 661 */     if ((idx1 < 0 && idx2 > 0) || (idx2 > 0 && idx2 < idx1)) {
/* 662 */       int idx3 = decl.indexOf('\'', idx2 + 1);
/* 663 */       if (idx3 < 0)
/* 664 */         return null; 
/* 665 */       return decl.substring(idx2 + 1, idx3);
/*     */     } 
/* 667 */     if ((idx2 < 0 && idx1 > 0) || (idx1 > 0 && idx1 < idx2)) {
/* 668 */       int idx3 = decl.indexOf('"', idx1 + 1);
/* 669 */       if (idx3 < 0)
/* 670 */         return null; 
/* 671 */       return decl.substring(idx1 + 1, idx3);
/*     */     } 
/* 673 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void parse(SimpleXMLDocHandler doc, Reader r) throws IOException {
/* 682 */     parse(doc, null, r, false);
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
/*     */   @Deprecated
/*     */   public static String escapeXML(String s, boolean onlyASCII) {
/* 699 */     return XMLUtil.escapeXML(s, onlyASCII);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/SimpleXMLParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */