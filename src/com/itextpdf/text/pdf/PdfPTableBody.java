/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.AccessibleElementId;
/*    */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*    */ import java.util.ArrayList;
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
/*    */ public class PdfPTableBody
/*    */   implements IAccessibleElement
/*    */ {
/* 54 */   protected AccessibleElementId id = new AccessibleElementId();
/* 55 */   protected ArrayList<PdfPRow> rows = null;
/* 56 */   protected PdfName role = PdfName.TBODY;
/* 57 */   protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/* 89 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(AccessibleElementId id) {
/* 93 */     this.id = id;
/*    */   }
/*    */   
/*    */   public boolean isInline() {
/* 97 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPTableBody.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */