package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Define weights for the different similarity calculation methods.
 * User: pasemes
 * Date: 14/05/11
 * Time: 19:56
 */
trait JavaStackSimilarityCalculationMethodWeights extends JavaStackSimilarityCalculationMethods {

    private val FULL_SIMILARITY_COMPARISON_WEIGHT = 1
    private val IGNORING_LINE_SIMILARITY_COMPARISON_WEIGHT = 0.9
    private val WILDCARD_SIMILARITY_COMPARISON_WEIGHT = 0.8

    abstract override protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            FULL_SIMILARITY_COMPARISON_WEIGHT * super.fullSimilarityComparison(stackTrace, otherStackTrace)

    abstract override protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            IGNORING_LINE_SIMILARITY_COMPARISON_WEIGHT * super.ignoringLineSimilarityComparison(stackTrace, otherStackTrace)

    abstract override protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            WILDCARD_SIMILARITY_COMPARISON_WEIGHT * super.wildcardSimilarityComparison(stackTrace, otherStackTrace)
}