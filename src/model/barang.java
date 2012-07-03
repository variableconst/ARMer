/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 *
 * @author rhapsodixx
 */
@Entity
public class barang implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    private String kodebarang;
    @Transient
    private int SupportCount;
    @Transient
    private double support;
    @Transient
    private double mvalues;

    public barang(String kodebarang) {
        this.kodebarang = kodebarang;
        SupportCount=1;
        support=0;
        mvalues=0;
    }

    public barang(String kodebarang, Integer SupportCount) {
        this.kodebarang = kodebarang;
        this.SupportCount = SupportCount;
        support=0;
        mvalues=0;
    }

    public barang(){
        SupportCount=1;
        support=0;
        mvalues=0;
    }

    public String getKodebarang() {
        return kodebarang;
    }

    public void setKodebarang(String kodebarang) {
        this.kodebarang = kodebarang;
    }

    public int getSupportCount() {
        return SupportCount;
    }

    public void setSupportCount(int SupportCount) {
        this.SupportCount = SupportCount;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getMvalues() {
        return mvalues;
    }

    public void setMvalues(double mvalues) {
        this.mvalues = mvalues;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the kodebarang fields are not set
        if (!(object instanceof barang)) {
            return false;
        }
        barang other = (barang) object;
        if ((this.kodebarang == null && other.kodebarang != null) || (this.kodebarang != null && !this.kodebarang.equals(other.kodebarang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.barang[kodebarang=" + kodebarang + "]";
    }

}
