package com.buggenome.matcher

import com.buggenome.stacktrace.StackTrace
import collection.mutable.ListBuffer
import scala.Array
import similaritycalculators._
import collection.immutable.ListSet

/**
 * Responsible for matching a stack trace against a set of stacks.
 * @param dependencies must contain an instantiated array with JavaStackSimilarityCalculators to be used by the matcher.<br/>
 *          The order of the calculators determine the order of the matching results. The first N matches will be<br/>
 *          based on the first calculator, the second M matches based on the second and so on ...
 * User: pasemes
 * Date: 07/05/11
 * Time: 20:04
 */
class JavaStackMatcher(val dependencies : { val similarityCalculators : Array[JavaStackSimilarityCalculator] } ) {

    /**
     * Computes similar stacks to one stack from a set of stacks.
     * @param stackTrace the stack which will serve as a model to compare to the stacks in the set
     * @param stackTracesSet a set of stacks from which similar stacks will be identified
     */
    def computeSimilarStacks(stackTrace : StackTrace, stackTracesSet : ListSet[StackTrace]) : List[StackTrace] = {
        var stackTracesList = stackTracesSet.toList
        val orderedSimilarStacks = new ListBuffer[StackTrace]()
        dependencies.similarityCalculators.foreach{ similarityCalculator =>
            val mappedSimilarity = stackTracesList.map{ //maps to a tuple (stackTrace, similarityValue)
                    similarStack => (similarStack, similarityCalculator.computeSimilarity(stackTrace, similarStack))}
            val orderedMappedSimilarity = mappedSimilarity.sortWith{ //sorts the stacks based on the similarityValue
                    (stackSimilarityTuple1, stackSimilarityTuple2) => stackSimilarityTuple1._2 < stackSimilarityTuple2._2}
            orderedSimilarStacks ++= orderedMappedSimilarity.filter(_._2 != 0).map(_._1) //removes the stacks which don't have any degree of similarity, mapping it back to a list of stacks
            stackTracesList = orderedMappedSimilarity.filter(_._2 == 0).map(_._1) //keeps only the stacks which don't have any degree of similarity to be processed by the next calculator
        }
        orderedSimilarStacks.toList
    }
}

/**
  * Configures a JavaStackMatcher with similarity calculators sorted by result relevance, i.e., the first calculator used by the matcher
  *      use the most precise and restrictive algorithm for similarity calculation and the last is the least precise/restrictive.
  */
object JavaStackMatcherPrecisionConfig {
    lazy val similarityCalculators : Array[JavaStackSimilarityCalculator] = {
        val calculators = new Array[JavaStackSimilarityCalculator](4)
        calculators(0) = new JavaStackCompleteMatchSimilarityCalculator with JavaStackSimilarityCalculationMethodWeights
        calculators(1) = new JavaStackChainedSimilarityCalculator with JavaStackSimilarityCalculationMethodWeights
        calculators(2) = new JavaStackTopDownSimilarityCalculator with JavaStackSimilarityCalculationMethodWeights
        calculators(3) = new JavaStackBottomUpSimilarityCalculator with JavaStackSimilarityCalculationMethodWeights
        calculators
    }
    lazy val javaStackMatcher : JavaStackMatcher = new JavaStackMatcher(this)
}