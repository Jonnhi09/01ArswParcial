package edu.eci.arsw.api.primesrepo.service;

import edu.eci.arsw.api.primesrepo.model.FoundPrime;
import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * @author Santiago Carrillo 2/22/18.
 */
@Service
public class PrimeServiceStub implements PrimeService {

    @Override
    public void addFoundPrime(FoundPrime foundPrime) {
        boolean found = false;
        for (FoundPrime f : users) {
            if (f.getPrime().equals(foundPrime.getPrime())) {
                found = true;
            }
        }
        if (!found) {
            synchronized (users) {
                users.add(foundPrime);
            }
        }
    }

    @Override
    public List<FoundPrime> getFoundPrimes() {
        return users;
    }

    @Override
    public FoundPrime getPrime(String prime) {
        for (FoundPrime f : users) {
            String primeFound = f.getPrime();
            if (primeFound.equals(prime)) {
                return f;
            }
        }
        return null;
    }

    private static final List<FoundPrime> users;

    static {
        users = new ArrayList<FoundPrime>();

        FoundPrime fp = new FoundPrime();
        fp.setUser("Jonh");
        fp.setPrime("1");

        users.add(fp);

        FoundPrime fp2 = new FoundPrime();
        fp2.setUser("Jane");
        fp2.setPrime("3");

        users.add(fp2);
    }
}
