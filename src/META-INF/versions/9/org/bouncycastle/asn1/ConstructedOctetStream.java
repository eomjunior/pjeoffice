/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetStringParser;
/*     */ import org.bouncycastle.asn1.ASN1StreamParser;
/*     */ 
/*     */ class ConstructedOctetStream
/*     */   extends InputStream
/*     */ {
/*     */   private final ASN1StreamParser _parser;
/*     */   private boolean _first = true;
/*     */   private InputStream _currentStream;
/*     */   
/*     */   ConstructedOctetStream(ASN1StreamParser paramASN1StreamParser) {
/*  17 */     this._parser = paramASN1StreamParser;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  22 */     if (this._currentStream == null) {
/*     */       
/*  24 */       if (!this._first)
/*     */       {
/*  26 */         return -1;
/*     */       }
/*     */       
/*  29 */       ASN1OctetStringParser aSN1OctetStringParser = getNextParser();
/*  30 */       if (aSN1OctetStringParser == null)
/*     */       {
/*  32 */         return -1;
/*     */       }
/*     */       
/*  35 */       this._first = false;
/*  36 */       this._currentStream = aSN1OctetStringParser.getOctetStream();
/*     */     } 
/*     */     
/*  39 */     int i = 0;
/*     */ 
/*     */     
/*     */     while (true) {
/*  43 */       int j = this._currentStream.read(paramArrayOfbyte, paramInt1 + i, paramInt2 - i);
/*     */       
/*  45 */       if (j >= 0) {
/*     */         
/*  47 */         i += j;
/*     */         
/*  49 */         if (i == paramInt2)
/*     */         {
/*  51 */           return i;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*  56 */       ASN1OctetStringParser aSN1OctetStringParser = getNextParser();
/*  57 */       if (aSN1OctetStringParser == null) {
/*     */         
/*  59 */         this._currentStream = null;
/*  60 */         return (i < 1) ? -1 : i;
/*     */       } 
/*     */       
/*  63 */       this._currentStream = aSN1OctetStringParser.getOctetStream();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  71 */     if (this._currentStream == null) {
/*     */       
/*  73 */       if (!this._first)
/*     */       {
/*  75 */         return -1;
/*     */       }
/*     */       
/*  78 */       ASN1OctetStringParser aSN1OctetStringParser = getNextParser();
/*  79 */       if (aSN1OctetStringParser == null)
/*     */       {
/*  81 */         return -1;
/*     */       }
/*     */       
/*  84 */       this._first = false;
/*  85 */       this._currentStream = aSN1OctetStringParser.getOctetStream();
/*     */     } 
/*     */ 
/*     */     
/*     */     while (true) {
/*  90 */       int i = this._currentStream.read();
/*     */       
/*  92 */       if (i >= 0)
/*     */       {
/*  94 */         return i;
/*     */       }
/*     */       
/*  97 */       ASN1OctetStringParser aSN1OctetStringParser = getNextParser();
/*  98 */       if (aSN1OctetStringParser == null) {
/*     */         
/* 100 */         this._currentStream = null;
/* 101 */         return -1;
/*     */       } 
/*     */       
/* 104 */       this._currentStream = aSN1OctetStringParser.getOctetStream();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ASN1OctetStringParser getNextParser() throws IOException {
/* 110 */     ASN1Encodable aSN1Encodable = this._parser.readObject();
/* 111 */     if (aSN1Encodable == null)
/*     */     {
/* 113 */       return null;
/*     */     }
/*     */     
/* 116 */     if (aSN1Encodable instanceof ASN1OctetStringParser)
/*     */     {
/* 118 */       return (ASN1OctetStringParser)aSN1Encodable;
/*     */     }
/*     */     
/* 121 */     throw new IOException("unknown object encountered: " + aSN1Encodable.getClass());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ConstructedOctetStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */