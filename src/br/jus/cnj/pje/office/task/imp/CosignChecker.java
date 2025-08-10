/*    */ package br.jus.cnj.pje.office.task.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.imp.UnsupportedCosignException;
/*    */ import br.jus.cnj.pje.office.task.ICosignChecker;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.OpenByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.file.Files;
/*    */ import org.bouncycastle.cms.CMSException;
/*    */ import org.bouncycastle.cms.CMSSignedData;
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
/*    */ enum CosignChecker
/*    */   implements ICosignChecker
/*    */ {
/* 45 */   XML
/*    */   {
/*    */     
/*    */     public void check(File file) {}
/*    */   },
/* 50 */   CMS
/*    */   {
/*    */     public void check(File file) throws IOException, UnsupportedCosignException {
/* 53 */       Args.requireNonNull(file, "file is null");
/* 54 */       UnsupportedCosignException rethrow = null;
/* 55 */       try (OpenByteArrayOutputStream out = new OpenByteArrayOutputStream(file.length())) {
/* 56 */         Files.copy(file.toPath(), (OutputStream)out);
/* 57 */         try (InputStream content = out.toInputStream()) {
/* 58 */           (new CMSSignedData(content)).getSignedContent();
/* 59 */           rethrow = new UnsupportedCosignException("Arquivo já se encontra assinado: " + file.getCanonicalPath());
/* 60 */         } catch (CMSException e) {
/* 61 */           rethrow = null;
/*    */         } 
/*    */       } 
/* 64 */       if (rethrow != null)
/* 65 */         throw rethrow; 
/*    */     }
/*    */   };
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/CosignChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */