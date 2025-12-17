# Usamos como base la imagen oficial de Nginx, versión 1.27.
# sobre Alpine Linux (muy ligera, ideal para contenedores de infraestructura).
# Elegimos una versión concreta (1.27) en lugar de 'latest' hace los despliegues
# más predecibles y evita sorpresas si la imagen cambia.
FROM nginx:1.27-alpine

# Instalamos 'openssl' dentro de la imagen.
# Usamos 'apk', que es el gestor de paquetes de Alpine (equivalente a apt/yum).
# La opción '--no-cache' evita que se guarden los índices de paquetes en disco,
# reduciendo el tamaño final de la imagen y evitando basura innecesaria.
# Necesitamos 'openssl' para poder generar certificados autofirmados
# en el script de entrada (docker-entrypoint.sh).
RUN apk add --no-cache openssl

# Copiamos nuestra configuración personalizada de Nginx desde el contexto
# de build (la carpeta del proyecto) al interior de la imagen.
# nginx.conf (config) es el archivo que definimos nosotros (reverse proxy,
# puertos, SSL, proxy_pass, etc).
# /etc/nginx/nginx.conf (destino) es el archivo principal que Nginx lee
# cuando arranca. Con estos sobreescribimos la configuración por defecto
# de la imagen oficial por nuestra configuración adaptada a la aplicación.
COPY nginx.conf /etc/nginx/nginx.conf

# Copiamos el script de entrada (entrypoint) al interior de la imagen.
# Este script será el responsable de:
# - Comprobar si ya existen certificados en /etc/nginx/certs.
# - Si no existen, generarlos con 'openssl' (certificado autofirmado).
# - Lanzar Nginx en primer plano (daemon off) para que el contenedor
#   no se cierre al ejecutar.
COPY docker-entrypoint.sh /docker-entrypoint.sh

# Damos permisos de ejecución al script de entrada.
# como ENTRYPOINT, de lo contrario el contenedor fallaría al arrancar.
RUN chmod +x /docker-entrypoint.sh

# Definimos el ENTRYPOINT del contenedor:
#   problemas de interpretación de argumentos por parte de una shell intermedia.
# - Este entrypoint lo normal es que al final se haga:
#   exec nginx -g 'daemon off;' para que Nginx pase a ser el proceso PID 1 del contenedor y se quede en foreground.
ENTRYPOINT ["/docker-entrypoint.sh"]
