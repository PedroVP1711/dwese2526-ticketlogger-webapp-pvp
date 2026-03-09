package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Servicio para la gestión de ficheros subidos por el usuario.
 *
 * Los archivos se almacenan en un directorio externo configurado mediante la
 * propiedad @code app.upload-root. Dentro de ese directorio, se utiliza
 * el subdirectorio @code /uploads para las imágenes de usuario.
 *
 * Ejemplo de configuración:
 *   - application.properties: app.upload-root=/var/www/ticket-logger
 *   - variable de entorno: __APP_UPLOAD_ROOT=/var/www/ticket-logger
 *
 * Luego, Spring Boot debe estar configurado para servir:
 *   spring.web.resources.static-locations=classpath:/static/;file:${app.upload-root}
 *
 * De este modo, cualquier archivo guardado en:
 *   ${app.upload-root}/uploads/logo.png
 * será accesible vía:
 *   /uploads/logo.png
 */

@Service
public class FileStorageServices {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServices.class);


    @Value("${app.upload-root}")
    private String uploadRootPath;

    /**
     * Directorio raíz donde se almaenan ficheros externos.
     * Se obtiene de la propiedad {@code app.upload-root}.
     */

    private static final String UPLOADS_SUBDIR = "uploads";

    /**
     * Guarda un arcgivo en el sistema de archivos y devuelve la ruta web relactiva
     * para acceder al fichero (por ejemplo {@code /uploads/uuid.png})

     * @param file el archivo a guardar.
     * @return la ruta web del archivo guardado (por ejemplo {@code /uploads/abc123.png}), o {@code null} si ocurre un error.
     */

    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            logger.warn("Intento de guardar un archivo nulo o vacío.");
            return null;
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFileName = UUID.randomUUID().toString();
            if (!fileExtension.isBlank()) {
                uniqueFileName += "." + fileExtension;
            }
            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Files.createDirectories(uploadsDir);
            Path filePath = uploadsDir.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());
            logger.info("Archivo {} guardado con éxito en {}", uniqueFileName, filePath);
            return "/uploads/" + uniqueFileName;
        } catch (IOException e) {
            logger.error("Error al guardar el archivo {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Elimina un archivo del sistema de archivos.
     *
     * Admite tanto el nombre de fichero bruto (ej. {@code abc.png})
     * como una ruta web con prefijo {@code /uploads/abc.png}.
     *
     * @param filePathOrWebPath el nombre del archivo o la ruta web relativa.
     */
    public void deleteFile(String filePathOrWebPath) {
        if (filePathOrWebPath == null || filePathOrWebPath.isBlank()) {
            logger.warn("Se ha intentado eliminar un archivo con nombre/ruta vacío.");
            return;
        }

        try {
            // Si viene como /uploads/xxxx, lo normalizamos al nombre de fichero
            String fileName = normalizeFileName(filePathOrWebPath);
            Path uploadsDir = Paths.get(uploadRootPath).resolve(UPLOADS_SUBDIR);
            Path filePath = uploadsDir.resolve(fileName);

            Files.deleteIfExists(filePath);
            logger.info("Archivo {} eliminado con éxito ({})", fileName, filePath);
        } catch (IOException e) {
            logger.error("Error al eliminar el archivo {}: {}", filePathOrWebPath, e.getMessage(), e);
        }
    }

    /**
     * Obtiene la extensión del archivo (sin el punto).
     *
     * @param fileName el nombre del archivo.
     * @return la extensión del archivo o cadena vacía si no tiene extensión.
     */
    private String getFileExtension(String fileName) {
        if (fileName != null) {
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0 && lastDot < fileName.length() - 1) {
                return fileName.substring(lastDot + 1);
            }
        }
        return "";
    }

    /**
     * Normaliza una ruta web o nombre de fichero para obtener solo el nombre.
     * Ejemplos:
     *  - "/uploads/abc.png" -> "abc.png"
     *  - "abc.png"          -> "abc.png"
     *
     * @param filePathOrWebPath ruta o nombre.
     * @return nombre de fichero normalizado.
     */
    private String normalizeFileName(String filePathOrWebPath) {
        String value = filePathOrWebPath.trim();

        if (value.startsWith("/uploads/")) {
            value = value.substring("/uploads".length());
        }

        int lastSlash = value.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < value.length() - 1) {
            value = value.substring(lastSlash + 1);
        }

        return value;
    }
}
