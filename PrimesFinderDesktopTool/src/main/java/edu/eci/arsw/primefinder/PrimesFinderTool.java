package edu.eci.arsw.primefinder;

import edu.eci.arsw.mouseutils.MouseMovementMonitor;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class PrimesFinderTool {

    public static void main(String[] args) {

        LinkedList<PrimeFinder> threadList = new LinkedList<>();
        boolean finalizar = false;
        int maxPrim = 10; //Para prueba lo puse en 10.
        int numThreads = 4;
        PrimesResultSet prs = new PrimesResultSet("john");

        //Variables para asignar los rangos dependiendo del numero de hilos.
        int div = maxPrim / numThreads;
        int mod = maxPrim % numThreads;
        int minRange = 0;
        int maxRange = minRange + div;
        int var = maxRange + mod;
        // Crear un hilo con su respectivo segmento de busqueda.
        while (var <= maxPrim) {
            if (var == maxPrim) {
                threadList.add(new PrimeFinder(new BigInteger(String.valueOf(minRange)), new BigInteger(String.valueOf(var)), prs));
            } else {
                threadList.add(new PrimeFinder(new BigInteger(String.valueOf(minRange)), new BigInteger(String.valueOf(maxRange)), prs));
            }
            minRange = maxRange;
            maxRange = minRange + div;
            var = maxRange + mod;
        }

        for (PrimeFinder i : threadList) {
            i.start();
        }

        //PrimeFinder.findPrimes(new BigInteger("1"), new BigInteger("10000"), prs);
        while (!finalizar) {
            try {
                //check every 10ms if the idle status (10 seconds without mouse
                //activity) was reached. 
                Thread.sleep(10);
                if (MouseMovementMonitor.getInstance().getTimeSinceLastMouseMovement() > 10000) {
                    int contador = 0;
                    for (PrimeFinder k : threadList) {
                        if (k.isFinalizar()) {
                            contador++;
                        }
                        k.reanudar();
                    }
                    if (contador == threadList.size()) {
                        finalizar = true;
                    }
                } else {
                    for (PrimeFinder k : threadList) {
                        k.pausar();
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(PrimesFinderTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Prime numbers found:");

        System.out.println(prs.getPrimes());

    }

}
