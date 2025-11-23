package com.github.simy4.alabaster

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import cursive.lexer.ClojureTokenTypes
import cursive.psi.ClojurePsiElement
import cursive.psi.api.ClListLike

class ClojureAnnotator: Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.elementType
    if (ClojureTokenTypes.SYMBOL_TOKEN == elementType) {
      (element.parent.parent as? ClListLike)
        ?.takeIf { it.type == ClojurePsiElement.LIST }
        ?.let { list ->
          val head = list.headText
          when (head) {
            "def"
              , "defn"
              , "defn-"
              , "defmulti"
              , "defmethod"
              , "defmacro"
              , "deftest"
              , "definterface"
              , "defprotocol"
              , "deftype" ->
                if (element.text !== head) {
                  holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                    .textAttributes(DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
                    .create()
                }
          }
      }
    }
  }
}