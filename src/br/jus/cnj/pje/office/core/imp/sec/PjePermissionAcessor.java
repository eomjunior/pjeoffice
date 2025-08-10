/*    */ package br.jus.cnj.pje.office.core.imp.sec;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjePermissionAccessor;
/*    */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*    */ import br.jus.cnj.pje.office.core.imp.PjeAccessTime;
/*    */ import br.jus.cnj.pje.office.gui.alert.PjePermissionAccessor;
/*    */ import com.github.utils4j.imp.Strings;
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
/*    */ 
/*    */ 
/*    */ enum PjePermissionAcessor
/*    */   implements IPjePermissionAccessor
/*    */ {
/* 42 */   PRODUCTION {
/* 43 */     private final IPjePermissionAccessor acessor = (IPjePermissionAccessor)new PjePermissionAccessor();
/*    */ 
/*    */     
/*    */     public PjeAccessTime tryAccess(IPjeServerAccess token) {
/* 47 */       return this.acessor.tryAccess(token);
/*    */     }
/*    */   },
/* 50 */   CONSOLE
/*    */   {
/*    */     public PjeAccessTime tryAccess(IPjeServerAccess token) {
/*    */       PjeAccessTime choosed;
/* 54 */       Scanner sc = new Scanner(System.in);
/*    */       while (true) {
/* 56 */         System.out.println("==============================");
/* 57 */         System.out.println("= Autorização de Servidor:    ");
/* 58 */         System.out.println("==============================");
/*    */         
/* 60 */         PjeAccessTime.printOptions(System.out);
/*    */         
/* 62 */         System.out.print("Option : ");
/* 63 */         String option = Strings.trim(sc.nextLine());
/*    */         
/* 65 */         choosed = PjeAccessTime.fromOptions(Strings.toInt(option, -1) - 1);
/*    */         
/* 67 */         if (choosed == null)
/*    */           continue; 
/*    */         break;
/*    */       } 
/* 71 */       return choosed;
/*    */     }
/*    */   };
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/sec/PjePermissionAcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */