# starwars-api

Este proyecto es una API desarrollada con Spring Boot para gestionar información relacionada Star Wars.

## Sistema de seguridad
Se utiliza para este caso practico "h2" como bd y jwt para el register y login seguro en la app.
Al hacer login se debe copiar el token generada en incluir como Authorization Bearer a la hora de hacer peticiones a los endpoints protegidos.

## Documentación de la API

La documentación de la API está generada con Swagger y puede ser accedida una vez que la aplicación esté en ejecución en la siguiente URL:

```
http://localhost:8080/swagger-ui/index.html
```

## Requisitos

* **Java:** Versión 17 o superior.
* **Maven:** Para la gestión de dependencias y construcción del proyecto.
* **Docker:** Para la contenerización y ejecución de la aplicación.

-Se utiliza docker por la facilidad de levantar y no tener problemas con versiones de java y/o maven

## Cómo Levantar el Proyecto con Docker
### 1. Construir la Imagen Docker

Asegúrate de tener Docker instalado en tu sistema. Navega al directorio raíz del proyecto (donde se encuentra el archivo `Dockerfile`) y ejecuta el siguiente comando en tu terminal:

```bash
docker build -t starwars-api .
```
### 2. Correr la Imagen Docker

```bash
docker run -p 8080:8080 starwars-api
```

* No se ofuzcan los properties por simplicidad de este caso practico
* Imagen ya generada se encuentra publica en docker hub: [starwars-api](https://hub.docker.com/r/svidall/starwars-api)
