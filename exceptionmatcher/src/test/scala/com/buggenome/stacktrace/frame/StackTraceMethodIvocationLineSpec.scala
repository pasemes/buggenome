package com.buggenome.stacktrace.frame

import org.specs.Specification

/*
* Created by IntelliJ IDEA.
* User: Paulo Sérgio
* Date: 16/02/11
* Time: 20:55
*/
class StackTraceMethodIvocationLineSpec extends Specification {

    "A stack trace method invocation line" should {

        "Return stack trace line elements from a well formed stack in string format" in {
            val stackLine : String = "at Class01.meth02(StackTr01.java:92)"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceMethodInvocationLine(clazz, method, file, language, line) =>
                    clazz        must beEqualTo("Class01")
                    method       must beEqualTo("meth02")
                    file.get     must beEqualTo("StackTr01")
                    language.get must beEqualTo("java")
                    line         must beEqualTo(92)
                    true
                case _ =>
                    false
            }
        }

        "Doesn't match when the stack line isn't well formed" in {
            val stackLine : String = "at Class01..java:92)"
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