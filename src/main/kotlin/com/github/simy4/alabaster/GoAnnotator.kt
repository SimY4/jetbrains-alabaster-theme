package com.github.simy4.alabaster

import com.goide.GoParserDefinition
import com.goide.GoTypes
import com.goide.psi.GoTokenType
import com.goide.psi.GoTypeSpec
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiElement

class GoAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.node.elementType
    if ((GoTypes.IDENTIFIER == elementType && "nil" == element.text)) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}