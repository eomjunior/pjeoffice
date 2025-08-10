/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.api.Indentable;
/*     */ import com.itextpdf.text.factories.RomanAlphabetFactory;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class List
/*     */   implements TextElementArray, Indentable, IAccessibleElement
/*     */ {
/*     */   public static final boolean ORDERED = true;
/*     */   public static final boolean UNORDERED = false;
/*     */   public static final boolean NUMERICAL = false;
/*     */   public static final boolean ALPHABETICAL = true;
/*     */   public static final boolean UPPERCASE = false;
/*     */   public static final boolean LOWERCASE = true;
/* 120 */   protected ArrayList<Element> list = new ArrayList<Element>();
/*     */ 
/*     */   
/*     */   protected boolean numbered = false;
/*     */ 
/*     */   
/*     */   protected boolean lettered = false;
/*     */   
/*     */   protected boolean lowercase = false;
/*     */   
/*     */   protected boolean autoindent = false;
/*     */   
/*     */   protected boolean alignindent = false;
/*     */   
/* 134 */   protected int first = 1;
/*     */   
/* 136 */   protected Chunk symbol = new Chunk("- ");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected String preSymbol = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected String postSymbol = ". ";
/*     */ 
/*     */   
/* 149 */   protected float indentationLeft = 0.0F;
/*     */   
/* 151 */   protected float indentationRight = 0.0F;
/*     */   
/* 153 */   protected float symbolIndent = 0.0F;
/*     */   
/* 155 */   protected PdfName role = PdfName.L;
/* 156 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/* 157 */   private AccessibleElementId id = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List() {
/* 163 */     this(false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List(float symbolIndent) {
/* 172 */     this.symbolIndent = symbolIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List(boolean numbered) {
/* 180 */     this(numbered, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List(boolean numbered, boolean lettered) {
/* 189 */     this.numbered = numbered;
/* 190 */     this.lettered = lettered;
/* 191 */     this.autoindent = true;
/* 192 */     this.alignindent = true;
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
/*     */   public List(boolean numbered, float symbolIndent) {
/* 206 */     this(numbered, false, symbolIndent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List(boolean numbered, boolean lettered, float symbolIndent) {
/* 216 */     this.numbered = numbered;
/* 217 */     this.lettered = lettered;
/* 218 */     this.symbolIndent = symbolIndent;
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
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 232 */       for (Element element : this.list) {
/* 233 */         listener.add(element);
/*     */       }
/* 235 */       return true;
/*     */     }
/* 237 */     catch (DocumentException de) {
/* 238 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 248 */     return 14;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public java.util.List<Chunk> getChunks() {
/* 257 */     java.util.List<Chunk> tmp = new ArrayList<Chunk>();
/* 258 */     for (Element element : this.list) {
/* 259 */       tmp.addAll(element.getChunks());
/*     */     }
/* 261 */     return tmp;
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
/*     */   public boolean add(String s) {
/* 274 */     if (s != null) {
/* 275 */       return add(new ListItem(s));
/*     */     }
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Element o) {
/* 288 */     if (o instanceof ListItem) {
/* 289 */       ListItem item = (ListItem)o;
/* 290 */       if (this.numbered || this.lettered) {
/* 291 */         Chunk chunk = new Chunk(this.preSymbol, this.symbol.getFont());
/* 292 */         chunk.setAttributes(this.symbol.getAttributes());
/* 293 */         int index = this.first + this.list.size();
/* 294 */         if (this.lettered) {
/* 295 */           chunk.append(RomanAlphabetFactory.getString(index, this.lowercase));
/*     */         } else {
/* 297 */           chunk.append(String.valueOf(index));
/* 298 */         }  chunk.append(this.postSymbol);
/* 299 */         item.setListSymbol(chunk);
/*     */       } else {
/*     */         
/* 302 */         item.setListSymbol(this.symbol);
/*     */       } 
/* 304 */       item.setIndentationLeft(this.symbolIndent, this.autoindent);
/* 305 */       item.setIndentationRight(0.0F);
/* 306 */       return this.list.add(item);
/*     */     } 
/* 308 */     if (o instanceof List) {
/* 309 */       List nested = (List)o;
/* 310 */       nested.setIndentationLeft(nested.getIndentationLeft() + this.symbolIndent);
/* 311 */       this.first--;
/* 312 */       return this.list.add(nested);
/*     */     } 
/* 314 */     return false;
/*     */   }
/*     */   
/*     */   public List cloneShallow() {
/* 318 */     List clone = new List();
/* 319 */     populateProperties(clone);
/* 320 */     return clone;
/*     */   }
/*     */   
/*     */   protected void populateProperties(List clone) {
/* 324 */     clone.indentationLeft = this.indentationLeft;
/* 325 */     clone.indentationRight = this.indentationRight;
/* 326 */     clone.autoindent = this.autoindent;
/* 327 */     clone.alignindent = this.alignindent;
/* 328 */     clone.symbolIndent = this.symbolIndent;
/* 329 */     clone.symbol = this.symbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void normalizeIndentation() {
/* 336 */     float max = 0.0F;
/* 337 */     for (Element o : this.list) {
/* 338 */       if (o instanceof ListItem) {
/* 339 */         max = Math.max(max, ((ListItem)o).getIndentationLeft());
/*     */       }
/*     */     } 
/* 342 */     for (Element o : this.list) {
/* 343 */       if (o instanceof ListItem) {
/* 344 */         ((ListItem)o).setIndentationLeft(max);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumbered(boolean numbered) {
/* 355 */     this.numbered = numbered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLettered(boolean lettered) {
/* 362 */     this.lettered = lettered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLowercase(boolean uppercase) {
/* 369 */     this.lowercase = uppercase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutoindent(boolean autoindent) {
/* 376 */     this.autoindent = autoindent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignindent(boolean alignindent) {
/* 382 */     this.alignindent = alignindent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFirst(int first) {
/* 391 */     this.first = first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListSymbol(Chunk symbol) {
/* 400 */     this.symbol = symbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListSymbol(String symbol) {
/* 411 */     this.symbol = new Chunk(symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationLeft(float indentation) {
/* 420 */     this.indentationLeft = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationRight(float indentation) {
/* 429 */     this.indentationRight = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSymbolIndent(float symbolIndent) {
/* 436 */     this.symbolIndent = symbolIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Element> getItems() {
/* 447 */     return this.list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 456 */     return this.list.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 465 */     return this.list.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getTotalLeading() {
/* 474 */     if (this.list.size() < 1) {
/* 475 */       return -1.0F;
/*     */     }
/* 477 */     ListItem item = (ListItem)this.list.get(0);
/* 478 */     return item.getTotalLeading();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNumbered() {
/* 489 */     return this.numbered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLettered() {
/* 497 */     return this.lettered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLowercase() {
/* 505 */     return this.lowercase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoindent() {
/* 513 */     return this.autoindent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAlignindent() {
/* 521 */     return this.alignindent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFirst() {
/* 529 */     return this.first;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chunk getSymbol() {
/* 537 */     return this.symbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationLeft() {
/* 545 */     return this.indentationLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationRight() {
/* 553 */     return this.indentationRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSymbolIndent() {
/* 561 */     return this.symbolIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 568 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 576 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostSymbol() {
/* 585 */     return this.postSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostSymbol(String postSymbol) {
/* 594 */     this.postSymbol = postSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreSymbol() {
/* 603 */     return this.preSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreSymbol(String preSymbol) {
/* 612 */     this.preSymbol = preSymbol;
/*     */   }
/*     */   
/*     */   public ListItem getFirstItem() {
/* 616 */     Element lastElement = (this.list.size() > 0) ? this.list.get(0) : null;
/* 617 */     if (lastElement != null) {
/* 618 */       if (lastElement instanceof ListItem)
/* 619 */         return (ListItem)lastElement; 
/* 620 */       if (lastElement instanceof List) {
/* 621 */         return ((List)lastElement).getFirstItem();
/*     */       }
/*     */     } 
/* 624 */     return null;
/*     */   }
/*     */   
/*     */   public ListItem getLastItem() {
/* 628 */     Element lastElement = (this.list.size() > 0) ? this.list.get(this.list.size() - 1) : null;
/* 629 */     if (lastElement != null) {
/* 630 */       if (lastElement instanceof ListItem)
/* 631 */         return (ListItem)lastElement; 
/* 632 */       if (lastElement instanceof List) {
/* 633 */         return ((List)lastElement).getLastItem();
/*     */       }
/*     */     } 
/* 636 */     return null;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 640 */     if (this.accessibleAttributes != null) {
/* 641 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 643 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 647 */     if (this.accessibleAttributes == null)
/* 648 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 649 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 653 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 657 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 661 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 665 */     if (this.id == null)
/* 666 */       this.id = new AccessibleElementId(); 
/* 667 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 671 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 675 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/List.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */