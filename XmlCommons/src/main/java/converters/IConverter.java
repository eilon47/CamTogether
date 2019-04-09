package converters;

import javax.xml.bind.JAXBException;

public interface IConverter {

    <T> T serializeFromString(String object, Class<T> tClass) throws JAXBException ;
    <T> String serializeToString(T object) throws JAXBException;

}
