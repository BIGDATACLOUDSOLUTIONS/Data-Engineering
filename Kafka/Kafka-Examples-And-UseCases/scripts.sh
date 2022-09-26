Start the Zookeeper in Moboxterm
cd /drives/c/Self_InstalledSoftware/kafka_2.12-2.1.1
bin/windows/zookeeper-server-start.bat config/zookeeper.properties &


Start Server Using Git Bash.
cd /drives/c/Self_InstalledSoftware/kafka_2.12-2.1.1
bin/windows/kafka-server-start.bat config/server.properties &
bin/windows/kafka-server-start.bat config/server-1.properties &
bin/windows/kafka-server-start.bat config/server-2.properties &



Create the topic in Moboxterm
cd /drives/c/Self_InstalledSoftware/kafka_2.12-2.1.1
bin/windows/kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
bin/windows/kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 3 --partitions 10 --topic SensorTopic2


Describe Topic:
bin/windows/kafka-topics.bat --zookeeper localhost:2181 --describe --topic test3


Start the consumer in Moboxterm
cd /drives/c/Self_InstalledSoftware/kafka_2.12-2.1.1
bin/windows/kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning


bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
