package ru.itis.semesterwork.utils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageSearch {
    private static Map<String, List<Image>> map = new HashMap<>();

    public static void putImage(String imageName, List<Image> images) {
        map.put(imageName, images);
    }

    public static List<Image> getImages(String imageName) {
        return map.get(imageName);
    }
}
