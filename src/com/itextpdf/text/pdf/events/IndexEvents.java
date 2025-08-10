/*     */ package com.itextpdf.text.pdf.events;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.pdf.PdfPageEventHelper;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexEvents
/*     */   extends PdfPageEventHelper
/*     */ {
/*  70 */   private Map<String, Integer> indextag = new TreeMap<String, Integer>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
/*  82 */     this.indextag.put(text, Integer.valueOf(writer.getPageNumber()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private long indexcounter = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private List<Entry> indexentry = new ArrayList<Entry>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chunk create(String text, String in1, String in2, String in3) {
/* 108 */     Chunk chunk = new Chunk(text);
/* 109 */     String tag = "idx_" + this.indexcounter++;
/* 110 */     chunk.setGenericTag(tag);
/* 111 */     chunk.setLocalDestination(tag);
/* 112 */     Entry entry = new Entry(in1, in2, in3, tag);
/* 113 */     this.indexentry.add(entry);
/* 114 */     return chunk;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Chunk create(String text, String in1) {
/* 125 */     return create(text, in1, "", "");
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
/*     */   public Chunk create(String text, String in1, String in2) {
/* 137 */     return create(text, in1, in2, "");
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
/*     */   public void create(Chunk text, String in1, String in2, String in3) {
/* 151 */     String tag = "idx_" + this.indexcounter++;
/* 152 */     text.setGenericTag(tag);
/* 153 */     text.setLocalDestination(tag);
/* 154 */     Entry entry = new Entry(in1, in2, in3, tag);
/* 155 */     this.indexentry.add(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(Chunk text, String in1) {
/* 165 */     create(text, in1, "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(Chunk text, String in1, String in2) {
/* 176 */     create(text, in1, in2, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   private Comparator<Entry> comparator = new Comparator<Entry>()
/*     */     {
/*     */       public int compare(IndexEvents.Entry en1, IndexEvents.Entry en2) {
/* 185 */         int rt = 0;
/* 186 */         if (en1.getIn1() != null && en2.getIn1() != null && (
/* 187 */           rt = en1.getIn1().compareToIgnoreCase(en2.getIn1())) == 0)
/*     */         {
/* 189 */           if (en1.getIn2() != null && en2.getIn2() != null && (
/*     */             
/* 191 */             rt = en1.getIn2().compareToIgnoreCase(en2.getIn2())) == 0)
/*     */           {
/* 193 */             if (en1.getIn3() != null && en2.getIn3() != null) {
/* 194 */               rt = en1.getIn3().compareToIgnoreCase(en2
/* 195 */                   .getIn3());
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */         
/* 201 */         return rt;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComparator(Comparator<Entry> aComparator) {
/* 210 */     this.comparator = aComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Entry> getSortedEntries() {
/* 219 */     Map<String, Entry> grouped = new HashMap<String, Entry>();
/*     */     
/* 221 */     for (int i = 0; i < this.indexentry.size(); i++) {
/* 222 */       Entry e = this.indexentry.get(i);
/* 223 */       String key = e.getKey();
/*     */       
/* 225 */       Entry master = grouped.get(key);
/* 226 */       if (master != null) {
/* 227 */         master.addPageNumberAndTag(e.getPageNumber(), e.getTag());
/*     */       } else {
/* 229 */         e.addPageNumberAndTag(e.getPageNumber(), e.getTag());
/* 230 */         grouped.put(key, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 235 */     List<Entry> sorted = new ArrayList<Entry>(grouped.values());
/* 236 */     Collections.sort(sorted, this.comparator);
/* 237 */     return sorted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Entry
/*     */   {
/*     */     private String in1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String in2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String in3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 273 */     private List<Integer> pagenumbers = new ArrayList<Integer>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 278 */     private List<String> tags = new ArrayList<String>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Entry(String aIn1, String aIn2, String aIn3, String aTag) {
/* 289 */       this.in1 = aIn1;
/* 290 */       this.in2 = aIn2;
/* 291 */       this.in3 = aIn3;
/* 292 */       this.tag = aTag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIn1() {
/* 300 */       return this.in1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIn2() {
/* 308 */       return this.in2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getIn3() {
/* 316 */       return this.in3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getTag() {
/* 324 */       return this.tag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getPageNumber() {
/* 332 */       int rt = -1;
/* 333 */       Integer i = (Integer)IndexEvents.this.indextag.get(this.tag);
/* 334 */       if (i != null) {
/* 335 */         rt = i.intValue();
/*     */       }
/* 337 */       return rt;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addPageNumberAndTag(int number, String tag) {
/* 346 */       this.pagenumbers.add(Integer.valueOf(number));
/* 347 */       this.tags.add(tag);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 355 */       return this.in1 + "!" + this.in2 + "!" + this.in3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Integer> getPagenumbers() {
/* 363 */       return this.pagenumbers;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> getTags() {
/* 371 */       return this.tags;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 380 */       StringBuffer buf = new StringBuffer();
/* 381 */       buf.append(this.in1).append(' ');
/* 382 */       buf.append(this.in2).append(' ');
/* 383 */       buf.append(this.in3).append(' ');
/* 384 */       for (int i = 0; i < this.pagenumbers.size(); i++) {
/* 385 */         buf.append(this.pagenumbers.get(i)).append(' ');
/*     */       }
/* 387 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/events/IndexEvents.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */