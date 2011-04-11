package com.buggenome.persistence

import com.mongodb.casbah.Imports._

/**
 * Contains the drivers configuration and serves as a place for the database connection
 * User: pasemes
 * Date: 03/04/11
 * Time: 16:58
 */
object MongoDriverManager {

    //this connection is already pooled
    private val mongoConn = MongoConnection()("buggenome")

    //list of connections for collections
    def javaStackTraceColl : MongoCollection = mongoConn("java_stack_trace")

}