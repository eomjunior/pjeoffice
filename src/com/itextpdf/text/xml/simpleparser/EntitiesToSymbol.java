/*     */ package com.itextpdf.text.xml.simpleparser;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Font;
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
/*     */ public class EntitiesToSymbol
/*     */ {
/*  66 */   private static final Map<String, Character> MAP = new HashMap<String, Character>(); static {
/*  67 */     MAP.put("169", Character.valueOf('ã'));
/*  68 */     MAP.put("172", Character.valueOf('Ø'));
/*  69 */     MAP.put("174", Character.valueOf('Ò'));
/*  70 */     MAP.put("177", Character.valueOf('±'));
/*  71 */     MAP.put("215", Character.valueOf('´'));
/*  72 */     MAP.put("247", Character.valueOf('¸'));
/*  73 */     MAP.put("8230", Character.valueOf('¼'));
/*  74 */     MAP.put("8242", Character.valueOf('¢'));
/*  75 */     MAP.put("8243", Character.valueOf('²'));
/*  76 */     MAP.put("8260", Character.valueOf('¤'));
/*  77 */     MAP.put("8364", Character.valueOf('ð'));
/*  78 */     MAP.put("8465", Character.valueOf('Á'));
/*  79 */     MAP.put("8472", Character.valueOf('Ã'));
/*  80 */     MAP.put("8476", Character.valueOf('Â'));
/*  81 */     MAP.put("8482", Character.valueOf('Ô'));
/*  82 */     MAP.put("8501", Character.valueOf('À'));
/*  83 */     MAP.put("8592", Character.valueOf('¬'));
/*  84 */     MAP.put("8593", Character.valueOf('­'));
/*  85 */     MAP.put("8594", Character.valueOf('®'));
/*  86 */     MAP.put("8595", Character.valueOf('¯'));
/*  87 */     MAP.put("8596", Character.valueOf('«'));
/*  88 */     MAP.put("8629", Character.valueOf('¿'));
/*  89 */     MAP.put("8656", Character.valueOf('Ü'));
/*  90 */     MAP.put("8657", Character.valueOf('Ý'));
/*  91 */     MAP.put("8658", Character.valueOf('Þ'));
/*  92 */     MAP.put("8659", Character.valueOf('ß'));
/*  93 */     MAP.put("8660", Character.valueOf('Û'));
/*  94 */     MAP.put("8704", Character.valueOf('"'));
/*  95 */     MAP.put("8706", Character.valueOf('¶'));
/*  96 */     MAP.put("8707", Character.valueOf('$'));
/*  97 */     MAP.put("8709", Character.valueOf('Æ'));
/*  98 */     MAP.put("8711", Character.valueOf('Ñ'));
/*  99 */     MAP.put("8712", Character.valueOf('Î'));
/* 100 */     MAP.put("8713", Character.valueOf('Ï'));
/* 101 */     MAP.put("8717", Character.valueOf('\''));
/* 102 */     MAP.put("8719", Character.valueOf('Õ'));
/* 103 */     MAP.put("8721", Character.valueOf('å'));
/* 104 */     MAP.put("8722", Character.valueOf('-'));
/* 105 */     MAP.put("8727", Character.valueOf('*'));
/* 106 */     MAP.put("8729", Character.valueOf('·'));
/* 107 */     MAP.put("8730", Character.valueOf('Ö'));
/* 108 */     MAP.put("8733", Character.valueOf('µ'));
/* 109 */     MAP.put("8734", Character.valueOf('¥'));
/* 110 */     MAP.put("8736", Character.valueOf('Ð'));
/* 111 */     MAP.put("8743", Character.valueOf('Ù'));
/* 112 */     MAP.put("8744", Character.valueOf('Ú'));
/* 113 */     MAP.put("8745", Character.valueOf('Ç'));
/* 114 */     MAP.put("8746", Character.valueOf('È'));
/* 115 */     MAP.put("8747", Character.valueOf('ò'));
/* 116 */     MAP.put("8756", Character.valueOf('\\'));
/* 117 */     MAP.put("8764", Character.valueOf('~'));
/* 118 */     MAP.put("8773", Character.valueOf('@'));
/* 119 */     MAP.put("8776", Character.valueOf('»'));
/* 120 */     MAP.put("8800", Character.valueOf('¹'));
/* 121 */     MAP.put("8801", Character.valueOf('º'));
/* 122 */     MAP.put("8804", Character.valueOf('£'));
/* 123 */     MAP.put("8805", Character.valueOf('³'));
/* 124 */     MAP.put("8834", Character.valueOf('Ì'));
/* 125 */     MAP.put("8835", Character.valueOf('É'));
/* 126 */     MAP.put("8836", Character.valueOf('Ë'));
/* 127 */     MAP.put("8838", Character.valueOf('Í'));
/* 128 */     MAP.put("8839", Character.valueOf('Ê'));
/* 129 */     MAP.put("8853", Character.valueOf('Å'));
/* 130 */     MAP.put("8855", Character.valueOf('Ä'));
/* 131 */     MAP.put("8869", Character.valueOf('^'));
/* 132 */     MAP.put("8901", Character.valueOf('×'));
/* 133 */     MAP.put("8992", Character.valueOf('ó'));
/* 134 */     MAP.put("8993", Character.valueOf('õ'));
/* 135 */     MAP.put("9001", Character.valueOf('á'));
/* 136 */     MAP.put("9002", Character.valueOf('ñ'));
/* 137 */     MAP.put("913", Character.valueOf('A'));
/* 138 */     MAP.put("914", Character.valueOf('B'));
/* 139 */     MAP.put("915", Character.valueOf('G'));
/* 140 */     MAP.put("916", Character.valueOf('D'));
/* 141 */     MAP.put("917", Character.valueOf('E'));
/* 142 */     MAP.put("918", Character.valueOf('Z'));
/* 143 */     MAP.put("919", Character.valueOf('H'));
/* 144 */     MAP.put("920", Character.valueOf('Q'));
/* 145 */     MAP.put("921", Character.valueOf('I'));
/* 146 */     MAP.put("922", Character.valueOf('K'));
/* 147 */     MAP.put("923", Character.valueOf('L'));
/* 148 */     MAP.put("924", Character.valueOf('M'));
/* 149 */     MAP.put("925", Character.valueOf('N'));
/* 150 */     MAP.put("926", Character.valueOf('X'));
/* 151 */     MAP.put("927", Character.valueOf('O'));
/* 152 */     MAP.put("928", Character.valueOf('P'));
/* 153 */     MAP.put("929", Character.valueOf('R'));
/* 154 */     MAP.put("931", Character.valueOf('S'));
/* 155 */     MAP.put("932", Character.valueOf('T'));
/* 156 */     MAP.put("933", Character.valueOf('U'));
/* 157 */     MAP.put("934", Character.valueOf('F'));
/* 158 */     MAP.put("935", Character.valueOf('C'));
/* 159 */     MAP.put("936", Character.valueOf('Y'));
/* 160 */     MAP.put("937", Character.valueOf('W'));
/* 161 */     MAP.put("945", Character.valueOf('a'));
/* 162 */     MAP.put("946", Character.valueOf('b'));
/* 163 */     MAP.put("947", Character.valueOf('g'));
/* 164 */     MAP.put("948", Character.valueOf('d'));
/* 165 */     MAP.put("949", Character.valueOf('e'));
/* 166 */     MAP.put("950", Character.valueOf('z'));
/* 167 */     MAP.put("951", Character.valueOf('h'));
/* 168 */     MAP.put("952", Character.valueOf('q'));
/* 169 */     MAP.put("953", Character.valueOf('i'));
/* 170 */     MAP.put("954", Character.valueOf('k'));
/* 171 */     MAP.put("955", Character.valueOf('l'));
/* 172 */     MAP.put("956", Character.valueOf('m'));
/* 173 */     MAP.put("957", Character.valueOf('n'));
/* 174 */     MAP.put("958", Character.valueOf('x'));
/* 175 */     MAP.put("959", Character.valueOf('o'));
/* 176 */     MAP.put("960", Character.valueOf('p'));
/* 177 */     MAP.put("961", Character.valueOf('r'));
/* 178 */     MAP.put("962", Character.valueOf('V'));
/* 179 */     MAP.put("963", Character.valueOf('s'));
/* 180 */     MAP.put("964", Character.valueOf('t'));
/* 181 */     MAP.put("965", Character.valueOf('u'));
/* 182 */     MAP.put("966", Character.valueOf('f'));
/* 183 */     MAP.put("967", Character.valueOf('c'));
/* 184 */     MAP.put("9674", Character.valueOf('à'));
/* 185 */     MAP.put("968", Character.valueOf('y'));
/* 186 */     MAP.put("969", Character.valueOf('w'));
/* 187 */     MAP.put("977", Character.valueOf('J'));
/* 188 */     MAP.put("978", Character.valueOf('¡'));
/* 189 */     MAP.put("981", Character.valueOf('j'));
/* 190 */     MAP.put("982", Character.valueOf('v'));
/* 191 */     MAP.put("9824", Character.valueOf('ª'));
/* 192 */     MAP.put("9827", Character.valueOf('§'));
/* 193 */     MAP.put("9829", Character.valueOf('©'));
/* 194 */     MAP.put("9830", Character.valueOf('¨'));
/* 195 */     MAP.put("Alpha", Character.valueOf('A'));
/* 196 */     MAP.put("Beta", Character.valueOf('B'));
/* 197 */     MAP.put("Chi", Character.valueOf('C'));
/* 198 */     MAP.put("Delta", Character.valueOf('D'));
/* 199 */     MAP.put("Epsilon", Character.valueOf('E'));
/* 200 */     MAP.put("Eta", Character.valueOf('H'));
/* 201 */     MAP.put("Gamma", Character.valueOf('G'));
/* 202 */     MAP.put("Iota", Character.valueOf('I'));
/* 203 */     MAP.put("Kappa", Character.valueOf('K'));
/* 204 */     MAP.put("Lambda", Character.valueOf('L'));
/* 205 */     MAP.put("Mu", Character.valueOf('M'));
/* 206 */     MAP.put("Nu", Character.valueOf('N'));
/* 207 */     MAP.put("Omega", Character.valueOf('W'));
/* 208 */     MAP.put("Omicron", Character.valueOf('O'));
/* 209 */     MAP.put("Phi", Character.valueOf('F'));
/* 210 */     MAP.put("Pi", Character.valueOf('P'));
/* 211 */     MAP.put("Prime", Character.valueOf('²'));
/* 212 */     MAP.put("Psi", Character.valueOf('Y'));
/* 213 */     MAP.put("Rho", Character.valueOf('R'));
/* 214 */     MAP.put("Sigma", Character.valueOf('S'));
/* 215 */     MAP.put("Tau", Character.valueOf('T'));
/* 216 */     MAP.put("Theta", Character.valueOf('Q'));
/* 217 */     MAP.put("Upsilon", Character.valueOf('U'));
/* 218 */     MAP.put("Xi", Character.valueOf('X'));
/* 219 */     MAP.put("Zeta", Character.valueOf('Z'));
/* 220 */     MAP.put("alefsym", Character.valueOf('À'));
/* 221 */     MAP.put("alpha", Character.valueOf('a'));
/* 222 */     MAP.put("and", Character.valueOf('Ù'));
/* 223 */     MAP.put("ang", Character.valueOf('Ð'));
/* 224 */     MAP.put("asymp", Character.valueOf('»'));
/* 225 */     MAP.put("beta", Character.valueOf('b'));
/* 226 */     MAP.put("cap", Character.valueOf('Ç'));
/* 227 */     MAP.put("chi", Character.valueOf('c'));
/* 228 */     MAP.put("clubs", Character.valueOf('§'));
/* 229 */     MAP.put("cong", Character.valueOf('@'));
/* 230 */     MAP.put("copy", Character.valueOf('Ó'));
/* 231 */     MAP.put("crarr", Character.valueOf('¿'));
/* 232 */     MAP.put("cup", Character.valueOf('È'));
/* 233 */     MAP.put("dArr", Character.valueOf('ß'));
/* 234 */     MAP.put("darr", Character.valueOf('¯'));
/* 235 */     MAP.put("delta", Character.valueOf('d'));
/* 236 */     MAP.put("diams", Character.valueOf('¨'));
/* 237 */     MAP.put("divide", Character.valueOf('¸'));
/* 238 */     MAP.put("empty", Character.valueOf('Æ'));
/* 239 */     MAP.put("epsilon", Character.valueOf('e'));
/* 240 */     MAP.put("equiv", Character.valueOf('º'));
/* 241 */     MAP.put("eta", Character.valueOf('h'));
/* 242 */     MAP.put("euro", Character.valueOf('ð'));
/* 243 */     MAP.put("exist", Character.valueOf('$'));
/* 244 */     MAP.put("forall", Character.valueOf('"'));
/* 245 */     MAP.put("frasl", Character.valueOf('¤'));
/* 246 */     MAP.put("gamma", Character.valueOf('g'));
/* 247 */     MAP.put("ge", Character.valueOf('³'));
/* 248 */     MAP.put("hArr", Character.valueOf('Û'));
/* 249 */     MAP.put("harr", Character.valueOf('«'));
/* 250 */     MAP.put("hearts", Character.valueOf('©'));
/* 251 */     MAP.put("hellip", Character.valueOf('¼'));
/* 252 */     MAP.put("horizontal arrow extender", Character.valueOf('¾'));
/* 253 */     MAP.put("image", Character.valueOf('Á'));
/* 254 */     MAP.put("infin", Character.valueOf('¥'));
/* 255 */     MAP.put("int", Character.valueOf('ò'));
/* 256 */     MAP.put("iota", Character.valueOf('i'));
/* 257 */     MAP.put("isin", Character.valueOf('Î'));
/* 258 */     MAP.put("kappa", Character.valueOf('k'));
/* 259 */     MAP.put("lArr", Character.valueOf('Ü'));
/* 260 */     MAP.put("lambda", Character.valueOf('l'));
/* 261 */     MAP.put("lang", Character.valueOf('á'));
/* 262 */     MAP.put("large brace extender", Character.valueOf('ï'));
/* 263 */     MAP.put("large integral extender", Character.valueOf('ô'));
/* 264 */     MAP.put("large left brace (bottom)", Character.valueOf('î'));
/* 265 */     MAP.put("large left brace (middle)", Character.valueOf('í'));
/* 266 */     MAP.put("large left brace (top)", Character.valueOf('ì'));
/* 267 */     MAP.put("large left bracket (bottom)", Character.valueOf('ë'));
/* 268 */     MAP.put("large left bracket (extender)", Character.valueOf('ê'));
/* 269 */     MAP.put("large left bracket (top)", Character.valueOf('é'));
/* 270 */     MAP.put("large left parenthesis (bottom)", Character.valueOf('è'));
/* 271 */     MAP.put("large left parenthesis (extender)", Character.valueOf('ç'));
/* 272 */     MAP.put("large left parenthesis (top)", Character.valueOf('æ'));
/* 273 */     MAP.put("large right brace (bottom)", Character.valueOf('þ'));
/* 274 */     MAP.put("large right brace (middle)", Character.valueOf('ý'));
/* 275 */     MAP.put("large right brace (top)", Character.valueOf('ü'));
/* 276 */     MAP.put("large right bracket (bottom)", Character.valueOf('û'));
/* 277 */     MAP.put("large right bracket (extender)", Character.valueOf('ú'));
/* 278 */     MAP.put("large right bracket (top)", Character.valueOf('ù'));
/* 279 */     MAP.put("large right parenthesis (bottom)", Character.valueOf('ø'));
/* 280 */     MAP.put("large right parenthesis (extender)", Character.valueOf('÷'));
/* 281 */     MAP.put("large right parenthesis (top)", Character.valueOf('ö'));
/* 282 */     MAP.put("larr", Character.valueOf('¬'));
/* 283 */     MAP.put("le", Character.valueOf('£'));
/* 284 */     MAP.put("lowast", Character.valueOf('*'));
/* 285 */     MAP.put("loz", Character.valueOf('à'));
/* 286 */     MAP.put("minus", Character.valueOf('-'));
/* 287 */     MAP.put("mu", Character.valueOf('m'));
/* 288 */     MAP.put("nabla", Character.valueOf('Ñ'));
/* 289 */     MAP.put("ne", Character.valueOf('¹'));
/* 290 */     MAP.put("not", Character.valueOf('Ø'));
/* 291 */     MAP.put("notin", Character.valueOf('Ï'));
/* 292 */     MAP.put("nsub", Character.valueOf('Ë'));
/* 293 */     MAP.put("nu", Character.valueOf('n'));
/* 294 */     MAP.put("omega", Character.valueOf('w'));
/* 295 */     MAP.put("omicron", Character.valueOf('o'));
/* 296 */     MAP.put("oplus", Character.valueOf('Å'));
/* 297 */     MAP.put("or", Character.valueOf('Ú'));
/* 298 */     MAP.put("otimes", Character.valueOf('Ä'));
/* 299 */     MAP.put("part", Character.valueOf('¶'));
/* 300 */     MAP.put("perp", Character.valueOf('^'));
/* 301 */     MAP.put("phi", Character.valueOf('f'));
/* 302 */     MAP.put("pi", Character.valueOf('p'));
/* 303 */     MAP.put("piv", Character.valueOf('v'));
/* 304 */     MAP.put("plusmn", Character.valueOf('±'));
/* 305 */     MAP.put("prime", Character.valueOf('¢'));
/* 306 */     MAP.put("prod", Character.valueOf('Õ'));
/* 307 */     MAP.put("prop", Character.valueOf('µ'));
/* 308 */     MAP.put("psi", Character.valueOf('y'));
/* 309 */     MAP.put("rArr", Character.valueOf('Þ'));
/* 310 */     MAP.put("radic", Character.valueOf('Ö'));
/* 311 */     MAP.put("radical extender", Character.valueOf('`'));
/* 312 */     MAP.put("rang", Character.valueOf('ñ'));
/* 313 */     MAP.put("rarr", Character.valueOf('®'));
/* 314 */     MAP.put("real", Character.valueOf('Â'));
/* 315 */     MAP.put("reg", Character.valueOf('Ò'));
/* 316 */     MAP.put("rho", Character.valueOf('r'));
/* 317 */     MAP.put("sdot", Character.valueOf('×'));
/* 318 */     MAP.put("sigma", Character.valueOf('s'));
/* 319 */     MAP.put("sigmaf", Character.valueOf('V'));
/* 320 */     MAP.put("sim", Character.valueOf('~'));
/* 321 */     MAP.put("spades", Character.valueOf('ª'));
/* 322 */     MAP.put("sub", Character.valueOf('Ì'));
/* 323 */     MAP.put("sube", Character.valueOf('Í'));
/* 324 */     MAP.put("sum", Character.valueOf('å'));
/* 325 */     MAP.put("sup", Character.valueOf('É'));
/* 326 */     MAP.put("supe", Character.valueOf('Ê'));
/* 327 */     MAP.put("tau", Character.valueOf('t'));
/* 328 */     MAP.put("there4", Character.valueOf('\\'));
/* 329 */     MAP.put("theta", Character.valueOf('q'));
/* 330 */     MAP.put("thetasym", Character.valueOf('J'));
/* 331 */     MAP.put("times", Character.valueOf('´'));
/* 332 */     MAP.put("trade", Character.valueOf('Ô'));
/* 333 */     MAP.put("uArr", Character.valueOf('Ý'));
/* 334 */     MAP.put("uarr", Character.valueOf('­'));
/* 335 */     MAP.put("upsih", Character.valueOf('¡'));
/* 336 */     MAP.put("upsilon", Character.valueOf('u'));
/* 337 */     MAP.put("vertical arrow extender", Character.valueOf('½'));
/* 338 */     MAP.put("weierp", Character.valueOf('Ã'));
/* 339 */     MAP.put("xi", Character.valueOf('x'));
/* 340 */     MAP.put("zeta", Character.valueOf('z'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Chunk get(String e, Font font) {
/* 350 */     char s = getCorrespondingSymbol(e);
/* 351 */     if (s == '\000') {
/*     */       try {
/* 353 */         return new Chunk(String.valueOf((char)Integer.parseInt(e)), font);
/*     */       }
/* 355 */       catch (Exception exception) {
/* 356 */         return new Chunk(e, font);
/*     */       } 
/*     */     }
/* 359 */     Font symbol = new Font(Font.FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
/* 360 */     return new Chunk(String.valueOf(s), symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char getCorrespondingSymbol(String name) {
/* 370 */     Character symbol = MAP.get(name);
/* 371 */     if (symbol == null) {
/* 372 */       return Character.MIN_VALUE;
/*     */     }
/* 374 */     return symbol.charValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/simpleparser/EntitiesToSymbol.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */