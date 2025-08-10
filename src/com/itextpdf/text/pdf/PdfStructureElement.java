/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.AccessibleElementId;
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.List;
/*     */ import com.itextpdf.text.ListBody;
/*     */ import com.itextpdf.text.ListItem;
/*     */ import com.itextpdf.text.ListLabel;
/*     */ import com.itextpdf.text.Paragraph;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class PdfStructureElement
/*     */   extends PdfDictionary
/*     */   implements IPdfStructureElement
/*     */ {
/*     */   private transient PdfStructureElement parent;
/*     */   private transient PdfStructureTreeRoot top;
/*     */   private AccessibleElementId elementId;
/*     */   private PdfIndirectReference reference;
/*     */   private PdfName structureType;
/*     */   
/*     */   public PdfStructureElement(PdfStructureElement parent, PdfName structureType) {
/*  85 */     this.top = parent.top;
/*  86 */     init(parent, structureType);
/*  87 */     this.parent = parent;
/*  88 */     put(PdfName.P, parent.reference);
/*  89 */     put(PdfName.TYPE, PdfName.STRUCTELEM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStructureElement(PdfStructureTreeRoot root, PdfName structureType) {
/*  98 */     this.top = root;
/*     */     
/* 100 */     init(root, structureType);
/* 101 */     put(PdfName.P, root.getReference());
/* 102 */     put(PdfName.TYPE, PdfName.STRUCTELEM);
/*     */   }
/*     */   
/*     */   protected PdfStructureElement(PdfDictionary parent, PdfName structureType, AccessibleElementId elementId) {
/* 106 */     this.elementId = elementId;
/* 107 */     if (parent instanceof PdfStructureElement) {
/* 108 */       this.top = ((PdfStructureElement)parent).top;
/* 109 */       init(parent, structureType);
/* 110 */       this.parent = (PdfStructureElement)parent;
/* 111 */       put(PdfName.P, ((PdfStructureElement)parent).reference);
/* 112 */       put(PdfName.TYPE, PdfName.STRUCTELEM);
/* 113 */     } else if (parent instanceof PdfStructureTreeRoot) {
/* 114 */       this.top = (PdfStructureTreeRoot)parent;
/* 115 */       init(parent, structureType);
/* 116 */       put(PdfName.P, ((PdfStructureTreeRoot)parent).getReference());
/* 117 */       put(PdfName.TYPE, PdfName.STRUCTELEM);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getStructureType() {
/* 124 */     return this.structureType;
/*     */   }
/*     */   
/*     */   private void init(PdfDictionary parent, PdfName structureType) {
/* 128 */     if (!this.top.getWriter().getStandardStructElems().contains(structureType)) {
/* 129 */       PdfDictionary roleMap = this.top.getAsDict(PdfName.ROLEMAP);
/* 130 */       if (roleMap == null || !roleMap.contains(structureType)) {
/* 131 */         throw new ExceptionConverter(new DocumentException(MessageLocalization.getComposedMessage("unknown.structure.element.role.1", new Object[] { structureType.toString() })));
/*     */       }
/* 133 */       this.structureType = roleMap.getAsName(structureType);
/*     */     } else {
/* 135 */       this.structureType = structureType;
/*     */     } 
/* 137 */     PdfObject kido = parent.get(PdfName.K);
/* 138 */     PdfArray kids = null;
/* 139 */     if (kido == null) {
/* 140 */       kids = new PdfArray();
/* 141 */       parent.put(PdfName.K, kids);
/* 142 */     } else if (kido instanceof PdfArray) {
/* 143 */       kids = (PdfArray)kido;
/*     */     } else {
/* 145 */       kids = new PdfArray();
/* 146 */       kids.add(kido);
/* 147 */       parent.put(PdfName.K, kids);
/*     */     } 
/* 149 */     if (kids.size() > 0) {
/* 150 */       if (kids.getAsNumber(0) != null)
/* 151 */         kids.remove(0); 
/* 152 */       if (kids.size() > 0) {
/* 153 */         PdfDictionary mcr = kids.getAsDict(0);
/* 154 */         if (mcr != null && PdfName.MCR.equals(mcr.getAsName(PdfName.TYPE))) {
/* 155 */           kids.remove(0);
/*     */         }
/*     */       } 
/*     */     } 
/* 159 */     put(PdfName.S, structureType);
/* 160 */     this.reference = this.top.getWriter().getPdfIndirectReference();
/* 161 */     kids.add(this.reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getParent() {
/* 169 */     return getParent(false);
/*     */   }
/*     */   
/*     */   public PdfDictionary getParent(boolean includeStructTreeRoot) {
/* 173 */     if (this.parent == null && includeStructTreeRoot) {
/* 174 */       return this.top;
/*     */     }
/* 176 */     return this.parent;
/*     */   }
/*     */   
/*     */   void setPageMark(int page, int mark) {
/* 180 */     if (mark >= 0)
/* 181 */       put(PdfName.K, new PdfNumber(mark)); 
/* 182 */     this.top.setPageMark(page, this.reference);
/*     */   }
/*     */   
/*     */   void setAnnotation(PdfAnnotation annot, PdfIndirectReference currentPage) {
/* 186 */     PdfArray kArray = getAsArray(PdfName.K);
/* 187 */     if (kArray == null) {
/* 188 */       kArray = new PdfArray();
/* 189 */       PdfObject k = get(PdfName.K);
/* 190 */       if (k != null) {
/* 191 */         kArray.add(k);
/*     */       }
/* 193 */       put(PdfName.K, kArray);
/*     */     } 
/* 195 */     PdfDictionary dict = new PdfDictionary();
/* 196 */     dict.put(PdfName.TYPE, PdfName.OBJR);
/* 197 */     dict.put(PdfName.OBJ, annot.getIndirectReference());
/* 198 */     if (annot.getRole() == PdfName.FORM)
/* 199 */       dict.put(PdfName.PG, currentPage); 
/* 200 */     kArray.add(dict);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getReference() {
/* 209 */     return this.reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject getAttribute(PdfName name) {
/* 218 */     PdfDictionary attr = getAsDict(PdfName.A);
/* 219 */     if (attr != null && 
/* 220 */       attr.contains(name)) {
/* 221 */       return attr.get(name);
/*     */     }
/* 223 */     PdfDictionary parent = getParent();
/* 224 */     if (parent instanceof PdfStructureElement)
/* 225 */       return ((PdfStructureElement)parent).getAttribute(name); 
/* 226 */     if (parent instanceof PdfStructureTreeRoot) {
/* 227 */       return ((PdfStructureTreeRoot)parent).getAttribute(name);
/*     */     }
/* 229 */     return new PdfNull();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(PdfName name, PdfObject obj) {
/* 237 */     PdfDictionary attr = getAsDict(PdfName.A);
/* 238 */     if (attr == null) {
/* 239 */       attr = new PdfDictionary();
/* 240 */       put(PdfName.A, attr);
/*     */     } 
/* 242 */     attr.put(name, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttributes(IAccessibleElement element) {
/* 251 */     if (element instanceof ListItem) {
/* 252 */       writeAttributes((ListItem)element);
/* 253 */     } else if (element instanceof Paragraph) {
/* 254 */       writeAttributes((Paragraph)element);
/* 255 */     } else if (element instanceof Chunk) {
/* 256 */       writeAttributes((Chunk)element);
/* 257 */     } else if (element instanceof Image) {
/* 258 */       writeAttributes((Image)element);
/* 259 */     } else if (element instanceof List) {
/* 260 */       writeAttributes((List)element);
/* 261 */     } else if (element instanceof ListLabel) {
/* 262 */       writeAttributes((ListLabel)element);
/* 263 */     } else if (element instanceof ListBody) {
/* 264 */       writeAttributes((ListBody)element);
/* 265 */     } else if (element instanceof PdfPTable) {
/* 266 */       writeAttributes((PdfPTable)element);
/* 267 */     } else if (element instanceof PdfPRow) {
/* 268 */       writeAttributes((PdfPRow)element);
/* 269 */     } else if (element instanceof PdfPHeaderCell) {
/* 270 */       writeAttributes((PdfPHeaderCell)element);
/* 271 */     } else if (element instanceof PdfPCell) {
/* 272 */       writeAttributes((PdfPCell)element);
/* 273 */     } else if (element instanceof PdfPTableHeader) {
/* 274 */       writeAttributes((PdfPTableHeader)element);
/* 275 */     } else if (element instanceof PdfPTableFooter) {
/* 276 */       writeAttributes((PdfPTableFooter)element);
/* 277 */     } else if (element instanceof PdfPTableBody) {
/* 278 */       writeAttributes((PdfPTableBody)element);
/* 279 */     } else if (element instanceof PdfDiv) {
/* 280 */       writeAttributes((PdfDiv)element);
/* 281 */     } else if (element instanceof PdfTemplate) {
/* 282 */       writeAttributes((PdfTemplate)element);
/* 283 */     } else if (element instanceof Document) {
/* 284 */       writeAttributes((Document)element);
/*     */     } 
/* 286 */     if (element.getAccessibleAttributes() != null) {
/* 287 */       for (PdfName key : element.getAccessibleAttributes().keySet()) {
/* 288 */         if (key.equals(PdfName.ID)) {
/* 289 */           PdfObject attr = element.getAccessibleAttribute(key);
/* 290 */           put(key, attr);
/* 291 */           this.top.putIDTree(attr.toString(), getReference()); continue;
/*     */         } 
/* 293 */         if (key.equals(PdfName.LANG) || key.equals(PdfName.ALT) || key.equals(PdfName.ACTUALTEXT) || key.equals(PdfName.E) || key.equals(PdfName.T)) {
/* 294 */           put(key, element.getAccessibleAttribute(key)); continue;
/*     */         } 
/* 296 */         setAttribute(key, element.getAccessibleAttribute(key));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeAttributes(Chunk chunk) {
/* 303 */     if (chunk != null)
/* 304 */       if (chunk.getImage() != null) {
/* 305 */         writeAttributes(chunk.getImage());
/*     */       } else {
/* 307 */         HashMap<String, Object> attr = chunk.getAttributes();
/* 308 */         if (attr != null) {
/* 309 */           setAttribute(PdfName.O, PdfName.LAYOUT);
/*     */           
/* 311 */           if (attr.containsKey("UNDERLINE")) {
/* 312 */             setAttribute(PdfName.TEXTDECORATIONTYPE, PdfName.UNDERLINE);
/*     */           }
/* 314 */           if (attr.containsKey("BACKGROUND")) {
/* 315 */             Object[] back = (Object[])attr.get("BACKGROUND");
/* 316 */             BaseColor color = (BaseColor)back[0];
/* 317 */             setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[] { color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F }));
/*     */           } 
/*     */ 
/*     */           
/* 321 */           IPdfStructureElement parent = (IPdfStructureElement)getParent(true);
/* 322 */           PdfObject obj = getParentAttribute(parent, PdfName.COLOR);
/* 323 */           if (chunk.getFont() != null && chunk.getFont().getColor() != null) {
/* 324 */             BaseColor c = chunk.getFont().getColor();
/* 325 */             setColorAttribute(c, obj, PdfName.COLOR);
/*     */           } 
/* 327 */           PdfObject decorThickness = getParentAttribute(parent, PdfName.TEXTDECORATIONTHICKNESS);
/* 328 */           PdfObject decorColor = getParentAttribute(parent, PdfName.TEXTDECORATIONCOLOR);
/* 329 */           if (attr.containsKey("UNDERLINE")) {
/* 330 */             Object[][] unders = (Object[][])attr.get("UNDERLINE");
/* 331 */             Object[] arr = unders[unders.length - 1];
/* 332 */             BaseColor color = (BaseColor)arr[0];
/* 333 */             float[] floats = (float[])arr[1];
/* 334 */             float thickness = floats[0];
/*     */             
/* 336 */             if (decorThickness instanceof PdfNumber) {
/* 337 */               float t = ((PdfNumber)decorThickness).floatValue();
/* 338 */               if (Float.compare(thickness, t) != 0) {
/* 339 */                 setAttribute(PdfName.TEXTDECORATIONTHICKNESS, new PdfNumber(thickness));
/*     */               }
/*     */             } else {
/*     */               
/* 343 */               setAttribute(PdfName.TEXTDECORATIONTHICKNESS, new PdfNumber(thickness));
/*     */             } 
/*     */             
/* 346 */             if (color != null) {
/* 347 */               setColorAttribute(color, decorColor, PdfName.TEXTDECORATIONCOLOR);
/*     */             }
/*     */           } 
/*     */           
/* 351 */           if (attr.containsKey("LINEHEIGHT")) {
/* 352 */             float height = ((Float)attr.get("LINEHEIGHT")).floatValue();
/* 353 */             PdfObject parentLH = getParentAttribute(parent, PdfName.LINEHEIGHT);
/* 354 */             if (parentLH instanceof PdfNumber) {
/* 355 */               float pLH = ((PdfNumber)parentLH).floatValue();
/* 356 */               if (Float.compare(pLH, height) != 0) {
/* 357 */                 setAttribute(PdfName.LINEHEIGHT, new PdfNumber(height));
/*     */               }
/*     */             } else {
/*     */               
/* 361 */               setAttribute(PdfName.LINEHEIGHT, new PdfNumber(height));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   private void writeAttributes(Image image) {
/* 369 */     if (image != null) {
/* 370 */       setAttribute(PdfName.O, PdfName.LAYOUT);
/* 371 */       if (image.getWidth() > 0.0F) {
/* 372 */         setAttribute(PdfName.WIDTH, new PdfNumber(image.getWidth()));
/*     */       }
/* 374 */       if (image.getHeight() > 0.0F) {
/* 375 */         setAttribute(PdfName.HEIGHT, new PdfNumber(image.getHeight()));
/*     */       }
/* 377 */       PdfRectangle rect = new PdfRectangle((Rectangle)image, image.getRotation());
/* 378 */       setAttribute(PdfName.BBOX, rect);
/* 379 */       if (image.getBackgroundColor() != null) {
/* 380 */         BaseColor color = image.getBackgroundColor();
/* 381 */         setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[] { color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfTemplate template) {
/* 387 */     if (template != null) {
/* 388 */       setAttribute(PdfName.O, PdfName.LAYOUT);
/* 389 */       if (template.getWidth() > 0.0F) {
/* 390 */         setAttribute(PdfName.WIDTH, new PdfNumber(template.getWidth()));
/*     */       }
/* 392 */       if (template.getHeight() > 0.0F) {
/* 393 */         setAttribute(PdfName.HEIGHT, new PdfNumber(template.getHeight()));
/*     */       }
/* 395 */       PdfRectangle rect = new PdfRectangle(template.getBoundingBox());
/* 396 */       setAttribute(PdfName.BBOX, rect);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(Paragraph paragraph) {
/* 401 */     if (paragraph != null) {
/* 402 */       setAttribute(PdfName.O, PdfName.LAYOUT);
/*     */       
/* 404 */       if (Float.compare(paragraph.getSpacingBefore(), 0.0F) != 0)
/* 405 */         setAttribute(PdfName.SPACEBEFORE, new PdfNumber(paragraph.getSpacingBefore())); 
/* 406 */       if (Float.compare(paragraph.getSpacingAfter(), 0.0F) != 0) {
/* 407 */         setAttribute(PdfName.SPACEAFTER, new PdfNumber(paragraph.getSpacingAfter()));
/*     */       }
/*     */       
/* 410 */       IPdfStructureElement parent = (IPdfStructureElement)getParent(true);
/* 411 */       PdfObject obj = getParentAttribute(parent, PdfName.COLOR);
/* 412 */       if (paragraph.getFont() != null && paragraph.getFont().getColor() != null) {
/* 413 */         BaseColor c = paragraph.getFont().getColor();
/* 414 */         setColorAttribute(c, obj, PdfName.COLOR);
/*     */       } 
/* 416 */       obj = getParentAttribute(parent, PdfName.TEXTINDENT);
/* 417 */       if (Float.compare(paragraph.getFirstLineIndent(), 0.0F) != 0) {
/* 418 */         boolean writeIndent = true;
/* 419 */         if (obj instanceof PdfNumber && 
/* 420 */           Float.compare(((PdfNumber)obj).floatValue(), (new Float(paragraph.getFirstLineIndent())).floatValue()) == 0) {
/* 421 */           writeIndent = false;
/*     */         }
/* 423 */         if (writeIndent)
/* 424 */           setAttribute(PdfName.TEXTINDENT, new PdfNumber(paragraph.getFirstLineIndent())); 
/*     */       } 
/* 426 */       obj = getParentAttribute(parent, PdfName.STARTINDENT);
/* 427 */       if (obj instanceof PdfNumber) {
/* 428 */         float startIndent = ((PdfNumber)obj).floatValue();
/* 429 */         if (Float.compare(startIndent, paragraph.getIndentationLeft()) != 0) {
/* 430 */           setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
/*     */         }
/* 432 */       } else if (Math.abs(paragraph.getIndentationLeft()) > Float.MIN_VALUE) {
/* 433 */         setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
/*     */       } 
/*     */       
/* 436 */       obj = getParentAttribute(parent, PdfName.ENDINDENT);
/* 437 */       if (obj instanceof PdfNumber) {
/* 438 */         float endIndent = ((PdfNumber)obj).floatValue();
/* 439 */         if (Float.compare(endIndent, paragraph.getIndentationRight()) != 0) {
/* 440 */           setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
/*     */         }
/* 442 */       } else if (Float.compare(paragraph.getIndentationRight(), 0.0F) != 0) {
/* 443 */         setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
/*     */       } 
/*     */       
/* 446 */       setTextAlignAttribute(paragraph.getAlignment());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(List list) {
/* 451 */     if (list != null) {
/* 452 */       setAttribute(PdfName.O, PdfName.LIST);
/* 453 */       if (list.isAutoindent())
/* 454 */         if (list.isNumbered()) {
/* 455 */           if (list.isLettered()) {
/* 456 */             if (list.isLowercase()) {
/* 457 */               setAttribute(PdfName.LISTNUMBERING, PdfName.LOWERROMAN);
/*     */             } else {
/* 459 */               setAttribute(PdfName.LISTNUMBERING, PdfName.UPPERROMAN);
/*     */             } 
/*     */           } else {
/* 462 */             setAttribute(PdfName.LISTNUMBERING, PdfName.DECIMAL);
/*     */           }
/*     */         
/*     */         }
/* 466 */         else if (list.isLettered()) {
/* 467 */           if (list.isLowercase()) {
/* 468 */             setAttribute(PdfName.LISTNUMBERING, PdfName.LOWERALPHA);
/*     */           } else {
/* 470 */             setAttribute(PdfName.LISTNUMBERING, PdfName.UPPERALPHA);
/*     */           } 
/*     */         }  
/* 473 */       PdfObject obj = getParentAttribute(this.parent, PdfName.STARTINDENT);
/* 474 */       if (obj instanceof PdfNumber) {
/* 475 */         float startIndent = ((PdfNumber)obj).floatValue();
/* 476 */         if (Float.compare(startIndent, list.getIndentationLeft()) != 0) {
/* 477 */           setAttribute(PdfName.STARTINDENT, new PdfNumber(list.getIndentationLeft()));
/*     */         }
/* 479 */       } else if (Math.abs(list.getIndentationLeft()) > Float.MIN_VALUE) {
/* 480 */         setAttribute(PdfName.STARTINDENT, new PdfNumber(list.getIndentationLeft()));
/*     */       } 
/*     */       
/* 483 */       obj = getParentAttribute(this.parent, PdfName.ENDINDENT);
/* 484 */       if (obj instanceof PdfNumber) {
/* 485 */         float endIndent = ((PdfNumber)obj).floatValue();
/* 486 */         if (Float.compare(endIndent, list.getIndentationRight()) != 0) {
/* 487 */           setAttribute(PdfName.ENDINDENT, new PdfNumber(list.getIndentationRight()));
/*     */         }
/* 489 */       } else if (Float.compare(list.getIndentationRight(), 0.0F) != 0) {
/* 490 */         setAttribute(PdfName.ENDINDENT, new PdfNumber(list.getIndentationRight()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(ListItem listItem) {
/* 496 */     if (listItem != null) {
/* 497 */       PdfObject obj = getParentAttribute(this.parent, PdfName.STARTINDENT);
/* 498 */       if (obj instanceof PdfNumber) {
/* 499 */         float startIndent = ((PdfNumber)obj).floatValue();
/* 500 */         if (Float.compare(startIndent, listItem.getIndentationLeft()) != 0) {
/* 501 */           setAttribute(PdfName.STARTINDENT, new PdfNumber(listItem.getIndentationLeft()));
/*     */         }
/* 503 */       } else if (Math.abs(listItem.getIndentationLeft()) > Float.MIN_VALUE) {
/* 504 */         setAttribute(PdfName.STARTINDENT, new PdfNumber(listItem.getIndentationLeft()));
/*     */       } 
/*     */       
/* 507 */       obj = getParentAttribute(this.parent, PdfName.ENDINDENT);
/* 508 */       if (obj instanceof PdfNumber) {
/* 509 */         float endIndent = ((PdfNumber)obj).floatValue();
/* 510 */         if (Float.compare(endIndent, listItem.getIndentationRight()) != 0) {
/* 511 */           setAttribute(PdfName.ENDINDENT, new PdfNumber(listItem.getIndentationRight()));
/*     */         }
/* 513 */       } else if (Float.compare(listItem.getIndentationRight(), 0.0F) != 0) {
/* 514 */         setAttribute(PdfName.ENDINDENT, new PdfNumber(listItem.getIndentationRight()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(ListBody listBody) {
/* 520 */     if (listBody != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeAttributes(ListLabel listLabel) {
/* 526 */     if (listLabel != null) {
/* 527 */       PdfObject obj = getParentAttribute(this.parent, PdfName.STARTINDENT);
/* 528 */       if (obj instanceof PdfNumber) {
/* 529 */         float startIndent = ((PdfNumber)obj).floatValue();
/* 530 */         if (Float.compare(startIndent, listLabel.getIndentation()) != 0) {
/* 531 */           setAttribute(PdfName.STARTINDENT, new PdfNumber(listLabel.getIndentation()));
/*     */         }
/* 533 */       } else if (Math.abs(listLabel.getIndentation()) > Float.MIN_VALUE) {
/* 534 */         setAttribute(PdfName.STARTINDENT, new PdfNumber(listLabel.getIndentation()));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPTable table) {
/* 540 */     if (table != null) {
/* 541 */       setAttribute(PdfName.O, PdfName.TABLE);
/*     */       
/* 543 */       if (Float.compare(table.getSpacingBefore(), 0.0F) != 0) {
/* 544 */         setAttribute(PdfName.SPACEBEFORE, new PdfNumber(table.getSpacingBefore()));
/*     */       }
/* 546 */       if (Float.compare(table.getSpacingAfter(), 0.0F) != 0) {
/* 547 */         setAttribute(PdfName.SPACEAFTER, new PdfNumber(table.getSpacingAfter()));
/*     */       }
/* 549 */       if (table.getTotalHeight() > 0.0F) {
/* 550 */         setAttribute(PdfName.HEIGHT, new PdfNumber(table.getTotalHeight()));
/*     */       }
/* 552 */       if (table.getTotalWidth() > 0.0F) {
/* 553 */         setAttribute(PdfName.WIDTH, new PdfNumber(table.getTotalWidth()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPRow row) {
/* 559 */     if (row != null) {
/* 560 */       setAttribute(PdfName.O, PdfName.TABLE);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPCell cell) {
/* 565 */     if (cell != null) {
/* 566 */       setAttribute(PdfName.O, PdfName.TABLE);
/* 567 */       if (cell.getColspan() != 1) {
/* 568 */         setAttribute(PdfName.COLSPAN, new PdfNumber(cell.getColspan()));
/*     */       }
/* 570 */       if (cell.getRowspan() != 1) {
/* 571 */         setAttribute(PdfName.ROWSPAN, new PdfNumber(cell.getRowspan()));
/*     */       }
/* 573 */       if (cell.getHeaders() != null) {
/* 574 */         PdfArray headers = new PdfArray();
/* 575 */         ArrayList<PdfPHeaderCell> list = cell.getHeaders();
/* 576 */         for (PdfPHeaderCell header : list) {
/* 577 */           if (header.getName() != null)
/* 578 */             headers.add(new PdfString(header.getName())); 
/*     */         } 
/* 580 */         if (!headers.isEmpty()) {
/* 581 */           setAttribute(PdfName.HEADERS, headers);
/*     */         }
/*     */       } 
/* 584 */       if (cell.getCalculatedHeight() > 0.0F) {
/* 585 */         setAttribute(PdfName.HEIGHT, new PdfNumber(cell.getCalculatedHeight()));
/*     */       }
/*     */       
/* 588 */       if (cell.getWidth() > 0.0F) {
/* 589 */         setAttribute(PdfName.WIDTH, new PdfNumber(cell.getWidth()));
/*     */       }
/*     */       
/* 592 */       if (cell.getBackgroundColor() != null) {
/* 593 */         BaseColor color = cell.getBackgroundColor();
/* 594 */         setAttribute(PdfName.BACKGROUNDCOLOR, new PdfArray(new float[] { color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPHeaderCell headerCell) {
/* 600 */     if (headerCell != null) {
/* 601 */       if (headerCell.getScope() != 0)
/* 602 */         switch (headerCell.getScope()) { case 1:
/* 603 */             setAttribute(PdfName.SCOPE, PdfName.ROW); break;
/* 604 */           case 2: setAttribute(PdfName.SCOPE, PdfName.COLUMN); break;
/* 605 */           case 3: setAttribute(PdfName.SCOPE, PdfName.BOTH);
/*     */             break; }
/*     */          
/* 608 */       if (headerCell.getName() != null)
/* 609 */         setAttribute(PdfName.NAME, new PdfName(headerCell.getName())); 
/* 610 */       writeAttributes(headerCell);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPTableHeader header) {
/* 615 */     if (header != null) {
/* 616 */       setAttribute(PdfName.O, PdfName.TABLE);
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeAttributes(PdfPTableBody body) {
/* 621 */     if (body != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeAttributes(PdfPTableFooter footer) {
/* 627 */     if (footer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeAttributes(PdfDiv div) {
/* 633 */     if (div != null) {
/*     */       
/* 635 */       if (div.getBackgroundColor() != null) {
/* 636 */         setColorAttribute(div.getBackgroundColor(), (PdfObject)null, PdfName.BACKGROUNDCOLOR);
/*     */       }
/*     */       
/* 639 */       setTextAlignAttribute(div.getTextAlignment());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeAttributes(Document document) {
/* 644 */     if (document != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean colorsEqual(PdfArray parentColor, float[] color) {
/* 650 */     if (Float.compare(color[0], parentColor.getAsNumber(0).floatValue()) != 0) {
/* 651 */       return false;
/*     */     }
/* 653 */     if (Float.compare(color[1], parentColor.getAsNumber(1).floatValue()) != 0) {
/* 654 */       return false;
/*     */     }
/* 656 */     if (Float.compare(color[2], parentColor.getAsNumber(2).floatValue()) != 0) {
/* 657 */       return false;
/*     */     }
/* 659 */     return true;
/*     */   }
/*     */   
/*     */   private void setColorAttribute(BaseColor newColor, PdfObject oldColor, PdfName attributeName) {
/* 663 */     float[] colorArr = { newColor.getRed() / 255.0F, newColor.getGreen() / 255.0F, newColor.getBlue() / 255.0F };
/* 664 */     if (oldColor != null && oldColor instanceof PdfArray) {
/* 665 */       PdfArray oldC = (PdfArray)oldColor;
/* 666 */       if (colorsEqual(oldC, colorArr)) {
/*     */         
/* 668 */         setAttribute(attributeName, new PdfArray(colorArr));
/*     */       } else {
/*     */         
/* 671 */         setAttribute(attributeName, new PdfArray(colorArr));
/*     */       } 
/*     */     } else {
/* 674 */       setAttribute(attributeName, new PdfArray(colorArr));
/*     */     } 
/*     */   }
/*     */   private void setTextAlignAttribute(int elementAlign) {
/* 678 */     PdfName align = null;
/* 679 */     switch (elementAlign) {
/*     */       case 0:
/* 681 */         align = PdfName.START;
/*     */         break;
/*     */       case 1:
/* 684 */         align = PdfName.CENTER;
/*     */         break;
/*     */       case 2:
/* 687 */         align = PdfName.END;
/*     */         break;
/*     */       case 3:
/* 690 */         align = PdfName.JUSTIFY;
/*     */         break;
/*     */     } 
/* 693 */     PdfObject obj = getParentAttribute(this.parent, PdfName.TEXTALIGN);
/* 694 */     if (obj instanceof PdfName) {
/* 695 */       PdfName textAlign = (PdfName)obj;
/* 696 */       if (align != null && !textAlign.equals(align)) {
/* 697 */         setAttribute(PdfName.TEXTALIGN, align);
/*     */       }
/* 699 */     } else if (align != null && !PdfName.START.equals(align)) {
/* 700 */       setAttribute(PdfName.TEXTALIGN, align);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 706 */     PdfWriter.checkPdfIsoConformance(writer, 16, this);
/* 707 */     super.toPdf(writer, os);
/*     */   }
/*     */   
/*     */   private PdfObject getParentAttribute(IPdfStructureElement parent, PdfName name) {
/* 711 */     if (parent == null)
/* 712 */       return null; 
/* 713 */     return parent.getAttribute(name);
/*     */   }
/*     */   
/*     */   protected void setStructureTreeRoot(PdfStructureTreeRoot root) {
/* 717 */     this.top = root;
/*     */   }
/*     */   
/*     */   protected void setStructureElementParent(PdfStructureElement parent) {
/* 721 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   protected AccessibleElementId getElementId() {
/* 725 */     return this.elementId;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfStructureElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */