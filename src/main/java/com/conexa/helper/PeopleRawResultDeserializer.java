package com.conexa.helper;


import com.conexa.model.PeopleRawResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PeopleRawResultDeserializer extends StdDeserializer<List<PeopleRawResult>> {

    public PeopleRawResultDeserializer() {
        this(null);
    }


    public PeopleRawResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<PeopleRawResult> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<PeopleRawResult> results = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                results.add(codec.treeToValue(item, PeopleRawResult.class));
            }
        } else if (node.isObject()) {
            results.add(codec.treeToValue(node, PeopleRawResult.class));
        }

        return results;
    }
}
