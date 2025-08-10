/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixType;
/*     */ import java.util.List;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class InternetDomainName
/*     */ {
/*  80 */   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
/*  81 */   private static final Splitter DOT_SPLITTER = Splitter.on('.');
/*  82 */   private static final Joiner DOT_JOINER = Joiner.on('.');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_SUFFIX_FOUND = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SUFFIX_NOT_INITIALIZED = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_PARTS = 127;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 253;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 63;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<String> parts;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @LazyInit
/* 129 */   private int publicSuffixIndexCache = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @LazyInit
/* 140 */   private int registrySuffixIndexCache = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternetDomainName(String name) {
/* 151 */     name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
/*     */     
/* 153 */     if (name.endsWith(".")) {
/* 154 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */     
/* 157 */     Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", name);
/* 158 */     this.name = name;
/*     */     
/* 160 */     this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
/* 161 */     Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", name);
/* 162 */     Preconditions.checkArgument(validateSyntax((List<String>)this.parts), "Not a valid domain name: '%s'", name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int publicSuffixIndex() {
/* 172 */     int publicSuffixIndexLocal = this.publicSuffixIndexCache;
/* 173 */     if (publicSuffixIndexLocal == -2) {
/* 174 */       this
/* 175 */         .publicSuffixIndexCache = publicSuffixIndexLocal = findSuffixOfType(Optional.absent());
/*     */     }
/* 177 */     return publicSuffixIndexLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int registrySuffixIndex() {
/* 187 */     int registrySuffixIndexLocal = this.registrySuffixIndexCache;
/* 188 */     if (registrySuffixIndexLocal == -2) {
/* 189 */       this
/* 190 */         .registrySuffixIndexCache = registrySuffixIndexLocal = findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
/*     */     }
/* 192 */     return registrySuffixIndexLocal;
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
/*     */   private int findSuffixOfType(Optional<PublicSuffixType> desiredType) {
/* 205 */     int partsSize = this.parts.size();
/*     */     
/* 207 */     for (int i = 0; i < partsSize; i++) {
/* 208 */       String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
/*     */       
/* 210 */       if (i > 0 && 
/* 211 */         matchesType(desiredType, 
/* 212 */           Optional.fromNullable(PublicSuffixPatterns.UNDER.get(ancestorName)))) {
/* 213 */         return i - 1;
/*     */       }
/*     */       
/* 216 */       if (matchesType(desiredType, 
/* 217 */           Optional.fromNullable(PublicSuffixPatterns.EXACT.get(ancestorName)))) {
/* 218 */         return i;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 224 */       if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
/* 225 */         return i + 1;
/*     */       }
/*     */     } 
/*     */     
/* 229 */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static InternetDomainName from(String domain) {
/* 252 */     return new InternetDomainName((String)Preconditions.checkNotNull(domain));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateSyntax(List<String> parts) {
/* 262 */     int lastIndex = parts.size() - 1;
/*     */ 
/*     */ 
/*     */     
/* 266 */     if (!validatePart(parts.get(lastIndex), true)) {
/* 267 */       return false;
/*     */     }
/*     */     
/* 270 */     for (int i = 0; i < lastIndex; i++) {
/* 271 */       String part = parts.get(i);
/* 272 */       if (!validatePart(part, false)) {
/* 273 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 277 */     return true;
/*     */   }
/*     */   
/* 280 */   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
/*     */   
/* 282 */   private static final CharMatcher DIGIT_MATCHER = CharMatcher.inRange('0', '9');
/*     */ 
/*     */   
/* 285 */   private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));
/*     */   
/* 287 */   private static final CharMatcher PART_CHAR_MATCHER = DIGIT_MATCHER
/* 288 */     .or(LETTER_MATCHER).or(DASH_MATCHER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePart(String part, boolean isFinalPart) {
/* 303 */     if (part.length() < 1 || part.length() > 63) {
/* 304 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     String asciiChars = CharMatcher.ascii().retainFrom(part);
/*     */     
/* 319 */     if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
/* 320 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 325 */     if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER
/* 326 */       .matches(part.charAt(part.length() - 1))) {
/* 327 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     if (isFinalPart && DIGIT_MATCHER.matches(part.charAt(0))) {
/* 338 */       return false;
/*     */     }
/*     */     
/* 341 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<String> parts() {
/* 350 */     return this.parts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPublicSuffix() {
/* 374 */     return (publicSuffixIndex() == 0);
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
/*     */   public boolean hasPublicSuffix() {
/* 390 */     return (publicSuffixIndex() != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public InternetDomainName publicSuffix() {
/* 401 */     return hasPublicSuffix() ? ancestor(publicSuffixIndex()) : null;
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
/*     */   public boolean isUnderPublicSuffix() {
/* 417 */     return (publicSuffixIndex() > 0);
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
/*     */   public boolean isTopPrivateDomain() {
/* 433 */     return (publicSuffixIndex() == 1);
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
/*     */ 
/*     */   
/*     */   public InternetDomainName topPrivateDomain() {
/* 453 */     if (isTopPrivateDomain()) {
/* 454 */       return this;
/*     */     }
/* 456 */     Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
/* 457 */     return ancestor(publicSuffixIndex() - 1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRegistrySuffix() {
/* 484 */     return (registrySuffixIndex() == 0);
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
/*     */   public boolean hasRegistrySuffix() {
/* 499 */     return (registrySuffixIndex() != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public InternetDomainName registrySuffix() {
/* 510 */     return hasRegistrySuffix() ? ancestor(registrySuffixIndex()) : null;
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
/*     */   public boolean isUnderRegistrySuffix() {
/* 522 */     return (registrySuffixIndex() > 0);
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
/*     */   public boolean isTopDomainUnderRegistrySuffix() {
/* 537 */     return (registrySuffixIndex() == 1);
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
/*     */   
/*     */   public InternetDomainName topDomainUnderRegistrySuffix() {
/* 556 */     if (isTopDomainUnderRegistrySuffix()) {
/* 557 */       return this;
/*     */     }
/* 559 */     Preconditions.checkState(isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
/* 560 */     return ancestor(registrySuffixIndex() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 565 */     return (this.parts.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName parent() {
/* 576 */     Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
/* 577 */     return ancestor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InternetDomainName ancestor(int levels) {
/* 588 */     return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
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
/*     */   public InternetDomainName child(String leftParts) {
/* 601 */     return from((String)Preconditions.checkNotNull(leftParts) + "." + this.name);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String name) {
/*     */     try {
/* 629 */       InternetDomainName unused = from(name);
/* 630 */       return true;
/* 631 */     } catch (IllegalArgumentException e) {
/* 632 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesType(Optional<PublicSuffixType> desiredType, Optional<PublicSuffixType> actualType) {
/* 642 */     return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 648 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object object) {
/* 658 */     if (object == this) {
/* 659 */       return true;
/*     */     }
/*     */     
/* 662 */     if (object instanceof InternetDomainName) {
/* 663 */       InternetDomainName that = (InternetDomainName)object;
/* 664 */       return this.name.equals(that.name);
/*     */     } 
/*     */     
/* 667 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 672 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/net/InternetDomainName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */