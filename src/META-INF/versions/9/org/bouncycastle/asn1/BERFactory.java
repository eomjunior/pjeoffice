/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ 
/*    */ class BERFactory {
/*  5 */   static final BERSequence EMPTY_SEQUENCE = new BERSequence();
/*  6 */   static final BERSet EMPTY_SET = new BERSet();
/*    */ 
/*    */   
/*    */   static BERSequence createSequence(ASN1EncodableVector paramASN1EncodableVector) {
/* 10 */     if (paramASN1EncodableVector.size() < 1)
/*    */     {
/* 12 */       return EMPTY_SEQUENCE;
/*    */     }
/*    */     
/* 15 */     return new BERSequence(paramASN1EncodableVector);
/*    */   }
/*    */ 
/*    */   
/*    */   static BERSet createSet(ASN1EncodableVector paramASN1EncodableVector) {
/* 20 */     if (paramASN1EncodableVector.size() < 1)
/*    */     {
/* 22 */       return EMPTY_SET;
/*    */     }
/*    */     
/* 25 */     return new BERSet(paramASN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */