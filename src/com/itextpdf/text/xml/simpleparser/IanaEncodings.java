/*     */ package com.itextpdf.text.xml.simpleparser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class IanaEncodings
/*     */ {
/*  76 */   private static final Map<String, String> MAP = new HashMap<String, String>();
/*     */ 
/*     */   
/*     */   static {
/*  80 */     MAP.put("BIG5", "Big5");
/*  81 */     MAP.put("CSBIG5", "Big5");
/*  82 */     MAP.put("CP037", "CP037");
/*  83 */     MAP.put("IBM037", "CP037");
/*  84 */     MAP.put("CSIBM037", "CP037");
/*  85 */     MAP.put("EBCDIC-CP-US", "CP037");
/*  86 */     MAP.put("EBCDIC-CP-CA", "CP037");
/*  87 */     MAP.put("EBCDIC-CP-NL", "CP037");
/*  88 */     MAP.put("EBCDIC-CP-WT", "CP037");
/*  89 */     MAP.put("IBM277", "CP277");
/*  90 */     MAP.put("CP277", "CP277");
/*  91 */     MAP.put("CSIBM277", "CP277");
/*  92 */     MAP.put("EBCDIC-CP-DK", "CP277");
/*  93 */     MAP.put("EBCDIC-CP-NO", "CP277");
/*  94 */     MAP.put("IBM278", "CP278");
/*  95 */     MAP.put("CP278", "CP278");
/*  96 */     MAP.put("CSIBM278", "CP278");
/*  97 */     MAP.put("EBCDIC-CP-FI", "CP278");
/*  98 */     MAP.put("EBCDIC-CP-SE", "CP278");
/*  99 */     MAP.put("IBM280", "CP280");
/* 100 */     MAP.put("CP280", "CP280");
/* 101 */     MAP.put("CSIBM280", "CP280");
/* 102 */     MAP.put("EBCDIC-CP-IT", "CP280");
/* 103 */     MAP.put("IBM284", "CP284");
/* 104 */     MAP.put("CP284", "CP284");
/* 105 */     MAP.put("CSIBM284", "CP284");
/* 106 */     MAP.put("EBCDIC-CP-ES", "CP284");
/* 107 */     MAP.put("EBCDIC-CP-GB", "CP285");
/* 108 */     MAP.put("IBM285", "CP285");
/* 109 */     MAP.put("CP285", "CP285");
/* 110 */     MAP.put("CSIBM285", "CP285");
/* 111 */     MAP.put("EBCDIC-CP-FR", "CP297");
/* 112 */     MAP.put("IBM297", "CP297");
/* 113 */     MAP.put("CP297", "CP297");
/* 114 */     MAP.put("CSIBM297", "CP297");
/* 115 */     MAP.put("EBCDIC-CP-AR1", "CP420");
/* 116 */     MAP.put("IBM420", "CP420");
/* 117 */     MAP.put("CP420", "CP420");
/* 118 */     MAP.put("CSIBM420", "CP420");
/* 119 */     MAP.put("EBCDIC-CP-HE", "CP424");
/* 120 */     MAP.put("IBM424", "CP424");
/* 121 */     MAP.put("CP424", "CP424");
/* 122 */     MAP.put("CSIBM424", "CP424");
/* 123 */     MAP.put("EBCDIC-CP-CH", "CP500");
/* 124 */     MAP.put("IBM500", "CP500");
/* 125 */     MAP.put("CP500", "CP500");
/* 126 */     MAP.put("CSIBM500", "CP500");
/* 127 */     MAP.put("EBCDIC-CP-CH", "CP500");
/* 128 */     MAP.put("EBCDIC-CP-BE", "CP500");
/* 129 */     MAP.put("IBM868", "CP868");
/* 130 */     MAP.put("CP868", "CP868");
/* 131 */     MAP.put("CSIBM868", "CP868");
/* 132 */     MAP.put("CP-AR", "CP868");
/* 133 */     MAP.put("IBM869", "CP869");
/* 134 */     MAP.put("CP869", "CP869");
/* 135 */     MAP.put("CSIBM869", "CP869");
/* 136 */     MAP.put("CP-GR", "CP869");
/* 137 */     MAP.put("IBM870", "CP870");
/* 138 */     MAP.put("CP870", "CP870");
/* 139 */     MAP.put("CSIBM870", "CP870");
/* 140 */     MAP.put("EBCDIC-CP-ROECE", "CP870");
/* 141 */     MAP.put("EBCDIC-CP-YU", "CP870");
/* 142 */     MAP.put("IBM871", "CP871");
/* 143 */     MAP.put("CP871", "CP871");
/* 144 */     MAP.put("CSIBM871", "CP871");
/* 145 */     MAP.put("EBCDIC-CP-IS", "CP871");
/* 146 */     MAP.put("IBM918", "CP918");
/* 147 */     MAP.put("CP918", "CP918");
/* 148 */     MAP.put("CSIBM918", "CP918");
/* 149 */     MAP.put("EBCDIC-CP-AR2", "CP918");
/* 150 */     MAP.put("EUC-JP", "EUCJIS");
/* 151 */     MAP.put("CSEUCPkdFmtJapanese", "EUCJIS");
/* 152 */     MAP.put("EUC-KR", "KSC5601");
/* 153 */     MAP.put("GB2312", "GB2312");
/* 154 */     MAP.put("CSGB2312", "GB2312");
/* 155 */     MAP.put("ISO-2022-JP", "JIS");
/* 156 */     MAP.put("CSISO2022JP", "JIS");
/* 157 */     MAP.put("ISO-2022-KR", "ISO2022KR");
/* 158 */     MAP.put("CSISO2022KR", "ISO2022KR");
/* 159 */     MAP.put("ISO-2022-CN", "ISO2022CN");
/*     */     
/* 161 */     MAP.put("X0201", "JIS0201");
/* 162 */     MAP.put("CSISO13JISC6220JP", "JIS0201");
/* 163 */     MAP.put("X0208", "JIS0208");
/* 164 */     MAP.put("ISO-IR-87", "JIS0208");
/* 165 */     MAP.put("X0208dbiJIS_X0208-1983", "JIS0208");
/* 166 */     MAP.put("CSISO87JISX0208", "JIS0208");
/* 167 */     MAP.put("X0212", "JIS0212");
/* 168 */     MAP.put("ISO-IR-159", "JIS0212");
/* 169 */     MAP.put("CSISO159JISX02121990", "JIS0212");
/* 170 */     MAP.put("SHIFT_JIS", "SJIS");
/* 171 */     MAP.put("CSSHIFT_JIS", "SJIS");
/* 172 */     MAP.put("MS_Kanji", "SJIS");
/*     */ 
/*     */     
/* 175 */     MAP.put("WINDOWS-1250", "Cp1250");
/* 176 */     MAP.put("WINDOWS-1251", "Cp1251");
/* 177 */     MAP.put("WINDOWS-1252", "Cp1252");
/* 178 */     MAP.put("WINDOWS-1253", "Cp1253");
/* 179 */     MAP.put("WINDOWS-1254", "Cp1254");
/* 180 */     MAP.put("WINDOWS-1255", "Cp1255");
/* 181 */     MAP.put("WINDOWS-1256", "Cp1256");
/* 182 */     MAP.put("WINDOWS-1257", "Cp1257");
/* 183 */     MAP.put("WINDOWS-1258", "Cp1258");
/* 184 */     MAP.put("TIS-620", "TIS620");
/*     */     
/* 186 */     MAP.put("ISO-8859-1", "ISO8859_1");
/* 187 */     MAP.put("ISO-IR-100", "ISO8859_1");
/* 188 */     MAP.put("ISO_8859-1", "ISO8859_1");
/* 189 */     MAP.put("LATIN1", "ISO8859_1");
/* 190 */     MAP.put("CSISOLATIN1", "ISO8859_1");
/* 191 */     MAP.put("L1", "ISO8859_1");
/* 192 */     MAP.put("IBM819", "ISO8859_1");
/* 193 */     MAP.put("CP819", "ISO8859_1");
/*     */     
/* 195 */     MAP.put("ISO-8859-2", "ISO8859_2");
/* 196 */     MAP.put("ISO-IR-101", "ISO8859_2");
/* 197 */     MAP.put("ISO_8859-2", "ISO8859_2");
/* 198 */     MAP.put("LATIN2", "ISO8859_2");
/* 199 */     MAP.put("CSISOLATIN2", "ISO8859_2");
/* 200 */     MAP.put("L2", "ISO8859_2");
/*     */     
/* 202 */     MAP.put("ISO-8859-3", "ISO8859_3");
/* 203 */     MAP.put("ISO-IR-109", "ISO8859_3");
/* 204 */     MAP.put("ISO_8859-3", "ISO8859_3");
/* 205 */     MAP.put("LATIN3", "ISO8859_3");
/* 206 */     MAP.put("CSISOLATIN3", "ISO8859_3");
/* 207 */     MAP.put("L3", "ISO8859_3");
/*     */     
/* 209 */     MAP.put("ISO-8859-4", "ISO8859_4");
/* 210 */     MAP.put("ISO-IR-110", "ISO8859_4");
/* 211 */     MAP.put("ISO_8859-4", "ISO8859_4");
/* 212 */     MAP.put("LATIN4", "ISO8859_4");
/* 213 */     MAP.put("CSISOLATIN4", "ISO8859_4");
/* 214 */     MAP.put("L4", "ISO8859_4");
/*     */     
/* 216 */     MAP.put("ISO-8859-5", "ISO8859_5");
/* 217 */     MAP.put("ISO-IR-144", "ISO8859_5");
/* 218 */     MAP.put("ISO_8859-5", "ISO8859_5");
/* 219 */     MAP.put("CYRILLIC", "ISO8859_5");
/* 220 */     MAP.put("CSISOLATINCYRILLIC", "ISO8859_5");
/*     */     
/* 222 */     MAP.put("ISO-8859-6", "ISO8859_6");
/* 223 */     MAP.put("ISO-IR-127", "ISO8859_6");
/* 224 */     MAP.put("ISO_8859-6", "ISO8859_6");
/* 225 */     MAP.put("ECMA-114", "ISO8859_6");
/* 226 */     MAP.put("ASMO-708", "ISO8859_6");
/* 227 */     MAP.put("ARABIC", "ISO8859_6");
/* 228 */     MAP.put("CSISOLATINARABIC", "ISO8859_6");
/*     */     
/* 230 */     MAP.put("ISO-8859-7", "ISO8859_7");
/* 231 */     MAP.put("ISO-IR-126", "ISO8859_7");
/* 232 */     MAP.put("ISO_8859-7", "ISO8859_7");
/* 233 */     MAP.put("ELOT_928", "ISO8859_7");
/* 234 */     MAP.put("ECMA-118", "ISO8859_7");
/* 235 */     MAP.put("GREEK", "ISO8859_7");
/* 236 */     MAP.put("CSISOLATINGREEK", "ISO8859_7");
/* 237 */     MAP.put("GREEK8", "ISO8859_7");
/*     */     
/* 239 */     MAP.put("ISO-8859-8", "ISO8859_8");
/* 240 */     MAP.put("ISO-8859-8-I", "ISO8859_8");
/* 241 */     MAP.put("ISO-IR-138", "ISO8859_8");
/* 242 */     MAP.put("ISO_8859-8", "ISO8859_8");
/* 243 */     MAP.put("HEBREW", "ISO8859_8");
/* 244 */     MAP.put("CSISOLATINHEBREW", "ISO8859_8");
/*     */     
/* 246 */     MAP.put("ISO-8859-9", "ISO8859_9");
/* 247 */     MAP.put("ISO-IR-148", "ISO8859_9");
/* 248 */     MAP.put("ISO_8859-9", "ISO8859_9");
/* 249 */     MAP.put("LATIN5", "ISO8859_9");
/* 250 */     MAP.put("CSISOLATIN5", "ISO8859_9");
/* 251 */     MAP.put("L5", "ISO8859_9");
/*     */     
/* 253 */     MAP.put("KOI8-R", "KOI8_R");
/* 254 */     MAP.put("CSKOI8-R", "KOI8_R");
/* 255 */     MAP.put("US-ASCII", "ASCII");
/* 256 */     MAP.put("ISO-IR-6", "ASCII");
/* 257 */     MAP.put("ANSI_X3.4-1986", "ASCII");
/* 258 */     MAP.put("ISO_646.IRV:1991", "ASCII");
/* 259 */     MAP.put("ASCII", "ASCII");
/* 260 */     MAP.put("CSASCII", "ASCII");
/* 261 */     MAP.put("ISO646-US", "ASCII");
/* 262 */     MAP.put("US", "ASCII");
/* 263 */     MAP.put("IBM367", "ASCII");
/* 264 */     MAP.put("CP367", "ASCII");
/* 265 */     MAP.put("UTF-8", "UTF8");
/* 266 */     MAP.put("UTF-16", "Unicode");
/* 267 */     MAP.put("UTF-16BE", "UnicodeBig");
/* 268 */     MAP.put("UTF-16LE", "UnicodeLittle");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaEncoding(String iana) {
/* 278 */     String IANA = iana.toUpperCase();
/* 279 */     String jdec = MAP.get(IANA);
/* 280 */     if (jdec == null)
/* 281 */       jdec = iana; 
/* 282 */     return jdec;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/IanaEncodings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */