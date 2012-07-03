/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author rhapsodixx
 */
@Entity
public class transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private int jumlahitem;

    public transaksi(String id) {
        this.id = id;
    }

    public transaksi() {
    }

    public transaksi(String id, Date tanggal) {
        this.id = id;
        this.tanggal = tanggal;
    }

    public transaksi(String id, Integer jumlahitem) {
        this.id = id;
        this.jumlahitem = jumlahitem;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToMany
    private List<barang> databarang=new ArrayList<barang> ();
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date tanggal;

    /**
     * Get the value of tanggal
     *
     * @return the value of tanggal
     */
    public Date getTanggal() {
        return tanggal;
    }

    /**
     * Set the value of tanggal
     *
     * @param tanggal new value of tanggal
     */
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public List<barang> getDatabarang() {
        return databarang;
    }

    public void setDatabarang(List<barang> databarang) {
        this.databarang = databarang;
    }

    public int getJumlahitem() {
        return jumlahitem;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof transaksi)) {
            return false;
        }
        transaksi other = (transaksi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.transaksi[id=" + id + "]";
    }

}
