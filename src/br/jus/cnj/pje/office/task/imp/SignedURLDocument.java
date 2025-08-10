/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.UnsupportedCosignException;
/*    */ import br.jus.cnj.pje.office.task.ICosignChecker;
/*    */ import br.jus.cnj.pje.office.task.ISignedURLDocument;
/*    */ import br.jus.cnj.pje.office.task.IURLOutputDocument;
/*    */ import com.github.signer4j.IByteProcessor;
/*    */ import com.github.signer4j.ISignedData;
/*    */ import com.github.signer4j.imp.exception.OutOfMemoryException;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class SignedURLDocument
/*    */   extends URLDocument
/*    */   implements ISignedURLDocument
/*    */ {
/*    */   protected final File notSignedFile;
/*    */   private ISignedData signedData;
/*    */   
/*    */   SignedURLDocument(IURLOutputDocument arquivo, File notSignedFile) {
/* 52 */     super(arquivo);
/* 53 */     this.notSignedFile = (File)Args.requireNonNull(notSignedFile, "notSignedFile is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<ISignedData> getSignedData() {
/* 58 */     return Optional.ofNullable(this.signedData);
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 63 */     super.dispose();
/* 64 */     this.signedData = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 69 */     return this.notSignedFile.getAbsolutePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getSignatureFieldName() {
/* 74 */     return getParameter("nomeDoCampoDoArquivo").orElse("arquivo");
/*    */   }
/*    */ 
/*    */   
/*    */   public final void sign(IByteProcessor signer, ICosignChecker checker) throws Signer4JException, IOException, UnsupportedCosignException {
/* 79 */     Args.requireNonNull(signer, "signer is null");
/* 80 */     Args.requireNonNull(checker, "checker is null");
/* 81 */     if (this.signedData == null)
/*    */       try {
/* 83 */         checker.check(this.notSignedFile);
/* 84 */         this.signedData = signer.config(Boolean.valueOf(isTerAtributosAssinados())).process(this.notSignedFile);
/* 85 */       } catch (OutOfMemoryError e) {
/* 86 */         throw new OutOfMemoryException("Arquivo " + this.notSignedFile.getCanonicalPath() + " muito grande!", e);
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/SignedURLDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */