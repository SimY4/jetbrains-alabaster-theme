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
    when (elementType) {
      ClojureTokenTypes.SYMBOL_TOKEN ->
        (element.parent.parent as? ClListLike)
          ?.takeIf { it.type == ClojurePsiElement.LIST }
          ?.let { list ->
            when (val head = list.headText) {
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
      ClojureTokenTypes.SEXP_COMMENT ->
        if (element.firstChild.elementType === ClojureTokenTypes.UNEVAL) {
          holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
            .textAttributes(DefaultLanguageHighlighterColors.METADATA)
            .create()
        }
    }
  }
}