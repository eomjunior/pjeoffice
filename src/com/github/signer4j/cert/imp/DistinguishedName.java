/*    */ package com.github.signer4j.cert.imp;
/*    */ 
/*    */ import com.github.signer4j.cert.IDistinguishedName;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.Strings;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Optional;
/*    */ import java.util.Properties;
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
/*    */ class DistinguishedName
/*    */   implements IDistinguishedName
/*    */ {
/*    */   private String fullName;
/* 43 */   private final Properties properties = new Properties();
/*    */   
/*    */   public DistinguishedName(String fullName) {
/* 46 */     this.fullName = (String)Args.requireNonNull(fullName, "fullName is null");
/* 47 */     setup();
/*    */   }
/*    */   
/*    */   private void setup() {
/* 51 */     try (ByteArrayInputStream s = new ByteArrayInputStream(this.fullName.replaceAll(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", "\n").getBytes())) {
/* 52 */       this.properties.load(s);
/* 53 */     } catch (IOException e) {
/* 54 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getFullName() {
/* 60 */     return this.fullName;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Optional<String> getProperty(String key) {
/* 65 */     return Strings.optional(this.properties.getProperty(key));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return this.fullName;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/DistinguishedName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */