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
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import marketbasketanalysis.GroupList;

/**
 *
 * @author rhapsodixx
 */
public class controller {

    static String database = "marketbasket";
    static String server = "localhost";
    static String user = "root";
    static String pass = "";
    static String end;
    static String start;
    private Connection koneksi;
    private Statement perintah;

    public static void parameter(String server, String user, String pass, String database) {
        controller.server = server;
        controller.user = user;
        controller.pass = pass;
        controller.database = database;
    }

    public static String getParamServer() {
        return server;
    }

    public static String getParamUser() {
        return user;
    }

    public static String getParamPass() {
        return pass;
    }

    public static String getParamDb() {
        return database;
    }

    public controller() {
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

    public List<transaksi> getDataQualified() {
        String sql = "SELECT DISTINCT kode_faktur FROM transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransaksi() {
        truncateTableT();
        try {
            String sql = "INSERT INTO transaksitemp "
                    + "SELECT * FROM detail_transaksi";
            perintah.execute(sql);
            System.out.println(sql);
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT DISTINCT kode_faktur FROM transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransaksiByDate(String start, String end) {
        truncateTableT();
        try {
            String sql = "INSERT INTO transaksitemp "
                    + "SELECT t.kode_faktur, t.kode_barang FROM detail_transaksi as t, faktur as f "
                    + "WHERE t.kode_faktur = f.kode_faktur AND f.tanggal_faktur BETWEEN '" + start + "' AND '" + end + "' ";
            perintah.execute(sql);
            System.out.println(sql);

        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT DISTINCT kode_faktur FROM transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransaksiFuzzy(Integer maxitem) {
        truncateTableT();
        String temp = "SELECT kode_faktur FROM detail_transaksi "
                + "GROUP BY kode_faktur HAVING COUNT(kode_barang) <= " + maxitem;
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
                        + "SELECT * FROM detail_transaksi WHERE kode_faktur = '" + object.getId() + "'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT DISTINCT kode_faktur FROM transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransaksiFuzzyByDate(String start, String end, Integer maxitem) {
        truncateTableT();
        String temp = "SELECT t.kode_faktur FROM detail_transaksi as t, faktur as f "
                + "WHERE t.kode_faktur = f.kode_faktur AND f.tanggal_faktur BETWEEN '" + start + "' AND '" + end + "' "
                + "GROUP BY t.kode_faktur HAVING COUNT(t.kode_barang) <= " + maxitem;
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
                        + "SELECT * FROM detail_transaksi WHERE kode_faktur = '" + object.getId() + "'";
                perintah.execute(sql);
                System.out.println(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "SELECT DISTINCT kode_faktur FROM transaksitemp";
        System.out.println(sql);
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public GroupList<barang> getBarangSupport() {
        String sql = "SELECT DISTINCT kode_barang,COUNT(kode_barang) FROM transaksitemp GROUP BY kode_barang";
        System.out.println(sql);
        GroupList<barang> hasil = new GroupList<barang>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new barang(data.getString(1), data.getInt(2)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransactionSupport1(barang databarang) {
        String sql = "SELECT kode_faktur "
                + "FROM transaksitemp "
                + "WHERE kode_barang = '" + databarang.getKodebarang() + "'";
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public List<transaksi> getTransactionSupport(GroupList<barang> databarang) {
        String temp = "";
        for (barang object : databarang) {
            temp += "'" + object.getKodebarang() + "',";
        }
        temp = temp.substring(0, temp.length() - 1);

        String sql = "SELECT kode_faktur "
                + "FROM transaksitemp "
                + "WHERE kode_barang IN (" + temp + ") "
                + "GROUP BY `kode_faktur` "
                + "HAVING count(`kode_faktur`) = " + databarang.size();
        List<transaksi> hasil = new ArrayList<transaksi>();
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil.add(new transaksi(data.getString(1)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public int getJumlahBarang(transaksi t) {
        String sql = "SELECT count(kode_barang) FROM transaksitemp WHERE kode_faktur = '" + t.getId() + "'";
        int hasil = 0;
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil = data.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public DefaultComboBoxModel getPrefix() {
        DefaultComboBoxModel dc = new DefaultComboBoxModel();
        String sql = "SELECT DISTINCT prefix FROM rules";
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                dc.addElement(data.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dc;
    }

    public DefaultTableModel getTabel(String prefix) {
//        String kolom[] = new String[]{"No", "Antecedent", "Consequent", "Support", "Confidence", "Lift", "Conviction"};
        String kolom[] = new String[]{"No", "Antecedent", "Consequent", "Support", "Confidence"};
        DefaultTableModel dt = new DefaultTableModel(kolom, 0);
        String sql = "SELECT * FROM rules WHERE prefix = '" + prefix + "' ORDER BY `rules`.`confidence` DESC";
        try {
            ResultSet data = perintah.executeQuery(sql);
            int i = 1;
            while (data.next()) {
//                String sql2 = "SELECT rules_kode.kode_barang FROM rules_kode WHERE rules_kode.id_rules =" + data.getInt(1);
                String sql2 = "SELECT barang.nama_barang FROM rules_kode, barang WHERE rules_kode.kode_barang = barang.kode_barang AND rules_kode.id_rules =" + data.getInt(1);
                Statement st = koneksi.createStatement();
                ResultSet data2 = st.executeQuery(sql2);

                String antecedent = " ";
                while (data2.next()) {
                    antecedent += data2.getString(1) + ",";
                }
                antecedent = antecedent.substring(0, antecedent.length() - 1);

//                String sql3 = "SELECT rules_kode.kode_barang FROM rules_kode WHERE rules_kode.id_rules =" + data.getInt(2);
                String sql3 = "SELECT barang.nama_barang FROM rules_kode, barang WHERE rules_kode.kode_barang = barang.kode_barang AND rules_kode.id_rules =" + data.getInt(2);
                ResultSet data3 = st.executeQuery(sql3);
                System.out.println(sql3);

                String consequent = " ";
                while (data3.next()) {
                    consequent += data3.getString(1) + ",";
                }
                consequent = consequent.substring(0, consequent.length() - 1);
//                dt.addRow(new Object[]{i, antecedent, consequent, data.getDouble(3) * 100, data.getDouble(4) * 100, data.getDouble(5), data.getDouble(6)});
                dt.addRow(new Object[]{i, antecedent, consequent, data.getDouble(3) * 100, data.getDouble(4) * 100});
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dt;
    }

    public String getTabelDescript(String prefix) {
        String hasil = "";
        String sql = "SELECT * FROM rules WHERE prefix = '" + prefix + "' ORDER BY `rules`.`confidence` DESC";
        try {
            ResultSet data = perintah.executeQuery(sql);
            int i = 1;
            while (data.next()) {
                String sql2 = "SELECT barang.nama_barang FROM rules_kode, barang WHERE rules_kode.kode_barang = barang.kode_barang AND rules_kode.id_rules =" + data.getInt(1);
                Statement st = koneksi.createStatement();
                ResultSet data2 = st.executeQuery(sql2);

                String antecedent = " ";
                while (data2.next()) {
                    antecedent += data2.getString(1) + ",";
                }
                antecedent = antecedent.substring(0, antecedent.length() - 1);

                String sql3 = "SELECT barang.nama_barang FROM rules_kode, barang WHERE rules_kode.kode_barang = barang.kode_barang AND rules_kode.id_rules =" + data.getInt(2);
                ResultSet data3 = st.executeQuery(sql3);
                System.out.println(sql3);

                String consequent = " ";
                while (data3.next()) {
                    consequent += data3.getString(1) + ",";
                }
                consequent = consequent.substring(0, consequent.length() - 1);
                hasil = hasil + i + ". " + antecedent + " -> " + consequent + ", dengan tingkat kepercayaan (Confidence) " + data.getDouble(4) * 100
                        + "%, dan tingkat kemunculan dalam keseluruhan transaksi (Support) " + data.getDouble(3) * 100 + "%\n\n";
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public void inputRule(List<barang> antecedent, GroupList<barang> consequent, double support, double confidence, String prefix) {
        String sql = "";
        int i = 0;
        try {
            String sql1 = "SELECT MAX(id_rules) FROM rules_kode";
            ResultSet data = perintah.executeQuery(sql1);
            while (data.next()) {
                i = data.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            for (barang object : antecedent) {
                //insert antecedent
                String sql2 = "INSERT INTO rules_kode(id_rules, kode_barang) VALUES (" + i + ",'" + object.getKodebarang() + "')";
                perintah.execute(sql2);
                System.out.println(sql2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        int j = i + 1;
        try {
            for (barang object : consequent) {
                String sql3 = "INSERT INTO rules_kode(id_rules, kode_barang) VALUES (" + j + ",'" + object.getKodebarang() + "')";
                perintah.execute(sql3);
                System.out.println(sql3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        //sql = "INSERT INTO  `marketbasket`.`rules` (`antecedent`,`consequent`,`support`,`confidence`,`lift`,`conviction`,`prefix`) VALUES (" + i + "," + j + "," + support + "," + confidence + "," + lift + "," + conviction + ",'" + prefix + "')";
        sql = "INSERT INTO  `marketbasket`.`rules` (`antecedent`,`consequent`,`support`,`confidence`,`prefix`) VALUES (" + i + "," + j + "," + support + "," + confidence + ",'" + prefix + "')";
        try {
            perintah.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double[][] getChart(String prefix) {
        double[][] hasil = null;
        String sql = "SELECT * FROM rules WHERE prefix = '" + prefix + "' ORDER BY `rules`.`confidence` DESC";
        System.out.println(sql);
        try {
            ResultSet data = perintah.executeQuery(sql);
            int i = 0;
            data.last();
            hasil = new double[data.getRow()][4];
            System.out.println(data.getRow());
            data.beforeFirst();
            while (data.next()) {
                hasil[i][0] = data.getDouble(3);
                hasil[i][1] = data.getDouble(4);
//                hasil[i][2] = data.getDouble(5);
//                hasil[i][3] = data.getDouble(6);
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public boolean deleteRule(String prefix) {
        boolean hasil = false;
        String sql = "DELETE FROM rules WHERE prefix = '" + prefix + "'";
        try {
            int data = perintah.executeUpdate(sql);
            hasil = true;
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }

    public double AvgTransactSize() {
        String sql = "SELECT COUNT(kode_barang)/COUNT(DISTINCT kode_faktur) FROM transaksitemp";
        double hasil = 0;
        try {
            ResultSet data = perintah.executeQuery(sql);
            while (data.next()) {
                hasil = data.getDouble(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hasil;
    }
}
