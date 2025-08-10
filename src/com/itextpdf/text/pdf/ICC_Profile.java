/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
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
/*     */ public class ICC_Profile
/*     */ {
/*     */   protected byte[] data;
/*     */   protected int numComponents;
/*  57 */   private static HashMap<String, Integer> cstags = new HashMap<String, Integer>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ICC_Profile getInstance(byte[] data, int numComponents) {
/*  63 */     if (data.length < 128 || data[36] != 97 || data[37] != 99 || data[38] != 115 || data[39] != 112)
/*     */     {
/*  65 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0])); } 
/*     */     try {
/*  67 */       ICC_Profile icc = new ICC_Profile();
/*  68 */       icc.data = data;
/*     */       
/*  70 */       Integer cs = cstags.get(new String(data, 16, 4, "US-ASCII"));
/*  71 */       int nc = (cs == null) ? 0 : cs.intValue();
/*  72 */       icc.numComponents = nc;
/*     */       
/*  74 */       if (nc != numComponents) {
/*  75 */         throw new IllegalArgumentException("ICC profile contains " + nc + " component(s), the image data contains " + numComponents + " component(s)");
/*     */       }
/*  77 */       return icc;
/*  78 */     } catch (UnsupportedEncodingException e) {
/*  79 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static ICC_Profile getInstance(byte[] data) {
/*     */     try {
/*  86 */       Integer cs = cstags.get(new String(data, 16, 4, "US-ASCII"));
/*  87 */       int numComponents = (cs == null) ? 0 : cs.intValue();
/*  88 */       return getInstance(data, numComponents);
/*  89 */     } catch (UnsupportedEncodingException e) {
/*  90 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ICC_Profile getInstance(InputStream file) {
/*     */     try {
/*  96 */       byte[] head = new byte[128];
/*  97 */       int remain = head.length;
/*  98 */       int ptr = 0;
/*  99 */       while (remain > 0) {
/* 100 */         int n = file.read(head, ptr, remain);
/* 101 */         if (n < 0)
/* 102 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0])); 
/* 103 */         remain -= n;
/* 104 */         ptr += n;
/*     */       } 
/* 106 */       if (head[36] != 97 || head[37] != 99 || head[38] != 115 || head[39] != 112)
/*     */       {
/* 108 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0])); } 
/* 109 */       remain = (head[0] & 0xFF) << 24 | (head[1] & 0xFF) << 16 | (head[2] & 0xFF) << 8 | head[3] & 0xFF;
/*     */       
/* 111 */       byte[] icc = new byte[remain];
/* 112 */       System.arraycopy(head, 0, icc, 0, head.length);
/* 113 */       remain -= head.length;
/* 114 */       ptr = head.length;
/* 115 */       while (remain > 0) {
/* 116 */         int n = file.read(icc, ptr, remain);
/* 117 */         if (n < 0)
/* 118 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile", new Object[0])); 
/* 119 */         remain -= n;
/* 120 */         ptr += n;
/*     */       } 
/* 122 */       return getInstance(icc);
/*     */     }
/* 124 */     catch (Exception ex) {
/* 125 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ICC_Profile GetInstance(String fname) {
/* 130 */     FileInputStream fs = null;
/*     */     try {
/* 132 */       fs = new FileInputStream(fname);
/* 133 */       ICC_Profile icc = getInstance(fs);
/* 134 */       return icc;
/*     */     }
/* 136 */     catch (Exception ex) {
/* 137 */       throw new ExceptionConverter(ex);
/*     */     } finally {
/*     */       
/* 140 */       try { fs.close(); } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/* 145 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getNumComponents() {
/* 149 */     return this.numComponents;
/*     */   }
/*     */   
/*     */   static {
/* 153 */     cstags.put("XYZ ", Integer.valueOf(3));
/* 154 */     cstags.put("Lab ", Integer.valueOf(3));
/* 155 */     cstags.put("Luv ", Integer.valueOf(3));
/* 156 */     cstags.put("YCbr", Integer.valueOf(3));
/* 157 */     cstags.put("Yxy ", Integer.valueOf(3));
/* 158 */     cstags.put("RGB ", Integer.valueOf(3));
/* 159 */     cstags.put("GRAY", Integer.valueOf(1));
/* 160 */     cstags.put("HSV ", Integer.valueOf(3));
/* 161 */     cstags.put("HLS ", Integer.valueOf(3));
/* 162 */     cstags.put("CMYK", Integer.valueOf(4));
/* 163 */     cstags.put("CMY ", Integer.valueOf(3));
/* 164 */     cstags.put("2CLR", Integer.valueOf(2));
/* 165 */     cstags.put("3CLR", Integer.valueOf(3));
/* 166 */     cstags.put("4CLR", Integer.valueOf(4));
/* 167 */     cstags.put("5CLR", Integer.valueOf(5));
/* 168 */     cstags.put("6CLR", Integer.valueOf(6));
/* 169 */     cstags.put("7CLR", Integer.valueOf(7));
/* 170 */     cstags.put("8CLR", Integer.valueOf(8));
/* 171 */     cstags.put("9CLR", Integer.valueOf(9));
/* 172 */     cstags.put("ACLR", Integer.valueOf(10));
/* 173 */     cstags.put("BCLR", Integer.valueOf(11));
/* 174 */     cstags.put("CCLR", Integer.valueOf(12));
/* 175 */     cstags.put("DCLR", Integer.valueOf(13));
/* 176 */     cstags.put("ECLR", Integer.valueOf(14));
/* 177 */     cstags.put("FCLR", Integer.valueOf(15));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ICC_Profile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */