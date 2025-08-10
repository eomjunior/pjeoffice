/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.api.Indentable;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class Section
/*     */   extends ArrayList<Element>
/*     */   implements TextElementArray, LargeElement, Indentable, IAccessibleElement
/*     */ {
/*     */   public static final int NUMBERSTYLE_DOTTED = 0;
/*     */   public static final int NUMBERSTYLE_DOTTED_WITHOUT_FINAL_DOT = 1;
/*     */   private static final long serialVersionUID = 3324172577544748043L;
/*     */   protected Paragraph title;
/*     */   protected String bookmarkTitle;
/*     */   protected int numberDepth;
/* 115 */   protected int numberStyle = 0;
/*     */ 
/*     */   
/*     */   protected float indentationLeft;
/*     */ 
/*     */   
/*     */   protected float indentationRight;
/*     */ 
/*     */   
/*     */   protected float indentation;
/*     */ 
/*     */   
/*     */   protected boolean bookmarkOpen = true;
/*     */ 
/*     */   
/*     */   protected boolean triggerNewPage = false;
/*     */ 
/*     */   
/* 133 */   protected int subsections = 0;
/*     */ 
/*     */   
/* 136 */   protected ArrayList<Integer> numbers = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean complete = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean addedCompletely = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean notAddedYet = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Section() {
/* 162 */     this.title = new Paragraph();
/* 163 */     this.numberDepth = 1;
/* 164 */     this.title.setRole(new PdfName("H" + this.numberDepth));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Section(Paragraph title, int numberDepth) {
/* 174 */     this.numberDepth = numberDepth;
/* 175 */     this.title = title;
/* 176 */     if (title != null) {
/* 177 */       title.setRole(new PdfName("H" + numberDepth));
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
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 192 */       for (Object element2 : this) {
/* 193 */         Element element = (Element)element2;
/* 194 */         listener.add(element);
/*     */       } 
/* 196 */       return true;
/*     */     }
/* 198 */     catch (DocumentException de) {
/* 199 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 209 */     return 13;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChapter() {
/* 219 */     return (type() == 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSection() {
/* 229 */     return (type() == 13);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 238 */     List<Chunk> tmp = new ArrayList<Chunk>();
/* 239 */     for (Object element : this) {
/* 240 */       tmp.addAll(((Element)element).getChunks());
/*     */     }
/* 242 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 250 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 258 */     return false;
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
/*     */   public void add(int index, Element element) {
/* 274 */     if (isAddedCompletely()) {
/* 275 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document", new Object[0]));
/*     */     }
/*     */     try {
/* 278 */       if (element.isNestable()) {
/* 279 */         super.add(index, element);
/*     */       } else {
/*     */         
/* 282 */         throw new ClassCastException(MessageLocalization.getComposedMessage("you.can.t.add.a.1.to.a.section", new Object[] { element.getClass().getName() }));
/*     */       }
/*     */     
/* 285 */     } catch (ClassCastException cce) {
/* 286 */       throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", new Object[] { cce.getMessage() }));
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
/*     */   public boolean add(Element element) {
/* 301 */     if (isAddedCompletely()) {
/* 302 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document", new Object[0]));
/*     */     }
/*     */     try {
/* 305 */       if (element.type() == 13) {
/* 306 */         Section section = (Section)element;
/* 307 */         section.setNumbers(++this.subsections, this.numbers);
/* 308 */         return super.add(section);
/*     */       } 
/* 310 */       if (element instanceof MarkedSection && ((MarkedObject)element).element.type() == 13) {
/* 311 */         MarkedSection mo = (MarkedSection)element;
/* 312 */         Section section = (Section)mo.element;
/* 313 */         section.setNumbers(++this.subsections, this.numbers);
/* 314 */         return super.add(mo);
/*     */       } 
/* 316 */       if (element.isNestable()) {
/* 317 */         return super.add(element);
/*     */       }
/*     */       
/* 320 */       throw new ClassCastException(MessageLocalization.getComposedMessage("you.can.t.add.a.1.to.a.section", new Object[] { element.getClass().getName() }));
/*     */     
/*     */     }
/* 323 */     catch (ClassCastException cce) {
/* 324 */       throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", new Object[] { cce.getMessage() }));
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
/*     */   public boolean addAll(Collection<? extends Element> collection) {
/* 338 */     if (collection.size() == 0)
/* 339 */       return false; 
/* 340 */     for (Element element : collection) {
/* 341 */       add(element);
/*     */     }
/* 343 */     return true;
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
/*     */   public Section addSection(float indentation, Paragraph title, int numberDepth) {
/* 357 */     if (isAddedCompletely()) {
/* 358 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("this.largeelement.has.already.been.added.to.the.document", new Object[0]));
/*     */     }
/* 360 */     Section section = new Section(title, numberDepth);
/* 361 */     section.setIndentation(indentation);
/* 362 */     add(section);
/* 363 */     return section;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(float indentation, Paragraph title) {
/* 374 */     return addSection(indentation, title, this.numberDepth + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(Paragraph title, int numberDepth) {
/* 385 */     return addSection(0.0F, title, numberDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MarkedSection addMarkedSection() {
/* 393 */     MarkedSection section = new MarkedSection(new Section(null, this.numberDepth + 1));
/* 394 */     add(section);
/* 395 */     return section;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(Paragraph title) {
/* 405 */     return addSection(0.0F, title, this.numberDepth + 1);
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
/*     */   public Section addSection(float indentation, String title, int numberDepth) {
/* 417 */     return addSection(indentation, new Paragraph(title), numberDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(String title, int numberDepth) {
/* 428 */     return addSection(new Paragraph(title), numberDepth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(float indentation, String title) {
/* 439 */     return addSection(indentation, new Paragraph(title));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Section addSection(String title) {
/* 449 */     return addSection(new Paragraph(title));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(Paragraph title) {
/* 460 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph getTitle() {
/* 469 */     return constructTitle(this.title, this.numbers, this.numberDepth, this.numberStyle);
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
/*     */   public static Paragraph constructTitle(Paragraph title, ArrayList<Integer> numbers, int numberDepth, int numberStyle) {
/* 482 */     if (title == null) {
/* 483 */       return null;
/*     */     }
/*     */     
/* 486 */     int depth = Math.min(numbers.size(), numberDepth);
/* 487 */     if (depth < 1) {
/* 488 */       return title;
/*     */     }
/* 490 */     StringBuffer buf = new StringBuffer(" ");
/* 491 */     for (int i = 0; i < depth; i++) {
/* 492 */       buf.insert(0, ".");
/* 493 */       buf.insert(0, ((Integer)numbers.get(i)).intValue());
/*     */     } 
/* 495 */     if (numberStyle == 1) {
/* 496 */       buf.deleteCharAt(buf.length() - 2);
/*     */     }
/* 498 */     Paragraph result = new Paragraph(title);
/*     */     
/* 500 */     result.add(0, new Chunk(buf.toString(), title.getFont()));
/* 501 */     return result;
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
/*     */   public void setNumberDepth(int numberDepth) {
/* 514 */     this.numberDepth = numberDepth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberDepth() {
/* 523 */     return this.numberDepth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumberStyle(int numberStyle) {
/* 534 */     this.numberStyle = numberStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberStyle() {
/* 543 */     return this.numberStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationLeft(float indentation) {
/* 552 */     this.indentationLeft = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationLeft() {
/* 561 */     return this.indentationLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationRight(float indentation) {
/* 570 */     this.indentationRight = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationRight() {
/* 579 */     return this.indentationRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentation(float indentation) {
/* 588 */     this.indentation = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentation() {
/* 597 */     return this.indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBookmarkOpen(boolean bookmarkOpen) {
/* 605 */     this.bookmarkOpen = bookmarkOpen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBookmarkOpen() {
/* 613 */     return this.bookmarkOpen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTriggerNewPage(boolean triggerNewPage) {
/* 621 */     this.triggerNewPage = triggerNewPage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTriggerNewPage() {
/* 629 */     return (this.triggerNewPage && this.notAddedYet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBookmarkTitle(String bookmarkTitle) {
/* 638 */     this.bookmarkTitle = bookmarkTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph getBookmarkTitle() {
/* 646 */     if (this.bookmarkTitle == null) {
/* 647 */       return getTitle();
/*     */     }
/* 649 */     return new Paragraph(this.bookmarkTitle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChapterNumber(int number) {
/* 657 */     this.numbers.set(this.numbers.size() - 1, Integer.valueOf(number));
/*     */     
/* 659 */     for (Iterator<Element> i = iterator(); i.hasNext(); ) {
/* 660 */       Object s = i.next();
/* 661 */       if (s instanceof Section) {
/* 662 */         ((Section)s).setChapterNumber(number);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDepth() {
/* 673 */     return this.numbers.size();
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
/*     */   private void setNumbers(int number, ArrayList<Integer> numbers) {
/* 685 */     this.numbers = new ArrayList<Integer>();
/* 686 */     this.numbers.add(Integer.valueOf(number));
/* 687 */     this.numbers.addAll(numbers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNotAddedYet() {
/* 696 */     return this.notAddedYet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotAddedYet(boolean notAddedYet) {
/* 706 */     this.notAddedYet = notAddedYet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isAddedCompletely() {
/* 714 */     return this.addedCompletely;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setAddedCompletely(boolean addedCompletely) {
/* 722 */     this.addedCompletely = addedCompletely;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushContent() {
/* 730 */     setNotAddedYet(false);
/* 731 */     this.title = null;
/*     */     
/* 733 */     for (Iterator<Element> i = iterator(); i.hasNext(); ) {
/* 734 */       Element element = i.next();
/* 735 */       if (element instanceof Section) {
/* 736 */         Section s = (Section)element;
/* 737 */         if (!s.isComplete() && size() == 1) {
/* 738 */           s.flushContent();
/*     */           
/*     */           return;
/*     */         } 
/* 742 */         s.setAddedCompletely(true);
/*     */       } 
/*     */       
/* 745 */       i.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 754 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComplete(boolean complete) {
/* 762 */     this.complete = complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void newPage() {
/* 770 */     add(Chunk.NEXTPAGE);
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 774 */     return this.title.getAccessibleAttribute(key);
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 778 */     this.title.setAccessibleAttribute(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 782 */     return this.title.getAccessibleAttributes();
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 786 */     return this.title.getRole();
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 790 */     this.title.setRole(role);
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 794 */     return this.title.getId();
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 798 */     this.title.setId(id);
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 802 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Section.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */