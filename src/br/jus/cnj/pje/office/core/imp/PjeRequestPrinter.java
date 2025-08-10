/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IHttpRequestPrinter;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PjeRequestPrinter
/*    */   implements IHttpRequestPrinter
/*    */ {
/*    */   private final PrintWriter printer;
/*    */   
/*    */   public PjeRequestPrinter(PrintWriter printer) {
/* 22 */     this.printer = (PrintWriter)Args.requireNonNull(printer, "printer is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void uri(String uri) {
/* 27 */     this.printer.print("uri: ");
/* 28 */     this.printer.println(uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public void method(String method) {
/* 33 */     this.printer.print("method: ");
/* 34 */     this.printer.println(method);
/*    */   }
/*    */ 
/*    */   
/*    */   public void beginHeaders() {
/* 39 */     this.printer.println("headers:");
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyValue(String key, String value) {
/* 44 */     this.printer.print('\t');
/* 45 */     this.printer.print(key);
/* 46 */     this.printer.print("=");
/* 47 */     this.printer.println(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void warning(String info) {
/* 52 */     this.printer.println(info);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void endHeaders() {}
/*    */ 
/*    */   
/*    */   public void beginQueryParams() {
/* 61 */     this.printer.println("query-params:");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void endQueryParams() {}
/*    */ 
/*    */   
/*    */   public void body(InputStream body) throws IOException {
/* 70 */     Args.requireNonNull(body, "body is null");
/* 71 */     this.printer.println("body:");
/* 72 */     (new BufferedReader(new InputStreamReader(body, IConstants.ISO_8859_1))).lines()
/* 73 */       .forEach(line -> Strings.split(line, '&').stream().map(Strings::trim).filter(Strings::hasText).forEach(()));
/*    */   }
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
/*    */   private static boolean isControlChar(String line) {
/* 86 */     for (int c = 0; c < line.length(); c++) {
/* 87 */       char chr = line.charAt(c);
/* 88 */       if ((chr >= '\000' && chr < ' ') || chr >= '') {
/* 89 */         return true;
/*    */       }
/*    */     } 
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeRequestPrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */