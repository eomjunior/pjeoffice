/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class ExtraFieldUtils
/*     */ {
/*     */   private static final int WORD = 4;
/*  45 */   private static final Map<ZipShort, Class<?>> implementations = new ConcurrentHashMap<>(); static {
/*  46 */     register(AsiExtraField.class);
/*  47 */     register(JarMarker.class);
/*  48 */     register(UnicodePathExtraField.class);
/*  49 */     register(UnicodeCommentExtraField.class);
/*  50 */     register(Zip64ExtendedInformationExtraField.class);
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
/*     */   public static void register(Class<?> c) {
/*     */     try {
/*  64 */       ZipExtraField ze = c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  65 */       implementations.put(ze.getHeaderId(), c);
/*  66 */     } catch (ClassCastException cc) {
/*  67 */       throw new RuntimeException(c + " doesn't implement ZipExtraField");
/*  68 */     } catch (InstantiationException ie) {
/*  69 */       throw new RuntimeException(c + " is not a concrete class");
/*  70 */     } catch (IllegalAccessException ie) {
/*  71 */       throw new RuntimeException(c + "'s no-arg constructor is not public");
/*  72 */     } catch (NoSuchMethodException e) {
/*  73 */       throw new RuntimeException(c + "'s no-arg constructor not found");
/*  74 */     } catch (InvocationTargetException e) {
/*  75 */       throw new RuntimeException(c + "'s no-arg constructor threw an exception:" + e
/*  76 */           .getMessage());
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
/*  91 */     Class<?> c = implementations.get(headerId);
/*  92 */     if (c != null) {
/*     */       
/*     */       try {
/*  95 */         return c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  96 */       } catch (InvocationTargetException e) {
/*  97 */         throw (InstantiationException)(new InstantiationException())
/*  98 */           .initCause(e.getTargetException());
/*  99 */       } catch (NoSuchMethodException e) {
/* 100 */         throw (InstantiationException)(new InstantiationException())
/* 101 */           .initCause(e);
/*     */       } 
/*     */     }
/* 104 */     UnrecognizedExtraField u = new UnrecognizedExtraField();
/* 105 */     u.setHeaderId(headerId);
/* 106 */     return u;
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
/*     */   public static ZipExtraField[] parse(byte[] data) throws ZipException {
/* 118 */     return parse(data, true, UnparseableExtraField.THROW);
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
/*     */   public static ZipExtraField[] parse(byte[] data, boolean local) throws ZipException {
/* 133 */     return parse(data, local, UnparseableExtraField.THROW);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZipExtraField[] parse(byte[] data, boolean local, UnparseableExtraField onUnparseableData) throws ZipException {
/* 151 */     List<ZipExtraField> v = new ArrayList<>();
/* 152 */     int start = 0;
/*     */     
/* 154 */     while (start <= data.length - 4) {
/* 155 */       ZipShort headerId = new ZipShort(data, start);
/* 156 */       int length = (new ZipShort(data, start + 2)).getValue();
/* 157 */       if (start + 4 + length > data.length) {
/* 158 */         UnparseableExtraFieldData field; switch (onUnparseableData.getKey()) {
/*     */           case 0:
/* 160 */             throw new ZipException("bad extra field starting at " + start + ".  Block length of " + length + " bytes exceeds remaining data of " + (data.length - start - 4) + " bytes.");
/*     */ 
/*     */ 
/*     */           
/*     */           case 2:
/* 165 */             field = new UnparseableExtraFieldData();
/* 166 */             if (local) {
/* 167 */               field.parseFromLocalFileData(data, start, data.length - start);
/*     */             } else {
/* 169 */               field.parseFromCentralDirectoryData(data, start, data.length - start);
/*     */             } 
/* 171 */             v.add(field);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 1:
/*     */             break;
/*     */         } 
/*     */         
/* 179 */         throw new ZipException("unknown UnparseableExtraField key: " + onUnparseableData
/* 180 */             .getKey());
/*     */       } 
/*     */       
/*     */       try {
/* 184 */         ZipExtraField ze = createExtraField(headerId);
/* 185 */         if (local || !(ze instanceof CentralDirectoryParsingZipExtraField)) {
/* 186 */           ze.parseFromLocalFileData(data, start + 4, length);
/*     */         } else {
/* 188 */           ((CentralDirectoryParsingZipExtraField)ze)
/* 189 */             .parseFromCentralDirectoryData(data, start + 4, length);
/*     */         } 
/* 191 */         v.add(ze);
/* 192 */       } catch (InstantiationException|IllegalAccessException ie) {
/* 193 */         throw new ZipException(ie.getMessage());
/*     */       } 
/* 195 */       start += length + 4;
/*     */     } 
/*     */     
/* 198 */     ZipExtraField[] result = new ZipExtraField[v.size()];
/* 199 */     return v.<ZipExtraField>toArray(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] mergeLocalFileDataData(ZipExtraField[] data) {
/* 209 */     boolean lastIsUnparseableHolder = (data.length > 0 && data[data.length - 1] instanceof UnparseableExtraFieldData);
/*     */     
/* 211 */     int regularExtraFieldCount = lastIsUnparseableHolder ? (data.length - 1) : data.length;
/*     */     
/* 213 */     int sum = 4 * regularExtraFieldCount;
/* 214 */     for (ZipExtraField element : data) {
/* 215 */       sum += element.getLocalFileDataLength().getValue();
/*     */     }
/*     */     
/* 218 */     byte[] result = new byte[sum];
/* 219 */     int start = 0;
/* 220 */     for (int i = 0; i < regularExtraFieldCount; i++) {
/* 221 */       System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
/*     */       
/* 223 */       System.arraycopy(data[i].getLocalFileDataLength().getBytes(), 0, result, start + 2, 2);
/*     */       
/* 225 */       byte[] local = data[i].getLocalFileDataData();
/* 226 */       System.arraycopy(local, 0, result, start + 4, local.length);
/* 227 */       start += local.length + 4;
/*     */     } 
/* 229 */     if (lastIsUnparseableHolder) {
/* 230 */       byte[] local = data[data.length - 1].getLocalFileDataData();
/* 231 */       System.arraycopy(local, 0, result, start, local.length);
/*     */     } 
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] mergeCentralDirectoryData(ZipExtraField[] data) {
/* 243 */     boolean lastIsUnparseableHolder = (data.length > 0 && data[data.length - 1] instanceof UnparseableExtraFieldData);
/*     */     
/* 245 */     int regularExtraFieldCount = lastIsUnparseableHolder ? (data.length - 1) : data.length;
/*     */     
/* 247 */     int sum = 4 * regularExtraFieldCount;
/* 248 */     for (ZipExtraField element : data) {
/* 249 */       sum += element.getCentralDirectoryLength().getValue();
/*     */     }
/* 251 */     byte[] result = new byte[sum];
/* 252 */     int start = 0;
/* 253 */     for (int i = 0; i < regularExtraFieldCount; i++) {
/* 254 */       System.arraycopy(data[i].getHeaderId().getBytes(), 0, result, start, 2);
/*     */       
/* 256 */       System.arraycopy(data[i].getCentralDirectoryLength().getBytes(), 0, result, start + 2, 2);
/*     */       
/* 258 */       byte[] local = data[i].getCentralDirectoryData();
/* 259 */       System.arraycopy(local, 0, result, start + 4, local.length);
/* 260 */       start += local.length + 4;
/*     */     } 
/* 262 */     if (lastIsUnparseableHolder) {
/* 263 */       byte[] local = data[data.length - 1].getCentralDirectoryData();
/* 264 */       System.arraycopy(local, 0, result, start, local.length);
/*     */     } 
/* 266 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class UnparseableExtraField
/*     */   {
/*     */     public static final int THROW_KEY = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int SKIP_KEY = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int READ_KEY = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     public static final UnparseableExtraField THROW = new UnparseableExtraField(0);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     public static final UnparseableExtraField SKIP = new UnparseableExtraField(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     public static final UnparseableExtraField READ = new UnparseableExtraField(2);
/*     */     
/*     */     private final int key;
/*     */     
/*     */     private UnparseableExtraField(int k) {
/* 307 */       this.key = k;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getKey() {
/* 316 */       return this.key;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ExtraFieldUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */