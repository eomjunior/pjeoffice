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
/*     */ public class EntitiesToUnicode
/*     */ {
/*  58 */   private static final Map<String, Character> MAP = new HashMap<String, Character>();
/*     */   static {
/*  60 */     MAP.put("nbsp", Character.valueOf(' '));
/*  61 */     MAP.put("iexcl", Character.valueOf('¡'));
/*  62 */     MAP.put("cent", Character.valueOf('¢'));
/*  63 */     MAP.put("pound", Character.valueOf('£'));
/*  64 */     MAP.put("curren", Character.valueOf('¤'));
/*  65 */     MAP.put("yen", Character.valueOf('¥'));
/*  66 */     MAP.put("brvbar", Character.valueOf('¦'));
/*  67 */     MAP.put("sect", Character.valueOf('§'));
/*  68 */     MAP.put("uml", Character.valueOf('¨'));
/*  69 */     MAP.put("copy", Character.valueOf('©'));
/*  70 */     MAP.put("ordf", Character.valueOf('ª'));
/*  71 */     MAP.put("laquo", Character.valueOf('«'));
/*  72 */     MAP.put("not", Character.valueOf('¬'));
/*  73 */     MAP.put("shy", Character.valueOf('­'));
/*  74 */     MAP.put("reg", Character.valueOf('®'));
/*  75 */     MAP.put("macr", Character.valueOf('¯'));
/*  76 */     MAP.put("deg", Character.valueOf('°'));
/*  77 */     MAP.put("plusmn", Character.valueOf('±'));
/*  78 */     MAP.put("sup2", Character.valueOf('²'));
/*  79 */     MAP.put("sup3", Character.valueOf('³'));
/*  80 */     MAP.put("acute", Character.valueOf('´'));
/*  81 */     MAP.put("micro", Character.valueOf('µ'));
/*  82 */     MAP.put("para", Character.valueOf('¶'));
/*  83 */     MAP.put("middot", Character.valueOf('·'));
/*  84 */     MAP.put("cedil", Character.valueOf('¸'));
/*  85 */     MAP.put("sup1", Character.valueOf('¹'));
/*  86 */     MAP.put("ordm", Character.valueOf('º'));
/*  87 */     MAP.put("raquo", Character.valueOf('»'));
/*  88 */     MAP.put("frac14", Character.valueOf('¼'));
/*  89 */     MAP.put("frac12", Character.valueOf('½'));
/*  90 */     MAP.put("frac34", Character.valueOf('¾'));
/*  91 */     MAP.put("iquest", Character.valueOf('¿'));
/*  92 */     MAP.put("Agrave", Character.valueOf('À'));
/*  93 */     MAP.put("Aacute", Character.valueOf('Á'));
/*  94 */     MAP.put("Acirc", Character.valueOf('Â'));
/*  95 */     MAP.put("Atilde", Character.valueOf('Ã'));
/*  96 */     MAP.put("Auml", Character.valueOf('Ä'));
/*  97 */     MAP.put("Aring", Character.valueOf('Å'));
/*  98 */     MAP.put("AElig", Character.valueOf('Æ'));
/*  99 */     MAP.put("Ccedil", Character.valueOf('Ç'));
/* 100 */     MAP.put("Egrave", Character.valueOf('È'));
/* 101 */     MAP.put("Eacute", Character.valueOf('É'));
/* 102 */     MAP.put("Ecirc", Character.valueOf('Ê'));
/* 103 */     MAP.put("Euml", Character.valueOf('Ë'));
/* 104 */     MAP.put("Igrave", Character.valueOf('Ì'));
/* 105 */     MAP.put("Iacute", Character.valueOf('Í'));
/* 106 */     MAP.put("Icirc", Character.valueOf('Î'));
/* 107 */     MAP.put("Iuml", Character.valueOf('Ï'));
/* 108 */     MAP.put("ETH", Character.valueOf('Ð'));
/* 109 */     MAP.put("Ntilde", Character.valueOf('Ñ'));
/* 110 */     MAP.put("Ograve", Character.valueOf('Ò'));
/* 111 */     MAP.put("Oacute", Character.valueOf('Ó'));
/* 112 */     MAP.put("Ocirc", Character.valueOf('Ô'));
/* 113 */     MAP.put("Otilde", Character.valueOf('Õ'));
/* 114 */     MAP.put("Ouml", Character.valueOf('Ö'));
/* 115 */     MAP.put("times", Character.valueOf('×'));
/* 116 */     MAP.put("Oslash", Character.valueOf('Ø'));
/* 117 */     MAP.put("Ugrave", Character.valueOf('Ù'));
/* 118 */     MAP.put("Uacute", Character.valueOf('Ú'));
/* 119 */     MAP.put("Ucirc", Character.valueOf('Û'));
/* 120 */     MAP.put("Uuml", Character.valueOf('Ü'));
/* 121 */     MAP.put("Yacute", Character.valueOf('Ý'));
/* 122 */     MAP.put("THORN", Character.valueOf('Þ'));
/* 123 */     MAP.put("szlig", Character.valueOf('ß'));
/* 124 */     MAP.put("agrave", Character.valueOf('à'));
/* 125 */     MAP.put("aacute", Character.valueOf('á'));
/* 126 */     MAP.put("acirc", Character.valueOf('â'));
/* 127 */     MAP.put("atilde", Character.valueOf('ã'));
/* 128 */     MAP.put("auml", Character.valueOf('ä'));
/* 129 */     MAP.put("aring", Character.valueOf('å'));
/* 130 */     MAP.put("aelig", Character.valueOf('æ'));
/* 131 */     MAP.put("ccedil", Character.valueOf('ç'));
/* 132 */     MAP.put("egrave", Character.valueOf('è'));
/* 133 */     MAP.put("eacute", Character.valueOf('é'));
/* 134 */     MAP.put("ecirc", Character.valueOf('ê'));
/* 135 */     MAP.put("euml", Character.valueOf('ë'));
/* 136 */     MAP.put("igrave", Character.valueOf('ì'));
/* 137 */     MAP.put("iacute", Character.valueOf('í'));
/* 138 */     MAP.put("icirc", Character.valueOf('î'));
/* 139 */     MAP.put("iuml", Character.valueOf('ï'));
/* 140 */     MAP.put("eth", Character.valueOf('ð'));
/* 141 */     MAP.put("ntilde", Character.valueOf('ñ'));
/* 142 */     MAP.put("ograve", Character.valueOf('ò'));
/* 143 */     MAP.put("oacute", Character.valueOf('ó'));
/* 144 */     MAP.put("ocirc", Character.valueOf('ô'));
/* 145 */     MAP.put("otilde", Character.valueOf('õ'));
/* 146 */     MAP.put("ouml", Character.valueOf('ö'));
/* 147 */     MAP.put("divide", Character.valueOf('÷'));
/* 148 */     MAP.put("oslash", Character.valueOf('ø'));
/* 149 */     MAP.put("ugrave", Character.valueOf('ù'));
/* 150 */     MAP.put("uacute", Character.valueOf('ú'));
/* 151 */     MAP.put("ucirc", Character.valueOf('û'));
/* 152 */     MAP.put("uuml", Character.valueOf('ü'));
/* 153 */     MAP.put("yacute", Character.valueOf('ý'));
/* 154 */     MAP.put("thorn", Character.valueOf('þ'));
/* 155 */     MAP.put("yuml", Character.valueOf('ÿ'));
/*     */     
/* 157 */     MAP.put("fnof", Character.valueOf('ƒ'));
/*     */     
/* 159 */     MAP.put("Alpha", Character.valueOf('Α'));
/* 160 */     MAP.put("Beta", Character.valueOf('Β'));
/* 161 */     MAP.put("Gamma", Character.valueOf('Γ'));
/* 162 */     MAP.put("Delta", Character.valueOf('Δ'));
/* 163 */     MAP.put("Epsilon", Character.valueOf('Ε'));
/* 164 */     MAP.put("Zeta", Character.valueOf('Ζ'));
/* 165 */     MAP.put("Eta", Character.valueOf('Η'));
/* 166 */     MAP.put("Theta", Character.valueOf('Θ'));
/* 167 */     MAP.put("Iota", Character.valueOf('Ι'));
/* 168 */     MAP.put("Kappa", Character.valueOf('Κ'));
/* 169 */     MAP.put("Lambda", Character.valueOf('Λ'));
/* 170 */     MAP.put("Mu", Character.valueOf('Μ'));
/* 171 */     MAP.put("Nu", Character.valueOf('Ν'));
/* 172 */     MAP.put("Xi", Character.valueOf('Ξ'));
/* 173 */     MAP.put("Omicron", Character.valueOf('Ο'));
/* 174 */     MAP.put("Pi", Character.valueOf('Π'));
/* 175 */     MAP.put("Rho", Character.valueOf('Ρ'));
/*     */     
/* 177 */     MAP.put("Sigma", Character.valueOf('Σ'));
/* 178 */     MAP.put("Tau", Character.valueOf('Τ'));
/* 179 */     MAP.put("Upsilon", Character.valueOf('Υ'));
/* 180 */     MAP.put("Phi", Character.valueOf('Φ'));
/* 181 */     MAP.put("Chi", Character.valueOf('Χ'));
/* 182 */     MAP.put("Psi", Character.valueOf('Ψ'));
/* 183 */     MAP.put("Omega", Character.valueOf('Ω'));
/* 184 */     MAP.put("alpha", Character.valueOf('α'));
/* 185 */     MAP.put("beta", Character.valueOf('β'));
/* 186 */     MAP.put("gamma", Character.valueOf('γ'));
/* 187 */     MAP.put("delta", Character.valueOf('δ'));
/* 188 */     MAP.put("epsilon", Character.valueOf('ε'));
/* 189 */     MAP.put("zeta", Character.valueOf('ζ'));
/* 190 */     MAP.put("eta", Character.valueOf('η'));
/* 191 */     MAP.put("theta", Character.valueOf('θ'));
/* 192 */     MAP.put("iota", Character.valueOf('ι'));
/* 193 */     MAP.put("kappa", Character.valueOf('κ'));
/* 194 */     MAP.put("lambda", Character.valueOf('λ'));
/* 195 */     MAP.put("mu", Character.valueOf('μ'));
/* 196 */     MAP.put("nu", Character.valueOf('ν'));
/* 197 */     MAP.put("xi", Character.valueOf('ξ'));
/* 198 */     MAP.put("omicron", Character.valueOf('ο'));
/* 199 */     MAP.put("pi", Character.valueOf('π'));
/* 200 */     MAP.put("rho", Character.valueOf('ρ'));
/* 201 */     MAP.put("sigmaf", Character.valueOf('ς'));
/* 202 */     MAP.put("sigma", Character.valueOf('σ'));
/* 203 */     MAP.put("tau", Character.valueOf('τ'));
/* 204 */     MAP.put("upsilon", Character.valueOf('υ'));
/* 205 */     MAP.put("phi", Character.valueOf('φ'));
/* 206 */     MAP.put("chi", Character.valueOf('χ'));
/* 207 */     MAP.put("psi", Character.valueOf('ψ'));
/* 208 */     MAP.put("omega", Character.valueOf('ω'));
/* 209 */     MAP.put("thetasym", Character.valueOf('ϑ'));
/* 210 */     MAP.put("upsih", Character.valueOf('ϒ'));
/* 211 */     MAP.put("piv", Character.valueOf('ϖ'));
/*     */     
/* 213 */     MAP.put("bull", Character.valueOf('•'));
/*     */     
/* 215 */     MAP.put("hellip", Character.valueOf('…'));
/* 216 */     MAP.put("prime", Character.valueOf('′'));
/* 217 */     MAP.put("Prime", Character.valueOf('″'));
/* 218 */     MAP.put("oline", Character.valueOf('‾'));
/* 219 */     MAP.put("frasl", Character.valueOf('⁄'));
/*     */     
/* 221 */     MAP.put("weierp", Character.valueOf('℘'));
/* 222 */     MAP.put("image", Character.valueOf('ℑ'));
/* 223 */     MAP.put("real", Character.valueOf('ℜ'));
/* 224 */     MAP.put("trade", Character.valueOf('™'));
/* 225 */     MAP.put("alefsym", Character.valueOf('ℵ'));
/*     */ 
/*     */ 
/*     */     
/* 229 */     MAP.put("larr", Character.valueOf('←'));
/* 230 */     MAP.put("uarr", Character.valueOf('↑'));
/* 231 */     MAP.put("rarr", Character.valueOf('→'));
/* 232 */     MAP.put("darr", Character.valueOf('↓'));
/* 233 */     MAP.put("harr", Character.valueOf('↔'));
/* 234 */     MAP.put("crarr", Character.valueOf('↵'));
/* 235 */     MAP.put("lArr", Character.valueOf('⇐'));
/*     */ 
/*     */ 
/*     */     
/* 239 */     MAP.put("uArr", Character.valueOf('⇑'));
/* 240 */     MAP.put("rArr", Character.valueOf('⇒'));
/*     */ 
/*     */ 
/*     */     
/* 244 */     MAP.put("dArr", Character.valueOf('⇓'));
/* 245 */     MAP.put("hArr", Character.valueOf('⇔'));
/*     */     
/* 247 */     MAP.put("forall", Character.valueOf('∀'));
/* 248 */     MAP.put("part", Character.valueOf('∂'));
/* 249 */     MAP.put("exist", Character.valueOf('∃'));
/* 250 */     MAP.put("empty", Character.valueOf('∅'));
/* 251 */     MAP.put("nabla", Character.valueOf('∇'));
/* 252 */     MAP.put("isin", Character.valueOf('∈'));
/* 253 */     MAP.put("notin", Character.valueOf('∉'));
/* 254 */     MAP.put("ni", Character.valueOf('∋'));
/*     */     
/* 256 */     MAP.put("prod", Character.valueOf('∏'));
/*     */ 
/*     */     
/* 259 */     MAP.put("sum", Character.valueOf('∑'));
/*     */ 
/*     */     
/* 262 */     MAP.put("minus", Character.valueOf('−'));
/* 263 */     MAP.put("lowast", Character.valueOf('∗'));
/* 264 */     MAP.put("radic", Character.valueOf('√'));
/* 265 */     MAP.put("prop", Character.valueOf('∝'));
/* 266 */     MAP.put("infin", Character.valueOf('∞'));
/* 267 */     MAP.put("ang", Character.valueOf('∠'));
/* 268 */     MAP.put("and", Character.valueOf('∧'));
/* 269 */     MAP.put("or", Character.valueOf('∨'));
/* 270 */     MAP.put("cap", Character.valueOf('∩'));
/* 271 */     MAP.put("cup", Character.valueOf('∪'));
/* 272 */     MAP.put("int", Character.valueOf('∫'));
/* 273 */     MAP.put("there4", Character.valueOf('∴'));
/* 274 */     MAP.put("sim", Character.valueOf('∼'));
/*     */ 
/*     */     
/* 277 */     MAP.put("cong", Character.valueOf('≅'));
/* 278 */     MAP.put("asymp", Character.valueOf('≈'));
/* 279 */     MAP.put("ne", Character.valueOf('≠'));
/* 280 */     MAP.put("equiv", Character.valueOf('≡'));
/* 281 */     MAP.put("le", Character.valueOf('≤'));
/* 282 */     MAP.put("ge", Character.valueOf('≥'));
/* 283 */     MAP.put("sub", Character.valueOf('⊂'));
/* 284 */     MAP.put("sup", Character.valueOf('⊃'));
/*     */ 
/*     */ 
/*     */     
/* 288 */     MAP.put("nsub", Character.valueOf('⊄'));
/* 289 */     MAP.put("sube", Character.valueOf('⊆'));
/* 290 */     MAP.put("supe", Character.valueOf('⊇'));
/* 291 */     MAP.put("oplus", Character.valueOf('⊕'));
/* 292 */     MAP.put("otimes", Character.valueOf('⊗'));
/* 293 */     MAP.put("perp", Character.valueOf('⊥'));
/* 294 */     MAP.put("sdot", Character.valueOf('⋅'));
/*     */ 
/*     */     
/* 297 */     MAP.put("lceil", Character.valueOf('⌈'));
/* 298 */     MAP.put("rceil", Character.valueOf('⌉'));
/* 299 */     MAP.put("lfloor", Character.valueOf('⌊'));
/* 300 */     MAP.put("rfloor", Character.valueOf('⌋'));
/* 301 */     MAP.put("lang", Character.valueOf('〈'));
/*     */ 
/*     */     
/* 304 */     MAP.put("rang", Character.valueOf('〉'));
/*     */ 
/*     */ 
/*     */     
/* 308 */     MAP.put("loz", Character.valueOf('◊'));
/*     */     
/* 310 */     MAP.put("spades", Character.valueOf('♠'));
/*     */     
/* 312 */     MAP.put("clubs", Character.valueOf('♣'));
/* 313 */     MAP.put("hearts", Character.valueOf('♥'));
/* 314 */     MAP.put("diams", Character.valueOf('♦'));
/*     */     
/* 316 */     MAP.put("quot", Character.valueOf('"'));
/* 317 */     MAP.put("amp", Character.valueOf('&'));
/* 318 */     MAP.put("apos", Character.valueOf('\''));
/* 319 */     MAP.put("lt", Character.valueOf('<'));
/* 320 */     MAP.put("gt", Character.valueOf('>'));
/*     */     
/* 322 */     MAP.put("OElig", Character.valueOf('Œ'));
/* 323 */     MAP.put("oelig", Character.valueOf('œ'));
/*     */     
/* 325 */     MAP.put("Scaron", Character.valueOf('Š'));
/* 326 */     MAP.put("scaron", Character.valueOf('š'));
/* 327 */     MAP.put("Yuml", Character.valueOf('Ÿ'));
/*     */     
/* 329 */     MAP.put("circ", Character.valueOf('ˆ'));
/* 330 */     MAP.put("tilde", Character.valueOf('˜'));
/*     */     
/* 332 */     MAP.put("ensp", Character.valueOf(' '));
/* 333 */     MAP.put("emsp", Character.valueOf(' '));
/* 334 */     MAP.put("thinsp", Character.valueOf(' '));
/* 335 */     MAP.put("zwnj", Character.valueOf('‌'));
/* 336 */     MAP.put("zwj", Character.valueOf('‍'));
/* 337 */     MAP.put("lrm", Character.valueOf('‎'));
/* 338 */     MAP.put("rlm", Character.valueOf('‏'));
/* 339 */     MAP.put("ndash", Character.valueOf('–'));
/* 340 */     MAP.put("mdash", Character.valueOf('—'));
/* 341 */     MAP.put("lsquo", Character.valueOf('‘'));
/* 342 */     MAP.put("rsquo", Character.valueOf('’'));
/* 343 */     MAP.put("sbquo", Character.valueOf('‚'));
/* 344 */     MAP.put("ldquo", Character.valueOf('“'));
/* 345 */     MAP.put("rdquo", Character.valueOf('”'));
/* 346 */     MAP.put("bdquo", Character.valueOf('„'));
/* 347 */     MAP.put("dagger", Character.valueOf('†'));
/* 348 */     MAP.put("Dagger", Character.valueOf('‡'));
/* 349 */     MAP.put("permil", Character.valueOf('‰'));
/* 350 */     MAP.put("lsaquo", Character.valueOf('‹'));
/*     */     
/* 352 */     MAP.put("rsaquo", Character.valueOf('›'));
/*     */     
/* 354 */     MAP.put("euro", Character.valueOf('€'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char decodeEntity(String name) {
/* 365 */     if (name.startsWith("#x")) {
/*     */       try {
/* 367 */         return (char)Integer.parseInt(name.substring(2), 16);
/*     */       }
/* 369 */       catch (NumberFormatException nfe) {
/* 370 */         return Character.MIN_VALUE;
/*     */       } 
/*     */     }
/* 373 */     if (name.startsWith("#")) {
/*     */       try {
/* 375 */         return (char)Integer.parseInt(name.substring(1));
/*     */       }
/* 377 */       catch (NumberFormatException nfe) {
/* 378 */         return Character.MIN_VALUE;
/*     */       } 
/*     */     }
/* 381 */     Character c = MAP.get(name);
/* 382 */     if (c == null) {
/* 383 */       return Character.MIN_VALUE;
/*     */     }
/* 385 */     return c.charValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String decodeString(String s) {
/* 393 */     int pos_amp = s.indexOf('&');
/* 394 */     if (pos_amp == -1) return s;
/*     */ 
/*     */ 
/*     */     
/* 398 */     StringBuffer buf = new StringBuffer(s.substring(0, pos_amp));
/*     */     
/*     */     while (true) {
/* 401 */       int pos_sc = s.indexOf(';', pos_amp);
/* 402 */       if (pos_sc == -1) {
/* 403 */         buf.append(s.substring(pos_amp));
/* 404 */         return buf.toString();
/*     */       } 
/* 406 */       int pos_a = s.indexOf('&', pos_amp + 1);
/* 407 */       while (pos_a != -1 && pos_a < pos_sc) {
/* 408 */         buf.append(s.substring(pos_amp, pos_a));
/* 409 */         pos_amp = pos_a;
/* 410 */         pos_a = s.indexOf('&', pos_amp + 1);
/*     */       } 
/* 412 */       char replace = decodeEntity(s.substring(pos_amp + 1, pos_sc));
/* 413 */       if (s.length() < pos_sc + 1) {
/* 414 */         return buf.toString();
/*     */       }
/* 416 */       if (replace == '\000') {
/* 417 */         buf.append(s.substring(pos_amp, pos_sc + 1));
/*     */       } else {
/*     */         
/* 420 */         buf.append(replace);
/*     */       } 
/* 422 */       pos_amp = s.indexOf('&', pos_sc);
/* 423 */       if (pos_amp == -1) {
/* 424 */         buf.append(s.substring(pos_sc + 1));
/* 425 */         return buf.toString();
/*     */       } 
/*     */       
/* 428 */       buf.append(s.substring(pos_sc + 1, pos_amp));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/EntitiesToUnicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */