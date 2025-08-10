/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Meta
/*     */   implements Element
/*     */ {
/*     */   private final int type;
/*     */   private final StringBuffer content;
/*     */   public static final String UNKNOWN = "unknown";
/*     */   public static final String PRODUCER = "producer";
/*     */   public static final String CREATIONDATE = "creationdate";
/*     */   public static final String AUTHOR = "author";
/*     */   public static final String KEYWORDS = "keywords";
/*     */   public static final String SUBJECT = "subject";
/*     */   public static final String TITLE = "title";
/*     */   
/*     */   Meta(int type, String content) {
/* 123 */     this.type = type;
/* 124 */     this.content = new StringBuffer(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Meta(String tag, String content) {
/* 134 */     this.type = getType(tag);
/* 135 */     this.content = new StringBuffer(content);
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
/* 149 */       return listener.add(this);
/*     */     }
/* 151 */     catch (DocumentException de) {
/* 152 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 162 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 171 */     return new ArrayList<Chunk>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 187 */     return false;
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
/*     */   public StringBuffer append(String string) {
/* 199 */     return this.content.append(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContent() {
/* 210 */     return this.content.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 220 */     switch (this.type) {
/*     */       case 2:
/* 222 */         return "subject";
/*     */       case 3:
/* 224 */         return "keywords";
/*     */       case 4:
/* 226 */         return "author";
/*     */       case 1:
/* 228 */         return "title";
/*     */       case 5:
/* 230 */         return "producer";
/*     */       case 6:
/* 232 */         return "creationdate";
/*     */     } 
/* 234 */     return "unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getType(String tag) {
/* 245 */     if ("subject".equals(tag)) {
/* 246 */       return 2;
/*     */     }
/* 248 */     if ("keywords".equals(tag)) {
/* 249 */       return 3;
/*     */     }
/* 251 */     if ("author".equals(tag)) {
/* 252 */       return 4;
/*     */     }
/* 254 */     if ("title".equals(tag)) {
/* 255 */       return 1;
/*     */     }
/* 257 */     if ("producer".equals(tag)) {
/* 258 */       return 5;
/*     */     }
/* 260 */     if ("creationdate".equals(tag)) {
/* 261 */       return 6;
/*     */     }
/* 263 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Meta.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */