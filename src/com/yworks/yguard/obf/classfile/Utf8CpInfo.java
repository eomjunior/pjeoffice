/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
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
/*     */ public class Utf8CpInfo
/*     */   extends CpInfo
/*     */ {
/*     */   private int u2length;
/*     */   private byte[] bytes;
/*     */   private String utf8string;
/*     */   
/*     */   protected Utf8CpInfo() {
/*  38 */     super(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Utf8CpInfo(String s) {
/*  48 */     super(1);
/*  49 */     setString(s);
/*  50 */     this.refCount = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decRefCount() {
/*  57 */     super.decRefCount();
/*  58 */     if (this.refCount == 0)
/*     */     {
/*  60 */       clearString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/*  71 */     if (this.utf8string == null) {
/*     */       
/*     */       try {
/*  74 */         this.utf8string = new String(this.bytes, "UTF8");
/*  75 */       } catch (UnsupportedEncodingException uee) {
/*  76 */         throw new RuntimeException("Could not decode UTF8");
/*     */       } 
/*     */     }
/*  79 */     return this.utf8string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setString(String str) {
/*  89 */     this.utf8string = str;
/*     */     try {
/*  91 */       this.bytes = str.getBytes("UTF8");
/*  92 */     } catch (UnsupportedEncodingException uee) {
/*  93 */       throw new RuntimeException("Could not encode UTF8");
/*     */     } 
/*  95 */     this.u2length = this.bytes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearString() {
/* 103 */     this.u2length = 0;
/* 104 */     this.bytes = new byte[0];
/* 105 */     this.utf8string = null;
/* 106 */     getString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/* 112 */     this.u2length = din.readUnsignedShort();
/* 113 */     this.bytes = new byte[this.u2length];
/* 114 */     din.readFully(this.bytes);
/* 115 */     getString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInfo(DataOutput dout) throws IOException {
/* 121 */     dout.writeShort(this.u2length);
/* 122 */     if (this.bytes.length > 0)
/* 123 */       dout.write(this.bytes); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/Utf8CpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */