/*    */ package com.itextpdf.text;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfName;
/*    */ import com.itextpdf.text.pdf.PdfObject;
/*    */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ public class ListBody
/*    */   implements IAccessibleElement
/*    */ {
/* 54 */   protected PdfName role = PdfName.LBODY;
/* 55 */   private AccessibleElementId id = null;
/* 56 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/* 57 */   protected ListItem parentItem = null;
/*    */   
/*    */   protected ListBody(ListItem parentItem) {
/* 60 */     this.parentItem = parentItem;
/*    */   }
/*    */   
/*    */   public PdfObject getAccessibleAttribute(PdfName key) {
/* 64 */     if (this.accessibleAttributes != null) {
/* 65 */       return this.accessibleAttributes.get(key);
/*    */     }
/* 67 */     return null;
/*    */   }
/*    */   
/*    */   public void setAccessibleAttribute(PdfName key, PdfObject value) {
/* 71 */     if (this.accessibleAttributes == null)
/* 72 */       this.accessibleAttributes = new HashMap<PdfName, PdfObject>(); 
/* 73 */     this.accessibleAttributes.put(key, value);
/*    */   }
/*    */   
/*    */   public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
/* 77 */     return this.accessibleAttributes;
/*    */   }
/*    */   
/*    */   public PdfName getRole() {
/* 81 */     return this.role;
/*    */   }
/*    */   
/*    */   public void setRole(PdfName role) {
/* 85 */     this.role = role;
/*    */   }
/*    */   
/*    */   public AccessibleElementId getId() {
/* 89 */     if (this.id == null)
/* 90 */       this.id = new AccessibleElementId(); 
/* 91 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(AccessibleElementId id) {
/* 95 */     this.id = id;
/*    */   }
/*    */   
/*    */   public boolean isInline() {
/* 99 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/ListBody.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */