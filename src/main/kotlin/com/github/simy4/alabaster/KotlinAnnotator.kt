package com.github.simy4.alabaster

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

class KotlinAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.elementType
    if (KtStubElementTypes.BOOLEAN_CONSTANT == elementType
      || KtStubElementTypes.NULL == elementType
    ) {
      holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
        .textAttributes(DefaultLanguageHighlighterColors.NUMBER)
        .create()
    }
  }
}