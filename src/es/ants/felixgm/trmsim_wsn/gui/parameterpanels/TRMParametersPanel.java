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

package es.ants.felixgm.trmsim_wsn.gui.parameterpanels;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the generic panel used to retrieve the parameters' values of
  every Trust and Reputation Model</p>
 <font color="#FF0000">
 <p><strong>A subclass of this class, containing the specific parameters fields, has to be 
 implemented in order to add a new Trust and Reputation Model</strong></p>
 </font>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.2
 */
public abstract class TRMParametersPanel extends javax.swing.JPanel {

    /**
     * This method retrieves the corresponding trust and reputation model parameters in a
     * {@link TRMParameters} object
     * @return The corresponding trust and reputation model parameters in a
     * {@link TRMParameters} object
     */
    public abstract TRMParameters get_TRMParameters();
    
    /**
     * This method sets all the parameters fields of the panel with the values contained
     * in the argument
     * @param trmParameters Trust and Reputation Model parameters values used to set the 
     * corresponding fields of the panel
     */
    public abstract void set_TRMParameters(TRMParameters trmParameters);
    
    /**
     * This method is used to enable and disable all the parameters fields and labels of the panel
     * @param enabled If true, enable all the parameters fields and labels of the panel. Otherwise, 
     * disable them
     */
    @Override
    public abstract void setEnabled(boolean enabled);    
}
