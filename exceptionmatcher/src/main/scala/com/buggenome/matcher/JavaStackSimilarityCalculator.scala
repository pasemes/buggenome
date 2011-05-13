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
     * @param stackTrace a stack that will be compared
     * @param otherStackTrace the other stack to compare to
     * @return an double value from 0 to 1, indicating the similarity
     */
    final def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {

        0.0

        //if()

        //chamar os metodos de calculo de similaridade
        //cada classe de calculo de similaridade pode ser um ator
    }

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
     *
     * TODO put an example for each type of wildcard use
     */
    protected def wildSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double


}

private class JavaStackCompleteMatchSimilarityCalculator extends JavaStackSimilarityCalculator {

    //sempre retorna 1
//    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
//        0.0
//    }
}

class JavaStackTopDownSimilarityCalculator extends JavaStackSimilarityCalculator {

    //exclui complete match (isto e, nunca retorna 1)
//    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
//        0.0
//    }
}

class JavaStackBottomUpSimilarityCalculator extends JavaStackSimilarityCalculator {

    //exclui complete match (isto e, nunca retorna 1)
//    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
//        0.0
//    }
}


class JavaStackChainedSimilarityCalculator extends JavaStackSimilarityCalculator {

    //pode dar match "no meio" (em uma stack chained) da stack e nao so no inicio ou no fim
//    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {
//        0.0
//    }
}