/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.task.IDocument;
/*    */ import com.github.utils4j.imp.Args;
/*    */ 
/*    */ public class DocumentWrapper
/*    */   implements IDocument
/*    */ {
/*    */   protected final IDocument document;
/*    */   
/*    */   protected DocumentWrapper(IDocument document) {
/* 12 */     this.document = (IDocument)Args.requireNonNull(document, "document is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 17 */     this.document.dispose();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/DocumentWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */