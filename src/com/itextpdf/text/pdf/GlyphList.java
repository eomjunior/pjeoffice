/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.pdf.fonts.FontsResourceAnchor;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class GlyphList
/*     */ {
/*  55 */   private static HashMap<Integer, String> unicode2names = new HashMap<Integer, String>();
/*  56 */   private static HashMap<String, int[]> names2unicode = (HashMap)new HashMap<String, int>();
/*     */   
/*     */   static {
/*  59 */     InputStream is = null;
/*     */     try {
/*  61 */       is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/glyphlist.txt", (new FontsResourceAnchor()).getClass().getClassLoader());
/*  62 */       if (is == null) {
/*  63 */         String msg = "glyphlist.txt not found as resource. (It must exist as resource in the package com.itextpdf.text.pdf.fonts)";
/*  64 */         throw new Exception(msg);
/*     */       } 
/*  66 */       byte[] buf = new byte[1024];
/*  67 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */       while (true) {
/*  69 */         int size = is.read(buf);
/*  70 */         if (size < 0)
/*     */           break; 
/*  72 */         out.write(buf, 0, size);
/*     */       } 
/*  74 */       is.close();
/*  75 */       is = null;
/*  76 */       String s = PdfEncodings.convertToString(out.toByteArray(), null);
/*  77 */       StringTokenizer tk = new StringTokenizer(s, "\r\n");
/*  78 */       while (tk.hasMoreTokens()) {
/*  79 */         String line = tk.nextToken();
/*  80 */         if (line.startsWith("#"))
/*     */           continue; 
/*  82 */         StringTokenizer t2 = new StringTokenizer(line, " ;\r\n\t\f");
/*  83 */         String name = null;
/*  84 */         String hex = null;
/*  85 */         if (!t2.hasMoreTokens())
/*     */           continue; 
/*  87 */         name = t2.nextToken();
/*  88 */         if (!t2.hasMoreTokens())
/*     */           continue; 
/*  90 */         hex = t2.nextToken();
/*  91 */         Integer num = Integer.valueOf(hex, 16);
/*  92 */         unicode2names.put(num, name);
/*  93 */         names2unicode.put(name, new int[] { num.intValue() });
/*     */       }
/*     */     
/*  96 */     } catch (Exception e) {
/*  97 */       System.err.println("glyphlist.txt loading error: " + e.getMessage());
/*     */     } finally {
/*     */       
/* 100 */       if (is != null) {
/*     */         try {
/* 102 */           is.close();
/*     */         }
/* 104 */         catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] nameToUnicode(String name) {
/* 112 */     int[] v = names2unicode.get(name);
/* 113 */     if (v == null && name.length() == 7 && name.toLowerCase().startsWith("uni")) {
/*     */       try {
/* 115 */         return new int[] { Integer.parseInt(name.substring(3), 16) };
/*     */       }
/* 117 */       catch (Exception exception) {}
/*     */     }
/*     */     
/* 120 */     return v;
/*     */   }
/*     */   
/*     */   public static String unicodeToName(int num) {
/* 124 */     return unicode2names.get(Integer.valueOf(num));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/GlyphList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */