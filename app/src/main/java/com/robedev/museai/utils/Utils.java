package com.robedev.museai.utils;

public class Utils {

    public static String formatArtistName(String artistName) {
        if (artistName == null || artistName.trim().isEmpty()) {
            return artistName; // Retorna null o vacío si lo es
        }

        // Quitar paréntesis y el texto dentro de ellos
        artistName = artistName.replaceAll("\\s*\\([^)]*\\)", "").trim();

        // Dividir el nombre en palabras
        String[] words = artistName.split("\\s+");

        // Si el nombre tiene dos palabras o menos, no formatear
        if (words.length <= 2) {
            return artistName;
        }

        StringBuilder formattedName = new StringBuilder();
        boolean hasVan = false;

        // Procesar las palabras
        for (String word : words) {
            // Comprobar si la palabra es "van"
            if (word.equalsIgnoreCase("van") && !hasVan) {
                formattedName.append(word).append(" "); // Agregar "van" y dejar un espacio
                hasVan = true; // Marcar que "van" ya fue agregado
            } else {
                // Agregar las demás palabras en la siguiente línea
                formattedName.append(word).append("\n");
            }
        }

        // Quitar el último salto de línea para evitar una línea vacía al final
        if (formattedName.length() > 0 && formattedName.charAt(formattedName.length() - 1) == '\n') {
            formattedName.setLength(formattedName.length() - 1);
        }

        return formattedName.toString().trim(); // Retornar el nombre formateado
    }

    public static String formatArtistNameForFile(String artistName) {
        // Eliminar caracteres especiales y espacios, y convertir a minúsculas
        String formattedName = artistName
                .toLowerCase()                       // Convertir a minúsculas
                .replaceAll("[^a-zA-Z0-9\\sáéíóúüÁÉÍÓÚÜ]", "") // Eliminar caracteres especiales excepto acentos y Ü ü
                .replaceAll("\\s+", "_");           // Reemplazar espacios con guiones bajos

        return formattedName;
    }
}
