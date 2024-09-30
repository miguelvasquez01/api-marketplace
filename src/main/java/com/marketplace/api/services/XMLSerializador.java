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

@Service
public class XMLSerializador {
//Los genéricos me permiten no castear los objetos, por eso no se usa la clase Object


    public <T> void serializarObjeto(String ruta, T objeto) throws FileNotFoundException {
        XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta));
        encoder.writeObject(objeto);
        encoder.close();
    }

    public <T> void serializarLista(String ruta, List<T> lista) {
        //try-with-resources cierra el flujo automáticamente
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(ruta))) {
            encoder.writeObject(lista);
        } catch (FileNotFoundException e) {
            throw new ArchivoNotFoundException(ruta);
        }
    }

    public <T> T deserializarObjeto(String ruta) throws FileNotFoundException {
        XMLDecoder decoder = new XMLDecoder(new FileInputStream(ruta));
        @SuppressWarnings("unchecked")
        T objeto = (T) decoder.readObject();
        decoder.close();
        return objeto;
    }

    public <T> List<T> deserializarLista(String ruta) {
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(ruta))) {
            @SuppressWarnings("unchecked")
            List<T> lista = (List<T>) decoder.readObject();
            return lista;
        } catch (FileNotFoundException e) {
            throw new ArchivoNotFoundException(ruta);
        } catch (Exception e) {
            throw new ArchivoVacioException(ruta);
        }
    }
}
