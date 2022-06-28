package org.sunbird.managers

import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang.StringUtils
import org.sunbird.common.dto.{Request, Response, ResponseHandler}
import org.sunbird.common.exception.{ClientException, ServerException}
import org.sunbird.common.{JsonUtils, Platform}
import org.sunbird.graph.OntologyEngineContext
import org.sunbird.graph.common.Identifier
import org.sunbird.graph.dac.model.Node
import org.sunbird.graph.nodes.DataNode
import org.sunbird.graph.schema.DefinitionNode
import org.sunbird.graph.utils.{NodeUtil, ScalaJsonUtils}
import org.sunbird.telemetry.logger.TelemetryManager
import org.sunbird.utils.{AssessmentConstants, BranchingUtil, HierarchyConstants}

import java.util
import java.util.concurrent.CompletionException
import java.util.{Optional, UUID}
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

object CommentManager {

	def addComment(request: Request)(implicit ec: ExecutionContext, oec: OntologyEngineContext ):Future[Response]={
		//validateRequest(request)
		println(request)
		DataNode.update(request).map(node => {
			ResponseHandler.OK.putAll(Map("identifier" -> node.getIdentifier.replace(".img", ""), "versionKey" -> node.getMetadata.get("versionKey")).asJava)
		})
	}

}