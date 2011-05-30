package com.buggenome.matcher.similaritycalculators

import org.specs.Specification
import com.buggenome.stacktrace.StackTrace

/**
 * User: pasemes
 * Date: 21/05/11
 * Time: 16:34
 */
class JavaStackSimilarityCalculationMethodWeightsSpec extends Specification {

    "A java stack similarity calculator method weights" should {

        "Modify the influence of the similarity calculator methods" in {
            class JavaStackSimilarityCalculatorStub extends JavaStackSimilarityCalculator {
                protected def fullSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 1
                protected def ignoringLineSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 1
                protected def wildcardSimilarityComparison(stackTrace : StackTrace, otherStackTrace : StackTrace) : Double = 1
            }
            val weightedSimilarityCalculator = new JavaStackSimilarityCalculatorStub with JavaStackSimilarityCalculationMethodWeights
            weightedSimilarityCalculator.computeSimilarity(new StackTrace, new StackTrace) must beEqualTo(0.9) //(1x1 + 1x0.9 + 1x0.8)/3 = 0.9
        }
    }
}