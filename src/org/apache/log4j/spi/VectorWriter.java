/*    */ package org.apache.log4j.spi;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Vector;
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
/*    */ class VectorWriter
/*    */   extends PrintWriter
/*    */ {
/*    */   private Vector v;
/*    */   
/*    */   VectorWriter() {
/* 36 */     super(new NullWriter());
/* 37 */     this.v = new Vector();
/*    */   }
/*    */   
/*    */   public void print(Object o) {
/* 41 */     this.v.addElement(String.valueOf(o));
/*    */   }
/*    */   
/*    */   public void print(char[] chars) {
/* 45 */     this.v.addElement(new String(chars));
/*    */   }
/*    */   
/*    */   public void print(String s) {
/* 49 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public void println(Object o) {
/* 53 */     this.v.addElement(String.valueOf(o));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void println(char[] chars) {
/* 59 */     this.v.addElement(new String(chars));
/*    */   }
/*    */   
/*    */   public void println(String s) {
/* 63 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public void write(char[] chars) {
/* 67 */     this.v.addElement(new String(chars));
/*    */   }
/*    */   
/*    */   public void write(char[] chars, int off, int len) {
/* 71 */     this.v.addElement(new String(chars, off, len));
/*    */   }
/*    */   
/*    */   public void write(String s, int off, int len) {
/* 75 */     this.v.addElement(s.substring(off, off + len));
/*    */   }
/*    */   
/*    */   public void write(String s) {
/* 79 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public String[] toStringArray() {
/* 83 */     int len = this.v.size();
/* 84 */     String[] sa = new String[len];
/* 85 */     for (int i = 0; i < len; i++) {
/* 86 */       sa[i] = this.v.elementAt(i);
/*    */     }
/* 88 */     return sa;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/spi/VectorWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */