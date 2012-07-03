/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketbasketanalysis;

/**
 *
 * @author rhapsodixx
 */
public interface log {

    public void write(String pesan);
    public void loading(String pesan);
    public void loadingdone(Boolean kondisi);

}
