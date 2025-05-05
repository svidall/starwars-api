package com.conexa.util;


import com.conexa.model.RawResultPeople;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RawResultPeopleDeserializer extends StdDeserializer<RawResultPeople[]> {

    public RawResultPeopleDeserializer() {
        this(null);
    }

    public RawResultPeopleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RawResultPeople[] deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<RawResultPeople> results = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                results.add(codec.treeToValue(item, RawResultPeople.class));
            }
        } else if (node.isObject()) {
            results.add(codec.treeToValue(node, RawResultPeople.class));
        }

        return results.toArray(new RawResultPeople[0]);
    }
}
