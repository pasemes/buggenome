package com.buggenome.matcher

import com.buggenome.stacktrace.StackTrace

/**
 * Responsible for matching a stack trace against other(s).
 * User: pasemes
 * Date: 07/05/11
 * Time: 17:43
 */

abstract class JavaStackSimilarityCalculator {

    /**
     * Compute the similarity between two stacks is based on the number of common lines.
     * @return an double value from 0 to 1, indicating the similarity
     */
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double




}