/*    */ package com.fasterxml.jackson.core.sym;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Name
/*    */ {
/*    */   protected final String _name;
/*    */   protected final int _hashCode;
/*    */   
/*    */   protected Name(String name, int hashCode) {
/* 17 */     this._name = name;
/* 18 */     this._hashCode = hashCode;
/*    */   }
/*    */   public String getName() {
/* 21 */     return this._name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean equals(int paramInt);
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean equals(int paramInt1, int paramInt2);
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean equals(int paramInt1, int paramInt2, int paramInt3);
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean equals(int[] paramArrayOfint, int paramInt);
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return this._name;
/*    */   } public final int hashCode() {
/* 45 */     return this._hashCode;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 49 */     return (o == this);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/sym/Name.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */