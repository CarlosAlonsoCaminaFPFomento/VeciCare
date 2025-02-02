# VeciCare
Aplicación Android para conectar personas mayores con voluntarios.

---

## Descripción

**VeciCare** es una aplicación diseñada para conectar a personas mayores con voluntarios o vecinos dispuestos a ayudarlas con tareas diarias y actividades sociales. El objetivo principal es fomentar la inclusión social, la colaboración intergeneracional y la mejora de la calidad de vida de las personas mayores.

---

## Funcionalidades Principales

1. **Chat de Voluntarios:**
   - Comunicación directa entre personas mayores y voluntarios.
   - Coordinación para la realización de tareas o actividades.

2. **Organización de Actividades Locales:**
   - Información sobre eventos y actividades comunitarias.
   - Registro y participación en eventos.

3. **Gestión de Actividades de Voluntariado:**
   - Una lista dinámica de actividades, obtenida de una base de datos local.
   - Soporte dual para la gestión de datos:
     - **SQLite**: Base de datos ligera para operaciones rápidas y acceso inicial.
     - **Room**: Framework moderno para la persistencia de datos.
       - Implementación de DAOs para manejar operaciones CRUD.
       - Uso de `ViewModel` y `StateFlow` para interactuar con los datos de manera reactiva.
       - Logs detallados para verificar la correcta ejecución de las operaciones.
   - **Arquitectura MVVM:**
     - Separación de responsabilidades entre UI, lógica de negocio y acceso a datos.
     - `ViewModels` administrados con **Hilt** para inyección de dependencias.
   - Actividades personalizables como:
     - Comprar alimentos.
     - Recoger medicinas.
     - Acompañamiento virtual o presencial.
     - Trámites administrativos.
     - Pequeñas reparaciones.
     - Paseo de mascotas.
     - Participación en eventos locales.
   - Cada actividad incluye un ícono representativo.
   - Persistencia de datos asegurada para que las tareas se guarden y estén disponibles al reiniciar la aplicación.

4. **Acceso a Internet y API Externas:**
   - **Retrofit** implementado para consumir datos externos.
   - **Ejemplo de integración:** API de bromas de Chuck Norris, almacenando caché local en caso de falta de conexión.

5. **Recordatorios Personalizados:**
   - Alarmas para medicamentos, citas médicas y videollamadas.

6. **Geolocalización:**
   - Identificación de personas mayores y voluntarios cercanos para asistencia inmediata.

7. **Clasificación de Servicios:**
   - Sistema de calificación (0 a 5 estrellas) para evaluar voluntarios o servicios.

8. **Alertas de Emergencia:**
   - Botón para solicitar ayuda urgente.

9. **Funciones Simplificadas:**
   - Interfaz amigable para usuarios con poca experiencia tecnológica.

---

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Framework de desarrollo**: Jetpack Compose
- **Arquitectura**: **MVVM**
- **Inyección de dependencias**: **Hilt** para proporcionar instancias de ViewModels y Repositorios.
- **Bases de datos locales**:
  - **SQLite**: Implementada con un helper para operaciones básicas.
  - **Room**:
    - Entidades, DAO y repositorios centralizados para la gestión eficiente de datos.
    - Uso de `ViewModel` con `StateFlow` para interacción con la base de datos en un entorno reactivo y seguro.
    - Verificación de operaciones mediante logs.
- **Acceso a Internet**:
  - Retrofit configurado para consumir APIs externas.
  - Mecanismo de caché local para funcionamiento sin conexión.
- **Gestión de Ciclo de Vida**:
  - Métodos como `onStart`, `onResume`, y `onStop` para optimizar el comportamiento de la aplicación.
  - Interacción con Room y SQLite en diferentes estados del ciclo de vida.
- **Servicios externos**: APIs públicas para eventos comunitarios y entretenimiento.
- **Geolocalización**: Google Maps SDK
- **Diseño**: Material Design 3

---

## Relevancia Social

VeciCare busca impactar positivamente en la comunidad, promoviendo el apoyo mutuo y la integración social. Es especialmente relevante para fomentar la inclusión y el bienestar de las personas mayores.

---

## Estado Actual del Proyecto

- **Conceptualización**: Completada.
- **Desarrollo**: Implementadas las funcionalidades principales, incluyendo:
  - Ciclo de vida gestionado.
  - Bases de datos locales: SQLite y Room.
  - Implementación de `ViewModel` y `StateFlow` con Hilt.
  - Integración con Retrofit para consumo de APIs externas.
  - Logs detallados para comprobar el correcto funcionamiento de la persistencia.

---

## Cómo Contribuir con el proyecto

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/VeciCare.git
   ```