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

package es.ants.felixgm.trmsim_wsn.network;

import es.ants.felixgm.trmsim_wsn.search.ISearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.search.IsSensorSearchCondition;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * <p>This class models a sensor in a Wireless Sensor Network</p>
 <font color="#FF0000">
 <p><strong>A subclass of this class, modeling the specific sensors, has to be
 implemented in order to add a new Trust and Reputation Model</strong></p>
 </font>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.1
 */
public abstract class Sensor implements Runnable {
    /** Indicates whether a collusion among malicious sensors is built */
    protected static boolean collusion = false;
    /** Indicates whether some sensors will switch off sometimes in order to save energy */
    protected static boolean dynamic = false;
    /** Indicates whether a simulation is currently running or not */
    protected static boolean runningSimulation = false;
    /** Sensors' identifier counter  */
    protected static int idCount = 1;
    /** Sensor's identifier */
    protected int id;
    /** Maximum distance between two nodes in the network */
    protected static double _maxDistance = 0;
    /** X coordinate of this sensor */
    protected double xPosition;
    /** Y coordinate of this sensor */
    protected double yPosition;
    /** Outcoming links of this sensor */
    protected Collection<Link> links;
    /** Goodness of this sensor related to each provided service */
    protected HashMap<Service,Double> servicesGoodness;
    /** Service requested by the clients */
    protected Service requiredService;
    /** Indicates whether this sensor is currently active or in an idle state */
    protected boolean activeState;
    /** Number of service requests provided by this sensor */
    protected int numRequests;
    /** Number of service requests provided by this sensor after which it goes to sleep */
    protected static final int numRequestsThreshold = 20;
    /** Used to determine the amount of time (in ms) a sensor stays asleep */
    private static final long sleepingTimeoutMilis = 1000;
    /** Current Trust and Reputation model used by every Sensor */
    protected static TRModel_WSN trmmodelWSN;
    /** Last outcome of a performed transaction */
    protected Outcome outcome;
    /** Total distance traveled by the messages sent from this sensor */
    protected long transmittedDistance;
    /** Three timers that are used to set the timing for sleep/active state */
    protected Timer numRequestsTimer;
    protected Timer sleepTimer;
    protected Timer sleepTimerAux;    
    
    /**
     * Class Sensor constructor.
     * Creates a new Sensor and locates it randomly
     */
    public Sensor() {
        this(idCount++,Math.random()*_maxDistance,Math.random()*_maxDistance);
    }

    /**
     * Class Sensor constructor
     * @param id Sensor's identifier
     * @param x X coordinate of this sensor
     * @param y Y coordinate of this sensor
     */
    public Sensor(int id, double x, double y) {
        this.id = id;
        links = new ArrayList<Link>();
        xPosition = x;
        yPosition = y;
        activeState = true;
        transmittedDistance = 0;

        servicesGoodness = new HashMap<Service,Double>();
        numRequests = 0;
        // Added by Hamed Khiabani
        numRequestsTimer = null;
        sleepTimer = null;
        sleepTimerAux = null;
        sleepIfInactive(sleepingTimeoutMilis/2+((int)(Math.random()*(sleepingTimeoutMilis/2))));
    }

    /**
       <p>This method uses the current trust and reputation model in order to find
       the most trustworthy server offering the required service. It requests that
       service to the server found and punishes or rewards it according to its
       satisfaction with the received service</p>
     */
    public void run() {
        if (reachesQualifiedService(requiredService)) {
            GatheredInformation gi = trmmodelWSN.gatherInformation(this, requiredService);
            Vector<Sensor> path = trmmodelWSN.scoreAndRanking(this,gi);
            outcome = trmmodelWSN.performTransaction(path,requiredService);
            if (outcome != null) {
                if (outcome.get_satisfaction().isSatisfied())
                    outcome = trmmodelWSN.reward(path,outcome);
                else
                    outcome = trmmodelWSN.punish(path,outcome);
            }
        } else
            outcome = null;
    }

    /**
     * This method returns a requested service. The similarity between the actually
     * offered service and the actually delivered service depends on the goodness of
     * this sensor about this service. If it has a high goodness both services will be
     * very similar (or equal), otherwise it will deliver a very different service than the
     * one requested
     * @param service Requested service
     * @param path Path leading from the client requesting the service to the server providing it
     * @return A service more or less similar to the requested one, depending on the goodness
     * of this sensor delivering that certain service
     */
    public Service serve(Service service, Vector<Sensor> path) {
        Service givenService = service.clone();
        try {
            if (get_goodness(service) < 0.5)
                givenService = null;
        } catch (Exception ex) {
            givenService = null;
        }

        for (int i = 0; i < path.size()-1; i++)
            transmittedDistance += path.get(i).distance(path.get(i+1));

        numRequests++;
        if (numRequests == numRequestsThreshold) { // Edited by Hamed Khiabani
            numRequests = 0;
            if (dynamic && runningSimulation) {
                activeState = false;
                numRequestsTimer = new Timer();
                numRequestsTimer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        activeState = true;
                        numRequestsTimer.cancel();
                    }
                },sleepingTimeoutMilis);
            }
        }
        
        return givenService;
    }

    /**
     * Indicates if this sensor can reach another sensor offering a given service with posiitve goodness
     * @param service The requested service
     * @return true if this sensor can reach another sensor offering a given service with posiitve goodness; false otherwise
     */
    private boolean reachesQualifiedService(Service service) {
        Collection<Vector<Sensor>> pathsToServers = findSensors(new IsServerSearchCondition(service,IsServerSearchCondition.BENEVOLENT_SERVER));
        return ((pathsToServers != null) && (pathsToServers.size() > 0));
    }

    /**
     * Adds a new service to the set of offered services of this sensor
     * @param service The new service to be added
     * @param goodness The goodness when offering that new service
     */
    public void addService(Service service, double goodness) {
        servicesGoodness.put(service,new Double(goodness));
    }

    /**
     * Removes a service from the set of offered services by this sensor
     * @param service Service to be removed from the set of offered services by this sensor
     */
    public void removeService(Service service) {
        servicesGoodness.remove(service);
        if (requiredService.equals(service))
            requiredService = null;
    }

    /**
     * Gets the goodness of a given service
     * @param service The service to get its goodness
     * @return The goodness of the given service
     * @throws Exception If this sensor does not offer the given service
     */
    public double get_goodness(Service service) throws Exception {
        if (!offersService(service.id()))
            throw new Exception("Server "+id+" doesn't offer service "+service.id());
        return servicesGoodness.get(getService(service.id())).doubleValue();
    }

    /**
     * Sets the goodness of a given service
     * @param service The service to set its goodness
     * @param goodness The goodness to be set
     * @throws Exception If this sensor does not offer the given service
     */
    public void set_goodness(Service service, double goodness) throws Exception {
        if (!offersService(service.id()))
            throw new Exception("Server "+id+" doesn't offer service "+service.id());

        servicesGoodness.put(service, new Double(goodness));
    }

    /**
     * Returns the number of services this sensor provides
     * @return Number of services
     */
    public int get_numServices() {
        if (servicesGoodness == null)
            return 0;
        return servicesGoodness.keySet().size();
    }

    /**
     * Returns a collection with the services this sensor provides
     * @return Collection of services.
     */
    public Collection<Service> get_services() { 
        if (servicesGoodness == null)
            return null;
        return servicesGoodness.keySet();
    }

    /**
     * Returns the requested service or null if this sensor does not offer such service
     * @param service The id of the service to be retrieved
     * @return The requested service or null if this sensor does not offer such service
     */
    public Service getService(String service) {
        if (servicesGoodness != null) {
            Iterator<Service> serviceIt = servicesGoodness.keySet().iterator();
            while (serviceIt.hasNext()) {
                Service service1 = serviceIt.next();
                if (service1.id().equalsIgnoreCase(service))
                    return service1;
            }
        }
        return null;
    }

    /**
     * This method finds the shortest paths from this Sensor to any reachable
     * Sensor satisfying a given condition (inactive or idle sensors are excluded from any path)
     * @param searchCondition Condition to be accomplished by a sensor in order to consider it "reachable"
     * @return Paths from this Sensor to any reachable sensor satisfying a given condition
     */
    public Collection<Vector<Sensor>> findSensors(ISearchCondition searchCondition) {
        Collection<Vector<Sensor>> out = new LinkedList<Vector<Sensor>>();
        Collection<Sensor> Q = new ArrayList<Sensor>(); // All nodes in the graph are unoptimized - thus are in Q
        Collection<Sensor> visitedNodes = new ArrayList<Sensor>(); // Visited nodes
        Hashtable<Sensor,Double> distanceFromSource = new Hashtable<Sensor,Double>(); // Distance from source
        Hashtable<Sensor,Sensor> previousNode = new Hashtable<Sensor,Sensor>(); // Previous node in optimal path from source

        distanceFromSource.put(this, 0.0); // Distance from source to source
        previousNode.put(this, this);
        Q.add(this);

        while (!Q.isEmpty()) { // The main loop
            double minD = Double.POSITIVE_INFINITY;
            Sensor closestNode = null;
            for (Sensor sensor : Q) //closestNode := vertex in Q with smallest dist[]
                if (distanceFromSource.get(sensor) < minD) {
                    minD = distanceFromSource.get(sensor);
                    closestNode = sensor;
                }

            Q.remove(closestNode);
            visitedNodes.add(closestNode);
            for (Sensor sensor : closestNode.getNeighbors()) { // where sensor has not yet been removed from Q.
                if ((!Q.contains(sensor)) && (!visitedNodes.contains(sensor))){
                    distanceFromSource.put(sensor, Double.POSITIVE_INFINITY);
                    Q.add(sensor);
                }

                double alternative = distanceFromSource.get(closestNode) + closestNode.distance(sensor);
                if (alternative < distanceFromSource.get(sensor)) {
                    distanceFromSource.put(sensor, alternative);
                    previousNode.put(sensor, closestNode);
                }
            }
        }

        for (Sensor sensor : previousNode.keySet()) {
            if ((sensor.id() != id) && sensor.isActive() && (searchCondition.sensorAcomplishesCondition(sensor))) {
                boolean intermediateInactiveSensor = false;
                LinkedList<Sensor> path1 = new LinkedList<Sensor>();
                path1.addFirst(sensor);
                Sensor prev = sensor;
                while (prev != this){
                    prev = previousNode.get(prev);
                    if (!prev.isActive())
                        intermediateInactiveSensor = true;
                    path1.addFirst(prev);
                }
                Vector<Sensor> path2 = new Vector<Sensor>();
                for (Sensor s1 : path1) 
                    path2.add(s1);

                if (!intermediateInactiveSensor) 
                    out.add(path2);
            }
        }

        return out;
    }

    /**
     * Indicates this sensor offers a certain service or not
     * @param service The asking service
     * @return true if this sensor indeed offers the asking service, false otherwise
     */
    public boolean offersService(Service service) {
        return offersService(service.id());
    }

    /**
     * Indicates if a sensor offers a certain service or not
     * @param service The asking service's name
     * @return true if this sensor indeed offers the asking service, false otherwise
     */
    protected boolean offersService(String service) {
        return (getService(service) != null);
    }


    /**
     * Returns all the neighbors of this sensor, i.e., all the sensors reachable from
     * this one, one step forward
     * @return Neighbor sensors of this sensor
     */
    public Collection<Sensor> getNeighbors() {
        Collection<Sensor> neighbors = new ArrayList<Sensor>();

        for (Link link : links) { // We add the destination of all the links
            neighbors.add(link.get_destination());
            transmittedDistance += distance(link.get_destination());
        }
        return neighbors;
    }
    
    /**
     * Deletes all the neighbors of this sensor
     */
    public void removeAllNeighbors() {
        links = new ArrayList<Link>();
    }

    /**
     * Indicates if a given sensor is neighbor of this one
     * @param sensor The asking sensor
     * @return true if the specified sensor is neighbor of this sensor, false otherwise
     */
    public boolean isNeighbor(Sensor sensor) {
        if (links != null)
            for (Link link : links) {
                if (link.get_destination().equals(sensor))
                    return true;
            }
        return false;
    }

    /**
     * Adds a link to a given sensor
     * @param sensor Sensor to link to
     */
    public void addLink(Sensor sensor) {
        if (!isNeighbor(sensor)) {
            if (links == null)
                links = new ArrayList<Link>();

            Link link = new Link(this,sensor);
            links.add(link);
        }
    }

    /**
     * Removes the link with a given sensor
     * @param sensor Sensor to remove link with
     */
    public void removeLink(Sensor sensor) {
        Iterator<Link> linkIt = links.iterator();
        while (linkIt.hasNext())
        {
            Link link = linkIt.next();
            if (link.get_destination().equals(sensor))
                linkIt.remove();
        }
    }
    
    /**
     * Calculates the distance between this sensor and a given one
     * @param sensor Sensor to calculate the distance from
     * @return The distance between this sensor and the given one
     */
    public double distance(Sensor sensor) {
        return Math.sqrt(Math.pow(xPosition-sensor.getX(),2)+Math.pow(yPosition-sensor.getY(),2));
    }

    /**
     * This method returns the minimum number of hops from this sensor to the
     * specified one
     * @param sensor Sensor whose distance from this sensor in number of hops
     * is requested
     * @return The minimum number of hops from this sensor to the specified one
     */
    public int distanceInHops(Sensor sensor) {
        int distanceInHops = Integer.MAX_VALUE;
        Collection<Vector<Sensor>> pathsToSensor = findSensors(new IsSensorSearchCondition(sensor.id()));

        if (pathsToSensor != null)
            for (Vector<Sensor> pathToSensor : pathsToSensor)
                if (pathToSensor.size() < distanceInHops)
                    distanceInHops = pathToSensor.size();

        return distanceInHops;
    }
    
    /**
     * Makes this sensor to go to sleep if it is inactive for a period of time
     * @param time Amount of time this sensor remains asleep
     * Edited by Hamed Khiabani
     */
    private void sleepIfInactive(final long time) {
        if (dynamic && runningSimulation) {
            sleepTimer = new Timer();
            sleepTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (numRequests < numRequestsThreshold / 2) {
                        activeState = false;
                        sleepTimerAux = new Timer();
                        sleepTimerAux.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                activeState = true;
                                sleepTimerAux.cancel();
                            }
                        }, time /2);
                    }
                    sleepTimer.cancel();
                }
            }, time, time);
        }
    }


    /**
     * Returns sensor's id.
     * @return Sensor's id
     */
    public int id() { return id; }
    
    /**
     * Returns the X coordinate
     * @return Coordinate X
     */
    public double getX() { return xPosition; }

    /**
     * Returns the Y coordinate
     * @return Coordinate Y
     */
    public double getY() { return yPosition; }

    /**
     * Returns the service required by this client
     * @return The service required by this client
     */
    public Service get_requiredService() { return requiredService; }
    
    /**
     * Returns the total distance traveled by the messages sent from this sensor
     * @return The total distance traveled by the messages sent from this sensor
     */
    public long get_transmittedDistance() { return transmittedDistance; }

    /**
     * This method returns a boolean indicating whether there is currently a simulation running or not
     * @return Boolean indicating whether there is currently a simulation running or not
     */
    public static boolean isRunningSimulation() { return runningSimulation; }
    
    /**
     * Returns Current Trust and Reputation model used by every Sensor
     * @return Current Trust and Reputation model used by every Sensor
     */
    public static TRModel_WSN get_TRModel_WSN() { return trmmodelWSN; }


    /**
     * Returns the last outcome of a performed transaction
     * @return The last outcome of a performed transaction
     */
    public Outcome get_outcome() { return outcome; }

    /**
     * This method updates the state of this sensor
     * @param active_state New state of this sensor: true if the new state is active, and false otherwise
     */
    public void setActiveState(boolean active_state) { this.activeState = active_state; }


    /**
     * Updates the collusion value.
     * @param coll New collusion value.
     */
    public static void setCollusion(boolean coll) {collusion = coll; }

    /**
     * Updates the dynamic value.
     * @param dyn New dynamic value.
     */
    public static void setDynamic(boolean dyn) { dynamic = dyn; }
    
    /**
     * Updates the runningSimulation attribute.
     * @param _runningSimulation New runningSimulation value.
     */
    public static void setRunningSimulation(boolean _runningSimulation) { runningSimulation = _runningSimulation; }
    
    /**
     * This method sets the maximum distance between two nodes in the network
     * @param maxDistance Maximum distance between two nodes in the network
     */
    public static void setMaxDistance(double maxDistance) { _maxDistance = maxDistance; }

    /**
     * Updates the client required Service.
     * @param requiredService New service.
     */
    public void set_requiredService(Service requiredService) { this.requiredService = requiredService; }

    /**
     * This method sets the current Trust and Reputation model used by every Sensor
     * @param TRModel_WSN New Trust and Reputation model used by every Sensor
     */
    public static void set_TRModel_WSN(TRModel_WSN TRModel_WSN) { trmmodelWSN = TRModel_WSN; }

    /**
     * Indicates if this sensor is active or not
     * @return true if this sensor is active, false otherwise
     */
    public boolean isActive() { return activeState;}

    /**
     * This method increases the total distance traveled by the messages sent from this sensor
     * @param distance Amount of distance to be added
     */
    public void addTransmittedDistance(long distance) { transmittedDistance += distance; }

    /**
     * Resets the identifier counter to 1
     */
    public static void resetId() { idCount = 1; }

    /**
     * It indicates if this sensor is equal to a certain one, according to their id
     * @param node Sensor to check its equality
     * @return true if this sensor is equal to the specified one, false otherwise
     */
    public boolean equals(Sensor node) {
        return (id == node.id());
    }

    /**
     * This method resets a Sensor to its initial state
     */
    public abstract void reset();

    /**
     * This method returns a String representation of this sensor
     * @return A String representation of this sensor
     */
    @Override
    public String toString()
    {
        String s = id+" ("+((int)(xPosition*100))/100.0+","+((int)(yPosition*100))/100.0+") ->";

        for (Link link : links)
            s += " "+link.get_destination().id();
        return s;
    }

    /**
     * This method cancels and purges the sensor timers
     * Added by Hamed Khiabani
     */
    public void cancelAllTimers() { 
        activeState = true;

        if(numRequestsTimer != null){
            numRequestsTimer.cancel();
            numRequestsTimer.purge();
        } 
        if(sleepTimer != null){
            sleepTimer.cancel();
            sleepTimer.purge();
        } 
        if(sleepTimerAux != null){
            sleepTimerAux.cancel();
            sleepTimerAux.purge();
        }         
    }
}