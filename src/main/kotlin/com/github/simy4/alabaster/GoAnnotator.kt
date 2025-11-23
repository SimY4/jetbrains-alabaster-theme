package com.github.simy4.alabaster

import com.goide.GoTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType

class GoAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.elementType
    if ((GoTypes.IDENTIFIER == elementType && "nil" == element.text)) {
      holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}