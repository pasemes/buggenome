/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 13/02/11
 * Time: 10:20
 */
package com.buggenome.stacktrace.frame

import util.matching.Regex

/**
 * Represents the frame at the top of the stack which is the execution point at
 * which the stack trace was generated.
 *
 * @param chained an boolean value indicating if the line is prefixed with "Caused by: ".
 *        When this prefix is present it means that the this Exception is chained
 *        with other.
 * @param declaringClass the fully qualified name of the exception class
 * @param message the optional exception message
 * @throws NullPointerException if <tt>declaringClass</tt> is null
 */
class StackTraceTopLine(val chained : Boolean, declaringClass : String, val message : Option[String]) extends StackTraceLine(declaringClass) {

    require(chained != null, "Chained param cannot be null")

    /**
     * Returns a string representation of this stack trace element.  Here
     * is a typical example:
     * <ul>
     * <li>
     *   <tt>"Caused by: com.test.NewEx02: Thrown from meth03"</tt> - Here,
     *   we have a complete stack trace top line with the modifier "Caused by: "
     *   and the exception message "Thrown from meth03". The exception
     *   fully-qualified name is "com.test.NewEx02".
     * <li>
     *   <tt>"com.test.NewEx02"</tt> - As above, but the modifier and the
     *   exception message are missing.
     * </ul>
     */
    override def toString() : String = {
        return (if(chained) "Caused by: " else "") +
                declaringClass + ": " + message.getOrElse(null)
    }

    /**
     * Returns true if the specified object is another
     * <tt>StackTraceTopLine</tt> instance representing the same execution
     * point as this instance.  Two stack trace lines <tt>a</tt> and
     * <tt>b</tt> are equal if and only if:
     * <pre>
     *     a.chained == b.chained &&
     *     a.declaringClass == b.declaringClass &&
     *     a.message == b.message
     * </pre>
     *
     * @param  obj the object to be compared with this stack trace top line.
     * @return true if the specified object is another
     *         <tt>StackTraceTopLine</tt> instance representing the same
     *         top line as this instance.
     */
    override def equals(obj : Any) : Boolean = {  //TODO testar equals
        if(this eq obj.asInstanceOf[AnyRef]) return true
        if(!obj.isInstanceOf[StackTraceTopLine]) return false

        val stackTraceTopLine = obj.asInstanceOf[StackTraceTopLine]

        return this.chained         == stackTraceTopLine.chained &&
               this.declaringClass  == stackTraceTopLine.declaringClass &&
               this.message         == stackTraceTopLine.message
    }

    /**
     * Returns a hash code value for this stack trace top line.
     */
    override def hashCode() : Int = {
        31*declaringClass.hashCode + 31*chained.hashCode + 31*message.hashCode
    }
}

/**
 * Regex for the stack trace top line
 * It has the format: ( [MODIFIER]? [EXCEPTION] ) ( [:] [MESSAGE] )?
 */
//object StackTraceTopLine extends Regex(regex = """^(Caused by:)?\s?([\w$.]+)(:\s[^\r]*)?""")

object StackTraceTopLine {

    //Regex for the stack trace top line
    // It has the format: ( [MODIFIER]? [EXCEPTION] ) ( [:] [MESSAGE] )?
    private val TopLineRE = """^(Caused by:)?\s?([\w$.]+)(:\s[^\r]*)?""".r

    def unapply(topLine : Any) : Option[(Boolean, String, Option[String])] = {
        topLine match {
            case TopLineRE(chained, declaringClass, message) =>
                Some((chained != null, declaringClass, if (message != null) new Some(message.drop(2)) else None)) //the drop(2) called from message removes the ': ' characteres left by the regex
            case _ =>
                None
        }
    }
}