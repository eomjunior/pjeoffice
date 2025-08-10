/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificates;
/*    */ import com.github.signer4j.IChoice;
/*    */ import com.github.signer4j.IKeyStoreAccess;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.util.List;
/*    */ import java.util.Scanner;
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
/*    */ public class ConsoleChooser
/*    */   extends AbstractCertificateChooser
/*    */ {
/*    */   public ConsoleChooser(IKeyStoreAccess keyStore, ICertificates certificates) {
/* 42 */     super(keyStore, certificates);
/*    */   }
/*    */ 
/*    */   
/*    */   protected IChoice doChoose(List<DefaultCertificateEntry> options) throws Signer4JException {
/*    */     int index;
/* 48 */     Scanner sc = new Scanner(System.in);
/*    */     while (true) {
/* 50 */       System.out.println("==============================");
/* 51 */       System.out.println("= Choose your alias: ");
/* 52 */       System.out.println("==============================");
/*    */       
/* 54 */       int i = 1;
/* 55 */       for (DefaultCertificateEntry e : options) {
/* 56 */         System.out.println("[" + i++ + "] : " + e.getCertificate().getAlias() + " -> " + e.certificate.getName());
/*    */       }
/* 58 */       System.out.println("[" + i++ + "] : Cancell");
/*    */       
/* 60 */       System.out.print("Option : ");
/*    */       
/* 62 */       String option = Strings.trim(sc.nextLine());
/*    */ 
/*    */       
/* 65 */       if ((index = Strings.toInt(option, -1)) <= 0 || --index > options.size())
/*    */         continue; 
/*    */       break;
/*    */     } 
/* 69 */     if (index == options.size()) {
/* 70 */       return Choice.CANCEL;
/*    */     }
/*    */     
/* 73 */     return toChoice(options.get(index));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ConsoleChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */