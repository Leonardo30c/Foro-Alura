🚀 Foro Alura - API REST con Spring Boot
¡Bienvenido/a al desafío de crear tu propio foro con Spring Boot! Este proyecto implementa una API REST completa con autenticación JWT, donde los usuarios pueden crear y gestionar tópicos en una comunidad interactiva.

📌 Características Principales
✅ Autenticación segura con JWT (Registro e Inicio de Sesión)
✅ CRUD de Tópicos (Crear, Listar, Eliminar)
✅ Relación Usuario-Tópico (Solo el creador puede borrar)
✅ Paginación y Ordenación (Mira todos los tópicos con GET /topicos)
✅ Validación de Datos (Spring Validation)
✅ Configuración para PostgreSQL (o la base de datos que prefieras)

🛠 Tecnologías Utilizadas
Categoría	Tecnologías
Backend	Spring Boot 3.1.5
Base de Datos	PostgreSQL (¡Pero puedes usar MySQL o H2 si prefieres!)
Autenticación	JWT (JSON Web Tokens)
Lenguaje	Java 17
Herramientas	Insomnia/Postman para probar los endpoints
⚡️ Cómo Empezar
1. Clona el repositorio
bash
git clone https://github.com/tu-usuario/foro-alura.git
cd foro-alura
2. Configura tu base de datos
Crea una base de datos llamada foro_db en PostgreSQL.

Configura las credenciales en src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/foro_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
3. Ejecuta la aplicación
bash
mvn spring-boot:run
¡Listo! La API estará disponible en http://localhost:8080.

🔍 Endpoints de la API
🔐 Autenticación
Método	Endpoint	Descripción
POST	/auth/register	Registra un nuevo usuario
POST	/auth/login	Inicia sesión y obtén un token JWT
📝 Tópicos
Método	Endpoint	Descripción	¿Requiere Token?
GET	/topicos	Lista todos los tópicos	❌ No
POST	/topicos	Crea un nuevo tópico	✅ Sí (Bearer Token)
DELETE	/topicos/{id}	Elimina un tópico (solo el creador)	✅ Sí (Bearer Token)
📌 Ejemplo de Uso
1️⃣ Registro de Usuario
http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "nombre": "Ana Pérez",
  "email": "ana@ejemplo.com",
  "password": "123456"
}
2️⃣ Inicio de Sesión (Obtener Token)
http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "ana@ejemplo.com",
  "password": "123456"
}
Respuesta:

json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
3️⃣ Crear un Tópico (Con Token JWT)
http
POST http://localhost:8080/topicos
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "titulo": "Cómo configurar Spring Security",
  "mensaje": "¿Alguien sabe cómo implementar JWT correctamente?",
  "curso": "Spring Boot"
}
🎯 ¿Qué Más Puedes Hacer?
✨ Personaliza tu foro:

Añade más campos a los tópicos (etiquetas, votos, respuestas).

Implementa roles (Admin, Moderador, Usuario).

Agrega un sistema de búsqueda.

📄 Licencia
Este proyecto está bajo la licencia MIT. ¡Siéntete libre de usarlo y modificarlo!
