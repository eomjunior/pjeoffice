/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.IDocument;
/*    */ import br.jus.cnj.pje.office.task.IURLOutputDocument;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
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
/*    */ class URLDocument
/*    */   extends DocumentWrapper
/*    */   implements IURLOutputDocument
/*    */ {
/*    */   protected URLDocument(IURLOutputDocument document) {
/* 38 */     super((IDocument)document);
/*    */   }
/*    */   
/*    */   private IURLOutputDocument document() {
/* 42 */     return (IURLOutputDocument)this.document;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getUrl() {
/* 47 */     return document().getUrl();
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getNome() {
/* 52 */     return document().getNome();
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isTerAtributosAssinados() {
/* 57 */     return document().isTerAtributosAssinados();
/*    */   }
/*    */ 
/*    */   
/*    */   public final List<String> getParamsEnvio() {
/* 62 */     return document().getParamsEnvio();
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getParameter(String paramName) {
/* 67 */     return document().getParameter(paramName);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/URLDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */