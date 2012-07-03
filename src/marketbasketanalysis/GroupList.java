/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marketbasketanalysis;

import java.util.ArrayList;

/**
 *
 * @author rhapsodixx
 */
public class GroupList<barang> extends ArrayList<barang> {
    private double support;
    private double mvalues;
    private int supportCount;

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getMvalues() {
        return mvalues;
    }

    public void setMvalues(double mvalue) {
        this.mvalues = mvalue;
    }

    public double getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public GroupList(){
        super();
    }

}
