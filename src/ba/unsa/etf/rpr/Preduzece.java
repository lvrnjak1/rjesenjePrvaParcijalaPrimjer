package ba.unsa.etf.rpr;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Preduzece {
    private int osnovica;
    private List<RadnoMjesto> radnaMjesta = new ArrayList<>();

    public Preduzece(int osnovica) throws NeispravnaOsnovica {
        if(osnovica <= 0){
            throw new NeispravnaOsnovica("Neispravna osnovica " + osnovica);
        }
        this.osnovica = osnovica;
    }

    public void novoRadnoMjesto(RadnoMjesto radnoMjesto) {
        radnaMjesta.add(radnoMjesto);
    }

    public void zaposli(Radnik radnik, String nazivRadnogMjesta) {
        List<RadnoMjesto> filtrirano = radnaMjesta.stream().filter(radnoMjesto ->
                nazivRadnogMjesta.equals(radnoMjesto.getNaziv()) && radnoMjesto.getRadnik() == null)
                .collect(Collectors.toList());

        if(filtrirano.isEmpty()){
            throw new IllegalStateException("Nijedno radno mjesto tog tipa nije slobodno");
        }

        filtrirano.get(0).setRadnik(radnik);
    }

    public void obracunajPlatu() {
        List<RadnoMjesto> filtrirani = radnaMjesta.stream().filter(radnoMjesto -> radnoMjesto.getRadnik() != null).collect(Collectors.toList());
        for(RadnoMjesto r : filtrirani){
            r.getRadnik().dodajPlatu(osnovica * r.getKoeficijent());
        }
        //.forEach(radnoMjesto -> radnoMjesto.getRadnik().dodajPlatu(osnovica * radnoMjesto.getKoeficijent()));
    }

    public void postaviOsnovicu(int osnovica) throws NeispravnaOsnovica {
        if(osnovica <= 0){
            throw new NeispravnaOsnovica("Neispravna osnovica " + osnovica);
        }
        this.osnovica = osnovica;
    }

    public Set<Radnik> radnici() {
        Set<Radnik> radnici = new TreeSet<>();
        for(RadnoMjesto radnoMjesto : radnaMjesta) {
            if(radnoMjesto.getRadnik() != null)
                radnici.add(radnoMjesto.getRadnik());
        }
        return radnici;
    }

    public Map<RadnoMjesto, Integer> sistematizacija() {
        Map<RadnoMjesto, Integer> mapaRadnaMjesta= new HashMap<>();
        for(RadnoMjesto radnoMjesto : radnaMjesta) {
            if(mapaRadnaMjesta.containsKey(radnoMjesto)){
                mapaRadnaMjesta.put(radnoMjesto, mapaRadnaMjesta.get(radnoMjesto) + 1);
            }
            else {
                mapaRadnaMjesta.put(radnoMjesto, 1);
            }
        }

        return mapaRadnaMjesta;
    }

    public double iznosPlate() {
        List<RadnoMjesto> filtrirani = radnaMjesta.stream().filter(radnoMjesto -> radnoMjesto.getRadnik() != null)
                .collect(Collectors.toList());

        double iznos = 0;
        for(RadnoMjesto r : filtrirani){
            iznos +=osnovica * r.getKoeficijent();
        }

        return  iznos;
    }

    public List<Radnik> filterRadnici(Predicate<Radnik> funkcijaZaFiltriranje) {
            return radnaMjesta.stream().filter( radnoMjesto -> radnoMjesto.getRadnik() != null)
                    .map(radnoMjesto -> radnoMjesto.getRadnik()).filter(funkcijaZaFiltriranje).collect(Collectors.toList());

            /*List<Radnik> radnici = new ArrayList<>();
            for(RadnoMjesto radnoMjesto : radnaMjesta) {
                if(radnoMjesto.getRadnik() != null && funkcijaZaFiltriranje.test(radnoMjesto.getRadnik())){
                    radnici.add(radnoMjesto.getRadnik());
                }
            }
            return radnici;*/
    }

    public List<Radnik> vecaProsjecnaPlata(double plata){
        return filterRadnici((Radnik radnik) -> {
            return radnik.prosjecnaPlata() > plata;
        });
    }

    public int dajOsnovicu() {
        return osnovica;
    }
}

