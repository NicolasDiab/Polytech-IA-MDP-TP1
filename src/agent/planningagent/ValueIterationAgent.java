package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;

import static java.lang.Math.abs;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 //* @param nbIterations
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
		
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon 
		this.delta = 0.0;
		this.vmin = 0;
		this.vmax = 1;
		//*** VOTRE CODE
		try {
			for (Etat etat : this.mdp.getEtatsAccessibles()){
				Double vks = 0D;
				for (Action action : mdp.getActionsPossibles(etat)){
					Map<Etat, Double> transitionProba = mdp.getEtatTransitionProba(etat, action);

					Double sum = 0D;
					for (Map.Entry<Etat, Double> transition : transitionProba.entrySet())
					{
						Double recompense = mdp.getRecompense(etat, action, transition.getKey());
						Double proba = transition.getValue();
						Double vk = this.V.get(transition.getKey());
						sum += proba * (recompense + this.gamma * vk);
					}
					if (sum > vks)
						vks = sum;
				}

				this.delta = Math.max(this.delta, Math.abs(vks - this.V.get(etat)));
				V.put(etat, vks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//*******************
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size() == 0)
			return Action2D.NONE;
		else{//choix aleatoire
			return actions.get(rand.nextInt(actions.size()));
		}

		
	}
	@Override
	public double getValeur(Etat _e) {
		return  V.get(_e);
	}

	/**
	 * renvoi la (les) action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		// retourne action de meilleure valeur dans _e selon V,
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();

		try {
			Double vks = 0D;
			for (Action action : mdp.getActionsPossibles(_e)) {
				Map<Etat, Double> transitionProba = mdp.getEtatTransitionProba(_e, action);
				Double sum = 0D;

				for (Map.Entry<Etat, Double> transition : transitionProba.entrySet()) {
					Double recompense = mdp.getRecompense(_e, action, transition.getKey());
					Double proba = transition.getValue();
					Double vk = this.V.get(transition.getKey());
					sum += proba * (recompense + this.gamma * vk);
				}
				if (sum > vks) {
					vks = sum;
					returnactions.clear();
					returnactions.add(action);
				}
				if (sum == vks) {
					returnactions.add(action);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		return returnactions;
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}
