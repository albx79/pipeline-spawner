package com.sky.ukiss.pipelinespawner

import com.sky.ukiss.pipelinespawner.api.{JobCreated, WsMessage, Job => JobData}
import io.fabric8.kubernetes.api.model.Job
import io.fabric8.kubernetes.client.Watcher.Action._
import io.fabric8.kubernetes.client.{KubernetesClient, KubernetesClientException, Watcher}
import org.json4s.{DefaultFormats, Extraction, Formats}
import org.scalatra.atmosphere.{AtmosphereClient, JsonMessage, TextMessage}
import org.slf4j.LoggerFactory
import prickle.Pickle

import scala.collection.JavaConverters._
import scala.collection.mutable

class JobEvents(client: KubernetesClient,
                namespace: String,
               ) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private val jobsCell: mutable.Set[JobData] = mutable.Set()

  private def jobs = jobsCell.seq

  logger.info("Getting initial jobs")

  jobs ++= client.extensions().jobs()
    .inNamespace(namespace)
    .list().getItems.asScala
    .map((j: Job) => JobData(j.getMetadata.getName, j.getMetadata.getLabels.get("app_name")))
    .toSet

  logger.info("Registering watcher with Kubernetes")

  client.extensions().jobs().inNamespace(namespace).watch(new Watcher[Job] {
    override def onClose(cause: KubernetesClientException): Unit = ???

    override def eventReceived(action: Watcher.Action, job: Job): Unit = {
      val name = job.getMetadata.getName
      logger.info("Job changed: " + name)
      val labels = job.getMetadata.getLabels
      val jobData = JobData(name, labels.get("app_name"))

      action match {
        case ADDED | MODIFIED => jobs += jobData
        case DELETED => jobs -= jobData
        case ERROR => logger.error("error about job events", job)
      }

//      implicit val formats: Formats = DefaultFormats
      import scala.concurrent.ExecutionContext.Implicits.global
      val theMessage = Pickle.intoString(JobCreated(jobData))

      AtmosphereClient.broadcastAll(TextMessage(theMessage))
    }
  })

}