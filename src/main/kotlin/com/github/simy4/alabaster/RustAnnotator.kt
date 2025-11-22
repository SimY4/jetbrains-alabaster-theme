package com.github.simy4.alabaster

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import org.rust.lang.core.psi.RsElementTypes

class RustAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.node.elementType
    if (RsElementTypes.TRUE == elementType
      || RsElementTypes.FALSE == elementType
      || RsElementTypes.BOOL_LITERAL == elementType) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}