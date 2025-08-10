/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.api.Indentable;
/*     */ import com.itextpdf.text.api.Spaceable;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfPTable;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
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
/*     */ public class Paragraph
/*     */   extends Phrase
/*     */   implements Indentable, Spaceable, IAccessibleElement
/*     */ {
/*     */   private static final long serialVersionUID = 7852314969733375514L;
/*  83 */   protected int alignment = -1;
/*     */ 
/*     */   
/*     */   protected float indentationLeft;
/*     */ 
/*     */   
/*     */   protected float indentationRight;
/*     */ 
/*     */   
/*  92 */   private float firstLineIndent = 0.0F;
/*     */ 
/*     */   
/*     */   protected float spacingBefore;
/*     */ 
/*     */   
/*     */   protected float spacingAfter;
/*     */ 
/*     */   
/* 101 */   private float extraParagraphSpace = 0.0F;
/*     */ 
/*     */   
/*     */   protected boolean keeptogether = false;
/*     */   
/*     */   protected float paddingTop;
/*     */   
/* 108 */   protected PdfName role = PdfName.P;
/* 109 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/* 110 */   protected AccessibleElementId id = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(float leading) {
/* 127 */     super(leading);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(Chunk chunk) {
/* 136 */     super(chunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(float leading, Chunk chunk) {
/* 147 */     super(leading, chunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(String string) {
/* 156 */     super(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(String string, Font font) {
/* 167 */     super(string, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(float leading, String string) {
/* 178 */     super(leading, string);
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
/*     */   public Paragraph(float leading, String string, Font font) {
/* 190 */     super(leading, string, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph(Phrase phrase) {
/* 199 */     super(phrase);
/* 200 */     if (phrase instanceof Paragraph) {
/* 201 */       Paragraph p = (Paragraph)phrase;
/* 202 */       setAlignment(p.alignment);
/* 203 */       setIndentationLeft(p.getIndentationLeft());
/* 204 */       setIndentationRight(p.getIndentationRight());
/* 205 */       setFirstLineIndent(p.getFirstLineIndent());
/* 206 */       setSpacingAfter(p.getSpacingAfter());
/* 207 */       setSpacingBefore(p.getSpacingBefore());
/* 208 */       setExtraParagraphSpace(p.getExtraParagraphSpace());
/* 209 */       setRole(p.role);
/* 210 */       this.id = p.getId();
/* 211 */       if (p.accessibleAttributes != null) {
/* 212 */         this.accessibleAttributes = new HashMap<PdfName, PdfObject>(p.accessibleAttributes);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paragraph cloneShallow(boolean spacingBefore) {
/* 221 */     Paragraph copy = new Paragraph();
/* 222 */     populateProperties(copy, spacingBefore);
/* 223 */     return copy;
/*     */   }
/*     */   
/*     */   protected void populateProperties(Paragraph copy, boolean spacingBefore) {
/* 227 */     copy.setFont(getFont());
/* 228 */     copy.setAlignment(getAlignment());
/* 229 */     copy.setLeading(getLeading(), this.multipliedLeading);
/* 230 */     copy.setIndentationLeft(getIndentationLeft());
/* 231 */     copy.setIndentationRight(getIndentationRight());
/* 232 */     copy.setFirstLineIndent(getFirstLineIndent());
/* 233 */     copy.setSpacingAfter(getSpacingAfter());
/* 234 */     if (spacingBefore)
/* 235 */       copy.setSpacingBefore(getSpacingBefore()); 
/* 236 */     copy.setExtraParagraphSpace(getExtraParagraphSpace());
/* 237 */     copy.setRole(this.role);
/* 238 */     copy.id = getId();
/* 239 */     if (this.accessibleAttributes != null)
/* 240 */       copy.accessibleAttributes = new HashMap<PdfName, PdfObject>(this.accessibleAttributes); 
/* 241 */     copy.setTabSettings(getTabSettings());
/* 242 */     copy.setKeepTogether(getKeepTogether());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Element> breakUp() {
/* 250 */     List<Element> list = new ArrayList<Element>();
/* 251 */     Paragraph tmp = null;
/* 252 */     for (Element e : this) {
/* 253 */       if (e.type() == 14 || e.type() == 23 || e.type() == 12) {
/* 254 */         if (tmp != null && tmp.size() > 0) {
/* 255 */           tmp.setSpacingAfter(0.0F);
/* 256 */           list.add(tmp);
/* 257 */           tmp = cloneShallow(false);
/*     */         } 
/* 259 */         if (list.size() == 0) {
/* 260 */           ListItem firstItem; switch (e.type()) {
/*     */             case 23:
/* 262 */               ((PdfPTable)e).setSpacingBefore(getSpacingBefore());
/*     */               break;
/*     */             case 12:
/* 265 */               ((Paragraph)e).setSpacingBefore(getSpacingBefore());
/*     */               break;
/*     */             case 14:
/* 268 */               firstItem = ((List)e).getFirstItem();
/* 269 */               if (firstItem != null) {
/* 270 */                 firstItem.setSpacingBefore(getSpacingBefore());
/*     */               }
/*     */               break;
/*     */           } 
/*     */ 
/*     */         
/*     */         } 
/* 277 */         list.add(e);
/*     */         continue;
/*     */       } 
/* 280 */       if (tmp == null) {
/* 281 */         tmp = cloneShallow((list.size() == 0));
/*     */       }
/* 283 */       tmp.add(e);
/*     */     } 
/*     */     
/* 286 */     if (tmp != null && tmp.size() > 0) {
/* 287 */       list.add(tmp);
/*     */     }
/* 289 */     if (list.size() != 0) {
/* 290 */       ListItem lastItem; Element lastElement = list.get(list.size() - 1);
/* 291 */       switch (lastElement.type()) {
/*     */         case 23:
/* 293 */           ((PdfPTable)lastElement).setSpacingAfter(getSpacingAfter());
/*     */           break;
/*     */         case 12:
/* 296 */           ((Paragraph)lastElement).setSpacingAfter(getSpacingAfter());
/*     */           break;
/*     */         case 14:
/* 299 */           lastItem = ((List)lastElement).getLastItem();
/* 300 */           if (lastItem != null) {
/* 301 */             lastItem.setSpacingAfter(getSpacingAfter());
/*     */           }
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 308 */     return list;
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
/*     */   public int type() {
/* 320 */     return 12;
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
/*     */   public boolean add(Element o) {
/* 333 */     if (o instanceof List) {
/* 334 */       List list = (List)o;
/* 335 */       list.setIndentationLeft(list.getIndentationLeft() + this.indentationLeft);
/* 336 */       list.setIndentationRight(this.indentationRight);
/* 337 */       return super.add(list);
/*     */     } 
/* 339 */     if (o instanceof Image) {
/* 340 */       addSpecial(o);
/* 341 */       return true;
/*     */     } 
/* 343 */     if (o instanceof Paragraph) {
/* 344 */       addSpecial(o);
/* 345 */       return true;
/*     */     } 
/* 347 */     return super.add(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int alignment) {
/* 358 */     this.alignment = alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationLeft(float indentation) {
/* 365 */     this.indentationLeft = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentationRight(float indentation) {
/* 372 */     this.indentationRight = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFirstLineIndent(float firstLineIndent) {
/* 380 */     this.firstLineIndent = firstLineIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpacingBefore(float spacing) {
/* 387 */     this.spacingBefore = spacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpacingAfter(float spacing) {
/* 394 */     this.spacingAfter = spacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepTogether(boolean keeptogether) {
/* 403 */     this.keeptogether = keeptogether;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getKeepTogether() {
/* 412 */     return this.keeptogether;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAlignment() {
/* 423 */     return this.alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationLeft() {
/* 430 */     return this.indentationLeft;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIndentationRight() {
/* 437 */     return this.indentationRight;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFirstLineIndent() {
/* 445 */     return this.firstLineIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSpacingBefore() {
/* 452 */     return this.spacingBefore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSpacingAfter() {
/* 459 */     return this.spacingAfter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getExtraParagraphSpace() {
/* 467 */     return this.extraParagraphSpace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtraParagraphSpace(float extraParagraphSpace) {
/* 475 */     this.extraParagraphSpace = extraParagraphSpace;
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
/*     */   @Deprecated
/*     */   public float spacingBefore() {
/* 489 */     return getSpacingBefore();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public float spacingAfter() {
/* 501 */     return this.spacingAfter;
/*     */   }
/*     */   
/*     */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 505 */     if (this.accessibleAttributes != null) {
/* 506 */       return this.accessibleAttributes.get(key);
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */   
/*     */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 512 */     if (this.accessibleAttributes == null)
/* 513 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 514 */     this.accessibleAttributes.put(key, value);
/*     */   }
/*     */   
/*     */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 518 */     return this.accessibleAttributes;
/*     */   }
/*     */   
/*     */   public PdfName getRole() {
/* 522 */     return this.role;
/*     */   }
/*     */   
/*     */   public void setRole(PdfName role) {
/* 526 */     this.role = role;
/*     */   }
/*     */   
/*     */   public AccessibleElementId getId() {
/* 530 */     if (this.id == null)
/* 531 */       this.id = new AccessibleElementId(); 
/* 532 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(AccessibleElementId id) {
/* 536 */     this.id = id;
/*     */   }
/*     */   
/*     */   public boolean isInline() {
/* 540 */     return false;
/*     */   }
/*     */   
/*     */   public float getPaddingTop() {
/* 544 */     return this.paddingTop;
/*     */   }
/*     */   
/*     */   public void setPaddingTop(float paddingTop) {
/* 548 */     this.paddingTop = paddingTop;
/*     */   }
/*     */   
/*     */   public Paragraph() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Paragraph.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */