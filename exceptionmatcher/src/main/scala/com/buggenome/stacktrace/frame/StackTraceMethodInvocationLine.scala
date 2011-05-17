/*
 * Created by IntelliJ IDEA.
 * User: Paulo Sérgio
 * Date: 13/02/11
 * Time: 10:20
 */
package com.buggenome.stacktrace.frame

/**
 * Represents an stack trace method invocation line. Usually the method invocation line
 * in the stack traces are the ones which begins with "at ".
 *
 * @param declaringClass the fully qualified name of the class containing
 *         the execution point represented by the stack trace element
 * @param methodName the name of the method containing the execution point
 *         represented by the stack trace element. If the execution point is
 *         contained in an instance or class initializer, this method will return
 *         the appropriate <i>special method name</i>, <tt>&lt;init&gt;</tt> or
 *         <tt>&lt;clinit&gt;</tt>, as per Section 3.9 of <i>The Java Virtual
 *         Machine Specification</i>.
 * @param fileName the name of the file containing the execution point
 *         represented by the stack trace element, or <tt>null</tt> if
 *         this information is unavailable. Generally, this corresponds
 *         to the <tt>SourceFile</tt> attribute of the relevant <tt>class</tt>
 *         file (as per <i>The Java Virtual Machine Specification</i>, Section
 *         4.7.7).  In some systems, the name may refer to some source code unit
 *         other than a file, such as an entry in source repository.
 * @param language the language into which the method was implemented, according to
 *         the source file extension
 * @param lineNumber the line number of the source line containing the
 *         execution point represented by this stack trace element, or
 *         a negative number if this information is unavailable. A value
 *         of -2 indicates that the method containing the execution point
 *         is a native method.
 * @throws NullPointerException if <tt>declaringClass</tt> or
 *         <tt>methodName</tt> is null
 */
class StackTraceMethodInvocationLine(declaringClass : String, val methodName : String,
                     val fileName : Option[String], val language : Option[String], val lineNumber : Int) extends StackTraceLine(declaringClass) {

    require(methodName != null, "Method name cannot be null")

    /**
     * Returns true if the method containing the execution point
     * represented by this stack trace element is a native method.
     *
     * @return <tt>true</tt> if the method containing the execution point
     *         represented by this stack trace element is a native method.
     */
    def isNativeMethod : Boolean = lineNumber == StackTraceMethodInvocationLine.NATIVE_METHOD_LINE

    /**
     * Returns a string representation of this stack trace element.  The
     * following examples may be regarded as typical:
     * <ul>
     * <li>
     *   <tt>"MyClass.mash(MyClass.java:9)"</tt> - Here, <tt>"MyClass"</tt>
     *   is the <i>fully-qualified name</i> of the class containing the
     *   execution point represented by this stack trace element,
     *   <tt>"mash"</tt> is the name of the method containing the execution
     *   point, <tt>"MyClass.java"</tt> is the source file containing the
     *   execution point, and <tt>"9"</tt> is the line number of the source
     *   line containing the execution poi31*declaringClass.hashCodent.
     * <li>
     *   <tt>"MyClass.mash(MyClass.java)"</tt> - As above, but the line
     *   number is unavailable.
     * <li>
     *   <tt>"MyClass.mash(Unknown Source)"</tt> - As above, but neither
     *   the file name nor the line  number are available.
     * <li>
     *   <tt>"MyClass.mash(Native Method)"</tt> - As above, but neither
     *   the file name nor the line  number are available, and the method
     *   containing the execution point is known to be a native method.
     * </ul>
     * @see    Throwable#printStackTrace()
     */
    override def toString() : String = {
        val methodFullName : String = declaringClass + "." + methodName

        "at " + methodFullName + {
            if(isNativeMethod) {
                "(Native Method)"
            } else {
                fileName match {
                    case Some(fn) if lineNumber >= 0 => "(" + fn + "." + language.get + ":" + lineNumber + ")"
                    case Some(fn) => "("+ fn + "." + language.get + ")"
                    case None => "(Unknown Source)"
                }
            }
        }
    }

    /**
     * Returns true if the specified object is another
     * <tt>StackTraceMethodInvocationLine</tt> instance representing the same execution
     * point as this instance.  Two stack trace lines <tt>a</tt> and
     * <tt>b</tt> are equal if and only if:
     * <pre>
     *     this.getClassName    == methodInvocationLine.getClassName &&
     *     this.getMethodName   == methodInvocationLine.getMethodName &&
     *     this.getFileName     == methodInvocationLine.getFileName &&
     *     this.getLanguage     == methodInvocationLine.getLanguage &&
     *     this.getLineNumber   == methodInvocationLine.getLineNumber
     * </pre>
     *
     * @param  obj the object to be compared with this method invocation line.
     * @return true if the specified object is another
     *         <tt>StackTraceMethodInvocationLine</tt> instance representing the same
     *         method invocation line as this instance.
     */
    override def equals(obj : Any) : Boolean = {
        if(this eq obj.asInstanceOf[AnyRef]) return true
        if(!obj.isInstanceOf[StackTraceMethodInvocationLine]) return false

        val methodInvocationLine = obj.asInstanceOf[StackTraceMethodInvocationLine]

        return equalIgnoringLineNumber(methodInvocationLine) &&
               this.lineNumber      == methodInvocationLine.lineNumber
    }

    /**
     * Compares two stack trace lines (same as equals method), but ignores the invocation line number on comparison.
     */
    def equalIgnoringLineNumber(otherLine: StackTraceMethodInvocationLine) : Boolean = {
         return this.declaringClass  == otherLine.declaringClass &&
                this.methodName      == otherLine.methodName &&
                this.fileName        == otherLine.fileName &&
                this.language        == otherLine.language
    }

    /**
     * Returns a hash code value for this method invocation line.
     */
    override def hashCode() : Int = {
        31*declaringClass.hashCode + 31*methodName.hashCode + 31*fileName.hashCode +
                31*language.hashCode + 31*lineNumber.hashCode
    }
}

/**
 * Companion object.
 */
object StackTraceMethodInvocationLine {

    //Regex for the stack trace lines (except for the top line)
    //It has the format: ( [at] [METHOD] [(] [FILE] [:] [LINE] [)] )*
    private val MethodInvocationLineRE = """at\s([\w$.]+)\.([\w<>$]+)\((Unknown\sSource|Native\sMethod|([\w$]+)\.(scala|java|groovy):([\d]+))\)""".r
    val NATIVE_METHOD_LINE = -2
    val UNKNOWN_SOURCE_LINE = -1

    def unapply(methodInvocationLine : Any) : Option[(String,String,Option[String],Option[String],Int)]  = { //options contentent corresponds to clazz, method, file, language, line

        methodInvocationLine match {
            case MethodInvocationLineRE(clazz, method, parenthesesContent, file, language, line) if parenthesesContent == "Native Method" =>
                Some((clazz, method, None, None, NATIVE_METHOD_LINE))
            case MethodInvocationLineRE(clazz, method, parenthesesContent, file, language, line) if parenthesesContent == "Unknown Source" =>
                Some((clazz, method, None, None, UNKNOWN_SOURCE_LINE))
            case MethodInvocationLineRE(clazz, method, parenthesesContent, file, language, line) =>
                Some((clazz, method, new Some(file), new Some(language), line.toInt))
            case _ =>
                None
        }
    }
}
