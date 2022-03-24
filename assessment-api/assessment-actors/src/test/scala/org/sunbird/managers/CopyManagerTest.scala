package org.sunbird.managers

import java.util
import akka.actor.Props
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import org.sunbird.cloudstore.StorageService
import org.sunbird.common.HttpUtil
import org.sunbird.common.dto.ResponseHandler
import org.sunbird.common.dto.{Property, Request, Response}
import org.sunbird.common.exception.{ClientException, ResponseCode}
import org.sunbird.graph.dac.model.{Node, SearchCriteria}
import org.sunbird.graph.utils.ScalaJsonUtils
import org.sunbird.graph.{GraphService, OntologyEngineContext}
import org.sunbird.kafka.client.KafkaClient

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CopyManagerTest extends FlatSpec with MockFactory {

  "CopyManager" should "return copied node identifier when content is copied" in {
    implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
    implicit val ss: StorageService = mock[StorageService]
    val graphDB = mock[GraphService]
    println("NODE: " + getNode().getMetadata)
    (oec.graphService _).expects().returns(graphDB).anyNumberOfTimes()
    (graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects("domain", "do_1234", false, *).returns(Future(getNode())).anyNumberOfTimes()
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    /*
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    //getNodeByUniqueId
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response())) //instructions, outcomedeclaration
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    //getNodeByUniqueIds
    //addNode
    //saveExternalProps
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response())) //hierarchy
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response())) //solutions, body, editorState, interactions, hints, responseDeclaration, media, answer, instructions
    //getNodeByUniqueId
    //getNodeByUniqueId
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response())) //hierarchy
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    //getNodeByUniqueIds
    //addNode
    //saveExternalProps
    //getNodeByUniqueIds
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    (graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    //getNodeByUniqueId
    //getNodeByUniqueIds
    //upsertNode
    //updateExternalProps}

     */


    //(graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(getNode())).atLeastOnce()
    //(graphDB.addNode(_: String, _: Node)).expects(*, *).returns(Future(getCopiedNode()))
    //(graphDB.saveExternalProps(_: Request)).expects(*).returns(Future(new Response())).anyNumberOfTimes
    //(graphDB.readExternalProps(_: Request, _: List[String])).expects(*, *).returns(Future(new Response()))
    //(graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(getDefinitionNode_channel()))
    //(graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(getDefinitionNode_channel()))
    //(graphDB.addNode(_: String, _: Node)).expects(*, *).returns(Future(getCopiedNode()))
    //(graphDB.getNodeByUniqueId(_: String, _: String, _: Boolean, _: Request)).expects(*, *, *, *).returns(Future(getCopiedNode()))
    //(graphDB.upsertNode(_: String, _: Node, _: Request)).expects(*, *, *).returns(Future(getCopiedNode()))
    //(graphDB.getNodeProperty(_: String, _: String, _: String)).expects(*, *, *).returns(Future(new Property("versionKey", new org.neo4j.driver.internal.value.StringValue("1234"))))
    CopyManager.copy(getCopyRequest()).map(resp => {
      println("RESPONSE: " + resp)
      assert(resp != null)
      assert(resp.getResponseCode == ResponseCode.OK)
      //assert(resp.getResult.get("node_id").asInstanceOf[util.HashMap[String, AnyRef]].get("do_1234").asInstanceOf[String] == "do_1234_copy")
    })
  }

  /*
  "Required property not sent" should "return client error response for" in {
    implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
    implicit val ss = mock[StorageService]
    val request = getInvalidCopyRequest_2()
    request.getContext.put("identifier", "do_1234")
    request.getRequest.putAll(mapAsJavaMap(Map("identifier" -> "do_1234")))
    val exception = intercept[ClientException] {
      CopyManager.validateRequest(request)
    }
    exception.getMessage shouldEqual "Please provide valid value for List(createdBy)"
  }

   */

  /*
  "Shallow Copy along with copy scheme" should "return client error response for copy content" in {
      implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
      implicit val ss = mock[StorageService]
      val request = getInvalidCopyRequest_1()
      request.getContext.put("identifier","do_1234")
      request.getRequest.putAll(mapAsJavaMap(Map("identifier" -> "do_1234")))
      val exception = intercept[ClientException] {
          CopyManager.validateRequest(request)
      }
      exception.getMessage shouldEqual "Content can not be shallow copied with copy scheme."
  }

  "Wrong CopyScheme sent" should "return client error response for copy content" in {
      implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
      implicit val ss = mock[StorageService]
      val request = getInvalidCopyRequest_3()
      request.getContext.put("identifier","do_1234")
      request.getRequest.putAll(mapAsJavaMap(Map("identifier" -> "do_1234")))
      val exception = intercept[ClientException] {
          CopyManager.validateRequest(request)
      }
      exception.getMessage shouldEqual "Invalid copy scheme, Please provide valid copy scheme"
  }

   */

  /*
  "Valid scheme type update" should "should populate new contentType in metadata" in {
      implicit val oec: OntologyEngineContext = mock[OntologyEngineContext]
      implicit val ss = mock[StorageService]
      val request = getInvalidCopyRequest_3()
      request.getContext.put("identifier","do_1234")
      request.getRequest.putAll(mapAsJavaMap(Map("identifier" -> "do_1234")))
      val metadata = new util.HashMap[String,Object]()
      CopyManager.updateToCopySchemeContentType(getValidCopyRequest_1(), "TextBook", metadata)
      assert(MapUtils.isNotEmpty(metadata))
  }

   */


  /*
  private def getNode(): Node = {
    val node = new Node()
    node.setGraphId("domain")
    node.setIdentifier("do_1234")
    node.setId(1234)
    node.setNodeType("DATA_NODE")
    node.setObjectType("QuestionSet")
    node.setMetadata(new util.HashMap[String, AnyRef]() {
      {
        put("code", "xyz")
        put("allowSkip", "Yes")
        put("containsUserData", "No")
        put("channel", "in.ekstep")
        /*
        put("language", new util.ArrayList[String]() {
          {
            add("English")
          }
        })

         */
        put("language", Array("do_5678"))
        put("showHints", "No")
        put("mimeType", "application/vnd.sunbird.questionset")
        put("createdOn", "2022-03-16T14:35:11.040+0530")
        put("objectType", "QuestionSet")
        put("primaryCategory", "Observation")
        put("contentDisposition", "inline")
        put("contentEncoding", "gzip")
        put("lastUpdatedOn", "2022-03-16T14:38:51.287+0530")
        put("generateDIALCodes", "No")
        put("showSolutions", "No")
        put("allowAnonymousAccess", "Yes")
        put("identifier", "do_1234")
        put("lastStatusChangedOn", "2022-03-16T14:35:11.040+0530")
        put("requiresSubmit", "No")
        put("visibility", "Default")
        put("showTimer", "No")
        /*
        put("childNodes", new util.ArrayList[String]() {
          {
            add("do_5678")
          }
        })

         */
        put("childNodes", Array("do_5678"))
        put("setType", "materialised")
        //put("version", 1.asInstanceOf[Number])
        put("showFeedback", "No")
        put("versionKey", "1234")
        put("license", "CC BY 4.0")
        //put("depth", 0.asInstanceOf[Number])
        //put("compatibilityLevel", 5.asInstanceOf[Number])
        put("allowBranching", "No")
        put("navigationMode", "non-linear")
        put("name", "CopyQuestionSet")
        put("shuffle", "true")
        put("status", "Live")
        /*
        put("children", new util.ArrayList[util.Map[String, AnyRef]]() {
          {
            add(getChildNode("do_5678").getMetadata)
          }
        })

         */
      }
    })
    node
  }


   */
  private def getNode(): Node = {
    val node = new Node("domain", "DATA_NODE", "QuestionSet")
    node.setGraphId("domain")
    node.setIdentifier("do_1234")
    node.setId(1234)
    node.setNodeType("DATA_NODE")
    node.setObjectType("QuestionSet")
    node.setMetadata(mapAsJavaMap(Map(
      "code" -> "xyz",
      "allowSkip" -> "Yes",
      "containsUserData" -> "No",
      //"channel" -> "in.ekstep",
      //"language" -> Array("English"),
      "showHints" -> "No",
      "mimeType" -> "application/vnd.sunbird.questionset",
      "createdOn" -> "2022-03-16T14:35:11.040+0530",
      "objectType" -> "QuestionSet",
      "primaryCategory" -> "Observation",
      "contentDisposition" -> "inline",
      "contentEncoding" -> "gzip",
      "lastUpdatedOn" -> "2022-03-16T14:38:51.287+0530",
      "generateDIALCodes" -> "No",
      "showSolutions" -> "No",
      "allowAnonymousAccess" -> "Yes",
      "identifier" -> "do_1234",
      "lastStatusChangedOn" -> "2022-03-16T14:35:11.040+0530",
      "requiresSubmit" -> "No",
      "visibility" -> "Default",
      "showTimer" -> "No",
      //"childNodes" -> Array("do_5678"),
      "setType" -> "materialised",
      //"version", 1.asInstanceOf[Number],
      "showFeedback" -> "No",
      "versionKey" -> "1234",
      "license" -> "CC BY 4.0",
      //"depth"-> 0.asInstanceOf[Number],
      //"compatibilityLevel"-> 5.asInstanceOf[Number],
      "allowBranching" -> "No",
      "navigationMode" -> "non-linear",
      "name" -> "CopyQuestionSet",
      "shuffle" -> true.asInstanceOf[AnyRef],
      "status" -> "Live"
    )))
    node
  }

  private def getChildNode(identifier: String): Node = {
    val node = new Node()
    node.setGraphId("domain")
    node.setIdentifier(identifier)
    node.setNodeType("DATA_NODE")
    node.setObjectType("Question")
    node.setMetadata(new util.HashMap[String, AnyRef]() {
      {
        put("parent", "do_1234")
        put("identifier", identifier)
        put("mimeType", "application/vnd.sunbird.question")
        put("status", "Live")
        put("primaryCategory", "Slider")
        put("objectType", "Question")
        put("visibility", "Default")
        put("code", "abc")
        put("versionKey", "1234")
        put("answer", "answer")
      }
    })
    node
  }

  private def getCopiedNode(): Node = {
    val node = new Node()
    node.setIdentifier("do_1234_copy")
    node.setNodeType("DATA_NODE")
    node.setObjectType("QuestionSet")
    node.setGraphId("domain")
    node.setMetadata(new util.HashMap[String, AnyRef]() {
      {
        put("identifier", "do_1234_copy")
        put("mimeType", "application/vnd.sunbird.questionset")
        put("status", "Draft")
        put("objectType", "QuestionSet")
        put("primaryCategory", "Observation")
        put("name", "ShikshaLokam-QS")
        put("code", "xyz")
        put("versionKey", "1234")
        put("createdBy", "ShikshaLokam")
        put("name", "ShikshaLokam-QS")
        put("childNodes", new util.ArrayList[String]() {
          {
            add("do_5678")
          }
        })
        put("children", new util.ArrayList[util.Map[String, AnyRef]]() {
          {
            add(getChildNode("do_5678").getMetadata)
          }
        })
      }
    })
    node
  }

  private def getCopyRequest(): Request = {
    val request = new Request()
    request.setContext(new util.HashMap[String, AnyRef]() {
      {
        put("graph_id", "domain")
        put("version", "1.0")
        put("objectType", "QuestionSet")
        put("schemaName", "questionset")

      }
    })
    request.setOperation("copyQuestionSet")
    request.setObjectType("QuestionSet")
    request.putAll(new util.HashMap[String, AnyRef]() {
      {
        put("identifier", "do_1234")
        put("createdBy", "ShikshaLokam")
        put("createdFor", Array("ShikshaLokam"))
        put("name", "ShikshaLokam-QS")
        put("copyType","deep")
        put("mode","")
      }
    })
    request
  }

  private def getInvalidCopyRequest_1(): Request = {
    val request = new Request()
    request.setContext(new util.HashMap[String, AnyRef]() {
      {
        put("graph_id", "domain")
        put("version", "1.0")
        put("objectType", "Content")
        put("schemaName", "questionset")
        put("copyScheme", "TextBookToCourse")
      }
    })
    request.setObjectType("Content")
    request.putAll(new util.HashMap[String, AnyRef]() {
      {
        put("createdBy", "EkStep")
        put("createdFor", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("organisation", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("copyType", "shallow")
      }
    })
    request
  }

  private def getInvalidCopyRequest_2(): Request = {
    val request = new Request()
    request.setContext(new util.HashMap[String, AnyRef]() {
      {
        put("graph_id", "domain")
        put("version", "1.0")
        put("objectType", "QuestionSet")
        put("schemaName", "questionset")
        put("copyScheme", "TextBookToCourse")
      }
    })
    request.setObjectType("Content")
    request.putAll(new util.HashMap[String, AnyRef]() {
      {
        put("createdFor", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("organisation", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("framework", "DevCon-NCERT")
      }
    })
    request
  }

  private def getInvalidCopyRequest_3(): Request = {
    val request = new Request()
    request.setContext(new util.HashMap[String, AnyRef]() {
      {
        put("graph_id", "domain")
        put("version", "1.0")
        put("objectType", "QuestionSet")
        put("schemaName", "questionset")
        put("copyScheme", "TextBookToCurriculumCourse")
      }
    })
    request.setObjectType("QuestionSet")
    request.putAll(new util.HashMap[String, AnyRef]() {
      {
        put("createdBy", "ShikshaLokam")
        put("createdFor", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("organisation", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("framework", "DevCon-NCERT")
      }
    })
    request
  }

  private def getValidCopyRequest_1(): Request = {
    val request = new Request()
    request.setContext(new util.HashMap[String, AnyRef]() {
      {
        put("graph_id", "domain")
        put("version", "1.0")
        put("objectType", "QuestionSet")
        put("schemaName", "questionset")
        put("copyScheme", "TextBookToCourse")
      }
    })
    request.setObjectType("QuestionSet")
    request.putAll(new util.HashMap[String, AnyRef]() {
      {
        put("createdBy", "ShikshaLokam")
        put("createdFor", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("organisation", new util.ArrayList[String]() {
          {
            add("ShikshaLokam")
          }
        })
        put("framework", "DevCon-NCERT")
      }
    })
    request
  }

  def getDefinitionNode_channel(): Node = {
    val node = new Node()
    node.setIdentifier("obj-cat:learning-resource_content_in.ekstep")
    node.setNodeType("DATA_NODE")
    node.setObjectType("QuestionSet")
    node.setGraphId("domain")
    node.setMetadata(mapAsJavaMap(
      ScalaJsonUtils.deserialize[Map[String, AnyRef]]("{\n    \"objectCategoryDefinition\": {\n      \"name\": \"Learning Resource\",\n      \"description\": \"QuestionSet Playlist\",\n      \"categoryId\": \"obj-cat:learning-resource\",\n      \"targetObjectType\": \"QuestionSet\",\n      \"objectMetadata\": {\n        \"config\": {},\n        \"schema\": {\n          \"required\": [\n            \"author\",\n            \"copyright\",\n        \"audience\"\n          ],\n          \"properties\": {\n            \"audience\": {\n              \"type\": \"array\",\n              \"items\": {\n                \"type\": \"string\",\n                \"enum\": [\n                  \"Student\",\n                  \"Teacher\"\n                ]\n              },\n              \"default\": [\n                \"Student\"\n              ]\n            },\n            \"mimeType\": {\n              \"type\": \"string\",\n              \"enum\": [\n                \"application/pdf\"\n              ]\n            }\n          }\n        }\n      }\n    }\n  }")))
    node
  }

  def getDefinitionNode(): Node = {
    val node = new Node()
    node.setIdentifier("obj-cat:learning-resource_content_all")
    node.setNodeType("DATA_NODE")
    node.setObjectType("QuestionSet")
    node.setGraphId("domain")
    node.setMetadata(mapAsJavaMap(
      ScalaJsonUtils.deserialize[Map[String, AnyRef]]("{\n    \"objectCategoryDefinition\": {\n      \"name\": \"Learning Resource\",\n      \"description\": \"QuestionSet Playlist\",\n      \"categoryId\": \"obj-cat:learning-resource\",\n      \"targetObjectType\": \"QuestionSet\",\n      \"objectMetadata\": {\n        \"config\": {},\n        \"schema\": {\n          \"required\": [\n            \"author\",\n            \"copyright\",\n         \"audience\"\n          ],\n          \"properties\": {\n            \"audience\": {\n              \"type\": \"array\",\n              \"items\": {\n                \"type\": \"string\",\n                \"enum\": [\n                  \"Student\",\n                  \"Teacher\"\n                ]\n              },\n              \"default\": [\n                \"Student\"\n              ]\n            },\n            \"mimeType\": {\n              \"type\": \"string\",\n              \"enum\": [\n                \"application/pdf\"\n              ]\n            }\n          }\n        }\n      }\n    }\n  }")))
    node
  }
}
