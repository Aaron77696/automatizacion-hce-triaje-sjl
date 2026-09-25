
    public static void listarUsuarios() {
        for (String linea : ArchivoUtil.leerLineas(ARCHIVO)) {
            String[] partes = linea.split("\\|");
            System.out.println("ID: " + partes[0] + " | Usuario: " + partes[1] + " | Rol: " + partes[3]);
        }
    }