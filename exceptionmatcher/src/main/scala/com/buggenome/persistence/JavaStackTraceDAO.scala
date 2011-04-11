package com.buggenome.persistence

import com.mongodb.casbah.Imports._
import com.buggenome.stacktrace.StackTrace
import com.buggenome.stacktrace.frame.{StackTraceFramesInCommon, StackTraceMethodInvocationLine, StackTraceTopLine}

/**
  * User: pasemes
  * Date: 01/04/11
  * Time: 21:38
  */
object JavaStackTraceDAO {

    //database fields for the JavaStackTrace collection. the structure is sketched below: //TODO complementar este comentario
    // + document_root
    //      |- FIELD_STACKTRACE_COMMON_FRAMES
    //      |- FIELD_STACKTRACE_METHOD_INVOCATION_LINES
    //
    private val FIELD_STACKTRACE_COMMON_FRAMES            =   "common_frames"
    private val FIELD_STACKTRACE_METHOD_INVOCATION_LINES  =   "method_invocation_lines"
    private val FIELD_STACKTRACE_RESULTED_IN_STACK        =   "resulted_in_stack"
    private val FIELD_TOPLINE_CHAINED                     =   "chained"
    private val FIELD_TOPLINE_EXCEPTION_CLASS             =   "exception_class"
    private val FIELD_TOPLINE_MESSAGE                     =   "message"
    private val FIELD_METHODLINE_CLASS                    =   "class"
    private val FIELD_METHODLINE_METHOD                   =   "method"
    private val FIELD_METHODLINE_FILE                     =   "file"
    private val FIELD_METHODLINE_LANGUAGE                 =   "language"
    private val FIELD_METHODLINE_LINENUMBER               =   "line"

    //the object for connecting to the database
    private val mongoColl = MongoDriverManager.javaStackTraceColl

    /**
     * Inserts an StackTrace into the DB.
     */
    def insert(stackTrace : StackTrace) {
        mongoColl.insert(buildDocument(stackTrace))
    }


    /**
     *  Search for StackTraces which have the top line passed as parameter.
     *  @param
     */
    def findByStackTopLine(stackTopLine : StackTraceTopLine, depth : Int) : Iterator[StackTrace] = {

        val orConditionsBuilder = MongoDBList.newBuilder //for each stack invocation line parsed below

        //the 'for' below builds the query conditions, considering the recursive embedded documents (chained stacks) placed in the FIELD_STACKTRACE_RESULTED_IN_STACK key
        //for instance, with depth = 1 we have the following query conditions for the $or operator:
        //[{"exception_class":"NewEx03","message":"Thrown from meth04"},{"resulted_in_stack.exception_class":"NewEx03","resulted_in_stack.message":"Thrown from meth04"}]
        //for each depth we prefix the field we want to search with a FIELD_STACKTRACE_RESULTED_IN_STACK key
        //really really not beautiful, but that is the only way i've figured of how to search in recursive fields like the FIELD_STACKTRACE_RESULTED_IN_STACK
        for(i <- 0 to depth) {
            val topLineBuilder = MongoDBObject.newBuilder
            topLineBuilder +=  ((FIELD_STACKTRACE_RESULTED_IN_STACK + ".") * i) + FIELD_TOPLINE_EXCEPTION_CLASS ->  stackTopLine.declaringClass
            topLineBuilder +=  ((FIELD_STACKTRACE_RESULTED_IN_STACK + ".") * i) + FIELD_TOPLINE_MESSAGE         ->  stackTopLine.message
            orConditionsBuilder += topLineBuilder.result()
        }

        mongoColl.find(MongoDBObject("$or" -> orConditionsBuilder.result())).map(buildStackTrace(_, null))
    }

    //##################################################################################################################
    //BELOW METHODS FOR TRANSLATING FROM/TO STACKTRACE OBJECTS TO/FROM JSON-DOCUMENTS
    //##################################################################################################################

    /**
     * Build a MongoDB document recursively
     */
    private def buildDocument(stackTrace : StackTrace) : MongoDBObject = {
        val stackBuilder = MongoDBObject.newBuilder //the document root
        val invocationLineListBuilder = MongoDBList.newBuilder //for each stack invocation line parsed below

        stackTrace.getLinesNotChained.foreach( _ match {

            case topLine : StackTraceTopLine => {
                stackBuilder += FIELD_TOPLINE_CHAINED         ->  topLine.chained
                stackBuilder += FIELD_TOPLINE_EXCEPTION_CLASS ->  topLine.declaringClass
                stackBuilder += FIELD_TOPLINE_MESSAGE         ->  topLine.message//.getOrElse(null)
            }

            //it's important to notice that we could distinguish the frames in common lines not inserting them (they can be retrieved from the caused-by stack)
            //but we still insert them to facilitate queries; this affects how we have to retrieve the stack trace back (see buildStackTrace method)
            case invocationLine : StackTraceMethodInvocationLine => {
                //the method invocation lines are inserted as an embedded document, so we create another builder
                val invocationLineBuilder = MongoDBObject.newBuilder

                invocationLineBuilder += FIELD_METHODLINE_CLASS         ->  invocationLine.declaringClass
                invocationLineBuilder += FIELD_METHODLINE_METHOD        ->  invocationLine.methodName
                invocationLineBuilder += FIELD_METHODLINE_FILE          ->  invocationLine.fileName//.getOrElse(null)
                invocationLineBuilder += FIELD_METHODLINE_LANGUAGE      ->  invocationLine.language//.getOrElse(null)
                invocationLineBuilder += FIELD_METHODLINE_LINENUMBER    ->  invocationLine.lineNumber

                invocationLineListBuilder += invocationLineBuilder.result
            }
        })

        if(stackTrace.hasFramesInCommon) stackBuilder += FIELD_STACKTRACE_COMMON_FRAMES ->  stackTrace.getNumberOfCommonFrames
        stackBuilder += FIELD_STACKTRACE_METHOD_INVOCATION_LINES ->  invocationLineListBuilder.result

        stackTrace.getResultingStack.foreach(
            stackBuilder += FIELD_STACKTRACE_RESULTED_IN_STACK ->  buildDocument(_)
        )

        stackBuilder.result()
    }

    /**
     *  Build a StackTrace from a MongoDBObject. It considers the chained stacks, calling this method again
     */
    private def buildStackTrace(stackTraceDBObject : MongoDBObject, causedByStack : StackTrace) : StackTrace = {

        val stackTrace = new StackTrace

        stackTrace.addFrame(new StackTraceTopLine(
            chained         = stackTraceDBObject.getAs[Boolean](FIELD_TOPLINE_CHAINED).get,
            declaringClass  = stackTraceDBObject.getAs[String](FIELD_TOPLINE_EXCEPTION_CLASS).get,
            message         = stackTraceDBObject.getAs[String](FIELD_TOPLINE_MESSAGE)))

        stackTraceDBObject.getAs[BasicDBList](FIELD_STACKTRACE_METHOD_INVOCATION_LINES).get.foreach( anyRefObject =>  { //since it is a list it gives us a generic object in the foreach method
            val invocationLineDBObject = anyRefObject.asInstanceOf[BasicDBObject] // we have to convert to the correct object
            stackTrace.addFrame(new StackTraceMethodInvocationLine(
                declaringClass  = invocationLineDBObject.getAs[String](FIELD_METHODLINE_CLASS).get,
                methodName      = invocationLineDBObject.getAs[String](FIELD_METHODLINE_METHOD).get,
                fileName        = invocationLineDBObject.getAs[String](FIELD_METHODLINE_FILE),
                language        = invocationLineDBObject.getAs[String](FIELD_METHODLINE_LANGUAGE),
                lineNumber      = invocationLineDBObject.getAs[Int](FIELD_METHODLINE_LINENUMBER).get))
        })

        stackTrace.causedBy(causedByStack)
        stackTraceDBObject.getAs[Int](FIELD_STACKTRACE_COMMON_FRAMES).foreach( framesInCommon => {
            // a bit ugly, but since all lines are persisted in database (even the common ones, to facilitate queries) we have to remove the commons
            stackTrace.removeLastLines(framesInCommon)
            // and add them in the "right way" from the StackTrace class perspective
            stackTrace.addFrame(new StackTraceFramesInCommon(framesInCommon, causedByStack))
        })

        stackTraceDBObject.getAs[DBObject](FIELD_STACKTRACE_RESULTED_IN_STACK).foreach(
            stackTraceDBObject => stackTrace.resultedIn(buildStackTrace(stackTraceDBObject,stackTrace))
        )

        stackTrace
    }
}