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
     * @return a positive number representing the number of similar lines when the parsing is done using a top-down approach<br>
     *         a negative number representing the number of different lines when the parsing is done using a bottom-up approach
     */
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Int




}