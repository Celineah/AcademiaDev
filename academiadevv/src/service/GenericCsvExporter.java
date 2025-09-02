package service;

import java.lang.reflect.Field;
import java.util.List;

public class GenericCsvExporter {
    public static <T> String exportToCsv(List<T> lista, String[] colunas) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < colunas.length; i++) {
            sb.append(colunas[i]);
            if (i < colunas.length - 1) {
                sb.append(",");
            }
        }
        sb.append("\n");

        for (T item : lista) {
            for (int i = 0; i < colunas.length; i++) {
                String campo = colunas[i];
                try {
                    Object valor = getFieldValue(item, campo);
                    String valorStr = (valor != null) ? valor.toString() : "";
                    valorStr = escapeCsv(valorStr);
                    sb.append(valorStr);
                } catch (Exception e) {
                    sb.append("");
                }
                if (i < colunas.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field field = null;

        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        if (field == null) {
            throw new NoSuchFieldException("Campo '" + fieldName + "' não encontrado na classe " + obj.getClass().getName());
        }

        field.setAccessible(true);
        return field.get(obj);
    }

    private static String escapeCsv(String valor) {
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n") || valor.contains("\r")) {
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }
        return valor;
    }
}
