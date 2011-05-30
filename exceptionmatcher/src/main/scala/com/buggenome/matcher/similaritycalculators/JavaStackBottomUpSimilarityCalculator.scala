package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Responsible for matching a stack trace against other(s) in a bottom up way.
 * User: pasemes
 * Date: 14/05/11
 * Time: 15:13
 */
//exclui complete match (isto e, nunca retorna 1)
class JavaStackBottomUpSimilarityCalculator extends JavaStackSimilarityCalculator {

    protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0 //TODO implement
    }

    protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0 //TODO implement
    }

    protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0 //TODO implement
    }

}