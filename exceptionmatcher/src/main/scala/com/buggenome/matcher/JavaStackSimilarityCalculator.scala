package com.buggenome.matcher

import com.buggenome.stacktrace.StackTrace

/**
 * Responsible for matching a stack trace against other(s).
 * User: pasemes
 * Date: 07/05/11
 * Time: 17:43
 */

trait JavaStackSimilarityCalculator {

    /**
     * Compute the similarity between two stacks is based on the number of common lines.
     * @return an double value from 0 to 1, indicating the similarity
     */
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double
}

class JavaStackCompleteMatchSimilarityCalculator extends JavaStackSimilarityCalculator {

    //sempre retorna 1
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {

    }
}

class JavaStackTopDownSimilarityCalculator extends JavaStackSimilarityCalculator {

    //exclui complete match (isto e, nunca retorna 1)
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {

    }
}

class JavaStackBottomUpSimilarityCalculator extends JavaStackSimilarityCalculator {

    //exclui complete match (isto e, nunca retorna 1)
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {

    }
}


class JavaStackChainedSimilarityCalculator extends JavaStackSimilarityCalculator {

    //pode dar match "no meio" (em uma stack chained) da stack e nao so no inicio ou no fim
    def computeSimilarity(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = {

    }

}