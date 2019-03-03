package handlers;

import xsd.Message;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;


public abstract class CommandHandler {

    protected <T> T serializeFromXml(String xml, Class<T> tClass) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return  (T) jaxbUnmarshaller.unmarshal(reader);
    }

    protected <T> String serializeToXML(T response) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(response.toString());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(response, sw);
        return sw.toString();
    }

    public abstract Message handle(Message request) throws JAXBException;
}
