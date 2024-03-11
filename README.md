# Kafka Consumer Throughput Comparison POC

## Overview
This Proof of Concept (POC) aims to compare the throughput of different Kafka consumers. By implementing traditional Kafka consumers, traditional Kafka consumers with executors, reactive Kafka consumers using Java Reactor Flux, and reactive Kafka consumers using reactive Java, we can analyze the performance characteristics of each approach. Throughput is measured by processing a certain number of messages and calculating the time taken for each consumer type.

## Kafka Infrastructure Setup

### Overview
Setting up Kafka infrastructure is crucial for running the Kafka consumers and conducting performance testing.

### Infrastructure Components
- **Kafka**: Kafka is a distributed streaming platform capable of handling large volumes of data. In this POC, we will use Kafka as the messaging system.
- **Zookeeper**: Zookeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services. Kafka relies on Zookeeper for various operations such as leader election and configuration management.
- **Kafka UI**: Kafka UI is a web-based user interface for monitoring Kafka topics, partitions, and messages. It provides insights into Kafka cluster health and performance.
- **Portainer**: Portainer is a lightweight container management tool that simplifies the management of Docker environments. It provides a user-friendly interface for managing Docker containers.

### Setup Instructions
1. **Pull Docker Images**: Pull the necessary Docker images for Kafka, Zookeeper, and Kafka UI:
    ```bash
    docker pull confluentinc/cp-kafka:latest
    docker pull confluentinc/cp-zookeeper:latest
    docker pull provectuslabs/kafka-ui:latest
    ```

2. **Run Docker Containers**: Run the Kafka, Zookeeper, and Kafka UI containers:
    ```bash
    docker run -d \
        --name kafka \
        -p 29092:29092 \
        confluentinc/cp-kafka:latest

    docker run -d \
        --name zookeeper \
        -p 22181:2181 \
        confluentinc/cp-zookeeper:latest

    docker run -d \
        --name kafka-ui \
        -p 8090:8080 \
        provectuslabs/kafka-ui:latest
    ```

3. **Access Kafka UI**: Kafka UI is available at [http://localhost:8090](http://localhost:8090). Use this interface to monitor Kafka topics and partitions.

4. **Access Portainer**: Portainer is available at [http://localhost:9000](http://localhost:9000). Use this interface to manage Docker containers.

## Kafka Consumer Setup

### Overview
The Kafka consumers are implemented using Spring Boot applications with different configurations to analyze their throughput.

### Components
- **KafkaConfig.java**: Configuration class for Kafka consumer properties and beans.
- **KafkaMessageListener.java**: Kafka message listener implementing traditional and reactive consumers.
- **ReactiveKafkaListner.java**: Kafka listener implementing reactive consumers using Java Reactor Flux.

### Dependencies
- **Spring Kafka**: Integration library for Kafka with Spring applications.
- **Reactor Kafka**: Reactive support for Kafka consumers.
- **Apache Kafka Clients**: Kafka client library for Java.
- **Spring Boot Starter Webflux**: Reactive web support for Spring Boot applications.

### Building and Running the Project
1. **Clone the Repository**: Clone the repository to your local machine.
2. **Build the Project**: Navigate to the project directory and build the project using Maven or Gradle:
    ```bash
    ./gradlew build
    ```
3. **Run the Application**: Run the Spring Boot application:
    ```bash
    java -jar <path-to-jar>/your-application.jar
    ```

## Performance Metrics
To evaluate the performance of each consumer type, measure the throughput by processing a specific number of messages and calculating the time taken.



---

## Observations

In our performance testing, traditional Kafka consumers exhibited the lowest throughput, while setups utilizing non-blocking processing—such as traditional Kafka consumers with executors and reactive Kafka consumers—demonstrated significantly higher throughput. Among these setups, both traditional Kafka consumers with executors and reactive Kafka consumers showed similar levels of throughput, significantly outperforming traditional Kafka consumers. This highlights the effectiveness of non-blocking processing methods in amplifying message processing capabilities, even with fewer partitions. Leveraging non-blocking streaming and multi-core processors further enhances performance and resource utilization, particularly evident in reactive Kafka consumers. Based on these findings, adopting non-blocking processing approaches, such as reactive Kafka consumers, is recommended for achieving higher throughput and optimizing resource utilization in Kafka consumer applications.

--- 

## Conclusion
Based on the observed throughput and performance analysis, determine the most efficient consumer type for your use case in terms of performance and scalability.

---