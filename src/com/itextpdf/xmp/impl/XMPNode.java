/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class XMPNode
/*     */   implements Comparable
/*     */ {
/*     */   private String name;
/*     */   private String value;
/*     */   private XMPNode parent;
/*  68 */   private List children = null;
/*     */   
/*  70 */   private List qualifier = null;
/*     */   
/*  72 */   private PropertyOptions options = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean implicit;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasAliases;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean alias;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasValueChild;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode(String name, String value, PropertyOptions options) {
/*  96 */     this.name = name;
/*  97 */     this.value = value;
/*  98 */     this.options = options;
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
/*     */   public XMPNode(String name, PropertyOptions options) {
/* 110 */     this(name, null, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 119 */     this.options = null;
/* 120 */     this.name = null;
/* 121 */     this.value = null;
/* 122 */     this.children = null;
/* 123 */     this.qualifier = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode getParent() {
/* 132 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode getChild(int index) {
/* 142 */     return getChildren().get(index - 1);
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
/*     */   public void addChild(XMPNode node) throws XMPException {
/* 154 */     assertChildNotExisting(node.getName());
/* 155 */     node.setParent(this);
/* 156 */     getChildren().add(node);
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
/*     */   public void addChild(int index, XMPNode node) throws XMPException {
/* 170 */     assertChildNotExisting(node.getName());
/* 171 */     node.setParent(this);
/* 172 */     getChildren().add(index - 1, node);
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
/*     */   public void replaceChild(int index, XMPNode node) {
/* 184 */     node.setParent(this);
/* 185 */     getChildren().set(index - 1, node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeChild(int itemIndex) {
/* 195 */     getChildren().remove(itemIndex - 1);
/* 196 */     cleanupChildren();
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
/*     */   public void removeChild(XMPNode node) {
/* 208 */     getChildren().remove(node);
/* 209 */     cleanupChildren();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanupChildren() {
/* 220 */     if (this.children.isEmpty())
/*     */     {
/* 222 */       this.children = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeChildren() {
/* 232 */     this.children = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChildrenLength() {
/* 241 */     return (this.children != null) ? this.children
/* 242 */       .size() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode findChildByName(String expr) {
/* 253 */     return find(getChildren(), expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode getQualifier(int index) {
/* 263 */     return getQualifier().get(index - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getQualifierLength() {
/* 272 */     return (this.qualifier != null) ? this.qualifier
/* 273 */       .size() : 0;
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
/*     */   public void addQualifier(XMPNode qualNode) throws XMPException {
/* 285 */     assertQualifierNotExisting(qualNode.getName());
/* 286 */     qualNode.setParent(this);
/* 287 */     qualNode.getOptions().setQualifier(true);
/* 288 */     getOptions().setHasQualifiers(true);
/*     */ 
/*     */     
/* 291 */     if (qualNode.isLanguageNode()) {
/*     */ 
/*     */       
/* 294 */       this.options.setHasLanguage(true);
/* 295 */       getQualifier().add(0, qualNode);
/*     */     }
/* 297 */     else if (qualNode.isTypeNode()) {
/*     */ 
/*     */       
/* 300 */       this.options.setHasType(true);
/* 301 */       getQualifier().add(
/* 302 */           !this.options.getHasLanguage() ? 0 : 1, qualNode);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 308 */       getQualifier().add(qualNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeQualifier(XMPNode qualNode) {
/* 319 */     PropertyOptions opts = getOptions();
/* 320 */     if (qualNode.isLanguageNode()) {
/*     */ 
/*     */       
/* 323 */       opts.setHasLanguage(false);
/*     */     }
/* 325 */     else if (qualNode.isTypeNode()) {
/*     */ 
/*     */       
/* 328 */       opts.setHasType(false);
/*     */     } 
/*     */     
/* 331 */     getQualifier().remove(qualNode);
/* 332 */     if (this.qualifier.isEmpty()) {
/*     */       
/* 334 */       opts.setHasQualifiers(false);
/* 335 */       this.qualifier = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeQualifiers() {
/* 346 */     PropertyOptions opts = getOptions();
/*     */     
/* 348 */     opts.setHasQualifiers(false);
/* 349 */     opts.setHasLanguage(false);
/* 350 */     opts.setHasType(false);
/* 351 */     this.qualifier = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMPNode findQualifierByName(String expr) {
/* 362 */     return find(this.qualifier, expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasChildren() {
/* 371 */     return (this.children != null && this.children.size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateChildren() {
/* 381 */     if (this.children != null)
/*     */     {
/* 383 */       return getChildren().iterator();
/*     */     }
/*     */ 
/*     */     
/* 387 */     return Collections.EMPTY_LIST.listIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasQualifier() {
/* 397 */     return (this.qualifier != null && this.qualifier.size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterateQualifier() {
/* 407 */     if (this.qualifier != null) {
/*     */       
/* 409 */       final Iterator it = getQualifier().iterator();
/*     */       
/* 411 */       return new Iterator()
/*     */         {
/*     */           public boolean hasNext()
/*     */           {
/* 415 */             return it.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Object next() {
/* 420 */             return it.next();
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 425 */             throw new UnsupportedOperationException("remove() is not allowed due to the internal contraints");
/*     */           }
/*     */         };
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     return Collections.EMPTY_LIST.iterator();
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
/*     */   public Object clone() {
/*     */     PropertyOptions newOptions;
/*     */     try {
/* 448 */       newOptions = new PropertyOptions(getOptions().getOptions());
/*     */     }
/* 450 */     catch (XMPException e) {
/*     */ 
/*     */       
/* 453 */       newOptions = new PropertyOptions();
/*     */     } 
/*     */     
/* 456 */     XMPNode newNode = new XMPNode(this.name, this.value, newOptions);
/* 457 */     cloneSubtree(newNode);
/*     */     
/* 459 */     return newNode;
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
/*     */   public void cloneSubtree(XMPNode destination) {
/*     */     try {
/* 473 */       for (Iterator<XMPNode> iterator1 = iterateChildren(); iterator1.hasNext(); ) {
/*     */         
/* 475 */         XMPNode child = iterator1.next();
/* 476 */         destination.addChild((XMPNode)child.clone());
/*     */       } 
/*     */       
/* 479 */       for (Iterator<XMPNode> it = iterateQualifier(); it.hasNext(); )
/*     */       {
/* 481 */         XMPNode qualifier = it.next();
/* 482 */         destination.addQualifier((XMPNode)qualifier.clone());
/*     */       }
/*     */     
/* 485 */     } catch (XMPException e) {
/*     */       assert false;
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
/*     */   public String dumpNode(boolean recursive) {
/* 501 */     StringBuffer result = new StringBuffer(512);
/* 502 */     dumpNode(result, recursive, 0, 0);
/* 503 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object xmpNode) {
/* 512 */     if (getOptions().isSchemaNode())
/*     */     {
/* 514 */       return this.value.compareTo(((XMPNode)xmpNode).getValue());
/*     */     }
/*     */ 
/*     */     
/* 518 */     return this.name.compareTo(((XMPNode)xmpNode).getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 528 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 537 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 546 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/* 555 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions getOptions() {
/* 564 */     if (this.options == null)
/*     */     {
/* 566 */       this.options = new PropertyOptions();
/*     */     }
/* 568 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(PropertyOptions options) {
/* 578 */     this.options = options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImplicit() {
/* 587 */     return this.implicit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplicit(boolean implicit) {
/* 596 */     this.implicit = implicit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasAliases() {
/* 605 */     return this.hasAliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasAliases(boolean hasAliases) {
/* 614 */     this.hasAliases = hasAliases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAlias() {
/* 623 */     return this.alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlias(boolean alias) {
/* 632 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasValueChild() {
/* 641 */     return this.hasValueChild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasValueChild(boolean hasValueChild) {
/* 650 */     this.hasValueChild = hasValueChild;
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
/*     */   public void sort() {
/* 669 */     if (hasQualifier()) {
/*     */ 
/*     */       
/* 672 */       XMPNode[] quals = (XMPNode[])getQualifier().toArray((Object[])new XMPNode[getQualifierLength()]);
/* 673 */       int sortFrom = 0;
/* 674 */       while (quals.length > sortFrom && ("xml:lang"
/*     */         
/* 676 */         .equals(quals[sortFrom].getName()) || "rdf:type"
/* 677 */         .equals(quals[sortFrom].getName()))) {
/*     */ 
/*     */         
/* 680 */         quals[sortFrom].sort();
/* 681 */         sortFrom++;
/*     */       } 
/*     */       
/* 684 */       Arrays.sort((Object[])quals, sortFrom, quals.length);
/* 685 */       ListIterator<XMPNode> it = this.qualifier.listIterator();
/* 686 */       for (int j = 0; j < quals.length; j++) {
/*     */         
/* 688 */         it.next();
/* 689 */         it.set(quals[j]);
/* 690 */         quals[j].sort();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 695 */     if (hasChildren()) {
/*     */       
/* 697 */       if (!getOptions().isArray())
/*     */       {
/* 699 */         Collections.sort(this.children);
/*     */       }
/* 701 */       for (Iterator<XMPNode> it = iterateChildren(); it.hasNext();)
/*     */       {
/* 703 */         ((XMPNode)it.next()).sort();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpNode(StringBuffer result, boolean recursive, int indent, int index) {
/* 726 */     for (int i = 0; i < indent; i++)
/*     */     {
/* 728 */       result.append('\t');
/*     */     }
/*     */ 
/*     */     
/* 732 */     if (this.parent != null) {
/*     */       
/* 734 */       if (getOptions().isQualifier())
/*     */       {
/* 736 */         result.append('?');
/* 737 */         result.append(this.name);
/*     */       }
/* 739 */       else if (getParent().getOptions().isArray())
/*     */       {
/* 741 */         result.append('[');
/* 742 */         result.append(index);
/* 743 */         result.append(']');
/*     */       }
/*     */       else
/*     */       {
/* 747 */         result.append(this.name);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 753 */       result.append("ROOT NODE");
/* 754 */       if (this.name != null && this.name.length() > 0) {
/*     */ 
/*     */         
/* 757 */         result.append(" (");
/* 758 */         result.append(this.name);
/* 759 */         result.append(')');
/*     */       } 
/*     */     } 
/*     */     
/* 763 */     if (this.value != null && this.value.length() > 0) {
/*     */       
/* 765 */       result.append(" = \"");
/* 766 */       result.append(this.value);
/* 767 */       result.append('"');
/*     */     } 
/*     */ 
/*     */     
/* 771 */     if (getOptions().containsOneOf(-1)) {
/*     */       
/* 773 */       result.append("\t(");
/* 774 */       result.append(getOptions().toString());
/* 775 */       result.append(" : ");
/* 776 */       result.append(getOptions().getOptionsString());
/* 777 */       result.append(')');
/*     */     } 
/*     */     
/* 780 */     result.append('\n');
/*     */ 
/*     */     
/* 783 */     if (recursive && hasQualifier()) {
/*     */ 
/*     */       
/* 786 */       XMPNode[] quals = (XMPNode[])getQualifier().toArray((Object[])new XMPNode[getQualifierLength()]);
/* 787 */       int j = 0;
/* 788 */       while (quals.length > j && ("xml:lang"
/* 789 */         .equals(quals[j].getName()) || "rdf:type"
/* 790 */         .equals(quals[j].getName())))
/*     */       {
/*     */         
/* 793 */         j++;
/*     */       }
/* 795 */       Arrays.sort((Object[])quals, j, quals.length);
/* 796 */       for (j = 0; j < quals.length; j++) {
/*     */         
/* 798 */         XMPNode qualifier = quals[j];
/* 799 */         qualifier.dumpNode(result, recursive, indent + 2, j + 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 804 */     if (recursive && hasChildren()) {
/*     */ 
/*     */       
/* 807 */       XMPNode[] children = (XMPNode[])getChildren().toArray((Object[])new XMPNode[getChildrenLength()]);
/* 808 */       if (!getOptions().isArray())
/*     */       {
/* 810 */         Arrays.sort((Object[])children);
/*     */       }
/* 812 */       for (int j = 0; j < children.length; j++) {
/*     */         
/* 814 */         XMPNode child = children[j];
/* 815 */         child.dumpNode(result, recursive, indent + 1, j + 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isLanguageNode() {
/* 826 */     return "xml:lang".equals(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTypeNode() {
/* 835 */     return "rdf:type".equals(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List getChildren() {
/* 846 */     if (this.children == null)
/*     */     {
/* 848 */       this.children = new ArrayList(0);
/*     */     }
/* 850 */     return this.children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getUnmodifiableChildren() {
/* 859 */     return Collections.unmodifiableList(new ArrayList(getChildren()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List getQualifier() {
/* 868 */     if (this.qualifier == null)
/*     */     {
/* 870 */       this.qualifier = new ArrayList(0);
/*     */     }
/* 872 */     return this.qualifier;
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
/*     */   protected void setParent(XMPNode parent) {
/* 885 */     this.parent = parent;
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
/*     */   private XMPNode find(List list, String expr) {
/* 898 */     if (list != null)
/*     */     {
/* 900 */       for (Iterator<XMPNode> it = list.iterator(); it.hasNext(); ) {
/*     */         
/* 902 */         XMPNode child = it.next();
/* 903 */         if (child.getName().equals(expr))
/*     */         {
/* 905 */           return child;
/*     */         }
/*     */       } 
/*     */     }
/* 909 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void assertChildNotExisting(String childName) throws XMPException {
/* 920 */     if (!"[]".equals(childName) && 
/* 921 */       findChildByName(childName) != null)
/*     */     {
/* 923 */       throw new XMPException("Duplicate property or field node '" + childName + "'", 203);
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
/*     */   private void assertQualifierNotExisting(String qualifierName) throws XMPException {
/* 936 */     if (!"[]".equals(qualifierName) && 
/* 937 */       findQualifierByName(qualifierName) != null)
/*     */     {
/* 939 */       throw new XMPException("Duplicate '" + qualifierName + "' qualifier", 203);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/XMPNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */