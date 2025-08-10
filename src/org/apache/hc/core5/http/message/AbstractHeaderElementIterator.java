/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
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
/*     */ abstract class AbstractHeaderElementIterator<T>
/*     */   implements Iterator<T>
/*     */ {
/*     */   private final Iterator<Header> headerIt;
/*     */   private T currentElement;
/*     */   private CharSequence buffer;
/*     */   private ParserCursor cursor;
/*     */   
/*     */   AbstractHeaderElementIterator(Iterator<Header> headerIterator) {
/*  54 */     this.headerIt = (Iterator<Header>)Args.notNull(headerIterator, "Header iterator");
/*     */   }
/*     */   
/*     */   private void bufferHeaderValue() {
/*  58 */     this.cursor = null;
/*  59 */     this.buffer = null;
/*  60 */     while (this.headerIt.hasNext()) {
/*  61 */       Header h = this.headerIt.next();
/*  62 */       if (h instanceof FormattedHeader) {
/*  63 */         this.buffer = (CharSequence)((FormattedHeader)h).getBuffer();
/*  64 */         this.cursor = new ParserCursor(0, this.buffer.length());
/*  65 */         this.cursor.updatePos(((FormattedHeader)h).getValuePos());
/*     */         break;
/*     */       } 
/*  68 */       String value = h.getValue();
/*  69 */       if (value != null) {
/*  70 */         this.buffer = value;
/*  71 */         this.cursor = new ParserCursor(0, value.length());
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   abstract T parseHeaderElement(CharSequence paramCharSequence, ParserCursor paramParserCursor);
/*     */   
/*     */   private void parseNextElement() {
/*  81 */     while (this.headerIt.hasNext() || this.cursor != null) {
/*  82 */       if (this.cursor == null || this.cursor.atEnd())
/*     */       {
/*  84 */         bufferHeaderValue();
/*     */       }
/*     */       
/*  87 */       if (this.cursor != null) {
/*     */         
/*  89 */         while (!this.cursor.atEnd()) {
/*  90 */           T e = parseHeaderElement(this.buffer, this.cursor);
/*  91 */           if (e != null) {
/*     */             
/*  93 */             this.currentElement = e;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*  98 */         if (this.cursor.atEnd()) {
/*     */           
/* 100 */           this.cursor = null;
/* 101 */           this.buffer = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 109 */     if (this.currentElement == null) {
/* 110 */       parseNextElement();
/*     */     }
/* 112 */     return (this.currentElement != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public T next() throws NoSuchElementException {
/* 117 */     if (this.currentElement == null) {
/* 118 */       parseNextElement();
/*     */     }
/*     */     
/* 121 */     if (this.currentElement == null) {
/* 122 */       throw new NoSuchElementException("No more header elements available");
/*     */     }
/*     */     
/* 125 */     T element = this.currentElement;
/* 126 */     this.currentElement = null;
/* 127 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 132 */     throw new UnsupportedOperationException("Remove not supported");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/AbstractHeaderElementIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */