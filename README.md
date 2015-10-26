# Tasks Manager Portlet
Ejercicio Tasks Manager Portlet

Añadir una nueva funcionalidad a Liferay Portal
6.2 CE que permita a cada usuario registrado en la plataforma gestionar una lista
de tareas personalizada.

La aplicación debe permitir tanto la creación de nuevas tareas, como el
borrado y la edición de las tareas existentes. Asímismo, una tarea ya realizada
debe de poder marcarse como finalizada.

# Estructura
- https://github.com/josepm9/tasks-portlet/dist: empaquetados (mongo/hibernate, también es posible generar uno genérico)
- https://github.com/josepm9/tasks-portlet/src: código fuente
  - https://github.com/josepm9/tasks-portlet/src/tasks-parent: proyecto maven POM, define versiones y módulos
  - https://github.com/josepm9/tasks-portlet/src/tasks-service: modelo y controlador de la gestión de tareas
  - https://github.com/josepm9/tasks-portlet/src/tasks-portlet: portlet de gestión de tareas, depende del anterior
  
# Tecnologías empleadas
- Servicio:
  - Cliente Mongo
  - Spring
  - Hibernate
- Portlet:
  - JSR286
  - JQuery
  - AngularJS

# Resumen
- Servicio: implementa el acceso a datos (Dao) y la lógica de las operaciones (controlador). Embebido en el propio
proyecto existen dos archivos de configuración: cliente mongo en Amazon EC2, BBDD H2 en memoria. Las factorías
disponen de mecanismos para reemplazar estos archivos con propiedades personalizadas.
- Portlet: implementa la parte de visualización. Es estrictamente compatible con JSR286 (no tiene dependencias de Liferay),
no soporte JSR168. Contiene un portlet (visualización y operaciones con tareas), con un sólo modo (VIEW), pero que admite
peticiones de recurso para soportar AJAX. El HTML generado está basado en JQuery y AngularJS.



