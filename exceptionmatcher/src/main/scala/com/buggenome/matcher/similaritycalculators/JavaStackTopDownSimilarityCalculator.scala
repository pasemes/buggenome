package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Created by IntelliJ IDEA.
 * User: pasemes
 * Date: 14/05/11
 * Time: 15:12
 */
//exclui complete match (isto e, nunca retorna 1)
class JavaStackTopDownSimilarityCalculator extends JavaStackSimilarityCalculator {

    protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0
    }

    protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0
    }

    protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        0.0
    }

}