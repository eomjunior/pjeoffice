/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicHeaderIterator
/*     */   implements Iterator<Header>
/*     */ {
/*     */   private final Header[] allHeaders;
/*     */   private int currentIndex;
/*     */   private final String headerName;
/*     */   
/*     */   public BasicHeaderIterator(Header[] headers, String name) {
/*  72 */     this.allHeaders = (Header[])Args.notNull(headers, "Header array");
/*  73 */     this.headerName = name;
/*  74 */     this.currentIndex = findNext(-1);
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
/*     */   private int findNext(int pos) {
/*  87 */     int from = pos;
/*  88 */     if (from < -1) {
/*  89 */       return -1;
/*     */     }
/*     */     
/*  92 */     int to = this.allHeaders.length - 1;
/*  93 */     boolean found = false;
/*  94 */     while (!found && from < to) {
/*  95 */       from++;
/*  96 */       found = filterHeader(from);
/*     */     } 
/*  98 */     return found ? from : -1;
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
/*     */   private boolean filterHeader(int index) {
/* 110 */     return (this.headerName == null || this.headerName
/* 111 */       .equalsIgnoreCase(this.allHeaders[index].getName()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 116 */     return (this.currentIndex >= 0);
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
/*     */   public Header next() throws NoSuchElementException {
/* 129 */     int current = this.currentIndex;
/* 130 */     if (current < 0) {
/* 131 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 134 */     this.currentIndex = findNext(current);
/*     */     
/* 136 */     return this.allHeaders[current];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 146 */     throw new UnsupportedOperationException("Removing headers is not supported.");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeaderIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */