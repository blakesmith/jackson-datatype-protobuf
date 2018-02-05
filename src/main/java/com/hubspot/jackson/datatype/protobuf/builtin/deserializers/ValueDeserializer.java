package com.hubspot.jackson.datatype.protobuf.builtin.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.NullValue;
import com.google.protobuf.Value;
import com.hubspot.jackson.datatype.protobuf.ProtobufDeserializer;

public class ValueDeserializer extends ProtobufDeserializer<Value, Value.Builder> {
  private static final FieldDescriptor STRUCT_FIELD = Value.getDescriptor().findFieldByName("struct_value");
  private static final FieldDescriptor LIST_FIELD = Value.getDescriptor().findFieldByName("list_value");

  public ValueDeserializer() {
    super(Value.class);
  }

  @Override
  protected void populate(
          Value.Builder builder,
          JsonParser parser,
          DeserializationContext context
  ) throws IOException {
    switch (parser.getCurrentToken()) {
      case START_OBJECT:
        readValue(builder, STRUCT_FIELD, null, parser, context);
        return;
      case START_ARRAY:
        readValue(builder, LIST_FIELD, null, parser, context);
        return;
      case VALUE_STRING:
        builder.setStringValue(parser.getText());
        return;
      case VALUE_NUMBER_INT:
      case VALUE_NUMBER_FLOAT:
        builder.setNumberValue(parser.getValueAsDouble());
        return;
      case VALUE_TRUE:
        builder.setBoolValue(true);
        return;
      case VALUE_FALSE:
        builder.setBoolValue(false);
        return;
      case VALUE_NULL:
        builder.setNullValue(NullValue.NULL_VALUE);
        return;
      default:
        String message = "Can not deserialize instance of com.google.protobuf.Value out of " + parser.currentToken() + " token";
        context.reportInputMismatch(Value.class, message);
        // the previous method should have thrown
        throw new AssertionError();
    }
  }
}
