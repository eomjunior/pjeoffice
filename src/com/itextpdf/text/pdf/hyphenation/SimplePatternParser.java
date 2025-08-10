/*     */ package com.itextpdf.text.pdf.hyphenation;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
/*     */ import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimplePatternParser
/*     */   implements SimpleXMLDocHandler, PatternConsumer
/*     */ {
/*     */   int currElement;
/*     */   PatternConsumer consumer;
/*  84 */   StringBuffer token = new StringBuffer(); ArrayList<Object> exception;
/*  85 */   char hyphenChar = '-'; SimpleXMLParser parser;
/*     */   static final int ELEM_CLASSES = 1;
/*     */   
/*     */   public void parse(InputStream stream, PatternConsumer consumer) {
/*  89 */     this.consumer = consumer;
/*     */     try {
/*  91 */       SimpleXMLParser.parse(this, stream);
/*  92 */     } catch (IOException e) {
/*  93 */       throw new ExceptionConverter(e);
/*     */     } finally {
/*     */       try {
/*  96 */         stream.close();
/*  97 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */   static final int ELEM_EXCEPTIONS = 2; static final int ELEM_PATTERNS = 3; static final int ELEM_HYPHEN = 4;
/*     */   
/*     */   protected static String getPattern(String word) {
/* 103 */     StringBuffer pat = new StringBuffer();
/* 104 */     int len = word.length();
/* 105 */     for (int i = 0; i < len; i++) {
/* 106 */       if (!Character.isDigit(word.charAt(i))) {
/* 107 */         pat.append(word.charAt(i));
/*     */       }
/*     */     } 
/* 110 */     return pat.toString();
/*     */   }
/*     */   
/*     */   protected ArrayList<Object> normalizeException(ArrayList<Object> ex) {
/* 114 */     ArrayList<Object> res = new ArrayList();
/* 115 */     for (int i = 0; i < ex.size(); i++) {
/* 116 */       Object item = ex.get(i);
/* 117 */       if (item instanceof String) {
/* 118 */         String str = (String)item;
/* 119 */         StringBuffer buf = new StringBuffer();
/* 120 */         for (int j = 0; j < str.length(); j++) {
/* 121 */           char c = str.charAt(j);
/* 122 */           if (c != this.hyphenChar) {
/* 123 */             buf.append(c);
/*     */           } else {
/* 125 */             res.add(buf.toString());
/* 126 */             buf.setLength(0);
/* 127 */             char[] h = new char[1];
/* 128 */             h[0] = this.hyphenChar;
/*     */ 
/*     */             
/* 131 */             res.add(new Hyphen(new String(h), null, null));
/*     */           } 
/*     */         } 
/* 134 */         if (buf.length() > 0) {
/* 135 */           res.add(buf.toString());
/*     */         }
/*     */       } else {
/* 138 */         res.add(item);
/*     */       } 
/*     */     } 
/* 141 */     return res;
/*     */   }
/*     */   
/*     */   protected String getExceptionWord(ArrayList<Object> ex) {
/* 145 */     StringBuffer res = new StringBuffer();
/* 146 */     for (int i = 0; i < ex.size(); i++) {
/* 147 */       Object item = ex.get(i);
/* 148 */       if (item instanceof String) {
/* 149 */         res.append((String)item);
/*     */       }
/* 151 */       else if (((Hyphen)item).noBreak != null) {
/* 152 */         res.append(((Hyphen)item).noBreak);
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     return res.toString();
/*     */   }
/*     */   
/*     */   protected static String getInterletterValues(String pat) {
/* 160 */     StringBuffer il = new StringBuffer();
/* 161 */     String word = pat + "a";
/* 162 */     int len = word.length();
/* 163 */     for (int i = 0; i < len; i++) {
/* 164 */       char c = word.charAt(i);
/* 165 */       if (Character.isDigit(c)) {
/* 166 */         il.append(c);
/* 167 */         i++;
/*     */       } else {
/* 169 */         il.append('0');
/*     */       } 
/*     */     } 
/* 172 */     return il.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument() {}
/*     */ 
/*     */   
/*     */   public void endElement(String tag) {
/* 180 */     if (this.token.length() > 0) {
/* 181 */       String word = this.token.toString();
/* 182 */       switch (this.currElement) {
/*     */         case 1:
/* 184 */           this.consumer.addClass(word);
/*     */           break;
/*     */         case 2:
/* 187 */           this.exception.add(word);
/* 188 */           this.exception = normalizeException(this.exception);
/* 189 */           this.consumer.addException(getExceptionWord(this.exception), (ArrayList<Object>)this.exception
/* 190 */               .clone());
/*     */           break;
/*     */         case 3:
/* 193 */           this.consumer.addPattern(getPattern(word), 
/* 194 */               getInterletterValues(word));
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 200 */       if (this.currElement != 4) {
/* 201 */         this.token.setLength(0);
/*     */       }
/*     */     } 
/* 204 */     if (this.currElement == 4) {
/* 205 */       this.currElement = 2;
/*     */     } else {
/* 207 */       this.currElement = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public void startElement(String tag, Map<String, String> h) {
/* 215 */     if (tag.equals("hyphen-char")) {
/* 216 */       String hh = h.get("value");
/* 217 */       if (hh != null && hh.length() == 1) {
/* 218 */         this.hyphenChar = hh.charAt(0);
/*     */       }
/* 220 */     } else if (tag.equals("classes")) {
/* 221 */       this.currElement = 1;
/* 222 */     } else if (tag.equals("patterns")) {
/* 223 */       this.currElement = 3;
/* 224 */     } else if (tag.equals("exceptions")) {
/* 225 */       this.currElement = 2;
/* 226 */       this.exception = new ArrayList();
/* 227 */     } else if (tag.equals("hyphen")) {
/* 228 */       if (this.token.length() > 0) {
/* 229 */         this.exception.add(this.token.toString());
/*     */       }
/* 231 */       this.exception.add(new Hyphen(h.get("pre"), h
/* 232 */             .get("no"), h.get("post")));
/* 233 */       this.currElement = 4;
/*     */     } 
/* 235 */     this.token.setLength(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(String str) {
/* 240 */     StringTokenizer tk = new StringTokenizer(str);
/* 241 */     while (tk.hasMoreTokens()) {
/* 242 */       String word = tk.nextToken();
/*     */       
/* 244 */       switch (this.currElement) {
/*     */         case 1:
/* 246 */           this.consumer.addClass(word);
/*     */         
/*     */         case 2:
/* 249 */           this.exception.add(word);
/* 250 */           this.exception = normalizeException(this.exception);
/* 251 */           this.consumer.addException(getExceptionWord(this.exception), (ArrayList<Object>)this.exception
/* 252 */               .clone());
/* 253 */           this.exception.clear();
/*     */         
/*     */         case 3:
/* 256 */           this.consumer.addPattern(getPattern(word), 
/* 257 */               getInterletterValues(word));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClass(String c) {
/* 265 */     System.out.println("class: " + c);
/*     */   }
/*     */   
/*     */   public void addException(String w, ArrayList<Object> e) {
/* 269 */     System.out.println("exception: " + w + " : " + e.toString());
/*     */   }
/*     */   
/*     */   public void addPattern(String p, String v) {
/* 273 */     System.out.println("pattern: " + p + " : " + v);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/SimplePatternParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */