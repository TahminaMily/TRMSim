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

package es.ants.felixgm.trmsim_wsn;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;

import es.ants.felixgm.trmsim_wsn.network.Sensor;

import java.util.Observable;
import java.util.Observer;
import java.util.Collection;
import java.util.ArrayList;

/**
 * <p>This class is used to run simulations of a trust and reputation model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.1
 */
public class Simulation extends Observable implements Runnable {

    /** Wireless sensor network to test */
    private Network network;
    /** Service requested by every client of each WSN */
    private Service requiredService;

    /** Minimum number of sensors composing every WSN */
    private int minNumSensors;
    /** Maximum number of sensors composing every WSN */
    private int maxNumSensors;

    /** The probability of a node to act as a client */
    private double probClients;
    /** The probability of a server to act just as a relay node (not offering the required service) */
    private double probRelay;
    /** The probability of a server offering the required service to act as a  malicious server (not providing the offered service, or providong a worse or different one) */
    private double probMalicious;
    /** Maximum wireless range of every sensor. It determines the neighborhood of every sensor */
    private double radioRange;

    /** It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links) */
    private boolean dynamic;
    /** It determines if the goodness of the servers belonging to the created WSN will change along the time */
    private boolean oscillating;
    /** It determines if the malicious servers belonging to the created WSN will form a collusion among them */
    private boolean collusion;

    /** Number of service requests of every client composing each WSN */
    private int numExecutions;
    /** Number of wireless sensor networks to test */
    private int numNetworks;
    /** Global outcomes of the performed simulations */
    private Collection<Outcome> globalOutcomes;
    /** It determines whether to stop and interrupt the current simulation */
    private boolean stop;
    
    /**
     * Creates a new Simulation
     * @param observers Used to communicate changes to the GUI
     * @param requiredService Service requested by every client of each WSN
     * @param minNumSensors Minimum number of sensors composing every WSN
     * @param maxNumSensors Maximum number of sensors composing every WSN
     * @param probClients The probability of a node to act as a client
     * @param probRelay The probability of a server to act just as a relay node (not offering the required service)
     * @param probMalicious The probability of a server offering the required service to act as a 
     * malicious server (not providing the offered service, or providing a worse or different one)
     * @param radioRange Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @param numNetworks Number of wireless sensor networks to test
     * @param numExecutions Number of service requests of every client composing each WSN
     */
    public Simulation(Collection<Observer> observers, Service requiredService, 
            int minNumSensors, int maxNumSensors,
            double probClients, double probRelay, double probMalicious,
            double radioRange, boolean dynamic, boolean oscillating, boolean collusion, 
            int numNetworks, int numExecutions) {
        network = null;
        this.requiredService = requiredService;
        
        this.minNumSensors = minNumSensors;
        this.maxNumSensors = maxNumSensors;
        
        this.probClients = probClients;
        this.probRelay = probRelay;
        this.probMalicious = probMalicious;
        this.radioRange = radioRange;
        
        this.dynamic = dynamic;
        this.oscillating = oscillating;
        this.collusion = collusion;
        
        this.numNetworks = numNetworks;
        this.numExecutions = numExecutions;

        globalOutcomes = new ArrayList<Outcome>();
        stop = false;
        for (Observer observer : observers)
            addObserver(observer);
    }
    
    /**
     * Creates a new Simulation
     * @param observers Used to communicate changes to the GUI
     * @param requiredService Service requested by every client of the specified WSN
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @param numExecutions Number of service requests of every client composing the specified WSN
     * @param network Wireless sensor network to test
     */
    public Simulation(Collection<Observer> observers, Service requiredService, 
            boolean dynamic, boolean oscillating, boolean collusion, 
            int numExecutions, Network network) {
        this.network = network;
        this.requiredService = requiredService;

        this.dynamic = dynamic;
        this.oscillating = oscillating;
        this.collusion = collusion;
        
        this.numNetworks = 1;
        this.numExecutions = numExecutions;

        globalOutcomes = new ArrayList<Outcome>();
        stop = false;
        for (Observer observer : observers)
            addObserver(observer);
    }
    
    /**
     * Stops the simulations
     */
    public synchronized void stop() {   stop = true;    }
    
    /**
     * Starts the simulations
     */
    public void run() {
        Sensor.setRunningSimulation(true);
        try {
            for (int net = 0; (net < numNetworks) && !stop; net++) {
                if ((network == null) || (numNetworks != 1)) 
                    network = Controller.C().createNewNetwork(minNumSensors,maxNumSensors,
                            probClients,probRelay,probMalicious,radioRange,
                            dynamic, oscillating, collusion);

                for (Sensor client : network.get_clients())
                    client.set_requiredService(requiredService);

                setChanged();
                notifyObservers(network);

                setChanged();
                notifyObservers("Running selected TRM over WSN "+(net+1)+"...\n");
                Collection<Outcome> outcomes = new ArrayList<Outcome>();
                int Ne = 0;
                for (; (Ne < numExecutions) && !stop; Ne++) {
                    Thread[] clients = new Thread[network.get_numClients()];
                    int j = 0;
                    for (Sensor client : network.get_clients()) 
                        clients[j++] = new Thread(client);

                    for (int i = 0; i < clients.length; i++)
                        clients[i].start();

                    for (int i = 0; i < clients.length; i++)
                        clients[i].join();

                    for (Sensor client : network.get_clients()) 
                        if (client.get_outcome() != null)
                            outcomes.add(client.get_outcome());
                    
                    if ((oscillating) && (Ne % 20 == 0))
                        network.oscillate(requiredService);

                    if ((dynamic) || ((oscillating) && (Ne % 20 == 0))) {
                        setChanged();
                        notifyObservers(network);
                    }

                    if (numNetworks == 1) {
                        Outcome outcome = Outcome.computeOutcomes(outcomes,network,requiredService,Ne);
                        if (outcome == null) {
                            setChanged();
                            notifyObservers("Any of the clients can reach any trustworthy server\n");
                            setChanged();
                            notifyObservers("Finishing simulations at "+(new java.util.Date())+"...\n");
                            // Cancel all timers - Added by Hamed Khiabani
                            for (Sensor sensor : network.get_sensors()) sensor.cancelAllTimers();
                            return;
                        } else {
                            globalOutcomes.add(outcome);
                            setChanged();
                            notifyObservers(globalOutcomes);
                        }
                    }
                }

                if (stop) {
                    for (Sensor sensor : network.get_sensors()) 
                        sensor.cancelAllTimers();
                    setChanged();
                    notifyObservers(network);
                }
                
                Outcome outcome = Outcome.computeOutcomes(outcomes,network,requiredService,Ne);
                if (outcome == null) {
                    setChanged();
                    notifyObservers("Any of the clients can reach any trustworthy server\n");
                    if (net > 0)
                        net--;
                } else if (numNetworks > 1) {
                    globalOutcomes.add(outcome);
                    setChanged();
                    notifyObservers(globalOutcomes);
                }
            }
            Sensor.setRunningSimulation(false);
            setChanged();
            notifyObservers("Finishing simulations at "+(new java.util.Date())+"...\n");
            // Cancel all timers - Added by Hamed Khiabani
            for (Sensor sensor : network.get_sensors()) 
                sensor.cancelAllTimers();
            if (stop) {
                setChanged();
                notifyObservers(network);
            }
            
            if ((globalOutcomes != null) && (globalOutcomes.size() > 0)) {
                setChanged();
                notifyObservers(globalOutcomes);
            }
        } catch (Exception ex) {
            Sensor.setRunningSimulation(false);
            setChanged();
            notifyObservers(ex);
        }
    }

    /**
     * This method returns the global outcomes achieved by this simulations
     * @return The global outcomes achieved by this simulations
     */
    public Collection<Outcome> get_globalOutcomes() { return globalOutcomes; }
}