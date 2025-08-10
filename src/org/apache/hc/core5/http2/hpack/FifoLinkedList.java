/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
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
/*     */ final class FifoLinkedList
/*     */ {
/*     */   private final InternalNode master;
/*     */   private int length;
/*     */   
/*     */   FifoLinkedList() {
/*  39 */     this.master = new InternalNode(null);
/*  40 */     this.master.previous = this.master;
/*  41 */     this.master.next = this.master;
/*     */   }
/*     */   
/*     */   public Header get(int index) {
/*  45 */     Args.check((index <= this.length), "Length %s cannot be greater then index %s ", new Object[] { Integer.valueOf(this.length), Integer.valueOf(index) });
/*  46 */     Args.notNegative(index, "index");
/*  47 */     InternalNode current = this.master.next;
/*  48 */     int n = 0;
/*  49 */     while (current != this.master) {
/*  50 */       if (index == n) {
/*  51 */         return current.header;
/*     */       }
/*  53 */       current = current.next;
/*  54 */       n++;
/*     */     } 
/*  56 */     return null;
/*     */   }
/*     */   
/*     */   public int getIndex(InternalNode node) {
/*  60 */     int seqNum = node.seqNum;
/*  61 */     if (seqNum < 1) {
/*  62 */       return -1;
/*     */     }
/*  64 */     return this.length - seqNum - this.master.previous.seqNum - 1;
/*     */   }
/*     */   
/*     */   public Header getFirst() {
/*  68 */     return this.master.next.header;
/*     */   }
/*     */   
/*     */   public Header getLast() {
/*  72 */     return this.master.previous.header;
/*     */   }
/*     */   
/*     */   public int size() {
/*  76 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public InternalNode addFirst(HPackHeader header) {
/*  81 */     InternalNode newNode = new InternalNode(header);
/*  82 */     InternalNode oldNode = this.master.next;
/*  83 */     this.master.next = newNode;
/*  84 */     newNode.previous = this.master;
/*  85 */     newNode.next = oldNode;
/*  86 */     oldNode.previous = newNode;
/*  87 */     newNode.seqNum = oldNode.seqNum + 1;
/*  88 */     this.length++;
/*  89 */     return newNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public InternalNode removeLast() {
/*  94 */     InternalNode last = this.master.previous;
/*  95 */     if (last.header != null) {
/*  96 */       InternalNode lastButOne = last.previous;
/*  97 */       this.master.previous = lastButOne;
/*  98 */       lastButOne.next = this.master;
/*  99 */       last.previous = null;
/* 100 */       last.next = null;
/* 101 */       last.seqNum = 0;
/* 102 */       this.length--;
/* 103 */       return last;
/*     */     } 
/* 105 */     this.master.seqNum = 0;
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 111 */     this.master.previous = this.master;
/* 112 */     this.master.next = this.master;
/* 113 */     this.master.seqNum = 0;
/* 114 */     this.length = 0;
/*     */   }
/*     */   
/*     */   class InternalNode
/*     */     implements HPackEntry {
/*     */     private final HPackHeader header;
/*     */     private InternalNode previous;
/*     */     private InternalNode next;
/*     */     private int seqNum;
/*     */     
/*     */     InternalNode(HPackHeader header) {
/* 125 */       this.header = header;
/*     */     }
/*     */ 
/*     */     
/*     */     public HPackHeader getHeader() {
/* 130 */       return this.header;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 135 */       return StaticTable.INSTANCE.length() + FifoLinkedList.this.getIndex(this) + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 140 */       return "[" + ((this.header != null) ? this.header
/* 141 */         .toString() : "master") + "; seqNum=" + this.seqNum + "; previous=" + ((this.previous != null) ? (String)this.previous.header : null) + "; next=" + ((this.next != null) ? (String)this.next.header : null) + ']';
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/FifoLinkedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */