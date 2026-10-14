import java.sql.*;

public class Guardado {
    private static final String DB_URL = "jdbc:sqlite:mascota_save.db";
    private static Guardado instancia;
    private Connection conexion;
    private int slotActivo = 1;

    private Guardado() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(DB_URL);
            crearTablas();
        } catch (Exception e) {
            System.err.println("Error al inicializar SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized Guardado getInstancia() {
        if (instancia == null) {
            instancia = new Guardado();
        }
        return instancia;
    }

    private void crearTablas() throws SQLException {
        Statement stmt = conexion.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS mascota (
                slot INTEGER PRIMARY KEY CHECK (slot >= 1 AND slot <= 3),
                felicidad INTEGER DEFAULT 50,
                panza INTEGER DEFAULT 50,
                diversion INTEGER DEFAULT 50,
                higiene INTEGER DEFAULT 100,
                exp INTEGER DEFAULT 0,
                nivel INTEGER DEFAULT 1,
                cansancio INTEGER DEFAULT 100,
                salud INTEGER DEFAULT 100,
                ataque INTEGER DEFAULT 10,
                defensa INTEGER DEFAULT 5,
                velocidad INTEGER DEFAULT 10,
                saludMax INTEGER DEFAULT 100,
                etapaEvolutiva TEXT DEFAULT 'Larvoideo',
                vivo INTEGER DEFAULT 1,
                nombreSlot TEXT DEFAULT 'Vacío'
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS progreso_combate (
                slot INTEGER PRIMARY KEY CHECK (slot >= 1 AND slot <= 3),
                oleadaMaximaDesbloqueada INTEGER DEFAULT 1
            )
        """);

        for (int i = 1; i <= 3; i++) {
            stmt.execute("INSERT OR IGNORE INTO mascota (slot, nombreSlot) VALUES (" + i + ", 'Vacío')");
            stmt.execute("INSERT OR IGNORE INTO progreso_combate (slot) VALUES (" + i + ")");
        }

        stmt.close();
    }

    public int getSlotActivo() { return slotActivo; }
    public void setSlotActivo(int slot) {
        if (slot >= 1 && slot <= 3) this.slotActivo = slot;
    }

    public void mostrarSlots() {
        System.out.println("\n========== SLOTS DE GUARDADO ==========");
        try {
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT slot, nombreSlot, etapaEvolutiva, nivel, vivo FROM mascota ORDER BY slot");

            while (rs.next()) {
                int slot = rs.getInt("slot");
                String nombre = rs.getString("nombreSlot");
                String etapa = rs.getString("etapaEvolutiva");
                int nivel = rs.getInt("nivel");
                boolean vivo = rs.getInt("vivo") == 1;

                String estado;
                if (nombre.equals("Vacío")) {
                    estado = "[VACÍO]";
                } else if (!vivo) {
                    estado = "[MUERTO] " + etapa + " Nv" + nivel;
                } else {
                    estado = "[" + etapa + " Nv" + nivel + "]";
                }

                String activo = (slot == slotActivo) ? " <-- ACTUAL" : "";
                System.out.println(slot + ". " + nombre + " " + estado + activo);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error mostrando slots: " + e.getMessage());
        }
        System.out.println("========================================");
    }

    public boolean slotEstaVacio(int slot) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT nombreSlot FROM mascota WHERE slot = ?");
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();
            boolean vacio = rs.next() && rs.getString("nombreSlot").equals("Vacío");
            rs.close();
            pstmt.close();
            return vacio;
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean slotEstaMuerto(int slot) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT vivo FROM mascota WHERE slot = ?");
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();
            boolean muerto = rs.next() && rs.getInt("vivo") == 0;
            rs.close();
            pstmt.close();
            return muerto;
        } catch (SQLException e) {
            return false;
        }
    }

    public void guardarMascota(Mascota mascota) {
        try {
            String sql = """
                UPDATE mascota SET
                    felicidad = ?,
                    panza = ?,
                    diversion = ?,
                    higiene = ?,
                    exp = ?,
                    nivel = ?,
                    cansancio = ?,
                    salud = ?,
                    ataque = ?,
                    defensa = ?,
                    velocidad = ?,
                    saludMax = ?,
                    etapaEvolutiva = ?,
                    vivo = ?
                WHERE slot = ?
            """;

            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, mascota.getFelicidad());
            pstmt.setInt(2, mascota.getPanza());
            pstmt.setInt(3, mascota.getDiversion());
            pstmt.setInt(4, mascota.getHigiene());
            pstmt.setInt(5, mascota.getExp());
            pstmt.setInt(6, mascota.getNivel());
            pstmt.setInt(7, mascota.getCansancio());
            pstmt.setInt(8, mascota.getSalud());
            pstmt.setInt(9, mascota.getAtaque());
            pstmt.setInt(10, mascota.getDefensa());
            pstmt.setInt(11, mascota.getVelocidad());
            pstmt.setInt(12, mascota.getSaludMax());
            pstmt.setString(13, mascota.getEtapaEvolutiva());
            pstmt.setInt(14, mascota.isVivo() ? 1 : 0);
            pstmt.setInt(15, slotActivo);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error guardando mascota en slot " + slotActivo + ": " + e.getMessage());
        }
    }

    public void guardarProgresoCombate(int oleadaMaxima) {
        try {
            String sql = "UPDATE progreso_combate SET oleadaMaximaDesbloqueada = ? WHERE slot = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setInt(1, oleadaMaxima);
            pstmt.setInt(2, slotActivo);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error guardando progreso en slot " + slotActivo + ": " + e.getMessage());
        }
    }

    public void guardarNombreSlot(String nombre) {
        try {
            String sql = "UPDATE mascota SET nombreSlot = ? WHERE slot = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setInt(2, slotActivo);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error guardando nombre: " + e.getMessage());
        }
    }

    public boolean cargarMascota(Mascota mascota, int slot) {
        this.slotActivo = slot;
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT * FROM mascota WHERE slot = ?");
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getString("nombreSlot").equals("Vacío")) {
                    rs.close();
                    pstmt.close();
                    return false;
                }

                mascota.cargarDesdeBD(
                        rs.getInt("felicidad"),
                        rs.getInt("panza"),
                        rs.getInt("diversion"),
                        rs.getInt("higiene"),
                        rs.getInt("exp"),
                        rs.getInt("nivel"),
                        rs.getInt("cansancio"),
                        rs.getInt("salud"),
                        rs.getInt("ataque"),
                        rs.getInt("defensa"),
                        rs.getInt("velocidad"),
                        rs.getInt("saludMax"),
                        rs.getString("etapaEvolutiva"),
                        rs.getInt("vivo") == 1
                );
                rs.close();
                pstmt.close();
                return true;
            }
            rs.close();
            pstmt.close();
            return false;
        } catch (SQLException e) {
            System.err.println("Error cargando mascota del slot " + slot + ": " + e.getMessage());
            return false;
        }
    }

    public int cargarProgresoCombate(int slot) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT oleadaMaximaDesbloqueada FROM progreso_combate WHERE slot = ?");
            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int progreso = rs.getInt("oleadaMaximaDesbloqueada");
                rs.close();
                pstmt.close();
                return progreso;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error cargando progreso del slot " + slot + ": " + e.getMessage());
        }
        return 1;
    }

    public void borrarSlot(int slot) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement(
                    "UPDATE mascota SET felicidad=50, panza=50, diversion=50, higiene=100, exp=0, nivel=1, " +
                            "cansancio=100, salud=100, ataque=10, defensa=5, velocidad=10, saludMax=100, " +
                            "etapaEvolutiva='Larvoideo', vivo=1, nombreSlot='Vacío' WHERE slot = ?"
            );
            pstmt.setInt(1, slot);
            pstmt.executeUpdate();
            pstmt.close();

            pstmt = conexion.prepareStatement("UPDATE progreso_combate SET oleadaMaximaDesbloqueada=1 WHERE slot = ?");
            pstmt.setInt(1, slot);
            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("Slot " + slot + " borrado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error borrando slot " + slot + ": " + e.getMessage());
        }
    }

    public void borrarTodo() {
        for (int i = 1; i <= 3; i++) {
            borrarSlot(i);
        }
        System.out.println("Todos los slots han sido borrados.");
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexion: " + e.getMessage());
        }
    }
}