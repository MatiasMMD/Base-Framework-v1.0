# 🚀 Base Automation Framework v1.0 🚀

![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk&style=for-the-badge)
![Selenium](https://img.shields.io/badge/Selenium-4-green?logo=selenium&style=for-the-badge)
![Cucumber](https://img.shields.io/badge/Cucumber-7-green?logo=cucumber&style=for-the-badge)
![Gradle](https://img.shields.io/badge/Gradle-8.5-blue?logo=gradle&style=for-the-badge)
![JUnit5](https://img.shields.io/badge/JUnit-5-blue?logo=junit5&style=for-the-badge)

* Este es un esqueleto de framework de automatización de pruebas web construido con Selenium, Java, Cucumber y Gradle. Está diseñado para ser robusto, escalable y fácil de mantener, siguiendo las mejores prácticas de la industria como el Page Object Model (POM).

## 🛠️ Tecnologías Utilizadas

* **Java 21**: Lenguaje de programación.
* **Selenium 4**: Para la automatización del navegador.
* **Cucumber 7**: Para el desarrollo guiado por comportamiento (BDD).
* **Gradle**: Como herramienta de construcción y gestión de dependencias.
* **JUnit**: Como motor de ejecución de pruebas para Cucumber.
* **WebDriverManager**: Para la gestión automática de los binarios de los drivers.
* **Log4j2**: Para un sistema de logging profesional.

## ⚙️ Configuración

1. **Clona el repositorio.**
2. **Configura tu entorno**: Las configuraciones principales se encuentran en `src/main/resources/config.properties`.
    * `browser`: El navegador a utilizar (`chrome`, `firefox`, `edge`).
    * `baseUrl`: La URL base de la aplicación a probar.
    * `explicitWait`: Tiempo máximo de espera explícita en segundos.
3. **Credenciales**: Añade tus credenciales de prueba en `src/main/resources/credentials.properties`. Este archivo está ignorado por Git para proteger tus datos sensibles.

## 📁 Estructura del Proyecto

A continuación, se detalla la función y estructura de cada componente clave del proyecto.

### 🏗️ **BasePage.java**

* **Funcionalidad:** Es la clase “madre” de todas las Page Objects. Centraliza interacciones comunes de Selenium (clics, escritura, esperas, selects, etc.), evitando duplicación de código y manteniendo las clases hijas enfocadas en su propia lógica.

* **Estructura:**
    * **Atributos:** Instancias protegidas de `WebDriver`, `WebDriverWait` y `ActionsHelper`.  
    * **Constructor:** Obtiene el driver desde `WebDriverSetup`, inicializa `WebDriverWait` con tiempos tomados de `config.properties` y crea una instancia de `ActionsHelper`.  
    * **Métodos:** Métodos genéricos para interactuar con elementos web, sobrecargados para aceptar un `String` (XPath) o un `WebElement`. Ejemplos: `clickElement()`, `write()`, `selectDropdownByValue()`, `elementIsDisplayed()`.

* **Implementación:** Cualquier Page Object debe heredar de `BasePage` para acceder automáticamente al driver y a los métodos utilitarios. Ejemplo: `public class LoginPage extends BasePage`.


### 🌐 **WebDriverSetup.java**

* **Funcionalidad:** Gestiona completamente el ciclo de vida del WebDriver. Crea, configura, entrega y destruye la instancia del navegador usando `ThreadLocal` para garantizar paralelismo seguro.

* **Estructura:**
    * **ThreadLocal webDriver:** Almacena una instancia de WebDriver por hilo.  
    * **setup():**  
        * Lee navegador desde `ConfigReader`.  
        * Usa WebDriverManager para configurar el driver.  
        * Crea el navegador con opciones como `--start-maximized`.  
        * Configura esperas implícitas.  
        * Guarda la instancia en el `ThreadLocal`.  
    * **getDriver():** Devuelve el WebDriver del hilo actual.  
    * **quitDriver():** Cierra el navegador y limpia el `ThreadLocal`.

* **Implementación:** Usado por Hooks (`@Before` → setup, `@After` → quitDriver).


### 🧾 **ConfigReader.java**

* **Funcionalidad:** Maneja la lectura de propiedades del framework. Centraliza configuraciones que pueden variar entre entornos: URL, navegador, timeouts, credenciales, etc.

* **Estructura:**
    * **Patrón Singleton:** Una única instancia cargada una vez para rendimiento óptimo.  
    * **Constructor:** Carga `config.properties` y `credentials.properties`.  
    * **Métodos get...():** Obtienen valores específicos como `getBrowser()`, `getBaseUrl()`, `getUsername()`, realizando conversiones si corresponde.

* **Implementación:** Utilizado por `WebDriverSetup`, `BasePage` y también en Step Definitions para datos de prueba.


### 📝 **Archivos .feature (Cucumber)**

* **Funcionalidad:** Los archivos `.feature` definen los escenarios de prueba utilizando el lenguaje Gherkin. Estos archivos describen el comportamiento esperado de la aplicación de forma clara, legible y orientada al negocio. Representan **qué** debe hacer la prueba, no **cómo** se ejecuta.

* **Estructura**
    Cada `.feature` sigue una estructura estándar:
      * **Feature:** Título general que describe la funcionalidad que se quiere validar.  
      * **Background:** Conjunto de pasos que se ejecutan antes de cada escenario de la feature. Útil para acciones repetitivas como navegar a la URL base, iniciar la aplicación, etc.
      * **Scenario/Scenario Outline:** Representan casos de prueba individuales.
        * Scenario: Caso puntual.
        * Scenario Outline: Permite repetir el mismo escenario con distintos datos.
      * **Steps**
        Los pasos del escenario, escritos con las palabras clave de Gherkin:
          * Given: Estado inicial.
          * When: Acción ejecutada.
          * Then: Resultado esperado.
          * And/But: Complementos de pasos.

* **Implementación:** Cada paso del archivo .feature busca una coincidencia en los Step Definitions mediante expresiones regulares o anotaciones de Cucumber.
    * Los Step Definitions interactúan con las Page Objects, que heredan de BasePage, manteniendo una separación clara entre:
        * Qué se prueba → .feature
        * Cómo se ejecuta → Steps + Pages
    * Esto garantiza un diseño limpio, escalable y fácil de mantener


### 🧰 **ActionsHelper.java**

* **Funcionalidad:** Facilita el uso de `Actions` de Selenium para acciones complejas como hover, doble clic, drag & drop.

* **Estructura:**
    * **Atributos:** Instancia de `Actions`.  
    * **Constructor:** Recibe el WebDriver e inicializa la instancia.  
    * **Métodos:** Acciones como `moveToElement()`, `doubleClick()`, encapsulando siempre `.perform()`.

* **Implementación:** Instanciado dentro de `BasePage`, accesible por todas las Pages.


### ⚙️ **Hooks.java**

* **Funcionalidad:** Ejecuta lógica previa y posterior a cada escenario de Cucumber. Controla apertura de navegador, capturas y limpieza.

* **Estructura:**
    * **@Before:** Llama a `WebDriverSetup.setup()` para abrir y configurar el navegador.  
    * **@After:**  
        * Si el escenario falla, captura pantalla y la adjunta al reporte.  
        * Siempre ejecuta `WebDriverSetup.quitDriver()`.

* **Implementación:** Cucumber ejecuta automáticamente los métodos anotados dentro del paquete `steps`.


### 🧪 **TestRunner.java**

* **Funcionalidad:** Punto de entrada para ejecutar Cucumber a través de JUnit.

* **Estructura:**
    * **@RunWith(Cucumber.class):** Indica a JUnit que use Cucumber como motor de ejecución.  
    * **@CucumberOptions:**  
        * `features`: Ruta a los archivos `.feature`.  
        * `glue`: Paquete donde están Steps y Hooks.  
        * `plugin`: Formatos de reporte (`pretty`, `html:target/cucumber-reports`).  
        * `tags`: Filtro de escenarios.

* **Implementación:** Ejecutado automáticamente al correr `./gradlew test`.


### 🤖 **build.gradle**

* **Funcionalidad:** Archivo central del build. Controla dependencias, compilación y ejecución.

* **Estructura:**
    * **plugins:** Activa el plugin de Java.  
    * **repositories:** `mavenCentral()`.  
    * **dependencies:** Selenium, Cucumber, JUnit, WebDriverManager, Log4j2, etc.  
    * **tasks.named("test"):** Configura la tarea `test` para pasar propiedades del sistema a la ejecución.

* **Implementación:** Usado a través de comandos como `./gradlew build` y `./gradlew test`.


### 📦 **Archivos de Configuración y Recursos**

* **config.properties / credentials.properties:**  
  Configuraciones del framework. Las credenciales deben ignorarse en Git por seguridad.  
* **log4j2.xml:**  
  Configura el logging (formato, destino y nivel).  
* **gradlew / gradlew.bat:**  
  Wrappers de Gradle que garantizan la misma versión de Gradle en todos los entornos.

## ▶️ Cómo Ejecutar las Pruebas

Puedes ejecutar las pruebas desde la línea de comandos usando el wrapper de Gradle:

```bash
# Ejecutar todos los escenarios
./gradlew test

# Ejecutar solo los escenarios con un tag específico (ej. @login)
./gradlew test -Dcucumber.filter.tags="@login"
```

* Los informes de la prueba se generarán en la carpeta `target/cucumber-reports`.
