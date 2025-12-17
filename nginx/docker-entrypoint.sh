#!/bin/sh
# Shell: Indica que este script se debe ejecutar con 'sh' (Bourne Shell).

set -e
# --- SECCIÓN DE CONFIGURACIÓN DE CERTIFICADOS ---
# Directorio donde guarda la ruta donde se almacenarán los certificados y la clave privada.
# Es un directorio que mapea el contenedor a la máquina anfitriona (host).
# Se utiliza para persistir los certificados entre reinicios de contenedor.
CERT_DIR=/etc/nginx/certs

# Ruta completa al fichero de clave privada del servidor (Formato PEM).
# Se usa para descifrar la clave privada del servidor para descifrar la información,
# y cifrada para los clientes con la clave pública del certificado.
KEY_FILE="$CERT_DIR/server.key"

# Ruta completa al fichero de certificado del servidor (Formato X.509 en PEM).
# Se usa para distribuir la clave pública (a los clientes, CA's (de la organización, etc.).
# Nginx lo usará junto con 'server.key' para establecer conexiones TLS.
CRT_FILE="$CERT_DIR/server.crt"

# --- LÓGICA DE VERIFICACIÓN Y GENERACIÓN DE CERTIFICADOS ---
# Comprobamos si US existe la clave o SI existe el certificado. Aseguramos si un archivo existe y es un archivo regular.
# Se usa un operador lógico de cadena '&&' (AND lógico) y '||' (OR lógico).
# $? es el código de salida del comando anterior (0 si es exitoso).
if [ ! -f "$KEY_FILE" ] || [ ! -f "$CRT_FILE" ]; then
  echo "[nginx] Certificado no encontrado, generando uno autofirmado..."

    # Creamos el directorio de certificados por si no existe.
    # -p: evita errores si el directorio ya existe (no pasa nada).
  mkdir -p "$CERT_DIR"

    # Comando 'openssl req' para generar un certificado autofirmado.
    # Desglose de opciones:
    # -newkey rsa:2048: genera una solicitud de Certificado (CSR) y un certificado.
    #               rsa:2048 genera una clave privada con contraseña. Esto es importante
    #               (NO DES) NO cifra la clave privada con contraseña. Esto es importante
    #               en entornos Nginx/Docker, evitando pedir una clave sin pedir passphrase.
    # -x509: genera un certificado autofirmado en vez de una CSR.
    # -days 365: el certificado será válido durante 365 días.
    # -out "$CRT_FILE": fichero donde guardar el certificado (clave pública + metadata).
    # -keyout "$KEY_FILE": fichero donde guardar la clave privada.
    # -subj /C=.../CN=...: Define los campos del sujeto del certificado sin pedirlos de forma interactiva:
    # C (Country): ES (España)
    # ST (State/Province): Andalucía
    # L (Locality): Castil de la Cuesta
    # O (Organization): TicketLogger
    # OU (Organizational Unit): DAMA
    # CN (Common Name): localhost (usa 'localhost' porque es para desarrollo local).
  openssl req -x509 -nodes -days 365 \
    -newkey rsa:2048 \
    -keyout "$KEY_FILE" \
    -out "$CRT_FILE" \
    -subj "/C=ES/ST=Andalucia/L=Castil de la Cuesta/O=TicketLogger/OU=DAMA/CN=localhost"
else
    # Si ya existen tanto la clave como el certificado, no los regeneramos.
    # Esto es importante cuando usamos un volumen para /etc/nginx/certs:
    # Evita la regeneración en cada inicio, acelerando reinicios de contenedor.
    echo "[nginx] Certificado ya existe, reutilizándolo..."
fi

# --- INICIO DE NGINX ---
# Mensaje informativo de que vamos a lanzar Nginx.
echo "[nginx] Arrancando Nginx..."

# Lanzamos Nginx en primer plano:
# exec: reemplaza el proceso actual (el script shell) con el proceso de Nginx.
#       Nginx toma el proceso PID 1 del contenedor.
#       Esto es crucial para que el contenedor permanezca activo, permitiendo
#       que se pare correctamente cuando haces 'docker stop'.
exec nginx -g 'daemon off;'