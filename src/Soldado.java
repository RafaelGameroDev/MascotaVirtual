public class Soldado {
    private String tipo;
    private String nombre;
    private int salud;
    private int ataque;
    private int defensa;
    private int velocidad;
    private int nivelTipo;

    public Soldado(String tipo, int nivelMascota) {
        this.tipo = tipo;
        this.nivelTipo = Math.max(1, nivelMascota + ((int)(Math.random() * 3) - 1));

        if (tipo.equals("Soldado")) {
            this.nombre = "Soldado Razo";
            this.salud = 30 + (nivelTipo * 5);
            this.ataque = 8 + (nivelTipo * 2);
            this.defensa = 3 + nivelTipo;
            this.velocidad = 10 + nivelTipo;
        } else if (tipo.equals("Juggernaut")) {
            this.nombre = "Juggernaut Acorazado";
            this.salud = 50 + (nivelTipo * 8);
            this.ataque = 12 + (nivelTipo * 2);
            this.defensa = 6 + nivelTipo;
            this.velocidad = 8 + nivelTipo;
        } else if (tipo.equals("Tanque")) {
            this.nombre = "Tanque Pesado";
            this.salud = 80 + (nivelTipo * 10);
            this.ataque = 15 + (nivelTipo * 3);
            this.defensa = 10 + nivelTipo;
            this.velocidad = 6 + nivelTipo;
        } else {
            this.nombre = "Soldado";
            this.salud = 30;
            this.ataque = 8;
            this.defensa = 3;
            this.velocidad = 10;
        }
    }

    public String getTipo() { return tipo; }
    public String getNombre() { return nombre; }
    public int getSalud() { return salud; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    public int getNivelTipo() { return nivelTipo; }

    public void recibirDanio(int danio) {
        int danioReal = Math.max(1, danio - (defensa / 2));
        salud = Math.max(salud - danioReal, 0);
        System.out.println("  " + nombre + " recibe " + danioReal + " de dano. Salud: " + salud);
    }

    public int atacar() {
        int danioBase = ataque + (nivelTipo * 2);
        int variacion = (int)(Math.random() * 6) - 3;
        return Math.max(1, danioBase + variacion);
    }

    public boolean estaVivo() {
        return salud > 0;
    }

    public void mostrarInfo() {
        System.out.println("[" + nombre + "] Nivel: " + nivelTipo + " | Salud: " + salud + " | ATK: " + ataque + " | DEF: " + defensa + " | VEL: " + velocidad);
    }
}