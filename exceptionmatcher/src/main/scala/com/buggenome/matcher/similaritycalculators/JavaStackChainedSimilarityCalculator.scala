package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Created by IntelliJ IDEA.
 * User: pasemes
 * Date: 14/05/11
 * Time: 15:14
 */
//pode dar match "no meio" (em uma stack chained) da stack e nao so no inicio ou no fim
class JavaStackChainedSimilarityCalculator extends JavaStackSimilarityCalculator {

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