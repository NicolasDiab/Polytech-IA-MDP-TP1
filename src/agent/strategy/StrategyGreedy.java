package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d =rand.nextDouble();
		List<Action> actions;
		if (this.agent.getActionsLegales(_e).isEmpty()){
			return null;
		}

		boolean probaEpsilon = rand.nextInt((int)Math.floor(this.getEpsilon() * 100)) == 0;

		if (probaEpsilon) {
			actions = this.agent.getActionsLegales(_e);
			if (actions.size() != 0)
				return actions.get((int)Math.floor(d * actions.size()));
			else
				return null; // normalement, on se doit jamais passer là, on le met pour afficher l'erreur en console
		} else {
			actions = this.agent.getPolitique(_e);
			if (actions.size() != 0)
				return actions.get((int)Math.floor(d * actions.size()));
			else
				return null; // normalement, on se doit jamais passer là, on le met pour afficher l'erreur en console
		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}
