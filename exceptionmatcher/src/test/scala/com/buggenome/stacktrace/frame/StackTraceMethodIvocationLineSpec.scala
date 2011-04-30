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
            val malformedStackLine : String = "at Class01..java:92)"
            malformedStackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
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

        "Be equal to another when they have same content" in {
            val stackLine1 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("java"), 92)
            val stackLine2 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("java"), 92)
            stackLine1 must beEqualTo(stackLine2)
        }

        "Be different to another when they have different content" in {
            val stackLine1 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("java"), 92)
            var stackLine2 = new StackTraceMethodInvocationLine("Class02", "meth02", Some("StackTr01"), Some("java"), 92)
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceMethodInvocationLine("Class01", "meth03", Some("StackTr01"), Some("java"), 92)
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr02"), Some("java"), 92)
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("scala"), 92)
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("java"), 93)
            stackLine1 must beDifferentFrom(stackLine2)
        }

        "Represent itself in string format resembling the original format thrown by the JVM" in {
            "at Class01.meth02(StackTr01.java:92)" must beEqualTo(new StackTraceMethodInvocationLine("Class01", "meth02", Some("StackTr01"), Some("java"), 92).toString)
            var stackLine = "at Class01.meth02(Unknown Source)"
            stackLine must beEqualTo(stackLine match { case StackTraceMethodInvocationLine(clazz, method, file, language, line)  => new StackTraceMethodInvocationLine(clazz, method, file, language, line).toString})
            stackLine = "at Class01.meth02(Native Method)"
            stackLine must beEqualTo(stackLine match { case StackTraceMethodInvocationLine(clazz, method, file, language, line)  => new StackTraceMethodInvocationLine(clazz, method, file, language, line).toString})
        }
    }
}