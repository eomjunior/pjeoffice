/*     */ package com.itextpdf.text.pdf.hyphenation;
/*     */ 
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hyphenator
/*     */ {
/*  35 */   private static Hashtable<String, HyphenationTree> hyphenTrees = new Hashtable<String, HyphenationTree>();
/*     */   
/*  37 */   private HyphenationTree hyphenTree = null;
/*  38 */   private int remainCharCount = 2;
/*  39 */   private int pushCharCount = 2;
/*     */   
/*     */   private static final String defaultHyphLocation = "com/itextpdf/text/pdf/hyphenation/hyph/";
/*     */   
/*  43 */   private static String hyphenDir = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hyphenator(String lang, String country, int leftMin, int rightMin) {
/*  53 */     this.hyphenTree = getHyphenationTree(lang, country);
/*  54 */     this.remainCharCount = leftMin;
/*  55 */     this.pushCharCount = rightMin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HyphenationTree getHyphenationTree(String lang, String country) {
/*  65 */     String key = lang;
/*     */     
/*  67 */     if (country != null && !country.equals("none")) {
/*  68 */       key = key + "_" + country;
/*     */     }
/*     */     
/*  71 */     if (hyphenTrees.containsKey(key)) {
/*  72 */       return hyphenTrees.get(key);
/*     */     }
/*  74 */     if (hyphenTrees.containsKey(lang)) {
/*  75 */       return hyphenTrees.get(lang);
/*     */     }
/*     */     
/*  78 */     HyphenationTree hTree = getResourceHyphenationTree(key);
/*  79 */     if (hTree == null) {
/*  80 */       hTree = getFileHyphenationTree(key);
/*     */     }
/*  82 */     if (hTree != null) {
/*  83 */       hyphenTrees.put(key, hTree);
/*     */     }
/*  85 */     return hTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HyphenationTree getResourceHyphenationTree(String key) {
/*     */     try {
/*  94 */       InputStream stream = StreamUtil.getResourceStream("com/itextpdf/text/pdf/hyphenation/hyph/" + key + ".xml");
/*  95 */       if (stream == null && key.length() > 2)
/*  96 */         stream = StreamUtil.getResourceStream("com/itextpdf/text/pdf/hyphenation/hyph/" + key.substring(0, 2) + ".xml"); 
/*  97 */       if (stream == null)
/*  98 */         return null; 
/*  99 */       HyphenationTree hTree = new HyphenationTree();
/* 100 */       hTree.loadSimplePatterns(stream);
/* 101 */       return hTree;
/*     */     }
/* 103 */     catch (Exception e) {
/* 104 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HyphenationTree getFileHyphenationTree(String key) {
/*     */     try {
/* 114 */       if (hyphenDir == null)
/* 115 */         return null; 
/* 116 */       InputStream stream = null;
/* 117 */       File hyphenFile = new File(hyphenDir, key + ".xml");
/* 118 */       if (hyphenFile.canRead())
/* 119 */         stream = new FileInputStream(hyphenFile); 
/* 120 */       if (stream == null && key.length() > 2) {
/* 121 */         hyphenFile = new File(hyphenDir, key.substring(0, 2) + ".xml");
/* 122 */         if (hyphenFile.canRead())
/* 123 */           stream = new FileInputStream(hyphenFile); 
/*     */       } 
/* 125 */       if (stream == null)
/* 126 */         return null; 
/* 127 */       HyphenationTree hTree = new HyphenationTree();
/* 128 */       hTree.loadSimplePatterns(stream);
/* 129 */       return hTree;
/*     */     }
/* 131 */     catch (Exception e) {
/* 132 */       return null;
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
/*     */   public static Hyphenation hyphenate(String lang, String country, String word, int leftMin, int rightMin) {
/* 147 */     HyphenationTree hTree = getHyphenationTree(lang, country);
/* 148 */     if (hTree == null)
/*     */     {
/*     */       
/* 151 */       return null;
/*     */     }
/* 153 */     return hTree.hyphenate(word, leftMin, rightMin);
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
/*     */   public static Hyphenation hyphenate(String lang, String country, char[] word, int offset, int len, int leftMin, int rightMin) {
/* 169 */     HyphenationTree hTree = getHyphenationTree(lang, country);
/* 170 */     if (hTree == null)
/*     */     {
/*     */       
/* 173 */       return null;
/*     */     }
/* 175 */     return hTree.hyphenate(word, offset, len, leftMin, rightMin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinRemainCharCount(int min) {
/* 182 */     this.remainCharCount = min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinPushCharCount(int min) {
/* 189 */     this.pushCharCount = min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLanguage(String lang, String country) {
/* 197 */     this.hyphenTree = getHyphenationTree(lang, country);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hyphenation hyphenate(char[] word, int offset, int len) {
/* 207 */     if (this.hyphenTree == null) {
/* 208 */       return null;
/*     */     }
/* 210 */     return this.hyphenTree.hyphenate(word, offset, len, this.remainCharCount, this.pushCharCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hyphenation hyphenate(String word) {
/* 219 */     if (this.hyphenTree == null) {
/* 220 */       return null;
/*     */     }
/* 222 */     return this.hyphenTree.hyphenate(word, this.remainCharCount, this.pushCharCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHyphenDir() {
/* 229 */     return hyphenDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setHyphenDir(String _hyphenDir) {
/* 236 */     hyphenDir = _hyphenDir;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/Hyphenator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */