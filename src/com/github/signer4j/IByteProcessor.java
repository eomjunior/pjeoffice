/*    */ package com.github.signer4j;
/*    */ 
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.OpenByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.file.Files;
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
/*    */ public interface IByteProcessor
/*    */ {
/*    */   ISignedData process(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws Signer4JException;
/*    */   
/*    */   default ISignedData process(byte[] content) throws Signer4JException {
/* 45 */     return process(content, 0, content.length);
/*    */   }
/*    */   
/*    */   default ISignedData process(File content) throws Signer4JException, IOException {
/* 49 */     Args.requireNonNull(content, "content is null");
/* 50 */     try (OpenByteArrayOutputStream out = new OpenByteArrayOutputStream(content.length())) {
/* 51 */       Files.copy(content.toPath(), (OutputStream)out);
/* 52 */       return (ISignedData)out.process(this::process);
/*    */     } 
/*    */   }
/*    */   
/*    */   default ISignedData process(String content) throws Signer4JException {
/* 57 */     return process(content, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   default ISignedData process(String content, Charset charset) throws Signer4JException {
/* 61 */     content = Args.requireText(content, "content is null or empty");
/* 62 */     charset = (Charset)Args.requireNonNull(charset, "charset is null");
/* 63 */     return process(content.getBytes(charset));
/*    */   }
/*    */   
/*    */   default byte[] processRaw(byte[] content) throws Signer4JException {
/* 67 */     return process(content).getSignature();
/*    */   }
/*    */   
/*    */   default byte[] processRaw(String content) throws Signer4JException {
/* 71 */     return processRaw(content, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   default byte[] processRaw(String content, Charset charset) throws Signer4JException {
/* 75 */     return process(content, charset).getSignature();
/*    */   }
/*    */   
/*    */   default String process64(byte[] content) throws Signer4JException {
/* 79 */     return process(content).getSignature64();
/*    */   }
/*    */   
/*    */   default String process64(String content) throws Signer4JException {
/* 83 */     return process64(content, IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   default String process64(String content, Charset charset) throws Signer4JException {
/* 87 */     return process(content, charset).getSignature64();
/*    */   }
/*    */   
/*    */   default String process64(File input) throws Signer4JException, IOException {
/* 91 */     return process(input).getSignature64();
/*    */   }
/*    */   
/*    */   default IByteProcessor config(Object param) {
/* 95 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IByteProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */