package partitioners

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.utils.Utils

class PriorityPartitioner extends Partitioner {
  private val partitionsPerPriority = 2
  private val priorities = Seq(("f2692d56-307b-485c-be8e-30384b76107a", 0), ("6014e175-04de-45d8-a4d6-53659d3fdc72", 1))

  override def configure(configs: java.util.Map[String, _]): Unit = {
  }

  override def partition(topic: String, key: Any, keyBytes: Array[Byte], value: Any, valueBytes: Array[Byte], cluster: Cluster): Int = {
    val numPartitions = cluster.partitionCountForTopic(topic)
    val prioritizedPartitions = priorities.length * partitionsPerPriority
    if ( numPartitions > prioritizedPartitions) {
      val parts = key.toString.split("_")
      val priorityKey = parts(0)
      val messageKey = parts(1)
      if (priorities.map(x => x._1).contains(priorityKey)) {
        val priority = priorities.find((p => p._1 == priorityKey))
        val priorityOffset = (partitionsPerPriority * priority.get._2)
        return (messageKey.hashCode % partitionsPerPriority) + priorityOffset

      }
    }
    Utils.toPositive(Utils.murmur2(keyBytes)) % (numPartitions - prioritizedPartitions) + prioritizedPartitions
  }
  override def close(): Unit = {
  }
}
