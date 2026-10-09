import java.util.Random;

public class GeneradorId {

    private static final Random RANDOM = new Random();

    public static String nuevo(String archivo) {
        String id;
        do {
            char letra = (char) ('A' + RANDOM.nextInt(26));
            int numeros = RANDOM.nextInt(1_000_000);
            id = letra + String.format("%06d", numeros);
        } while (ArchivoUtil.existeValor(archivo, 0, id));
        return id;
    }
}
