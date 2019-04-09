package converters;

import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import xmls.Album;
import xmls.ObjectFactory;
import xmls.User;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonConverter implements IConverter {
    private static JAXBContext context = createJC();

    @Override
    public <T> T serializeFromString(String object, Class<T> tClass) throws JAXBException {
        Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
        //Set JSON type
        jaxbUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
        jaxbUnmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
        return (T) jaxbUnmarshaller.unmarshal(new StringReader(object));
    }

    @Override
    public <T> String serializeToString(T object) throws JAXBException {
        Marshaller jaxbMarshaller = context.createMarshaller();
        // To format JSON
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //Set JSON type
        jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);

        //Print JSON String to Console
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(object, sw);
        return sw.toString();
    }

    private static JAXBContext createJC() {
        //Set the various properties you want
        Map<String, Object> properties = new HashMap<>();
        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
        try {
            return JAXBContextFactory.createContext(new Class[]{ObjectFactory.class}, properties);
        } catch (JAXBException je) {
            je.printStackTrace();
            return null;
        }
    }
//    public static void main(String args[]) throws JAXBException {
//        User user = new User();
//        user.setUserID("dvfd");
//        user.setUserName("vfdvfd");
//        Album album = new Album();
//        album.setAlbumName("name");
//        album.setDescription("cdvdvfd");
//        album.setManager(user);
//
//        IConverter converter = new JsonConverter();
//        String xml = converter.serializeToString(album);
//        System.out.println(xml);
//        Album album1 = converter.serializeFromString(xml);
//
//        User user1 = album1.getManager();
//        System.out.println(user1.getUserID());
//    }
}
