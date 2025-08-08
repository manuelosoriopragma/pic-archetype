# Arquetipo Java - Arquitectura limplia - PIC REDEBAN

Aplicación hecha con el plugin de gradle Scaffold Clean Architecture para 
la creción de distintos servicios para el proyecto PIC de redeban. Este
proyecto es un arquetipo base pensado como punto de partida. Los usuarios 
pueden adaptarlo agregando o eliminado funcionalidades segun sus necesidades.
Ajustelo conforme a su propio flujo de trabajo.

## Requisitos de sistema
Antes de instalar el repositorio asegurate de tener las siguientes herramientas para el desarrollo de tu aplicación:
* [Gradle version 8.8 o posterior](https://gradle.org/install/) 
* [JDK version 21(aun puede ajustarse para version 17)](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
* [Git](https://git-scm.com/)

## Instalación manual y configuración del arquetipo

1. Clonar el repositorio e ingresar a la carpeta

```bash
    git clone https://github.com/manuelosoriopragma/pic-archetype.git <nombre_carpeta>
    cd <nombre_capeta>
```
2. Eliminar el .git del arquetipo

```bash
    rm -rf .git
```
```powershell
    Remove-Item -Path ".git" -Recurse -Force
```
3. Inicializar nuevo repositorio

```bash
    git init
    git fetch
```

4. Agregar nuevo repositorio de trabajo

```bash
    git remote add origin <url>
```
5. Ajustar la propiedad rootProject.name en el archivo settings.gradle

```
    pluginManagement {
        repositories {
            //mavenLocal()
            //maven { url = '...' }
            gradlePluginPortal()
        }
    }
    
    buildCache {
        local {
            directory = new File(rootDir, 'build-cache')
        }
    }
    
    rootProject.name = '<nombre_proyecto>'
    
    include ':app-service'
    include ':model'
    include ':usecase'
    project(':app-service').projectDir = file('./applications/app-service')
    project(':model').projectDir = file('./domain/model')
    project(':usecase').projectDir = file('./domain/usecase')
    include ':reactive-web'
    project(':reactive-web').projectDir = file('./infrastructure/entry-points/reactive-web')
```

6. Ajustar archivo applications/app-service/src/main/resources/application.yaml
   * El puerto (server.port)
   * El nombre del servcicio (spring.application.name)
   * la ruta base por defecto (spring.application.webflux.base-path)

```yaml
    server:
    port: <puerto>
    spring:
      application:
        name: "<nombre_proyecto>"
      webflux:
        base-path: /<ruta_base>
```
7. Si lo desea tambien puede ajustar el nombre del servicio en la configuración 
swagger ubicada en infrastructure/entry-points/reactive-web/src/main/java/co/com/redeban/api/config/OpenApiConfig.java

```java
    @Configuration
    @OpenAPIDefinition(
        info = @Info(
                title = "<nombre_proyecto>", //set this line
                version = "<version>", //set this line
                summary = "<resumen_proyecto>" //set this line
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "local server")
        }
    )
    public class OpenApiConfig {
    }
```

## Ejecución Local

En la consola entra a la carpeta del proyecto

```bash
    cd pic-archetype
```

cargar el gradle wrapper

```bash
    gradle wrapper
```

limpiar el proyecto
```bash
    ./gradlew clean
```
ejecución del proyecto
```bash
    ./gradlew bootRun
```
## Tests
Para ejecutar los test, ejecuta el siguiente comando en la terminal
```bash
    ./gradlew test
```

## Explicación del proyecto

## Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

## Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

### Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

### Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

### Infrastructure

#### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

#### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

#### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

### Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

## Estrucutura del proyecto
el proyecto tendrá la siguiente estrucutura: 
```bash
.
├── README.md
├── applications
│   └── app-service
│       ├── build.gradle
│       └── src
│           ├── main
│           │   ├── java
│           │   │   └── co
│           │   │       └── com
│           │   │           └── redeban
│           │   │               ├── MainApplication.java
│           │   │               └── config
│           │   │                   └── UseCasesConfig.java
│           │   └── resources
│           │       ├── application.yaml
│           │       └── log4j2.properties
│           └── test
│               └── java
│                   └── co
│                       └── com
│                           └── redeban
│                               ├── ArchitectureTest.java
│                               ├── Utils.java
│                               └── config
│                                   └── UseCasesConfigTest.java
├── build.gradle
├── deployment
│   └── Dockerfile
├── domain
│   ├── model
│   │   ├── build.gradle
│   │   └── src
│   │       ├── main
│   │       │   └── java
│   │       │       └── co
│   │       │           └── com
│   │       │               └── redeban
│   │       │                   └── model
│   │       │                       ├── config
│   │       │                       │   ├── ErrorCode.java
│   │       │                       │   ├── ErrorDictionary.java
│   │       │                       │   └── RedebanException.java
│   │       │                       └── example
│   │       │                           ├── Example.java
│   │       │                           └── gateways
│   │       │                               └── ExampleRepository.java
│   │       └── test
│   │           └── java
│   │               └── co
│   │                   └── com
│   │                       └── redeban
│   │                           └── model
│   │                               └── example
│   └── usecase
│       ├── build.gradle
│       └── src
│           ├── main
│           │   └── java
│           │       └── co
│           │           └── com
│           │               └── redeban
│           │                   └── usecase
│           │                       ├── errordictionary
│           │                       │   └── ErrorDictionaryUseCase.java
│           │                       └── example
│           │                           └── ExampleUseCase.java
│           └── test
│               └── java
│                   └── co
│                       └── com
│                           └── redeban
│                               └── usecase
│                                   ├── errordictionary
│                                   │   └── ErrorDictionaryUseCaseTest.java
│                                   └── example
│                                       └── ExampleUseCaseTest.java
├── gradle.properties
├── gradlew
├── gradlew.bat
├── infrastructure
│   ├── driven-adapters
│   ├── entry-points
│   │   └── reactive-web
│   │       ├── build.gradle
│   │       └── src
│   │           ├── main
│   │           │   └── java
│   │           │       └── co
│   │           │           └── com
│   │           │               └── redeban
│   │           │                   └── api
│   │           │                       ├── Handler.java
│   │           │                       ├── RouterRest.java
│   │           │                       ├── config
│   │           │                       │   ├── AppConfig.java
│   │           │                       │   ├── CorsConfig.java
│   │           │                       │   ├── OpenApiConfig.java
│   │           │                       │   └── SecurityHeadersConfig.java
│   │           │                       ├── dto
│   │           │                       │   ├── ExampleDto.java
│   │           │                       │   └── ResponseErrorDto.java
│   │           │                       ├── helper
│   │           │                       │   ├── GlobalErrorHandler.java
│   │           │                       │   └── ValidationUtil.java
│   │           │                       └── mapper
│   │           │                           ├── ExampleMapper.java
│   │           │                           └── ResponseErrorMapper.java
│   │           └── test
│   │               └── java
│   │                   └── co
│   │                       └── com
│   │                           └── redeban
│   │                               └── api
│   │                                   ├── RouterRestTest.java
│   │                                   └── config
│   │                                       └── ConfigTest.java
│   └── helpers
├── lombok.config
├── main.gradle
└── settings.gradle
```

## Recursos:
Para más infomración cosulte la la documentación del [plugin Scaffold Clean Architecture](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro), 
sobre la creación y eliminación de nuevos componentes.

