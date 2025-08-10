/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeItem
/*     */ {
/*     */   protected boolean isSynthetic;
/*     */   protected int access;
/*  43 */   protected ClassTree classTree = null;
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected TreeItem parent = null;
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected String sep = "/";
/*  52 */   private String inName = null;
/*  53 */   private String outName = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFixed = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFromScript = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFromScriptMap = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNRMatch(String pattern, String string) {
/*     */     try {
/*  71 */       Enumeration<Cons> enum1 = ClassTree.getNameEnum(pattern);
/*  72 */       Enumeration<Cons> enum2 = ClassTree.getNameEnum(string);
/*  73 */       while (enum1.hasMoreElements() && enum2.hasMoreElements()) {
/*  74 */         Cons nameSegment1 = enum1.nextElement();
/*  75 */         char tag1 = ((Character)nameSegment1.car).charValue();
/*  76 */         String name1 = (String)nameSegment1.cdr;
/*  77 */         Cons nameSegment2 = enum2.nextElement();
/*  78 */         char tag2 = ((Character)nameSegment2.car).charValue();
/*  79 */         String name2 = (String)nameSegment2.cdr;
/*  80 */         if (tag1 != tag2 || !isMatch(name1, name2)) {
/*  81 */           return false;
/*     */         }
/*     */       } 
/*  84 */       if (enum1.hasMoreElements() || enum2.hasMoreElements()) {
/*  85 */         return false;
/*     */       }
/*  87 */     } catch (Exception e) {
/*  88 */       return false;
/*     */     } 
/*  90 */     return true;
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
/*     */   public static boolean isMatch(String pattern, String string) {
/* 102 */     if (pattern == null || string == null) {
/* 103 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (pattern.indexOf('*') == -1) {
/* 110 */       return pattern.equals(string);
/*     */     }
/*     */ 
/*     */     
/* 114 */     int pos = -1;
/* 115 */     if (pattern.charAt(0) != '*') {
/* 116 */       pos = pattern.indexOf('*');
/* 117 */       String head = pattern.substring(0, pos);
/* 118 */       if (string.length() < head.length()) {
/* 119 */         return false;
/*     */       }
/* 121 */       if (!string.substring(0, head.length()).equals(head)) {
/* 122 */         return false;
/*     */       }
/* 124 */       pattern = pattern.substring(pos);
/* 125 */       string = string.substring(pos);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     if (pattern.charAt(pattern.length() - 1) != '*') {
/* 133 */       pos = pattern.lastIndexOf('*');
/* 134 */       String tail = pattern.substring(pos + 1);
/* 135 */       if (string.length() < tail.length()) {
/* 136 */         return false;
/*     */       }
/* 138 */       if (!string.substring(string.length() - tail.length()).equals(tail)) {
/* 139 */         return false;
/*     */       }
/* 141 */       pattern = pattern.substring(0, pos + 1);
/* 142 */       string = string.substring(0, string.length() - tail.length());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Vector<String> section = new Vector();
/* 150 */     pos = pattern.indexOf('*');
/* 151 */     int rpos = -1;
/* 152 */     while ((rpos = pattern.indexOf('*', pos + 1)) != -1) {
/* 153 */       if (rpos != pos + 1) {
/* 154 */         section.addElement(pattern.substring(pos + 1, rpos));
/*     */       }
/* 156 */       pos = rpos;
/*     */     } 
/*     */     
/* 159 */     for (Enumeration<String> enumeration = section.elements(); enumeration.hasMoreElements(); ) {
/* 160 */       String chunk = enumeration.nextElement();
/*     */       
/* 162 */       pos = string.indexOf(chunk);
/* 163 */       if (pos == -1) {
/* 164 */         return false;
/*     */       }
/* 166 */       string = string.substring(pos + chunk.length());
/*     */     } 
/* 168 */     return true;
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
/*     */   public TreeItem(TreeItem parent, String name) {
/* 182 */     this.parent = parent;
/* 183 */     this.inName = name;
/* 184 */     if (parent != null)
/*     */     {
/* 186 */       this.classTree = parent.classTree;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getModifiers() {
/* 195 */     return this.access;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInName() {
/* 202 */     return this.inName;
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
/*     */   public void setOutName(String outName) {
/* 216 */     this.outName = outName;
/* 217 */     this.isFixed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOutName() {
/* 225 */     return (this.outName != null) ? this.outName : this.inName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObfName() {
/* 232 */     return this.outName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFromScript() {
/* 237 */     this.isFromScript = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFromScriptMap() {
/* 242 */     this.isFromScriptMap = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFixed() {
/* 249 */     return this.isFixed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFromScript() {
/* 256 */     return this.isFromScript;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFromScriptMap() {
/* 263 */     return this.isFromScriptMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/* 270 */     return this.isSynthetic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(TreeItem parent) {
/* 277 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeItem getParent() {
/* 284 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFullInName() {
/* 293 */     if (this.parent == null)
/*     */     {
/* 295 */       return "";
/*     */     }
/* 297 */     if (this.parent.parent == null)
/*     */     {
/* 299 */       return getInName();
/*     */     }
/*     */ 
/*     */     
/* 303 */     return this.parent.getFullInName() + this.sep + getInName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFullOutName() {
/* 314 */     if (this.parent == null)
/*     */     {
/* 316 */       return "";
/*     */     }
/* 318 */     if (this.parent.parent == null)
/*     */     {
/* 320 */       return getOutName();
/*     */     }
/*     */ 
/*     */     
/* 324 */     return this.parent.getFullOutName() + this.sep + getOutName();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/TreeItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */