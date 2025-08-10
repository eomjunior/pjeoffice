/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.anssi.ANSSINamedCurves;
/*     */ import org.bouncycastle.asn1.cryptlib.CryptlibObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
/*     */ import org.bouncycastle.asn1.gm.GMNamedCurves;
/*     */ import org.bouncycastle.asn1.nist.NISTNamedCurves;
/*     */ import org.bouncycastle.asn1.sec.SECNamedCurves;
/*     */ import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
/*     */ import org.bouncycastle.asn1.x9.X962NamedCurves;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.crypto.ec.CustomNamedCurves;
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
/*     */ public class ECNamedCurveTable
/*     */ {
/*     */   public static X9ECParameters getByName(String paramString) {
/*  31 */     X9ECParameters x9ECParameters = X962NamedCurves.getByName(paramString);
/*     */     
/*  33 */     if (x9ECParameters == null)
/*     */     {
/*  35 */       x9ECParameters = SECNamedCurves.getByName(paramString);
/*     */     }
/*     */     
/*  38 */     if (x9ECParameters == null)
/*     */     {
/*  40 */       x9ECParameters = NISTNamedCurves.getByName(paramString);
/*     */     }
/*     */     
/*  43 */     if (x9ECParameters == null)
/*     */     {
/*  45 */       x9ECParameters = TeleTrusTNamedCurves.getByName(paramString);
/*     */     }
/*     */     
/*  48 */     if (x9ECParameters == null)
/*     */     {
/*  50 */       x9ECParameters = ANSSINamedCurves.getByName(paramString);
/*     */     }
/*     */     
/*  53 */     if (x9ECParameters == null)
/*     */     {
/*  55 */       x9ECParameters = ECGOST3410NamedCurves.getByNameX9(paramString);
/*     */     }
/*     */     
/*  58 */     if (x9ECParameters == null)
/*     */     {
/*  60 */       x9ECParameters = GMNamedCurves.getByName(paramString);
/*     */     }
/*     */     
/*  63 */     return x9ECParameters;
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
/*     */   public static ASN1ObjectIdentifier getOID(String paramString) {
/*  75 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = X962NamedCurves.getOID(paramString);
/*     */     
/*  77 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/*  79 */       aSN1ObjectIdentifier = SECNamedCurves.getOID(paramString);
/*     */     }
/*     */     
/*  82 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/*  84 */       aSN1ObjectIdentifier = NISTNamedCurves.getOID(paramString);
/*     */     }
/*     */     
/*  87 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/*  89 */       aSN1ObjectIdentifier = TeleTrusTNamedCurves.getOID(paramString);
/*     */     }
/*     */     
/*  92 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/*  94 */       aSN1ObjectIdentifier = ANSSINamedCurves.getOID(paramString);
/*     */     }
/*     */     
/*  97 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/*  99 */       aSN1ObjectIdentifier = ECGOST3410NamedCurves.getOID(paramString);
/*     */     }
/*     */     
/* 102 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/* 104 */       aSN1ObjectIdentifier = GMNamedCurves.getOID(paramString);
/*     */     }
/*     */     
/* 107 */     if (aSN1ObjectIdentifier == null && paramString.equals("curve25519"))
/*     */     {
/* 109 */       aSN1ObjectIdentifier = CryptlibObjectIdentifiers.curvey25519;
/*     */     }
/*     */     
/* 112 */     return aSN1ObjectIdentifier;
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
/*     */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 125 */     String str = X962NamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     
/* 127 */     if (str == null)
/*     */     {
/* 129 */       str = SECNamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 132 */     if (str == null)
/*     */     {
/* 134 */       str = NISTNamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 137 */     if (str == null)
/*     */     {
/* 139 */       str = TeleTrusTNamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 142 */     if (str == null)
/*     */     {
/* 144 */       str = ANSSINamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 147 */     if (str == null)
/*     */     {
/* 149 */       str = ECGOST3410NamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 152 */     if (str == null)
/*     */     {
/* 154 */       str = GMNamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 157 */     if (str == null)
/*     */     {
/* 159 */       str = CustomNamedCurves.getName(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 162 */     return str;
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
/*     */   public static X9ECParameters getByOID(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 175 */     X9ECParameters x9ECParameters = X962NamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     
/* 177 */     if (x9ECParameters == null)
/*     */     {
/* 179 */       x9ECParameters = SECNamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 184 */     if (x9ECParameters == null)
/*     */     {
/* 186 */       x9ECParameters = TeleTrusTNamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 189 */     if (x9ECParameters == null)
/*     */     {
/* 191 */       x9ECParameters = ANSSINamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 194 */     if (x9ECParameters == null)
/*     */     {
/* 196 */       x9ECParameters = ECGOST3410NamedCurves.getByOIDX9(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 199 */     if (x9ECParameters == null)
/*     */     {
/* 201 */       x9ECParameters = GMNamedCurves.getByOID(paramASN1ObjectIdentifier);
/*     */     }
/*     */     
/* 204 */     return x9ECParameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enumeration getNames() {
/* 214 */     Vector vector = new Vector();
/*     */     
/* 216 */     addEnumeration(vector, X962NamedCurves.getNames());
/* 217 */     addEnumeration(vector, SECNamedCurves.getNames());
/* 218 */     addEnumeration(vector, NISTNamedCurves.getNames());
/* 219 */     addEnumeration(vector, TeleTrusTNamedCurves.getNames());
/* 220 */     addEnumeration(vector, ANSSINamedCurves.getNames());
/* 221 */     addEnumeration(vector, ECGOST3410NamedCurves.getNames());
/* 222 */     addEnumeration(vector, GMNamedCurves.getNames());
/*     */     
/* 224 */     return vector.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addEnumeration(Vector paramVector, Enumeration paramEnumeration) {
/* 231 */     while (paramEnumeration.hasMoreElements())
/*     */     {
/* 233 */       paramVector.addElement(paramEnumeration.nextElement());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/ECNamedCurveTable.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */