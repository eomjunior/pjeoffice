/*     */ package org.apache.hc.core5.http.config;
/*     */ 
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
/*     */ public class NamedElementChain<E>
/*     */ {
/*     */   private final Node master;
/*     */   private int size;
/*     */   
/*     */   public NamedElementChain() {
/*  45 */     this.master = new Node("master", null);
/*  46 */     this.master.previous = this.master;
/*  47 */     this.master.next = this.master;
/*  48 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public Node getFirst() {
/*  52 */     return (this.master.next != this.master) ? this.master.next : null;
/*     */   }
/*     */   
/*     */   public Node getLast() {
/*  56 */     return (this.master.previous != this.master) ? this.master.previous : null;
/*     */   }
/*     */   
/*     */   public Node addFirst(E value, String name) {
/*  60 */     Args.notBlank(name, "Name");
/*  61 */     Args.notNull(value, "Value");
/*  62 */     Node newNode = new Node(name, value);
/*  63 */     Node oldNode = this.master.next;
/*  64 */     this.master.next = newNode;
/*  65 */     newNode.previous = this.master;
/*  66 */     newNode.next = oldNode;
/*  67 */     oldNode.previous = newNode;
/*  68 */     this.size++;
/*  69 */     return newNode;
/*     */   }
/*     */   
/*     */   public Node addLast(E value, String name) {
/*  73 */     Args.notBlank(name, "Name");
/*  74 */     Args.notNull(value, "Value");
/*  75 */     Node newNode = new Node(name, value);
/*  76 */     Node oldNode = this.master.previous;
/*  77 */     this.master.previous = newNode;
/*  78 */     newNode.previous = oldNode;
/*  79 */     newNode.next = this.master;
/*  80 */     oldNode.next = newNode;
/*  81 */     this.size++;
/*  82 */     return newNode;
/*     */   }
/*     */   
/*     */   public Node find(String name) {
/*  86 */     Args.notBlank(name, "Name");
/*  87 */     return doFind(name);
/*     */   }
/*     */   
/*     */   private Node doFind(String name) {
/*  91 */     Node current = this.master.next;
/*  92 */     while (current != this.master) {
/*  93 */       if (name.equals(current.name)) {
/*  94 */         return current;
/*     */       }
/*  96 */       current = current.next;
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public Node addBefore(String existing, E value, String name) {
/* 102 */     Args.notBlank(name, "Name");
/* 103 */     Args.notNull(value, "Value");
/* 104 */     Node current = doFind(existing);
/* 105 */     if (current == null) {
/* 106 */       return null;
/*     */     }
/* 108 */     Node newNode = new Node(name, value);
/* 109 */     Node previousNode = current.previous;
/* 110 */     previousNode.next = newNode;
/* 111 */     newNode.previous = previousNode;
/* 112 */     newNode.next = current;
/* 113 */     current.previous = newNode;
/* 114 */     this.size++;
/* 115 */     return newNode;
/*     */   }
/*     */   
/*     */   public Node addAfter(String existing, E value, String name) {
/* 119 */     Args.notBlank(name, "Name");
/* 120 */     Args.notNull(value, "Value");
/* 121 */     Node current = doFind(existing);
/* 122 */     if (current == null) {
/* 123 */       return null;
/*     */     }
/* 125 */     Node newNode = new Node(name, value);
/* 126 */     Node nextNode = current.next;
/* 127 */     current.next = newNode;
/* 128 */     newNode.previous = current;
/* 129 */     newNode.next = nextNode;
/* 130 */     nextNode.previous = newNode;
/* 131 */     this.size++;
/* 132 */     return newNode;
/*     */   }
/*     */   
/*     */   public boolean remove(String name) {
/* 136 */     Node node = doFind(name);
/* 137 */     if (node == null) {
/* 138 */       return false;
/*     */     }
/* 140 */     node.previous.next = node.next;
/* 141 */     node.next.previous = node.previous;
/* 142 */     node.previous = null;
/* 143 */     node.next = null;
/* 144 */     this.size--;
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public boolean replace(String existing, E value) {
/* 149 */     Node node = doFind(existing);
/* 150 */     if (node == null) {
/* 151 */       return false;
/*     */     }
/* 153 */     node.value = value;
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 158 */     return this.size;
/*     */   }
/*     */   
/*     */   public class Node
/*     */   {
/*     */     private final String name;
/*     */     private E value;
/*     */     private Node previous;
/*     */     private Node next;
/*     */     
/*     */     Node(String name, E value) {
/* 169 */       this.name = name;
/* 170 */       this.value = value;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 174 */       return this.name;
/*     */     }
/*     */     
/*     */     public E getValue() {
/* 178 */       return this.value;
/*     */     }
/*     */     
/*     */     public Node getPrevious() {
/* 182 */       return (this.previous != NamedElementChain.this.master) ? this.previous : null;
/*     */     }
/*     */     
/*     */     public Node getNext() {
/* 186 */       return (this.next != NamedElementChain.this.master) ? this.next : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 191 */       return this.name + ": " + this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/config/NamedElementChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */