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
import cursive.psi.api.ClojureFile
import cursive.psi.api.symbols.ClSymbol

class ClojureAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    val elementType = element.elementType
    when (elementType) {
      ClojureTokenTypes.SYMBOL_TOKEN -> {
        (element.parent.parent as? ClListLike)
          ?.takeIf { it.type == ClojurePsiElement.LIST }
          ?.let { list ->
            when (val head = list.headText) {
              "def",
              "defn",
              "defn-",
              "defmulti",
              "defmethod",
              "defmacro",
              "deftest",
              "definterface",
              "defprotocol",
              "deftype",
                ->
                if (element.text !== head) {
                  holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
                    .textAttributes(DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
                    .create()
                }
            }
          }

        // highlight namespaces with greyish colour
        if (element.parent is ClSymbol && element.parent.parent is ClSymbol) {
          holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
            .textAttributes(DefaultLanguageHighlighterColors.METADATA)
            .create()
        }
      }

      // Top level parentheses are highlighted as standard parentheses, otherwise they are greyish colour.
      ClojureTokenTypes.LEFT_PAREN, ClojureTokenTypes.RIGHT_PAREN ->
        (element.parent.parent as? ClojureFile)
          ?.takeIf { it.elementType == ClojurePsiElement.FILE }
          ?.let {
            holder.newSilentAnnotation(HighlightSeverity.TEXT_ATTRIBUTES)
              .textAttributes(DefaultLanguageHighlighterColors.BRACES)
              .create()
          }
    }
  }
}