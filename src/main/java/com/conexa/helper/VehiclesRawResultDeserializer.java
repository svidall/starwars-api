package com.conexa.helper;


import com.conexa.model.StarshipsRawResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehiclesRawResultDeserializer extends StdDeserializer<List<StarshipsRawResult>> {

    public VehiclesRawResultDeserializer() {
        this(null);
    }
    public VehiclesRawResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<StarshipsRawResult> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<StarshipsRawResult> results = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                results.add(codec.treeToValue(item, StarshipsRawResult.class));
            }
        } else if (node.isObject()) {
            results.add(codec.treeToValue(node, StarshipsRawResult.class));
        }

        return results;
    }
}
