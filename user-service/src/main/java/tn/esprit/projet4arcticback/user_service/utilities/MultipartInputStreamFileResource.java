package tn.esprit.projet4arcticback.user_service.utilities;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Permet d’envoyer un InputStream comme si c’était un fichier dans un multipart.
 */
public class MultipartInputStreamFileResource extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        // Indique à Spring de ne pas tenter de lire la taille en amont
        return -1;
    }
}