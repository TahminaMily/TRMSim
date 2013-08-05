/**
 *  "TRMSim-WSN, Trust and Reputation Models Simulator for Wireless 
 * Sensor Networks" is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version always keeping 
 * the additional terms specified in this license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * 
 * Additional Terms of this License
 * --------------------------------
 * 
 * 1. It is Required the preservation of specified reasonable legal notices
 *   and author attributions in that material and in the Appropriate Legal
 *   Notices displayed by works containing it.
 * 
 * 2. It is limited the use for publicity purposes of names of licensors or
 *   authors of the material.
 * 
 * 3. It is Required indemnification of licensors and authors of that material
 *   by anyone who conveys the material (or modified versions of it) with
 *   contractual assumptions of liability to the recipient, for any liability
 *   that these contractual assumptions directly impose on those licensors
 *   and authors.
 * 
 * 4. It is Prohibited misrepresentation of the origin of that material, and it is
 *   required that modified versions of such material be marked in reasonable
 *   ways as different from the original version.
 * 
 * 5. It is Declined to grant rights under trademark law for use of some trade
 *   names, trademarks, or service marks.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (lgpl.txt).  If not, see <http://www.gnu.org/licenses/>
*/

package es.ants.felixgm.trmsim_wsn.trm.btrm_wsn;

import java.util.Vector;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;

/**
 * <p>This class models an ant in the ant colony system. It includes the path being built by the ant,
 * the service it is searching and the current average pheromone of the path.</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 */
public class BTRM_Ant {
    /** Service being searched by this ant */
    protected Service searchingService;
    /** Client in the network requesting a certain service */
    protected BTRM_Sensor client;
    /** Current built path of nodes leading to a server offering the desired service */
    protected Vector<BTRM_Sensor> solution;
    /** Collection of visited servers by this ant */
    protected Vector<BTRM_Sensor> visitedSensors;
    /** Average pheromone of the current built path */
    protected double averagePheromone;
    /** Boolean indicating if a solution has been found or not */
    protected boolean solutionFound;
	
    /**
     * Class BTRM_Ant constructor
     * @param searchingService The service desired by the client and searched by the BTRM_Ant
     * @param client The client asking for the service
     */
    public BTRM_Ant(Service searchingService, BTRM_Sensor client) {
        this.searchingService = searchingService;
        this.client = client;
        reset();
    }

    /**
     * Adds a sensor to the solution of this ant and computes the average pheromone,
     * and the average weighted pheromone
     * @param sensor Sensor to add to the current path
     */
    public void addSensor(BTRM_Sensor sensor) {
        BTRM_Sensor currentSensor = getCurrentSensor();
        double pheromone = currentSensor.getPheromone(sensor);

        solution.add(sensor);
        visitedSensors.add(sensor);

        //Dynamic computation of average pheromone
        averagePheromone = averagePheromone*(solution.size()-1);
        averagePheromone += pheromone;
        averagePheromone = averagePheromone/solution.size();

        currentSensor.addTransmittedDistance((long)currentSensor.distance(sensor));
    }

    /**
     * The ant moves back until it finds a server offering the desired service
     * or a server with more paths to explore or until it reaches the client it departed from
     * @return New current server to keep exploring from or null if it reaches
     * a server offering the desired service
     */
    public Sensor moveBack() {
        if (solution.size() > 1)
            if (getCurrentSensor().offersService(searchingService))
                    return null;

        while (solution.size() > 1) {
            Sensor deletedNode = getCurrentSensor();
            solution.removeElement(deletedNode);
            BTRM_Sensor currentNode = getCurrentSensor();
            double pheromone = currentNode.getPheromone(deletedNode);

            currentNode.addTransmittedDistance((long)currentNode.distance(deletedNode));

            //Dynamic computation of average pheromone
            averagePheromone = averagePheromone*(solution.size()+1);
            averagePheromone -= pheromone;
            averagePheromone = averagePheromone/solution.size();

            //We check if currentNode has more not visited neighbors
            for (Sensor neighbour : currentNode.getNeighbors())
                    if (!visitedSensors.contains((BTRM_Sensor)neighbour))
                            return currentNode;
            //Current node has no more non visited neighbors
            //If we've reached a final server, we need to stop
            //and return current solution
            if (currentNode.offersService(searchingService))
                    return null;
            //If it isn't a final server and it hasn't more non visited neighbors
            //then keep on moving back
        }
        //If we reach the client, we need to stop
        return null;
    }

    /**
     * @return The current node being visited by the BTRM_Ant
     */
    private BTRM_Sensor getCurrentSensor() {
       return solution.lastElement();
    }

    /**
     * Resets the ant to its initial state, with no path and no average pheromone
     */
    public void reset() {
        solution = new Vector<BTRM_Sensor>();
        solution.addElement(client);
        visitedSensors = new Vector<BTRM_Sensor>();
        visitedSensors.addElement(client);
        averagePheromone = 0.0;
        solutionFound = false;
    }

    /**
     * Indicates if a solution has been found or not
     * @return true if a final solution has been found by this ant, false otherwise
     */
    public boolean isSolutionFound() {
        return solutionFound;
    }

    /**
     * Sets the current path as a final solution. After a call to this method,
     * {@link #isSolutionFound isSolutionFound} returns true
     */
    public void setSolutionFound() {
        solutionFound = true;
    }

    /**
     * Gets the pheromone of the last edge of the built path
     * @return The pheromone of the last edge of the built path
     */
    public double getLastNodePheromone() {
        if (solution.size() == 2)
            return client.getPheromone(solution.lastElement());

        if (solution.size() > 2) {
            BTRM_Sensor n1 = solution.lastElement();
            BTRM_Sensor n2 = solution.get(solution.lastIndexOf(n1)-1);
            return n2.getPheromone(n1);
        }

        return 0.0;
    }

    /**
     * Returns the next node to be visited by the ant
     * @param alpha Weight of the pheromone in the transition rule of the ant colony system
     * @param beta Weight of the heuristic in the transition rule of the ant colony system
     * @param q0 Probability of using a pseudo random proportional transition rule or a
     * leading exploration transition rule
     * @return The next server to visit according to the transition rule, 
     * or null if the current node has no more neighbors or if a loop has been made
     */
    public BTRM_Sensor getNextSensor(double alpha, double beta, double q0) {
        BTRM_Sensor nextSensor;
        if (Math.random() <= q0)
            nextSensor = pseudoRandomProportionalRule(alpha, beta);
        else 
            nextSensor = leadingExploration(alpha, beta);

        return nextSensor;	  
    }

    /**
     * \frac{\tau_j^{\alpha} * \eta_j^{\beta}}{\sum_i \tau_i^{\alpha} * \eta_i^{\beta}}
     * @param alpha Alpha parameter -> Pheromone weight
     * @param beta Beta parameter -> Heuristic weight
     * @return The selected sensor according to the formula described above
     */
    private BTRM_Sensor leadingExploration(double alpha, double beta) {
        BTRM_Sensor currentNode = getCurrentSensor();

        Vector<Double> transitionProb = new Vector<Double>();
        Vector<BTRM_Sensor> candidates = new Vector<BTRM_Sensor>();
        double addition = 0.0;
        for (Sensor neighbour : currentNode.getNeighbors())
            if (!visitedSensors.contains((BTRM_Sensor)neighbour)) {
                double probability = Math.pow(currentNode.getPheromone(neighbour), alpha)*
                                                        Math.pow(currentNode.getHeuristic(neighbour), beta);
                if (probability > 0) {
                    addition += probability;
                    transitionProb.add(probability);
                    candidates.add((BTRM_Sensor)neighbour);
                }
            }
        double aleat = Math.random();
        double accumulator = 0.0;
        for (int j = 0; j < transitionProb.size(); j++) {
            if ((accumulator <= aleat) && 
                            (aleat <= (accumulator + transitionProb.get(j)/addition)))
                    return candidates.get(j);
            accumulator += transitionProb.get(j)/addition;
        }
        return null;
    }

    /**
     * \max_i (\tau_i^{\alpha} * \eta_i^{\beta})
     * @param alpha Alpha parameter -> Pheromone weight
     * @param beta Beta parameter -> Heuristic weight
     * @return The selected sensor according to the formula described above
     */
    private BTRM_Sensor pseudoRandomProportionalRule(double alpha, double beta) {
        BTRM_Sensor currentNode = getCurrentSensor();
        BTRM_Sensor selectedSensor = null;
        double max = Double.NEGATIVE_INFINITY;

        for (Sensor neighbour : currentNode.getNeighbors())
            if (!visitedSensors.contains((BTRM_Sensor)neighbour) &&
                    (Math.pow(currentNode.getPheromone(neighbour), alpha)*
                     Math.pow(currentNode.getHeuristic(neighbour), beta) > max))
            {
                max = Math.pow(currentNode.getPheromone(neighbour), alpha)*
                Math.pow(currentNode.getHeuristic(neighbour), beta);
                selectedSensor = (BTRM_Sensor)neighbour;
            }
        return selectedSensor;
    }

    /**
     * This method carries out the pheromone local updating of the edge connecting the current
     * last server in the path and the next server passed as an argument
     * @param phi Parameter phi, a constant within the interval [0,1]
     * @param nextSensor Next server to visit by the ant
     */
    public void pheromoneLocalUpdating(double phi, Sensor nextSensor) {
        BTRM_Sensor sensor = getCurrentSensor();
        double pheromone = sensor.getPheromone(nextSensor);
        double heuristic = sensor.getHeuristic(nextSensor);
        double Omega = (1 + (1-phi)*(1-pheromone)*heuristic)*pheromone;

        sensor.setPheromone(nextSensor, (1-phi)*pheromone + phi*Omega);
    }

    /**
     * This method carries out the pheromone global updating of the best current path
     * @param phi Parameter phi, a constant within the interval [0,1]
     * @param rho Parameter rho, a constant within the interval [0,1]
     * @param maxGlobal Quality of the best current path
     */
    public void pheromoneGlobalUpdating(double phi, double rho, double maxGlobal) {
        for (int i = 0; i < solution.size()-1; i++) {
            BTRM_Sensor currentServer = solution.get(i);
            BTRM_Sensor nextServer = solution.get(i+1);
            double pheromone = currentServer.getPheromone(nextServer);
            double heuristic = currentServer.getHeuristic(nextServer);
            double z2 = (1 + pheromone*heuristic*maxGlobal)*pheromone;
            currentServer.setPheromone(nextServer, (1-rho)*pheromone + rho*z2);
        }
    }

    /**
     * Returns the current built path of nodes leading to a server offering the desired service
     * @return The current built path of nodes leading to a server offering the desired service
     */
    public Vector<BTRM_Sensor> get_solution() { return solution; }

    /**
     * Returns the average pheromone of the current built path
     * @return The average pheromone of the current built path
     */
    public double get_averagePheromone() { return averagePheromone; }
}