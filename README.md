# SistemaGestion

API REST del backend del sistema de gestión (cursos, alumnos e inscripciones), construida con Spring Boot 4, Java 17, MySQL 8 y autenticación JWT.

## Requisitos

Elegí una de las dos opciones:

- **Docker**: Docker y Docker Compose (no necesitás nada más).
- **Local**: JDK 17 y una instancia de MySQL 8 (Maven no hace falta, se usa el wrapper `./mvnw`).

## Levantar la API

### Opción 1: Docker Compose (recomendada)

Levanta MySQL y la API juntos:

```bash
docker compose up --build
```

- API (base de los endpoints): `http://localhost:8080/api` (no tiene página propia, usá Swagger para probar)
- MySQL: `localhost:3306` (base `demo_db`, usuario `root`, contraseña `password`)

Para bajar todo:

```bash
docker compose down
```

### Opción 2: Local con Maven

1. Levantá solo MySQL (con Docker, por ejemplo):

   ```bash
   docker compose up -d mysqldb
   ```

   O usá tu propio MySQL en `localhost:3306` con usuario `root` / contraseña `password` (la base `demo_db` se crea sola).

2. Iniciá la API:

   ```bash
   ./mvnw spring-boot:run
   ```

   También podés generar el jar y ejecutarlo:

   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/*.jar
   ```

## Verificar que funciona

- Swagger UI (documentación interactiva): http://localhost:8080/api/swagger-ui/index.html
- Todos los endpoints cuelgan del prefijo `/api`; abrir `/api/` directamente da error porque no hay ruta raíz.

## Configuración

La configuración está en `src/main/resources/application.yml`.

| Propiedad | Descripción | Valor por defecto |
|---|---|---|
| `SPRING_DATASOURCE_URL` (variable de entorno) | URL JDBC de MySQL | `jdbc:mysql://localhost:3306/demo_db?...` |
| `spring.datasource.username` / `password` | Credenciales de la base | `root` / `password` |
| `jwt.secret` | Clave de firma de los JWT | clave de desarrollo |
| `jwt.access-validity-seconds` | Duración del access token | `900` (15 min) |
| `jwt.refresh-validity-days` | Duración del refresh token | `7` |
| `spring.mail.*` | SMTP para el envío de mails (recuperar contraseña) | Mailtrap sandbox, hay que completar `username` y `password` |
| `frontend.reset-password-url` | URL del frontend para resetear contraseña | `http://localhost:5173/reset-password` |

### Usuario administrador por defecto

Se crea al iniciar la aplicación:

- Email: `admin@gl.com`
- Usuario: `admin`
- Contraseña: `admin1234`

## Notas

- `spring.jpa.hibernate.ddl-auto` está en `create`: **el esquema se recrea y los datos se pierden cada vez que se reinicia la API**. Cambialo a `update` si querés persistir datos entre reinicios.
- Los valores por defecto (contraseñas, `jwt.secret`) son solo para desarrollo; no los uses en producción.
- Para que funcione el envío de mails, cargá tus credenciales de Mailtrap (u otro SMTP) en `spring.mail.username` y `spring.mail.password`.

## Tests

```bash
./mvnw test
```
