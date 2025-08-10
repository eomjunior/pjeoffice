/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Counter;
/*     */ import com.itextpdf.text.log.CounterFactory;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfSmartCopy
/*     */   extends PdfCopy
/*     */ {
/*  72 */   private static final Logger LOGGER = LoggerFactory.getLogger(PdfSmartCopy.class);
/*     */ 
/*     */   
/*  75 */   private HashMap<ByteStore, PdfIndirectReference> streamMap = null;
/*  76 */   private final HashMap<RefKey, Integer> serialized = new HashMap<RefKey, Integer>();
/*     */   
/*  78 */   protected Counter COUNTER = CounterFactory.getCounter(PdfSmartCopy.class);
/*     */   protected Counter getCounter() {
/*  80 */     return this.COUNTER;
/*     */   }
/*     */ 
/*     */   
/*     */   public PdfSmartCopy(Document document, OutputStream os) throws DocumentException {
/*  85 */     super(document, os);
/*  86 */     this.streamMap = new HashMap<ByteStore, PdfIndirectReference>();
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
/*     */   protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
/*     */     PdfIndirectReference theRef;
/* 102 */     PdfObject srcObj = PdfReader.getPdfObjectRelease(in);
/* 103 */     ByteStore streamKey = null;
/* 104 */     boolean validStream = false;
/* 105 */     if (srcObj.isStream()) {
/* 106 */       streamKey = new ByteStore((PRStream)srcObj, this.serialized);
/* 107 */       validStream = true;
/* 108 */       PdfIndirectReference streamRef = this.streamMap.get(streamKey);
/* 109 */       if (streamRef != null) {
/* 110 */         return streamRef;
/*     */       }
/*     */     }
/* 113 */     else if (srcObj.isDictionary()) {
/* 114 */       streamKey = new ByteStore((PdfDictionary)srcObj, this.serialized);
/* 115 */       validStream = true;
/* 116 */       PdfIndirectReference streamRef = this.streamMap.get(streamKey);
/* 117 */       if (streamRef != null) {
/* 118 */         return streamRef;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 123 */     RefKey key = new RefKey(in);
/* 124 */     PdfCopy.IndirectReferences iRef = this.indirects.get(key);
/* 125 */     if (iRef != null) {
/* 126 */       theRef = iRef.getRef();
/* 127 */       if (iRef.getCopied()) {
/* 128 */         return theRef;
/*     */       }
/*     */     } else {
/* 131 */       theRef = this.body.getPdfIndirectReference();
/* 132 */       iRef = new PdfCopy.IndirectReferences(theRef);
/* 133 */       this.indirects.put(key, iRef);
/*     */     } 
/* 135 */     if (srcObj.isDictionary()) {
/* 136 */       PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary)srcObj).get(PdfName.TYPE));
/* 137 */       if (type != null) {
/* 138 */         if (PdfName.PAGE.equals(type)) {
/* 139 */           return theRef;
/*     */         }
/* 141 */         if (PdfName.CATALOG.equals(type)) {
/* 142 */           LOGGER.warn(MessageLocalization.getComposedMessage("make.copy.of.catalog.dictionary.is.forbidden", new Object[0]));
/* 143 */           return null;
/*     */         } 
/*     */       } 
/*     */     } 
/* 147 */     iRef.setCopied();
/*     */     
/* 149 */     if (validStream) {
/* 150 */       this.streamMap.put(streamKey, theRef);
/*     */     }
/*     */     
/* 153 */     PdfObject obj = copyObject(srcObj);
/* 154 */     addToBody(obj, theRef);
/* 155 */     return theRef;
/*     */   }
/*     */ 
/*     */   
/*     */   public void freeReader(PdfReader reader) throws IOException {
/* 160 */     this.serialized.clear();
/* 161 */     super.freeReader(reader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
/* 166 */     if (this.currentPdfReaderInstance.getReader() != this.reader)
/* 167 */       this.serialized.clear(); 
/* 168 */     super.addPage(iPage);
/*     */   }
/*     */   
/*     */   static class ByteStore {
/*     */     private final byte[] b;
/*     */     private final int hash;
/*     */     private MessageDigest md5;
/*     */     
/*     */     private void serObject(PdfObject obj, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
/* 177 */       if (level <= 0)
/*     */         return; 
/* 179 */       if (obj == null) {
/* 180 */         bb.append("$Lnull");
/*     */         return;
/*     */       } 
/* 183 */       PdfIndirectReference ref = null;
/* 184 */       ByteBuffer savedBb = null;
/*     */       
/* 186 */       if (obj.isIndirect()) {
/* 187 */         ref = (PdfIndirectReference)obj;
/* 188 */         RefKey key = new RefKey(ref);
/* 189 */         if (serialized.containsKey(key)) {
/* 190 */           bb.append(((Integer)serialized.get(key)).intValue());
/*     */           
/*     */           return;
/*     */         } 
/* 194 */         savedBb = bb;
/* 195 */         bb = new ByteBuffer();
/*     */       } 
/*     */       
/* 198 */       obj = PdfReader.getPdfObject(obj);
/* 199 */       if (obj.isStream()) {
/* 200 */         bb.append("$B");
/* 201 */         serDic((PdfDictionary)obj, level - 1, bb, serialized);
/* 202 */         if (level > 0) {
/* 203 */           this.md5.reset();
/* 204 */           bb.append(this.md5.digest(PdfReader.getStreamBytesRaw((PRStream)obj)));
/*     */         }
/*     */       
/* 207 */       } else if (obj.isDictionary()) {
/* 208 */         serDic((PdfDictionary)obj, level - 1, bb, serialized);
/*     */       }
/* 210 */       else if (obj.isArray()) {
/* 211 */         serArray((PdfArray)obj, level - 1, bb, serialized);
/*     */       }
/* 213 */       else if (obj.isString()) {
/* 214 */         bb.append("$S").append(obj.toString());
/*     */       }
/* 216 */       else if (obj.isName()) {
/* 217 */         bb.append("$N").append(obj.toString());
/*     */       } else {
/*     */         
/* 220 */         bb.append("$L").append(obj.toString());
/*     */       } 
/* 222 */       if (savedBb != null) {
/* 223 */         RefKey key = new RefKey(ref);
/* 224 */         if (!serialized.containsKey(key))
/* 225 */           serialized.put(key, Integer.valueOf(calculateHash(bb.getBuffer()))); 
/* 226 */         savedBb.append(bb);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void serDic(PdfDictionary dic, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
/* 231 */       bb.append("$D");
/* 232 */       if (level <= 0)
/*     */         return; 
/* 234 */       Object[] keys = dic.getKeys().toArray();
/* 235 */       Arrays.sort(keys);
/* 236 */       for (int k = 0; k < keys.length; k++) {
/* 237 */         if (!keys[k].equals(PdfName.P) || (!dic.get((PdfName)keys[k]).isIndirect() && !dic.get((PdfName)keys[k]).isDictionary())) {
/*     */           
/* 239 */           serObject((PdfObject)keys[k], level, bb, serialized);
/* 240 */           serObject(dic.get((PdfName)keys[k]), level, bb, serialized);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void serArray(PdfArray array, int level, ByteBuffer bb, HashMap<RefKey, Integer> serialized) throws IOException {
/* 246 */       bb.append("$A");
/* 247 */       if (level <= 0)
/*     */         return; 
/* 249 */       for (int k = 0; k < array.size(); k++) {
/* 250 */         serObject(array.getPdfObject(k), level, bb, serialized);
/*     */       }
/*     */     }
/*     */     
/*     */     ByteStore(PRStream str, HashMap<RefKey, Integer> serialized) throws IOException {
/*     */       try {
/* 256 */         this.md5 = MessageDigest.getInstance("MD5");
/*     */       }
/* 258 */       catch (Exception e) {
/* 259 */         throw new ExceptionConverter(e);
/*     */       } 
/* 261 */       ByteBuffer bb = new ByteBuffer();
/* 262 */       int level = 100;
/* 263 */       serObject(str, level, bb, serialized);
/* 264 */       this.b = bb.toByteArray();
/* 265 */       this.hash = calculateHash(this.b);
/* 266 */       this.md5 = null;
/*     */     }
/*     */     
/*     */     ByteStore(PdfDictionary dict, HashMap<RefKey, Integer> serialized) throws IOException {
/*     */       try {
/* 271 */         this.md5 = MessageDigest.getInstance("MD5");
/*     */       }
/* 273 */       catch (Exception e) {
/* 274 */         throw new ExceptionConverter(e);
/*     */       } 
/* 276 */       ByteBuffer bb = new ByteBuffer();
/* 277 */       int level = 100;
/* 278 */       serObject(dict, level, bb, serialized);
/* 279 */       this.b = bb.toByteArray();
/* 280 */       this.hash = calculateHash(this.b);
/* 281 */       this.md5 = null;
/*     */     }
/*     */     
/*     */     private static int calculateHash(byte[] b) {
/* 285 */       int hash = 0;
/* 286 */       int len = b.length;
/* 287 */       for (int k = 0; k < len; k++)
/* 288 */         hash = hash * 31 + (b[k] & 0xFF); 
/* 289 */       return hash;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 294 */       if (!(obj instanceof ByteStore))
/* 295 */         return false; 
/* 296 */       if (hashCode() != obj.hashCode())
/* 297 */         return false; 
/* 298 */       return Arrays.equals(this.b, ((ByteStore)obj).b);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 303 */       return this.hash;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfSmartCopy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */