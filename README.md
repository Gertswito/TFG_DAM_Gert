<div>
  <img style="100%" src="https://capsule-render.vercel.app/api?type=waving&height=100&section=header&reversal=false&text=Librerías%20Gert&fontSize=70&fontColor=FFFFF&fontAlign=50&fontAlignY=50&stroke=-&descSize=20&descAlign=50&descAlignY=50&textBg=false&theme=onedark"  />
</div>

###

<div align="left">
  <p>Mi TFG para Desarrollo de Aplicaciones Multiplataforma es una app Android de una librería llamada "Librerías Gert"</p>
  <p>El frontend está desarrollado en Kotlin con Android Studio y el backend en Java en Visual Studio Code con Spring, la BBDD es MySQL</p>
</div>

###

<hr>

###

<div align="center">
  <h2>INICIALIZACIÓN</h2>
</div>

###

<div align="left">
  <p>Para poder usar la app hace falta usar el SQL adjunto en la carpeta SQL_TFG, ejecutando primero el de crear las tablas y luego el de rellenar datos (obviamente)</p>

  ###
  
  <p>Ya que la contraseña de los usuarios de ejemplo están protegidas, las dejo por aquí para que se pueda testear:</p>
  <p>    - Usuario: gertswito | Contraseña: 1234</p>
  <p>    - Usuario: admin | Contraseña: admin</p>

  ###
  
  <p>También hace falta en MySQL Workbench un usuario con todos los permisos y con las credenciales "admin" y contraseña "admin"</p>
</div>

###

<hr>

###

<div align="center">
  <h2>LANZAR EL PROYECTO</h2>
</div>

###

<div align="left">
  <p>Para lanzar el frontend solo hace falta meterte a Android Studio y darle a iniciar proyecto</p>
  <p>Para lanzar el back es necesario una de dos cosas:</p>
  <p>    - Descargar las extensiones de Spring en Visual Studio Code y lanzarlo desde ahí</p>
  <p>    - Escribir en la parte de "Consola" el siguiente comando desde la carpeta Backend_TFG: mvn springboot:run</p>

  ###

  <p>Como he usado la API Sanbox de PayPal hace falta usar un usuario creado por mi para testear las ventas, dejo por aquí las credenciales:</p>
  <p>    - Correo electrónico: gertjustgonzalezbreto@gmail.com | Contraseña: 1234</p>
</div>

###

<hr>

###

<div align="center">
  <h2>PRUEBAS MANUALES Y AUTOMÁTICAS</h2>
</div>

###

<div align="left">
  <p>A lo largo del desarrollo he realizado pruebas tanto manuales como automáticas. Las pruebas manuales que he realizado han sido la mayoría de cosas del frontend (tanto la parte ADMIN con la creación de usuarios como la de USER para carritos de compra o pasarelas de pago). Para las pruebas automáticas he usado JUnit en el backend, para ello adjunto una captura del resultado de estos:</p>
</div>

<div align="center">
  <img height="325" src="https://raw.githubusercontent.com/Gertswito/Gertswito/main/TEST.PNG" />
</div>

<div>
  <img style="100%" src="https://capsule-render.vercel.app/api?type=waving&height=100&section=footer&reversal=false&text=&fontSize=20&fontColor=FFFFFF&fontAlign=50&fontAlignY=50&stroke=-&descSize=20&descAlign=50&descAlignY=50&theme=onedark"  />
</div>

