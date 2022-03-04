package de.codecentric.javaland.idflavors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.model.domain.spi.BasicTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.io.IOException;

public class SealedIdFeatures {
    public static class Deserialize extends JsonDeserializer<Sealed.Id<?>> {
        @Override
        public Sealed.Id<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            long raw = p.getLongValue();
            return new Sealed.Id(raw);
        }
    }

    public static class Serialize extends JsonSerializer<Sealed.Id<?>> {
        @Override
        public void serialize(Sealed.Id<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.getRaw());
        }
    }

    private static class IdLongTypeDescription implements JavaTypeDescriptor<Sealed.Id> {
        final static IdLongTypeDescription INSTANCE = new IdLongTypeDescription();


        @Override
        public Class<Sealed.Id> getJavaTypeClass() {
            return Sealed.Id.class;
        }

        @Override
        public Sealed.Id fromString(String string) {
            return new Sealed.Id(Long.parseLong(string));
        }

        @Override
        public <X> X unwrap(Sealed.Id value, Class<X> type, WrapperOptions options) {
            // just for example
            if (type == Long.class) {
                return type.cast(value.getRaw());
            }
            return null;
        }

        @Override
        public <X> Sealed.Id wrap(X value, WrapperOptions options) {
            if (value instanceof Long) {
                return new Sealed.Id((Long) value);
            }
            return null;
        }
    }

    public class IdLongColumnType extends AbstractSingleColumnStandardBasicType<Sealed.Id> {

        public IdLongColumnType() {
            super(BigIntTypeDescriptor.INSTANCE, IdLongTypeDescription.INSTANCE);
        }

        @Override
        public String getName() {
            return "SealedId";
        }
    }
}
