/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketbasketanalysis;

import gui.AnalysisProsesUI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.barang;
import model.controller;
import model.transaksi;

/**
 * @author rhapsodixx
 * Market Basket Analyzer
 * 2011
 */
public class Apriori implements Runnable {

    private boolean useexisting;
    private double minsupport;
    private double minconfidence;
    private static double waktueksekusi = 0;
    private static double waktueksekusi2 = 0;
    private String prefix;
    private int rules;
    private int HitungLarge;
    private boolean loop = false;
    private controller c = new controller();
    private List<barang> candidate1 = new ArrayList<barang>();
    private List<transaksi> dataqualified = new ArrayList<transaksi>();
    private List<GroupList<barang>> candidateK = new ArrayList<GroupList<barang>>();
    private List<GroupList<barang>> large = new ArrayList<GroupList<barang>>();
    private List<GroupList<barang>> prevLarge = new ArrayList<GroupList<barang>>();
    private List<Integer> candidatecount = new ArrayList<Integer>();
    private List<Integer> largecount = new ArrayList<Integer>();
    private String from = "";
    private String until = "";
    private log lg;

    public void setLg(log lg) {
        this.lg = lg;
    }

    /**
     * @param args the command line arguments
     */
    public void prosesApriori(String from, String until, Double minsup, Double mincon, String prefix, Boolean useexisting) {
        this.useexisting = useexisting;
        this.from = from;
        this.until = until;
        this.minsupport = minsup;
        this.minconfidence = mincon;
        this.prefix = prefix;
        this.useexisting = useexisting;
        Thread t = new Thread(this);
        t.start();
    }

    public List<transaksi> getDataqualified(String from, String until) {
        List<transaksi> data = new ArrayList<transaksi>();
        if (from.isEmpty() && until.isEmpty()) {
            data = c.getTransaksi();
        } else {
            data = c.getTransaksiByDate(from, until);
        }
        return data;
    }

    public List<barang> getSupportCandidate1() {
        List<barang> data = new ArrayList<barang>();
        data = c.getBarangSupport();
        return data;
    }

    public void getParameter(Double minsup, Double mincon, String prefix) {
        if (minsup.isInfinite() || mincon.isInfinite()) {
            System.out.println("Tidak ada nilai");
        } else {
            minsupport = minsup;
            minconfidence = mincon;
            this.prefix = prefix;
        }
    }

    public void hitungSupportCandidate1(List<barang> candidate1) {
        HitungLarge = 0;
        int i = 1;
        lg.write("\nJumlah Candidate 1 : " + candidate1.size() + "\nLarge:\n");
        for (barang b : candidate1) {
            //hitung support
            double support = (double) b.getSupportCount() / dataqualified.size();
//            System.out.println(b.getSupportCount() +" , "+ dataqualified.size());
            b.setSupport(support);
            //masukkan kedalam large
            if (b.getSupport() >= minsupport) {
                GroupList<barang> temp = new GroupList<barang>();
                temp.add(b);
                temp.setSupport(b.getSupport());
                large.add(temp);
                HitungLarge++;
                lg.write(i + ". " + "[ " + b.getKodebarang() + " ], Support= " + b.getSupport() + "\n");
                i++;
            }
            System.out.println(b.getKodebarang() + ", Jumlah= " + b.getSupportCount() + ", Support= " + b.getSupport());
        }
        lg.write("\n");
        largecount.add(HitungLarge);
    }

    public void hitungSupportCandidate2(List<GroupList<barang>> candidateK) {
        HitungLarge = 0;
        int i = 1;
        lg.write("Jumlah Candidate 2 : " + candidateK.size() + "\nLarge:\n");
        int hitungmundur = candidateK.size();
        for (GroupList<barang> cK : candidateK) {
            hitungmundur--;
            System.out.println("Support2." + hitungmundur);
            //mengambil transaksi yang mengandung candidate itemset k
            List<transaksi> transaksiTemp = c.getTransactionSupport(cK);
            //menghitung support
            double support = (double) transaksiTemp.size() / dataqualified.size();
            if (support >= minsupport) {
                large.add(cK);
                cK.setSupport(support);
                HitungLarge++;
                System.out.println(cK.get(0).getKodebarang() + "," + cK.get(1).getKodebarang() + ", support : " + support);
                lg.write(i + ". " + "[ " + cK.get(0).getKodebarang() + " " + cK.get(1).getKodebarang() + " ]" + ", Support : " + support + "\n");
                i++;
            }
        }
        lg.write("\n");
        largecount.add(HitungLarge);
    }

    public void hitungSupportCandidateK(List<GroupList<barang>> candidateK, Integer k) {
        HitungLarge = 0;
        int i = 1;
        lg.write("Jumlah Candidate " + k + " : " + candidateK.size() + "\nLarge:\n");
        int hitungmundur = candidateK.size();
        for (GroupList<barang> cK : candidateK) {
            hitungmundur--;
            System.out.println("Support" + k + "." + hitungmundur);
            //mengambil transaksi yang mengandung candidate itemset k
            List<transaksi> transaksiTemp = c.getTransactionSupport(cK);
            //menghitung support
            double support = (double) transaksiTemp.size() / (double) dataqualified.size();
            if (support >= minsupport) {
                loop = true;
                large.add(cK);
                cK.setSupport(support);
                HitungLarge++;

                lg.write(i + ". " + "[ ");
                for (barang object : cK) {
                    System.out.print(object.getKodebarang() + ",");
                    lg.write(object.getKodebarang() + " ");
                }
                lg.write("]");

                System.out.println(support);
                lg.write(", Support : " + support + "\n");
                i++;
            }
        }
        lg.write("\n");
        largecount.add(HitungLarge);
    }

    public List<GroupList<barang>> mengambilPrevLarge(Integer jumlahkombinasi, List<GroupList<barang>> large) {
        List<GroupList<barang>> dataPrev = new ArrayList<GroupList<barang>>();
        for (GroupList<barang> l : large) {
            if (l.size() == (jumlahkombinasi - 1)) {
                dataPrev.add(l);
            }
        }
        return dataPrev;
    }

    public void generateCandidate2(List<GroupList<barang>> large) {
        int hitung = 0;
        for (int i = 0; i < large.size(); i++) {
            for (int j = i + 1; j < large.size(); j++) {
                GroupList<barang> temp = new GroupList<barang>();
                temp.add(large.get(i).get(0));
                temp.add(large.get(j).get(0));
                candidateK.add(temp);
                hitung++;
            }
        }
        candidatecount.add(hitung);
    }

    public List<GroupList<barang>> generateCandidateK(List<GroupList<barang>> prevLarge, Integer jumlahkombinasi) {
        List<GroupList<barang>> data = new ArrayList<GroupList<barang>>();
        int x = 0;
        int y = 0;
        x = prevLarge.size();
        y = prevLarge.size();
        for (int i = 0; i < prevLarge.size(); i++) {
            x = x - i;
            for (int j = i + 1; j < prevLarge.size(); j++) {
                y = y - j;
                System.out.println(x + "." + y);
                int jumlahkodebarang = prevLarge.get(0).size();
                GroupList<barang> temp = new GroupList<barang>();

                //mengambil prefix yang sama
                for (int k = 0; k < jumlahkodebarang - 1; k++) {
                    if (prevLarge.get(i).get(k).getKodebarang().equals(prevLarge.get(j).get(k).getKodebarang())) {
                        temp.add(prevLarge.get(i).get(k));
                    }
                }

                //jika ternyata prefix sama, maka tambahkan sisanya
                if (temp.size() == jumlahkodebarang - 1) {
                    System.out.println("checking subset");
                    temp.add(prevLarge.get(i).get(jumlahkodebarang - 1));
                    temp.add(prevLarge.get(j).get(jumlahkodebarang - 1));

                    //Jika Subset Frequent maka tambahkan (prunning)
                    //if (!checkKombinasi(data, temp)) {
                    if (checkSubset(temp, prevLarge) == true) {
                        data.add(temp);
                        System.out.println("passed");
                    }
                    //}
                }
            }
        }
        candidatecount.add(data.size());
        return data;
    }

    public void generateRules(List<GroupList<barang>> large) {
        lg.write("Rules\n");
        int hitung = 1;
        int hitungmundur = large.size();
        for (GroupList<barang> listLarge : large) {
            hitungmundur--;
            System.out.println("Large." + hitungmundur);
            if (listLarge.size() > 1) {

                //mendapatkan barang yang ada pada tiap large
                GroupList<barang> temp = mengambilListLarge(listLarge);

                int k = temp.size();//mendapatkan nilai besar large
                for (int i = 1; i < k; i++) {
                    //mencari subset
                    Subset<barang> cg = new Subset<barang>(temp, i);
                    for (List<barang> combination : cg) {
                        for (GroupList<barang> antecedent : large) {
                            if (combination.size() == antecedent.size()) {

                                //mencari large yang sama dengan subset yang dihasilkan
                                int jumlahbarang = 0;
                                for (barang object : antecedent) {
                                    if (combination.contains(object)) {
                                        jumlahbarang++;
                                    }
                                }

                                if (jumlahbarang == antecedent.size()) {
                                    GroupList<barang> consequent = new GroupList<barang>();
                                    for (barang object : listLarge) {
                                        if (!antecedent.contains(object)) {
                                            consequent.add(object);
                                        }
                                    }

                                    rules++;
                                    double confidence = (double) listLarge.getSupport() / (double) antecedent.getSupport();
                                    //double lift = confidence / ConsequentSupport(consequent);
                                    //double conviction = (1 - ConsequentSupport(consequent)) / (1 - confidence);
//                                    if (conviction == Double.POSITIVE_INFINITY) {
//                                        conviction = 0;
//                                    }

                                    if (confidence >= minconfidence) {
                                        //c.inputRule(combination, consequent, listLarge.getSupport(), confidence, lift, conviction, prefix);
                                        c.inputRule(combination, consequent, listLarge.getSupport(), confidence, prefix);
//                                        System.out.println(antecedent + " -> " + consequent + ", Confidence : " + confidence + " support " + listLarge.getSupport() + ", lift : " + lift + ", conviction : " + conviction + ", prefix: " + prefix);
                                        System.out.println(antecedent + " -> " + consequent + ", Confidence : " + confidence + " support " + listLarge.getSupport() + ", prefix: " + prefix);

                                        lg.write(hitung + ". [ ");
                                        for (barang object : antecedent) {
                                            lg.write(object.getKodebarang() + " ");
                                        }
                                        lg.write("] -> [ ");
                                        for (barang object : consequent) {
                                            lg.write(object.getKodebarang() + " ");
                                        }
                                        //.write("], Confidence : " + confidence + ", Support : " + listLarge.getSupport() + ", lift : " + lift + ", conviction : " + conviction + "\n");
                                        hitung++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }//end rule generation
    }

    public double getWaktuEksekusi(Long waktuawal) {
        double waktueksekusi = System.currentTimeMillis() - waktuawal;
        return waktueksekusi / 1000;
    }

    public GroupList<barang> mengambilListLarge(GroupList<barang> listLarge) {
        GroupList<barang> temp = new GroupList<barang>();
        for (barang object : listLarge) {
            temp.add(object);
        }
        return temp;
    }

    public double factorial(double n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    private boolean checkKombinasi(List<GroupList<barang>> data, GroupList<barang> temp) {
        for (GroupList<barang> l : data) {
            int jumlahbarang = temp.size();
            if (l.size() == temp.size()) {
                for (barang b : l) {
                    if (temp.contains(b)) {
                        jumlahbarang--;
                    }
                }
            }
            if (jumlahbarang == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSubset(GroupList<barang> s, List<GroupList<barang>> l) {
        int jumlahbarang = s.size() - 1;
        for (int i = 0; i < s.size(); i++) {
            GroupList<barang> b = new GroupList<barang>();
            for (int j = 0; j < s.size(); j++) {
                if (j != (jumlahbarang - i)) {
                    b.add(s.get(j));
                }
            }

            boolean ada = false;

            for (GroupList<barang> list : l) {
                if (list.size() == b.size()) {
                    int jumlahbarang2 = b.size();

                    for (barang object : list) {
                        if (b.contains(object)) {
                            jumlahbarang2--;
                        }
                    }

                    if (jumlahbarang2 == 0) {
                        ada = true;
                    }
                }
            }

            if (ada == false) {
                return false;
            }
        }

        return true;
    }

    public double ConsequentSupport(GroupList<barang> cK) {
        List<transaksi> transaksiTemp = c.getTransactionSupport(cK);
        double support = (double) transaksiTemp.size() / (double) dataqualified.size();
        return support;
    }

    public void run() {
        lg.loadingdone(true);
        lg.loading("Selecting Transaction");
        if (useexisting == true) {
            dataqualified = c.getDataQualified();
        } else {
            dataqualified = getDataqualified(from, until);
        }

        //ambil nilai parameter
        getParameter(minsupport, minconfidence, prefix);

        long waktuawal = System.currentTimeMillis();
        //Generate candidate itemset 1
        if (dataqualified.size() != 0) {
            lg.loading("Generate Candidate Itemset 1");
            candidate1 = getSupportCandidate1();
        }
        candidatecount.add(candidate1.size());

        //hitung support candidate 1, large frequent itemset 1
        lg.loading("Support Count Candidate 1");
        hitungSupportCandidate1(candidate1);
        System.out.println(minconfidence + " " + minsupport);

        //Candidate Generation untuk k = 2
        lg.loading("Generate Candidate Itemset 2");
        generateCandidate2(large);

        //Hitung support candidate 2, large frequent itemset 2
        lg.loading("Support Count Candidate 2");
        hitungSupportCandidate2(candidateK);

        //Memulai perulangan Candidate Generation untuk k >= 3, large frequent itemset k >= 3
        int jumlahkombinasi = 3;
        do {
            loop = false;
            //largecandidate menyimpan large sebelumnya
            List<GroupList<barang>> prevLarge = mengambilPrevLarge(jumlahkombinasi, large);
            //Generate Candidate Itemset untuk k>=3
            lg.loading("Generate Candidate Itemset " + jumlahkombinasi);
            candidateK = generateCandidateK(prevLarge, jumlahkombinasi);
            if (candidateK.isEmpty()) {
                break;
            }
            //hitung support Candidate Itemset untuk k>=3, large frequent itemset k >= 3
            lg.loading("Support Count Candidate " + jumlahkombinasi);
            hitungSupportCandidateK(candidateK, jumlahkombinasi);
            jumlahkombinasi++;
        } while (loop);

        //Rule Generation
        lg.loading("Generate Rules");
        waktueksekusi = getWaktuEksekusi(waktuawal);

        long waktuawal2 = System.currentTimeMillis();
        generateRules(large);
        waktueksekusi2 = getWaktuEksekusi(waktuawal2);

        System.out.println("jumlah transaksi : " + dataqualified.size() + ", jumlah barang : " + candidate1.size() + ", rata-rata barang dalam transaksi : " + c.AvgTransactSize());
        lg.write("\nJumlah transaksi : " + dataqualified.size() + ", Jumlah barang : " + candidate1.size() + ", rata-rata barang dalam transaksi : " + c.AvgTransactSize() + ", Jumlah rules : " + rules + "\n");
        for (int i = 0; i < candidatecount.size() - 1; i++) {
            int j = i + 1;
            System.out.println("Jumlah Candidate " + j + ": " + candidatecount.get(i) + ", Jumlah Large " + j + ": " + largecount.get(i));
            lg.write("Jumlah Candidate " + j + ": " + candidatecount.get(i) + ", Jumlah Large " + j + ": " + largecount.get(i) + "\n");
        }
        System.out.println();



        System.out.println("Waktu Eksekusi : " + getWaktuEksekusi(waktuawal) + " Detik");
        lg.write("Waktu Eksekusi Association Analysis : " + waktueksekusi + " Detik\n");
        lg.write("Waktu Eksekusi Rule Analysis : " + waktueksekusi2 + " Detik\n");
        lg.write("Prefix Tabel Keluaran : " + prefix + ". Proses Selesai.");
        lg.loading("Done.");
        lg.loadingdone(false);
        JOptionPane.showMessageDialog(null, "Done!");
    }
}
