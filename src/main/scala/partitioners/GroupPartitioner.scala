package partitioners

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster

class GroupPartitioner extends Partitioner {
  private val partitionsPerGroup = 5

  override def configure(configs: java.util.Map[String, _]): Unit = {
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int = {
    val numPartitions = cluster.partitionCountForTopic(topic)

    val parts = key.toString.split("_")
    val groupKey = parts(0)
    val withinGroupKey = parts(1)

    val numGroups = numPartitions / partitionsPerGroup

    val group = Math.abs(groupKey.hashCode % numGroups)
    val partitionWithinGroup = Math.abs(withinGroupKey.hashCode % partitionsPerGroup)

    group * partitionsPerGroup + partitionWithinGroup
  }
  override def close(): Unit = {
  }
}
