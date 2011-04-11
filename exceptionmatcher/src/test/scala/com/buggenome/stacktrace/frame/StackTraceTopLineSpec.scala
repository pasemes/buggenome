package com.buggenome.stacktrace.frame

/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 13/02/11
 * Time: 10:20
 */
import org.specs.Specification

class StackTraceTopLineSpec extends Specification {

    "A stack top line" should {

        "Return the stack trace line elements from a well formed stack in string format" in {
            val stackLine : String = "Caused by: NewEx02: Thrown from meth03"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceTopLine(chained, exception, message) =>
                    chained     must notBeNull //the value isn't important in this case - the StackTraceTopLine constructor expects only a boolean for this argument
                    exception   must beEqualTo("NewEx02")
                    message     must beEqualTo(": Thrown from meth03")
                    true
                case _ =>
                    false
            }
        }

        "Doesn't match when the stack line isn't well formed" in {
            val stackLine : String = "Caused by: NewEx02:: Thrown from meth03"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceFramesInCommon(numberFramesInCommon) =>
                    false //shouldn't match this string
                case StackTraceMethodInvocationLine(clazz, method, file, language, line) =>
                    false //shouldn't match this string
                case StackTraceTopLine(chained, exception, message) =>
                    false //shouldn't match this string
                case _ =>
                    true //doesn't matched any case
            }
        }
    }
}