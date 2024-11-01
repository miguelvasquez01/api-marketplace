package com.marketplace.api.services;

import com.marketplace.api.exceptions.ArchivoNotFoundException;
import com.marketplace.api.exceptions.ArchivoVacioException;
import org.springframework.stereotype.Service;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class XMLSerializador {

    private static final Logger LOGGER = Logger.getLogger(XMLSerializador.class.getName());

    static {
        LoggerConfigService.getInstance().configureLogger(LOGGER);
    }

    public <T> void serializarObjeto(String ruta, T objeto) throws FileNotFoundException {
        LOGGER.log(Level.INFO, "Serializando objeto en la ruta: " + ruta);

        XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta));
        encoder.writeObject(objeto);
        encoder.close();

        LOGGER.log(Level.INFO, "Objeto serializado correctamente en la ruta: " + ruta);
    }

    public <T> void serializarLista(String ruta, List<T> lista) {
        LOGGER.log(Level.INFO, "Serializando lista en la ruta: " + ruta);

        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta))) {
            encoder.writeObject(lista);
            LOGGER.log(Level.INFO, "Lista serializada correctamente en la ruta: " + ruta);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Archivo no encontrado al intentar serializar en la ruta: " + ruta, e);
            throw new ArchivoNotFoundException(ruta);
        }
    }

    public <T> T deserializarObjeto(String ruta) throws FileNotFoundException {
        LOGGER.log(Level.INFO, "Deserializando objeto desde la ruta: " + ruta);

        XMLDecoder decoder = new XMLDecoder(new FileInputStream(ruta));
        @SuppressWarnings("unchecked")
        T objeto = (T) decoder.readObject();
        decoder.close();

        LOGGER.log(Level.INFO, "Objeto deserializado correctamente desde la ruta: " + ruta);
        return objeto;
    }

    public <T> List<T> deserializarLista(String ruta) {
        LOGGER.log(Level.INFO, "Deserializando lista desde la ruta: " + ruta);

        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(ruta))) {
            @SuppressWarnings("unchecked")
            List<T> lista = (List<T>) decoder.readObject();
            LOGGER.log(Level.INFO, "Lista deserializada correctamente desde la ruta: " + ruta);
            return lista;
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Archivo no encontrado al intentar deserializar desde la ruta: " + ruta, e);
            throw new ArchivoNotFoundException(ruta);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al deserializar la lista o archivo vacío en la ruta: " + ruta, e);
            throw new ArchivoVacioException(ruta);
        }
    }
}
