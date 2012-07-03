/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rhapsodixx
 */
public class CleanerController {

    static String database = "marketbasket";
    static String server = "localhost";
    static String user = "root";
    static String pass = "";
    private Connection koneksi;
    private Statement perintah;
    private int maxitem = 999;

    public static void parameter(String server, String user, String pass, String database) {
        controller.server = server;
        controller.user = user;
        controller.pass = pass;
        controller.database = database;
    }

    public CleanerController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, user, pass);
            perintah = koneksi.createStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void truncateTableT() {
        String sql = "TRUNCATE TABLE transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            perintah.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDataQualified() {
        String temp = "SELECT kode_faktur FROM transaksi "
                +"GROUP BY kode_faktur HAVING COUNT(kode_barang) > 1";
        System.out.println(temp);
        List<transaksi> temptransaksi = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(temp);
            while (data.next()) {
                temptransaksi.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (transaksi object : temptransaksi) {
                String sql = "INSERT INTO transaksitemp "
                        +"SELECT * FROM transaksi WHERE kode_faktur = '"+object.getId()+"'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDataQualifiedByDate(String start, String end) {
        String temp = "SELECT t.kode_faktur FROM transaksi as t, faktur as f "
                +"WHERE t.kode_faktur = f.kode_faktur AND f.tanggal_faktur BETWEEN '"+start+"' AND '"+end+"' "
                +"GROUP BY t.kode_faktur HAVING COUNT(t.kode_barang) > 1";
        System.out.println(temp);
        List<transaksi> temptransaksi = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(temp);
            while (data.next()) {
                temptransaksi.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (transaksi object : temptransaksi) {
                String sql = "INSERT INTO transaksitemp "
                        +"SELECT * FROM transaksi WHERE kode_faktur = '"+object.getId()+"'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDataQualifiedFuzzy(Integer maxitem) {
        String temp = "SELECT kode_faktur FROM transaksi "
                +"GROUP BY kode_faktur HAVING COUNT(kode_barang) > 1 AND COUNT(kode_barang) <= "+maxitem;
        System.out.println(temp);
        List<transaksi> temptransaksi = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(temp);
            while (data.next()) {
                temptransaksi.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (transaksi object : temptransaksi) {
                String sql = "INSERT INTO transaksitemp "
                        +"SELECT * FROM transaksi WHERE kode_faktur = '"+object.getId()+"'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDataQualifiedFuzzyByDate(String start, String end, Integer maxitem) {
        String temp = "SELECT t.kode_faktur FROM transaksi as t, faktur as f "
                +"WHERE t.kode_faktur = f.kode_faktur AND f.tanggal_faktur BETWEEN '"+start+"' AND '"+end+"' "
                +"GROUP BY t.kode_faktur HAVING COUNT(t.kode_barang) > 1 AND COUNT(t.kode_barang) <= "+maxitem;
        System.out.println(temp);
        List<transaksi> temptransaksi = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(temp);
            while (data.next()) {
                temptransaksi.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (transaksi object : temptransaksi) {
                String sql = "INSERT INTO transaksitemp "
                        +"SELECT * FROM transaksi WHERE kode_faktur = '"+object.getId()+"'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
