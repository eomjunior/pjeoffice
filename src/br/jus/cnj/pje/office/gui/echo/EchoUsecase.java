/*    */ package br.jus.cnj.pje.office.gui.echo;
/*    */ 
/*    */ import com.github.utils4j.echo.IEcho;
/*    */ import com.github.utils4j.echo.imp.EchoFrame;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import com.github.utils4j.imp.Threads;
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.subjects.BehaviorSubject;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
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
/*    */ final class EchoUsecase
/*    */ {
/*    */   public static void main(String[] args) throws InterruptedException {
/* 47 */     BehaviorSubject<String> subject = BehaviorSubject.create();
/*    */ 
/*    */ 
/*    */     
/* 51 */     EchoFrame dialog = new EchoFrame((Observable)subject, (IEcho)new Signer2BackendPanel());
/*    */     
/* 53 */     SwingTools.invokeLater(() -> dialog.showToFront());
/*    */     
/* 55 */     Threads.startAsync(() -> {
/*    */           BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
/*    */ 
/*    */           
/*    */           try {
/*    */             System.out.println("Echo input: ");
/*    */ 
/*    */             
/*    */             String line;
/*    */             
/*    */             while (Strings.hasText(line = console.readLine())) {
/*    */               subject.onNext(line);
/*    */             }
/* 68 */           } catch (IOException e) {
/*    */             subject.onError(e);
/*    */           } finally {
/*    */             subject.onComplete();
/*    */           } 
/* 73 */         }).join();
/*    */     
/* 75 */     dialog.close();
/*    */     
/* 77 */     System.out.println("Fim");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/echo/EchoUsecase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */