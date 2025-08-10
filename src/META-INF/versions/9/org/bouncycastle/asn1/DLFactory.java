/*    */ package META-INF.versions.9.org.bouncycastle.asn1;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ 
/*    */ class DLFactory {
/*  5 */   static final ASN1Sequence EMPTY_SEQUENCE = (ASN1Sequence)new DLSequence();
/*  6 */   static final ASN1Set EMPTY_SET = (ASN1Set)new DLSet();
/*    */ 
/*    */   
/*    */   static ASN1Sequence createSequence(ASN1EncodableVector paramASN1EncodableVector) {
/* 10 */     if (paramASN1EncodableVector.size() < 1)
/*    */     {
/* 12 */       return EMPTY_SEQUENCE;
/*    */     }
/*    */     
/* 15 */     return (ASN1Sequence)new DLSequence(paramASN1EncodableVector);
/*    */   }
/*    */ 
/*    */   
/*    */   static ASN1Set createSet(ASN1EncodableVector paramASN1EncodableVector) {
/* 20 */     if (paramASN1EncodableVector.size() < 1)
/*    */     {
/* 22 */       return EMPTY_SET;
/*    */     }
/*    */     
/* 25 */     return (ASN1Set)new DLSet(paramASN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */