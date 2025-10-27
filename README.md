# Base Automation Framework v1.0

Este es un esqueleto de framework de automatización de pruebas web construido con Selenium, Java, Cucumber y Gradle. Está diseñado para ser robusto, escalable y fácil de mantener, siguiendo las mejores prácticas de la industria como el Page Object Model (POM).

## Tecnologías Utilizadas

*   **Java 21**: Lenguaje de programación.
*   **Selenium 4**: Para la automatización del navegador.
*   **Cucumber 7**: Para el desarrollo guiado por comportamiento (BDD).
*   **Gradle**: Como herramienta de construcción y gestión de dependencias.
*   **JUnit 5**: Como motor de ejecución de pruebas para Cucumber.
*   **WebDriverManager**: Para la gestión automática de los binarios de los drivers.
*   **Log4j2**: Para un sistema de logging profesional.

## Configuración

1.  **Clona el repositorio.**
2.  **Configura tu entorno**: Las configuraciones principales se encuentran en `src/main/resources/config.properties`.
    *   `browser`: El navegador a utilizar (`chrome`, `firefox`, `edge`).
    *   `baseUrl`: La URL base de la aplicación a probar.
    *   `explicitWait`: Tiempo máximo de espera explícita en segundos.

## Cómo Ejecutar las Pruebas

Puedes ejecutar las pruebas desde la línea de comandos usando Gradle:

```bash
# Ejecutar todos los escenarios
./gradlew test

# Ejecutar solo los escenarios con un tag específico (ej. @login)
./gradlew test -Dcucumber.filter.tags="@login"
```

Los informes de la prueba se generarán en la carpeta `target/cucumber-reports`.