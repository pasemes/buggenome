package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Define weights for the different similarity calculation methods.
 * User: pasemes
 * Date: 14/05/11
 * Time: 19:56
 */
trait JavaStackSimilarityCalculationMethodWeights extends JavaStackSimilarityCalculationMethods {

    protected[similaritycalculators] val FULL_SIMILARITY_COMPARISON_WEIGHT = 1
    protected[similaritycalculators] val IGNORING_LINE_SIMILARITY_COMPARISON_WEIGHT = 0.9
    protected[similaritycalculators] val WILDCARD_SIMILARITY_COMPARISON_WEIGHT = 0.8

    abstract override protected[similaritycalculators] def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            FULL_SIMILARITY_COMPARISON_WEIGHT * super.fullSimilarityComparison(stackTrace, otherStackTrace)

    abstract override protected[similaritycalculators] def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            IGNORING_LINE_SIMILARITY_COMPARISON_WEIGHT * super.ignoringLineSimilarityComparison(stackTrace, otherStackTrace)

    abstract override protected[similaritycalculators] def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double =
            WILDCARD_SIMILARITY_COMPARISON_WEIGHT * super.wildcardSimilarityComparison(stackTrace, otherStackTrace)
}