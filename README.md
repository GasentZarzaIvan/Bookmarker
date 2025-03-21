# Bookmarker
Busca, ordena y añade marcadores a libros de OpenLibrary desde tu Android.

Trabajo de fin de grado de DAM hecho por Iván Gasent Zarza. 

## Objetivos planteados
Crear una aplicación en Android que permita buscar libros por Internet, guardar su información en una base de datos local agrupándolos en secciones creadas por el usuario y crear marcadores  asociados a los libros. 

## Objetivos alcanzados
Se ha realizado una aplicación Android siguiendo el patrón de arquitectura Model View ViewModel (MVVM) que realiza consultas mediante distintas RESTful APIs que ofrece OpenLibray y guarda sus resultados en una base de datos local SQLite con la biblioteca de persistencia Room. Las llamadas a las APIs de OpenLibrary se realizan con la libreria Android Asynchronous Http Client hecha por James Smith y la visualización de imagenes en web se muestran gracias a la libreria Picasso de Square Open Source.

## Indicaciones sobre la instalación y ejecución: 
Para instalar la aplicación se necesita haber construido un archivo APK a partir del código fuente. Este archivo APK puede instalarse en dispositivos Android con una versión igual o superior 7.0 y deben tener activada la opción de Orígenes desconocidos en los ajustes de seguridad.

## Documentación
En la carpeta `doc` se encuentra la documentación realizada para el proyecto, siendo estos el guión, un resumen del proyecto y un archivo txt con la dirección a las diapositivas que se utilizaron en la defensa del proyecto.
