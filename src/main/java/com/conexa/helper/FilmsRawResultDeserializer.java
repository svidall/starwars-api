package com.conexa.helper;


import com.conexa.model.FilmsRawResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilmsRawResultDeserializer extends StdDeserializer<List<FilmsRawResult>> {

    public FilmsRawResultDeserializer() {
        this(null);
    }
    public FilmsRawResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<FilmsRawResult> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);

        List<FilmsRawResult> results = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode item : node) {
                results.add(codec.treeToValue(item, FilmsRawResult.class));
            }
        } else if (node.isObject()) {
            results.add(codec.treeToValue(node, FilmsRawResult.class));
        }

        return results;
    }
}
