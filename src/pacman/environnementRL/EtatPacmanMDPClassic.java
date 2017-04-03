package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	Position2D pacman;
	List<Position2D> ghost;
	List<Position2D> dots;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		StateAgentPacman s = _stategamepacman.getPacmanState(0);
		this.pacman = new Position2D(s.getX(),s.getY());
		this.ghost = new ArrayList<>();
		for (int i =0; i < _stategamepacman.getNumberOfGhosts();i++) {
			s = _stategamepacman.getGhostState(i);
			this.ghost.add(new Position2D(s.getX(),s.getY()));
		}


		MazePacman maze = _stategamepacman.getMaze();
		int i ,j;
		dots = new ArrayList<>();
		for (i = 0; i< maze.getSizeX(); i++)
			for (j = 0; j < maze.getSizeY(); j++)
				if(maze.isFood(i,j))
					dots.add(new Position2D(i,j));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;
		return Objects.equals(pacman, that.pacman) &&
				Objects.equals(ghost, that.ghost) &&
				Objects.equals(dots, that.dots);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pacman, ghost, dots);
	}

	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return clone;
	}



	

}
