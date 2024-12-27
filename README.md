# Ancore Gaming API

Ancore Gaming es un e-commerce diseñado para ofrecer los mejores descuentos en videojuegos digitales. Esta API es el núcleo de la plataforma, gestionando usuarios, productos, transacciones, y más.

## Características principales

### Base de Datos
    - Utiliza PostgreSQL como base de datos relacional.
    - Integración con Spring Data JPA para manejar las entidades y consultas.
### Autenticación y Autorización
    - Sistema completo implementado con Spring Security.
    - Uso de JWT para autenticación segura y escalable.
    - Control de acceso detallado para proteger endpoints sensibles.
### Gestión de Usuarios
    - Soporte para wishlist personalizadas por usuario.
    - Creación de reviews con funcionalidades de likes y dislikes.
    - Manejo eficiente de perfiles y preferencias de los usuarios.
### Gestión de Productos
    - Gestión completa de productos, incluyendo:
        1. Subida y administración de imágenes y videos.
        2. Información detallada de los juegos: requisitos mínimos y recomendados, géneros, plataformas, y etiquetas.
    - Optimización para manejar grandes volúmenes de datos.
### Carrito de Compras
    - Carrito robusto, diseñado para evitar problemas de concurrencia.
    - Garantía de integridad en las operaciones de adición y eliminación de productos.
### Pasarela de Pagos
    - Integración con Stripe para manejar pagos de forma segura.
    - Soporte para diversas formas de pago.
### Notificaciones por Correo Electrónico
    - Envío automatizado de correos electrónicos para:
        1. Confirmaciones de registro.
        2. Confirmaciones de compra.
    - Integración con Thymeleaf para personalizar los correos.

## Requisitos

### Tecnologías principales
    - Java 17
    - Spring Boot
    - PostgreSQL
    - Cloudinary
### Herramientas adicionales
    - Thymeleaf para plantillas de correo electrónico.
    - JWT para autenticación.
    - Spring Security para autorización.

## Documentacion
    - Documentacion generada con Swagger y se encuentra en el endpoint:
    - http://localhost:8080/swagger-ui/index.html