import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Guardado guardado = Guardado.getInstancia();

        System.out.println("   BIENVENIDO A STARTROOP SHIPPERS");
        System.out.println("         MASCOTA ALIENIGENA");
        System.out.println("   (Había que esquivar el copyright)");

        int slotSeleccionado = 0;
        boolean slotValido = false;

        while (!slotValido) {
            guardado.mostrarSlots();
            System.out.println("\nOpciones:");
            System.out.println("1-3. Seleccionar slot");
            System.out.println("4. Borrar un slot");
            System.out.println("5. Borrar TODOS los slots");
            System.out.print("\nElige: ");

            int opcionInicio = scanner.nextInt();

            if (opcionInicio >= 1 && opcionInicio <= 3) {
                slotSeleccionado = opcionInicio;

                if (guardado.slotEstaVacio(slotSeleccionado)) {
                    System.out.println("\n>>> SLOT " + slotSeleccionado + " VACÍO <<<");
                    System.out.print("Nombre de tu mascota: ");
                    scanner.nextLine();
                    String nombreMascota = scanner.nextLine();
                    if (nombreMascota.trim().isEmpty()) nombreMascota = "Larvoideo";

                    guardado.setSlotActivo(slotSeleccionado);
                    guardado.guardarNombreSlot(nombreMascota);
                    System.out.println("\n¡" + nombreMascota + " ha nacido en el slot " + slotSeleccionado + "!");
                    slotValido = true;

                } else if (guardado.slotEstaMuerto(slotSeleccionado)) {
                    System.out.println("\n⚠ Esta mascota ha MUERTO.");
                    System.out.println("1. Ver pantalla de GAME OVER y salir");
                    System.out.println("2. Borrar este slot y empezar de nuevo");
                    System.out.print("Elige: ");
                    int opcionMuerto = scanner.nextInt();

                    if (opcionMuerto == 2) {
                        guardado.borrarSlot(slotSeleccionado);
                        System.out.println("\nSlot borrado. Volviendo a selección...");
                    } else {
                        guardado.setSlotActivo(slotSeleccionado);
                        slotValido = true;
                    }
                } else {
                    guardado.setSlotActivo(slotSeleccionado);
                    System.out.println("\n>>> Cargando partida del slot " + slotSeleccionado + "... <<<");
                    slotValido = true;
                }

            } else if (opcionInicio == 4) {
                System.out.print("¿Qué slot quieres borrar (1-3)? ");
                int slotBorrar = scanner.nextInt();
                if (slotBorrar >= 1 && slotBorrar <= 3) {
                    if (guardado.slotEstaVacio(slotBorrar)) {
                        System.out.println("Ese slot ya está vacío.");
                    } else {
                        System.out.print("¿Seguro? Esta acción no se puede deshacer (s/n): ");
                        String confirmar = scanner.next();
                        if (confirmar.equalsIgnoreCase("s")) {
                            guardado.borrarSlot(slotBorrar);
                        } else {
                            System.out.println("Borrado cancelado.");
                        }
                    }
                } else {
                    System.out.println("Slot inválido.");
                }

            } else if (opcionInicio == 5) {
                System.out.print("¿Seguro que quieres borrar TODOS los slots? (s/n): ");
                String confirmar = scanner.next();
                if (confirmar.equalsIgnoreCase("s")) {
                    guardado.borrarTodo();
                } else {
                    System.out.println("Borrado cancelado.");
                }
            } else {
                System.out.println("Opción inválida.");
            }
        }

        Mascota miMascota = new Mascota();
        boolean partidaCargada = guardado.cargarMascota(miMascota, slotSeleccionado);

        if (!partidaCargada) {
            System.out.println("\nTu Larvoideo acaba de nacer!");
            System.out.println("Etapa inicial: " + miMascota.getEtapaEvolutiva());
        } else if (!miMascota.isVivo()) {
            System.out.println("\n=========================================");
            System.out.println("              GAME OVER");
            System.out.println("    Tu mascota no sobrevivio.");
            System.out.println("   La Federacion lo lamenta!");
            System.out.println("=========================================");
            System.out.println("\nVuelve a iniciar el juego para jugar en otro slot.");
            guardado.cerrar();
            System.exit(0);
        }

        TemporizadorMascota temporizador = new TemporizadorMascota(miMascota);
        Thread hiloTiempo = new Thread(temporizador);
        hiloTiempo.start();

        SistemaCombate combate = new SistemaCombate(miMascota);

        boolean jugando = true;

        while (jugando && miMascota.isVivo()) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("[Slot " + slotSeleccionado + "]");
            System.out.println("1. Comer");
            System.out.println("2. Jugar");
            System.out.println("3. Descansar");
            System.out.println("4. COMBATE (modo oleadas)");
            System.out.println("5. Ver estado completo");
            System.out.println("6. Cambiar de slot (guarda y sale)");
            System.out.println("7. Borrar este slot y salir");
            System.out.println("8. Salir del juego");
            System.out.print("Elige una opcion: ");

            int opcion = scanner.nextInt();

            switch(opcion) {
                case 1:
                    System.out.println("1. Snack (+25 panza) [Basico]");
                    if (miMascota.getEtapaEvolutiva().equals("Aliendescente") ||
                            miMascota.getEtapaEvolutiva().equals("Alphalien")) {
                        System.out.println("2. Plato completo (+50 panza) [Intermedio]");
                    }
                    if (miMascota.getEtapaEvolutiva().equals("Alphalien")) {
                        System.out.println("3. Festin (+100 panza) [Avanzado]");
                    }
                    System.out.print("Opcion: ");
                    int comida = scanner.nextInt();
                    if (comida == 1) miMascota.comerPoco();
                    else if (comida == 2) miMascota.comerMucho();
                    else if (comida == 3) miMascota.comerLleno();
                    break;
                case 2:
                    System.out.println("1. Jugar poco (+25 diversion) [Basico]");
                    if (miMascota.getEtapaEvolutiva().equals("Aliendescente") ||
                            miMascota.getEtapaEvolutiva().equals("Alphalien")) {
                        System.out.println("2. Jugar normal (+50 diversion) [Intermedio]");
                    }
                    if (miMascota.getEtapaEvolutiva().equals("Alphalien")) {
                        System.out.println("3. Jugar mucho (+100 diversion) [Avanzado]");
                    }
                    System.out.print("Opcion: ");
                    int juego = scanner.nextInt();
                    if (juego == 1) miMascota.jugarPoco();
                    else if (juego == 2) miMascota.jugarNormal();
                    else if (juego == 3) miMascota.jugarMucho();
                    break;
                case 3:
                    miMascota.descansar();
                    break;
                case 4:
                    combate.mostrarOleadasDisponibles();
                    int maxOleada = combate.getOleadaMaximaDesbloqueada();
                    System.out.print("\nElige oleada (1-" + maxOleada + "): ");
                    int oleada = scanner.nextInt();

                    if (oleada >= 1 && oleada <= maxOleada) {
                        combate.iniciarOleada(oleada);
                    } else {
                        System.out.println("Oleada no valida o aun bloqueada.");
                    }
                    break;
                case 5:
                    miMascota.mostrarEstado();
                    break;
                case 6:
                    System.out.println("Guardando y volviendo a selección de slots...");
                    jugando = false;
                    temporizador.detener();
                    guardado.cerrar();
                    main(args);
                    return;
                case 7:
                    System.out.print("¿Seguro que quieres BORRAR este slot? (s/n): ");
                    String confirmar = scanner.next();
                    if (confirmar.equalsIgnoreCase("s")) {
                        guardado.borrarSlot(slotSeleccionado);
                        System.out.println("Slot borrado. Volviendo a selección...");
                        jugando = false;
                        temporizador.detener();
                        guardado.cerrar();
                        main(args);
                        return;
                    } else {
                        System.out.println("Borrado cancelado.");
                    }
                    break;
                case 8:
                    System.out.println("Hasta luego, recluta!");
                    jugando = false;
                    temporizador.detener();
                    break;
                default:
                    System.out.println("Opcion invalida");
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

        if (!miMascota.isVivo()) {
            System.out.println("\n=========================================");
            System.out.println("              GAME OVER");
            System.out.println("    Tu mascota no sobrevivio.");
            System.out.println("   La Federacion lo lamenta!");
            System.out.println("=========================================");
            System.out.println("\nTu partida ha sido guardada en el slot " + slotSeleccionado + ".");
            System.out.println("Vuelve a iniciar para ver opciones.");
        }

        scanner.close();
        temporizador.detener();
        guardado.cerrar();
        System.exit(0);
    }
}