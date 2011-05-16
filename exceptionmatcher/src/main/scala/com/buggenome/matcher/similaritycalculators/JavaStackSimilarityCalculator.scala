package com.buggenome.matcher.similaritycalculators

import com.buggenome.stacktrace.StackTrace

/**
 * Responsible for matching a stack trace against other(s).
 * User: pasemes
 * Date: 07/05/11
 * Time: 17:43
 */
abstract class JavaStackSimilarityCalculator extends JavaStackSimilarityCalculationMethods {

    /**
     * Compute the similarity between two stacks is based on the number of common lines, using different calculation
     *      methods and using the with the best result.
     * @param stackTrace a stack that will be compared
     * @param otherStackTrace the other stack to compare to
     * @return an double value from 0 to 1, indicating the similarity
     */
    final def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
        println("JavaStackSimilarityCalculator->computeSimilarity")
        this.wildcardSimilarityComparison(null, null)
        0.0

        //if()

        //chamar os metodos de calculo de similaridade
        //cada classe de calculo de similaridade pode ser um ator
    }

}