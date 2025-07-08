# Sistema de Chat Multiplataforma con Microservicios

Este proyecto implementa una arquitectura de microservicios para integrar, procesar, analizar y visualizar mensajes de chat en tiempo real provenientes de plataformas como Twitch y YouTube. Utiliza tecnologías modernas como Spring Boot, Kafka, Redis, MySQL, Docker y React.

## ¿Qué hace este proyecto?

- **Ingesta de mensajes** desde Twitch y YouTube en tiempo real.
- **Agregación y procesamiento** de mensajes usando microservicios.
- **Análisis inteligente** de los chats mediante IA (Ollama).
- **Persistencia** de mensajes en base de datos y caché.
- **Visualización web** del estado de los microservicios y los mensajes.

## Arquitectura

- **api-gateway**: Punto de entrada HTTP, enruta solicitudes y expone el estado de los servicios.
- **chat-aggregator**: Recibe y agrupa mensajes de los ingestors.
- **chat-intel**: Analiza y resume mensajes usando IA.
- **chat-storage**: Almacena mensajes en MySQL y Redis.
- **twitch-ingestor** / **youtube-ingestor**: Extraen mensajes de Twitch/YouTube y los envían al sistema.
- **frontend**: Interfaz web en React para monitoreo y visualización.
- **commons**: Modelos y utilidades compartidas.

## Instalación y ejecución

### 1. Clona el repositorio

```bash
git clone <URL-del-repositorio>
cd <carpeta-del-proyecto>
```

### 2. Levanta los servicios base (Kafka, Redis, MySQL, etc.)

```bash
docker compose up -d
```

### 3. Compila los microservicios

```bash
mvn clean install
```

O puedes compilar un microservicio específico, por ejemplo:

```bash
mvn clean package -pl chat-intel
```

### 4. Ejecuta los microservicios de streaming

En terminales separadas, ejecuta cada uno:

```bash
java -jar youtube-ingestor/target/youtube-ingestor-1.0-SNAPSHOT.jar
java -jar twitch-ingestor/target/twitch-ingestor-1.0-SNAPSHOT.jar
java -jar chat-aggregator/target/chat-aggregator-1.0-SNAPSHOT.jar
java -jar chat-storage/target/chat-storage-1.0-SNAPSHOT.jar
java -jar chat-intel/target/chat-intel-1.0-SNAPSHOT.jar
java -jar api-gateway/target/api-gateway-1.0-SNAPSHOT.jar
java -jar chat-moderator/target/chat-moderator-1.0-SNAPSHOT.jar
```

### 5. Ejecuta el motor de IA (Ollama)

```bash
ollama run gemma3
```

### 6. Levanta el frontend

```bash
cd frontend
npm install
npm run dev
```

## Notas
- Asegúrate de tener Docker, Java 17+, Maven y Node.js instalados.
- El frontend estará disponible en `http://localhost:5173` (por defecto).
- El API Gateway expone el estado de los microservicios en `http://localhost:8086/health`.

## Licencia

Este proyecto es solo para fines educativos y de demostración. 