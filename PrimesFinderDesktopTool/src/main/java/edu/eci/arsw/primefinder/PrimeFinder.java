package edu.eci.arsw.primefinder;

import edu.eci.arsw.math.MathUtilities;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeFinder extends Thread {

    public boolean finalizar = false;
    public boolean pausa = false;
    public Object lock = new Object();
    BigInteger a, b;
    PrimesResultSet prs;

    public PrimeFinder(BigInteger a, BigInteger b, PrimesResultSet prs) {
        this.a = a;
        this.b = b;
        this.prs = prs;
    }

    @Override
    public void run() {
        findPrimes(a, b, prs);
    }

    public void findPrimes(BigInteger _a, BigInteger _b, PrimesResultSet prs) {
        BigInteger a = _a;
        BigInteger b = _b;
        MathUtilities mt = new MathUtilities();
        BigInteger i = a;
        int itCount = 0;
        while (i.compareTo(b) <= 0) {
            itCount++;
            if (mt.isPrime(i)) {
                prs.addPrime(i);
            }
            i = i.add(BigInteger.ONE);
            if (pausa) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PrimeFinder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        finalizar = true;
    }

    public void pausar() {
        pausa = true;
        System.out.println("User working again!");
    }

    public void reanudar() {
        pausa = false;
        synchronized (lock) {
            lock.notifyAll();
        }
        System.out.println("Idle CPU ");
    }

    public boolean isFinalizar() {
        return finalizar;
    }

}
