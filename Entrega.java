import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javax.sound.sampled.SourceDataLine;


/*
 * Aquesta entrega consisteix en implementar tots els mètodes anomenats "exerciciX". Ara mateix la
 * seva implementació consisteix en llançar `UnsupportedOperationException`, ho heu de canviar així
 * com els aneu fent.
 *
 * Criteris d'avaluació:
 *
 * - Si el codi no compila tendreu un 0.
 *
 * - Les úniques modificacions que podeu fer al codi són:
 *    + Afegir un mètode (dins el tema que el necessiteu)
 *    + Afegir proves a un mètode "tests()"
 *    + Òbviament, implementar els mètodes que heu d'implementar ("exerciciX")
 *   Si feu una modificació que no sigui d'aquesta llista, tendreu un 0.
 *
 * - Principalment, la nota dependrà del correcte funcionament dels mètodes implementats (provant
 *   amb diferents entrades).
 *
 * - Tendrem en compte la neteja i organització del codi. Un estandard que podeu seguir és la guia
 *   d'estil de Google per Java: https://google.github.io/styleguide/javaguide.html . Per exemple:
 *    + IMPORTANT: Aquesta entrega està codificada amb UTF-8 i finals de línia LF.
 *    + Indentació i espaiat consistent
 *    + Bona nomenclatura de variables
 *    + Declarar les variables el més aprop possible al primer ús (és a dir, evitau blocs de
 *      declaracions).
 *    + Convé utilitzar el for-each (for (int x : ...)) enlloc del clàssic (for (int i = 0; ...))
 *      sempre que no necessiteu l'índex del recorregut. Igualment per while si no és necessari.
 *
 * Per com està plantejada aquesta entrega, no necessitau (ni podeu) utilitzar cap `import`
 * addicional, ni qualificar classes que no estiguin ja importades. El que sí podeu fer és definir
 * tots els mètodes addicionals que volgueu (de manera ordenada i dins el tema que pertoqui).
 *
 * Podeu fer aquesta entrega en grups de com a màxim 3 persones, i necessitareu com a minim Java 10.
 * Per entregar, posau els noms i cognoms de tots els membres del grup a l'array `Entrega.NOMS` que
 * està definit a la línia 53.
 *
 * L'entrega es farà a través d'una tasca a l'Aula Digital que obrirem abans de la data que se us
 * hagui comunicat. Si no podeu visualitzar bé algun enunciat, assegurau-vos de que el vostre editor
 * de texte estigui configurat amb codificació UTF-8.
 */
class Entrega {

    static final String[] NOMS = {"MANUEL MAYA CONDE", "ZHIJUN LIN"};

    /*
   * Aquí teniu els exercicis del Tema 1 (Lògica).
     */
    static class Tema1 {

        /*
     * Determinau si l'expressió és una tautologia o no:
     *
     * (((vars[0] ops[0] vars[1]) ops[1] vars[2]) ops[2] vars[3]) ...
     *
     * Aquí, vars.length == ops.length+1, i cap dels dos arrays és buid. Podeu suposar que els
     * identificadors de les variables van de 0 a N-1, i tenim N variables diferents (mai més de 20
     * variables).
     *
     * Cada ops[i] pot ser: CONJ, DISJ, IMPL, NAND.
     *
     * Retornau:
     *   1 si és una tautologia
     *   0 si és una contradicció
     *   -1 en qualsevol altre cas.
     *
     * Vegeu els tests per exemples.
         */
        static final char CONJ = '∧';
        static final char DISJ = '∨';
        static final char IMPL = '→';
        static final char NAND = '.';

        static int exercici1(char[] ops, int[] vars) {
            // Obtiene una lista sin variables repetidas
            ArrayList<Integer> valores = revisarComponentesRepetidas(vars);
            // Genera la tabla de verdad para esas variables
            boolean[][] tablaVerdad = generarTablaVerdad(valores.size());
            // Aplica las operaciones lógicas sobre cada fila de la tabla
            boolean[] resultados = hacerOperciones(tablaVerdad, ops, vars);
            // Evalúa si es tautología, contradicción o ninguna
            int resultadoFinal = revisarResultados(resultados);
            return resultadoFinal;
        }

        static ArrayList<Integer> revisarComponentesRepetidas(int[] vars) {
            ArrayList<Integer> valores = new ArrayList<>();

            valores.add(vars[0]);
            // Agrega solo variables únicas
            for (int var1 : vars) {
                boolean meter = true;
                for (int var2 : valores) {
                    if (var1 == var2) {
                        meter = false;
                    }
                }
                if (meter) {
                    valores.add(var1);
                }
            }
            return valores;
        }

        static int revisarResultados(boolean[] resultados) {
            int resultadFinal;

            boolean resultadoContradiccion = true;
            boolean resultadoTautologia = true;

            // Verifica si todos son true (tautología), todos false (contradicción) o mixto
            for (boolean b : resultados) {
                if (b == true) {
                    resultadoContradiccion = false;
                } else {
                    resultadoTautologia = false;
                }
            }

            if (resultadoTautologia) {
                resultadFinal = 1;
            } else if (resultadoContradiccion) {
                resultadFinal = 0;
            } else {
                resultadFinal = -1;
            }
            return resultadFinal;
        }

        static boolean[] hacerOperciones(boolean[][] tablaVerdad, char[] ops, int[] vars) {
            boolean[] resultado = new boolean[tablaVerdad.length];
            for (int i = 0; i < tablaVerdad.length; i++) {
                // Inicializa con el primer valor
                resultado[i] = tablaVerdad[i][vars[0]];
                for (int j = 0; j < ops.length; j++) {
                    // Aplica operaciones en orden
                    resultado[i] = operacion(resultado[i], tablaVerdad[i][vars[j + 1]], ops[j]);
                }
            }
            return resultado;
        }

        static boolean[][] generarTablaVerdad(int numVar) {
            int fila = (int) Math.pow(2, numVar);
            boolean[][] tablaVerdad = new boolean[fila][numVar];
            // Llena la tabla de verdad con todas las combinaciones posibles
            for (int i = 0; i < fila; i++) {
                for (int j = numVar - 1; j >= 0; j--) {
                    if ((i / (int) Math.pow(2, j)) % 2 == 1) {
                        tablaVerdad[i][j] = true;
                    } else {
                        tablaVerdad[i][j] = false;
                    }
                }
            }
            return tablaVerdad;
        }

        static boolean operacion(boolean a, boolean b, char operacion) {
            // Aplica la operación lógica correspondiente
            boolean resultado = false;
            switch (operacion) {
                case CONJ ->
                    resultado = (a && b);
                case DISJ ->
                    resultado = (a || b);
                case IMPL ->
                    resultado = (!a || b);
                case NAND ->
                    resultado = !(a && b);
            }
            return resultado;
        }

        /*
     * Aquest mètode té de paràmetre l'univers (representat com un array) i els predicats
     * adients `p` i `q`. Per avaluar aquest predicat, si `x` és un element de l'univers, podeu
     * fer-ho com `p.test(x)`, que té com resultat un booleà (true si `P(x)` és cert).
     *
     * Amb l'univers i els predicats `p` i `q` donats, returnau true si la següent proposició és
     * certa.
     *
     * (∀x : P(x)) <-> (∃!x : Q(x))
         */
        static boolean exercici2(int[] universe, Predicate<Integer> p, Predicate<Integer> q) {
            boolean todosCumplenP = true;
            boolean unicoCumpleQ = false;
            int cuentaQ = 0;
            // Recorre el conjunt
            // Verifica si todos cumplen P
            for (int x : universe) {
                if (!p.test(x)) {
                    todosCumplenP = false;
                }
                // Verifica si cumple Q
                if (q.test(x)) {
                    cuentaQ++;
                }
            }
            if (cuentaQ == 1) {
                unicoCumpleQ = true;
            }
            boolean resultado = todosCumplenP == unicoCumpleQ;
            return resultado;
        }

        static void tests() {
            // Exercici 1
            // Taules de veritat

            // Tautologia: ((p0 → p2) ∨ p1) ∨ p0
            test(1, 1, 1, () -> exercici1(new char[]{IMPL, DISJ, DISJ}, new int[]{0, 2, 1, 0}) == 1);

            // Contradicció: (p0 . p0) ∧ p0
            test(1, 1, 2, () -> exercici1(new char[]{NAND, CONJ}, new int[]{0, 0, 0}) == 0);

            // Exercici 2
            // Equivalència
            test(1, 2, 1, () -> {
                return exercici2(new int[]{1, 2, 3}, (x) -> x == 0, (x) -> x == 0);
            });

            test(1, 2, 2, () -> {
                return exercici2(new int[]{1, 2, 3}, (x) -> x >= 1, (x) -> x % 2 == 0);
            });
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 2 (Conjunts).
   *
   * Per senzillesa tractarem els conjunts com arrays (sense elements repetits). Per tant, un
   * conjunt de conjunts d'enters tendrà tipus int[][]. Podeu donar per suposat que tots els
   * arrays que representin conjunts i us venguin per paràmetre estan ordenats de menor a major.
   *
   * Les relacions també les representarem com arrays de dues dimensions, on la segona dimensió
   * només té dos elements. L'array estarà ordenat lexicogràficament. Per exemple
   *   int[][] rel = {{0,0}, {0,1}, {1,1}, {2,2}};
   * i també donarem el conjunt on està definida, per exemple
   *   int[] a = {0,1,2};
   * Als tests utilitzarem extensivament la funció generateRel definida al final (també la podeu
   * utilitzar si la necessitau).
   *
   * Les funcions f : A -> B (on A i B son subconjunts dels enters) les representam o bé amb el seu
   * graf o bé amb un objecte de tipus Function<Integer, Integer>. Sempre donarem el domini int[] a
   * i el codomini int[] b. En el cas de tenir un objecte de tipus Function<Integer, Integer>, per
   * aplicar f a x, és a dir, "f(x)" on x és d'A i el resultat f.apply(x) és de B, s'escriu
   * f.apply(x).
     */
    static class Tema2 {

        /*
     * Trobau el nombre de particions diferents del conjunt `a`, que podeu suposar que no és buid.
     *
     * Pista: Cercau informació sobre els nombres de Stirling.
         */
        static int exercici1(int[] a) {
            int numeroElementosA = a.length;

            // Tabla donde stirling[i][j] representa S(i, j)
            int[][] stirling = new int[numeroElementosA + 1][numeroElementosA + 1];

            // Caso base: hay 1 forma de particionar 0 elementos en 0 conjuntos
            stirling[0][0] = 1;

            // Rellenamos la tabla usando la fórmula recursiva vista en wikipedia
            // S(n, k) = S(n-1, k-1) + k * S(n-1, k)
            for (int elementos = 1; elementos <= numeroElementosA; elementos++) {
                for (int subconjuntos = 1; subconjuntos <= elementos; subconjuntos++) {
                    stirling[elementos][subconjuntos]
                            = stirling[elementos - 1][subconjuntos - 1]
                            + subconjuntos * stirling[elementos - 1][subconjuntos];
                }
            }

            // Sumamos S(n, k) para k = 1 hasta n → número total de particiones
            int totalParticiones = 0;
            for (int k = 1; k <= numeroElementosA; k++) {
                totalParticiones += stirling[numeroElementosA][k];
            }

            return totalParticiones;
        }

        /*
     * Trobau el cardinal de la relació d'ordre parcial sobre `a` més petita que conté `rel` (si
     * existeix). En altres paraules, el cardinal de la seva clausura reflexiva, transitiva i
     * antisimètrica.
     *
     * Si no existeix, retornau -1.
         */
        static int exercici2(int[] a, int[][] rel) {

            ArrayList<int[]> relacion = new ArrayList<>();
            for (int[] elemento : rel) {
                relacion.add(elemento);
            }
            // Comprovació de reflexivitat
            for (int elemento : a) {
                boolean existe = false;
                for (int[] elementoRel : relacion) {
                    if (elemento == elementoRel[0] && elemento == elementoRel[1]) {
                        existe = true;
                    }
                }
                if (!existe) {
                    relacion.add(new int[]{elemento, elemento});
                }
            }
            //comprovacion transitiva
            for (int parOrdenado1 = 0; parOrdenado1 < relacion.size(); parOrdenado1++) {
                for (int parOrdenado2 = 0; parOrdenado2 < relacion.size(); parOrdenado2++) {
                    boolean esTransitiva = false;
                    boolean estanRelacionados = false;
                    if (relacion.get(parOrdenado1)[1] == relacion.get(parOrdenado2)[0]) {
                        estanRelacionados = true;
                        for (int indiceAux = 0; indiceAux < relacion.size(); indiceAux++) {
                            if (relacion.get(parOrdenado1)[0] == relacion.get(indiceAux)[0] && relacion.get(parOrdenado2)[1] == relacion.get(indiceAux)[1]) {
                                esTransitiva = true;
                            }
                        }
                    }
                    if (!esTransitiva && estanRelacionados) {
                        relacion.add(new int[]{relacion.get(parOrdenado1)[0], relacion.get(parOrdenado2)[1]});
                    }
                }
            }
            //antitransitiva 
            for (int parOrdenado1 = 0; parOrdenado1 < relacion.size(); parOrdenado1++) {
                for (int parOrdenado2 = parOrdenado1 + 1; parOrdenado2 < relacion.size(); parOrdenado2++) {
                    if (relacion.get(parOrdenado1)[0] == relacion.get(parOrdenado2)[1] && relacion.get(parOrdenado1)[1] == relacion.get(parOrdenado2)[0]) {
                        if (relacion.get(parOrdenado1)[0] != relacion.get(parOrdenado1)[1]) {
                            return -1;
                        }
                    }
                }
            }
            return relacion.size();
        }

        /*
     * Donada una relació d'ordre parcial `rel` definida sobre `a` i un subconjunt `x` de `a`,
     * retornau:
     * - L'ínfim de `x` si existeix i `op` és false
     * - El suprem de `x` si existeix i `op` és true
     * - null en qualsevol altre cas
         */
        static Integer exercici3(int[] a, int[][] rel, int[] x, boolean op) {
            int n = a.length;
            boolean[][] mat = new boolean[n][n];
            for (int[] par : rel) {
                int i = index(a, par[0]);
                int j = index(a, par[1]);
                mat[i][j] = true;
            }

            List<Integer> cotas = new ArrayList<>();

            // Paso 1: encontrar todas las cotas (inferiores o superiores)
            for (int i = 0; i < n; i++) {
                boolean esCota = true;
                for (int xi : x) {
                    int idxX = index(a, xi);
                    if (op) {
                        // Buscando suprem: a[i] debe ser ≥ xi → xi ≤ a[i]
                        if (!mat[idxX][i]) {
                            esCota = false;
                            break;
                        }
                    } else {
                        // Buscando ínfim: a[i] ≤ xi
                        if (!mat[i][idxX]) {
                            esCota = false;
                            break;
                        }
                    }
                }
                if (esCota) {
                    cotas.add(i); // Guardamos el índice

                }
            }

            if (cotas.isEmpty()) {
                return null;
            }

            // Paso 2: buscar el mínimo (suprem) o máximo (ínfim) entre las cotas, si es único
            List<Integer> maximals = new ArrayList<>();
            for (int i : cotas) {
                boolean esMaximal = true;
                for (int j : cotas) {
                    if (i != j) {
                        if (op) {
                            // Suprem: buscamos el mínimo → i debe ser ≤ j
                            if (!mat[i][j]) {
                                esMaximal = false;
                            }
                        } else {
                            // Ínfim: buscamos el máximo → i debe ser ≥ j
                            if (!mat[j][i]) {
                                esMaximal = false;
                            }
                        }
                        if (!esMaximal) {
                            break;
                        }
                    }
                }
                if (esMaximal) {
                    maximals.add(i);
                }
            }

            return (maximals.size() == 1) ? a[maximals.get(0)] : null;
        }

        static int index(int[] a, int val) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == val) {
                    return i;
                }
            }
            return -1;
        }

        /*
     * Donada una funció `f` de `a` a `b`, retornau:
     *  - El graf de la seva inversa (si existeix)
     *  - Sinó, el graf d'una inversa seva per l'esquerra (si existeix)
     *  - Sinó, el graf d'una inversa seva per la dreta (si existeix)
     *  - Sinó, null.
         */
        static int[][] exercici4(int[] a, int[] b, Function<Integer, Integer> f) {
           
            // Paso 1: calcular imagen de a bajo f (pares (f(x), x))
            int[][] pares = new int[a.length][2];
            for (int i = 0; i < a.length; i++) {
                pares[i][1] = f.apply(a[i]); // f(a[i])
                pares[i][0] = a[i];
            }

            // Paso 2: contar cuántos valores distintos hay en f(x)
            int totalImagen = 0;
            for (int i = 0; i < pares.length; i++) {
                boolean repetido = false;
                for (int j = 0; j < i; j++) {
                    if (pares[i][1] == pares[j][1]) {
                        repetido = true;
                        break;
                    }
                }
                if (!repetido) {
                    totalImagen++;
                }
            }

            // Paso 3: verificar si f es biyectiva
            boolean esInyectiva = true;
            for (int i = 0; i < pares.length; i++) {
                for (int j = i + 1; j < pares.length; j++) {
                    if (pares[i][1] == pares[j][1]) {
                        esInyectiva = false;
                        break;
                    }
                }
                if (!esInyectiva) {
                    break;
                }
            }

            boolean esSobreyectiva = true;
            for (int i = 0; i < b.length; i++) {
                boolean encontrado = false;
                for (int j = 0; j < pares.length; j++) {
                    if (pares[j][1] == b[i]) {
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    esSobreyectiva = false;
                    break;
                }
            }

            // Caso 1: Inversa total (f es biyectiva)
            if (esInyectiva && esSobreyectiva) {
                int[][] inversa = new int[pares.length][2];
                for (int i = 0; i < pares.length; i++) {
                    inversa[i][1] = pares[i][1]; // f(x)
                    inversa[i][0] = pares[i][0]; // x
                }
                return inversa;
            }

            // Caso 2: Inversa por la izquierda (f inyectiva)
            if (esInyectiva) {
                // Tomamos solo los pares de la imagen
                int[][] inversaIzquierda = new int[b.length][2];
                int posicion = 0;
                for (int i : b) {
                    inversaIzquierda[posicion][0] = i;
                    posicion++;
                }
                int pos = 0;
                for (int i = 0; i < pares.length; i++) {
                    boolean repetido = false;
                    for (int j = 0; j < i; j++) {
                        if (pares[i][1] == pares[j][1]) {
                            repetido = true;
                            break;
                        }
                    }
                    if (!repetido) {
                        inversaIzquierda[pos][0] = pares[i][1]; // f(x)
                        inversaIzquierda[pos][1] = pares[i][0]; // x
                        pos++;
                    }
                }
                System.out.println("funcion");
                for (int [] elem : pares) {
                    System.out.println(Arrays.toString(elem));
                }
                System.out.println("inversa izquierda");
                for (int [] elem : inversaIzquierda) {
                    System.out.println(Arrays.toString(elem));
                }
                return inversaIzquierda;
            }

            // Caso 3: Inversa por la derecha (f sobreyectiva)
            if (esSobreyectiva) {
                // Para cada y ∈ b, buscar un x ∈ a tal que f(x) == y
                int[][] inversaDerecha = new int[b.length][2];
                for (int i = 0; i < b.length; i++) {
                    boolean encontrado = false;
                    for (int j = 0; j < a.length; j++) {
                        if (f.apply(a[j]) == b[i]) {
                            inversaDerecha[i][0] = b[i]; // y
                            inversaDerecha[i][1] = a[j]; // x tal que f(x) = y
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        return null; // no debería pasar
                    }
                }
                return inversaDerecha;
            }

            // Ningún tipo de inversa
            return null;
        }

        /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
         */
        static void tests() {
            // Exercici 1
            // Nombre de particions

            test(2, 1, 1, () -> exercici1(new int[]{1}) == 1);
            test(2, 1, 2, () -> exercici1(new int[]{1, 2, 3}) == 5);

            // Exercici 2
            // Clausura d'ordre parcial
            final int[] INT02 = {0, 1, 2};

            test(2, 2, 1, () -> exercici2(INT02, new int[][]{{0, 1}, {1, 2}}) == 6);
            test(2, 2, 2, () -> exercici2(INT02, new int[][]{{0, 1}, {1, 0}, {1, 2}}) == -1);

            // Exercici 3
            // Ínfims i suprems
            final int[] INT15 = {1, 2, 3, 4, 5};
            final int[][] DIV15 = generateRel(INT15, (n, m) -> m % n == 0);
            final Integer ONE = 1;
            final Integer TWO = 2;
            final Integer THREE = 3;
            final Integer FOUR = 4;
            final Integer FIVE = 5;

            test(2, 3, 1, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{2, 3}, false)));
            test(2, 3, 2, () -> exercici3(INT15, DIV15, new int[]{2, 3}, true) == null);
            test(2, 3, 3, () -> TWO.equals(exercici3(INT15, DIV15, new int[]{2, 4}, false)));
            test(2, 3, 4, () -> FOUR.equals(exercici3(INT15, DIV15, new int[]{2, 4}, true)));
            test(2, 3, 5, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{3, 5}, false)));
            test(2, 3, 6, () -> exercici3(INT15, DIV15, new int[]{3, 5}, true) == null);
            test(2, 3, 7, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{4, 5}, false)));
            test(2, 3, 8, () -> exercici3(INT15, DIV15, new int[]{4, 5}, true) == null);
            test(2, 3, 9, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{1, 2, 3}, false)));
            test(2, 3, 10, () -> exercici3(INT15, DIV15, new int[]{1, 2, 3}, true) == null);
            test(2, 3, 11, () -> ONE.equals(exercici3(INT15, DIV15, new int[]{2, 3, 4}, false)));
            test(2, 3, 12, () -> exercici3(INT15, DIV15, new int[]{2, 3, 4}, true) == null);

            // Exercici 4
            // Inverses
            final int[] INT05 = {0, 1, 2, 3, 4, 5};

            test(2, 4, 1, () -> {
                var inv = exercici4(INT05, INT02, (x) -> x / 2);

                if (inv == null) {
                    return false;
                }

                inv = lexSorted(inv);

                if (inv.length != INT02.length) {
                    return false;
                }

                for (int i = 0; i < INT02.length; i++) {
                    if (inv[i][0] != i || inv[i][1] / 2 != i) {
                        return false;
                    }
                }

                return true;
            });

            test(2, 4, 2, () -> {
                var inv = exercici4(INT02, INT05, (x) -> x);

                if (inv == null) {
                    return false;
                }

                inv = lexSorted(inv);

                if (inv.length != INT05.length) {
                    return false;
                }

                for (int i = 0; i < INT02.length; i++) {
                    if (inv[i][0] != i || inv[i][1] != i) {
                        return false;
                    }
                }

                return true;
            });
        }

        /*
     * Ordena lexicogràficament un array de 2 dimensions
     * Per exemple:
     *  arr = {{1,0}, {2,2}, {0,1}}
     *  resultat = {{0,1}, {1,0}, {2,2}}
         */
        static int[][] lexSorted(int[][] arr) {
            if (arr == null) {
                return null;
            }

            var arr2 = Arrays.copyOf(arr, arr.length);
            Arrays.sort(arr2, Arrays::compare);
            return arr2;
        }

        /*
     * Genera un array int[][] amb els elements {a, b} (a de as, b de bs) que satisfàn pred.test(a, b)
     * Per exemple:
     *   as = {0, 1}
     *   bs = {0, 1, 2}
     *   pred = (a, b) -> a == b
     *   resultat = {{0,0}, {1,1}}
         */
        static int[][] generateRel(int[] as, int[] bs, BiPredicate<Integer, Integer> pred) {
            var rel = new ArrayList<int[]>();

            for (int a : as) {
                for (int b : bs) {
                    if (pred.test(a, b)) {
                        rel.add(new int[]{a, b});
                    }
                }
            }

            return rel.toArray(new int[][]{});
        }

        // Especialització de generateRel per as = bs
        static int[][] generateRel(int[] as, BiPredicate<Integer, Integer> pred) {
            return generateRel(as, as, pred);
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 3 (Grafs).
   *
   * Els (di)grafs vendran donats com llistes d'adjacència (és a dir, tractau-los com diccionaris
   * d'adjacència on l'índex és la clau i els vèrtexos estan numerats de 0 a n-1). Per exemple,
   * podem donar el graf cicle no dirigit d'ordre 3 com:
   *
   * int[][] c3dict = {
   *   {1, 2}, // veïns de 0
   *   {0, 2}, // veïns de 1
   *   {0, 1}  // veïns de 2
   * };
     */
    static class Tema3 {

        /*
     * Determinau si el graf `g` (no dirigit) té cicles.
         */
        static boolean exercici1(int[][] g) {
            int vertices = g.length;
            boolean[] visitado = new boolean[vertices];
            int[] padre = new int[vertices];
            Arrays.fill(padre, -1);

            for (int inicio = 0; inicio < vertices; inicio++) {
                if (!visitado[inicio]) {
                    ArrayList<Integer> pila = new ArrayList<>();
                    pila.add(inicio);
                    visitado[inicio] = true;

                    while (!pila.isEmpty()) {
                        int actual = pila.remove(pila.size() - 1);

                        for (int vecino : g[actual]) {
                            if (!visitado[vecino]) {
                                visitado[vecino] = true;
                                padre[vecino] = actual;
                                pila.add(vecino);
                            } else if (vecino != padre[actual]) {
                                // Encontramos un ciclo
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }


        /*
     * Determinau si els dos grafs són isomorfs. Podeu suposar que cap dels dos té ordre major que
     * 10.
         */
        static boolean exercici2(int[][] g1, int[][] g2) {
            int numeroVerticesG1 = g1.length;
            if (g2.length != numeroVerticesG1) {
                return false;
            }

            // Comparamos los grados
            int[] grados1 = new int[numeroVerticesG1];
            int[] grados2 = new int[numeroVerticesG1];
            for (int i = 0; i < numeroVerticesG1; i++) {
                grados1[i] = g1[i].length;
                grados2[i] = g2[i].length;
            }

            Arrays.sort(grados1);
            Arrays.sort(grados2);

            for (int indice = 0; indice < grados1.length; indice++) {
                if (grados1[indice] != grados2[indice]) {
                    return false;
                }
            }

            // Probar todas las permutaciones
            int[] permutaciones = new int[numeroVerticesG1];
            for (int i = 0; i < numeroVerticesG1; i++) {
                permutaciones[i] = i;
            }

            do {
                if (sonIsomorfos(g1, g2, permutaciones)) {
                    return true;
                }
            } while (siguientePermutacion(permutaciones));

            return false;
        }

// Compara g1 con g2 permutado por `perm`
        static boolean sonIsomorfos(int[][] g1, int[][] g2, int[] permutaciones) {
            int numeroPermutaciones = permutaciones.length;

            for (int permutacion = 0; permutacion < numeroPermutaciones; permutacion++) {
                boolean[] relaciones1 = new boolean[numeroPermutaciones];
                for (int vecino : g1[permutacion]) {
                    relaciones1[permutaciones[vecino]] = true;
                }

                boolean[] relaciones2 = new boolean[numeroPermutaciones];
                for (int vecino : g2[permutaciones[permutacion]]) {
                    relaciones2[vecino] = true;
                }

                if (!Arrays.equals(relaciones1, relaciones2)) {
                    return false;
                }
            }

            return true;
        }

// Genera la siguiente permutación lexicográfica
        static boolean siguientePermutacion(int[] permutaciones) {
            // Paso 1: Buscar el primer índice 'i' desde la derecha tal que permutaciones[i] < permutaciones[i + 1]
            int i = permutaciones.length - 2;
            while (i >= 0 && permutaciones[i] >= permutaciones[i + 1]) {
                i--; // retrocede hasta encontrar un par ascendente
            }

            // Si no se encontró tal 'i', la permutación es la última en orden lexicográfico
            if (i < 0) {
                return false; // no hay siguiente permutación
            }

            // Paso 2: Buscar el primer índice 'j' desde la derecha tal que permutaciones[j] > permutaciones[i]
            int j = permutaciones.length - 1;
            while (permutaciones[j] <= permutaciones[i]) {
                j--;
            }

            // Paso 3: Intercambiar los valores en las posiciones 'i' y 'j'
            int temporal = permutaciones[i];
            permutaciones[i] = permutaciones[j];
            permutaciones[j] = temporal;

            // Paso 4: Invertir el sufijo a partir de la posición 'i + 1' hasta el final del arreglo
            for (int k = i + 1, l = permutaciones.length - 1; k < l; k++, l--) {
                temporal = permutaciones[k];
                permutaciones[k] = permutaciones[l];
                permutaciones[l] = temporal;
            }

            // Se ha generado correctamente la siguiente permutación lexicográfica
            return true;
        }


        /*
     * Determinau si el graf `g` (no dirigit) és un arbre. Si ho és, retornau el seu recorregut en
     * postordre desde el vèrtex `r`. Sinó, retornau null;
     *
     * En cas de ser un arbre, assumiu que l'ordre dels fills vé donat per l'array de veïns de cada
     * vèrtex.
         */
        static int[] exercici3(int[][] g, int r) {
            boolean[] visitado = new boolean[g.length];
            List<Integer> postorden = new ArrayList<>();

            // Usamos DFS y detectamos ciclos
            if (tieneCiclo(g, r, -1, visitado)) {
                return null;
            }

            // Verificamos si es conexo
            for (boolean v : visitado) {
                if (!v) {
                    return null;
                }
            }

            // Resetear para realizar el recorrido postorden real
            Arrays.fill(visitado, false);
            recorridoPostorden(g, r, visitado, postorden);
            int[] resultado = new int[postorden.size()];
            for (int i = 0; i < postorden.size(); i++) {
                resultado[i] = postorden.get(i);
            }
            return resultado;
        }

        // DFS para detectar ciclos (usando `padre` para ignorar la arista de donde vinimos)
        static boolean tieneCiclo(int[][] g, int actual, int padre, boolean[] visitado) {
            visitado[actual] = true;
            for (int vecino : g[actual]) {
                if (!visitado[vecino]) {
                    if (tieneCiclo(g, vecino, actual, visitado)) {
                        return true;
                    }
                } else if (vecino != padre) {
                    return true;
                }
            }
            return false;
        }

        // Recorrido postorden clásico (DFS)
        static void recorridoPostorden(int[][] g, int actual, boolean[] visitado, List<Integer> resultado) {
            visitado[actual] = true;
            for (int vecino : g[actual]) {
                if (!visitado[vecino]) {
                    recorridoPostorden(g, vecino, visitado, resultado);
                }
            }
            resultado.add(actual);
        }


        /*
     * Suposau que l'entrada és un mapa com el següent, donat com String per files (vegeu els tests)
     *
     *   _____________________________________
     *  |          #       #########      ####|
     *  |       O  # ###   #########  ##  ####|
     *  |    ####### ###   #########  ##      |
     *  |    ####  # ###   #########  ######  |
     *  |    ####    ###              ######  |
     *  |    ######################## ##      |
     *  |    ####                     ## D    |
     *  |_____________________________##______|
     *
     * Els límits del mapa els podeu considerar com els límits de l'array/String, no fa falta que
     * cerqueu els caràcters "_" i "|", i a més podeu suposar que el mapa és rectangular.
     *
     * Donau el nombre mínim de caselles que s'han de recorrer per anar de l'origen "O" fins al
     * destí "D" amb les següents regles:
     *  - No es pot sortir dels límits del mapa
     *  - No es pot passar per caselles "#"
     *  - No es pot anar en diagonal
     *
     * Si és impossible, retornau -1.
         */
        static int exercici4(char[][] mapa) {
            int filas = mapa.length;
            int columnas = mapa[0].length;
            boolean[][] visitado = new boolean[filas][columnas];

            // Movimientos posibles: arriba, abajo, izquierda, derecha
            int[] movFila = {-1, 1, 0, 0};
            int[] movColumna = {0, 0, -1, 1};

            // Lista para simular la cola del BFS
            ArrayList<int[]> lista = new ArrayList<>();

            // Encontrar la posición inicial 'O'
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (mapa[i][j] == 'O') {
                        lista.add(new int[]{i, j, 0});
                        visitado[i][j] = true;
                        break;
                    }
                }
            }

            // Índice para simular el comportamiento de una cola
            int indice = 0;

            while (indice < lista.size()) {
                int[] actual = lista.get(indice++);
                int filaActual = actual[0];
                int columnaActual = actual[1];
                int pasos = actual[2];

                // Si encontramos el destino
                if (mapa[filaActual][columnaActual] == 'D') {
                    return pasos;
                }

                // Explorar los 4 movimientos posibles
                for (int k = 0; k < 4; k++) {
                    int nuevaFila = filaActual + movFila[k];
                    int nuevaColumna = columnaActual + movColumna[k];

                    // Verificar que el movimiento es válido
                    if (nuevaFila >= 0 && nuevaFila < filas
                            && nuevaColumna >= 0 && nuevaColumna < columnas
                            && !visitado[nuevaFila][nuevaColumna]
                            && mapa[nuevaFila][nuevaColumna] != '#') {

                        visitado[nuevaFila][nuevaColumna] = true;
                        lista.add(new int[]{nuevaFila, nuevaColumna, pasos + 1});
                    }
                }
            }

            // No se encontró camino
            return -1;
        }


        /*
     * Aquí teniu alguns exemples i proves relacionades amb aquests exercicis (vegeu `main`)
         */
        static void tests() {

            final int[][] D2 = {{}, {}};
            final int[][] C3 = {{1, 2}, {0, 2}, {0, 1}};

            final int[][] T1 = {{1, 2}, {0}, {0}};
            final int[][] T2 = {{1}, {0, 2}, {1}};

            // Exercici 1
            // G té cicles?
            test(3, 1, 1, () -> !exercici1(D2));
            test(3, 1, 2, () -> exercici1(C3));
            final int[][] G1 = {{1}, {0}}; // 2 nodos, 1 arista — sin ciclo
            final int[][] G2 = {{1, 2}, {0}, {0, 3}, {2}}; // árbol — sin ciclo
            final int[][] G3 = {{1}, {0, 2}, {1, 3, 4}, {2}, {2}}; // árbol — sin ciclo
            final int[][] G4 = {{1, 2}, {0, 2}, {0, 1}}; // triángulo — con ciclo
            final int[][] G5 = {{1, 3}, {0, 2}, {1, 3}, {0, 2}}; // ciclo de 4 nodos — con ciclo

            test(3, 1, 3, () -> !exercici1(G1));
            test(3, 1, 4, () -> !exercici1(G2));
            test(3, 1, 5, () -> !exercici1(G3));
            test(3, 1, 6, () -> exercici1(G4));
            test(3, 1, 7, () -> exercici1(G5));

            // Exercici 2
            // Isomorfisme de grafs
            test(3, 2, 1, () -> exercici2(T1, T2));
            test(3, 2, 2, () -> !exercici2(T1, C3));

            // Exercici 3
            // Postordre
            test(3, 3, 1, () -> exercici3(C3, 1) == null);
            test(3, 3, 2, () -> Arrays.equals(exercici3(T1, 0), new int[]{1, 2, 0}));

            // Exercici 4
            // Laberint
            test(3, 4, 1, () -> {
                return -1 == exercici4(new char[][]{
                    " #O".toCharArray(),
                    "D# ".toCharArray(),
                    " # ".toCharArray(),});
            });

            test(3, 4, 2, () -> {
                return 8 == exercici4(new char[][]{
                    "###D".toCharArray(),
                    "O # ".toCharArray(),
                    " ## ".toCharArray(),
                    "    ".toCharArray(),});
            });
        }
    }

    /*
   * Aquí teniu els exercicis del Tema 4 (Aritmètica).
   *
   * En aquest tema no podeu:
   *  - Utilitzar la força bruta per resoldre equacions: és a dir, provar tots els nombres de 0 a n
   *    fins trobar el que funcioni.
   *  - Utilitzar long, float ni double.
   *
   * Si implementau algun dels exercicis així, tendreu un 0 d'aquell exercici.
     */
    static class Tema4 {

        /*
     * Primer, codificau el missatge en blocs de longitud 2 amb codificació ASCII. Després encriptau
     * el missatge utilitzant xifrat RSA amb la clau pública donada.
     *
     * Per obtenir els codis ASCII del String podeu utilitzar `msg.getBytes()`.
     *
     * Podeu suposar que:
     * - La longitud de `msg` és múltiple de 2
     * - El valor de tots els caràcters de `msg` està entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
     *
     * Pista: https://en.wikipedia.org/wiki/Exponentiation_by_squaring
         */
        static int[] exercici1(String msg, int n, int e) {
            int[] encriptado = new int[msg.length() / 2];

            codificarAscii(msg, encriptado);

            encriptar(encriptado, e, n);

            return encriptado;
        }

        public static void codificarAscii(String msg, int[] encriptado) {
            int indice = 0;
            for (int i = 0; i < encriptado.length; i++) {
                encriptado[i] += ((int) msg.charAt(indice) * 128);
                indice++;
                encriptado[i] += (int) msg.charAt(indice);
                indice++;
            }
        }

        public static void encriptar(int[] encriptado, int e, int n) {

            for (int i = 0; i < encriptado.length; i++) {
                encriptado[i] = modPow(encriptado[i], e, n);
            }

        }

        public static int modPow(int base, int exp, int mod) {
            int result = 1;
            int b = base % mod;
            while (exp > 0) {
                if ((exp & 1) == 1) {
                    result = (result * b) % mod;
                }
                b = (b * b) % mod;
                exp >>>= 1;
            }
            return result;
        }


        /*
     * Primer, desencriptau el missatge utilitzant xifrat RSA amb la clau pública donada. Després
     * descodificau el missatge en blocs de longitud 2 amb codificació ASCII (igual que l'exercici
     * anterior, però al revés).
     *
     * Per construir un String a partir d'un array de bytes podeu fer servir el constructor
     * `new String(byte[])`. Si heu de factoritzar algun nombre, ho podeu fer per força bruta.
     *
     * També podeu suposar que:
     * - La longitud del missatge original és múltiple de 2
     * - El valor de tots els caràcters originals estava entre 32 i 127.
     * - La clau pública (n, e) és de la forma vista a les transparències.
     * - n és major que 2¹⁴, i n² és menor que Integer.MAX_VALUE
         */
        static String exercici2(int[] m, int n, int e) {
            int[] primos= factorizar(n);
            
            int phi = calcularPhi(primos);

            int clavePrivada = descifrarClavePrivada(phi,e);

            int desencriptado[] = descifrarMensaje(m,clavePrivada,n);

            char [] mensaje = reconstruccion(desencriptado);

            return new String(mensaje);
        }

        static int[] factorizar(int n) {
            int primos[] = new int [2];

            for(int i = 2; i*i < n; i++){
                if(n%i == 0){
                    primos[0] = i;
                    primos[1] = n/i;
                }
            }
            return primos;
        }

        static int calcularPhi(int [] primos){

            return (primos[0]-1)*(primos[1]-1);
        }

        static int descifrarClavePrivada(int phi, int e){
            
            for (int clavePrivada = 1; clavePrivada < phi; clavePrivada++) {
                if ((long) clavePrivada * e % phi == 1) {
                    return clavePrivada;
                }
            }
            return 0;
        }

        static int[] descifrarMensaje(int[] m, int clavePrivada, int n){

            int [] resultado = new int[m.length];

            int indice = 0;
            for(int i : m){

                resultado [indice] = modPow(i, clavePrivada, n);
                indice++;
            }

            return resultado;
        }

        static char[] reconstruccion(int[] mensaje){
            char [] resultado = new char[mensaje.length*2];

            for(int indice = 0; indice < mensaje.length; indice++){
                resultado[2*indice] = (char) (mensaje[indice]/128);
                resultado[2*indice + 1] = (char) (mensaje[indice]%128);
            }

            return resultado;
        }
        static void tests() {
            // Exercici 1
            // Codificar i encriptar
            test(4, 1, 1, () -> {
                var n = 2 * 8209;
                var e = 5;

                var encr = exercici1("Patata", n, e);
                return Arrays.equals(encr, new int[]{4907, 4785, 4785});
            });
            // Test 5: mensaje mínimo (2 caracteres)
            test(4, 1, 2, () -> {
                int n = 20011;       //  > 2^14, y n^2 < Integer.MAX_VALUE
                int e = 3;
                int[] encr = exercici1("AB", n, e);
                // bloque = 65*128 + 66 = 8386, luego 8386^3 mod 20011 = 14785
                return Arrays.equals(encr, new int[]{14785});
            });

            // Test 6: mensaje de 6 caracteres
            test(4, 1, 3, () -> {
                int n = 25013;
                int e = 7;
                int[] encr = exercici1("Test12", n, e);
                // bloques = [ 'T','e' ; 's','t' ; '1','2' ] 
                //         = [ 84*128+101=10933, 115*128+116=14756, 49*128+50=6292 ]
                // encriptados mod 25013 con exp=7 = [1298, 9290, 12806]
                return Arrays.equals(encr, new int[]{1298, 9290, 12806});
            });

            // Test 7: mensaje de 8 caracteres con símbolo
            test(4, 1, 4, () -> {
                int n = 30011;
                int e = 17;
                int[] encr = exercici1("ChatGPT!", n, e);
                // bloques: "Ch","at","GP","T!" → [20059, 5654, 10584, 10947]
                return Arrays.equals(encr, new int[]{20059, 5654, 10584, 10947});
            });

            // Test 8: mensaje de 10 caracteres
            test(4, 1, 5, () -> {
                int n = 32771;
                int e = 11;
                int[] encr = exercici1("RSAEncrypt", n, e);
                // bloques: "RS","AE","nc","ry","pt" → [17881, 18530, 16021, 1463, 31450]
                return Arrays.equals(encr, new int[]{17881, 18530, 16021, 1463, 31450});
            });

            // Exercici 2
            // Desencriptar i decodificar
            test(4, 2, 1, () -> {
                var n = 2 * 8209;
                var e = 5;

                var encr = new int[]{4907, 4785, 4785};
                var decr = exercici2(encr, n, e);
                return "Patata".equals(decr);
            });
        }
    }

    /*
   * Aquest mètode `main` conté alguns exemples de paràmetres i dels resultats que haurien de donar
   * els exercicis. Podeu utilitzar-los de guia i també en podeu afegir d'altres (no els tendrem en
   * compte, però és molt recomanable).
   *
   * Podeu aprofitar el mètode `test` per comprovar fàcilment que un valor sigui `true`.
     */
    public static void main(String[] args) {
        System.out.println("---- Tema 1 ----");
        Tema1.tests();
        System.out.println("---- Tema 2 ----");
        Tema2.tests();
        System.out.println("---- Tema 3 ----");
        Tema3.tests();
        System.out.println("---- Tema 4 ----");
        Tema4.tests();
    }

    // Informa sobre el resultat de p, juntament amb quin tema, exercici i test es correspon.
    static void test(int tema, int exercici, int test, BooleanSupplier p) {
        try {
            if (p.getAsBoolean()) {
                System.out.printf("Tema %d, exercici %d, test %d: OK\n", tema, exercici, test);
            } else {
                System.out.printf("Tema %d, exercici %d, test %d: Error\n", tema, exercici, test);
            }
        } catch (Exception e) {
            if (e instanceof UnsupportedOperationException && "pendent".equals(e.getMessage())) {
                System.out.printf("Tema %d, exercici %d, test %d: Pendent\n", tema, exercici, test);
            } else {
                System.out.printf("Tema %d, exercici %d, test %d: Excepció\n", tema, exercici, test);
                e.printStackTrace();
            }
        }
    }
}

// vim: set textwidth=100 shiftwidth=2 expandtab :
