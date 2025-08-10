/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Stack<T>
/*    */   implements Iterable<T>
/*    */ {
/* 34 */   private LinkedList<T> list = new LinkedList<>();
/*    */   
/*    */   public void push(T x) {
/* 37 */     this.list.add(x);
/*    */   }
/*    */   
/*    */   public T pop() {
/* 41 */     return this.list.removeLast();
/*    */   }
/*    */   
/*    */   public T peek() {
/* 45 */     return this.list.getLast();
/*    */   }
/*    */   
/*    */   public int size() {
/* 49 */     return this.list.size();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 53 */     return this.list.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 58 */     return this.list.iterator();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 62 */     this.list.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Stack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */