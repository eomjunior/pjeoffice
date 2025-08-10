/*     */ package com.itextpdf.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpecialSymbol
/*     */ {
/*     */   public static int index(String string) {
/*  71 */     int length = string.length();
/*  72 */     for (int i = 0; i < length; i++) {
/*  73 */       if (getCorrespondingSymbol(string.charAt(i)) != ' ') {
/*  74 */         return i;
/*     */       }
/*     */     } 
/*  77 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Chunk get(char c, Font font) {
/*  87 */     char greek = getCorrespondingSymbol(c);
/*  88 */     if (greek == ' ') {
/*  89 */       return new Chunk(String.valueOf(c), font);
/*     */     }
/*  91 */     Font symbol = new Font(Font.FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
/*  92 */     String s = String.valueOf(greek);
/*  93 */     return new Chunk(s, symbol);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char getCorrespondingSymbol(char c) {
/* 103 */     switch (c) {
/*     */       case 'Α':
/* 105 */         return 'A';
/*     */       case 'Β':
/* 107 */         return 'B';
/*     */       case 'Γ':
/* 109 */         return 'G';
/*     */       case 'Δ':
/* 111 */         return 'D';
/*     */       case 'Ε':
/* 113 */         return 'E';
/*     */       case 'Ζ':
/* 115 */         return 'Z';
/*     */       case 'Η':
/* 117 */         return 'H';
/*     */       case 'Θ':
/* 119 */         return 'Q';
/*     */       case 'Ι':
/* 121 */         return 'I';
/*     */       case 'Κ':
/* 123 */         return 'K';
/*     */       case 'Λ':
/* 125 */         return 'L';
/*     */       case 'Μ':
/* 127 */         return 'M';
/*     */       case 'Ν':
/* 129 */         return 'N';
/*     */       case 'Ξ':
/* 131 */         return 'X';
/*     */       case 'Ο':
/* 133 */         return 'O';
/*     */       case 'Π':
/* 135 */         return 'P';
/*     */       case 'Ρ':
/* 137 */         return 'R';
/*     */       case 'Σ':
/* 139 */         return 'S';
/*     */       case 'Τ':
/* 141 */         return 'T';
/*     */       case 'Υ':
/* 143 */         return 'U';
/*     */       case 'Φ':
/* 145 */         return 'F';
/*     */       case 'Χ':
/* 147 */         return 'C';
/*     */       case 'Ψ':
/* 149 */         return 'Y';
/*     */       case 'Ω':
/* 151 */         return 'W';
/*     */       case 'α':
/* 153 */         return 'a';
/*     */       case 'β':
/* 155 */         return 'b';
/*     */       case 'γ':
/* 157 */         return 'g';
/*     */       case 'δ':
/* 159 */         return 'd';
/*     */       case 'ε':
/* 161 */         return 'e';
/*     */       case 'ζ':
/* 163 */         return 'z';
/*     */       case 'η':
/* 165 */         return 'h';
/*     */       case 'θ':
/* 167 */         return 'q';
/*     */       case 'ι':
/* 169 */         return 'i';
/*     */       case 'κ':
/* 171 */         return 'k';
/*     */       case 'λ':
/* 173 */         return 'l';
/*     */       case 'μ':
/* 175 */         return 'm';
/*     */       case 'ν':
/* 177 */         return 'n';
/*     */       case 'ξ':
/* 179 */         return 'x';
/*     */       case 'ο':
/* 181 */         return 'o';
/*     */       case 'π':
/* 183 */         return 'p';
/*     */       case 'ρ':
/* 185 */         return 'r';
/*     */       case 'ς':
/* 187 */         return 'V';
/*     */       case 'σ':
/* 189 */         return 's';
/*     */       case 'τ':
/* 191 */         return 't';
/*     */       case 'υ':
/* 193 */         return 'u';
/*     */       case 'φ':
/* 195 */         return 'f';
/*     */       case 'χ':
/* 197 */         return 'c';
/*     */       case 'ψ':
/* 199 */         return 'y';
/*     */       case 'ω':
/* 201 */         return 'w';
/*     */     } 
/* 203 */     return ' ';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/SpecialSymbol.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */