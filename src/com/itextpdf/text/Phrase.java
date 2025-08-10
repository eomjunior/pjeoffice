/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.HyphenationEvent;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class Phrase
/*     */   extends ArrayList<Element>
/*     */   implements TextElementArray
/*     */ {
/*     */   private static final long serialVersionUID = 2643594602455068231L;
/*  88 */   protected float leading = Float.NaN;
/*     */ 
/*     */   
/*  91 */   protected float multipliedLeading = 0.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   protected Font font;
/*     */ 
/*     */ 
/*     */   
/*  99 */   protected HyphenationEvent hyphenation = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected TabSettings tabSettings = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase() {
/* 113 */     this(16.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(Phrase phrase) {
/* 122 */     addAll(phrase);
/* 123 */     setLeading(phrase.getLeading(), phrase.getMultipliedLeading());
/* 124 */     this.font = phrase.getFont();
/* 125 */     this.tabSettings = phrase.getTabSettings();
/* 126 */     setHyphenation(phrase.getHyphenation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(float leading) {
/* 135 */     this.leading = leading;
/* 136 */     this.font = new Font();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(Chunk chunk) {
/* 145 */     super.add(chunk);
/* 146 */     this.font = chunk.getFont();
/* 147 */     setHyphenation(chunk.getHyphenation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(float leading, Chunk chunk) {
/* 158 */     this.leading = leading;
/* 159 */     super.add(chunk);
/* 160 */     this.font = chunk.getFont();
/* 161 */     setHyphenation(chunk.getHyphenation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(String string) {
/* 170 */     this(Float.NaN, string, new Font());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(String string, Font font) {
/* 180 */     this(Float.NaN, string, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase(float leading, String string) {
/* 190 */     this(leading, string, new Font());
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
/*     */   public Phrase(float leading, String string, Font font) {
/* 202 */     this.leading = leading;
/* 203 */     this.font = font;
/*     */     
/* 205 */     if (string != null && string.length() != 0) {
/* 206 */       super.add(new Chunk(string, font));
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
/* 221 */       for (Object element : this) {
/* 222 */         listener.add((Element)element);
/*     */       }
/* 224 */       return true;
/*     */     }
/* 226 */     catch (DocumentException de) {
/* 227 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 237 */     return 11;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 246 */     List<Chunk> tmp = new ArrayList<Chunk>();
/* 247 */     for (Element element : this) {
/* 248 */       tmp.addAll(element.getChunks());
/*     */     }
/* 250 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 266 */     return true;
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
/*     */   public void add(int index, Element element) {
/*     */     Chunk chunk;
/* 282 */     if (element == null)
/* 283 */       return;  switch (element.type()) {
/*     */       case 10:
/* 285 */         chunk = (Chunk)element;
/* 286 */         if (!this.font.isStandardFont()) {
/* 287 */           chunk.setFont(this.font.difference(chunk.getFont()));
/*     */         }
/* 289 */         if (this.hyphenation != null && chunk.getHyphenation() == null && !chunk.isEmpty()) {
/* 290 */           chunk.setHyphenation(this.hyphenation);
/*     */         }
/* 292 */         super.add(index, chunk);
/*     */         return;
/*     */       case 11:
/*     */       case 12:
/*     */       case 14:
/*     */       case 17:
/*     */       case 23:
/*     */       case 29:
/*     */       case 37:
/*     */       case 50:
/*     */       case 55:
/*     */       case 666:
/* 304 */         super.add(index, element);
/*     */         return;
/*     */     } 
/* 307 */     throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", new Object[] { element.getClass().getName() }));
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
/*     */   public boolean add(String s) {
/* 319 */     if (s == null) {
/* 320 */       return false;
/*     */     }
/* 322 */     return super.add(new Chunk(s, this.font));
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
/* 336 */     if (element == null) return false;  try {
/*     */       Phrase phrase;
/*     */       boolean success;
/* 339 */       switch (element.type()) {
/*     */         case 10:
/* 341 */           return addChunk((Chunk)element);
/*     */         case 11:
/*     */         case 12:
/* 344 */           phrase = (Phrase)element;
/* 345 */           success = true;
/*     */           
/* 347 */           for (Object element2 : phrase) {
/* 348 */             Element e = (Element)element2;
/* 349 */             if (e instanceof Chunk) {
/* 350 */               success &= addChunk((Chunk)e);
/*     */               continue;
/*     */             } 
/* 353 */             success &= add(e);
/*     */           } 
/*     */           
/* 356 */           return success;
/*     */         case 14:
/*     */         case 17:
/*     */         case 23:
/*     */         case 29:
/*     */         case 37:
/*     */         case 50:
/*     */         case 55:
/*     */         case 666:
/* 365 */           return super.add(element);
/*     */       } 
/* 367 */       throw new ClassCastException(String.valueOf(element.type()));
/*     */     
/*     */     }
/* 370 */     catch (ClassCastException cce) {
/* 371 */       throw new ClassCastException(MessageLocalization.getComposedMessage("insertion.of.illegal.element.1", new Object[] { cce.getMessage() }));
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
/* 385 */     for (Element e : collection) {
/* 386 */       add(e);
/*     */     }
/* 388 */     return true;
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
/*     */   protected boolean addChunk(Chunk chunk) {
/* 400 */     Font f = chunk.getFont();
/* 401 */     String c = chunk.getContent();
/* 402 */     if (this.font != null && !this.font.isStandardFont()) {
/* 403 */       f = this.font.difference(chunk.getFont());
/*     */     }
/* 405 */     if (size() > 0 && !chunk.hasAttributes()) {
/*     */       try {
/* 407 */         boolean sameRole; Chunk previous = (Chunk)get(size() - 1);
/* 408 */         PdfName previousRole = previous.getRole();
/* 409 */         PdfName chunkRole = chunk.getRole();
/*     */         
/* 411 */         if (previousRole == null || chunkRole == null) {
/*     */           
/* 413 */           sameRole = true;
/*     */         } else {
/* 415 */           sameRole = previousRole.equals(chunkRole);
/* 416 */         }  if (sameRole && !previous.hasAttributes() && !chunk.hasAccessibleAttributes() && !previous.hasAccessibleAttributes() && (f == null || f
/*     */           
/* 418 */           .compareTo(previous.getFont()) == 0) && 
/* 419 */           !"".equals(previous.getContent().trim()) && 
/* 420 */           !"".equals(c.trim())) {
/* 421 */           previous.append(c);
/* 422 */           return true;
/*     */         }
/*     */       
/* 425 */       } catch (ClassCastException classCastException) {}
/*     */     }
/*     */     
/* 428 */     Chunk newChunk = new Chunk(c, f);
/* 429 */     newChunk.setAttributes(chunk.getAttributes());
/* 430 */     newChunk.role = chunk.getRole();
/* 431 */     newChunk.accessibleAttributes = chunk.getAccessibleAttributes();
/* 432 */     if (this.hyphenation != null && newChunk.getHyphenation() == null && !newChunk.isEmpty()) {
/* 433 */       newChunk.setHyphenation(this.hyphenation);
/*     */     }
/* 435 */     return super.add(newChunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSpecial(Element object) {
/* 444 */     super.add(object);
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
/*     */   public void setLeading(float fixedLeading, float multipliedLeading) {
/* 459 */     this.leading = fixedLeading;
/* 460 */     this.multipliedLeading = multipliedLeading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLeading(float fixedLeading) {
/* 467 */     this.leading = fixedLeading;
/* 468 */     this.multipliedLeading = 0.0F;
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
/*     */   public void setMultipliedLeading(float multipliedLeading) {
/* 480 */     this.leading = 0.0F;
/* 481 */     this.multipliedLeading = multipliedLeading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFont(Font font) {
/* 489 */     this.font = font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLeading() {
/* 500 */     if (Float.isNaN(this.leading) && this.font != null) {
/* 501 */       return this.font.getCalculatedLeading(1.5F);
/*     */     }
/* 503 */     return this.leading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMultipliedLeading() {
/* 511 */     return this.multipliedLeading;
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
/*     */   public float getTotalLeading() {
/* 524 */     float m = (this.font == null) ? (12.0F * this.multipliedLeading) : this.font.getCalculatedLeading(this.multipliedLeading);
/* 525 */     if (m > 0.0F && !hasLeading()) {
/* 526 */       return m;
/*     */     }
/* 528 */     return getLeading() + m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLeading() {
/* 537 */     if (Float.isNaN(this.leading)) {
/* 538 */       return false;
/*     */     }
/* 540 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font getFont() {
/* 549 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContent() {
/* 558 */     StringBuffer buf = new StringBuffer();
/* 559 */     for (Chunk c : getChunks()) {
/* 560 */       buf.append(c.toString());
/*     */     }
/* 562 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*     */     Element element;
/* 573 */     switch (size()) {
/*     */       case 0:
/* 575 */         return true;
/*     */       case 1:
/* 577 */         element = get(0);
/* 578 */         if (element.type() == 10 && ((Chunk)element).isEmpty()) {
/* 579 */           return true;
/*     */         }
/* 581 */         return false;
/*     */     } 
/* 583 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HyphenationEvent getHyphenation() {
/* 593 */     return this.hyphenation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHyphenation(HyphenationEvent hyphenation) {
/* 602 */     this.hyphenation = hyphenation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TabSettings getTabSettings() {
/* 611 */     return this.tabSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTabSettings(TabSettings tabSettings) {
/* 620 */     this.tabSettings = tabSettings;
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
/*     */   public static final Phrase getInstance(String string) {
/* 639 */     return getInstance(16, string, new Font());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Phrase getInstance(int leading, String string) {
/* 649 */     return getInstance(leading, string, new Font());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Phrase getInstance(int leading, String string, Font font) {
/* 660 */     Phrase p = new Phrase(true);
/* 661 */     p.setLeading(leading);
/* 662 */     p.font = font;
/* 663 */     if (font.getFamily() != Font.FontFamily.SYMBOL && font.getFamily() != Font.FontFamily.ZAPFDINGBATS && font.getBaseFont() == null) {
/*     */       int index;
/* 665 */       while ((index = SpecialSymbol.index(string)) > -1) {
/* 666 */         if (index > 0) {
/* 667 */           String firstPart = string.substring(0, index);
/* 668 */           p.add(new Chunk(firstPart, font));
/* 669 */           string = string.substring(index);
/*     */         } 
/* 671 */         Font symbol = new Font(Font.FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
/* 672 */         StringBuffer buf = new StringBuffer();
/* 673 */         buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
/* 674 */         string = string.substring(1);
/* 675 */         while (SpecialSymbol.index(string) == 0) {
/* 676 */           buf.append(SpecialSymbol.getCorrespondingSymbol(string.charAt(0)));
/* 677 */           string = string.substring(1);
/*     */         } 
/* 679 */         p.add(new Chunk(buf.toString(), symbol));
/*     */       } 
/*     */     } 
/* 682 */     if (string != null && string.length() != 0) {
/* 683 */       p.add(new Chunk(string, font));
/*     */     }
/* 685 */     return p;
/*     */   }
/*     */   
/*     */   public boolean trim() {
/* 689 */     while (size() > 0) {
/* 690 */       Element firstChunk = get(0);
/* 691 */       if (firstChunk instanceof Chunk && ((Chunk)firstChunk).isWhitespace()) {
/* 692 */         remove(firstChunk);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 697 */     while (size() > 0) {
/* 698 */       Element lastChunk = get(size() - 1);
/* 699 */       if (lastChunk instanceof Chunk && ((Chunk)lastChunk).isWhitespace()) {
/* 700 */         remove(lastChunk);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 705 */     return (size() > 0);
/*     */   }
/*     */   
/*     */   private Phrase(boolean dummy) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Phrase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */