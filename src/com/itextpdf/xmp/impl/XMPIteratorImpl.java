/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPIterator;
/*     */ import com.itextpdf.xmp.XMPMetaFactory;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPathParser;
/*     */ import com.itextpdf.xmp.options.IteratorOptions;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ import com.itextpdf.xmp.properties.XMPPropertyInfo;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMPIteratorImpl
/*     */   implements XMPIterator
/*     */ {
/*     */   private IteratorOptions options;
/*  62 */   private String baseNS = null;
/*     */   
/*     */   protected boolean skipSiblings = false;
/*     */   
/*     */   protected boolean skipSubtree = false;
/*     */   
/*  68 */   private Iterator nodeIterator = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPIteratorImpl(XMPMetaImpl xmp, String schemaNS, String propPath, IteratorOptions options) throws XMPException {
/*  84 */     this.options = (options != null) ? options : new IteratorOptions();
/*     */ 
/*     */     
/*  87 */     XMPNode startNode = null;
/*  88 */     String initialPath = null;
/*  89 */     boolean baseSchema = (schemaNS != null && schemaNS.length() > 0);
/*  90 */     boolean baseProperty = (propPath != null && propPath.length() > 0);
/*     */     
/*  92 */     if (!baseSchema && !baseProperty) {
/*     */ 
/*     */       
/*  95 */       startNode = xmp.getRoot();
/*     */     }
/*  97 */     else if (baseSchema && baseProperty) {
/*     */ 
/*     */       
/* 100 */       XMPPath path = XMPPathParser.expandXPath(schemaNS, propPath);
/*     */ 
/*     */       
/* 103 */       XMPPath basePath = new XMPPath();
/* 104 */       for (int i = 0; i < path.size() - 1; i++)
/*     */       {
/* 106 */         basePath.add(path.getSegment(i));
/*     */       }
/*     */       
/* 109 */       startNode = XMPNodeUtils.findNode(xmp.getRoot(), path, false, null);
/* 110 */       this.baseNS = schemaNS;
/* 111 */       initialPath = basePath.toString();
/*     */     }
/* 113 */     else if (baseSchema && !baseProperty) {
/*     */ 
/*     */       
/* 116 */       startNode = XMPNodeUtils.findSchemaNode(xmp.getRoot(), schemaNS, false);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 121 */       throw new XMPException("Schema namespace URI is required", 101);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 126 */     if (startNode != null) {
/*     */       
/* 128 */       if (!this.options.isJustChildren())
/*     */       {
/* 130 */         this.nodeIterator = new NodeIterator(startNode, initialPath, 1);
/*     */       }
/*     */       else
/*     */       {
/* 134 */         this.nodeIterator = new NodeIteratorChildren(startNode, initialPath);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 140 */       this.nodeIterator = Collections.EMPTY_LIST.iterator();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipSubtree() {
/* 150 */     this.skipSubtree = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipSiblings() {
/* 159 */     skipSubtree();
/* 160 */     this.skipSiblings = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 169 */     return this.nodeIterator.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object next() {
/* 178 */     return this.nodeIterator.next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 187 */     throw new UnsupportedOperationException("The XMPIterator does not support remove().");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IteratorOptions getOptions() {
/* 196 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBaseNS() {
/* 205 */     return this.baseNS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setBaseNS(String baseNS) {
/* 214 */     this.baseNS = baseNS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class NodeIterator
/*     */     implements Iterator
/*     */   {
/*     */     protected static final int ITERATE_NODE = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected static final int ITERATE_CHILDREN = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected static final int ITERATE_QUALIFIER = 2;
/*     */ 
/*     */ 
/*     */     
/* 238 */     private int state = 0;
/*     */     
/*     */     private XMPNode visitedNode;
/*     */     
/*     */     private String path;
/*     */     
/* 244 */     private Iterator childrenIterator = null;
/*     */     
/* 246 */     private int index = 0;
/*     */     
/* 248 */     private Iterator subIterator = Collections.EMPTY_LIST.iterator();
/*     */     
/* 250 */     private XMPPropertyInfo returnProperty = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NodeIterator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NodeIterator(XMPNode visitedNode, String parentPath, int index) {
/* 270 */       this.visitedNode = visitedNode;
/* 271 */       this.state = 0;
/* 272 */       if (visitedNode.getOptions().isSchemaNode())
/*     */       {
/* 274 */         XMPIteratorImpl.this.setBaseNS(visitedNode.getName());
/*     */       }
/*     */ 
/*     */       
/* 278 */       this.path = accumulatePath(visitedNode, parentPath, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 289 */       if (this.returnProperty != null)
/*     */       {
/*     */         
/* 292 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 296 */       if (this.state == 0)
/*     */       {
/* 298 */         return reportNode();
/*     */       }
/* 300 */       if (this.state == 1) {
/*     */         
/* 302 */         if (this.childrenIterator == null)
/*     */         {
/* 304 */           this.childrenIterator = this.visitedNode.iterateChildren();
/*     */         }
/*     */         
/* 307 */         boolean hasNext = iterateChildren(this.childrenIterator);
/*     */         
/* 309 */         if (!hasNext && this.visitedNode.hasQualifier() && !XMPIteratorImpl.this.getOptions().isOmitQualifiers()) {
/*     */           
/* 311 */           this.state = 2;
/* 312 */           this.childrenIterator = null;
/* 313 */           hasNext = hasNext();
/*     */         } 
/* 315 */         return hasNext;
/*     */       } 
/*     */ 
/*     */       
/* 319 */       if (this.childrenIterator == null)
/*     */       {
/* 321 */         this.childrenIterator = this.visitedNode.iterateQualifier();
/*     */       }
/*     */       
/* 324 */       return iterateChildren(this.childrenIterator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean reportNode() {
/* 335 */       this.state = 1;
/* 336 */       if (this.visitedNode.getParent() != null && (
/* 337 */         !XMPIteratorImpl.this.getOptions().isJustLeafnodes() || !this.visitedNode.hasChildren())) {
/*     */         
/* 339 */         this.returnProperty = createPropertyInfo(this.visitedNode, XMPIteratorImpl.this.getBaseNS(), this.path);
/* 340 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 344 */       return hasNext();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean iterateChildren(Iterator<XMPNode> iterator) {
/* 356 */       if (XMPIteratorImpl.this.skipSiblings) {
/*     */ 
/*     */         
/* 359 */         XMPIteratorImpl.this.skipSiblings = false;
/* 360 */         this.subIterator = Collections.EMPTY_LIST.iterator();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 365 */       if (!this.subIterator.hasNext() && iterator.hasNext()) {
/*     */         
/* 367 */         XMPNode child = iterator.next();
/* 368 */         this.index++;
/* 369 */         this.subIterator = new NodeIterator(child, this.path, this.index);
/*     */       } 
/*     */       
/* 372 */       if (this.subIterator.hasNext()) {
/*     */         
/* 374 */         this.returnProperty = this.subIterator.next();
/* 375 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 379 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object next() {
/* 393 */       if (hasNext()) {
/*     */         
/* 395 */         XMPPropertyInfo result = this.returnProperty;
/* 396 */         this.returnProperty = null;
/* 397 */         return result;
/*     */       } 
/*     */ 
/*     */       
/* 401 */       throw new NoSuchElementException("There are no more nodes to return");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 412 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String accumulatePath(XMPNode currNode, String parentPath, int currentIndex) {
/*     */       String separator;
/*     */       String segmentName;
/* 426 */       if (currNode.getParent() == null || currNode.getOptions().isSchemaNode())
/*     */       {
/* 428 */         return null;
/*     */       }
/* 430 */       if (currNode.getParent().getOptions().isArray()) {
/*     */         
/* 432 */         separator = "";
/* 433 */         segmentName = "[" + String.valueOf(currentIndex) + "]";
/*     */       }
/*     */       else {
/*     */         
/* 437 */         separator = "/";
/* 438 */         segmentName = currNode.getName();
/*     */       } 
/*     */ 
/*     */       
/* 442 */       if (parentPath == null || parentPath.length() == 0)
/*     */       {
/* 444 */         return segmentName;
/*     */       }
/* 446 */       if (XMPIteratorImpl.this.getOptions().isJustLeafname())
/*     */       {
/* 448 */         return !segmentName.startsWith("?") ? segmentName : segmentName
/*     */           
/* 450 */           .substring(1);
/*     */       }
/*     */ 
/*     */       
/* 454 */       return parentPath + separator + segmentName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected XMPPropertyInfo createPropertyInfo(final XMPNode node, final String baseNS, final String path) {
/* 469 */       final String value = node.getOptions().isSchemaNode() ? null : node.getValue();
/*     */       
/* 471 */       return new XMPPropertyInfo()
/*     */         {
/*     */           public String getNamespace()
/*     */           {
/* 475 */             if (!node.getOptions().isSchemaNode()) {
/*     */ 
/*     */               
/* 478 */               QName qname = new QName(node.getName());
/* 479 */               return XMPMetaFactory.getSchemaRegistry().getNamespaceURI(qname.getPrefix());
/*     */             } 
/*     */ 
/*     */             
/* 483 */             return baseNS;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public String getPath() {
/* 489 */             return path;
/*     */           }
/*     */ 
/*     */           
/*     */           public String getValue() {
/* 494 */             return value;
/*     */           }
/*     */ 
/*     */           
/*     */           public PropertyOptions getOptions() {
/* 499 */             return node.getOptions();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public String getLanguage() {
/* 505 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Iterator getChildrenIterator() {
/* 516 */       return this.childrenIterator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void setChildrenIterator(Iterator childrenIterator) {
/* 525 */       this.childrenIterator = childrenIterator;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected XMPPropertyInfo getReturnProperty() {
/* 534 */       return this.returnProperty;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void setReturnProperty(XMPPropertyInfo returnProperty) {
/* 543 */       this.returnProperty = returnProperty;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class NodeIteratorChildren
/*     */     extends NodeIterator
/*     */   {
/*     */     private String parentPath;
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterator childrenIterator;
/*     */ 
/*     */ 
/*     */     
/* 561 */     private int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NodeIteratorChildren(XMPNode parentNode, String parentPath) {
/* 571 */       if (parentNode.getOptions().isSchemaNode())
/*     */       {
/* 573 */         XMPIteratorImpl.this.setBaseNS(parentNode.getName());
/*     */       }
/* 575 */       this.parentPath = accumulatePath(parentNode, parentPath, 1);
/*     */       
/* 577 */       this.childrenIterator = parentNode.iterateChildren();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 588 */       if (getReturnProperty() != null)
/*     */       {
/*     */         
/* 591 */         return true;
/*     */       }
/* 593 */       if (XMPIteratorImpl.this.skipSiblings)
/*     */       {
/* 595 */         return false;
/*     */       }
/* 597 */       if (this.childrenIterator.hasNext()) {
/*     */         
/* 599 */         XMPNode child = this.childrenIterator.next();
/* 600 */         this.index++;
/*     */         
/* 602 */         String path = null;
/* 603 */         if (child.getOptions().isSchemaNode()) {
/*     */           
/* 605 */           XMPIteratorImpl.this.setBaseNS(child.getName());
/*     */         }
/* 607 */         else if (child.getParent() != null) {
/*     */ 
/*     */           
/* 610 */           path = accumulatePath(child, this.parentPath, this.index);
/*     */         } 
/*     */ 
/*     */         
/* 614 */         if (!XMPIteratorImpl.this.getOptions().isJustLeafnodes() || !child.hasChildren()) {
/*     */           
/* 616 */           setReturnProperty(createPropertyInfo(child, XMPIteratorImpl.this.getBaseNS(), path));
/* 617 */           return true;
/*     */         } 
/*     */ 
/*     */         
/* 621 */         return hasNext();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 626 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPIteratorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */