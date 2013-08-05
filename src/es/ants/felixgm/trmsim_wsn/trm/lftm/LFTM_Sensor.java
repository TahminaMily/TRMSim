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

package es.ants.felixgm.trmsim_wsn.trm.lftm;

import es.ants.felixgm.trmsim_wsn.network.Link;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.DefuzzifierCenterOfGravity;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRuleSet;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * <p>This class models a Sensor implementing {@link LFTM}</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class LFTM_Sensor extends Sensor {
    /** Number of servers composing the network this sensor belongs to */
    protected static int _numServers = 0;
    /** Goodness of this sensor related to each provided service */
    protected HashMap<LFTM_Service,Variable> _servicesGoodness;
    /** Client's conformity, used to assess the client satisfaction with a received service */
    protected Variable clientConformity;
    /** Client's goodness, used to assess the level of punishment or reward to apply to a trustworthy or untrustworthy server */
    protected Variable clientGoodness;
    /** Service's price weight used when comparing two services */
    protected double priceWeight = 1.0;
    /** Service's cost weight used when comparing two services */
    protected double costWeight = 1.0;
    /** Service's delivery time weight used when comparing two services */
    protected double deliveryWeight = 1.0;
    /** Service's quality weight used when comparing two services */
    protected double qualityWeight = 1.0;

    /**
     * This constructor creates a new Sensor implementing LFTM
     */
    public LFTM_Sensor () {
        super();
        _servicesGoodness = new HashMap<LFTM_Service,Variable>();
        clientConformity = new Variable("ClientConformity", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        clientConformity.setValue("Medium");
        clientGoodness = new Variable("ClientGoodness", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        clientGoodness.setValue("Medium");
    }

    /**
     * This constructor creates a new Sensor implementing LFTM
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public LFTM_Sensor(int id, double x, double y) {
        super(id,x,y);
        _servicesGoodness = new HashMap<LFTM_Service,Variable>();
        clientConformity = new Variable("ClientConformity", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        clientConformity.setValue("Medium");
        clientGoodness = new Variable("ClientGoodness", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        clientGoodness.setValue("Medium");
    }

    /**
     * This method evaluates the client satisfaction with the received service
     * using both the comparison between the expected service and the actually
     * received one, and the client's conformity
     * @param servicesComparison Comparison between the expected and the actually received service
     * @return Client's satisfaction with the received service (as a fuzzy set)
     */
    public Variable evaluateSatisfaction(Variable servicesComparison) {
        Variable clientSatisfaction = new Variable("ClientSatisfaction", LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(), LFTM_Parameters.get_U_MAX()));

        FuzzyRuleSet frsClientSatisfaction = LFTM_Parameters.getFRSClientSatisfaction(clientConformity,servicesComparison,clientSatisfaction);
        frsClientSatisfaction.evaluate();

        return clientSatisfaction;
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
    @Override
    public Service serve(Service service, Vector<Sensor> path) {
        LFTM_Service offeredService = null;
        Variable serverGoodness = null;
        for (LFTM_Service lftmService : _servicesGoodness.keySet())
            if (lftmService.equals(service)) {
                offeredService = lftmService;
                serverGoodness = _servicesGoodness.get(lftmService);
            }

        Variable givenServicePrice = new Variable("Price", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        Variable givenServiceCost = new Variable("Cost", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        Variable givenServiceDelivery = new Variable("Delivery", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));
        Variable givenServiceQuality = new Variable("Quality", LFTM_Parameters.get_linguisticTerms(),new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(),LFTM_Parameters.get_U_MAX()));

        FuzzyRuleSet frsPrize = LFTM_Parameters.getFRSServerGoodnessNegative(serverGoodness,offeredService.get_price(),givenServicePrice);
        FuzzyRuleSet frsCost = LFTM_Parameters.getFRSServerGoodnessPositive(serverGoodness,offeredService.get_cost(),givenServiceCost);
        FuzzyRuleSet frsDelivery = LFTM_Parameters.getFRSServerGoodnessNegative(serverGoodness,offeredService.get_delivery(),givenServiceDelivery);
        FuzzyRuleSet frsQuality = LFTM_Parameters.getFRSServerGoodnessPositive(serverGoodness,offeredService.get_quality(),givenServiceQuality);

        frsPrize.evaluate();
        frsCost.evaluate();
        frsDelivery.evaluate();
        frsQuality.evaluate();

        LFTM_Service givenService = new LFTM_Service(offeredService.id(),givenServicePrice,givenServiceCost,givenServiceDelivery,givenServiceQuality);

        for (int i = 0; i < path.size()-1; i++)
            transmittedDistance += path.get(i).distance(path.get(i+1));

        numRequests++;
        if (numRequests == numRequestsThreshold) {
            numRequests = 0;
            if (dynamic) {
                activeState = false;
                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    public void run() {
                        activeState = true;
                    }
                },100);
                timer.cancel();
                timer = null;
            }
        }

        return givenService;
    }

    @Override
    public void addService(Service service, double goodness) {
        LFTM_Service lftmService = new LFTM_Service(service, LFTM_Parameters.get_linguisticTerms(), "Medium","Medium","Medium","Medium");
        Variable _goodness = new Variable("Server "+id+"goodness",LFTM_Parameters.get_linguisticTerms(),0.0,1.0);
        if (goodness == 0.0)
            goodness = Math.pow(10, -6);
        else if (goodness == 1.0)
            goodness = 1.0 - Math.pow(10, -6);
        _goodness.setValue(goodness);
        _servicesGoodness.put(lftmService,_goodness);
        servicesGoodness.put(lftmService,new Double(goodness));
    }

    @Override
    public void removeService(Service service) {
        servicesGoodness.remove(service);
        _servicesGoodness.remove((LFTM_Service)service);
        if ((requiredService != null) && (requiredService.equals(service)))
            requiredService = null;
    }

    @Override
    public double get_goodness(Service service) throws Exception {
        if (!offersService(service.id()))
            throw new Exception("Server "+id+" doesn't offer service "+service.id());
        for (LFTM_Service lftmService : _servicesGoodness.keySet())
            if (lftmService.equals(service))
                return _servicesGoodness.get(lftmService).getValue();
        throw new Exception("Server "+id+" doesn't offer service "+service.id());
    }

    @Override
    public void set_goodness(Service service, double goodness) throws Exception {
        if (!offersService(service.id()))
            throw new Exception("Server "+id+" doesn't offer service "+service.id());

        servicesGoodness.put(service, new Double(goodness));
        if (goodness == 0.0)
            goodness = Math.pow(10, -6);
        else if (goodness == 1.0)
            goodness = 1.0 - Math.pow(10, -6);

        for (LFTM_Service lftmService : _servicesGoodness.keySet())
            if (lftmService.equals(service))
                _servicesGoodness.get(lftmService).setValue(goodness);
    }

    @Override
    public void set_requiredService(Service requiredService) {
        this.requiredService = new LFTM_Service(requiredService,LFTM_Parameters.get_linguisticTerms(),"Medium","Medium","Medium","Medium");
    }

    @Override
    public void addLink(Sensor node) {
        if (!isNeighbor(node)) {
            if (links == null)
                links = new ArrayList<Link>();

            LFTM_Link link = new LFTM_Link(this,(LFTM_Sensor)node);
            links.add(link);
        }
    }

    /**
     * Returns the number of servers of the network
     * @return Number of servers
     */
    public int get_numServers() { return _numServers; }

    /**
     * Sets the number of servers
     * @param numServers Number of servers
     */
    public static void setNumServers(int numServers){ _numServers = numServers; }

    /**
     * Gets the pheromone trace with a given neighbor
     * @param sensor One of this sensor's neighbors
     * @return The pheromone trace with the given neighbor sensor
     */
    synchronized public double getPheromone(Sensor sensor) {
        if (sensor.isActive())
            for (Link link : links)
                if (link.get_destination().equals(sensor)) {
                    transmittedDistance += this.distance(sensor);
                    try {
                        if (collusion && (get_goodness(requiredService) < 0.5) &&
                                (sensor.get_numServices() > 0)) {
                            if (sensor.get_goodness(requiredService) < 0.5)
                                return LFTM_Link.MAX_PHEROMONE;
                            else
                                return LFTM_Link.MIN_PHEROMONE;
                        } else
                            return ((LFTM_Link)link).get_pheromone();
                    } catch(Exception ex) {}
                }

        return 0.0;
    }

    /**
     * Gets the heuristic value with a given neighbor
     * @param sensor One of this sensor's neighbors
     * @return The heuristic value with the given neighbor sensor
     */
    synchronized public double getHeuristic(Sensor sensor) {
        if (sensor.isActive())
            for (Link link : links)
                if (link.get_destination().equals(sensor)) {
                    transmittedDistance += this.distance(sensor);
                    return ((LFTM_Link)link).get_heuristic();
                }

        return 0.0;
    }

    /**
     * Sets the pheromone trace value of the link connecting to a certain neighbor
     * @param sensor Neighbor whose pheromone trace is to be modified
     * @param value New pheromone value
     */
    synchronized public void setPheromone(Sensor sensor, double value) {
        for (Link link : links)
            if (link.get_destination().equals(sensor)) {
                transmittedDistance += this.distance(sensor);
                ((LFTM_Link)link).set_pheromone(value);
            }
    }

    /**
     * Sets the heuristic trace value of the link connecting to a certain neighbor
     * @param sensor Neighbor whose heuristic trace is to be modified
     * @param value New heuristic value
     */
    synchronized public void setHeuristic(Sensor sensor, double value) {
        for (Link link : links)
            if (link.get_destination().equals(sensor)) {
                transmittedDistance += this.distance(sensor);
                ((LFTM_Link)link).set_heuristic(value);
            }
    }

    /**
     * This method returns this client's goodness
     * @return This client's goodness
     */
    public Variable get_clientGoodness() { return clientGoodness; }
    /**
     * This method returns the service's price weight used when comparing two services
     * @return The service's price weight used when comparing two services
     */
    public double get_priceWeight() { return priceWeight; }
    /**
     * This method returns the service's cost weight used when comparing two services
     * @return The service's cost weight used when comparing two services
     */
    public double get_costWeight() { return costWeight; }
    /**
     * This method returns the service's delivery time weight used when comparing two services
     * @return The service's delivery time weight used when comparing two services
     */
    public double get_deliveryWeight() { return deliveryWeight; }
    /**
     * This method returns the service's quality weight used when comparing two services
     * @return The service's quality weight used when comparing two services
     */
    public double get_qualityWeight() { return qualityWeight; }

    @Override
    public void reset() {
        for (Link link : links) {
            ((LFTM_Link)link).reset();
        }
    }

    @Override
    public String toString() {
        String s = id+" ("+xPosition+","+yPosition+") ->";

        for (Link link : links)
                s += " ("+link.get_destination().id()
                +","+((int)(getPheromone(link.get_destination())*100))/100.0
                +","+((int)(getHeuristic(link.get_destination())*100))/100.0
                +") |";
        return s;
    }
}