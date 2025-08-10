/*     */ package com.itextpdf.text.xml.xmp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class DublinCoreSchema
/*     */   extends XmpSchema
/*     */ {
/*     */   private static final long serialVersionUID = -4551741356374797330L;
/*     */   public static final String DEFAULT_XPATH_ID = "dc";
/*     */   public static final String DEFAULT_XPATH_URI = "http://purl.org/dc/elements/1.1/";
/*     */   public static final String CONTRIBUTOR = "dc:contributor";
/*     */   public static final String COVERAGE = "dc:coverage";
/*     */   public static final String CREATOR = "dc:creator";
/*     */   public static final String DATE = "dc:date";
/*     */   public static final String DESCRIPTION = "dc:description";
/*     */   public static final String FORMAT = "dc:format";
/*     */   public static final String IDENTIFIER = "dc:identifier";
/*     */   public static final String LANGUAGE = "dc:language";
/*     */   public static final String PUBLISHER = "dc:publisher";
/*     */   public static final String RELATION = "dc:relation";
/*     */   public static final String RIGHTS = "dc:rights";
/*     */   public static final String SOURCE = "dc:source";
/*     */   public static final String SUBJECT = "dc:subject";
/*     */   public static final String TITLE = "dc:title";
/*     */   public static final String TYPE = "dc:type";
/*     */   
/*     */   public DublinCoreSchema() {
/*  98 */     super("xmlns:dc=\"http://purl.org/dc/elements/1.1/\"");
/*  99 */     setProperty("dc:format", "application/pdf");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTitle(String title) {
/* 107 */     XmpArray array = new XmpArray("rdf:Alt");
/* 108 */     array.add(title);
/* 109 */     setProperty("dc:title", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTitle(LangAlt title) {
/* 117 */     setProperty("dc:title", title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDescription(String desc) {
/* 125 */     XmpArray array = new XmpArray("rdf:Alt");
/* 126 */     array.add(desc);
/* 127 */     setProperty("dc:description", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDescription(LangAlt desc) {
/* 135 */     setProperty("dc:description", desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubject(String subject) {
/* 143 */     XmpArray array = new XmpArray("rdf:Bag");
/* 144 */     array.add(subject);
/* 145 */     setProperty("dc:subject", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubject(String[] subject) {
/* 154 */     XmpArray array = new XmpArray("rdf:Bag");
/* 155 */     for (int i = 0; i < subject.length; i++) {
/* 156 */       array.add(subject[i]);
/*     */     }
/* 158 */     setProperty("dc:subject", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAuthor(String author) {
/* 166 */     XmpArray array = new XmpArray("rdf:Seq");
/* 167 */     array.add(author);
/* 168 */     setProperty("dc:creator", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAuthor(String[] author) {
/* 176 */     XmpArray array = new XmpArray("rdf:Seq");
/* 177 */     for (int i = 0; i < author.length; i++) {
/* 178 */       array.add(author[i]);
/*     */     }
/* 180 */     setProperty("dc:creator", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPublisher(String publisher) {
/* 188 */     XmpArray array = new XmpArray("rdf:Seq");
/* 189 */     array.add(publisher);
/* 190 */     setProperty("dc:publisher", array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPublisher(String[] publisher) {
/* 198 */     XmpArray array = new XmpArray("rdf:Seq");
/* 199 */     for (int i = 0; i < publisher.length; i++) {
/* 200 */       array.add(publisher[i]);
/*     */     }
/* 202 */     setProperty("dc:publisher", array);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/xmp/DublinCoreSchema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */