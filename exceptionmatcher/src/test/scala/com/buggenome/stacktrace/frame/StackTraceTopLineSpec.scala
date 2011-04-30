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
                    chained         must be(true) //the value isn't important in this case - the StackTraceTopLine constructor expects only a boolean for this argument
                    exception       must beEqualTo("NewEx02")
                    message.get     must beEqualTo("Thrown from meth03")
                    true
                case _ =>
                    false
            }
        }

        "Doesn't match when the stack line isn't well formed" in {
            val stackLine : String = "Caused by: NewEx02:: Thrown from meth03"
            stackLine must beLike {//the boolean values inside cases are captured by the "must beLike" Steps matcher
                case StackTraceTopLine(chained, exception, message) =>
                    false //shouldn't match this string
                case _ =>
                    true //doesn't matched any case
            }
        }

        "Be equal to another when they have same content" in {
            val stackLine1 = new StackTraceTopLine(true, "NewEx02", Some("Thrown from meth03"))
            val stackLine2 = new StackTraceTopLine(true, "NewEx02", Some("Thrown from meth03"))
            stackLine1 must beEqualTo(stackLine2)
        }

        "Be different to another when they have different content" in {
            val stackLine1 = new StackTraceTopLine(true, "NewEx02", Some("Thrown from meth03"))
            var stackLine2 = new StackTraceTopLine(false, "NewEx02", Some("Thrown from meth03"))
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceTopLine(true, "NewEx03", Some("Thrown from meth03"))
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceTopLine(true, "NewEx02", Some("Thrown from meth04"))
            stackLine1 must beDifferentFrom(stackLine2)

            stackLine2 = new StackTraceTopLine(true, "NewEx02", None)
            stackLine1 must beDifferentFrom(stackLine2)
        }

        "Represent itself in string format resembling the original format thrown by the JVM" in {
            "Caused by: com.NewEx02: Thrown from meth03" must beEqualTo(new StackTraceTopLine(true, "com.NewEx02", Some("Thrown from meth03")).toString)
            "com.NewEx02: Thrown from meth03" must beEqualTo(new StackTraceTopLine(false, "com.NewEx02", Some("Thrown from meth03")).toString)
            "com.NewEx02" must beEqualTo(new StackTraceTopLine(false, "com.NewEx02", None).toString)
        }
    }
}