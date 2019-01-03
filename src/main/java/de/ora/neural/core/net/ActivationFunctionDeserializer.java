package de.ora.neural.core.net;

import de.ora.neural.core.activation.ActivationFunction;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class ActivationFunctionDeserializer extends JsonDeserializer<ActivationFunction> {
    @Override
    public ActivationFunction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String type = jsonParser.getText();
        try {
            Class<?> aClass = Class.forName(type);
            return (ActivationFunction) aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
