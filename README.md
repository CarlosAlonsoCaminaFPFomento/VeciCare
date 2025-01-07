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
   - Una lista dinámica de actividades, obtenida de una base de datos local (Room).
   - Actividades personalizables como:
     - Comprar alimentos.
     - Recoger medicinas.
     - Acompañamiento virtual o presencial.
     - Trámites administrativos.
     - Pequeñas reparaciones.
     - Paseo de mascotas.
     - Participación en eventos locales.
   - Cada actividad incluye un ícono representativo.

4. **Recordatorios Personalizados:**
   - Alarmas para medicamentos, citas médicas, y videollamadas.

5. **Geolocalización:**
   - Identificación de personas mayores y voluntarios cercanos para asistencia inmediata.

6. **Clasificación de Servicios:**
   - Sistema de calificación (0 a 5 estrellas) para evaluar voluntarios o servicios.

7. **Alertas de Emergencia:**
   - Botón para solicitar ayuda urgente.

8. **Funciones Simplificadas:**
   - Interfaz amigable para usuarios con poca experiencia tecnológica.

---

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Framework de desarrollo**: Jetpack Compose
- **Base de datos local**: Room
  - Entidades y DAO para gestionar las actividades de voluntariado.
  - Uso de un repositorio central para interactuar con la base de datos.
- **Gestión de Ciclo de Vida**:
  - Uso de métodos como `onStart`, `onResume`, y `onStop` para optimizar el comportamiento de la aplicación.
- **Servicios externos**: APIs públicas para eventos comunitarios.
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
  - Base de datos local con Room para actividades.

---

## Cómo Contribuir con el proyecto

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/VeciCare.git
