/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ abstract class AbstractCertificates
/*    */   implements ICertificates
/*    */ {
/* 39 */   protected final List<ICertificate> certificates = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int size() {
/* 46 */     return this.certificates.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public final List<ICertificate> toList() {
/* 51 */     return Collections.unmodifiableList(this.certificates);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractCertificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */