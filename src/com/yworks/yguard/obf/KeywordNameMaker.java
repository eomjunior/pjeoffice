/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeywordNameMaker
/*     */   implements NameMaker
/*     */ {
/*     */   private static final String DUMMY_ARG_LIST = "dummy";
/*  31 */   private int skipped = 0;
/*  32 */   private Vector namesToDate = new Vector();
/*  33 */   private Hashtable argCount = new Hashtable<>();
/*  34 */   private String[] noObfNames = null;
/*     */   private String[] keywordsToUse;
/*     */   private String[] keywordsToExclude;
/*     */   private String[] firstLetter;
/*     */   private String[] nextLetter;
/*  39 */   private String[] noKeywords = new String[0];
/*  40 */   private String[] someKeywords = new String[] { "a", "if", "do", "for", "int", "new", "try", "byte", "case", "char", "else", "goto", "long", "null", "void" };
/*     */ 
/*     */ 
/*     */   
/*  44 */   private String[] allKeywords = new String[] { "if", "do", "for", "int", "new", "try", "byte", "case", "char", "else", "goto", "long", "null", "this", "void", "true", "false", "break", "catch", "class", "const", "float", "final", "short", "super", "throw", "while", "double", "import", "native", "public", "return", "static", "switch", "throws", "boolean", "default", "extends", "finally", "package", "private", "abstract", "continue", "volatile", "interface", "protected", "transient", "implements", "instanceof", "synchronized" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private static final AtomicBoolean scrambled = new AtomicBoolean(false);
/*  56 */   private static String[] firstLetterLower = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
/*     */ 
/*     */   
/*  59 */   private static String[] nextLetterLower = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
/*     */ 
/*     */   
/*  62 */   private static String[] firstLetterAll = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private static String[] nextLetterAll = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void scramble() {
/*  79 */     if (scrambled.compareAndSet(false, true)) {
/*  80 */       firstLetterLower = scramble(firstLetterLower);
/*  81 */       nextLetterLower = scramble(nextLetterLower);
/*  82 */       firstLetterAll = scramble(firstLetterAll);
/*  83 */       nextLetterAll = scramble(nextLetterAll);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String[] scramble(String... orderedInput) {
/*  88 */     List<String> list = Arrays.asList(orderedInput);
/*  89 */     Collections.shuffle(list);
/*  90 */     String[] randomOutput = list.<String>toArray(new String[0]);
/*  91 */     return randomOutput;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeywordNameMaker() {
/* 123 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeywordNameMaker(String[] noObfNames) {
/* 133 */     this(noObfNames, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeywordNameMaker(String[] noObfNames, boolean useKeywords) {
/* 144 */     this(noObfNames, true, false);
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
/*     */   public KeywordNameMaker(String[] noObfNames, boolean useKeywords, boolean lowerCaseOnly) {
/* 156 */     this.noObfNames = (noObfNames == null) ? new String[0] : noObfNames;
/* 157 */     if (useKeywords) {
/*     */       
/* 159 */       this.keywordsToUse = this.someKeywords;
/* 160 */       this.keywordsToExclude = this.someKeywords;
/*     */     }
/*     */     else {
/*     */       
/* 164 */       this.keywordsToUse = this.noKeywords;
/* 165 */       this.keywordsToExclude = this.allKeywords;
/*     */     } 
/* 167 */     if (lowerCaseOnly) {
/*     */       
/* 169 */       this.firstLetter = firstLetterLower;
/* 170 */       this.nextLetter = nextLetterLower;
/*     */     }
/*     */     else {
/*     */       
/* 174 */       this.firstLetter = firstLetterAll;
/* 175 */       this.nextLetter = nextLetterAll;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nextName(String descriptor) {
/* 183 */     String argList = "dummy";
/* 184 */     if (descriptor != null)
/*     */     {
/* 186 */       argList = getArgList(descriptor);
/*     */     }
/* 188 */     Integer intCount = (Integer)this.argCount.get(argList);
/* 189 */     int theCount = 0;
/* 190 */     if (intCount == null) {
/*     */       
/* 192 */       this.argCount.put(argList, new Integer(theCount));
/*     */     }
/*     */     else {
/*     */       
/* 196 */       theCount = intCount.intValue() + 1;
/* 197 */       this.argCount.remove(argList);
/* 198 */       this.argCount.put(argList, new Integer(theCount));
/*     */     } 
/* 200 */     return getName(theCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getArgList(String descriptor) {
/* 206 */     int pos = descriptor.indexOf(')');
/* 207 */     return descriptor.substring(1, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getName(int index) {
/* 214 */     String name = null;
/* 215 */     if (index < this.namesToDate.size()) {
/*     */       
/* 217 */       name = this.namesToDate.elementAt(index);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*     */       while (true) {
/*     */         
/* 224 */         name = getNewName(index + this.skipped);
/* 225 */         if (!Tools.isInArray(name, this.noObfNames) && (index + this.skipped < this.keywordsToUse.length || 
/*     */           
/* 227 */           !Tools.isInArray(name, this.keywordsToExclude))) {
/*     */           break;
/*     */         }
/*     */         
/* 231 */         this.skipped++;
/*     */       } 
/* 233 */       this.namesToDate.addElement(name);
/*     */     } 
/* 235 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getNewName(int index) {
/* 241 */     String name = null;
/*     */ 
/*     */     
/* 244 */     if (index < this.keywordsToUse.length) {
/*     */       
/* 246 */       name = this.keywordsToUse[index];
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 251 */       index -= this.keywordsToUse.length;
/* 252 */       if (index < this.firstLetter.length) {
/*     */         
/* 254 */         name = this.firstLetter[index];
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 259 */         index -= this.firstLetter.length;
/* 260 */         int nextLetters = 1;
/* 261 */         int subspaceSize = this.nextLetter.length;
/* 262 */         while (index >= this.firstLetter.length * subspaceSize) {
/*     */           
/* 264 */           index -= this.firstLetter.length * subspaceSize;
/* 265 */           nextLetters++;
/* 266 */           subspaceSize *= this.nextLetter.length;
/*     */         } 
/*     */ 
/*     */         
/* 270 */         StringBuffer sb = new StringBuffer(this.firstLetter[index / subspaceSize]);
/* 271 */         while (subspaceSize != 1) {
/*     */           
/* 273 */           index %= subspaceSize;
/* 274 */           subspaceSize /= this.nextLetter.length;
/* 275 */           sb.append(this.nextLetter[index / subspaceSize]);
/*     */         } 
/*     */ 
/*     */         
/* 279 */         name = sb.toString();
/*     */       } 
/*     */     } 
/* 282 */     return name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/KeywordNameMaker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */