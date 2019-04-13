package converters;

import xmls.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;


public class XmlConverter implements IConverter {
    private static JAXBContext context = createJC();

    @Override
    public <T> T serializeFromString(String object, Class<T> tClass) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(object);
        return  (T) jaxbUnmarshaller.unmarshal(reader);
    }

    @Override
    public <T> String serializeToString(T object) throws JAXBException{
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(object, sw);
        return sw.toString();
    }

    private static JAXBContext createJC(){
        JAXBContext context = null;
        try{
            context = JAXBContext.newInstance(ObjectFactory.class);
        } catch (JAXBException ex){
            ex.printStackTrace(); //TODO logger
        }
        return context;
    }
}
