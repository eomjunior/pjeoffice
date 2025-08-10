/*     */ package org.apache.tools.ant.types.spi;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Service
/*     */   extends ProjectComponent
/*     */ {
/*  38 */   private List<Provider> providerList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private String type;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(String className) {
/*  46 */     Provider provider = new Provider();
/*  47 */     provider.setClassName(className);
/*  48 */     this.providerList.add(provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredProvider(Provider provider) {
/*  56 */     provider.check();
/*  57 */     this.providerList.add(provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  64 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/*  74 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getAsStream() throws IOException {
/*  85 */     return new ByteArrayInputStream(((String)this.providerList
/*  86 */         .stream().map(Provider::getClassName)
/*  87 */         .collect(Collectors.joining("\n"))).getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check() {
/*  95 */     if (this.type == null) {
/*  96 */       throw new BuildException("type attribute must be set for service element", 
/*     */           
/*  98 */           getLocation());
/*     */     }
/* 100 */     if (this.type.isEmpty()) {
/* 101 */       throw new BuildException("Invalid empty type classname", 
/* 102 */           getLocation());
/*     */     }
/* 104 */     if (this.providerList.isEmpty())
/* 105 */       throw new BuildException("provider attribute or nested provider element must be set!", 
/*     */           
/* 107 */           getLocation()); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/spi/Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */