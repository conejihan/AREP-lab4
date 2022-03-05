# Taller Arquitecturas de Servidores de Aplicaciones, Meta Protocolos de Objetos, Patrón IOC y reflexión

En este taller se realizara un servidor web en java, el cual entrega respuestas html e imagenes   tipo PNG

## Prerrequisitos

Para la realización de este se utilizaron los siguientes elementos:
* Maven
* Java versión 8
* Git
* Heroku

## ¿Cómo funciona?

El programa contiene una aplicacion web capaz de recibir multiples solicitudes no concurrentes, creando un IoC para la construccion de la aplicacion a partir de POJOs, esto ayuda a acceder a recursoso estaticos. 

## Ejecución

Para ejecutar el proyecto se puede realizar de tres maneras:
* Primero clonamos el repositorio por medio del comando desde el cmd y la ubicación donde queremos guardar.

  ```git clone https://github.com/conejihan/AREP-lab4```

* Despues compilamos el proyecto por medio del siguiente comando.

  ```mvn compile```

* Despues de hacer esto ejecutamos el proyecto de la siguiente manera usando java.

  ```web: java $JAVA_OPTS -cp target/classes:target/dependency/* edu.escuelaing.arep.App```

* Y podriamos ingresar por medio de nuestro localhost o utilizando el link de heroku que se encuentra más adelante.



## ¿Comó extenderlo?

La mejor forma para extenderlo se tendria que primero implementar interfaces para que al hacer cambios en este no se vea muy afectado. A parte de esto se podría hacer que aparte de obtener una imagén, este devuelva cualquier tipo de archivo, aparte de esto se puede hacer que ademas de imagenes y una pagina HTML pueda tambein entregar videos o archivos al usuario.


## Heroku

[![Deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://arep-lab4.herokuapp.com/)

