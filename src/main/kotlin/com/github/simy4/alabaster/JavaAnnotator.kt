package com.github.simy4.alabaster

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class JavaAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.elementType
    if (JavaTokenType.TRUE_KEYWORD == elementType
      || JavaTokenType.FALSE_KEYWORD == elementType
      || JavaTokenType.NULL_KEYWORD == elementType) {
      holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}