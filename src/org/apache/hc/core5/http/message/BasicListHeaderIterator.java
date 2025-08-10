/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BasicListHeaderIterator
/*     */   implements Iterator<Header>
/*     */ {
/*     */   private final List<Header> allHeaders;
/*     */   private int currentIndex;
/*     */   private int lastIndex;
/*     */   private final String headerName;
/*     */   
/*     */   public BasicListHeaderIterator(List<Header> headers, String name) {
/*  78 */     this.allHeaders = (List<Header>)Args.notNull(headers, "Header list");
/*  79 */     this.headerName = name;
/*  80 */     this.currentIndex = findNext(-1);
/*  81 */     this.lastIndex = -1;
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
/*     */   protected int findNext(int pos) {
/*  94 */     int from = pos;
/*  95 */     if (from < -1) {
/*  96 */       return -1;
/*     */     }
/*     */     
/*  99 */     int to = this.allHeaders.size() - 1;
/* 100 */     boolean found = false;
/* 101 */     while (!found && from < to) {
/* 102 */       from++;
/* 103 */       found = filterHeader(from);
/*     */     } 
/* 105 */     return found ? from : -1;
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
/* 117 */     if (this.headerName == null) {
/* 118 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 122 */     String name = ((Header)this.allHeaders.get(index)).getName();
/*     */     
/* 124 */     return this.headerName.equalsIgnoreCase(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 129 */     return (this.currentIndex >= 0);
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
/*     */   public Header next() throws NoSuchElementException {
/* 141 */     int current = this.currentIndex;
/* 142 */     if (current < 0) {
/* 143 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 146 */     this.lastIndex = current;
/* 147 */     this.currentIndex = findNext(current);
/*     */     
/* 149 */     return this.allHeaders.get(current);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 157 */     Asserts.check((this.lastIndex >= 0), "No header to remove");
/* 158 */     this.allHeaders.remove(this.lastIndex);
/* 159 */     this.lastIndex = -1;
/* 160 */     this.currentIndex--;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicListHeaderIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */