package sample; /**
 * Created by green on 3/17/2019.
 */
import xmls.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XsdUtils {
    private static JAXBContext context = createJC();

    public static  <T> T serializeFromXml(String xml, Class<T> tClass) throws JAXBException{
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return  (T) jaxbUnmarshaller.unmarshal(reader);
    }

    public static  <T> String serializeToXML(T response) throws JAXBException{
        Marshaller jaxbMarshaller = context.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(response, sw);
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
