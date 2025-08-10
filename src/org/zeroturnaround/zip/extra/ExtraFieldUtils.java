/*     */ package org.zeroturnaround.zip.extra;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.zip.ZipException;
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
/*     */ public class ExtraFieldUtils
/*     */ {
/*     */   private static final int WORD = 4;
/*  48 */   private static final Map<ZipShort, Class<?>> implementations = new ConcurrentHashMap<ZipShort, Class<?>>(); static {
/*  49 */     register(AsiExtraField.class);
/*     */   }
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
/*     */   public static void register(Class<?> c) {
/*     */     try {
/*  65 */       ZipExtraField ze = (ZipExtraField)c.newInstance();
/*  66 */       implementations.put(ze.getHeaderId(), c);
/*     */     }
/*  68 */     catch (ClassCastException cc) {
/*  69 */       throw new RuntimeException(c + " doesn't implement ZipExtraField");
/*     */     }
/*  71 */     catch (InstantiationException ie) {
/*  72 */       throw new RuntimeException(c + " is not a concrete class");
/*     */     }
/*  74 */     catch (IllegalAccessException ie) {
/*  75 */       throw new RuntimeException(c + "'s no-arg constructor is not public");
/*     */     } 
/*     */   }
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
/*     */   public static ZipExtraField createExtraField(ZipShort headerId) throws InstantiationException, IllegalAccessException {
/*  90 */     Class<?> c = implementations.get(headerId);
/*  91 */     if (c != null) {
/*  92 */       return (ZipExtraField)c.newInstance();
/*     */     }
/*  94 */     UnrecognizedExtraField u = new UnrecognizedExtraField();
/*  95 */     u.setHeaderId(headerId);
/*  96 */     return u;
/*     */   }
/*     */   
/*     */   public static ZipExtraField[] parseA(byte[] data) throws ZipException {
/* 100 */     List<ZipExtraField> v = parse(data);
/* 101 */     ZipExtraField[] result = new ZipExtraField[v.size()];
/* 102 */     return v.<ZipExtraField>toArray(result);
/*     */   }
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
/*     */   public static List<ZipExtraField> parse(byte[] data) throws ZipException {
/* 115 */     List<ZipExtraField> v = new ArrayList<ZipExtraField>();
/* 116 */     if (data == null) {
/* 117 */       return v;
/*     */     }
/* 119 */     int start = 0;
/* 120 */     while (start <= data.length - 4) {
/* 121 */       ZipShort headerId = new ZipShort(data, start);
/* 122 */       int length = (new ZipShort(data, start + 2)).getValue();
/* 123 */       if (start + 4 + length > data.length) {
/* 124 */         throw new ZipException("bad extra field starting at " + start + ".  Block length of " + length + " bytes exceeds remaining data of " + (data.length - start - 4) + " bytes.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 132 */         ZipExtraField ze = createExtraField(headerId);
/* 133 */         ze.parseFromLocalFileData(data, start + 4, length);
/* 134 */         v.add(ze);
/*     */       }
/* 136 */       catch (InstantiationException ie) {
/* 137 */         throw new ZipException(ie.getMessage());
/*     */       }
/* 139 */       catch (IllegalAccessException iae) {
/* 140 */         throw new ZipException(iae.getMessage());
/*     */       } 
/* 142 */       start += length + 4;
/*     */     } 
/* 144 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] mergeLocalFileDataData(List<ZipExtraField> data) {
/* 155 */     int regularExtraFieldCount = data.size();
/*     */     
/* 157 */     int sum = 4 * regularExtraFieldCount;
/* 158 */     for (ZipExtraField element : data) {
/* 159 */       sum += element.getLocalFileDataLength().getValue();
/*     */     }
/*     */     
/* 162 */     byte[] result = new byte[sum];
/* 163 */     int start = 0;
/* 164 */     for (ZipExtraField element : data) {
/* 165 */       System.arraycopy(element.getHeaderId().getBytes(), 0, result, start, 2);
/*     */       
/* 167 */       System.arraycopy(element.getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
/*     */       
/* 169 */       byte[] local = element.getLocalFileDataData();
/* 170 */       System.arraycopy(local, 0, result, start + 4, local.length);
/* 171 */       start += local.length + 4;
/*     */     } 
/* 173 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/ExtraFieldUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */