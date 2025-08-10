/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IToken;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ final class Unavailables
/*    */ {
/*    */   static ICertificates getCertificates(final IToken token) {
/* 42 */     return new ICertificates()
/*    */       {
/*    */         public IToken getToken() {
/* 45 */           return token;
/*    */         }
/*    */ 
/*    */         
/*    */         public List<ICertificate> toList() {
/* 50 */           return Collections.emptyList();
/*    */         }
/*    */ 
/*    */         
/*    */         public Iterator<ICertificate> iterator() {
/* 55 */           return Collections.<ICertificate>emptyList().iterator();
/*    */         }
/*    */ 
/*    */         
/*    */         public int size() {
/* 60 */           return 0;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Unavailables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */