/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IHttpRequestPrinter;
/*    */ import br.jus.cnj.pje.office.core.IPjeRequestDumper;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.stream.Collectors;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class PjeAbstractRequestDumper
/*    */   implements IPjeRequestDumper
/*    */ {
/* 24 */   protected static final Logger LOGGER = LoggerFactory.getLogger(PjeHttpRequestDumper.class);
/*    */   
/*    */   private final Optional<String> warning;
/*    */   
/*    */   public PjeAbstractRequestDumper() {
/* 29 */     this(null);
/*    */   }
/*    */   
/*    */   public PjeAbstractRequestDumper(String warning) {
/* 33 */     this.warning = Strings.optional(warning);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dump(IHttpRequestPrinter printer) throws IOException {
/* 38 */     Args.requireNonNull(printer, "printer is null");
/*    */     
/* 40 */     printer.uri(getURI());
/* 41 */     this.warning.ifPresent(printer::warning);
/*    */     
/* 43 */     printer.method(getMethod());
/* 44 */     printer.beginHeaders();
/* 45 */     forEachHeaders((key, values) -> printer.keyValue(key, values.stream().collect(Collectors.joining(" "))));
/* 46 */     printer.endHeaders();
/*    */     
/* 48 */     printer.beginQueryParams();
/* 49 */     forEachParams(printer::keyValue);
/* 50 */     printer.endQueryParams();
/*    */     
/* 52 */     printer.body(getBody());
/*    */   }
/*    */   
/*    */   static String asString(IPjeRequestDumper dumper) {
/* 56 */     Args.requireNonNull(dumper, "dumper is null");
/* 57 */     try (StringWriter stringWriter = new StringWriter()) {
/* 58 */       try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
/* 59 */         dumper.dump(new PjeRequestPrinter(printWriter));
/*    */       } 
/* 61 */       return stringWriter.toString();
/* 62 */     } catch (IOException e) {
/* 63 */       return "Não foi possível depurar a requisição: " + e.getMessage();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract String getURI();
/*    */   
/*    */   protected abstract String getMethod();
/*    */   
/*    */   protected abstract InputStream getBody() throws IOException;
/*    */   
/*    */   protected abstract void forEachParams(BiConsumer<String, String> paramBiConsumer);
/*    */   
/*    */   protected abstract void forEachHeaders(BiConsumer<String, List<String>> paramBiConsumer);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeAbstractRequestDumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */