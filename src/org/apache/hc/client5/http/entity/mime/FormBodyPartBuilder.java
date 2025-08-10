/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ public class FormBodyPartBuilder
/*     */ {
/*     */   private String name;
/*     */   private ContentBody body;
/*     */   private final Header header;
/*     */   
/*     */   public static FormBodyPartBuilder create(String name, ContentBody body) {
/*  51 */     return new FormBodyPartBuilder(name, body);
/*     */   }
/*     */   
/*     */   public static FormBodyPartBuilder create() {
/*  55 */     return new FormBodyPartBuilder();
/*     */   }
/*     */   
/*     */   FormBodyPartBuilder(String name, ContentBody body) {
/*  59 */     this();
/*  60 */     this.name = name;
/*  61 */     this.body = body;
/*     */   }
/*     */   
/*     */   FormBodyPartBuilder() {
/*  65 */     this.header = new Header();
/*     */   }
/*     */   
/*     */   public FormBodyPartBuilder setName(String name) {
/*  69 */     this.name = name;
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public FormBodyPartBuilder setBody(ContentBody body) {
/*  74 */     this.body = body;
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FormBodyPartBuilder addField(String name, String value, List<NameValuePair> parameters) {
/*  82 */     Args.notNull(name, "Field name");
/*  83 */     this.header.addField(new MimeField(name, value, parameters));
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public FormBodyPartBuilder addField(String name, String value) {
/*  88 */     Args.notNull(name, "Field name");
/*  89 */     this.header.addField(new MimeField(name, value));
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public FormBodyPartBuilder setField(String name, String value) {
/*  94 */     Args.notNull(name, "Field name");
/*  95 */     this.header.setField(new MimeField(name, value));
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public FormBodyPartBuilder removeFields(String name) {
/* 100 */     Args.notNull(name, "Field name");
/* 101 */     this.header.removeFields(name);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public FormBodyPart build() {
/* 106 */     Asserts.notBlank(this.name, "Name");
/* 107 */     Asserts.notNull(this.body, "Content body");
/* 108 */     Header headerCopy = new Header();
/* 109 */     List<MimeField> fields = this.header.getFields();
/* 110 */     for (MimeField field : fields) {
/* 111 */       headerCopy.addField(field);
/*     */     }
/* 113 */     if (headerCopy.getField("Content-Disposition") == null) {
/* 114 */       List<NameValuePair> fieldParameters = new ArrayList<>();
/* 115 */       fieldParameters.add(new BasicNameValuePair("name", this.name));
/* 116 */       if (this.body.getFilename() != null) {
/* 117 */         fieldParameters.add(new BasicNameValuePair("filename", this.body.getFilename()));
/*     */       }
/* 119 */       headerCopy.addField(new MimeField("Content-Disposition", "form-data", fieldParameters));
/*     */     } 
/* 121 */     if (headerCopy.getField("Content-Type") == null) {
/*     */       ContentType contentType;
/* 123 */       if (this.body instanceof AbstractContentBody) {
/* 124 */         contentType = ((AbstractContentBody)this.body).getContentType();
/*     */       } else {
/* 126 */         contentType = null;
/*     */       } 
/* 128 */       if (contentType != null) {
/* 129 */         headerCopy.addField(new MimeField("Content-Type", contentType.toString()));
/*     */       } else {
/* 131 */         StringBuilder buffer = new StringBuilder();
/* 132 */         buffer.append(this.body.getMimeType());
/* 133 */         if (this.body.getCharset() != null) {
/* 134 */           buffer.append("; charset=");
/* 135 */           buffer.append(this.body.getCharset());
/*     */         } 
/* 137 */         headerCopy.addField(new MimeField("Content-Type", buffer.toString()));
/*     */       } 
/*     */     } 
/* 140 */     return new FormBodyPart(this.name, this.body, headerCopy);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/FormBodyPartBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */