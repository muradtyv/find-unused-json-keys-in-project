package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        String azJsonPath =
                "***\\az.json";
        String tsxFilePath = "***\\src";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode mainNode = objectMapper.readTree(new File(azJsonPath));

        List<String> allKeys = new ArrayList<>();
        getAllKeys(mainNode, allKeys);

//        Iterator<Map.Entry<String, JsonNode>> fields = mainNode.fields();
//
//        while (fields.hasNext()) {
//            Map.Entry<String, JsonNode> field = fields.next();
//            allKeys.add(field.getKey());
//        }

//        System.out.println(allKeys);

        List<String> unusedKeys = new ArrayList<>(allKeys);

        findAndCheckFiles(new File(tsxFilePath), unusedKeys);

        System.out.println("unusedKeys: " + unusedKeys);
    }

    private static void findAndCheckFiles(File directory, List<String> unusedKeys) throws IOException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                findAndCheckFiles(file, unusedKeys);
            } else if (file.getName().endsWith(".tsx")) {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
//                System.out.println(content + " content ");
                unusedKeys.removeIf(content::contains);

//                System.out.println(unusedKeys + " istifade olunmayan keyler1 ");
            }
        }
    }

    private static void getAllKeys(JsonNode node, List<String> keys) {
        if(node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
//                System.out.println(field.getKey());
                keys.add(field.getKey());
                getAllKeys(field.getValue(), keys);
            }
        }
    }
}