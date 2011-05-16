package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Defines approaches to calculate the similarity between
 * User: pasemes
 * Date: 14/05/11
 * Time: 22:27
 */
trait JavaStackSimilarityCalculationMethods {

     /**
     * Calculates the stacks similarity fully comparing each of its lines.
     */
    protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double

    /**
     * Calculates the stacks similarity ignoring the line number of the method calls on the stack when comparing each of its lines.
     */
    protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double

    /**
     * Calculates the stacks similarity ignoring class definition/methods names/exceptions messages<br/>
     *      when a wildcard is found in the corresponding part of a stack trace line.
     */
    protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double
}