package partitioners

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import scala.util.hashing.MurmurHash3

class GroupPartitioner extends Partitioner {
  val partitionsPerGroup = 5

  override def configure(configs: java.util.Map[String, _]): Unit = {
    // Configure your partitioner if needed
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int = {
    // Split the key by underscore
    val parts = key.toString.split("_")
    val groupKey = parts(0).toInt // Assuming the first part is orgId
    val inGroupKey = parts(1).toInt // Assuming the second part is id

    val numPartitions = cluster.partitionCountForTopic(topic)
    val numGroups = numPartitions / partitionsPerGroup

    // Partitioning logic
    val group = Math.abs(groupKey.hashCode % numGroups)
    val partitionWithinGroup = Math.abs(inGroupKey.hashCode % partitionsPerGroup)

    group * partitionsPerGroup + partitionWithinGroup
  }
  override def close(): Unit = {
    // Clean up resources if needed
  }
}
