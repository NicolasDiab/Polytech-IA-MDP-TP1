package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * Renvoi 0 pour valeurs initiales de Q
 *
 * @author laetitiamatignon
 */
public class QLearningAgent extends RLAgent {
    /**
     * format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
     */
    protected HashMap<Etat, HashMap<Action, Double>> qvaleurs;

    //AU CHOIX: vous pouvez utiliser une Map avec des Pair pour les clés si vous préférez
    //protected HashMap<Pair<Etat,Action>,Double> qvaleurs;


    /*/**
     * @param alpha
     * @param gamma
     * @param _env
     * @param nbS           attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
     * @param nbA
     */
    public QLearningAgent(double alpha, double gamma,
                          Environnement _env) {
        super(alpha, gamma, _env);
        qvaleurs = new HashMap<Etat, HashMap<Action, Double>>();


    }


    /**
     * renvoi la (les) action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques)
     * renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        // retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
        // retourne liste vide si aucune action legale (etat terminal)
        List<Action> actionsPossible = this.getActionsLegales(e);

		//*** VOTRE CODE
        if (actionsPossible.size() == 0) {//etat  absorbant; impossible de le verifier via environnement
            System.out.println("aucune action legale");
            return new ArrayList<Action>();
        } else
        	return actionsPossible.stream().filter(action -> this.getQValeur(e,action) == this.getValeur(e)).collect(Collectors.toList());
    }

    @Override
    public double getValeur(Etat e) {
        //*** VOTRE CODE
        return (this.qvaleurs.containsKey(e))?this.qvaleurs.get(e).entrySet().stream().mapToDouble(entry -> (double)entry.getValue()).reduce(0, Double::max):0.0;
    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE
        // Incertain
        return (this.qvaleurs.containsKey(e) && this.qvaleurs.get(e).containsKey(a)) ?
                this.qvaleurs.get(e).get(a)
                : 0.0;
    }


    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //*** VOTRE CODE
        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur de max pour tout s de V
        //vmin est la valeur de min pour tout s de V
        // ...
        // Incertain calcul du double à faire (peut être)
        if (!qvaleurs.containsKey(e))
            this.qvaleurs.put(e, new HashMap<>());
        this.qvaleurs.get(e).put(a, d);
        this.notifyObs();

    }


    /**
     * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL)
            System.out.println("QL mise a jour etat " + e + " action " + a + " etat' " + esuivant + " r " + reward);

        //*** VOTRE CODE
        double valeur = (1 - this.getAlpha()) * this.getQValeur(e, a) + this.getAlpha() * (reward + this.getGamma() * this.getValeur(esuivant));
        this.setQValeur(e,a,valeur/*Valeur à calculer*/);
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    @Override
    public void reset() {
        super.reset();
        this.qvaleurs.clear();

        this.episodeNb = 0;
        this.notifyObs();
    }


}
