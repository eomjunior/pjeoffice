/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.signer4j.imp.SwitchRepositoryException;
/*    */ import com.github.utils4j.imp.function.IBiProcedure;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public interface ICertificateListUI {
/*    */   IChoice choose(List<ICertificateEntry> paramList) throws SwitchRepositoryException;
/*    */   
/*    */   public static interface IConfigSavedCallback extends IBiProcedure<List<IFilePath>, List<IFilePath>> {
/*    */     public static final IConfigSavedCallback NOTHING = (a, b) -> {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   public static interface IChoice extends Supplier<Optional<ICertificateEntry>> {
/*    */     public static final IChoice NEED_RELOAD = () -> Optional.empty();
/*    */   }
/*    */   
/*    */   public static interface ICertificateEntry {
/*    */     String getDevice();
/*    */     
/*    */     String getName();
/*    */     
/*    */     String getIssuer();
/*    */     
/*    */     String getDate();
/*    */     
/*    */     String getId();
/*    */     
/*    */     ICertificate getCertificate();
/*    */     
/*    */     boolean isRemembered();
/*    */     
/*    */     boolean isExpired();
/*    */     
/*    */     void setRemembered(boolean param1Boolean);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificateListUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */