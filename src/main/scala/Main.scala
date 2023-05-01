

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import org.apache.flink.streaming.api.scala._

object Main extends App{

  //Environment init. Checkpointing configuration.
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.enableCheckpointing(60000, CheckpointingMode.EXACTLY_ONCE)
  env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

  //Building KafkaSource.
  val source : KafkaSource[String] = KafkaSource.builder()
    .setBootstrapServers("localhost:9092")
  .setTopics("flink-input")
  .setStartingOffsets(OffsetsInitializer.earliest())
    .setValueOnlyDeserializer(new SimpleStringSchema())
    .build()

  //CassandraSink only works with Tuples or POJOs. In Scala, it only works for Tuples.
  val tuples: DataStream[String] = env.fromSource(source, WatermarkStrategy.noWatermarks(), "KafkaSource")


  val serializer: KafkaRecordSerializationSchema[String] = KafkaRecordSerializationSchema.builder()
    .setValueSerializationSchema(new SimpleStringSchema())
    .setTopic("flink-out")
    .build();

  val sink: KafkaSink[String] = KafkaSink.builder()
    .setBootstrapServers("localhost:9092")
    .setRecordSerializer(serializer)
    .build()


  tuples.print()
  //actual sinking
  tuples.sinkTo(sink)
  println("heloow")
  //executing Flink job
  env.execute("Flink test")



}
