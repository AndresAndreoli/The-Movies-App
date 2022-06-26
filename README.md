# The-Movies-App

Challengue Android:
https://drive.google.com/file/d/19sWXyPTBQ-dTeov779GZyid572iu9c0o/view

Tecnologia: Kotlin.

Adicionales:

<-- REALIZADO  -->

Dado que el endpoint de películas populares es paginado, la aplicación
podrá consultar por nuevas a medida que el usuario navegue entre estas.
Es decir, que al llegar al final de la lista que se muestra en pantalla, la
aplicación busque nuevo contenido en la API.

<-- REALIZADO  -->

Agregar un campo de búsqueda, que permita filtrar aquellas películas que
contengan dicho texto. La búsqueda deberá realizarse entre el listado que
se encuentra visible, si no hay resultado satisfactorio, deberá mostrarse su
error correspondiente.

<-- REALIZADO -->

Comentario: No se si la implementacion esta bien realizada. Por cuestiones de tiempo, no llegue a implementar una funcionalidad que solo te permita votar una unica vez la pelicula.

La API posee un endpoint para evaluar una película determinada (ver The
Movie Database API). Agregar en la vista de detalle la posibilidad de
evaluar una película y actualizar la API con dicha información.

<-- REALIZADO  -->

Para aumentar la performance de la aplicación, se busca evitar la consulta
continua de una misma película. Por este motivo, la aplicación deberá
almacenar los detalles de las películas ya vistas. En caso de que el usuario
seleccione una de ellas se consultará dicha información guardada en el
dispositivo, caso contrario, deberá consultar a la API correspondiente.
