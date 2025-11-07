package com.github.simy4.alabaster

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.startOffset

class KeywordAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.node.elementType.debugName
    if ("true" == elementType
      || "false" == elementType
      || "null" == elementType
      || "NULL" == elementType
      || "NULL_KEYWORD" == elementType
      || "TRUE_KEYWORD" == elementType
      || "FALSE_KEYWORD" == elementType) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}