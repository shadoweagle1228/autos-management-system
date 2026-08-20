# 🚗 Sistema de Gestión de Autos y Usuarios

Plataforma integral para el registro y gestión del inventario de vehículos de usuarios. Este proyecto está construido bajo una arquitectura robusta orientada a microservicios y diseño guiado por el dominio (DDD) a través de Arquitectura Hexagonal.

---

## 🚀 1. Guía de Despliegue (Manual de Instalación)

Para ejecutar este proyecto en tu entorno local, asegúrate de tener instalados **Docker**, **Docker Compose**, **Java (JDK 17+)** y **Node.js / Angular CLI**.

### Despliegue del Backend (Microservicios & Base de Datos)
Toda la infraestructura del backend y la base de datos SQL Server están orquestadas mediante contenedores.

1. Abre tu terminal en la raíz del proyecto backend.
2. Ejecuta el siguiente comando para construir y levantar los contenedores en segundo plano:
   ```bash
    docker-compose up --build -d
   ```
3. Espera unos segundos a que los servicios inicialicen. Flyway ejecutará automáticamente los scripts de migración para crear los esquemas de base de datos e insertar los datos de prueba (V1 y V2).

## 🔗 2. URLs y Accesos del Sistema
Una vez que todos los servicios estén corriendo, puedes acceder a las diferentes capas del sistema a través de las siguientes rutas:

- Aplicación Web (Frontend): http://localhost:4200

- API Gateway: http://localhost:8080 (Punto de entrada unificado)

- Microservicio de Autos: http://localhost:8082/api/autos

- Microservicio de Usuarios: http://localhost:8081/api/auth

- Documentación API (Swagger/OpenAPI): http://localhost:8082/swagger-ui/index.html (Documentación del Autos Service)

## 🛠️ 3. Stack Tecnológico
1. Frontend:
- Angular 17+ (Componentes Standalone)
- Bootstrap 5 (Diseño Responsivo)
- RxJS & Zone.js

2. Backend:
- Java / Spring Boot 3
- Spring Security & JWT (Autenticación Stateless)
- Spring Data JPA / Hibernate
- Spring Cloud OpenFeign (Comunicación interna)
- Flyway (Control de versiones de base de datos)
- JUnit & Mockito (Pruebas Unitarias)

3. Infraestructura:
- SQL Server (Base de Datos)
- Docker & Docker Compose

## 🏛️ 4. Documento de Decisiones Arquitectónicas (ADR)
El sistema requiere gestionar el registro de usuarios y el inventario de sus vehículos, garantizando que cada usuario solo tenga acceso a su propia información mediante autenticación segura. Para demostrar capacidades avanzadas en ingeniería de software, escalabilidad y diseño de sistemas distribuidos, se ha descartado el enfoque de monolito tradicional en favor de una arquitectura modular basada en microservicios.

1. Estilo Arquitectónico: Microservicios
- Decisión: El backend se divide en dos microservicios independientes
- Justificación: Permite escalar dominios de forma independiente (ej. el servicio de autenticación suele recibir más carga). Además, demuestra el dominio en la orquestación de sistemas distribuidos, resolviendo los retos de comunicación por red y consistencia.

2. Patrón de Diseño Interno: Arquitectura Hexagonal
- Decisión: Cada microservicio implementa Arquitectura Hexagonal (Puertos y Adaptadores), separando estrictamente el Dominio (Core) de la Infraestructura (Controladores REST y Repositorios JPA).
- Justificación: Aísla las reglas de negocio de los frameworks y la base de datos. Esto garantiza un alto nivel de desacoplamiento, facilita las pruebas unitarias al 100% aislando el dominio, y permite cambiar tecnologías externas sin reescribir la lógica central.

3. Estrategia de Base de Datos: "Database per Service" (Lógica)
- Decisión: Se utiliza una única instancia de SQL Server, pero implementando separación lógica de datos mediante la creación de esquemas distintos (users_schema y cars_schema). No existen llaves foráneas físicas entre las tablas de distintos esquemas.
- Justificación: Cumple con el principio de microservicios donde cada servicio es dueño exclusivo de sus datos. Evita el acoplamiento a nivel de base de datos y obliga a que cualquier integración de información ocurra mediante APIs, preparando el sistema para una eventual separación física de servidores.

4. Comunicación Inter-Servicios: Síncrona con Spring Cloud OpenFeign
- Decisión: Cuando el Car-Service requiere información extendida del propietario de un vehículo, realiza una llamada HTTP interna y síncrona al User-Auth-Service utilizando clientes declarativos de OpenFeign.
- Justificación: OpenFeign abstrae la complejidad de las peticiones REST y la serialización JSON, manteniendo un código limpio e interfaces claras. Es ideal para consultas puntuales de lectura entre dominios.

5. Seguridad: JWT Stateless y "Token Relay"
- Decisión: La autenticación se maneja sin estado (stateless) mediante JSON Web Tokens (JWT). El frontend en Angular almacena el token y lo envía en cada petición. Para la comunicación interna, el Car-Service propaga (Token Relay) el mismo JWT hacia el User-Auth-Service a través de un interceptor de Feign.
- Justificación: Evita el cuello de botella de tener que validar la sesión en una base de datos centralizada por cada petición. Ambos microservicios pueden validar la autenticidad del token localmente comprobando su firma, garantizando un flujo seguro de extremo a extremo.