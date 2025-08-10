/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class AccessibleElementId
/*    */   implements Comparable<AccessibleElementId>, Serializable
/*    */ {
/* 50 */   private static int id_counter = 0;
/* 51 */   private int id = 0;
/*    */   
/*    */   public AccessibleElementId() {
/* 54 */     this.id = ++id_counter;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 58 */     return Integer.toString(this.id);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 62 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     return (o instanceof AccessibleElementId && this.id == ((AccessibleElementId)o).id);
/*    */   }
/*    */   
/*    */   public int compareTo(AccessibleElementId elementId) {
/* 70 */     if (this.id < elementId.id)
/* 71 */       return -1; 
/* 72 */     if (this.id > elementId.id) {
/* 73 */       return 1;
/*    */     }
/* 75 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/AccessibleElementId.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */