package com.ehabibov.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ContentWellnessValidator {

    public static boolean isValidJSON(final byte[] json) {
        boolean valid = true;
        try{
            new ObjectMapper().readTree(new ByteArrayInputStream(json));
        } catch(IOException e){
            valid = false;
        }
        return valid;
    }

    public static boolean isValidXML(final byte[] byteArray) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (
                ParserConfigurationException e) {
            e.printStackTrace();
        }

        InputSource a = new InputSource();
        a.setByteStream(new ByteArrayInputStream(byteArray));
        try {
            builder.parse(a);
        } catch (SAXException | IOException e) {
            return false;
        }
        return true;
    }
}
